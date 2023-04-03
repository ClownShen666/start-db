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

import org.apache.spark.{SparkConf, SparkContext}
import org.urbcomp.cupid.db.algorithm.mapmatch.tihmm.TiHmmMapMatcher
import org.urbcomp.cupid.db.algorithm.shortestpath.ManyToManyShortestPath
import org.urbcomp.cupid.db.model.roadnetwork.RoadNetwork
import org.urbcomp.cupid.db.model.trajectory.{MapMatchedTrajectory, Trajectory}

import java.util
import scala.collection.JavaConverters._

class MapMatch {

  def mapMatch(
      roadNetwork: RoadNetwork,
      trajectory: Array[Trajectory]
  ): util.List[MapMatchedTrajectory] = {
    val sparkConf = new SparkConf().setAppName("MapMatchApp")
    sparkConf.setMaster("local")
    val context = new SparkContext(sparkConf)
    val trajRdd = context.parallelize(trajectory)
    val bcRoadNetwork = context.broadcast[RoadNetwork](roadNetwork)
    val mapMatchRdd = trajRdd.mapPartitions(trajIter => {
      val mapMatcher =
        new TiHmmMapMatcher(bcRoadNetwork.value, new ManyToManyShortestPath(roadNetwork))
      trajIter.flatMap(traj => Option(mapMatcher.mapMatch(traj)))
    })
    mapMatchRdd.collect().toList.asJava

  }

}
