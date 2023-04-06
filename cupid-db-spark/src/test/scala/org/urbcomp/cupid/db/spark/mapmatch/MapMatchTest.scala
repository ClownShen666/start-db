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
package org.urbcomp.cupid.db.spark.mapmatch

import org.scalatest.FunSuite
import org.urbcomp.cupid.db.model.sample.ModelGenerator.{generateRoadNetwork, generateTrajectory}
import org.urbcomp.cupid.db.model.trajectory.Trajectory

class MapMatchTest extends FunSuite {

  test("test map matching") {
    val mapMatch: MapMatch = new MapMatch
    val rn = generateRoadNetwork()
    val traj = generateTrajectory()
    val trajArray = Array[Trajectory](traj)
    val result = mapMatch.mapMatch(rn, trajArray)

    assertResult(result.get(0).getMmPtList.size())(117)
  }

}
