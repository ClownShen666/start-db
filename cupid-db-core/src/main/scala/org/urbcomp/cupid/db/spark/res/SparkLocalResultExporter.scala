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

import org.apache.spark.sql.DataFrame
import com.alibaba.fastjson.JSON
import org.apache.spark.sql.types.Metadata
import org.urbcomp.cupid.db.datatype.DataTypeField
import org.urbcomp.cupid.db.model.data.DataExportType
import org.urbcomp.cupid.db.spark.cache.ResultCacheFactory
import org.urbcomp.cupid.db.util.SparkSqlParam
import scala.collection.JavaConverters._

/**
  * @author jimo
  * */
class SparkLocalResultExporter extends ISparkResultExporter {
  override def getType: DataExportType = DataExportType.LOCAL

  override def exportData(param: SparkSqlParam, data: DataFrame): Unit = {
    val sqlId = param.getSqlId
    val schema = data.schema
    ResultCacheFactory.getGlobalInstance
      .addSchema(
        sqlId,
        schema.fields
          .map(
            s =>
              new DataTypeField(
                s.name,
                s.dataType.simpleString,
                s.nullable,
                metadataToMap(s.metadata)
              )
          )
          .toList
          .asJava
      )
    val numFields = schema.fields.length
    data
      .coalesce(1)
      .foreach(row => {
        var rowArr: Array[AnyRef] = Array()
        for (i <- 0 until numFields) {
          val value = row.get(i)
          rowArr +:= value.asInstanceOf[AnyRef]
        }
        ResultCacheFactory.getGlobalInstance.addRow(sqlId, rowArr)
      })
  }

  def metadataToMap(md: Metadata): java.util.Map[String, Object] = {
    val json = md.json
    JSON.parseObject(json)
  }
}
