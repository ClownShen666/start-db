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
package org.urbcomp.start.serializer.serializers;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.urbcomp.start.db.model.trajectory.Trajectory;
import org.urbcomp.start.serializer.StartBinarySerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * TODO optimize implementation
 */
public class TrajectoryBS implements StartBinarySerializer<Trajectory> {

    @Override
    public void write(OutputStream os, Trajectory trajectory) {
        try {
            String json = trajectory.toGeoJSON();
            Output output = new Output(os);
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            output.writeInt(bytes.length);
            output.writeBytes(bytes);
            output.flush();
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serializer roadSegment");
        }
    }

    @Override
    public Trajectory read(InputStream is, Class<? extends Trajectory> type) {
        try {
            Input input = new Input(is);
            int len = input.readInt();
            byte[] bytes = input.readBytes(len);
            return Trajectory.fromGeoJSON(new String(bytes, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to deserializer RoadSegment");
        }
    }
}
