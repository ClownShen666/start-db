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
package org.apache.calcite.avatica;

import org.apache.calcite.avatica.proto.Common;
import org.apache.calcite.avatica.util.ByteString;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.calcite.avatica.util.StartTypes;
import org.locationtech.jts.geom.*;
import org.urbcomp.cupid.com.google.protobuf.Descriptors.FieldDescriptor;
import org.urbcomp.start.db.model.roadnetwork.RoadNetwork;
import org.urbcomp.start.db.model.roadnetwork.RoadSegment;
import org.urbcomp.start.db.model.trajectory.Trajectory;

import java.lang.reflect.Type;
import java.sql.Array;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Metadata for a column.
 *
 * <p>
 * (Compare with {@link java.sql.ResultSetMetaData}.)
 */
public class ColumnMetaData {
    private static final FieldDescriptor CATALOG_NAME_DESCRIPTOR = Common.ColumnMetaData
        .getDescriptor()
        .findFieldByNumber(Common.ColumnMetaData.CATALOG_NAME_FIELD_NUMBER);
    private static final FieldDescriptor SCHEMA_NAME_DESCRIPTOR = Common.ColumnMetaData
        .getDescriptor()
        .findFieldByNumber(Common.ColumnMetaData.SCHEMA_NAME_FIELD_NUMBER);
    private static final FieldDescriptor LABEL_DESCRIPTOR = Common.ColumnMetaData.getDescriptor()
        .findFieldByNumber(Common.ColumnMetaData.LABEL_FIELD_NUMBER);
    private static final FieldDescriptor COLUMN_NAME_DESCRIPTOR = Common.ColumnMetaData
        .getDescriptor()
        .findFieldByNumber(Common.ColumnMetaData.COLUMN_NAME_FIELD_NUMBER);
    private static final FieldDescriptor TABLE_NAME_DESCRIPTOR = Common.ColumnMetaData
        .getDescriptor()
        .findFieldByNumber(Common.ColumnMetaData.TABLE_NAME_FIELD_NUMBER);
    private static final FieldDescriptor COLUMN_CLASS_NAME_DESCRIPTOR = Common.ColumnMetaData
        .getDescriptor()
        .findFieldByNumber(Common.ColumnMetaData.COLUMN_CLASS_NAME_FIELD_NUMBER);

    public final int ordinal; // 0-based
    public final boolean autoIncrement;
    public final boolean caseSensitive;
    public final boolean searchable;
    public final boolean currency;
    public final int nullable;
    public final boolean signed;
    public final int displaySize;
    public final String label;
    public final String columnName;
    public final String schemaName;
    public final int precision;
    public final int scale;
    public final String tableName;
    public final String catalogName;
    public final boolean readOnly;
    public final boolean writable;
    public final boolean definitelyWritable;
    public final String columnClassName;
    public final AvaticaType type;

    @JsonCreator
    public ColumnMetaData(
        @JsonProperty("ordinal") int ordinal,
        @JsonProperty("autoIncrement") boolean autoIncrement,
        @JsonProperty("caseSensitive") boolean caseSensitive,
        @JsonProperty("searchable") boolean searchable,
        @JsonProperty("currency") boolean currency,
        @JsonProperty("nullable") int nullable,
        @JsonProperty("signed") boolean signed,
        @JsonProperty("displaySize") int displaySize,
        @JsonProperty("label") String label,
        @JsonProperty("columnName") String columnName,
        @JsonProperty("schemaName") String schemaName,
        @JsonProperty("precision") int precision,
        @JsonProperty("scale") int scale,
        @JsonProperty("tableName") String tableName,
        @JsonProperty("catalogName") String catalogName,
        @JsonProperty("type") AvaticaType type,
        @JsonProperty("readOnly") boolean readOnly,
        @JsonProperty("writable") boolean writable,
        @JsonProperty("definitelyWritable") boolean definitelyWritable,
        @JsonProperty("columnClassName") String columnClassName
    ) {
        this.ordinal = ordinal;
        this.autoIncrement = autoIncrement;
        this.caseSensitive = caseSensitive;
        this.searchable = searchable;
        this.currency = currency;
        this.nullable = nullable;
        this.signed = signed;
        this.displaySize = displaySize;
        this.label = label;
        // Per the JDBC spec this should be just columnName.
        // For example, the query
        // select 1 as x, c as y from t
        // should give columns
        // (label=x, column=null, table=null)
        // (label=y, column=c table=t)
        // But DbUnit requires every column to have a name. Duh.
        this.columnName = first(columnName, label);
        this.schemaName = schemaName;
        this.precision = precision;
        this.scale = scale;
        this.tableName = tableName;
        this.catalogName = catalogName;
        this.type = type;
        this.readOnly = readOnly;
        this.writable = writable;
        this.definitelyWritable = definitelyWritable;
        this.columnClassName = columnClassName;
    }

