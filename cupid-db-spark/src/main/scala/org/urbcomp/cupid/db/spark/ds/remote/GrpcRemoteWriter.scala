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
package org.urbcomp.cupid.db.spark.ds.remote

import org.apache.spark.sql.catalyst.InternalRow
import org.urbcomp.cupid.db.spark.data.GrpcRemote.RowRequest
import org.urbcomp.cupid.db.spark.data.RemoteClient

/**
 * 使用grpc的原因是支持流式传输
 *
 * @author jimo
 * */
class GrpcRemoteWriter(options: java.util.Map[String, String]) extends IRemoteWriter {

  private val remoteClient = new RemoteClient()

  override def commit(): Unit = remoteClient.commit()

  override def abort(): Unit = remoteClient.error()

  override def writeOne(record: InternalRow): Unit = {
    remoteClient.sendRow(RowRequest.newBuilder().setSqlId("sqlid").build())
  }

  override def writeOneCommit(): Unit = {}

  override def writeOneAbort(): Unit = remoteClient.error()

  override def writeOneClose(): Unit = {}
//  /**
//   * 最终提交时
//   */
//  override def commit(): Unit = {}
//
//  /**
//   * 最终意外终止
//   */
//  override def abort(): Unit = {}
//
//  override def writeOne(record: InternalRow): Unit = {}
//
//  /**
//   * 写一条数据writeOne之后调用, 调用顺序是 commit/abort -> close
//   */
//  override def writeOneCommit(): Unit = {}
//
//  override def writeOneAbort(): Unit = {}
//
//  override def writeOneClose(): Unit = {}
}
