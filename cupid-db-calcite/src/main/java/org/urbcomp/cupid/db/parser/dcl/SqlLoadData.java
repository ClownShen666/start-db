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

package org.urbcomp.cupid.db.parser.dcl;

import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParserPos;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class SqlLoadData extends SqlCall {

    public static final SqlSpecialOperator OPERATOR = new SqlSpecialOperator(
        "LOAD DATA",
        SqlKind.OTHER
    );

    public SqlIdentifier tableName;
    public String path;
    public SqlNodeList mappings;
    public String delimiter;
    public String quote;
    public boolean hasDelimiter;
    public boolean hasQuotes;
    public boolean hasHeader;

    public SqlLoadData(
        SqlParserPos pos,
        String path,
        SqlIdentifier tableName,
        SqlNodeList mappings,
        String delimiter,
        String quotes,
        boolean hasDelimiter,
        boolean hasQuotes,
        boolean hasHeader
    ) {
        super(pos);
        this.path = path;
        this.tableName = tableName;
        this.mappings = mappings;
        this.delimiter = delimiter;
        this.quote = quotes;
        this.hasDelimiter = hasDelimiter;
        this.hasQuotes = hasQuotes;
        this.hasHeader = hasHeader;
    }

    @Nonnull
    @Override
    public SqlOperator getOperator() {
        return OPERATOR;
    }

    @Nonnull
    @Override
    public List<SqlNode> getOperandList() {
        return Collections.emptyList();
    }

    @Override
    public void unparse(SqlWriter writer, int leftPrec, int rightPrec) {
        writer.keyword("LOAD CSV INPATH");
        writer.keyword("'" + path + "'");
        writer.keyword("TO");
        tableName.unparse(writer, leftPrec, rightPrec);
        if (!mappings.getList().isEmpty()) {
            SqlWriter.Frame frame = writer.startList("(", ")");
            for (SqlNode m : mappings) {
                writer.sep(",");
                m.unparse(writer, 0, 0);
            }
            writer.endList(frame);
        }
        if (hasDelimiter && hasQuotes) {
            writer.keyword("FIELDS");
        }
        if (hasDelimiter) {
            writer.keyword("DELIMITER");
            writer.keyword("'" + delimiter + "'");
        }
        if (hasQuotes) {
            writer.keyword("QUOTES");
            writer.keyword("'" + quote + "'");
        }
        if (hasHeader) {
            writer.keyword("WITH HEADER");
        } else {
            writer.keyword("WITHOUT HEADER");
        }
    }
}
