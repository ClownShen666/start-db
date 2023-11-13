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

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.urbcomp.cupid.db.flink.function.Functions;

import java.util.List;

public class FlinkGeometrySupportTest {

    @Test
    public void testPointType() throws Exception {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(90.0, 90.0);
        Point point = geometryFactory.createPoint(coordinate);

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        DataStreamSource<Tuple3<Double, Double, Point>> sensorDS = env.fromElements(
            Tuple3.of(90.0, 90.0, point)
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("pointFunction", Functions.ST_Point.class);

        Table sink = tableEnv.sqlQuery("select pointFunction(f0, f1) , f2 from sensor;");
        DataStream<Row> resultStream = tableEnv.toDataStream(sink, Row.class);
        List<Row> resultList = resultStream.executeAndCollect(1);
        Assert.assertNotEquals("result has no record", 0, resultList.size());

        Point point1 = (Point) resultList.get(0).getField(0);
        Point point2 = (Point) resultList.get(0).getField(1);

        Assert.assertNotNull("Point1 is null", point1);
        Assert.assertNotNull("Point2 is null", point2);
        Assert.assertTrue("two points are not equal", point1.equals(point2));

        tableEnv.toDataStream(sink);
        tableEnv.toChangelogStream(sink);
        env.execute();
    }
}
