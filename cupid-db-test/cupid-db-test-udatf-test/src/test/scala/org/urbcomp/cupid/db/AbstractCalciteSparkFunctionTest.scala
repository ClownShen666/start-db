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
package org.urbcomp.cupid.db

import org.apache.spark.sql.DataFrame
import org.junit.Assert.assertTrue
import org.locationtech.jts.geom.Geometry
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.slf4j.Logger
import org.urbcomp.cupid.db.metadata.CalciteHelper
import org.urbcomp.cupid.db.util.{LogUtil, SqlParam}

import java.sql.{Connection, ResultSet, Statement, Timestamp}
import java.util.TimeZone
import scala.collection.mutable.ArrayBuffer

/**
  * Test for Calcite and Geomesa
  *
  * @author zaiyuan
  * @since 0.1.0
  */
abstract class AbstractCalciteSparkFunctionTest extends FunSuite with BeforeAndAfterAll {
  var connect: Connection = _
  TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
  val log: Logger = LogUtil.getLogger
  var statement: Statement = _

  override protected def beforeAll(): Unit = {
    SqlParam.CACHE.set(new SqlParam("root", "default"))
    connect = CalciteHelper.createConnection()
    statement = connect.createStatement()
  }

  override protected def afterAll(): Unit = {
    statement.close()
    connect.close()
    SparkExecuteWrapper.getSparkExecute.stop()
  }

  protected def executeQueryCheck(sql: String, expectsList: List[Any]*): Unit = {
    val rs = connect.createStatement().executeQuery(sql)
    val rsBuf = rsBuffer(rs)
    val df = SparkExecuteWrapper.getSparkExecute.executeSql(sql)
    assertTrue(isEqualCalciteAndSpark(rsBuf, df))
    var i: Int = 0
    for (expects <- expectsList) {
      var j: Int = 0
      for (expect <- expects) {
        assertTrue(isEqual(expect, rsBuf(i)(j)))
        j = j + 1
      }
      i = i + 1
    }
  }

  /**
    * attention
    * geometry result from calcite is String while from spark is geometry
    */
  private def isEqualCalciteAndSpark(rs: ArrayBuffer[ArrayBuffer[Any]], df: DataFrame): Boolean = {
    val dfList = df.collectAsList()
    if (dfList.size() != rs.size) return false

    val rowIter1 = rs.iterator
    val rowIter2 = dfList.iterator()
    while (rowIter1.hasNext && rowIter2.hasNext) {
      val row1 = rowIter1.next()
      val row2 = rowIter2.next()
      if (row1.length != row2.length) return false

      for (i <- row1.indices) {
        if (row1(i).isInstanceOf[Timestamp]) {
          println(row1(i).asInstanceOf[Timestamp].toString)
          println(row2.get(i).asInstanceOf[Timestamp].toString)
          if (Math.abs(
                row1(i)
                  .asInstanceOf[Timestamp]
                  .getTime - row2.get(i).asInstanceOf[Timestamp].getTime
              ) > 1 * 60 * 1000)
            return false
        } else if (!isEqual(row1(i), row2.get(i))) {
          return false
        }
      }
    }
    true
  }

  private def rsBuffer(rs: ResultSet): ArrayBuffer[ArrayBuffer[Any]] = {
    val result = ArrayBuffer[ArrayBuffer[Any]]()
    while (rs.next()) {
      val item = ArrayBuffer[Any]()
      for (i <- 1 to rs.getMetaData.getColumnCount) {
        item += rs.getObject(i)
      }
      result += item
    }
    result
  }

  private def isEqual(expectVal: Any, actualVal: Any): Boolean = {
    expectVal match {
      case _: java.math.BigDecimal =>
        actualVal match {
          case d: Double =>
            if (java.math.BigDecimal.valueOf(d) != expectVal) return false
          case _ =>
            if (expectVal
                  .asInstanceOf[java.math.BigDecimal]
                  .compareTo(expectVal.asInstanceOf[java.math.BigDecimal]) != 0) {
              return false
            }
        }
      case _: java.lang.Double =>
        val tolerance = 0.00000000000000001
        actualVal match {
          case fl: Float =>
            if ((expectVal.asInstanceOf[Double] - fl).abs >= tolerance)
              return false
          case _ =>
            if ((expectVal.asInstanceOf[Double] - actualVal.asInstanceOf[Double]).abs >= tolerance)
              return false
        }
      case _: Timestamp =>
        if ((expectVal
              .asInstanceOf[Timestamp]
              .getTime - actualVal.asInstanceOf[Timestamp].getTime).abs > 1 * 60 * 1000)
          return false
        else
          return true
      case _ =>
        if (actualVal.isInstanceOf[Geometry] || expectVal.isInstanceOf[Geometry]) {
          if (expectVal.toString != actualVal.toString) {
            return false
          } else {
            return true
          }
        } else if (expectVal != actualVal) {
          return false
        } else {
          return true
        }
    }
    true
  }
}
