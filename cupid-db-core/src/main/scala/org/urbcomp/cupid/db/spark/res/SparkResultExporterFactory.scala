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
package org.urbcomp.cupid.db.spark.res

import org.urbcomp.cupid.db.model.data.DataExportType
import org.urbcomp.cupid.db.spark.SparkQueryExecutor.log

import java.util.ServiceLoader
import scala.collection.mutable

/**
  * @author jimo
  * */
object SparkResultExporterFactory {

  def getInstances: mutable.Map[DataExportType, ISparkResultExporter] = {
    val exporters: ServiceLoader[ISparkResultExporter] =
      ServiceLoader.load(classOf[ISparkResultExporter])
    val it = exporters.iterator()
    val map = new mutable.HashMap[DataExportType, ISparkResultExporter]()
    while (it.hasNext) {
      val exporter = it.next()
      map.put(exporter.getType, exporter)
      log.info("add exporter " + exporter + " with type " + exporter.getType)
    }
    map
  }

  private val INSTANCES = getInstances

  def getInstance(exportType: DataExportType): ISparkResultExporter = {

    val e = INSTANCES.get(exportType)
    if (e == null) {
      throw new IllegalArgumentException("not support type now:" + exportType)
    }
    e.get
  }
}
