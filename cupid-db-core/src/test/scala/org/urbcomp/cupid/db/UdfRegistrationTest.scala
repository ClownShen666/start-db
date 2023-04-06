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
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory

/**
  * Udf Registration Test
  *
  * @author Hang Wu
  * @date 2023-03-08
  */
class UdfRegistrationTest extends AbstractCalciteFunctionTest {

  test("udf registration test") {
    val statement = connect.createStatement()
    statement.executeUpdate("DROP TABLE IF EXISTS test3")
    statement.executeUpdate("create table test3 (dat Int)")
    statement.executeUpdate("insert into table test3 values (1)")
    statement.executeUpdate("insert into table test3 values (2)")
    statement.executeUpdate("insert into table test3 values (7)")
    val resultSet =
      statement.executeQuery("select dat, AddOne(dat) from test3")
    while (resultSet.next()) {
      val u = resultSet.getObject(1).toString.toInt
      val v = resultSet.getObject(2).toString.toInt
      assertEquals(0, u + 1 - v)
    }
  }

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
