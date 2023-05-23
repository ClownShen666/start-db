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

class CreateTest extends AbstractCalciteSparkFunctionTest {

  test("create table like") {

    val stmt = connect.createStatement()
    stmt.executeUpdate("drop   table if exists sourceTable")
    stmt.executeUpdate("create table if not exists sourceTable(idx INTEGER , name INTEGER)")
    stmt.executeUpdate("drop   table if exists target1Table")
    stmt.executeUpdate("create table target1Table like sourceTable")
    val rs = stmt.executeQuery("select  * from target1Table")

    assertEquals(1, rs.findColumn("idx"))
    assertEquals(2, rs.findColumn("name"))

  }

}
