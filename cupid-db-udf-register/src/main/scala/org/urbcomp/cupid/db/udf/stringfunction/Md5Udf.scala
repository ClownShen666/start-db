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
package org.urbcomp.cupid.db.udf.stringfunction

import org.urbcomp.cupid.db.udf.{AbstractUdf, DataEngine}
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

import java.security.MessageDigest

class Md5Udf extends AbstractUdf {
  override def name(): String = "md5"

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  def evaluate(str: String): String = {
    val hexDigits =
      Array[Char]('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
    val btInput = str.getBytes()
    val mdInst = MessageDigest.getInstance("MD5")
    mdInst.update(btInput)
    val md = mdInst.digest()
    val j = md.length
    val code = new Array[Char](j * 2)
    var k = 0
    for (e <- md) {
      code(k) = hexDigits(e >>> 4 & 0xf)
      k += 1
      code(k) = hexDigits(e & 0xf)
      k += 1
    }
    new String(code)
  }
}
