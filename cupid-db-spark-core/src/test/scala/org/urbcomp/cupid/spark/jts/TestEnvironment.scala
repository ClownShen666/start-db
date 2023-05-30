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

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row, SQLContext, SparkSession}

trait TestEnvironment {

  implicit lazy val spark: SparkSession = {
    SparkSession
      .builder()
      .appName("testSpark")
      .master("local[*]")
      .getOrCreate()
      .withCupid
  }

  lazy val sc: SQLContext = spark.sqlContext.withCupid // <-- this should be a noop given the above, but is here to test that code path

  /**
    * Constructor for creating a DataFrame with a single row and no columns.
    * Useful for testing the invocation of data constructing UDFs.
    */
  def dfBlank(implicit spark: SparkSession): DataFrame = {
    // This is to enable us to do a single row creation select operation in DataFrame
    // world. Probably a better/easier way of doing this.
    spark.createDataFrame(spark.sparkContext.makeRDD(Seq(Row())), StructType(Seq.empty))
  }

}