    public Common.ColumnMetaData toProto() {
        Common.ColumnMetaData.Builder builder = Common.ColumnMetaData.newBuilder();

        // Primitive fields (can't be null)
        builder.setOrdinal(ordinal)
            .setAutoIncrement(autoIncrement)
            .setCaseSensitive(caseSensitive)
            .setSearchable(searchable)
            .setCurrency(currency)
            .setNullable(nullable)
            .setSigned(signed)
            .setDisplaySize(displaySize)
            .setPrecision(precision)
            .setScale(scale)
            .setReadOnly(readOnly)
            .setWritable(writable)
            .setDefinitelyWritable(definitelyWritable);

        // Potentially null fields
        if (null != label) {
            builder.setLabel(label);
        }

        if (null != columnName) {
            builder.setColumnName(columnName);
        }

        if (null != schemaName) {
            builder.setSchemaName(schemaName);
        }

        if (null != tableName) {
            builder.setTableName(tableName);
        }

        if (null != catalogName) {
            builder.setCatalogName(catalogName);
        }

        if (null != type) {
            builder.setType(type.toProto());
        }

        if (null != columnClassName) {
            builder.setColumnClassName(columnClassName);
        }

        return builder.build();
    }

    public static ColumnMetaData fromProto(Common.ColumnMetaData proto) {
        AvaticaType nestedType = AvaticaType.fromProto(proto.getType());

        String catalogName = null;
        if (proto.hasField(CATALOG_NAME_DESCRIPTOR)) {
            catalogName = proto.getCatalogName();
        }

        String schemaName = null;
        if (proto.hasField(SCHEMA_NAME_DESCRIPTOR)) {
            schemaName = proto.getSchemaName();
        }

        String label = null;
        if (proto.hasField(LABEL_DESCRIPTOR)) {
            label = proto.getLabel();
        }

        String columnName = null;
        if (proto.hasField(COLUMN_NAME_DESCRIPTOR)) {
            columnName = proto.getColumnName();
        }

        String tableName = null;
        if (proto.hasField(TABLE_NAME_DESCRIPTOR)) {
            tableName = proto.getTableName();
        }

        String columnClassName = null;
        if (proto.hasField(COLUMN_CLASS_NAME_DESCRIPTOR)) {
            columnClassName = proto.getColumnClassName();
        }

        // Recreate the ColumnMetaData
        return new ColumnMetaData(
            proto.getOrdinal(),
            proto.getAutoIncrement(),
            proto.getCaseSensitive(),
            proto.getSearchable(),
            proto.getCurrency(),
            proto.getNullable(),
            proto.getSigned(),
            proto.getDisplaySize(),
            label,
            columnName,
            schemaName,
            proto.getPrecision(),
            proto.getScale(),
            tableName,
            catalogName,
            nestedType,
            proto.getReadOnly(),
            proto.getWritable(),
            proto.getDefinitelyWritable(),
            columnClassName
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            autoIncrement,
            caseSensitive,
            catalogName,
            columnClassName,
            columnName,
            currency,
            definitelyWritable,
            displaySize,
            label,
            nullable,
            ordinal,
            precision,
            readOnly,
            scale,
            schemaName,
            searchable,
            signed,
            tableName,
            type,
            writable
        );
    }

