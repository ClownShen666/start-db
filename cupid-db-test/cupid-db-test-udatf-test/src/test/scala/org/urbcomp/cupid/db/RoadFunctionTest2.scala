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

import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory

import scala.collection.convert.ImplicitConversions._

/**
  * Road Segment/Network Function test
  * test need creat table are to be done
  * @author XiangHe
  */
class RoadFunctionTest2 extends AbstractCalciteSparkFunctionTest {

  val rs: RoadSegment = ModelGenerator.generateRoadSegment()
  val rn: RoadNetwork = ModelGenerator.generateRoadNetwork()
  val rs2: List[RoadSegment] = ModelGenerator.generateRoadSegments().toList
  val rsGeoJson: String = rs.toGeoJSON
  val trajectory: Trajectory = ModelGenerator.generateTrajectory()
  val tGeo: String = trajectory.toGeoJSON
  val rnGeoJson: String = rn.toGeoJSON

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

}
