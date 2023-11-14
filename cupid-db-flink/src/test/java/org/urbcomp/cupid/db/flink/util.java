package org.urbcomp.cupid.db.flink;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.junit.Assert;

import java.util.List;

public class util {
    public static void checkF1EqualF2(StreamTableEnvironment tableEnv, Table sink) throws Exception {
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
