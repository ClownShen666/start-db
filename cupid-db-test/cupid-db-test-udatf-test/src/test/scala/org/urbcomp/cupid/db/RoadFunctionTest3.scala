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

import org.urbcomp.cupid.db.model.roadnetwork.RoadNetwork
import org.urbcomp.cupid.db.model.sample.ModelGenerator

class RoadFunctionTest3 extends AbstractCalciteSparkFunctionTest {

  val rn: RoadNetwork = ModelGenerator.generateRoadNetwork(25000)
  val rnGeoJson: String = rn.toGeoJSON

  test("st_rn_reachableConcaveHull") {
    // This test cost much memory so renew spark session to drop some temp view to get more memory
    SparkExecuteWrapper.getSparkExecute.renewSparkSession()
    checkCalciteSparkFlink(
      "select st_rn_reachableConcaveHull(st_rn_fromGeoJson(\'"
        + rnGeoJson + "\'),st_makePoint(108.98897,34.25815), 180.0, \'Drive\')",
      List(
        "POLYGON ((34.2581641120433 108.988074520568, 34.2581700303819 108.988132324219, " +
          "34.2582291666667 108.988320583767, 34.2582282263107 108.988253975218, " +
          "34.2582275390625 108.988205295139, 34.2580460611979 108.986832139757, " +
          "34.2580186631944 108.986594509549, 34.2578765190972 108.985404188368, " +
          "34.2577994791667 108.984770236545, 34.2577338324653 108.984903971354, " +
          "34.2575575086806 108.985347222222, 34.2575290256076 108.98542453342, " +
          "34.2575168185764 108.985468207465, 34.2575111219618 108.985507541233, " +
          "34.2581108940972 108.987557508681, 34.2581258138021 108.98770046658, " +
          "34.2581641120433 108.988074520568))"
      )
    )
  }

}
