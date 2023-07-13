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
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.tools.CitibikeDataUtils

import java.util.UUID

class LoadTest extends AbstractCalciteSparkFunctionTest {

  val trajectory: Trajectory = ModelGenerator.generateTrajectory()
  val tGeo: String = trajectory.toGeoJSON
  val PATH
      : String = CitibikeDataUtils.getProjectRoot + "/cupid-db-test/cupid-db-test-geomesa-geotools/src/main/resources/" + "202204-citibike-tripdata_clip_slice.csv"
  val PATH_OF_TRA
      : String = CitibikeDataUtils.getProjectRoot + "/cupid-db-test/cupid-db-test-geomesa-geotools/src/main/resources/" + "202204-citibike-tripdata_with_tra_road.csv"
  val PATH_OF_DATA_FOR_SPARK
      : String = CitibikeDataUtils.getProjectRoot + "/cupid-db-test/cupid-db-test-geomesa-geotools/src/main/resources/" + "202204-citibike-tripdata_with_tra_road_for_spark.csv"
  val rs: RoadSegment = ModelGenerator.generateRoadSegment()
  val rsGeoJson: String = rs.toGeoJSON

  test("test load - single column - no udf") {
    val randTableName = s"Table_${UUID.randomUUID().toString.replace("-", "_")}"
    val createTableSql = s"create table if not exists $randTableName (idx Integer)"
    val loadSql =
      s"""LOAD CSV INPATH \"$PATH\" TO $randTableName (
         |  idx _c1)
         |  WITH HEADER""".stripMargin
    val querySql = s"select count(idx) from $randTableName"

    val stmt = connect.createStatement()
    stmt.execute(createTableSql)
    stmt.execute(loadSql)

    executeQueryCheck(querySql, List(10))
  }

  test("test load - multiple columns - no udf") {
    val randTableName = s"Table_${UUID.randomUUID().toString.replace("-", "_")}"
    val createTableSql =
      s"create table if not exists $randTableName (idx Integer, ride_id String, rideable_type String)"
    val loadSql =
      s"""LOAD CSV INPATH \"$PATH\" TO $randTableName (
         |  idx _c1,
         |  ride_id _c2,
         |  rideable_type _c3)
         |  WITH HEADER
         |""".stripMargin
    val querySql = s"select ride_id, rideable_type from $randTableName where idx = 1"
    val stmt = connect.createStatement()
    stmt.execute(createTableSql)
    stmt.execute(loadSql)
    executeQueryCheck(querySql, List("8B88A6F8158F650D", "electric_bike"))
  }

  test("test load -traj -road  - with udf") {
    val randTableName = s"Table_${UUID.randomUUID().toString.replace("-", "_")}"
    val createTableSql =
      s"create table if not exists $randTableName(idx Integer, ride_id String, rideable_type String, traj Trajectory, road RoadSegment)"
    val loadSql =
      s"""LOAD CSV INPATH \"$PATH_OF_TRA\" TO $randTableName (
         |  idx _c1 ,
         |  ride_id _c2 ,
         |  rideable_type _c3,
         |  traj st_traj_fromGeoJSON(_c4),
         |  road st_rs_fromGeoJSON(_c5))
         |  FIELDS DELIMITER "!"
         |  WITH HEADER
         |""".stripMargin
    val querySql = s"select ride_id, rideable_type , traj , road from $randTableName where idx = 1"

    val stmt = connect.createStatement()
    stmt.execute(createTableSql)
    stmt.execute(loadSql)
    executeQueryCheck(querySql, List("8B88A6F8158F650D", "electric_bike", tGeo, rsGeoJson))
  }

  test("test load - multiple columns - with udf") {
    val randTableName = s"Table_${UUID.randomUUID().toString.replace("-", "_")}"
    val createTableSql =
      s"create table if not exists $randTableName (idx Integer, ride_id String, start_point Point)"
    val loadSql =
      s"""LOAD CSV INPATH \"$PATH\" TO $randTableName (
         |  idx _c1,
         |  ride_id _c2,
         |  start_point st_makePoint(_c10, _c11))
         |  WITH HEADER
         |""".stripMargin
    val querySql = s"select start_point from $randTableName where idx = 2"

    val stmt = connect.createStatement()
    stmt.execute(createTableSql)
    stmt.execute(loadSql)
    executeQueryCheck(querySql, List("POINT (40.646475 -74.026081)"))
  }

  test("test load -traj   - with udf in spark") {
    val randTableName = s"Table_${UUID.randomUUID().toString.replace("-", "_")}"
    val createTableSql =
      s"create table if not exists $randTableName(idx Integer, ride_id String, rideable_type String, traj Trajectory, road RoadSegment)"
    val loadSql =
      s"""LOAD CSV INPATH \"$PATH_OF_DATA_FOR_SPARK\" TO $randTableName (
         |  idx idx ,
         |  ride_id ride_id ,
         |  rideable_type rideable_type,
         |  traj st_traj_fromGeoJSON(traj),
         |  road st_rs_fromGeoJSON(road))
         |  FIELDS DELIMITER "!" QUOTES "'"
         |  WITH HEADER
         |""".stripMargin
    val querySql =
      s"select ride_id, rideable_type , traj , road, idx from $randTableName where idx = 1"

    val stmt = connect.createStatement()
    stmt.execute(createTableSql)
    SparkExecuteWrapper.getSparkExecute.executeSql(loadSql)
    SparkExecuteWrapper.getSparkExecute.executeSql(querySql)
    executeQueryCheck(querySql, List("8B88A6F8158F650D", "electric_bike", tGeo, rsGeoJson))
  }
}
