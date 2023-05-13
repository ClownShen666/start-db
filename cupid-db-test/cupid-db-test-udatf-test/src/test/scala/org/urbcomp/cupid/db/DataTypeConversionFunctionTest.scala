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
    executeQueryCheck("select castToInteger('1234')", List(1234))
    executeQueryCheck("select castToInteger(null)", List(null))
  }

  test("castToLong") {
    executeQueryCheck("select castToLong('12344')", List(12344L))
    executeQueryCheck("select castToLong(null)", List(null))
  }

  test("castToFloat") {
    executeQueryCheck("select castToFloat('123.1')", List(123.1f))
    executeQueryCheck("select castToFloat(null)", List(null))
  }

  test("castToDouble") {
    executeQueryCheck("select castToDouble('123444555.3')", List(123444555.3d))
    executeQueryCheck("select castToDouble(null)", List(null))
  }

  test("castToBoolean") {
    executeQueryCheck("select castToBoolean('true')", List(true))
    executeQueryCheck("select castToBoolean('false')", List(false))
  }

  test("castToString") {
    executeQueryCheck("select castToString('1234')", List("1234"))
    executeQueryCheck("select castToString(null)", List(null))
  }

  test("parseInteger") {
    executeQueryCheck("select parseInteger('1234')", List(1234))
    executeQueryCheck("select parseInteger(null)", List(null))
  }

  test("parseLong") {
    executeQueryCheck("select parseLong('1234')", List(1234L))
    executeQueryCheck("select parseLong(null)", List(null))
  }

  test("parseDouble") {
    executeQueryCheck("select parseDouble('12345678901234567890')", List(12345678901234567890d))
    executeQueryCheck("select parseDouble(null)", List(null))
  }
}
