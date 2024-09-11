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

import org.urbcomp.start.db.data.hbase.{StartHBaseDataStore, StartHBaseDataStoreFactory}
import org.urbcomp.start.db.executor.utils.ExecutorUtil
import org.urbcomp.start.db.infra.{BaseExecutor, MetadataResult}
import org.urbcomp.start.db.metadata.MetadataAccessUtil
import org.urbcomp.start.db.parser.ddl.SqlDropIndex
import org.urbcomp.start.db.schema.IndexType
import org.urbcomp.start.db.util.MetadataUtil

import java.io.Serializable
import scala.collection.JavaConverters.asScalaBufferConverter

case class DropIndexExecutor(n: SqlDropIndex) extends BaseExecutor {
  override def execute[Int](): MetadataResult[Int] = {
    val (userName, dbName, tableName) = ExecutorUtil.getUserNameDbNameAndTableName(n.tableName)

    val targetIndexName = n.indexName
    val existIndexes = MetadataAccessUtil.getIndexes(userName, dbName, tableName)
    val indexToDrop = existIndexes.asScala.filter(index => index.getName.equals(targetIndexName))

    // Delete metadata
    val affectedRows =
      MetadataAccessUtil.dropIndex(indexToDrop.map(index => index.getId).mkString.toLong)

    // Delete business data in Hbase
    val params = ExecutorUtil.getDataStoreParams(userName, dbName)
    val store = new StartHBaseDataStoreFactory()
      .createDataStore(params.asInstanceOf[java.util.Map[String, Serializable]])
      .asInstanceOf[StartHBaseDataStore]

    val tableId = indexToDrop.map(index => index.getTableId).mkString.toLong
    val schemaName = MetadataUtil.makeSchemaName(tableId)
    val indexString = indexToDrop
      .map(index => {
        s"${index.getIndexType}:${index.getFieldsIdList.split(",").mkString(":")}"
      })
      .mkString
    store.dropIndices(schemaName, indexString)

    MetadataResult.buildDDLResult(affectedRows.toInt)

  }
}
