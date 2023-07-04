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
package org.locationtech.geomesa.spark

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.cupid.{AbstractCupidUDT, CupidTypes}
import org.apache.spark.sql.jts.JTSTypes
import org.apache.spark.sql.sources._
import org.apache.spark.sql.types.{DataTypes, StructField, StructType, TimestampType}
import org.geotools.factory.CommonFactoryFinder
import org.geotools.feature.simple.SimpleFeatureTypeBuilder
import org.locationtech.geomesa.features.ScalaSimpleFeature
import org.locationtech.geomesa.utils.geotools.ObjectType
import org.locationtech.geomesa.utils.uuid.TimeSortedUuidGenerator
import org.opengis.feature.`type`.AttributeDescriptor
import org.opengis.feature.simple.{SimpleFeature, SimpleFeatureType}
import org.opengis.filter.FilterFactory2
import org.urbcomp.cupid.db.model.roadnetwork.RoadSegment
import org.urbcomp.cupid.db.model.trajectory.Trajectory

import java.sql.Timestamp
import java.util.Date
import scala.util.Try

object SparkUtils extends LazyLogging {

  import scala.collection.JavaConverters._

  @transient
  val ff: FilterFactory2 = CommonFactoryFinder.getFilterFactory2

  // the SFT attributes do not have the __fid__ so we have to translate accordingly
  def getExtractors(
      requiredColumns: Array[String],
      schema: StructType
  ): Array[SimpleFeature => AnyRef] = {
    val requiredAttributes = requiredColumns.filterNot(_ == "__fid__")

    type EXTRACTOR = SimpleFeature => AnyRef
    val IdExtractor: SimpleFeature => AnyRef = sf => sf.getID

    requiredColumns.map {
      case "__fid__" => IdExtractor
      case col =>
        val schemaIndex = schema.fieldIndex(col)
        val fieldType = schema.fields(schemaIndex).dataType

        fieldType match {
          case Timestamp =>
            sf: SimpleFeature => {
              val name = sf.getProperties.asScala
                .filter(p => p.getName.toString.equals(col))
                .head
                .getName
                .toString
              val attr = sf.getAttribute(name)
              if (attr == null) {
                null
              } else {
                new Timestamp(attr.asInstanceOf[Date].getTime)
              }
            }
          case TrajectoryUDT =>
            sf: SimpleFeature => {
              val name = sf.getProperties.asScala
                .filter(p => p.getName.toString.endsWith(".tid"))
                .head
                .getName
                .toString
              val js = sf.getAttribute(name.substring(0, name.length - 4) + ".geoJson")
              Trajectory.fromGeoJSON(js.asInstanceOf[String])
            }

          case RoadSegmentUDT =>
            sf: SimpleFeature => {
              val name = sf.getProperties.asScala
                .filter(p => p.getName.toString.endsWith(".rsId"))
                .head
                .getName
                .toString
              val js = sf.getAttribute(name.substring(0, name.size - 5) + ".rsGeoJson")
              RoadSegment.fromGeoJSON(js.asInstanceOf[String])
            }

          case _ =>
            sf: SimpleFeature => {
              val name = sf.getProperties.asScala
                .filter(p => p.getName.toString.equals(col))
                .head
                .getName
                .toString
              sf.getAttribute(name)
            }
        }
    }
  }

