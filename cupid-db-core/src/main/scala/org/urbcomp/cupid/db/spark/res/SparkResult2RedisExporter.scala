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

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.sql.types.Metadata
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.urbcomp.cupid.db.config.DynamicConfig
import org.urbcomp.cupid.db.datatype.DataTypeField
import org.urbcomp.cupid.db.model.data.DataExportType
import org.urbcomp.cupid.db.spark.SparkQueryExecutor.log
import org.urbcomp.cupid.db.util.{JacksonUtil, SparkSqlParam}

/**
  * @author Hang Wu
  * */
class SparkResult2RedisExporter extends ISparkResultExporter {

  override def getType: DataExportType = DataExportType.REDIS

  override def exportData(param: SparkSqlParam, data: DataFrame): Unit = {
    val sqlId = param.getSqlId

    log.info("Redis schema table: " + DynamicConfig.getResultSchemaName(sqlId))
    val schemaJson = data.schema.json
    import data.sparkSession.implicits._
    val schemaDf = List(schemaJson).toDF()
    schemaDf
      .coalesce(1)
      .write
      .format("org.apache.spark.sql.redis")
      .option("table", DynamicConfig.getResultSchemaName(sqlId))
      .mode(SaveMode.Overwrite)
      .save()
    log.info("Redis dataframe table: " + DynamicConfig.getResultDataName(sqlId))
    data
      .coalesce(1)
      .write
      .format("org.apache.spark.sql.redis")
      .option("table", DynamicConfig.getResultDataName(sqlId))
      .mode(SaveMode.Overwrite)
      .option("model", "binary")
      .save()
  }
}
