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
package org.urbcomp.start.db.parser.visitor
import org.apache.calcite.sql._
import org.apache.calcite.sql.ddl.{SqlDropSchema, SqlDropTable}
import org.apache.calcite.sql.parser.SqlParser
import org.junit.Assert.{assertEquals, assertFalse, assertNotNull, assertTrue}
import org.scalatest.{BeforeAndAfterEach, FunSuite}
import org.urbcomp.start.db.parser.dcl.{SqlCreateUser, SqlLoadData}
import org.urbcomp.start.db.parser.ddl._
import org.urbcomp.start.db.parser.dql.{
  SqlShowCreateTable,
  SqlShowDatabases,
  SqlShowIndex,
  SqlShowTables
}
import org.urbcomp.start.db.parser.driver.StartDBParseDriver
import org.urbcomp.start.db.parser.{StartDBSQLSamples, SqlHelper}
import org.urbcomp.start.db.util.{MetadataUtil, SqlParam}

import java.nio.file.Paths

class StartDBVisitorTest extends FunSuite with BeforeAndAfterEach {

  def driver: StartDBParseDriver.type = StartDBParseDriver

  def testUser = "start_db"
  def testDatabase = "default"
  def tableName = "table_name"

  override def beforeEach(): Unit = {
    SqlParam.CACHE.set(new SqlParam(testUser, testDatabase))
  }

  def calciteParse(sql: String): SqlNode = {
    val parser = SqlParser.create(sql)
    parser.parseStmt()
  }

  test("convert insert into table select ... to SqlNode") {
    val parsed = driver.parseSql("insert into table1 select * from table2")
    assertTrue(parsed.isInstanceOf[SqlInsert])
    val node = parsed.asInstanceOf[SqlInsert]
    assertTrue(node.getSource.isInstanceOf[SqlSelect])
  }

  test("convert show databases to SqlNode") {
    val parsed = driver.parseSql(StartDBSQLSamples.SHOW_DATABASES)
    assertTrue(parsed.isInstanceOf[SqlShowDatabases])
  }

  test("convert describe statement to SqlNode") {
    val parsed = driver.parseSql(StartDBSQLSamples.DESCRIBE_TABLE_SAMPLE)
    assertTrue(parsed.isInstanceOf[SqlDescribeTable])
    val node = parsed.asInstanceOf[SqlDescribeTable]
    assertEquals(3, node.getTable.names.size())
  }

  test("convert truncate table statement to SqlNode") {
    val parsed = driver.parseSql(StartDBSQLSamples.TRUNCATE_TABLE_SAMPLE)
    assertTrue(parsed.isInstanceOf[SqlTruncateTable])
  }

  test("convert show tables to SqlNode") {
    val parsed = driver.parseSql(StartDBSQLSamples.SHOW_TABLES)
    assertTrue(parsed.isInstanceOf[SqlShowTables])
    val node = parsed.asInstanceOf[SqlShowTables]
    assertEquals(MetadataUtil.combineUserDbKey(testUser, testDatabase), node.fullDatabaseName())
  }

  test("convert show index to SqlNode") {
    val parsed = driver.parseSql(StartDBSQLSamples.SHOW_INDEX)
    assertTrue(parsed.isInstanceOf[SqlShowIndex])
  }

  test("convert create database statement to SqlNode") {
    val parsed = driver.parseSql(StartDBSQLSamples.CREATE_DATABASE_SAMPLE)
    val node = parsed.asInstanceOf[SqlCreateDatabase]
    assertEquals("database_name", node.getDatabaseName.names.get(0))
    assertFalse(node.ifNotExists)
  }

  test("convert use database statement to SqlNode") {
    val parsed = driver.parseSql(StartDBSQLSamples.USE_DATABASE_SAMPLE)
    val node = parsed.asInstanceOf[SqlUseDatabase]
    assertEquals("database_name", node.getFullDatabaseName)
  }

