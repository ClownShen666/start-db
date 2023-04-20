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
package org.apache.spark.sql.cupid.model.roadnetwork

import org.apache.hadoop.hive.ql.exec.spark.KryoSerializer
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.GenericInternalRow
import org.apache.spark.sql.types._
import org.urbcomp.cupid.db.model.roadnetwork.RoadSegment

class RoadSegmentUDT extends UserDefinedType[RoadSegment] {
  override def typeName: String = "RoadSegment"

  override def serialize(roadSegment: RoadSegment): InternalRow = {
    new GenericInternalRow(Array[Any](KryoSerializer.serialize(roadSegment.toString)))
  }

  override def deserialize(datum: Any): RoadSegment = {
    val ir = datum.asInstanceOf[InternalRow]
    val roadSegmentString = KryoSerializer.deserialize(ir.getBinary(0), classOf[String])
    RoadSegment.fromGeoJSON(roadSegmentString)
  }

  override def sqlType: DataType =
    StructType(Seq(StructField("RoadSegmentBinaryData", DataTypes.BinaryType)))

  override def userClass: Class[RoadSegment] = classOf[RoadSegment]
}
