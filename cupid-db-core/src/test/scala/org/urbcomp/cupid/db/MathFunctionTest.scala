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

import org.junit.Assert.assertEquals

class MathFunctionTest extends AbstractCalciteFunctionTest {

  /**
    * test for log
    */
  test("log") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select log(3.3,9.6)")
    resultSet.next()
    assertEquals(BigDecimal(1.8943969631190591).bigDecimal, resultSet.getObject(1))
  }

  /**
    * test for pi
    */
  test("pi") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select pi()")
    resultSet.next()
    assertEquals(Math.PI, resultSet.getObject(1))
  }

  /**
    * test for log1p
    */
  test("log1p") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select log1p(1.5)")
    resultSet.next()
    assertEquals(BigDecimal(0.9162907318741551).bigDecimal, resultSet.getObject(1))
  }

  /**
    * test for log2
    */
  test("log2") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select log2(8.6)")
    resultSet.next()
    assertEquals(BigDecimal(3.1043366598147353).bigDecimal, resultSet.getObject(1))
  }

  /**
    * test for pow
    */
  test("pow") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select pow(0.5,3)")
    resultSet.next()
    assertEquals(BigDecimal(0.125).bigDecimal, resultSet.getObject(1))
  }

  /**
    * test for toRadians
    */
  test("toRadians") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select toRadians(90)")
    resultSet.next()
    assertEquals(BigDecimal(1.5707963267948966).bigDecimal, resultSet.getObject(1))
  }

  /**
    * test for toDegrees
    */
  test("toDegrees") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select toDegrees(1)")
    resultSet.next()
    assertEquals(BigDecimal(57.29577951308232).bigDecimal, resultSet.getObject(1))
  }
}
