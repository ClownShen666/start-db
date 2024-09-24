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

import org.locationtech.jts.geom.Coordinate
import org.urbcomp.start.db.util.GeometryFactoryUtils

import java.sql.Timestamp

class QueryTest extends AbstractCalciteSparkFunctionTest {
  val DEFAULT_TIMESTAMP: Timestamp = Timestamp.valueOf("2022-06-29 10:00:00.000")
  val POINT = GeometryFactoryUtils.defaultGeometryFactory.createPoint(new Coordinate(10, 20))

  test("test query col") {
    val stmt = connect.createStatement()
    stmt.execute("drop table  if exists jack_test_query ")
    stmt.execute("create table if not exists  jack_test_query(i int , b string)")
    stmt.execute("insert into jack_test_query values (556,  \"false\")")
    checkCalciteSpark("select  i from jack_test_query  where b = \"false\"", List(556))
  }

  test("test select one column") {
    val stmt = connect.createStatement()
    stmt.execute("drop table if exists t_int ")
    stmt.execute("create table if not exists t_int(i int)")
    stmt.execute("insert into t_int values (123)")
    checkCalciteSpark("select  *  from t_int ", List(123))
  }

  test("select timestamp") {
    val stmt = connect.createStatement()
    stmt.execute("drop table if exists t_timestamp ")
    stmt.execute("create table if not exists t_timestamp  (timestamp11 timestamp);")
    stmt.execute("insert into t_timestamp values (toTimestamp(\"2022-06-29 10:00:00.000\"));")
    checkCalciteSpark("select * from t_timestamp;")
  }

  test("bool equal test") {
    val stmt = connect.createStatement()
    stmt.execute("drop table if exists t_bool ")
    stmt.execute("create table if not exists t_bool (bool7 bool);")
    stmt.execute("insert into t_bool values (true);")
    checkCalciteSpark("select * from t_bool where bool7 = true;")
  }

  test("chinese string insert test") {
    val stmt = connect.createStatement()
    stmt.execute("drop table if exists t_string10 ")
    stmt.execute("""
                       |create table if not exists t_string10 (string6 string)
                       |""".stripMargin)

    stmt.execute("""
                       |insert into t_string10 values ('字符串')
                       |""".stripMargin)
    checkCalciteSpark("select * from t_string10", List("字符串"))
  }

  test("geometry equal test") {
    val stmt = connect.createStatement()
    stmt.execute("drop table if exists t_point ")
    stmt.execute("create table if not exists t_point (point13 point)")
    stmt.execute("""
                   |insert into t_point values (st_PointFromWkt("Point(10 20)"))
                   |""".stripMargin)

    checkCalciteSpark("""
        |select * from t_point where point13 = st_PointFromWkt("Point(10 20)")
        |""".stripMargin, List(POINT))
  }
}
