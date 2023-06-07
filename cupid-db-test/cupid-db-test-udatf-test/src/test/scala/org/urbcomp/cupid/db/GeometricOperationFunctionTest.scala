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

  test("st_bufferPoint(point, distanceInM)") {
//    executeQueryCheck(
//      "select st_bufferPoint(st_makePoint(116.307683,39.978879), 1)",
//      List(
//        "POLYGON ((116.30769472304785 39.978879, 116.30769469991509 39.97887973609627, 116.30769463060811 39.978880469287496, 116.30769451540044 39.97888119668011, 116.30769435474674 39.97888191540345, 116.30769414928105 39.97888262262101, 116.30769389981423 39.978883315541744, 116.30769360733083 39.978883991431005, 116.30769327298513 39.97888464762137, 116.30769289809666 39.978885281523155, 116.30769248414494 39.978885890634636, 116.30769203276361 39.97888647255193, 116.3076915457341 39.97888702497848, 116.30769102497847 39.978887545734096, 116.30769047255193 39.978888032763614, 116.30768989063463 39.97888848414493, 116.30768928152315 39.97888889809667, 116.30768864762138 39.978889272985136, 116.30768799143101 39.97888960733083, 116.30768731554174 39.978889899814234, 116.307686622621 39.97889014928104, 116.30768591540344 39.97889035474674, 116.30768519668011 39.97889051540044, 116.3076844692875 39.97889063060811, 116.30768373609627 39.97889069991509, 116.307683 39.97889072304785, 116.30768226390373 39.97889069991509, 116.3076815307125 39.97889063060811, 116.30768080331988 39.97889051540044, 116.30768008459656 39.97889035474674, 116.30767937737899 39.97889014928104, 116.30767868445825 39.978889899814234, 116.30767800856898 39.97888960733083, 116.30767735237862 39.978889272985136, 116.30767671847684 39.97888889809667, 116.30767610936536 39.97888848414493, 116.30767552744807 39.978888032763614, 116.30767497502153 39.978887545734096, 116.3076744542659 39.97888702497848, 116.30767396723638 39.97888647255193, 116.30767351585506 39.978885890634636, 116.30767310190333 39.978885281523155, 116.30767272701486 39.97888464762137, 116.30767239266916 39.978883991431005, 116.30767210018577 39.978883315541744, 116.30767185071895 39.97888262262101, 116.30767164525325 39.97888191540345, 116.30767148459955 39.97888119668011, 116.30767136939188 39.978880469287496, 116.3076713000849 39.97887973609627, 116.30767127695215 39.978879, 116.3076713000849 39.97887826390373, 116.30767136939188 39.9788775307125, 116.30767148459955 39.978876803319885, 116.30767164525325 39.97887608459655, 116.30767185071895 39.97887537737899, 116.30767210018577 39.978874684458255, 116.30767239266916 39.978874008568994, 116.30767272701486 39.97887335237863, 116.30767310190333 39.97887271847684, 116.30767351585506 39.97887210936536, 116.30767396723638 39.97887152744807, 116.3076744542659 39.97887097502152, 116.30767497502153 39.9788704542659, 116.30767552744807 39.978869967236385, 116.30767610936536 39.97886951585507, 116.30767671847684 39.97886910190333, 116.30767735237862 39.97886872701486, 116.30767800856898 39.97886839266917, 116.30767868445825 39.978868100185764, 116.30767937737899 39.978867850718956, 116.30768008459656 39.97886764525326, 116.30768080331988 39.97886748459956, 116.3076815307125 39.978867369391885, 116.30768226390373 39.97886730008491, 116.307683 39.97886727695215, 116.30768373609627 39.97886730008491, 116.3076844692875 39.978867369391885, 116.30768519668011 39.97886748459956, 116.30768591540344 39.97886764525326, 116.307686622621 39.978867850718956, 116.30768731554174 39.978868100185764, 116.30768799143101 39.97886839266917, 116.30768864762138 39.97886872701486, 116.30768928152315 39.97886910190333, 116.30768989063463 39.97886951585507, 116.30769047255193 39.978869967236385, 116.30769102497847 39.9788704542659, 116.3076915457341 39.97887097502152, 116.30769203276361 39.97887152744807, 116.30769248414494 39.97887210936536, 116.30769289809666 39.97887271847684, 116.30769327298513 39.97887335237863, 116.30769360733083 39.978874008568994, 116.30769389981423 39.978874684458255, 116.30769414928105 39.97887537737899, 116.30769435474674 39.97887608459655, 116.30769451540044 39.978876803319885, 116.30769463060811 39.9788775307125, 116.30769469991509 39.97887826390373, 116.30769472304785 39.978879))"
//      )
//    )
    // todo
 
  }

  test("st_convexHull(geom)") {
    executeQueryCheck(
      "select st_convexHull(st_makePoint(116.307683,39.978879))",
      List("POINT (116.307683 39.978879)")
    )
    // todo LineString等完成之后，再测
  }
}
