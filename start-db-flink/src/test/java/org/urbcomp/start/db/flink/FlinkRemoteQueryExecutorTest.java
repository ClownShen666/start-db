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
package org.urbcomp.start.db.flink;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.*;
import org.urbcomp.start.db.config.ExecuteEngine;
import org.urbcomp.start.db.flink.visitor.SelectFromTableVisitor;
import org.urbcomp.start.db.metadata.CalciteHelper;
import org.urbcomp.start.db.util.FlinkSqlParam;
import org.urbcomp.start.db.util.SqlParam;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.urbcomp.start.db.flink.FlinkQueryExecutor.getTables;
import static org.urbcomp.start.db.flink.TestUtil.checkTable;
import static org.urbcomp.start.db.flink.connector.kafkaConnector.*;

// run the "docker/flink-kafka", "docker/local" and package start-db before test
// run the "docker/flink-kafka" and package start-db before test
public class FlinkRemoteQueryExecutorTest {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
    static SqlParam sqlParam = new SqlParam("root", "default", ExecuteEngine.FLINK, false);
    static FlinkSqlParam flinkSqlParam = new FlinkSqlParam(sqlParam);

    @BeforeClass
    public static void setParam() {
        SqlParam.CACHE.set(sqlParam);
        flinkSqlParam.setTestNum(1);
        flinkSqlParam.setLocal(false);
        flinkSqlParam.setHost("localhost");
        flinkSqlParam.setPort(8081);
        flinkSqlParam.setJarFilesPath(
            "target/start-db-flink-1.0.0-SNAPSHOT-jar-with-dependencies.jar"
        );
        flinkSqlParam.setBootstrapServers("kafka:9093");
        FlinkSqlParam.CACHE.set(flinkSqlParam);
    }

    @Ignore
    @Test
    public void selectall() throws Exception {
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate(
                "create stream table if not exists table1("
                    + "geometry1 Geometry,"
                    + "point1 Point,"
                    + "linestring1 LineString,"
                    + "polygon1 Polygon,"
                    + "multipoint1 MultiPoint,"
                    + "multilinestring1 MultiLineString,"
                    + "multipolygon1 MultiPolygon,"
                    + "SPATIAL INDEX indexName(geometry1))"
            );
            stmt.executeUpdate(
                "create stream table if not exists table2("
                    + "geometry1 Geometry,"
                    + "point1 Point,"
                    + "linestring1 LineString,"
                    + "polygon1 Polygon,"
                    + "multipoint1 MultiPoint,"
                    + "multilinestring1 MultiLineString,"
                    + "multipolygon1 MultiPolygon,"
                    + "SPATIAL INDEX indexName(geometry1))"
            );

            SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(
                "select * from table1 inner join table2;"
            );
            List<org.urbcomp.start.db.metadata.entity.Table> tableList = getTables(
                selectVisitor.getDbTableList()
            );
            List<String> topicList = new ArrayList<>();
            topicList.add(getKafkaTopic(tableList.get(0)));
            topicList.add(getKafkaTopic(tableList.get(1)));

            List<String> recordList = new ArrayList<>();
            recordList.add(
                "+I["
                    + "POINT (90 90),,"
                    + "POINT (90 90),,"
                    + "LINESTRING (0 0, 1 1, 1 2),,"
                    + "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11)),,"
                    + "MULTIPOINT ((3.5 5.6), (4.8 10.5)),,"
                    + "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4)),,"
                    + "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))]"

            );
            produceKafkaMessage("localhost:9092", topicList.get(0), recordList);

            // execute sql
            stmt.executeUpdate(
                "insert into table2 "
                    + "select "
                    + "st_geometryFromWKT(st_geometryAsWKT(geometry1)),"
                    + "st_pointFromWKT(st_pointAsWKT(point1)),"
                    + "st_lineStringFromWKT(st_lineStringAsWKT(linestring1)),"
                    + "st_polygonFromWKT(st_polygonAsWKT(polygon1)),"
                    + "st_multiPointFromWKT(st_multiPointAsWKT(multipoint1)),"
                    + "st_multiLineStringFromWKT(st_multiLineStringAsWKT(multilinestring1)),"
                    + "st_multiPolygonFromWKT(st_multiPolygonAsWKT(multipolygon1))"
                    + " from table1;"
            );

            // read target table in kafka
            KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics(topicList.get(1))
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
            DataStream<String> kafkaStream = env.fromSource(
                kafkaSource,
                WatermarkStrategy.noWatermarks(),
                "kafkaSource",
                TypeInformation.of(String.class)
            );

            List<String> expected = new ArrayList<>();
            expected.add(
                "POINT (90 90),, "
                    + "POINT (90 90),, "
                    + "LINESTRING (0 0, 1 1, 1 2),, "
                    + "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11)),, "
                    + "MULTIPOINT ((3.5 5.6), (4.8 10.5)),, "
                    + "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4)),, "
                    + "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))"
            );

            // check table
            checkTable(tableEnv, tableEnv.fromDataStream(kafkaStream), expected);
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
