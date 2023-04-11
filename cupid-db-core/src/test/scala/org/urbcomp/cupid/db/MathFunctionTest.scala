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
import java.math.BigDecimal
class MathFunctionTest extends AbstractCalciteFunctionTest {

  /**
    * test for log
    */
  test("log") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select log(3.4,7.4)")
    resultSet.next()
    assertEquals(BigDecimal.valueOf(1.6354961445476646), resultSet.getObject(1))
    val resultSet2 = statement.executeQuery("select log(null,null)")
    resultSet2.next();
    assertEquals(null, resultSet2.getObject(1))
  }

  /**
    * test for pi
    */
  test("pi") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select pi()")
    resultSet.next()
    assertEquals(BigDecimal.valueOf(Math.PI), resultSet.getObject(1))
  }

  /**
    * test for log1p
    */
  test("log1p") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select log1p(1.5)")
    resultSet.next()
    assertEquals(BigDecimal.valueOf(0.9162907318741551), resultSet.getObject(1))
    val resultSet2 = statement.executeQuery("select log1p(null)")
    resultSet2.next()
    assertEquals(null, resultSet2.getObject(1))
  }

  /**
    * test for log2
    */
  test("log2") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select log2(8.6)")
    resultSet.next()
    assertEquals(BigDecimal.valueOf(3.1043366598147353), resultSet.getObject(1))
    val resultSet2 = statement.executeQuery("select log2(null)")
    resultSet2.next()
    assertEquals(null, resultSet2.getObject(1))
  }

  /**
    * test for pow
    */
  test("pow") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select pow(0.5,3)")
    resultSet.next()
    assertEquals(BigDecimal.valueOf(0.125), resultSet.getObject(1))
    val resultSet2 = statement.executeQuery("select pow(null,null)")
    resultSet2.next()
    assertEquals(null, resultSet2.getObject(1))

  }

  /**
    * test for toRadians
    */
  test("toRadians") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select toRadians(90)")
    resultSet.next()
    assertEquals(BigDecimal.valueOf(1.5707963267948966), resultSet.getObject(1))
    val resultSet2 = statement.executeQuery("select toRadians(null)")
    resultSet2.next()
    assertEquals(null, resultSet2.getObject(1))

  }

  /**
    * test for toDegrees
    */
  test("toDegrees") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select toDegrees(1)")
    resultSet.next()
    assertEquals(BigDecimal.valueOf(57.29577951308232), resultSet.getObject(1))
    val resultSet2 = statement.executeQuery("select toDegrees(null)")
    resultSet2.next()
    assertEquals(null, resultSet2.getObject(1))
  }

  test("abs") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select abs(-1.9)")
    resultSet.next()
    assertEquals(BigDecimal.valueOf(1.9), resultSet.getObject(1))
    val resultSet2 = statement.executeQuery("select abs(null)")
    resultSet2.next()
    assertEquals(null, resultSet2.getObject(1))
  }

  test("floor") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select floor(1.9)")
    resultSet.next()
    assertEquals(BigDecimal.valueOf(1.0), resultSet.getObject(1))
    val resultSet2 = statement.executeQuery("select floor(null)")
    resultSet2.next()
    assertEquals(null, resultSet2.getObject(1))
  }

  test("ceil") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select ceil(1.9)")
    resultSet.next()
    assertEquals(BigDecimal.valueOf(2.0), resultSet.getObject(1))
    val resultSet2 = statement.executeQuery("select ceil(null)")
    resultSet2.next()
    assertEquals(null, resultSet2.getObject(1))
  }

  test("mod") {
    val statement = connect.createStatement()
    val resultSet = statement.executeQuery("select mod(2.4,5)")
    resultSet.next()
    assertEquals(BigDecimal.valueOf(2.4), resultSet.getObject(1))
    val resultSet2 = statement.executeQuery("select mod(null,null)")
    resultSet2.next()
    assertEquals(null, resultSet2.getObject(1))
  }

}
