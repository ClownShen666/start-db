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

  def castToPoint(geom: Geometry): Point = {
    if (geom == null) null
    else
      geom match {
        case point: Point => point
        case _            => null
      }
  }

  @throws[ParseException]
  def geomFromGeoJSON(geoJson: String): Geometry = {
    if (geoJson == null) null
    else {
      val geoJsonReader = new GeoJsonReader
      geoJsonReader.read(geoJson)
    }
  }

  def castToLineString(geom: Geometry): LineString = {
    if (geom == null) null
    else
      geom match {
        case string: LineString => string
        case _                  => null
      }
  }

  def castToPolygon(geom: Geometry): Polygon = {
    if (geom == null) null
    else
      geom match {
        case polygon: Polygon => polygon
        case _                => null
      }
  }

  def castToMPoint(geom: Geometry): MultiPoint = {
    if (geom == null) null
    else
      geom match {
        case point: MultiPoint => point
        case _                 => null
      }
  }

  def castToMLineString(geom: Geometry): MultiLineString = {
    if (geom == null) null
    else
      geom match {
        case string: MultiLineString => string
        case _                       => null
      }
  }

  def castToMPolygon(geom: Geometry): MultiPolygon =
    if (geom == null) null
    else
      geom match {
        case polygon: MultiPolygon => polygon
        case _                     => null
      }
  @throws[ParseException]
  def geomFromWKT(wktString: String): Geometry = {
    if (wktString == null) null
    else {
      val wktReader = new WKTReader
      wktReader.read(wktString)
    }
  }
  @throws[ParseException]
  def geomFromWKB(wkb: Array[Byte]): Geometry = {
    if (wkb == null) null
    else {
      val wkbReader = new WKBReader
      wkbReader.read(wkb)
    }
  }
}
