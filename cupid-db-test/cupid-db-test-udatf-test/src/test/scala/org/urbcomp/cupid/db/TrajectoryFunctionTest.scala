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
import org.locationtech.jts.geom.MultiPoint
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.spark.SparkQueryExecutor
import scala.collection.JavaConverters.seqAsJavaList
import java.sql.Timestamp
import org.urbcomp.cupid.spark.jts._

class TrajectoryFunctionTest extends AbstractCalciteSparkFunctionTest {
  val nameArray: Array[String] = Array[String]("int", "str", "double", "point")
  val typeArray: Array[String] = Array[String]("Integer", "String", "Double", "Point")
  val trajectory: Trajectory =
    ModelGenerator.generateTrajectory(seqAsJavaList(nameArray), seqAsJavaList(typeArray))
  val tGeo: String = trajectory.toGeoJSON
  val trajectorySeg: Trajectory =
    ModelGenerator.generateTrajectory("data/stayPointSegmentationTraj.txt")

  test("st_traj_asGeoJSON & st_traj_fromGeoJSON") {
    executeQueryCheck("select st_traj_asGeoJSON(st_traj_fromGeoJSON(\'" + tGeo + "\'))", List(tGeo))
  }

  test("st_traj_fromGeoJSON(str)") {
    executeQueryCheck("select st_traj_fromGeoJSON(\'" + tGeo + "\')")
  }

  test("st_traj_oid(Trajectory)") {}

