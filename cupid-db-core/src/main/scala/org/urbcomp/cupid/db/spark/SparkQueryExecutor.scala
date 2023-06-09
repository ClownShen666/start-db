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
import org.apache.calcite.sql.SqlSelect
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.geotools.data.DataStoreFinder
import org.geotools.util.factory.Hints
import org.locationtech.geomesa.features.ScalaSimpleFeature
import org.locationtech.geomesa.spark.jts._
import org.locationtech.geomesa.spark.{GeoMesaSpark, GeoMesaSparkKryoRegistrator}
import org.locationtech.geomesa.utils.uuid.TimeSortedUuidGenerator
import org.opengis.feature.simple.SimpleFeature
import org.reflections.Reflections
import org.slf4j.Logger
import org.urbcomp.cupid.db.config.DynamicConfig
import org.urbcomp.cupid.db.executor.utils.ExecutorUtil
import org.urbcomp.cupid.db.metadata.MetadataAccessUtil
import org.urbcomp.cupid.db.parser.SqlHelper
import org.urbcomp.cupid.db.parser.dcl.SqlLoadData
import org.urbcomp.cupid.db.parser.driver.CupidDBParseDriver
import org.urbcomp.cupid.spark.jts._
import org.urbcomp.cupid.db.spark.res.SparkResultExporterFactory
import org.urbcomp.cupid.db.udf.DataEngine.Spark
import org.urbcomp.cupid.db.udf.{DataEngine, UdfFactory}
import org.urbcomp.cupid.db.udtf.AbstractUdtf
import org.urbcomp.cupid.db.util.{LogUtil, MetadataUtil, SparkSqlParam, SqlParam}

import java.lang.reflect.Method
import java.time.Instant
import scala.collection.JavaConverters.mapAsScalaMapConverter
import scala.collection.convert.ImplicitConversions._
import scala.reflect.api
import scala.reflect.runtime.universe._
@Slf4j
object SparkQueryExecutor {
  val log: Logger = LogUtil.getLogger

