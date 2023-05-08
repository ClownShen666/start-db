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
package org.urbcomp.cupid.db.spark.mapmatch

import org.scalatest.FunSuite
import org.urbcomp.cupid.db.model.roadnetwork.RoadSegment
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.spark.SparkQueryExecutor
import org.urbcomp.cupid.db.spark.model.roadSegmentEncoder

import scala.collection.convert.ImplicitConversions._

class MapMatchTest extends FunSuite {
  val rs: List[RoadSegment] = ModelGenerator.generateRoadSegments().toList
  val traj: Trajectory = ModelGenerator.generateTrajectory()

  test("test map matching") {
    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    import spark.implicits._
    val roadsRDD = spark.sparkContext.parallelize(rs)
    val roadDf = roadsRDD.toDF("roads").coalesce(2)
    val trajRdd = spark.sparkContext.parallelize(Seq((1, traj), (2, traj), (3, traj)))
    val trajDf = trajRdd.toDF("id", "trajectory").coalesce(2)
    trajDf.createOrReplaceTempView("trajDf")
    roadDf.createOrReplaceTempView("roadDf")

    val df = spark.sql(
      "select st_traj_mapMatch(t2.t, t1.trajectory) from trajDf t1," +
        "(select St_rn_makeRoadNetwork(collect_list(roads)) as t from roadDf) as t2"
    )
    df.show()
    spark.close()
  }

}
