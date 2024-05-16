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
import org.junit.Assert;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TestUtil {
    public static void checkTableNotNull(StreamTableEnvironment tableEnv, Table sink)
        throws Exception {
        DataStream<Row> resultStream = tableEnv.toDataStream(sink, Row.class);
        List<Row> resultList = resultStream.executeAndCollect(1);
        Assert.assertNotEquals("result has no record", 0, resultList.size());
    }

    public static void checkTable(StreamTableEnvironment tableEnv, Table sink, List<String> rows)
        throws Exception {
        DataStream<Row> resultStream = tableEnv.toDataStream(sink, Row.class);
        int num = rows.size();
        List<String> resultList = resultStream.executeAndCollect(num)
            .stream()
            .map(Row::toString)
            .collect(Collectors.toList());

        Collections.sort(rows);
        Collections.sort(resultList);
        for (int i = 0; i < num; i++) {
            Assert.assertEquals(
                "result is not as expected",
                "+I[" + rows.get(i) + "]",
                resultList.get(i)
            );
        }
    }

    public static void checkTable2(StreamTableEnvironment tableEnv, Table sink, List<String> rows)
        throws Exception {
        DataStream<Row> resultStream = tableEnv.toDataStream(sink, Row.class);
        int num = rows.size();
        List<String> resultList = resultStream.executeAndCollect(num)
            .stream()
            .map(Row::toString)
            .collect(Collectors.toList());
        Collections.sort(rows);
        Collections.sort(resultList);
        for (int i = 0; i < num; i++) {
            Assert.assertEquals("result is not as expected", rows.get(i), resultList.get(i));
        }
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

    public static void addRandomData(List<String> recordList) {
        Random random = new Random();
        double minLongitude = 105.0;
        double maxLongitude = 110.0;
        double minLatitude = 28.0;
        double maxLatitude = 32.20;
        for (int i = 0; i < 100; i++) {
            double longitude = minLongitude + (maxLongitude - minLongitude) * random.nextDouble();
            double latitude = minLatitude + (maxLatitude - minLatitude) * random.nextDouble();
            recordList.add("+I[" + i + ",," + "POINT (" + longitude + " " + latitude + ")]");
        }
    }

    public static void addRandomData(List<String> recordList, List<String> expected) {
        addRandomData(recordList);
        expected.addAll(recordList);
    }
}
