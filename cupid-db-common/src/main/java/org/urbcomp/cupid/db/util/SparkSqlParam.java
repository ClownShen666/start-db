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

import lombok.ToString;
import org.urbcomp.cupid.db.config.DynamicConfig;
import org.urbcomp.cupid.db.model.data.DataExportType;

import java.util.Map;

/**
 * @author jimo
 **/
@ToString
public class SparkSqlParam extends SqlParam {

    public static final ThreadLocal<SparkSqlParam> CACHE = new ThreadLocal<>();

    public static final String SQL_ID_KEY = "sqlId";
    public static final String REMOTE_PORT_KEY = "port";
    public static final String REMOTE_HOST_KEY = "hostname";

    public SparkSqlParam() {
        this.hbaseZookeepers = DynamicConfig.getHBaseZookeepers();
    }

    public SparkSqlParam(SqlParam sqlParam) {
        this();
        this.setUserName(sqlParam.getUserName());
        this.setDbName(sqlParam.getDbName());
        this.setExecuteEngine(sqlParam.getExecuteEngine());
        this.setSql(sqlParam.getSql());
        final Map<String, String> options = sqlParam.getOptions();
        this.isLocal = Boolean.parseBoolean(options.getOrDefault("spark.local", "true"));
        this.async = Boolean.parseBoolean(options.getOrDefault("spark.async", "false"));
        this.exportType = DataExportType.valueOf(
            options.getOrDefault("spark.exportType", "local").toUpperCase()
        );
        this.redisHost = options.getOrDefault("spark.redis.host", null);
        this.redisPort = Integer.parseInt(options.getOrDefault("spark.redis.port", "0"));
        this.redisAuth = options.getOrDefault("spark.redis.auth", null);
    }

    private boolean isLocal;

    private String hbaseZookeepers;
    /**
     * 是否异步执行
     */
    private boolean async;

    private String remoteHost;
    private int remotePort;

    private DataExportType exportType;

    private String redisHost = null;
    private int redisPort;
    private String redisAuth = null;

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public String getHbaseZookeepers() {
        return hbaseZookeepers;
    }

    public void setHbaseZookeepers(String hbaseZookeepers) {
        this.hbaseZookeepers = hbaseZookeepers;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public DataExportType getExportType() {
        return exportType;
    }

    public void setExportType(DataExportType exportType) {
        this.exportType = exportType;
    }

    public String getRedisHost() {
        return this.redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public int getRedisPort() {
        return this.redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisAuth() {
        return this.redisAuth;
    }

    public void setRedisAuth(String redisAuth) {
        this.redisAuth = redisAuth;
    }
}
