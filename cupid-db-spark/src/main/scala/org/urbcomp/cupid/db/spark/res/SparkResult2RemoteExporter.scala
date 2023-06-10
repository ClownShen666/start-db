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
package org.urbcomp.cupid.db.spark.res

import org.apache.spark.sql.{DataFrame, SaveMode}
import org.urbcomp.cupid.db.model.data.DataExportType
import org.urbcomp.cupid.db.spark.ds.remote.RemoteWriteSource
import org.urbcomp.cupid.db.util.SparkSqlParam

/**
  * @author jimo
  * */
class SparkResult2RemoteExporter extends ISparkResultExporter {
  override def exportData(param: SparkSqlParam, data: DataFrame): Unit = {
    data
      .coalesce(1)
      .write
      .format(RemoteWriteSource.getClass.getCanonicalName.split("\\$").head)
      .mode(SaveMode.Overwrite)
      .option(RemoteWriteSource.SCHEMA_KEY, data.schema.json)
      .option(SparkSqlParam.SQL_ID_KEY, param.getSqlId)
      .option(SparkSqlParam.REMOTE_HOST_KEY, param.getRemoteHost)
      .option(SparkSqlParam.REMOTE_PORT_KEY, param.getRemotePort)
      .save()
  }

  override def getType: DataExportType = DataExportType.CACHE
}
