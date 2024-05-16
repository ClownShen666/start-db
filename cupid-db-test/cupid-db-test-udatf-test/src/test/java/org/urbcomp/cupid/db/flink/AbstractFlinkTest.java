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

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.BeforeClass;
import org.urbcomp.cupid.db.config.ExecuteEngine;
import org.urbcomp.cupid.db.flink.visitor.SelectFromTableVisitor;
import org.urbcomp.cupid.db.util.FlinkSqlParam;
import org.urbcomp.cupid.db.util.SqlParam;

import java.util.List;

public abstract class AbstractFlinkTest {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
    static SqlParam sqlParam = new SqlParam("root", "default", ExecuteEngine.FLINK, true);
    static FlinkSqlParam flinkSqlParam = new FlinkSqlParam(sqlParam);

    @BeforeClass
    public static void setParam() {
        SqlParam.CACHE.set(sqlParam);
        flinkSqlParam.setTestNum(1);
        FlinkSqlParam.CACHE.set(flinkSqlParam);
    }

    public DataStream<String> getDataStream(String topic) {
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
            .setBootstrapServers("localhost:9092")
            .setTopics(topic)
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .build();
        return env.fromSource(
            kafkaSource,
            WatermarkStrategy.noWatermarks(),
            "kafkaSource",
            TypeInformation.of(String.class)
        );
    }

    public static org.apache.flink.table.api.Table getFlinkQueryTable(
        SelectFromTableVisitor selectVisitor,
        List<org.urbcomp.cupid.db.metadata.entity.Table> tableList,
        String sql
    ) {
        FlinkQueryExecutor flink = FlinkQueryExecutor.FLINK_EXECUTOR_INSTANCE;
        flink.loadTables(
            flinkSqlParam,
            selectVisitor.getTableList(),
            selectVisitor.getDbTableList(),
            tableList
        );
        return flink.getTableEnv().sqlQuery(sql);

    }

}
