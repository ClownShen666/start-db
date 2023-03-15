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

import com.typesafe.scalalogging.LazyLogging

import org.apache.spark.SparkConf
import org.apache.spark.sql.catalyst.ScalaReflection
import org.apache.spark.sql.SparkSession
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
    SparkResultExporterFactory.getInstance(param.getExportType).exportData(param.getSqlId, df)
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
        val method = clazz.getDeclaredMethod("udfWrapper").invoke(instance)
        val rm = ScalaReflection.mirror
        val functionType =
          rm.classSymbol(clazz).typeSignature.decl(TermName("udfWrapper")).asMethod.returnType
        def typeToTypeTag[T](tpe: Type): TypeTag[T] =
          TypeTag(rm, new api.TypeCreator {
            def apply[U <: api.Universe with Singleton](m: api.Mirror[U]): U#Type =
              tpe.asInstanceOf[U#Type]
          })
        method match {
          case method: Function0[rt] => {
            spark.udf.register(name, method)(typeToTypeTag[rt](functionType.typeArgs(0)))
          }
          case method: Function1[x1, rt] => {
            spark.udf.register(name, method)(
              typeToTypeTag[rt](functionType.typeArgs(1)),
              typeToTypeTag[x1](functionType.typeArgs(0))
            )
          }
          case method: Function2[x1, x2, rt] => {
            spark.udf.register(name, method)(
              typeToTypeTag[rt](functionType.typeArgs(2)),
              typeToTypeTag[x1](functionType.typeArgs(0)),
              typeToTypeTag[x2](functionType.typeArgs(1))
            )
          }
          case method: Function3[x1, x2, x3, rt] => {
            spark.udf.register(name, method)(
              typeToTypeTag[rt](functionType.typeArgs(3)),
              typeToTypeTag[x1](functionType.typeArgs(0)),
              typeToTypeTag[x2](functionType.typeArgs(1)),
              typeToTypeTag[x3](functionType.typeArgs(2))
            )
          }
          case method: Function4[x1, x2, x3, x4, rt] => {
            spark.udf.register(name, method)(
              typeToTypeTag[rt](functionType.typeArgs(4)),
              typeToTypeTag[x1](functionType.typeArgs(0)),
              typeToTypeTag[x2](functionType.typeArgs(1)),
              typeToTypeTag[x3](functionType.typeArgs(2)),
              typeToTypeTag[x4](functionType.typeArgs(3))
            )
          }
          case method: Function5[x1, x2, x3, x4, x5, rt] => {
            spark.udf.register(name, method)(
              typeToTypeTag[rt](functionType.typeArgs(5)),
              typeToTypeTag[x1](functionType.typeArgs(0)),
              typeToTypeTag[x2](functionType.typeArgs(1)),
              typeToTypeTag[x3](functionType.typeArgs(2)),
              typeToTypeTag[x4](functionType.typeArgs(3)),
              typeToTypeTag[x5](functionType.typeArgs(4))
            )
          }
          case method: Function6[x1, x2, x3, x4, x5, x6, rt] => {
            spark.udf.register(name, method)(
              typeToTypeTag[rt](functionType.typeArgs(6)),
              typeToTypeTag[x1](functionType.typeArgs(0)),
              typeToTypeTag[x2](functionType.typeArgs(1)),
              typeToTypeTag[x3](functionType.typeArgs(2)),
              typeToTypeTag[x4](functionType.typeArgs(3)),
              typeToTypeTag[x5](functionType.typeArgs(4)),
              typeToTypeTag[x6](functionType.typeArgs(5))
            )
          }
          case method: Function7[x1, x2, x3, x4, x5, x6, x7, rt] => {
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
          }
          case method: Function8[x1, x2, x3, x4, x5, x6, x7, x8, rt] => {
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
          }
          case method: Function9[x1, x2, x3, x4, x5, x6, x7, x8, x9, rt] => {
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
          }
          case method: Function10[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, rt] => {
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
          }
          case method: Function11[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, rt] => {
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
          }
          case method: Function12[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, rt] => {
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
          }
          case method: Function13[x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, rt] => {
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
          }
          case _ => logger.warn("Not register function " + name + " due to pattern mismatch!")
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
