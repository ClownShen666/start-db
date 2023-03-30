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
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.spark.model._

class SparkCupidTypeTest extends FunSuite {
  val trajectory: Trajectory = ModelGenerator.generateTrajectory()
  val rn: RoadNetwork = ModelGenerator.generateRoadNetwork()
  val rs: RoadSegment = ModelGenerator.generateRoadSegment()

  test("geomesa point type test") {
    val spark = SparkQueryExecutor.getSparkSession(isLocal = true, enableHiveSupport = true).withJTS
    val point: Point = new GeometryFactory().createPoint(new Coordinate(3.4, 5.6))
    val df = spark.createDataset(Seq(point)).toDF("points")
    assertEquals(point, df.select("points").as[Point].collect.toList.head)
  }

  test("cupid trajectory type test") {
    val spark =
      SparkQueryExecutor.getSparkSession(isLocal = true, enableHiveSupport = true).withCupid

    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(trajectory))
    val df = rdd.toDF("traj")
    assertEquals(trajectory, df.select("traj").as[Trajectory].collect.toList.head)
  }

  test("cupid road network type test") {
    val spark =
      SparkQueryExecutor.getSparkSession(isLocal = true, enableHiveSupport = true).withCupid

    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(rn))
    val df = rdd.toDF("roadNetwork")
    assertEquals(rn, df.select("roadNetwork").as[RoadNetwork].collect.toList.head)
  }

  test("cupid road segment type test") {
    val spark =
      SparkQueryExecutor.getSparkSession(isLocal = true, enableHiveSupport = true).withCupid

    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq(rs))
    val df = rdd.toDF("roadSegment")
    assertEquals(rs, df.select("roadSegment").as[RoadSegment].collect.toList.head)
  }
}
