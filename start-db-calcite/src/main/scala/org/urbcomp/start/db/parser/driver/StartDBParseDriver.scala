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
package org.urbcomp.start.db.parser.driver

import org.antlr.v4.runtime.{
  BaseErrorListener,
  CharStream,
  CharStreams,
  CommonTokenStream,
  RecognitionException,
  Recognizer
}
import org.apache.calcite.sql.SqlNode
import org.urbcomp.start.db.parser.parser.{StartDBSqlLexer, StartDBSqlParser}
import org.urbcomp.start.db.parser.visitor.StartDBVisitor
import org.urbcomp.start.db.util.SqlParam

/**
  * Start DB parse driver
  *
  * @author : zaiyuan
  */
object StartDBParseDriver {
  def parseSql(sql: String): SqlNode = {
    val charStream: CharStream = CharStreams.fromString(sql)
    val lexer = new StartDBSqlLexer(charStream)
    lexer.removeErrorListeners()
    val parser = new StartDBSqlParser(new CommonTokenStream(lexer))

    parser.removeErrorListeners()
    parser.addErrorListener(new BaseErrorListener() {
      override def syntaxError(
          recognizer: Recognizer[_, _],
          offendingSymbol: Any,
          line: Int,
          charPositionInLine: Int,
          msg: String,
          e: RecognitionException
      ): Unit = {
        throw new IllegalArgumentException(
          "[Invalid SQL] line " + line + ":" + charPositionInLine + " " + msg
        );
      }
    })

    val tree = parser.program()
    val param = SqlParam.CACHE.get()
    val visitor = new StartDBVisitor(param.getUserName, param.getDbName)
    visitor.visitProgram(tree)
  }
}
