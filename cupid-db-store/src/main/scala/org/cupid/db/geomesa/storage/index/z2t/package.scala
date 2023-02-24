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

package org.cupid.db.geomesa.storage.index

import org.cupid.db.geomesa.storage.curve.Z2TSFC
import org.locationtech.geomesa.filter.{Bounds, FilterValues}
import org.locationtech.geomesa.index.index.{SpatialIndexValues, TemporalIndexValues}
import org.locationtech.jts.geom.Geometry
import java.time.ZonedDateTime

package object z2t {

  /**
    * Index key for z2t values
    *
    * @param bin date epoch
    * @param z z2t value within the epoch
    */
  case class Z2TIndexKey(bin: Short, z: Long) extends Ordered[Z2TIndexKey] {
    override def compare(that: Z2TIndexKey): Int = {
      val b = Ordering.Short.compare(bin, that.bin)
      if (b != 0) {
        b
      } else {
        Ordering.Long.compare(z, that.z)
      }
    }
  }

  /**
    * Index values extracted from a filter for z2t queries
    *
    * @param sfc               specific curve being used
    * @param geometries        extracted geometries
    * @param spatialBounds     the spatial bounds from the extracted geometries, as bounding boxes
    * @param intervals         extracted dates
    * @param temporalBounds    the temporal bounds from the extracted dates, as time units (depending on the sfc),
    *                          keyed by epoch
    * @param temporalUnbounded unbounded temporal epochs, i.e. all time values are covered. will be either
    *                          `(0, t)`, `(t, Short.MaxValue)` or `(0, Short.MaxValue)` for upper, lower,
    *                          and unbounded queries, respectively
    */
  case class Z2TIndexValues(
      SFC: Z2TSFC,
      geometries: FilterValues[Geometry],
      spatialBounds: Seq[(Double, Double, Double, Double)],
      intervals: FilterValues[Bounds[ZonedDateTime]],
      temporalBounds: Map[Short, Seq[(Long, Long)]],
      temporalUnbounded: Seq[(Short, Short)]
  ) extends TemporalIndexValues
      with SpatialIndexValues

}
