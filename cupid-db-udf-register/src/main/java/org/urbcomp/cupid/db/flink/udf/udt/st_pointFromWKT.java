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
package org.urbcomp.cupid.db.flink.udf.udt;

import org.apache.flink.table.annotation.DataTypeHint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.urbcomp.cupid.db.flink.udf.Udt;
import org.urbcomp.cupid.db.flink.udf.util;

public class st_pointFromWKT extends Udt {
    @DataTypeHint(value = "RAW", bridgedTo = Point.class)
    public Point eval(@DataTypeHint("String") String wkt) throws ParseException {
        return (Point) util.fromWKT(wkt);
    }

    @Override
    public String name() {
        return "st_pointFromWKT";
    }
}
