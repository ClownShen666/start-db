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

import org.geotools.data.DataStore;
import org.junit.Ignore;
import org.junit.Test;
import org.urbcomp.cupid.db.config.DynamicConfig;
import org.urbcomp.cupid.db.config.ExecuteEngine;
import org.urbcomp.cupid.db.infra.MetadataResult;
import org.urbcomp.cupid.db.model.data.DataExportType;
import org.urbcomp.cupid.db.tools.CitibikeDataUtils;
import org.urbcomp.cupid.db.util.SparkSqlParam;

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

    private void testLoadSql(String sql) {
        String userName = "root";
        String dbName = "default";
        DataStore store = CitibikeDataUtils.getStore(userName, dbName);
        CitibikeDataUtils.prepareTable(store);
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
    public void testLoadData() {
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
            // + "started_at toTimestamp(started_at),"
            + "start_point st_makePoint(start_lat, start_lng)) "
            + "WITH HEADER";
        testLoadSql(sql);
    }

    @Test
    public void testLoadDataWithoutHeader() {
        String path = CitibikeDataUtils.getProjectRoot()
            + "/cupid-db-test/cupid-db-test-geomesa-geotools/src/main/resources/"
            + "202204-citibike-tripdata_clip_slice_without_header.csv";
        String sql = "LOAD CSV INPATH '"
            + path
            + "' TO "
            + CitibikeDataUtils.TEST_TABLE_NAME
            + " (idx _c0, ride_id _c1,"
            + "rideable_type _c2,"
            // + "started_at toTimestamp(started_at),"
            + "start_point st_makePoint(_c9, _c10)) "
            + "WITHOUT HEADER";
        testLoadSql(sql);
    }

}
