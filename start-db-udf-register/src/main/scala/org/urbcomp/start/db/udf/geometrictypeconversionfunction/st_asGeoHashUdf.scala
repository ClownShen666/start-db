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
package org.urbcomp.start.db.udf.geometrictypeconversionfunction

import org.apache.flink.table.annotation.DataTypeHint
import org.locationtech.geomesa.spark.jts.util.GeoHashUtils
import org.locationtech.jts.geom.{
  Geometry,
  LineString,
  MultiLineString,
  MultiPoint,
  MultiPolygon,
  Point,
  Polygon
}
import org.urbcomp.start.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.start.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction

class st_asGeoHashUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_asGeoHash"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry]) geom: Geometry,
      precision: Int
  ): java.lang.String = {
    process(geom, precision)
  }

  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Point]) geom: Point,
      precision: Int
  ): java.lang.String = {
    process(geom, precision)
  }

  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[LineString]) geom: LineString,
      precision: Int
  ): java.lang.String = {
    process(geom, precision)
  }

  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[Polygon]) geom: Polygon,
      precision: Int
  ): java.lang.String = {
    process(geom, precision)
  }

  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[MultiPoint]) geom: MultiPoint,
      precision: Int
  ): java.lang.String = {
    process(geom, precision)
  }

  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[MultiLineString]) geom: MultiLineString,
      precision: Int
  ): java.lang.String = {
    process(geom, precision)
  }

  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[MultiPolygon]) geom: MultiPolygon,
      precision: Int
  ): java.lang.String = {
    process(geom, precision)
  }

  private def process(geom: Geometry, precision: Int): java.lang.String = {
    if (geom == null) null
    else {
      GeoHashUtils.encode(geom, precision)
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (Geometry, Int) => java.lang.String = eval

}
