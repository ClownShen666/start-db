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

import java.util.List;

public class util {
    public static void checkF1EqualF2(StreamTableEnvironment tableEnv, Table sink)
        throws Exception {
        DataStream<Row> resultStream = tableEnv.toDataStream(sink, Row.class);
        List<Row> resultList = resultStream.executeAndCollect(1);
        Assert.assertNotEquals("result has no record", 0, resultList.size());

        for (Row row : resultList) {
            String field1 = row.getField(0).toString();
            String field2 = row.getField(1).toString();
            Assert.assertNotNull("field1 is null", field1);
            Assert.assertNotNull("field2 is null", field2);
            Assert.assertEquals("two fields are not equal", field1, field2);
        }
    }
}
