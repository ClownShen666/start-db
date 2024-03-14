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

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.*;
import org.urbcomp.cupid.db.config.ExecuteEngine;
import org.urbcomp.cupid.db.flink.connector.SelectFromTableVisitor;
import org.urbcomp.cupid.db.metadata.CalciteHelper;
import org.urbcomp.cupid.db.util.FlinkSqlParam;
import org.urbcomp.cupid.db.util.SqlParam;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.urbcomp.cupid.db.flink.FlinkQueryExecutor.getTables;
import static org.urbcomp.cupid.db.flink.connector.kafkaConnector.*;

// run the "docker/local" and package cupid-db before test
public class FlinkLocalQueryExecutorTest {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
    static SqlParam sqlParam = new SqlParam("root", "default", ExecuteEngine.FLINK, true);
    static FlinkSqlParam flinkSqlParam = new FlinkSqlParam(sqlParam);

    @BeforeClass
    public static void setParam() {
        SqlParam.CACHE.set(sqlParam);
        flinkSqlParam.setTestNum(1);
        flinkSqlParam.setLocal(true);
        FlinkSqlParam.CACHE.set(flinkSqlParam);
    }
    @Ignore
    @Test
    public void selectall() {
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
            List<org.urbcomp.cupid.db.metadata.entity.Table> tableList = getTables(
                selectVisitor.getDbTableList()
            );
            List<String> topicList = new ArrayList<>();
            topicList.add(getKafkaTopic(tableList.get(0)));

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
            produceKafkaMessage("localhost:9092", topicList.get(0), recordList);

            // test
            stmt.executeQuery("select * from table1");
            produceKafkaMessage("localhost:9092", topicList.get(0), recordList);

            // delete topic
            stmt.executeUpdate("drop table if exists table1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