    @Override
    public boolean equals(Object o) {
        return o == this
            || o instanceof ColumnMetaData
                && autoIncrement == ((ColumnMetaData) o).autoIncrement
                && caseSensitive == ((ColumnMetaData) o).caseSensitive
                && Objects.equals(catalogName, ((ColumnMetaData) o).catalogName)
                && Objects.equals(columnClassName, ((ColumnMetaData) o).columnClassName)
                && Objects.equals(columnName, ((ColumnMetaData) o).columnName)
                && currency == ((ColumnMetaData) o).currency
                && definitelyWritable == ((ColumnMetaData) o).definitelyWritable
                && displaySize == ((ColumnMetaData) o).displaySize
                && Objects.equals(label, ((ColumnMetaData) o).label)
                && nullable == ((ColumnMetaData) o).nullable
                && ordinal == ((ColumnMetaData) o).ordinal
                && precision == ((ColumnMetaData) o).precision
                && readOnly == ((ColumnMetaData) o).readOnly
                && scale == ((ColumnMetaData) o).scale
                && Objects.equals(schemaName, ((ColumnMetaData) o).schemaName)
                && searchable == ((ColumnMetaData) o).searchable
                && signed == ((ColumnMetaData) o).signed
                && Objects.equals(tableName, ((ColumnMetaData) o).tableName)
                && Objects.equals(type, ((ColumnMetaData) o).type)
                && writable == ((ColumnMetaData) o).writable;
    }

    private static <T> T first(T t0, T t1) {
        return t0 != null ? t0 : t1;
    }

    /** Creates a {@link ScalarType}. */
    public static ScalarType scalar(int type, String typeName, Rep rep) {
        return new ScalarType(type, typeName, rep);
    }

    /** Creates a {@link StructType}. */
    public static StructType struct(List<ColumnMetaData> columns) {
        return new StructType(columns);
    }

    /** Creates an {@link ArrayType}. */
    public static ArrayType array(AvaticaType componentType, String typeName, Rep rep) {
        return new ArrayType(Types.ARRAY, typeName, rep, componentType);
    }

    /**
     * Creates a ColumnMetaData for result sets that are not based on a struct but need to have a
     * single 'field' for purposes of {@link java.sql.ResultSetMetaData}.
     */
    public static ColumnMetaData dummy(AvaticaType type, boolean nullable) {
        return new ColumnMetaData(
            0,
            false,
            true,
            false,
            false,
            nullable ? DatabaseMetaData.columnNullable : DatabaseMetaData.columnNoNulls,
            true,
            -1,
            null,
            null,
            null,
            -1,
            -1,
            null,
            null,
            type,
            true,
            false,
            false,
            type.columnClassName()
        );
    }

    public ColumnMetaData setRep(Rep rep) {
        return new ColumnMetaData(
            ordinal,
            autoIncrement,
            caseSensitive,
            searchable,
            currency,
            nullable,
            signed,
            displaySize,
            label,
            columnName,
            schemaName,
            precision,
            scale,
            tableName,
            catalogName,
            type.setRep(rep),
            readOnly,
            writable,
            definitelyWritable,
            columnClassName
        );
    }

    /**
     * Description of the type used to internally represent a value. For example, a
     * {@link java.sql.Date} might be represented as a {@link #PRIMITIVE_INT} if not nullable, or a
     * {@link #JAVA_SQL_DATE}.
     */
    public enum Rep {
        PRIMITIVE_BOOLEAN(boolean.class, Types.BOOLEAN),
        PRIMITIVE_BYTE(byte.class, Types.TINYINT),
        PRIMITIVE_CHAR(char.class, Types.CHAR),
        PRIMITIVE_SHORT(short.class, Types.SMALLINT),
        PRIMITIVE_INT(int.class, Types.INTEGER),
        PRIMITIVE_LONG(long.class, Types.BIGINT),
        PRIMITIVE_FLOAT(float.class, Types.FLOAT),
        PRIMITIVE_DOUBLE(double.class, Types.DOUBLE),
        BOOLEAN(Boolean.class, Types.BOOLEAN),
        BYTE(Byte.class, Types.TINYINT),
        CHARACTER(Character.class, Types.CHAR),
        SHORT(Short.class, Types.SMALLINT),
        INTEGER(Integer.class, Types.INTEGER),
        LONG(Long.class, Types.BIGINT),
        FLOAT(Float.class, Types.FLOAT),
        DOUBLE(Double.class, Types.DOUBLE),
        JAVA_SQL_TIME(Time.class, Types.TIME),
        JAVA_SQL_TIMESTAMP(Timestamp.class, Types.TIMESTAMP),
        JAVA_SQL_DATE(java.sql.Date.class, Types.DATE),
        JAVA_UTIL_DATE(java.util.Date.class, Types.DATE),
        BYTE_STRING(ByteString.class, Types.VARBINARY),
        STRING(String.class, Types.VARCHAR),

