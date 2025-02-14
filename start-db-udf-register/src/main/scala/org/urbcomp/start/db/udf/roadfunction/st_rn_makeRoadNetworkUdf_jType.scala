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
package org.urbcomp.start.db.udf.roadfunction

import com.fasterxml.jackson.core.JsonProcessingException
import org.apache.flink.table.annotation.DataTypeHint
import org.urbcomp.start.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.start.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.start.db.udf.DataEngine.Calcite

class st_rn_makeRoadNetworkUdf_jType extends AbstractUdf {

  override def name(): String = "st_rn_makeRoadNetwork"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite)
  @throws[JsonProcessingException]
  @DataTypeHint(value = "RAW", bridgedTo = classOf[RoadNetwork])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[java.util.List[RoadSegment]]) rsList: java.util.List[
        RoadSegment
      ]
  ): RoadNetwork = {
    if (rsList == null) null
    else new RoadNetwork(rsList)
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: java.util.List[RoadSegment] => RoadNetwork = eval
}
