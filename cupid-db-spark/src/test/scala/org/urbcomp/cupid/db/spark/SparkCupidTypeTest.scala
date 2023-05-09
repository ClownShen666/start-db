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
package org.urbcomp.cupid.db.spark

import org.junit.Assert.assertEquals
import org.scalatest.FunSuite
import org.locationtech.jts.geom._
import org.locationtech.geomesa.spark.jts._
import org.locationtech.geomesa.utils.geohash.GeoHashIterator.geometryFactory
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.spark.model._

import java.sql.Timestamp
import scala.collection.JavaConverters.seqAsJavaList

class SparkCupidTypeTest extends FunSuite {
  val trajectory: Trajectory = ModelGenerator.generateTrajectory()
  val rn: RoadNetwork = ModelGenerator.generateRoadNetwork()
  val rs: RoadSegment = ModelGenerator.generateRoadSegment()

  test("geomesa point type test") {
    val spark =
      SparkQueryExecutor.getSparkSession(isLocal = true)
    val point: Point = new GeometryFactory().createPoint(new Coordinate(3.4, 5.6))
    val df = spark.createDataset(Seq(point)).toDF("points")
    assertEquals(point, df.select("points").as[Point].collect.toList.head)
    spark.stop()
  }

  test("cupid trajectory type test") {
    val spark =
      SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(trajectory))
    val df = rdd.toDF("traj")
    assertEquals(trajectory, df.select("traj").as[Trajectory].collect.toList.head)
    spark.stop()
  }

  test("cupid road network type test") {
    val spark =
      SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(rn))
    val df = rdd.toDF("roadNetwork")
    assertEquals(rn, df.select("roadNetwork").as[RoadNetwork].collect.toList.head)
    spark.stop()
  }

  test("cupid road segment type test") {
    val spark =
      SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(rs, rs))
    val df = rdd.toDF("roadSegment")
    assertEquals(rs, df.select("roadSegment").as[RoadSegment].collect.toList.head)
    spark.stop()
  }

  test("cupid functionRegistry test") {
    val spark =
      SparkQueryExecutor.getSparkSession(isLocal = true)
    val className = spark.sessionState.functionRegistry.getClass.getCanonicalName
    assertEquals("FullFunctionRegistry", className.split("\\.").last)
    spark.stop()
  }

  test("cupid road segment type test 2") {
    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq((1, rs)))
    val df = rdd.toDF("a", "b")
    val li = df.select("a", "b").as[(Int, RoadSegment)].collect.toList
    assertEquals(1, li.size)
    assertEquals((1, rs), li.head)
    df.printSchema()
    df.registerTempTable("ttt")
    spark.sql("desc ttt").show()
    spark.stop()
  }

  test("st_traj_stayPointDetect spark test") {
    val trajectoryStp: Trajectory =
      ModelGenerator.generateTrajectory("data/stayPointSegmentationTraj.txt")
    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(trajectoryStp))
    val df = rdd.toDF("traj")
    val df2 = df.selectExpr("st_traj_stayPointDetect(traj, 10, 10)")
    df2.createOrReplaceTempView("table1")
    val df3 = spark.sql("select starttime, endtime, st_mPointFromWKT(gpspoints) from table1")
    val li = df3
      .as[(Timestamp, Timestamp, MultiPoint)]
      .collect
      .toList
      .map(x => (x._1.toString, x._2.toString, x._3.toString))
    val expected = List(
      (
        "2018-10-09 07:28:24.0",
        "2018-10-09 07:28:39.0",
        "MULTIPOINT ((108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822))"
      ),
      (
        "2018-10-09 07:30:01.0",
        "2018-10-09 07:30:15.0",
        "MULTIPOINT ((108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891))"
      )
    )
    assertEquals(expected.sorted, li.sorted)
    spark.stop()
  }

  test("st_traj_stayPointDetect spark test 2") {
    val trajectoryStp: Trajectory =
      ModelGenerator.generateTrajectory("data/stayPointSegmentationTraj.txt")
    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(trajectoryStp, trajectoryStp))
    val df = rdd.toDF("traj")
    df.createOrReplaceTempView("table1")
    val df3 = spark.sql(
      "select f.starttime, f.endtime, st_mPointFromWKT(f.gpspoints) from (select st_traj_stayPointDetect(traj, 10, 10) from table1) as f"
    )
    df3.show()
    val li = df3
      .as[(Timestamp, Timestamp, MultiPoint)]
      .collect
      .toList
      .map(x => (x._1.toString, x._2.toString, x._3.toString))
    val expected = List(
      (
        "2018-10-09 07:28:24.0",
        "2018-10-09 07:28:39.0",
        "MULTIPOINT ((108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822))"
      ),
      (
        "2018-10-09 07:28:24.0",
        "2018-10-09 07:28:39.0",
        "MULTIPOINT ((108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822), (108.99552 34.27822))"
      ),
      (
        "2018-10-09 07:30:01.0",
        "2018-10-09 07:30:15.0",
        "MULTIPOINT ((108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891))"
      ),
      (
        "2018-10-09 07:30:01.0",
        "2018-10-09 07:30:15.0",
        "MULTIPOINT ((108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891), (108.99546 34.26891))"
      )
    )
    assertEquals(expected.sorted, li.sorted)
    spark.stop()
  }

  test("st_traj_timeIntervalSegment") {
    val nameArray: Array[String] = Array[String]("int", "str", "double", "point")
    val typeArray: Array[String] = Array[String]("Integer", "String", "Double", "Point")
    val trajectory: Trajectory =
      ModelGenerator.generateTrajectory(seqAsJavaList(nameArray), seqAsJavaList(typeArray))
    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(trajectory))
    val df1 = rdd.toDF("traj")
    df1.createOrReplaceTempView("table1")
    val df2 = spark.sql(
      "select d.subtrajectory from (select st_traj_timeIntervalSegment(traj, 2) from table1) as d"
    )
    var count = 0;
    df2
      .as[String]
      .collect
      .toList
      .foreach(s => {
        count += Trajectory.fromGeoJSON(s).getGPSPointList.size()
      })
    assertEquals(trajectory.getGPSPointList.size, count)
    spark.stop()
  }

  test("DBSCANClustering") {
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
    val df = rdd.toDF("points")
    df.createOrReplaceTempView("dbscan_test1")
    val df3 = spark.sql(
      "select st_dbscan_clustering(t1, 1.6, 2) " +
        "from " +
        "(select collect_list(points) as t1 from dbscan_test1)"
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

}
