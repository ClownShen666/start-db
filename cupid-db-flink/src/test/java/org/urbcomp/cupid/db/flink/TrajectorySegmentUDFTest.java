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

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Ignore;
import org.junit.Test;
import org.urbcomp.cupid.db.flink.algorithm.step.object.SegGpsPoint;
import org.urbcomp.cupid.db.flink.source.GPSSource;
import org.urbcomp.cupid.db.flink.udf.TrajectorySegmentUDF;

public class TrajectorySegmentUDFTest {
    @Test
    @Ignore
    public void TrajectorySegment() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        tableEnv.createTemporaryFunction("seg", TrajectorySegmentUDF.class);

        DataStream<Tuple2<String, SegGpsPoint>> gspPoints = env.addSource(new GPSSource())
            .name("gps");

        tableEnv.createTemporaryView("gps", gspPoints);

        Table table = tableEnv.from("gps");

        Table result = tableEnv.sqlQuery("select seg(f1) from gps group by f0");

        tableEnv.toChangelogStream(result).print();

        env.execute();

    }
}
