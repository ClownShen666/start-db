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
package org.urbcomp.cupid.db.udf.geometricrelationfunction

import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.prep.PreparedGeometry
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

import java.util.Optional

class St_touchesUdf extends AbstractUdf {
  override def name(): String = "st_touches"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  def evaluate(geom1: Geometry, geom2: Geometry): java.lang.Boolean = {
    if (geom1 == null || geom2 == null) null
    else {
      val preparedGeom1 = prepareGeometry(geom1)
      preparedGeom1 match {
        case Some(s) => s.touches(geom2)
        case _       => geom1.touches(geom2)

      }
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (Geometry, Geometry) => java.lang.Boolean = evaluate

}
