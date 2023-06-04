package org.urbcomp.cupid.db.udf.stringfunction

import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

import scala.collection.mutable

class PadUdf extends AbstractUdf {
  override def name(): String = "pad"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  def evaluate(str: String, len: Int, pad: String): String = {
    if (str == null || pad == null) null
    else if (len < str.length) str.substring(0, len)
    else {
      val sb = new mutable.StringBuilder()
      var i = 0
      while (i < len - str.length) {
        sb.append(pad)
        i += 1
      }
      sb.toString()
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: (String, Int, String) => String = evaluate
}
