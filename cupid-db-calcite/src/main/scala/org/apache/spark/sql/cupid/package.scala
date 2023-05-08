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
package org.apache.spark.sql

import org.apache.spark.sql.cupid.model.AbstractCupidUDT
import org.apache.spark.sql.types.{UDTRegistration, UserDefinedType}
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.model.trajectory.Trajectory

package object model {
  def registerTypes(): Unit = registration

  val typeMap: Map[Class[_], Class[_ <: UserDefinedType[_]]] = Map(
    classOf[Trajectory] -> classOf[TrajectoryUDT],
    classOf[RoadNetwork] -> classOf[RoadNetworkUDT],
    classOf[RoadSegment] -> classOf[RoadSegmentUDT]
  )

  /** Trick to defer initialization until `registerUDTs` is called,
    * and ensure its only called once per ClassLoader.
    */
  private[model] lazy val registration: Unit = typeMap.foreach {
    case (l, r) => UDTRegistration.register(l.getCanonicalName, r.getCanonicalName)
  }
}

private[spark] class TrajectoryUDT extends AbstractCupidUDT[Trajectory]("Trajectory", "fromGeoJSON")
object TrajectoryUDT extends TrajectoryUDT

private[spark] class RoadNetworkUDT
    extends AbstractCupidUDT[RoadNetwork]("RoadNetwork", "fromGeoJSON")
object RoadNetworkUDT extends RoadNetworkUDT

private[spark] class RoadSegmentUDT
    extends AbstractCupidUDT[RoadSegment]("RoadSegment", "fromGeoJSON")
object RoadSegmentUDT extends RoadSegmentUDT
