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
import org.urbcomp.start.db.metadata.MetadataAccessorFromDb;
import org.urbcomp.start.db.metadata.entity.Field;
import org.urbcomp.start.db.metadata.entity.Table;
import org.urbcomp.start.db.parser.parser.StartDBSqlBaseVisitor;
import org.urbcomp.start.db.parser.parser.StartDBSqlLexer;
import org.urbcomp.start.db.parser.parser.StartDBSqlParser;
import org.urbcomp.start.db.util.SqlParam;

import java.util.ArrayList;
import java.util.List;

public class SelectFieldVisitor extends StartDBSqlBaseVisitor<Void> {
    private List<String> fieldNameList = new ArrayList<>();

    private List<String> fieldTypeList = new ArrayList<>();

    @Override
    public Void visitSelectListItem(StartDBSqlParser.SelectListItemContext ctx) {
        SqlParam sqlParam = SqlParam.CACHE.get();
        if (ctx.getText().equals("*")) {
            SelectFromTableVisitor selectFromTableVisitor = new SelectFromTableVisitor(
                sqlParam.getSql()
            );
            List<String> tableNameList = selectFromTableVisitor.getTableList();
            for (String tableName : tableNameList) {
                MetadataAccessorFromDb accessor = new MetadataAccessorFromDb();
                List<Field> fieldList = accessor.getFields(
                    sqlParam.getUserName(),
                    sqlParam.getDbName(),
                    tableName
                );
                for (Field field : fieldList) {
                    fieldNameList.add(field.getName());
                    fieldTypeList.add(field.getType());
                }
            }
        } else {
            String tableName;
            String fieldName;
            if (ctx.getText().contains("\\.")) {
                tableName = ctx.getText().split("\\.")[0];
                fieldName = ctx.getText().split("\\.")[1];
            } else {
                tableName = new SelectFromTableVisitor(sqlParam.getSql()).getTableList().get(0);
                fieldName = ctx.getText();
            }
            Table table = MetadataAccessUtil.getTable(
                sqlParam.getUserName(),
                sqlParam.getDbName(),
                tableName
            );
            List<Field> fieldList = table.getFieldList();
            for (Field field : fieldList) {
                if (field.getName().equals(fieldName)) {
                    fieldNameList.add(fieldName);
                    fieldTypeList.add(field.getType());
                }
            }
        }
        return null;
    }

    public SelectFieldVisitor(String sql) {
        CharStream charStream = CharStreams.fromString(sql);
        StartDBSqlLexer lexer = new StartDBSqlLexer(charStream);
        lexer.removeErrorListeners();
        StartDBSqlParser parser = new StartDBSqlParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        StartDBSqlParser.ProgramContext tree = parser.program();
        this.visitProgram(tree);
    }

    public List<String> getFieldNameList() {
        return fieldNameList;
    }

    public List<String> getFieldTypeList() {
        return fieldTypeList;
    }
}
