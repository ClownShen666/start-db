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

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class util {
    public static void checkTableNotNull(StreamTableEnvironment tableEnv, Table sink)
        throws Exception {
        DataStream<Row> resultStream = tableEnv.toDataStream(sink, Row.class);
        List<Row> resultList = resultStream.executeAndCollect(1);
        Assert.assertNotEquals("result has no record", 0, resultList.size());
    }

    public static void checkF1EqualF2(StreamTableEnvironment tableEnv, Table sink)
        throws Exception {
        DataStream<Row> resultStream = tableEnv.toDataStream(sink, Row.class);
        List<Row> resultList = resultStream.executeAndCollect(10);
        Assert.assertNotEquals("result has no record", 0, resultList.size());

        for (Row row : resultList) {
            Assert.assertNotNull("field1 is null", row.getField(0));
            Assert.assertNotNull("field2 is null", row.getField(1));
            String field1 = row.getField(0).toString();
            String field2 = row.getField(1).toString();
            Assert.assertEquals("two fields are not equal", field1, field2);
        }
    }

    public static void checkF1EqualF2(StreamTableEnvironment tableEnv, Table sink, int maxRow)
        throws Exception {
        DataStream<Row> resultStream = tableEnv.toDataStream(sink, Row.class);
        List<Row> resultList = resultStream.executeAndCollect(maxRow);
        Assert.assertNotEquals("result has no record", 0, resultList.size());

        for (Row row : resultList) {
            Assert.assertNotNull("field1 is null", row.getField(0));
            Assert.assertNotNull("field2 is null", row.getField(1));
            String field1 = row.getField(0).toString();
            String field2 = row.getField(1).toString();
            Assert.assertEquals("two fields are not equal", field1, field2);
        }
    }

    public static void KafkaCreateTopic(String topicName) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(props)) {
            NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
            adminClient.createTopics(Collections.singletonList(newTopic));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void KafkaDeleteTopic(String topicName) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (AdminClient adminClient = AdminClient.create(props)) {
            adminClient.deleteTopics(Collections.singleton(topicName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void kafkaProducer(String ip, String topic, List<String> recordList) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", ip);
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        for (String str : recordList) {
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(
                topic,
                "forTest",
                str
            );
            try {
                RecordMetadata metadata = (RecordMetadata) producer.send(record).get();
                System.out.println(metadata.offset());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
