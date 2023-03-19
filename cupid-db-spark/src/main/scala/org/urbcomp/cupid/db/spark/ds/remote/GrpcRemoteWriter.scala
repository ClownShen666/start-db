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
import org.urbcomp.cupid.db.spark.data.GrpcRemote.{RowRequest, SchemaRequest}
import org.urbcomp.cupid.db.spark.data.RemoteClient

import java.util
import java.util.concurrent.TimeUnit

/**
  * 使用grpc的原因是支持流式传输
  *
  * @author jimo
  * */
class GrpcRemoteWriter extends IRemoteWriter {

  private val options: util.Map[String, String] = IRemoteWriter.options
  private val remoteClient: RemoteClient = new RemoteClient(8848)
  private val sqlId = options.get("sqlId")

  // 先发送schema
  remoteClient.sendSchema(
    SchemaRequest
      .newBuilder()
      .setSqlId(sqlId)
      .setSchemaJson(options.get(RemoteWriteSource.SCHEMA_KEY))
      .build()
  )

  override def commit(): Unit = {
    remoteClient.commit()
    // 留时间flush数据
    TimeUnit.SECONDS.sleep(1)
  }

  override def abort(): Unit = remoteClient.error()

  override def writeOne(record: InternalRow): Unit = {
    remoteClient.sendRow(RowRequest.newBuilder().setSqlId(sqlId).build())
  }

  override def writeOneCommit(): Unit = {}

  override def writeOneAbort(): Unit = remoteClient.error()

  override def writeOneClose(): Unit = {}
}
