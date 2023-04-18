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
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

class St_containsUdf extends AbstractUdf {
  override def name(): String = "st_contains"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  /**
    * Geometry A contains Geometry B if no points of B lie in the exterior of A,
    * and at least one point of the interior of B lies in the interior of A.
    * The difference between contain and cover, e.g., when a line is
    * on the edge of a polygon, contain return false but cover return true
    */
  def evaluate(geom1: Geometry, geom2: Geometry): java.lang.Boolean = {
    if (geom1 == null || geom2 == null) null
    else {
      val preparedGeom1 = prepareGeometry(geom1)
      preparedGeom1
        .map(preparedGeometry => preparedGeometry.contains(geom2))
        .getOrElse(geom1.contains(geom2))
    }
  }

}
