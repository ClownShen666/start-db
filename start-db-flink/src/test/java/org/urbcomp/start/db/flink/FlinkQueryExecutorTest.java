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
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.junit.*;
import org.urbcomp.start.db.config.ExecuteEngine;

import org.urbcomp.start.db.flink.processfunction.JoinProcess;
import org.urbcomp.start.db.flink.serializer.StringToRow;
import org.urbcomp.start.db.flink.visitor.SelectFromTableVisitor;
import org.urbcomp.start.db.metadata.CalciteHelper;
import org.urbcomp.start.db.metadata.MetadataAccessUtil;
import org.urbcomp.start.db.model.sample.ModelGenerator;
import org.urbcomp.start.db.model.trajectory.Trajectory;
import org.urbcomp.start.db.util.FlinkSqlParam;
import org.urbcomp.start.db.util.SqlParam;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.urbcomp.start.db.flink.FlinkQueryExecutor.getTables;
import static org.urbcomp.start.db.flink.TestUtil.*;
import static org.urbcomp.start.db.flink.connector.kafkaConnector.*;

// run the "docker/flink-kafka", "docker/local" and package start-db before test
public class FlinkQueryExecutorTest {

    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
    static SqlParam sqlParam = new SqlParam("root", "default", ExecuteEngine.AUTO, true);
    static FlinkSqlParam flinkSqlParam = new FlinkSqlParam(sqlParam);
    FlinkQueryExecutor flinkQueryExecutor = new FlinkQueryExecutor();

    @BeforeClass
    public static void setParam() {
        SqlParam.CACHE.set(sqlParam);
        flinkSqlParam.setTestNum(1);
        FlinkSqlParam.CACHE.set(flinkSqlParam);
    }

    @Ignore
    @Test
    public void remoteSqlTest() throws Exception {
        flinkSqlParam.setLocal(false);
        flinkSqlParam.setHost("localhost");
        flinkSqlParam.setPort(8081);
        flinkSqlParam.setJarFilesPath("start-db-flink-1.0.0-SNAPSHOT-jar-with-dependencies.jar");
        flinkSqlParam.setBootstrapServers("localhost:9092");
        FlinkSqlParam.CACHE.set(flinkSqlParam);
        streamSelectSqlTest();
        // streamInsertSqlTest();
        // streamDropTableSqlTest();
    }

