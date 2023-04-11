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
import scala.reflect.runtime.universe._
import scala.reflect.api
import org.urbcomp.cupid.db.udf.DataEngine.Spark
import org.locationtech.jts.geom._
import org.locationtech.geomesa.spark.jts._
import java.lang.reflect.Method
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

  def getAllMethods[T](clazz: Class[T], name: String): Array[Method] = {
    clazz.getMethods.filter(method => method.getName == name && !method.isBridge)
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
        val instance = clazz.newInstance()
        val rm = ScalaReflection.mirror

        def typeToTypeTag[T](tpe: Type): TypeTag[T] =
          TypeTag(rm, new api.TypeCreator {
            def apply[U <: api.Universe with Singleton](m: api.Mirror[U]): U#Type =
              tpe.asInstanceOf[U#Type]
          })
        val paramTypeList = rm
          .classSymbol(clazz)
          .typeSignature
          .decl(TermName("evaluate"))
          .asMethod
          .paramLists(0)
          .map(_.typeSignature)
        val returnType =
          rm.classSymbol(clazz).typeSignature.decl(TermName("evaluate")).asMethod.returnType
        paramTypeList.size match {
          case 0 =>
            lazy val method: Function0[Object] =
              () => getAllMethods(clazz, "evaluate")(0).invoke(instance)
            method match {
              case method: Function0[rt] =>
                spark.udf.register(name, method)(typeToTypeTag[rt](returnType))
            }
            log.info("Spark registers udf " + name)
          case 1 =>
            lazy val method: Function1[Object, Object] =
              getAllMethods(clazz, "evaluate")(0).invoke(instance, _)
            method match {
              case method: Function1[x1, rt] =>
                spark.udf.register(name, method.asInstanceOf[Function1[x1, rt]])(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0))
                )
            }
            log.info("Spark registers udf " + name)
          case 2 =>
            lazy val method: Function2[Object, Object, Object] =
              getAllMethods(clazz, "evaluate")(0).invoke(instance, _, _)
            method match {
              case method: Function2[x1, x2, rt] =>
                spark.udf.register(name, method.asInstanceOf[Function2[x1, x2, rt]])(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1))
                )
            }
            log.info("Spark registers udf " + name)
          case 3 =>
            lazy val method: Function3[Object, Object, Object, Object] =
              getAllMethods(clazz, "evaluate")(0).invoke(instance, _, _, _)
            method match {
              case method: Function3[x1, x2, x3, rt] =>
                spark.udf.register(name, method.asInstanceOf[Function3[x1, x2, x3, rt]])(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2))
                )
            }
            log.info("Spark registers udf " + name)
          case 4 =>
            lazy val method: Function4[Object, Object, Object, Object, Object] =
              getAllMethods(clazz, "evaluate")(0).invoke(instance, _, _, _, _)
            method match {
              case method: Function4[x1, x2, x3, x4, rt] =>
                spark.udf.register(name, method.asInstanceOf[Function4[x1, x2, x3, x4, rt]])(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3))
                )
            }
            log.info("Spark registers udf " + name)
          case 5 =>
            lazy val method: Function5[Object, Object, Object, Object, Object, Object] =
              getAllMethods(clazz, "evaluate")(0).invoke(instance, _, _, _, _, _)
            method match {
              case method: Function5[x1, x2, x3, x4, x5, rt] =>
                spark.udf.register(name, method.asInstanceOf[Function5[x1, x2, x3, x4, x5, rt]])(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4))
                )
            }
            log.info("Spark registers udf " + name)
          case 6 =>
            lazy val method: Function6[Object, Object, Object, Object, Object, Object, Object] =
              getAllMethods(clazz, "evaluate")(0).invoke(instance, _, _, _, _, _, _)
            method match {
              case method: Function6[x1, x2, x3, x4, x5, x6, rt] =>
                spark.udf
                  .register(name, method.asInstanceOf[Function6[x1, x2, x3, x4, x5, x6, rt]])(
                    typeToTypeTag[rt](returnType),
                    typeToTypeTag[x1](paramTypeList(0)),
                    typeToTypeTag[x2](paramTypeList(1)),
                    typeToTypeTag[x3](paramTypeList(2)),
                    typeToTypeTag[x4](paramTypeList(3)),
                    typeToTypeTag[x5](paramTypeList(4)),
                    typeToTypeTag[x6](paramTypeList(5))
                  )
            }
            log.info("Spark registers udf " + name)
          case 7 =>
            lazy val method
                : Function7[Object, Object, Object, Object, Object, Object, Object, Object] =
              getAllMethods(clazz, "evaluate")(0).invoke(instance, _, _, _, _, _, _, _)
            method match {
              case method: Function7[x1, x2, x3, x4, x5, x6, x7, rt] =>
                spark.udf
                  .register(name, method.asInstanceOf[Function7[x1, x2, x3, x4, x5, x6, x7, rt]])(
                    typeToTypeTag[rt](returnType),
                    typeToTypeTag[x1](paramTypeList(0)),
                    typeToTypeTag[x2](paramTypeList(1)),
                    typeToTypeTag[x3](paramTypeList(2)),
                    typeToTypeTag[x4](paramTypeList(3)),
                    typeToTypeTag[x5](paramTypeList(4)),
                    typeToTypeTag[x6](paramTypeList(5)),
                    typeToTypeTag[x7](paramTypeList(6))
                  )
            }
            log.info("Spark registers udf " + name)
          case 8 =>
            lazy val method: Function8[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0).invoke(instance, _, _, _, _, _, _, _, _)
            method match {
              case method: Function8[x1, x2, x3, x4, x5, x6, x7, x8, rt] =>
                spark.udf.register(
                  name,
                  method.asInstanceOf[Function8[x1, x2, x3, x4, x5, x6, x7, x8, rt]]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7))
                )
            }
            log.info("Spark registers udf " + name)
          case 9 =>
            lazy val method: Function9[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0).invoke(instance, _, _, _, _, _, _, _, _, _)
            method match {
              case method: Function9[x1, x2, x3, x4, x5, x6, x7, x8, x9, rt] =>
                spark.udf.register(
                  name,
                  method.asInstanceOf[Function9[x1, x2, x3, x4, x5, x6, x7, x8, x9, rt]]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8))
                )
            }
            log.info("Spark registers udf " + name)
          case 10 =>
            lazy val method: Function10[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0).invoke(instance, _, _, _, _, _, _, _, _, _, _)
            method match {
              case method: Function10[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, rt] =>
                spark.udf.register(
                  name,
                  method.asInstanceOf[Function10[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, rt]]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9))
                )
            }
            log.info("Spark registers udf " + name)
          case 11 =>
            lazy val method: Function11[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] =
              getAllMethods(clazz, "evaluate")(0).invoke(instance, _, _, _, _, _, _, _, _, _, _, _)
            method match {
              case method: Function11[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, rt] =>
                spark.udf.register(
                  name,
                  method.asInstanceOf[Function11[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, rt]]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9)),
                  typeToTypeTag[x11](paramTypeList(10))
                )
            }
            log.info("Spark registers udf " + name)
          case 12 =>
            lazy val method: Function12[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0)
              .invoke(instance, _, _, _, _, _, _, _, _, _, _, _, _)
            method match {
              case method: Function12[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, rt] =>
                spark.udf.register(
                  name,
                  method
                    .asInstanceOf[Function12[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, rt]]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9)),
                  typeToTypeTag[x11](paramTypeList(10)),
                  typeToTypeTag[x12](paramTypeList(11))
                )
            }
            log.info("Spark registers udf " + name)
          case 13 =>
            lazy val method: Function13[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0)
              .invoke(instance, _, _, _, _, _, _, _, _, _, _, _, _, _)
            method match {
              case method: Function13[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, rt] =>
                spark.udf.register(
                  name,
                  method.asInstanceOf[
                    Function13[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, rt]
                  ]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9)),
                  typeToTypeTag[x11](paramTypeList(10)),
                  typeToTypeTag[x12](paramTypeList(11)),
                  typeToTypeTag[x13](paramTypeList(12))
                )
            }
            log.info("Spark registers udf " + name)
          case 14 =>
            lazy val method: Function14[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0)
              .invoke(instance, _, _, _, _, _, _, _, _, _, _, _, _, _, _)
            method match {
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
                spark.udf.register(
                  name,
                  method.asInstanceOf[
                    Function14[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, rt]
                  ]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9)),
                  typeToTypeTag[x11](paramTypeList(10)),
                  typeToTypeTag[x12](paramTypeList(11)),
                  typeToTypeTag[x13](paramTypeList(12)),
                  typeToTypeTag[x14](paramTypeList(13))
                )
            }
            log.info("Spark registers udf " + name)
          case 15 =>
            lazy val method: Function15[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0)
              .invoke(instance, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)
            method match {
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
                spark.udf.register(
                  name,
                  method.asInstanceOf[
                    Function15[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, rt]
                  ]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9)),
                  typeToTypeTag[x11](paramTypeList(10)),
                  typeToTypeTag[x12](paramTypeList(11)),
                  typeToTypeTag[x13](paramTypeList(12)),
                  typeToTypeTag[x14](paramTypeList(13)),
                  typeToTypeTag[x15](paramTypeList(14))
                )
            }
            log.info("Spark registers udf " + name)
          case 16 =>
            lazy val method: Function16[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0)
              .invoke(instance, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)
            method match {
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
                spark.udf.register(
                  name,
                  method.asInstanceOf[Function16[
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
                  ]]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9)),
                  typeToTypeTag[x11](paramTypeList(10)),
                  typeToTypeTag[x12](paramTypeList(11)),
                  typeToTypeTag[x13](paramTypeList(12)),
                  typeToTypeTag[x14](paramTypeList(13)),
                  typeToTypeTag[x15](paramTypeList(14)),
                  typeToTypeTag[x16](paramTypeList(15))
                )
            }
            log.info("Spark registers udf " + name)
          case 17 =>
            lazy val method: Function17[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0)
              .invoke(instance, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)
            method match {
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
                spark.udf.register(
                  name,
                  method.asInstanceOf[Function17[
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
                  ]]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9)),
                  typeToTypeTag[x11](paramTypeList(10)),
                  typeToTypeTag[x12](paramTypeList(11)),
                  typeToTypeTag[x13](paramTypeList(12)),
                  typeToTypeTag[x14](paramTypeList(13)),
                  typeToTypeTag[x15](paramTypeList(14)),
                  typeToTypeTag[x16](paramTypeList(15)),
                  typeToTypeTag[x17](paramTypeList(16))
                )
            }
            log.info("Spark registers udf " + name)
          case 18 =>
            lazy val method: Function18[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0)
              .invoke(instance, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)
            method match {
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
                spark.udf.register(
                  name,
                  method.asInstanceOf[Function18[
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
                  ]]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9)),
                  typeToTypeTag[x11](paramTypeList(10)),
                  typeToTypeTag[x12](paramTypeList(11)),
                  typeToTypeTag[x13](paramTypeList(12)),
                  typeToTypeTag[x14](paramTypeList(13)),
                  typeToTypeTag[x15](paramTypeList(14)),
                  typeToTypeTag[x16](paramTypeList(15)),
                  typeToTypeTag[x17](paramTypeList(16)),
                  typeToTypeTag[x18](paramTypeList(17))
                )
            }
            log.info("Spark registers udf " + name)
          case 19 =>
            lazy val method: Function19[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0)
              .invoke(instance, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)
            method match {
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
                spark.udf.register(
                  name,
                  method.asInstanceOf[Function19[
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
                  ]]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9)),
                  typeToTypeTag[x11](paramTypeList(10)),
                  typeToTypeTag[x12](paramTypeList(11)),
                  typeToTypeTag[x13](paramTypeList(12)),
                  typeToTypeTag[x14](paramTypeList(13)),
                  typeToTypeTag[x15](paramTypeList(14)),
                  typeToTypeTag[x16](paramTypeList(15)),
                  typeToTypeTag[x17](paramTypeList(16)),
                  typeToTypeTag[x18](paramTypeList(17)),
                  typeToTypeTag[x19](paramTypeList(18))
                )
            }
            log.info("Spark registers udf " + name)
          case 20 =>
            lazy val method: Function20[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0)
              .invoke(instance, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)
            method match {
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
                spark.udf.register(
                  name,
                  method.asInstanceOf[Function20[
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
                  ]]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9)),
                  typeToTypeTag[x11](paramTypeList(10)),
                  typeToTypeTag[x12](paramTypeList(11)),
                  typeToTypeTag[x13](paramTypeList(12)),
                  typeToTypeTag[x14](paramTypeList(13)),
                  typeToTypeTag[x15](paramTypeList(14)),
                  typeToTypeTag[x16](paramTypeList(15)),
                  typeToTypeTag[x17](paramTypeList(16)),
                  typeToTypeTag[x18](paramTypeList(17)),
                  typeToTypeTag[x19](paramTypeList(18)),
                  typeToTypeTag[x20](paramTypeList(19))
                )
            }
            log.info("Spark registers udf " + name)
          case 21 =>
            lazy val method: Function21[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0)
              .invoke(instance, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)
            method match {
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
                spark.udf.register(
                  name,
                  method.asInstanceOf[Function21[
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
                  ]]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9)),
                  typeToTypeTag[x11](paramTypeList(10)),
                  typeToTypeTag[x12](paramTypeList(11)),
                  typeToTypeTag[x13](paramTypeList(12)),
                  typeToTypeTag[x14](paramTypeList(13)),
                  typeToTypeTag[x15](paramTypeList(14)),
                  typeToTypeTag[x16](paramTypeList(15)),
                  typeToTypeTag[x17](paramTypeList(16)),
                  typeToTypeTag[x18](paramTypeList(17)),
                  typeToTypeTag[x19](paramTypeList(18)),
                  typeToTypeTag[x20](paramTypeList(19)),
                  typeToTypeTag[x21](paramTypeList(20))
                )
            }
            log.info("Spark registers udf " + name)
          case 22 =>
            lazy val method: Function22[
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object,
              Object
            ] = getAllMethods(clazz, "evaluate")(0)
              .invoke(instance, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _)
            method match {
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
                spark.udf.register(
                  name,
                  method.asInstanceOf[Function22[
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
                  ]]
                )(
                  typeToTypeTag[rt](returnType),
                  typeToTypeTag[x1](paramTypeList(0)),
                  typeToTypeTag[x2](paramTypeList(1)),
                  typeToTypeTag[x3](paramTypeList(2)),
                  typeToTypeTag[x4](paramTypeList(3)),
                  typeToTypeTag[x5](paramTypeList(4)),
                  typeToTypeTag[x6](paramTypeList(5)),
                  typeToTypeTag[x7](paramTypeList(6)),
                  typeToTypeTag[x8](paramTypeList(7)),
                  typeToTypeTag[x9](paramTypeList(8)),
                  typeToTypeTag[x10](paramTypeList(9)),
                  typeToTypeTag[x11](paramTypeList(10)),
                  typeToTypeTag[x12](paramTypeList(11)),
                  typeToTypeTag[x13](paramTypeList(12)),
                  typeToTypeTag[x14](paramTypeList(13)),
                  typeToTypeTag[x15](paramTypeList(14)),
                  typeToTypeTag[x16](paramTypeList(15)),
                  typeToTypeTag[x17](paramTypeList(16)),
                  typeToTypeTag[x18](paramTypeList(17)),
                  typeToTypeTag[x19](paramTypeList(18)),
                  typeToTypeTag[x20](paramTypeList(19)),
                  typeToTypeTag[x21](paramTypeList(20)),
                  typeToTypeTag[x22](paramTypeList(21))
                )
            }
            log.info("Spark registers udf " + name)
          case _ => log.warn("Spark cannot register function " + name + " due to pattern mismatch!")
        }
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
