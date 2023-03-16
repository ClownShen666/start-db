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
