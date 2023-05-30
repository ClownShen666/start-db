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
package org.urbcomp.cupid.db.udf.geometricoperationfunction

import org.locationtech.jts.geom.Geometry
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

class st_isValidUdf extends AbstractUdf {

  override def name(): String = "st_isValid"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  def evaluate(geom: Geometry): java.lang.Boolean = Some(geom).map(_.isValid) match {
    case Some(s) => s
    case _       => null
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: Geometry => java.lang.Boolean = evaluate
}