  test("convert create database if not exists statement to SqlNode") {
    val parsed = driver.parseSql(StartDBSQLSamples.CREATE_DATABASE_IF_NOT_EXISTS_SAMPLE)
    val node = parsed.asInstanceOf[SqlCreateDatabase]
    assertEquals("database_name", node.getDatabaseName.names.get(0))
    assertTrue(node.ifNotExists)
  }

  test("convert drop database statement to SqlNode") {
    val parsed = driver.parseSql(StartDBSQLSamples.DROP_DATABASE_SAMPLE)
    val node = parsed.asInstanceOf[SqlDropSchema]
    assertEquals(SqlKind.DROP_SCHEMA, node.getKind)
    assertEquals("database_name", node.name.names.get(0))
  }

  test("convert drop table statement to SqlNode") {
    val parsed = driver.parseSql(StartDBSQLSamples.DROP_TABLE_IF_EXISTS_SAMPLE)
    val node = parsed.asInstanceOf[SqlDropTable]
    assertEquals(SqlKind.DROP_TABLE, node.getKind)
    assertEquals(tableName, node.name.names.get(0))
  }

  test("convert drop index statement to SqlNode") {
    val parsed = driver.parseSql(StartDBSQLSamples.DROP_INDEX_SAMPLE)
    val node = parsed.asInstanceOf[SqlDropIndex]
    assertEquals(SqlKind.DROP_INDEX, node.getKind)
    assertEquals("indexName", node.indexName)
    assertEquals("tableName", node.tableName.names.get(0))
  }

