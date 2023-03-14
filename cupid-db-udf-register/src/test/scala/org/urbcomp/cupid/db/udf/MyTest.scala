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

package org.urbcomp.cupid.db.udf

import org.apache.spark.sql.catalyst.ScalaReflection
import ScalaReflection.universe._
import org.apache.spark.sql.SparkSession
import org.reflections.Reflections

import scala.collection.JavaConverters.asScalaSetConverter
import scala.collection.convert.ImplicitConversions.`collection asJava`
import scala.reflect.api

trait Base
class A extends Base {
  def impl(x: Int): String = "ab" + x.toString
}

object MyTest {
  val spark: SparkSession = SparkSession
    .builder()
    .master("local")
    .appName("Spark app")
    .getOrCreate()

  val reflections = new Reflections()
  val classes = reflections.getSubTypesOf(classOf[AbstractUdf]).asScala.toList
  val clazz = classes.head

  val rm = ScalaReflection.mirror
  val classSymbol = rm.classSymbol(clazz)
  val methodSymbol = classSymbol.typeSignature.decl(TermName("impl")).asMethod
  val returnType = methodSymbol.returnType
  val argType = methodSymbol.paramLists.head.head.typeSignature

  val constructorSymbol = classSymbol.typeSignature.decl(termNames.CONSTRUCTOR).asMethod
  val instance = rm.reflectClass(classSymbol).reflectConstructor(constructorSymbol)()
  val impl: Any => Any = rm.reflect(instance).reflectMethod(methodSymbol)(_)

  def typeToTypeTag[T](tpe: Type): TypeTag[T] =
    TypeTag(rm, new api.TypeCreator {
      def apply[U <: api.Universe with Singleton](m: api.Mirror[U]) =
        tpe.asInstanceOf[U#Type]
    })

  impl match {
    case impl: Function1[x, y] =>
      spark.udf.register("foo", impl)(typeToTypeTag[y](returnType), typeToTypeTag[x](argType))
  }

  def getSparkSession() = spark

  def main(args: Array[String]): Unit = {
    getSparkSession().sql("""SELECT foo(10)""").show()
  }

}
