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
    checkCalciteSparkFlink("select castToInteger('1234')", List(1234))
    checkCalciteSparkFlink("select castToInteger(null)", List(null))
  }

  test("castToLong") {
    checkCalciteSparkFlink("select castToLong('12344')", List(12344L))
    checkCalciteSparkFlink("select castToLong(null)", List(null))
  }

  test("castToFloat") {
    checkCalciteSparkFlink("select castToFloat('123.1')", List(123.1f))
    checkCalciteSparkFlink("select castToFloat(null)", List(null))
  }

  test("castToDouble") {
    checkCalciteSparkFlink("select castToDouble('123444555.3')", List(123444555.3d))
    checkCalciteSparkFlink("select castToDouble(null)", List(null))
  }

  test("castToBoolean") {
    checkCalciteSparkFlink("select castToBoolean('true')", List(true))
    checkCalciteSparkFlink("select castToBoolean('TRUE')", List(true))
    checkCalciteSparkFlink("select castToBoolean('false')", List(false))
    checkCalciteSparkFlink("select castToBoolean('FALSE')", List(false))
    checkCalciteSparkFlink("select castToBoolean(null)", List(null))
    checkCalciteSparkFlink("select castToBoolean('notaboolean')", List(null))
  }

  test("castToString") {
    checkCalciteSparkFlink("select castToString(1234)", List("1234"))
    checkCalciteSparkFlink("select castToString(null)", List(null))
  }

  test("parseInteger") {
    checkCalciteSparkFlink("select parseInteger('1234')", List(1234))
    checkCalciteSparkFlink("select parseInteger(null)", List(null))
  }

  test("parseLong") {
    checkCalciteSparkFlink("select parseLong('1234')", List(1234L))
    checkCalciteSparkFlink("select parseLong(null)", List(null))
  }

  test("parseDouble") {
    checkCalciteSparkFlink(
      "select parseDouble('12345678901234567890')",
      List(12345678901234567890d)
    )
    checkCalciteSparkFlink("select parseDouble(null)", List(null))
  }
}
