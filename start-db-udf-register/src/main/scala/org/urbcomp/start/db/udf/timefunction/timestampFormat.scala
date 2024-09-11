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
import java.text.SimpleDateFormat
import java.util.Date

class timestampFormat extends ScalarFunction with AbstractUdf {

  override def name(): String = "timestampFormat"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  /**
    * Formats the timestamp in the specified format
    *
    * @param ts     timestamp
    * @param string time format
    * @return the specified format instance
    */
  def eval(ts: String, string: String): String = {
    if (ts == null || string == null) return null // deal with the null input
    val simpleDateFormat = new SimpleDateFormat(string)
    simpleDateFormat.format(new Date(Timestamp.valueOf(ts).getTime))

  }

  def eval(ts: Timestamp, string: String): String = {
    if (ts == null || string == null) return null // deal with the null input

    val simpleDateFormat = new SimpleDateFormat(string)
    simpleDateFormat.format(new Date(ts.getTime))
  }

  def eval(ts: Long, string: String): String = {
    if (ts == null || string == null) return null // deal with the null input

    val simpleDateFormat = new SimpleDateFormat(string)
    var v = ts.toString.reverse.toLong
    var dl = 0L
    while (v != 0) {
      dl = dl * 10 + (v % 10)
      v = v / 10
    }
    simpleDateFormat.format(new Date(dl).getTime)
  }

  def udfSparkEntries: List[String] = List("udfWrapper", "udfWrapper2", "udfWrapper3")

  def udfWrapper: (String, String) => String = eval
  def udfWrapper2: (Timestamp, String) => String = eval
  def udfWrapper3: (Long, String) => String = eval
}
