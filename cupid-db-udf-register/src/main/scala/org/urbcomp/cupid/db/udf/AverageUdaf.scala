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

import org.apache.spark.sql.{Encoder, Encoders, functions}
import org.apache.spark.sql.expressions.{Aggregator, UserDefinedFunction}

case class AverageData(var sum: Long, var count: Long)

object AverageUdaf extends Aggregator[Long, AverageData, Double] {
  // A zero value for this aggregation. Should satisfy the property that any b + zero = b
  def zero: AverageData = AverageData(0L, 0L)
  // Combine two values to produce a new value. For performance, the function may modify `buffer`
  // and return it instead of constructing a new object
  def reduce(buffer: AverageData, data: Long): AverageData = {
    buffer.sum += data
    buffer.count += 1
    buffer
  }
  // Merge two intermediate values
  def merge(b1: AverageData, b2: AverageData): AverageData = {
    b1.sum += b2.sum
    b1.count += b2.count
    b1
  }
  // Transform the output of the reduction
  def finish(reduction: AverageData): Double = reduction.sum.toDouble / reduction.count
  // Specifies the Encoder for the intermediate value type
  def bufferEncoder: Encoder[AverageData] = Encoders.product
  // Specifies the Encoder for the final output value type
  def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}

class AverageUdaf extends AbstractUdf {
  override def name(): String = "Average"

  override def registerCalcite(): Boolean = true

  override def registerSpark(): Boolean = true

  override def function(): UserDefinedFunction = functions.udaf(AverageUdaf)
}