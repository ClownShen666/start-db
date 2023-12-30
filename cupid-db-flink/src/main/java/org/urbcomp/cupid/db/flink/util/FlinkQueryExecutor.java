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

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.table.api.Table;
import org.apache.flink.types.Row;
import org.urbcomp.cupid.db.metadata.MetadataAccessUtil;
import org.urbcomp.cupid.db.metadata.MetadataAccessorFromDb;
import org.urbcomp.cupid.db.metadata.entity.Field;
import org.urbcomp.cupid.db.util.MetadataUtil;
import org.urbcomp.cupid.db.util.SqlParam;

import java.util.ArrayList;
import java.util.List;

public class FlinkQueryExecutor {
    public List<String> getKafkaTopic(FlinkSqlParam param) {
        if (param != null) {
            FlinkSqlParam.CACHE.set(param);
            SqlParam.CACHE.set(param);
        }
        List<String> topicList = new ArrayList<>();
        String sql = param.getSql();
        for (String dbTable : CupidFlinkTableExtractVisitor.getTableList(sql)) {
            String userName = FlinkSqlParam.CACHE.get().getUserName();
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

    public List<Table> loadTable(FlinkSqlParam param) {
        if (param != null) {
            FlinkSqlParam.CACHE.set(param);
            SqlParam.CACHE.set(param);
        }
        String sql = param.getSql();
        List<Table> tableList = new ArrayList<Table>();
        for (String dbTable : CupidFlinkTableExtractVisitor.getTableList(sql)) {
            String userName = FlinkSqlParam.CACHE.get().getUserName();
            String[] dbTableNames = dbTable.split("\\.");
            String dbName = dbTableNames[0];
            String tableName = dbTableNames[1];
            String catalogName = MetadataUtil.makeCatalog(userName, dbName);
            org.urbcomp.cupid.db.metadata.entity.Table table = MetadataAccessUtil.getTable(
                userName,
                dbName,
                tableName
            );
            if (table == null) {
                throw new IllegalArgumentException("Table Not Exists: " + dbTable);
            }
            String sft = MetadataUtil.makeSchemaName(table.getId());
            List<Field> fieldList;
            MetadataAccessorFromDb accessor = new MetadataAccessorFromDb();
            fieldList = accessor.getFields(userName, dbName, tableName);
            if (fieldList == null) {
                throw new IllegalArgumentException("Table Fields Are Null: " + dbTable);
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
                .setTopics(sft)
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

            // create sourceTable
            Table flinkTable = param.getTableEnv().fromDataStream(source);
            tableList.add(flinkTable);
        }
        return tableList;
    }

}
