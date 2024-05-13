/* 
 * Copyright (C) 2022  ST-Lab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.urbcomp.cupid.db.executor
import org.apache.kafka.clients.consumer.{Consumer, ConsumerRecords, KafkaConsumer}
import org.urbcomp.cupid.db.flink.KafkaListenerThread
import org.urbcomp.cupid.db.flink.index.GridIndex

import java.time.Duration
import java.util
import java.util.Properties
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.calcite.sql.{SqlIdentifier}
import org.apache.calcite.sql.ddl.SqlColumnDeclaration
import org.geotools.data.DataStoreFinder
import org.geotools.feature.simple.SimpleFeatureTypeBuilder
import org.urbcomp.cupid.db.executor.utils.ExecutorUtil
import org.urbcomp.cupid.db.infra.{BaseExecutor, MetadataResult}
import org.urbcomp.cupid.db.metadata.{CalciteHelper, MetadataAccessUtil}
import org.urbcomp.cupid.db.metadata.entity.{Field, Index, Table}
import org.urbcomp.cupid.db.parser.ddl.{SqlCupidCreateTable, SqlIndexDeclaration}
import org.urbcomp.cupid.db.schema.IndexType
import org.urbcomp.cupid.db.transformer.{
  RoadSegmentAndGeomesaTransformer,
  TrajectoryAndFeatureTransformer
}
import org.urbcomp.cupid.db.util.{DataTypeUtils, MetadataUtil, SqlParam}
import org.urbcomp.cupid.db.flink.connector.kafkaConnector.{createKafkaTopic, getKafkaTopic}
import org.urbcomp.cupid.db.model.trajectory.Trajectory

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

case class CreateTableExecutor(n: SqlCupidCreateTable) extends BaseExecutor {

  override def execute[Int](): MetadataResult[Int] = {
    val targetTable = n.name
    val (userName, dbName, tableName) = ExecutorUtil.getUserNameDbNameAndTableName(targetTable)

    val db = MetadataAccessUtil.getDatabase(userName, dbName)
    val existedTable = MetadataAccessUtil.getTable(userName, dbName, tableName)
    if (existedTable != null) {
      if (n.ifNotExists) {
        return MetadataResult.buildDDLResult(0)
      } else {
        throw new IllegalArgumentException("table already exist: " + tableName)
      }
    }

    var affectedRows = 0L
    MetadataAccessUtil.withRollback(
      _ => {
        // create metadata table
        val sql = SqlParam.CACHE.get().getSql
        var fromTableList: Array[String] = Array.empty[String]
        if (n.union && sql.contains("from")) {
          fromTableList = sql.split("from")(1).split(";")(0).split(",").map(_.trim)
          val fromTables = fromTableList.mkString(",")
          val table = new Table(0L /* unused */, db.getId, tableName, "union")
          table.setFromTables(fromTables)
          affectedRows = MetadataAccessUtil.insertTable(table)
        } else {
          val storageEngine = if (n.union) "union" else if (n.stream) "kafka" else "hbase"
          affectedRows = MetadataAccessUtil.insertTable(
            new Table(0L /* unused */, db.getId, tableName, storageEngine)
          )
        }

        // set field and index
        val fieldTypeList = new util.ArrayList[String]
        val createdTable = MetadataAccessUtil.getTable(userName, dbName, tableName)
        val tableId = createdTable.getId
        val sfb = new SimpleFeatureTypeBuilder
        val schemaName = MetadataUtil.makeSchemaName(tableId)
        sfb.setName(schemaName)

        val fieldMap = collection.mutable.Map[String, Field]()
        var pointFieldName = ""
        n.columnList.forEach(column => {
          val node = column.asInstanceOf[SqlColumnDeclaration]
          val name = node.name.names.get(0)
          val dataType = node.dataType.getTypeName.names.get(0)
          val classType = DataTypeUtils.getClass(dataType)
          if (DataTypeUtils.isGeometry(dataType)) {
            sfb.add(name, classType, 4326)
          } else {
            sfb.add(name, classType)
          }
          if (DataTypeUtils.isPoint(dataType)) {
            pointFieldName = name
          }
          val field = new Field(0, tableId, name, dataType, 0)
          fieldTypeList.add(dataType)
          MetadataAccessUtil.insertField(field)
          fieldMap.put(name, field)
        })

        val indexes = getIndexes(tableId, fieldMap.toMap, n)
        checkIndexNames(indexes)
        indexes.foreach(index => MetadataAccessUtil.insertIndex(index))

        // union from point and trajectory tables
        // currently assume a point/trajectory table only has one point/trajectory field
        if (fromTableList.nonEmpty) {
          var hasPoint = false
          var pointFieldName = ""
          val trajectoryMap = new scala.collection.mutable.HashMap[String, String]
          fromTableList.foreach(fromTableName => {
            val fields = MetadataAccessUtil.getFields(userName, dbName, fromTableName)
            fields.forEach(field => {
              if (field.getType.equals("Point")) {
                hasPoint = true
                pointFieldName = field.getName
              } else if (field.getType.equals("Trajectory")) {
                trajectoryMap += fromTableName -> field.getName
              }
            })
          })

          // create point table from trajectory table in hbase
          if (hasPoint && trajectoryMap.nonEmpty) {
            val unionFields = new mutable.StringBuilder()
            val unionColumns = new mutable.StringBuilder("(")
            val fieldList = new ListBuffer[String]
            n.columnList.forEach(column => {
              val node = column.asInstanceOf[SqlColumnDeclaration]
              unionFields.append(node.name.names.get(0)).append(",")
              unionColumns
                .append(node.name.names.get(0))
                .append(" ")
                .append(node.dataType.getTypeName.names.get(0))
                .append(",")
              fieldList.append(node.name.names.get(0))
            })
            unionFields.deleteCharAt(unionFields.length - 1)
            unionColumns.deleteCharAt(unionColumns.length - 1).append(")")
            val selectSql = new mutable.StringBuilder("select " + unionFields + " from ")
            val insertSql = new mutable.StringBuilder("insert into ")
            trajectoryMap.foreach(map => {
              val connect = CalciteHelper.createConnection()
              val stmt = connect.createStatement()

              // create point table
              val pointTable = MetadataAccessUtil.getTable(userName, dbName, map._1 + "_point")
              if (pointTable == null) {
                stmt.executeUpdate(
                  "create table if not exists " + map._1 + "_point " + unionColumns
                )
              }

              // query trajectory table
              val result = stmt.executeQuery((selectSql + map._1).replace(pointFieldName, map._2))
              connect.close()

              // get insert sql
              while (result.next()) {
                val trajectory = result.getObject(map._2, classOf[Trajectory])
                insertSql.append(map._1 + "_point values ")
                trajectory.getGPSPointList.forEach(point => {
                  insertSql.append("(")
                  fieldList.foreach(field => {
                    if (field.equals(pointFieldName)) {
                      insertSql.append("st_pointFromWKT(\'" + point + "\'),")
                    } else {
                      insertSql.append(result.getObject(field) + ",")
                    }
                  })
                  insertSql.deleteCharAt(insertSql.length - 1).append("),")
                })
                insertSql.deleteCharAt(insertSql.length - 1)
              }
            })

            // insert data into point table
            if (!insertSql.toString().equals("insert into ")) {
              val connect = CalciteHelper.createConnection()
              val stmt = connect.createStatement()
              stmt.executeUpdate(insertSql.toString())
              connect.close()
            }
          }
        }

        // create stream table(topic) in kafka
        if (n.union || n.stream) {
          createKafkaTopic("localhost:9092", getKafkaTopic(createdTable))
          val minLongitude = 105.0
          val maxLongitude = 110.0
          val minLatitude = 28.108
          val maxLatitude = 32.20

          val kafkaListener = new Thread(() => {
            val props: Properties = new Properties
            props.put("bootstrap.servers", "localhost:9092")
            props.put("group.id", "test-consumer-group")
            props.put("key.deserializer", classOf[StringDeserializer].getName)
            props.put("value.deserializer", classOf[StringDeserializer].getName)
            props.put("auto.offset.reset", "earliest")
            val consumer: Consumer[String, String] = new KafkaConsumer[String, String](props)

            consumer.subscribe(java.util.Collections.singletonList(getKafkaTopic(createdTable)))

            var running = true
            val gridIndex =
              new GridIndex(minLongitude, maxLongitude, minLatitude, maxLatitude, 5000)
            KafkaListenerThread.threadRunningMap.put(tableName, true)
            gridIndex.setFieldTypeList(fieldTypeList)

            while (running) {
              val records: ConsumerRecords[String, String] = consumer.poll(Duration.ofMillis(1000))
              records.forEach(record => {
                gridIndex
                  .addSpatialObject(record.value(), tableName, record.offset())
              })
              running = KafkaListenerThread.threadRunningMap.get(tableName)
            }
            consumer.close()

          })
          kafkaListener.setName(tableName)
          KafkaListenerThread.getInstance().submit(kafkaListener)
        }

        // create table in hbase
        if (n.union || !n.stream) {
          val params = ExecutorUtil.getDataStoreParams(userName, dbName)
          val dataStore = DataStoreFinder.getDataStore(params)
          if (dataStore == null) {
            throw new IllegalArgumentException("Cannot find data store!")
          }
          val schema = dataStore.getSchema(schemaName)
          if (schema != null) {
            throw new IllegalStateException("schema already exist " + schemaName)
          }

          var sft = sfb.buildFeatureType()
          sft = new TrajectoryAndFeatureTransformer().getGeoMesaSFT(sft)
          sft = new RoadSegmentAndGeomesaTransformer().getGeoMesaSFT(sft)

          // allow mixed geometry types for support cupid-db type `Geometry`
          sft.getUserData.put("geomesa.mixed.geometries", java.lang.Boolean.TRUE)

          val geomesaIndexDecl = indexes
            .map(idx => {
              s"${idx.getIndexType}:${idx.getFieldsIdList.split(",").mkString(":")}"
            })
            .mkString(",")
          sft.getUserData.put("geomesa.indices.enabled", geomesaIndexDecl)

          dataStore.createSchema(sft)
        }
      },
      classOf[Exception]
    )
    MetadataResult.buildDDLResult(affectedRows.toInt)
  }

  private def formatName(colName: String, order: Int): String = {
    s"$colName${if (order == 1) "" else s"_$order"}"
  }

  /**
    * Check if index names duplicate
    * For index name not explicitly defined, use ${columnName}_${order} with minimum order satisfy name not duplicate
    */
  private def checkIndexNames(indexes: Array[Index]): Unit = {
    val names = collection.mutable.Set[String]()
    indexes.foreach(idx => {
      if (idx.getName == null) {
        val colName = idx.getFieldsIdList.split(",")(0)
        var order = 1
        while (names.contains(formatName(colName, order))) {
          order += 1
        }
        idx.setName(formatName(colName, order))
      }
      if (names.contains(idx.getName)) {
        throw new IllegalArgumentException(s"Duplicate index name ${idx.getName}")
      }
      names.add(idx.getName)
    })
  }

  private def getIndexType(
      indexType: IndexType,
      fields: Array[String],
      fieldMap: Map[String, Field]
  ): String = {
    if (fields.length < 1) {
      throw new IllegalArgumentException("Invalid index no fields")
    }
    indexType match {
      case IndexType.ATTRIBUTE => "attr"
      case IndexType.SPATIAL =>
        val geoType = fieldMap(fields.head).getType
        fields.length match {
          case 1 =>
            val geoType = fieldMap(fields.head).getType
            if (DataTypeUtils.isPoint(geoType))
              "z2"
            else "xz2"
          case 2 =>
            if (DataTypeUtils.isPoint(geoType))
              "z2t"
            else "xz2t"
          case _ => throw new IllegalArgumentException("index type mismatch columns")
        }
      case _ =>
        throw new IllegalArgumentException("unexpected index type " + indexType.name())
    }
  }

  /**
    * get index declaration or add default index declaration
    */
  private def getIndexes(
      tableId: Long,
      fieldMap: Map[String, Field],
      n: SqlCupidCreateTable
  ): Array[Index] = {
    if (n.indexList != null && n.indexList.size() > 0) {
      n.indexList.asScala
        .map(i => {
          val index = i.asInstanceOf[SqlIndexDeclaration]
          val fields = index.getOperandList.asScala
            .map(op => op.asInstanceOf[SqlIdentifier].names.get(0))
            .toArray
          val indexName: String =
            if (index.indexName != null) index.indexName.names.get(0) else null

          var indexImplType = getIndexType(index.indexType, fields, fieldMap)
          if (index.indexImplType != null) {
            indexImplType = index.indexImplType.names.get(0)
          }

          new Index(tableId, indexImplType, indexName, fields.mkString(","), "")
        })
        .toArray
    } else {
      // add default index if no index explicitly defined
      var rss = Array[Index]()
      val firstGeo = n.columnList.asScala
        .map(c => {
          val column = c.asInstanceOf[SqlColumnDeclaration]
          val dataType = column.dataType.getTypeName.names.get(0)
          if (DataTypeUtils.isGeometry(dataType)) column.name.names.get(0) else null
        })
        .find(name => name != null)
        .orNull

      if (firstGeo != null) {
        rss = rss :+
          new Index(
            tableId,
            getIndexType(IndexType.SPATIAL, Array(firstGeo), fieldMap),
            null,
            firstGeo,
            ""
          )
        val firstDate = n.columnList.asScala
          .map(c => {
            val column = c.asInstanceOf[SqlColumnDeclaration]
            val dataType = column.dataType.getTypeName.names.get(0)
            if (DataTypeUtils.isDate(dataType)) column.name.names.get(0) else null
          })
          .find(name => name != null)
          .orNull
        if (firstDate != null) {
          rss = rss :+
            new Index(
              tableId,
              getIndexType(IndexType.SPATIAL, Array(firstGeo, firstDate), fieldMap),
              null,
              Array(firstGeo, firstDate).mkString(","),
              ""
            )
        }
      }
      rss
    }
  }

}
