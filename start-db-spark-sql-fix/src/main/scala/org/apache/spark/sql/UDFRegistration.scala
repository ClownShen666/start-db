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
package org.apache.spark.sql

import java.lang.reflect.ParameterizedType
import scala.reflect.runtime.universe.TypeTag
import scala.util.Try
import org.apache.spark.annotation.Stable
import org.apache.spark.api.python.PythonEvalType
import org.apache.spark.internal.Logging
import org.apache.spark.sql.api.java._
import org.apache.spark.sql.catalyst.{JavaTypeInference, ScalaReflection}
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.catalyst.expressions.{Expression, ScalaUDF}
import org.apache.spark.sql.execution.aggregate.ScalaUDAF
import org.apache.spark.sql.execution.python.UserDefinedPythonFunction
import org.apache.spark.sql.expressions.{
  SparkUserDefinedFunction,
  UserDefinedAggregateFunction,
  UserDefinedAggregator,
  UserDefinedFunction
}
import org.apache.spark.sql.types.{AbstractDataType, DataType}
import org.apache.spark.util.Utils

/**
  * Functions for registering user-defined functions. Use `SparkSession.udf` to access this:
  *
  * {{{
  *   spark.udf
  * }}}
  *
  * @since 1.3.0
  */
@Stable
class UDFRegistration private[sql] (functionRegistry: FunctionRegistry) extends Logging {

  protected[sql] def registerPython(name: String, udf: UserDefinedPythonFunction): Unit = {
    log.debug(s"""
        | Registering new PythonUDF:
        | name: $name
        | command: ${udf.func.command.toSeq}
        | envVars: ${udf.func.envVars}
        | pythonIncludes: ${udf.func.pythonIncludes}
        | pythonExec: ${udf.func.pythonExec}
        | dataType: ${udf.dataType}
        | pythonEvalType: ${PythonEvalType.toString(udf.pythonEvalType)}
        | udfDeterministic: ${udf.udfDeterministic}
      """.stripMargin)

    functionRegistry.createOrReplaceTempFunction(name, udf.builder)
  }

  /**
    * Registers a user-defined aggregate function (UDAF).
    *
    * @param name the name of the UDAF.
    * @param udaf the UDAF needs to be registered.
    * @return the registered UDAF.
    *
    * @since 1.5.0
    * @deprecated this method and the use of UserDefinedAggregateFunction are deprecated.
    * Aggregator[IN, BUF, OUT] should now be registered as a UDF via the functions.udaf(agg) method.
    */
  @deprecated(
    "Aggregator[IN, BUF, OUT] should now be registered as a UDF" +
      " via the functions.udaf(agg) method.",
    "3.0.0"
  )
  def register(name: String, udaf: UserDefinedAggregateFunction): UserDefinedAggregateFunction = {
    def builder(children: Seq[Expression]) = ScalaUDAF(children, udaf)
    functionRegistry.createOrReplaceTempFunction(name, builder)
    udaf
  }

  /**
    * Registers a user-defined function (UDF), for a UDF that's already defined using the Dataset
    * API (i.e. of type UserDefinedFunction). To change a UDF to nondeterministic, call the API
    * `UserDefinedFunction.asNondeterministic()`. To change a UDF to nonNullable, call the API
    * `UserDefinedFunction.asNonNullable()`.
    *
    * Example:
    * {{{
    *   val foo = udf(() => Math.random())
    *   spark.udf.register("random", foo.asNondeterministic())
    *
    *   val bar = udf(() => "bar")
    *   spark.udf.register("stringLit", bar.asNonNullable())
    * }}}
    *
    * @param name the name of the UDF.
    * @param udf the UDF needs to be registered.
    * @return the registered UDF.
    *
    * @since 2.2.0
    */
  def register(name: String, udf: UserDefinedFunction): UserDefinedFunction = {
    udf match {
      case udaf: UserDefinedAggregator[_, _, _] =>
        def builder(children: Seq[Expression]) = udaf.scalaAggregator(children)
        functionRegistry.createOrReplaceTempFunction(name, builder)
        udf
      case _ =>
        def builder(children: Seq[Expression]) = udf.apply(children.map(Column.apply): _*).expr
        functionRegistry.createOrReplaceTempFunction(name, builder)
        udf
    }
  }

