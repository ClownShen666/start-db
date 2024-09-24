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
package org.urbcomp.start.db.data.hbase

import org.geotools.feature.NameImpl
import org.geotools.feature.simple.SimpleFeatureTypeBuilder
import org.locationtech.geomesa.hbase.data.HBaseConnectionPool.ConnectionWrapper
import org.locationtech.geomesa.hbase.data.HBaseDataStore
import org.locationtech.geomesa.hbase.data.HBaseDataStoreFactory.HBaseDataStoreConfig
import org.locationtech.geomesa.index.api.{GeoMesaFeatureIndex, GeoMesaFeatureIndexFactory}
import org.locationtech.geomesa.index.metadata.GeoMesaMetadata.AttributesKey
import org.locationtech.geomesa.utils.conf.IndexId
import org.locationtech.geomesa.utils.geotools.RichSimpleFeatureType.RichSimpleFeatureType
import org.locationtech.geomesa.utils.geotools.SimpleFeatureTypes
import org.locationtech.geomesa.utils.geotools.SimpleFeatureTypes.Configs
import org.locationtech.geomesa.utils.geotools.converters.FastConverter
import org.opengis.feature.`type`.Name
import org.opengis.feature.simple.SimpleFeatureType

/**
  * It will be fascinate if the original HBaseDataStore#updateSchema support update
  * indices. But it is not because update indexes need to create a new index table
  * and then re write all data.
  *
  *
  * StartHbaseDataStore extend the original DataStore and provide a new #dropIndices
  * interface for drop index
  */
class StartHBaseDataStore(con: ConnectionWrapper, config: HBaseDataStoreConfig)
    extends HBaseDataStore(con, config) {

  def dropIndices(typeName: String, dropping: String): Unit = {
    dropIndices(new NameImpl(typeName), dropping)
  }

  private def getDropCandidates(previous: SimpleFeatureType, dropping: String): Seq[IndexId] = {
    val builder = new SimpleFeatureTypeBuilder()
    builder.init(previous)
    val copy = builder.buildFeatureType()
    copy.getUserData.putAll(previous.getUserData)
    copy.getUserData.put(Configs.EnabledIndices, dropping)
    GeoMesaFeatureIndexFactory.indices(copy)
  }

  private def dropIndices(typeName: Name, dropping: String): Unit = {
    val lock = acquireCatalogLock()
    try {
      // get previous schema and user data
      val previous = getSchema(typeName)
      if (previous == null) {
        throw new IllegalArgumentException(s"Schema '$typeName' does not exist")
      }

      val candidates = getDropCandidates(previous, dropping)
      val existing = manager.indices(previous)
      val toDrop = candidates.flatMap { i =>
        existing.find(e => e.name == i.name && e.attributes == i.attributes)
      }
      val toDropIds = toDrop.map { e =>
        IndexId(e.name, e.version, e.attributes, e.mode)
      }
      val toKeep = previous.getIndices.filter(i => !toDropIds.contains(i))

      if (toKeep.isEmpty) {
        throw new IllegalStateException(s"At least 1 index need to be kept")
      }

      val sft = SimpleFeatureTypes.mutable(previous)
      sft.setIndices(toKeep)

      // if all is well, update the metadata - first back it up
      if (FastConverter.convertOrElse[java.lang.Boolean](
            sft.getUserData.get(Configs.UpdateBackupMetadata),
            true
          )) {
        metadata.backup(typeName.getLocalPart)
      }

      // now insert the new spec string
      metadata.insert(
        sft.getTypeName,
        AttributesKey,
        SimpleFeatureTypes.encodeType(sft, includeUserData = true)
      )

      // delete dropped index table
      onIndicesDropped(toDrop)
    } finally {
      lock.release()
    }
  }

  // create or delete real index tables
  private def onIndicesDropped(dropped: Seq[GeoMesaFeatureIndex[_, _]]): Unit = {
    if (dropped.nonEmpty) {
      dropped.foreach(index => adapter.deleteTables(index.deleteTableNames(None)))
      logger.info(s"removed indices ${dropped.map(_.identifier).mkString(",")}")
    }
  }
}
