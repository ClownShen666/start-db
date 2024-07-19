package org.urbcomp.cupid.db.metadata.accessor;

import org.urbcomp.cupid.db.metadata.MetadataAccessUtil;
import org.urbcomp.cupid.db.metadata.entity.Job;
import org.urbcomp.cupid.db.metadata.mapper.JobMapper;

public class JobAccessor implements IAccessor<Job, JobMapper> {

    public long updateName(long id,String name) {
        return getMapper().updateName(id, name);
    }

    @Override
    public JobMapper getMapper() {
        return MetadataAccessUtil.getSqlSession().getMapper(JobMapper.class);
    }

    @Override
    public boolean isNotValid(Job job) {
        //This does not require validation
        return false;
    }
}
