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

package org.urbcomp.cupid.db.parser.ddl;

import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.util.ImmutableNullableList;

import java.util.List;

public class SqlCupidCreateTableLike extends SqlCreate {

    public final SqlIdentifier targetTableName;

    public final SqlIdentifier sourceTableName;

    private static final SqlOperator OPERATOR = new SqlSpecialOperator(
        "CREATE TABLE",
        SqlKind.CREATE_TABLE
    );

    public SqlCupidCreateTableLike(
        SqlParserPos pos,
        boolean replace,
        boolean ifNotExists,
        SqlIdentifier targetTableName,
        SqlIdentifier sourceTableName
    ) {
        super(OPERATOR, pos, replace, ifNotExists);
        this.targetTableName = targetTableName;
        this.sourceTableName = sourceTableName;
    }

    /** Creates a SqlCreateTableLike. */

    public List<SqlNode> getOperandList() {
        return ImmutableNullableList.of(targetTableName, sourceTableName);
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("CREATE");
        writer.keyword("TABLE");
        if (ifNotExists) {
            writer.keyword("IF NOT EXISTS");
        }
        targetTableName.unparse(writer, leftPrec, rightPrec);
        writer.keyword("LIKE");
        sourceTableName.unparse(writer, leftPrec, rightPrec);

    }
}
