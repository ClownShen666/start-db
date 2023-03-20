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
package org.urbcomp.cupid.db.spark.cache;

import org.urbcomp.cupid.db.datatype.DataTypeField;
import org.urbcomp.cupid.db.datatype.StructTypeJson;
import org.urbcomp.cupid.db.spark.data.GrpcRemote;

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
    public void addRow(GrpcRemote.RowRequest request) {
        final String sqlId = request.getSqlId();
        ROW_CACHE.computeIfAbsent(sqlId, s -> new ArrayList<>(8))
            .add(SparkDataDeserialize.deserialize(request.getData().toByteArray()));
    }

    @Override
    public void addSchema(GrpcRemote.SchemaRequest schemaRequest) {
        SCHEMA_CACHE.put(
            schemaRequest.getSqlId(),
            StructTypeJson.deserializeJson(schemaRequest.getSchemaJson())
        );
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
