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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class PolygonSerializer extends Serializer<Polygon> implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Polygon.class);

    @Override
    public Polygon read(Kryo kryo, Input input, Class aClass) {
        String wkt = input.readString();
        WKTReader reader = new WKTReader();
        try {
            return (Polygon) reader.read(wkt);
        } catch (ParseException e) {
            logger.error("Parse failed while serializing Polygon");
        }
        return null;
    }

    @Override
    public void write(Kryo kryo, Output output, Polygon polygon) {
        output.writeString(polygon.toString());
    }
}
