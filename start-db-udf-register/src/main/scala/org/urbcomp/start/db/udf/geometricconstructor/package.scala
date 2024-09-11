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
package org.urbcomp.start.db.udf

import org.locationtech.jts.geom.{
  Coordinate,
  GeometryFactory,
  LineString,
  LinearRing,
  Point,
  Polygon
}
import org.urbcomp.start.db.util.GeometryFactoryUtils

import java.math.BigDecimal
import java.util
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.convert.ImplicitConversions.`collection asJava`

package object geometricconstructor {
  def makePoint(x: BigDecimal, y: BigDecimal): Point = {
    val X = x.doubleValue()
    val Y = y.doubleValue()
    if (X > 180 || X < -180 || Y > 90 || Y < -90) return null
    val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory

    val ans = geometryFactory.createPoint(new Coordinate(X, Y))

    ans
  }

  def makePoint(x: Double, y: Double): Point = {
    val X = x.doubleValue()
    val Y = y.doubleValue()
    if (X > 180 || X < -180 || Y > 90 || Y < -90) return null
    val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory

    val ans = geometryFactory.createPoint(new Coordinate(X, Y))

    ans
  }

  def makeLineString(points: List[Point]): LineString = {
    val geometryFactory: GeometryFactory = GeometryFactoryUtils.defaultGeometryFactory
    geometryFactory.createLineString(points.toStream.map(point => point.getCoordinate).toArray)
  }

  def makePolygon(shell: LineString): Polygon = {
    val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory
    geometryFactory.createPolygon(geometryFactory.createLinearRing(shell.getCoordinateSequence))
  }

  def makePolygon(shell: LineString, holes: List[LineString]): Polygon = {
    val geometryFactory: GeometryFactory = GeometryFactoryUtils.defaultGeometryFactory
    geometryFactory.createPolygon(
      geometryFactory.createLinearRing(shell.getCoordinateSequence),
      holes.toStream
        .map((o: LineString) => geometryFactory.createLinearRing(o.getCoordinateSequence))
        .toArray
    )
  }

  def makeBBox(
      lowerX: BigDecimal,
      lowerY: BigDecimal,
      upperX: BigDecimal,
      upperY: BigDecimal
  ): Polygon = {
    val points: util.List[Point] = new util.ArrayList[Point](5)
    points.add(makePoint(lowerX, lowerY))
    points.add(makePoint(lowerX, upperY))
    points.add(makePoint(upperX, upperY))
    points.add(makePoint(upperX, lowerY))
    points.add(makePoint(lowerX, lowerY))
    val pointss = points.asScala.toList
    makePolygon(makeLineString(pointss))
  }
}
