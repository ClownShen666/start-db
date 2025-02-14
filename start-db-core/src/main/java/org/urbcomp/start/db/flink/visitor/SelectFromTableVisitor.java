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
import org.urbcomp.start.db.metadata.MetadataAccessUtil;
import org.urbcomp.start.db.metadata.entity.Field;
import org.urbcomp.start.db.parser.parser.StartDBSqlBaseVisitor;
import org.urbcomp.start.db.parser.parser.StartDBSqlLexer;
import org.urbcomp.start.db.parser.parser.StartDBSqlParser;
import org.urbcomp.start.db.util.FlinkSqlParam;
import org.urbcomp.start.db.util.SqlParam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class SelectFromTableVisitor extends StartDBSqlBaseVisitor<Void> {

    private final List<String> tableList = new ArrayList<>();

    private final List<String> dbTableList = new ArrayList<>();

    private List<String> streamTableList = new ArrayList<>();

    private List<String> dimensionTableList = new ArrayList<>();

    private boolean unionFromPointAndTrajectory = false;

    private List<String> trajectoryTableList = new ArrayList<>();

    public List<String> getTableList() {
        return tableList;
    }

    public List<String> getDbTableList() {
        return dbTableList;
    }

    public List<String> getStreamTableList() {
        return streamTableList;
    }

    public List<String> getDimensionTableList() {
        return dimensionTableList;
    }

    public boolean isUnionFromPointAndTrajectory() {
        return unionFromPointAndTrajectory;
    }

    public List<String> getTrajectoryTableList() {
        return trajectoryTableList;
    }

    public SelectFromTableVisitor(String sql) {
        CharStream charStream = CharStreams.fromString(sql);
        StartDBSqlLexer lexer = new StartDBSqlLexer(charStream);
        lexer.removeErrorListeners();
        StartDBSqlParser parser = new StartDBSqlParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        StartDBSqlParser.ProgramContext tree = parser.program();
        this.visitProgram(tree);
    }

    @Override
    public Void visitFromTableNameClause(StartDBSqlParser.FromTableNameClauseContext ctx) {
        StartDBSqlParser.IdentContext names = ctx.tableName().ident();
        if (names.identItem().size() == 1) {
            tableList.add(names.getText());
            dbTableList.add(SqlParam.CACHE.get().getDbName() + "." + names.getText());
        } else if (names.identItem().size() == 2) {
            tableList.add(names.identItem(0).getText() + "." + names.identItem(1).getText());
            dbTableList.add(names.identItem(0).getText() + "." + names.identItem(1).getText());
        } else {
            tableList.add(
                names.identItem(0).getText()
                    + "."
                    + names.identItem(1).getText()
                    + "."
                    + names.identItem(2).getText()
            );
            dbTableList.add(names.identItem(1).getText() + "." + names.identItem(2).getText());
        }
        return null;
    }

    public void getAllTables(FlinkSqlParam param) {
        LinkedList<String> unionTables = new LinkedList<>(tableList);
        HashSet<String> historyTables = new HashSet<>();
        String user = param.getUserName();
        String db = param.getDbName();
        while (!unionTables.isEmpty()) {
            String tableName = unionTables.poll();
            // skip tables already processed
            if (historyTables.contains(tableName)) {
                break;
            }
            historyTables.add(tableName);
            org.urbcomp.start.db.metadata.entity.Table table = MetadataAccessUtil.getTable(
                user,
                db,
                tableName
            );
            if (table.getStorageEngine().equals("union")) {
                streamTableList.add(tableName);
                dimensionTableList.add(tableName);

                // union from tables
                if (table.getFromTables() != null) {
                    boolean hasPoint = false;
                    boolean hasTrajectory = false;

                    String[] tableList = table.getFromTables().split(",");
                    for (String tableName1 : tableList) {
                        // skip tables already processed
                        if (historyTables.contains(tableName1)) {
                            continue;
                        }

                        List<Field> fields = MetadataAccessUtil.getFields(
                            param.getUserName(),
                            param.getDbName(),
                            tableName1
                        );
                        for (Field field : fields) {
                            if (field.getType().equals("Point")) {
                                hasPoint = true;
                            } else if (field.getType().equals("Trajectory")) {
                                hasTrajectory = true;
                                trajectoryTableList.add(tableName1);
                            }
                        }

                        org.urbcomp.start.db.metadata.entity.Table table1 = MetadataAccessUtil
                            .getTable(user, db, tableName1);
                        if (table1.getStorageEngine().equals("kafka")) {
                            historyTables.add(tableName1);
                            streamTableList.add(tableName1);
                        } else if (table1.getStorageEngine().equals("hbase")) {
                            historyTables.add(tableName1);
                            dimensionTableList.add(tableName1);
                        } else {
                            unionTables.add(tableName1);
                        }
                    }

                    if (hasPoint && hasTrajectory) {
                        unionFromPointAndTrajectory = true;
                    }
                }
            } else if (table.getStorageEngine().equals("kafka")) {
                streamTableList.add(table.getName());
            } else {
                dimensionTableList.add(table.getName());
            }
        }
    }
}
