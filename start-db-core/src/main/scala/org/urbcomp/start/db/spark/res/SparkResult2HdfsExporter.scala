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
package org.urbcomp.start.db.spark.res

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.sql.types.Metadata
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.urbcomp.start.db.config.DynamicConfig
import org.urbcomp.start.db.datatype.DataTypeField
import org.urbcomp.start.db.model.data.DataExportType
import org.urbcomp.start.db.spark.SparkQueryExecutor.log
import org.urbcomp.start.db.util.{JacksonUtil, SparkSqlParam}

/**
  * @author jimo
  * */
class SparkResult2HdfsExporter extends ISparkResultExporter {

  override def getType: DataExportType = DataExportType.HDFS

  override def exportData(param: SparkSqlParam, data: DataFrame): Unit = {
    val sqlId = param.getSqlId
    val hdfsPath = DynamicConfig.getSparkHdfsResultPath
    log.info("HDFS schema path: " + hdfsPath + DynamicConfig.getResultSchemaName(sqlId))
    val schemaJson = data.schema.json
    import data.sparkSession.implicits._
    val schemaDf = List(schemaJson).toDF()
    schemaDf
      .coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .text(hdfsPath + DynamicConfig.getResultSchemaName(sqlId))
    log.info("HDFS dataframe path: " + hdfsPath + DynamicConfig.getResultDataName(sqlId))
    // We use default parquet format instead of csv because csv does not support Geomesa types and UDTs
    // FIXME: Reading dataframe without using geomesa format due to compatibility
    data
      .coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .option("header", value = false)
      .option("sep", DynamicConfig.getHdfsDataSplitter)
      .save(hdfsPath + DynamicConfig.getResultDataName(sqlId))
  }
}
