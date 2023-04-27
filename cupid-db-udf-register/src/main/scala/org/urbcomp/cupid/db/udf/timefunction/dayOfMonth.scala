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
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import java.time.{DateTimeException, LocalDateTime}

class dayOfMonth extends AbstractUdf {

  override def name(): String = "dayOfMonth"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  /**
    * get day of month
    *
    * @param localDateTime datetime
    * @return day of month
    */
  def evaluate(localDateTime: LocalDateTime): Int = localDateTime.getDayOfMonth

  /**
    * get day of month
    *
    * @param dtString datetime string
    * @return day of month
    * @throws DateTimeException parse exception
    */
  @throws[DateTimeException]
  def evaluate(dtString: String): Int = {
    val to = new toDatetime
    to.evaluate(dtString).getDayOfMonth
  }
  def udfSparkEntries: List[String] = List("udfWrapper", "udfWrapper2")

  def udfWrapper: LocalDateTime => Int = evaluate

  def udfWrapper2: String => Int = evaluate

}
