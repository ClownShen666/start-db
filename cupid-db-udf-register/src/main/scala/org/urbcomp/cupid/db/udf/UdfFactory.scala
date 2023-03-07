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
import org.apache.spark.sql.expressions.UserDefinedFunction

import org.reflections.Reflections

import scala.collection.convert.ImplicitConversions._
import scala.collection.mutable

class UdfFactory {
  private var calciteUdfMap = mutable.HashMap.empty[String, UserDefinedFunction]
  private var sparkUdfMap = mutable.HashMap.empty[String, UserDefinedFunction]

  {
    val reflections = new Reflections("org.urbcomp.cupid.db.udf")
    val classes = reflections.getSubTypesOf(classOf[AbstractUdf]).toSet[Class[_ <: AbstractUdf]]
    classes.forEach(clazz => {
      val instance = clazz.newInstance()
      val name: String = clazz.getDeclaredMethod("name").invoke(instance).asInstanceOf[String]
      val udf: UserDefinedFunction =
        clazz.getDeclaredMethod("function").invoke(instance).asInstanceOf[UserDefinedFunction]
      if (clazz.getDeclaredMethod("registerCalcite").invoke(instance).asInstanceOf[Boolean]) {
        calciteUdfMap += (name -> udf)
      }
      if (clazz.getDeclaredMethod("registerSpark").invoke(instance).asInstanceOf[Boolean]) {
        sparkUdfMap += (name -> udf)
      }
    })
    System.out.println(calciteUdfMap)
    System.out.println(sparkUdfMap)
  }

  def getCalciteUdfMap: mutable.HashMap[String, UserDefinedFunction] = { calciteUdfMap }
  def getSparkUdfMap: mutable.HashMap[String, UserDefinedFunction] = { sparkUdfMap }
}
