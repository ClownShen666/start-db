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

import com.google.protobuf.ByteString
import io.grpc.inprocess.InProcessChannelBuilder
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.types.{DataType, StructType}
import org.urbcomp.cupid.db.config.DynamicConfig.{getRemoteServerHostname, getRemoteServerPort}
import org.urbcomp.cupid.db.spark.cache.SparkDataSerializer
import org.urbcomp.cupid.db.spark.data.GrpcRemote.{RowRequest, SchemaRequest}
import org.urbcomp.cupid.db.spark.data.RemoteClient
import org.urbcomp.cupid.db.util.SparkSqlParam

import java.util
import java.util.concurrent.TimeUnit

/**
  * 使用grpc的原因是支持流式传输
  *
  * @author jimo
  * */
class GrpcRemoteWriter extends IRemoteWriter {

  private val options: util.Map[String, String] = IRemoteWriter.options
  private val remoteClient: RemoteClient =
    if (options.get("InProcessChannelForTest") == null)
      new RemoteClient(
        options.getOrDefault(SparkSqlParam.REMOTE_HOST_KEY, getRemoteServerHostname),
        options.getOrDefault(SparkSqlParam.REMOTE_PORT_KEY, getRemoteServerPort.toString).toInt
      )
    else
      new RemoteClient(
        InProcessChannelBuilder.forName(options.get("InProcessChannelForTest")).build()
      )
  private val sqlId = options.get(SparkSqlParam.SQL_ID_KEY)

  private val schemaJson: String = options.get(RemoteWriteSource.SCHEMA_KEY)
  private val schema: StructType = DataType.fromJson(schemaJson).asInstanceOf[StructType]

  // 先发送schema
  remoteClient.sendSchema(
    SchemaRequest
      .newBuilder()
      .setSqlId(sqlId)
      .setSchemaJson(schemaJson)
      .build()
  )

  override def commit(): Unit = {
    remoteClient.commit()
    // 留时间flush数据
    TimeUnit.SECONDS.sleep(1)
  }

  override def abort(): Unit = remoteClient.error()

  override def writeOne(record: InternalRow): Unit = {
    val bytes = SparkDataSerializer.serialize(record, schema)
    remoteClient.sendRow(
      RowRequest.newBuilder().setSqlId(sqlId).setData(ByteString.copyFrom(bytes)).build()
    )
  }

  override def writeOneCommit(): Unit = {}

  override def writeOneAbort(): Unit = remoteClient.error()

  override def writeOneClose(): Unit = {}
}
