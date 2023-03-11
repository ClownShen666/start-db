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

import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

trait AbstractUdf {

  def name(): String
  def udfType(): UdfType.Value
  def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)
}