    // Error response from daemon: network-scoped alias is supported only for containers in user
    // defined networks
    @Ignore
    @Test
    public void writeQueryResultToKafkaTest() {
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();

            // create table
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("create stream table if not exists table1(idd int, point1 Point);");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate("create stream table if not exists table2(idd int, point1 Point);");

            // produce message in kafka
            SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(
                "select * from table1, table2;"
            );
            List<org.urbcomp.start.db.metadata.entity.Table> tableList = getTables(
                selectVisitor.getDbTableList()
            );
            List<String> topicList = new ArrayList<>();
            topicList.add(getKafkaTopic(tableList.get(0)));
            topicList.add(getKafkaTopic(tableList.get(1)));
            List<String> recordList = new ArrayList<>();
            recordList.add("+I[1,,POINT (90 90)]");
            produceKafkaMessage("localhost:9092", topicList.get(0), recordList);
            recordList.clear();
            recordList.add("+I[2,,POINT (45 45)]");
            produceKafkaMessage("localhost:9092", topicList.get(1), recordList);

            // read target table in kafka
            String topicName = flinkQueryExecutor.getMD5(
                flinkSqlParam.getUserName() + "select * from table1, table2"
            );

            KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics(topicName)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
            DataStream<String> resultStream = env.fromSource(
                kafkaSource,
                WatermarkStrategy.noWatermarks(),
                "kafkaSource",
                TypeInformation.of(String.class)
            );

            // test
            stmt.executeQuery("select * from table1, table2");
            List<String> expected = new ArrayList<>();
            expected.add(
                "{\"idd\":\"1\",\"point1\":\"POINT (90 90)\",\"idd\":\"2\",\"point1\":\"POINT (45 45)\"}"
            );
            checkTable(tableEnv, tableEnv.fromDataStream(resultStream), expected);

            // drop table
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Ignore
    @Test
    public void unionSelectSqlTest() throws Exception {
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();

            // create table
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("create table if not exists table1(idd int);");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate("create stream table if not exists table2(idd int);");
            stmt.executeUpdate("drop table if exists table3");
            stmt.executeUpdate(
                "create union table if not exists table3(idd int) from table1, table2;"
            );
            stmt.executeUpdate("insert into table1 values(1)");
            stmt.executeQuery("insert into table2 select idd from table3");

            // read target table in kafka
            SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(
                "select * from table2;"
            );
            List<org.urbcomp.start.db.metadata.entity.Table> tableList = getTables(
                selectVisitor.getDbTableList()
            );
            KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics(getKafkaTopic(tableList.get(0)))
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
            DataStream<String> insertStream = env.fromSource(
                kafkaSource,
                WatermarkStrategy.noWatermarks(),
                "kafkaSource",
                TypeInformation.of(String.class)
            );

            // test insert result
            List<String> expected = new ArrayList<>();
            expected.add("1");
            checkTable(tableEnv, tableEnv.fromDataStream(insertStream), expected);

            // drop table
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate("drop table if exists table3");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Ignore
    @Test
    public void unionFromPointAndTrajectoryTest() throws Exception {
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();

            // create table and insert
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("create table if not exists table1(idd int, tt Trajectory);");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate("create stream table if not exists table2(idd int, pp Point);");
            Trajectory trajectory = ModelGenerator.generateTrajectory();
            String tGeo = trajectory.toGeoJSON();
            stmt.execute("INSERT INTO table1 values (1, st_traj_fromGeoJSON(\'" + tGeo + "\'))");
            stmt.executeUpdate("drop table if exists table3");
            stmt.executeUpdate(
                "create union table if not exists table3(idd int, pp Point) from table1, table2;"
            );
            stmt.executeUpdate("insert into table2 select * from table3");

            // read target table in kafka
            SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(
                "select * from table2;"
            );
            List<org.urbcomp.start.db.metadata.entity.Table> tableList = getTables(
                selectVisitor.getDbTableList()
            );
            KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics(getKafkaTopic(tableList.get(0)))
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
            DataStream<String> insertStream = env.fromSource(
                kafkaSource,
                WatermarkStrategy.noWatermarks(),
                "kafkaSource",
                TypeInformation.of(String.class)
            );

            // test insert result
            checkTableNotNull(tableEnv, tableEnv.fromDataStream(insertStream));

            // drop table
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate("drop table if exists table3");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Ignore
    @Test
    public void pointTableUpdateTest() throws Exception {
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();

            // create table and insert
            stmt.executeUpdate("drop table if exists table1_point");
            stmt.executeUpdate("create table if not exists table1_point(idd int, pp Point);");
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("create table if not exists table1(idd int, tt Trajectory);");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate("create stream table if not exists table2(idd int, pp Point);");
            Trajectory trajectory = ModelGenerator.generateTrajectory();
            String tGeo = trajectory.toGeoJSON();
            stmt.execute("INSERT INTO table1 values (1, st_traj_fromGeoJSON(\'" + tGeo + "\'))");
            stmt.executeUpdate("drop table if exists table3");
            stmt.executeUpdate(
                "create union table if not exists table3(idd int, pp Point) from table1, table2;"
            );

            stmt.executeUpdate("insert into table2 select * from table3");

            // read target table in kafka
            SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(
                "select * from table2;"
            );
            List<org.urbcomp.start.db.metadata.entity.Table> tableList = getTables(
                selectVisitor.getDbTableList()
            );
            KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics(getKafkaTopic(tableList.get(0)))
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
            DataStream<String> insertStream = env.fromSource(
                kafkaSource,
                WatermarkStrategy.noWatermarks(),
                "kafkaSource",
                TypeInformation.of(String.class)
            );

            // test insert result
            checkTableNotNull(tableEnv, tableEnv.fromDataStream(insertStream));

            // drop table
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate("drop table if exists table3");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Ignore
    @Test
    public void parseSqlTest() {
        // // create table
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("create table if not exists table1(id1 int);");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate("create table if not exists table2(id2 int);");
            stmt.executeUpdate("drop table if exists table3");
            stmt.executeUpdate("create stream table if not exists table3(id3 int);");
            stmt.executeQuery(
                // "select table1.id1, table2.id2 as i2, log(table3.id3, table2.id2) as logid from
                // table1, table2, table3, table4;"
                // "select * from table3, table2 join table1 on table1.id1 = table3.id3;"
                "insert into table1 select *, table1.id1 as t1 from table1 left join table2 on table1.id1 = table2.id2 right join table3 on table1.id1 = table3.id3 and table2.id2 = table3.id3 where table1.id1 < table2.id2 and table2.id2 > table3.id3;"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Ignore
    @Test
    public void streamDropTableSqlTest() {
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
            Set<String> topicsBefore = showTopics("localhost:9092");
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
                    + "SPATIAL INDEX indexName(geometry1))"
            );
            stmt.executeUpdate("drop table table1");
            stmt.executeUpdate("drop table table2");
            Set<String> topicsAfter = showTopics("localhost:9092");

            assertEquals(topicsBefore, topicsAfter);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void joinSqlExecutorTest() {

        env.setParallelism(1);

        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate("drop table if exists table3");

            stmt.executeUpdate(
                "create stream table if not exists table1("
                    + "idx int,"
                    + "geometry1 Geometry,"
                    + "point1 Point,"
                    + "linestring1 LineString)"
            );

            stmt.executeUpdate(
                "create table if not exists table2(idx int, ride_id string, start_point point);"
            );
            stmt.executeUpdate("create table if not exists table3(idx int, end_point point);");

            stmt.execute(
                "Insert into table2 (idx, ride_id, start_point) values (1, '05608CC867EBDF63'"
                    + ", st_makePoint(2.1, 2)), (2, 'aaaaaaaaaaa', st_makePoint(4.1, 2))"
                    + ", (2, '05608CC867EBDF63', st_makePoint(10.1, 10))"
            );
            stmt.execute(
                "Insert into table2 (idx, ride_id, start_point) values (4, '05608CC86f7EBDF3', st_makePoint(2.2, 2)), (5, '05608CC867EBDF63', st_makePoint(4.1, 2))"
            );
            stmt.execute("Insert into table3 (idx, end_point) values (3, st_makePoint(100, 50.1))");

            String joinSql =
                "select table1.idx, table2.ride_id, table2.start_point, table3.end_point from table1"
                    + " left join table2 on table1.idx = table2.idx left join table3 on table1.idx = table3.idx";
            stmt.executeQuery(joinSql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Ignore
    @Test
    public void join3TableSqlTest() {
        env.setParallelism(1);

        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate("drop table if exists table3");

            stmt.executeUpdate(
                "create stream table if not exists table1("
                    + "idx int,"
                    + "geometry1 Geometry,"
                    + "point1 Point,"
                    + "linestring1 LineString)"
            );

            stmt.executeUpdate(
                "create table if not exists table2(idx int, ride_id string, start_point point);"
            );
            stmt.executeUpdate("create table if not exists table3(idx int, end_point point);");

            stmt.execute(
                "Insert into table2 (idx, ride_id, start_point) values (1, '05608CC867EBDF63'"
                    + ", st_makePoint(2.1, 2)), (2, 'aaaaaaaaaaa', st_makePoint(4.1, 2))"
                    + ", (2, '05608CC867EBDF63', st_makePoint(10.1, 10))"
            );
            stmt.execute(
                "Insert into table2 (idx, ride_id, start_point) values (4, '05608CC86f7EBDF3', st_makePoint(2.2, 2)), (5, '05608CC867EBDF63', st_makePoint(4.1, 2))"
            );
            stmt.execute("Insert into table3 (idx, end_point) values (3, st_makePoint(100, 50.1))");

            String joinSql =
                "select table1.idx, table2.ride_id, table2.start_point, table3.end_point from table1"
                    + " left join table2 on table1.idx = table2.idx left join table3 on table1.idx = table3.idx";

            // get topic names
            SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(joinSql);
            List<org.urbcomp.start.db.metadata.entity.Table> tableList = getTables(
                selectVisitor.getDbTableList()
            );

            List<String> topicList = new ArrayList<>();
            topicList.add(getKafkaTopic(tableList.get(0)));
            topicList.add(getKafkaTopic(tableList.get(1)));

            // produce message
            List<String> recordList = new ArrayList<>();
            recordList.add(
                "+I["
                    + "1,,"
                    + "POINT (90 90),,"
                    + "POINT (90 90),,"
                    + "LINESTRING (0 0, 1 1, 1 2)]"

            );
            recordList.add(
                "+I["
                    + "2,,"
                    + "POINT (90 90),,"
                    + "POINT (90 90),,"
                    + "LINESTRING (0 0, 1 1, 1 2)]"

            );
            recordList.add(
                "+I["
                    + "3,,"
                    + "POINT (90 90),,"
                    + "POINT (90 90),,"
                    + "LINESTRING (0 0, 1 1, 1 2)]"

            );
            produceKafkaMessage("localhost:9092", topicList.get(0), recordList);
            // read target table in kafka
            KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics(topicList.get(0))
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();

            DataStream<Row> joinStream = env.fromSource(
                kafkaSource,
                WatermarkStrategy.noWatermarks(),
                "kafkaSource",
                TypeInformation.of(String.class)
            )
                .map(
                    new StringToRow(
                        getNameList("table1", sqlParam),
                        getTypeList("table1", sqlParam)
                    )
                )
                .process(new JoinProcess(joinSql));

            List<String> expected = new ArrayList<>();
            expected.add("1, 05608CC867EBDF63, POINT (2.1 2), null");
            expected.add("2, aaaaaaaaaaa, POINT (4.1 2), null");
            expected.add("2, 05608CC867EBDF63, POINT (10.1 10), null");
            expected.add("3, null, null, POINT (100 50.1)");

            checkTable2(tableEnv, joinStream, expected);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Ignore
    @Test
    public void joinSqlTest() {
        env.setParallelism(1);

        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");

            stmt.executeUpdate(
                "create stream table if not exists table1("
                    + "idx int,"
                    + "geometry1 Geometry,"
                    + "point1 Point,"
                    + "linestring1 LineString)"
            );

            stmt.executeUpdate(
                "create table if not exists table2(idx int, ride_id string, start_point point);"
            );
            stmt.execute(
                "Insert into table2 (idx, ride_id, start_point) values (1, '05608CC867EBDF63'"
                    + ", st_makePoint(2.1, 2)), (2, 'aaaaaaaaaaa', st_makePoint(4.1, 2))"
                    + ", (2, '05608CC867EBDF63', st_makePoint(10.1, 10))"
            );
            stmt.execute(
                "Insert into table2 (idx, ride_id, start_point) values (4, '05608CC86f7EBDF3', st_makePoint(2.2, 2)), (5, '05608CC867EBDF63', st_makePoint(4.1, 2))"
            );

            String joinSql =
                "select table1.idx, table2.ride_id, table2.start_point from table1 left join table2 on table1.idx = table2.idx";

            // get topic names
            SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(joinSql);
            List<org.urbcomp.start.db.metadata.entity.Table> tableList = getTables(
                selectVisitor.getDbTableList()
            );
            List<String> topicList = new ArrayList<>();
            topicList.add(getKafkaTopic(tableList.get(0)));
            topicList.add(getKafkaTopic(tableList.get(1)));

            // produce message
            List<String> recordList = new ArrayList<>();
            recordList.add(
                "+I["
                    + "1,,"
                    + "POINT (90 90),,"
                    + "POINT (90 90),,"
                    + "LINESTRING (0 0, 1 1, 1 2)]"

            );
            recordList.add(
                "+I["
                    + "2,,"
                    + "POINT (90 90),,"
                    + "POINT (90 90),,"
                    + "LINESTRING (0 0, 1 1, 1 2)]"

            );
            recordList.add(
                "+I["
                    + "3,,"
                    + "POINT (90 90),,"
                    + "POINT (90 90),,"
                    + "LINESTRING (0 0, 1 1, 1 2)]"

            );
            produceKafkaMessage("localhost:9092", topicList.get(0), recordList);
            // read target table in kafka
            KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics(topicList.get(0))
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
            DataStream<Row> joinStream = env.fromSource(
                kafkaSource,
                WatermarkStrategy.noWatermarks(),
                "kafkaSource",
                TypeInformation.of(String.class)
            )
                .map(
                    new StringToRow(
                        getNameList("table1", sqlParam),
                        getTypeList("table1", sqlParam)
                    )
                )
                .process(new JoinProcess(joinSql));

            List<String> expected = new ArrayList<>();
            expected.add("1, " + "05608CC867EBDF63, " + "POINT (2.1 2)");
            expected.add("2, " + "aaaaaaaaaaa, " + "POINT (4.1 2)");
            expected.add("2, " + "05608CC867EBDF63, " + "POINT (10.1 10)");
            expected.add("3, " + "null, " + "null");

            checkTable2(tableEnv, joinStream, expected);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Ignore
    @Test
    public void streamSelectSqlTest() {
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table1");
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

            // get topic
            SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(
                "select * from table1;"
            );
            List<org.urbcomp.start.db.metadata.entity.Table> tableList = getTables(
                selectVisitor.getDbTableList()
            );
            List<String> topicList = new ArrayList<>();
            topicList.add(getKafkaTopic(tableList.get(0)));
            // service kafka declares mutually exclusive `network_mode` and `networks`: invalid
            // compose project
            // produce message
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
            // produceKafkaMessage("localhost:9092", topicList.get(0), recordList);
            String userName = SqlParam.CACHE.get().getUserName();

            System.out.println(
                MetadataAccessUtil.getTable(userName, "default", "table1").toString()
            );
            // test
            stmt.executeQuery("select * from table1");
            produceKafkaMessage("localhost:9092", topicList.get(0), recordList);

            // delete topic
            stmt.executeUpdate("drop table if exists table1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Ignore
    @Test
    public void streamInsertSqlTest() throws Exception {
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate(
                "create stream table if not exists table1("
                    + "int1 int,"
                    + "long1 Long,"
                    + "float1 Float,"
                    + "double1 Double,"
                    + "string1 String,"
                    + "bool1 Bool,"
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
                    + "int1 int,"
                    + "long1 Long,"
                    + "float1 Float,"
                    + "double1 Double,"
                    + "string1 String,"
                    + "bool1 Bool,"
                    + "geometry1 Geometry,"
                    + "point1 Point,"
                    + "linestring1 LineString,"
                    + "polygon1 Polygon,"
                    + "multipoint1 MultiPoint,"
                    + "multilinestring1 MultiLineString,"
                    + "multipolygon1 MultiPolygon,"
                    + "SPATIAL INDEX indexName(geometry1))"
            );

            // get topic names
            SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(
                "select * from table1, table2;"
            );
            List<org.urbcomp.start.db.metadata.entity.Table> tableList = getTables(
                selectVisitor.getDbTableList()
            );
            List<String> topicList = new ArrayList<>();
            topicList.add(getKafkaTopic(tableList.get(0)));
            topicList.add(getKafkaTopic(tableList.get(1)));

            // produce message
            List<String> recordList = new ArrayList<>();
            recordList.add(
                "+I["
                    + "1,,"
                    + "1,,"
                    + "1.0,,"
                    + "1.0,,"
                    + "start-db,,"
                    + "true,,"
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
            stmt.executeUpdate("insert into table2 select * from table1;");

            // read target table in kafka
            KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics(topicList.get(1))
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
            DataStream<String> insertStream = env.fromSource(
                kafkaSource,
                WatermarkStrategy.noWatermarks(),
                "kafkaSource",
                TypeInformation.of(String.class)
            );

            // test insert result
            List<String> expected = new ArrayList<>();
            expected.add(
                "1,, "
                    + "1,, "
                    + "1.0,, "
                    + "1.0,, "
                    + "start-db,, "
                    + "true,, "
                    + "POINT (90 90),, "
                    + "POINT (90 90),, "
                    + "LINESTRING (0 0, 1 1, 1 2),, "
                    + "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11)),, "
                    + "MULTIPOINT ((3.5 5.6), (4.8 10.5)),, "
                    + "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4)),, "
                    + "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))"
            );
            checkTable(tableEnv, tableEnv.fromDataStream(insertStream), expected);

            // delete topic
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Ignore
    @Test
    public void selectTest() throws Exception {
        // create table
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate(
                "create table if not exists table1("
                    + "geometry1 Geometry,"
                    + "point1 Point,"
                    + "linestring1 LineString,"
                    + "polygon1 Polygon,"
                    + "multipoint1 MultiPoint,"
                    + "multilinestring1 MultiLineString,"
                    + "multipolygon1 MultiPolygon,"
                    + "SPATIAL INDEX indexName(geometry1))"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // get topic names
        SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor("select * from table1;");
        List<org.urbcomp.start.db.metadata.entity.Table> tableList = getTables(
            selectVisitor.getDbTableList()
        );
        List<String> topicList = new ArrayList<>();
        topicList.add(getKafkaTopic(tableList.get(0)));

        // create topic and add message
        createKafkaTopic("localhost:9092", topicList.get(0));
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

        // test select sql
        flinkSqlParam.setSql("select * from table1;");
        DataStream<Row> resultStream = flinkQueryExecutor.execute(flinkSqlParam);
        List<String> expected = new ArrayList<>();
        expected.add(
            "POINT (90 90), "
                + "POINT (90 90), "
                + "LINESTRING (0 0, 1 1, 1 2), "
                + "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11)), "
                + "MULTIPOINT ((3.5 5.6), (4.8 10.5)), "
                + "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4)), "
                + "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))"
        );
        checkTable(
            flinkQueryExecutor.getTableEnv(),
            flinkQueryExecutor.getTableEnv().fromDataStream(resultStream),
            expected
        );

        // delete topic
        deleteKafkaTopic("localhost:9092", topicList.get(0));
    }

    @Ignore
    @Test
    public void insertTest() throws Exception {
        // create table
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate(
                "create stream table if not exists table1("
                    + "int1 int,"
                    + "long1 Long,"
                    + "float1 Float,"
                    + "double1 Double,"
                    + "string1 String,"
                    + "bool1 Bool,"
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
                    + "int1 int,"
                    + "long1 Long,"
                    + "float1 Float,"
                    + "double1 Double,"
                    + "string1 String,"
                    + "bool1 Bool,"
                    + "geometry1 Geometry,"
                    + "point1 Point,"
                    + "linestring1 LineString,"
                    + "polygon1 Polygon,"
                    + "multipoint1 MultiPoint,"
                    + "multilinestring1 MultiLineString,"
                    + "multipolygon1 MultiPolygon,"
                    + "SPATIAL INDEX indexName(geometry1))"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // get topic names
        SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(
            "select * from table1, table2;"
        );
        List<org.urbcomp.start.db.metadata.entity.Table> tableList = getTables(
            selectVisitor.getDbTableList()
        );
        List<String> topicList = new ArrayList<>();
        topicList.add(getKafkaTopic(tableList.get(0)));
        topicList.add(getKafkaTopic(tableList.get(1)));

        // create topic and add message
        List<String> recordList = new ArrayList<>();
        recordList.add(
            "+I["
                + "1,,"
                + "1,,"
                + "1.0,,"
                + "1.0,,"
                + "start-db,,"
                + "true,,"
                + "POINT (90 90),,"
                + "POINT (90 90),,"
                + "LINESTRING (0 0, 1 1, 1 2),,"
                + "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11)),,"
                + "MULTIPOINT ((3.5 5.6), (4.8 10.5)),,"
                + "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4)),,"
                + "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))]"
        );
        produceKafkaMessage("localhost:9092", topicList.get(0), recordList);

        // test insert sql
        flinkSqlParam.setSql("insert into table2 select * from table1;");
        flinkQueryExecutor.execute(flinkSqlParam);

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
            .setBootstrapServers("localhost:9092")
            .setTopics(topicList.get(1))
            .setStartingOffsets(OffsetsInitializer.earliest())
            .setValueOnlyDeserializer(new SimpleStringSchema())
            .build();
        DataStream<String> insertStream = flinkQueryExecutor.getEnv()
            .fromSource(
                kafkaSource,
                WatermarkStrategy.noWatermarks(),
                "kafkaSource",
                TypeInformation.of(String.class)
            );
        List<String> expected = new ArrayList<>();
        expected.add(
            "1,, "
                + "1,, "
                + "1.0,, "
                + "1.0,, "
                + "start-db,, "
                + "true,, "
                + "POINT (90 90),, "
                + "POINT (90 90),, "
                + "LINESTRING (0 0, 1 1, 1 2),, "
                + "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11)),, "
                + "MULTIPOINT ((3.5 5.6), (4.8 10.5)),, "
                + "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4)),, "
                + "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))"
        );
        checkTable(
            flinkQueryExecutor.getTableEnv(),
            flinkQueryExecutor.getTableEnv().fromDataStream(insertStream),
            expected
        );

        // delete topic
        deleteKafkaTopic("localhost:9092", topicList.get(0));
        deleteKafkaTopic("localhost:9092", topicList.get(1));
    }

    @Ignore
    @Test
    public void loadTableTest() throws Exception {
        // create table
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table1");
            stmt.executeUpdate("drop table if exists table2");
            stmt.executeUpdate(
                "create table if not exists table1("
                    + "int1 int,"
                    + "long1 Long,"
                    + "float1 Float,"
                    + "double1 Double,"
                    + "string1 String,"
                    + "bool1 Bool,"
                    + "geometry1 Geometry,"
                    + "point1 Point,"
                    + "linestring1 LineString,"
                    + "polygon1 Polygon,"
                    + "multipoint1 MultiPoint,"
                    + "multilinestring1 MultiLineString,"
                    + "multipolygon1 MultiPolygon,"
                    + "SPATIAL INDEX indexName(geometry1))"
            );
            stmt.executeUpdate("create table if not exists table2(idd int)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // get topic names
        SelectFromTableVisitor visitor = new SelectFromTableVisitor(
            "select * from table1, table2;"
        );
        List<org.urbcomp.start.db.metadata.entity.Table> tableList = getTables(
            visitor.getDbTableList()
        );
        List<String> topicList = new ArrayList<>();
        topicList.add(getKafkaTopic(tableList.get(0)));
        topicList.add(getKafkaTopic(tableList.get(1)));

        // create topic and add message
        createKafkaTopic("localhost:9092", topicList.get(0));
        createKafkaTopic("localhost:9092", topicList.get(1));
        List<String> recordList = new ArrayList<>();
        recordList.add(
            "+I["
                + "1,,"
                + "1,,"
                + "1.0,,"
                + "1.0,,"
                + "start-db,,"
                + "true,,"
                + "POINT (90 90),,"
                + "POINT (90 90),,"
                + "LINESTRING (0 0, 1 1, 1 2),,"
                + "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11)),,"
                + "MULTIPOINT ((3.5 5.6), (4.8 10.5)),,"
                + "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4)),,"
                + "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))]"
        );
        produceKafkaMessage("localhost:9092", topicList.get(0), recordList);
        recordList.clear();
        recordList.add("2");
        produceKafkaMessage("localhost:9092", topicList.get(1), recordList);

        // load table and test
        flinkQueryExecutor.loadTables(
            flinkSqlParam,
            visitor.getTableList(),
            visitor.getDbTableList(),
            tableList
        );
        Table table1 = flinkQueryExecutor.getTableEnv().sqlQuery("select * from table1;");
        Table table2 = flinkQueryExecutor.getTableEnv().sqlQuery("select * from table2;");
        checkTableNotNull(flinkQueryExecutor.getTableEnv(), table1);
        checkTableNotNull(flinkQueryExecutor.getTableEnv(), table2);

        List<String> expected = new ArrayList<>();
        expected.add(
            "1, "
                + "1, "
                + "1.0, "
                + "1.0, "
                + "start-db, "
                + "true, "
                + "POINT (90 90), "
                + "POINT (90 90), "
                + "LINESTRING (0 0, 1 1, 1 2), "
                + "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11)), "
                + "MULTIPOINT ((3.5 5.6), (4.8 10.5)), "
                + "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4)), "
                + "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))"
        );
        checkTable(flinkQueryExecutor.getTableEnv(), table1, expected);

        // delete topic
        deleteKafkaTopic("localhost:9092", topicList.get(0));
        deleteKafkaTopic("localhost:9092", topicList.get(1));
    }

    @Ignore
    @Test
    public void registerUdfTest() throws Exception {
        flinkQueryExecutor.registerUdf();
    }

}
