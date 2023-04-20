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
package org.apache.spark.sql.cupid.model

import org.apache.hadoop.hive.ql.exec.spark.KryoSerializer
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.GenericInternalRow
import org.apache.spark.sql.types._

import java.lang.reflect.Method
import scala.reflect.{ClassTag, classTag}

abstract class AbstractCupidUDT[T >: Null: ClassTag](
    override val simpleString: String,
    val stringConstructorCallback: String
) extends UserDefinedType[T] {
  override def typeName: String = simpleString

  override def serialize(obj: T): InternalRow = {
    new GenericInternalRow(Array[Any](KryoSerializer.serialize(obj.toString)))
  }

  override def deserialize(datum: Any): T = {
    val ir = datum.asInstanceOf[InternalRow]
    val objString: String = KryoSerializer.deserialize(ir.getBinary(0), classOf[String])
    val method: Method =
      classTag[T].runtimeClass.getDeclaredMethod(stringConstructorCallback, classOf[String])
    method.invoke(null, objString).asInstanceOf[T]
  }

  override def sqlType: DataType =
    StructType(Seq(StructField(simpleString + "BinaryData", DataTypes.BinaryType)))

  override def userClass: Class[T] = classTag[T].runtimeClass.asInstanceOf[Class[T]]
}
