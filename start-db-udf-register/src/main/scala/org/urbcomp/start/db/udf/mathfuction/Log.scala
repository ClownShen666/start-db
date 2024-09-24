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
package org.urbcomp.start.db.udf.mathfuction

import org.apache.flink.table.annotation.DataTypeHint
import org.urbcomp.start.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.start.db.udf.DataEngine.{Calcite, Flink, Spark}
import org.apache.flink.table.functions.ScalarFunction

import java.math.BigDecimal

class Log extends ScalarFunction with AbstractUdf {

  override def name(): String = "log"

  // There is an udf with the same name in flink
  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  /**
    * Returns the (base) logarithm of a double value (num).
    *
    * @param base double
    * @param num  double
    * @return log result
    */
  @DataTypeHint(value = "RAW", bridgedTo = classOf[BigDecimal])
  def eval(
      @DataTypeHint(value = "RAW", bridgedTo = classOf[BigDecimal]) base: BigDecimal,
      @DataTypeHint(value = "RAW", bridgedTo = classOf[BigDecimal]) num: BigDecimal
  ): BigDecimal = {
    if (base == null || num == null || base.doubleValue <= 0 || base.doubleValue == 1 || num.doubleValue == 0)
      return null
    val res = Math.log(num.doubleValue) / Math.log(base.doubleValue)
    BigDecimal.valueOf(res)
  }

  @DataTypeHint(value = "RAW", bridgedTo = classOf[BigDecimal])
  def eval(base: java.lang.Double, num: java.lang.Double): BigDecimal = {
    if (base == null || num == null || base.doubleValue <= 0 || base.doubleValue == 1 || num.doubleValue == 0)
      return null
    val res = Math.log(num.doubleValue) / Math.log(base.doubleValue)
    BigDecimal.valueOf(res)
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (BigDecimal, BigDecimal) => BigDecimal = eval
}
