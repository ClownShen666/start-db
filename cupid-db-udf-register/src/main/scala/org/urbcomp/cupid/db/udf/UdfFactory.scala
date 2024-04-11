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
package org.urbcomp.cupid.db.udf

import org.reflections.Reflections
import scala.collection.convert.ImplicitConversions._
import scala.collection.mutable

class UdfFactory extends Serializable {

  // map the Engine and it's registered udfs
  private val engineUdfMap =
    mutable.HashMap[DataEngine.Value, mutable.Map[String, Class[_ <: AbstractUdf]]]()

  {

    // scan the package and get the udf which extends the AbstractUdf
    val reflections = new Reflections("org.urbcomp.cupid.db.udf")
    val udfClasses =
      reflections.getSubTypesOf(classOf[AbstractUdf]).toSet[Class[_ <: AbstractUdf]].toList
    udfClasses.forEach(clazz => {
      val instance = clazz.newInstance()

      // get udf's name
      val name: String = clazz.getDeclaredMethod("name").invoke(instance).asInstanceOf[String]

      // get udf's registerEngines
      val registerEngines: List[DataEngine.Value] =
        clazz
          .getDeclaredMethod("registerEngines")
          .invoke(instance)
          .asInstanceOf[List[DataEngine.Value]]

      // add engineUdfMap
      registerEngines.foreach { engine =>
        if (!engineUdfMap.contains(engine))
          engineUdfMap += (engine -> mutable.Map[String, Class[_ <: AbstractUdf]]())
        engineUdfMap(engine) += (name -> clazz)
      }
    })
  }

  def getUdfMap(engine: DataEngine.Value): mutable.Map[String, Class[_ <: AbstractUdf]] = {
    engineUdfMap.getOrElse(engine, mutable.Map[String, Class[_ <: AbstractUdf]]())
  }
  def getEngineUdfMap
      : mutable.HashMap[DataEngine.Value, mutable.Map[String, Class[_ <: AbstractUdf]]] =
    engineUdfMap

  private val flinkProcessedUdf: mutable.HashSet[String] = mutable.HashSet(
    "st_contains",
    "st_covers",
    "st_crosses",
    "st_disjoint",
    "st_equals",
    "st_intersects",
    "st_overlaps",
    "st_relate",
    "st_touches",
    "st_within",
    "st_area",
    "st_BBox",
    "st_centroid",
    "st_closestPoint",
    "st_convexHull",
    "st_difference",
    "st_distanceSphere",
    "st_distanceSpheroid",
    "st_distance",
    "st_intersection",
    "st_isValid",
    "st_length",
    "st_numPoints",
    "st_x",
    "st_y",
    "st_asGeoJSON",
    "st_asWKB",
    "st_asWKT",
    "st_castToLineString",
    "st_castToMLineString",
    "st_castToMPoint",
    "st_castToMPolygon",
    "st_castToPoint",
    "st_castToPolygon"
  )

  def getFlinkProcessedUdf: mutable.HashSet[String] = flinkProcessedUdf
}
