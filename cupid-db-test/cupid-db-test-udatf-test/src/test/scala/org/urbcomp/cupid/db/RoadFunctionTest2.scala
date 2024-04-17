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

/**
  * Road Segment/Network Function test
  * test need creat table are to be done
  * @author XiangHe
  */
class RoadFunctionTest2 extends AbstractCalciteSparkFunctionTest {

  val rn: RoadNetwork = ModelGenerator.generateRoadNetwork(25000)
  val rnGeoJson: String = rn.toGeoJSON

  test("st_rn_reachableConvexHull") {
    // This test cost much memory so renew spark session to drop some temp view to get more memory
    SparkExecuteWrapper.getSparkExecute.renewSparkSession()
    checkCalciteSpark(
      "select st_rn_reachableConvexHull(st_rn_fromGeoJson(\'"
        + rnGeoJson + "\'),st_makePoint(108.98897,34.25815), 180.0, \'Drive\')",
      List(
        "POLYGON ((34.2577994791667 108.984770236545, 34.2577338324653 108.984903971354, " +
          "34.2575575086806 108.985347222222, 34.2575290256076 108.98542453342, 34.2575168185764 108.985468207465, " +
          "34.2575111219618 108.985507541233, 34.2581700303819 108.988132324219, 34.2582291666667 108.988320583767, " +
          "34.2582282263107 108.988253975218, 34.2582275390625 108.988205295139, 34.2577994791667 108.984770236545))"
      )
    )
  }

}
