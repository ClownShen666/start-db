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

import org.locationtech.jts.geom.{Coordinate, LineString}
import org.locationtech.spatial4j.context.jts.JtsSpatialContext
import org.locationtech.spatial4j.distance.{DistanceCalculator, DistanceUtils}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

class St_lengthSphereUdf extends AbstractUdf {

  override def name(): String = "st_lengthSphere"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  def evaluate(geom: LineString): java.lang.Double = {
    if(geom == null) null
    else {
      var sum = 0.0
      val ca = ThreadLocal.withInitial(() => JtsSpatialContext.GEO.getDistCalc()).get()
      val cs = geom.getCoordinates()
      for (i <- 1 until cs.length) {
        val startPoint = JtsSpatialContext.GEO.getShapeFactory.pointXY(cs(i - 1).x, cs(i - 1).y)
        sum += ca.distance(startPoint, cs(i).x, cs(i).y)
      }
      sum * DistanceUtils.DEG_TO_KM * 1000
    }
  }

}
