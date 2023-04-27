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
import java.time.LocalDateTime
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
class timestampToDatetime extends AbstractUdf {

  override def name(): String = "timestampToDatetime"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  /**
    * Convert datetime to timestamp
    *
    * @param timestamp Timestamp instance
    * @return datetime instance
    */
  def evaluate(timestamp: Timestamp): LocalDateTime = {
    var tsString: String = timestamp.toString
    if (tsString.length != 23) {
      tsString = tsString.substring(0, tsString.length - 2)
    }
    val to = new toDatetime
    to.evaluate(tsString)
  }

  /**
    * Convert timestamp to datetime
    *
    * @param tsString Timestamp String
    * @return datetime datetime instance
    * @throws ParseException parse exception
    */
  @throws[ParseException]
  def evaluate(tsString: String): LocalDateTime = {
    val to = new toTimestamp
    evaluate(to.evaluate(tsString))
  }

  def udfSparkEntries: List[String] = List("udfWrapper", "udfWrapper2")

  def udfWrapper: Timestamp => LocalDateTime = evaluate

  def udfWrapper2: String => LocalDateTime = evaluate

}
