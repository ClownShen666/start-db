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
package org.urbcomp.cupid.db.spark

import org.apache.spark.sql.catalyst.parser.LegacyTypeStringParser
import org.apache.spark.sql.types.{AbstractDataType, DataType, StructType}
import org.apache.spark.sql.{DataFrame, SaveMode}
import org.junit.Assert.assertEquals
import org.scalatest.FunSuite
import org.locationtech.jts.geom._
import org.locationtech.geomesa.spark.jts._
import org.urbcomp.cupid.db.config.DynamicConfig
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.model.sample.ModelGenerator
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.apache.spark.sql.types.AbstractDataType
import org.urbcomp.cupid.db.spark.SparkQueryExecutor.log
import org.urbcomp.cupid.db.spark.reader.SparkDataReadRedis

class SparkCupidTypeTest extends FunSuite {
  val trajectory: Trajectory = ModelGenerator.generateTrajectory()
  val rn: RoadNetwork = ModelGenerator.generateRoadNetwork()
  val rs: RoadSegment = ModelGenerator.generateRoadSegment()

  test("geomesa point type test") {
    val spark = SparkQueryExecutor.getSparkSession(
      isLocal = true,
      Some(
        SparkQueryExecutor.RedisConf(
          DynamicConfig.getSparkRedisHost,
          DynamicConfig.getSparkRedisPort,
          DynamicConfig.getSparkRedisAuth
        )
      )
    )
    val point: Point = new GeometryFactory().createPoint(new Coordinate(3.4, 5.6))
    val df = spark.createDataset(Seq(point)).toDF("points")
    assertEquals(point, df.select("points").as[Point].collect.toList.head)
    spark.stop()
  }

  test("cupid functionRegistry test") {
    val spark =
      SparkQueryExecutor.getSparkSession(
        isLocal = true,
        Some(
          SparkQueryExecutor.RedisConf(
            DynamicConfig.getSparkRedisHost,
            DynamicConfig.getSparkRedisPort,
            DynamicConfig.getSparkRedisAuth
          )
        )
      )
    val className = spark.sessionState.functionRegistry.getClass.getCanonicalName
    assertEquals("FullFunctionRegistry", className.split("\\.").last)
    spark.stop()
  }

  test("cupid road segment type test 2") {
    val spark = SparkQueryExecutor.getSparkSession(
      isLocal = true,
      Some(
        SparkQueryExecutor.RedisConf(
          DynamicConfig.getSparkRedisHost,
          DynamicConfig.getSparkRedisPort,
          DynamicConfig.getSparkRedisAuth
        )
      )
    )
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

  test("cupid local redis test") {
    val spark = SparkQueryExecutor.getSparkSession(
      isLocal = true,
      Some(
        SparkQueryExecutor.RedisConf(
          DynamicConfig.getSparkRedisHost,
          DynamicConfig.getSparkRedisPort,
          DynamicConfig.getSparkRedisAuth
        )
      )
    )
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(
      Seq((1, new GeometryFactory().createPoint(new Coordinate(3.4, 5.6))))
    )
    val df: DataFrame = rdd.toDF("a", "b")
    log.info(df.schema.json)
    val schemaJson = df.schema.json
    val schemaDf = List(schemaJson).toDF()
    schemaDf
      .coalesce(1)
      .write
      .format("org.apache.spark.sql.redis")
      .option("table", DynamicConfig.getResultSchemaName("sql001"))
      .mode(SaveMode.Overwrite)
      .save()
    df.coalesce(1)
      .write
      .format("org.apache.spark.sql.redis")
      .option("table", DynamicConfig.getResultDataName("sql001"))
      .mode(SaveMode.Overwrite)
      .option("model", "binary")
      .save()
    val redisSchema = spark.read
      .format("org.apache.spark.sql.redis")
      .option("table", DynamicConfig.getResultSchemaName("sql001"))
      .load
    redisSchema.printSchema()
    redisSchema.show(truncate = false)
    val redisStructType =
      new SparkDataReadRedis().readSchema(spark, DynamicConfig.getResultSchemaName("sql001"))
    val redisData = spark.read
      .format("org.apache.spark.sql.redis")
      .schema(redisStructType)
      .option("table", DynamicConfig.getResultDataName("sql001"))
      .option("model", "binary")
      .load
    redisData.printSchema()
    redisData.show(truncate = false)
    spark.stop()
  }

  test("cupid local redis test 2") {
    val spark = SparkQueryExecutor.getSparkSession(
      isLocal = true,
      Some(
        SparkQueryExecutor.RedisConf(
          DynamicConfig.getSparkRedisHost,
          DynamicConfig.getSparkRedisPort,
          DynamicConfig.getSparkRedisAuth
        )
      )
    )
    import spark.implicits._
    val rdd = spark.sparkContext.parallelize(Seq((1, rs)))
    val df: DataFrame = rdd.toDF("a", "b")
    val schemaJson = df.schema.json
    val schemaDf = List(schemaJson).toDF()
    schemaDf
      .coalesce(1)
      .write
      .format("org.apache.spark.sql.redis")
      .option("table", DynamicConfig.getResultSchemaName("sql000"))
      .mode(SaveMode.Overwrite)
      .save()
    df.coalesce(1)
      .write
      .format("org.apache.spark.sql.redis")
      .option("table", DynamicConfig.getResultDataName("sql000"))
      .mode(SaveMode.Overwrite)
      .option("model", "binary")
      .save()
    val redisSchema = spark.read
      .format("org.apache.spark.sql.redis")
      .option("table", DynamicConfig.getResultSchemaName("sql000"))
      .load
    redisSchema.printSchema()
    redisSchema.show(truncate = false)
    val redisStructType =
      new SparkDataReadRedis().readSchema(spark, DynamicConfig.getResultSchemaName("sql000"))
    val redisData = spark.read
      .schema(redisStructType)
      .format("org.apache.spark.sql.redis")
      .option("table", DynamicConfig.getResultDataName("sql000"))
      .option("model", "binary")
      .load
    redisData.printSchema()
    redisData.show(truncate = false)
    spark.stop()
  }
}
