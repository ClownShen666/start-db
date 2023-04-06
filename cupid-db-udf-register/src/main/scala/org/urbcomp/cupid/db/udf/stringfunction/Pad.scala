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

import scala.collection.mutable

trait Pad {
  def pad(str: String, len: Int, pad: String): String = {
    if (str == null || pad == null) null
    else if (len < str.length) str.substring(0, len)
    else {
      val sb = new mutable.StringBuilder()
      var i = 0
      while (i < len - str.length) {
        sb.append(pad)
        i += 1
      }
      sb.toString()
    }
  }
}
