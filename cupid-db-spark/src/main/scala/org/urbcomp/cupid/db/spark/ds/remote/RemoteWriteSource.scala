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

  override def getTable(structType: StructType, transforms: Array[Transform], map: util.Map[String, String]): Table =
    new RemoteTable(map)
}

class RemoteTable(map: util.Map[String, String]) extends SupportsWrite {
  override def newWriteBuilder(logicalWriteInfo: LogicalWriteInfo): WriteBuilder = {
    val options = logicalWriteInfo.options()
    println("options:" + options)
    new RemoteWriteBuilder(map)
  }

  override def name(): String = "RemoteWrite"

  override def schema(): StructType = {
    println("schema")
    new StructType().add("col1", StringType).add("col2", StringType)
  }

  override def capabilities(): util.Set[TableCapability] = Set(TableCapability.BATCH_WRITE,
    TableCapability.TRUNCATE, TableCapability.OVERWRITE_BY_FILTER).asJava
}

class RemoteWriteBuilder(options: util.Map[String, String]) extends WriteBuilder with SupportsOverwrite {
  override def buildForBatch(): BatchWrite = new RemoteBatchWrite(options)

  override def overwrite(filters: Array[Filter]): WriteBuilder = this
}

class RemoteBatchWrite(options: util.Map[String, String]) extends BatchWrite {
  override def createBatchWriterFactory(info: PhysicalWriteInfo): DataWriterFactory = new RemoteDataWriterFactory(options)

  override def commit(messages: Array[WriterCommitMessage]): Unit = {}

  override def abort(messages: Array[WriterCommitMessage]): Unit = {}
}

class RemoteDataWriterFactory(options: util.Map[String, String]) extends DataWriterFactory {
  override def createWriter(partitionId: Int, taskId: Long): DataWriter[InternalRow] = new RemoteWriter(options)
}

class RemoteWriter(options: util.Map[String, String]) extends DataWriter[InternalRow] {

  override def write(record: InternalRow): Unit = {
    val s = record.getString(0)
    println(options.get("op1") + ",row: " + s)
  }

  override def commit(): WriterCommitMessage = {
    println("commit")
    WriteSucceed
  }

  override def abort(): Unit = {
    println("abort")
  }

  override def close(): Unit = {
    println("close")
  }
}

object WriteSucceed extends WriterCommitMessage {
}