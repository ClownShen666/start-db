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

package org.cupid.db.geomesa.storage.index.z2t

import org.locationtech.geomesa.index.api.ShardStrategy.ZShardStrategy
import org.locationtech.geomesa.index.api.{GeoMesaFeatureIndex, IndexKeySpace}
import org.locationtech.geomesa.index.geotools.GeoMesaDataStore
import org.locationtech.geomesa.index.index.{ConfiguredIndex, SpatioTemporalIndex}
import org.locationtech.geomesa.index.strategies.SpatioTemporalFilterStrategy
import org.locationtech.geomesa.utils.index.IndexMode.IndexMode
import org.opengis.feature.simple.SimpleFeatureType

class Z2TIndex protected (
    ds: GeoMesaDataStore[_],
    sft: SimpleFeatureType,
    version: Int,
    geom: String,
    dtg: String,
    mode: IndexMode
    ) extends GeoMesaFeatureIndex[Z2TIndexValues, Z2TIndexKey](ds, sft, Z2TIndex.name, version, Seq(geom, dtg), mode)
  with SpatioTemporalFilterStrategy[Z2TIndexValues, Z2TIndexKey]
  with SpatioTemporalIndex[Z2TIndexValues, Z2TIndexKey] {

  def this(ds: GeoMesaDataStore[_], sft: SimpleFeatureType, geomField: String, dtgField: String, mode: IndexMode) =
    this(ds, sft, Z2TIndex.version, geomField, dtgField, mode)

  override val keySpace: Z2TIndexKeySpace = new Z2TIndexKeySpace(sft, ZShardStrategy(sft), geom, dtg)

  override val tieredKeySpace: Option[IndexKeySpace[_, _]] = None
}

object Z2TIndex extends ConfiguredIndex {

  import org.locationtech.geomesa.utils.geotools.RichSimpleFeatureType.RichSimpleFeatureType

  override val name = "z2t"
  override val version = 1

  override def supports(sft: SimpleFeatureType, attributes: Seq[String]): Boolean =
    Z2TIndexKeySpace.supports(sft, attributes)

  override def defaults(sft: SimpleFeatureType): Seq[Seq[String]] = {
    if (sft.isPoints && sft.getDtgField.isDefined) { Seq(Seq(sft.getGeomField, sft.getDtgField.get)) } else { Seq.empty }
  }

}
