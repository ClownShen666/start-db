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
package org.urbcomp.start.db.udf.datatypeconversionfunction

import org.urbcomp.start.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.start.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction
class CastToBooleanUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "castToBoolean"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)

  def eval(str: String): java.lang.Boolean = {

    Option(str) match {
      case Some(s) if s.equalsIgnoreCase("false") => false
      case Some(s) if s.equalsIgnoreCase("true")  => true
      case _                                      => null
    }

  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: String => java.lang.Boolean = eval
}
