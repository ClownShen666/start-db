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
package org.urbcomp.start.db.udf.timefunction

import org.urbcomp.start.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.start.db.udf.{AbstractUdf, DataEngine}

import java.sql.Timestamp
import java.text.ParseException

class datetimeToTimestamp extends ScalarFunction with AbstractUdf {

  override def name(): String = "datetimeToTimestamp"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  /**
    * Converts a Datetime string to timestamp(with TimeZone)
    *
    * @param dtString datetime String
    * @return timestamp timestamp instance
    */
  @throws[ParseException]
  def eval(dtString: String): Timestamp = {
    val toTimestamp = new toTimestamp
    toTimestamp.eval(dtString)
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: String => Timestamp = eval

}
