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

  val rn: RoadNetwork = ModelGenerator.generateRoadNetwork()
  val rnGeoJson: String = rn.toGeoJSON

  ignore("st_rn_reachableConcavexHull") {
    // This test cost much memory so renew spark session to drop some temp view to get more memory
    SparkExecuteWrapper.getSparkExecute.renewSparkSession()
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
