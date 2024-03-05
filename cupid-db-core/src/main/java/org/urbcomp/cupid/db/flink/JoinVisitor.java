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

import java.util.*;

public class JoinVisitor extends CupidDBSqlBaseVisitor<Void> {

    private List<String> streamTableList;

    private List<String> dimensionTableList;

    // record table is stream table or dimension table
    private Map<String, String> tableType = new HashMap<>();

    private String streamJoinSql;

    private String fromTable;

    private List<Field> fieldList = new ArrayList<>();

    private List<Field> dimensionFieldList = new ArrayList<>();

    private List<Join> joinList = new ArrayList<>();

    private List<Join> dimensionJoinList = new ArrayList<>();

    private List<On> onList = new ArrayList<>();

    private List<On> streamOnList = new ArrayList<>();

    private List<On> dimensionOnList = new ArrayList<>();

    private String where = "";

    public JoinVisitor(String sql) {
        CharStream charStream = CharStreams.fromString(sql);
        CupidDBSqlLexer lexer = new CupidDBSqlLexer(charStream);
        lexer.removeErrorListeners();
        CupidDBSqlParser parser = new CupidDBSqlParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        CupidDBSqlParser.ProgramContext tree = parser.program();
        this.visitProgram(tree);

        FlinkSqlParam param = FlinkSqlParam.CACHE.get();
        streamTableList = param.getStreamTables();
        dimensionTableList = param.getDimensionTables();
        getTableType();
        if (streamTableList.size() > 1) {
            getStreamSql();
        }
        System.out.println("stream join sql：" + streamJoinSql);
    }

    @Override
    public Void visitSubselectStmt(CupidDBSqlParser.SubselectStmtContext ctx) {
        // todo: function
        // query field
        parseField(ctx.selectList().selectListItem());

        // from table
        fromTable = ctx.fromClause().fromTableClause().getText();

        // join
        if (!ctx.fromClause().fromJoinClause().isEmpty()) {
            parseJoin(ctx.fromClause().fromJoinClause());
        }

        // where
        if (ctx.whereClause() != null) {
            parseWhere(ctx.whereClause());
        }
        return null;
    }

    private void parseField(List<CupidDBSqlParser.SelectListItemContext> selectList) {
        for (CupidDBSqlParser.SelectListItemContext selectListItem : selectList) {
            String fieldStr = selectListItem.getText();
            Field field = new Field();
            if (selectListItem.selectListAlias() == null) {
                field.field = "*";
                fieldList.add(field);
            } else {
                String aliasStr = selectListItem.selectListAlias().getText();
                field.table = fieldStr.split("\\.")[0];
                field.field = fieldStr.split("\\.")[1].split(aliasStr)[0];
                field.alias = aliasStr.substring(2);
                fieldList.add(field);
            }
        }
    }

    private void parseJoin(List<CupidDBSqlParser.FromJoinClauseContext> joinClauseContexts) {
        for (CupidDBSqlParser.FromJoinClauseContext joinClauseContext : joinClauseContexts) {
            Join join = new Join();
            // join type
            if (joinClauseContext.fromJoinTypeClause() != null) {
                if (!joinClauseContext.fromJoinTypeClause().getText().equals("join")) {
                    join.type = joinClauseContext.fromJoinTypeClause().getText().split("join")[0]
                        + " join";
                }
            }
            // join table
            join.table = joinClauseContext.fromTableClause().getText();
            joinList.add(join);
            // on
            if (joinClauseContext.boolExpr().boolExpr().isEmpty()) {
                parseOn(joinClauseContext.boolExpr());
            } else {
                for (CupidDBSqlParser.BoolExprContext boolExprContext : joinClauseContext.boolExpr()
                    .boolExpr()) {
                    parseOn(boolExprContext);
                }
            }
        }
    }

    private void parseOn(CupidDBSqlParser.BoolExprContext boolExprContext) {
        if (boolExprContext.boolExprAtom().boolExprBinary() != null) {
            CupidDBSqlParser.BoolExprBinaryContext binaryContext = boolExprContext.boolExprAtom()
                .boolExprBinary();
            On on = new On();
            on.left = binaryContext.expr(0).getText();
            on.right = binaryContext.expr(1).getText();
            on.type = binaryContext.boolExprBinaryOperator().getText();
            onList.add(on);
        } else {
            throw new UnsupportedOperationException("Unsupported stream join dimension sql.");
        }
    }

