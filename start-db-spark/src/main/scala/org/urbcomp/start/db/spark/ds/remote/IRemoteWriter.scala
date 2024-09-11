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
package org.urbcomp.start.db.spark.ds.remote

import org.apache.spark.sql.catalyst.InternalRow
import org.urbcomp.start.db.spark.SparkQueryExecutor.log

import java.util

/**
  * 写接口抽象,实现可考虑不同协议和序列化
  *
  * @author jimo
  * */
trait IRemoteWriter extends Serializable {

  /**
    * 最终提交时
    */
  def commit()

  /**
    * 最终意外终止
    */
  def abort()

  def writeOne(record: InternalRow)

  /**
    * 写一条数据writeOne之后调用, 调用顺序是 commit/abort -> close
    */
  def writeOneCommit()

  def writeOneAbort()

  def writeOneClose()
}

object IRemoteWriter extends Serializable {}