        // start-db add start
        POINT(Point.class, StartTypes.POINT),
        MULTIPOINT(MultiPoint.class, StartTypes.MULTIPOINT),
        LINESTRING(LineString.class, StartTypes.LINESTRING),
        MULTILINESTRING(MultiLineString.class, StartTypes.MULTILINESTRING),
        POLYGON(Polygon.class, StartTypes.POLYGON),
        MULTIPOLYGON(MultiPolygon.class, StartTypes.MULTIPOLYGON),
        GEOMETRY(Geometry.class, StartTypes.GEOMETRY),
        GEOMETRYCOLLECTION(GeometryCollection.class, StartTypes.GEOMETRYCOLLECTION),
        DATETIME(LocalDateTime.class, StartTypes.DATETIME),
        TRAJECTORY(Trajectory.class, StartTypes.TRAJECTORY),
        ROAD_NETWORK(RoadNetwork.class, StartTypes.ROAD_NETWORK),
        ROAD_SEGMENT(RoadSegment.class, StartTypes.ROAD_SEGMENT),
        // start-db add end

        /**
         * Values are represented as some sub-class of {@link Number}. The JSON encoding does this.
         */
        NUMBER(Number.class, Types.NUMERIC),

        ARRAY(Array.class, Types.ARRAY),
        MULTISET(List.class, Types.JAVA_OBJECT),
        STRUCT(Struct.class, Types.JAVA_OBJECT),

        OBJECT(Object.class, Types.JAVA_OBJECT);

        public final Class clazz;
        public final int typeId;

        public static final Map<Class, Rep> VALUE_MAP;

        static {
            Map<Class, Rep> builder = new HashMap<>();
            for (Rep rep : values()) {
                builder.put(rep.clazz, rep);
            }
            builder.put(byte[].class, BYTE_STRING);
            VALUE_MAP = Collections.unmodifiableMap(builder);
        }

        Rep(Class clazz, int typeId) {
            this.clazz = clazz;
            this.typeId = typeId;
        }

        public static Rep of(Type clazz) {
            // noinspection SuspiciousMethodCalls
            final Rep rep = VALUE_MAP.get(clazz);
            return rep != null ? rep : OBJECT;
        }

        /** Returns the value of a column of this type from a result set. */
        public Object jdbcGet(ResultSet resultSet, int i) throws SQLException {
            switch (this) {
                case PRIMITIVE_BOOLEAN:
                    return resultSet.getBoolean(i);
                case PRIMITIVE_BYTE:
                    return resultSet.getByte(i);
                case PRIMITIVE_SHORT:
                    return resultSet.getShort(i);
                case PRIMITIVE_INT:
                    return resultSet.getInt(i);
                case PRIMITIVE_LONG:
                    return resultSet.getLong(i);
                case PRIMITIVE_FLOAT:
                    return resultSet.getFloat(i);
                case PRIMITIVE_DOUBLE:
                    return resultSet.getDouble(i);
                case BOOLEAN:
                    final boolean aBoolean = resultSet.getBoolean(i);
                    return resultSet.wasNull() ? null : aBoolean;
                case BYTE:
                    final byte aByte = resultSet.getByte(i);
                    return resultSet.wasNull() ? null : aByte;
                case SHORT:
                    final short aShort = resultSet.getShort(i);
                    return resultSet.wasNull() ? null : aShort;
                case INTEGER:
                    final int anInt = resultSet.getInt(i);
                    return resultSet.wasNull() ? null : anInt;
                case LONG:
                    final long aLong = resultSet.getLong(i);
                    return resultSet.wasNull() ? null : aLong;
                case FLOAT:
                    final float aFloat = resultSet.getFloat(i);
                    return resultSet.wasNull() ? null : aFloat;
                case DOUBLE:
                    final double aDouble = resultSet.getDouble(i);
                    return resultSet.wasNull() ? null : aDouble;
                case JAVA_SQL_DATE:
                    return resultSet.getDate(i);
                case JAVA_SQL_TIME:
                    return resultSet.getTime(i);
                case JAVA_SQL_TIMESTAMP:
                    return resultSet.getTimestamp(i);
                case ARRAY:
                    return resultSet.getArray(i);
                case STRUCT:
                    return resultSet.getObject(i, Struct.class);
                default:
                    return resultSet.getObject(i);
            }
        }

