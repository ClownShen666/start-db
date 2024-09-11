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
package org.urbcomp.start.db.udf.roadfunction

import org.apache.flink.table.annotation.DataTypeHint
import org.locationtech.jts.geom.{Point, Polygon}
import org.urbcomp.start.db.algorithm.reachable.ReachableAreaConcaveHull
import org.urbcomp.start.db.model.point.SpatialPoint
import org.urbcomp.start.db.model.roadnetwork.RoadNetwork
import org.urbcomp.start.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.start.db.udf.{AbstractUdf, DataEngine}

import java.math.BigDecimal

class st_rn_reachableConcaveHullUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_rn_reachableConcaveHull"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Polygon])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[RoadNetwork]) roadNetwork: RoadNetwork,
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Point]) startPt: Point,
      @DataTypeHint(value = "RAW", bridgedTo = classOf[BigDecimal]) timeInSec: BigDecimal,
      travelMode: String
  ): Polygon = {
    if (roadNetwork == null || startPt == null || timeInSec == null || travelMode == null) null
    else {
      val startSpatialPoint = new SpatialPoint(startPt.getCoordinate)
      val reachable = new ReachableAreaConcaveHull(
        roadNetwork,
        startSpatialPoint,
        timeInSec.doubleValue,
        travelMode
      )
      reachable.getHull
    }
  }

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Polygon])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[RoadNetwork]) roadNetwork: RoadNetwork,
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Point]) startPt: Point,
      timeInSec: Double,
      travelMode: String
  ): Polygon = {
    if (roadNetwork == null || startPt == null || timeInSec == null || travelMode == null) null
    else {
      val startSpatialPoint = new SpatialPoint(startPt.getCoordinate)
      val reachable = new ReachableAreaConcaveHull(
        roadNetwork,
        startSpatialPoint,
        timeInSec.doubleValue,
        travelMode
      )
      reachable.getHull
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (RoadNetwork, Point, BigDecimal, String) => Polygon = eval

}
