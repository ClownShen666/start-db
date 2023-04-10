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
package org.urbcomp.cupid.db.udf.mathfuction

import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

import java.math.BigDecimal

class Pi extends AbstractUdf {

  override def name(): String = "pi"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  /**
    * The double value that is closer than any other to pi
    *
    * @return PI double.
    */
  def evaluate(): BigDecimal = {
    BigDecimal.valueOf(Math.PI)
  }
}
