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
package org.urbcomp.start.db.udf.geometrictypeconversionfunction

import org.apache.flink.table.annotation.DataTypeHint
import org.locationtech.geomesa.spark.jts.util.GeoHashUtils
import org.locationtech.jts.geom.Point
import org.urbcomp.start.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.start.db.udf.{AbstractUdf, DataEngine}

class st_pointFromGeoHashUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_pointFromGeoHash"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  @DataTypeHint(value = "RAW", bridgedTo = classOf[Point])
  def eval(geoHashStr: String, precision: Int): Point = {
    if (geoHashStr == null || Int == null) null
    else GeoHashUtils.decode(geoHashStr, precision).getInteriorPoint
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (String, Int) => Point = eval

}
