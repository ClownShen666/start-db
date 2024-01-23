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
package org.urbcomp.cupid.db.config;

/**
 * @author jimo
 **/
public enum ExecuteEngine {
    CALCITE("calcite"),
    /**
     * 表示在本地起一个local模式的spark执行，用于测试
     */
    SPARK_LOCAL("spark_local"),
    /**
     * 提交到远程spark执行
     */
    SPARK_CLUSTER("spark_cluster"),
    /**
     * 表示在本地起一个local模式的flink执行，用于测试
     */
    FLINK_LOCAL("flink_local"),
    /**
     * 提交到远程flink执行
     */
    FLINK_CLUSTER("flink_cluster");

    private String value;

    ExecuteEngine(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isSpark(ExecuteEngine engine) {
        return (engine == SPARK_LOCAL || engine == SPARK_CLUSTER);
    }
}
