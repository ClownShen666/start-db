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

package org.urbcomp.cupid.db.geomesa

import org.apache.calcite.schema.impl.{AggregateFunctionImpl, ScalarFunctionImpl, TableFunctionImpl}
import org.apache.calcite.schema.{ScalarFunction, Schema, SchemaFactory, SchemaPlus}
import org.urbcomp.cupid.db.function.udaf.CollectList
import org.urbcomp.cupid.db.udf.{UdfFactory, UdfType}
import org.urbcomp.cupid.db.udtf.{
  DBSCANClustering,
  Fibonacci,
  StayPointDetect,
  TimeIntervalTrajectorySegment
}

import java.lang.reflect.Method
import java.util
import org.apache.calcite.schema.Function
import lombok.extern.slf4j.Slf4j

/**
  * Schema Factory of Geomesa
  *
  * @author zaiyuan
  * @since 0.1.0
  */
class GeomesaSchemaFactory extends SchemaFactory {
  override def create(
      schemaPlus: SchemaPlus,
      schemaName: String,
      operands: util.Map[String, AnyRef]
  ): Schema = {
    initUdf(schemaPlus)
    initTableFunction(schemaPlus)
    new GeomesaSchema
  }

  def findMethod[T](clazz: Class[T], name: String): Boolean = {
    clazz.getMethods.foreach { method: Method =>
      if (method.getName == name && !method.isBridge) return true
    }
    false
  }

  @Slf4j
  private def initUdf(schemaPlus: SchemaPlus): Unit = {
    new UdfFactory().getUdfMap("Calcite").foreach {
      case (name, clazz) => {
        val instance = clazz.newInstance()
        val udfType: UdfType.Value =
          clazz.getDeclaredMethod("udfType").invoke(instance).asInstanceOf[UdfType.Value]
        var function: Function = null
        if (udfType == UdfType.Udf) {
          val udfEntryName: String =
            clazz.getDeclaredMethod("udfEntryName").invoke(instance).asInstanceOf[String]
          function = ScalarFunctionImpl.create(clazz, udfEntryName)
        } else if (udfType == UdfType.Udaf) function = AggregateFunctionImpl.create(clazz)
        else {
          val udfEntryName: String =
            clazz.getDeclaredMethod("udfEntryName").invoke(instance).asInstanceOf[String]
          function = TableFunctionImpl.create(clazz, udfEntryName)
        }
        if (function == null) {} else {
          schemaPlus.add(name, function)
        }
      }
    }
  }

  private def initTableFunction(schemaPlus: SchemaPlus): Unit = {
    schemaPlus.add("fibonacci", TableFunctionImpl.create(Fibonacci.FIBONACCI2_TABLE_METHOD))
    schemaPlus.add(
      "st_traj_timeIntervalSegment",
      TableFunctionImpl.create(TimeIntervalTrajectorySegment.TIMEINTERVALSEGMENT_TABLE_METHOD)
    )
    schemaPlus.add(
      "st_traj_stayPointDetect",
      TableFunctionImpl.create(StayPointDetect.STAY_POINT_DETECTION_TABLE_METHOD)
    )
    schemaPlus.add(
      "st_dbscan_clustering",
      TableFunctionImpl.create(DBSCANClustering.DBSCAN_CLUSTERING_TABLE_METHOD)
    )
    schemaPlus.add("st_collect_list", AggregateFunctionImpl.create(classOf[CollectList]))
  }

}
