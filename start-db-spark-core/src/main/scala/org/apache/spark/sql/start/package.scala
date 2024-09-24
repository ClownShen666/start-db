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

import org.apache.spark.sql.types.UDTRegistration
import org.urbcomp.start.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.start.db.model.trajectory.Trajectory

package object start {

  /**
    * This must be called before any JTS types are used.
    */
  def registerTypes(): Unit = registration

  /** Trick to defer initialization until `registerUDTs` is called,
    * and ensure its only called once per ClassLoader.
    */
  private[start] lazy val registration: Unit = {
    UDTRegistration.register(
      classOf[Trajectory].getCanonicalName,
      classOf[TrajectoryUDT].getCanonicalName
    )
    UDTRegistration.register(
      classOf[RoadSegment].getCanonicalName,
      classOf[RoadSegmentUDT].getCanonicalName
    )
    UDTRegistration.register(
      classOf[RoadNetwork].getCanonicalName,
      classOf[RoadNetworkUDT].getCanonicalName
    )
  }

}
