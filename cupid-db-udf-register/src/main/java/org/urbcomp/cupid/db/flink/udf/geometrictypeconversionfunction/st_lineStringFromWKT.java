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
package org.urbcomp.cupid.db.flink.udf.geometrictypeconversionfunction;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.ParseException;
import org.urbcomp.cupid.db.flink.udf.FlinkFunction;
import org.urbcomp.cupid.db.flink.udf.util;

@FlinkFunction
public class st_lineStringFromWKT extends ScalarFunction {
    @DataTypeHint(value = "RAW", bridgedTo = LineString.class)
    public LineString eval(@DataTypeHint("String") String wkt) throws ParseException {
        return (LineString) util.fromWKT(wkt);
    }
}
