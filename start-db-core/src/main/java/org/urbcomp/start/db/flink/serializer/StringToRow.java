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
package org.urbcomp.start.db.flink.serializer;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.types.Row;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;

import java.util.List;

import static org.apache.flink.types.RowKind.*;

public class StringToRow implements MapFunction<String, Row> {
    private final List<String> fieldNameList;

    private final List<String> fieldTypeList;

    public StringToRow(List<String> fieldNameList, List<String> fieldTypeList) {
        this.fieldNameList = fieldNameList;
        this.fieldTypeList = fieldTypeList;
    }

    @Override
    public Row map(String str) {
        try {
            int len = fieldTypeList.size();
            Row row = new Row(len);
            String rowStr = setRowKind(row, str);
            // kafka 字段分隔符
            String[] columnList = rowStr.split(",,");
            WKTReader reader = new WKTReader();
            for (int i = 0; i < len; i++) {
                // 将kafka消息的每个字段转成flink表格里一行的字段
                switch (fieldTypeList.get(i)) {
                    case "int":
                        row.setField(i, Integer.valueOf(columnList[i]));
                        break;
                    case "Long":
                        row.setField(i, Long.valueOf(columnList[i]));
                        break;
                    case "Float":
                        row.setField(i, Float.valueOf(columnList[i]));
                        break;
                    case "Double":
                        row.setField(i, Double.valueOf(columnList[i]));
                        break;
                    case "String":
                        row.setField(i, columnList[i]);
                        break;
                    case "Boolean":
                    case "Bool":
                        row.setField(i, Boolean.valueOf(columnList[i]));
                        break;
                    case "Geometry":
                        Geometry geometry = reader.read(columnList[i]);
                        row.setField(i, geometry);
                        break;
                    case "Point":
                        Point point = (Point) reader.read(columnList[i]);
                        row.setField(i, point);
                        break;
                    case "LineString":
                        LineString lineString = (LineString) reader.read(columnList[i]);
                        row.setField(i, lineString);
                        break;
                    case "Polygon":
                        Polygon polygon = (Polygon) reader.read(columnList[i]);
                        row.setField(i, polygon);
                        break;
                    case "MultiPoint":
                        MultiPoint multiPoint = (MultiPoint) reader.read(columnList[i]);
                        row.setField(i, multiPoint);
                        break;
                    case "MultiLineString":
                        MultiLineString multiLineString = (MultiLineString) reader.read(
                            columnList[i]
                        );
                        row.setField(i, multiLineString);
                        break;
                    case "MultiPolygon":
                        MultiPolygon multiPolygon = (MultiPolygon) reader.read(columnList[i]);
                        row.setField(i, multiPolygon);
                        break;
                    default:
                        row.setField(i, columnList[i]);
                        break;
                }
            }
            return row;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String setRowKind(Row row, String str) {
        String rowStr = str;
        if (str.startsWith("+I[") && str.endsWith("]")) {
            row.setKind(INSERT);
            rowStr = str.substring(3, str.length() - 1);
        } else if (str.startsWith("-U[") && str.endsWith("]")) {
            row.setKind(UPDATE_BEFORE);
            rowStr = str.substring(3, str.length() - 1);
        } else if (str.startsWith("+U[") && str.endsWith("]")) {
            row.setKind(UPDATE_AFTER);
            rowStr = str.substring(3, str.length() - 1);
        } else if (str.startsWith("-D[") && str.endsWith("]")) {
            row.setKind(DELETE);
            rowStr = str.substring(3, str.length() - 1);
        }
        return rowStr;
    }

}
