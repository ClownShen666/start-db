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

import lombok.extern.slf4j.Slf4j
import org.apache.spark.SparkConf
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.SparkSession
import org.locationtech.geomesa.spark.GeoMesaSparkKryoRegistrator
import org.urbcomp.cupid.db.metadata.MetadataAccessUtil
import org.urbcomp.cupid.db.spark.res.SparkResultExporterFactory
import org.urbcomp.cupid.db.util.{LogUtil, MetadataUtil, SparkSqlParam}
import org.urbcomp.cupid.db.udf.UdfFactory
import org.slf4j.Logger

import org.urbcomp.cupid.db.udf.DataEngine.Spark
import org.locationtech.jts.geom._
import org.locationtech.geomesa.spark.jts._

@Slf4j
object SparkQueryExecutor {
  val log: Logger = LogUtil.getLogger

  def execute(param: SparkSqlParam, sparkSession: SparkSession = null): Unit = {
    if (param != null) SparkSqlParam.CACHE.set(param)
    var spark = sparkSession

    if (spark == null)
      spark = getSparkSession(
        param.isLocal,
        enableHiveSupport = param.isEnableHiveSupport,
        withJTS = param.isWithJTS
      )

    val sql = param.getSql
    try {
      CupidSparkTableExtractVisitor.getTableList(sql).foreach { i =>
        val userName = SparkSqlParam.CACHE.get().getUserName
        val dbTableNames = i.split("\\.")
        val dbName = dbTableNames(0)
        val tableName = dbTableNames(1)
        val catalogName = MetadataUtil.makeCatalog(userName, dbName)
        val table = MetadataAccessUtil.getTable(userName, dbName, tableName)
        if (table == null) throw new IllegalArgumentException("Table Not Exists: " + i)
        val sft = MetadataUtil.makeSchemaName(table.getId)
        loadTable(tableName, sft, catalogName, spark, param.getHbaseZookeepers)
      }
      val df = spark.sql(sql)
      SparkResultExporterFactory.getInstance(param.getExportType).exportData(param, df)
    } finally {
      spark.stop()
      SparkSession.clearActiveSession()
    }
  }

  def loadTable(
      tableName: String,
      baseName: String,
      catalogName: String,
      sparkSession: SparkSession,
      hbaseZookeepers: String
  ): Unit = {
    sparkSession.read
      .format("geomesa")
      .options(Map("hbase.catalog" -> catalogName, "hbase.zookeepers" -> hbaseZookeepers))
      .option("geomesa.feature", baseName)
      .load()
      .createTempView(tableName)
  }

  def getSparkSession(
      isLocal: Boolean,
      enableHiveSupport: Boolean,
      withJTS: Boolean
  ): SparkSession = {
    val builder = SparkSession.builder().config(buildSparkConf()).appName("Cupid-SPARK")
    if (isLocal) builder.master("local[*]")
    if (enableHiveSupport) builder.enableHiveSupport()
    var spark = builder.getOrCreate()
    if (withJTS) spark = spark.withJTS
    new UdfFactory().getUdfMap(Spark).foreach {
      case (name, clazz) =>
        spark.sql("CREATE OR REPLACE TEMPORARY FUNCTION " + name + " as '" + clazz.getName + "'")
        log.info("Spark registers udf " + name)
    }
    new UdfFactory().getUdtfMap(Spark).foreach {
      case (name, clazz) => {
        spark.sql("CREATE OR REPLACE TEMPORARY FUNCTION " + name + " as '" + clazz.getName + "'")
        log.info("Spark registers udtf " + name)
      }
    }
    spark
  }

  def buildSparkConf(): SparkConf = {
    val conf = new SparkConf()
    conf.set("spark.sql.adaptive.enabled", "true")
    conf.set("spark.sql.crossJoin.enabled", "true")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    conf.set("spark.kryo.registrator", classOf[GeoMesaSparkKryoRegistrator].getName)
    conf.set("spark.kryoserializer.buffer.max", "256m")
    conf.set("spark.kryoserializer.buffer", "64m")
    conf
  }

}
