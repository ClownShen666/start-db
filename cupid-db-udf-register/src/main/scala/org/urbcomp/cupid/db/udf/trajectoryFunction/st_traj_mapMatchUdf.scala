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
package org.urbcomp.cupid.db.udf.trajectoryFunction

import com.fasterxml.jackson.core.JsonProcessingException
import org.urbcomp.cupid.db.algorithm.mapmatch.tihmm.TiHmmMapMatcher
import org.urbcomp.cupid.db.algorithm.shortestpath.ManyToManyShortestPath
import org.urbcomp.cupid.db.exception.AlgorithmExecuteException
import org.urbcomp.cupid.db.model.roadnetwork.RoadNetwork
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}

class st_traj_mapMatchUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_traj_mapMatch"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)
  @throws[AlgorithmExecuteException]
  @throws[JsonProcessingException]
  def eval(roadNetwork: RoadNetwork, trajectory: Trajectory): String = {
    if (roadNetwork == null || trajectory == null) null
    else {
      val mapMatcher = new TiHmmMapMatcher(roadNetwork, new ManyToManyShortestPath(roadNetwork))
      val mmTrajectory = mapMatcher.mapMatch(trajectory)
      mmTrajectory.toGeoJSON
    }
  }
  def udfSparkEntries: List[String] = List("udfWrapper1")

  def udfWrapper1: (RoadNetwork, Trajectory) => String = eval
}
