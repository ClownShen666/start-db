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
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.catalog.ResolvedSchema;
import org.apache.flink.table.functions.UserDefinedFunction;
import org.apache.flink.table.types.DataType;
import org.apache.flink.types.Row;
import org.apache.kafka.common.TopicPartition;
import org.locationtech.jts.geom.*;
import org.urbcomp.cupid.db.config.ExecuteEngine;
import org.urbcomp.cupid.db.flink.cache.GlobalCache;
import org.urbcomp.cupid.db.flink.index.GridIndex;
import org.urbcomp.cupid.db.flink.kafka.ThreadLocalCleaner;
import org.urbcomp.cupid.db.flink.processfunction.JoinProcess;
import org.urbcomp.cupid.db.flink.visitor.*;
import org.urbcomp.cupid.db.flink.serializer.StringToRow;

import org.urbcomp.cupid.db.metadata.CalciteHelper;
import org.urbcomp.cupid.db.metadata.MetadataAccessUtil;
import org.urbcomp.cupid.db.metadata.MetadataAccessorFromDb;
import org.urbcomp.cupid.db.metadata.entity.Field;
import org.urbcomp.cupid.db.metadata.entity.Index;
import org.urbcomp.cupid.db.parser.driver.CupidDBParseDriver;
import org.urbcomp.cupid.db.udf.AbstractUdf;
import org.urbcomp.cupid.db.udf.DataEngine$;
import org.urbcomp.cupid.db.udf.UdfFactory;
import org.urbcomp.cupid.db.util.*;
import org.urbcomp.cupid.db.util.FlinkSqlParam;
import org.urbcomp.cupid.db.util.SqlParam;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

import org.slf4j.Logger;
import scala.collection.JavaConverters;

import static org.urbcomp.cupid.db.flink.connector.kafkaConnector.*;

public enum FlinkQueryExecutor {
    FLINK_EXECUTOR_INSTANCE;

    public static final ThreadLocal<SqlNode> sqlNodeCache = new ThreadLocal<>();
    private volatile boolean isRegistered = false;
    private final Logger logger = LogUtil.getLogger();
    private StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    private StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

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

