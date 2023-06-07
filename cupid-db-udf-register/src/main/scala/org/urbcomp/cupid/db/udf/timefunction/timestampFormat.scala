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

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
class timestampFormat extends AbstractUdf {

  override def name(): String = "timestampFormat"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  /**
    * Formats the timestamp in the specified format
    *
    * @param ts     timestamp
    * @param string time format
    * @return the specified format instance
    */
  def evaluate(ts: Any, string: String): String = {

    if (ts == null || string == null) return null // deal with the null input

    val simpleDateFormat = new SimpleDateFormat(string)
    if (ts.isInstanceOf[Timestamp])
      return simpleDateFormat.format(new Date(ts.asInstanceOf[Timestamp].getTime))
    else if (ts.isInstanceOf[java.lang.Long]) {
      // can not directly pass the ts's long value to new Date( spark will get a wrong answer)
      val v: Long = ts.toString.reverse.toLong
      val l: Long = Stream
        .iterate(v)(_ / 10)
        .takeWhile(_ != 0)
        .map(_ % 10)
        .foldLeft(0L)((acc, digit) => acc * 10 + digit)
      simpleDateFormat.format(new Date(l).getTime)
    } else if (ts.isInstanceOf[String])
      return simpleDateFormat.format(new Date(Timestamp.valueOf(ts.asInstanceOf[String]).getTime))
    else null

  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (Timestamp, String) => String = evaluate

}
