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
package org.urbcomp.cupid.db.flink.udf;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class util {
    public static Geometry fromWKT(String wkt) throws ParseException {
        if (wkt == null) {
            return null;
        } else {
            WKTReader wktReader = new WKTReader();
            return wktReader.read(wkt);
        }
    }

    public static String asWKT(Geometry geometry) throws IOException {
        if (geometry == null) {
            return null;
        } else {
            WKTWriter wktWriter = new WKTWriter();
            StringWriter writer = new StringWriter();
            wktWriter.write(geometry, writer);
            return writer.toString();
        }
    }

    public static List<Class<?>> getClasses(String packageName) throws ClassNotFoundException,
        IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        List<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName)
        throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(
                    Class.forName(
                        packageName + '.' + file.getName().substring(0, file.getName().length() - 6)
                    )
                );
            }
        }

        return classes;
    }

    public static List<Class<?>> getClassesWithAnnotation(
        String basePackage,
        Class<? extends Annotation> annotation
    ) throws ClassNotFoundException, IOException {
        String path = basePackage.replace('.', '/');
        System.out.println(path);
        path = "/D:/st-lab/cupid-db/cupid-db-flink/target/classes/org/urbcomp/cupid/db/flink/udf";

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);

        List<File> dirs = Collections.list(resources)
            .stream()
            .map(url -> new File(url.getFile()))
            .collect(Collectors.toList());

        return dirs.stream().flatMap(directory -> {
            try {
                return findClassesWithAnnotation(directory, basePackage, annotation).stream();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    private static List<Class<?>> findClassesWithAnnotation(
        File directory,
        String basePackage,
        Class<? extends Annotation> annotation
    ) throws ClassNotFoundException {
        if (!directory.exists()) {
            return new ArrayList<>();
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(files).flatMap(file -> {
            if (file.isDirectory()) {
                try {
                    return findClassesWithAnnotation(
                        file,
                        basePackage + "." + file.getName(),
                        annotation
                    ).stream();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (file.getName().endsWith(".class")) {
                String className = basePackage + '.' + file.getName()
                    .substring(0, file.getName().length() - 6);
                try {
                    Class<?> clazz = Class.forName(className);
                    return clazz.isAnnotationPresent(annotation)
                        ? Stream.of(clazz)
                        : Stream.empty();
                } catch (ClassNotFoundException e) {
                    // Handle exception or log it
                    return Stream.empty();
                }
            } else {
                return Stream.empty();
            }
        }).collect(Collectors.toList());
    }
}
