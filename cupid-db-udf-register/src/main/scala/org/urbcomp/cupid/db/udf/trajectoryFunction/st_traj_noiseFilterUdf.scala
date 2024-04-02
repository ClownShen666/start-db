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

import org.apache.flink.table.annotation.DataTypeHint
import org.urbcomp.cupid.db.model.point.GPSPoint
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.util.GeoFunctions

import java.math.BigDecimal
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.collection.convert.ImplicitConversions.`list asScalaBuffer`
import scala.collection.mutable.ListBuffer

class st_traj_noiseFilterUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_traj_noiseFilter"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Trajectory])
  def eval(@DataTypeHint(value = "RAW", bridgedTo = classOf[Trajectory]) trajectory: Trajectory, speedLimitInMPerS: BigDecimal): Trajectory = {
    if (trajectory == null || speedLimitInMPerS == null) return null
    val gpsPoints = trajectory.getGPSPointList
    if (gpsPoints.length <= 1) {
      trajectory.setTid(trajectory.getTid + "_filterNoise")
      return trajectory
    }

    //Filter noise
    val filterGPSPoints = new ListBuffer[GPSPoint]
    //We assume the first point is always correct
    var pre = gpsPoints(0)
    filterGPSPoints += pre

    var i = 1
    while (i < gpsPoints.length) {
      val cur = gpsPoints(i)
      if (cur.getTime.after(pre.getTime)) {
        val distanceInM = GeoFunctions.getDistanceInM(cur, pre)
        val timeSpanInS = (cur.getTime.getTime - pre.getTime.getTime) / 1000.0
        if (distanceInM / timeSpanInS <= speedLimitInMPerS.doubleValue) {
          filterGPSPoints += cur
          pre = cur
        }
      }
      i += 1
    }

    trajectory.setTid(trajectory.getTid + "_filterNoise")
    trajectory.setPointList(filterGPSPoints.toList.asJava)
    trajectory
  }

  def udfSparkEntries: List[String] = List("udfWrapper1")

  def udfWrapper1: (Trajectory, BigDecimal) => Trajectory = eval
}
