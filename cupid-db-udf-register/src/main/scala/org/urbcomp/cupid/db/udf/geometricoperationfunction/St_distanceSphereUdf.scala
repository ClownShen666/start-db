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

import org.locationtech.jts.geom.{Coordinate, Geometry, GeometryFactory, Point}
import org.locationtech.spatial4j.context.jts.JtsSpatialContext
import org.locationtech.spatial4j.distance.{DistanceCalculator, DistanceUtils}
import org.locationtech.spatial4j.shape.jts.JtsGeometry
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}

import scala.util.Try

class St_distanceSphereUdf extends AbstractUdf {

  override def name(): String = "st_distanceSphere"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  def evaluate(geom1: Geometry, geom2: Geometry): java.lang.Double = {
    if (geom1 == null || geom2 == null) null
    else {
      val c1: Coordinate = geom1.getCoordinate()
      val c2: Coordinate = geom2.getCoordinate()
      val ca: DistanceCalculator =
        ThreadLocal.withInitial(() => JtsSpatialContext.GEO.getDistCalc).get()
      val jtsGeom1: JtsGeometry = new JtsGeometry(geom1, JtsSpatialContext.GEO, false, false)
      val jtsGeom2: JtsGeometry = new JtsGeometry(geom2, JtsSpatialContext.GEO, false, false)
      val startPoint1: org.locationtech.spatial4j.shape.Point =
        JtsSpatialContext.GEO.getShapeFactory.pointXY(c1.x, c1.y)
      val startPoint2: org.locationtech.spatial4j.shape.Point =
        JtsSpatialContext.GEO.getShapeFactory.pointXY(c2.x, c2.y)
      DistanceUtils.DEG_TO_KM * ca.distance(startPoint1, startPoint2) * 1000
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (Geometry, Geometry) => java.lang.Double = evaluate
}
