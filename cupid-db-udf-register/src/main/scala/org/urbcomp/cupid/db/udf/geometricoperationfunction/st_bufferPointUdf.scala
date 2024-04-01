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
import org.locationtech.jts.geom._
import org.locationtech.jts.util.GeometricShapeFactory
import org.locationtech.spatial4j.context.jts.JtsSpatialContext
import org.locationtech.spatial4j.shape.jts.JtsPoint
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.util.{GeoFunctions, GeometryFactoryUtils}

class st_bufferPointUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_bufferPoint"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Geometry])
  def eval(point: Point, distanceInM: Double): Geometry = {
    if (point == null) null
    else {
      val degrees = GeoFunctions.getDegreeFromM(distanceInM)
      val jstPoint = new JtsPoint(point, JtsSpatialContext.GEO)
      val circle = jstPoint.getBuffered(degrees, JtsSpatialContext.GEO)
      val gsf = ThreadLocal
        .withInitial(() => new GeometricShapeFactory(GeometryFactoryUtils.defaultGeometryFactory))
        .get
      gsf.setSize(circle.getBoundingBox.getWidth)
      gsf.setNumPoints(4 * 25)
      gsf.setCentre(new Coordinate(circle.getCenter.getX, circle.getCenter.getY))
      val geomTemp = gsf.createCircle
      val geomCopy = GeometryFactoryUtils.defaultGeometryFactory.createGeometry(geomTemp)
      if (geomCopy.getEnvelopeInternal.getMinX < -180 || geomCopy.getEnvelopeInternal.getMaxX > 180)
        geomCopy.apply(new CoordinateSequenceFilter() {
          override def filter(seq: CoordinateSequence, i: Int): Unit = {
            seq.setOrdinate(i, CoordinateSequence.X, seq.getX(i) + degreesToTranslate(seq.getX(i)))
          }

          override def isDone = false

          override def isGeometryChanged = true
        })
      val datelineSafeShape = JtsSpatialContext.GEO.getShapeFactory.makeShapeFromGeometry(geomCopy)
      JtsSpatialContext.GEO.getShapeFactory.getGeometryFrom(datelineSafeShape)
    }
  }

  private def degreesToTranslate(x: Double): java.lang.Integer = {
    Some(x) match {
      case Some(x0) => (Math.floor((x0 + 180) / 360.0) * -360).toInt
      case _        => null
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (Point, Double) => Geometry = eval
}