        public Common.Rep toProto() {
            return Common.Rep.valueOf(name());
        }

        public static Rep fromProto(Common.Rep proto) {
            if (Common.Rep.UNRECOGNIZED == proto) {
                // Un-set in the message, treat it as null
                return null;
            } else if (Common.Rep.BIG_DECIMAL == proto) {
                // BIG_DECIMAL has to come back as a NUMBER
                return Rep.NUMBER;
            } else if (Common.Rep.NULL == proto) {
                return Rep.OBJECT;
            }
            return Rep.valueOf(proto.name());
        }

        /**
         * Computes the given JDBC type for a primitive to the corresponding {@link Rep} for the
         * equivalent Object type. If the provided type is not for a primitive, a {@link Rep} for
         * the provided Object is returned.
         *
         * @param type The type of a value (based on {@link java.sql.Types}).
         * @return The corresponding non-primitive {@link Rep} for the given {@code type}.
         */
        public static ColumnMetaData.Rep nonPrimitiveRepOf(SqlType type) {
            if (null == type) {
                throw new NullPointerException();
            }
            if (boolean.class == type.clazz) {
                return ColumnMetaData.Rep.BOOLEAN;
            } else if (byte.class == type.clazz) {
                return ColumnMetaData.Rep.BYTE;
            } else if (char.class == type.clazz) {
                return ColumnMetaData.Rep.CHARACTER;
            } else if (short.class == type.clazz) {
                return ColumnMetaData.Rep.SHORT;
            } else if (int.class == type.clazz) {
                return ColumnMetaData.Rep.INTEGER;
            } else if (long.class == type.clazz) {
                return ColumnMetaData.Rep.LONG;
            } else if (float.class == type.clazz) {
                return ColumnMetaData.Rep.FLOAT;
            } else if (double.class == type.clazz) {
                return ColumnMetaData.Rep.DOUBLE;
            }
            return ColumnMetaData.Rep.of(type.clazz);
        }

        /**
         * Computes the given JDBC type into the {@link Rep} for the wire (serial) form of that
         * type.
         *
         * @param type The type of a value (based on {@link java.sql.Types}).
         * @return The corresponding {@link Rep} for the serial form of the {@code type}.
         */
        public static ColumnMetaData.Rep serialRepOf(SqlType type) {
            if (null == type) {
                throw new NullPointerException();
            }
            if (boolean.class == type.internal) {
                return ColumnMetaData.Rep.BOOLEAN;
            } else if (byte.class == type.internal) {
                return ColumnMetaData.Rep.BYTE;
            } else if (char.class == type.internal) {
                return ColumnMetaData.Rep.CHARACTER;
            } else if (short.class == type.internal) {
                return ColumnMetaData.Rep.SHORT;
            } else if (int.class == type.internal) {
                return ColumnMetaData.Rep.INTEGER;
            } else if (long.class == type.internal) {
                return ColumnMetaData.Rep.LONG;
            } else if (float.class == type.internal) {
                return ColumnMetaData.Rep.FLOAT;
            } else if (double.class == type.internal) {
                return ColumnMetaData.Rep.DOUBLE;
            }
            return ColumnMetaData.Rep.of(type.internal);
        }
    }

