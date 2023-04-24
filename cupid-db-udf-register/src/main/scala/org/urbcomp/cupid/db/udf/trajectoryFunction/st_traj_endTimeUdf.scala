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
package org.urbcomp.cupid.db.udf.trajectoryFunction

import org.urbcomp.cupid.db.model.point.GPSPoint
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.util.GeoFunctions

import java.math.BigDecimal
import java.sql.Timestamp
import scala.collection.JavaConverters.seqAsJavaListConverter
import scala.collection.convert.ImplicitConversions.`list asScalaBuffer`
import scala.collection.mutable.ListBuffer

class st_traj_endTimeUdf extends AbstractUdf {

  override def name(): String = "st_traj_endTime"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  def evaluate(trajectory: Trajectory): Timestamp = {
    if (trajectory == null) null
    else trajectory.getEndTime
  }
  def udfSparkEntries: List[String] = List("udfWrapper1")

  def udfWrapper1: (Trajectory) => Timestamp = evaluate
}
