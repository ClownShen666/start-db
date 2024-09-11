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
package org.urbcomp.start.db.datatype;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Map;

public class DataTypeFieldDeserializer extends StdDeserializer<DataTypeField> {

    public DataTypeFieldDeserializer() {
        this(null);
    }

    public DataTypeFieldDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public DataTypeField deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        DataTypeField dataTypeField = new DataTypeField();

        JsonNode root = jp.getCodec().readTree(jp);
        dataTypeField.setName(root.get("name").asText());
        dataTypeField.setNullable(root.get("nullable").booleanValue());
        Map<String, Object> metadata = mapper.readValue(
            root.get("metadata").toString(),
            new TypeReference<Map<String, Object>>() {
            }
        );
        dataTypeField.setMetadata(metadata);

        JsonNode typeNode = root.get("type");
        if (typeNode.isTextual()) {
            dataTypeField.setType(typeNode.asText());
        } else {
            dataTypeField.setType(mapper.readValue(typeNode.toString(), UserDefinedType.class));
        }
        return dataTypeField;
    }

}
