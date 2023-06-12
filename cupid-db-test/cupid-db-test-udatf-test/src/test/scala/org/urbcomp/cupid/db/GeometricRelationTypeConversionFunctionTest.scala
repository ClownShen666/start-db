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

class GeometricRelationTypeConversionFunctionTest extends AbstractCalciteSparkFunctionTest {
  test("st_pointFromGeoJSON(String)") {
    executeQueryCheck(
      "select st_pointFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List("POINT (1 2)")
    )
  }

  test("st_lineStringFromGeoJSON(String)") {
    executeQueryCheck(
      "select st_lineStringFromGeoJSON(st_asGeoJSON(st_lineStringFromWKT('LINESTRING(0 0,1 1,1 2)'))), " +
        "st_lineStringFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List("LINESTRING (0 0, 1 1, 1 2)", null)
    )
  }

  test("st_polygonFromGeoJSON(String)") {
    executeQueryCheck(
      "select st_polygonFromGeoJSON(st_asGeoJSON(st_polygonFromWKT('POLYGON((10 11,12 12,13 14,15 16,10 11))')))," +
        "st_polygonFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List("POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))", null)
    )
  }

  test("st_mPointFromGeoJSON(String)") {
    executeQueryCheck(
      "select st_mPointFromGeoJSON(st_asGeoJSON(st_mPointFromWKT('MULTIPOINT(3.5 5.6, 4.8 10.5)')))," +
        " st_mPointFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List("MULTIPOINT ((3.5 5.6), (4.8 10.5))", null)
    )
  }

  test("st_mLineStringFromGeoJSON(String)") {
    executeQueryCheck(
      "select st_mLineStringFromGeoJSON(st_asGeoJSON(" +
        "st_mLineStringFromWKT('MULTILINESTRING((3 4,1 5,2 5),(-5 -8,-10 -8,-15 -4))')))," +
        "st_mLineStringFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List("MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))", null)
    )
  }

  test("st_mPolygonFromGeoJSON(String)") {
    executeQueryCheck(
      "select st_mPolygonFromGeoJSON(st_asGeoJSON(" +
        "st_mPolygonFromWKT('MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2)),((6 3,9 2,9 4,6 3)))')))," +
        "st_mPolygonFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List(
        "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))",
        null
      )
    )
  }

  test("st_geomFromGeoJSON(String)") {
    executeQueryCheck(
      "select st_geomFromGeoJSON(st_asGeoJSON(st_makePoint(1, 2)))",
      List("POINT (1 2)")
    )
  }

  test("st_asGeoJSON(geom)") {
    executeQueryCheck(
      "select st_asGeoJSON(st_makePoint(1, 2))",
      List(
        "{\"type\":\"Point\",\"coordinates\":[1,2],\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"EPSG:4326\"}}}"
      )
    )
  }

  test("st_pointFromWKT(WKT)") {
    executeQueryCheck(
      "select st_pointFromWKT('POINT(2 3)'), st_pointFromWKT('LINESTRING(0 0,1 1,1 2)')",
      List("POINT (2 3)", null)
    )
  }

  test("st_lineStringFromWKT(WKT)") {
    executeQueryCheck(
      "select st_lineStringFromWKT('LINESTRING(0 0,1 1,1 2)'), st_lineStringFromWKT('POINT(2 3)')",
      List("LINESTRING (0 0, 1 1, 1 2)", null)
    )
  }

  test("st_polygonFromWKT(WKT)") {
    executeQueryCheck(
      "select st_polygonFromWKT('POLYGON((10 11,12 12,13 14,15 16,10 11))'), " +
        "st_polygonFromWKT('POINT(2 3)')",
      List("POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))", null)
    )
  }

  test("st_mPointFromWKT(WKT)") {
    executeQueryCheck(
      "select st_mPointFromWKT('MULTIPOINT(3.5 5.6, 4.8 10.5)'), " +
        "st_mPointFromWKT('POINT(2 3)')",
      List("MULTIPOINT ((3.5 5.6), (4.8 10.5))", null)
    )
  }

  test("st_mLineStringFromWKT(WKT)") {
    executeQueryCheck(
      "select st_mLineStringFromWKT('MULTILINESTRING((3 4,1 5,2 5),(-5 -8,-10 -8,-15 -4))'), " +
        "st_mLineStringFromWKT('POINT(2 3)')",
      List("MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))", null)
    )
  }

  test("st_mPolygonFromWKT(WKT)") {
    executeQueryCheck(
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

  test("st_geomFromWKT(WKT)") {
    executeQueryCheck(
      "select st_geomFromWKT('GEOMETRYCOLLECTION(POINT(4 6),LINESTRING(4 6,7 10))')",
      List("GEOMETRYCOLLECTION (POINT (4 6), LINESTRING (4 6, 7 10))")
    )
  }

  test("st_asWKT(geom)") {
    executeQueryCheck("select st_asWKT(st_makePoint(1, 2))", List("POINT (1 2)"))
  }

  test("st_castToPoint(geom)") {
    executeQueryCheck(
      "select st_castToPoint(st_makePoint(1, 2)), " +
        "st_castToPoint(st_makeBBox(1, 2, 3, 4))",
      List("POINT (1 2)", null)
    )
  }

  test("st_castToLineString(geom)") {
    executeQueryCheck(
      "select st_castToLineString(st_geomFromWKT('LINESTRING(0 0,1 1,1 2)'))," +
        " st_castToLineString(st_makeBBox(1, 2, 3, 4))",
      List("LINESTRING (0 0, 1 1, 1 2)", null)
    )
  }

  test("st_castToPolygon(geom)") {
    executeQueryCheck(
      "select st_castToPolygon(st_geomFromWKT('POLYGON((10 11,12 12,13 14,15 16,10 11))'))" +
        ", st_castToPolygon(st_makePoint(1, 2))",
      List("POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))", null)
    )
  }

  test("st_castToMPoint(geom)") {
    executeQueryCheck(
      "select st_castToMPoint(st_geomFromWKT('MULTIPOINT(3.5 5.6, 4.8 10.5)'))" +
        ", st_castToMPoint(st_makeBBox(1, 2, 3, 4))",
      List("MULTIPOINT ((3.5 5.6), (4.8 10.5))", null)
    )
  }

  test("st_castToMLineString(geom)") {
    executeQueryCheck(
      "select st_castToMLineString(st_geomFromWKT('MULTILINESTRING((3 4,1 5,2 5),(-5 -8,-10 -8,-15 -4))'))" +
        ", st_castToMLineString(st_makeBBox(1, 2, 3, 4))",
      List("MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))", null)
    )
  }

  test("st_castToMPolygon(geom)") {
    executeQueryCheck(
      "select st_castToMPolygon(st_geomFromWKT" +
        "('MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2)),((6 3,9 2,9 4,6 3)))')), " +
        "st_castToMPolygon(st_makeBBox(1, 2, 3, 4))",
      List(
        "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))",
        null
      )
    )
  }

  test("st_castToGeometry(geom)") {
    executeQueryCheck(
      "select st_castToGeometry(st_lineStringFromWKT('LINESTRING(0 0,1 1,1 2)'))",
      List("LINESTRING (0 0, 1 1, 1 2)")
    )
  }

  test("st_pointFromWKB(WKB)") {
    executeQueryCheck("select st_pointFromWKB(st_asWKB(st_makePoint(1, 2)))", List("POINT (1 2)"))
  }

  test("st_lineStringFromWKB(WKB)") {
    executeQueryCheck(
      "select st_lineStringFromWKB(st_asWKB(st_lineStringFromWKT('LINESTRING(0 0,1 1,1 2)'))), " +
        "st_lineStringFromWKB(st_asWKB(st_makePoint(1, 2)))",
      List("LINESTRING (0 0, 1 1, 1 2)", null)
    )
  }

  test("st_polygonFromWKB(WKB)") {
    executeQueryCheck(
      "select st_polygonFromWKB(st_asWKB(st_polygonFromWKT('POLYGON((10 11,12 12,13 14,15 16,10 11))')))," +
        " st_polygonFromWKB(st_asWKB(st_makePoint(1, 2)))",
      List("POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))", null)
    )
  }

  test("st_mPointFromWKB(WKB)") {
    executeQueryCheck(
      "select st_mPointFromWKB(st_asWKB(st_mPointFromWKT('MULTIPOINT(3.5 5.6, 4.8 10.5)')))," +
        " st_mPointFromWKB(st_asWKB(st_makePoint(1, 2)))",
      List("MULTIPOINT ((3.5 5.6), (4.8 10.5))", null)
    )
  }

  test("st_mLineStringFromWKB(WKB)") {
    executeQueryCheck(
      "select st_mLineStringFromWKB(st_asWKB(" +
        "st_mLineStringFromWKT('MULTILINESTRING((3 4,1 5,2 5),(-5 -8,-10 -8,-15 -4))')))," +
        "st_mLineStringFromWKB(st_asWKB(st_makePoint(1, 2)))",
      List("MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))", null)
    )
  }

  test("st_mPolygonFromWKB(WKB)") {
    executeQueryCheck(
      "select st_mPolygonFromWKB(st_asWKB(" +
        "st_mPolygonFromWKT('MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2)),((6 3,9 2,9 4,6 3)))'))), " +
        "st_mPolygonFromWKB(st_asWKB(st_makePoint(1, 2)))",
      List(
        "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))",
        null
      )
    )
  }

  test("st_geomFromWKB(WKB)&st_asWKB(geom)") {
    executeQueryCheck("select st_geomFromWKB(st_asWKB(st_makePoint(1, 2)))", List("POINT (1 2)"))
  }

  test("st_pointFromGeoHash(str, precision)") {
    executeQueryCheck("select st_pointFromGeoHash('s10', 12)", List("POINT (2.8125 7.03125)"))
  }

  test("st_geomFromGeoHash(str, precision)") {
    executeQueryCheck(
      "select st_geomFromGeoHash('s10', 12)",
      List("POLYGON ((0 5.625, 0 8.4375, 5.625 8.4375, 5.625 5.625, 0 5.625))")
    )
  }

  test("st_asGeoHash(geom, precision)") {
    executeQueryCheck(
      "select st_asGeoHash(st_geomFromWKT('MULTIPOINT(3.5 5.6, 4.8 10.5)'), 12)",
      List("s10")
    )
  }
}
