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
package org.urbcomp.start.db.spark.ds.remote

import io.grpc.inprocess.InProcessServerBuilder
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.scalatest.FunSuite
import org.urbcomp.start.db.spark.data.{RemoteServer, RemoteServiceImpl}
import org.urbcomp.start.db.util.SparkSqlParam

class RemoteTableTest extends FunSuite {

  test("remote source") {
    val serverName = InProcessServerBuilder.generateName()
    val server = new RemoteServer(
      InProcessServerBuilder.forName(serverName).addService(new RemoteServiceImpl).build()
    )
    server.start()

    val conf = new SparkConf()

    val session = SparkSession
      .builder()
      .master("local[*]")
      .appName("jimo")
      .config(conf)
      .getOrCreate()

    val df =
      session.createDataFrame(Seq(("a", "a2"), ("b", "b2"), ("c", "c2"))).toDF("col1", "col2")

    df.coalesce(1)
      .write
      .format("org.urbcomp.start.db.spark.ds.remote.RemoteWriteSource")
      .mode(SaveMode.Overwrite)
      .option(RemoteWriteSource.SCHEMA_KEY, df.schema.json)
      .option(SparkSqlParam.SQL_ID_KEY, "sqlIdYa")
      .option("InProcessChannelForTest", serverName)
      .save()
  }

  test("test StructType serialize") {
    import org.apache.spark.sql.types._

    val structType = StructType(
      Seq(
        StructField("name", StringType),
        StructField("age", IntegerType),
        StructField("email", StringType)
      )
    )

    // {
    // "type":"struct",
    // "fields":
    // [{"name":"name","type":"string","nullable":true,"metadata":{}},
    // {"name":"age","type":"integer","nullable":true,"metadata":{}},
    // {"name":"email","type":"string","nullable":true,"metadata":{}}]
    // }
    val json = structType.json

    val structType1 = DataType.fromJson(json).asInstanceOf[StructType]

    assertResult(structType.length)(structType1.length)
  }

  test("grpc test") {}
}
