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

import org.urbcomp.cupid.db.data.hbase.{CupidHBaseDataStore, CupidHBaseDataStoreFactory}
import org.urbcomp.cupid.db.executor.utils.ExecutorUtil
import org.urbcomp.cupid.db.infra.{BaseExecutor, MetadataResult}
import org.urbcomp.cupid.db.metadata.MetadataAccessUtil
import org.urbcomp.cupid.db.parser.ddl.SqlDropIndex
import org.urbcomp.cupid.db.schema.IndexType
import org.urbcomp.cupid.db.util.MetadataUtil

import java.io.Serializable
import scala.collection.JavaConverters.asScalaBufferConverter

case class DropIndexExecutor(n: SqlDropIndex) extends BaseExecutor {
  override def execute[Int](): MetadataResult[Int] = {
    val (userName, dbName, tableName) = ExecutorUtil.getUserNameDbNameAndTableName(n.tableName)

    val targetIndexName = n.indexName
    val targetIndexType = n.indexType

    val existIndexes = MetadataAccessUtil.getIndexes(userName, dbName, tableName)
    val indexToDrop = existIndexes.asScala
      .filter(index => {
        val indexName = index.getName
        val indexType = index.getIndexType match {
          case "attr" => IndexType.ATTRIBUTE
          case _      => IndexType.SPATIAL
        }

        indexName.equals(targetIndexName) && indexType.equals(targetIndexType)
      })

    // Delete metadata
    val affectedRows =
      MetadataAccessUtil.dropIndex(indexToDrop.map(index => index.getId).mkString.toLong)

    // Delete business data in Hbase
    val params = ExecutorUtil.getDataStoreParams(userName, dbName)
    val store = new CupidHBaseDataStoreFactory()
      .createDataStore(params.asInstanceOf[java.util.Map[String, Serializable]])
      .asInstanceOf[CupidHBaseDataStore]

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
