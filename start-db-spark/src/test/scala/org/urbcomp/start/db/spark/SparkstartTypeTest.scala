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
package org.urbcomp.start.db.spark

import org.junit.Assert.assertEquals
import org.scalatest.FunSuite
import org.locationtech.jts.geom._
import org.locationtech.geomesa.spark.jts._
import org.urbcomp.start.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.start.db.model.sample.ModelGenerator
import org.urbcomp.start.db.model.trajectory.Trajectory

class SparkStartTypeTest extends FunSuite {
  val trajectory: Trajectory = ModelGenerator.generateTrajectory()
  val rn: RoadNetwork = ModelGenerator.generateRoadNetwork()
  val rs: RoadSegment = ModelGenerator.generateRoadSegment()

  test("geomesa point type test") {
    val spark = SparkQueryExecutor.getSparkSession(isLocal = true, None)
    val point: Point = new GeometryFactory().createPoint(new Coordinate(3.4, 5.6))
    val df = spark.createDataset(Seq(point)).toDF("points")
    assertEquals(point, df.select("points").as[Point].collect.toList.head)
    spark.stop()
  }

  test("start functionRegistry test") {
    val spark = SparkQueryExecutor.getSparkSession(isLocal = true, None)
    val className = spark.sessionState.functionRegistry.getClass.getCanonicalName
    assertEquals("FullFunctionRegistry", className.split("\\.").last)
    spark.stop()
  }

  test("start road segment type test 2") {
    val spark = SparkQueryExecutor.getSparkSession(isLocal = true, None)
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
}