  def sparkFilterToCQLFilter(
      filt: org.apache.spark.sql.sources.Filter
  ): Option[org.opengis.filter.Filter] = filt match {
    case GreaterThanOrEqual(attribute, v) =>
      Some(ff.greaterOrEqual(ff.property(attribute), ff.literal(v)))
    case GreaterThan(attr, v)                  => Some(ff.greater(ff.property(attr), ff.literal(v)))
    case LessThanOrEqual(attr, v)              => Some(ff.lessOrEqual(ff.property(attr), ff.literal(v)))
    case LessThan(attr, v)                     => Some(ff.less(ff.property(attr), ff.literal(v)))
    case EqualTo(attr, v) if attr == "__fid__" => Some(ff.id(ff.featureId(v.toString)))
    case EqualTo(attr, v)                      => Some(ff.equals(ff.property(attr), ff.literal(v)))
    case In(attr, values) if attr == "__fid__" =>
      Some(ff.id(values.map(v => ff.featureId(v.toString)).toSet.asJava))
    case In(attr, values) =>
      Some(
        values
          .map(v => ff.equals(ff.property(attr), ff.literal(v)))
          .reduce[org.opengis.filter.Filter]((l, r) => ff.or(l, r))
      )
    case And(left, right) =>
      Some(ff.and(sparkFilterToCQLFilter(left).get, sparkFilterToCQLFilter(right).get)) // TODO: can these be null
    case Or(left, right) =>
      Some(ff.or(sparkFilterToCQLFilter(left).get, sparkFilterToCQLFilter(right).get))
    case Not(f)                 => Some(ff.not(sparkFilterToCQLFilter(f).get))
    case StringStartsWith(a, v) => Some(ff.like(ff.property(a), s"$v%"))
    case StringEndsWith(a, v)   => Some(ff.like(ff.property(a), s"%$v"))
    case StringContains(a, v)   => Some(ff.like(ff.property(a), s"%$v%"))
    case IsNull(attr)           => None
    case IsNotNull(attr)        => None
  }

  def createFeatureType(name: String, struct: StructType): SimpleFeatureType = {
    val builder = new SimpleFeatureTypeBuilder
    builder.setName(name)

    struct.fields.filter(_.name != "__fid__").foreach { field =>
      val binding = field.dataType match {
        case DataTypes.StringType              => classOf[java.lang.String]
        case DataTypes.DateType                => classOf[java.util.Date]
        case DataTypes.TimestampType           => classOf[java.util.Date]
        case DataTypes.IntegerType             => classOf[java.lang.Integer]
        case DataTypes.LongType                => classOf[java.lang.Long]
        case DataTypes.FloatType               => classOf[java.lang.Float]
        case DataTypes.DoubleType              => classOf[java.lang.Double]
        case DataTypes.BooleanType             => classOf[java.lang.Boolean]
        case JTSTypes.PointTypeInstance        => classOf[org.locationtech.jts.geom.Point]
        case JTSTypes.LineStringTypeInstance   => classOf[org.locationtech.jts.geom.LineString]
        case JTSTypes.PolygonTypeInstance      => classOf[org.locationtech.jts.geom.Polygon]
        case JTSTypes.MultipolygonTypeInstance => classOf[org.locationtech.jts.geom.MultiPolygon]
        case JTSTypes.GeometryTypeInstance     => classOf[org.locationtech.jts.geom.Geometry]
      }
      builder.add(field.name, binding)
    }

    builder.buildFeatureType()
  }

  def createStructType(sft: SimpleFeatureType): StructType = {
    var sfb = new SimpleFeatureTypeBuilder()
    sfb.setName(sft.getName)
    val fields = sft.getAttributeDescriptors.asScala.flatMap(createStructField).toList
    var nf = new Array[StructField](0)
    val f = fields.asJava
    var i = 0
    while (i < f.size) {
      val name = f.get(i).name
      if (f.get(i).name.endsWith(".tid")) {

        val fi = new StructField(name.substring(0, name.size - 4), TrajectoryUDT, true)
        nf = nf :+ fi
        i = i + 5
      } else if (name.endsWith(".rsId")) {
        val fi = new StructField(name.substring(0, name.size - 5), RoadSegmentUDT, true)
        nf = nf :+ fi
        i = i + 2

      } else nf = nf :+ f.get(i)
      i = i + 1

    }
    StructType(StructField("__fid__", DataTypes.StringType, nullable = false) :: nf.toList)
  }

