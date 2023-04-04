package org.urbcomp.cupid.db.udf.mathfuction

import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

import java.math.BigDecimal

class Log extends Serializable with AbstractUdf {

  override def name(): String = "log"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  override def udfCalciteEntryName(): String = "udfImpl"

  override def udfSparkEntryName(): String = "udfWrapper"

  /**
   * Returns the (base) logarithm of a double value (num).
   *
   * @param base double
   * @param num  double
   * @return log result
   */
  def udfImpl(base: BigDecimal, num: BigDecimal): BigDecimal = {
    if (base == null || num == null || base.doubleValue <= 0 || base.doubleValue == 1 || num.doubleValue == 0) return null
    val res = Math.log(num.doubleValue) / Math.log(base.doubleValue)
     BigDecimal.valueOf(res)  }

  def udfWrapper: (BigDecimal,BigDecimal) => BigDecimal = udfImpl
}