  // scalastyle:off line.size.limit

  def calculateScore(parameterTypes: Seq[AbstractDataType], inputTypes: Seq[DataType]): Int = {
    var score: Int = 0
    for (i <- parameterTypes.indices) {
      if (!parameterTypes(i).acceptsType(inputTypes(i))) {
        score += 1
      }
    }
    System.out.println(parameterTypes, inputTypes.toList, score)
    score
  }

  /* register 0-22 were generated by this script

    (0 to 22).foreach { x =>
      val types = (1 to x).foldRight("RT")((i, s) => {s"A$i, $s"})
      val typeTags = (1 to x).map(i => s"A$i: TypeTag").foldLeft("RT: TypeTag")(_ + ", " + _)
      val inputEncoders = (1 to x).foldRight("Nil")((i, s) => {s"Try(ExpressionEncoder[A$i]()).toOption :: $s"})
      println(s"""
        |/**
        | * Registers a deterministic Scala closure of $x arguments as user-defined function (UDF).
        | * @tparam RT return type of UDF.
        | * @since 1.3.0
        | */
        |def register[$typeTags](name: String, func: Function$x[$types]): UserDefinedFunction = {
        |  val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
        |  val inputEncoders: Seq[Option[ExpressionEncoder[_]]] = $inputEncoders
        |  val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
        |  val finalUdf = if (nullable) udf else udf.asNonNullable()
        |  def builder(e: Seq[Expression]) = if (e.length == $x) {
        |    finalUdf.createScalaUDF(e)
        |  } else {
        |    throw new AnalysisException("Invalid number of arguments for function " + name +
        |      ". Expected: $x; Found: " + e.length)
        |  }
        |  functionRegistry.createOrReplaceTempFunction(name, builder)
        |  finalUdf
        |}""".stripMargin)
    }

    (0 to 22).foreach { i =>
      val extTypeArgs = (0 to i).map(_ => "_").mkString(", ")
      val anyTypeArgs = (0 to i).map(_ => "Any").mkString(", ")
      val anyCast = s".asInstanceOf[UDF$i[$anyTypeArgs]]"
      val anyParams = (1 to i).map(_ => "_: Any").mkString(", ")
      val version = if (i == 0) "2.3.0" else "1.3.0"
      val funcCall = if (i == 0) s"() => f$anyCast.call($anyParams)" else s"f$anyCast.call($anyParams)"
      println(s"""
        |/**
        | * Register a deterministic Java UDF$i instance as user-defined function (UDF).
        | * @since $version
        | */
        |def register(name: String, f: UDF$i[$extTypeArgs], returnType: DataType): Unit = {
        |  val func = $funcCall
        |  def builder(e: Seq[Expression]) = if (e.length == $i) {
        |    ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
        |  } else {
        |    throw new AnalysisException("Invalid number of arguments for function " + name +
        |      ". Expected: $i; Found: " + e.length)
        |  }
        |  functionRegistry.createOrReplaceTempFunction(name, builder)
        |}""".stripMargin)
    }
   */

  /**
    * Registers a deterministic Scala closure of 0 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[RT: TypeTag](name: String, func: Function0[RT]): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders: Seq[Option[ExpressionEncoder[_]]] = Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 0) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 0; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 1 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[RT: TypeTag, A1: TypeTag](
      name: String,
      func: Function1[A1, RT]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 1) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 1; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 2 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[RT: TypeTag, A1: TypeTag, A2: TypeTag](
      name: String,
      func: Function2[A1, A2, RT]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 2) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 2; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 3 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[RT: TypeTag, A1: TypeTag, A2: TypeTag, A3: TypeTag](
      name: String,
      func: Function3[A1, A2, A3, RT]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 3) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 3; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 4 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[RT: TypeTag, A1: TypeTag, A2: TypeTag, A3: TypeTag, A4: TypeTag](
      name: String,
      func: Function4[A1, A2, A3, A4, RT]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 4) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 4; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 5 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[RT: TypeTag, A1: TypeTag, A2: TypeTag, A3: TypeTag, A4: TypeTag, A5: TypeTag](
      name: String,
      func: Function5[A1, A2, A3, A4, A5, RT]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 5) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 5; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 6 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag
  ](name: String, func: Function6[A1, A2, A3, A4, A5, A6, RT]): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 6) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 6; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 7 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag
  ](name: String, func: Function7[A1, A2, A3, A4, A5, A6, A7, RT]): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 7) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 7; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 8 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag
  ](name: String, func: Function8[A1, A2, A3, A4, A5, A6, A7, A8, RT]): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 8) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 8; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 9 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag
  ](name: String, func: Function9[A1, A2, A3, A4, A5, A6, A7, A8, A9, RT]): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 9) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 9; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 10 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag
  ](
      name: String,
      func: Function10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, RT]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 10) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 10; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 11 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag,
      A11: TypeTag
  ](
      name: String,
      func: Function11[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, RT]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Try(
      ExpressionEncoder[A11]()
    ).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 11) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 11; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 12 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag,
      A11: TypeTag,
      A12: TypeTag
  ](
      name: String,
      func: Function12[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, RT]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Try(
      ExpressionEncoder[A11]()
    ).toOption :: Try(ExpressionEncoder[A12]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 12) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 12; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 13 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag,
      A11: TypeTag,
      A12: TypeTag,
      A13: TypeTag
  ](
      name: String,
      func: Function13[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, RT]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Try(
      ExpressionEncoder[A11]()
    ).toOption :: Try(ExpressionEncoder[A12]()).toOption :: Try(ExpressionEncoder[A13]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 13) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 13; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 14 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag,
      A11: TypeTag,
      A12: TypeTag,
      A13: TypeTag,
      A14: TypeTag
  ](
      name: String,
      func: Function14[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, RT]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Try(
      ExpressionEncoder[A11]()
    ).toOption :: Try(ExpressionEncoder[A12]()).toOption :: Try(ExpressionEncoder[A13]()).toOption :: Try(
      ExpressionEncoder[A14]()
    ).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 14) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 14; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 15 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag,
      A11: TypeTag,
      A12: TypeTag,
      A13: TypeTag,
      A14: TypeTag,
      A15: TypeTag
  ](
      name: String,
      func: Function15[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, RT]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Try(
      ExpressionEncoder[A11]()
    ).toOption :: Try(ExpressionEncoder[A12]()).toOption :: Try(ExpressionEncoder[A13]()).toOption :: Try(
      ExpressionEncoder[A14]()
    ).toOption :: Try(ExpressionEncoder[A15]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 15) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 15; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 16 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag,
      A11: TypeTag,
      A12: TypeTag,
      A13: TypeTag,
      A14: TypeTag,
      A15: TypeTag,
      A16: TypeTag
  ](
      name: String,
      func: Function16[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, RT]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Try(
      ExpressionEncoder[A11]()
    ).toOption :: Try(ExpressionEncoder[A12]()).toOption :: Try(ExpressionEncoder[A13]()).toOption :: Try(
      ExpressionEncoder[A14]()
    ).toOption :: Try(ExpressionEncoder[A15]()).toOption :: Try(ExpressionEncoder[A16]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 16) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 16; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 17 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag,
      A11: TypeTag,
      A12: TypeTag,
      A13: TypeTag,
      A14: TypeTag,
      A15: TypeTag,
      A16: TypeTag,
      A17: TypeTag
  ](
      name: String,
      func: Function17[
        A1,
        A2,
        A3,
        A4,
        A5,
        A6,
        A7,
        A8,
        A9,
        A10,
        A11,
        A12,
        A13,
        A14,
        A15,
        A16,
        A17,
        RT
      ]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Try(
      ExpressionEncoder[A11]()
    ).toOption :: Try(ExpressionEncoder[A12]()).toOption :: Try(ExpressionEncoder[A13]()).toOption :: Try(
      ExpressionEncoder[A14]()
    ).toOption :: Try(ExpressionEncoder[A15]()).toOption :: Try(ExpressionEncoder[A16]()).toOption :: Try(
      ExpressionEncoder[A17]()
    ).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 17) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 17; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 18 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag,
      A11: TypeTag,
      A12: TypeTag,
      A13: TypeTag,
      A14: TypeTag,
      A15: TypeTag,
      A16: TypeTag,
      A17: TypeTag,
      A18: TypeTag
  ](
      name: String,
      func: Function18[
        A1,
        A2,
        A3,
        A4,
        A5,
        A6,
        A7,
        A8,
        A9,
        A10,
        A11,
        A12,
        A13,
        A14,
        A15,
        A16,
        A17,
        A18,
        RT
      ]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Try(
      ExpressionEncoder[A11]()
    ).toOption :: Try(ExpressionEncoder[A12]()).toOption :: Try(ExpressionEncoder[A13]()).toOption :: Try(
      ExpressionEncoder[A14]()
    ).toOption :: Try(ExpressionEncoder[A15]()).toOption :: Try(ExpressionEncoder[A16]()).toOption :: Try(
      ExpressionEncoder[A17]()
    ).toOption :: Try(ExpressionEncoder[A18]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 18) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 18; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 19 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag,
      A11: TypeTag,
      A12: TypeTag,
      A13: TypeTag,
      A14: TypeTag,
      A15: TypeTag,
      A16: TypeTag,
      A17: TypeTag,
      A18: TypeTag,
      A19: TypeTag
  ](
      name: String,
      func: Function19[
        A1,
        A2,
        A3,
        A4,
        A5,
        A6,
        A7,
        A8,
        A9,
        A10,
        A11,
        A12,
        A13,
        A14,
        A15,
        A16,
        A17,
        A18,
        A19,
        RT
      ]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Try(
      ExpressionEncoder[A11]()
    ).toOption :: Try(ExpressionEncoder[A12]()).toOption :: Try(ExpressionEncoder[A13]()).toOption :: Try(
      ExpressionEncoder[A14]()
    ).toOption :: Try(ExpressionEncoder[A15]()).toOption :: Try(ExpressionEncoder[A16]()).toOption :: Try(
      ExpressionEncoder[A17]()
    ).toOption :: Try(ExpressionEncoder[A18]()).toOption :: Try(ExpressionEncoder[A19]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 19) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 19; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 20 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag,
      A11: TypeTag,
      A12: TypeTag,
      A13: TypeTag,
      A14: TypeTag,
      A15: TypeTag,
      A16: TypeTag,
      A17: TypeTag,
      A18: TypeTag,
      A19: TypeTag,
      A20: TypeTag
  ](
      name: String,
      func: Function20[
        A1,
        A2,
        A3,
        A4,
        A5,
        A6,
        A7,
        A8,
        A9,
        A10,
        A11,
        A12,
        A13,
        A14,
        A15,
        A16,
        A17,
        A18,
        A19,
        A20,
        RT
      ]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Try(
      ExpressionEncoder[A11]()
    ).toOption :: Try(ExpressionEncoder[A12]()).toOption :: Try(ExpressionEncoder[A13]()).toOption :: Try(
      ExpressionEncoder[A14]()
    ).toOption :: Try(ExpressionEncoder[A15]()).toOption :: Try(ExpressionEncoder[A16]()).toOption :: Try(
      ExpressionEncoder[A17]()
    ).toOption :: Try(ExpressionEncoder[A18]()).toOption :: Try(ExpressionEncoder[A19]()).toOption :: Try(
      ExpressionEncoder[A20]()
    ).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 20) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 20; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 21 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag,
      A11: TypeTag,
      A12: TypeTag,
      A13: TypeTag,
      A14: TypeTag,
      A15: TypeTag,
      A16: TypeTag,
      A17: TypeTag,
      A18: TypeTag,
      A19: TypeTag,
      A20: TypeTag,
      A21: TypeTag
  ](
      name: String,
      func: Function21[
        A1,
        A2,
        A3,
        A4,
        A5,
        A6,
        A7,
        A8,
        A9,
        A10,
        A11,
        A12,
        A13,
        A14,
        A15,
        A16,
        A17,
        A18,
        A19,
        A20,
        A21,
        RT
      ]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Try(
      ExpressionEncoder[A11]()
    ).toOption :: Try(ExpressionEncoder[A12]()).toOption :: Try(ExpressionEncoder[A13]()).toOption :: Try(
      ExpressionEncoder[A14]()
    ).toOption :: Try(ExpressionEncoder[A15]()).toOption :: Try(ExpressionEncoder[A16]()).toOption :: Try(
      ExpressionEncoder[A17]()
    ).toOption :: Try(ExpressionEncoder[A18]()).toOption :: Try(ExpressionEncoder[A19]()).toOption :: Try(
      ExpressionEncoder[A20]()
    ).toOption :: Try(ExpressionEncoder[A21]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 21) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 21; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  /**
    * Registers a deterministic Scala closure of 22 arguments as user-defined function (UDF).
    * @tparam RT return type of UDF.
    * @since 1.3.0
    */
  def register[
      RT: TypeTag,
      A1: TypeTag,
      A2: TypeTag,
      A3: TypeTag,
      A4: TypeTag,
      A5: TypeTag,
      A6: TypeTag,
      A7: TypeTag,
      A8: TypeTag,
      A9: TypeTag,
      A10: TypeTag,
      A11: TypeTag,
      A12: TypeTag,
      A13: TypeTag,
      A14: TypeTag,
      A15: TypeTag,
      A16: TypeTag,
      A17: TypeTag,
      A18: TypeTag,
      A19: TypeTag,
      A20: TypeTag,
      A21: TypeTag,
      A22: TypeTag
  ](
      name: String,
      func: Function22[
        A1,
        A2,
        A3,
        A4,
        A5,
        A6,
        A7,
        A8,
        A9,
        A10,
        A11,
        A12,
        A13,
        A14,
        A15,
        A16,
        A17,
        A18,
        A19,
        A20,
        A21,
        A22,
        RT
      ]
  ): UserDefinedFunction = {
    val ScalaReflection.Schema(dataType, nullable) = ScalaReflection.schemaFor[RT]
    val inputEncoders
        : Seq[Option[ExpressionEncoder[_]]] = Try(ExpressionEncoder[A1]()).toOption :: Try(
      ExpressionEncoder[A2]()
    ).toOption :: Try(ExpressionEncoder[A3]()).toOption :: Try(ExpressionEncoder[A4]()).toOption :: Try(
      ExpressionEncoder[A5]()
    ).toOption :: Try(ExpressionEncoder[A6]()).toOption :: Try(ExpressionEncoder[A7]()).toOption :: Try(
      ExpressionEncoder[A8]()
    ).toOption :: Try(ExpressionEncoder[A9]()).toOption :: Try(ExpressionEncoder[A10]()).toOption :: Try(
      ExpressionEncoder[A11]()
    ).toOption :: Try(ExpressionEncoder[A12]()).toOption :: Try(ExpressionEncoder[A13]()).toOption :: Try(
      ExpressionEncoder[A14]()
    ).toOption :: Try(ExpressionEncoder[A15]()).toOption :: Try(ExpressionEncoder[A16]()).toOption :: Try(
      ExpressionEncoder[A17]()
    ).toOption :: Try(ExpressionEncoder[A18]()).toOption :: Try(ExpressionEncoder[A19]()).toOption :: Try(
      ExpressionEncoder[A20]()
    ).toOption :: Try(ExpressionEncoder[A21]()).toOption :: Try(ExpressionEncoder[A22]()).toOption :: Nil
    val udf = SparkUserDefinedFunction(func, dataType, inputEncoders).withName(name)
    val finalUdf = if (nullable) udf else udf.asNonNullable()
    def builder(e: Seq[Expression]) =
      if (e.length == 22) {
        val scalaUdf: ScalaUDF = finalUdf.createScalaUDF(e)
        throw new ScoreException(scalaUdf, calculateScore(scalaUdf.inputTypes, e.map(_.dataType)))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 22; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
    finalUdf
  }

  //////////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////

  /**
    * Register a Java UDF class using reflection, for use from pyspark
    *
    * @param name   udf name
    * @param className   fully qualified class name of udf
    * @param returnDataType  return type of udf. If it is null, spark would try to infer
    *                        via reflection.
    */
  private[sql] def registerJava(name: String, className: String, returnDataType: DataType): Unit = {

    try {
      val clazz = Utils.classForName[AnyRef](className)
      val udfInterfaces = clazz.getGenericInterfaces
        .filter(_.isInstanceOf[ParameterizedType])
        .map(_.asInstanceOf[ParameterizedType])
        .filter(
          e =>
            e.getRawType.isInstanceOf[Class[_]] && e.getRawType
              .asInstanceOf[Class[_]]
              .getCanonicalName
              .startsWith("org.apache.spark.sql.api.java.UDF")
        )
      if (udfInterfaces.length == 0) {
        throw new AnalysisException(s"UDF class $className doesn't implement any UDF interface")
      } else if (udfInterfaces.length > 1) {
        throw new AnalysisException(
          s"It is invalid to implement multiple UDF interfaces, UDF class $className"
        )
      } else {
        try {
          val udf = clazz.getConstructor().newInstance()
          val udfReturnType = udfInterfaces(0).getActualTypeArguments.last
          var returnType = returnDataType
          if (returnType == null) {
            returnType = JavaTypeInference.inferDataType(udfReturnType)._1
          }

          udfInterfaces(0).getActualTypeArguments.length match {
            case 1 => register(name, udf.asInstanceOf[UDF0[_]], returnType)
            case 2 => register(name, udf.asInstanceOf[UDF1[_, _]], returnType)
            case 3 => register(name, udf.asInstanceOf[UDF2[_, _, _]], returnType)
            case 4 => register(name, udf.asInstanceOf[UDF3[_, _, _, _]], returnType)
            case 5 => register(name, udf.asInstanceOf[UDF4[_, _, _, _, _]], returnType)
            case 6 => register(name, udf.asInstanceOf[UDF5[_, _, _, _, _, _]], returnType)
            case 7 => register(name, udf.asInstanceOf[UDF6[_, _, _, _, _, _, _]], returnType)
            case 8 => register(name, udf.asInstanceOf[UDF7[_, _, _, _, _, _, _, _]], returnType)
            case 9 => register(name, udf.asInstanceOf[UDF8[_, _, _, _, _, _, _, _, _]], returnType)
            case 10 =>
              register(name, udf.asInstanceOf[UDF9[_, _, _, _, _, _, _, _, _, _]], returnType)
            case 11 =>
              register(name, udf.asInstanceOf[UDF10[_, _, _, _, _, _, _, _, _, _, _]], returnType)
            case 12 =>
              register(
                name,
                udf.asInstanceOf[UDF11[_, _, _, _, _, _, _, _, _, _, _, _]],
                returnType
              )
            case 13 =>
              register(
                name,
                udf.asInstanceOf[UDF12[_, _, _, _, _, _, _, _, _, _, _, _, _]],
                returnType
              )
            case 14 =>
              register(
                name,
                udf.asInstanceOf[UDF13[_, _, _, _, _, _, _, _, _, _, _, _, _, _]],
                returnType
              )
            case 15 =>
              register(
                name,
                udf.asInstanceOf[UDF14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _]],
                returnType
              )
            case 16 =>
              register(
                name,
                udf.asInstanceOf[UDF15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]],
                returnType
              )
            case 17 =>
              register(
                name,
                udf.asInstanceOf[UDF16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]],
                returnType
              )
            case 18 =>
              register(
                name,
                udf.asInstanceOf[UDF17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]],
                returnType
              )
            case 19 =>
              register(
                name,
                udf.asInstanceOf[UDF18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]],
                returnType
              )
            case 20 =>
              register(
                name,
                udf.asInstanceOf[UDF19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]],
                returnType
              )
            case 21 =>
              register(
                name,
                udf.asInstanceOf[
                  UDF20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
                ],
                returnType
              )
            case 22 =>
              register(
                name,
                udf.asInstanceOf[
                  UDF21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
                ],
                returnType
              )
            case 23 =>
              register(
                name,
                udf.asInstanceOf[
                  UDF22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _]
                ],
                returnType
              )
            case n =>
              throw new AnalysisException(s"UDF class with $n type arguments is not supported.")
          }
        } catch {
          case e @ (_: InstantiationException | _: IllegalArgumentException) =>
            throw new AnalysisException(
              s"Can not instantiate class $className, please make sure it has public non argument constructor"
            )
        }
      }
    } catch {
      case e: ClassNotFoundException =>
        throw new AnalysisException(
          s"Can not load class $className, please make sure it is on the classpath"
        )
    }

  }

  /**
    * Register a Java UDAF class using reflection, for use from pyspark
    *
    * @param name     UDAF name
    * @param className    fully qualified class name of UDAF
    */
  private[sql] def registerJavaUDAF(name: String, className: String): Unit = {
    try {
      val clazz = Utils.classForName[AnyRef](className)
      if (!classOf[UserDefinedAggregateFunction].isAssignableFrom(clazz)) {
        throw new AnalysisException(
          s"class $className doesn't implement interface UserDefinedAggregateFunction"
        )
      }
      val udaf = clazz.getConstructor().newInstance().asInstanceOf[UserDefinedAggregateFunction]
      register(name, udaf)
    } catch {
      case e: ClassNotFoundException =>
        throw new AnalysisException(
          s"Can not load class ${className}, please make sure it is on the classpath"
        )
      case e @ (_: InstantiationException | _: IllegalArgumentException) =>
        throw new AnalysisException(
          s"Can not instantiate class ${className}, please make sure it has public non argument constructor"
        )
    }
  }

  /**
    * Register a deterministic Java UDF0 instance as user-defined function (UDF).
    * @since 2.3.0
    */
  def register(name: String, f: UDF0[_], returnType: DataType): Unit = {
    val func = () => f.asInstanceOf[UDF0[Any]].call()
    def builder(e: Seq[Expression]) =
      if (e.length == 0) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 0; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF1 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(name: String, f: UDF1[_, _], returnType: DataType): Unit = {
    val func = f.asInstanceOf[UDF1[Any, Any]].call(_: Any)
    def builder(e: Seq[Expression]) =
      if (e.length == 1) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 1; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF2 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(name: String, f: UDF2[_, _, _], returnType: DataType): Unit = {
    val func = f.asInstanceOf[UDF2[Any, Any, Any]].call(_: Any, _: Any)
    def builder(e: Seq[Expression]) =
      if (e.length == 2) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 2; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF3 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(name: String, f: UDF3[_, _, _, _], returnType: DataType): Unit = {
    val func = f.asInstanceOf[UDF3[Any, Any, Any, Any]].call(_: Any, _: Any, _: Any)
    def builder(e: Seq[Expression]) =
      if (e.length == 3) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 3; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF4 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(name: String, f: UDF4[_, _, _, _, _], returnType: DataType): Unit = {
    val func = f.asInstanceOf[UDF4[Any, Any, Any, Any, Any]].call(_: Any, _: Any, _: Any, _: Any)
    def builder(e: Seq[Expression]) =
      if (e.length == 4) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 4; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF5 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(name: String, f: UDF5[_, _, _, _, _, _], returnType: DataType): Unit = {
    val func = f
      .asInstanceOf[UDF5[Any, Any, Any, Any, Any, Any]]
      .call(_: Any, _: Any, _: Any, _: Any, _: Any)
    def builder(e: Seq[Expression]) =
      if (e.length == 5) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 5; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF6 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(name: String, f: UDF6[_, _, _, _, _, _, _], returnType: DataType): Unit = {
    val func = f
      .asInstanceOf[UDF6[Any, Any, Any, Any, Any, Any, Any]]
      .call(_: Any, _: Any, _: Any, _: Any, _: Any, _: Any)
    def builder(e: Seq[Expression]) =
      if (e.length == 6) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 6; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF7 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(name: String, f: UDF7[_, _, _, _, _, _, _, _], returnType: DataType): Unit = {
    val func = f
      .asInstanceOf[UDF7[Any, Any, Any, Any, Any, Any, Any, Any]]
      .call(_: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any)
    def builder(e: Seq[Expression]) =
      if (e.length == 7) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 7; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF8 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(name: String, f: UDF8[_, _, _, _, _, _, _, _, _], returnType: DataType): Unit = {
    val func = f
      .asInstanceOf[UDF8[Any, Any, Any, Any, Any, Any, Any, Any, Any]]
      .call(_: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any)
    def builder(e: Seq[Expression]) =
      if (e.length == 8) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 8; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF9 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(name: String, f: UDF9[_, _, _, _, _, _, _, _, _, _], returnType: DataType): Unit = {
    val func = f
      .asInstanceOf[UDF9[Any, Any, Any, Any, Any, Any, Any, Any, Any, Any]]
      .call(_: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any)
    def builder(e: Seq[Expression]) =
      if (e.length == 9) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 9; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF10 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF10[_, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[UDF10[Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any]]
      .call(_: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any)
    def builder(e: Seq[Expression]) =
      if (e.length == 10) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 10; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF11 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF11[_, _, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[UDF11[Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any]]
      .call(_: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any, _: Any)
    def builder(e: Seq[Expression]) =
      if (e.length == 11) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 11; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF12 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF12[_, _, _, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[UDF12[Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any]]
      .call(
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any
      )
    def builder(e: Seq[Expression]) =
      if (e.length == 12) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 12; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF13 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF13[_, _, _, _, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[UDF13[Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any]]
      .call(
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any
      )
    def builder(e: Seq[Expression]) =
      if (e.length == 13) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 13; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF14 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF14[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[
        UDF14[Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any]
      ]
      .call(
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any
      )
    def builder(e: Seq[Expression]) =
      if (e.length == 14) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 14; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF15 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF15[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[
        UDF15[Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any]
      ]
      .call(
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any
      )
    def builder(e: Seq[Expression]) =
      if (e.length == 15) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 15; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF16 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF16[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[
        UDF16[Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any, Any]
      ]
      .call(
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any
      )
    def builder(e: Seq[Expression]) =
      if (e.length == 16) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 16; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF17 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF17[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[UDF17[
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any
      ]]
      .call(
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any
      )
    def builder(e: Seq[Expression]) =
      if (e.length == 17) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 17; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF18 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF18[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[UDF18[
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any
      ]]
      .call(
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any
      )
    def builder(e: Seq[Expression]) =
      if (e.length == 18) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 18; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF19 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF19[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[UDF19[
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any
      ]]
      .call(
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any
      )
    def builder(e: Seq[Expression]) =
      if (e.length == 19) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 19; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF20 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF20[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[UDF20[
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any
      ]]
      .call(
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any
      )
    def builder(e: Seq[Expression]) =
      if (e.length == 20) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 20; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF21 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF21[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[UDF21[
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any
      ]]
      .call(
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any
      )
    def builder(e: Seq[Expression]) =
      if (e.length == 21) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 21; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  /**
    * Register a deterministic Java UDF22 instance as user-defined function (UDF).
    * @since 1.3.0
    */
  def register(
      name: String,
      f: UDF22[_, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _, _],
      returnType: DataType
  ): Unit = {
    val func = f
      .asInstanceOf[UDF22[
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any,
        Any
      ]]
      .call(
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any,
        _: Any
      )
    def builder(e: Seq[Expression]) =
      if (e.length == 22) {
        ScalaUDF(func, returnType, e, Nil, udfName = Some(name))
      } else {
        throw new AnalysisException(
          "Invalid number of arguments for function " + name +
            ". Expected: 22; Found: " + e.length
        )
      }
    functionRegistry.createOrReplaceTempFunction(name, builder)
  }

  // scalastyle:on line.size.limit

}
