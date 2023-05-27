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

import org.junit.Assert.assertNotNull
import org.urbcomp.cupid.db.tools.CitibikeDataUtils

import java.util.UUID

class LoadTest extends AbstractCalciteSparkFunctionTest {
  test("test load") {
    val path = CitibikeDataUtils.getProjectRoot + "/cupid-db-test/cupid-db-test-geomesa-geotools/src/main/resources/" + "202204-citibike-tripdata_clip_slice.csv"
    val randTableName = s"Table_${UUID.randomUUID().toString.replace("-", "_")}"
    val loadSql =
      s"""LOAD CSV INPATH $path TO $randTableName (
         |  idx idx, ride_id ride_id,
         |  rideable_type rideable_type,
         |  start_point st_makePoint(start_lat, start_lng))
         |  WITH HEADER""".stripMargin
    val querySql = s"select count(1) from $randTableName"

    val stmt = connect.createStatement()
    stmt.execute(loadSql)

    val resultSet = stmt.executeQuery(querySql)
    assertNotNull(resultSet)
  }
}
