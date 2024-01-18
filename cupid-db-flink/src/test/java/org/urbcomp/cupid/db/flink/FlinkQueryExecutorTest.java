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
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.junit.Ignore;
import org.junit.Test;
import org.urbcomp.cupid.db.flink.util.FlinkQueryExecutor;
import org.urbcomp.cupid.db.flink.util.FlinkSqlParam;
import org.urbcomp.cupid.db.flink.util.SelectFromTableVisitor;
import org.urbcomp.cupid.db.metadata.CalciteHelper;
import org.urbcomp.cupid.db.util.SqlParam;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.urbcomp.cupid.db.flink.TestUtil.checkTable;
import static org.urbcomp.cupid.db.flink.TestUtil.checkTableNotNull;
import static org.urbcomp.cupid.db.flink.util.FlinkQueryExecutor.getTables;

// run the "docker/flink-kafka" before test
public class FlinkQueryExecutorTest {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

    @Ignore
    @Test
    public void selectTest() throws Exception {
        // create table
        SqlParam.CACHE.set(new SqlParam("root", "default"));
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

        // set param and get topic names
        FlinkSqlParam param = new FlinkSqlParam();
        param.setBootstrapServers("localhost:9092");
        param.setSql("select * from table1;");
        param.setUserName("root");
        param.setDbName("default");
        param.setEnv(env);
        param.setTableEnv(tableEnv);
        FlinkSqlParam.CACHE.set(param);
        FlinkQueryExecutor flink = new FlinkQueryExecutor();
        SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(param.getSql());
        List<org.urbcomp.cupid.db.metadata.entity.Table> tableList = getTables(
            selectVisitor.getDbTableList()
        );
        List<String> topicList = new ArrayList<>();
        topicList.add(flink.getKafkaTopic(tableList.get(0)));

        // create topic and add message
        TestUtil.KafkaCreateTopic("localhost:9092", topicList.get(0));
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
        TestUtil.kafkaProducer("localhost:9092", topicList.get(0), recordList);

        // test select sql
        param.setSql(
            "select "
                + "st_geometryFromWKT(st_geometryAsWKT(geometry1)),"
                + "st_pointFromWKT(st_pointAsWKT(point1)),"
                + "st_lineStringFromWKT(st_lineStringAsWKT(linestring1)),"
                + "st_polygonFromWKT(st_polygonAsWKT(polygon1)),"
                + "st_multiPointFromWKT(st_multiPointAsWKT(multipoint1)),"
                + "st_multiLineStringFromWKT(st_multiLineStringAsWKT(multilinestring1)),"
                + "st_multiPolygonFromWKT(st_multiPolygonAsWKT(multipolygon1))"
                + " from table1;"
        );
        DataStream<Row> resultStream = flink.execute(param);
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
        checkTable(tableEnv, tableEnv.fromDataStream(resultStream), expected);

        TestUtil.KafkaDeleteTopic("localhost:9092", topicList.get(0));
    }

