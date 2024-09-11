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
package org.urbcomp.start.db.spark

import org.antlr.v4.runtime.{CharStream, CharStreams, CommonTokenStream}
import org.urbcomp.start.db.parser.parser.StartDBSqlParser.FromTableNameClauseContext
import org.urbcomp.start.db.parser.parser.{StartDBSqlBaseVisitor, StartDBSqlLexer, StartDBSqlParser}
import org.urbcomp.start.db.util.SparkSqlParam

import java.util
import scala.collection.JavaConverters._
import scala.collection.mutable

class StartSparkTableExtractVisitor extends StartDBSqlBaseVisitor[Unit] {

  val tableList = new util.ArrayList[String]()

  override def visitFromTableNameClause(ctx: FromTableNameClauseContext): Unit = {
    val names = ctx.tableName().ident()
    if (names.identItem().size() == 1) {
      tableList.add(s"${SparkSqlParam.CACHE.get().getDbName}.${names.getText}")
    } else {
      tableList.add(s"${names.identItem(0).getText}.${names.identItem(1).getText}")
    }
  }
}

object StartSparkTableExtractVisitor {
  def getTableList(sql: String): mutable.Buffer[String] = {
    val charStream: CharStream = CharStreams.fromString(sql)
    val lexer = new StartDBSqlLexer(charStream)
    lexer.removeErrorListeners()
    val parser = new StartDBSqlParser(new CommonTokenStream(lexer))
    parser.removeErrorListeners()
    val tree = parser.program()
    val visitor = new StartSparkTableExtractVisitor
    visitor.visitProgram(tree)
    visitor.tableList.asScala
  }
}
