/*
 * This file is inherited from Apache Calcite and modifed by ST-Lab under apache license.
 * You can find the original code from
 *
 * https://github.com/apache/calcite
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.urbcomp.start.db.parser.ddl;

import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class SqlRenameTable extends SqlCall {

    public final SqlIdentifier oldName;
    public final SqlIdentifier newName;

    private static final SqlOperator OPERATOR = new SqlSpecialOperator(
        "RENAME TABLE",
        SqlKind.OTHER_DDL
    );

    public SqlRenameTable(SqlParserPos pos, SqlIdentifier oldName, SqlIdentifier newName) {
        super(pos);
        this.oldName = Objects.requireNonNull(oldName);
        this.newName = Objects.requireNonNull(newName);
    }

    @Nonnull
    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(oldName, newName);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("RENAME");
        writer.keyword("TABLE");
        oldName.unparse(writer, leftPrec, rightPrec);
        writer.keyword("TO");
        newName.unparse(writer, leftPrec, rightPrec);
    }
}
