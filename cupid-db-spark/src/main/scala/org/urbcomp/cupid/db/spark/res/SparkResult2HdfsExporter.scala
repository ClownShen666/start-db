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
import org.urbcomp.cupid.db.util.{JacksonUtil, SparkSqlParam}

/**
  * @author jimo
  * */
class SparkResult2HdfsExporter extends ISparkResultExporter {

  override def exportData(param: SparkSqlParam, data: DataFrame): Unit = {
    val sqlId = param.getSqlId
    val hdfsPath = DynamicConfig.getSparkHdfsResultPath

    val typeFields =
      data.schema.fields.map(
        s =>
          new DataTypeField(s.name, s.dataType.simpleString, s.nullable, metadataToMap(s.metadata))
      )
    val fieldJson = JacksonUtil.MAPPER.writeValueAsString(typeFields)
    import data.sparkSession.implicits._
    val schemaDf = List(fieldJson).toDF()
    schemaDf
      .coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .text(hdfsPath + DynamicConfig.getResultSchemaName(sqlId))

    data
      .coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .option("header", value = false)
      .option("sep", DynamicConfig.getHdfsDataSplitter)
      .csv(hdfsPath + DynamicConfig.getResultDataName(sqlId))
  }

  def metadataToMap(md: Metadata): java.util.Map[String, Object] = {
    val json = md.json
    JSON.parseObject(json)
  }

  override def getType: DataExportType = DataExportType.HDFS
}
