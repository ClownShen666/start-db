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
package org.urbcomp.cupid.db;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.urbcomp.cupid.db.util.MetadataUtil;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class kafkaConnector {
    public static String getKafkaTopic(org.urbcomp.cupid.db.metadata.entity.Table table) {
        return MetadataUtil.makeSchemaName(table.getId());
    }

    public static void KafkaCreateTopic(String ip, String topicName) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, ip);

        try (AdminClient adminClient = AdminClient.create(props)) {
            NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
            adminClient.createTopics(Collections.singletonList(newTopic));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void KafkaDeleteTopic(String ip, String topicName) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, ip);

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
