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
package org.urbcomp.cupid.db.datatype;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Map;

/**
 * 抽象的数据类型
 *
 * @author jimo
 **/
@Data
@JsonDeserialize(using = DataTypeFieldDeserializer.class)
public class DataTypeField {

    private String name;
    /**
     * integer, string...
     */
    private Object type;
    private boolean nullable;

    private Map<String, Object> metadata;

    public DataTypeField() {}

    public DataTypeField(String name, Object type, boolean nullable, Map<String, Object> metadata) {
        this.name = name;
        this.type = type;
        this.nullable = nullable;
        this.metadata = metadata;
    }
}
