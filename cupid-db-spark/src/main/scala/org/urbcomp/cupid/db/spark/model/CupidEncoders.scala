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
package org.urbcomp.cupid.db.spark.model

import org.apache.spark.sql.Encoder
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.model.trajectory.Trajectory

/** Encoders are Spark SQL's mechanism for converting a JVM type into a Catalyst representation.
  * They are fetched from implicit scope whenever types move beween RDDs and Datasets. Because each
  * of the types supported below has a corresponding UDT, we are able to use a standard Spark Encoder
  * to construct these implicits. */
trait CupidEncoders {
  implicit def trajectoryEncoder: Encoder[Trajectory] = ExpressionEncoder()
  implicit def roadNetworkEncoder: Encoder[RoadNetwork] = ExpressionEncoder()
  implicit def roadSegmentEncoder: Encoder[RoadSegment] = ExpressionEncoder()
}
