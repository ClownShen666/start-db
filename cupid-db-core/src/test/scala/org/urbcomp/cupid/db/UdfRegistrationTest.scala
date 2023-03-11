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

import org.junit.Assert.{assertNull, assertTrue}
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory

/**
  * Udf Registration Test
  *
  * @author Hang Wu
  * @date 2023-03-08
  */
class UdfRegistrationTest extends AbstractCalciteFunctionTest {

  val trajectory: Trajectory = ModelGenerator.generateTrajectory()
  val tGeo: String = trajectory.toGeoJSON

  test("udf registration test2") {
    val statement = connect.createStatement()
    statement.executeUpdate("DROP TABLE IF EXISTS test2")
    statement.executeUpdate("create table test2 (dat Int)")
    statement.executeUpdate("insert into table test2 values (1)")
    statement.executeUpdate("insert into table test2 values (2)")
    statement.executeUpdate("insert into table test2 values (7)")
    val resultSet =
      statement.executeQuery("select dat, AddOne(dat) from test2")
    while (resultSet.next()) {
      System.out.println(resultSet.getObject(1).toString)
    }
  }
}
