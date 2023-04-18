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
package org.urbcomp.cupid.db.data.hbase

import org.geotools.data.{DataStore, Query, Transaction}
import org.geotools.feature.simple.{SimpleFeatureBuilder, SimpleFeatureTypeBuilder}
import org.geotools.filter.identity.FeatureIdImpl
import org.geotools.geometry.jts.JTSFactoryFinder
import org.geotools.util.factory.Hints
import org.junit.Assert.assertEquals
import org.locationtech.geomesa.utils.geotools.SimpleFeatureTypes.Configs
import org.locationtech.jts.geom.Coordinate
import org.opengis.feature.simple.{SimpleFeature, SimpleFeatureType}
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.urbcomp.cupid.db.data.hbase.{CupidHBaseDataStore, CupidHBaseDataStoreFactory}
import org.urbcomp.cupid.db.executor.utils.ExecutorUtil
import org.urbcomp.cupid.db.util.DataTypeUtils

import java.io.{IOException, Serializable}
import java.util.{Date, UUID}

class CupidHBaseDataStoreTest extends FunSuite with BeforeAndAfterAll {

  test("create then drop indices") {
    val params = ExecutorUtil.getDataStoreParams("cupid_db", "default")
    val store = new CupidHBaseDataStoreFactory()
      .createDataStore(params.asInstanceOf[java.util.Map[String, Serializable]])
      .asInstanceOf[CupidHBaseDataStore]

    // create a schema with 1 z3 index and 1 z2t index
    val schemaName = "dropIndexTest"
    store.removeSchema(schemaName)
    val sfb = new SimpleFeatureTypeBuilder
    sfb.setName(schemaName)
    sfb.add("name", DataTypeUtils.getClass("String"))
    sfb.add("st", DataTypeUtils.getClass("Point"), 4326)
    sfb.add("et", DataTypeUtils.getClass("Point"), 4326)
    sfb.add("dtg", DataTypeUtils.getClass("Datetime"))
    val sft = sfb.buildFeatureType()
    sft.getUserData.put(Configs.EnabledIndices, "z3:st:dtg,z2t:et:dtg")
    store.createSchema(sft)

    val saved = store.getSchema(schemaName)
    assertEquals(saved.getUserData.get("geomesa.indices").asInstanceOf[String].split(",").length, 2)
    writeData(store, saved)
    val savedCount = readAll(store, saved)
    assertEquals(1, savedCount)

    // drop 1 z3 index
    store.dropIndices(schemaName, "z3:st:dtg")
    val updated = store.getSchema(schemaName)

    // assert only left one index and data can still be fetched
    assertEquals(
      updated.getUserData.get("geomesa.indices").asInstanceOf[String].split(",").length,
      1
    )
    val updatedCount = readAll(store, updated)
    assertEquals(updatedCount, 1)
    store.removeSchema(schemaName)
  }

  private def writeData(store: DataStore, sft: SimpleFeatureType): Unit = {
    val featureBuilder = new SimpleFeatureBuilder(sft)
    val geometryFactory = JTSFactoryFinder.getGeometryFactory
    val st = geometryFactory.createPoint(new Coordinate(1.0d, 1.0d))
    val et = geometryFactory.createPoint(new Coordinate(2.0d, 2.0d))
    val dtg = new Date()
    featureBuilder.set("name", "test-record-1")
    featureBuilder.set("st", st)
    featureBuilder.set("et", et)
    featureBuilder.set("dtg", dtg)
    val feat = featureBuilder.buildFeature(UUID.randomUUID().toString)
    writeFeature(store, sft, feat)
  }

  @throws[IOException]
  private def writeFeature(
      dataStore: DataStore,
      sft: SimpleFeatureType,
      feature: SimpleFeature
  ): Unit = {
    val writer = dataStore.getFeatureWriterAppend(sft.getTypeName, Transaction.AUTO_COMMIT)
    val toWrite = writer.next
    toWrite.setAttributes(feature.getAttributes)
    toWrite.getIdentifier.asInstanceOf[FeatureIdImpl].setID(feature.getID)
    toWrite.getUserData.put(Hints.USE_PROVIDED_FID, java.lang.Boolean.TRUE)
    toWrite.getUserData.putAll(feature.getUserData)
    writer.write()
    writer.close()
  }

  private def readAll(store: DataStore, sft: SimpleFeatureType): Int = {
    var count = 0
    val reader = store.getFeatureReader(new Query(sft.getTypeName), Transaction.AUTO_COMMIT)
    while ({ reader.hasNext }) {
      reader.next
      count += 1
    }
    count
  }

}
