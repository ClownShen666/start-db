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
package org.urbcomp.cupid.db.udf.geometricconstructor

import org.apache.flink.table.annotation.DataTypeHint
import org.locationtech.jts.geom.{Point, Polygon}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}

import java.math.BigDecimal
import java.util
import scala.collection.JavaConverters.asScalaBufferConverter

class st_makeBBoxUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_makeBBox"

  override def registerEngines(): scala.collection.immutable.List[DataEngine.Value] =
    scala.collection.immutable.List(Calcite, Spark, Flink)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Polygon])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[BigDecimal]) lowerX: BigDecimal,
      @DataTypeHint(value = "RAW", bridgedTo = classOf[BigDecimal]) lowerY: BigDecimal,
      @DataTypeHint(value = "RAW", bridgedTo = classOf[BigDecimal]) upperX: BigDecimal,
      @DataTypeHint(value = "RAW", bridgedTo = classOf[BigDecimal]) upperY: BigDecimal
  ): Polygon = {
    if (lowerX == null || lowerY == null || upperX == null || upperY == null) null
    else {
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

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Polygon])
  def eval(lowerX: Double, lowerY: Double, upperX: Double, upperY: Double): Polygon = {
    if (lowerX == null || lowerY == null || upperX == null || upperY == null) null
    else {
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

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Polygon])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Point]) point1: Point,
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Point]) point2: Point
  ): Polygon = {
    if (point1 == null || point2 == null) null
    else {
      makeBBox(
        BigDecimal.valueOf(point1.getX),
        BigDecimal.valueOf(point1.getY),
        BigDecimal.valueOf(point2.getX),
        BigDecimal.valueOf(point2.getY)
      )
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper", "udfWrapper2")

  def udfWrapper: (BigDecimal, BigDecimal, BigDecimal, BigDecimal) => Polygon = eval
  def udfWrapper2: (Point, Point) => Polygon = eval
}
