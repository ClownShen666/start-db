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
package org.urbcomp.start.db.spark.reader;

import lombok.extern.slf4j.Slf4j;
import org.urbcomp.start.db.datatype.DataTypeField;
import org.urbcomp.start.db.infra.MetadataResult;
import org.urbcomp.start.db.spark.ISparkDataRead;
import org.urbcomp.start.db.spark.cache.ResultCacheFactory;

import java.util.List;

/**
 * 从本地缓存里读取spark写回的数据
 *
 * @author jimo
 **/
@Slf4j
public class SparkDataReadCache implements ISparkDataRead {

    @Override
    public <T> MetadataResult<T> read(String sqlId) {
        final List<DataTypeField> schema = ResultCacheFactory.getGlobalInstance().schema(sqlId);
        final List<Object[]> values = ResultCacheFactory.getGlobalInstance().values(sqlId);
        if (schema == null || values == null) {
            log.error("No Spark Cache Result:{},{}", schema, values);
            throw new RuntimeException("No Spark Cache Result");
        }
        return (MetadataResult<T>) MetadataResult.buildResult(
            schema.stream().map(DataTypeField::getName).toArray(String[]::new),
            values
        );
    }
}
