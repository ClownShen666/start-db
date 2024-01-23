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
package org.urbcomp.cupid.db.flink;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.urbcomp.cupid.db.parser.parser.CupidDBSqlBaseVisitor;
import org.urbcomp.cupid.db.parser.parser.CupidDBSqlLexer;
import org.urbcomp.cupid.db.parser.parser.CupidDBSqlParser;
import org.urbcomp.cupid.db.util.SqlParam;

public class InsertIntoTableVisitor extends CupidDBSqlBaseVisitor<Void> {

    private String table = null;

    private String dbTable = null;

    private CupidDBSqlParser.SelectStmtContext selectStmtContext = null;

    public String getTable() {
        return table;
    }

    public String getDbTable() {
        return dbTable;
    }

    @Override
    public Void visitInsertStmt(CupidDBSqlParser.InsertStmtContext ctx) {
        CupidDBSqlParser.IdentContext tableNameCtx = ctx.tableName().ident();
        selectStmtContext = ctx.selectStmt();
        if (tableNameCtx.identItem().size() == 1) {
            table = tableNameCtx.getText();
            dbTable = SqlParam.CACHE.get().getDbName() + "." + tableNameCtx.getText();
        } else {
            table = tableNameCtx.identItem(0).getText() + "." + tableNameCtx.identItem(1).getText();
            dbTable = tableNameCtx.identItem(0).getText()
                + "."
                + tableNameCtx.identItem(1).getText();
        }
        return null;
    }

    public InsertIntoTableVisitor(String sql) {
        CharStream charStream = CharStreams.fromString(sql);
        CupidDBSqlLexer lexer = new CupidDBSqlLexer(charStream);
        lexer.removeErrorListeners();
        CupidDBSqlParser parser = new CupidDBSqlParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        CupidDBSqlParser.ProgramContext tree = parser.program();
        this.visitProgram(tree);
    }

    public boolean haveSelect() {
        return selectStmtContext != null;
    }
}
