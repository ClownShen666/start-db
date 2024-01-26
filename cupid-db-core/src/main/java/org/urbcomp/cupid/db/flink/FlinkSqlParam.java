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
package org.urbcomp.cupid.db.flink;

import org.urbcomp.cupid.db.util.SqlParam;

public class FlinkSqlParam extends SqlParam {
    public static final ThreadLocal<FlinkSqlParam> CACHE = new ThreadLocal<>();

    private String host = null;
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

    private String bootstrapServers;

    private int testNum = 0;

    public FlinkSqlParam() {}

    public FlinkSqlParam(SqlParam sqlParam) {
        this.setUserName(sqlParam.getUserName());
        this.setDbName(sqlParam.getDbName());
        this.setExecuteEngine(sqlParam.getExecuteEngine());
        this.setSql(sqlParam.getSql());
        this.setLocal(sqlParam.isLocal());
        this.bootstrapServers = "localhost:9092";
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public int getTestNum() {
        return testNum;
    }

    public void setTestNum(int testNum) {
        this.testNum = testNum;
    }
}
