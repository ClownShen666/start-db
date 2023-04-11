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
import org.urbcomp.cupid.db.model.data.DataExportType;
import org.urbcomp.cupid.db.util.SparkSqlParam;

public class CupidSparkDriverTest {
    @Test
    public void testExecute() {
        final SparkSqlParam param = new SparkSqlParam();
        param.setUserName("root");
        param.setDbName("default");
        param.setSql("select * from t_test");
        param.setExportType(DataExportType.PRINT);
        param.setLocal(true);
        param.setWithJTS(true);
        SparkQueryExecutor.execute(param, null);
    }

    @Test
    @Ignore // 本地要起HDFS来测试
    public void testExport2Hdfs() {
        final SparkSqlParam param = new SparkSqlParam();
        param.setUserName("root");
        param.setDbName("default");
        param.setSql("select 1+1");
        param.setExportType(DataExportType.HDFS);
        param.setLocal(true);
        param.setSqlId("testSqlId");
        SparkQueryExecutor.execute(param, null);
    }

    @Test
    public void testUdf1() {
        final SparkSqlParam param = new SparkSqlParam();
        param.setUserName("root");
        param.setDbName("default");
        param.setEnableHiveSupport(true);
        param.setWithJTS(true);
        param.setSql("select AddOne(23) as addOne");
        param.setExportType(DataExportType.PRINT);
        param.setLocal(true);
        SparkQueryExecutor.execute(param, null);
    }

    @Test
    public void testUdtf1() {
        final SparkSqlParam param = new SparkSqlParam();
        param.setUserName("root");
        param.setDbName("default");
        param.setEnableHiveSupport(true);
        param.setSql("select StringSplit('aaa bbb ccc') as StringName");
        param.setExportType(DataExportType.PRINT);
        param.setLocal(true);
        SparkQueryExecutor.execute(param, null);
    }

    @Test
    public void testUdtf2() {
        final SparkSqlParam param = new SparkSqlParam();
        param.setUserName("root");
        param.setDbName("default");
        param.setEnableHiveSupport(true);
        param.setSql("select StringSplitTwice(';', 'abc:123;efd:567;utf:890')");
        param.setExportType(DataExportType.PRINT);
        param.setLocal(true);
        SparkQueryExecutor.execute(param, null);
    }

    private void execute(String sql) {
        final SparkSqlParam param = new SparkSqlParam();
        param.setUserName("root");
        param.setDbName("default");
        param.setEnableHiveSupport(true);
        param.setSql(sql);
        param.setExportType(DataExportType.PRINT);
        param.setLocal(true);
        // param.setWithJTS(true);
        SparkQueryExecutor.execute(param, null);
    }

    @Test
    public void reverseTest() {
        execute("select reverse('abc')");
    }

    @Test
    public void charLengthTest() {
        execute("select charLength('abc')");
    }

    @Test
    public void concatTest() {
        execute("select concat('abc','bcd')");
    }

    @Test
    public void lengthTest() {
        execute("select length('abc')");
    }

    @Test
    public void locate() {
        execute("select locate('bc', 'abcabc')");
    }

    @Test
    public void lpad() {
        execute("select lpad('abcde', 2)");
    }

    @Test
    public void ltrim() {
        execute("select ltrim('  abcde ')");
    }

    @Test
    public void rpad() {
        execute("select rpad('abcde',2)");
    }

    @Test
    public void md5() {
        execute("select md5('abcde')");
    }

    @Test
    public void rtrim() {
        execute("select rtrim('  abcde ')");
    }

    @Test
    public void trim() {
        execute("select trim('  abcde ')");
    }

    @Test
    public void testAbs() {
        execute("select abs(-1)");
    }

    @Test
    public void testCeil() {
        execute("select ceil(1.5)");
    }

    @Test
    public void testFloor() {
        execute("select floor(1.5)");
    }

    @Test
    public void testLog() {
        execute("select log(pi(), pi() * pi())");
    }

    @Test
    public void testMod() {
        execute("select mod(pow(2, 3), 5)");
    }

    @Test
    public void testToDegrees() {
        execute("select toDegrees(pi())");
    }

    @Test
    public void testToRadians() {
        execute("select toRadians(180)");
    }

}
