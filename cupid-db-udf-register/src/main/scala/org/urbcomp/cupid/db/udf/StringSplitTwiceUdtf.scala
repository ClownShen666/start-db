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
package org.urbcomp.cupid.db.udf

import org.apache.calcite.sql.`type`.SqlTypeName
import org.apache.hadoop.hive.ql.exec.UDFArgumentException
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory
import org.apache.hadoop.hive.serde2.objectinspector.{
  ObjectInspector,
  ObjectInspectorFactory,
  StructObjectInspector
}

import java.util
import scala.collection.JavaConverters._

class StringSplitTwiceUdtf extends AbstractUdtf with Serializable {

  override def name(): String = "StringSplitTwice"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  override def inputColumnsCount: Int = 2
  override def outputColumns(): List[(String, SqlTypeName)] =
    List(("Col1", SqlTypeName.VARCHAR), ("Col2", SqlTypeName.VARCHAR))

  override def initialize(argOIs: Array[ObjectInspector]): StructObjectInspector = {
    //判断传入的参数是否只有一个
    if (argOIs.length != inputColumnsCount) {
      throw new UDFArgumentException("有且只能有" + inputColumnsCount + "个参数")
    }
    //判断参数类型
    if (argOIs(0).getCategory != ObjectInspector.Category.PRIMITIVE) {
      throw new UDFArgumentException("参数类型不匹配")
    }
    val fieldNames = outputColumns().map(_._1).asJava
    val fieldOIs = new util.ArrayList[ObjectInspector]
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector)
    fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector)
    ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs)
  }

  override def udtfImpl(objects: Seq[AnyRef]): Array[Array[AnyRef]] = {
    val delimiter = objects.head.toString
    val input = objects(1).toString
    if (input.isEmpty) new Array[Array[AnyRef]](0)
    else {
      val test = input.split(delimiter)
      val ret = new Array[Array[AnyRef]](test.length)
      var idx = 0
      for (i <- 0 until test.length) {
        val result = test(i).split(":")
        ret(idx) = result.asInstanceOf[Array[AnyRef]]
        idx += 1
      }
      ret
    }
  }
}
