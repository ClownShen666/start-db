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
package org.urbcomp.start.db.udtf

import org.apache.calcite.sql.`type`.SqlTypeName
import org.apache.hadoop.hive.ql.exec.UDFArgumentException
import org.urbcomp.start.db.udf.DataEngine.{Calcite, Spark}
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory
import org.apache.hadoop.hive.serde2.objectinspector.{
  ObjectInspector,
  ObjectInspectorFactory,
  StructObjectInspector
}
import org.urbcomp.start.db.algorithm.trajectorysegment.TimeIntervalSegment
import org.urbcomp.start.db.model.trajectory.Trajectory
import org.urbcomp.start.db.udf.DataEngine
import org.urbcomp.start.serializer.serializers.TrajectoryBS

import java.io.ByteArrayInputStream
import scala.collection.JavaConverters._
import java.util

class st_traj_timeIntervalSegmentUdf extends AbstractUdtf with Serializable {

  override def name(): String = "st_traj_timeIntervalSegment"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  override def inputColumnsCount: Int = 2

  override def outputColumns(): List[(String, SqlTypeName)] =
    List(("SubTrajectory", SqlTypeName.TRAJECTORY))

  override def initialize(argOIs: Array[ObjectInspector]): StructObjectInspector = {
    //判断传入的参数个数
    if (argOIs.length != inputColumnsCount) {
      throw new UDFArgumentException("有且只能有" + inputColumnsCount + "个参数")
    }

    val fieldNames = outputColumns().map(_._1).asJava
    val fieldOIs = new util.ArrayList[ObjectInspector]
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector)
    ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs)
  }

  override def udtfImpl(objects: Seq[AnyRef]): Array[Array[AnyRef]] = {
    val trajectory = objects.head match {
      case traj: Trajectory => traj
      case list: java.util.ArrayList[Array[Byte]] =>
        val buf = list.get(0)
        new TrajectoryBS().read(new ByteArrayInputStream(buf), classOf[Trajectory])
      case data: Any => throw new Exception("Unrecognizable data " + data)
    }
    val maxTimeIntervalInSec = objects(1).toString.toDouble
    val trajectorySegment = new TimeIntervalSegment(maxTimeIntervalInSec)
    val subTrajectoryList = trajectorySegment.segment(trajectory).toArray()
    val res = new Array[Array[AnyRef]](subTrajectoryList.length)
    var idx = 0
    for (subTrajectory <- subTrajectoryList) {
      val tmp = new Array[AnyRef](1)
      tmp(0) = subTrajectory
      res(idx) = tmp
      idx += 1
    }
    res
  }
}
