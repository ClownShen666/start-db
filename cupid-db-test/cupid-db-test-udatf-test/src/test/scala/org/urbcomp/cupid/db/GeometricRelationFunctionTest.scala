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

import org.junit.Assert.{assertEquals, assertTrue}
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory

import scala.collection.JavaConverters.seqAsJavaList

/**
  * Geometry relation Function test
  *
  * @author XiangHe
  */
// TODO test the relation between linestring and polygon
// Fixme flink ci fail
class GeometricRelationFunctionTest extends AbstractCalciteSparkFunctionTest {
  val nameArray: Array[String] = Array[String]("int", "str", "double", "point")
  val typeArray: Array[String] = Array[String]("Integer", "String", "Double", "Point")
  val trajectory: Trajectory =
    ModelGenerator.generateTrajectory(seqAsJavaList(nameArray), seqAsJavaList(typeArray))
  val tGeo: String = trajectory.toGeoJSON
  test("st_equals") {
    checkCalciteSparkFlink(
      "select st_equals(st_makePoint(1, 2), st_makePoint(2, 3)), " +
        "st_equals(st_makePoint(1, 2), st_makeBBox(1, 2, 3, 4))," +
        "st_equals(st_makeBBox(1, 2, 3, 4), st_makeBBox(1, 2, 3, 4))",
      List(false, false, true)
    )
  }

  test("st_contains") {
    checkCalciteSparkFlink(
      "select st_contains(st_makeBBox(0, 0, 4, 4), st_makePoint(1, 2))," +
        "st_contains(st_makeBBox(1, 1, 2, 2), st_makeBBox(1, 1, 2, 2))," +
        "st_contains(st_makeBBox(0, 0, 2, 2), st_makePoint(2, 2))," +
        "st_contains(st_makeBBox(0, 0, 2, 2), st_makePoint(3, 3))",
      List(true, true, false, false)
    )
  }

  test("st_covers") {
    checkCalciteSparkFlink(
      "select st_covers(st_makeBBox(0, 0, 4, 4), st_makePoint(1, 2))," +
        "st_covers(st_makeBBox(1, 1, 2, 2), st_makeBBox(1, 1, 2, 2))," +
        "st_covers(st_makeBBox(0, 0, 2, 2), st_makePoint(2, 2))," +
        "st_covers(st_makeBBox(0, 0, 2, 2), st_makePoint(3, 3))",
      List(true, true, true, false)
    )
  }

  test("st_crosses") {
    checkCalciteSparkFlink(
      "select st_crosses(st_makeBBox(0, 0, 4, 4), st_makePoint(1, 2))," +
        "st_crosses(st_makeBBox(1, 1, 3, 2), st_makeBBox(2, 1, 4, 2))",
      List(false, false)
    )
  }

  test("st_disjoint") {
    checkCalciteSparkFlink(
      "select st_disjoint(st_makeBBox(0, 0, 4, 4), st_makePoint(1, 2))," +
        "st_disjoint(st_makeBBox(1, 1, 2, 2), st_makeBBox(1, 1, 2, 2))," +
        "st_disjoint(st_makeBBox(0, 0, 2, 2), st_makePoint(3, 3))," +
        "st_disjoint(st_makeBBox(0, 0, 2, 2), st_makePoint(2, 2))",
      List(false, false, true, false)
    )
  }

  test("st_intersects") {
    checkCalciteSparkFlink(
      "select st_intersects(st_makeBBox(0, 0, 4, 4), st_makePoint(1, 2))," +
        "st_intersects(st_makeBBox(1, 1, 2, 2), st_makeBBox(0, 0, 2, 2))," +
        "st_intersects(st_makeBBox(0, 0, 2, 2), st_makePoint(3, 3))," +
        "st_intersects(st_makeBBox(0, 0, 2, 2), st_makePoint(2, 2))",
      List(true, true, false, true)
    )
  }

  test("st_touches") {
    checkCalciteSparkFlink(
      "select st_touches(st_makeBBox(0, 0, 4, 4), st_makePoint(1, 2))," +
        "st_touches(st_makeBBox(1, 1, 2, 2), st_makeBBox(2, 1, 3, 2))," +
        "st_touches(st_makeBBox(0, 0, 2, 2), st_makePoint(3, 3))," +
        "st_touches(st_makeBBox(0, 0, 2, 2), st_makePoint(2, 2))",
      List(false, true, false, true)
    )
  }

  test("st_within") {
    checkCalciteSparkFlink(
      "select st_within(st_makePoint(1, 2), st_makeBBox(0, 0, 4, 4))," +
        "st_within(st_makeBBox(1, 1, 2, 2), st_makeBBox(1, 1, 2, 2))," +
        "st_within(st_makePoint(2, 2), st_makeBBox(0, 0, 2, 2))," +
        "st_within(st_makePoint(3, 3), st_makeBBox(0, 0, 2, 2))",
      List(true, true, false, false)
    )
  }

  test("st_overlaps") {
    checkCalciteSparkFlink(
      "select st_overlaps(st_makePoint(1, 2), st_makeBBox(0, 0, 4, 4))," +
        "st_overlaps(st_makeBBox(1, 1, 2, 2), st_makeBBox(1, 1, 2, 2))," +
        "st_overlaps(st_makeBBox(1, 1, 3, 3), st_makeBBox(2, 2, 4, 4))," +
        "st_overlaps(st_makePoint(2, 2), st_makePoint(2, 2))",
      List(false, false, true, false)
    )
  }

  test("st_relate") {
    checkCalciteSparkFlink(
      "select st_relate(st_makePoint(1, 2), st_makeBBox(0, 0, 4, 4))," +
        "st_relate(st_makeBBox(1, 1, 2, 2), st_makeBBox(1, 1, 2, 2))," +
        "st_relate(st_makePoint(2, 2), st_makePoint(2, 2))",
      List("0FFFFF212", "2FFF1FFF2", "0FFFFFFF2")
    )
  }
}
