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
package org.urbcomp.cupid.db.flink.visitor;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.urbcomp.cupid.db.parser.parser.CupidDBSqlBaseVisitor;
import org.urbcomp.cupid.db.parser.parser.CupidDBSqlLexer;
import org.urbcomp.cupid.db.parser.parser.CupidDBSqlParser;
import org.urbcomp.cupid.db.util.FlinkSqlParam;

import java.util.*;
import java.util.stream.Collectors;

public class JoinVisitor extends CupidDBSqlBaseVisitor<Void> {
    private String streamJoinSql;

    private String dimensionJoinSql;

    // record table is stream table or dimension table
    private Map<String, String> tableType = new HashMap<>();

    private String fromTable;

    private List<String> streamTableList;

    private List<String> dimensionTableList;

    private List<Field> streamFieldList = new ArrayList<>();

    private List<Field> dimensionFieldList = new ArrayList<>();

    private List<Join> streamJoinList = new ArrayList<>();

    private List<Join> dimensionJoinList = new ArrayList<>();

    private List<On> streamOnList = new ArrayList<>();

    private List<On> dimensionOnList = new ArrayList<>();

    private boolean isSplitOn = false;

    private String where = "";
    private final List<String> selectFiledList = new ArrayList<>();
    private final Set<String> selectBatchList;

    public List<String> getSelectFiledList() {
        List<String> res = new ArrayList<>();
        for (String s : selectFiledList) {
            if (selectBatchList.contains(s)) res.add(s);
            else res.add("streamResult." + s.split("\\.")[1]);

        }
        return res;
    }

    // initialize parameters
    public JoinVisitor(String sql) {
        FlinkSqlParam param = FlinkSqlParam.CACHE.get();
        streamTableList = param.getStreamTables();
        dimensionTableList = param.getDimensionTables();
        getTableType();

        CharStream charStream = CharStreams.fromString(sql);
        CupidDBSqlLexer lexer = new CupidDBSqlLexer(charStream);
        lexer.removeErrorListeners();
        CupidDBSqlParser parser = new CupidDBSqlParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        CupidDBSqlParser.ProgramContext tree = parser.program();
        this.visitProgram(tree);

        streamJoinSql = makeJoinSql(streamTableList, streamFieldList, streamJoinList, streamOnList);
        dimensionJoinSql = makeJoinSql(
            dimensionTableList,
            dimensionFieldList,
            dimensionJoinList,
            dimensionOnList
        ) + where;
        selectBatchList = new HashSet<>(getBatchSelectFiledList());
        this.isSplitOn = !getBatchSql().equals("");
        System.out.println("stream join sql：" + streamJoinSql);
        System.out.println("dimension join sql：" + dimensionJoinSql);
    }

