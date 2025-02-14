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
package org.urbcomp.start.db

class GeometricRelationTypeConversionFunctionTest extends AbstractCalciteSparkFunctionTest {
  test("st_pointFromGeoJSON(String)") {
    checkCalciteSparkFlink(
      "select st_pointFromGeoJSON(st_asGeoJSON(st_makePoint(1.0, 2.0)))",
      List("POINT (1 2)")
    )
  }

  test("st_lineStringFromGeoJSON(String)") {
    checkCalciteSparkFlink(
      "select st_lineStringFromGeoJSON(st_asGeoJSON(st_lineStringFromWKT('LINESTRING(0 0,1 1,1 2)'))), " +
        "st_lineStringFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List("LINESTRING (0 0, 1 1, 1 2)", null)
    )
  }

  test("st_polygonFromGeoJSON(String)") {
    checkCalciteSparkFlink(
      "select st_polygonFromGeoJSON(st_asGeoJSON(st_polygonFromWKT('POLYGON((10 11,12 12,13 14,15 16,10 11))')))," +
        "st_polygonFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List("POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))", null)
    )
  }

  test("st_mPointFromGeoJSON(String)") {
    checkCalciteSparkFlink(
      "select st_mPointFromGeoJSON(st_asGeoJSON(st_mPointFromWKT('MULTIPOINT(3.5 5.6, 4.8 10.5)')))," +
        " st_mPointFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List("MULTIPOINT ((3.5 5.6), (4.8 10.5))", null)
    )
  }

  test("st_mLineStringFromGeoJSON(String)") {
    checkCalciteSparkFlink(
      "select st_mLineStringFromGeoJSON(st_asGeoJSON(" +
        "st_mLineStringFromWKT('MULTILINESTRING((3 4,1 5,2 5),(-5 -8,-10 -8,-15 -4))')))," +
        "st_mLineStringFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List("MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))", null)
    )
  }

  test("st_mPolygonFromGeoJSON(String)") {
    checkCalciteSparkFlink(
      "select st_mPolygonFromGeoJSON(st_asGeoJSON(" +
        "st_mPolygonFromWKT('MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2)),((6 3,9 2,9 4,6 3)))')))," +
        "st_mPolygonFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List(
        "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))",
        null
      )
    )
  }

  test("st_geometryFromGeoJSON(String)") {
    checkCalciteSparkFlink(
      "select st_geometryFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List("POINT (1 2)")
    )
  }

  test("st_asGeoJSON(geom)") {
    checkCalciteSparkFlink(
      "select st_asGeoJSON(st_makePoint(1, 2))",
      List(
        "{\"type\":\"Point\",\"coordinates\":[1,2],\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:4326\"}}}"
      )
    )
  }

  test("st_pointFromWKT(WKT)") {
    checkCalciteSparkFlink(
      "select st_pointFromWKT('POINT(2 3)'), st_pointFromWKT('LINESTRING(0 0,1 1,1 2)')",
      List("POINT (2 3)", null)
    )
  }

  test("st_lineStringFromWKT(WKT)") {
    checkCalciteSparkFlink(
      "select st_lineStringFromWKT('LINESTRING(0 0,1 1,1 2)'), st_lineStringFromWKT('POINT(2 3)')",
      List("LINESTRING (0 0, 1 1, 1 2)", null)
    )
  }

  test("st_polygonFromWKT(WKT)") {
    checkCalciteSparkFlink(
      "select st_polygonFromWKT('POLYGON((10 11,12 12,13 14,15 16,10 11))'), " +
        "st_polygonFromWKT('POINT(2 3)')",
      List("POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))", null)
    )
  }

  test("st_mPointFromWKT(WKT)") {
    checkCalciteSparkFlink(
      "select st_mPointFromWKT('MULTIPOINT(3.5 5.6, 4.8 10.5)'), " +
        "st_mPointFromWKT('POINT(2 3)')",
      List("MULTIPOINT ((3.5 5.6), (4.8 10.5))", null)
    )
  }

  test("st_mLineStringFromWKT(WKT)") {
    checkCalciteSparkFlink(
      "select st_mLineStringFromWKT('MULTILINESTRING((3 4,1 5,2 5),(-5 -8,-10 -8,-15 -4))'), " +
        "st_mLineStringFromWKT('POINT(2 3)')",
      List("MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))", null)
    )
  }

  test("st_mPolygonFromWKT(WKT)") {
    checkCalciteSparkFlink(
      "select st_mPolygonFromWKT(" +
        "'MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2)),((6 3,9 2,9 4,6 3)))')" +
        ", st_mPolygonFromWKT('POINT(2 3)')",
      List(
        "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), " +
          "((6 3, 9 2, 9 4, 6 3)))",
        null
      )
    )
  }

  test("st_geometryFromWKT(WKT)") {
    checkCalciteSparkFlink(
      "select st_geometryFromWKT('GEOMETRYCOLLECTION(POINT(4 6),LINESTRING(4 6,7 10))')",
      List("GEOMETRYCOLLECTION (POINT (4 6), LINESTRING (4 6, 7 10))")
    )
  }

  test("st_asWKT(geom)") {
    checkCalciteSparkFlink("select st_asWKT(st_makePoint(1, 2))", List("POINT (1 2)"))
  }

  test("st_castToPoint(geom)") {
    checkCalciteSparkFlink(
      "select st_castToPoint(st_makePoint(1, 2)), " +
        "st_castToPoint(st_makeBBox(1, 2, 3, 4))",
      List("POINT (1 2)", null)
    )
  }

  test("st_castToLineString(geom)") {
    checkCalciteSparkFlink(
      "select st_castToLineString(st_geometryFromWKT('LINESTRING(0 0,1 1,1 2)'))," +
        " st_castToLineString(st_makeBBox(1, 2, 3, 4))",
      List("LINESTRING (0 0, 1 1, 1 2)", null)
    )
  }

  test("st_castToPolygon(geom)") {
    checkCalciteSparkFlink(
      "select st_castToPolygon(st_geometryFromWKT('POLYGON((10 11,12 12,13 14,15 16,10 11))'))" +
        ", st_castToPolygon(st_makePoint(1, 2))",
      List("POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))", null)
    )
  }

  test("st_castToMPoint(geom)") {
    checkCalciteSparkFlink(
      "select st_castToMPoint(st_geometryFromWKT('MULTIPOINT(3.5 5.6, 4.8 10.5)'))" +
        ", st_castToMPoint(st_makeBBox(1, 2, 3, 4))",
      List("MULTIPOINT ((3.5 5.6), (4.8 10.5))", null)
    )
  }

  test("st_castToMLineString(geom)") {
    checkCalciteSparkFlink(
      "select st_castToMLineString(st_geometryFromWKT('MULTILINESTRING((3 4,1 5,2 5),(-5 -8,-10 -8,-15 -4))'))" +
        ", st_castToMLineString(st_makeBBox(1, 2, 3, 4))",
      List("MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))", null)
    )
  }

  test("st_castToMPolygon(geom)") {
    checkCalciteSparkFlink(
      "select st_castToMPolygon(st_geometryFromWKT" +
        "('MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2)),((6 3,9 2,9 4,6 3)))')), " +
        "st_castToMPolygon(st_makeBBox(1, 2, 3, 4))",
      List(
        "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))",
        null
      )
    )
  }

  test("st_castToGeometry(geom)") {
    checkCalciteSparkFlink(
      "select st_castToGeometry(st_lineStringFromWKT('LINESTRING(0 0,1 1,1 2)'))",
      List("LINESTRING (0 0, 1 1, 1 2)")
    )
  }

  test("st_pointFromWKB(WKB)") {
    checkCalciteSparkFlink(
      "select st_pointFromWKB(st_asWKB(st_makePoint(1, 2)))",
      List("POINT (1 2)")
    )
  }

  test("st_lineStringFromWKB(WKB)") {
    checkCalciteSparkFlink(
      "select st_lineStringFromWKB(st_asWKB(st_lineStringFromWKT('LINESTRING(0 0,1 1,1 2)'))), " +
        "st_lineStringFromWKB(st_asWKB(st_makePoint(1, 2)))",
      List("LINESTRING (0 0, 1 1, 1 2)", null)
    )
  }

  test("st_polygonFromWKB(WKB)") {
    checkCalciteSparkFlink(
      "select st_polygonFromWKB(st_asWKB(st_polygonFromWKT('POLYGON((10 11,12 12,13 14,15 16,10 11))')))," +
        " st_polygonFromWKB(st_asWKB(st_makePoint(1, 2)))",
      List("POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))", null)
    )
  }

  test("st_mPointFromWKB(WKB)") {
    checkCalciteSparkFlink(
      "select st_mPointFromWKB(st_asWKB(st_mPointFromWKT('MULTIPOINT(3.5 5.6, 4.8 10.5)')))," +
        " st_mPointFromWKB(st_asWKB(st_makePoint(1, 2)))",
      List("MULTIPOINT ((3.5 5.6), (4.8 10.5))", null)
    )
  }

  test("st_mLineStringFromWKB(WKB)") {
    checkCalciteSparkFlink(
      "select st_mLineStringFromWKB(st_asWKB(" +
        "st_mLineStringFromWKT('MULTILINESTRING((3 4,1 5,2 5),(-5 -8,-10 -8,-15 -4))')))," +
        "st_mLineStringFromWKB(st_asWKB(st_makePoint(1, 2)))",
      List("MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))", null)
    )
  }

  test("st_mPolygonFromWKB(WKB)") {
    checkCalciteSparkFlink(
      "select st_mPolygonFromWKB(st_asWKB(" +
        "st_mPolygonFromWKT('MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2)),((6 3,9 2,9 4,6 3)))'))), " +
        "st_mPolygonFromWKB(st_asWKB(st_makePoint(1, 2)))",
      List(
        "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))",
        null
      )
    )
  }

  test("st_geometryFromWKB(WKB)&st_asWKB(geom)") {
    checkCalciteSparkFlink(
      "select st_geometryFromWKB(st_asWKB(st_makePoint(1, 2)))",
      List("POINT (1 2)")
    )
  }

  test("st_pointFromGeoHash(str, precision)") {
    checkCalciteSparkFlink("select st_pointFromGeoHash('s10', 12)", List("POINT (2.8125 7.03125)"))
  }

  test("st_geometryFromGeoHash(str, precision)") {
    checkCalciteSparkFlink(
      "select st_geometryFromGeoHash('s10', 12)",
      List("POLYGON ((0 5.625, 0 8.4375, 5.625 8.4375, 5.625 5.625, 0 5.625))")
    )
  }

  test("st_asGeoHash(geom, precision)") {
    checkCalciteSparkFlink(
      "select st_asGeoHash(st_geometryFromWKT('MULTIPOINT(3.5 5.6, 4.8 10.5)'), 12)",
      List("s10")
    )
  }
}
