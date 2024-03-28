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
package org.urbcomp.cupid.db.udf.timefunction

import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}

import java.time.format.DateTimeFormatter
import java.time.DateTimeException

class datetimeFormat extends ScalarFunction with AbstractUdf {

  override def name(): String = "datetimeFormat"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  /**
    * Formats one datetime string into the specified format
    *
    * @param dtStr  datetime String
    * @param format format string
    * @return datetime string
    */
  @throws[DateTimeException]
  def eval(dtStr: String, format: String): String = {
    val to = new toDatetime
    val DateTime = to.eval(dtStr)
    val dateTimeFormatter = DateTimeFormatter.ofPattern(format.trim)
    dateTimeFormatter.format(DateTime)
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (String, String) => String = eval

}
