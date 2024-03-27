package org.urbcomp.cupid.db.udf.geometrictypeconversionfunction

import org.apache.flink.table.annotation.DataTypeHint
import org.apache.flink.table.functions.ScalarFunction
import org.locationtech.jts.geom.MultiPoint
import org.locationtech.jts.io.WKTWriter
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Flink, Spark}

import java.io.{IOException, StringWriter}

class st_mPointAsWKTUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_mPointAsWKT"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)
  @throws[IOException]
  def eval(@DataTypeHint(value = "RAW", bridgedTo = classOf[MultiPoint]) mPoint: MultiPoint): java.lang.String = {
    if (mPoint == null) null
    else {
      val wktWriter = new WKTWriter
      val writer = new StringWriter
      wktWriter.write(mPoint, writer)
      writer.toString
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: MultiPoint => java.lang.String = eval

}
