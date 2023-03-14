/*
 * Copyright 2022 ST-Lab
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */

package org.urbcomp.cupid.db.udf

import org.apache.spark.sql.functions.udf
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.udf.UdfType.Udf

class AddOneUdf extends Serializable with AbstractUdf {

  override def name(): String = "AddOne"

  override def udfType(): UdfType.Value = Udf

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  def udfImpl(x: Int): Int = { x + 1 }

  def udfEntryName(): String = "udfImpl"

  // Must use this variable to register in Spark
  def udfWrapper: Function1[Int, Int] = udfImpl
}