  test("convert delete statement to SqlNode") {
    val sql = StartDBSQLSamples.DELETE_SAMPLE
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlDelete]
    val cond = node.getCondition.asInstanceOf[SqlBasicCall]
    assertEquals(SqlKind.EQUALS, cond.getKind)
    assertEquals(2, cond.getOperands.length)
  }

  test("convert show create table statement to SqlNode") {
    val sql = StartDBSQLSamples.SHOW_CREATE_TABLE_SAMPLE
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlShowCreateTable]
    assertEquals(tableName, node.getTableName.names.get(0))
  }

  test("convert create table statement to SqlNode") {
    val sql = StartDBSQLSamples.CREATE_TABLE_SAMPLE
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlStartCreateTable]
    assertEquals("start_default_table", node.name.names.get(0))
    assertEquals(12, node.columnList.size())
  }

  test("convert create table like statement to SqlNode") {
    val sql = StartDBSQLSamples.CREATE_TABLE_LIKE_SAMPLE
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlStartCreateTableLike]
    assertEquals("target_table", node.targetTableName.names.get(0))

  }

  test("convert update table statement to SqlNode") {
    val sql = StartDBSQLSamples.UPDATE_SAMPLE
    val parser = SqlParser.create(sql)
    val calciteNode = parser.parseStmt().asInstanceOf[SqlUpdate]
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlUpdate]
    assertEquals(calciteNode.getTargetColumnList.size(), node.getTargetColumnList.size())
    assertEquals(calciteNode.getSourceExpressionList.size(), node.getSourceExpressionList.size())
  }

  test("convert create user statement to SqlNode") {
    val sql = StartDBSQLSamples.CREATE_USER_SAMPLE
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlCreateUser]
    assertEquals("test_user", node.getUserName.names.get(0))
    assertEquals("password", node.getPassword)
  }

  test("convert create table with index to SqlNode") {
    val sql = StartDBSQLSamples.CREATE_TABLE_WITH_INDEX
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlStartCreateTable]
    assertEquals(3, node.indexList.size())
    val index1 = node.indexList.get(1).asInstanceOf[SqlIndexDeclaration]
    assertEquals("spatial_index", index1.indexName.names.get(0))
    assertEquals(4, node.columnList.size())
  }

  test("invalid create table sql will fail with exception") {
    val sql = s"""CREATE TABLE gemo_%s (
                 |    name String,
                 |    st Point,
                 |    dtg Datetime
                 |    SPATIAL INDEX spatial_index2(st, dtg) type nonsense
                 |)"""
    val thrown = intercept[Exception] {
      driver.parseSql(sql)
    }
    assertTrue(thrown.getMessage.contains("[Invalid SQL]"))

    val sql2 = "CREATE TABLE IF EXISTS test_table (name String)"
    val thrown2 = intercept[Exception] {
      driver.parseSql(sql2)
    }
    assertTrue(thrown2.getMessage.contains("[Invalid SQL]"))

    val sql3 = "CREATE TABLE IF NOT EXISTS test_table (name String)"
    val node = driver.parseSql(sql3)
    assertNotNull(node)
  }

  test("convert load data sql to node and transform it to select") {
    val sql = StartDBSQLSamples.LOAD_DATA_SAMPLE
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlLoadData]
    val expectLoadSql =
      s"LOAD CSV INPATH 'HDFS://USER/DATA.CSV' TO gemo_table (road.oid _c0, name _c1, startp _c2, endp _c3, dtg to_timestamp(_c4)) WITH HEADER"
    assertEquals(expectLoadSql, SqlHelper.toSqlString(node))
    val selectNode = SqlHelper.convertToSelectNode(node, "tmp")
    val convertedSql =
      s"""SELECT _c0 AS road.oid, _c1 AS name, _c2 AS startp, _c3 AS endp, to_timestamp(_c4) AS dtg
         |FROM tmp""".stripMargin
    assertEquals(convertedSql, SqlHelper.toSqlString(selectNode))
  }

  test("convert load data with delimiter and quotes sql to node and transform it to select") {
    val sql = StartDBSQLSamples.LOAD_DATA_WITH_DELIMITER_AND_QUOTES_SAMPLE
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlLoadData]
    val expectLoadSql =
      s"LOAD CSV INPATH 'HDFS://USER/DATA.CSV' TO gemo_table (road.oid _c0, name _c1, startp _c2, endp _c3, dtg to_timestamp(_c4)) FIELDS DELIMITER ',' QUOTES " + "'\"'" + " WITH HEADER"
    assertEquals(expectLoadSql, SqlHelper.toSqlString(node))
    val selectNode = SqlHelper.convertToSelectNode(node, "tmp")
    val convertedSql =
      s"""SELECT _c0 AS road.oid, _c1 AS name, _c2 AS startp, _c3 AS endp, to_timestamp(_c4) AS dtg
         |FROM tmp""".stripMargin
    assertEquals(convertedSql, SqlHelper.toSqlString(selectNode))
  }

  test("convert load data without column mapping sql to node and transform it to select") {
    // To get path of real csv
    var path = Paths.get("").toAbsolutePath
    while (!path.endsWith("start-db")) path = path.getParent
    val vsvPath = path
      .normalize()
      .toString + "/start-db-test/start-db-test-geomesa-geotools/src/main/resources/" + "202204-citibike-tripdata_clip_slice_without_column_mapping.csv"

    // run example
    val sql = StartDBSQLSamples.LOAD_DATA_WITHOUT_COLUMN_MAPPING_SAMPLE;
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlLoadData]
    val expectLoadSql =
      s"LOAD CSV INPATH 'HDFS://USER/DATA.CSV' TO gemo_table FIELDS DELIMITER ',' QUOTES " + "'\"'" + " WITH HEADER"
    assertEquals(expectLoadSql, SqlHelper.toSqlString(node))

    // change path to real path
    node.path = vsvPath
    val selectNode = SqlHelper.convertToSelectNode(node, "tmp")
    val convertedSql =
      s"""SELECT idx AS idx, ride_id AS ride_id, rideable_type AS rideable_type
         |FROM tmp""".stripMargin
    assertEquals(convertedSql, SqlHelper.toSqlString(selectNode))
  }

  test("RENAME TABLE") {
    val sql = StartDBSQLSamples.RENAME_TABLE_SAMPLE
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlRenameTable]
    val expectRenameSql =
      s"RENAME TABLE old_name TO new_name"
    assertEquals(expectRenameSql, SqlHelper.toSqlString(node))
  }

}
