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
package org.urbcomp.cupid.db.udf

import org.apache.calcite.DataContext
import org.apache.calcite.config.CalciteConnectionConfig
import org.apache.calcite.linq4j.{AbstractEnumerable, Enumerable, Enumerator}
import org.apache.calcite.rel.`type`.{RelDataType, RelDataTypeFactory}
import org.apache.calcite.schema.{ScannableTable, Schema, Statistic, Statistics}
import org.apache.calcite.sql.`type`.SqlTypeName
import org.apache.calcite.sql.{SqlCall, SqlNode}
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF
import org.urbcomp.cupid.db.udf.DataEngine.{Calcite, Spark}

trait AbstractUdtf extends GenericUDTF {

  def name(): String

  def registerEngines(): List[DataEngine.Value] = List(Calcite, Spark)

  def inputColumnsCount: Int
  def outputColumns(): List[(String, SqlTypeName)]

  def udtfImpl(objects: Seq[AnyRef]): Array[Array[AnyRef]]

  private def udtfCalciteEntryTemplate(inputs: Array[AnyRef]): ScannableTable = {
    new ScannableTable {
      var result: Array[Array[AnyRef]] = null

      override def getRowType(relDataTypeFactory: RelDataTypeFactory): RelDataType = {
        var builder = relDataTypeFactory.builder
        for (col <- outputColumns()) {
          builder = builder.add(col._1, col._2)
        }
        builder.build
      }

      override def scan(dataContext: DataContext): Enumerable[Array[AnyRef]] = {
        new AbstractEnumerable[Array[AnyRef]]() {
          var counter: Int = 0
          var curval: Array[AnyRef] = null

          override def enumerator(): Enumerator[Array[AnyRef]] = new Enumerator[Array[AnyRef]]() {

            /**
             * current result
             *
             * @return current
             */
            override def current(): Array[AnyRef] = {
              curval
            }

            override def moveNext: Boolean = {
              if (result == null) {
                result = udtfImpl(inputs)
              }
              if (counter < result.length) {
                curval = result(counter)
                counter += 1
                true
              } else false
            }

            override def reset(): Unit = {
              counter = 0
            }

            override def close(): Unit = {}
          }
        }
      }

      override def getStatistic: Statistic = {
        Statistics.UNKNOWN
      }

      override def getJdbcTableType: Schema.TableType = {
        Schema.TableType.TABLE
      }

      override def isRolledUp(s: String): Boolean = {
        false
      }

      override def rolledUpColumnValidInsideAgg(
                                                 s: String,
                                                 sqlCall: SqlCall,
                                                 sqlNode: SqlNode,
                                                 calciteConnectionConfig: CalciteConnectionConfig
                                               ): Boolean = {
        true
      }
    }
  }

  def udtfCalciteEntry1(input1: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1))

  def udtfCalciteEntry2(input1: AnyRef, input2: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2))

  def udtfCalciteEntry3(input1: AnyRef, input2: AnyRef, input3: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3))

  def udtfCalciteEntry4(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4))

  def udtfCalciteEntry5(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5))

  def udtfCalciteEntry6(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6))

  def udtfCalciteEntry7(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7))

  def udtfCalciteEntry8(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8))

  def udtfCalciteEntry9(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9))

  def udtfCalciteEntry10(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10))

  def udtfCalciteEntry11(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef, input11: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11))

  def udtfCalciteEntry12(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef, input11: AnyRef, input12: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12))

  def udtfCalciteEntry13(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef, input11: AnyRef, input12: AnyRef, input13: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13))

  def udtfCalciteEntry14(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef, input11: AnyRef, input12: AnyRef, input13: AnyRef, input14: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14))

  def udtfCalciteEntry15(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef, input11: AnyRef, input12: AnyRef, input13: AnyRef, input14: AnyRef, input15: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14, input15))

  def udtfCalciteEntry16(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef, input11: AnyRef, input12: AnyRef, input13: AnyRef, input14: AnyRef, input15: AnyRef, input16: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14, input15, input16))

  def udtfCalciteEntry17(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef, input11: AnyRef, input12: AnyRef, input13: AnyRef, input14: AnyRef, input15: AnyRef, input16: AnyRef, input17: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14, input15, input16, input17))

  def udtfCalciteEntry18(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef, input11: AnyRef, input12: AnyRef, input13: AnyRef, input14: AnyRef, input15: AnyRef, input16: AnyRef, input17: AnyRef, input18: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14, input15, input16, input17, input18))

  def udtfCalciteEntry19(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef, input11: AnyRef, input12: AnyRef, input13: AnyRef, input14: AnyRef, input15: AnyRef, input16: AnyRef, input17: AnyRef, input18: AnyRef, input19: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14, input15, input16, input17, input18, input19))

  def udtfCalciteEntry20(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef, input11: AnyRef, input12: AnyRef, input13: AnyRef, input14: AnyRef, input15: AnyRef, input16: AnyRef, input17: AnyRef, input18: AnyRef, input19: AnyRef, input20: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14, input15, input16, input17, input18, input19, input20))

  def udtfCalciteEntry21(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef, input11: AnyRef, input12: AnyRef, input13: AnyRef, input14: AnyRef, input15: AnyRef, input16: AnyRef, input17: AnyRef, input18: AnyRef, input19: AnyRef, input20: AnyRef, input21: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14, input15, input16, input17, input18, input19, input20, input21))

  def udtfCalciteEntry22(input1: AnyRef, input2: AnyRef, input3: AnyRef, input4: AnyRef, input5: AnyRef, input6: AnyRef, input7: AnyRef, input8: AnyRef, input9: AnyRef, input10: AnyRef, input11: AnyRef, input12: AnyRef, input13: AnyRef, input14: AnyRef, input15: AnyRef, input16: AnyRef, input17: AnyRef, input18: AnyRef, input19: AnyRef, input20: AnyRef, input21: AnyRef, input22: AnyRef): ScannableTable = udtfCalciteEntryTemplate(Array(input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14, input15, input16, input17, input18, input19, input20, input21, input22))

  override protected def process(objects: Array[AnyRef]): Unit = {
    val ret = udtfImpl(objects)
    for (elem <- ret) {
      super.forward(elem)
    }
  }

  override def close(): Unit = {}
}
