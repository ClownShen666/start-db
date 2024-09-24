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

package org.urbcomp.start.db.parser;

import org.apache.calcite.sql.*;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.urbcomp.start.db.parser.dcl.SqlColumnMappingDeclaration;
import org.urbcomp.start.db.parser.dcl.SqlLoadData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SqlHelper {

    /**
     * Convert to start db sql string without quote identifiers.
     */
    public static String toSqlString(SqlNode node) {
        return node.toSqlString(config -> config.withQuoteAllIdentifiers(false)).toString();
    }

    /**
     * Convert load node to select node, so it can run on spark with sql
     */
    public static SqlSelect convertToSelectNode(SqlLoadData loader, String tableName) {
        SqlParserPos pos = SqlParserPos.ZERO;

        List<SqlBasicCall> nodes = null;
        if (!loader.mappings.getList().isEmpty()) {
            nodes = new ArrayList<>(loader.mappings.getList().size());
            for (SqlNode mapping : loader.mappings.getList()) {
                SqlColumnMappingDeclaration decl = (SqlColumnMappingDeclaration) mapping;
                nodes.add(
                    new SqlBasicCall(
                        SqlStdOperatorTable.AS,
                        Arrays.asList(decl.expr, decl.field).toArray(new SqlNode[] {}),
                        pos
                    )
                );
            }
        } else {
            nodes = new ArrayList<>();
            String[] csvFields = null;
            try (
                BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Files.newInputStream(Paths.get(loader.path)))
                )
            ) {
                csvFields = reader.readLine().split(loader.delimiter);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (String csvField : Objects.requireNonNull(csvFields)) {
                nodes.add(
                    new SqlBasicCall(
                        SqlStdOperatorTable.AS,
                        Arrays.asList(
                            new SqlIdentifier(csvField, pos),
                            new SqlIdentifier(csvField, pos)
                        ).toArray(new SqlNode[] {}),
                        pos
                    )
                );
            }
        }
        SqlIdentifier from = new SqlIdentifier(Collections.singletonList(tableName), pos);
        SqlNodeList selectList = new SqlNodeList(nodes, pos);
        return new SqlSelect(
            pos,
            null,
            selectList,
            from,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

}
