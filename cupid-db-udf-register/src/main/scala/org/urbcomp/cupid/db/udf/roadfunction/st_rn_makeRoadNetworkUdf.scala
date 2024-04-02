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
import org.apache.flink.table.annotation.DataTypeHint
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.Spark

import scala.collection.JavaConverters.seqAsJavaListConverter

class st_rn_makeRoadNetworkUdf extends AbstractUdf {

  override def name(): String = "st_rn_makeRoadNetwork"

  override def registerEngines(): List[DataEngine.Value] = List(Spark)
  @throws[JsonProcessingException]
  @DataTypeHint(value = "RAW", bridgedTo = classOf[RoadNetwork])
  def eval(@DataTypeHint(value = "RAW", bridgedTo = classOf[Seq[RoadSegment]]) rsList: Seq[RoadSegment]): RoadNetwork = {
    if (rsList == null) null
    else new RoadNetwork(rsList.toList.asJava)
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: Seq[RoadSegment] => RoadNetwork = eval
}
