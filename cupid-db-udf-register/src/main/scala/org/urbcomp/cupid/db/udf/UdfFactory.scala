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
  private var udfMap = mutable.HashMap.empty[String, UserDefinedFunction]

  {
    val reflections = new Reflections("org.urbcomp.cupid.db.udf")
    val clazz = reflections.getSubTypesOf(classOf[AbstractUdf]).toSet[Class[_ <: AbstractUdf]]
    clazz.forEach(cla => {
      val nameMethod = cla.getDeclaredMethod("name")
      val name: String = nameMethod.invoke(cla.newInstance()).asInstanceOf[String]
      val functionMethod = cla.getDeclaredMethod("function")
      val udf: UserDefinedFunction = functionMethod.invoke(cla.newInstance()).asInstanceOf[UserDefinedFunction]
      udfMap += (name -> udf)
    })
  }

}