    @Ignore
    @Test
    public void insertTest() throws Exception {
        // create table
        SqlParam.CACHE.set(new SqlParam("root", "default"));
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
            stmt.executeUpdate(
                "create table if not exists table2("
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

        // set param and get topic names
        FlinkSqlParam param = new FlinkSqlParam();
        param.setBootstrapServers("localhost:9092");
        param.setSql("select * from table1 inner join table2;");
        param.setUserName("root");
        param.setDbName("default");
        param.setEnv(env);
        param.setTableEnv(tableEnv);
        FlinkSqlParam.CACHE.set(param);
        FlinkQueryExecutor flink = new FlinkQueryExecutor();
        SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(param.getSql());
        List<org.urbcomp.cupid.db.metadata.entity.Table> tableList = getTables(
            selectVisitor.getDbTableList()
        );
        List<String> topicList = new ArrayList<>();
        topicList.add(flink.getKafkaTopic(tableList.get(0)));
        topicList.add(flink.getKafkaTopic(tableList.get(1)));

        // create topic and add message
        TestUtil.KafkaCreateTopic("localhost:9092", topicList.get(0));
        TestUtil.KafkaCreateTopic("localhost:9092", topicList.get(1));
        List<String> recordList = new ArrayList<>();
        recordList.add(
            "+I["
                + "1,,"
                + "1,,"
                + "1.0,,"
                + "1.0,,"
                + "cupid-db,,"
                + "true,,"
                + "POINT (90 90),,"
                + "POINT (90 90),,"
                + "LINESTRING (0 0, 1 1, 1 2),,"
                + "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11)),,"
                + "MULTIPOINT ((3.5 5.6), (4.8 10.5)),,"
                + "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4)),,"
                + "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))]"
        );
        TestUtil.kafkaProducer("localhost:9092", topicList.get(0), recordList);

        // test insert sql
        param.setSql(
            "insert into table2 "
                + "select "
                + "int1,"
                + "long1,"
                + "float1,"
                + "double1,"
                + "string1,"
                + "bool1,"
                + "st_geometryFromWKT(st_geometryAsWKT(geometry1)),"
                + "st_pointFromWKT(st_pointAsWKT(point1)),"
                + "st_lineStringFromWKT(st_lineStringAsWKT(linestring1)),"
                + "st_polygonFromWKT(st_polygonAsWKT(polygon1)),"
                + "st_multiPointFromWKT(st_multiPointAsWKT(multipoint1)),"
                + "st_multiLineStringFromWKT(st_multiLineStringAsWKT(multilinestring1)),"
                + "st_multiPolygonFromWKT(st_multiPolygonAsWKT(multipolygon1))"
                + " from table1;"
        );
        flink.execute(param);
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
        List<String> expected = new ArrayList<>();
        expected.add(
            "1,, "
                + "1,, "
                + "1.0,, "
                + "1.0,, "
                + "cupid-db,, "
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

        TestUtil.KafkaDeleteTopic("localhost:9092", topicList.get(0));
        TestUtil.KafkaDeleteTopic("localhost:9092", topicList.get(1));
    }

    @Ignore
    @Test
    public void loadTableTest() throws Exception {
        // create table
        SqlParam.CACHE.set(new SqlParam("root", "default"));
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

        // set param and get topic names
        FlinkSqlParam param = new FlinkSqlParam();
        param.setBootstrapServers("localhost:9092");
        param.setSql("select * from table1 inner join table2;");
        param.setUserName("root");
        param.setDbName("default");
        param.setEnv(env);
        param.setTableEnv(tableEnv);
        FlinkSqlParam.CACHE.set(param);
        FlinkQueryExecutor flink = new FlinkQueryExecutor();
        SelectFromTableVisitor visitor = new SelectFromTableVisitor(param.getSql());
        List<org.urbcomp.cupid.db.metadata.entity.Table> tableList = getTables(
            visitor.getDbTableList()
        );
        List<String> topicList = new ArrayList<>();
        topicList.add(flink.getKafkaTopic(tableList.get(0)));
        topicList.add(flink.getKafkaTopic(tableList.get(1)));

        // create topic and add message
        TestUtil.KafkaCreateTopic("localhost:9092", topicList.get(0));
        TestUtil.KafkaCreateTopic("localhost:9092", topicList.get(1));
        List<String> recordList = new ArrayList<>();
        recordList.add(
            "+I["
                + "1,,"
                + "1,,"
                + "1.0,,"
                + "1.0,,"
                + "cupid-db,,"
                + "true,,"
                + "POINT (90 90),,"
                + "POINT (90 90),,"
                + "LINESTRING (0 0, 1 1, 1 2),,"
                + "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11)),,"
                + "MULTIPOINT ((3.5 5.6), (4.8 10.5)),,"
                + "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4)),,"
                + "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))]"
        );
        TestUtil.kafkaProducer("localhost:9092", topicList.get(0), recordList);
        recordList.clear();
        recordList.add("2");
        TestUtil.kafkaProducer("localhost:9092", topicList.get(1), recordList);

        // load table and test
        flink.loadTables(param, visitor.getTableList(), visitor.getDbTableList(), tableList);
        Table table1 = param.getTableEnv().sqlQuery("select * from table1;");
        Table table2 = param.getTableEnv().sqlQuery("select * from table2;");
        checkTableNotNull(tableEnv, table1);
        checkTableNotNull(tableEnv, table2);

        List<String> expected = new ArrayList<>();
        expected.add(
            "1, "
                + "1, "
                + "1.0, "
                + "1.0, "
                + "cupid-db, "
                + "true, "
                + "POINT (90 90), "
                + "POINT (90 90), "
                + "LINESTRING (0 0, 1 1, 1 2), "
                + "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11)), "
                + "MULTIPOINT ((3.5 5.6), (4.8 10.5)), "
                + "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4)), "
                + "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))"
        );
        checkTable(tableEnv, table1, expected);

        TestUtil.KafkaDeleteTopic("localhost:9092", topicList.get(0));
        TestUtil.KafkaDeleteTopic("localhost:9092", topicList.get(1));
    }

    @Ignore
    @Test
    public void registerUdfTest() throws Exception {
        // create table
        SqlParam.CACHE.set(new SqlParam("root", "default"));
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

        // set param and get topic names
        FlinkSqlParam param = new FlinkSqlParam();
        param.setBootstrapServers("localhost:9092");
        param.setSql("select * from table1;");
        param.setUserName("root");
        param.setDbName("default");
        param.setEnv(env);
        param.setTableEnv(tableEnv);
        FlinkSqlParam.CACHE.set(param);
        FlinkQueryExecutor flink = new FlinkQueryExecutor();
        SelectFromTableVisitor visitor = new SelectFromTableVisitor(param.getSql());
        List<org.urbcomp.cupid.db.metadata.entity.Table> tableList = getTables(
            visitor.getDbTableList()
        );
        List<String> topicList = new ArrayList<>();
        topicList.add(flink.getKafkaTopic(tableList.get(0)));

        // create topic and add message
        TestUtil.KafkaCreateTopic("localhost:9092", topicList.get(0));
        List<String> recordList = new ArrayList<>();
        recordList.add(
            "POINT (90 90),,"
                + "POINT (90 90),,"
                + "LINESTRING (0 0, 1 1, 1 2),,"
                + "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11)),,"
                + "MULTIPOINT ((3.5 5.6), (4.8 10.5)),,"
                + "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4)),,"
                + "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))"
        );
        TestUtil.kafkaProducer("localhost:9092", topicList.get(0), recordList);

        // load table
        flink.loadTable(
            param,
            visitor.getTableList().get(0),
            visitor.getDbTableList().get(0),
            tableList.get(0)
        );
        Table table1 = param.getTableEnv().sqlQuery("select * from table1;");
        checkTableNotNull(tableEnv, table1);

        // register udf and test
        flink.registerUdf(param);
        tableEnv.sqlQuery("select st_geometryFromWKT(st_geometryAsWKT(geometry1)) from table1;");
        tableEnv.sqlQuery("select st_pointFromWKT(st_pointAsWKT(point1)) from table1;");
        tableEnv.sqlQuery(
            "select st_lineStringFromWKT(st_lineStringAsWKT(linestring1)) from table1;"
        );
        tableEnv.sqlQuery("select st_polygonFromWKT(st_polygonAsWKT(polygon1)) from table1;");
        tableEnv.sqlQuery(
            "select st_multiPointFromWKT(st_multiPointAsWKT(multipoint1)) from table1;"
        );
        tableEnv.sqlQuery(
            "select st_multiLineStringFromWKT(st_multiLineStringAsWKT(multilinestring1)) from table1;"
        );
        tableEnv.sqlQuery(
            "select st_multiPolygonFromWKT(st_multiPolygonAsWKT(multipolygon1)) from table1;"
        );

        TestUtil.KafkaDeleteTopic("localhost:9092", topicList.get(0));
    }

}
