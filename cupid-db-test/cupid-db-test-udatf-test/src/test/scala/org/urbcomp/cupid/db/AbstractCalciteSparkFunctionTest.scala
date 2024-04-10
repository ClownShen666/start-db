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

import org.apache.spark.sql.{DataFrame, Row}
import org.junit.Assert.assertTrue
import org.locationtech.jts.geom.Geometry
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.slf4j.Logger
import org.urbcomp.cupid.db.flink.FlinkQueryExecutor
import org.urbcomp.cupid.db.metadata.CalciteHelper
import org.urbcomp.cupid.db.model.roadnetwork.RoadSegment
import org.urbcomp.cupid.db.model.trajectory.Trajectory
import org.urbcomp.cupid.db.util.{LogUtil, SqlParam}

import java.sql.{Connection, ResultSet, Statement}
import java.util.TimeZone
import scala.collection.JavaConverters.asScalaIteratorConverter
import scala.collection.mutable
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
  val flinkQueryExecutor: FlinkQueryExecutor = new FlinkQueryExecutor()

  override protected def beforeAll(): Unit = {
    SqlParam.CACHE.set(new SqlParam("root", "default"))
    connect = CalciteHelper.createConnection()
    statement = connect.createStatement()
    flinkQueryExecutor.registerUdf();
  }

  override protected def afterAll(): Unit = {
    statement.close()
    connect.close()
    SparkExecuteWrapper.getSparkExecute.stop()
  }

  protected def checkCalciteSpark(sql: String, expectsList: List[Any]*): Unit = {
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

  protected def checkCalciteSparkFlink(sql: String, expectsList: List[Any]*): Unit = {
    val rs = connect.createStatement().executeQuery(sql)
    val rsBuf = rsBuffer(rs)
    val df = SparkExecuteWrapper.getSparkExecute.executeSql(sql)
    val flinkResult = flinkQueryExecutor.getTableEnv
      .executeSql(flinkQueryExecutor.processSql(sql))
      .collect()
      .asScala
      .toList
    var i: Int = 0
    for (expects <- expectsList) {
      var j: Int = 0
      for (expect <- expects) {
        assertTrue(isEqual(expect, rsBuf(i)(j)))
        assertTrue(isEqual(expect, flinkResult(i).getField(j)))
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
    val dfList: java.util.List[Row] = df.collectAsList()
    if (dfList.size() != rs.size) return false
    // Since both are unordered, I use naive comparisons ...
    val dfData = ArrayBuffer[ArrayBuffer[Any]]()
    for (i <- rs.indices) {
      if (rs(i).length != dfList.get(i).length) return false
      val item = ArrayBuffer[Any]()
      for (j <- rs(i).indices) {
        item += dfList.get(i).get(j)
      }
      dfData += item
    }
    val unmatchedRows: mutable.Set[Int] = dfData.indices.to[mutable.Set]
    for (row <- rs) {
      var found = false
      for (idx <- dfData.indices) {
        if (unmatchedRows.contains(idx)) {
          var matched: Boolean = true
          for (col <- row.indices) {
            if (matched && !isEqual(row(col), dfData(idx)(col))) {
              matched = false
            }
          }
          if (matched) {
            // find a match
            found = true
            unmatchedRows -= idx
          }
        }
      }
      if (!found) return false
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
      case _: Trajectory =>
        return actualVal.toString.equals(expectVal.toString)
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
        val tolerance = 1e-9
        actualVal match {
          case fl: Float =>
            if ((expectVal.asInstanceOf[Double] - fl).abs >= tolerance)
              return false
          case _ =>
            if ((expectVal.asInstanceOf[Double] - actualVal.asInstanceOf[Double]).abs >= tolerance)
              return false
        }
      case _ =>
        if (actualVal.isInstanceOf[Geometry] || expectVal.isInstanceOf[Geometry]) {
          if (expectVal.toString != actualVal.toString) {
            return false
          } else {
            return true
          }
        } else if (actualVal.isInstanceOf[java.sql.Timestamp]) {
          return actualVal.toString.equals(expectVal.toString)
        } else
          actualVal match {
            case trajectory: Trajectory =>
              return trajectory.toString.equals(expectVal.toString)
            case segment: RoadSegment =>
              return segment.toString.equals(expectVal.toString)
            case _ =>
              if (expectVal != actualVal) {
                return false
              } else {
                return true
              }
          }
    }
    true
  }
}
