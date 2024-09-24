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
package org.urbcomp.start.db.udf.geometricconstructor

import org.apache.flink.table.annotation.{DataTypeHint, FunctionHint}
import org.locationtech.jts.geom.{Coordinate, Point}
import org.urbcomp.start.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.start.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.start.db.util.GeometryFactoryUtils

import java.math.BigDecimal

class st_makePointUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_makePoint"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Point])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[BigDecimal]) x: BigDecimal,
      @DataTypeHint(value = "RAW", bridgedTo = classOf[BigDecimal]) y: BigDecimal
  ): Point = {
    if (x == null || y == null) return null
    val X = x.doubleValue()
    val Y = y.doubleValue()
    if (X > 180 || X < -180 || Y > 90 || Y < -90) null
    else {
      val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory
      geometryFactory.createPoint(new Coordinate(X, Y))
    }
  }

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Point])
  def eval(x: Double, y: Double): Point = {
    if (x == null || y == null) return null
    val X = x.doubleValue()
    val Y = y.doubleValue()
    if (X > 180 || X < -180 || Y > 90 || Y < -90) null
    else {
      val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory
      geometryFactory.createPoint(new Coordinate(X, Y))
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (BigDecimal, BigDecimal) => Point = eval
}
