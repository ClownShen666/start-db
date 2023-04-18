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
package org.urbcomp.cupid.db.udf

import org.locationtech.jts.geom.prep.{
  PreparedGeometry,
  PreparedLineString,
  PreparedPoint,
  PreparedPolygon
}
import org.locationtech.jts.geom.{Geometry, Lineal, Polygonal, Puntal}

package object geometricrelationfunction {

  def prepareGeometry(geom: Geometry): Option[PreparedGeometry] = {
    if (geom == null) None
    else {
      geom.getGeometryType match {
        case Geometry.TYPENAME_MULTIPOINT =>
          Some(new PreparedPoint(geom.asInstanceOf[Puntal]))
        case Geometry.TYPENAME_LINESTRING =>
          Some(new PreparedLineString(geom.asInstanceOf[Lineal]))
        case Geometry.TYPENAME_POLYGON =>
          Some(new PreparedPolygon(geom.asInstanceOf[Polygonal]))
        case _ =>
          None
      }
    }
  }
}
