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
import org.apache.spark.sql.catalyst.expressions.GenericInternalRow
import org.apache.spark.sql.jts.PointUDT
import org.locationtech.jts.geom.Point
import org.urbcomp.cupid.db.model.point.SpatialPoint

import java.util
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`

class KMeansClusteringUdtf extends AbstractUdtf with Serializable {
  override def name(): String = "st_kmeans_clustering"

  override def inputColumnsCount: Int = 2

  override def outputColumns(): List[(String, SqlTypeName)] = List(
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
      if (data.isInstanceOf[Point]) {
        listPoint.add(new SpatialPoint(data.asInstanceOf[Point].getCoordinate))
      } else if (data.isInstanceOf[util.ArrayList[Byte]]) {
        val temp = PointUDT.deserialize(
          new GenericInternalRow(
            data.asInstanceOf[util.ArrayList[Byte]].toArray.asInstanceOf[Array[Any]]
          )
        )
        listPoint.add(new SpatialPoint(temp.getCoordinate))
      } else throw new Exception("Unrecognizable data " + data)
    }
    val k = objects(1).toString.toInt
    val clusterBean = new org.urbcomp.cupid.db.algorithm.clustering.KMeansClustering(listPoint, k)
    val result = clusterBean.cluster()
    result
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
