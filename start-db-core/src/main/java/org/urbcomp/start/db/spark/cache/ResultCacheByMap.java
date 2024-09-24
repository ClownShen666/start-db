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
package org.urbcomp.start.db.spark.cache;

import org.urbcomp.start.db.datatype.DataTypeField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * implement by hashmap
 *
 * @author jimo
 **/
public class ResultCacheByMap implements IResultCache {

    private final static Map<String, List<DataTypeField>> SCHEMA_CACHE = new ConcurrentHashMap<>();
    private final static Map<String, List<Object[]>> ROW_CACHE = new ConcurrentHashMap<>();

    @Override
    public void addRow(String sqlId, Object[] row) {
        ROW_CACHE.get(sqlId).add(row);
    }

    @Override
    public void addSchema(String sqlId, List<DataTypeField> schema) {
        SCHEMA_CACHE.put(sqlId, schema);
        ROW_CACHE.put(sqlId, new ArrayList<>(8));
    }

    @Override
    public List<DataTypeField> schema(String sqlId) {
        return SCHEMA_CACHE.get(sqlId);
    }

    @Override
    public List<Object[]> values(String sqlId) {
        return ROW_CACHE.get(sqlId);
    }
}
