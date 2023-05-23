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
package org.urbcomp.cupid.db;

import org.urbcomp.cupid.db.model.sample.ModelGenerator;
import org.urbcomp.cupid.db.model.trajectory.Trajectory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConnectRemoteServer {

    public static void main(String[] args) throws Exception {
        test_remote();
    }

    public static void test_remote() throws Exception {
        final Properties conf = new Properties();
        conf.put("user", "root");
        conf.put("password", "cupid-db");
        conf.put("engine", "spark_cluster");
        conf.put("spark.local", "false");
        conf.put("spark.async", "false");
        conf.put("spark.exportType", "cache");
        try (
                Connection conn = DriverManager.getConnection(
                        "jdbc:cupid-db:url=http://192.168.4.51:8000;db=default",
                        conf
                )
        ) {
            /*final String sqlTxt = Files.readAllLines(
                            Paths.get("cupid-db-core/src/main/resources/metadata/ddl.sql")
                    )
                    .stream()
                    .filter(line -> !line.trim().startsWith("#") && !line.trim().startsWith("--"))
                    .collect(Collectors.joining());
            final String[] sqlList = sqlTxt.split(";");
            for (String sql : sqlList) {
                final Statement stat = conn.createStatement();
                stat.execute(sql.trim());
            }*/

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select 1+1 as s");
            rs.next();
            System.out.println(rs.getString(1));
            assert("2".equals(rs.getString(1)));

            /*Trajectory trajectoryStp = ModelGenerator.generateTrajectory("data/stayPointSegmentationTraj.txt");
            String tGeo = trajectoryStp.toGeoJSON();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select starttime, endtime, st_mPointFromWKT(gpspoints) from " +
                    "(select st_traj_stayPointDetect(traj, 10, 10) " +
                    "from (select st_traj_fromGeoJSON(\'" + tGeo + "\') as traj))");
            rs.next();
            System.out.println(rs.getTimestamp(1));
            System.out.println(rs.getTimestamp(2));
            System.out.println(rs.getObject(3).toString());*/
        }
    }
}
