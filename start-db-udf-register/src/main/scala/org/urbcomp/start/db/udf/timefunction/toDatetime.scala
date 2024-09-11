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

import org.urbcomp.start.db.udf.timefunction.DefaultConstant.DEFAULT_FORMATS

import java.text.ParseException
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.time.{DateTimeException, LocalDateTime}

class toDatetime {

  /**
    * Converts a date string to a timestamp
    *
    * @param dateString date(time) String
    * @param format     date format
    * @return timestamp
    * @throws ParseException parse exception
    */
  @throws[DateTimeException]
  def eval(dateString: String, format: String): LocalDateTime = {
    if (dateString == null || format == null) {
      return null
    }
    var DateString = dateString
    var Format = format
    if (dateString.length == 10) {
      DateString += " 00:00:00"
      Format += " HH:mm:ss"
    }
    val formatter = DateTimeFormatter.ofPattern(Format.trim)
    LocalDateTime.parse(DateString, formatter)
  }

  /**
    * Converts a String to Datetime
    *
    * @param dateString date(time) String
    * @return Datetime instance
    * @throws DateTimeParseException parse exception
    */
  @throws[DateTimeParseException]
  def eval(dateString: String): LocalDateTime = {
    if (dateString == null) return null
    var localDateTime = LocalDateTime.MIN
    var isCorrect = false
    var pe: DateTimeParseException = null
    var DateString = dateString
    if (dateString.length == 10) DateString += " 00:00:00"
    for (format <- DEFAULT_FORMATS) {
      try {
        val formatter = DateTimeFormatter.ofPattern(format)
        localDateTime = LocalDateTime.parse(DateString, formatter)
        isCorrect = true
        // break //todo: break is not supported
      } catch {
        case exception: DateTimeParseException =>
          pe = exception
      }
    }
    if (!isCorrect && pe != null)
      throw new DateTimeParseException(
        "Date format is error. Only receive " + DEFAULT_FORMATS.mkString(","),
        DateString,
        pe.getErrorIndex
      )
    localDateTime
  }

  def udfSparkEntries: List[String] = List("udfWrapper", "udfWrapper2")

  def udfWrapper: (String, String) => LocalDateTime = eval

  def udfWrapper2: String => LocalDateTime = eval

}
