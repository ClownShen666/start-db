package org.urbcomp.cupid.db.spark.ds.remote

import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.scalatest.FunSuite

class RemoteTableTest extends FunSuite {

  test("remote source") {
    val conf = new SparkConf()

    val session = SparkSession.builder()
      .master("local[*]")
      .appName("jimo")
      .config(conf)
      .getOrCreate()

    val df = session.createDataFrame(Seq(
      ("a", "a2"),
      ("b", "b2"),
      ("c", "c2")
    )).toDF("col1", "col2")

    df.coalesce(1)
      .write
      .format("org.urbcomp.cupid.db.spark.ds.remote.RemoteWriteSource")
      .mode(SaveMode.Overwrite)
      .option("op1", "v1")
      .option("op2", "v2")
      .save()
  }
}
