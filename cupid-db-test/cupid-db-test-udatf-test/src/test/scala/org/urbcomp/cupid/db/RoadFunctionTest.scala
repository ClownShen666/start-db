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
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory

import scala.collection.convert.ImplicitConversions._
import org.urbcomp.cupid.spark.jts._

/**
  * Road Segment/Network Function test
  * test need creat table are to be done
  * @author XiangHe
  */
class RoadFunctionTest extends AbstractCalciteSparkFunctionTest {

  val rs: RoadSegment = ModelGenerator.generateRoadSegment()
  val rn: RoadNetwork = ModelGenerator.generateRoadNetwork()
  val rs2: List[RoadSegment] = ModelGenerator.generateRoadSegments().toList
  val rsGeoJson: String = rs.toGeoJSON
  val trajectory: Trajectory = ModelGenerator.generateTrajectory()
  val tGeo: String = trajectory.toGeoJSON
  val rnGeoJson: String = rn.toGeoJSON

  test("st_rn_shortestPath") {
    val statement = connect.createStatement
    statement.execute("create table if not exists t_road_segment_test (a Integer, b RoadSegment);")
    val set = statement.executeQuery("select count(1) from t_road_segment_test")
    set.next()
    val count = set.getObject(1)
    if (count == 0) {
      statement.execute(
        "insert into t_road_segment_test values (2, st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))"
      )
    }
    executeQueryCheck(
      "select st_rn_shortestPath(st_rn_makeRoadNetwork(collect_list(b))," +
        " st_makePoint(111.37939453125,54.00776876193478)," +
        " st_makePoint(116.3671875,53.05442186546102)) from t_road_segment_test",
      List(
        "{\"type\":\"Feature\",\"properties\":{\"roadSegmentIds\":[-1]," +
          "\"lengthInMeter\":346582.30322217953},\"geometry\":{\"type\":\"LineString\"," +
          "\"coordinates\":[[111.37939453125,54.00776876193478],[111.37939453125,54.00776876193478]," +
          "[116.3671875,53.05442186546102],[116.3671875,53.05442186546102]]}}"
      )
    )
    // todo test error
  }

  test("st_rn_makeRoadNetwork") {
    val statement = connect.createStatement
    statement.execute("create table if not exists t_road_segment_test (a Integer, b RoadSegment);")
    val set = statement.executeQuery("select count(1) from t_road_segment_test")
    set.next()
    val count = set.getObject(1)
    if (count == 0) {
      statement.execute(
        "insert into t_road_segment_test values (2, st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))"
      )
    }
    val resultSet =
      statement.executeQuery(
        "select st_rn_makeRoadNetwork(collect_list(b)) from t_road_segment_test"
      )
    resultSet.next()
    assertEquals(
      "class org.urbcomp.cupid.db.model.roadnetwork.RoadNetwork",
      resultSet.getObject(1).getClass.toString
    )
    // todo test error
  }

  test("st_rs_fromGeoJSON") {
    executeQueryCheck("select st_rs_fromGeoJSON(\'" + rsGeoJson + "\')", List(rs))
  }

  test("st_rs_rsid") {
    executeQueryCheck(
      "select st_rs_rsid(st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))",
      List(rs.getRoadSegmentId.toString)
    )
  }

  test("st_rs_geom") {
    executeQueryCheck(
      "select st_rs_geom(st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))",
      List("LINESTRING (111.37939453125 54.00776876193478, 116.3671875 53.05442186546102)")
    )
  }

  test("st_rs_direction") {
    executeQueryCheck(
      "select st_rs_direction(st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))",
      List(rs.getDirection.toString)
    )
  }

  test("st_rs_speedLimitInKMPerHour") {
    executeQueryCheck(
      "select st_rs_speedLimitInKMPerHour(st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))",
      List(rs.getSpeedLimit)
    )
  }

