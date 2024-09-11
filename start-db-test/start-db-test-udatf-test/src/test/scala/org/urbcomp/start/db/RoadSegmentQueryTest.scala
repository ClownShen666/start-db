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

import org.junit.Assert.{assertEquals, assertTrue}
import org.urbcomp.start.db.model.roadnetwork.RoadSegment
import org.urbcomp.start.db.model.sample.ModelGenerator

/**
  * test for RoadSegmentQuery
  *
  * @author WangBohong
  * @date 2022-06-16
  */
class RoadSegmentQueryTest extends AbstractCalciteSparkFunctionTest {

  val rs: RoadSegment = ModelGenerator.generateRoadSegment()
  val rsGeoJson: String = rs.toGeoJSON

  test("basic roadSegment query") {
    statement.execute("drop table if exists t_basic_road_segment_query_test")
    statement.execute("create table if not exists t_basic_road_segment_query_test (rs RoadSegment)")
    checkCalciteSpark("select count(1) from t_basic_road_segment_query_test", List(0))
  }

  test("roadSegment query") {
    statement.execute("drop table if exists t_road_segment_query_test")
    statement.execute("create table if not exists t_road_segment_query_test (rs RoadSegment)")
    statement.execute(
      s"insert into t_road_segment_query_test values (st_rs_fromGeoJSON(\'$rsGeoJson\'))"
    )
    checkCalciteSpark(
      "select rs from t_road_segment_query_test",
      List(
        "{\"type\":\"Feature\",\"properties\":{\"endId\":2,\"level\":6,\"startId\":1,\"rsId\":1,\"speedLimit\":30.0,\"lengthInMeter\":120.0,\"direction\":1},\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[111.37939453125,54.00776876193478],[116.3671875,53.05442186546102]]}}"
      )
    )
  }

}
