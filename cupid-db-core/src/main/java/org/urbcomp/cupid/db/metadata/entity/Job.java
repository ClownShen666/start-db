package org.urbcomp.cupid.db.metadata.entity;


/**
 * This class is used to encapsulate the basic information of job
 */
public class Job extends AbstractEntity{

    private long userId;
    public Job(long id, String name, long userId, String sql) {
        super(id, name);
        this.userId = userId;
        this.sql = sql;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    private String sql;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    private long startTime;
    private long duration;

    private long endTime;

    private int state;



}
