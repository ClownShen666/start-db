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
package org.cupid.db.geomesa.storage.index.xzstar

import org.locationtech.geomesa.index.api.{GeoMesaFeatureIndex, IndexKeySpace}
import org.locationtech.geomesa.index.api.ShardStrategy.ZShardStrategy
import org.locationtech.geomesa.index.geotools.GeoMesaDataStore
import org.locationtech.geomesa.index.index.{ConfiguredIndex, SpatioTemporalIndex}
import org.locationtech.geomesa.index.strategies.SpatioTemporalFilterStrategy
import org.locationtech.geomesa.utils.index.IndexMode.IndexMode
import org.opengis.feature.simple.SimpleFeatureType

class XZStarTIndex protected (
    ds: GeoMesaDataStore[_],
    sft: SimpleFeatureType,
    version: Int,
    geom: String,
    dtg: String,
    mode: IndexMode
) extends GeoMesaFeatureIndex[XZStarTIndexValues, XZStarTIndexKey](
      ds,
      sft,
      XZStarTIndex.name,
      version,
      Seq(geom, dtg),
      mode
    )
    with SpatioTemporalFilterStrategy[XZStarTIndexValues, XZStarTIndexKey]
    with SpatioTemporalIndex[XZStarTIndexValues, XZStarTIndexKey] {

  def this(
      ds: GeoMesaDataStore[_],
      sft: SimpleFeatureType,
      geomField: String,
      dtgField: String,
      mode: IndexMode
  ) =
    this(ds, sft, XZStarTIndex.version, geomField, dtgField, mode)

  override val keySpace: XZStarTIndexKeySpace =
    new XZStarTIndexKeySpace(sft, ZShardStrategy(sft), geom, dtg)

  override val tieredKeySpace: Option[IndexKeySpace[_, _]] = None

}

object XZStarTIndex extends ConfiguredIndex {

  import org.locationtech.geomesa.utils.geotools.RichSimpleFeatureType.RichSimpleFeatureType

  override val name = "xzstart"
  override val version = 1

  override def supports(sft: SimpleFeatureType, attributes: Seq[String]): Boolean =
    XZStarTIndexKeySpace.supports(sft, attributes)

  override def defaults(sft: SimpleFeatureType): Seq[Seq[String]] =
    if (sft.nonPoints && sft.getDtgField.isDefined) {
      Seq(Seq(sft.getGeomField, sft.getDtgField.get))
    } else {
      Seq.empty
    }

}
