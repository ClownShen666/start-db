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

import org.reflections.Reflections
import scala.collection.convert.ImplicitConversions._
import scala.collection.mutable

class UdfFactory extends Serializable {
  private var engineUdfMap = mutable.HashMap[String, mutable.Map[String, Class[_ <: AbstractUdf]]]()

  {
    val reflections = new Reflections("org.urbcomp.cupid.db.udf")
    val classes =
      reflections.getSubTypesOf(classOf[AbstractUdf]).toSet[Class[_ <: AbstractUdf]].toList
    classes.forEach(clazz => {
      val instance = clazz.newInstance()
      val name: String = clazz.getDeclaredMethod("name").invoke(instance).asInstanceOf[String]
      val registerEngines: List[DataEngine.Value] =
        clazz
          .getDeclaredMethod("registerEngines")
          .invoke(instance)
          .asInstanceOf[List[DataEngine.Value]]
      registerEngines.foreach { engine =>
        val engineStr = engine.toString
        if (!engineUdfMap.contains(engineStr))
          engineUdfMap += (engineStr -> mutable.Map[String, Class[_ <: AbstractUdf]]())
        engineUdfMap(engineStr) += (name -> clazz)
      }
    })
  }

  def getUdfMap(engineName: String): mutable.Map[String, Class[_ <: AbstractUdf]] = {
    engineUdfMap.getOrElse(engineName, mutable.Map[String, Class[_ <: AbstractUdf]]())
  }
  def getEngineUdfMap: mutable.HashMap[String, mutable.Map[String, Class[_ <: AbstractUdf]]] =
    engineUdfMap
}
