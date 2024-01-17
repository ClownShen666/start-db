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
package org.urbcomp.cupid.db.flink.util;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.urbcomp.cupid.db.parser.parser.CupidDBSqlBaseVisitor;
import org.urbcomp.cupid.db.parser.parser.CupidDBSqlLexer;
import org.urbcomp.cupid.db.parser.parser.CupidDBSqlParser;

import java.util.ArrayList;
import java.util.List;

public class SelectFromTableVisitor extends CupidDBSqlBaseVisitor<Void> {

    private final List<String> tableList = new ArrayList<>();

    private final List<String> dbTableList = new ArrayList<>();

    public List<String> getTableList() {
        return tableList;
    }

    public List<String> getDbTableList() {
        return dbTableList;
    }

    public SelectFromTableVisitor(String sql) {
        CharStream charStream = CharStreams.fromString(sql);
        CupidDBSqlLexer lexer = new CupidDBSqlLexer(charStream);
        lexer.removeErrorListeners();
        CupidDBSqlParser parser = new CupidDBSqlParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        CupidDBSqlParser.ProgramContext tree = parser.program();
        this.visitProgram(tree);
    }

    @Override
    public Void visitFromTableNameClause(CupidDBSqlParser.FromTableNameClauseContext ctx) {
        CupidDBSqlParser.IdentContext names = ctx.tableName().ident();
        if (names.identItem().size() == 1) {
            tableList.add(names.getText());
            dbTableList.add(FlinkSqlParam.CACHE.get().getDbName() + "." + names.getText());
        } else {
            tableList.add(names.identItem(0).getText() + "." + names.identItem(1).getText());
            dbTableList.add(names.identItem(0).getText() + "." + names.identItem(1).getText());
        }
        return null;
    }

    public boolean haveSelect() {
        return tableList.size() != 0;
    }
}
