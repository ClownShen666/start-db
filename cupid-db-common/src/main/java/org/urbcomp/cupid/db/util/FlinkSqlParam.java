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
package org.urbcomp.cupid.db.util;

import java.util.List;

public class FlinkSqlParam extends SqlParam {

    public static final ThreadLocal<FlinkSqlParam> CACHE = new ThreadLocal<>();

    public static String BOOST_STRAP_SERVERS = "kafka:9093";
    private String jobId = null;
    private String host = null;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    private Integer port = null;
    private String jarFilesPath = null;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getJarFilesPath() {
        return jarFilesPath;
    }

    public void setJarFilesPath(String jarFilesPath) {
        this.jarFilesPath = jarFilesPath;
    }

    private String bootstrapServers = BOOST_STRAP_SERVERS;

    private boolean streamJoinDimension = false;

    private boolean insertStreamIntoDimension = false;

    private boolean hasUnion = false;

    private List<String> unionTables;

    private List<String> streamTables;

    private List<String> dimensionTables;

    private int testNum = 0;

    public FlinkSqlParam() {}

    public FlinkSqlParam(SqlParam sqlParam) {
        this.setUserName(sqlParam.getUserName());
        this.setDbName(sqlParam.getDbName());
        this.setExecuteEngine(sqlParam.getExecuteEngine());
        this.setSql(sqlParam.getSql());
        this.setLocal(sqlParam.isLocal());
        this.bootstrapServers = BOOST_STRAP_SERVERS;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public boolean isStreamJoinDimension() {
        return streamJoinDimension;
    }

    public void setStreamJoinDimension(boolean streamJoinDimension) {
        this.streamJoinDimension = streamJoinDimension;
    }

    public boolean isInsertStreamIntoDimension() {
        return insertStreamIntoDimension;
    }

    public void setInsertStreamIntoDimension(boolean insertStreamIntoDimension) {
        this.insertStreamIntoDimension = insertStreamIntoDimension;
    }

    public boolean isHasUnion() {
        return hasUnion;
    }

    public void setHasUnion(boolean hasUnion) {
        this.hasUnion = hasUnion;
    }

    public List<String> getUnionTables() {
        return unionTables;
    }

    public void setUnionTables(List<String> unionTables) {
        this.unionTables = unionTables;
    }

    public List<String> getStreamTables() {
        return streamTables;
    }

    public void setStreamTables(List<String> streamTables) {
        this.streamTables = streamTables;
    }

    public List<String> getDimensionTables() {
        return dimensionTables;
    }

    public void setDimensionTables(List<String> dimensionTables) {
        this.dimensionTables = dimensionTables;
    }

    public int getTestNum() {
        return testNum;
    }

    public void setTestNum(int testNum) {
        this.testNum = testNum;
    }
}
