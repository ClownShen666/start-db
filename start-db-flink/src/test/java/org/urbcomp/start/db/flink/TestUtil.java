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

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.junit.Assert;
import org.urbcomp.start.db.metadata.MetadataAccessorFromDb;
import org.urbcomp.start.db.metadata.entity.Field;
import org.urbcomp.start.db.util.SqlParam;

import java.util.*;
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

    public static void checkTable2(
        StreamTableEnvironment tableEnv,
        DataStream<Row> dataStream,
        List<String> rows
    ) throws Exception {
        int num = rows.size();
        List<String> resultList = dataStream.executeAndCollect(num)
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

    public static List<String> getNameList(String tableName, SqlParam sqlParam) {
        List<Field> fields = getFiledList(tableName, sqlParam);
        List<String> nameList = new ArrayList<>();
        for (Field field : fields) {
            nameList.add(field.getName());
        }
        return nameList;
    }

    public static List<String> getTypeList(String tableName, SqlParam sqlParam) {

        List<Field> fields = getFiledList(tableName, sqlParam);
        List<String> res = new ArrayList<>();
        for (Field field : fields) {
            res.add(field.getType());
        }
        return res;
    }

    private static List<Field> getFiledList(String tableName, SqlParam sqlParam) {
        MetadataAccessorFromDb accessor = new MetadataAccessorFromDb();
        return accessor.getFields(sqlParam.getUserName(), sqlParam.getDbName(), tableName);
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
}
