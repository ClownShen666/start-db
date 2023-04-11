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
package org.urbcomp.cupid.db.udf.coordtransformfunction.coordtransform

import org.locationtech.jts.geom.{
  Geometry,
  GeometryCollection,
  LineString,
  MultiLineString,
  MultiPoint,
  MultiPolygon,
  Point,
  Polygon
}

object MatchUtil {
  def MatchCoordinate(transformer: AbstractCoordTransformer, st: Geometry): Geometry = {
    st match {
      case res: Point      => transformer.pointTransform(res)
      case res: LineString => transformer.lineStringTransform(res)
      case res: Polygon    => transformer.polygonTransform(res)
      case res: MultiPoint => transformer.multiPointTransform(res)
      case res: MultiLineString =>
        transformer.multiLineStringTransform(res)
      case res: MultiPolygon => transformer.multiPolygonTransform(res)
      case res: Geometry     => transformer.geometryTransform(res)
      case res: GeometryCollection =>
        transformer.geometryCollectionTransform(res)
      case _ => throw new IllegalStateException("This parameter is not supported ")
    }
  }

}
