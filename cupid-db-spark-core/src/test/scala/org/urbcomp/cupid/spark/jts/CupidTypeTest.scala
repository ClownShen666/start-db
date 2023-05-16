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
package org.urbcomp.cupid.spark.jts

import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory

@RunWith(classOf[JUnitRunner])
class CupidTypeTest extends Specification with TestEnvironment {

  val trajectory: Trajectory = ModelGenerator.generateTrajectory()
  val rn: RoadNetwork = ModelGenerator.generateRoadNetwork()
  val rs: RoadSegment = ModelGenerator.generateRoadSegment()

  "cupid jts module" should {

    "create a df from trajectory" >> {
      import spark.implicits._
      val rdd = spark.sparkContext.parallelize(Seq(trajectory))
      val df = rdd.toDF("traj")
      trajectory mustEqual df.select("traj").as[Trajectory].collect().toList.head
    }

    "create a df from road network" >> {
      import spark.implicits._
      val rdd = spark.sparkContext.parallelize(Seq(rn))
      val df = rdd.toDF("roadNetwork")
      rn mustEqual df.select("roadNetwork").as[RoadNetwork].collect.toList.head
    }

    "create a df from road segment" >> {
      import spark.implicits._
      val rdd = spark.sparkContext.parallelize(Seq(rs, rs))
      val df = rdd.toDF("roadSegment")
      rs mustEqual df.select("roadSegment").as[RoadSegment].collect.toList.head
    }

  }

}
