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

import java.util.Arrays;
import java.util.List;

public class InsertIntoFieldVisitor extends StartDBSqlBaseVisitor<Void> {
    private List<String> fieldNameList = null;

    public List<String> getFieldNameList() {
        return fieldNameList;
    }

    @Override
    public Void visitInsertStmtCols(StartDBSqlParser.InsertStmtColsContext ctx) {
        String fieldStr = ctx.getText();
        fieldNameList = Arrays.asList(fieldStr.substring(1, fieldStr.length() - 1).split(","));
        return null;
    }

    public InsertIntoFieldVisitor(String sql) {
        CharStream charStream = CharStreams.fromString(sql);
        StartDBSqlLexer lexer = new StartDBSqlLexer(charStream);
        lexer.removeErrorListeners();
        StartDBSqlParser parser = new StartDBSqlParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        StartDBSqlParser.ProgramContext tree = parser.program();
        this.visitProgram(tree);
    }
}
