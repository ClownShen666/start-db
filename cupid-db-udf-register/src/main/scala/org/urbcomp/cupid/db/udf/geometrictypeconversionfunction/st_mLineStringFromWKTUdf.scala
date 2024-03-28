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

import org.apache.flink.table.annotation.DataTypeHint
import org.apache.flink.table.functions.ScalarFunction
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.io.ParseException
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}

class st_mLineStringFromWKTUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_mLineStringFromWKT"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)
  @throws[ParseException]
  @DataTypeHint(value = "RAW", bridgedTo = classOf[MultiLineString])
  def eval(wktString: String): MultiLineString = {
    if (wktString == null) null
    else {
      castToMLineString(geomFromWKT(wktString))
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: String => MultiLineString = eval
}