    private void parseWhere(CupidDBSqlParser.WhereClauseContext whereClauseContext) {
        if (whereClauseContext.boolExpr().boolExpr().isEmpty()) {
            parseBoolExpr(whereClauseContext.boolExpr());
        } else {
            boolean first = true;
            for (CupidDBSqlParser.BoolExprContext boolExprContext : whereClauseContext.boolExpr()
                .boolExpr()) {
                if (first) {
                    first = false;
                    parseBoolExpr(boolExprContext);
                } else {
                    where = String.join(" ", where, "and");
                    parseBoolExpr(boolExprContext);
                }
            }
        }
    }

    // currently only support binary boolExpr
    private void parseBoolExpr(CupidDBSqlParser.BoolExprContext boolExprContext) {
        if (boolExprContext.boolExprAtom().boolExprBinary() != null) {
            CupidDBSqlParser.BoolExprBinaryContext binaryContext = boolExprContext.boolExprAtom()
                .boolExprBinary();
            where = String.join(
                " ",
                where,
                binaryContext.expr(0).getText(),
                binaryContext.boolExprBinaryOperator().getText(),
                binaryContext.expr(1).getText()
            );
        } else {
            throw new UnsupportedOperationException("Unsupported stream join dimension sql.");
        }
    }

    private void getStreamSql() {
        StringBuilder stringBuilder = new StringBuilder("select ");
        streamJoinSql = "select ";
        // query field
        for (Field field : fieldList) {
            if (field.field == "*") {
                stringBuilder.append("*");
            } else {
                if (tableType.get(field.table).equals("stream")) {
                    stringBuilder.append(field.table).append(".").append(field.field);
                } else {
                    dimensionFieldList.add(field);
                    continue;
                }
            }
            stringBuilder.append(", ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());

        // from table
        if (tableType.get(fromTable).equals("dimension")) {
            dimensionJoinList.add(joinList.get(0));
        }
        stringBuilder.append(" from ").append(streamTableList.get(0));

        // split stream on and dimension on
        for (On on : onList) {
            if (tableType.get(on.left.split("\\.")[0]).equals("dimension")
                || tableType.get(on.right.split("\\.")[0]).equals("dimension")) {
                dimensionOnList.add(on);
            } else {
                streamOnList.add(on);
            }
        }

        // join
        List<String> leftTable = new ArrayList<>();
        leftTable.add(streamTableList.get(0));
        for (Join join : joinList) {
            if (tableType.get(join.table).equals("stream")
                && !join.table.equals(streamTableList.get(0))) {
                stringBuilder.append(" ").append(join.type).append(" ").append(join.table);
                boolean first = true;
                // on
                for (On on : streamOnList) {
                    if (leftTable.contains(on.left.split("\\.")[0])
                        && on.right.split("\\.")[0].equals(join.table)) {
                        if (first) {
                            stringBuilder.append(" on ");
                            first = false;
                        } else {
                            stringBuilder.append(" and ");
                        }
                        stringBuilder.append(on.left)
                            .append(" ")
                            .append(on.type)
                            .append(" ")
                            .append(on.right);
                    }
                }
                leftTable.add(join.table);
            } else if (tableType.get(join.table).equals("dimension")) {
                dimensionJoinList.add(join);
            }
        }
        streamJoinSql = stringBuilder.toString();
    }

    private void getTableType() {
        for (String streamTable : streamTableList) {
            tableType.put(streamTable, "stream");
        }
        for (String dimensionTable : dimensionTableList) {
            tableType.put(dimensionTable, "dimension");
        }
    }

    private class Field {
        public String table;
        public String field;
        public String alias;
    }

    private class Join {
        public String type;
        public String table;
    }

    private class On {
        public String type;
        public String left;
        public String right;
    }

    public List<String> getStreamTableList() {
        return streamTableList;
    }

    public List<String> getDimensionTableList() {
        return dimensionTableList;
    }

    public String getStreamJoinSql() {
        return streamJoinSql;
    }

    public String getFromTable() {
        return fromTable;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public List<Field> getDimensionFieldList() {
        return dimensionFieldList;
    }

    public List<Join> getJoinList() {
        return joinList;
    }

    public List<Join> getDimensionJoinList() {
        return dimensionJoinList;
    }

    public List<On> getOnList() {
        return onList;
    }

    public List<On> getStreamOnList() {
        return streamOnList;
    }

    public List<On> getDimensionOnList() {
        return dimensionOnList;
    }

    public String getWhere() {
        return where;
    }
}
