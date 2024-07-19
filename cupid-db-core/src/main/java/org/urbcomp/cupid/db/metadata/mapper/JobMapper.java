package org.urbcomp.cupid.db.metadata.mapper;

import org.apache.ibatis.annotations.Param;
import org.urbcomp.cupid.db.metadata.entity.Job;


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
