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

package org.urbcomp.cupid.db.spark.ds.remote

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.scalatest.FunSuite

class RemoteTableTest extends FunSuite {

  test("remote source") {
    val conf = new SparkConf()

    val session = SparkSession
      .builder()
      .master("local[*]")
      .appName("jimo")
      .config(conf)
      .getOrCreate()

    val df =
      session.createDataFrame(Seq(("a", "a2"), ("b", "b2"), ("c", "c2"))).toDF("col1", "col2")

    df.coalesce(2)
      .write
      .format("org.urbcomp.cupid.db.spark.ds.remote.RemoteWriteSource")
      .mode(SaveMode.Overwrite)
      .option("op1", "v1")
      .option("op2", "v2")
      .save()
  }
}
