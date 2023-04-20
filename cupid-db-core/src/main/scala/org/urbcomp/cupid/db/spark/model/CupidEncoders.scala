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
package org.urbcomp.cupid.db.spark.model

import org.apache.spark.sql.{Encoder, Encoders}
import org.apache.spark.sql.catalyst.encoders.{ExpressionEncoder, encoderFor}
import org.urbcomp.cupid.db.model.roadnetwork.{RoadNetwork, RoadSegment}
import org.urbcomp.cupid.db.model.trajectory.Trajectory

/** Encoders are Spark SQL's mechanism for converting a JVM type into a Catalyst representation.
  * They are fetched from implicit scope whenever types move beween RDDs and Datasets. Because each
  * of the types supported below has a corresponding UDT, we are able to use a standard Spark Encoder
  * to construct these implicits. */
trait CupidEncoders {
  implicit def trajectoryEncoder: Encoder[Trajectory] = ExpressionEncoder()
  implicit def roadNetworkEncoder: Encoder[RoadNetwork] = ExpressionEncoder()
  implicit def roadSegmentEncoder: Encoder[RoadSegment] = ExpressionEncoder()
  implicit def bigDecimalEncoder: Encoder[BigDecimal] = ExpressionEncoder()

  implicit def tuple2[A1, A2](implicit e1: Encoder[A1], e2: Encoder[A2]): Encoder[(A1, A2)] =
    Encoders.tuple[A1, A2](e1, e2)

