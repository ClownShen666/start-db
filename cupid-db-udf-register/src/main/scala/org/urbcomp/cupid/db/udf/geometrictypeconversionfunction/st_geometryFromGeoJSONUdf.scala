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
package org.urbcomp.cupid.db.udf.geometrictypeconversionfunction

import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.io.ParseException
import org.locationtech.jts.io.geojson.GeoJsonReader
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

class st_geometryFromGeoJSONUdf extends AbstractUdf {

  override def name(): String = "st_geometryFromGeoJSON"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)
  @throws[ParseException]
  def evaluate(geoJson: String): Geometry = {
    if (geoJson == null) null
    else {
      val geoJsonReader = new GeoJsonReader
      geoJsonReader.read(geoJson)
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: String => Geometry = evaluate

}
