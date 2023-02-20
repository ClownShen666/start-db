/*
 * Copyright 2022 ST-Lab
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */

package org.urbcomp.cupid.db

import org.junit.Assert.assertEquals
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import scala.collection.mutable.ListBuffer

/**
 * Clustering Test
 *
 * @author Hang Wu
 * @date 2023-02-11
 */
class ClusteringTest extends AbstractCalciteFunctionTest {

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
    var results = ListBuffer[String]()
    while (resultSet.next()) {
      results += resultSet.getObject(1).toString
    }
    assertEquals(results.size, 2)
    val sortedResults = results.toList.sorted
    assertEquals("MULTIPOINT ((1 2), (1.00001 2.00001))", sortedResults(0))
    assertEquals("MULTIPOINT ((1.00003 2.00002), (1.00004 2.00003))", sortedResults(1))
  }

}
