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
package org.urbcomp.cupid.db.tools;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.locationtech.geomesa.utils.geotools.SimpleFeatureTypes;
import org.opengis.feature.simple.SimpleFeatureType;
import org.urbcomp.cupid.db.util.MetadataUtil;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitibikeDataUtils {

    public static final String TEST_TABLE_NAME = "citibike";

    public static DataStore getStore(String userName, String dbName) {
        try {
            Map<String, String> params = new HashMap<>();
            String CATALOG = MetadataUtil.makeCatalog(userName, dbName);
            params.put("hbase.catalog", CATALOG);
            params.put("hbase.zookeepers", "localhost:2181");
            return DataStoreFinder.getDataStore(params);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void prepareTable(DataStore store) {

        List<String> cols = Arrays.asList(
            "idx:Integer",
            "ride_id:String",
            "rideable_type:String",
            // "started_at:Timestamp",
            // "ended_at:Timestamp",
            // "start_station_name:String",
            // "start_station_id:Double",
            "start_point:Point:srid=4326"
            // "end_station_name:String",
            // "end_station_id:Double",
            // "end_point:Point:srid=4326",
            // "track:LineString:srid=4326",
            // "member_casual:String"
        );

        try {
            store.removeSchema(TEST_TABLE_NAME);
            SimpleFeatureType sft = SimpleFeatureTypes.createType(
                TEST_TABLE_NAME,
                String.join(",", cols)
            );
            store.createSchema(sft);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void dropTable(DataStore store) {
        try {
            store.removeSchema(TEST_TABLE_NAME);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String getProjectRoot() {
        Path path = Paths.get("").toAbsolutePath();
        while (!path.endsWith("cupid-db")) {
            path = path.getParent();
        }
        return path.normalize().toString();
    }
}
