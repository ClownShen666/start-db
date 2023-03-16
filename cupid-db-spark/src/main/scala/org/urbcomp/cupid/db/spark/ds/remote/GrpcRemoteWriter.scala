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

/**
  * 使用grpc的原因是支持流式传输
  *
  * @author jimo
  * */
class GrpcRemoteWriter(options: java.util.Map[String, String]) extends IRemoteWriter {

  override def commit(): Unit = {}

  override def abort(): Unit = {}

  override def writeOne(record: InternalRow): Unit = {}

  override def writeOneCommit(): Unit = {}

  override def writeOneAbort(): Unit = {}

  override def writeOneClose(): Unit = {}
}
