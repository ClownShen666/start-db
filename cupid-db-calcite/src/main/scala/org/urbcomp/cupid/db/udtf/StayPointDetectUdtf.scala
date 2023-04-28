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
package org.urbcomp.cupid.db.udtf

import org.apache.calcite.sql.`type`.SqlTypeName
import org.urbcomp.cupid.db.algorithm.staypointdetect.StayPointDetectResult
import org.urbcomp.cupid.db.model.point.GPSPoint
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.udf.DataEngine
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.util.GeoFunctions

import scala.collection.JavaConverters.mutableSeqAsJavaListConverter
import scala.collection.convert.ImplicitConversions.`collection asJava`
import scala.collection.mutable

class StayPointDetectUdtf extends AbstractUdtf with Serializable {
  override def name(): String = "st_traj_stayPointDetect"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  override def inputColumnsCount: Int = 3

  override def outputColumns(): List[(String, SqlTypeName)] = List(
    ("startTime", SqlTypeName.TIMESTAMP),
    ("endTime", SqlTypeName.TIMESTAMP),
    ("gpsPoints", SqlTypeName.MULTIPOINT)
  )

  override def udtfImpl(objects: Seq[AnyRef]): Array[Array[AnyRef]] = {
    val trajectory = objects.head.asInstanceOf[Trajectory]
    val d = objects(1).asInstanceOf[Double]
    val t = objects(2).asInstanceOf[Double]
    val stayPoints = mutable.MutableList.empty[Array[AnyRef]]
    val list = trajectory.getGPSPointList
    val trLen = list.size
    var start = 0
    var end = 0
    var newSpFlag = true
    var i = 0
    while (i < trLen - 1) {
      var j = i + 1
      // find the first point that does not satisfy the conditions
      while (j < trLen && GeoFunctions.getDistanceInM(list.get(i), list.get(j)) <= d) j += 1
      if (j > i + 1 && (list
            .get(j - 1)
            .getTime
            .getTime - list.get(i).getTime.getTime) / 1000.0 >= t) {
        if (newSpFlag) {
          start = i
          newSpFlag = false
        }
        end = j - 1
      }
      i += 1
      // form a new stay point
      if (!newSpFlag && i > end) {
        val res = new StayPointDetectResult
        res.setStarTime(list.get(start).getTime)
        res.setEndTime(list.get(end).getTime)
        val temp = mutable.MutableList.empty[GPSPoint]
        for (k <- start to end) {
          temp.add(list.get(k))
        }
        res.setMultiPoint(temp.asJava)
        val ir = new Array[AnyRef](1)
        ir(0) = res
        stayPoints.add(ir)
        newSpFlag = true
      }
    }
    stayPoints.toArray
  }
}
