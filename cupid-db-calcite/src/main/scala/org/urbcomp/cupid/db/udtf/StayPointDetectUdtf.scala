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
import org.apache.hadoop.hive.ql.exec.UDFArgumentException
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory
import org.apache.hadoop.hive.serde2.objectinspector.{
  ObjectInspector,
  ObjectInspectorFactory,
  StructObjectInspector
}
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.apache.spark.sql.catalyst.expressions.GenericInternalRow
import org.locationtech.jts.geom.MultiPoint
import org.urbcomp.cupid.db.model.point.SpatialPoint
import org.urbcomp.cupid.db.udf.DataEngine
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.util.GeometryFactoryUtils
import org.urbcomp.cupid.serializer.serializers.TrajectoryBS

import java.io.ByteArrayInputStream
import java.util
import scala.collection.JavaConverters._
import scala.collection.convert.ImplicitConversions._

class StayPointDetectUdtf extends AbstractUdtf with Serializable {
  override def name(): String = "st_traj_stayPointDetect"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  override def inputColumnsCount: Int = 3

  override def outputColumns(): List[(String, SqlTypeName)] = List(
    ("startTime", SqlTypeName.TIMESTAMP),
    ("endTime", SqlTypeName.TIMESTAMP),
    ("gpsPoints", SqlTypeName.MULTIPOINT)
  )

  override def initialize(argOIs: Array[ObjectInspector]): StructObjectInspector = {
    //判断传入的参数个数
    if (argOIs.length != inputColumnsCount) {
      throw new UDFArgumentException("有且只能有" + inputColumnsCount + "个参数")
    }
    val fieldNames = outputColumns().map(_._1).asJava
    val fieldOIs = new util.ArrayList[ObjectInspector]
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaTimestampObjectInspector)
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaTimestampObjectInspector)
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
    val d = objects(1).toString.toDouble
    val t = objects(2).toString.toDouble
    val detector = new org.urbcomp.cupid.db.algorithm.staypointdetect.StayPointDetect()
    val result = detector.detect(trajectory, d, t)

    result
      .map(x => {
        val multipoint = new MultiPoint(
          x.getMultiPoint.map(p => p.asInstanceOf[SpatialPoint]).toArray,
          GeometryFactoryUtils.defaultGeometryFactory
        )
        val data = multipoint.toString
        Array[AnyRef](
          x.getStarTime.asInstanceOf[AnyRef],
          x.getEndTime.asInstanceOf[AnyRef],
          data.asInstanceOf[AnyRef]
        )
      })
      .toArray
  }
}
