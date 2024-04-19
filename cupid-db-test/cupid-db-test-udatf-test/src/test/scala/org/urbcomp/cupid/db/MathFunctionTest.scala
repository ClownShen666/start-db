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
    checkCalciteSparkFlink("select log(3.4,9.6)", List(BigDecimal.valueOf(1.848184756802823800)))
    checkCalciteSpark("select log(null,null)", List(null))
  }

  test("pi") {
    checkCalciteSparkFlink("select pi()", List(BigDecimal.valueOf(Math.PI)))
  }

  test("log1p") {
    checkCalciteSparkFlink("select log1p(1.5)", List(BigDecimal.valueOf(0.9162907318741551)))
    checkCalciteSpark("select log1p(null)", List(null))
  }

  test("log2") {
    checkCalciteSparkFlink("select log2(8.6)", List(BigDecimal.valueOf(3.1043366598147353)))
    checkCalciteSpark("select log2(null)", List(null))
  }

  test("pow") {
    checkCalciteSparkFlink("select pow(0.5,3)", List(BigDecimal.valueOf(0.125)))
    checkCalciteSpark("select pow(null,null)", List(null))
  }

  test("toRadians") {
    checkCalciteSparkFlink("select toRadians(90)", List(BigDecimal.valueOf(1.5707963267948966)))
    checkCalciteSpark("select toRadians(null)", List(null))
  }

  test("toDegrees") {
    checkCalciteSparkFlink("select toDegrees(1)", List(BigDecimal.valueOf(57.29577951308232)))
    checkCalciteSpark("select toDegrees(null)", List(null))
  }

  test("abs") {
    checkCalciteSparkFlink("select abs(-1.9)", List(BigDecimal.valueOf(1.9)))
    checkCalciteSpark("select abs(null)", List(null))
  }

  test("floor") {
    checkCalciteSparkFlink("select floor(1.9)", List(BigDecimal.valueOf(1.0)))
    checkCalciteSpark("select floor(null)", List(null))
  }

  test("ceil") {
    checkCalciteSparkFlink("select ceil(1.9)", List(BigDecimal.valueOf(2.0)))
    checkCalciteSpark("select ceil(null)", List(null))
  }

  test("mod") {
    checkCalciteSparkFlink("select mod(2.4,5)", List(BigDecimal.valueOf(2.4)))
    checkCalciteSpark("select mod(null,null)", List(null))
  }
}
