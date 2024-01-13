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
package org.urbcomp.cupid.db.flink.util;

import org.apache.calcite.sql.SqlNode;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.functions.UserDefinedFunction;
import org.apache.flink.types.Row;
import org.urbcomp.cupid.db.metadata.MetadataAccessUtil;
import org.urbcomp.cupid.db.metadata.MetadataAccessorFromDb;
import org.urbcomp.cupid.db.metadata.entity.Field;
import org.urbcomp.cupid.db.parser.driver.CupidDBParseDriver;
import org.urbcomp.cupid.db.util.MetadataUtil;
import org.urbcomp.cupid.db.util.SqlParam;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FlinkQueryExecutor {

    public DataStream<Row> execute(FlinkSqlParam param) {
        if (param != null) {
            FlinkSqlParam.CACHE.set(param);
            SqlParam.CACHE.set(param);
        }
        String sql = param.getSql();
        SqlNode sqlNode = CupidDBParseDriver.parseSql(sql);

        switch (sqlNode.getKind()) {
            case SELECT:
                loadTable(param);
                registerUdf(param);
                Table resultTable = param.getTableEnv().sqlQuery(sql);
                return param.getTableEnv().toChangelogStream(resultTable);
            default:
                throw new UnsupportedOperationException(
                    "Unsupported sql kind: " + sqlNode.getKind()
                );
        }
    }

    public void loadTable(FlinkSqlParam param) {
        String sql = param.getSql();
        List<String> tableList = CupidFlinkTableExtractVisitor.getTableList(sql);
        List<String> dbTableList = CupidFlinkTableExtractVisitor.getDbTableList(sql);
        for (int i = 0; i < tableList.size(); i++) {
            String userName = FlinkSqlParam.CACHE.get().getUserName();
            String[] dbTableName = dbTableList.get(i).split("\\.");
            String dbName = dbTableName[0];
            String tableName = dbTableName[1];
            String catalogName = MetadataUtil.makeCatalog(userName, dbName);
            org.urbcomp.cupid.db.metadata.entity.Table table = MetadataAccessUtil.getTable(
                userName,
                dbName,
                tableName
            );
            if (table == null) {
                throw new IllegalArgumentException("Table Not Exists: " + dbTableName);
            }
            String topicName = MetadataUtil.makeSchemaName(table.getId());
            List<Field> fieldList;
            MetadataAccessorFromDb accessor = new MetadataAccessorFromDb();
            fieldList = accessor.getFields(userName, dbName, tableName);
            if (fieldList == null) {
                throw new IllegalArgumentException("Table Fields Are Null: " + dbTableName);
            }
            List<String> fieldTypeList = new ArrayList<>();
            List<String> fieldNameList = new ArrayList<>();
            for (Field field : fieldList) {
                fieldTypeList.add(field.getType());
                fieldNameList.add(field.getName());
            }
            // create kafkaSource
            KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers(param.getBootstrapServers())
                .setTopics(topicName)
                .setGroupId(catalogName)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

            // convert string stream to row stream
            StringToRow stringToRow = new StringToRow(fieldNameList, fieldTypeList);
            DataStream<Row> source = param.getEnv()
                .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "source")
                .map(stringToRow)
                .returns(stringToRow.getRowTypeInfo());

            // create sourceTable in tableEnv
            param.getTableEnv().createTemporaryView(tableList.get(i), source);
        }
    }

    public List<String> getKafkaTopic(FlinkSqlParam param) {
        if (param != null) {
            FlinkSqlParam.CACHE.set(param);
            SqlParam.CACHE.set(param);
        }
        List<String> topicList = new ArrayList<>();
        String sql = param.getSql();
        for (String dbTable : CupidFlinkTableExtractVisitor.getDbTableList(sql)) {
            String userName = param.getUserName();
            String[] dbTableNames = dbTable.split("\\.");
            String dbName = dbTableNames[0];
            String tableName = dbTableNames[1];
            org.urbcomp.cupid.db.metadata.entity.Table table = MetadataAccessUtil.getTable(
                userName,
                dbName,
                tableName
            );
            if (table == null) {
                throw new IllegalArgumentException("Table Not Exists: " + dbTable);
            }
            topicList.add(MetadataUtil.makeSchemaName(table.getId()));
        }
        return topicList;
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
        String udfPath = (Paths.get(path).getParent().toString() + "/udf").substring(5);
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
