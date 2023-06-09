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
package org.urbcomp.cupid.db.datatype;

import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.urbcomp.cupid.db.model.trajectory.Trajectory;
import org.urbcomp.cupid.db.util.GeometryFactoryUtils;

/**
 * @author jimo
 **/
public class DataConvertFactory {

    /**
     * 将 val转换为 type类型的数据
     */
    public static Object convert(String val, DataTypeField type) throws Exception {
        switch (type.getType()) {
            case "integer":
            case "int":
                return Integer.parseInt(val);
            case "string":
                return val;
            case "timestamp":
                return java.sql.Timestamp.valueOf(val);
            case "multipoint":
                return (MultiPoint) new GeoJsonReader(GeometryFactoryUtils.defaultGeometryFactory())
                    .read(val);
            case "trajectory":
                return (Trajectory) Trajectory.fromGeoJSON(val);
            default:
                throw new NoClassDefFoundError("Unknown DataTypeField " + type);
        }
    }

    public static Class<?> strTypeToClass(String typeName) {
        switch (typeName) {
            case "integer":
            case "int":
                return int.class;
            case "string":
                return String.class;
            case "timestamp":
                return java.sql.Timestamp.class;
            case "multipoint":
                return MultiPoint.class;
            case "trajectory":
                return Trajectory.class;
            default:
                throw new NoClassDefFoundError("Unknown typeName " + typeName);
        }
    }
}
