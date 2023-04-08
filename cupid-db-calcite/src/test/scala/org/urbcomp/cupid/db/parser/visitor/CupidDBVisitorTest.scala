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
package org.urbcomp.cupid.db.parser.visitor
import org.apache.calcite.sql._
import org.apache.calcite.sql.ddl.{SqlDropSchema, SqlDropTable}
import org.apache.calcite.sql.parser.SqlParser
import org.junit.Assert.{assertEquals, assertFalse, assertNotNull, assertTrue}
import org.scalatest.{BeforeAndAfterEach, FunSuite, nodurations}
import org.urbcomp.cupid.db.parser.CupidDBSQLSamples
import org.urbcomp.cupid.db.parser.dcl.SqlCreateUser
import org.urbcomp.cupid.db.parser.ddl.{
  SqlCreateDatabase,
  SqlCupidCreateTable,
  SqlDropIndex,
  SqlIndexDeclaration,
  SqlTruncateTable,
  SqlUseDatabase
}
import org.urbcomp.cupid.db.parser.dql.{
  SqlShowCreateTable,
  SqlShowDatabases,
  SqlShowIndex,
  SqlShowTables
}
import org.urbcomp.cupid.db.parser.driver.CupidDBParseDriver
import org.urbcomp.cupid.db.schema.IndexType
import org.urbcomp.cupid.db.util.{MetadataUtil, SqlParam}

class CupidDBVisitorTest extends FunSuite with BeforeAndAfterEach {

  def driver = CupidDBParseDriver

  def testUser = "cupid_db";
  def testDatabase = "default";
  def tableName = "table_name";

  override def beforeEach(): Unit = {
    SqlParam.CACHE.set(new SqlParam(testUser, testDatabase))
  }

  def calciteParse(sql: String): SqlNode = {
    val parser = SqlParser.create(sql);
    parser.parseStmt();
  }

  test("convert show databases to SqlNode") {
    val parsed = driver.parseSql(CupidDBSQLSamples.SHOW_DATABASES)
    assertTrue(parsed.isInstanceOf[SqlShowDatabases])
  }

  test("convert describe statement to SqlNode") {
    val parsed = driver.parseSql(CupidDBSQLSamples.DESCRIBE_TABLE_SAMPLE)
    assertTrue(parsed.isInstanceOf[SqlDescribeTable])
    val node = parsed.asInstanceOf[SqlDescribeTable]
    assertEquals(3, node.getTable.names.size())
  }

  test("convert truncate table statement to SqlNode") {
    val parsed = driver.parseSql(CupidDBSQLSamples.TRUNCATE_TABLE_SAMPLE)
    assertTrue(parsed.isInstanceOf[SqlTruncateTable])
  }

  test("convert show tables to SqlNode") {
    val parsed = driver.parseSql(CupidDBSQLSamples.SHOW_TABLES)
    assertTrue(parsed.isInstanceOf[SqlShowTables])
    val node = parsed.asInstanceOf[SqlShowTables];
    assertEquals(MetadataUtil.combineUserDbKey(testUser, testDatabase), node.fullDatabaseName())
  }

  test("convert show index to SqlNode") {
    val parsed = driver.parseSql(CupidDBSQLSamples.SHOW_INDEX)
    assertTrue(parsed.isInstanceOf[SqlShowIndex])
  }

  test("convert create dababase statement to SqlNode") {
    val parsed = driver.parseSql(CupidDBSQLSamples.CREATE_DATABASE_SAMPLE)
    val node = parsed.asInstanceOf[SqlCreateDatabase]
    assertEquals("database_name", node.getDatabaseName.names.get(0))
    assertFalse(node.ifNotExists)
  }

  test("convert use dababase statement to SqlNode") {
    val parsed = driver.parseSql(CupidDBSQLSamples.USE_DATABASE_SAMPLE)
    val node = parsed.asInstanceOf[SqlUseDatabase]
    assertEquals("database_name", node.getFullDatabaseName);
  }

  test("convert create dababase if not exists statement to SqlNode") {
    val parsed = driver.parseSql(CupidDBSQLSamples.CREATE_DATABASE_IF_NOT_EXISTS_SAMPLE)
    val node = parsed.asInstanceOf[SqlCreateDatabase]
    assertEquals("database_name", node.getDatabaseName.names.get(0))
    assertTrue(node.ifNotExists);
  }

  test("convert drop dababase statement to SqlNode") {
    val parsed = driver.parseSql(CupidDBSQLSamples.DROP_DATABASE_SAMPLE)
    val node = parsed.asInstanceOf[SqlDropSchema]
    assertEquals(SqlKind.DROP_SCHEMA, node.getKind)
    assertEquals("database_name", node.name.names.get(0))
  }

  test("convert drop table statement to SqlNode") {
    val parsed = driver.parseSql(CupidDBSQLSamples.DROP_TABLE_IF_EXISTS_SAMPLE)
    val node = parsed.asInstanceOf[SqlDropTable]
    assertEquals(SqlKind.DROP_TABLE, node.getKind)
    assertEquals(tableName, node.name.names.get(0))
  }

  test("convert drop st-index statement to SqlNode") {
    val parsed = driver.parseSql(CupidDBSQLSamples.DROP_INDEX_SAMPLE)
    val node = parsed.asInstanceOf[SqlDropIndex]
    assertEquals(SqlKind.DROP_INDEX, node.getKind)
    assertEquals(IndexType.SPATIAL, node.indexType)
    assertEquals("indexName", node.indexName)
    assertEquals("tableName", node.tableName.names.get(0))
  }

  test("convert delete statement to SqlNode") {
    val sql = CupidDBSQLSamples.DELETE_SAMPLE
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlDelete]
    val cond = node.getCondition.asInstanceOf[SqlBasicCall]
    assertEquals(SqlKind.EQUALS, cond.getKind)
    assertEquals(2, cond.getOperands.length)
  }

  test("convert show create table statement to SqlNode") {
    val sql = CupidDBSQLSamples.SHOW_CREATE_TABLE_SAMPLE
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlShowCreateTable]
    assertEquals(tableName, node.getTableName.names.get(0));
  }

  test("convert create table statement to SqlNode") {
    val sql = CupidDBSQLSamples.CREATE_TABLE_SAMPLE
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlCupidCreateTable]
    assertEquals("start_default_table", node.name.names.get(0))
    assertEquals(12, node.columnList.size())
  }

  test("convert update table statement to SqlNode") {
    val sql = CupidDBSQLSamples.UPDATE_SAMPLE;
    val parser = SqlParser.create(sql);
    val calciteNode = parser.parseStmt().asInstanceOf[SqlUpdate];
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlUpdate];
    assertEquals(calciteNode.getTargetColumnList.size(), node.getTargetColumnList.size());
    assertEquals(calciteNode.getSourceExpressionList.size(), node.getSourceExpressionList.size());
  }

  test("convert create user statement to SqlNode") {
    val sql = CupidDBSQLSamples.CREATE_USER_SAMPLE
    val parsed = driver.parseSql(sql)
    val node = parsed.asInstanceOf[SqlCreateUser]
    assertEquals("test_user", node.getUserName.names.get(0));
    assertEquals("password", node.getPassword)
  }

  test("convert create table with index to SqlNode") {
    val sql = CupidDBSQLSamples.CREATE_TABLE_WITH_INDEX
    val parsed = driver.parseSql(sql);
    val node = parsed.asInstanceOf[SqlCupidCreateTable]
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
}
