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

import org.locationtech.jts.geom.{
  Geometry,
  LineString,
  MultiLineString,
  MultiPoint,
  MultiPolygon,
  Point,
  Polygon
}
import org.locationtech.jts.io.{ParseException, WKBReader, WKTReader}
import org.locationtech.jts.io.geojson.GeoJsonReader

package object geometrictypeconversionfunction {

  def st_castToPoint(geom: Geometry): Point = {
    if (geom == null) null
    else if (geom.isInstanceOf[Point]) geom.asInstanceOf[Point]
    else null
  }

  @throws[ParseException]
  def st_geomFromGeoJSON(geoJson: String): Geometry = {
    if (geoJson == null) null
    else {
      val geoJsonReader = new GeoJsonReader
      geoJsonReader.read(geoJson)
    }
  }

  def st_castToLineString(geom: Geometry): LineString = {
    if (geom == null) null
    else if (geom.isInstanceOf[LineString]) geom.asInstanceOf[LineString]
    else null
  }

  def st_castToPolygon(geom: Geometry): Polygon = {
    if (geom == null) null
    else if (geom.isInstanceOf[Polygon]) geom.asInstanceOf[Polygon]
    else null
  }

  def st_castToMPoint(geom: Geometry): MultiPoint = {
    if (geom == null) null
    else if (geom.isInstanceOf[MultiPoint]) geom.asInstanceOf[MultiPoint]
    else null
  }

  def st_castToMLineString(geom: Geometry): MultiLineString = {
    if (geom == null) null
    else if (geom.isInstanceOf[MultiLineString]) geom.asInstanceOf[MultiLineString]
    else null
  }

  def st_castToMPolygon(geom: Geometry): MultiPolygon =
    if (geom == null) null
    else if (geom.isInstanceOf[MultiPolygon]) geom.asInstanceOf[MultiPolygon]
    else null
  @throws[ParseException]
  def st_geomFromWKT(wktString: String): Geometry = {
    if (wktString == null) null
    else {
      val wktReader = new WKTReader
      wktReader.read(wktString)
    }
  }
  @throws[ParseException]
  def st_geomFromWKB(wkb: Array[Byte]): Geometry = {
    if (wkb == null) null
    else {
      val wkbReader = new WKBReader
      wkbReader.read(wkb)
    }
  }
}
