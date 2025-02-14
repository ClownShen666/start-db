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
package org.urbcomp.start.db.metadata.mapper;

import org.apache.ibatis.annotations.Param;
import org.urbcomp.start.db.metadata.entity.Field;

/**
 * This interface is used to encapsulate some table mapping information.
 * 
 * @author Wang Bohong, Xiang He
 * @Date 2022-05-20 17:32:15
 */
public interface FieldMapper extends IMapper<Field> {

    /**
     * insert one field instance into sys_field table
     * 
     * @param field field instance
     * @return number of affected rows
     */
    @Override
    long insert(@Param("field") Field field);

    /**
     * insert one field instance into sys_field table
     * 
     * @param field field instance
     * @return number of affected rows
     */
    @Override
    long update(@Param("field") Field field);

    @Override
    long deleteByFid(@Param("fid") long fid);

}
