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
package org.urbcomp.cupid.db.udf

import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}
import org.urbcomp.cupid.db.udf.UdfType.Udf
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF

class StringSplitUdtf extends GenericUDTF with AbstractUdtf with Serializable {

  override def name(): String = "AddOne"

  override def udfType(): UdfType.Value = Udf

  override def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  override def process(objects: Array[AnyRef]): Unit = {
    val strings: Array[String] = objects(0).toString.split(" ")
    for (elem <- strings) {
      val tmp = new Array[String](1)
      tmp(0) = elem
      forward(tmp)
    }
  }

  override def close(): Unit = {}
}
