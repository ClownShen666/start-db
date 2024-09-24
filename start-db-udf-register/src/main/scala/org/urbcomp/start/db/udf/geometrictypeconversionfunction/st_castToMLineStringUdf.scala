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
package org.urbcomp.start.db.udf.geometrictypeconversionfunction

import org.apache.flink.table.annotation.DataTypeHint
import org.locationtech.jts.geom.{Geometry, MultiLineString}
import org.urbcomp.start.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.start.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction

class st_castToMLineStringUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_castToMLineString"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[MultiLineString])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry]) geom: Geometry
  ): MultiLineString = {
    if (geom == null) null
    else
      geom match {
        case string: MultiLineString => string
        case _                       => null
      }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: Geometry => MultiLineString = eval

}
