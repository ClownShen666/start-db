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

import org.junit.Assert.assertEquals
import org.locationtech.geomesa.utils.geohash.GeoHashIterator.geometryFactory
import org.locationtech.jts.geom.Coordinate
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.spark.SparkQueryExecutor

import scala.collection.mutable.ListBuffer

import org.locationtech.geomesa.spark.jts._

import org.urbcomp.cupid.db.spark.model._


/**
  * Clustering Test
  *
  * @author Hang Wu
  * */
class ClusteringTest extends AbstractCalciteSparkFunctionTest {

  val trajectory: Trajectory = ModelGenerator.generateTrajectory()
  val tGeo: String = trajectory.toGeoJSON

  test("dbscan test1") {
    val statement = connect.createStatement()
    statement.executeUpdate("DROP TABLE IF EXISTS dbscan_test1")
    statement.executeUpdate("create table dbscan_test1 (points point)")
    statement.executeUpdate(
      "insert into table dbscan_test1 values (st_makePoint(1.000000, 2.000000))"
    )
    statement.executeUpdate(
      "insert into table dbscan_test1 values (st_makePoint(1.000010, 2.000010))"
    )
    statement.executeUpdate(
      "insert into table dbscan_test1 values (st_makePoint(1.000030, 2.000020))"
    )
    statement.executeUpdate(
      "insert into table dbscan_test1 values (st_makePoint(1.000040, 2.000030))"
    )
    val resultSet =
      statement.executeQuery(
        "select st_dbscan_clustering(t1, 1.6, 2) " +
          "from " +
          "(select collect_list(points) as t1 from dbscan_test1)"
      )
    val results = ListBuffer[String]()
    while (resultSet.next()) {
      results += resultSet.getObject(1).toString
    }
    assertEquals(results.size, 2)
    val sortedResults = results.toList.sorted
    assert(
      sortedResults.head == "MULTIPOINT ((1 2), (1.00001 2.00001))" || sortedResults.head == "MULTIPOINT ((1.00001 2.00001), (1 2))"
    )
    assert(
      sortedResults(1) == "MULTIPOINT ((1.00003 2.00002), (1.00004 2.00003))" || sortedResults(1) == "MULTIPOINT ((1.00004 2.00003), (1.00003 2.00002))"
    )

    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(
      Seq(
        geometryFactory.createPoint(new Coordinate(1.000000, 2.000000)),
        geometryFactory.createPoint(new Coordinate(1.000010, 2.000010)),
        geometryFactory.createPoint(new Coordinate(1.000030, 2.000020)),
        geometryFactory.createPoint(new Coordinate(1.000040, 2.000030))
      )
    )
    val df = rdd.toDF("points2")
    df.createOrReplaceTempView("dbscan_test2")
    val df3 = spark.sql(
      "select st_dbscan_clustering(t1, 1.6, 2) " +
        "from " +
        "(select collect_list(points2) as t1 from dbscan_test2)"
    )
    df3.show()
    val res = df3
      .as[(String, String, String)]
      .collect
      .toList
    val expected = List(
      (
        "MULTIPOINT ((1 2), (1.00001 2.00001))",
        "POINT (1.000005 2.000005)",
        "GEOMETRYCOLLECTION EMPTY"
      ),
      (
        "MULTIPOINT ((1.00003 2.00002), (1.00004 2.00003))",
        "POINT (1.000035 2.000025)",
        "GEOMETRYCOLLECTION EMPTY"
      )
    )
    assertEquals(expected, res)
    spark.stop()
  }

  test("kmeans") {
    val statement = connect.createStatement()
    statement.executeUpdate("DROP TABLE IF EXISTS kmeans_test1")
    statement.executeUpdate("create table kmeans_test1 (points point)")
    statement.executeUpdate(
      "insert into table kmeans_test1 values (st_makePoint(1.000000, 2.000000))"
    )
    statement.executeUpdate(
      "insert into table kmeans_test1 values (st_makePoint(1.000010, 2.000010))"
    )
    statement.executeUpdate(
      "insert into table kmeans_test1 values (st_makePoint(1.000030, 2.000020))"
    )
    statement.executeUpdate(
      "insert into table kmeans_test1 values (st_makePoint(1.000040, 2.000030))"
    )
    val resultSet =
      statement.executeQuery(
        "select st_kmeans_clustering(t1, 2) " +
          "from " +
          "(select collect_list(points) as t1 from kmeans_test1)"
      )
    val results = ListBuffer[String]()
    while (resultSet.next()) {
      results += resultSet.getObject(1).toString
    }
    assertEquals(results.size, 2)
    val sortedResults = results.toList.sorted
    assert(
      sortedResults.head == "MULTIPOINT ((1 2), (1.00001 2.00001))" || sortedResults.head == "MULTIPOINT ((1.00001 2.00001), (1 2))"
    )
    assert(
      sortedResults(1) == "MULTIPOINT ((1.00003 2.00002), (1.00004 2.00003))" || sortedResults(1) == "MULTIPOINT ((1.00004 2.00003), (1.00003 2.00002))"
    )

    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(
      Seq(
        geometryFactory.createPoint(new Coordinate(1.000000, 2.000000)),
        geometryFactory.createPoint(new Coordinate(1.000010, 2.000010)),
        geometryFactory.createPoint(new Coordinate(1.000030, 2.000020)),
        geometryFactory.createPoint(new Coordinate(1.000040, 2.000030))
      )
    )
    val df = rdd.toDF("points2")
    df.createOrReplaceTempView("kmeans_test2")
    val df3 = spark.sql(
      "select st_kmeans_clustering(t1, 2) " +
        "from " +
        "(select collect_list(points2) as t1 from kmeans_test2)"
    )
    df3.show()
    val res = df3
      .as[(String, String, String)]
      .collect
      .toList
    assertEquals(2, res.size)
    val expected = List(
      (
        "MULTIPOINT ((1 2), (1.00001 2.00001))",
        "POINT (1.000005 2.000005)",
        "GEOMETRYCOLLECTION EMPTY"
      ),
      (
        "MULTIPOINT ((1.00003 2.00002), (1.00004 2.00003))",
        "POINT (1.000035 2.000025)",
        "GEOMETRYCOLLECTION EMPTY"
      )
    )
    assertEquals(expected, res)
    spark.stop()
  }

}
