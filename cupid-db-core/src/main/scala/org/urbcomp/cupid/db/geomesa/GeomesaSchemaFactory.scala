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
package org.urbcomp.cupid.db.geomesa

import org.apache.calcite.schema.impl.{AggregateFunctionImpl, ScalarFunctionImpl, TableFunctionImpl}
import org.apache.calcite.schema.{Schema, SchemaFactory, SchemaPlus}
import org.urbcomp.cupid.db.function.udaf.CollectList
import org.urbcomp.cupid.db.udf.{DataEngine, UdfFactory}
import org.urbcomp.cupid.db.udtf.{
  AbstractUdtf,
  DBSCANClustering,
  Fibonacci,
  HybridTrajectorySegment,
  KMeansClustering,
  StayPointDetect,
  StayPointTrajectorySegment,
  TimeIntervalTrajectorySegment
}

import scala.collection.convert.ImplicitConversions._
import java.lang.reflect.Method
import java.util
import org.apache.calcite.schema.Function
import lombok.extern.slf4j.Slf4j
import org.reflections.Reflections
import org.urbcomp.cupid.db.udf.DataEngine.Calcite
import org.slf4j.Logger
import org.urbcomp.cupid.db.util.LogUtil

/**
  * Schema Factory of Geomesa
  *
  * @author zaiyuan
  * @since 0.1.0
  */
@Slf4j
class GeomesaSchemaFactory extends SchemaFactory {
  val log: Logger = LogUtil.getLogger

  override def create(
      schemaPlus: SchemaPlus,
      schemaName: String,
      operands: util.Map[String, AnyRef]
  ): Schema = {
    initUdf(schemaPlus)
    initTableFunction(schemaPlus)
    new GeomesaSchema
  }

  def getAllMethods[T](clazz: Class[T], name: String): Array[Method] = {
    clazz.getMethods.filter(method => method.getName == name && !method.isBridge)
  }

  private def initUdf(schemaPlus: SchemaPlus): Unit = {
    new UdfFactory().getUdfMap(Calcite).foreach {
      case (name, clazz) =>
        getAllMethods(clazz, "evaluate").foreach {
          case method: Method =>
            val function: Function = ScalarFunctionImpl.create(method)
            if (function != null) {
              log.warn("Calcite registers udf " + name + " using " + method.toString)
              schemaPlus.add(name, function)
            } else {
              log.warn("Calcite cannot register udf " + name)
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
      if (registerEngines.contains(Calcite)) {
        val inputColumnsCount: Int =
          clazz.getDeclaredMethod("inputColumnsCount").invoke(instance).asInstanceOf[Int]
        val function: Function =
          TableFunctionImpl.create(clazz, "udtfCalciteEntry" + inputColumnsCount.toString)
        if (function != null) {
          log.warn("Calcite registers udtf " + name)
          schemaPlus.add(name, function)
        } else {
          log.warn("Calcite cannot register udtf " + name)
        }
      }
    })
  }

  private def initTableFunction(schemaPlus: SchemaPlus): Unit = {
    schemaPlus.add("fibonacci", TableFunctionImpl.create(Fibonacci.FIBONACCI2_TABLE_METHOD))
    schemaPlus.add(
      "st_traj_timeIntervalSegment",
      TableFunctionImpl.create(TimeIntervalTrajectorySegment.TIMEINTERVALSEGMENT_TABLE_METHOD)
    )
    schemaPlus.add(
      "st_traj_stayPointSegment",
      TableFunctionImpl.create(StayPointTrajectorySegment.STAYPOINTSEGMENT_TABLE_METHOD)
    )
    schemaPlus.add(
      "st_traj_hybridSegment",
      TableFunctionImpl.create(HybridTrajectorySegment.HYBRID_SEGMENT_TABLE_METHOD)
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
    schemaPlus.add(
      "st_kmeans_clustering",
      TableFunctionImpl.create(KMeansClustering.KMEANS_CLUSTERING_TABLE_METHOD)
    )
  }

}