  test("st_rs_level") {
    executeQueryCheck(
      "select st_rs_level(st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))",
      List(rs.getLevel.value)
    )
  }

  test("st_rs_startId") {
    executeQueryCheck(
      "select st_rs_startId(st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))",
      List(rs.getStartNode.getNodeId.toString)
    )
  }

  test("st_rs_endId") {
    executeQueryCheck(
      "select st_rs_endId(st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))",
      List(rs.getEndNode.getNodeId.toString)
    )
  }

  test("st_rs_lengthInKM") {
    executeQueryCheck(
      "select st_rs_lengthInKM(st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))",
      List(rs.getLengthInMeter / 1000)
    )
  }

  test("st_traj_mapMatch(Trajectory)") {
    val statement = connect.createStatement()
    statement.execute("create table if not exists t_road_segment_test (a Integer, b RoadSegment);")
    val set = statement.executeQuery("select count(1) from t_road_segment_test")
    set.next()
    val count = set.getObject(1)
    if (count == 0) {
      statement.execute(
        "insert into t_road_segment_test values (2, st_rs_fromGeoJSON(\'" + rsGeoJson + "\'))"
      )
    }
    executeQueryCheck(
      "select st_traj_mapMatch(st_rn_makeRoadNetwork(collect_list(b)), " +
        "st_traj_fromGeoJSON(\'" + tGeo + "\')) from t_road_segment_test",
      List(
        "{\"type\":\"FeatureCollection\",\"features\":[],\"properties\":" +
          "{\"oid\":\"afab91fa68cb417c2f663924a0ba1ff9\"," +
          "\"tid\":\"afab91fa68cb417c2f663924a0ba1ff92018-10-09 07:28:21.0\"}}"
      )
    )
    // todo test error
  }

  test("st_rn_reachableConvexHull") {
    executeQueryCheck(
      "select st_rn_reachableConvexHull(st_rn_fromGeoJson(\'"
        + rnGeoJson + "\'),st_makePoint(108.98897,34.25815), 180.0, \"Drive\")",
      List(
        "POLYGON ((34.2595789930556 108.966970757378, 34.2594382052951 108.966974012587, " +
          "34.2580482313368 108.967071940104, 34.2414320203993 108.984652235243, " +
          "34.2402975802951 108.986984049479, 34.2398065863715 108.988017306858, " +
          "34.2427346462674 108.996708170573, 34.2589547543304 109.00989398021, " +
          "34.2591219075521 109.009806043837, 34.2773616536458 108.995655110677, " +
          "34.2763425021701 108.98442437066, 34.2640614149306 108.970958387587, " +
          "34.2606890190972 108.967848307292, 34.2595789930556 108.966970757378))"
      )
    )
  }

  test("st_rn_reachableConcavexHull") {
    executeQueryCheck(
      "select st_rn_reachableConcaveHull(st_rn_fromGeoJson(\'"
        + rnGeoJson + "\'),st_makePoint(108.98897,34.25815), 180.0, \"Drive\")",
      List(
        "POLYGON ((34.2398065863715 108.988017306858, 34.2427346462674 108.996708170573, " +
          "34.2510427517361 109.002337510851, 34.2589547543304 109.00989398021, " +
          "34.2591219075521 109.009806043837, 34.2611219618056 109.005419650608, " +
          "34.2694417317708 108.999469129774, 34.2773616536458 108.995655110677, " +
          "34.2763425021701 108.98442437066, 34.2670222981771 108.975173611111, " +
          "34.2640614149306 108.970958387587, 34.2606890190972 108.967848307292, " +
          "34.2595789930556 108.966970757378, 34.2594382052951 108.966974012587, " +
          "34.2580482313368 108.967071940104, 34.2511817768687 108.975064108939, " +
          "34.2476974826389 108.978150770399, 34.2414320203993 108.984652235243, " +
          "34.2402975802951 108.986984049479, 34.2398065863715 108.988017306858))"
      )
    )
  }
}
