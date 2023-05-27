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
    stmt.executeUpdate("drop table if exists sourceTable")
    stmt.executeUpdate(
      "create table sourceTable(age INTEGER, name String, st Point, SPATIAL INDEX indexName(st))"
    )
    stmt.executeUpdate("drop   table if exists target1Table")
    stmt.executeUpdate("create table target1table like sourceTable")

    //test col name
    val rsOfCol = stmt.executeQuery("select  * from target1Table")
    rsOfCol.next()
    assertEquals(1, rsOfCol.findColumn("age"))
    assertEquals(2, rsOfCol.findColumn("name"))
    assertEquals(3, rsOfCol.findColumn("st"))

    //test index
    val rsOfIndex = stmt.executeQuery("show index from target1Table")
    rsOfIndex.next()
    assertEquals("target1table", rsOfIndex.getString(1))
    assertEquals("indexName", rsOfIndex.getString(2))
    assertEquals("z2", rsOfIndex.getString(3))
    assertEquals("st", rsOfIndex.getString(4))

  }

}
