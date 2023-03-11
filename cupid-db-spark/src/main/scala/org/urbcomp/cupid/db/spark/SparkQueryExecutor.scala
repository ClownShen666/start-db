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

package org.urbcomp.cupid.db.spark

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.SparkConf
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.{SparkSession, functions}
import org.locationtech.geomesa.spark.GeoMesaSparkKryoRegistrator
import org.urbcomp.cupid.db.metadata.MetadataAccessUtil
import org.urbcomp.cupid.db.spark.res.SparkResultExporterFactory
import org.urbcomp.cupid.db.util.{MetadataUtil, SparkSqlParam}
import org.urbcomp.cupid.db.udf.UdfFactory
import scala.reflect.runtime.universe._
import scala.reflect.api

object SparkQueryExecutor extends LazyLogging {

  def execute(param: SparkSqlParam, sparkSession: SparkSession = null): Unit = {
    if (param != null) SparkSqlParam.CACHE.set(param)
    var spark = sparkSession

    if (spark == null) spark = getSparkSession(param.isLocal, enableHiveSupport = false)

    val sql = param.getSql
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
    SparkResultExporterFactory.getInstance(param.getExportType).exportData(df)
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

  def getSparkSession(isLocal: Boolean, enableHiveSupport: Boolean): SparkSession = {
    val builder = SparkSession.builder().config(buildSparkConf()).appName("Cupid-SPARK")
    if (isLocal) builder.master("local[*]")
    if (enableHiveSupport) builder.enableHiveSupport()
    val spark = builder.getOrCreate()
    new UdfFactory().getUdfMap("Spark").foreach {
      case (name, clazz) => {
        val instance = clazz.newInstance()
        val udfEntryName: String =
          clazz.getDeclaredMethod("udfEntryName").invoke(instance).asInstanceOf[String]
        val method = clazz.getDeclaredMethod(udfEntryName).invoke(instance)
        val rm = ScalaReflection.mirror
        val functionType =
          rm.classSymbol(clazz).typeSignature.decl(TermName(udfEntryName)).asMethod.returnType
        def typeToTypeTag[T](tpe: Type): TypeTag[T] =
          TypeTag(rm, new api.TypeCreator {
            def apply[U <: api.Universe with Singleton](m: api.Mirror[U]): U#Type =
              tpe.asInstanceOf[U#Type]
          })
        method match {
          case method: scala.Function0[rt] => {
            val List(returnType) = functionType.typeArgs
            spark.udf.register(name, method)(typeToTypeTag[rt](returnType))
          }
          case method: Function1[x1, rt] => {
            val List(argType, returnType) = functionType.typeArgs
            spark.udf
              .register(name, method)(typeToTypeTag[rt](returnType), typeToTypeTag[x1](argType))
          }
          case method: Function2[x1, x2, rt] => {
            val List(arg1Type, arg2Type, returnType) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type)
            )
          }
          case method: Function3[x1, x2, x3, rt] => {
            val List(arg1Type, arg2Type, arg3Type, returnType) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type)
            )
          }
          case method: Function4[x1, x2, x3, x4, rt] => {
            val List(arg1Type, arg2Type, arg3Type, arg4Type, returnType) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type)
            )
          }
          case method: Function5[x1, x2, x3, x4, x5, rt] => {
            val List(arg1Type, arg2Type, arg3Type, arg4Type, arg5Type, returnType) =
              functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type)
            )
          }
          case method: Function6[x1, x2, x3, x4, x5, x6, rt] => {
            val List(arg1Type, arg2Type, arg3Type, arg4Type, arg5Type, arg6Type, returnType) =
              functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type)
            )
          }
          case method: Function7[x1, x2, x3, x4, x5, x6, x7, rt] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type)
            )
          }
          case method: Function8[x1, x2, x3, x4, x5, x6, x7, x8, rt] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type)
            )
          }
          case method: Function9[x1, x2, x3, x4, x5, x6, x7, x8, x9, rt] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type)
            )
          }
          case method: Function10[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, rt] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type)
            )
          }
          case method: Function11[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, rt] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              arg11Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type),
              typeToTypeTag[x11](arg11Type)
            )
          }
          case method: Function12[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, rt] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              arg11Type,
              arg12Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type),
              typeToTypeTag[x11](arg11Type),
              typeToTypeTag[x12](arg12Type)
            )
          }
          case method: Function13[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, rt] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              arg11Type,
              arg12Type,
              arg13Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type),
              typeToTypeTag[x11](arg11Type),
              typeToTypeTag[x12](arg12Type),
              typeToTypeTag[x13](arg13Type)
            )
          }
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
              ] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              arg11Type,
              arg12Type,
              arg13Type,
              arg14Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type),
              typeToTypeTag[x11](arg11Type),
              typeToTypeTag[x12](arg12Type),
              typeToTypeTag[x13](arg13Type),
              typeToTypeTag[x14](arg14Type)
            )
          }
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
              ] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              arg11Type,
              arg12Type,
              arg13Type,
              arg14Type,
              arg15Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type),
              typeToTypeTag[x11](arg11Type),
              typeToTypeTag[x12](arg12Type),
              typeToTypeTag[x13](arg13Type),
              typeToTypeTag[x14](arg14Type),
              typeToTypeTag[x15](arg15Type)
            )
          }
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
              ] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              arg11Type,
              arg12Type,
              arg13Type,
              arg14Type,
              arg15Type,
              arg16Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type),
              typeToTypeTag[x11](arg11Type),
              typeToTypeTag[x12](arg12Type),
              typeToTypeTag[x13](arg13Type),
              typeToTypeTag[x14](arg14Type),
              typeToTypeTag[x15](arg15Type),
              typeToTypeTag[x16](arg16Type)
            )
          }
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
              ] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              arg11Type,
              arg12Type,
              arg13Type,
              arg14Type,
              arg15Type,
              arg16Type,
              arg17Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type),
              typeToTypeTag[x11](arg11Type),
              typeToTypeTag[x12](arg12Type),
              typeToTypeTag[x13](arg13Type),
              typeToTypeTag[x14](arg14Type),
              typeToTypeTag[x15](arg15Type),
              typeToTypeTag[x16](arg16Type),
              typeToTypeTag[x17](arg17Type)
            )
          }
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
              ] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              arg11Type,
              arg12Type,
              arg13Type,
              arg14Type,
              arg15Type,
              arg16Type,
              arg17Type,
              arg18Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type),
              typeToTypeTag[x11](arg11Type),
              typeToTypeTag[x12](arg12Type),
              typeToTypeTag[x13](arg13Type),
              typeToTypeTag[x14](arg14Type),
              typeToTypeTag[x15](arg15Type),
              typeToTypeTag[x16](arg16Type),
              typeToTypeTag[x17](arg17Type),
              typeToTypeTag[x18](arg18Type)
            )
          }
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
              ] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              arg11Type,
              arg12Type,
              arg13Type,
              arg14Type,
              arg15Type,
              arg16Type,
              arg17Type,
              arg18Type,
              arg19Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type),
              typeToTypeTag[x11](arg11Type),
              typeToTypeTag[x12](arg12Type),
              typeToTypeTag[x13](arg13Type),
              typeToTypeTag[x14](arg14Type),
              typeToTypeTag[x15](arg15Type),
              typeToTypeTag[x16](arg16Type),
              typeToTypeTag[x17](arg17Type),
              typeToTypeTag[x18](arg18Type),
              typeToTypeTag[x19](arg19Type)
            )
          }
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
              ] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              arg11Type,
              arg12Type,
              arg13Type,
              arg14Type,
              arg15Type,
              arg16Type,
              arg17Type,
              arg18Type,
              arg19Type,
              arg20Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type),
              typeToTypeTag[x11](arg11Type),
              typeToTypeTag[x12](arg12Type),
              typeToTypeTag[x13](arg13Type),
              typeToTypeTag[x14](arg14Type),
              typeToTypeTag[x15](arg15Type),
              typeToTypeTag[x16](arg16Type),
              typeToTypeTag[x17](arg17Type),
              typeToTypeTag[x18](arg18Type),
              typeToTypeTag[x19](arg19Type),
              typeToTypeTag[x20](arg20Type)
            )
          }
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
              ] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              arg11Type,
              arg12Type,
              arg13Type,
              arg14Type,
              arg15Type,
              arg16Type,
              arg17Type,
              arg18Type,
              arg19Type,
              arg20Type,
              arg21Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type),
              typeToTypeTag[x11](arg11Type),
              typeToTypeTag[x12](arg12Type),
              typeToTypeTag[x13](arg13Type),
              typeToTypeTag[x14](arg14Type),
              typeToTypeTag[x15](arg15Type),
              typeToTypeTag[x16](arg16Type),
              typeToTypeTag[x17](arg17Type),
              typeToTypeTag[x18](arg18Type),
              typeToTypeTag[x19](arg19Type),
              typeToTypeTag[x20](arg20Type),
              typeToTypeTag[x21](arg21Type)
            )
          }
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
              ] => {
            val List(
              arg1Type,
              arg2Type,
              arg3Type,
              arg4Type,
              arg5Type,
              arg6Type,
              arg7Type,
              arg8Type,
              arg9Type,
              arg10Type,
              arg11Type,
              arg12Type,
              arg13Type,
              arg14Type,
              arg15Type,
              arg16Type,
              arg17Type,
              arg18Type,
              arg19Type,
              arg20Type,
              arg21Type,
              arg22Type,
              returnType
            ) = functionType.typeArgs
            spark.udf.register(name, method)(
              typeToTypeTag[rt](returnType),
              typeToTypeTag[x1](arg1Type),
              typeToTypeTag[x2](arg2Type),
              typeToTypeTag[x3](arg3Type),
              typeToTypeTag[x4](arg4Type),
              typeToTypeTag[x5](arg5Type),
              typeToTypeTag[x6](arg6Type),
              typeToTypeTag[x7](arg7Type),
              typeToTypeTag[x8](arg8Type),
              typeToTypeTag[x9](arg9Type),
              typeToTypeTag[x10](arg10Type),
              typeToTypeTag[x11](arg11Type),
              typeToTypeTag[x12](arg12Type),
              typeToTypeTag[x13](arg13Type),
              typeToTypeTag[x14](arg14Type),
              typeToTypeTag[x15](arg15Type),
              typeToTypeTag[x16](arg16Type),
              typeToTypeTag[x17](arg17Type),
              typeToTypeTag[x18](arg18Type),
              typeToTypeTag[x19](arg19Type),
              typeToTypeTag[x20](arg20Type),
              typeToTypeTag[x21](arg21Type),
              typeToTypeTag[x22](arg22Type)
            )
          }
        }
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
