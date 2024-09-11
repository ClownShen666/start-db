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
package org.start.db.geomesa.storage.index.xzstar

import org.locationtech.geomesa.index.api.ShardStrategy.ZShardStrategy
import org.locationtech.geomesa.index.api.{GeoMesaFeatureIndex, IndexKeySpace}
import org.locationtech.geomesa.index.geotools.GeoMesaDataStore
import org.locationtech.geomesa.index.index.{ConfiguredIndex, SpatialIndex}
import org.locationtech.geomesa.index.strategies.SpatialFilterStrategy
import org.locationtech.geomesa.utils.index.IndexMode.IndexMode
import org.opengis.feature.simple.SimpleFeatureType

class XZStarIndex protected (
    ds: GeoMesaDataStore[_],
    sft: SimpleFeatureType,
    version: Int,
    geom: String,
    mode: IndexMode
) extends GeoMesaFeatureIndex[XZStarIndexValues, Long](
      ds,
      sft,
      XZStarIndex.name,
      version,
      Seq(geom),
      mode
    )
    with SpatialFilterStrategy[XZStarIndexValues, Long]
    with SpatialIndex[XZStarIndexValues, Long] {

  def this(ds: GeoMesaDataStore[_], sft: SimpleFeatureType, geom: String, mode: IndexMode) =
    this(ds, sft, XZStarIndex.version, geom, mode)

  override val keySpace: XZStarIndexKeySpace =
    new XZStarIndexKeySpace(sft, ZShardStrategy(sft), geom)

  override val tieredKeySpace: Option[IndexKeySpace[_, _]] = None
}

object XZStarIndex extends ConfiguredIndex {

  import org.locationtech.geomesa.utils.geotools.RichSimpleFeatureType.RichSimpleFeatureType

  override val name = "xzstar"
  override val version = 1

  override def supports(sft: SimpleFeatureType, attributes: Seq[String]): Boolean = true
//    XZStarIndexKeySpace.supports(sft, attributes)

  override def defaults(sft: SimpleFeatureType): Seq[Seq[String]] =
    if (sft.nonPoints) {
      Seq(Seq(sft.getGeomField))
    } else {
      Seq.empty
    }

}
