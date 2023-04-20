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

import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.types._
import org.apache.spark.unsafe.types.UTF8String
import org.urbcomp.cupid.db.model.roadnetwork.RoadNetwork

abstract class RoadNetworkUDT extends UserDefinedType[RoadNetwork] {
  override def typeName: String = "RoadNetwork"

  override def serialize(roadNetwork: RoadNetwork): InternalRow

  override def deserialize(datum: Any): RoadNetwork

  override def sqlType: DataType = StructType(Seq(StructField("roadNetwork", DataTypes.StringType)))

  override def userClass: Class[RoadNetwork] = classOf[RoadNetwork]
}
