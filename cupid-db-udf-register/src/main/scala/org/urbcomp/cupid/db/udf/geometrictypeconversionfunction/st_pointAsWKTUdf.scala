package org.urbcomp.cupid.db.udf.geometrictypeconversionfunction

import org.apache.flink.table.annotation.DataTypeHint
import org.apache.flink.table.functions.ScalarFunction
import org.locationtech.jts.geom.{Geometry, Point}
import org.locationtech.jts.io.WKTWriter
import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Flink, Spark}

import java.io.{IOException, StringWriter}

class st_pointAsWKTUdf extends ScalarFunction with AbstractUdf {

  override def name(): String = "st_pointAsWKT"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark, Flink)
  @throws[IOException]
  def eval(@DataTypeHint(value = "RAW", bridgedTo = classOf[Point]) point: Point): java.lang.String = {
    if (point == null) null
    else {
      val wktWriter = new WKTWriter
      val writer = new StringWriter
      wktWriter.write(point, writer)
      writer.toString
    }
  }

  def udfSparkEntries: List[String] = List("udfWrapper")

  def udfWrapper: Point => java.lang.String = eval

}

