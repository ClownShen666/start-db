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
import org.apache.spark.sql.connector.catalog.{SupportsWrite, Table, TableCapability, TableProvider}
import org.apache.spark.sql.connector.expressions.Transform
import org.apache.spark.sql.connector.write._
import org.apache.spark.sql.sources.Filter
import org.apache.spark.sql.types.{StringType, StructType}
import org.apache.spark.sql.util.CaseInsensitiveStringMap

import java.util
import scala.collection.JavaConverters._

class RemoteWriteSource extends TableProvider {

  override def inferSchema(caseInsensitiveStringMap: CaseInsensitiveStringMap): StructType =
    getTable(null, Array.empty[Transform], caseInsensitiveStringMap.asCaseSensitiveMap()).schema()

  override def getTable(
      structType: StructType,
      transforms: Array[Transform],
      map: util.Map[String, String]
  ): Table =
    new RemoteTable(map)
}

class RemoteTable(map: util.Map[String, String]) extends SupportsWrite {
  override def newWriteBuilder(logicalWriteInfo: LogicalWriteInfo): WriteBuilder = {
    new RemoteWriteBuilder(map)
  }

  override def name(): String = "RemoteWrite"

  override def schema(): StructType = {
    new StructType().add("col1", StringType).add("col2", StringType)
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

  private val remoteWriter: IRemoteWriter = IRemoteWriter.getInstance(options)

  override def createBatchWriterFactory(info: PhysicalWriteInfo): DataWriterFactory =
    new RemoteDataWriterFactory(options, remoteWriter)

  override def commit(messages: Array[WriterCommitMessage]): Unit = {
    remoteWriter.commit()
  }

  override def abort(messages: Array[WriterCommitMessage]): Unit = {
    remoteWriter.abort()
  }
}

class RemoteDataWriterFactory(options: util.Map[String, String], remoteWriter: IRemoteWriter)
    extends DataWriterFactory {
  override def createWriter(partitionId: Int, taskId: Long): DataWriter[InternalRow] = {
    new RemoteWriter(options, remoteWriter)
  }
}

class RemoteWriter(options: util.Map[String, String], remoteWriter: IRemoteWriter)
    extends DataWriter[InternalRow] {

  override def write(record: InternalRow): Unit = {
    val s = record.getString(0)
    println(options.get("op1") + ",row: " + s)
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

object WriteSucceed extends WriterCommitMessage {}
