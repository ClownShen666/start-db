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
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.urbcomp.cupid.db.flink.serialize.kafka.GeometryDeserializer;
import org.urbcomp.cupid.db.flink.serialize.kafka.KafkaSerializer;
import org.urbcomp.cupid.db.flink.udf.st_geometryAsWKT;
import org.urbcomp.cupid.db.flink.udf.st_geometryFromWKT;

import java.util.ArrayList;
import java.util.List;

import static org.urbcomp.cupid.db.flink.util.kafkaConnector.*;

// run the "docker/flink-kafka" before test
public class ConnectWithKafkaTest {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

    @Ignore
    @Test
    public void datastreamApiTest() throws Exception {
        KafkaCreateTopic("localhost:9092", "Source");
        KafkaCreateTopic("localhost:9092", "Sink");
        List<String> recordList = new ArrayList<>();
        recordList.add("POINT (90 90)");
        recordList.add("LINESTRING (0 0, 1 1, 1 2)");
        recordList.add("POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))");
        recordList.add("MULTIPOINT ((3.5 5.6), (4.8 10.5))");
        recordList.add("MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))");
        recordList.add(
            "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))"
        );
        kafkaProducer("localhost:9092", "Source", recordList);

        KafkaSource<Geometry> source1 = KafkaSource.<Geometry>builder()
            .setBootstrapServers("localhost:9092")
            .setTopics("Source")
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new GeometryDeserializer())
            .build();
        DataStreamSource<Geometry> ds1 = env.fromSource(
            source1,
            WatermarkStrategy.noWatermarks(),
            "kafkaSource1",
            TypeInformation.of(Geometry.class)
        );
        tableEnv.createTemporaryView("kafkaSource1", tableEnv.fromDataStream(ds1));
        tableEnv.createTemporaryFunction("st_geometryAsWKT", st_geometryAsWKT.class);
        tableEnv.createTemporaryFunction("st_geometryFromWKT", st_geometryFromWKT.class);
        Table res1 = tableEnv.sqlQuery(
            "select st_geometryFromWKT(st_geometryAsWKT(f0)), f0 from kafkaSource1;"
        );

        TestUtil.checkF1EqualF2(tableEnv, res1, 6);

        KafkaSink<Geometry> sink = KafkaSink.<Geometry>builder()
            .setBootstrapServers("localhost:9092")
            .setRecordSerializer(
                KafkaRecordSerializationSchema.builder()
                    .setTopic("Sink")
                    .setValueSerializationSchema(new KafkaSerializer())
                    .build()
            )
            .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
            .build();

        DataStream sinkStream = tableEnv.toChangelogStream(res1);
        sinkStream.sinkTo(sink);

        KafkaSource<String> source2 = KafkaSource.<String>builder()
            .setBootstrapServers("localhost:9092")
            .setTopics("Sink")
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .build();
        DataStreamSource<String> ds2 = env.fromSource(
            source2,
            WatermarkStrategy.noWatermarks(),
            "kafkaSource2",
            TypeInformation.of(String.class)
        );
        tableEnv.createTemporaryView("kafkaSource2", tableEnv.fromDataStream(ds2));
        Table res2 = tableEnv.fromDataStream(ds2);

        TestUtil.checkTableNotNull(tableEnv, res2);

        KafkaDeleteTopic("localhost:9092", "Source");
        KafkaDeleteTopic("localhost:9092", "Sink");
    }

}
