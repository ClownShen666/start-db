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

/**
 * @author jimo
 **/
public class SparkSqlParam extends SqlParam {

    public static final ThreadLocal<SparkSqlParam> CACHE = new ThreadLocal<>();

    public static final String SQL_ID_KEY = "sqlId";
    public static final String REMOTE_PORT_KEY = "port";
    public static final String REMOTE_HOST_KEY = "hostname";

    private String hbaseZookeepers;
    /**
     * 是否异步执行
     */
    private boolean async;

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
}
