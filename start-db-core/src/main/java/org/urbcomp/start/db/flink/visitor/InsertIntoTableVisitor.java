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
package org.urbcomp.start.db.flink.visitor;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.urbcomp.start.db.parser.parser.StartDBSqlBaseVisitor;
import org.urbcomp.start.db.parser.parser.StartDBSqlLexer;
import org.urbcomp.start.db.parser.parser.StartDBSqlParser;
import org.urbcomp.start.db.util.SqlParam;

public class InsertIntoTableVisitor extends StartDBSqlBaseVisitor<Void> {

    private String table = null;

    private String dbTable = null;

    private StartDBSqlParser.SelectStmtContext selectStmtContext = null;

    public String getTable() {
        return table;
    }

    public String getDbTable() {
        return dbTable;
    }

    @Override
    public Void visitInsertStmt(StartDBSqlParser.InsertStmtContext ctx) {
        StartDBSqlParser.IdentContext tableNameCtx = ctx.tableName().ident();
        selectStmtContext = ctx.selectStmt();
        if (tableNameCtx.identItem().size() == 1) {
            table = tableNameCtx.getText();
            dbTable = SqlParam.CACHE.get().getDbName() + "." + tableNameCtx.getText();
        } else if (tableNameCtx.identItem().size() == 2) {
            table = tableNameCtx.identItem(0).getText() + "." + tableNameCtx.identItem(1).getText();
            dbTable = tableNameCtx.identItem(0).getText()
                + "."
                + tableNameCtx.identItem(1).getText();
        } else {
            table = tableNameCtx.identItem(0).getText()
                + "."
                + tableNameCtx.identItem(1).getText()
                + "."
                + tableNameCtx.identItem(2).getText();
            dbTable = tableNameCtx.identItem(1).getText()
                + "."
                + tableNameCtx.identItem(2).getText();
        }
        return null;
    }

    public InsertIntoTableVisitor(String sql) {
        CharStream charStream = CharStreams.fromString(sql);
        StartDBSqlLexer lexer = new StartDBSqlLexer(charStream);
        lexer.removeErrorListeners();
        StartDBSqlParser parser = new StartDBSqlParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        StartDBSqlParser.ProgramContext tree = parser.program();
        this.visitProgram(tree);
    }

    public boolean haveSelect() {
        return selectStmtContext != null;
    }
}
