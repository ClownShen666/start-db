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
import org.apache.hadoop.hive.serde2.objectinspector.{
  ObjectInspector,
  ObjectInspectorFactory,
  StructObjectInspector
}
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory
import org.apache.spark.sql.catalyst.expressions.GenericInternalRow
import org.apache.spark.sql.jts.PointUDT
import org.locationtech.jts.geom.Point
import org.urbcomp.start.db.algorithm.clustering
import org.urbcomp.start.db.model.point.SpatialPoint
import org.urbcomp.start.db.udf.DataEngine
import org.urbcomp.start.db.udf.DataEngine.{Calcite, Spark}

import java.util
import scala.collection.JavaConverters._
import scala.collection.convert.ImplicitConversions._

class DBSCANClusteringUdtf extends AbstractUdtf with Serializable {

  override def name(): String = "st_dbscan_clustering"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  override def inputColumnsCount: Int = 3

  override def outputColumns(): List[(String, SqlTypeName)] =
    List(
      ("cluster", SqlTypeName.MULTIPOINT),
      ("clusterCentroid", SqlTypeName.POINT),
      ("clusterBoundary", SqlTypeName.GEOMETRY)
    )

  override def initialize(argOIs: Array[ObjectInspector]): StructObjectInspector = {
    //判断传入的参数个数
    if (argOIs.length != inputColumnsCount) {
      throw new UDFArgumentException("有且只能有" + inputColumnsCount + "个参数")
    }

    val fieldNames = outputColumns().map(_._1).asJava
    val fieldOIs = new util.ArrayList[ObjectInspector]
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector)
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector)
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector)
    ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs)
  }

  override def udtfImpl(objects: Seq[AnyRef]): Array[Array[AnyRef]] = {
    val listPoint = new util.ArrayList[SpatialPoint]()
    for (data <- objects.head.asInstanceOf[util.List[Object]]) {
      data match {
        case point: Point =>
          listPoint.add(new SpatialPoint(point.getCoordinate))
        case bytes: util.ArrayList[Byte] =>
          val temp =
            PointUDT.deserialize(new GenericInternalRow(bytes.toArray.asInstanceOf[Array[Any]]))
          listPoint.add(new SpatialPoint(temp.getCoordinate))
        case _ => throw new Exception("Unrecognizable data " + data)
      }
    }
    val distanceInM = objects(1).toString.toDouble
    val minPoints = objects(2).toString.toInt
    val dbscan = new clustering.DBSCANClustering(listPoint, distanceInM, minPoints)
    val res = dbscan.cluster()
    res
      .map(s => {
        val data = s.toString
        Array[AnyRef](
          data.asInstanceOf[AnyRef],
          s.getCentroid.toString.asInstanceOf[AnyRef],
          s.getBoundary.toString.asInstanceOf[AnyRef]
        )
      })
      .toArray
  }

}
