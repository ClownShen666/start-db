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
package org.urbcomp.start.db.flink.udf.udt;

import org.apache.flink.table.annotation.DataTypeHint;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.urbcomp.start.db.flink.udf.Udt;
import org.urbcomp.start.db.flink.udf.util;

import java.io.IOException;

public class st_polygonAsWKT extends Udt {
    @DataTypeHint("String")
    public String eval(@DataTypeHint(value = "RAW", bridgedTo = Polygon.class) Geometry geometry)
        throws IOException {
        return util.asWKT(geometry);
    }

    @Override
    public String name() {
        return "st_polygonAsWKT";
    }
}
