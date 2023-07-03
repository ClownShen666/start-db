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
package org.urbcomp.cupid.db.jdbc;

import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.urbcomp.cupid.db.model.roadnetwork.RoadSegment;
import org.urbcomp.cupid.db.model.sample.ModelGenerator;

import java.sql.*;

import static org.junit.Assert.assertEquals;

@Ignore
// Tests depends on server
public class DriverTypeTest {

    private final static String DEFAULT_USER = "root";
    private final static String DEFAULT_PASSWORD = "cupid-db";
    private final static String rsGeoJson;

    static {
        try {
            rsGeoJson = ModelGenerator.generateRoadSegment().toGeoJSON();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Test
    public void testGetPoint() throws SQLException {
        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:cupid-db:url=http://127.0.0.1:8000",
                DEFAULT_USER,
                DEFAULT_PASSWORD
            )
        ) {
            final Statement stmt = conn.createStatement();
            stmt.execute("drop table if exists t_test2");
            stmt.execute("create table if not exists t_test2(idx int, start_point point)");
            stmt.execute("insert into t_test2 (idx, start_point) values (1, st_makePoint(2.1, 2))");
            final ResultSet rs = stmt.executeQuery("select * from t_test2");
            rs.next();
            Point pt = rs.getObject(2, Point.class);
        }
    }

    @Test
    public void testGetRoadSegment() throws SQLException {
        try (
            Connection conn = DriverManager.getConnection(
                "jdbc:cupid-db:url=http://127.0.0.1:8000",
                DEFAULT_USER,
                DEFAULT_PASSWORD
            )
        ) {
            final Statement stmt = conn.createStatement();
            stmt.execute("drop table if exists t_road_segment_test");
            stmt.execute(
                "create table if not exists t_road_segment_test (a Integer, b RoadSegment);"
            );
            stmt.execute(
                "insert into t_road_segment_test values (2, st_rs_fromGeoJSON(\'"
                    + rsGeoJson
                    + "\')), (3, st_rs_fromGeoJSON(\'"
                    + rsGeoJson
                    + "\'))"
            );

            ResultSet rs = stmt.executeQuery("select * from t_road_segment_test");
            rs.next();
            RoadSegment segment = rs.getObject(2, RoadSegment.class);
            assertEquals(2, segment.getRoadSegmentId());
        }
    }

}