  test("st_traj_tid(Trajectory)") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery("select st_traj_tid(st_traj_fromGeoJSON(\'" + tGeo + "\'))")
    resultSet.next()
    assertEquals("afab91fa68cb417c2f663924a0ba1ff92018-10-09 07:28:21.0", resultSet.getObject(1))

  }

  test("st_traj_startTime(Trajectory)") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery("select st_traj_startTime(st_traj_fromGeoJSON(\'" + tGeo + "\'))")
    resultSet.next()
    assertEquals("2018-10-09 07:28:21.0", resultSet.getObject(1).toString)

  }

  test("st_traj_endTime(Trajectory)") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery("select st_traj_endTime(st_traj_fromGeoJSON(\'" + tGeo + "\'))")
    resultSet.next()
    assertEquals("2018-10-09 07:34:18.0", resultSet.getObject(1).toString)

  }

  test("st_traj_startPoint(Trajectory)") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery("select st_traj_startPoint(st_traj_fromGeoJSON(\'" + tGeo + "\'))")
    resultSet.next()
    assertEquals("POINT (108.99553 34.27859)", resultSet.getObject(1).toString)

  }

  test("st_traj_endPoint(Trajectory)") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery("select st_traj_endPoint(st_traj_fromGeoJSON(\'" + tGeo + "\'))")
    resultSet.next()
    assertEquals("POINT (108.98897 34.25815)", resultSet.getObject(1).toString)

  }

  test("st_traj_numOfPoints(Trajectory)") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery("select st_traj_numOfPoints(st_traj_fromGeoJSON(\'" + tGeo + "\'))")
    resultSet.next()
    assertEquals("117", resultSet.getObject(1).toString)

  }

  test("st_traj_pointN(Trajectory,int)") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery("select st_traj_pointN(st_traj_fromGeoJSON(\'" + tGeo + "\'),2)")
    resultSet.next()
    assertEquals("POINT (108.99552 34.27786)", resultSet.getObject(1).toString)

  }

  test("st_traj_timeN(Trajectory,int)") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery("select st_traj_timeN(st_traj_fromGeoJSON(\'" + tGeo + "\'),2)")
    resultSet.next()
    assertEquals("2018-10-09 07:28:27.0", resultSet.getObject(1).toString)

  }

  test("st_traj_lengthInKM(Trajectory)") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery("select st_traj_lengthInKM(st_traj_fromGeoJSON(\'" + tGeo + "\'))")
    resultSet.next()
    assertEquals("2.9989194858191373", resultSet.getObject(1).toString)

  }

  test("st_traj_speedInKMPerHour(Trajectory)") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery(
        "select st_traj_speedInKMPerHour(st_traj_fromGeoJSON(\'" + tGeo + "\'))"
      )
    resultSet.next()
    assertEquals("30.24120489901651", resultSet.getObject(1).toString)

  }

  test("st_traj_geom(Trajectory)") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery("select st_traj_geom(st_traj_fromGeoJSON(\'" + tGeo + "\'))")
    resultSet.next()
    assertEquals(
      "class org.locationtech.jts.geom.LineString",
      resultSet.getObject(1).getClass.toString
    )

  }

  test("st_traj_timeIntervalSegment") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery(
        "select st_traj_timeIntervalSegment(st_traj_fromGeoJSON(\'" + tGeo + "\')," + 2 + ")"
      )
    var count = 0
    while (resultSet.next()) {
      count = count + Trajectory.fromGeoJSON(resultSet.getObject(1).toString).getGPSPointList.size()
    }
    assertEquals(trajectory.getGPSPointList.size, count)

    val nameArray: Array[String] = Array[String]("int", "str", "double", "point")
    val typeArray: Array[String] = Array[String]("Integer", "String", "Double", "Point")
    val trajectory2: Trajectory =
      ModelGenerator.generateTrajectory(seqAsJavaList(nameArray), seqAsJavaList(typeArray))
    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(trajectory2))
    val df1 = rdd.toDF("traj")
    df1.createOrReplaceTempView("table1")
    val df2 = spark.sql(
      "select d.subtrajectory from (select st_traj_timeIntervalSegment(traj, 2) from table1) as d"
    )
    var count2 = 0
    df2
      .as[String]
      .collect
      .toList
      .foreach(s => {
        count2 += Trajectory.fromGeoJSON(s).getGPSPointList.size()
      })
    assertEquals(count, count2)
    spark.stop()

  }

  test("st_traj_stayPointSegment") {
    val statement = connect.createStatement()
    val tGeoSeg: String = trajectorySeg.toGeoJSON
    val resultSet =
      statement.executeQuery(
        "select st_traj_stayPointSegment(st_traj_fromGeoJSON(\'" + tGeoSeg + "\')," + 10 + "," + 10 + ")"
      )
    var count = 0
    var seg = 0
    while (resultSet.next()) {
      count = count + Trajectory.fromGeoJSON(resultSet.getObject(1).toString).getGPSPointList.size()
      seg += 1
    }
    assertEquals(seg, 3)
    assertEquals(trajectorySeg.getGPSPointList.size - 8, count)

    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(trajectorySeg))
    val df = rdd.toDF("traj")
    val df2 = df.selectExpr("st_traj_stayPointSegment(traj, 10, 10)")
    df2.createOrReplaceTempView("table1")
    val df3 = spark.sql("select d.subtrajectory from table1 as d")
    var count2 = 0
    var seg2 = 0
    df3
      .as[String]
      .collect
      .toList
      .foreach(s => {
        count2 += Trajectory.fromGeoJSON(s).getGPSPointList.size()
        seg2 += 1
      })

    assertEquals(seg, seg2)
    assertEquals(count, count2)

    spark.stop()

  }

  test("st_traj_hybridSegment") {
    val statement = connect.createStatement()
    val trajectorySeg: Trajectory =
      ModelGenerator.generateTrajectory("data/stayPointSegmentationTraj.txt")
    val tGeoSeg: String = trajectorySeg.toGeoJSON
    val resultSet =
      statement.executeQuery(
        "select st_traj_hybridSegment(st_traj_fromGeoJSON(\'" + tGeoSeg + "\')," + 10 + "," + 10 + "," + 10 + ")"
      )
    var count = 0
    var seg = 0
    while (resultSet.next()) {
      count = count + Trajectory.fromGeoJSON(resultSet.getObject(1).toString).getGPSPointList.size()
      seg += 1
    }
    assertEquals(seg, 5)
    assertEquals(trajectorySeg.getGPSPointList.size - 8, count)

    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(trajectorySeg))
    val df = rdd.toDF("traj")
    val df2 = df.selectExpr("st_traj_hybridSegment(traj, 10, 10, 10)")
    df2.createOrReplaceTempView("table1")
    val df3 = spark.sql("select SubTrajectory from table1")
    var count2 = 0
    var seg2 = 0
    df3
      .as[String]
      .collect
      .toList
      .foreach(s => {
        count2 += Trajectory.fromGeoJSON(s).getGPSPointList.size()
        seg2 += 1
      })
    assertEquals(seg, seg2)
    assertEquals(count, count2)
    spark.stop()

  }

  test("st_traj_stayPointDetect") {
    val statement = connect.createStatement()
    val trajectoryStp: Trajectory =
      ModelGenerator.generateTrajectory("data/stayPointSegmentationTraj.txt")
    val tGeoStp: String = trajectoryStp.toGeoJSON
    val resultSet1 = statement.executeQuery(
      "select st_traj_stayPointDetect(st_traj_fromGeoJSON(\'" + tGeoStp + "\'),10,10)"
    )
    resultSet1.next()
    assertEquals("2018-10-09 07:28:24.0", resultSet1.getObject(1).toString)
    assertEquals("2018-10-09 07:28:39.0", resultSet1.getObject(2).toString)
    assertEquals(
      "[POINT (108.99552 34.27822), POINT (108.99552 34.27822), POINT (108.99552 34.27822), POINT (108.99552 34.27822), POINT (108.99552 34.27822), POINT (108.99552 34.27822)]",
      resultSet1.getObject(3).toString
    )
    resultSet1.next()
    assertEquals("2018-10-09 07:30:01.0", resultSet1.getObject(1).toString)
    assertEquals("2018-10-09 07:30:15.0", resultSet1.getObject(2).toString)
    assertEquals(
      "[POINT (108.99546 34.26891), POINT (108.99546 34.26891), POINT (108.99546 34.26891), POINT (108.99546 34.26891), POINT (108.99546 34.26891), POINT (108.99546 34.26891)]",
      resultSet1.getObject(3).toString
    )

    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(trajectorySeg, trajectorySeg))
    val df = rdd.toDF("traj")
    df.createOrReplaceTempView("table1")
    val df3 = spark.sql(
      "select f.starttime, f.endtime, st_mPointFromWKT(f.gpspoints) from (select st_traj_stayPointDetect(traj, 10, 10) from table1) as f"
    )
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

  test("st_traj_noiseFilter_test1") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery(
        "select st_traj_noiseFilter(st_traj_fromGeoJSON(\'" + tGeo + "\')," + "1)"
      )
    resultSet.next()
    assertEquals(
      trajectory.getGPSPointList.subList(0, 1),
      Trajectory.fromGeoJSON(resultSet.getObject(1).toString).getGPSPointList
    )

  }
  test("st_traj_noiseFilter_test2") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery(
        "select st_traj_noiseFilter(st_traj_fromGeoJSON(\'" + tGeo + "\')," + "8.5)"
      )
    resultSet.next()
    assertEquals(
      "[POINT (108.99553 34.27859), POINT (108.99655 34.25891), POINT (108.99657 34.25875), POINT (108.99655 34.25857), POINT (108.99654 34.25837), POINT (108.99652 34.25826), POINT (108.99647 34.25821), POINT (108.99639 34.25818), POINT (108.99624 34.25818), POINT (108.99598 34.25819), POINT (108.99433 34.25818), POINT (108.99391 34.25818), POINT (108.99337 34.25817), POINT (108.99312 34.25817), POINT (108.99287 34.25817), POINT (108.9926 34.25816), POINT (108.99245 34.25816), POINT (108.9923 34.25816), POINT (108.99205 34.25815)]",
      Trajectory.fromGeoJSON(resultSet.getObject(1).toString).getGPSPointList.toString
    )

  }
  test("st_traj_noiseFilter_test3") {
    val statement = connect.createStatement()
    val resultSet =
      statement.executeQuery(
        "select st_traj_noiseFilter(st_traj_fromGeoJSON(\'" + tGeo + "\')," + "30)"
      )
    resultSet.next()
    val correctResult = trajectory.getGPSPointList
    correctResult.remove(64)
    assertEquals(
      correctResult,
      Trajectory.fromGeoJSON(resultSet.getObject(1).toString).getGPSPointList
    )

  }
}
