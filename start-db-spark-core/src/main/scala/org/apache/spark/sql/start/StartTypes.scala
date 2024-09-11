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

import org.urbcomp.start.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.start.db.model.trajectory.Trajectory
import org.urbcomp.start.serializer.serializers.{RoadNetworkBS, RoadSegmentBS, TrajectoryBS}

object StartTypes {
  val trajectorySerializer = new TrajectoryBS
  val roadNetworkSerializer = new RoadNetworkBS
  val roadSegmentSerializer = new RoadSegmentBS
}

private[spark] class TrajectoryUDT
    extends AbstractStartUDT[Trajectory]("trajectory", StartTypes.trajectorySerializer)
object TrajectoryUDT extends TrajectoryUDT

private[spark] class RoadSegmentUDT
    extends AbstractStartUDT[RoadSegment]("roadSegment", StartTypes.roadSegmentSerializer)
object RoadSegmentUDT extends RoadSegmentUDT

private[spark] class RoadNetworkUDT
    extends AbstractStartUDT[RoadNetwork]("roadNetwork", StartTypes.roadNetworkSerializer)
object RoadNetworkUDT extends RoadNetworkUDT
