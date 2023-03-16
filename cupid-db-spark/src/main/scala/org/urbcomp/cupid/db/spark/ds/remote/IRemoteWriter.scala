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