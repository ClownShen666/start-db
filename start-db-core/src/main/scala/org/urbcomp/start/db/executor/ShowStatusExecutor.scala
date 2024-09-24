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

import scala.language.higherKinds

/**
  * @author jimo
  * */
case class ShowStatusExecutor() extends BaseExecutor {

  override def execute[Array](): MetadataResult[Array] = {
    MetadataResult
      .buildResult(Array("Variable_name", "Value"), java.util.Arrays.asList(Array("status", "ok")))
      .asInstanceOf[MetadataResult[Array]]
  }
}
