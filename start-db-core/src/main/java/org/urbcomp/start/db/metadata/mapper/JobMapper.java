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
import org.urbcomp.start.db.metadata.entity.Job;

public interface JobMapper extends IMapper<Job> {
    /**
     * insert a job instance into sys_job table
     *
     * @param job table instance
     * @return number of affected rows
     */
    @Override
    long insert(@Param("job") Job job);

    /**
     * update a job instance into sys_job table
     *
     * @param job table instance
     * @return number of affected rows
     */
    @Override
    long update(@Param("job") Job job);

    long updateName(@Param("id") long id, @Param("name") String name);

}
