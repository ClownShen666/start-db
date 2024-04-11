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

import org.apache.flink.table.annotation.DataTypeHint
import org.locationtech.jts.geom.{
  Geometry,
  LineString,
  MultiLineString,
  MultiPoint,
  MultiPolygon,
  Point,
  Polygon
}
import org.locationtech.jts.geom.util.AffineTransformation
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction

class st_translateUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_translate"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry]) geom: Geometry,
      deltaX: Double,
      deltaY: Double
  ): Geometry = process(geom, deltaX, deltaY)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Point]) geom: Point,
      deltaX: Double,
      deltaY: Double
  ): Geometry = process(geom, deltaX, deltaY)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[LineString]) geom: LineString,
      deltaX: Double,
      deltaY: Double
  ): Geometry = process(geom, deltaX, deltaY)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Polygon]) geom: Polygon,
      deltaX: Double,
      deltaY: Double
  ): Geometry = process(geom, deltaX, deltaY)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[MultiPoint]) geom: MultiPoint,
      deltaX: Double,
      deltaY: Double
  ): Geometry = process(geom, deltaX, deltaY)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[MultiLineString]) geom: MultiLineString,
      deltaX: Double,
      deltaY: Double
  ): Geometry = process(geom, deltaX, deltaY)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[MultiPolygon]) geom: MultiPolygon,
      deltaX: Double,
      deltaY: Double
  ): Geometry = process(geom, deltaX, deltaY)

  private def process(geom: Geometry, deltaX: Double, deltaY: Double): Geometry = {
    if (geom == null) null
    else {
      val at = new AffineTransformation
      at.setToTranslation(deltaX, deltaY)
      at.transform(geom)
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (Geometry, Double, Double) => Geometry = eval
}
