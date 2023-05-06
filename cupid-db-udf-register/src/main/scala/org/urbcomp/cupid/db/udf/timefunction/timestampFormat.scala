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
package org.urbcomp.cupid.db.udf.timefunction

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
class timestampFormat extends AbstractUdf {

  override def name(): String = "timestampFormat"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  /**
    * Formats the timestamp in the specified format
    *
    * @param ts     timestamp
    * @param string time format
    * @return the specified format instance
    */
  def evaluate(ts: Timestamp, string: String): String = {
    val simpleDateFormat = new SimpleDateFormat(string)
    simpleDateFormat.format(new Date(ts.getTime))
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (Timestamp, String) => String = evaluate

}
