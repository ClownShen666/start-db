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
import java.text.ParseException
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
class timestampToLong extends AbstractUdf {

  override def name(): String = "timestampToLong"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  /**
    * Convert the timestamp to a Long instance
    *
    * @param ts timestamp
    * @return long instance
    */
  def evaluate(ts: Timestamp): Long = ts.getTime

  /**
    * Convert the timestamp string to a Long instance
    *
    * @param tsStr timestamp string
    * @return long instance
    */
  @throws[ParseException]
  def evaluate(tsStr: String): Long = {
    val toTimestamp = new toTimestamp
    val timestamp = toTimestamp.evaluate(tsStr)
    timestamp.getTime
  }
  def udfSparkEntries: List[String] = List("udfWrapper", "udfWrapper2")

  def udfWrapper: Timestamp => Long = evaluate

  def udfWrapper2: String => Long = evaluate
}
