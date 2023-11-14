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

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.junit.Test;
import org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf.*;

import static org.urbcomp.cupid.db.flink.util.checkF1EqualF2;

public class geometricTypeConversionUdfTest {

    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

    @Test
    public void st_GeometryFromWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "POINT (90 90)",
                "LINESTRING (0 0, 1 1, 1 2)",
                "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))",
                "MULTIPOINT ((3.5 5.6), (4.8 10.5))",
                "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))",
                "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_geometryFromWKT", st_geometryFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_geometryFromWKT(f0), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }

    @Test
    public void st_pointFromWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "POINT (90 90)"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_pointFromWKT", st_pointFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_pointFromWKT(f0), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }

    @Test
    public void st_lineStringFromWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "LINESTRING (0 0, 1 1, 1 2)"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_lineStringFromWKT", st_lineStringFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_lineStringFromWKT(f0), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }

    @Test
    public void st_polygonFromWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_polygonFromWKT", st_polygonFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_polygonFromWKT(f0), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }

    @Test
    public void st_multiPointFromWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "MULTIPOINT ((3.5 5.6), (4.8 10.5))"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_multiPointFromWKT", st_multiPointFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_multiPointFromWKT(f0), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }

    @Test
    public void st_multiLineStringFromWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_multiLineStringFromWKT", st_multiLineStringFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_multiLineStringFromWKT(f0), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }

    @Test
    public void st_multiPolygonFromWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_multiPolygonFromWKT", st_multiPolygonFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_multiPolygonFromWKT(f0), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }

    @Test
    public void st_geometryAsWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "POINT (90 90)",
                "LINESTRING (0 0, 1 1, 1 2)",
                "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))",
                "MULTIPOINT ((3.5 5.6), (4.8 10.5))",
                "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))",
                "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_asWKT", st_geometryAsWKT.class);
        tableEnv.createTemporaryFunction("st_geometryFromWKT", st_geometryFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_asWKT(st_geometryFromWKT(f0)), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);

    }

    @Test
    public void st_pointAsWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "POINT (90 90)"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_pointAsWKT", st_pointAsWKT.class);
        tableEnv.createTemporaryFunction("st_pointFromWKT", st_pointFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_pointAsWKT(st_pointFromWKT(f0)), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }

    @Test
    public void st_lineStringAsWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "LINESTRING (0 0, 1 1, 1 2)"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_lineStringAsWKT", st_lineStringAsWKT.class);
        tableEnv.createTemporaryFunction("st_lineStringFromWKT", st_lineStringFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_lineStringAsWKT(st_lineStringFromWKT(f0)), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }

    @Test
    public void st_polygonAsWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "POLYGON ((10 11, 12 12, 13 14, 15 16, 10 11))"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_polygonAsWKT", st_polygonAsWKT.class);
        tableEnv.createTemporaryFunction("st_polygonFromWKT", st_polygonFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_polygonAsWKT(st_polygonFromWKT(f0)), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }

    @Test
    public void st_multiPointAsWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "MULTIPOINT ((3.5 5.6), (4.8 10.5))"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_multiPointAsWKT", st_multiPointAsWKT.class);
        tableEnv.createTemporaryFunction("st_multiPointFromWKT", st_multiPointFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_multiPointAsWKT(st_multiPointFromWKT(f0)), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }

    @Test
    public void st_multiLineStringAsWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "MULTILINESTRING ((3 4, 1 5, 2 5), (-5 -8, -10 -8, -15 -4))"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_multiLineStringAsWKT", st_multiLineStringAsWKT.class);
        tableEnv.createTemporaryFunction("st_multiLineStringFromWKT", st_multiLineStringFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_multiLineStringAsWKT(st_multiLineStringFromWKT(f0)), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }

    @Test
    public void st_multiPolygonAsWKT() throws Exception {
        DataStreamSource<String> sensorDS = env.fromElements(
                "MULTIPOLYGON (((1 1, 5 1, 5 5, 1 5, 1 1), (2 2, 2 3, 3 3, 3 2, 2 2)), ((6 3, 9 2, 9 4, 6 3)))"
        );

        tableEnv.createTemporaryView("sensor", sensorDS);
        tableEnv.createTemporaryFunction("st_multiPolygonAsWKT", st_multiPolygonAsWKT.class);
        tableEnv.createTemporaryFunction("st_multiPolygonFromWKT", st_multiPolygonFromWKT.class);
        Table sink = tableEnv.sqlQuery("select st_multiPolygonAsWKT(st_multiPolygonFromWKT(f0)), f0 from sensor;");

        checkF1EqualF2(tableEnv, sink);
    }
}
