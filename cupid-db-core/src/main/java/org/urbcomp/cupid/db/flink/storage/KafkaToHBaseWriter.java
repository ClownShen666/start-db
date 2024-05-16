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
package org.urbcomp.cupid.db.flink.storage;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.urbcomp.cupid.db.executor.utils.ExecutorUtil;
import org.urbcomp.cupid.db.metadata.MetadataAccessUtil;
import org.urbcomp.cupid.db.metadata.entity.Field;
import org.urbcomp.cupid.db.metadata.entity.Table;
import org.urbcomp.cupid.db.util.MetadataUtil;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KafkaToHBaseWriter {
    public final static String BATCH_TABLE_SUFFIX = "_batch";

    public final static String HBASE_KAFKA_GROUP_SUFFIX = "_hbase";

    private final static WKTReader reader = new WKTReader();

    public static void write(
        String userName,
        String dbName,
        String tableName,
        List<String> strRowList,
        List<String> fieldNameList,
        List<String> fieldTypeList
    ) {
        Table table = MetadataAccessUtil.getTable(userName, dbName, tableName);
        if (table == null) {
            throw new RuntimeException("There is no corresponding table!");
        }
        List<Field> fields = MetadataAccessUtil.getFields(userName, dbName, tableName);
        if (fields == null) throw new RuntimeException("There is no corresponding fields!");

        Map<String, String> params = ExecutorUtil.getDataStoreParams(userName, dbName);
        DataStore dataStore;
        try {
            dataStore = DataStoreFinder.getDataStore(params);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        String schemaName = MetadataUtil.makeSchemaName(table.getId());

        try (
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore
                .getFeatureWriterAppend(schemaName, Transaction.AUTO_COMMIT)
        ) {

            for (String strRow : strRowList) {
                SimpleFeature sf = writer.next();
                String[] columnList = strRow.substring(3, strRow.length() - 1).split(",,");
                for (int i = 0; i < columnList.length; i++) {
                    sf.setAttribute(
                        fieldNameList.get(i),
                        strToRelType(columnList[i], fieldTypeList.get(i))
                    );
                }
                writer.write();
            }

        } catch (IOException | ParseException e) {
            dataStore.dispose();
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        dataStore.dispose();

    }

    public static void write(
        String userName,
        String dbName,
        String tableName,
        ConsumerRecords<String, String> records,
        List<String> fieldNameList,
        List<String> fieldTypeList
    ) throws IOException, ParseException {
        if (records.count() == 0) return;
        List<String> strRowList = new ArrayList<>();

        for (ConsumerRecord<String, String> record : records) {
            strRowList.add(record.value());
        }
        write(userName, dbName, tableName, strRowList, fieldNameList, fieldTypeList);
    }

    public static <T> T strToRelType(String str, String filedType) throws ParseException {
        switch (filedType) {
            case "int":
                return (T) Integer.valueOf(str);
            case "Long":
                return (T) Long.valueOf(str);
            case "Float":
                return (T) Float.valueOf(str);
            case "Double":
                return (T) Double.valueOf(str);
            case "String":
                return (T) str;
            case "Boolean":
            case "Bool":
                return (T) Boolean.valueOf(str);
            case "Geometry":
                return (T) reader.read(str);
            case "Point":
                Point point = (Point) reader.read(str);
                return (T) point;
            case "LineString":
                LineString lineString = (LineString) reader.read(str);
                return (T) lineString;
            case "Polygon":
                Polygon polygon = (Polygon) reader.read(str);
                return (T) polygon;
            case "MultiPoint":
                MultiPoint multiPoint = (MultiPoint) reader.read(str);
                return (T) multiPoint;
            case "MultiLineString":
                MultiLineString multiLineString = (MultiLineString) reader.read(str);
                return (T) multiLineString;
            case "MultiPolygon":
                MultiPolygon multiPolygon = (MultiPolygon) reader.read(str);
                return (T) multiPolygon;
            default:
                throw new IllegalArgumentException("unexpected filedType " + filedType);
        }
    }
}
