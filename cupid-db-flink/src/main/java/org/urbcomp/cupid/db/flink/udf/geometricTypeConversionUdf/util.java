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
package org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

import java.io.IOException;
import java.io.StringWriter;

public class util {
    public static Geometry fromWKT(String wkt) throws ParseException {
        if (wkt == null) {
            return null;
        } else {
            WKTReader wktReader = new WKTReader();
            return wktReader.read(wkt);
        }
    }

    public static String asWKT(Geometry geometry) throws IOException {
        if (geometry == null) {
            return null;
        } else {
            WKTWriter wktWriter = new WKTWriter();
            StringWriter writer = new StringWriter();
            wktWriter.write(geometry, writer);
            return writer.toString();
        }
    }
}
