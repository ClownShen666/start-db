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

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.sql
import org.urbcomp.cupid.db.algorithm.mapmatch.tihmm.TiHmmMapMatcher
import org.urbcomp.cupid.db.algorithm.shortestpath.ManyToManyShortestPath
import org.urbcomp.cupid.db.model.roadnetwork.RoadNetwork
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.spark.SparkQueryExecutor
class MapMatch {

  def mapMatch(roadNetwork: RoadNetwork, trajDf: sql.DataFrame): sql.DataFrame = {

    val spark = SparkQueryExecutor.getSparkSession(isLocal = true)
    val partitionNum = spark.sparkContext.defaultParallelism
    val repartitionRdd = trajDf.rdd.repartition(partitionNum)

    val schema = StructType(Seq(StructField("item", DataTypes.StringType)))
    val rn = spark.sparkContext.broadcast(roadNetwork)

    val itemRdd = repartitionRdd.mapPartitions(iter => {
      val mapMatcher = new TiHmmMapMatcher(rn.value, new ManyToManyShortestPath(rn.value))
      iter
        .map(row => {
          mapMatcher.mapMatch(row.getAs[Trajectory](1)).toGeoJSON
        })
        .filter(_ != null)
        .map(Row(_))
    })
    spark.createDataFrame(itemRdd, schema)
  }

}
