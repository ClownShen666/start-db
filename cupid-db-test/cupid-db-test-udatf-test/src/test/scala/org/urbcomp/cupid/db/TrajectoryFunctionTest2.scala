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

import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory

class TrajectoryFunctionTest2 extends AbstractCalciteSparkFunctionTest {

  val trajectoryStp: Trajectory =
    ModelGenerator.generateTrajectory("data/stayPointSegmentationTraj.txt")
  val tGeoStp: String = trajectoryStp.toGeoJSON

  test("st_traj_stayPointDetect") {
    executeQueryCheck(
      "select st_traj_stayPointDetect(st_traj_fromGeoJSON(\'" + tGeoStp + "\'),10,10)",
      List(
        "2018-10-09 07:28:24.0",
        "2018-10-09 07:28:39.0",
        "MULTIPOINT ((108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822))"
      ),
      List(
        "2018-10-09 07:30:01.0",
        "2018-10-09 07:30:15.0",
        "MULTIPOINT ((108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891))"
      )
    )
  }

}
