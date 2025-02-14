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

import org.apache.calcite.sql.ddl.SqlDropTable
import org.urbcomp.start.db.executor.utils.ExecutorUtil
import org.urbcomp.start.db.infra.{BaseExecutor, MetadataResult}
import org.urbcomp.start.db.metadata.MetadataAccessUtil
import org.urbcomp.start.db.flink.connector.kafkaConnector.{deleteKafkaTopic, getKafkaTopic}
import org.urbcomp.start.db.flink.kafka.KafkaListenerThread
import org.urbcomp.start.db.util.{FlinkSqlParam, MetadataUtil, SqlParam}

case class DropTableExecutor(n: SqlDropTable) extends BaseExecutor {
  override def execute[Int](): MetadataResult[Int] = {
    val targetTable = n.name
    val (userName, dbName, tableName) = ExecutorUtil.getUserNameDbNameAndTableName(targetTable)

    val existedTable = MetadataAccessUtil.getTable(userName, dbName, tableName)

    if (existedTable == null) {
      if (n.ifExists) {
        return MetadataResult.buildDDLResult(0)
      } else {
        throw new IllegalArgumentException("table does not exist " + tableName)
      }
    }

    // drop stream table(topic) in kafka
    val storageEngine = existedTable.getStorageEngine
    if (storageEngine.equals("union") || storageEngine.equals("kafka")) {
      KafkaListenerThread.endKafkaListening(
        MetadataUtil.combineUserDbTableKey(userName, dbName, tableName)
      )
      deleteKafkaTopic(FlinkSqlParam.BOOST_STRAP_SERVERS, getKafkaTopic(existedTable))
    }

    val affectedRows = MetadataAccessUtil.dropTable(userName, dbName, tableName)
    MetadataAccessUtil.dropTable(userName, dbName, tableName + "_point")

    // TODO transform start db type
    MetadataResult.buildDDLResult(affectedRows.toInt)
  }
}
