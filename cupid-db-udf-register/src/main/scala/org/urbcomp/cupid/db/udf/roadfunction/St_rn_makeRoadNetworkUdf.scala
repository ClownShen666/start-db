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
package org.urbcomp.cupid.db.udf.roadfunction

import com.fasterxml.jackson.core.JsonProcessingException
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

import java.util
import java.util.List
import scala.collection.immutable.List

class St_rn_makeRoadNetworkUdf extends AbstractUdf {

  override def name(): String = "st_rn_fromGeoJson"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)
  @throws[JsonProcessingException]
  def evaluate(rsList: util.List[RoadSegment]): RoadNetwork = new RoadNetwork(rsList)

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (util.List[RoadSegment]) => RoadNetwork = evaluate

}
