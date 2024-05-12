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

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.io.ParseException;
import org.urbcomp.cupid.db.flink.cache.GlobalCache;
import org.urbcomp.cupid.db.flink.index.GridIndex;
import org.urbcomp.cupid.db.flink.storage.KafkaToHBaseWriter;
import org.urbcomp.cupid.db.flink.visitor.SelectFromTableVisitor;
import org.urbcomp.cupid.db.metadata.CalciteHelper;
import org.urbcomp.cupid.db.metadata.entity.Table;
import org.urbcomp.cupid.db.util.MetadataUtil;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.urbcomp.cupid.db.flink.FlinkQueryExecutor.getTables;
import static org.urbcomp.cupid.db.flink.TestUtil.*;
import static org.urbcomp.cupid.db.flink.connector.kafkaConnector.getKafkaTopic;
import static org.urbcomp.cupid.db.flink.connector.kafkaConnector.produceKafkaMessage;

public class GridIndexAndWhriteHbase extends AbstractFlinkTest {

    @Ignore
    @Test
    public void streamDataWriteToHbaseTest() throws IOException, ParseException {
        try (Connection connect = CalciteHelper.createConnection()) {
            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table_2");
            stmt.executeUpdate(
                "drop table if exists table_2" + KafkaToHBaseWriter.BATCH_TABLE_SUFFIX
            );
            stmt.executeUpdate("create stream table if not exists table_2(idx int, point1 Point)");

            // get topic
            SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(
                "select * from table_2;"
            );
            List<Table> tableList = getTables(selectVisitor.getDbTableList());
            List<String> topicList = new ArrayList<>();
            topicList.add(getKafkaTopic(tableList.get(0)));

            // produce message
            int count = 10;
            List<String> recordList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                recordList.add("+I[" + i + ",," + "POINT (2.2 2.4)]");
            }
            produceKafkaMessage("localhost:9092", topicList.get(0), recordList);

            Thread.sleep(1000);
            ResultSet resultSet = stmt.executeQuery(
                "select * from table_2" + KafkaToHBaseWriter.BATCH_TABLE_SUFFIX
            );
            int relCount = 0;
            while (resultSet.next()) {
                relCount++;
            }
            Assert.assertEquals(count, relCount);
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Ignore
    @Test
    public void GridIndexTest() throws Exception {
        try (Jedis jedis = GlobalCache.pool.getResource()) {
            for (String key : jedis.keys("*")) {
                jedis.del(key);
            }
        }
        try (Connection connect = CalciteHelper.createConnection()) {

            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table_2");
            stmt.executeUpdate(
                "drop table if exists table_2" + KafkaToHBaseWriter.BATCH_TABLE_SUFFIX
            );
            stmt.executeUpdate(
                "create stream table if not exists table_2("
                    + "idx int,"
                    + "point1 Point,GRID INDEX indexName(point1))"
            );

            // get topic
            SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(
                "select * from table_2;"
            );
            List<org.urbcomp.cupid.db.metadata.entity.Table> tableList = getTables(
                selectVisitor.getDbTableList()
            );
            List<String> topicList = new ArrayList<>();
            topicList.add(getKafkaTopic(tableList.get(0)));

            // produce message
            List<String> recordList = new ArrayList<>();

            addRandomData(recordList);

            produceKafkaMessage("localhost:9092", topicList.get(0), recordList);
            // 5s后再执行查询
            Thread.sleep(5000);
            org.apache.flink.table.api.Table table1 = getFlinkQueryTable(
                selectVisitor,
                tableList,
                "select * from table_2;"
            );

            GridIndex gridIndex = new GridIndex(
                105.0,
                110.0,
                28.108,
                32.20,
                5000,
                GlobalCache.pool.getResource()
            );
            List<String> filteredData = gridIndex.querySpatialObjects(
                107.0,
                30.108,
                109.0,
                31.20,
                MetadataUtil.combineUserDbTableKey(
                    flinkSqlParam.getUserName(),
                    flinkSqlParam.getDbName(),
                    "table_2"
                )
            ).stream().map(s -> s.replace(",,", ", ")).collect(Collectors.toList());

            // check table
            checkTable2(
                FlinkQueryExecutor.FLINK_EXECUTOR_INSTANCE.getTableEnv(),
                table1,
                filteredData
            );

        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Ignore
    @Test
    public void noGridIndexTest() throws Exception {
        try (Jedis jedis = GlobalCache.pool.getResource()) {
            for (String key : jedis.keys("*")) {
                jedis.del(key);
            }
        }
        try (Connection connect = CalciteHelper.createConnection()) {

            Statement stmt = connect.createStatement();
            stmt.executeUpdate("drop table if exists table_2");
            stmt.executeUpdate(
                "drop table if exists table_2" + KafkaToHBaseWriter.BATCH_TABLE_SUFFIX
            );
            stmt.executeUpdate(
                "create stream table if not exists table_2(" + "idx int," + "point1 Point)"
            );

            // get topic
            SelectFromTableVisitor selectVisitor = new SelectFromTableVisitor(
                "select * from table_2;"
            );
            List<org.urbcomp.cupid.db.metadata.entity.Table> tableList = getTables(
                selectVisitor.getDbTableList()
            );
            List<String> topicList = new ArrayList<>();
            topicList.add(getKafkaTopic(tableList.get(0)));

            // produce message
            List<String> recordList = new ArrayList<>();
            List<String> expected = new ArrayList<>();

            addRandomData(recordList, expected);

            produceKafkaMessage("localhost:9092", topicList.get(0), recordList);
            // 5s后再执行查询
            Thread.sleep(5000);
            org.apache.flink.table.api.Table table1 = getFlinkQueryTable(
                selectVisitor,
                tableList,
                "select * from table_2;"
            );

            // check table
            checkTable2(FlinkQueryExecutor.FLINK_EXECUTOR_INSTANCE.getTableEnv(), table1, expected);

        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
