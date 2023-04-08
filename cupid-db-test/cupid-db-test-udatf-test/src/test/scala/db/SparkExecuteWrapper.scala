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
package db

import org.apache.spark.sql.{Dataset, Row}
import org.urbcomp.cupid.db.model.data.DataExportType
import org.urbcomp.cupid.db.spark.SparkQueryExecutor
import org.urbcomp.cupid.db.util.SparkSqlParam

object SparkExecuteWrapper {
  private var sparkExecute: SparkExecuteWrapper = _
  def getSparkExecute: SparkExecuteWrapper = {
    if (sparkExecute == null) {
      sparkExecute = new SparkExecuteWrapper(new SparkSqlParam())
    }
    sparkExecute
  }
}

class SparkExecuteWrapper private (param: SparkSqlParam) {
  param.setUserName("root")
  param.setDbName("default")
  param.setEnableHiveSupport(true)
  param.setExportType(DataExportType.PRINT)
  param.setLocal(true)

  def executeSql(sql: String): Unit = {
    param.setSql(sql)
    SparkQueryExecutor.execute(param, null)

  }

}
