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
package org.apache.spark.sql.start

import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.GenericInternalRow
import org.apache.spark.sql.types._
import org.urbcomp.start.serializer.StartBinarySerializer

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import scala.reflect.{ClassTag, classTag}

abstract class AbstractStartUDT[T >: Null: ClassTag](
    override val simpleString: String,
    serializer: StartBinarySerializer[T]
) extends UserDefinedType[T] {

  override def serialize(obj: T): InternalRow = {
    val os = new ByteArrayOutputStream();
    serializer.write(os, obj)
    new GenericInternalRow(Array[Any](os.toByteArray))
  }

  override def sqlType: DataType = StructType(Seq(StructField("start", DataTypes.BinaryType)))

  override def userClass: Class[T] = classTag[T].runtimeClass.asInstanceOf[Class[T]]

  override def deserialize(datum: Any): T = {
    val ir = datum.asInstanceOf[InternalRow]
    serializer.read(new ByteArrayInputStream(ir.getBinary(0)), userClass)
  }
}
