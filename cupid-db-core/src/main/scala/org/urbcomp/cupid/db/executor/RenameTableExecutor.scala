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
package org.urbcomp.cupid.db.executor

import org.geotools.data.DataStoreFinder
import org.geotools.feature.simple.SimpleFeatureTypeBuilder
import org.opengis.feature.simple.SimpleFeatureType
import org.urbcomp.cupid.db.executor.utils.ExecutorUtil
import org.urbcomp.cupid.db.infra.{BaseExecutor, MetadataResult}
import org.urbcomp.cupid.db.metadata.MetadataAccessUtil
import org.urbcomp.cupid.db.parser.ddl.SqlRenameTable

import java.io.IOException

case class RenameTableExecutor(n: SqlRenameTable) extends BaseExecutor {

  override def execute[Int](): MetadataResult[Int] = {
    val targetTable = n.oldName
    val (userName, dbName, tableName) = ExecutorUtil.getUserNameDbNameAndTableName(targetTable)
    val db = MetadataAccessUtil.getDatabase(userName, dbName)
    val existedTable = MetadataAccessUtil.getTable(db.getId, tableName)
    if (existedTable == null) {
      throw new Exception("Table doesn't exist: " + tableName)
    } else {
      try {
        existedTable.setName(n.newName.toString)
        MetadataAccessUtil.updateTable(existedTable)
        val affectRows = 1
        MetadataResult.buildDDLResult(affectRows)
      } catch {
        case ex: IOException => throw new Exception("Failed to rename table: " + tableName, ex)
      }
    }
  }
}
