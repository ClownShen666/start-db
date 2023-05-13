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

import org.locationtech.jts.geom.{Coordinate, GeometryFactory, LineString, Point, Polygon}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.util.GeometryFactoryUtils

import java.util
import java.util.List
import scala.collection.convert.ImplicitConversions.`collection AsScalaIterable`
import scala.collection.immutable.List

class St_makePolygonUdf extends AbstractUdf {

  override def name(): String = "st_makePolygon"

  override def registerEngines(): scala.collection.immutable.List[DataEngine.Value] =
    scala.collection.immutable.List(Calcite, Spark)

  def evaluate(shell: LineString): Polygon = {
    val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory
    geometryFactory.createPolygon(geometryFactory.createLinearRing(shell.getCoordinateSequence))
  }

  def evaluate(shell: LineString, holes: util.List[LineString]): Polygon = {
    if (shell == null || holes == null) null
    else {
      val geometryFactory: GeometryFactory = GeometryFactoryUtils.defaultGeometryFactory
      geometryFactory.createPolygon(
        geometryFactory.createLinearRing(shell.getCoordinateSequence),
        holes.toStream
          .map((o: LineString) => geometryFactory.createLinearRing(o.getCoordinateSequence))
          .toArray
      )
    }
  }

  def udfSparkEntries: scala.collection.immutable.List[String] =
    scala.collection.immutable.List("udfWrapper")

  def udfWrapper: (LineString) => Polygon = evaluate
  def udfWrapper1: (LineString, util.List[LineString]) => Polygon = evaluate
}
