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

import org.locationtech.jts.geom.Coordinate
import org.urbcomp.cupid.db.util.GeometryFactoryUtils

class GeometricConstructorFunctionTest extends AbstractCalciteSparkFunctionTest {
  test("st_makePoint(x, y)") {
    val geometryFactory = GeometryFactoryUtils.defaultGeometryFactory
    val expected = geometryFactory.createPoint(new Coordinate(1.1, 2))
    checkCalciteSparkFlink("select st_makePoint(1.1, 2)", List(expected))
  }

  test("st_makeBBox(lowerX, lowerY, upperX, upperY)") {
    checkCalciteSparkFlink(
      "select st_makeBBox(1.2, 2, 3, 4)",
      List("POLYGON ((1.2 2, 1.2 4, 3 4, 3 2, 1.2 2))")
    )
  }

  test("st_makeBBox(point1, point2)") {
    checkCalciteSparkFlink(
      "select st_makeBBox(st_makePoint(1, 2), st_makePoint(3, 4))",
      List("POLYGON ((1 2, 1 4, 3 4, 3 2, 1 2))")
    )
  }

  test("st_makeCircle(center, radiusInM)") {
    checkCalciteSparkFlink(
      "select st_makeCircle(st_makePoint(1, 2), 10)",
      List(
        "POLYGON ((1.000089831528412 2, 1.0000881054407826 1.999982474738195, 1.000082993510474" +
          " 1.9999656229623728, 1.0000746921861012 1.9999500922768276, 1.0000635204829045 " +
          "1.9999364795170955, 1.0000499077231724 1.9999253078138988, 1.0000343770376272 " +
          "1.999917006489526, 1.000017525261805 1.9999118945592174, 1 1.999910168471588, " +
          "0.9999824747381949 1.9999118945592174, 0.9999656229623727 1.999917006489526, " +
          "0.9999500922768276 1.9999253078138988, 0.9999364795170955 1.9999364795170955, " +
          "0.9999253078138988 1.9999500922768276, 0.999917006489526 1.9999656229623728, " +
          "0.9999118945592175 1.999982474738195, 0.9999101684715881 2, 0.9999118945592175 " +
          "2.000017525261805, 0.999917006489526 2.000034377037627, 0.9999253078138988 " +
          "2.0000499077231724, 0.9999364795170955 2.0000635204829043, 0.9999500922768276 " +
          "2.000074692186101, 0.9999656229623727 2.000082993510474, 0.9999824747381949 " +
          "2.0000881054407826, 1 2.000089831528412, 1.000017525261805 2.0000881054407826, " +
          "1.0000343770376272 2.000082993510474, 1.0000499077231724 2.000074692186101, " +
          "1.0000635204829045 2.0000635204829043, 1.0000746921861012 2.0000499077231724, " +
          "1.000082993510474 2.000034377037627, 1.0000881054407826 2.000017525261805, " +
          "1.000089831528412 2))"
      )
    )
  }
}
