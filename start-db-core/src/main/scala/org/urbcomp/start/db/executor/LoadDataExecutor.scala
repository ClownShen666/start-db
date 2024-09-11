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
package org.urbcomp.start.db.executor

import org.geotools.data.{DataStoreFinder, Transaction}
import org.locationtech.geomesa.utils.io.WithClose
import org.urbcomp.start.db.executor.utils.ExecutorUtil
import org.urbcomp.start.db.infra.{BaseExecutor, MetadataResult}
import org.urbcomp.start.db.metadata.{CalciteHelper, MetadataAccessUtil}
import org.urbcomp.start.db.model.roadnetwork.RoadSegment
import org.urbcomp.start.db.model.trajectory.Trajectory
import org.urbcomp.start.db.parser.dcl.{SqlColumnMappingDeclaration, SqlLoadData}
import org.urbcomp.start.db.util.MetadataUtil

import java.io.{BufferedReader, FileInputStream, InputStreamReader}
import java.sql.{Connection, ResultSet}
import java.util.regex.Pattern
import scala.collection.JavaConverters._

case class LoadDataExecutor(n: SqlLoadData) extends BaseExecutor {
  override def execute[Int](): MetadataResult[Int] = {
    // Get store config
    val (userName, dbName, tableName) = ExecutorUtil.getUserNameDbNameAndTableName(n.tableName)
    val table = MetadataAccessUtil.getTable(userName, dbName, tableName)
    if (table == null) {
      throw new IllegalArgumentException(s"Table $tableName does not exist.")
    }
    val params = ExecutorUtil.getDataStoreParams(userName, dbName)
    val dataStore = DataStoreFinder.getDataStore(params)
    val schemaName = MetadataUtil.makeSchemaName(table.getId)

    // Check mapping
    val tableFields =
      MetadataAccessUtil
        .getFields(userName, dbName, tableName)
        .asScala
        .map(field => (field.getName, field.getType))
        .toMap
    val mappingWrongCol = n.mappings.getList.asScala
      .map(sqlNode => {
        val field = sqlNode.asInstanceOf[SqlColumnMappingDeclaration].field.names.get(0)
        (field, tableFields.keySet.contains(field))
      })
      .filter(fieldExist => !fieldExist._2)
      .toMap
    if (mappingWrongCol.nonEmpty) {
      throw new IllegalArgumentException(
        s"Fields ${mappingWrongCol.keySet.mkString(",")} do not exist."
      )
    }

    // convert mappings to Array[(Field, Expr)]
    val mapFieldExpr = n.mappings.getList.asScala
      .map(sqlNode => {
        val field = sqlNode.asInstanceOf[SqlColumnMappingDeclaration].field.names.get(0)
        val expr = sqlNode.asInstanceOf[SqlColumnMappingDeclaration].expr.toString
        (field, expr)
      })
      .toArray
      .asInstanceOf[Array[(String, String)]]

    val writer = dataStore.getFeatureWriterAppend(schemaName, Transaction.AUTO_COMMIT)
    val bufferSize = 1000
    var affectedRows = 0
    val connection = CalciteHelper.createConnection()
    // Parse and write
    WithClose(new BufferedReader(new InputStreamReader(new FileInputStream(n.path)), bufferSize)) {
      reader =>
        {
          // Ignore header
          if (n.hasHeader) {
            reader.readLine()
          }

          reader
            .lines()
            .iterator()
            .asScala
            .map(line => {
              val valueGroup = line.split(n.delimiter)
              mapFieldExpr.map(FieldExpr => {
                // replace Expr with value
                val matcher = Pattern.compile("_c\\d+").matcher(FieldExpr._2)
                var rExpr = FieldExpr._2
                while (matcher.find()) {
                  val colIndex = matcher.group().split("c")(1).toInt
                  rExpr = rExpr.replaceFirst("_c\\d+", valueGroup(colIndex - 1))
                }
                // For special situation
                if (tableFields.getOrElse(FieldExpr._1, "NULL").equals("String")) {
                  rExpr = "\"" + rExpr + "\""
                }
                val resultObj = WithClose(executeQuery(connection, rExpr.replace("`", ""))) { rs =>
                  {
                    rs.next()
                    rs.getObject(1)
                  }
                }
                (FieldExpr._1, resultObj)
              })
            })
            .foreach(fieldValueArray => {
              // Write into geomesa-hbase
              val sf = writer.next()

              fieldValueArray.foreach(fieldValue => {
                fieldValue._2 match {
                  case rs: RoadSegment =>
                    ExecutorUtil.writeRoadSegment(fieldValue._1, sf, rs)
                  case traj: Trajectory =>
                    ExecutorUtil.writeTrajectory(fieldValue._1, sf, traj)
                  case _ =>
                    sf.setAttribute(fieldValue._1, fieldValue._2)
                }
              })
              affectedRows += 1
              writer.write()
            })
        }
    }
    writer.close()
    connection.close()

    dataStore.dispose()
    MetadataResult.buildDDLResult(affectedRows)
  }

  private def executeQuery(connection: Connection, queryObj: String): ResultSet = {
    val statement = connection.createStatement()
    statement.executeQuery("select %s".format(queryObj))
  }
}
