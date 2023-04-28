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

import org.joda.time.DateTime

import java.time.DateTimeException
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
class hour extends AbstractUdf {

  override def name(): String = "hour"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  /**
    * get hour value of datetime
    *
    * @param dateTime datetime
    * @return hour value
    */
  def evaluate(dateTime: DateTime): Int = dateTime.getHourOfDay

  /**
    * get hour value of datetime
    *
    * @param dtString datetime string
    * @return hour value
    * @throws DateTimeException parse exception
    */
  @throws[DateTimeException]
  def evaluate(dtString: String): Int = {
    val to = new toDatetime
    to.evaluate(dtString).getHour
  }
  def udfSparkEntries: List[String] = List("udfWrapper", "udfWrapper2")

  def udfWrapper: DateTime => Int = evaluate

  def udfWrapper2: String => Int = evaluate

}
