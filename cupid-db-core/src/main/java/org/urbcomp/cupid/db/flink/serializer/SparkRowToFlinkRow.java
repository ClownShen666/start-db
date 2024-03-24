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
package org.urbcomp.cupid.db.flink.serializer;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.types.Row;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.WKTReader;
import java.util.List;
import java.util.function.Function;

public class SparkRowToFlinkRow implements Function<org.apache.spark.sql.Row, Row> {
    private List<String> fieldNameList;

    private List<String> fieldTypeList;

    public SparkRowToFlinkRow(List<String> fieldNameList, List<String> fieldTypeList) {
        this.fieldNameList = fieldNameList;
        this.fieldTypeList = fieldTypeList;
    }

    @Override
    public Row apply(org.apache.spark.sql.Row sparkRow) {
        Row row = new Row(fieldTypeList.size());
        WKTReader reader = new WKTReader();
        try {
            for (int i = 0; i < sparkRow.size(); i++) {
                switch (fieldTypeList.get(i)) {
                    case "int":
                        row.setField(i, sparkRow.get(i));
                        break;
                    case "Long":
                        row.setField(i, sparkRow.get(i));
                        break;
                    case "Float":
                        row.setField(i, sparkRow.get(i));
                        break;
                    case "Double":
                        row.setField(i, sparkRow.get(i));
                        break;
                    case "String":
                        row.setField(i, sparkRow.get(i));
                        break;
                    case "Boolean":
                    case "Bool":
                        row.setField(i, sparkRow.get(i));
                        break;
                    case "Geometry":
                        Geometry geometry = (Geometry) sparkRow.get(i);
                        row.setField(i, geometry);
                        break;
                    case "Point":
                        Point point = (Point) sparkRow.get(i);
                        row.setField(i, point);
                        break;
                    case "LineString":
                        LineString lineString = (LineString) sparkRow.get(i);
                        row.setField(i, lineString);
                        break;
                    case "Polygon":
                        Polygon polygon = (Polygon) sparkRow.get(i);
                        row.setField(i, polygon);
                        break;
                    case "MultiPoint":
                        MultiPoint multiPoint = (MultiPoint) sparkRow.get(i);
                        row.setField(i, multiPoint);
                        break;
                    case "MultiLineString":
                        MultiLineString multiLineString = (MultiLineString) sparkRow.get(i);
                        row.setField(i, multiLineString);
                        break;
                    case "MultiPolygon":
                        MultiPolygon multiPolygon = (MultiPolygon) sparkRow.get(i);
                        row.setField(i, multiPolygon);
                        break;
                    default:
                        row.setField(i, sparkRow.get(i));
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    public RowTypeInfo getRowTypeInfo() {
        TypeInformation<?>[] fieldTypes = new TypeInformation<?>[fieldTypeList.size()];
        int len = fieldTypeList.size();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                switch (fieldTypeList.get(i)) {
                    case "Integer":
                    case "int":
                        fieldTypes[i] = Types.INT;
                        break;
                    case "Long":
                        fieldTypes[i] = Types.LONG;
                        break;
                    case "Float":
                        fieldTypes[i] = Types.FLOAT;
                        break;
                    case "Double":
                        fieldTypes[i] = Types.DOUBLE;
                        break;
                    case "String":
                        fieldTypes[i] = Types.STRING;
                        break;
                    case "Boolean":
                    case "Bool":
                        fieldTypes[i] = Types.BOOLEAN;
                        break;
                    case "Geometry":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(Geometry.class);
                        break;
                    case "Point":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(Point.class);
                        break;
                    case "LineString":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(LineString.class);
                        break;
                    case "Polygon":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(Polygon.class);
                        break;
                    case "MultiPoint":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(MultiPoint.class);
                        break;
                    case "MultiLineString":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(MultiLineString.class);
                        break;
                    case "MultiPolygon":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(MultiPolygon.class);
                        break;
                    default:
                        throw new UnsupportedOperationException(
                            "Unsupported field type: " + fieldTypeList.get(i)
                        );
                }
            }
        }
        String[] fieldNames = fieldNameList.toArray(new String[0]);
        return new RowTypeInfo(fieldTypes, fieldNames);
    }
}
