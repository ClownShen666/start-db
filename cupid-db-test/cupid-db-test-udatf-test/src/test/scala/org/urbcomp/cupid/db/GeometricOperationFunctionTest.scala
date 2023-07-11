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
package org.urbcomp.cupid.db

/**
  * st_distanceSphere
  * st_distanceSpheroid
  * spark's answer is 0
  */
class GeometricOperationFunctionTest extends AbstractCalciteSparkFunctionTest {
  test("st_translate(geom, deltaX, deltaY)") {
    executeQueryCheck(
      "select st_translate(st_makePoint(1, 2), 1, 1), st_translate(st_makeBBox(1, 2, 3, 4), 1, 1)",
      List("POINT (2 3)", "POLYGON ((2 3, 2 5, 4 5, 4 3, 2 3))")
    )
  }

  test("st_x(point)") {
    executeQueryCheck(
      "select st_x(st_makePoint(1, 2)), st_x(st_makeBBox(1, 2, 3, 4))",
      List(1.0, null)
    )
  }

  test("st_y(point)") {
    executeQueryCheck(
      "select st_y(st_makePoint(1, 2)), st_y(st_makeBBox(1, 2, 3, 4))",
      List(2.0, null)
    )
  }

  test("st_BBox(geom)") {
    executeQueryCheck(
      "select st_BBox(st_makePoint(1, 2)), st_BBox(st_makeBBox(1, 2, 3, 4))",
      List("POINT (1 2)", "POLYGON ((1 2, 1 4, 3 4, 3 2, 1 2))")
    )
  }

  test("st_numPoints(geom)") {
    executeQueryCheck(
      "select st_numPoints(st_makePoint(1, 2)), st_numPoints(st_makeBBox(1, 2, 3, 4))",
      List(1, 5)
    )
  }

  test("st_pointN(geom, n)") {
    executeQueryCheck(
      "select st_pointN(st_makePoint(1, 2), 1), st_pointN(st_makeBBox(1, 2, 3, 4), 1)",
      List(null, null)
    )
    // todo check LineString
  }

  test("st_area(geom)") {
    executeQueryCheck(
      "select st_area(st_makePoint(1, 2)), st_area(st_makeBBox(1, 2, 3, 4))",
      List(0.0, 4.0)
    )
  }

  test("st_centroid(geom)") {
    executeQueryCheck(
      "select st_centroid(st_makePoint(1, 2)), st_centroid(st_makeBBox(1, 2, 3, 4))",
      List("POINT (1 2)", "POINT (2 3)")
    )
  }

  test("st_closestPoint(geom1, geom2)") {
    executeQueryCheck(
      "select st_closestPoint(st_makePoint(1, 2), st_makeBBox(1, 2, 3, 4))",
      List("POINT (1 2)")
    )
  }

  test("st_distance(geom1, geom2)") {
    executeQueryCheck(
      "select st_distance(st_makePoint(1, 2), st_makeBBox(1, 2, 3, 4)), " +
        "st_distance(st_makePoint(0, 0), st_makeBBox(1, 2, 3, 4))",
      List(0.0, Math.sqrt(5))
    )
  }

  test("st_distanceSphere(geom1, geom2)") {
    executeQueryCheck(
      "select st_distanceSphere(st_makePoint(116.307683,39.978879), st_makePoint(116.337579,39.984186))",
      List(2614.7025275922806)
    )
  }

  test("st_distanceSpheroid(geom1, geom2)") {
    executeQueryCheck(
      "select st_distanceSpheroid(st_makePoint(116.307683,39.978879), st_makePoint(116.337579,39.984186))",
      List(2620.727593714579)
    )
  }

  test("st_intersection(geom1, geom2)") {
    executeQueryCheck(
      "select st_intersection(st_makePoint(116.307683,39.978879), st_makePoint(116.337579,39.984186)), " +
        "st_intersection(st_makeBBox(1, 2, 3, 4), st_makePoint(2, 3)), " +
        "st_intersection(st_makeBBox(1, 2, 3, 4), st_makeBBOX(2, 3, 4, 5))",
      List("POINT EMPTY", "POINT (2 3)", "POLYGON ((2 4, 3 4, 3 3, 2 3, 2 4))")
    )
  }

  test("st_length(geom)") {
    executeQueryCheck(
      "select st_length(st_makePoint(116.307683,39.978879)), st_length(st_makeBBox(1, 2, 3, 4))",
      List(0.0, 8.0)
    )
  }

  test("st_lengthSphere(lineString)") {
    // todo 测试linestring的结果
  }

  test("st_lengthSpheroid(lineString)") {
    // todo 测试linestring的结果
  }

  test("st_difference(geom1, geom2)") {
    executeQueryCheck(
      "select st_difference(st_makeBBox(1, 2, 3, 4), st_makeBBox(2, 3, 4, 5))",
      List("POLYGON ((1 2, 1 4, 2 4, 2 3, 3 3, 3 2, 1 2))")
    )
  }

  test("st_isValid(geom)") {
    executeQueryCheck(
      "select st_isValid(st_makeBBox(1, 2, 3, 4)), " +
        "st_isValid(st_polygonFromWKT('POLYGON((0 0, 1 1, 1 2, 1 1, 0 0))'))",
      List(true, false)
    )
  }

  test("st_convexHull(geom)") {
    executeQueryCheck(
      "select st_convexHull(st_makePoint(116.307683,39.978879))",
      List("POINT (116.307683 39.978879)")
    )
    // todo LineString等完成之后，再测
  }
}
