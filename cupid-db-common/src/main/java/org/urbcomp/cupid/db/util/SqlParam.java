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

import org.urbcomp.cupid.db.config.ExecuteEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * SqlParams: username, DBName
 *
 * @author Wang Bohong
 * @date 2022-06-14
 */
public class SqlParam {

    public static final ThreadLocal<SqlParam> CACHE = new ThreadLocal<>();
    private static final Map<String, SqlParam> connectionParams = new HashMap<>();

    /**
     * username
     */
    private String userName;

    /**
     * db name
     */
    private String dbName;

    private String sql;

    private String sqlId;

    private ExecuteEngine executeEngine;

    private Map<String, String> options;

    private boolean isLocal = true;

    public SqlParam() {}

    public SqlParam(String userName, String dbName) {
        this(userName, dbName, ExecuteEngine.CALCITE, null);
    }

    public SqlParam(
        String userName,
        String dbName,
        ExecuteEngine executeEngine,
        Map<String, String> options
    ) {
        this.userName = userName;
        this.dbName = dbName;
        this.executeEngine = executeEngine;
        this.options = options;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public static void setParam(String connectionId, SqlParam param) {
        connectionParams.put(connectionId, param);
    }

    public static SqlParam getParam(String connectionId) {
        return connectionParams.get(connectionId);
    }

    public static void removeParam(String connectionId) {
        connectionParams.remove(connectionId);
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSqlId() {
        return sqlId;
    }

    public void setSqlId(String sqlId) {
        this.sqlId = sqlId;
    }

    public ExecuteEngine getExecuteEngine() {
        return executeEngine;
    }

    public void setExecuteEngine(ExecuteEngine executeEngine) {
        this.executeEngine = executeEngine;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public Map<String, String> getOptions() {
        return options == null ? new HashMap<>(4) : options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
