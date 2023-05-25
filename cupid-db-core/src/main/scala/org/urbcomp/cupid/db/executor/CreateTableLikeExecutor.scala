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

import org.geotools.data.DataStoreFinder
import org.geotools.feature.simple.SimpleFeatureTypeBuilder
import org.urbcomp.cupid.db.executor.utils.ExecutorUtil
import org.urbcomp.cupid.db.infra.{BaseExecutor, MetadataResult}
import org.urbcomp.cupid.db.metadata.MetadataAccessUtil
import org.urbcomp.cupid.db.metadata.entity.{Field, Index, Table}
import org.urbcomp.cupid.db.parser.ddl.SqlCupidCreateTableLike
import org.urbcomp.cupid.db.transformer.{
  RoadSegmentAndGeomesaTransformer,
  TrajectoryAndFeatureTransformer
}
import org.urbcomp.cupid.db.util.{DataTypeUtils, MetadataUtil}

import scala.collection.JavaConverters._

case class CreateTableLikeExecutor(n: SqlCupidCreateTableLike) extends BaseExecutor {

  override def execute[Int](): MetadataResult[Int] = {

    val targetTable = n.targetTableName
    val (userName, dbName, tableName) = ExecutorUtil.getUserNameDbNameAndTableName(targetTable)
    val db = MetadataAccessUtil.getDatabase(userName, dbName)
    val existedTargetTable = MetadataAccessUtil.getTable(db.getId, tableName)
    if (existedTargetTable != null) {
      if (n.ifNotExists) {
        return MetadataResult.buildDDLResult(0)
      } else {
        throw new IllegalArgumentException("table already exist " + tableName)
      }
    }

    val sourceTable = n.sourceTableName
    val (sourceUserName, sourceDbName, sourceTableName) =
      ExecutorUtil.getUserNameDbNameAndTableName(sourceTable)
    val sourceDb = MetadataAccessUtil.getDatabase(sourceUserName, sourceDbName)
    val existedSourceTable = MetadataAccessUtil.getTable(sourceDb.getId, sourceTableName)
    if (existedSourceTable == null)
      throw new IllegalArgumentException("sourceTable not exist " + tableName)

    var affectedRows = 0L
    MetadataAccessUtil.withRollback(
      _ => {
        affectedRows =
          MetadataAccessUtil.insertTable(new Table(0L /* unused */, db.getId, tableName, "hbase"))
        val createdTable = MetadataAccessUtil.getTable(db.getId, tableName)
        val tableId = createdTable.getId

        val sfb = new SimpleFeatureTypeBuilder
        val schemaName = MetadataUtil.makeSchemaName(tableId)
        sfb.setName(schemaName)

        val params = ExecutorUtil.getDataStoreParams(userName, dbName)
        val dataStore = DataStoreFinder.getDataStore(params)
        val schema = dataStore.getSchema(schemaName)
        if (schema != null) {
          throw new IllegalStateException("schema already exist " + schemaName)
        }

        //copy col
        val fieldMap = collection.mutable.Map[String, Field]()
        MetadataAccessUtil
          .getFields(sourceUserName, sourceDbName, sourceTableName)
          .forEach(field => {
            val sourceDataType = field.getType
            val sourceClassType = DataTypeUtils.getClass(sourceDataType)
            if (DataTypeUtils.isGeometry(sourceDataType)) {
              sfb.add(field.getName, sourceClassType, 4326)
            } else {
              sfb.add(field.getName, sourceClassType)
            }

            val sourceField =
              new Field(0, tableId, field.getName, field.getType, field.getIsPrimary)
            MetadataAccessUtil.insertField(sourceField)
            fieldMap.put(field.getName, field)
          })

        //copy index
        val indexes = MetadataAccessUtil
          .getIndexes(sourceUserName, sourceDbName, sourceTableName)
          .asScala
          .toList
          .toArray

        if (indexes != null) {
          checkIndexNames(indexes)
          indexes.foreach(index => {
            val tarIndex = new Index(
              tableId,
              index.getIndexType,
              index.getName,
              index.getFieldsIdList,
              index.getIndexProperties
            )
            MetadataAccessUtil.insertIndex(tarIndex)
          })

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

}
