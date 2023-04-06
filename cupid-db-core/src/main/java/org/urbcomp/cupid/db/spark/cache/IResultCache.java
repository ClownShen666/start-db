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

import java.util.List;

/**
 * 缓存spark返回的数据的接口
 * <p>
 * 每个请求有唯一sqlId，该缓存的实现需要考虑很多请求的结果.
 * </p>
 *
 * @author jimo
 **/
public interface IResultCache {

    void addRow(String sqlId, Object[] row);

    void addSchema(String sqlId, List<DataTypeField> schema);

    List<DataTypeField> schema(String sqlId);

    List<Object[]> values(String sqlId);
}
