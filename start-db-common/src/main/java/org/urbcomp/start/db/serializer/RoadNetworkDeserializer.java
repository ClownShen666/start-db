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
package org.urbcomp.start.db.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.urbcomp.start.db.model.roadnetwork.RoadNetwork;

import java.io.IOException;

public class RoadNetworkDeserializer extends StdDeserializer<RoadNetwork> {
    protected RoadNetworkDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public RoadNetwork deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return RoadNetwork.fromGeoJSON(p.getValueAsString());
    }
}
