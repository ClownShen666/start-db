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
package org.urbcomp.cupid.db.flink.util;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.urbcomp.cupid.db.util.SqlParam;

public class FlinkSqlParam extends SqlParam {
    public static final ThreadLocal<FlinkSqlParam> CACHE = new ThreadLocal<>();

    private StreamExecutionEnvironment env;

    private StreamTableEnvironment tableEnv;

    private String bootstrapServers;

    public StreamExecutionEnvironment getEnv() {
        return env;
    }

    public void setEnv(StreamExecutionEnvironment env) {
        this.env = env;
    }

    public StreamTableEnvironment getTableEnv() {
        return tableEnv;
    }

    public void setTableEnv(StreamTableEnvironment tableEnv) {
        this.tableEnv = tableEnv;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }
}
