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
import org.apache.spark.sql.connector.catalog.{SupportsWrite, Table, TableCapability, TableProvider}
import org.apache.spark.sql.connector.expressions.Transform
import org.apache.spark.sql.connector.write._
import org.apache.spark.sql.sources.Filter
import org.apache.spark.sql.types.{DataType, StructType}
import org.apache.spark.sql.util.CaseInsensitiveStringMap
import org.urbcomp.start.db.spark.SparkQueryExecutor.log

import java.util
import scala.collection.JavaConverters._

class RemoteWriteSource extends TableProvider {

  override def inferSchema(caseInsensitiveStringMap: CaseInsensitiveStringMap): StructType =
    getTable(null, Array.empty[Transform], caseInsensitiveStringMap.asCaseSensitiveMap()).schema()

  override def getTable(
      structType: StructType,
      transforms: Array[Transform],
      map: util.Map[String, String]
  ): Table = {
    log.info("RemoteWriteSource getTable show!")
    if (map == null) {
      log.info("map is null")
    } else {
      import scala.collection.JavaConversions._
      for (entry <- map.entrySet) {
        log.info(entry.getKey + ": " + entry.getValue)
      }
    }
    log.info("RemoteWriteSource finished!")
    new RemoteTable(map)
  }
}

object RemoteWriteSource {
  val SCHEMA_KEY = "schema"
}

class RemoteTable(map: util.Map[String, String]) extends SupportsWrite {
  override def newWriteBuilder(logicalWriteInfo: LogicalWriteInfo): WriteBuilder = {
    new RemoteWriteBuilder(map)
  }

  override def name(): String = "RemoteWrite"

  override def schema(): StructType = {
    DataType.fromJson(map.get(RemoteWriteSource.SCHEMA_KEY)).asInstanceOf[StructType]
  }

  override def capabilities(): util.Set[TableCapability] =
    Set(TableCapability.BATCH_WRITE, TableCapability.TRUNCATE, TableCapability.OVERWRITE_BY_FILTER).asJava
}

class RemoteWriteBuilder(options: util.Map[String, String])
    extends WriteBuilder
    with SupportsOverwrite {

  override def buildForBatch(): BatchWrite = new RemoteBatchWrite(options)

  override def overwrite(filters: Array[Filter]): WriteBuilder = this
}

class RemoteBatchWrite(options: util.Map[String, String]) extends BatchWrite {
  var factory: RemoteDataWriterFactory = null
  override def createBatchWriterFactory(info: PhysicalWriteInfo): DataWriterFactory = {
    factory = new RemoteDataWriterFactory(options)
    factory
  }

  override def commit(messages: Array[WriterCommitMessage]): Unit = {
    if (factory != null && factory.writer != null && factory.writer.remoteWriter != null) {
      factory.writer.remoteWriter.commit()
    }
  }

  override def abort(messages: Array[WriterCommitMessage]): Unit = {
    if (factory != null && factory.writer != null && factory.writer.remoteWriter != null) {
      factory.writer.remoteWriter.abort()
    }
  }
}

class RemoteDataWriterFactory(options: util.Map[String, String]) extends DataWriterFactory {
  var writer: RemoteWriter = null
  override def createWriter(partitionId: Int, taskId: Long): DataWriter[InternalRow] = {
    writer = new RemoteWriter(options)
    writer
  }
}

class RemoteWriter(options: util.Map[String, String]) extends DataWriter[InternalRow] {

  {
    log.info("RemoteWriter show options!")
    log.info("options:")
    if (options == null) {
      log.info("options is null")
    } else {
      import scala.collection.JavaConversions._
      for (entry <- options.entrySet) {
        log.info(entry.getKey + ": " + entry.getValue)
      }
    }
    log.info("RemoteWriter show options finished")
    //if (IRemoteWriter.options == null) IRemoteWriter.options = new util.HashMap[String, String]()
    //IRemoteWriter.options.putAll(options)
  }

  val remoteWriter: IRemoteWriter = new GrpcRemoteWriter(options)

  override def write(record: InternalRow): Unit = {
    remoteWriter.writeOne(record)
  }

  override def commit(): WriterCommitMessage = {
    remoteWriter.writeOneCommit()
    WriteSucceed
  }

  override def abort(): Unit = {
    remoteWriter.writeOneAbort()
  }

  override def close(): Unit = {
    remoteWriter.writeOneClose()
  }
}

object RemoteWriter {

  /**
  * 这里用静态方式的原因是：RemoteWriter 里的grpc连接无法序列化
  */

}

object WriteSucceed extends WriterCommitMessage {}
