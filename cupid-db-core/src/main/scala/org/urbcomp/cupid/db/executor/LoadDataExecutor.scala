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

import org.geotools.data.{DataStoreFinder, Transaction}
import org.locationtech.geomesa.utils.io.WithClose
import org.urbcomp.cupid.db.executor.utils.ExecutorUtil
import org.urbcomp.cupid.db.infra.{BaseExecutor, MetadataResult}
import org.urbcomp.cupid.db.metadata.{CalciteHelper, MetadataAccessUtil}
import org.urbcomp.cupid.db.parser.dcl.{SqlColumnMappingDeclaration, SqlLoadData}
import org.urbcomp.cupid.db.util.MetadataUtil

import java.io.File
import java.sql.ResultSet
import java.util
import java.util.Scanner
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
      MetadataAccessUtil.getFields(userName, dbName, tableName).asScala.map(_.getName).toArray
    val mappingCorrectness = n.mappings.getList.asScala
      .map(sqlNode => {
        val field = sqlNode.asInstanceOf[SqlColumnMappingDeclaration].field.names.get(0)
        tableFields.contains(field)
      })
      .toArray
      .asInstanceOf[Array[Boolean]]
    if (mappingCorrectness.contains(false)) {
      throw new IllegalArgumentException("Some fields do not exist.")
    }

    // Ready for reading
    val reader = new Scanner(new File(n.path))

    // Ignore header
    if (n.hasHeader) {
      reader.nextLine()
    }

    // Read all lines
    val allLines = new util.ArrayList[String]
    while (reader.hasNext()) {
      allLines.add(reader.nextLine())
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

    // map: line -> select sql -> Array[Array[(String, AnyRef)]]
    val resultObjs = allLines.asScala
      .map(line => {
        val valueGroup = line.split(n.delimiter)
        mapFieldExpr.map(FieldExpr => {
          // replace Expr with value
          val matcher = Pattern.compile("-c[1-9]+").matcher(FieldExpr._2)
          var rExpr = FieldExpr._2
          while (matcher.find()) {
            val colIndex = matcher.group().split("c")(1).toInt
            rExpr = rExpr.replaceFirst("-c[1-9]+", valueGroup(colIndex - 1))
          }
          // select sql
          val resultObj = WithClose(executeQuery(s"select $rExpr")) { rs =>
            {
              rs.next()
              rs.getObject(1)
            }
          }
          (FieldExpr._1, resultObj)
        })
      })
      .toArray
      .asInstanceOf[Array[Array[(String, AnyRef)]]]

    // Write into geomesa
    var affectedRows = 0
    WithClose(dataStore.getFeatureWriterAppend(schemaName, Transaction.AUTO_COMMIT)) { writer =>
      resultObjs.foreach(fieldValueArray => {
        val sf = writer.next()
        fieldValueArray.foreach(fieldValue => sf.setAttribute(fieldValue._1, fieldValue._2))
        affectedRows += 1
        writer.write()
      })
    }

    dataStore.dispose()
    MetadataResult.buildDDLResult(affectedRows)
  }

  def executeQuery[R](querySql: String): ResultSet = {
    val connection = CalciteHelper.createConnection()
    val statement = connection.createStatement()
    statement.executeQuery(querySql)
  }
}
