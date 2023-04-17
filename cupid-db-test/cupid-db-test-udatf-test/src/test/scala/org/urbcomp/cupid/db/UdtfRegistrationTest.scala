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

/**
  * Udf Registration Test
  *
  * @author Hang Wu
  * @date 2023-03-08
  */
class UdtfRegistrationTest extends AbstractCalciteSparkFunctionTest {

  test("udtf registration test1") {
    val statement = connect.createStatement()
    statement.executeUpdate("DROP TABLE IF EXISTS test1")
    statement.executeUpdate("create table test1 (name String, class String)")
    statement.executeUpdate("insert into table test1 values ('Java', 'Hadoop scala')")
    statement.executeUpdate("insert into table test1 values ('Python', 'Hadoop kafka')")
    statement.executeUpdate("insert into table test1 values ('www', 'spark hive sqoop')")
    val resultSet =
      statement.executeQuery("select name, StringSplit(class) from test1")
    val correctOutp = List(
      ("Java", "Hadoop"),
      ("Java", "scala"),
      ("www", "spark"),
      ("www", "hive"),
      ("www", "sqoop"),
      ("Python", "Hadoop"),
      ("Python", "kafka")
    )
    var queryOutp: List[(String, String)] = List()
    while (resultSet.next()) {
      queryOutp :+= ((resultSet.getObject(1).toString, resultSet.getObject(2).toString))
    }
    assertEquals(correctOutp.sorted, queryOutp.sorted)
  }

  test("udtf registration test2") {
    val statement = connect.createStatement()
    statement.executeUpdate("DROP TABLE IF EXISTS test2")
    statement.executeUpdate("create table test2 (delimiter String, class String)")
    statement.executeUpdate("insert into table test2 values (';', 'abc:123;efd:567;utf:890')")
    statement.executeUpdate("insert into table test2 values (';', 'Hadoop:kafka')")
    statement.executeUpdate("insert into table test2 values ('_', 'abc:12_efd:56_fff:89')")
    val resultSet =
      statement.executeQuery("select StringSplitTwice(delimiter, class) from test2")
    val correctOutp = List(
      ("abc", "123"),
      ("efd", "567"),
      ("utf", "890"),
      ("Hadoop", "kafka"),
      ("abc", "12"),
      ("efd", "56"),
      ("fff", "89")
    )
    var queryOutp: List[(String, String)] = List()
    while (resultSet.next()) {
      queryOutp :+= ((resultSet.getObject(1).toString, resultSet.getObject(2).toString))
    }
    assertEquals(correctOutp.sorted, queryOutp.sorted)
  }
}
