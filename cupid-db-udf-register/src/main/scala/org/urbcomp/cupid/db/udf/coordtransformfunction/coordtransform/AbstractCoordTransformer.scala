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
package org.urbcomp.cupid.db.udf.coordtransformfunction.coordtransform

import org.locationtech.jts.geom.{
  Coordinate,
  Geometry,
  GeometryCollection,
  LineString,
  LinearRing,
  MultiLineString,
  MultiPoint,
  MultiPolygon,
  Point,
  Polygon
}
import org.urbcomp.cupid.db.model.point.{GPSPoint, SpatialPoint}
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.util.GeometryFactoryUtils

abstract class AbstractCoordTransformer {
  def pointTransform(point: Point): Point = {
    val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory
    val lngLat = transform(point.getX, point.getY)
    geometryFactory.createPoint(new Coordinate(lngLat(0), lngLat(1)))
  }

  def lineStringTransform(lineString: LineString): LineString = {
    val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory
    val coordinates = lineString.getCoordinates
    val res = new Array[Coordinate](coordinates.length)
    for (i <- 0 until coordinates.length) {
      val lngLat = transform(coordinates(i).getX, coordinates(i).getY)
      res(i) = new Coordinate(lngLat(0), lngLat(1))
    }
    geometryFactory.createLineString(res)
  }

  def polygonTransform(polygon: Polygon): Polygon = {
    val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory
    val shell = geometryFactory.createLinearRing(
      lineStringTransform(polygon.getExteriorRing).getCoordinateSequence
    )
    val holes = new Array[LinearRing](polygon.getNumInteriorRing)
    for (i <- 0 until polygon.getNumInteriorRing) {
      val linearRing = polygon.getInteriorRingN(i)
      val lineString = lineStringTransform(linearRing)
      holes(i) = geometryFactory.createLinearRing(lineString.getCoordinateSequence)
    }
    geometryFactory.createPolygon(shell, holes)
  }

  def multiPointTransform(multiPoint: MultiPoint): MultiPoint = {
    val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory
    val points = new Array[Point](multiPoint.getNumGeometries)
    for (i <- 0 until multiPoint.getNumGeometries) {
      val point = multiPoint.getGeometryN(i).asInstanceOf[Point]
      points(i) = pointTransform(point)
    }
    geometryFactory.createMultiPoint(points)
  }

  def multiLineStringTransform(multiLineString: MultiLineString): MultiLineString = {
    val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory
    val lineStrings = new Array[LineString](multiLineString.getNumGeometries)
    for (i <- 0 until multiLineString.getNumGeometries) {
      val lineString = multiLineString.getGeometryN(i).asInstanceOf[LineString]
      lineStrings(i) = lineStringTransform(lineString)
    }
    geometryFactory.createMultiLineString(lineStrings)
  }

  def multiPolygonTransform(mPolygon: MultiPolygon): MultiPolygon = {
    val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory
    val polygons = new Array[Polygon](mPolygon.getNumGeometries)
    for (i <- 0 until mPolygon.getNumGeometries) {
      val polygon = mPolygon.getGeometryN(i).asInstanceOf[Polygon]
      polygons(i) = polygonTransform(polygon)
    }
    geometryFactory.createMultiPolygon(polygons)
  }

  def geometryTransform(geometry: Geometry): Geometry = {
    geometry match {
      case point: Point                   => pointTransform(point)
      case string: LineString             => lineStringTransform(string)
      case polygon: Polygon               => polygonTransform(polygon)
      case multiPoint: MultiPoint         => multiPointTransform(multiPoint)
      case multiString: MultiLineString   => multiLineStringTransform(multiString)
      case multiPolygon: MultiPolygon     => multiPolygonTransform(multiPolygon)
      case collection: GeometryCollection => geometryCollectionTransform(collection)
      case _                              =>
    }
    null
  }

  def geometryCollectionTransform(geometryCollection: GeometryCollection): GeometryCollection = {
    val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory
    val geometries = new Array[Geometry](geometryCollection.getNumGeometries)
    for (i <- 0 until geometryCollection.getNumGeometries) {
      val geometry = geometryCollection.getGeometryN(i)
      geometries(i) = geometryTransform(geometry)
    }
    geometryFactory.createGeometryCollection(geometries)
  }

  def trajectoryTransform(trajectory: Trajectory): Trajectory = {
    val points = trajectory.getGPSPointList.stream
      .map((o: GPSPoint) => {
        def foo(o: GPSPoint) = {
          val coords = transform(o.getLng, o.getLat)
          new GPSPoint(o.getTime, coords(0), coords(1))
        }

        foo(o)
      })
      .collect(List.asInstanceOf) //collect(Collectors.toList)
    new Trajectory(trajectory.getTid, trajectory.getOid, points)
  }

  def roadSegmentTransform(rs: RoadSegment): RoadSegment = {
    val points = rs.getPoints.stream
      .map((o: SpatialPoint) => {
        def foo(o: SpatialPoint) = {
          val coords = transform(o.getLng, o.getLat)
          new SpatialPoint(coords(0), coords(1))
        }

        foo(o)
      })
      .collect(List.asInstanceOf)
    new RoadSegment(rs.getRoadSegmentId, rs.getStartNode.getNodeId, rs.getEndNode.getNodeId, points)
      .setDirection(rs.getDirection)
      .setSpeedLimit(rs.getSpeedLimit)
      .setLevel(rs.getLevel)
      .setLengthInMeter(rs.getLengthInMeter)
  }

  def roadNetworkTransform(rn: RoadNetwork): RoadNetwork = {
    val roadSegments =
      rn.getRoadSegments.stream.map(this.roadSegmentTransform).collect(List.asInstanceOf)
    new RoadNetwork(roadSegments)
  }

  protected def transform(lng: Double, lat: Double): Array[Double]

}
