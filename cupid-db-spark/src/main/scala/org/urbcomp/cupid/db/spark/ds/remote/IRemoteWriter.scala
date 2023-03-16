/*
 * Copyright 2022 ST-Lab
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 */

package org.urbcomp.cupid.db.spark.ds.remote

import org.apache.spark.sql.catalyst.InternalRow

import java.util

/**
  * 写接口抽象,实现可考虑不同协议和序列化
  *
  * @author jimo
  * */
trait IRemoteWriter {

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

object IRemoteWriter {

  def getInstance(options: util.Map[String, String]): IRemoteWriter = {
    new GrpcRemoteWriter(options)
  }
}
