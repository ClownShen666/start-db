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
package org.urbcomp.start.db.config;

/**
 * @author jimo
 **/
public enum ExecuteEngine {
    /**
     * 默认执行引擎
     */
    CALCITE("calcite"),
    /**
     * 自动选择执行引擎
     */
    AUTO("auto"),
    /**
     * 提交给spark执行
     */
    SPARK("spark"),
    /**
     * 提交给flink执行
     */
    FLINK("flink");

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
        return (engine == SPARK || engine == AUTO);
    }

    public static boolean isFlink(ExecuteEngine engine) {
        return (engine == FLINK || engine == AUTO);
    }
}
