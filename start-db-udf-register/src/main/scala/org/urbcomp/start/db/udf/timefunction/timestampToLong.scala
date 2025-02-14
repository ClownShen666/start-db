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

import java.sql.Timestamp
import java.text.ParseException
import org.urbcomp.start.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.start.db.udf.{AbstractUdf, DataEngine}

class timestampToLong extends ScalarFunction with AbstractUdf {

  override def name(): String = "timestampToLong"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  /**
    * Convert the timestamp to a Long instance
    *
    * @param ts timestamp
    * @return long instance
    */
  def eval(ts: Timestamp): java.lang.Long = {
    Option(ts) match {
      case Some(s) => s.getTime
      case _       => null
    }
  }

  /**
    * Convert the timestamp string to a Long instance
    *
    * @param tsStr timestamp string
    * @return long instance
    */
  @throws[ParseException]
  def eval(tsStr: String): java.lang.Long = {
    Option(tsStr) match {
      case Some(s) => (new toTimestamp).eval(tsStr).getTime
      case _       => null
    }

  }
  def udfSparkEntries: List[String] = List("udfWrapper", "udfWrapper2")

  def udfWrapper: Timestamp => java.lang.Long = eval

  def udfWrapper2: String => java.lang.Long = eval
}
