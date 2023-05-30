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

import java.math.BigDecimal
class MathFunctionTest extends AbstractCalciteSparkFunctionTest {
  test("log") {
    executeQueryCheck(
      "select log(3.4,9.6) , log(null,null)",
      List(BigDecimal.valueOf(1.848184756802823800), null)
    )
  }

  test("pi") {
    executeQueryCheck("select pi()", List(BigDecimal.valueOf(Math.PI)))
  }

  test("log1p") {
    executeQueryCheck(
      "select log1p(1.5),log1p(null)",
      List(BigDecimal.valueOf(0.9162907318741551), null)
    )
  }

  test("log2") {
    executeQueryCheck(
      "select log2(8.6), log2(null)",
      List(BigDecimal.valueOf(3.1043366598147353), null)
    )
  }

  test("pow") {
    executeQueryCheck("select pow(0.5,3), pow(null,null)", List(BigDecimal.valueOf(0.125), null))
  }

  test("toRadians") {
    executeQueryCheck(
      "select toRadians(90), toRadians(null)",
      List(BigDecimal.valueOf(1.5707963267948966), null)
    )
  }

  test("toDegrees") {
    executeQueryCheck(
      "select toDegrees(1), toDegrees(null)",
      List(BigDecimal.valueOf(57.29577951308232), null)
    )
  }

  test("abs") {
    executeQueryCheck("select abs(-1.9),abs(null)", List(BigDecimal.valueOf(1.9), null))
  }

  test("floor") {
    executeQueryCheck("select floor(1.9), floor(null)", List(BigDecimal.valueOf(1.0), null))
  }

  test("ceil") {
    executeQueryCheck("select ceil(1.9), ceil(null)", List(BigDecimal.valueOf(2.0), null))
  }

  test("mod") {
    executeQueryCheck("select mod(2.4,5), mod(null,null)", List(BigDecimal.valueOf(2.4), null))
  }
}
