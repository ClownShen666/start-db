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
package org.urbcomp.cupid.db

import java.sql.Timestamp
import java.time.LocalDateTime

class TimeFunctionTest extends AbstractCalciteSparkFunctionTest {
  val DEFAULT_TIME_STR = "2021-05-20 11:21:01.234"
  val DEFAULT_DATETIME: LocalDateTime = LocalDateTime.of(2021, 5, 20, 11, 21, 1, 234000000)
  val DEFAULT_TIMESTAMP: Timestamp = Timestamp.valueOf(DEFAULT_TIME_STR)
  val DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
  //FIXME flink No match found for function signature toTimestamp(<CHARACTER>, <CHARACTER>)
  test("toTimestamp(str, format)") {
    checkCalciteSpark(
      "select toTimestamp(\'" + DEFAULT_TIME_STR + "\', \'" + DEFAULT_FORMAT + "\')",
      List(DEFAULT_TIMESTAMP)
    )
  }

  //FIXME flink No match found for function signature toTimestamp(<CHARACTER>)
  test("toTimestamp(str)") {
    checkCalciteSpark("select toTimestamp(\'" + DEFAULT_TIME_STR + "\')", List(DEFAULT_TIMESTAMP))
  }

  // FIXME ci failed for value mismatch, maybe caused by timezone
  ignore("currentTimestamp") {
    checkCalciteSpark(
      "select timestampFormat(currentTimestamp(),'yyyy-MM-dd' )",
      List(new Timestamp(System.currentTimeMillis).toString.substring(0, "yyyy-MM-dd".length))
    )
  }

  test("timestampToLong(str)") {
    checkCalciteSpark(
      "select timestampToLong('" + DEFAULT_TIME_STR + "')",
      List(DEFAULT_TIMESTAMP.getTime)
    )
    checkCalciteSpark("select timestampToLong(null)", List(null))
  }

  test("timestampToLong(timestamp)") {
    checkCalciteSpark(
      "select timestampToLong(longToTimestamp(" + DEFAULT_TIMESTAMP.getTime + "))",
      List(DEFAULT_TIMESTAMP.getTime)
    )
  }

  // FIXME flink output format
  test("longToTimestamp(long)") {
    checkCalciteSpark(
      "select longToTimestamp(" + DEFAULT_TIMESTAMP.getTime + ")",
      List(DEFAULT_TIMESTAMP)
    )
  }

  test("timestampFormat(str, format)") {
    checkCalciteSpark(
      "select timestampFormat('" + DEFAULT_TIME_STR + "', 'yyyy-MM-dd')",
      List(DEFAULT_TIME_STR.substring(0, "yyyy-MM-dd".length))
    )
  }

  test("toDatetime(str, format)") {
//    checkCalciteSpark("select toDatetime('" + DEFAULT_TIME_STR + "', '" + DEFAULT_FORMAT + "')",
//      List(DEFAULT_TIMESTAMP))
    //todo fixme
  }

  test("toDatetime(str)") {
//    checkCalciteSpark("select toDatetime('" + DEFAULT_TIME_STR + "')",
//      List(DEFAULT_TIMESTAMP))
    // todo fixme
  }

  test("datetimeToTimestamp") {
//    checkCalciteSpark("select datetimeToTimestamp('" + DEFAULT_TIME_STR + "')",
//      List(DEFAULT_TIMESTAMP))
    // todo fixme
  }

  test("timestampToDatetime") {
//    checkCalciteSpark("select timestampToDatetime('" + DEFAULT_TIME_STR + "')",
//      List(DEFAULT_TIMESTAMP))
    // todo fixme
  }

  test("currentDatetime") {
    //checkCalciteSpark("select timestampFormat(currentDatetime(), 'yyyy-MM-dd')")
    // todo fixme
  }

  test("datetimeFormat") {
    checkCalciteSpark(
      "select datetimeFormat('" + DEFAULT_TIME_STR + "', 'yyyy-MM-dd')",
      List(DEFAULT_TIME_STR.substring(0, "yyyy-MM-dd".length))
    )
  }

  test("hour") {
    checkCalciteSpark("select hour('" + DEFAULT_TIME_STR + "')", List(11))
  }

  test("minute") {
    checkCalciteSpark("select minute('" + DEFAULT_TIME_STR + "')", List(21))
  }

  test("second") {
    checkCalciteSpark("select second('" + DEFAULT_TIME_STR + "')", List(1))
  }

  test("week") {
    checkCalciteSpark("select week('" + DEFAULT_TIME_STR + "')", List(20))
    checkCalciteSpark("select week(null)", List(null))
  }

  test("month") {
    checkCalciteSpark("select month('" + DEFAULT_TIME_STR + "')", List(5))
  }

  test("year") {
    checkCalciteSpark("select year('" + DEFAULT_TIME_STR + "')", List(2021))
    checkCalciteSpark("select year(null)", List(null))
  }

  test("dayOfMonth") {
    checkCalciteSpark("select dayOfMonth('" + DEFAULT_TIME_STR + "')", List(20))
  }

  test("dayOfWeek") {
    checkCalciteSpark("select dayOfWeek('" + DEFAULT_TIME_STR + "')", List(4))
  }

  test("dayOfYear") {
    checkCalciteSpark("select dayOfYear('" + DEFAULT_TIME_STR + "')", List(140))
  }

  test("now") {
    checkCalciteSpark("select timestampFormat(now(), 'yyyy-MM-dd')")
  }
}
