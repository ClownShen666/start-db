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

import org.apache.flink.table.annotation.DataTypeHint
import org.locationtech.jts.geom.Geometry
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.cupid.db.udf.coordtransformfunction.coordtransform.{
  GCJ02ToBD09Transformer,
  MatchUtil
}

class st_GCJ02ToBD09 extends ScalarFunction with AbstractUdf {
  override def name(): String = "st_GCJ02ToBD09"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  lazy val transformer = new GCJ02ToBD09Transformer

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry])
  def eval(@DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry]) st: Geometry): Geometry = {
    if (st == null) return null
    MatchUtil.MatchCoordinate(new GCJ02ToBD09Transformer, st)
  }

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Trajectory])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Trajectory]) st: Trajectory
  ): Trajectory = {
    if (st == null) return null
    transformer.trajectoryTransform(st)
  }

  @DataTypeHint(value = "RAW", bridgedTo = classOf[RoadNetwork])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[RoadNetwork]) st: RoadNetwork
  ): RoadNetwork = {
    if (st == null) return null
    transformer.roadNetworkTransform(st)
  }

  @DataTypeHint(value = "RAW", bridgedTo = classOf[RoadSegment])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[RoadSegment]) st: RoadSegment
  ): RoadSegment = {
    if (st == null) return null
    transformer.roadSegmentTransform(st)
  }

  def udfSparkEntries: List[String] =
    List("udfWrapper", "udfWrapper2", "udfWrapper3", "udfWrapper4")

  def udfWrapper: Geometry => Geometry = eval
  def udfWrapper2: Trajectory => Trajectory = eval
  def udfWrapper3: RoadNetwork => RoadNetwork = eval
  def udfWrapper4: RoadSegment => RoadSegment = eval
}
