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
package org.urbcomp.start.db

import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.urbcomp.start.db.model.data.DataExportType
import org.urbcomp.start.db.spark.SparkQueryExecutor
import org.urbcomp.start.db.util.SparkSqlParam

object SparkExecuteWrapper {
  private val sparkExecute: SparkExecuteWrapper = new SparkExecuteWrapper(new SparkSqlParam())
  def getSparkExecute: SparkExecuteWrapper = {
    sparkExecute
  }
}

class SparkExecuteWrapper private (param: SparkSqlParam) {
  param.setUserName("root")
  param.setDbName("default")
  param.setExportType(DataExportType.PRINT)
  param.setLocal(true)
  var sparkSession: SparkSession = null
  def executeSql(sql: String): Dataset[Row] = {
    if (sparkSession == null) {
      sparkSession = SparkQueryExecutor.getSparkSession(param.isLocal)
    }
    param.setSql(sql)
    SparkQueryExecutor.execute(param, sparkSession)
  }

  def renewSparkSession(): Unit = {
    if (sparkSession != null) {
      sparkSession.close()
    }
    sparkSession = null
  }

  /**
    *Description: Single case, close the current sparkSession after execution
   **/
  def stop(): Unit = {
    if (sparkSession != null) {
      sparkSession.stop()
      sparkSession = null
    }
  }
}