    /** Base class for a column type. */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = ScalarType.class)
    @JsonSubTypes(
        {
            @JsonSubTypes.Type(value = ScalarType.class, name = "scalar"),
            @JsonSubTypes.Type(value = StructType.class, name = "struct"),
            @JsonSubTypes.Type(value = ArrayType.class, name = "array") }
    )
    public static class AvaticaType {
        public final int id;
        public final String name;

        /** The type of the field that holds the value. Not a JDBC property. */
        public final Rep rep;

        public AvaticaType(int id, String name, Rep rep) {
            this.id = id;
            this.name = Objects.requireNonNull(name);
            this.rep = Objects.requireNonNull(rep);
        }

        public String columnClassName() {
            // start-db modified start
            // SQL Data Type
            final Class<?> originCls = SqlType.valueOf(id).boxedClass();
            // Java Data Type
            final Class<?> deepCls = this.rep.clazz;
            return originCls == Object.class || Geometry.class.isAssignableFrom(deepCls)
                ? deepCls.getName()
                : originCls.getName();
            // start-db modified end
        }

        public String getName() {
            return name;
        }

        public AvaticaType setRep(Rep rep) {
            throw new UnsupportedOperationException();
        }

        public Common.AvaticaType toProto() {
            Common.AvaticaType.Builder builder = Common.AvaticaType.newBuilder();

            builder.setName(name);
            builder.setId(id);
            builder.setRep(rep.toProto());

            return builder.build();
        }

        public static AvaticaType fromProto(Common.AvaticaType proto) {
            Common.Rep repProto = proto.getRep();
            Rep rep = Rep.valueOf(repProto.name());
            AvaticaType type;

            if (proto.hasComponent()) {
                // ArrayType
                // recurse on the type for the array elements
                AvaticaType nestedType = AvaticaType.fromProto(proto.getComponent());
                type = ColumnMetaData.array(nestedType, proto.getName(), rep);
            } else if (proto.getColumnsCount() > 0) {
                // StructType
                List<ColumnMetaData> columns = new ArrayList<>(proto.getColumnsCount());
                for (Common.ColumnMetaData protoColumn : proto.getColumnsList()) {
                    columns.add(ColumnMetaData.fromProto(protoColumn));
                }
                type = ColumnMetaData.struct(columns);
            } else {
                // ScalarType
                type = ColumnMetaData.scalar(proto.getId(), proto.getName(), rep);
            }

            return type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, rep);
        }

        @Override
        public boolean equals(Object o) {
            return o == this
                || o instanceof AvaticaType
                    && id == ((AvaticaType) o).id
                    && Objects.equals(name, ((AvaticaType) o).name)
                    && rep == ((AvaticaType) o).rep;
        }
    }

    /** Scalar type. */
    public static class ScalarType extends AvaticaType {
        @JsonCreator
        public ScalarType(
            @JsonProperty("id") int id,
            @JsonProperty("name") String name,
            @JsonProperty("rep") Rep rep
        ) {
            super(id, name, rep);
        }

        @Override
        public AvaticaType setRep(Rep rep) {
            return new ScalarType(id, name, rep);
        }
    }

    /** Record type. */
    public static class StructType extends AvaticaType {
        public final List<ColumnMetaData> columns;

        @JsonCreator
        public StructType(List<ColumnMetaData> columns) {
            super(Types.STRUCT, "STRUCT", ColumnMetaData.Rep.OBJECT);
            this.columns = columns;
        }

        @Override
        public Common.AvaticaType toProto() {
            Common.AvaticaType.Builder builder = Common.AvaticaType.newBuilder(super.toProto());
            for (ColumnMetaData valueType : columns) {
                builder.addColumns(valueType.toProto());
            }
            return builder.build();
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, rep, columns);
        }

        @Override
        public boolean equals(Object o) {
            return o == this
                || o instanceof StructType
                    && super.equals(o)
                    && Objects.equals(columns, ((StructType) o).columns);
        }
    }

    /** Array type. */
    public static class ArrayType extends AvaticaType {
        private AvaticaType component;

        /**
         * Not for public use. Use {@link ColumnMetaData#array(AvaticaType, String, Rep)}.
         */
        @JsonCreator
        public ArrayType(
            @JsonProperty("type") int type,
            @JsonProperty("name") String typeName,
            @JsonProperty("rep") Rep representation,
            @JsonProperty("component") AvaticaType component
        ) {
            super(type, typeName, representation);
            this.component = component;
        }

        /**
         * Updates the component of {@code this} to the given value. This is necessary to provide as
         * accurate-as-possible of an {@code ArrayType} in the {@code Signature}. It cannot be done
         * at initial construction of this object.
         */
        public void updateComponentType(AvaticaType component) {
            this.component = Objects.requireNonNull(component);
        }

        public AvaticaType getComponent() {
            return component;
        }

        @Override
        public Common.AvaticaType toProto() {
            Common.AvaticaType.Builder builder = Common.AvaticaType.newBuilder(super.toProto());

            builder.setComponent(component.toProto());

            return builder.build();
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, rep, component);
        }

        @Override
        public boolean equals(Object o) {
            return o == this
                || o instanceof ArrayType
                    && super.equals(o)
                    && Objects.equals(component, ((ArrayType) o).component);
        }
    }
}

// End ColumnMetaData.java
