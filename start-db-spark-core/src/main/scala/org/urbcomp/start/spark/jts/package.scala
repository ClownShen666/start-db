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
package org.urbcomp.start.spark
import org.apache.spark.sql.{SQLContext, SparkSession}

package object jts extends StartEncoders {

  /**
    * Initialization function that must be called before any JTS functionality
    * is accessed. This function can be called directly, or one of the `initJTS`
    * enrichment methods on [[SQLContext]] or [[SparkSession]] can be used instead.
    */
  def initStart(): Unit = {
    org.apache.spark.sql.start.registerTypes()
  }

  /** Enrichment over [[SQLContext]] to add `withStart` "literate" method. */
  implicit class SQLContextWithStart(val sqlContext: SQLContext) extends AnyVal {
    def withStart: SQLContext = {
      initStart()
      sqlContext
    }
  }

  /** Enrichment over [[SparkSession]] to add `withStart` "literate" method. */
  implicit class SparkSessionWithStart(val spark: SparkSession) extends AnyVal {
    def withStart: SparkSession = {
      initStart()
      spark
    }
  }

}
