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
import org.urbcomp.cupid.db.udf.timefunction.DefaultConstant.DEFAULT_FORMATS
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}

import java.sql.Timestamp
import java.text.{ParseException, SimpleDateFormat}
import scala.util.control.Breaks.{break, breakable}
class toTimestamp extends AbstractUdf {

  override def name(): String = "toTimestamp"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  /**
    * Converts a date string to a timestamp
    *
    * @param dateString date(time) String
    * @param format     date format
    * @return timestamp
    * @throws ParseException parse exception
    */
  @throws[ParseException]
  def evaluate(dateString: String, format: String): Timestamp = {
    val simpleDateFormat = new SimpleDateFormat(format.trim)
    val date = simpleDateFormat.parse(dateString)
    val time = date.getTime
    new Timestamp(time)
  }

  /**
    * Converts a date string to a timestamp
    *
    * @param dateString date(time) String
    * @return timestamp
    * @throws ParseException parse exception
    */
  @throws[ParseException]
  def evaluate(dateString: String): Timestamp = {
    var time = 0L
    var isCorrect = false
    var pe: ParseException = null

    breakable {
      for (format <- DEFAULT_FORMATS) {
        try {
          val simpleDateFormat = new SimpleDateFormat(format)
          val date = simpleDateFormat.parse(dateString)
          time = date.getTime
          isCorrect = true
          break() //todo: break is not supported
        } catch {
          case ex: ParseException =>
            pe = ex
        }
      }
    }

    if (!isCorrect && pe != null)
      throw new ParseException(
        "Date format is error. Only receive " + DefaultConstant.DEFAULT_FORMATS.mkString(","),
        pe.getErrorOffset
      )
    new Timestamp(time)
  }

  def udfSparkEntries: List[String] = List("udfWrapper", "udfWrapper2")

  def udfWrapper: (String, String) => Timestamp = evaluate

  def udfWrapper2: String => Timestamp = evaluate

}