  private def createStructField(ad: AttributeDescriptor): Option[StructField] = {
    val bindings = Try(ObjectType.selectType(ad)).getOrElse(Seq.empty)
    val dt = bindings.head match {
      case ObjectType.STRING  => DataTypes.StringType
      case ObjectType.INT     => DataTypes.IntegerType
      case ObjectType.LONG    => DataTypes.LongType
      case ObjectType.FLOAT   => DataTypes.FloatType
      case ObjectType.DOUBLE  => DataTypes.DoubleType
      case ObjectType.BOOLEAN => DataTypes.BooleanType
      case ObjectType.DATE    => DataTypes.TimestampType
      case ObjectType.UUID    => null // not supported
      case ObjectType.BYTES   => null // not supported
      case ObjectType.LIST    => null // not supported
      case ObjectType.MAP     => null // not supported
      case ObjectType.GEOMETRY =>
        bindings.last match {
          case ObjectType.POINT               => JTSTypes.PointTypeInstance
          case ObjectType.LINESTRING          => JTSTypes.LineStringTypeInstance
          case ObjectType.POLYGON             => JTSTypes.PolygonTypeInstance
          case ObjectType.MULTIPOINT          => JTSTypes.MultiPointTypeInstance
          case ObjectType.MULTILINESTRING     => JTSTypes.MultiLineStringTypeInstance
          case ObjectType.MULTIPOLYGON        => JTSTypes.MultipolygonTypeInstance
          case ObjectType.GEOMETRY_COLLECTION => JTSTypes.GeometryTypeInstance
          case _                              => JTSTypes.GeometryTypeInstance
        }

      case _ =>
        logger.warn(s"Unexpected bindings for descriptor $ad: ${bindings.mkString(", ")}"); null
    }
    Option(dt).map(StructField(ad.getLocalName, _))
  }

  /**
    * Creates a function to convert a row to a simple feature. Columns will be mapped to attributes based on
    * matching names.
    *
    * If the row has a `__fid__` column, it will be used for the feature id. Otherwise, it will
    * use a random id prefixed with the current time
    *
    * @param sft simple feature type
    * @param schema dataframe schema
    * @return
    */
  def rowsToFeatures(sft: SimpleFeatureType, schema: StructType): SimpleFeatureRowMapping = {
    val mappings = Seq.tabulate(sft.getAttributeCount)(
      i => i -> schema.fieldIndex(sft.getDescriptor(i).getLocalName)
    )
    val fid: Row => String = schema.fields.indexWhere(_.name == "__fid__") match {
      case -1 => _ => TimeSortedUuidGenerator.createUuid().toString
      case i  => r => r.getString(i)
    }
    SimpleFeatureRowMapping(sft, mappings, fid)
  }

  /**
    * Creates a function to convert a row to a simple feature, which will be based on the columns in
    * the row schema.
    *
    * If the row has a `__fid__` column, it will be used for the feature id. Otherwise, it will
    * use a random id prefixed with the current time
    *
    * @param name simple feature type name to use
    * @param schema row schema
    * @return
    */
  def rowsToFeatures(name: String, schema: StructType): SimpleFeatureRowMapping =
    rowsToFeatures(createFeatureType(name, schema), schema)

  def sf2row(
      schema: StructType,
      sf: SimpleFeature,
      extractors: Array[SimpleFeature => AnyRef]
  ): Row = {
    val res = Array.ofDim[Any](extractors.length)
    var i = 0
    while (i < extractors.length) {
      res(i) = extractors(i)(sf)
      i += 1
    }
    new GenericRowWithSchema(res, schema)
  }

  def joinedSf2row(
      schema: StructType,
      sf1: SimpleFeature,
      sf2: SimpleFeature,
      extractors: Array[SimpleFeature => AnyRef]
  ): Row = {
    val leftLength = sf1.getAttributeCount + 1
    val res = Array.ofDim[Any](extractors.length)
    var i = 0
    while (i < leftLength) {
      res(i) = extractors(i)(sf1)
      i += 1
    }
    while (i < extractors.length) {
      res(i) = extractors(i)(sf2)
      i += 1
    }
    new GenericRowWithSchema(res, schema)
  }

  case class SimpleFeatureRowMapping(
      sft: SimpleFeatureType,
      mappings: Seq[(Int, Int)],
      id: Row => String
  ) {
    def apply(row: Row): SimpleFeature = {
      val feature = new ScalaSimpleFeature(sft, id(row))
      mappings.foreach {
        case (to, from) => feature.setAttributeNoConvert(to, row.getAs[Object](from))
      }
      feature
    }
  }
  private object TrajectoryUDT
      extends AbstractCupidUDT[Trajectory]("trajectory", CupidTypes.trajectorySerializer)
  private object RoadSegmentUDT
      extends AbstractCupidUDT[RoadSegment]("roadSegment", CupidTypes.roadSegmentSerializer)
}