  def execute(param: SparkSqlParam, sparkSession: SparkSession = null): Unit = {
    if (param != null) {
      SparkSqlParam.CACHE.set(param)
      SqlParam.CACHE.set(param)
    }
    val spark = if (sparkSession != null) sparkSession else getSparkSession(param.isLocal)
    val sql = param.getSql
    val node = CupidDBParseDriver.parseSql(sql)

    try {
      node match {
        case node: SqlLoadData =>
          val (userName, dbName, tableName) =
            ExecutorUtil.getUserNameDbNameAndTableName(node.tableName)
          val catalogName = MetadataUtil.makeCatalog(userName, dbName)
          val table = MetadataAccessUtil.getTable(userName, dbName, tableName)
          if (table == null) {
            throw new IllegalArgumentException(s"table not exist $tableName")
          }
          val schemaName = MetadataUtil.makeSchemaName(table.getId)
          val df = if (node.hasDelimiter && node.hasQuotes) {
            spark.read
              .option("header", node.hasHeader)
              .option("delimiter", node.delimiter)
              .option("quote", node.quote)
              .options(
                Map("hbase.catalog" -> catalogName, "hbase.zookeepers" -> param.getHbaseZookeepers)
              )
              .csv(node.path)
          } else if (node.hasDelimiter) {
            spark.read
              .option("header", node.hasHeader)
              .option("delimiter", node.delimiter)
              .options(
                Map("hbase.catalog" -> catalogName, "hbase.zookeepers" -> param.getHbaseZookeepers)
              )
              .csv(node.path)
          } else if (node.hasQuotes) {
            spark.read
              .option("header", node.hasHeader)
              .option("quote", node.quote)
              .options(
                Map("hbase.catalog" -> catalogName, "hbase.zookeepers" -> param.getHbaseZookeepers)
              )
              .csv(node.path)
          } else {
            spark.read
              .option("header", node.hasHeader)
              .options(
                Map("hbase.catalog" -> catalogName, "hbase.zookeepers" -> param.getHbaseZookeepers)
              )
              .csv(node.path)
          }
          val tmpView = "load_data_tmp_" + Instant.now().toEpochMilli
          df.createOrReplaceTempView(tmpView)
          val selectSql = SqlHelper.toSqlString(SqlHelper.convertToSelectNode(node, tmpView))
          val data = spark.sql(selectSql)

          val params = ExecutorUtil.getDataStoreParams(userName, dbName)

          val rddToSave = data.rdd.mapPartitions(partition => {
            val store = DataStoreFinder.getDataStore(params)
            val sft = store.getSchema(schemaName)
            val attrs = sft.getAttributeDescriptors
            partition.map(row => {
              val rowCols = row.schema.map(s => s.name)
              val sf = new ScalaSimpleFeature(sft, TimeSortedUuidGenerator.createUuid().toString)
              attrs.forEach(attr => {
                val col = attr.getLocalName
                if (rowCols.contains(col)) {
                  val rawValue = row.getAs[Object](col)
                  sf.setAttribute(col, rawValue)
                }
              })
              sf.getUserData.put(Hints.USE_PROVIDED_FID, java.lang.Boolean.FALSE)
              sf.asInstanceOf[SimpleFeature]
            })
          })
          GeoMesaSpark(params).save(rddToSave, params.asScala.toMap, schemaName)
          SparkResultExporterFactory.getInstance(param.getExportType).exportData(param, df)

        case _: SqlSelect =>
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

        case _ => throw new UnsupportedOperationException("Unexpected sql kind " + node.getKind);
      }
    } catch {
      case e: Exception =>
        e.printStackTrace()
        throw new RuntimeException(e)
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

  def getAllMethods[T](clazz: Class[T], name: String): Array[Method] = {
    clazz.getMethods.filter(method => method.getName == name && !method.isBridge)
  }

  def getSparkSession(isLocal: Boolean): SparkSession = {
    val builder = SparkSession.builder().config(buildSparkConf()).appName("Cupid-SPARK")
    if (isLocal) builder.master("local[*]")
    val spark = builder
      .enableHiveSupport()
      .getOrCreate()
      .withJTS
      .withCupid

    new UdfFactory().getUdfMap(Spark).foreach {
      case (name, clazz) =>
        val instance = clazz.newInstance()
        val rm = runtimeMirror(clazz.getClassLoader)
        def typeToTypeTag[T](tpe: Type): TypeTag[T] =
          TypeTag(rm, new api.TypeCreator {
            def apply[U <: api.Universe with Singleton](m: api.Mirror[U]): U#Type =
              tpe.asInstanceOf[U#Type]
          })
        val udfSparkEntries: List[String] =
          clazz.getDeclaredMethod("udfSparkEntries").invoke(instance).asInstanceOf[List[String]]
        udfSparkEntries.foreach { methodName: String =>
          {
            val method = clazz.getDeclaredMethod(methodName).invoke(instance)
            val functionType =
              rm.classSymbol(clazz).typeSignature.decl(TermName(methodName)).asMethod.returnType
            method match {
              case method: Function0[rt] =>
                spark.udf.register(name, method)(typeToTypeTag[rt](functionType.typeArgs(0)))
                log.info("Spark registers udf " + name)
              case method: Function1[x1, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(1)),
                  typeToTypeTag[x1](functionType.typeArgs(0))
                )
                log.info("Spark registers udf " + name)
              case method: Function2[x1, x2, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(2)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1))
                )
                log.info("Spark registers udf " + name)
              case method: Function3[x1, x2, x3, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(3)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2))
                )
                log.info("Spark registers udf " + name)
              case method: Function4[x1, x2, x3, x4, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(4)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3))
                )
                log.info("Spark registers udf " + name)
              case method: Function5[x1, x2, x3, x4, x5, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(5)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4))
                )
                log.info("Spark registers udf " + name)
              case method: Function6[x1, x2, x3, x4, x5, x6, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(6)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5))
                )
                log.info("Spark registers udf " + name)
              case method: Function7[x1, x2, x3, x4, x5, x6, x7, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(7)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6))
                )
                log.info("Spark registers udf " + name)
              case method: Function8[x1, x2, x3, x4, x5, x6, x7, x8, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(8)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7))
                )
                log.info("Spark registers udf " + name)
              case method: Function9[x1, x2, x3, x4, x5, x6, x7, x8, x9, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(9)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8))
                )
                log.info("Spark registers udf " + name)
              case method: Function10[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(10)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9))
                )
                log.info("Spark registers udf " + name)
              case method: Function11[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(11)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9)),
                  typeToTypeTag[x11](functionType.typeArgs(10))
                )
                log.info("Spark registers udf " + name)
              case method: Function12[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(12)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9)),
                  typeToTypeTag[x11](functionType.typeArgs(10)),
                  typeToTypeTag[x12](functionType.typeArgs(11))
                )
                log.info("Spark registers udf " + name)
              case method: Function13[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, rt] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(13)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9)),
                  typeToTypeTag[x11](functionType.typeArgs(10)),
                  typeToTypeTag[x12](functionType.typeArgs(11)),
                  typeToTypeTag[x13](functionType.typeArgs(12))
                )
                log.info("Spark registers udf " + name)
              case method: Function14[
                    x1,
                    x2,
                    x3,
                    x4,
                    x5,
                    x6,
                    x7,
                    x8,
                    x9,
                    x10,
                    x11,
                    x12,
                    x13,
                    x14,
                    rt
                  ] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(14)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9)),
                  typeToTypeTag[x11](functionType.typeArgs(10)),
                  typeToTypeTag[x12](functionType.typeArgs(11)),
                  typeToTypeTag[x13](functionType.typeArgs(12)),
                  typeToTypeTag[x14](functionType.typeArgs(13))
                )
                log.info("Spark registers udf " + name)
              case method: Function15[
                    x1,
                    x2,
                    x3,
                    x4,
                    x5,
                    x6,
                    x7,
                    x8,
                    x9,
                    x10,
                    x11,
                    x12,
                    x13,
                    x14,
                    x15,
                    rt
                  ] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(15)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9)),
                  typeToTypeTag[x11](functionType.typeArgs(10)),
                  typeToTypeTag[x12](functionType.typeArgs(11)),
                  typeToTypeTag[x13](functionType.typeArgs(12)),
                  typeToTypeTag[x14](functionType.typeArgs(13)),
                  typeToTypeTag[x15](functionType.typeArgs(14))
                )
                log.info("Spark registers udf " + name)
              case method: Function16[
                    x1,
                    x2,
                    x3,
                    x4,
                    x5,
                    x6,
                    x7,
                    x8,
                    x9,
                    x10,
                    x11,
                    x12,
                    x13,
                    x14,
                    x15,
                    x16,
                    rt
                  ] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(16)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9)),
                  typeToTypeTag[x11](functionType.typeArgs(10)),
                  typeToTypeTag[x12](functionType.typeArgs(11)),
                  typeToTypeTag[x13](functionType.typeArgs(12)),
                  typeToTypeTag[x14](functionType.typeArgs(13)),
                  typeToTypeTag[x15](functionType.typeArgs(14)),
                  typeToTypeTag[x16](functionType.typeArgs(15))
                )
                log.info("Spark registers udf " + name)
              case method: Function17[
                    x1,
                    x2,
                    x3,
                    x4,
                    x5,
                    x6,
                    x7,
                    x8,
                    x9,
                    x10,
                    x11,
                    x12,
                    x13,
                    x14,
                    x15,
                    x16,
                    x17,
                    rt
                  ] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(17)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9)),
                  typeToTypeTag[x11](functionType.typeArgs(10)),
                  typeToTypeTag[x12](functionType.typeArgs(11)),
                  typeToTypeTag[x13](functionType.typeArgs(12)),
                  typeToTypeTag[x14](functionType.typeArgs(13)),
                  typeToTypeTag[x15](functionType.typeArgs(14)),
                  typeToTypeTag[x16](functionType.typeArgs(15)),
                  typeToTypeTag[x17](functionType.typeArgs(16))
                )
                log.info("Spark registers udf " + name)
              case method: Function18[
                    x1,
                    x2,
                    x3,
                    x4,
                    x5,
                    x6,
                    x7,
                    x8,
                    x9,
                    x10,
                    x11,
                    x12,
                    x13,
                    x14,
                    x15,
                    x16,
                    x17,
                    x18,
                    rt
                  ] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(18)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9)),
                  typeToTypeTag[x11](functionType.typeArgs(10)),
                  typeToTypeTag[x12](functionType.typeArgs(11)),
                  typeToTypeTag[x13](functionType.typeArgs(12)),
                  typeToTypeTag[x14](functionType.typeArgs(13)),
                  typeToTypeTag[x15](functionType.typeArgs(14)),
                  typeToTypeTag[x16](functionType.typeArgs(15)),
                  typeToTypeTag[x17](functionType.typeArgs(16)),
                  typeToTypeTag[x18](functionType.typeArgs(17))
                )
                log.info("Spark registers udf " + name)
              case method: Function19[
                    x1,
                    x2,
                    x3,
                    x4,
                    x5,
                    x6,
                    x7,
                    x8,
                    x9,
                    x10,
                    x11,
                    x12,
                    x13,
                    x14,
                    x15,
                    x16,
                    x17,
                    x18,
                    x19,
                    rt
                  ] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(19)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9)),
                  typeToTypeTag[x11](functionType.typeArgs(10)),
                  typeToTypeTag[x12](functionType.typeArgs(11)),
                  typeToTypeTag[x13](functionType.typeArgs(12)),
                  typeToTypeTag[x14](functionType.typeArgs(13)),
                  typeToTypeTag[x15](functionType.typeArgs(14)),
                  typeToTypeTag[x16](functionType.typeArgs(15)),
                  typeToTypeTag[x17](functionType.typeArgs(16)),
                  typeToTypeTag[x18](functionType.typeArgs(17)),
                  typeToTypeTag[x19](functionType.typeArgs(18))
                )
                log.info("Spark registers udf " + name)
              case method: Function20[
                    x1,
                    x2,
                    x3,
                    x4,
                    x5,
                    x6,
                    x7,
                    x8,
                    x9,
                    x10,
                    x11,
                    x12,
                    x13,
                    x14,
                    x15,
                    x16,
                    x17,
                    x18,
                    x19,
                    x20,
                    rt
                  ] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(20)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9)),
                  typeToTypeTag[x11](functionType.typeArgs(10)),
                  typeToTypeTag[x12](functionType.typeArgs(11)),
                  typeToTypeTag[x13](functionType.typeArgs(12)),
                  typeToTypeTag[x14](functionType.typeArgs(13)),
                  typeToTypeTag[x15](functionType.typeArgs(14)),
                  typeToTypeTag[x16](functionType.typeArgs(15)),
                  typeToTypeTag[x17](functionType.typeArgs(16)),
                  typeToTypeTag[x18](functionType.typeArgs(17)),
                  typeToTypeTag[x19](functionType.typeArgs(18)),
                  typeToTypeTag[x20](functionType.typeArgs(19))
                )
                log.info("Spark registers udf " + name)
              case method: Function21[
                    x1,
                    x2,
                    x3,
                    x4,
                    x5,
                    x6,
                    x7,
                    x8,
                    x9,
                    x10,
                    x11,
                    x12,
                    x13,
                    x14,
                    x15,
                    x16,
                    x17,
                    x18,
                    x19,
                    x20,
                    x21,
                    rt
                  ] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(21)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9)),
                  typeToTypeTag[x11](functionType.typeArgs(10)),
                  typeToTypeTag[x12](functionType.typeArgs(11)),
                  typeToTypeTag[x13](functionType.typeArgs(12)),
                  typeToTypeTag[x14](functionType.typeArgs(13)),
                  typeToTypeTag[x15](functionType.typeArgs(14)),
                  typeToTypeTag[x16](functionType.typeArgs(15)),
                  typeToTypeTag[x17](functionType.typeArgs(16)),
                  typeToTypeTag[x18](functionType.typeArgs(17)),
                  typeToTypeTag[x19](functionType.typeArgs(18)),
                  typeToTypeTag[x20](functionType.typeArgs(19)),
                  typeToTypeTag[x21](functionType.typeArgs(20))
                )
                log.info("Spark registers udf " + name)
              case method: Function22[
                    x1,
                    x2,
                    x3,
                    x4,
                    x5,
                    x6,
                    x7,
                    x8,
                    x9,
                    x10,
                    x11,
                    x12,
                    x13,
                    x14,
                    x15,
                    x16,
                    x17,
                    x18,
                    x19,
                    x20,
                    x21,
                    x22,
                    rt
                  ] =>
                spark.udf.register(name, method)(
                  typeToTypeTag[rt](functionType.typeArgs(22)),
                  typeToTypeTag[x1](functionType.typeArgs(0)),
                  typeToTypeTag[x2](functionType.typeArgs(1)),
                  typeToTypeTag[x3](functionType.typeArgs(2)),
                  typeToTypeTag[x4](functionType.typeArgs(3)),
                  typeToTypeTag[x5](functionType.typeArgs(4)),
                  typeToTypeTag[x6](functionType.typeArgs(5)),
                  typeToTypeTag[x7](functionType.typeArgs(6)),
                  typeToTypeTag[x8](functionType.typeArgs(7)),
                  typeToTypeTag[x9](functionType.typeArgs(8)),
                  typeToTypeTag[x10](functionType.typeArgs(9)),
                  typeToTypeTag[x11](functionType.typeArgs(10)),
                  typeToTypeTag[x12](functionType.typeArgs(11)),
                  typeToTypeTag[x13](functionType.typeArgs(12)),
                  typeToTypeTag[x14](functionType.typeArgs(13)),
                  typeToTypeTag[x15](functionType.typeArgs(14)),
                  typeToTypeTag[x16](functionType.typeArgs(15)),
                  typeToTypeTag[x17](functionType.typeArgs(16)),
                  typeToTypeTag[x18](functionType.typeArgs(17)),
                  typeToTypeTag[x19](functionType.typeArgs(18)),
                  typeToTypeTag[x20](functionType.typeArgs(19)),
                  typeToTypeTag[x21](functionType.typeArgs(20)),
                  typeToTypeTag[x22](functionType.typeArgs(21))
                )
                log.info("Spark registers udf " + name)
              case _ =>
                log.warn("Spark cannot register function " + name + " due to pattern mismatch!")
            }
          }
        }
    }
    val reflections = new Reflections("org.urbcomp.cupid.db.udtf")
    val udtfClasses =
      reflections.getSubTypesOf(classOf[AbstractUdtf]).toSet[Class[_ <: AbstractUdtf]].toList
    udtfClasses.forEach(clazz => {
      val instance = clazz.newInstance()
      val name: String = clazz.getDeclaredMethod("name").invoke(instance).asInstanceOf[String]
      val registerEngines: List[DataEngine.Value] =
        clazz
          .getDeclaredMethod("registerEngines")
          .invoke(instance)
          .asInstanceOf[List[DataEngine.Value]]
      if (registerEngines.contains(Spark)) {
        spark.sql("CREATE OR REPLACE TEMPORARY FUNCTION " + name + " as '" + clazz.getName + "'")
        log.info("Spark registers udtf " + name)
      }
    })
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
