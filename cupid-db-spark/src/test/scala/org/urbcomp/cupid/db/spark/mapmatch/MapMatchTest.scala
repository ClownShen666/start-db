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
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.spark.SparkQueryExecutor

class MapMatchTest extends FunSuite {

  test("test map matching") {
    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._

    val roadNetwork = ModelGenerator.generateRoadNetwork()
    val traj: Trajectory = ModelGenerator.generateTrajectory()
    val trajRdd = spark.sparkContext.parallelize(Seq((1, traj), (2, traj), (3, traj)))
    val trajDf = trajRdd.toDF("id", "trajectory")

    val mapMatch: MapMatch = new MapMatch
    val result = mapMatch.mapMatch(roadNetwork, trajDf)
    result.show(numRows = 1, truncate = false)
  }

}
