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
package org.urbcomp.start.db

import org.junit.Assert.assertEquals

import scala.collection.mutable.ListBuffer

/**
  * Udf Registration Test
  *
  * @author Hang Wu
  * @date 2023-03-08
  */
class UdfRegistrationTest extends AbstractCalciteSparkFunctionTest {

  test("reverse") {
    val statement = connect.createStatement
    val resultSet = statement.executeQuery("select reverse('abcde')")
    resultSet.next()
    assertEquals("edcba", resultSet.getObject(1))
    val resultSet2 = statement.executeQuery("select reverse(null)")
    resultSet2.next()
    assertEquals(null, resultSet2.getObject(1))
  }
}
