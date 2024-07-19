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
package org.urbcomp.cupid.db.metadata.accessor;

import org.urbcomp.cupid.db.metadata.MetadataAccessUtil;
import org.urbcomp.cupid.db.metadata.entity.Job;
import org.urbcomp.cupid.db.metadata.mapper.JobMapper;

public class JobAccessor implements IAccessor<Job, JobMapper> {

    public long updateName(long id, String name) {
        return getMapper().updateName(id, name);
    }

    @Override
    public JobMapper getMapper() {
        return MetadataAccessUtil.getSqlSession().getMapper(JobMapper.class);
    }

    @Override
    public boolean isNotValid(Job job) {
        // This does not require validation
        return false;
    }
}
