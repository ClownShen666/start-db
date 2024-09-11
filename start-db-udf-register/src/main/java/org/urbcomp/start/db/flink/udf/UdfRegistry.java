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
package org.urbcomp.start.db.flink.udf;

import org.reflections.Reflections;

import java.util.*;
import java.util.stream.Collectors;

public class UdfRegistry {
    public static class UdfInfo {
        private final String name;
        private final String type;
        private final Class<? extends IUdf> function;

        public String getName() {
            return name;
        }

        public UdfInfo(String name, String type, Class<? extends IUdf> function) {
            this.name = name;
            this.type = type;
            this.function = function;
        }

        public String getType() {
            return type;
        }

        public Class<? extends IUdf> getFunction() {
            return function;
        }
    }

    private final List<UdfInfo> udfInfoList;

    public UdfRegistry() {

        this.udfInfoList = getAllUdfInfoList();
    }

    public List<UdfInfo> getUdfInfoList() {
        return udfInfoList;
    }

    private List<UdfInfo> getUdfInfoListForClass(Class<? extends IUdf> udfClass) {
        return new Reflections("org.urbcomp.start.db.flink.udf").getSubTypesOf(udfClass)
            .stream()
            .map(clazz -> {
                try {
                    return new UdfInfo(
                        clazz.newInstance().name(),
                        clazz.getSuperclass().getSimpleName(),
                        clazz
                    );
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            })
            .collect(Collectors.toList());
    }

    private List<UdfInfo> getAllUdfInfoList() {
        List<UdfInfo> res = new ArrayList<>();

        res.addAll(getUdfInfoListForClass(Udt.class));
        res.addAll(getUdfInfoListForClass(StatefulUdf.class));

        return res;
    }

}
