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

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.urbcomp.start.db.util.JacksonUtil;

import java.util.List;

/**
 * 反序列化StructType
 *
 * @author jimo
 **/
@Data
public class StructTypeJson {
    private String type;

    private List<DataTypeField> fields;

    public static List<DataTypeField> deserializeJson(String json) {
        try {
            final StructTypeJson struct = JacksonUtil.MAPPER.readValue(json, StructTypeJson.class);
            return struct.getFields();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
