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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * resource util
 *
 * @author jimo
 * @date 2022/5/11 6:26
 **/
public class ResourceUtil {

    /**
     * read file in classpath or absolute path **stream should be closed by caller**
     *
     * @param path classpath or absolute path
     * @return input stream or throws IllegalArgumentException if not found
     */
    public static InputStream readResource(String path) {
        final InputStream resource = ResourceUtil.class.getClassLoader().getResourceAsStream(path);
        if (resource != null) {
            return resource;
        }
        final File file = new File(path);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("loading resource error：file doesn't exist: " + path, e);
            }
        }
        throw new IllegalArgumentException("can't find resource: " + path);
    }
}