    public DataStream<Row> execute(FlinkSqlParam param) {
        if (param != null) {
            FlinkSqlParam.CACHE.set(param);
            SqlParam.CACHE.set(param);
        } else {
            throw new IllegalArgumentException("FlinkSqlParam is null.");
        }

        StreamExecutionEnvironment preEnv = getEnv();

        if (!param.isLocal()) {
            if (param.getHost() == null
                || param.getPort() == null
                || param.getJarFilesPath() == null) {
                throw new IllegalArgumentException("Flink remote parameters are null.");
            }
            setEnv(
                StreamExecutionEnvironment.createRemoteEnvironment(
                    param.getHost(),
                    param.getPort(),
                    param.getJarFilesPath()
                )
            );
        }
        setTableEnv(StreamTableEnvironment.create(getEnv()));

        if (preEnv != getEnv()) {
            registerUdf();

        }

        String sql = new UdfVisitor(param.getSql()).getProcessedSql().replace(";", "");
        SqlNode sqlNode;
        try (ThreadLocalCleaner ignored = new ThreadLocalCleaner(sqlNodeCache)) {
            sqlNode = sqlNodeCache.get();
        }
        if (sqlNode == null) {
            sqlNode = CupidDBParseDriver.parseSql(sql);
        }
        switch (sqlNode.getKind()) {
            case SELECT:
                // stream join dimension table
                if (param.isStreamJoinDimension()) {

                    // todo: has union table
                    if (param.isHasUnion()) {
                        List<String> unionTables = param.getUnionTables();

                        // union table as stream table
                        FlinkSqlParam param1 = new FlinkSqlParam();
                        List<String> streamTables = param.getStreamTables();
                        streamTables.addAll(unionTables);
                        param1.setStreamTables(streamTables);
                        param1.setDimensionTables(param.getDimensionTables());
                        JoinVisitor joinVisitor1 = new JoinVisitor(sql, param1);

                        // union table as dimension table
                        FlinkSqlParam param2 = new FlinkSqlParam();
                        List<String> dimensionTables = param.getDimensionTables();
                        dimensionTables.addAll(unionTables);
                        param2.setDimensionTables(dimensionTables);
                        param1.setStreamTables(param.getStreamTables());
                        JoinVisitor joinVisitor2 = new JoinVisitor(sql, param2);

                        // todo: execute query twice and then union two result

                    } else {
                        SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(sql);
                        List<String> tableNameList = selectVisitor.getTableList();
                        List<String> dbTableNameList = selectVisitor.getDbTableList();
                        List<org.urbcomp.cupid.db.metadata.entity.Table> tableList = getTables(
                            dbTableNameList
                        );
                        loadTables(param, tableNameList, dbTableNameList, tableList);
                        JoinVisitor joinVisitor = new JoinVisitor(sql, param);

                        // get stream join result
                        DataStream<Row> streamJoin = getTableEnv().toChangelogStream(
                            getTableEnv().sqlQuery(joinVisitor.getStreamJoinSql())
                        );

                        // get stream join dimension result, parse mix sql in JoinProcess
                        streamJoin.process(new JoinProcess(sql));
                        return streamJoin;
                    }
                    return null;
                }

                // query stream or union table
                else {

                    // load select tables and register udf
                    SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(sql);
                    List<String> tableNameList = selectVisitor.getTableList();
                    List<String> dbTableNameList = selectVisitor.getDbTableList();
                    List<org.urbcomp.cupid.db.metadata.entity.Table> tableList = getTables(
                        dbTableNameList
                    );
                    DataStream<Row> resultStream;

                    // query union table
                    if (param.isHasUnion()) {
                        if (tableNameList.size() > 1) {
                            throw new UnsupportedOperationException("Unsupported sql: " + sql);
                        }
                        SelectFieldVisitor selectFieldVisitor = new SelectFieldVisitor(sql);
                        selectVisitor.getAllTables(param);
                        List<String> streamTableList = selectVisitor.getStreamTableList();
                        List<String> dimensionTableList = selectVisitor.getDimensionTableList();
                        List<Field> fieldList = MetadataAccessUtil.getFields(
                            param.getUserName(),
                            param.getDbName(),
                            tableNameList.get(0)
                        );
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Field field : fieldList) {
                            stringBuilder.append(field.getName()).append(",");
                        }
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                        sql = sql.replace("*", stringBuilder.toString());

                        // get dimension query sql
                        String dimensionSql = sql;
                        String unionTable = tableNameList.get(0);
                        for (int i = 1; i < dimensionTableList.size(); i++) {
                            dimensionSql = dimensionSql.concat(
                                " union " + sql.replace(unionTable, dimensionTableList.get(i))
                            );
                        }
                        if (selectVisitor.isUnionFromPointAndTrajectory()) {
                            List<String> trajectoryTables = selectVisitor.getTrajectoryTableList();
                            for (String trajectoryTable : trajectoryTables) {
                                dimensionSql = dimensionSql.replace(
                                    trajectoryTable,
                                    trajectoryTable + "_point"
                                );
                            }
                        }

                        // get stream query sql
                        String streamSql = sql;
                        for (int i = 1; i < streamTableList.size(); i++) {
                            streamSql = streamSql.concat(
                                " union " + sql.replace(unionTable, streamTableList.get(i))
                            );
                        }

                        // calicite
                        // get dimension query result
                        try (Connection connect = CalciteHelper.createConnection()) {
                            Statement stmt = connect.createStatement();
                            SqlParam.CACHE.get().setExecuteEngine(ExecuteEngine.CALCITE);
                            ResultSet resultSet = stmt.executeQuery(dimensionSql);
                            List<org.apache.flink.types.Row> flinkRowResult = new ArrayList<>();
                            ResultSetMetaData metaData = resultSet.getMetaData();
                            int columnCount = metaData.getColumnCount();

                            while (resultSet.next()) {
                                Row row = new Row(columnCount);
                                for (int i = 1; i <= columnCount; i++) {
                                    Object value = resultSet.getObject(i);
                                    row.setField(i - 1, value);
                                }
                                flinkRowResult.add(row);
                            }
                            if (flinkRowResult.isEmpty()) {
                                throw new IllegalArgumentException(
                                    "dimension query result is null"
                                );
                            }

                            // get union query result stream
                            getTableEnv().createTemporaryView(
                                "dimensionResult",
                                getEnv().fromCollection(flinkRowResult)
                                    .returns(
                                        getRowTypeInfo(
                                            selectFieldVisitor.getFieldNameList(),
                                            selectFieldVisitor.getFieldTypeList()
                                        )
                                    )
                            );
                            SelectFromTableVisitor selectFromTableVisitor =
                                new SelectFromTableVisitor(streamSql);
                            loadTables(
                                param,
                                selectFromTableVisitor.getTableList(),
                                selectFromTableVisitor.getDbTableList(),
                                getTables(selectFromTableVisitor.getDbTableList())
                            );
                            resultStream = getTableEnv().toChangelogStream(
                                getTableEnv().sqlQuery(
                                    "select * from dimensionResult union " + streamSql
                                )
                            );
                            writeQueryResultToKafka(
                                param,
                                resultStream,
                                selectFieldVisitor.getFieldNameList()
                            );

                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        // // spark
                        // // get dimension query result
                        // SparkSqlParam sparkSqlParam = new SparkSqlParam(SqlParam.CACHE.get());
                        // sparkSqlParam.setExportType(DataExportType.PRINT);
                        // sparkSqlParam.setLocal(true);
                        // List<org.apache.spark.sql.Row> sparkRowResult =
                        // SparkQueryExecutor.execute(
                        // sparkSqlParam,
                        // SparkQueryExecutor.getSparkSession(true, null)
                        // ).collectAsList();
                        //
                        // // convert sparkRow to flinkRow
                        // SparkRowToFlinkRow sparkRowToFlinkRow = new SparkRowToFlinkRow(
                        // selectFieldVisitor.getFieldNameList(),
                        // selectFieldVisitor.getFieldTypeList()
                        // );
                        // List<org.apache.flink.types.Row> flinkRowResult = sparkRowResult.stream()
                        // .map(sparkRowToFlinkRow)
                        // .collect(Collectors.toList());
                        //
                        // // get union query result stream
                        // getTableEnv().createTemporaryView(
                        // "dimensionResult",
                        // getEnv().fromCollection(flinkRowResult)
                        // .returns(getRowTypeInfo(selectFieldVisitor.getFieldNameList(),
                        // selectFieldVisitor.getFieldTypeList()))
                        // );
                        // resultStream = getTableEnv().toChangelogStream(
                        // getTableEnv().sqlQuery("select * from dimensionResult union " + sql)
                        // );
                    }

                    // flink
                    // query stream table
                    else {

                        // get stream query result
                        loadTables(param, tableNameList, dbTableNameList, tableList);
                        resultStream = getTableEnv().toChangelogStream(getTableEnv().sqlQuery(sql));
                        writeQueryResultToKafka(
                            param,
                            resultStream,
                            new SelectFieldVisitor(sql).getFieldNameList()
                        );
                    }

                    // execute and return result stream
                    try {
                        if (param.getTestNum() == 0) {
                            getEnv().execute();
                        } else {
                            resultStream.executeAndCollect(param.getTestNum());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return resultStream;
                }

            case INSERT:
                // todo: insert stream into dimension table
                if (param.isInsertStreamIntoDimension()) {

                } else {
                    InsertIntoTableVisitor insertVisitor = new InsertIntoTableVisitor(sql);
                    InsertIntoFieldVisitor fieldVisitor = new InsertIntoFieldVisitor(sql);

                    // only execute insert into table select ...
                    if (!insertVisitor.haveSelect()) {
                        throw new UnsupportedOperationException("Unsupported sql: " + sql);
                    }

                    // get select sql
                    String selectSql = "select " + sql.split(" (?i)select ")[1];
                    FlinkSqlParam flinkSqlParam = FlinkSqlParam.CACHE.get();
                    flinkSqlParam.setSql(selectSql);
                    sqlNodeCache.set(CupidDBParseDriver.parseSql(selectSql));
                    DataStream<Row> resultStream = FLINK_EXECUTOR_INSTANCE.execute(flinkSqlParam);

                    // write result to kafka
                    org.urbcomp.cupid.db.metadata.entity.Table insertTable = getTable(
                        insertVisitor.getDbTable()
                    );
                    checkInsert(
                        getTableEnv().fromDataStream(resultStream),
                        fieldVisitor.getFieldNameList(),
                        insertTable
                    );
                    insertTable(param, resultStream, insertTable);

                    // execute and return query result
                    try {
                        if (param.getTestNum() == 0) {
                            getEnv().execute();
                        } else {
                            resultStream.executeAndCollect(param.getTestNum());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return resultStream;
                }
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
        DataStream<Row> source = getEnv().fromSource(
            kafkaSource,
            WatermarkStrategy.noWatermarks(),
            "source"
        ).map(stringToRow).returns(getRowTypeInfo(fieldNameList, fieldTypeList));

        // create sourceTable in tableEnv
        getTableEnv().createTemporaryView(tableName, source);
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
            KafkaSource<String> kafkaSource;
            boolean isGridIndex = isGridIndex(param, tableNameList.get(i));
            String userDbTableKey = MetadataUtil.combineUserDbTableKey(
                param.getUserName(),
                param.getDbName(),
                tableList.get(i).getName()
            );                // 重庆市范围

            if (isGridIndex) {
                Map<TopicPartition, Long> offsets = new HashMap<>();
                // now default only 1 partition
                TopicPartition partition = new TopicPartition(topicName, 0);
                offsets.put(partition, GridIndex.tableQueryTimeOffset.get(userDbTableKey));

                kafkaSource = KafkaSource.<String>builder()
                    .setBootstrapServers(param.getBootstrapServers())
                    .setTopics(topicName)
                    .setGroupId(groupId)
                    .setStartingOffsets(OffsetsInitializer.offsets(offsets))
                    .setValueOnlyDeserializer(new SimpleStringSchema())
                    .build();
            } else {
                kafkaSource = KafkaSource.<String>builder()
                    .setBootstrapServers(param.getBootstrapServers())
                    .setTopics(topicName)
                    .setGroupId(groupId)
                    .setStartingOffsets(OffsetsInitializer.earliest())
                    .setValueOnlyDeserializer(new SimpleStringSchema())
                    .build();
            }

            // convert string stream to row stream
            List<Field> fieldList = tableList.get(i).getFieldList();
            List<String> fieldTypeList = new ArrayList<>();
            List<String> fieldNameList = new ArrayList<>();
            for (Field field : fieldList) {
                fieldTypeList.add(field.getType());
                fieldNameList.add(field.getName());
            }
            StringToRow stringToRow = new StringToRow(fieldNameList, fieldTypeList);

            List<String> filteredData = new ArrayList<>();

            if (isGridIndex) {

                GridIndex gridIndex = new GridIndex(
                    105.0,
                    110.0,
                    28.108,
                    32.20,
                    5000,
                    GlobalCache.pool.getResource()
                );
                filteredData = gridIndex.querySpatialObjects(
                    107.0,
                    30.108,
                    109.0,
                    31.20,
                    userDbTableKey
                );
            }
            DataStream<String> source = getEnv().fromSource(
                kafkaSource,
                WatermarkStrategy.noWatermarks(),
                "source"
            );

            DataStream<Row> rowSource;
            if (isGridIndex) {
                rowSource = source.union(getEnv().fromCollection(filteredData))
                    .map(stringToRow)
                    .returns(getRowTypeInfo(fieldNameList, fieldTypeList));
            } else {
                rowSource = source.map(stringToRow)
                    .returns(getRowTypeInfo(fieldNameList, fieldTypeList));
            }

            // create sourceTable in tableEnv
            getTableEnv().createTemporaryView(tableNameList.get(i), rowSource);
        }
    }

    public static boolean isGridIndex(FlinkSqlParam param, String tableName) {
        boolean isGridIndex = false;
        for (Index index : MetadataAccessUtil.getIndexes(
            param.getUserName(),
            param.getDbName(),
            tableName
        )) {
            if ("grid".equals(index.getIndexType())) {
                isGridIndex = true;
                break;
            }
        }
        return isGridIndex;
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

    public RowTypeInfo getRowTypeInfo(List<String> fieldNameList, List<String> fieldTypeList) {
        TypeInformation<?>[] fieldTypes = new TypeInformation<?>[fieldTypeList.size()];
        int len = fieldTypeList.size();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                switch (fieldTypeList.get(i)) {
                    case "Integer":
                    case "int":
                        fieldTypes[i] = Types.INT;
                        break;
                    case "Long":
                        fieldTypes[i] = Types.LONG;
                        break;
                    case "Float":
                        fieldTypes[i] = Types.FLOAT;
                        break;
                    case "Double":
                        fieldTypes[i] = Types.DOUBLE;
                        break;
                    case "string":
                    case "String":
                        fieldTypes[i] = Types.STRING;
                        break;
                    case "Boolean":
                    case "Bool":
                        fieldTypes[i] = Types.BOOLEAN;
                        break;
                    case "Geometry":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(Geometry.class);
                        break;
                    case "point":
                    case "Point":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(Point.class);
                        break;
                    case "LineString":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(LineString.class);
                        break;
                    case "Polygon":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(Polygon.class);
                        break;
                    case "MultiPoint":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(MultiPoint.class);
                        break;
                    case "MultiLineString":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(MultiLineString.class);
                        break;
                    case "MultiPolygon":
                        fieldTypes[i] = TypeExtractor.createTypeInfo(MultiPolygon.class);
                        break;
                    default:
                        throw new UnsupportedOperationException(
                            "Unsupported field type: " + fieldTypeList.get(i)
                        );
                }
            }
        }
        String[] fieldNames = fieldNameList.toArray(new String[0]);
        return new RowTypeInfo(fieldTypes, fieldNames);
    }

    public void registerUdf() {
        if (!isRegistered) {
            synchronized (this) {

                java.util.Map<String, Class<? extends AbstractUdf>> udfMap = JavaConverters
                    .mapAsJavaMapConverter(new UdfFactory().getUdfMap(DataEngine$.MODULE$.Flink()))
                    .asJava();
                for (Map.Entry<String, Class<? extends AbstractUdf>> udf : udfMap.entrySet()) {
                    getTableEnv().createTemporaryFunction(
                        udf.getKey(),
                        (Class<? extends UserDefinedFunction>) udf.getValue()
                    );
                    logger.info("Flink registers udf: " + udf.getKey());
                }
                isRegistered = true;

            }
        }

    }

    // process udf in the sql
    public String processSql(String sql) {
        return new UdfVisitor(sql).getProcessedSql();
    }

    public void writeQueryResultToKafka(
        FlinkSqlParam param,
        DataStream<Row> dataStream,
        List<String> fieldNameList
    ) {
        String topicName = getMD5(param.getUserName() + param.getSql());
        deleteKafkaTopic(param.getBootstrapServers(), topicName);
        createKafkaTopic(param.getBootstrapServers(), topicName);
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
        dataStream.map(row -> rowToJson(row, fieldNameList)).sinkTo(sink);
    }

    public String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String rowToJson(Row row, List<String> fieldNameList) {
        StringBuilder jsonBuilder = new StringBuilder("{");
        for (int i = 0; i < row.getArity(); i++) {
            jsonBuilder.append("\"")
                .append(fieldNameList.get(i))
                .append("\":\"")
                .append(row.getField(i))
                .append("\"");
            if (i < row.getArity() - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("}");
        return jsonBuilder.toString();
    }
}