  implicit def tuple3[A1, A2, A3](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3]
  ): Encoder[(A1, A2, A3)] = Encoders.tuple[A1, A2, A3](e1, e2, e3)

  implicit def tuple4[A1, A2, A3, A4](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4]
  ): Encoder[(A1, A2, A3, A4)] = Encoders.tuple[A1, A2, A3, A4](e1, e2, e3, e4)

  implicit def tuple5[A1, A2, A3, A4, A5](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5]
  ): Encoder[(A1, A2, A3, A4, A5)] = Encoders.tuple[A1, A2, A3, A4, A5](e1, e2, e3, e4, e5)

  implicit def tuple6[A1, A2, A3, A4, A5, A6](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6]
  ): Encoder[(A1, A2, A3, A4, A5, A6)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6)
        )
      )
      .asInstanceOf[ExpressionEncoder[(A1, A2, A3, A4, A5, A6)]]

  implicit def tuple7[A1, A2, A3, A4, A5, A6, A7](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7]
  ): Encoder[(A1, A2, A3, A4, A5, A6, A7)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7)
        )
      )
      .asInstanceOf[ExpressionEncoder[(A1, A2, A3, A4, A5, A6, A7)]]

  implicit def tuple8[A1, A2, A3, A4, A5, A6, A7, A8](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8]
  ): Encoder[(A1, A2, A3, A4, A5, A6, A7, A8)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8)
        )
      )
      .asInstanceOf[ExpressionEncoder[(A1, A2, A3, A4, A5, A6, A7, A8)]]

  implicit def tuple9[A1, A2, A3, A4, A5, A6, A7, A8, A9](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9]
  ): Encoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9)
        )
      )
      .asInstanceOf[ExpressionEncoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9)]]

  implicit def tuple10[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10]
  ): Encoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10)
        )
      )
      .asInstanceOf[ExpressionEncoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10)]]

  implicit def tuple11[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10],
      e11: Encoder[A11]
  ): Encoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10),
          encoderFor(e11)
        )
      )
      .asInstanceOf[ExpressionEncoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11)]]

  implicit def tuple12[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10],
      e11: Encoder[A11],
      e12: Encoder[A12]
  ): Encoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10),
          encoderFor(e11),
          encoderFor(e12)
        )
      )
      .asInstanceOf[ExpressionEncoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12)]]

  implicit def tuple13[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10],
      e11: Encoder[A11],
      e12: Encoder[A12],
      e13: Encoder[A13]
  ): Encoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10),
          encoderFor(e11),
          encoderFor(e12),
          encoderFor(e13)
        )
      )
      .asInstanceOf[ExpressionEncoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13)]]

  implicit def tuple14[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10],
      e11: Encoder[A11],
      e12: Encoder[A12],
      e13: Encoder[A13],
      e14: Encoder[A14]
  ): Encoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10),
          encoderFor(e11),
          encoderFor(e12),
          encoderFor(e13),
          encoderFor(e14)
        )
      )
      .asInstanceOf[ExpressionEncoder[
        (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14)
      ]]

  implicit def tuple15[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10],
      e11: Encoder[A11],
      e12: Encoder[A12],
      e13: Encoder[A13],
      e14: Encoder[A14],
      e15: Encoder[A15]
  ): Encoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10),
          encoderFor(e11),
          encoderFor(e12),
          encoderFor(e13),
          encoderFor(e14),
          encoderFor(e15)
        )
      )
      .asInstanceOf[ExpressionEncoder[
        (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15)
      ]]

  implicit def tuple16[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10],
      e11: Encoder[A11],
      e12: Encoder[A12],
      e13: Encoder[A13],
      e14: Encoder[A14],
      e15: Encoder[A15],
      e16: Encoder[A16]
  ): Encoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10),
          encoderFor(e11),
          encoderFor(e12),
          encoderFor(e13),
          encoderFor(e14),
          encoderFor(e15),
          encoderFor(e16)
        )
      )
      .asInstanceOf[ExpressionEncoder[
        (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16)
      ]]

  implicit def tuple17[A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10],
      e11: Encoder[A11],
      e12: Encoder[A12],
      e13: Encoder[A13],
      e14: Encoder[A14],
      e15: Encoder[A15],
      e16: Encoder[A16],
      e17: Encoder[A17]
  ): Encoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10),
          encoderFor(e11),
          encoderFor(e12),
          encoderFor(e13),
          encoderFor(e14),
          encoderFor(e15),
          encoderFor(e16),
          encoderFor(e17)
        )
      )
      .asInstanceOf[ExpressionEncoder[
        (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17)
      ]]

  implicit def tuple18[
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
      A18
  ](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10],
      e11: Encoder[A11],
      e12: Encoder[A12],
      e13: Encoder[A13],
      e14: Encoder[A14],
      e15: Encoder[A15],
      e16: Encoder[A16],
      e17: Encoder[A17],
      e18: Encoder[A18]
  ): Encoder[(A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18)] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10),
          encoderFor(e11),
          encoderFor(e12),
          encoderFor(e13),
          encoderFor(e14),
          encoderFor(e15),
          encoderFor(e16),
          encoderFor(e17),
          encoderFor(e18)
        )
      )
      .asInstanceOf[ExpressionEncoder[
        (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18)
      ]]

  implicit def tuple19[
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
      A19
  ](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10],
      e11: Encoder[A11],
      e12: Encoder[A12],
      e13: Encoder[A13],
      e14: Encoder[A14],
      e15: Encoder[A15],
      e16: Encoder[A16],
      e17: Encoder[A17],
      e18: Encoder[A18],
      e19: Encoder[A19]
  ): Encoder[
    (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19)
  ] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10),
          encoderFor(e11),
          encoderFor(e12),
          encoderFor(e13),
          encoderFor(e14),
          encoderFor(e15),
          encoderFor(e16),
          encoderFor(e17),
          encoderFor(e18),
          encoderFor(e19)
        )
      )
      .asInstanceOf[ExpressionEncoder[
        (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19)
      ]]

  implicit def tuple20[
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
      A20
  ](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10],
      e11: Encoder[A11],
      e12: Encoder[A12],
      e13: Encoder[A13],
      e14: Encoder[A14],
      e15: Encoder[A15],
      e16: Encoder[A16],
      e17: Encoder[A17],
      e18: Encoder[A18],
      e19: Encoder[A19],
      e20: Encoder[A20]
  ): Encoder[
    (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20)
  ] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10),
          encoderFor(e11),
          encoderFor(e12),
          encoderFor(e13),
          encoderFor(e14),
          encoderFor(e15),
          encoderFor(e16),
          encoderFor(e17),
          encoderFor(e18),
          encoderFor(e19),
          encoderFor(e20)
        )
      )
      .asInstanceOf[ExpressionEncoder[
        (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20)
      ]]

  implicit def tuple21[
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
      A21
  ](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10],
      e11: Encoder[A11],
      e12: Encoder[A12],
      e13: Encoder[A13],
      e14: Encoder[A14],
      e15: Encoder[A15],
      e16: Encoder[A16],
      e17: Encoder[A17],
      e18: Encoder[A18],
      e19: Encoder[A19],
      e20: Encoder[A20],
      e21: Encoder[A21]
  ): Encoder[
    (A1, A2, A3, A4, A5, A6, A7, A8, A9, A10, A11, A12, A13, A14, A15, A16, A17, A18, A19, A20, A21)
  ] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10),
          encoderFor(e11),
          encoderFor(e12),
          encoderFor(e13),
          encoderFor(e14),
          encoderFor(e15),
          encoderFor(e16),
          encoderFor(e17),
          encoderFor(e18),
          encoderFor(e19),
          encoderFor(e20),
          encoderFor(e21)
        )
      )
      .asInstanceOf[ExpressionEncoder[
        (
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
            A21
        )
      ]]

  implicit def tuple22[
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
      A22
  ](
      implicit e1: Encoder[A1],
      e2: Encoder[A2],
      e3: Encoder[A3],
      e4: Encoder[A4],
      e5: Encoder[A5],
      e6: Encoder[A6],
      e7: Encoder[A7],
      e8: Encoder[A8],
      e9: Encoder[A9],
      e10: Encoder[A10],
      e11: Encoder[A11],
      e12: Encoder[A12],
      e13: Encoder[A13],
      e14: Encoder[A14],
      e15: Encoder[A15],
      e16: Encoder[A16],
      e17: Encoder[A17],
      e18: Encoder[A18],
      e19: Encoder[A19],
      e20: Encoder[A20],
      e21: Encoder[A21],
      e22: Encoder[A22]
  ): Encoder[
    (
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
        A22
    )
  ] =
    ExpressionEncoder
      .tuple(
        Seq(
          encoderFor(e1),
          encoderFor(e2),
          encoderFor(e3),
          encoderFor(e4),
          encoderFor(e5),
          encoderFor(e6),
          encoderFor(e7),
          encoderFor(e8),
          encoderFor(e9),
          encoderFor(e10),
          encoderFor(e11),
          encoderFor(e12),
          encoderFor(e13),
          encoderFor(e14),
          encoderFor(e15),
          encoderFor(e16),
          encoderFor(e17),
          encoderFor(e18),
          encoderFor(e19),
          encoderFor(e20),
          encoderFor(e21),
          encoderFor(e22)
        )
      )
      .asInstanceOf[ExpressionEncoder[
        (
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
            A22
        )
      ]]

}
