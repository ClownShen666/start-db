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
package org.urbcomp.cupid.db.spark;

import org.junit.Ignore;
import org.junit.Test;
import org.urbcomp.cupid.db.config.DynamicConfig;
import org.urbcomp.cupid.db.config.ExecuteEngine;
import org.urbcomp.cupid.db.infra.MetadataResult;
import org.urbcomp.cupid.db.metadata.CalciteHelper;
import org.urbcomp.cupid.db.model.data.DataExportType;
import org.urbcomp.cupid.db.tools.CitibikeDataUtils;
import org.urbcomp.cupid.db.util.SparkSqlParam;
import org.urbcomp.cupid.db.util.SqlParam;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.Assert.assertNotNull;
import static org.urbcomp.cupid.db.config.DynamicConfig.DB_SPARK_JARS;

public class SparkExecutorTest {

    @Ignore
    public void testExecute() {
        DynamicConfig.updateProperties(
            DB_SPARK_JARS,
            "/opt/spark/examples/jars/spark-examples_2.12-3.0.2.jar"
        );

        final SparkExecutor executor = new SparkExecutor();

        final SparkSqlParam param = new SparkSqlParam();
        param.setExportType(DataExportType.PRINT);
        param.setSql("1+1");
        final MetadataResult<Object> res = executor.execute(param);
        assertNotNull(res);
    }

    @Test
    public void testExecuteLocal() {
        final SparkExecutor executor = new SparkExecutor();
        final SparkSqlParam param = new SparkSqlParam();
        param.setLocal(true);
        param.setExecuteEngine(ExecuteEngine.SPARK_LOCAL);
        param.setExportType(DataExportType.LOCAL);
        param.setSql("select 1+1");
        final MetadataResult<Object> res = executor.execute(param);
        assertNotNull(res);
    }

    private void testLoadSql(String sql) throws Exception {
        String userName = "root";
        String dbName = "default";
        SqlParam.CACHE.set(new SqlParam(userName, dbName));
        try (Connection connect = CalciteHelper.createConnection()) {
            String createTableSql = "create table if not exists "
                + CitibikeDataUtils.TEST_TABLE_NAME
                + " (    idx Integer,"
                + " ride_id String,"
                + " rideable_type String,"
                + " start_point Point)";
            Statement stmt = connect.createStatement();
            stmt.executeUpdate(createTableSql);
        }
        // for hadoop just need to provide a hadoop path
        final SparkExecutor executor = new SparkExecutor();
        final SparkSqlParam param = new SparkSqlParam();
        param.setLocal(true);
        param.setExecuteEngine(ExecuteEngine.SPARK_LOCAL);
        param.setExportType(DataExportType.LOCAL);
        param.setSql(sql);
        param.setUserName(userName);
        param.setDbName(dbName);
        final MetadataResult<Object> res = executor.execute(param);
        assertNotNull(res);
    }

    @Test
    public void testLoadData() throws Exception {
        // for hadoop just need to provide a hadoop path
        String path = CitibikeDataUtils.getProjectRoot()
            + "/cupid-db-test/cupid-db-test-geomesa-geotools/src/main/resources/"
            + "202204-citibike-tripdata_clip_slice.csv";
        String sql = "LOAD CSV INPATH '"
            + path
            + "' TO "
            + CitibikeDataUtils.TEST_TABLE_NAME
            + " (idx idx, ride_id ride_id,"
            + "rideable_type rideable_type,"
            + "start_point st_makePoint(start_lat, start_lng)) "
            + "WITH HEADER";
        testLoadSql(sql);
    }

    @Test
    public void testLoadDataWithoutHeader() throws Exception {
        String path = CitibikeDataUtils.getProjectRoot()
            + "/cupid-db-test/cupid-db-test-geomesa-geotools/src/main/resources/"
            + "202204-citibike-tripdata_clip_slice_without_header.csv";
        String sql = "LOAD CSV INPATH '"
            + path
            + "' TO "
            + CitibikeDataUtils.TEST_TABLE_NAME
            + " (idx _c0, ride_id _c1,"
            + "rideable_type _c2,"
            + "start_point st_makePoint(_c9, _c10)) "
            + "WITHOUT HEADER";
        testLoadSql(sql);
    }

    @Test
    public void testLoadDataWithDelimiterAndQuotes() throws Exception {
        String path = CitibikeDataUtils.getProjectRoot()
            + "/cupid-db-test/cupid-db-test-geomesa-geotools/src/main/resources/"
            + "202204-citibike-tripdata_clip_slice_with_delimiter_and_quotes.csv";
        String sql = "LOAD CSV INPATH '"
            + path
            + "' TO "
            + CitibikeDataUtils.TEST_TABLE_NAME
            + " (idx idx, ride_id ride_id,"
            + "rideable_type rideable_type,"
            // + "started_at toTimestamp(started_at),"
            + "start_point st_makePoint(start_lat, start_lng)) "
            + "FIELDS DELIMITER '=' QUOTES '*' "
            + "WITH HEADER";
        testLoadSql(sql);
    }

}
