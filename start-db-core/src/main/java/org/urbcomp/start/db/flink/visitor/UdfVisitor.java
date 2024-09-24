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
import org.urbcomp.start.db.udf.UdfFactory;
import scala.collection.mutable.HashSet;

public class UdfVisitor extends StartDBSqlBaseVisitor<Void> {

    private String processedSql;

    @Override
    public Void visitExprFunc(StartDBSqlParser.ExprFuncContext ctx) {
        UdfFactory udfFactory = new UdfFactory();
        HashSet<String> castToGeometryUdf = udfFactory.getCastToGeometryUdf();
        HashSet<String> castToStringUdf = udfFactory.getCastToStringUdf();
        StringBuilder origin = new StringBuilder(ctx.ident().getText()).append("(");
        StringBuilder replace = new StringBuilder(ctx.ident().getText()).append("(");
        if (!ctx.exprFuncParams().funcParam().isEmpty()) {

            // add st_castToGeometry
            if (castToGeometryUdf.contains(ctx.ident().getText())) {
                for (StartDBSqlParser.FuncParamContext funcParamContext : ctx.exprFuncParams()
                    .funcParam()) {
                    String param = funcParamContext.expr().getText();
                    origin.append(param).append(",");
                    replace.append("st_castToGeometry(").append(param).append("),");
                }
                origin.delete(origin.length() - 2, origin.length()).append(")");
                replace.delete(replace.length() - 2, replace.length()).append(")");
            }

            // add cast(param as STRING)
            if (castToStringUdf.contains(ctx.ident().getText())) {
                for (StartDBSqlParser.FuncParamContext funcParamContext : ctx.exprFuncParams()
                    .funcParam()) {
                    String param = funcParamContext.expr().getText();
                    origin.append(param).append(",");
                    replace.append("CAST(").append(param).append(" as STRING),");
                }
                origin.delete(origin.length() - 1, origin.length()).append(")");
                replace.delete(replace.length() - 2, replace.length()).append("))");
            }
        }
        processedSql = processedSql.replace(origin, replace);
        return visitChildren(ctx);
    }

    public UdfVisitor(String sql) {
        processedSql = sql.replace(", ", ",");
        CharStream charStream = CharStreams.fromString(processedSql);
        StartDBSqlLexer lexer = new StartDBSqlLexer(charStream);
        lexer.removeErrorListeners();
        StartDBSqlParser parser = new StartDBSqlParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        StartDBSqlParser.ProgramContext tree = parser.program();
        this.visitProgram(tree);
    }

    public String getProcessedSql() {
        return processedSql;
    }

}
