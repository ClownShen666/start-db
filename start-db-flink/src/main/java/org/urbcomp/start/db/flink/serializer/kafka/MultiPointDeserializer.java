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
package org.urbcomp.start.db.flink.serializer.kafka;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class MultiPointDeserializer implements DeserializationSchema {

    @Override
    public MultiPoint deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        String wkt = new String(bytes);
        WKTReader reader = new WKTReader();
        try {
            return (MultiPoint) reader.read(wkt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isEndOfStream(Object o) {
        return false;
    }

    @Override
    public TypeInformation<MultiPoint> getProducedType() {
        return TypeInformation.of(MultiPoint.class);
    }
}
