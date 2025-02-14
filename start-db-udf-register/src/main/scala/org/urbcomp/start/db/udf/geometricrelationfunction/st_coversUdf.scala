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
package org.urbcomp.start.db.udf.geometricrelationfunction

import org.apache.flink.table.annotation.DataTypeHint
import org.locationtech.jts.geom.Geometry
import org.urbcomp.start.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.start.db.udf.{AbstractUdf, DataEngine}

class st_coversUdf extends ScalarFunction with AbstractUdf {
  override def name(): String = "st_covers"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry]) geom1: Geometry,
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry]) geom2: Geometry
  ): java.lang.Boolean = {
    if (geom1 == null || geom2 == null) null
    else {
      val preparedGeom1 = prepareGeometry(geom1)
      preparedGeom1 match {
        case Some(s) => s.covers(geom2)
        case _       => geom1.covers(geom2)

      }
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (Geometry, Geometry) => java.lang.Boolean = eval

}
