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

import org.urbcomp.cupid.db.model.roadnetwork.RoadSegment
import org.urbcomp.cupid.db.model.sample.ModelGenerator

/**
  * test for Insert
  * @author  WangBohong
  * @date    2022-06-08
  */
class InsertTest extends AbstractCalciteSparkFunctionTest {

  val rs: RoadSegment = ModelGenerator.generateRoadSegment()
  val rsGeoJson: String = rs.toGeoJSON

  /**
    * test for insert into table values ...
    */
  test("insert into table Values") {
    val statement = connect.createStatement()
    statement.execute("drop table if exists tt_test_demo;")
    statement.execute(
      "create table if not exists tt_test_demo (idx Integer, ride_id string, start_point point);"
    )
    statement.execute(
      "Insert into tt_test_demo (idx, ride_id, start_point) values (171, '05608CC867EBDF63', st_makePoint(2.1, 2))"
    )
    executeQueryCheck("select count(1) from tt_test_demo", List(1))
  }

  /**
    * test for roadsegment insert
    */
  test("roadsegment insert") {
    val statement = connect.createStatement()
    statement.execute("drop table if exists t_road_segment_test;")
    statement.execute("create table if not exists t_road_segment_test (a Integer, b RoadSegment);")

    statement.execute(
      "insert into t_road_segment_test (a, b) values (2, st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))"
    )
    executeQueryCheck("select count(1) from t_road_segment_test", List(1))
  }

  /**
    * test for multiple data insert (insert into t (a, b) values (1, 2), (3, 4);)
    */
  test("multiple data insert") {
    val statement = connect.createStatement()
    statement.execute("drop table if exists t1_test;")
    statement.execute(
      "create table if not exists t1_test (idx Integer, ride_id string, start_point point);"
    )
    statement.execute(
      "Insert into t1_test (idx, ride_id, start_point) values (171, '05608CC867EBDF63', st_makePoint(2.1, 2)), (172, '05608CC867EBDF63', st_makePoint(4.1, 2))"
    )
    statement.execute(
      "Insert into t1_test (idx, ride_id, start_point) values (172, '05608CC86f7EBDF3', st_makePoint(2.2, 2)), (172, '05608CC867EBDF63', st_makePoint(4.1, 2))"
    )
    executeQueryCheck("select count(1) from t1_test", List(4))
  }

  /**
    * test for insert (insert into t values (1, 2))
    */
  test("insert test (without target column)") {
    val statement = connect.createStatement()
    statement.execute("drop table if exists t_road_segment_test;")
    statement.execute("create table if not exists t_road_segment_test (a Integer, b RoadSegment);")
    statement.execute(
      "insert into t_road_segment_test values (2, st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))"
    )
    executeQueryCheck("select count(1) from t_road_segment_test", List(1))
  }

  /**
    * test for insert (insert into t values (1, 2), (3, 4))
    */
  test("multiple data insert test (without target column)") {
    val statement = connect.createStatement()
    statement.execute("drop table if exists t_road_segment_test;")
    statement.execute("create table if not exists t_road_segment_test (a Integer, b RoadSegment);")
    statement.execute(
      "insert into t_road_segment_test values (2, st_rs_fromGeoJSON(\'" + rsGeoJson + "\')), (3, st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))"
    )
    executeQueryCheck("select count(1) from t_road_segment_test", List(2))
  }

  /**
    * test for insert into table select ...
    */
  test("insert into table select") {
    val statement = connect.createStatement()
    statement.executeUpdate("drop table if exists table1")
    statement.executeUpdate("drop table if exists table2")
    statement.executeUpdate("create table if not exists table1(int1 int)")
    statement.executeUpdate("create table if not exists table2(int2 int)")
    statement.executeUpdate("insert into table2 values (1),(2),(3);")
    statement.executeUpdate("insert into table1 select int2 from table2;")
    executeQueryCheck("select count(1) from table1;", List(3))
  }
}