    // visit sql
    @Override
    public Void visitSubselectStmt(CupidDBSqlParser.SubselectStmtContext ctx) {
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
            field.isSelect = true;
            // select *
            if (fieldStr.equals("*")) {
                field.table = "null";
                field.field = "*";
                streamFieldList.add(field);
                dimensionFieldList.add(field);
            }

            // select function(field1, field2...)
            else if (fieldStr.contains("(")) {
                field.expression = fieldStr.replaceAll(",", ", ").replace("as", " as ");
                String[] fields = fieldStr.split("\\(")[1].split("\\)")[0].split(",");
                boolean allStream = true;
                for (String fieldTable : fields) {
                    if (tableType.get(fieldTable.split("\\.")[0]).equals("dimension")) {
                        allStream = false;
                    }
                }
                if (allStream) {
                    streamFieldList.add(field);
                } else {
                    for (String fieldTable : fields) {
                        addField(fieldTable);
                    }
                }
            }

            // select field
            else {
                field.table = fieldStr.split("\\.")[0];
                field.field = fieldStr.split("\\.")[1];
                selectFiledList.add(fieldStr);
                // select field as alias
                if (selectListItem.selectListAlias() != null) {
                    String aliasStr = selectListItem.selectListAlias().getText();
                    field.field = fieldStr.split("\\.")[1].split(aliasStr)[0];
                    field.alias = aliasStr.substring(2);
                }

                if (tableType.get(field.table).equals("stream")) {
                    for (Field streamField : streamFieldList) {
                        if (field.table.equals(streamField.table)
                            && field.field.equals(streamField.field)) {
                            return;
                        }
                    }
                    streamFieldList.add(field);
                } else {
                    for (Field dimensionField : dimensionFieldList) {
                        if (field.table.equals(dimensionField.table)
                            && field.field.equals(dimensionField.field)) {
                            return;
                        }
                    }
                    dimensionFieldList.add(field);
                }
            }
        }
    }

    private void parseJoin(List<CupidDBSqlParser.FromJoinClauseContext> joinClauseContexts) {
        for (CupidDBSqlParser.FromJoinClauseContext joinClauseContext : joinClauseContexts) {
            Join join = new Join();

            // join type
            if (joinClauseContext.fromJoinTypeClause() != null) {
                String joinStr = joinClauseContext.fromJoinTypeClause().getText();
                if (joinStr.equals("join")) {
                    join.type = joinStr;
                } else {
                    join.type = joinStr.split("join")[0] + " join";
                }

                // on
                if (joinClauseContext.boolExpr().boolExpr().isEmpty()) {
                    parseOn(joinClauseContext.boolExpr());
                } else {
                    for (CupidDBSqlParser.BoolExprContext boolExprContext : joinClauseContext
                        .boolExpr()
                        .boolExpr()) {
                        parseOn(boolExprContext);
                    }
                }
            } else {
                join.type = ",";
            }

            // join table
            join.table = joinClauseContext.fromTableClause().getText();

            // classify stream join and dimension join
            if (tableType.get(join.table).equals("dimension")) {
                dimensionJoinList.add(join);
            } else if (join.table.equals(streamTableList.get(0))) {
                join.table = "streamResult";
                dimensionJoinList.add(join);
            } else if (streamTableList.size() > 1) {
                streamJoinList.add(join);
            }
        }
    }

    // currently only support binary boolExpr, and assume no function
    private void parseOn(CupidDBSqlParser.BoolExprContext boolExprContext) {
        if (boolExprContext.boolExprAtom().boolExprBinary() != null) {
            CupidDBSqlParser.BoolExprBinaryContext binaryContext = boolExprContext.boolExprAtom()
                .boolExprBinary();
            On on = new On();
            on.left = binaryContext.expr(0).getText();
            on.right = binaryContext.expr(1).getText();
            on.type = binaryContext.boolExprBinaryOperator().getText();
            if (tableType.get(on.left.split("\\.")[0]).equals("dimension")
                || tableType.get(on.right.split("\\.")[0]).equals("dimension")) {
                if (tableType.get(on.left.split("\\.")[0]).equals("stream")) {
                    addField(on.left);
                    on.left = "streamResult." + on.left.split("\\.")[1];
                }
                if (tableType.get(on.right.split("\\.")[0]).equals("stream")) {
                    addField(on.right);
                    on.right = "streamResult." + on.right.split("\\.")[1];
                }
                dimensionOnList.add(on);
            } else {
                streamOnList.add(on);
            }
            addField(on.left);
            addField(on.right);
        } else {
            throw new UnsupportedOperationException("Unsupported stream join dimension sql.");
        }
    }

    private void parseWhere(CupidDBSqlParser.WhereClauseContext whereClauseContext) {
        where = " where";
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

    // currently only support binary boolExpr and assume no function
    private void parseBoolExpr(CupidDBSqlParser.BoolExprContext boolExprContext) {
        if (boolExprContext.boolExprAtom().boolExprBinary() != null) {
            CupidDBSqlParser.BoolExprBinaryContext binaryContext = boolExprContext.boolExprAtom()
                .boolExprBinary();
            String left = binaryContext.expr(0).getText();
            String right = binaryContext.expr(1).getText();

            // replace stream table name with streamResult
            if (tableType.get(left.split("\\.")[0]).equals("stream")) {
                left = String.join(".", "streamResult", left.split("\\.")[1]);
            }
            if (tableType.get(right.split("\\.")[0]).equals("stream")) {
                right = String.join(".", "streamResult", right.split("\\.")[1]);
            }
            where = String.join(
                " ",
                where,
                left,
                binaryContext.boolExprBinaryOperator().getText(),
                right
            );
            addField(binaryContext.expr(0).getText());
            addField(binaryContext.expr(1).getText());
        } else {
            throw new UnsupportedOperationException("Unsupported stream join dimension sql.");
        }
    }

    // classify stream field and dimension field
    private void addField(String fieldStr) {
        Field field = new Field();

        // function(field1, field2...)
        if (fieldStr.contains("(")) {
            field.expression = fieldStr.replaceAll(",", ", ").replace("as", " as ");
            String[] fields = fieldStr.split("\\(")[1].split("\\)")[0].split(",");
            boolean allStream = true;
            for (String fieldTable : fields) {
                if (tableType.get(fieldTable.split("\\.")[0]).equals("dimension")) {
                    allStream = false;
                }
            }
            if (allStream) {
                streamFieldList.add(field);
            } else {
                for (String fieldTable : fields) {
                    addField(fieldTable);
                }
            }
        }

        // field
        else {
            field.table = fieldStr.split("\\.")[0];
            field.field = fieldStr.split("\\.")[1];
            if (tableType.get(field.table).equals("stream")) {
                for (Field streamField : streamFieldList) {
                    if (field.table.equals(streamField.table)
                        && field.field.equals(streamField.field)) {
                        return;
                    }
                }
                streamFieldList.add(field);
            } else {
                for (Field dimensionField : dimensionFieldList) {
                    if (field.table.equals(dimensionField.table)
                        && field.field.equals(dimensionField.field)) {
                        return;
                    }
                }

                dimensionFieldList.add(field);
            }
        }
    }

    private String makeJoinSql(
        List<String> tableList,
        List<Field> fieldList,
        List<Join> joinList,
        List<On> onList
    ) {
        StringBuilder stringBuilder = new StringBuilder("select ");

        // query field
        for (Field field : fieldList) {
            if (field.expression != null) {
                stringBuilder.append(field.expression);
            } else {
                if (field.field.equals("*")) {
                    stringBuilder.append("*");
                } else {
                    stringBuilder.append(field.table).append(".").append(field.field);
                }
            }
            stringBuilder.append(", ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());

        // from table
        stringBuilder.append(" from ").append(tableList.get(0));

        // join
        List<String> leftTable = new ArrayList<>();
        leftTable.add(tableList.get(0));
        for (Join join : joinList) {
            stringBuilder.append(" ").append(join.type).append(" ").append(join.table);
            boolean first = true;

            // on
            for (On on : onList) {
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
        }
        return stringBuilder.toString();
    }

    private void getTableType() {
        for (String streamTable : streamTableList) {
            tableType.put(streamTable, "stream");
        }
        for (String dimensionTable : dimensionTableList) {
            tableType.put(dimensionTable, "dimension");
        }
        tableType.put("streamResult", "dimension");
    }

    public class Field {
        public String table;
        public String field;
        public String alias;
        public String expression;
        public boolean isSelect = false;
    }

    public class Join {
        public String type;
        public String table;
    }

    public class On {
        public String type;
        public String left;
        public String right;
    }

    public String getStreamJoinSql() {
        return streamJoinSql;
    }

    public String getDimensionJoinSql() {
        return dimensionJoinSql;
    }

    public String getFromTable() {
        return fromTable;
    }

    public List<String> getStreamTableList() {
        return streamTableList;
    }

    public List<String> getDimensionTableList() {
        return dimensionTableList;
    }

    public List<Field> getStreamFieldList() {
        return streamFieldList;
    }

    public List<Field> getDimensionFieldList() {
        return dimensionFieldList;
    }

    public List<Join> getStreamJoinList() {
        return streamJoinList;
    }

    public List<Join> getDimensionJoinList() {
        return dimensionJoinList;
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

    public List<String> getBatchSelectFiledList() {
        return getDimensionFieldList().stream()
            .filter(s -> !"streamResult".equals(s.table))
            .map(s -> s.table + "." + s.field)
            .collect(Collectors.toList());
    }

    public List<On> getBatchOnList() {
        return getDimensionOnList().stream()
            .filter(
                s -> !"streamResult".equals(s.left.split("\\.")[0])
                    && !"streamResult".equals(s.right.split("\\.")[0])
            )
            .collect(Collectors.toList());
    }

    public List<On> getMixOnList() {
        return getDimensionOnList().stream()
            .filter(
                s -> !"streamResult".equals(s.left.split("\\.")[0]) | !"streamResult".equals(
                    s.right.split("\\.")[0]
                )
            )
            .collect(Collectors.toList());
    }

    public List<String> getBatchjoinTypeList() {
        return getDimensionJoinList().stream()
            .filter(s -> !"streamResult".equals(s.table))
            .map(s -> s.type)
            .collect(Collectors.toList());
    }

    public String getBatchSql() {
        StringBuilder batchSql = new StringBuilder("select ");
        getBatchSelectFiledList().forEach(s -> batchSql.append(s).append(","));
        batchSql.deleteCharAt(batchSql.length() - 1);
        batchSql.append(" from ");
        List<On> onList = getBatchOnList();
        if (onList.size() == 0) return "";
        List<String> joinTypeList = getBatchjoinTypeList();
        batchSql.append(onList.get(0).left.split("\\.")[0]).append(" ");
        for (int i = 0; i < onList.size(); i++) {
            batchSql.append(joinTypeList.get(0)).append(" ");
            JoinVisitor.On on = onList.get(0);
            batchSql.append(on.left)
                .append(" ")
                .append(on.type)
                .append(" ")
                .append(on.right)
                .append(" ");
        }
        return batchSql.toString();
    }

    // public String selectList(String sql) {
    // // sql.split(",")
    //
    // }

}
