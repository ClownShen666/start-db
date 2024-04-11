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

class DataTypeConversionFunctionTest extends AbstractCalciteSparkFunctionTest {

  test("castToInteger") {
    checkCalciteSpark("select castToInteger('1234')", List(1234))
    checkCalciteSpark("select castToInteger(null)", List(null))
  }

  test("castToLong") {
    checkCalciteSpark("select castToLong('12344')", List(12344L))
    checkCalciteSpark("select castToLong(null)", List(null))
  }

  test("castToFloat") {
    checkCalciteSpark("select castToFloat('123.1')", List(123.1f))
    checkCalciteSpark("select castToFloat(null)", List(null))
  }

  test("castToDouble") {
    checkCalciteSpark("select castToDouble('123444555.3')", List(123444555.3d))
    checkCalciteSpark("select castToDouble(null)", List(null))
  }

  test("castToBoolean") {
    checkCalciteSpark("select castToBoolean('true')", List(true))
    checkCalciteSpark("select castToBoolean('TRUE')", List(true))
    checkCalciteSpark("select castToBoolean('false')", List(false))
    checkCalciteSpark("select castToBoolean('FALSE')", List(false))
    checkCalciteSpark("select castToBoolean(null)", List(null))
    checkCalciteSpark("select castToBoolean('notaboolean')", List(null))
  }

  // FIXME flink the input parameter is AnyRef
  test("castToString") {
    checkCalciteSpark("select castToString(1234)", List("1234"))
    checkCalciteSpark("select castToString(null)", List(null))
  }

  // FIXME flink the input parameter is AnyRef
  test("parseInteger") {
    checkCalciteSpark("select parseInteger('1234')", List(1234))
    checkCalciteSpark("select parseInteger(null)", List(null))
  }

  // FIXME flink the input parameter is AnyRef
  test("parseLong") {
    checkCalciteSpark("select parseLong('1234')", List(1234L))
    checkCalciteSpark("select parseLong(null)", List(null))
  }

  // FIXME flink the input parameter is AnyRef
  test("parseDouble") {
    checkCalciteSpark("select parseDouble('12345678901234567890')", List(12345678901234567890d))
    checkCalciteSpark("select parseDouble(null)", List(null))
  }
}
