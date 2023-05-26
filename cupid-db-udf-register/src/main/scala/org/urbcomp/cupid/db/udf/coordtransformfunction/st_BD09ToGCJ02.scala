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
package org.urbcomp.cupid.db.udf.coordtransformfunction

import org.locationtech.jts.geom.Geometry
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.udf.coordtransformfunction.coordtransform.{
  BD09ToGCJ02Transformer,
  MatchUtil
}

class st_BD09ToGCJ02 extends AbstractUdf {
  override def name(): String = "st_BD09ToGCJ02"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  def evaluate(st: Geometry): Geometry = {
    if (st == null) return null
    val transformer = new BD09ToGCJ02Transformer
    MatchUtil.MatchCoordinate(transformer, st)
  }

  def evaluate(st: Trajectory): Trajectory = {
    if (st == null) return null
    val transformer = new BD09ToGCJ02Transformer
    transformer.trajectoryTransform(st)
  }

  def evaluate(st: RoadNetwork): RoadNetwork = {
    if (st == null) return null
    val transformer = new BD09ToGCJ02Transformer
    transformer.roadNetworkTransform(st)
  }

  def evaluate(st: RoadSegment): RoadSegment = {
    if (st == null) return null
    val transformer = new BD09ToGCJ02Transformer
    transformer.roadSegmentTransform(st)
  }

  def udfSparkEntries: List[String] =
    List("udfWrapper", "udfWrapper2", "udfWrapper3", "udfWrapper4")

  def udfWrapper: Geometry => Geometry = evaluate
  def udfWrapper2: Trajectory => Trajectory = evaluate
  def udfWrapper3: RoadNetwork => RoadNetwork = evaluate
  def udfWrapper4: RoadSegment => RoadSegment = evaluate
}
