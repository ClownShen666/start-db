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
package org.urbcomp.cupid.db.flink;

import org.apache.calcite.sql.SqlNode;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.catalog.ResolvedSchema;
import org.apache.flink.table.functions.UserDefinedFunction;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;
import org.urbcomp.cupid.db.metadata.MetadataAccessUtil;
import org.urbcomp.cupid.db.metadata.MetadataAccessorFromDb;
import org.urbcomp.cupid.db.metadata.entity.Field;
import org.urbcomp.cupid.db.parser.driver.CupidDBParseDriver;
import org.urbcomp.cupid.db.util.SqlParam;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.urbcomp.cupid.db.flink.kafkaConnector.getKafkaGroup;
import static org.urbcomp.cupid.db.flink.kafkaConnector.getKafkaTopic;

public class FlinkQueryExecutor {

    private SqlNode sqlNode = null;

    public FlinkQueryExecutor() {}

    public FlinkQueryExecutor(SqlNode sqlNode) {
        this.sqlNode = sqlNode;
    }

    public DataStream<Row> execute(FlinkSqlParam param) {
        if (param != null) {
            FlinkSqlParam.CACHE.set(param);
            SqlParam.CACHE.set(param);
        } else {
            throw new IllegalArgumentException("FlinkSqlParam is null.");
        }
        String sql = param.getSql();
        if (sqlNode == null) {
            sqlNode = CupidDBParseDriver.parseSql(sql);
        }
        switch (sqlNode.getKind()) {
            case SELECT:
                SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(sql);

                // load select tables and register udf
                List<String> tableNameList = selectVisitor.getTableList();
                List<String> dbTableNameList = selectVisitor.getDbTableList();
                List<org.urbcomp.cupid.db.metadata.entity.Table> tableList = getTables(
                    dbTableNameList
                );
                loadTables(param, tableNameList, dbTableNameList, tableList);
                registerUdf(param);

                // execute and return select result stream
                DataStream<Row> resultStream = param.getTableEnv()
                    .toChangelogStream(param.getTableEnv().sqlQuery(sql));
                try {
                    if (param.getTestNum() == 0) {
                        param.getEnv().execute();
                    }
                    // for test
                    else {
                        resultStream.executeAndCollect(param.getTestNum());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return resultStream;

            case INSERT:
                InsertIntoTableVisitor insertVisitor = new InsertIntoTableVisitor(sql);
                SelectFromTableVisitor selectVisitor2 = new SelectFromTableVisitor(sql);
                InsertIntoFieldVisitor fieldVisitor = new InsertIntoFieldVisitor(sql);

                // only execute insert into table select ...
                if (!insertVisitor.haveSelect()) {
                    throw new UnsupportedOperationException("Unsupported sql: " + sql);
                }

                // get select sql
                String selectSql = "select " + sql.split(" (?i)select ")[1];

                // load select tables and register udf
                List<String> tableNameList2 = selectVisitor2.getTableList();
                List<String> dbTableNameList2 = selectVisitor2.getDbTableList();
                List<org.urbcomp.cupid.db.metadata.entity.Table> tableList2 = getTables(
                    dbTableNameList2
                );
                loadTables(param, tableNameList2, dbTableNameList2, tableList2);
                registerUdf(param);

                // get select result
                Table resultTable = param.getTableEnv().sqlQuery(selectSql);
                DataStream<Row> resultStream2 = param.getTableEnv().toChangelogStream(resultTable);

                // write result to kafka
                org.urbcomp.cupid.db.metadata.entity.Table insertTable = getTable(
                    insertVisitor.getDbTable()
                );
                checkInsert(resultTable, fieldVisitor.getFieldNameList(), insertTable);
                insertTable(param, resultStream2, insertTable);

                // execute and return select result
                try {
                    if (param.getTestNum() == 0) {
                        param.getEnv().execute();
                    }
                    // for test
                    else {
                        resultStream2.executeAndCollect(param.getTestNum());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return resultStream2;

            default:
                throw new UnsupportedOperationException("Unsupported sql: " + sql);
        }
    }

    public void loadTable(
        FlinkSqlParam param,
        String tableName,
        String dbTableName,
        org.urbcomp.cupid.db.metadata.entity.Table table
    ) {
        // create kafkaSource
        String topicName = getKafkaTopic(table);
        String groupId = getKafkaGroup(dbTableName);
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
            .setBootstrapServers(param.getBootstrapServers())
            .setTopics(topicName)
            .setGroupId(groupId)
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .build();

        // convert string stream to row stream
        List<Field> fieldList = table.getFieldList();
        List<String> fieldTypeList = new ArrayList<>();
        List<String> fieldNameList = new ArrayList<>();
        for (Field field : fieldList) {
            fieldTypeList.add(field.getType());
            fieldNameList.add(field.getName());
        }
        StringToRow stringToRow = new StringToRow(fieldNameList, fieldTypeList);
        DataStream<Row> source = param.getEnv()
            .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "source")
            .map(stringToRow)
            .returns(stringToRow.getRowTypeInfo());

        // create sourceTable in tableEnv
        param.getTableEnv().createTemporaryView(tableName, source);
    }

    public void loadTables(
        FlinkSqlParam param,
        List<String> tableNameList,
        List<String> dbTableNameList,
        List<org.urbcomp.cupid.db.metadata.entity.Table> tableList
    ) {
        for (int i = 0; i < tableList.size(); i++) {
            // create kafkaSource
            String topicName = getKafkaTopic(tableList.get(i));
            String groupId = getKafkaGroup(dbTableNameList.get(i));
            KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers(param.getBootstrapServers())
                .setTopics(topicName)
                .setGroupId(groupId)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

            // convert string stream to row stream
            List<Field> fieldList = tableList.get(i).getFieldList();
            List<String> fieldTypeList = new ArrayList<>();
            List<String> fieldNameList = new ArrayList<>();
            for (Field field : fieldList) {
                fieldTypeList.add(field.getType());
                fieldNameList.add(field.getName());
            }
            StringToRow stringToRow = new StringToRow(fieldNameList, fieldTypeList);
            DataStream<Row> source = param.getEnv()
                .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "source")
                .map(stringToRow)
                .returns(stringToRow.getRowTypeInfo());
            // create sourceTable in tableEnv
            param.getTableEnv().createTemporaryView(tableNameList.get(i), source);
        }
    }

    public void checkInsert(
        Table resultTable,
        List<String> fieldNameList,
        org.urbcomp.cupid.db.metadata.entity.Table insertTable
    ) {
        ResolvedSchema schema = resultTable.getResolvedSchema();
        List<String> insertTableFieldTypeList = new ArrayList<>();
        if (fieldNameList != null) {
            for (String fieldName : fieldNameList) {
                for (Field field : insertTable.getFieldList()) {
                    if (fieldName.equals(field.getName())) {
                        insertTableFieldTypeList.add(field.getType());
                    }
                }
            }
        } else {
            for (Field field : insertTable.getFieldList()) {
                insertTableFieldTypeList.add(field.getType());
            }
        }
        List<DataType> resultTableFieldTypeList = new ArrayList<>(schema.getColumnDataTypes());

        for (int i = 0; i < resultTableFieldTypeList.size(); i++) {
            switch (resultTableFieldTypeList.get(i).toString()) {
                case "INT":
                    if (!insertTableFieldTypeList.get(i).equals("int")
                        && !insertTableFieldTypeList.get(i).equals("Integer")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: INT insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                case "BIGINT":
                    if (!insertTableFieldTypeList.get(i).equals("Long")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: BIGINT insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                case "FLOAT":
                    if (!insertTableFieldTypeList.get(i).equals("Float")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: FLOAT insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                case "DOUBLE":
                    if (!insertTableFieldTypeList.get(i).equals("Double")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: DOUBLE insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                case "STRING":
                    if (!insertTableFieldTypeList.get(i).equals("String")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: STRING insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                case "BOOLEAN":
                    if (!insertTableFieldTypeList.get(i).equals("Bool")
                        && !insertTableFieldTypeList.get(i).equals("Boolean")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: BOOLEAN insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                case "RAW('org.locationtech.jts.geom.Geometry', '...')":
                    if (!insertTableFieldTypeList.get(i).equals("Geometry")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: Geometry insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                case "RAW('org.locationtech.jts.geom.Point', '...')":
                    if (!insertTableFieldTypeList.get(i).equals("Point")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: Point insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                case "RAW('org.locationtech.jts.geom.LineString', '...')":
                    if (!insertTableFieldTypeList.get(i).equals("LineString")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: LineString insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                case "RAW('org.locationtech.jts.geom.Polygon', '...')":
                    if (!insertTableFieldTypeList.get(i).equals("Polygon")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: Polygon insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                case "RAW('org.locationtech.jts.geom.MultiPoint', '...')":
                    if (!insertTableFieldTypeList.get(i).equals("MultiPoint")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: MultiPoint insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                case "RAW('org.locationtech.jts.geom.MultiLineString', '...')":
                    if (!insertTableFieldTypeList.get(i).equals("MultiLineString")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: MultiLineString insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                case "RAW('org.locationtech.jts.geom.MultiPolygon', '...')":
                    if (!insertTableFieldTypeList.get(i).equals("MultiPolygon")) {
                        throw new IllegalArgumentException(
                            "insert field type doesn't match: MultiPolygon insert into "
                                + insertTableFieldTypeList.get(i)
                        );
                    }
                    break;
                default:
                    throw new UnsupportedOperationException(
                        "Unsupported insert field type: " + insertTableFieldTypeList.get(i)
                    );
            }
        }
    }

    public void insertTable(
        FlinkSqlParam param,
        DataStream<Row> resultStream,
        org.urbcomp.cupid.db.metadata.entity.Table table
    ) {
        // create kafkaSink
        String topicName = getKafkaTopic(table);
        KafkaSink<String> sink = KafkaSink.<String>builder()
            .setBootstrapServers(param.getBootstrapServers())
            .setRecordSerializer(
                KafkaRecordSerializationSchema.builder()
                    .setTopic(topicName)
                    .setValueSerializationSchema(new SimpleStringSchema())
                    .build()
            )
            .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
            .build();

        // convert row stream to formatted string stream
        DataStream<String> insertStream = resultStream.map(row -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < row.getArity(); i++) {
                if (i > 0) sb.append(",, ");
                sb.append(row.getField(i).toString());
            }
            return sb.toString();
        });

        // write insert stream to kafka
        insertStream.sinkTo(sink);
    }

    public static org.urbcomp.cupid.db.metadata.entity.Table getTable(String dbTable) {
        // read table metadata from mysql
        String userName = SqlParam.CACHE.get().getUserName();
        String[] dbTableName = dbTable.split("\\.");
        String dbName = dbTableName[0];
        String tableName = dbTableName[1];
        org.urbcomp.cupid.db.metadata.entity.Table table = MetadataAccessUtil.getTable(
            userName,
            dbName,
            tableName
        );
        if (table == null) {
            throw new IllegalArgumentException("Table Not Exists: " + tableName);
        }

        // read field metadata from mysql
        MetadataAccessorFromDb accessor = new MetadataAccessorFromDb();
        List<Field> fieldList = accessor.getFields(userName, dbName, tableName);
        if (fieldList == null) {
            throw new IllegalArgumentException("Table Fields Are Null: " + tableName);
        }
        table.setFieldList(fieldList);

        return table;
    }

    public static List<org.urbcomp.cupid.db.metadata.entity.Table> getTables(
        List<String> dbTableList
    ) {
        List<org.urbcomp.cupid.db.metadata.entity.Table> tableList = new ArrayList<>();
        for (String dbTable : dbTableList) {
            // read table metadata from mysql
            String userName = SqlParam.CACHE.get().getUserName();
            String[] dbTableName = dbTable.split("\\.");
            String dbName = dbTableName[0];
            String tableName = dbTableName[1];
            org.urbcomp.cupid.db.metadata.entity.Table table = MetadataAccessUtil.getTable(
                userName,
                dbName,
                tableName
            );
            if (table == null) {
                throw new IllegalArgumentException("Table Not Exists: " + tableName);
            }

            // read field metadata from mysql
            MetadataAccessorFromDb accessor = new MetadataAccessorFromDb();
            List<Field> fieldList = accessor.getFields(userName, dbName, tableName);
            if (fieldList == null) {
                throw new IllegalArgumentException("Table Fields Are Null: " + tableName);
            }
            table.setFieldList(fieldList);
            tableList.add(table);
        }
        return tableList;
    }

    public void registerUdf(FlinkSqlParam param) {
        List<Class<?>> udfClasses = loadUdfClasses(this.getClass().getResource("").toString());
        for (Class<?> udfClass : udfClasses) {
            param.getTableEnv()
                .createTemporaryFunction(
                    udfClass.getName().replace("org.urbcomp.cupid.db.flink.udf.", ""),
                    (Class<? extends UserDefinedFunction>) udfClass
                );
        }
    }

    public List<Class<?>> loadUdfClasses(String path) {
        String udfPath = (path.split("/target/")[0]
            + "/target/classes/org/urbcomp/cupid/db/flink/udf").substring(5);
        List<Class<?>> udfClasses = new ArrayList<>();
        File folder = new File(udfPath);
        File[] files = folder.listFiles(
            (dir, name) -> name.endsWith(".class") && !name.equals("util.class")
        );

        if (files != null) {
            for (File file : files) {
                try {
                    String className = "org.urbcomp.cupid.db.flink.udf."
                        + file.getName().replace(".class", "");
                    Class<?> loadedClass = Class.forName(className);
                    udfClasses.add(loadedClass);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return udfClasses;
    }
}
