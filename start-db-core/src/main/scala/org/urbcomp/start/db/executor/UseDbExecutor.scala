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
package org.urbcomp.start.db.executor

import org.urbcomp.start.db.infra.{BaseExecutor, MetadataResult}
import org.urbcomp.start.db.metadata.MetadataAccessUtil
import org.urbcomp.start.db.parser.ddl.SqlUseDatabase
import org.urbcomp.start.db.util.SqlParam

/**
  * use db
  * update cache
  *
  * @author jimo
  * */
case class UseDbExecutor(n: SqlUseDatabase) extends BaseExecutor {

  override def execute[Int](): MetadataResult[Int] = {
    val param = SqlParam.CACHE.get()
    // check db exists
    val dbName = n.getFullDatabaseName
    val db = MetadataAccessUtil.getDatabase(param.getUserName, dbName)
    if (db == null) {
      throw new IllegalArgumentException(s"db[$dbName] not exists")
    }
    param.setDbName(dbName)
    MetadataResult.buildDDLResult(1)
  }
}
