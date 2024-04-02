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
package org.urbcomp.cupid.db.udf.geometricoperationfunction

import org.apache.flink.table.annotation.DataTypeHint
import org.geotools.referencing.GeodeticCalculator
import org.locationtech.jts.geom.{Coordinate, Geometry}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction

class st_distanceSpheroidUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_distanceSpheroid"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry]) geom1: Geometry,
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry]) geom2: Geometry
  ): java.lang.Double = {
    if (geom1 == null || geom2 == null) null
    else {
      val c1: Coordinate = geom1.getCoordinate
      val c2: Coordinate = geom2.getCoordinate
      val gc: GeodeticCalculator = ThreadLocal.withInitial(() => new GeodeticCalculator()).get()
      gc.setStartingGeographicPoint(c1.x, c1.y)
      gc.setDestinationGeographicPoint(c2.x, c2.y)
      gc.getOrthodromicDistance()
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (Geometry, Geometry) => java.lang.Double = eval
}
