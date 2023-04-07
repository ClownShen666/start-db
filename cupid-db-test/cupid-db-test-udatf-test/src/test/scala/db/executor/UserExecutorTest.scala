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
package db.executor

import db.AbstractCalciteFunctionTest

class UserExecutorTest extends AbstractCalciteFunctionTest {

  private val randomNum = scala.util.Random.nextInt(100000)
  private val CREATE_USER_EXAMPLE =
    s"""CREATE USER test_user_%d IDENTIFIED BY 'password'""".format(randomNum).stripMargin

  test("test create user") {
    val stmt = connect.createStatement()
    stmt.executeUpdate(CREATE_USER_EXAMPLE)
  }

}
