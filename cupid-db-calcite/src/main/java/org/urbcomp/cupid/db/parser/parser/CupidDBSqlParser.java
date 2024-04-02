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

package org.urbcomp.cupid.db.parser.parser;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({ "all", "warnings", "unchecked", "unused", "cast" })
public class CupidDBSqlParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
        new PredictionContextCache();
    public static final int T__0 = 1, T_ACTION = 2, T_ADD2 = 3, T_ALL = 4, T_ALLOCATE = 5, T_ALTER =
        6, T_AND = 7, T_ANSI_NULLS = 8, T_ANSI_PADDING = 9, T_AS = 10, T_ASC = 11, T_ASSOCIATE = 12,
        T_AT = 13, T_ATTRIBUTE = 14, T_AUTO = 15, T_AUTO_INCREMENT = 16, T_AVG = 17, T_BATCHSIZE =
            18, T_BEGIN = 19, T_BETWEEN = 20, T_BIGINT = 21, T_BINARY_DOUBLE = 22, T_BINARY_FLOAT =
                23, T_BINARY_INTEGER = 24, T_BIT = 25, T_BODY = 26, T_BREAK = 27, T_BUILD = 28,
        T_BY = 29, T_BYTE = 30, T_CALL = 31, T_CALLER = 32, T_CASCADE = 33, T_CASE = 34,
        T_CASESPECIFIC = 35, T_CAST = 36, T_CHAR = 37, T_CHARACTER = 38, T_CHARSET = 39, T_CLIENT =
            40, T_CLOSE = 41, T_CLUSTERED = 42, T_CMP = 43, T_COLLECT = 44, T_COLLECTION = 45,
        T_COLUMN = 46, T_COMMENT = 47, T_CONFIG = 48, T_CONSTANT = 49, T_COMMIT = 50, T_COMPRESS =
            51, T_CONCAT = 52, T_CONDITION = 53, T_CONSTRAINT = 54, T_CONTINUE = 55, T_COPY = 56,
        T_COUNT = 57, T_COUNT_BIG = 58, T_CREATE = 59, T_CREATION = 60, T_CREATOR = 61, T_CS = 62,
        T_CSV = 63, T_CURRENT = 64, T_CURRENT_SCHEMA = 65, T_CURSOR = 66, T_DATABASE = 67,
        T_DATABASES = 68, T_DATA = 69, T_DATE = 70, T_DATETIME = 71, T_DAY = 72, T_DAYS = 73,
        T_DEC = 74, T_DECIMAL = 75, T_DECLARE = 76, T_DEFAULT = 77, T_DEFERRED = 78, T_DEFINED = 79,
        T_DEFINER = 80, T_DEFINITION = 81, T_DELETE = 82, T_DELIMITED = 83, T_DELIMITER = 84,
        T_DESC = 85, T_DESCRIBE = 86, T_DIAGNOSTICS = 87, T_DIR = 88, T_DIRECTORY = 89, T_DISTINCT =
            90, T_DISTRIBUTE = 91, T_DO = 92, T_DOUBLE = 93, T_DOWNLOAD = 94, T_DROP = 95,
        T_DYNAMIC = 96, T_ELSE = 97, T_ELSEIF = 98, T_ELSIF = 99, T_ENABLE = 100, T_END = 101,
        T_ENGINE = 102, T_ESCAPED = 103, T_EXCEPT = 104, T_EXEC = 105, T_EXECUTE = 106,
        T_EXCEPTION = 107, T_EXCLUSIVE = 108, T_EXISTS = 109, T_EXIT = 110, T_FALLBACK = 111,
        T_FALSE = 112, T_FETCH = 113, T_FIELDS = 114, T_FILE = 115, T_FILES = 116, T_FLOAT = 117,
        T_FOR = 118, T_FOREIGN = 119, T_FORMAT = 120, T_FOUND = 121, T_FROM = 122, T_FULL = 123,
        T_FUNCTION = 124, T_GET = 125, T_GLOBAL = 126, T_GO = 127, T_GRANT = 128, T_GRANTS = 129,
        T_GROUP = 130, T_HANDLER = 131, T_HASH = 132, T_HAVING = 133, T_HEADER = 134, T_HOST = 135,
        T_IDENTITY = 136, T_IDENTIFIED = 137, T_IF = 138, T_IGNORE = 139, T_IMMEDIATE = 140, T_IN =
            141, T_INCLUDE = 142, T_INCREMENT = 143, T_INDEX = 144, T_INITRANS = 145, T_INNER = 146,
        T_INOUT = 147, T_INPATH = 148, T_INSERT = 149, T_INT = 150, T_INT2 = 151, T_INT4 = 152,
        T_INT8 = 153, T_INTEGER = 154, T_INTERSECT = 155, T_INTERVAL = 156, T_INTO = 157,
        T_INVOKER = 158, T_IS = 159, T_ISOPEN = 160, T_ITEMS = 161, T_JOIN = 162, T_KEEP = 163,
        T_KILL = 164, T_KEY = 165, T_KEYS = 166, T_KV_SEARCH = 167, T_LANGUAGE = 168, T_LEAVE = 169,
        T_LEFT = 170, T_LIKE = 171, T_LIMIT = 172, T_LINES = 173, T_LOAD = 174, T_LOCAL = 175,
        T_LOCATION = 176, T_LOCATOR = 177, T_LOCATORS = 178, T_LOCKS = 179, T_LOG = 180, T_LOGGED =
            181, T_LOGGING = 182, T_LOOP = 183, T_MAP = 184, T_MATCHED = 185, T_MAX = 186,
        T_MAXTRANS = 187, T_MERGE = 188, T_MESSAGE_TEXT = 189, T_MICROSECOND = 190, T_MICROSECONDS =
            191, T_MIN = 192, T_MULTISET = 193, T_NCHAR = 194, T_NEW = 195, T_NVARCHAR = 196, T_NO =
                197, T_NOCOUNT = 198, T_NOCOMPRESS = 199, T_NOLOGGING = 200, T_NONE = 201, T_NOT =
                    202, T_NOTFOUND = 203, T_NULL = 204, T_NUMERIC = 205, T_NUMBER = 206, T_OBJECT =
                        207, T_OFF = 208, T_ON = 209, T_ONLY = 210, T_OPEN = 211, T_OR = 212,
        T_ORDER = 213, T_OUT = 214, T_OUTER = 215, T_OVER = 216, T_OVERWRITE = 217, T_OWNER = 218,
        T_PACKAGE = 219, T_PARTITION = 220, T_PASSWORD = 221, T_PCTFREE = 222, T_PCTUSED = 223,
        T_PERCENT = 224, T_PLS_INTEGER = 225, T_PRECISION = 226, T_PRESERVE = 227, T_PRIVILEGES =
            228, T_PRIMARY = 229, T_PRINT = 230, T_PROC = 231, T_PROCEDURE = 232, T_PYRAMID = 233,
        T_PYRAMIDS = 234, T_QUERY = 235, T_QUERIES = 236, T_QUALIFY = 237, T_QUERY_BAND = 238,
        T_QUIT = 239, T_QUOTED_IDENTIFIER = 240, T_QUOTES = 241, T_RAISE = 242, T_REAL = 243,
        T_REFERENCES = 244, T_REGEXP = 245, T_RENAME = 246, T_REPLACE = 247, T_RESIGNAL = 248,
        T_RESTRICT = 249, T_RESULT = 250, T_RESULT_SET_LOCATOR = 251, T_RETURN = 252, T_RETURNS =
            253, T_REVERSE = 254, T_REVOKE = 255, T_RIGHT = 256, T_RLIKE = 257, T_ROLE = 258,
        T_ROLLBACK = 259, T_ROW = 260, T_ROWS = 261, T_ROWTYPE = 262, T_ROW_COUNT = 263, T_RR = 264,
        T_RS = 265, T_PWD = 266, T_TRIM = 267, T_TABLESAMPLE = 268, T_SCHEMA = 269, T_SECOND = 270,
        T_SECONDS = 271, T_SECURITY = 272, T_SEGMENT = 273, T_SEL = 274, T_SELECT = 275, T_SET =
            276, T_SESSION = 277, T_SESSIONS = 278, T_SETS = 279, T_SHOW = 280, T_SIGNAL = 281,
        T_SIMPLE_DOUBLE = 282, T_SIMPLE_FLOAT = 283, T_SIMPLE_INTEGER = 284, T_SMALLDATETIME = 285,
        T_SMALLINT = 286, T_SQL = 287, T_SQLEXCEPTION = 288, T_SQLINSERT = 289, T_SQLSTATE = 290,
        T_SQLWARNING = 291, T_SRID = 292, T_STATS = 293, T_STATISTICS = 294, T_STATUS = 295,
        T_STEP = 296, T_STORAGE = 297, T_STORE = 298, T_STORED = 299, T_STREAM = 300, T_STRING =
            301, T_SUBDIR = 302, T_SUBSTRING = 303, T_SUM = 304, T_SUMMARY = 305, T_SYS_REFCURSOR =
                306, T_SPATIAL = 307, T_TABLE = 308, T_TILE = 309, T_VIEW = 310, T_TABLES = 311,
        T_VIEWS = 312, T_TABLESPACE = 313, T_TEMPORARY = 314, T_TERMINATED = 315, T_TEXTIMAGE_ON =
            316, T_THEN = 317, T_TIMESTAMP = 318, T_TINYINT = 319, T_TITLE = 320, T_TO = 321,
        T_TOP = 322, T_TRANSACTION = 323, T_TRUE = 324, T_TRUNCATE = 325, T_TYPE = 326, T_UNION =
            327, T_UNIQUE = 328, T_UPDATE = 329, T_UR = 330, T_USE = 331, T_USING = 332, T_VALUE =
                333, T_VALUES = 334, T_VAR = 335, T_VARCHAR = 336, T_VARCHAR2 = 337, T_VARYING =
                    338, T_VOLATILE = 339, T_WHEN = 340, T_WHERE = 341, T_WHILE = 342, T_WITH = 343,
        T_WITHOUT = 344, T_WORK = 345, T_XACT_ABORT = 346, T_XML = 347, T_YES = 348,
        T_ACTIVITY_COUNT = 349, T_CUME_DIST = 350, T_CURRENT_DATE = 351, T_CURRENT_TIME = 352,
        T_PI = 353, T_CURRENT_TIMESTAMP = 354, T_CURRENT_USER = 355, T_DENSE_RANK = 356,
        T_FIRST_VALUE = 357, T_LAG = 358, T_LAST_VALUE = 359, T_LEAD = 360, T_MAX_PART_STRING = 361,
        T_MIN_PART_STRING = 362, T_MAX_PART_INT = 363, T_MIN_PART_INT = 364, T_MAX_PART_DATE = 365,
        T_MIN_PART_DATE = 366, T_PART_COUNT = 367, T_PART_LOC = 368, T_RANK = 369, T_ROW_NUMBER =
            370, T_STDEV = 371, T_SYSDATE = 372, T_VARIANCE = 373, T_USER = 374, T_METADATA = 375,
        T_LONG = 376, T_BOOLEAN = 377, T_BOOL = 378, T_BINARY = 379, T_GEOMETRY = 380, T_POINT =
            381, T_LINESTRING = 382, T_POLYGON = 383, T_MULTIPOINT = 384, T_MULTILINESTRING = 385,
        T_MULTIPOLYGON = 386, T_GEOMETRYCOLLECTION = 387, T_TRAJECTORY = 388, T_ROADSEGMENT = 389,
        T_ROADNETWORK = 390, T_ADD = 391, T_COLON = 392, T_COMMA = 393, T_PIPE = 394, T_DIV = 395,
        T_DOT = 396, T_DOT2 = 397, T_EQUAL = 398, T_EQUAL2 = 399, T_NOTEQUAL = 400, T_NOTEQUAL2 =
            401, T_GREATER = 402, T_GREATEREQUAL = 403, T_LESS = 404, T_LESSEQUAL = 405, T_MUL =
                406, T_OPEN_B = 407, T_OPEN_P = 408, T_OPEN_SB = 409, T_CLOSE_B = 410, T_CLOSE_P =
                    411, T_CLOSE_SB = 412, T_SEMICOLON = 413, T_SUB = 414, L_ID = 415, L_S_STRING =
                        416, L_D_STRING = 417, L_INT = 418, L_DEC = 419, L_WS = 420, L_M_COMMENT =
                            421, L_S_COMMENT = 422, L_FILE = 423;
    public static final int RULE_program = 0, RULE_stmt = 1, RULE_createTableLikeStmt = 2,
        RULE_dbDotTable = 3, RULE_userDotDbDotTable = 4, RULE_assignmentStmtItem = 5,
        RULE_assignmentStmtSingleItem = 6, RULE_assignmentStmtMultipleItem = 7,
        RULE_assignmentStmtSelectItem = 8, RULE_showCreateTableStmt = 9, RULE_showStatusStmt = 10,
        RULE_showIndexStmt = 11, RULE_createTableStmt = 12, RULE_createUserStmt = 13,
        RULE_create_table_definition = 14, RULE_create_table_columns = 15,
        RULE_create_table_columns_item = 16, RULE_key_list = 17, RULE_index_type_decl = 18,
        RULE_column_name = 19, RULE_create_table_column_inline_cons = 20,
        RULE_create_table_column_cons = 21, RULE_create_table_fk_action = 22,
        RULE_create_table_preoptions = 23, RULE_create_table_preoptions_item = 24,
        RULE_create_table_preoptions_td_item = 25, RULE_create_table_options = 26,
        RULE_create_table_options_item = 27, RULE_create_table_options_mysql_item = 28,
        RULE_createIndexStmt = 29, RULE_createIndexCol = 30, RULE_dropIndexStmt = 31,
        RULE_dtype_default = 32, RULE_showTablesStmt = 33, RULE_dtype_attr = 34, RULE_dtype = 35,
        RULE_dtypeLen = 36, RULE_createDatabaseStmt = 37, RULE_showDatabasesStmt = 38,
        RULE_dropDatabaseStmt = 39, RULE_createDatabaseOption = 40, RULE_dropTableStmt = 41,
        RULE_insertStmt = 42, RULE_insertStmtCols = 43, RULE_insertStmtRows = 44,
        RULE_insertStmtRow = 45, RULE_truncateStmt = 46, RULE_useStmt = 47, RULE_selectStmt = 48,
        RULE_cteSelectStmt = 49, RULE_cteSelectStmtItem = 50, RULE_cteSelectCols = 51,
        RULE_fullselectStmt = 52, RULE_fullselectStmtItem = 53, RULE_fullselectSetClause = 54,
        RULE_subselectStmt = 55, RULE_selectList = 56, RULE_selectListSet = 57,
        RULE_selectListLimit = 58, RULE_selectListItem = 59, RULE_selectListAlias = 60,
        RULE_selectListAsterisk = 61, RULE_intoClause = 62, RULE_fromClause = 63,
        RULE_fromTableClause = 64, RULE_fromTableNameClause = 65, RULE_fromSubselectClause = 66,
        RULE_fromJoinClause = 67, RULE_fromJoinTypeClause = 68, RULE_fromTableValuesClause = 69,
        RULE_fromTableValuesRow = 70, RULE_fromAliasClause = 71, RULE_tableName = 72,
        RULE_whereClause = 73, RULE_groupByClause = 74, RULE_havingClause = 75, RULE_qualifyClause =
            76, RULE_orderByClause = 77, RULE_orderByClauseItem = 78, RULE_sampleClause = 79,
        RULE_selectOptions = 80, RULE_selectOptionsItem = 81, RULE_updateStmt = 82,
        RULE_updateAssignment = 83, RULE_updateTable = 84, RULE_updateUpsert = 85, RULE_deleteStmt =
            86, RULE_deleteAlias = 87, RULE_describeStmt = 88, RULE_loadStmt = 89,
        RULE_load_mapping_columns = 90, RULE_load_mapping_items = 91, RULE_load_mapping_item = 92,
        RULE_csv_file_options = 93, RULE_csv_file_format = 94, RULE_renameTableStmt = 95,
        RULE_old_name = 96, RULE_new_name = 97, RULE_boolExpr = 98, RULE_boolExprAtom = 99,
        RULE_boolExprUnary = 100, RULE_boolExprSingleIn = 101, RULE_boolExprMultiIn = 102,
        RULE_boolExprBinary = 103, RULE_boolExprLogicalOperator = 104, RULE_boolExprBinaryOperator =
            105, RULE_expr = 106, RULE_exprAtom = 107, RULE_exprInterval = 108, RULE_intervalItem =
                109, RULE_exprConcat = 110, RULE_exprConcatItem = 111, RULE_exprCase = 112,
        RULE_exprCaseSimple = 113, RULE_exprCaseSearched = 114, RULE_exprCaseItem = 115,
        RULE_exprCursorAttribute = 116, RULE_exprAggWindowFunc = 117, RULE_exprFuncAllDistinct =
            118, RULE_exprFuncOverClause = 119, RULE_exprFuncPartitionByClause = 120,
        RULE_exprSpecFunc = 121, RULE_exprFunc = 122, RULE_exprFuncParams = 123, RULE_funcParam =
            124, RULE_table_name = 125, RULE_user_name = 126, RULE_qident = 127, RULE_dateLiteral =
                128, RULE_timestampLiteral = 129, RULE_ident = 130, RULE_identItem = 131,
        RULE_string = 132, RULE_intNumber = 133, RULE_decNumber = 134, RULE_boolLiteral = 135,
        RULE_nullConst = 136, RULE_nonReservedWords = 137;

    private static String[] makeRuleNames() {
        return new String[] {
            "program",
            "stmt",
            "createTableLikeStmt",
            "dbDotTable",
            "userDotDbDotTable",
            "assignmentStmtItem",
            "assignmentStmtSingleItem",
            "assignmentStmtMultipleItem",
            "assignmentStmtSelectItem",
            "showCreateTableStmt",
            "showStatusStmt",
            "showIndexStmt",
            "createTableStmt",
            "createUserStmt",
            "create_table_definition",
            "create_table_columns",
            "create_table_columns_item",
            "key_list",
            "index_type_decl",
            "column_name",
            "create_table_column_inline_cons",
            "create_table_column_cons",
            "create_table_fk_action",
            "create_table_preoptions",
            "create_table_preoptions_item",
            "create_table_preoptions_td_item",
            "create_table_options",
            "create_table_options_item",
            "create_table_options_mysql_item",
            "createIndexStmt",
            "createIndexCol",
            "dropIndexStmt",
            "dtype_default",
            "showTablesStmt",
            "dtype_attr",
            "dtype",
            "dtypeLen",
            "createDatabaseStmt",
            "showDatabasesStmt",
            "dropDatabaseStmt",
            "createDatabaseOption",
            "dropTableStmt",
            "insertStmt",
            "insertStmtCols",
            "insertStmtRows",
            "insertStmtRow",
            "truncateStmt",
            "useStmt",
            "selectStmt",
            "cteSelectStmt",
            "cteSelectStmtItem",
            "cteSelectCols",
            "fullselectStmt",
            "fullselectStmtItem",
            "fullselectSetClause",
            "subselectStmt",
            "selectList",
            "selectListSet",
            "selectListLimit",
            "selectListItem",
            "selectListAlias",
            "selectListAsterisk",
            "intoClause",
            "fromClause",
            "fromTableClause",
            "fromTableNameClause",
            "fromSubselectClause",
            "fromJoinClause",
            "fromJoinTypeClause",
            "fromTableValuesClause",
            "fromTableValuesRow",
            "fromAliasClause",
            "tableName",
            "whereClause",
            "groupByClause",
            "havingClause",
            "qualifyClause",
            "orderByClause",
            "orderByClauseItem",
            "sampleClause",
            "selectOptions",
            "selectOptionsItem",
            "updateStmt",
            "updateAssignment",
            "updateTable",
            "updateUpsert",
            "deleteStmt",
            "deleteAlias",
            "describeStmt",
            "loadStmt",
            "load_mapping_columns",
            "load_mapping_items",
            "load_mapping_item",
            "csv_file_options",
            "csv_file_format",
            "renameTableStmt",
            "old_name",
            "new_name",
            "boolExpr",
            "boolExprAtom",
            "boolExprUnary",
            "boolExprSingleIn",
            "boolExprMultiIn",
            "boolExprBinary",
            "boolExprLogicalOperator",
            "boolExprBinaryOperator",
            "expr",
            "exprAtom",
            "exprInterval",
            "intervalItem",
            "exprConcat",
            "exprConcatItem",
            "exprCase",
            "exprCaseSimple",
            "exprCaseSearched",
            "exprCaseItem",
            "exprCursorAttribute",
            "exprAggWindowFunc",
            "exprFuncAllDistinct",
            "exprFuncOverClause",
            "exprFuncPartitionByClause",
            "exprSpecFunc",
            "exprFunc",
            "exprFuncParams",
            "funcParam",
            "table_name",
            "user_name",
            "qident",
            "dateLiteral",
            "timestampLiteral",
            "ident",
            "identItem",
            "string",
            "intNumber",
            "decNumber",
            "boolLiteral",
            "nullConst",
            "nonReservedWords" };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames() {
        return new String[] {
            null,
            "'%'",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "'+'",
            "':'",
            "','",
            "'||'",
            "'/'",
            "'.'",
            "'..'",
            "'='",
            "'=='",
            "'<>'",
            "'!='",
            "'>'",
            "'>='",
            "'<'",
            "'<='",
            "'*'",
            "'{'",
            "'('",
            "'['",
            "'}'",
            "')'",
            "']'",
            "';'",
            "'-'" };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames() {
        return new String[] {
            null,
            null,
            "T_ACTION",
            "T_ADD2",
            "T_ALL",
            "T_ALLOCATE",
            "T_ALTER",
            "T_AND",
            "T_ANSI_NULLS",
            "T_ANSI_PADDING",
            "T_AS",
            "T_ASC",
            "T_ASSOCIATE",
            "T_AT",
            "T_ATTRIBUTE",
            "T_AUTO",
            "T_AUTO_INCREMENT",
            "T_AVG",
            "T_BATCHSIZE",
            "T_BEGIN",
            "T_BETWEEN",
            "T_BIGINT",
            "T_BINARY_DOUBLE",
            "T_BINARY_FLOAT",
            "T_BINARY_INTEGER",
            "T_BIT",
            "T_BODY",
            "T_BREAK",
            "T_BUILD",
            "T_BY",
            "T_BYTE",
            "T_CALL",
            "T_CALLER",
            "T_CASCADE",
            "T_CASE",
            "T_CASESPECIFIC",
            "T_CAST",
            "T_CHAR",
            "T_CHARACTER",
            "T_CHARSET",
            "T_CLIENT",
            "T_CLOSE",
            "T_CLUSTERED",
            "T_CMP",
            "T_COLLECT",
            "T_COLLECTION",
            "T_COLUMN",
            "T_COMMENT",
            "T_CONFIG",
            "T_CONSTANT",
            "T_COMMIT",
            "T_COMPRESS",
            "T_CONCAT",
            "T_CONDITION",
            "T_CONSTRAINT",
            "T_CONTINUE",
            "T_COPY",
            "T_COUNT",
            "T_COUNT_BIG",
            "T_CREATE",
            "T_CREATION",
            "T_CREATOR",
            "T_CS",
            "T_CSV",
            "T_CURRENT",
            "T_CURRENT_SCHEMA",
            "T_CURSOR",
            "T_DATABASE",
            "T_DATABASES",
            "T_DATA",
            "T_DATE",
            "T_DATETIME",
            "T_DAY",
            "T_DAYS",
            "T_DEC",
            "T_DECIMAL",
            "T_DECLARE",
            "T_DEFAULT",
            "T_DEFERRED",
            "T_DEFINED",
            "T_DEFINER",
            "T_DEFINITION",
            "T_DELETE",
            "T_DELIMITED",
            "T_DELIMITER",
            "T_DESC",
            "T_DESCRIBE",
            "T_DIAGNOSTICS",
            "T_DIR",
            "T_DIRECTORY",
            "T_DISTINCT",
            "T_DISTRIBUTE",
            "T_DO",
            "T_DOUBLE",
            "T_DOWNLOAD",
            "T_DROP",
            "T_DYNAMIC",
            "T_ELSE",
            "T_ELSEIF",
            "T_ELSIF",
            "T_ENABLE",
            "T_END",
            "T_ENGINE",
            "T_ESCAPED",
            "T_EXCEPT",
            "T_EXEC",
            "T_EXECUTE",
            "T_EXCEPTION",
            "T_EXCLUSIVE",
            "T_EXISTS",
            "T_EXIT",
            "T_FALLBACK",
            "T_FALSE",
            "T_FETCH",
            "T_FIELDS",
            "T_FILE",
            "T_FILES",
            "T_FLOAT",
            "T_FOR",
            "T_FOREIGN",
            "T_FORMAT",
            "T_FOUND",
            "T_FROM",
            "T_FULL",
            "T_FUNCTION",
            "T_GET",
            "T_GLOBAL",
            "T_GO",
            "T_GRANT",
            "T_GRANTS",
            "T_GROUP",
            "T_HANDLER",
            "T_HASH",
            "T_HAVING",
            "T_HEADER",
            "T_HOST",
            "T_IDENTITY",
            "T_IDENTIFIED",
            "T_IF",
            "T_IGNORE",
            "T_IMMEDIATE",
            "T_IN",
            "T_INCLUDE",
            "T_INCREMENT",
            "T_INDEX",
            "T_INITRANS",
            "T_INNER",
            "T_INOUT",
            "T_INPATH",
            "T_INSERT",
            "T_INT",
            "T_INT2",
            "T_INT4",
            "T_INT8",
            "T_INTEGER",
            "T_INTERSECT",
            "T_INTERVAL",
            "T_INTO",
            "T_INVOKER",
            "T_IS",
            "T_ISOPEN",
            "T_ITEMS",
            "T_JOIN",
            "T_KEEP",
            "T_KILL",
            "T_KEY",
            "T_KEYS",
            "T_KV_SEARCH",
            "T_LANGUAGE",
            "T_LEAVE",
            "T_LEFT",
            "T_LIKE",
            "T_LIMIT",
            "T_LINES",
            "T_LOAD",
            "T_LOCAL",
            "T_LOCATION",
            "T_LOCATOR",
            "T_LOCATORS",
            "T_LOCKS",
            "T_LOG",
            "T_LOGGED",
            "T_LOGGING",
            "T_LOOP",
            "T_MAP",
            "T_MATCHED",
            "T_MAX",
            "T_MAXTRANS",
            "T_MERGE",
            "T_MESSAGE_TEXT",
            "T_MICROSECOND",
            "T_MICROSECONDS",
            "T_MIN",
            "T_MULTISET",
            "T_NCHAR",
            "T_NEW",
            "T_NVARCHAR",
            "T_NO",
            "T_NOCOUNT",
            "T_NOCOMPRESS",
            "T_NOLOGGING",
            "T_NONE",
            "T_NOT",
            "T_NOTFOUND",
            "T_NULL",
            "T_NUMERIC",
            "T_NUMBER",
            "T_OBJECT",
            "T_OFF",
            "T_ON",
            "T_ONLY",
            "T_OPEN",
            "T_OR",
            "T_ORDER",
            "T_OUT",
            "T_OUTER",
            "T_OVER",
            "T_OVERWRITE",
            "T_OWNER",
            "T_PACKAGE",
            "T_PARTITION",
            "T_PASSWORD",
            "T_PCTFREE",
            "T_PCTUSED",
            "T_PERCENT",
            "T_PLS_INTEGER",
            "T_PRECISION",
            "T_PRESERVE",
            "T_PRIVILEGES",
            "T_PRIMARY",
            "T_PRINT",
            "T_PROC",
            "T_PROCEDURE",
            "T_PYRAMID",
            "T_PYRAMIDS",
            "T_QUERY",
            "T_QUERIES",
            "T_QUALIFY",
            "T_QUERY_BAND",
            "T_QUIT",
            "T_QUOTED_IDENTIFIER",
            "T_QUOTES",
            "T_RAISE",
            "T_REAL",
            "T_REFERENCES",
            "T_REGEXP",
            "T_RENAME",
            "T_REPLACE",
            "T_RESIGNAL",
            "T_RESTRICT",
            "T_RESULT",
            "T_RESULT_SET_LOCATOR",
            "T_RETURN",
            "T_RETURNS",
            "T_REVERSE",
            "T_REVOKE",
            "T_RIGHT",
            "T_RLIKE",
            "T_ROLE",
            "T_ROLLBACK",
            "T_ROW",
            "T_ROWS",
            "T_ROWTYPE",
            "T_ROW_COUNT",
            "T_RR",
            "T_RS",
            "T_PWD",
            "T_TRIM",
            "T_TABLESAMPLE",
            "T_SCHEMA",
            "T_SECOND",
            "T_SECONDS",
            "T_SECURITY",
            "T_SEGMENT",
            "T_SEL",
            "T_SELECT",
            "T_SET",
            "T_SESSION",
            "T_SESSIONS",
            "T_SETS",
            "T_SHOW",
            "T_SIGNAL",
            "T_SIMPLE_DOUBLE",
            "T_SIMPLE_FLOAT",
            "T_SIMPLE_INTEGER",
            "T_SMALLDATETIME",
            "T_SMALLINT",
            "T_SQL",
            "T_SQLEXCEPTION",
            "T_SQLINSERT",
            "T_SQLSTATE",
            "T_SQLWARNING",
            "T_SRID",
            "T_STATS",
            "T_STATISTICS",
            "T_STATUS",
            "T_STEP",
            "T_STORAGE",
            "T_STORE",
            "T_STORED",
            "T_STREAM",
            "T_STRING",
            "T_SUBDIR",
            "T_SUBSTRING",
            "T_SUM",
            "T_SUMMARY",
            "T_SYS_REFCURSOR",
            "T_SPATIAL",
            "T_TABLE",
            "T_TILE",
            "T_VIEW",
            "T_TABLES",
            "T_VIEWS",
            "T_TABLESPACE",
            "T_TEMPORARY",
            "T_TERMINATED",
            "T_TEXTIMAGE_ON",
            "T_THEN",
            "T_TIMESTAMP",
            "T_TINYINT",
            "T_TITLE",
            "T_TO",
            "T_TOP",
            "T_TRANSACTION",
            "T_TRUE",
            "T_TRUNCATE",
            "T_TYPE",
            "T_UNION",
            "T_UNIQUE",
            "T_UPDATE",
            "T_UR",
            "T_USE",
            "T_USING",
            "T_VALUE",
            "T_VALUES",
            "T_VAR",
            "T_VARCHAR",
            "T_VARCHAR2",
            "T_VARYING",
            "T_VOLATILE",
            "T_WHEN",
            "T_WHERE",
            "T_WHILE",
            "T_WITH",
            "T_WITHOUT",
            "T_WORK",
            "T_XACT_ABORT",
            "T_XML",
            "T_YES",
            "T_ACTIVITY_COUNT",
            "T_CUME_DIST",
            "T_CURRENT_DATE",
            "T_CURRENT_TIME",
            "T_PI",
            "T_CURRENT_TIMESTAMP",
            "T_CURRENT_USER",
            "T_DENSE_RANK",
            "T_FIRST_VALUE",
            "T_LAG",
            "T_LAST_VALUE",
            "T_LEAD",
            "T_MAX_PART_STRING",
            "T_MIN_PART_STRING",
            "T_MAX_PART_INT",
            "T_MIN_PART_INT",
            "T_MAX_PART_DATE",
            "T_MIN_PART_DATE",
            "T_PART_COUNT",
            "T_PART_LOC",
            "T_RANK",
            "T_ROW_NUMBER",
            "T_STDEV",
            "T_SYSDATE",
            "T_VARIANCE",
            "T_USER",
            "T_METADATA",
            "T_LONG",
            "T_BOOLEAN",
            "T_BOOL",
            "T_BINARY",
            "T_GEOMETRY",
            "T_POINT",
            "T_LINESTRING",
            "T_POLYGON",
            "T_MULTIPOINT",
            "T_MULTILINESTRING",
            "T_MULTIPOLYGON",
            "T_GEOMETRYCOLLECTION",
            "T_TRAJECTORY",
            "T_ROADSEGMENT",
            "T_ROADNETWORK",
            "T_ADD",
            "T_COLON",
            "T_COMMA",
            "T_PIPE",
            "T_DIV",
            "T_DOT",
            "T_DOT2",
            "T_EQUAL",
            "T_EQUAL2",
            "T_NOTEQUAL",
            "T_NOTEQUAL2",
            "T_GREATER",
            "T_GREATEREQUAL",
            "T_LESS",
            "T_LESSEQUAL",
            "T_MUL",
            "T_OPEN_B",
            "T_OPEN_P",
            "T_OPEN_SB",
            "T_CLOSE_B",
            "T_CLOSE_P",
            "T_CLOSE_SB",
            "T_SEMICOLON",
            "T_SUB",
            "L_ID",
            "L_S_STRING",
            "L_D_STRING",
            "L_INT",
            "L_DEC",
            "L_WS",
            "L_M_COMMENT",
            "L_S_COMMENT",
            "L_FILE" };
    }

    private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;
    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "CupidDBSql.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public CupidDBSqlParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class ProgramContext extends ParserRuleContext {
        public StmtContext stmt() {
            return getRuleContext(StmtContext.class, 0);
        }

        public TerminalNode EOF() {
            return getToken(CupidDBSqlParser.EOF, 0);
        }

        public TerminalNode T_SEMICOLON() {
            return getToken(CupidDBSqlParser.T_SEMICOLON, 0);
        }

        public ProgramContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_program;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitProgram(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ProgramContext program() throws RecognitionException {
        ProgramContext _localctx = new ProgramContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_program);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(276);
                stmt();
                setState(278);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_SEMICOLON) {
                    {
                        setState(277);
                        match(T_SEMICOLON);
                    }
                }

                setState(280);
                match(EOF);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class StmtContext extends ParserRuleContext {
        public CreateDatabaseStmtContext createDatabaseStmt() {
            return getRuleContext(CreateDatabaseStmtContext.class, 0);
        }

        public CreateTableStmtContext createTableStmt() {
            return getRuleContext(CreateTableStmtContext.class, 0);
        }

        public CreateTableLikeStmtContext createTableLikeStmt() {
            return getRuleContext(CreateTableLikeStmtContext.class, 0);
        }

        public CreateIndexStmtContext createIndexStmt() {
            return getRuleContext(CreateIndexStmtContext.class, 0);
        }

        public DescribeStmtContext describeStmt() {
            return getRuleContext(DescribeStmtContext.class, 0);
        }

        public DropDatabaseStmtContext dropDatabaseStmt() {
            return getRuleContext(DropDatabaseStmtContext.class, 0);
        }

        public DropTableStmtContext dropTableStmt() {
            return getRuleContext(DropTableStmtContext.class, 0);
        }

        public DropIndexStmtContext dropIndexStmt() {
            return getRuleContext(DropIndexStmtContext.class, 0);
        }

        public TruncateStmtContext truncateStmt() {
            return getRuleContext(TruncateStmtContext.class, 0);
        }

        public UseStmtContext useStmt() {
            return getRuleContext(UseStmtContext.class, 0);
        }

        public ShowDatabasesStmtContext showDatabasesStmt() {
            return getRuleContext(ShowDatabasesStmtContext.class, 0);
        }

        public ShowTablesStmtContext showTablesStmt() {
            return getRuleContext(ShowTablesStmtContext.class, 0);
        }

        public ShowCreateTableStmtContext showCreateTableStmt() {
            return getRuleContext(ShowCreateTableStmtContext.class, 0);
        }

        public ShowStatusStmtContext showStatusStmt() {
            return getRuleContext(ShowStatusStmtContext.class, 0);
        }

        public ShowIndexStmtContext showIndexStmt() {
            return getRuleContext(ShowIndexStmtContext.class, 0);
        }

        public InsertStmtContext insertStmt() {
            return getRuleContext(InsertStmtContext.class, 0);
        }

        public UpdateStmtContext updateStmt() {
            return getRuleContext(UpdateStmtContext.class, 0);
        }

        public DeleteStmtContext deleteStmt() {
            return getRuleContext(DeleteStmtContext.class, 0);
        }

        public SelectStmtContext selectStmt() {
            return getRuleContext(SelectStmtContext.class, 0);
        }

        public CreateUserStmtContext createUserStmt() {
            return getRuleContext(CreateUserStmtContext.class, 0);
        }

        public LoadStmtContext loadStmt() {
            return getRuleContext(LoadStmtContext.class, 0);
        }

        public RenameTableStmtContext renameTableStmt() {
            return getRuleContext(RenameTableStmtContext.class, 0);
        }

        public StmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_stmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final StmtContext stmt() throws RecognitionException {
        StmtContext _localctx = new StmtContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_stmt);
        try {
            setState(304);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 1, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(282);
                    createDatabaseStmt();
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(283);
                    createTableStmt();
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(284);
                    createTableLikeStmt();
                }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4); {
                    setState(285);
                    createIndexStmt();
                }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5); {
                    setState(286);
                    describeStmt();
                }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6); {
                    setState(287);
                    dropDatabaseStmt();
                }
                    break;
                case 7:
                    enterOuterAlt(_localctx, 7); {
                    setState(288);
                    dropTableStmt();
                }
                    break;
                case 8:
                    enterOuterAlt(_localctx, 8); {
                    setState(289);
                    dropIndexStmt();
                }
                    break;
                case 9:
                    enterOuterAlt(_localctx, 9); {
                    setState(290);
                    truncateStmt();
                }
                    break;
                case 10:
                    enterOuterAlt(_localctx, 10); {
                    setState(291);
                    useStmt();
                }
                    break;
                case 11:
                    enterOuterAlt(_localctx, 11); {
                    setState(292);
                    showDatabasesStmt();
                }
                    break;
                case 12:
                    enterOuterAlt(_localctx, 12); {
                    setState(293);
                    showTablesStmt();
                }
                    break;
                case 13:
                    enterOuterAlt(_localctx, 13); {
                    setState(294);
                    showCreateTableStmt();
                }
                    break;
                case 14:
                    enterOuterAlt(_localctx, 14); {
                    setState(295);
                    showStatusStmt();
                }
                    break;
                case 15:
                    enterOuterAlt(_localctx, 15); {
                    setState(296);
                    showIndexStmt();
                }
                    break;
                case 16:
                    enterOuterAlt(_localctx, 16); {
                    setState(297);
                    insertStmt();
                }
                    break;
                case 17:
                    enterOuterAlt(_localctx, 17); {
                    setState(298);
                    updateStmt();
                }
                    break;
                case 18:
                    enterOuterAlt(_localctx, 18); {
                    setState(299);
                    deleteStmt();
                }
                    break;
                case 19:
                    enterOuterAlt(_localctx, 19); {
                    setState(300);
                    selectStmt();
                }
                    break;
                case 20:
                    enterOuterAlt(_localctx, 20); {
                    setState(301);
                    createUserStmt();
                }
                    break;
                case 21:
                    enterOuterAlt(_localctx, 21); {
                    setState(302);
                    loadStmt();
                }
                    break;
                case 22:
                    enterOuterAlt(_localctx, 22); {
                    setState(303);
                    renameTableStmt();
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CreateTableLikeStmtContext extends ParserRuleContext {
        public TerminalNode T_CREATE() {
            return getToken(CupidDBSqlParser.T_CREATE, 0);
        }

        public TerminalNode T_TABLE() {
            return getToken(CupidDBSqlParser.T_TABLE, 0);
        }

        public List<Table_nameContext> table_name() {
            return getRuleContexts(Table_nameContext.class);
        }

        public Table_nameContext table_name(int i) {
            return getRuleContext(Table_nameContext.class, i);
        }

        public TerminalNode T_LIKE() {
            return getToken(CupidDBSqlParser.T_LIKE, 0);
        }

        public TerminalNode T_IF() {
            return getToken(CupidDBSqlParser.T_IF, 0);
        }

        public TerminalNode T_NOT() {
            return getToken(CupidDBSqlParser.T_NOT, 0);
        }

        public TerminalNode T_EXISTS() {
            return getToken(CupidDBSqlParser.T_EXISTS, 0);
        }

        public CreateTableLikeStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_createTableLikeStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreateTableLikeStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CreateTableLikeStmtContext createTableLikeStmt() throws RecognitionException {
        CreateTableLikeStmtContext _localctx = new CreateTableLikeStmtContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_createTableLikeStmt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(306);
                match(T_CREATE);
                setState(307);
                match(T_TABLE);
                setState(311);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 2, _ctx)) {
                    case 1: {
                        setState(308);
                        match(T_IF);
                        setState(309);
                        match(T_NOT);
                        setState(310);
                        match(T_EXISTS);
                    }
                        break;
                }
                setState(313);
                table_name();
                setState(314);
                match(T_LIKE);
                setState(315);
                table_name();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DbDotTableContext extends ParserRuleContext {
        public Token db;
        public Token table;

        public List<TerminalNode> T_MUL() {
            return getTokens(CupidDBSqlParser.T_MUL);
        }

        public TerminalNode T_MUL(int i) {
            return getToken(CupidDBSqlParser.T_MUL, i);
        }

        public List<TerminalNode> T_DEFAULT() {
            return getTokens(CupidDBSqlParser.T_DEFAULT);
        }

        public TerminalNode T_DEFAULT(int i) {
            return getToken(CupidDBSqlParser.T_DEFAULT, i);
        }

        public List<TerminalNode> L_ID() {
            return getTokens(CupidDBSqlParser.L_ID);
        }

        public TerminalNode L_ID(int i) {
            return getToken(CupidDBSqlParser.L_ID, i);
        }

        public TerminalNode T_DOT() {
            return getToken(CupidDBSqlParser.T_DOT, 0);
        }

        public DbDotTableContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_dbDotTable;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDbDotTable(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DbDotTableContext dbDotTable() throws RecognitionException {
        DbDotTableContext _localctx = new DbDotTableContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_dbDotTable);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(319);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 3, _ctx)) {
                    case 1: {
                        setState(317);
                        ((DbDotTableContext) _localctx).db = _input.LT(1);
                        _la = _input.LA(1);
                        if (!(_la == T_DEFAULT || _la == T_MUL || _la == L_ID)) {
                            ((DbDotTableContext) _localctx).db = (Token) _errHandler.recoverInline(
                                this
                            );
                        } else {
                            if (_input.LA(1) == Token.EOF) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                        setState(318);
                        match(T_DOT);
                    }
                        break;
                }
                setState(321);
                ((DbDotTableContext) _localctx).table = _input.LT(1);
                _la = _input.LA(1);
                if (!(_la == T_DEFAULT || _la == T_MUL || _la == L_ID)) {
                    ((DbDotTableContext) _localctx).table = (Token) _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class UserDotDbDotTableContext extends ParserRuleContext {
        public Token user;

        public DbDotTableContext dbDotTable() {
            return getRuleContext(DbDotTableContext.class, 0);
        }

        public TerminalNode T_DOT() {
            return getToken(CupidDBSqlParser.T_DOT, 0);
        }

        public TerminalNode L_ID() {
            return getToken(CupidDBSqlParser.L_ID, 0);
        }

        public UserDotDbDotTableContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_userDotDbDotTable;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitUserDotDbDotTable(this);
            else return visitor.visitChildren(this);
        }
    }

    public final UserDotDbDotTableContext userDotDbDotTable() throws RecognitionException {
        UserDotDbDotTableContext _localctx = new UserDotDbDotTableContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_userDotDbDotTable);
        try {
            setState(329);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 5, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(323);
                    dbDotTable();
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(326);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 4, _ctx)) {
                        case 1: {
                            setState(324);
                            ((UserDotDbDotTableContext) _localctx).user = match(L_ID);
                            setState(325);
                            match(T_DOT);
                        }
                            break;
                    }
                    setState(328);
                    dbDotTable();
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class AssignmentStmtItemContext extends ParserRuleContext {
        public AssignmentStmtSingleItemContext assignmentStmtSingleItem() {
            return getRuleContext(AssignmentStmtSingleItemContext.class, 0);
        }

        public AssignmentStmtMultipleItemContext assignmentStmtMultipleItem() {
            return getRuleContext(AssignmentStmtMultipleItemContext.class, 0);
        }

        public AssignmentStmtSelectItemContext assignmentStmtSelectItem() {
            return getRuleContext(AssignmentStmtSelectItemContext.class, 0);
        }

        public AssignmentStmtItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_assignmentStmtItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitAssignmentStmtItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final AssignmentStmtItemContext assignmentStmtItem() throws RecognitionException {
        AssignmentStmtItemContext _localctx = new AssignmentStmtItemContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_assignmentStmtItem);
        try {
            setState(334);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 6, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(331);
                    assignmentStmtSingleItem();
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(332);
                    assignmentStmtMultipleItem();
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(333);
                    assignmentStmtSelectItem();
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class AssignmentStmtSingleItemContext extends ParserRuleContext {
        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_EQUAL() {
            return getToken(CupidDBSqlParser.T_EQUAL, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode T_COLON() {
            return getToken(CupidDBSqlParser.T_COLON, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public AssignmentStmtSingleItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_assignmentStmtSingleItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitAssignmentStmtSingleItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final AssignmentStmtSingleItemContext assignmentStmtSingleItem()
        throws RecognitionException {
        AssignmentStmtSingleItemContext _localctx = new AssignmentStmtSingleItemContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 12, RULE_assignmentStmtSingleItem);
        int _la;
        try {
            setState(352);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_ACTION:
                case T_ADD2:
                case T_ALL:
                case T_ALLOCATE:
                case T_ALTER:
                case T_AND:
                case T_ANSI_NULLS:
                case T_ANSI_PADDING:
                case T_AS:
                case T_ASC:
                case T_ASSOCIATE:
                case T_AT:
                case T_AVG:
                case T_BATCHSIZE:
                case T_BEGIN:
                case T_BETWEEN:
                case T_BIGINT:
                case T_BINARY_DOUBLE:
                case T_BINARY_FLOAT:
                case T_BIT:
                case T_BODY:
                case T_BREAK:
                case T_BY:
                case T_BYTE:
                case T_CALL:
                case T_CALLER:
                case T_CASCADE:
                case T_CASE:
                case T_CASESPECIFIC:
                case T_CAST:
                case T_CHAR:
                case T_CHARACTER:
                case T_CHARSET:
                case T_CLIENT:
                case T_CLOSE:
                case T_CLUSTERED:
                case T_CMP:
                case T_COLLECT:
                case T_COLLECTION:
                case T_COLUMN:
                case T_COMMENT:
                case T_CONSTANT:
                case T_COMMIT:
                case T_COMPRESS:
                case T_CONCAT:
                case T_CONDITION:
                case T_CONSTRAINT:
                case T_CONTINUE:
                case T_COPY:
                case T_COUNT:
                case T_COUNT_BIG:
                case T_CREATION:
                case T_CREATOR:
                case T_CS:
                case T_CURRENT:
                case T_CURRENT_SCHEMA:
                case T_CURSOR:
                case T_DATABASE:
                case T_DATA:
                case T_DATE:
                case T_DATETIME:
                case T_DAY:
                case T_DAYS:
                case T_DEC:
                case T_DECIMAL:
                case T_DECLARE:
                case T_DEFAULT:
                case T_DEFERRED:
                case T_DEFINED:
                case T_DEFINER:
                case T_DEFINITION:
                case T_DELETE:
                case T_DELIMITED:
                case T_DELIMITER:
                case T_DESC:
                case T_DESCRIBE:
                case T_DIAGNOSTICS:
                case T_DIR:
                case T_DIRECTORY:
                case T_DISTINCT:
                case T_DISTRIBUTE:
                case T_DO:
                case T_DOUBLE:
                case T_DOWNLOAD:
                case T_DROP:
                case T_DYNAMIC:
                case T_ENABLE:
                case T_ENGINE:
                case T_ESCAPED:
                case T_EXCEPT:
                case T_EXEC:
                case T_EXECUTE:
                case T_EXCEPTION:
                case T_EXCLUSIVE:
                case T_EXISTS:
                case T_EXIT:
                case T_FALLBACK:
                case T_FALSE:
                case T_FETCH:
                case T_FIELDS:
                case T_FILE:
                case T_FILES:
                case T_FLOAT:
                case T_FOR:
                case T_FOREIGN:
                case T_FORMAT:
                case T_FOUND:
                case T_FROM:
                case T_FULL:
                case T_FUNCTION:
                case T_GET:
                case T_GLOBAL:
                case T_GO:
                case T_GRANT:
                case T_GROUP:
                case T_HANDLER:
                case T_HASH:
                case T_HAVING:
                case T_HOST:
                case T_IDENTITY:
                case T_IF:
                case T_IGNORE:
                case T_IMMEDIATE:
                case T_IN:
                case T_INCLUDE:
                case T_INDEX:
                case T_INITRANS:
                case T_INNER:
                case T_INOUT:
                case T_INSERT:
                case T_INT:
                case T_INT2:
                case T_INT4:
                case T_INT8:
                case T_INTEGER:
                case T_INTERSECT:
                case T_INTERVAL:
                case T_INTO:
                case T_INVOKER:
                case T_IS:
                case T_ISOPEN:
                case T_ITEMS:
                case T_JOIN:
                case T_KEEP:
                case T_KEY:
                case T_KEYS:
                case T_LANGUAGE:
                case T_LEAVE:
                case T_LEFT:
                case T_LIKE:
                case T_LIMIT:
                case T_LINES:
                case T_LOCAL:
                case T_LOCATION:
                case T_LOCATOR:
                case T_LOCATORS:
                case T_LOCKS:
                case T_LOG:
                case T_LOGGED:
                case T_LOGGING:
                case T_LOOP:
                case T_MAP:
                case T_MATCHED:
                case T_MAX:
                case T_MAXTRANS:
                case T_MERGE:
                case T_MESSAGE_TEXT:
                case T_MICROSECOND:
                case T_MICROSECONDS:
                case T_MIN:
                case T_MULTISET:
                case T_NCHAR:
                case T_NEW:
                case T_NVARCHAR:
                case T_NO:
                case T_NOCOUNT:
                case T_NOCOMPRESS:
                case T_NOLOGGING:
                case T_NONE:
                case T_NOT:
                case T_NOTFOUND:
                case T_NUMERIC:
                case T_NUMBER:
                case T_OBJECT:
                case T_OFF:
                case T_ON:
                case T_ONLY:
                case T_OPEN:
                case T_OR:
                case T_ORDER:
                case T_OUT:
                case T_OUTER:
                case T_OVER:
                case T_OVERWRITE:
                case T_OWNER:
                case T_PACKAGE:
                case T_PARTITION:
                case T_PCTFREE:
                case T_PCTUSED:
                case T_PRECISION:
                case T_PRESERVE:
                case T_PRIMARY:
                case T_PRINT:
                case T_PROC:
                case T_PROCEDURE:
                case T_QUALIFY:
                case T_QUERY_BAND:
                case T_QUIT:
                case T_QUOTED_IDENTIFIER:
                case T_QUOTES:
                case T_RAISE:
                case T_REAL:
                case T_REFERENCES:
                case T_REGEXP:
                case T_REPLACE:
                case T_RESIGNAL:
                case T_RESTRICT:
                case T_RESULT:
                case T_RESULT_SET_LOCATOR:
                case T_RETURN:
                case T_RETURNS:
                case T_REVERSE:
                case T_RIGHT:
                case T_RLIKE:
                case T_ROLE:
                case T_ROLLBACK:
                case T_ROW:
                case T_ROWS:
                case T_ROW_COUNT:
                case T_RR:
                case T_RS:
                case T_PWD:
                case T_TRIM:
                case T_SCHEMA:
                case T_SECOND:
                case T_SECONDS:
                case T_SECURITY:
                case T_SEGMENT:
                case T_SEL:
                case T_SELECT:
                case T_SET:
                case T_SESSION:
                case T_SESSIONS:
                case T_SETS:
                case T_SIGNAL:
                case T_SIMPLE_DOUBLE:
                case T_SIMPLE_FLOAT:
                case T_SMALLDATETIME:
                case T_SMALLINT:
                case T_SQL:
                case T_SQLEXCEPTION:
                case T_SQLINSERT:
                case T_SQLSTATE:
                case T_SQLWARNING:
                case T_STATS:
                case T_STATISTICS:
                case T_STEP:
                case T_STORAGE:
                case T_STORE:
                case T_STORED:
                case T_STREAM:
                case T_STRING:
                case T_SUBDIR:
                case T_SUBSTRING:
                case T_SUM:
                case T_SUMMARY:
                case T_SYS_REFCURSOR:
                case T_TABLE:
                case T_TABLESPACE:
                case T_TEMPORARY:
                case T_TERMINATED:
                case T_TEXTIMAGE_ON:
                case T_THEN:
                case T_TIMESTAMP:
                case T_TITLE:
                case T_TO:
                case T_TOP:
                case T_TRANSACTION:
                case T_TRUE:
                case T_TRUNCATE:
                case T_UNIQUE:
                case T_UPDATE:
                case T_UR:
                case T_USE:
                case T_USING:
                case T_VALUE:
                case T_VALUES:
                case T_VAR:
                case T_VARCHAR:
                case T_VARCHAR2:
                case T_VARYING:
                case T_VOLATILE:
                case T_WHILE:
                case T_WITH:
                case T_WITHOUT:
                case T_WORK:
                case T_XACT_ABORT:
                case T_XML:
                case T_YES:
                case T_ACTIVITY_COUNT:
                case T_CUME_DIST:
                case T_CURRENT_DATE:
                case T_CURRENT_TIME:
                case T_PI:
                case T_CURRENT_TIMESTAMP:
                case T_CURRENT_USER:
                case T_DENSE_RANK:
                case T_FIRST_VALUE:
                case T_LAG:
                case T_LAST_VALUE:
                case T_LEAD:
                case T_PART_COUNT:
                case T_PART_LOC:
                case T_RANK:
                case T_ROW_NUMBER:
                case T_STDEV:
                case T_SYSDATE:
                case T_VARIANCE:
                case T_USER:
                case T_SUB:
                case L_ID:
                    enterOuterAlt(_localctx, 1); {
                    setState(336);
                    ident();
                    setState(338);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COLON) {
                        {
                            setState(337);
                            match(T_COLON);
                        }
                    }

                    setState(340);
                    match(T_EQUAL);
                    setState(341);
                    expr(0);
                }
                    break;
                case T_OPEN_P:
                    enterOuterAlt(_localctx, 2); {
                    setState(343);
                    match(T_OPEN_P);
                    setState(344);
                    ident();
                    setState(345);
                    match(T_CLOSE_P);
                    setState(347);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COLON) {
                        {
                            setState(346);
                            match(T_COLON);
                        }
                    }

                    setState(349);
                    match(T_EQUAL);
                    setState(350);
                    expr(0);
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class AssignmentStmtMultipleItemContext extends ParserRuleContext {
        public List<TerminalNode> T_OPEN_P() {
            return getTokens(CupidDBSqlParser.T_OPEN_P);
        }

        public TerminalNode T_OPEN_P(int i) {
            return getToken(CupidDBSqlParser.T_OPEN_P, i);
        }

        public List<IdentContext> ident() {
            return getRuleContexts(IdentContext.class);
        }

        public IdentContext ident(int i) {
            return getRuleContext(IdentContext.class, i);
        }

        public List<TerminalNode> T_CLOSE_P() {
            return getTokens(CupidDBSqlParser.T_CLOSE_P);
        }

        public TerminalNode T_CLOSE_P(int i) {
            return getToken(CupidDBSqlParser.T_CLOSE_P, i);
        }

        public TerminalNode T_EQUAL() {
            return getToken(CupidDBSqlParser.T_EQUAL, 0);
        }

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public TerminalNode T_COLON() {
            return getToken(CupidDBSqlParser.T_COLON, 0);
        }

        public AssignmentStmtMultipleItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_assignmentStmtMultipleItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitAssignmentStmtMultipleItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final AssignmentStmtMultipleItemContext assignmentStmtMultipleItem()
        throws RecognitionException {
        AssignmentStmtMultipleItemContext _localctx = new AssignmentStmtMultipleItemContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 14, RULE_assignmentStmtMultipleItem);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(354);
                match(T_OPEN_P);
                setState(355);
                ident();
                setState(360);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(356);
                            match(T_COMMA);
                            setState(357);
                            ident();
                        }
                    }
                    setState(362);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(363);
                match(T_CLOSE_P);
                setState(365);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_COLON) {
                    {
                        setState(364);
                        match(T_COLON);
                    }
                }

                setState(367);
                match(T_EQUAL);
                setState(368);
                match(T_OPEN_P);
                setState(369);
                expr(0);
                setState(374);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(370);
                            match(T_COMMA);
                            setState(371);
                            expr(0);
                        }
                    }
                    setState(376);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(377);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class AssignmentStmtSelectItemContext extends ParserRuleContext {
        public TerminalNode T_EQUAL() {
            return getToken(CupidDBSqlParser.T_EQUAL, 0);
        }

        public List<TerminalNode> T_OPEN_P() {
            return getTokens(CupidDBSqlParser.T_OPEN_P);
        }

        public TerminalNode T_OPEN_P(int i) {
            return getToken(CupidDBSqlParser.T_OPEN_P, i);
        }

        public SelectStmtContext selectStmt() {
            return getRuleContext(SelectStmtContext.class, 0);
        }

        public List<TerminalNode> T_CLOSE_P() {
            return getTokens(CupidDBSqlParser.T_CLOSE_P);
        }

        public TerminalNode T_CLOSE_P(int i) {
            return getToken(CupidDBSqlParser.T_CLOSE_P, i);
        }

        public List<IdentContext> ident() {
            return getRuleContexts(IdentContext.class);
        }

        public IdentContext ident(int i) {
            return getRuleContext(IdentContext.class, i);
        }

        public TerminalNode T_COLON() {
            return getToken(CupidDBSqlParser.T_COLON, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public AssignmentStmtSelectItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_assignmentStmtSelectItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitAssignmentStmtSelectItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final AssignmentStmtSelectItemContext assignmentStmtSelectItem()
        throws RecognitionException {
        AssignmentStmtSelectItemContext _localctx = new AssignmentStmtSelectItemContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 16, RULE_assignmentStmtSelectItem);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(391);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case T_ACTION:
                    case T_ADD2:
                    case T_ALL:
                    case T_ALLOCATE:
                    case T_ALTER:
                    case T_AND:
                    case T_ANSI_NULLS:
                    case T_ANSI_PADDING:
                    case T_AS:
                    case T_ASC:
                    case T_ASSOCIATE:
                    case T_AT:
                    case T_AVG:
                    case T_BATCHSIZE:
                    case T_BEGIN:
                    case T_BETWEEN:
                    case T_BIGINT:
                    case T_BINARY_DOUBLE:
                    case T_BINARY_FLOAT:
                    case T_BIT:
                    case T_BODY:
                    case T_BREAK:
                    case T_BY:
                    case T_BYTE:
                    case T_CALL:
                    case T_CALLER:
                    case T_CASCADE:
                    case T_CASE:
                    case T_CASESPECIFIC:
                    case T_CAST:
                    case T_CHAR:
                    case T_CHARACTER:
                    case T_CHARSET:
                    case T_CLIENT:
                    case T_CLOSE:
                    case T_CLUSTERED:
                    case T_CMP:
                    case T_COLLECT:
                    case T_COLLECTION:
                    case T_COLUMN:
                    case T_COMMENT:
                    case T_CONSTANT:
                    case T_COMMIT:
                    case T_COMPRESS:
                    case T_CONCAT:
                    case T_CONDITION:
                    case T_CONSTRAINT:
                    case T_CONTINUE:
                    case T_COPY:
                    case T_COUNT:
                    case T_COUNT_BIG:
                    case T_CREATION:
                    case T_CREATOR:
                    case T_CS:
                    case T_CURRENT:
                    case T_CURRENT_SCHEMA:
                    case T_CURSOR:
                    case T_DATABASE:
                    case T_DATA:
                    case T_DATE:
                    case T_DATETIME:
                    case T_DAY:
                    case T_DAYS:
                    case T_DEC:
                    case T_DECIMAL:
                    case T_DECLARE:
                    case T_DEFAULT:
                    case T_DEFERRED:
                    case T_DEFINED:
                    case T_DEFINER:
                    case T_DEFINITION:
                    case T_DELETE:
                    case T_DELIMITED:
                    case T_DELIMITER:
                    case T_DESC:
                    case T_DESCRIBE:
                    case T_DIAGNOSTICS:
                    case T_DIR:
                    case T_DIRECTORY:
                    case T_DISTINCT:
                    case T_DISTRIBUTE:
                    case T_DO:
                    case T_DOUBLE:
                    case T_DOWNLOAD:
                    case T_DROP:
                    case T_DYNAMIC:
                    case T_ENABLE:
                    case T_ENGINE:
                    case T_ESCAPED:
                    case T_EXCEPT:
                    case T_EXEC:
                    case T_EXECUTE:
                    case T_EXCEPTION:
                    case T_EXCLUSIVE:
                    case T_EXISTS:
                    case T_EXIT:
                    case T_FALLBACK:
                    case T_FALSE:
                    case T_FETCH:
                    case T_FIELDS:
                    case T_FILE:
                    case T_FILES:
                    case T_FLOAT:
                    case T_FOR:
                    case T_FOREIGN:
                    case T_FORMAT:
                    case T_FOUND:
                    case T_FROM:
                    case T_FULL:
                    case T_FUNCTION:
                    case T_GET:
                    case T_GLOBAL:
                    case T_GO:
                    case T_GRANT:
                    case T_GROUP:
                    case T_HANDLER:
                    case T_HASH:
                    case T_HAVING:
                    case T_HOST:
                    case T_IDENTITY:
                    case T_IF:
                    case T_IGNORE:
                    case T_IMMEDIATE:
                    case T_IN:
                    case T_INCLUDE:
                    case T_INDEX:
                    case T_INITRANS:
                    case T_INNER:
                    case T_INOUT:
                    case T_INSERT:
                    case T_INT:
                    case T_INT2:
                    case T_INT4:
                    case T_INT8:
                    case T_INTEGER:
                    case T_INTERSECT:
                    case T_INTERVAL:
                    case T_INTO:
                    case T_INVOKER:
                    case T_IS:
                    case T_ISOPEN:
                    case T_ITEMS:
                    case T_JOIN:
                    case T_KEEP:
                    case T_KEY:
                    case T_KEYS:
                    case T_LANGUAGE:
                    case T_LEAVE:
                    case T_LEFT:
                    case T_LIKE:
                    case T_LIMIT:
                    case T_LINES:
                    case T_LOCAL:
                    case T_LOCATION:
                    case T_LOCATOR:
                    case T_LOCATORS:
                    case T_LOCKS:
                    case T_LOG:
                    case T_LOGGED:
                    case T_LOGGING:
                    case T_LOOP:
                    case T_MAP:
                    case T_MATCHED:
                    case T_MAX:
                    case T_MAXTRANS:
                    case T_MERGE:
                    case T_MESSAGE_TEXT:
                    case T_MICROSECOND:
                    case T_MICROSECONDS:
                    case T_MIN:
                    case T_MULTISET:
                    case T_NCHAR:
                    case T_NEW:
                    case T_NVARCHAR:
                    case T_NO:
                    case T_NOCOUNT:
                    case T_NOCOMPRESS:
                    case T_NOLOGGING:
                    case T_NONE:
                    case T_NOT:
                    case T_NOTFOUND:
                    case T_NUMERIC:
                    case T_NUMBER:
                    case T_OBJECT:
                    case T_OFF:
                    case T_ON:
                    case T_ONLY:
                    case T_OPEN:
                    case T_OR:
                    case T_ORDER:
                    case T_OUT:
                    case T_OUTER:
                    case T_OVER:
                    case T_OVERWRITE:
                    case T_OWNER:
                    case T_PACKAGE:
                    case T_PARTITION:
                    case T_PCTFREE:
                    case T_PCTUSED:
                    case T_PRECISION:
                    case T_PRESERVE:
                    case T_PRIMARY:
                    case T_PRINT:
                    case T_PROC:
                    case T_PROCEDURE:
                    case T_QUALIFY:
                    case T_QUERY_BAND:
                    case T_QUIT:
                    case T_QUOTED_IDENTIFIER:
                    case T_QUOTES:
                    case T_RAISE:
                    case T_REAL:
                    case T_REFERENCES:
                    case T_REGEXP:
                    case T_REPLACE:
                    case T_RESIGNAL:
                    case T_RESTRICT:
                    case T_RESULT:
                    case T_RESULT_SET_LOCATOR:
                    case T_RETURN:
                    case T_RETURNS:
                    case T_REVERSE:
                    case T_RIGHT:
                    case T_RLIKE:
                    case T_ROLE:
                    case T_ROLLBACK:
                    case T_ROW:
                    case T_ROWS:
                    case T_ROW_COUNT:
                    case T_RR:
                    case T_RS:
                    case T_PWD:
                    case T_TRIM:
                    case T_SCHEMA:
                    case T_SECOND:
                    case T_SECONDS:
                    case T_SECURITY:
                    case T_SEGMENT:
                    case T_SEL:
                    case T_SELECT:
                    case T_SET:
                    case T_SESSION:
                    case T_SESSIONS:
                    case T_SETS:
                    case T_SIGNAL:
                    case T_SIMPLE_DOUBLE:
                    case T_SIMPLE_FLOAT:
                    case T_SMALLDATETIME:
                    case T_SMALLINT:
                    case T_SQL:
                    case T_SQLEXCEPTION:
                    case T_SQLINSERT:
                    case T_SQLSTATE:
                    case T_SQLWARNING:
                    case T_STATS:
                    case T_STATISTICS:
                    case T_STEP:
                    case T_STORAGE:
                    case T_STORE:
                    case T_STORED:
                    case T_STREAM:
                    case T_STRING:
                    case T_SUBDIR:
                    case T_SUBSTRING:
                    case T_SUM:
                    case T_SUMMARY:
                    case T_SYS_REFCURSOR:
                    case T_TABLE:
                    case T_TABLESPACE:
                    case T_TEMPORARY:
                    case T_TERMINATED:
                    case T_TEXTIMAGE_ON:
                    case T_THEN:
                    case T_TIMESTAMP:
                    case T_TITLE:
                    case T_TO:
                    case T_TOP:
                    case T_TRANSACTION:
                    case T_TRUE:
                    case T_TRUNCATE:
                    case T_UNIQUE:
                    case T_UPDATE:
                    case T_UR:
                    case T_USE:
                    case T_USING:
                    case T_VALUE:
                    case T_VALUES:
                    case T_VAR:
                    case T_VARCHAR:
                    case T_VARCHAR2:
                    case T_VARYING:
                    case T_VOLATILE:
                    case T_WHILE:
                    case T_WITH:
                    case T_WITHOUT:
                    case T_WORK:
                    case T_XACT_ABORT:
                    case T_XML:
                    case T_YES:
                    case T_ACTIVITY_COUNT:
                    case T_CUME_DIST:
                    case T_CURRENT_DATE:
                    case T_CURRENT_TIME:
                    case T_PI:
                    case T_CURRENT_TIMESTAMP:
                    case T_CURRENT_USER:
                    case T_DENSE_RANK:
                    case T_FIRST_VALUE:
                    case T_LAG:
                    case T_LAST_VALUE:
                    case T_LEAD:
                    case T_PART_COUNT:
                    case T_PART_LOC:
                    case T_RANK:
                    case T_ROW_NUMBER:
                    case T_STDEV:
                    case T_SYSDATE:
                    case T_VARIANCE:
                    case T_USER:
                    case T_SUB:
                    case L_ID: {
                        setState(379);
                        ident();
                    }
                        break;
                    case T_OPEN_P: {
                        {
                            setState(380);
                            match(T_OPEN_P);
                            setState(381);
                            ident();
                            setState(386);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            while (_la == T_COMMA) {
                                {
                                    {
                                        setState(382);
                                        match(T_COMMA);
                                        setState(383);
                                        ident();
                                    }
                                }
                                setState(388);
                                _errHandler.sync(this);
                                _la = _input.LA(1);
                            }
                            setState(389);
                            match(T_CLOSE_P);
                        }
                    }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
                setState(394);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_COLON) {
                    {
                        setState(393);
                        match(T_COLON);
                    }
                }

                setState(396);
                match(T_EQUAL);
                setState(397);
                match(T_OPEN_P);
                setState(398);
                selectStmt();
                setState(399);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ShowCreateTableStmtContext extends ParserRuleContext {
        public IdentContext relation;

        public TerminalNode T_SHOW() {
            return getToken(CupidDBSqlParser.T_SHOW, 0);
        }

        public TerminalNode T_CREATE() {
            return getToken(CupidDBSqlParser.T_CREATE, 0);
        }

        public TerminalNode T_TABLE() {
            return getToken(CupidDBSqlParser.T_TABLE, 0);
        }

        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public ShowCreateTableStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_showCreateTableStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitShowCreateTableStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ShowCreateTableStmtContext showCreateTableStmt() throws RecognitionException {
        ShowCreateTableStmtContext _localctx = new ShowCreateTableStmtContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_showCreateTableStmt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(401);
                match(T_SHOW);
                setState(402);
                match(T_CREATE);
                setState(403);
                match(T_TABLE);
                setState(404);
                ((ShowCreateTableStmtContext) _localctx).relation = ident();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ShowStatusStmtContext extends ParserRuleContext {
        public TerminalNode T_SHOW() {
            return getToken(CupidDBSqlParser.T_SHOW, 0);
        }

        public TerminalNode T_STATUS() {
            return getToken(CupidDBSqlParser.T_STATUS, 0);
        }

        public ShowStatusStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_showStatusStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitShowStatusStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ShowStatusStmtContext showStatusStmt() throws RecognitionException {
        ShowStatusStmtContext _localctx = new ShowStatusStmtContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_showStatusStmt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(406);
                match(T_SHOW);
                setState(407);
                match(T_STATUS);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ShowIndexStmtContext extends ParserRuleContext {
        public Token dbName;

        public TerminalNode T_SHOW() {
            return getToken(CupidDBSqlParser.T_SHOW, 0);
        }

        public TerminalNode T_INDEX() {
            return getToken(CupidDBSqlParser.T_INDEX, 0);
        }

        public List<TerminalNode> T_FROM() {
            return getTokens(CupidDBSqlParser.T_FROM);
        }

        public TerminalNode T_FROM(int i) {
            return getToken(CupidDBSqlParser.T_FROM, i);
        }

        public TableNameContext tableName() {
            return getRuleContext(TableNameContext.class, 0);
        }

        public TerminalNode L_ID() {
            return getToken(CupidDBSqlParser.L_ID, 0);
        }

        public ShowIndexStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_showIndexStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitShowIndexStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ShowIndexStmtContext showIndexStmt() throws RecognitionException {
        ShowIndexStmtContext _localctx = new ShowIndexStmtContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_showIndexStmt);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(409);
                match(T_SHOW);
                setState(410);
                match(T_INDEX);
                setState(411);
                match(T_FROM);
                setState(412);
                tableName();
                setState(415);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_FROM) {
                    {
                        setState(413);
                        match(T_FROM);
                        setState(414);
                        ((ShowIndexStmtContext) _localctx).dbName = match(L_ID);
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CreateTableStmtContext extends ParserRuleContext {
        public TerminalNode T_CREATE() {
            return getToken(CupidDBSqlParser.T_CREATE, 0);
        }

        public TerminalNode T_TABLE() {
            return getToken(CupidDBSqlParser.T_TABLE, 0);
        }

        public Table_nameContext table_name() {
            return getRuleContext(Table_nameContext.class, 0);
        }

        public Create_table_definitionContext create_table_definition() {
            return getRuleContext(Create_table_definitionContext.class, 0);
        }

        public TerminalNode T_STREAM() {
            return getToken(CupidDBSqlParser.T_STREAM, 0);
        }

        public TerminalNode T_IF() {
            return getToken(CupidDBSqlParser.T_IF, 0);
        }

        public TerminalNode T_NOT() {
            return getToken(CupidDBSqlParser.T_NOT, 0);
        }

        public TerminalNode T_EXISTS() {
            return getToken(CupidDBSqlParser.T_EXISTS, 0);
        }

        public Create_table_preoptionsContext create_table_preoptions() {
            return getRuleContext(Create_table_preoptionsContext.class, 0);
        }

        public TerminalNode T_UNION() {
            return getToken(CupidDBSqlParser.T_UNION, 0);
        }

        public FromClauseContext fromClause() {
            return getRuleContext(FromClauseContext.class, 0);
        }

        public CreateTableStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_createTableStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreateTableStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CreateTableStmtContext createTableStmt() throws RecognitionException {
        CreateTableStmtContext _localctx = new CreateTableStmtContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_createTableStmt);
        int _la;
        try {
            setState(446);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 22, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(417);
                    match(T_CREATE);
                    setState(419);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_STREAM) {
                        {
                            setState(418);
                            match(T_STREAM);
                        }
                    }

                    setState(421);
                    match(T_TABLE);
                    setState(425);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 18, _ctx)) {
                        case 1: {
                            setState(422);
                            match(T_IF);
                            setState(423);
                            match(T_NOT);
                            setState(424);
                            match(T_EXISTS);
                        }
                            break;
                    }
                    setState(427);
                    table_name();
                    setState(429);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COMMA) {
                        {
                            setState(428);
                            create_table_preoptions();
                        }
                    }

                    setState(431);
                    create_table_definition();
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(433);
                    match(T_CREATE);
                    setState(434);
                    match(T_UNION);
                    setState(435);
                    match(T_TABLE);
                    setState(439);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 20, _ctx)) {
                        case 1: {
                            setState(436);
                            match(T_IF);
                            setState(437);
                            match(T_NOT);
                            setState(438);
                            match(T_EXISTS);
                        }
                            break;
                    }
                    setState(441);
                    table_name();
                    setState(444);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case T_AS:
                        case T_SEL:
                        case T_SELECT:
                        case T_WITH:
                        case T_OPEN_P: {
                            setState(442);
                            create_table_definition();
                        }
                            break;
                        case T_FROM: {
                            setState(443);
                            fromClause();
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                    }
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CreateUserStmtContext extends ParserRuleContext {
        public StringContext password;

        public TerminalNode T_CREATE() {
            return getToken(CupidDBSqlParser.T_CREATE, 0);
        }

        public TerminalNode T_USER() {
            return getToken(CupidDBSqlParser.T_USER, 0);
        }

        public User_nameContext user_name() {
            return getRuleContext(User_nameContext.class, 0);
        }

        public TerminalNode T_IDENTIFIED() {
            return getToken(CupidDBSqlParser.T_IDENTIFIED, 0);
        }

        public TerminalNode T_BY() {
            return getToken(CupidDBSqlParser.T_BY, 0);
        }

        public StringContext string() {
            return getRuleContext(StringContext.class, 0);
        }

        public CreateUserStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_createUserStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreateUserStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CreateUserStmtContext createUserStmt() throws RecognitionException {
        CreateUserStmtContext _localctx = new CreateUserStmtContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_createUserStmt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(448);
                match(T_CREATE);
                setState(449);
                match(T_USER);
                setState(450);
                user_name();
                setState(451);
                match(T_IDENTIFIED);
                setState(452);
                match(T_BY);
                setState(453);
                ((CreateUserStmtContext) _localctx).password = string();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Create_table_definitionContext extends ParserRuleContext {
        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public SelectStmtContext selectStmt() {
            return getRuleContext(SelectStmtContext.class, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public Create_table_columnsContext create_table_columns() {
            return getRuleContext(Create_table_columnsContext.class, 0);
        }

        public Create_table_optionsContext create_table_options() {
            return getRuleContext(Create_table_optionsContext.class, 0);
        }

        public TerminalNode T_AS() {
            return getToken(CupidDBSqlParser.T_AS, 0);
        }

        public Create_table_definitionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_create_table_definition;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreate_table_definition(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Create_table_definitionContext create_table_definition()
        throws RecognitionException {
        Create_table_definitionContext _localctx = new Create_table_definitionContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 28, RULE_create_table_definition);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(470);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 25, _ctx)) {
                    case 1: {
                        setState(456);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if (_la == T_AS) {
                            {
                                setState(455);
                                match(T_AS);
                            }
                        }

                        setState(458);
                        match(T_OPEN_P);
                        setState(459);
                        selectStmt();
                        setState(460);
                        match(T_CLOSE_P);
                    }
                        break;
                    case 2: {
                        setState(463);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if (_la == T_AS) {
                            {
                                setState(462);
                                match(T_AS);
                            }
                        }

                        setState(465);
                        selectStmt();
                    }
                        break;
                    case 3: {
                        setState(466);
                        match(T_OPEN_P);
                        setState(467);
                        create_table_columns();
                        setState(468);
                        match(T_CLOSE_P);
                    }
                        break;
                }
                setState(473);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if ((((_la) & ~0x3f) == 0
                    && ((1L << _la) & ((1L << T_AUTO_INCREMENT) | (1L << T_CHARACTER) | (1L
                        << T_CHARSET) | (1L << T_COMMENT))) != 0)
                    || _la == T_DEFAULT
                    || _la == T_ENGINE
                    || _la == T_ON) {
                    {
                        setState(472);
                        create_table_options();
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Create_table_columnsContext extends ParserRuleContext {
        public List<Create_table_columns_itemContext> create_table_columns_item() {
            return getRuleContexts(Create_table_columns_itemContext.class);
        }

        public Create_table_columns_itemContext create_table_columns_item(int i) {
            return getRuleContext(Create_table_columns_itemContext.class, i);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public Create_table_columnsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_create_table_columns;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreate_table_columns(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Create_table_columnsContext create_table_columns() throws RecognitionException {
        Create_table_columnsContext _localctx = new Create_table_columnsContext(_ctx, getState());
        enterRule(_localctx, 30, RULE_create_table_columns);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(475);
                create_table_columns_item();
                setState(480);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(476);
                            match(T_COMMA);
                            setState(477);
                            create_table_columns_item();
                        }
                    }
                    setState(482);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Create_table_columns_itemContext extends ParserRuleContext {
        public Column_nameContext column_name() {
            return getRuleContext(Column_nameContext.class, 0);
        }

        public DtypeContext dtype() {
            return getRuleContext(DtypeContext.class, 0);
        }

        public List<Dtype_attrContext> dtype_attr() {
            return getRuleContexts(Dtype_attrContext.class);
        }

        public Dtype_attrContext dtype_attr(int i) {
            return getRuleContext(Dtype_attrContext.class, i);
        }

        public List<Create_table_column_inline_consContext> create_table_column_inline_cons() {
            return getRuleContexts(Create_table_column_inline_consContext.class);
        }

        public Create_table_column_inline_consContext create_table_column_inline_cons(int i) {
            return getRuleContext(Create_table_column_inline_consContext.class, i);
        }

        public Create_table_column_consContext create_table_column_cons() {
            return getRuleContext(Create_table_column_consContext.class, 0);
        }

        public TerminalNode T_CONSTRAINT() {
            return getToken(CupidDBSqlParser.T_CONSTRAINT, 0);
        }

        public QidentContext qident() {
            return getRuleContext(QidentContext.class, 0);
        }

        public TerminalNode T_INDEX() {
            return getToken(CupidDBSqlParser.T_INDEX, 0);
        }

        public Key_listContext key_list() {
            return getRuleContext(Key_listContext.class, 0);
        }

        public TerminalNode T_SPATIAL() {
            return getToken(CupidDBSqlParser.T_SPATIAL, 0);
        }

        public TerminalNode T_ATTRIBUTE() {
            return getToken(CupidDBSqlParser.T_ATTRIBUTE, 0);
        }

        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public Index_type_declContext index_type_decl() {
            return getRuleContext(Index_type_declContext.class, 0);
        }

        public Create_table_columns_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_create_table_columns_item;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreate_table_columns_item(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Create_table_columns_itemContext create_table_columns_item()
        throws RecognitionException {
        Create_table_columns_itemContext _localctx = new Create_table_columns_itemContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 32, RULE_create_table_columns_item);
        int _la;
        try {
            int _alt;
            setState(511);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 33, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(483);
                    column_name();
                    setState(484);
                    dtype();
                    setState(488);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 28, _ctx);
                    while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                        if (_alt == 1) {
                            {
                                {
                                    setState(485);
                                    dtype_attr();
                                }
                            }
                        }
                        setState(490);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 28, _ctx);
                    }
                    setState(494);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == T_AUTO_INCREMENT
                        || ((((_la - 77)) & ~0x3f) == 0
                            && ((1L << (_la - 77)) & ((1L << (T_DEFAULT - 77)) | (1L << (T_ENABLE
                                - 77)) | (1L << (T_IDENTITY - 77)))) != 0)
                        || ((((_la - 202)) & ~0x3f) == 0
                            && ((1L << (_la - 202)) & ((1L << (T_NOT - 202)) | (1L << (T_NULL
                                - 202)) | (1L << (T_PRIMARY - 202)) | (1L << (T_REFERENCES
                                    - 202)))) != 0)
                        || _la == T_UNIQUE
                        || _la == T_WITH
                        || _la == T_COLON
                        || _la == T_EQUAL) {
                        {
                            {
                                setState(491);
                                create_table_column_inline_cons();
                            }
                        }
                        setState(496);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(499);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_CONSTRAINT) {
                        {
                            setState(497);
                            match(T_CONSTRAINT);
                            setState(498);
                            qident();
                        }
                    }

                    setState(501);
                    create_table_column_cons();
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(502);
                    _la = _input.LA(1);
                    if (!(_la == T_ATTRIBUTE || _la == T_SPATIAL)) {
                        _errHandler.recoverInline(this);
                    } else {
                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                        _errHandler.reportMatch(this);
                        consume();
                    }
                    setState(503);
                    match(T_INDEX);
                    setState(505);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if ((((_la) & ~0x3f) == 0
                        && ((1L << _la) & ((1L << T_ACTION) | (1L << T_ADD2) | (1L << T_ALL) | (1L
                            << T_ALLOCATE) | (1L << T_ALTER) | (1L << T_AND) | (1L << T_ANSI_NULLS)
                            | (1L << T_ANSI_PADDING) | (1L << T_AS) | (1L << T_ASC) | (1L
                                << T_ASSOCIATE) | (1L << T_AT) | (1L << T_AVG) | (1L << T_BATCHSIZE)
                            | (1L << T_BEGIN) | (1L << T_BETWEEN) | (1L << T_BIGINT) | (1L
                                << T_BINARY_DOUBLE) | (1L << T_BINARY_FLOAT) | (1L << T_BIT) | (1L
                                    << T_BODY) | (1L << T_BREAK) | (1L << T_BY) | (1L << T_BYTE)
                            | (1L << T_CALL) | (1L << T_CALLER) | (1L << T_CASCADE) | (1L << T_CASE)
                            | (1L << T_CASESPECIFIC) | (1L << T_CAST) | (1L << T_CHAR) | (1L
                                << T_CHARACTER) | (1L << T_CHARSET) | (1L << T_CLIENT) | (1L
                                    << T_CLOSE) | (1L << T_CLUSTERED) | (1L << T_CMP) | (1L
                                        << T_COLLECT) | (1L << T_COLLECTION) | (1L << T_COLUMN)
                            | (1L << T_COMMENT) | (1L << T_CONSTANT) | (1L << T_COMMIT) | (1L
                                << T_COMPRESS) | (1L << T_CONCAT) | (1L << T_CONDITION) | (1L
                                    << T_CONSTRAINT) | (1L << T_CONTINUE) | (1L << T_COPY) | (1L
                                        << T_COUNT) | (1L << T_COUNT_BIG) | (1L << T_CREATION) | (1L
                                            << T_CREATOR) | (1L << T_CS))) != 0)
                        || ((((_la - 64)) & ~0x3f) == 0
                            && ((1L << (_la - 64)) & ((1L << (T_CURRENT - 64)) | (1L
                                << (T_CURRENT_SCHEMA - 64)) | (1L << (T_CURSOR - 64)) | (1L
                                    << (T_DATABASE - 64)) | (1L << (T_DATA - 64)) | (1L << (T_DATE
                                        - 64)) | (1L << (T_DATETIME - 64)) | (1L << (T_DAY - 64))
                                | (1L << (T_DAYS - 64)) | (1L << (T_DEC - 64)) | (1L << (T_DECIMAL
                                    - 64)) | (1L << (T_DECLARE - 64)) | (1L << (T_DEFAULT - 64))
                                | (1L << (T_DEFERRED - 64)) | (1L << (T_DEFINED - 64)) | (1L
                                    << (T_DEFINER - 64)) | (1L << (T_DEFINITION - 64)) | (1L
                                        << (T_DELETE - 64)) | (1L << (T_DELIMITED - 64)) | (1L
                                            << (T_DELIMITER - 64)) | (1L << (T_DESC - 64)) | (1L
                                                << (T_DESCRIBE - 64)) | (1L << (T_DIAGNOSTICS - 64))
                                | (1L << (T_DIR - 64)) | (1L << (T_DIRECTORY - 64)) | (1L
                                    << (T_DISTINCT - 64)) | (1L << (T_DISTRIBUTE - 64)) | (1L
                                        << (T_DO - 64)) | (1L << (T_DOUBLE - 64)) | (1L
                                            << (T_DOWNLOAD - 64)) | (1L << (T_DROP - 64)) | (1L
                                                << (T_DYNAMIC - 64)) | (1L << (T_ENABLE - 64)) | (1L
                                                    << (T_ENGINE - 64)) | (1L << (T_ESCAPED - 64))
                                | (1L << (T_EXCEPT - 64)) | (1L << (T_EXEC - 64)) | (1L
                                    << (T_EXECUTE - 64)) | (1L << (T_EXCEPTION - 64)) | (1L
                                        << (T_EXCLUSIVE - 64)) | (1L << (T_EXISTS - 64)) | (1L
                                            << (T_EXIT - 64)) | (1L << (T_FALLBACK - 64)) | (1L
                                                << (T_FALSE - 64)) | (1L << (T_FETCH - 64)) | (1L
                                                    << (T_FIELDS - 64)) | (1L << (T_FILE - 64))
                                | (1L << (T_FILES - 64)) | (1L << (T_FLOAT - 64)) | (1L << (T_FOR
                                    - 64)) | (1L << (T_FOREIGN - 64)) | (1L << (T_FORMAT - 64))
                                | (1L << (T_FOUND - 64)) | (1L << (T_FROM - 64)) | (1L << (T_FULL
                                    - 64)) | (1L << (T_FUNCTION - 64)) | (1L << (T_GET - 64)) | (1L
                                        << (T_GLOBAL - 64)) | (1L << (T_GO - 64)))) != 0)
                        || ((((_la - 128)) & ~0x3f) == 0
                            && ((1L << (_la - 128)) & ((1L << (T_GRANT - 128)) | (1L << (T_GROUP
                                - 128)) | (1L << (T_HANDLER - 128)) | (1L << (T_HASH - 128)) | (1L
                                    << (T_HAVING - 128)) | (1L << (T_HOST - 128)) | (1L
                                        << (T_IDENTITY - 128)) | (1L << (T_IF - 128)) | (1L
                                            << (T_IGNORE - 128)) | (1L << (T_IMMEDIATE - 128)) | (1L
                                                << (T_IN - 128)) | (1L << (T_INCLUDE - 128)) | (1L
                                                    << (T_INDEX - 128)) | (1L << (T_INITRANS - 128))
                                | (1L << (T_INNER - 128)) | (1L << (T_INOUT - 128)) | (1L
                                    << (T_INSERT - 128)) | (1L << (T_INT - 128)) | (1L << (T_INT2
                                        - 128)) | (1L << (T_INT4 - 128)) | (1L << (T_INT8 - 128))
                                | (1L << (T_INTEGER - 128)) | (1L << (T_INTERSECT - 128)) | (1L
                                    << (T_INTERVAL - 128)) | (1L << (T_INTO - 128)) | (1L
                                        << (T_INVOKER - 128)) | (1L << (T_IS - 128)) | (1L
                                            << (T_ISOPEN - 128)) | (1L << (T_ITEMS - 128)) | (1L
                                                << (T_JOIN - 128)) | (1L << (T_KEEP - 128)) | (1L
                                                    << (T_KEY - 128)) | (1L << (T_KEYS - 128)) | (1L
                                                        << (T_LANGUAGE - 128)) | (1L << (T_LEAVE
                                                            - 128)) | (1L << (T_LEFT - 128)) | (1L
                                                                << (T_LIKE - 128)) | (1L << (T_LIMIT
                                                                    - 128)) | (1L << (T_LINES
                                                                        - 128)) | (1L << (T_LOCAL
                                                                            - 128)) | (1L
                                                                                << (T_LOCATION
                                                                                    - 128)) | (1L
                                                                                        << (T_LOCATOR
                                                                                            - 128))
                                | (1L << (T_LOCATORS - 128)) | (1L << (T_LOCKS - 128)) | (1L
                                    << (T_LOG - 128)) | (1L << (T_LOGGED - 128)) | (1L << (T_LOGGING
                                        - 128)) | (1L << (T_LOOP - 128)) | (1L << (T_MAP - 128))
                                | (1L << (T_MATCHED - 128)) | (1L << (T_MAX - 128)) | (1L
                                    << (T_MAXTRANS - 128)) | (1L << (T_MERGE - 128)) | (1L
                                        << (T_MESSAGE_TEXT - 128)) | (1L << (T_MICROSECOND - 128))
                                | (1L << (T_MICROSECONDS - 128)))) != 0)
                        || ((((_la - 192)) & ~0x3f) == 0
                            && ((1L << (_la - 192)) & ((1L << (T_MIN - 192)) | (1L << (T_MULTISET
                                - 192)) | (1L << (T_NCHAR - 192)) | (1L << (T_NEW - 192)) | (1L
                                    << (T_NVARCHAR - 192)) | (1L << (T_NO - 192)) | (1L
                                        << (T_NOCOUNT - 192)) | (1L << (T_NOCOMPRESS - 192)) | (1L
                                            << (T_NOLOGGING - 192)) | (1L << (T_NONE - 192)) | (1L
                                                << (T_NOT - 192)) | (1L << (T_NOTFOUND - 192)) | (1L
                                                    << (T_NUMERIC - 192)) | (1L << (T_NUMBER - 192))
                                | (1L << (T_OBJECT - 192)) | (1L << (T_OFF - 192)) | (1L << (T_ON
                                    - 192)) | (1L << (T_ONLY - 192)) | (1L << (T_OPEN - 192)) | (1L
                                        << (T_OR - 192)) | (1L << (T_ORDER - 192)) | (1L << (T_OUT
                                            - 192)) | (1L << (T_OUTER - 192)) | (1L << (T_OVER
                                                - 192)) | (1L << (T_OVERWRITE - 192)) | (1L
                                                    << (T_OWNER - 192)) | (1L << (T_PACKAGE - 192))
                                | (1L << (T_PARTITION - 192)) | (1L << (T_PCTFREE - 192)) | (1L
                                    << (T_PCTUSED - 192)) | (1L << (T_PRECISION - 192)) | (1L
                                        << (T_PRESERVE - 192)) | (1L << (T_PRIMARY - 192)) | (1L
                                            << (T_PRINT - 192)) | (1L << (T_PROC - 192)) | (1L
                                                << (T_PROCEDURE - 192)) | (1L << (T_QUALIFY - 192))
                                | (1L << (T_QUERY_BAND - 192)) | (1L << (T_QUIT - 192)) | (1L
                                    << (T_QUOTED_IDENTIFIER - 192)) | (1L << (T_QUOTES - 192)) | (1L
                                        << (T_RAISE - 192)) | (1L << (T_REAL - 192)) | (1L
                                            << (T_REFERENCES - 192)) | (1L << (T_REGEXP - 192))
                                | (1L << (T_REPLACE - 192)) | (1L << (T_RESIGNAL - 192)) | (1L
                                    << (T_RESTRICT - 192)) | (1L << (T_RESULT - 192)) | (1L
                                        << (T_RESULT_SET_LOCATOR - 192)) | (1L << (T_RETURN - 192))
                                | (1L << (T_RETURNS - 192)) | (1L << (T_REVERSE - 192)))) != 0)
                        || ((((_la - 256)) & ~0x3f) == 0
                            && ((1L << (_la - 256)) & ((1L << (T_RIGHT - 256)) | (1L << (T_RLIKE
                                - 256)) | (1L << (T_ROLE - 256)) | (1L << (T_ROLLBACK - 256)) | (1L
                                    << (T_ROW - 256)) | (1L << (T_ROWS - 256)) | (1L << (T_ROW_COUNT
                                        - 256)) | (1L << (T_RR - 256)) | (1L << (T_RS - 256)) | (1L
                                            << (T_PWD - 256)) | (1L << (T_TRIM - 256)) | (1L
                                                << (T_SCHEMA - 256)) | (1L << (T_SECOND - 256))
                                | (1L << (T_SECONDS - 256)) | (1L << (T_SECURITY - 256)) | (1L
                                    << (T_SEGMENT - 256)) | (1L << (T_SEL - 256)) | (1L << (T_SELECT
                                        - 256)) | (1L << (T_SET - 256)) | (1L << (T_SESSION - 256))
                                | (1L << (T_SESSIONS - 256)) | (1L << (T_SETS - 256)) | (1L
                                    << (T_SIGNAL - 256)) | (1L << (T_SIMPLE_DOUBLE - 256)) | (1L
                                        << (T_SIMPLE_FLOAT - 256)) | (1L << (T_SMALLDATETIME - 256))
                                | (1L << (T_SMALLINT - 256)) | (1L << (T_SQL - 256)) | (1L
                                    << (T_SQLEXCEPTION - 256)) | (1L << (T_SQLINSERT - 256)) | (1L
                                        << (T_SQLSTATE - 256)) | (1L << (T_SQLWARNING - 256)) | (1L
                                            << (T_STATS - 256)) | (1L << (T_STATISTICS - 256)) | (1L
                                                << (T_STEP - 256)) | (1L << (T_STORAGE - 256)) | (1L
                                                    << (T_STORE - 256)) | (1L << (T_STORED - 256))
                                | (1L << (T_STREAM - 256)) | (1L << (T_STRING - 256)) | (1L
                                    << (T_SUBDIR - 256)) | (1L << (T_SUBSTRING - 256)) | (1L
                                        << (T_SUM - 256)) | (1L << (T_SUMMARY - 256)) | (1L
                                            << (T_SYS_REFCURSOR - 256)) | (1L << (T_TABLE - 256))
                                | (1L << (T_TABLESPACE - 256)) | (1L << (T_TEMPORARY - 256)) | (1L
                                    << (T_TERMINATED - 256)) | (1L << (T_TEXTIMAGE_ON - 256)) | (1L
                                        << (T_THEN - 256)) | (1L << (T_TIMESTAMP - 256)))) != 0)
                        || ((((_la - 320)) & ~0x3f) == 0
                            && ((1L << (_la - 320)) & ((1L << (T_TITLE - 320)) | (1L << (T_TO
                                - 320)) | (1L << (T_TOP - 320)) | (1L << (T_TRANSACTION - 320))
                                | (1L << (T_TRUE - 320)) | (1L << (T_TRUNCATE - 320)) | (1L
                                    << (T_UNIQUE - 320)) | (1L << (T_UPDATE - 320)) | (1L << (T_UR
                                        - 320)) | (1L << (T_USE - 320)) | (1L << (T_USING - 320))
                                | (1L << (T_VALUE - 320)) | (1L << (T_VALUES - 320)) | (1L << (T_VAR
                                    - 320)) | (1L << (T_VARCHAR - 320)) | (1L << (T_VARCHAR2 - 320))
                                | (1L << (T_VARYING - 320)) | (1L << (T_VOLATILE - 320)) | (1L
                                    << (T_WHILE - 320)) | (1L << (T_WITH - 320)) | (1L << (T_WITHOUT
                                        - 320)) | (1L << (T_WORK - 320)) | (1L << (T_XACT_ABORT
                                            - 320)) | (1L << (T_XML - 320)) | (1L << (T_YES - 320))
                                | (1L << (T_ACTIVITY_COUNT - 320)) | (1L << (T_CUME_DIST - 320))
                                | (1L << (T_CURRENT_DATE - 320)) | (1L << (T_CURRENT_TIME - 320))
                                | (1L << (T_PI - 320)) | (1L << (T_CURRENT_TIMESTAMP - 320)) | (1L
                                    << (T_CURRENT_USER - 320)) | (1L << (T_DENSE_RANK - 320)) | (1L
                                        << (T_FIRST_VALUE - 320)) | (1L << (T_LAG - 320)) | (1L
                                            << (T_LAST_VALUE - 320)) | (1L << (T_LEAD - 320)) | (1L
                                                << (T_PART_COUNT - 320)) | (1L << (T_PART_LOC
                                                    - 320)) | (1L << (T_RANK - 320)) | (1L
                                                        << (T_ROW_NUMBER - 320)) | (1L << (T_STDEV
                                                            - 320)) | (1L << (T_SYSDATE - 320))
                                | (1L << (T_VARIANCE - 320)) | (1L << (T_USER - 320)))) != 0)
                        || _la == T_SUB
                        || _la == L_ID) {
                        {
                            setState(504);
                            ident();
                        }
                    }

                    setState(507);
                    key_list();
                    setState(509);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_TYPE) {
                        {
                            setState(508);
                            index_type_decl();
                        }
                    }

                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Key_listContext extends ParserRuleContext {
        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public List<QidentContext> qident() {
            return getRuleContexts(QidentContext.class);
        }

        public QidentContext qident(int i) {
            return getRuleContext(QidentContext.class, i);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public Key_listContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_key_list;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitKey_list(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Key_listContext key_list() throws RecognitionException {
        Key_listContext _localctx = new Key_listContext(_ctx, getState());
        enterRule(_localctx, 34, RULE_key_list);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(513);
                match(T_OPEN_P);
                setState(514);
                qident();
                setState(519);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(515);
                            match(T_COMMA);
                            setState(516);
                            qident();
                        }
                    }
                    setState(521);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(522);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Index_type_declContext extends ParserRuleContext {
        public TerminalNode T_TYPE() {
            return getToken(CupidDBSqlParser.T_TYPE, 0);
        }

        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public Index_type_declContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_index_type_decl;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitIndex_type_decl(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Index_type_declContext index_type_decl() throws RecognitionException {
        Index_type_declContext _localctx = new Index_type_declContext(_ctx, getState());
        enterRule(_localctx, 36, RULE_index_type_decl);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(524);
                match(T_TYPE);
                setState(525);
                ident();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Column_nameContext extends ParserRuleContext {
        public QidentContext qident() {
            return getRuleContext(QidentContext.class, 0);
        }

        public Column_nameContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_column_name;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitColumn_name(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Column_nameContext column_name() throws RecognitionException {
        Column_nameContext _localctx = new Column_nameContext(_ctx, getState());
        enterRule(_localctx, 38, RULE_column_name);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(527);
                qident();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Create_table_column_inline_consContext extends ParserRuleContext {
        public Dtype_defaultContext dtype_default() {
            return getRuleContext(Dtype_defaultContext.class, 0);
        }

        public TerminalNode T_NULL() {
            return getToken(CupidDBSqlParser.T_NULL, 0);
        }

        public TerminalNode T_NOT() {
            return getToken(CupidDBSqlParser.T_NOT, 0);
        }

        public TerminalNode T_PRIMARY() {
            return getToken(CupidDBSqlParser.T_PRIMARY, 0);
        }

        public TerminalNode T_KEY() {
            return getToken(CupidDBSqlParser.T_KEY, 0);
        }

        public TerminalNode T_UNIQUE() {
            return getToken(CupidDBSqlParser.T_UNIQUE, 0);
        }

        public TerminalNode T_REFERENCES() {
            return getToken(CupidDBSqlParser.T_REFERENCES, 0);
        }

        public Table_nameContext table_name() {
            return getRuleContext(Table_nameContext.class, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public QidentContext qident() {
            return getRuleContext(QidentContext.class, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public List<Create_table_fk_actionContext> create_table_fk_action() {
            return getRuleContexts(Create_table_fk_actionContext.class);
        }

        public Create_table_fk_actionContext create_table_fk_action(int i) {
            return getRuleContext(Create_table_fk_actionContext.class, i);
        }

        public TerminalNode T_IDENTITY() {
            return getToken(CupidDBSqlParser.T_IDENTITY, 0);
        }

        public List<TerminalNode> L_INT() {
            return getTokens(CupidDBSqlParser.L_INT);
        }

        public TerminalNode L_INT(int i) {
            return getToken(CupidDBSqlParser.L_INT, i);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public TerminalNode T_AUTO_INCREMENT() {
            return getToken(CupidDBSqlParser.T_AUTO_INCREMENT, 0);
        }

        public TerminalNode T_ENABLE() {
            return getToken(CupidDBSqlParser.T_ENABLE, 0);
        }

        public Create_table_column_inline_consContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_create_table_column_inline_cons;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreate_table_column_inline_cons(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Create_table_column_inline_consContext create_table_column_inline_cons()
        throws RecognitionException {
        Create_table_column_inline_consContext _localctx =
            new Create_table_column_inline_consContext(_ctx, getState());
        enterRule(_localctx, 40, RULE_create_table_column_inline_cons);
        int _la;
        try {
            setState(561);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_DEFAULT:
                case T_WITH:
                case T_COLON:
                case T_EQUAL:
                    enterOuterAlt(_localctx, 1); {
                    setState(529);
                    dtype_default();
                }
                    break;
                case T_NOT:
                case T_NULL:
                    enterOuterAlt(_localctx, 2); {
                    setState(531);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_NOT) {
                        {
                            setState(530);
                            match(T_NOT);
                        }
                    }

                    setState(533);
                    match(T_NULL);
                }
                    break;
                case T_PRIMARY:
                    enterOuterAlt(_localctx, 3); {
                    setState(534);
                    match(T_PRIMARY);
                    setState(535);
                    match(T_KEY);
                }
                    break;
                case T_UNIQUE:
                    enterOuterAlt(_localctx, 4); {
                    setState(536);
                    match(T_UNIQUE);
                }
                    break;
                case T_REFERENCES:
                    enterOuterAlt(_localctx, 5); {
                    setState(537);
                    match(T_REFERENCES);
                    setState(538);
                    table_name();
                    setState(539);
                    match(T_OPEN_P);
                    setState(540);
                    qident();
                    setState(541);
                    match(T_CLOSE_P);
                    setState(545);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == T_ON) {
                        {
                            {
                                setState(542);
                                create_table_fk_action();
                            }
                        }
                        setState(547);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                }
                    break;
                case T_IDENTITY:
                    enterOuterAlt(_localctx, 6); {
                    setState(548);
                    match(T_IDENTITY);
                    setState(549);
                    match(T_OPEN_P);
                    setState(550);
                    match(L_INT);
                    setState(555);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == T_COMMA) {
                        {
                            {
                                setState(551);
                                match(T_COMMA);
                                setState(552);
                                match(L_INT);
                            }
                        }
                        setState(557);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                    setState(558);
                    match(T_CLOSE_P);
                }
                    break;
                case T_AUTO_INCREMENT:
                    enterOuterAlt(_localctx, 7); {
                    setState(559);
                    match(T_AUTO_INCREMENT);
                }
                    break;
                case T_ENABLE:
                    enterOuterAlt(_localctx, 8); {
                    setState(560);
                    match(T_ENABLE);
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Create_table_column_consContext extends ParserRuleContext {
        public TerminalNode T_PRIMARY() {
            return getToken(CupidDBSqlParser.T_PRIMARY, 0);
        }

        public TerminalNode T_KEY() {
            return getToken(CupidDBSqlParser.T_KEY, 0);
        }

        public List<TerminalNode> T_OPEN_P() {
            return getTokens(CupidDBSqlParser.T_OPEN_P);
        }

        public TerminalNode T_OPEN_P(int i) {
            return getToken(CupidDBSqlParser.T_OPEN_P, i);
        }

        public List<QidentContext> qident() {
            return getRuleContexts(QidentContext.class);
        }

        public QidentContext qident(int i) {
            return getRuleContext(QidentContext.class, i);
        }

        public List<TerminalNode> T_CLOSE_P() {
            return getTokens(CupidDBSqlParser.T_CLOSE_P);
        }

        public TerminalNode T_CLOSE_P(int i) {
            return getToken(CupidDBSqlParser.T_CLOSE_P, i);
        }

        public TerminalNode T_CLUSTERED() {
            return getToken(CupidDBSqlParser.T_CLUSTERED, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public TerminalNode T_ENABLE() {
            return getToken(CupidDBSqlParser.T_ENABLE, 0);
        }

        public List<TerminalNode> T_ASC() {
            return getTokens(CupidDBSqlParser.T_ASC);
        }

        public TerminalNode T_ASC(int i) {
            return getToken(CupidDBSqlParser.T_ASC, i);
        }

        public List<TerminalNode> T_DESC() {
            return getTokens(CupidDBSqlParser.T_DESC);
        }

        public TerminalNode T_DESC(int i) {
            return getToken(CupidDBSqlParser.T_DESC, i);
        }

        public TerminalNode T_FOREIGN() {
            return getToken(CupidDBSqlParser.T_FOREIGN, 0);
        }

        public TerminalNode T_REFERENCES() {
            return getToken(CupidDBSqlParser.T_REFERENCES, 0);
        }

        public Table_nameContext table_name() {
            return getRuleContext(Table_nameContext.class, 0);
        }

        public List<Create_table_fk_actionContext> create_table_fk_action() {
            return getRuleContexts(Create_table_fk_actionContext.class);
        }

        public Create_table_fk_actionContext create_table_fk_action(int i) {
            return getRuleContext(Create_table_fk_actionContext.class, i);
        }

        public Create_table_column_consContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_create_table_column_cons;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreate_table_column_cons(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Create_table_column_consContext create_table_column_cons()
        throws RecognitionException {
        Create_table_column_consContext _localctx = new Create_table_column_consContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 42, RULE_create_table_column_cons);
        int _la;
        try {
            setState(617);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_PRIMARY:
                    enterOuterAlt(_localctx, 1); {
                    setState(563);
                    match(T_PRIMARY);
                    setState(564);
                    match(T_KEY);
                    setState(566);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_CLUSTERED) {
                        {
                            setState(565);
                            match(T_CLUSTERED);
                        }
                    }

                    setState(568);
                    match(T_OPEN_P);
                    setState(569);
                    qident();
                    setState(571);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_ASC || _la == T_DESC) {
                        {
                            setState(570);
                            _la = _input.LA(1);
                            if (!(_la == T_ASC || _la == T_DESC)) {
                                _errHandler.recoverInline(this);
                            } else {
                                if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                _errHandler.reportMatch(this);
                                consume();
                            }
                        }
                    }

                    setState(580);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == T_COMMA) {
                        {
                            {
                                setState(573);
                                match(T_COMMA);
                                setState(574);
                                qident();
                                setState(576);
                                _errHandler.sync(this);
                                _la = _input.LA(1);
                                if (_la == T_ASC || _la == T_DESC) {
                                    {
                                        setState(575);
                                        _la = _input.LA(1);
                                        if (!(_la == T_ASC || _la == T_DESC)) {
                                            _errHandler.recoverInline(this);
                                        } else {
                                            if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                            _errHandler.reportMatch(this);
                                            consume();
                                        }
                                    }
                                }

                            }
                        }
                        setState(582);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                    setState(583);
                    match(T_CLOSE_P);
                    setState(585);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_ENABLE) {
                        {
                            setState(584);
                            match(T_ENABLE);
                        }
                    }

                }
                    break;
                case T_FOREIGN:
                    enterOuterAlt(_localctx, 2); {
                    setState(587);
                    match(T_FOREIGN);
                    setState(588);
                    match(T_KEY);
                    setState(589);
                    match(T_OPEN_P);
                    setState(590);
                    qident();
                    setState(595);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == T_COMMA) {
                        {
                            {
                                setState(591);
                                match(T_COMMA);
                                setState(592);
                                qident();
                            }
                        }
                        setState(597);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                    setState(598);
                    match(T_CLOSE_P);
                    setState(599);
                    match(T_REFERENCES);
                    setState(600);
                    table_name();
                    setState(601);
                    match(T_OPEN_P);
                    setState(602);
                    qident();
                    setState(607);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == T_COMMA) {
                        {
                            {
                                setState(603);
                                match(T_COMMA);
                                setState(604);
                                qident();
                            }
                        }
                        setState(609);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                    setState(610);
                    match(T_CLOSE_P);
                    setState(614);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == T_ON) {
                        {
                            {
                                setState(611);
                                create_table_fk_action();
                            }
                        }
                        setState(616);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Create_table_fk_actionContext extends ParserRuleContext {
        public TerminalNode T_ON() {
            return getToken(CupidDBSqlParser.T_ON, 0);
        }

        public TerminalNode T_UPDATE() {
            return getToken(CupidDBSqlParser.T_UPDATE, 0);
        }

        public TerminalNode T_DELETE() {
            return getToken(CupidDBSqlParser.T_DELETE, 0);
        }

        public TerminalNode T_NO() {
            return getToken(CupidDBSqlParser.T_NO, 0);
        }

        public TerminalNode T_ACTION() {
            return getToken(CupidDBSqlParser.T_ACTION, 0);
        }

        public TerminalNode T_RESTRICT() {
            return getToken(CupidDBSqlParser.T_RESTRICT, 0);
        }

        public TerminalNode T_SET() {
            return getToken(CupidDBSqlParser.T_SET, 0);
        }

        public TerminalNode T_NULL() {
            return getToken(CupidDBSqlParser.T_NULL, 0);
        }

        public TerminalNode T_DEFAULT() {
            return getToken(CupidDBSqlParser.T_DEFAULT, 0);
        }

        public TerminalNode T_CASCADE() {
            return getToken(CupidDBSqlParser.T_CASCADE, 0);
        }

        public Create_table_fk_actionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_create_table_fk_action;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreate_table_fk_action(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Create_table_fk_actionContext create_table_fk_action()
        throws RecognitionException {
        Create_table_fk_actionContext _localctx = new Create_table_fk_actionContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 44, RULE_create_table_fk_action);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(619);
                match(T_ON);
                setState(620);
                _la = _input.LA(1);
                if (!(_la == T_DELETE || _la == T_UPDATE)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(629);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 48, _ctx)) {
                    case 1: {
                        setState(621);
                        match(T_NO);
                        setState(622);
                        match(T_ACTION);
                    }
                        break;
                    case 2: {
                        setState(623);
                        match(T_RESTRICT);
                    }
                        break;
                    case 3: {
                        setState(624);
                        match(T_SET);
                        setState(625);
                        match(T_NULL);
                    }
                        break;
                    case 4: {
                        setState(626);
                        match(T_SET);
                        setState(627);
                        match(T_DEFAULT);
                    }
                        break;
                    case 5: {
                        setState(628);
                        match(T_CASCADE);
                    }
                        break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Create_table_preoptionsContext extends ParserRuleContext {
        public List<Create_table_preoptions_itemContext> create_table_preoptions_item() {
            return getRuleContexts(Create_table_preoptions_itemContext.class);
        }

        public Create_table_preoptions_itemContext create_table_preoptions_item(int i) {
            return getRuleContext(Create_table_preoptions_itemContext.class, i);
        }

        public Create_table_preoptionsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_create_table_preoptions;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreate_table_preoptions(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Create_table_preoptionsContext create_table_preoptions()
        throws RecognitionException {
        Create_table_preoptionsContext _localctx = new Create_table_preoptionsContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 46, RULE_create_table_preoptions);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(632);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(631);
                            create_table_preoptions_item();
                        }
                    }
                    setState(634);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while (_la == T_COMMA);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Create_table_preoptions_itemContext extends ParserRuleContext {
        public TerminalNode T_COMMA() {
            return getToken(CupidDBSqlParser.T_COMMA, 0);
        }

        public Create_table_preoptions_td_itemContext create_table_preoptions_td_item() {
            return getRuleContext(Create_table_preoptions_td_itemContext.class, 0);
        }

        public Create_table_preoptions_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_create_table_preoptions_item;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreate_table_preoptions_item(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Create_table_preoptions_itemContext create_table_preoptions_item()
        throws RecognitionException {
        Create_table_preoptions_itemContext _localctx = new Create_table_preoptions_itemContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 48, RULE_create_table_preoptions_item);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(636);
                match(T_COMMA);
                setState(637);
                create_table_preoptions_td_item();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Create_table_preoptions_td_itemContext extends ParserRuleContext {
        public TerminalNode T_LOG() {
            return getToken(CupidDBSqlParser.T_LOG, 0);
        }

        public TerminalNode T_FALLBACK() {
            return getToken(CupidDBSqlParser.T_FALLBACK, 0);
        }

        public TerminalNode T_NO() {
            return getToken(CupidDBSqlParser.T_NO, 0);
        }

        public Create_table_preoptions_td_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_create_table_preoptions_td_item;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreate_table_preoptions_td_item(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Create_table_preoptions_td_itemContext create_table_preoptions_td_item()
        throws RecognitionException {
        Create_table_preoptions_td_itemContext _localctx =
            new Create_table_preoptions_td_itemContext(_ctx, getState());
        enterRule(_localctx, 50, RULE_create_table_preoptions_td_item);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(640);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_NO) {
                    {
                        setState(639);
                        match(T_NO);
                    }
                }

                setState(642);
                _la = _input.LA(1);
                if (!(_la == T_FALLBACK || _la == T_LOG)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Create_table_optionsContext extends ParserRuleContext {
        public List<Create_table_options_itemContext> create_table_options_item() {
            return getRuleContexts(Create_table_options_itemContext.class);
        }

        public Create_table_options_itemContext create_table_options_item(int i) {
            return getRuleContext(Create_table_options_itemContext.class, i);
        }

        public Create_table_optionsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_create_table_options;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreate_table_options(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Create_table_optionsContext create_table_options() throws RecognitionException {
        Create_table_optionsContext _localctx = new Create_table_optionsContext(_ctx, getState());
        enterRule(_localctx, 52, RULE_create_table_options);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(645);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(644);
                            create_table_options_item();
                        }
                    }
                    setState(647);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while ((((_la) & ~0x3f) == 0
                    && ((1L << _la) & ((1L << T_AUTO_INCREMENT) | (1L << T_CHARACTER) | (1L
                        << T_CHARSET) | (1L << T_COMMENT))) != 0)
                    || _la == T_DEFAULT
                    || _la == T_ENGINE
                    || _la == T_ON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Create_table_options_itemContext extends ParserRuleContext {
        public TerminalNode T_ON() {
            return getToken(CupidDBSqlParser.T_ON, 0);
        }

        public TerminalNode T_COMMIT() {
            return getToken(CupidDBSqlParser.T_COMMIT, 0);
        }

        public TerminalNode T_ROWS() {
            return getToken(CupidDBSqlParser.T_ROWS, 0);
        }

        public TerminalNode T_DELETE() {
            return getToken(CupidDBSqlParser.T_DELETE, 0);
        }

        public TerminalNode T_PRESERVE() {
            return getToken(CupidDBSqlParser.T_PRESERVE, 0);
        }

        public Create_table_options_mysql_itemContext create_table_options_mysql_item() {
            return getRuleContext(Create_table_options_mysql_itemContext.class, 0);
        }

        public Create_table_options_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_create_table_options_item;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreate_table_options_item(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Create_table_options_itemContext create_table_options_item()
        throws RecognitionException {
        Create_table_options_itemContext _localctx = new Create_table_options_itemContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 54, RULE_create_table_options_item);
        int _la;
        try {
            setState(654);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_ON:
                    enterOuterAlt(_localctx, 1); {
                    setState(649);
                    match(T_ON);
                    setState(650);
                    match(T_COMMIT);
                    setState(651);
                    _la = _input.LA(1);
                    if (!(_la == T_DELETE || _la == T_PRESERVE)) {
                        _errHandler.recoverInline(this);
                    } else {
                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                        _errHandler.reportMatch(this);
                        consume();
                    }
                    setState(652);
                    match(T_ROWS);
                }
                    break;
                case T_AUTO_INCREMENT:
                case T_CHARACTER:
                case T_CHARSET:
                case T_COMMENT:
                case T_DEFAULT:
                case T_ENGINE:
                    enterOuterAlt(_localctx, 2); {
                    setState(653);
                    create_table_options_mysql_item();
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Create_table_options_mysql_itemContext extends ParserRuleContext {
        public TerminalNode T_AUTO_INCREMENT() {
            return getToken(CupidDBSqlParser.T_AUTO_INCREMENT, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode T_EQUAL() {
            return getToken(CupidDBSqlParser.T_EQUAL, 0);
        }

        public TerminalNode T_COMMENT() {
            return getToken(CupidDBSqlParser.T_COMMENT, 0);
        }

        public TerminalNode T_CHARACTER() {
            return getToken(CupidDBSqlParser.T_CHARACTER, 0);
        }

        public TerminalNode T_SET() {
            return getToken(CupidDBSqlParser.T_SET, 0);
        }

        public TerminalNode T_CHARSET() {
            return getToken(CupidDBSqlParser.T_CHARSET, 0);
        }

        public TerminalNode T_DEFAULT() {
            return getToken(CupidDBSqlParser.T_DEFAULT, 0);
        }

        public TerminalNode T_ENGINE() {
            return getToken(CupidDBSqlParser.T_ENGINE, 0);
        }

        public Create_table_options_mysql_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_create_table_options_mysql_item;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreate_table_options_mysql_item(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Create_table_options_mysql_itemContext create_table_options_mysql_item()
        throws RecognitionException {
        Create_table_options_mysql_itemContext _localctx =
            new Create_table_options_mysql_itemContext(_ctx, getState());
        enterRule(_localctx, 56, RULE_create_table_options_mysql_item);
        int _la;
        try {
            setState(683);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_AUTO_INCREMENT:
                    enterOuterAlt(_localctx, 1); {
                    setState(656);
                    match(T_AUTO_INCREMENT);
                    setState(658);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_EQUAL) {
                        {
                            setState(657);
                            match(T_EQUAL);
                        }
                    }

                    setState(660);
                    expr(0);
                }
                    break;
                case T_COMMENT:
                    enterOuterAlt(_localctx, 2); {
                    setState(661);
                    match(T_COMMENT);
                    setState(663);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_EQUAL) {
                        {
                            setState(662);
                            match(T_EQUAL);
                        }
                    }

                    setState(665);
                    expr(0);
                }
                    break;
                case T_CHARACTER:
                case T_CHARSET:
                case T_DEFAULT:
                    enterOuterAlt(_localctx, 3); {
                    setState(667);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_DEFAULT) {
                        {
                            setState(666);
                            match(T_DEFAULT);
                        }
                    }

                    setState(672);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case T_CHARACTER: {
                            setState(669);
                            match(T_CHARACTER);
                            setState(670);
                            match(T_SET);
                        }
                            break;
                        case T_CHARSET: {
                            setState(671);
                            match(T_CHARSET);
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    setState(675);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_EQUAL) {
                        {
                            setState(674);
                            match(T_EQUAL);
                        }
                    }

                    setState(677);
                    expr(0);
                }
                    break;
                case T_ENGINE:
                    enterOuterAlt(_localctx, 4); {
                    setState(678);
                    match(T_ENGINE);
                    setState(680);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_EQUAL) {
                        {
                            setState(679);
                            match(T_EQUAL);
                        }
                    }

                    setState(682);
                    expr(0);
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CreateIndexStmtContext extends ParserRuleContext {
        public TerminalNode T_CREATE() {
            return getToken(CupidDBSqlParser.T_CREATE, 0);
        }

        public TerminalNode T_INDEX() {
            return getToken(CupidDBSqlParser.T_INDEX, 0);
        }

        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_ON() {
            return getToken(CupidDBSqlParser.T_ON, 0);
        }

        public TableNameContext tableName() {
            return getRuleContext(TableNameContext.class, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public List<CreateIndexColContext> createIndexCol() {
            return getRuleContexts(CreateIndexColContext.class);
        }

        public CreateIndexColContext createIndexCol(int i) {
            return getRuleContext(CreateIndexColContext.class, i);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public TerminalNode T_UNIQUE() {
            return getToken(CupidDBSqlParser.T_UNIQUE, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public CreateIndexStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_createIndexStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreateIndexStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CreateIndexStmtContext createIndexStmt() throws RecognitionException {
        CreateIndexStmtContext _localctx = new CreateIndexStmtContext(_ctx, getState());
        enterRule(_localctx, 58, RULE_createIndexStmt);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(685);
                match(T_CREATE);
                setState(687);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_UNIQUE) {
                    {
                        setState(686);
                        match(T_UNIQUE);
                    }
                }

                setState(689);
                match(T_INDEX);
                setState(690);
                ident();
                setState(691);
                match(T_ON);
                setState(692);
                tableName();
                setState(693);
                match(T_OPEN_P);
                setState(694);
                createIndexCol();
                setState(699);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(695);
                            match(T_COMMA);
                            setState(696);
                            createIndexCol();
                        }
                    }
                    setState(701);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(702);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CreateIndexColContext extends ParserRuleContext {
        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_ASC() {
            return getToken(CupidDBSqlParser.T_ASC, 0);
        }

        public TerminalNode T_DESC() {
            return getToken(CupidDBSqlParser.T_DESC, 0);
        }

        public CreateIndexColContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_createIndexCol;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreateIndexCol(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CreateIndexColContext createIndexCol() throws RecognitionException {
        CreateIndexColContext _localctx = new CreateIndexColContext(_ctx, getState());
        enterRule(_localctx, 60, RULE_createIndexCol);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(704);
                ident();
                setState(706);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_ASC || _la == T_DESC) {
                    {
                        setState(705);
                        _la = _input.LA(1);
                        if (!(_la == T_ASC || _la == T_DESC)) {
                            _errHandler.recoverInline(this);
                        } else {
                            if (_input.LA(1) == Token.EOF) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DropIndexStmtContext extends ParserRuleContext {
        public Token indexName;

        public TerminalNode T_DROP() {
            return getToken(CupidDBSqlParser.T_DROP, 0);
        }

        public TerminalNode T_INDEX() {
            return getToken(CupidDBSqlParser.T_INDEX, 0);
        }

        public TerminalNode T_ON() {
            return getToken(CupidDBSqlParser.T_ON, 0);
        }

        public TableNameContext tableName() {
            return getRuleContext(TableNameContext.class, 0);
        }

        public TerminalNode L_ID() {
            return getToken(CupidDBSqlParser.L_ID, 0);
        }

        public DropIndexStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_dropIndexStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDropIndexStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DropIndexStmtContext dropIndexStmt() throws RecognitionException {
        DropIndexStmtContext _localctx = new DropIndexStmtContext(_ctx, getState());
        enterRule(_localctx, 62, RULE_dropIndexStmt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(708);
                match(T_DROP);
                setState(709);
                match(T_INDEX);
                setState(710);
                ((DropIndexStmtContext) _localctx).indexName = match(L_ID);
                setState(711);
                match(T_ON);
                setState(712);
                tableName();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Dtype_defaultContext extends ParserRuleContext {
        public TerminalNode T_EQUAL() {
            return getToken(CupidDBSqlParser.T_EQUAL, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode T_COLON() {
            return getToken(CupidDBSqlParser.T_COLON, 0);
        }

        public TerminalNode T_DEFAULT() {
            return getToken(CupidDBSqlParser.T_DEFAULT, 0);
        }

        public TerminalNode T_WITH() {
            return getToken(CupidDBSqlParser.T_WITH, 0);
        }

        public Dtype_defaultContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_dtype_default;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDtype_default(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Dtype_defaultContext dtype_default() throws RecognitionException {
        Dtype_defaultContext _localctx = new Dtype_defaultContext(_ctx, getState());
        enterRule(_localctx, 64, RULE_dtype_default);
        int _la;
        try {
            setState(726);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_COLON:
                case T_EQUAL:
                    enterOuterAlt(_localctx, 1); {
                    setState(715);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COLON) {
                        {
                            setState(714);
                            match(T_COLON);
                        }
                    }

                    setState(717);
                    match(T_EQUAL);
                    setState(718);
                    expr(0);
                }
                    break;
                case T_DEFAULT:
                case T_WITH:
                    enterOuterAlt(_localctx, 2); {
                    setState(720);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_WITH) {
                        {
                            setState(719);
                            match(T_WITH);
                        }
                    }

                    setState(722);
                    match(T_DEFAULT);
                    setState(724);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 65, _ctx)) {
                        case 1: {
                            setState(723);
                            expr(0);
                        }
                            break;
                    }
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ShowTablesStmtContext extends ParserRuleContext {
        public TerminalNode T_SHOW() {
            return getToken(CupidDBSqlParser.T_SHOW, 0);
        }

        public TerminalNode T_TABLES() {
            return getToken(CupidDBSqlParser.T_TABLES, 0);
        }

        public ShowTablesStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_showTablesStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitShowTablesStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ShowTablesStmtContext showTablesStmt() throws RecognitionException {
        ShowTablesStmtContext _localctx = new ShowTablesStmtContext(_ctx, getState());
        enterRule(_localctx, 66, RULE_showTablesStmt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(728);
                match(T_SHOW);
                setState(729);
                match(T_TABLES);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Dtype_attrContext extends ParserRuleContext {
        public TerminalNode T_NULL() {
            return getToken(CupidDBSqlParser.T_NULL, 0);
        }

        public TerminalNode T_NOT() {
            return getToken(CupidDBSqlParser.T_NOT, 0);
        }

        public TerminalNode T_CHARACTER() {
            return getToken(CupidDBSqlParser.T_CHARACTER, 0);
        }

        public TerminalNode T_SET() {
            return getToken(CupidDBSqlParser.T_SET, 0);
        }

        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_CASESPECIFIC() {
            return getToken(CupidDBSqlParser.T_CASESPECIFIC, 0);
        }

        public TerminalNode T_CS() {
            return getToken(CupidDBSqlParser.T_CS, 0);
        }

        public Dtype_attrContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_dtype_attr;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDtype_attr(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Dtype_attrContext dtype_attr() throws RecognitionException {
        Dtype_attrContext _localctx = new Dtype_attrContext(_ctx, getState());
        enterRule(_localctx, 68, RULE_dtype_attr);
        int _la;
        try {
            setState(742);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 69, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(732);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_NOT) {
                        {
                            setState(731);
                            match(T_NOT);
                        }
                    }

                    setState(734);
                    match(T_NULL);
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(735);
                    match(T_CHARACTER);
                    setState(736);
                    match(T_SET);
                    setState(737);
                    ident();
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(739);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_NOT) {
                        {
                            setState(738);
                            match(T_NOT);
                        }
                    }

                    setState(741);
                    _la = _input.LA(1);
                    if (!(_la == T_CASESPECIFIC || _la == T_CS)) {
                        _errHandler.recoverInline(this);
                    } else {
                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                        _errHandler.reportMatch(this);
                        consume();
                    }
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DtypeContext extends ParserRuleContext {
        public TerminalNode T_DATETIME() {
            return getToken(CupidDBSqlParser.T_DATETIME, 0);
        }

        public TerminalNode T_DOUBLE() {
            return getToken(CupidDBSqlParser.T_DOUBLE, 0);
        }

        public TerminalNode T_FLOAT() {
            return getToken(CupidDBSqlParser.T_FLOAT, 0);
        }

        public TerminalNode T_INT() {
            return getToken(CupidDBSqlParser.T_INT, 0);
        }

        public TerminalNode T_INTEGER() {
            return getToken(CupidDBSqlParser.T_INTEGER, 0);
        }

        public TerminalNode T_STRING() {
            return getToken(CupidDBSqlParser.T_STRING, 0);
        }

        public TerminalNode T_TIMESTAMP() {
            return getToken(CupidDBSqlParser.T_TIMESTAMP, 0);
        }

        public TerminalNode T_LONG() {
            return getToken(CupidDBSqlParser.T_LONG, 0);
        }

        public TerminalNode T_BOOLEAN() {
            return getToken(CupidDBSqlParser.T_BOOLEAN, 0);
        }

        public TerminalNode T_BOOL() {
            return getToken(CupidDBSqlParser.T_BOOL, 0);
        }

        public TerminalNode T_BINARY() {
            return getToken(CupidDBSqlParser.T_BINARY, 0);
        }

        public TerminalNode T_GEOMETRY() {
            return getToken(CupidDBSqlParser.T_GEOMETRY, 0);
        }

        public TerminalNode T_POINT() {
            return getToken(CupidDBSqlParser.T_POINT, 0);
        }

        public TerminalNode T_LINESTRING() {
            return getToken(CupidDBSqlParser.T_LINESTRING, 0);
        }

        public TerminalNode T_POLYGON() {
            return getToken(CupidDBSqlParser.T_POLYGON, 0);
        }

        public TerminalNode T_MULTIPOINT() {
            return getToken(CupidDBSqlParser.T_MULTIPOINT, 0);
        }

        public TerminalNode T_MULTILINESTRING() {
            return getToken(CupidDBSqlParser.T_MULTILINESTRING, 0);
        }

        public TerminalNode T_MULTIPOLYGON() {
            return getToken(CupidDBSqlParser.T_MULTIPOLYGON, 0);
        }

        public TerminalNode T_GEOMETRYCOLLECTION() {
            return getToken(CupidDBSqlParser.T_GEOMETRYCOLLECTION, 0);
        }

        public TerminalNode T_TRAJECTORY() {
            return getToken(CupidDBSqlParser.T_TRAJECTORY, 0);
        }

        public TerminalNode T_ROADSEGMENT() {
            return getToken(CupidDBSqlParser.T_ROADSEGMENT, 0);
        }

        public TerminalNode T_ROADNETWORK() {
            return getToken(CupidDBSqlParser.T_ROADNETWORK, 0);
        }

        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_TYPE() {
            return getToken(CupidDBSqlParser.T_TYPE, 0);
        }

        public TerminalNode T_ROWTYPE() {
            return getToken(CupidDBSqlParser.T_ROWTYPE, 0);
        }

        public DtypeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_dtype;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDtype(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DtypeContext dtype() throws RecognitionException {
        DtypeContext _localctx = new DtypeContext(_ctx, getState());
        enterRule(_localctx, 70, RULE_dtype);
        int _la;
        try {
            setState(771);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 71, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(744);
                    match(T_DATETIME);
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(745);
                    match(T_DOUBLE);
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(746);
                    match(T_FLOAT);
                }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4); {
                    setState(747);
                    match(T_INT);
                }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5); {
                    setState(748);
                    match(T_INTEGER);
                }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6); {
                    setState(749);
                    match(T_STRING);
                }
                    break;
                case 7:
                    enterOuterAlt(_localctx, 7); {
                    setState(750);
                    match(T_TIMESTAMP);
                }
                    break;
                case 8:
                    enterOuterAlt(_localctx, 8); {
                    setState(751);
                    match(T_LONG);
                }
                    break;
                case 9:
                    enterOuterAlt(_localctx, 9); {
                    setState(752);
                    match(T_BOOLEAN);
                }
                    break;
                case 10:
                    enterOuterAlt(_localctx, 10); {
                    setState(753);
                    match(T_BOOL);
                }
                    break;
                case 11:
                    enterOuterAlt(_localctx, 11); {
                    setState(754);
                    match(T_BINARY);
                }
                    break;
                case 12:
                    enterOuterAlt(_localctx, 12); {
                    setState(755);
                    match(T_GEOMETRY);
                }
                    break;
                case 13:
                    enterOuterAlt(_localctx, 13); {
                    setState(756);
                    match(T_POINT);
                }
                    break;
                case 14:
                    enterOuterAlt(_localctx, 14); {
                    setState(757);
                    match(T_LINESTRING);
                }
                    break;
                case 15:
                    enterOuterAlt(_localctx, 15); {
                    setState(758);
                    match(T_POLYGON);
                }
                    break;
                case 16:
                    enterOuterAlt(_localctx, 16); {
                    setState(759);
                    match(T_MULTIPOINT);
                }
                    break;
                case 17:
                    enterOuterAlt(_localctx, 17); {
                    setState(760);
                    match(T_MULTILINESTRING);
                }
                    break;
                case 18:
                    enterOuterAlt(_localctx, 18); {
                    setState(761);
                    match(T_MULTIPOLYGON);
                }
                    break;
                case 19:
                    enterOuterAlt(_localctx, 19); {
                    setState(762);
                    match(T_GEOMETRYCOLLECTION);
                }
                    break;
                case 20:
                    enterOuterAlt(_localctx, 20); {
                    setState(763);
                    match(T_TRAJECTORY);
                }
                    break;
                case 21:
                    enterOuterAlt(_localctx, 21); {
                    setState(764);
                    match(T_ROADSEGMENT);
                }
                    break;
                case 22:
                    enterOuterAlt(_localctx, 22); {
                    setState(765);
                    match(T_ROADNETWORK);
                }
                    break;
                case 23:
                    enterOuterAlt(_localctx, 23); {
                    setState(766);
                    ident();
                    setState(769);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T__0) {
                        {
                            setState(767);
                            match(T__0);
                            setState(768);
                            _la = _input.LA(1);
                            if (!(_la == T_ROWTYPE || _la == T_TYPE)) {
                                _errHandler.recoverInline(this);
                            } else {
                                if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                _errHandler.reportMatch(this);
                                consume();
                            }
                        }
                    }

                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DtypeLenContext extends ParserRuleContext {
        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public List<TerminalNode> L_INT() {
            return getTokens(CupidDBSqlParser.L_INT);
        }

        public TerminalNode L_INT(int i) {
            return getToken(CupidDBSqlParser.L_INT, i);
        }

        public TerminalNode T_MAX() {
            return getToken(CupidDBSqlParser.T_MAX, 0);
        }

        public TerminalNode T_COMMA() {
            return getToken(CupidDBSqlParser.T_COMMA, 0);
        }

        public TerminalNode T_CHAR() {
            return getToken(CupidDBSqlParser.T_CHAR, 0);
        }

        public TerminalNode T_BYTE() {
            return getToken(CupidDBSqlParser.T_BYTE, 0);
        }

        public DtypeLenContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_dtypeLen;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDtypeLen(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DtypeLenContext dtypeLen() throws RecognitionException {
        DtypeLenContext _localctx = new DtypeLenContext(_ctx, getState());
        enterRule(_localctx, 72, RULE_dtypeLen);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(773);
                match(T_OPEN_P);
                setState(774);
                _la = _input.LA(1);
                if (!(_la == T_MAX || _la == L_INT)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(776);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_BYTE || _la == T_CHAR) {
                    {
                        setState(775);
                        _la = _input.LA(1);
                        if (!(_la == T_BYTE || _la == T_CHAR)) {
                            _errHandler.recoverInline(this);
                        } else {
                            if (_input.LA(1) == Token.EOF) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                    }
                }

                setState(780);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_COMMA) {
                    {
                        setState(778);
                        match(T_COMMA);
                        setState(779);
                        match(L_INT);
                    }
                }

                setState(782);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CreateDatabaseStmtContext extends ParserRuleContext {
        public Token dbName;

        public TerminalNode T_CREATE() {
            return getToken(CupidDBSqlParser.T_CREATE, 0);
        }

        public TerminalNode T_DATABASE() {
            return getToken(CupidDBSqlParser.T_DATABASE, 0);
        }

        public TerminalNode T_SCHEMA() {
            return getToken(CupidDBSqlParser.T_SCHEMA, 0);
        }

        public TerminalNode L_ID() {
            return getToken(CupidDBSqlParser.L_ID, 0);
        }

        public TerminalNode T_IF() {
            return getToken(CupidDBSqlParser.T_IF, 0);
        }

        public TerminalNode T_NOT() {
            return getToken(CupidDBSqlParser.T_NOT, 0);
        }

        public TerminalNode T_EXISTS() {
            return getToken(CupidDBSqlParser.T_EXISTS, 0);
        }

        public List<CreateDatabaseOptionContext> createDatabaseOption() {
            return getRuleContexts(CreateDatabaseOptionContext.class);
        }

        public CreateDatabaseOptionContext createDatabaseOption(int i) {
            return getRuleContext(CreateDatabaseOptionContext.class, i);
        }

        public CreateDatabaseStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_createDatabaseStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreateDatabaseStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CreateDatabaseStmtContext createDatabaseStmt() throws RecognitionException {
        CreateDatabaseStmtContext _localctx = new CreateDatabaseStmtContext(_ctx, getState());
        enterRule(_localctx, 74, RULE_createDatabaseStmt);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(784);
                match(T_CREATE);
                setState(785);
                _la = _input.LA(1);
                if (!(_la == T_DATABASE || _la == T_SCHEMA)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(789);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_IF) {
                    {
                        setState(786);
                        match(T_IF);
                        setState(787);
                        match(T_NOT);
                        setState(788);
                        match(T_EXISTS);
                    }
                }

                setState(791);
                ((CreateDatabaseStmtContext) _localctx).dbName = match(L_ID);
                setState(795);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMENT || _la == T_LOCATION) {
                    {
                        {
                            setState(792);
                            createDatabaseOption();
                        }
                    }
                    setState(797);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ShowDatabasesStmtContext extends ParserRuleContext {
        public TerminalNode T_SHOW() {
            return getToken(CupidDBSqlParser.T_SHOW, 0);
        }

        public TerminalNode T_DATABASES() {
            return getToken(CupidDBSqlParser.T_DATABASES, 0);
        }

        public ShowDatabasesStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_showDatabasesStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitShowDatabasesStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ShowDatabasesStmtContext showDatabasesStmt() throws RecognitionException {
        ShowDatabasesStmtContext _localctx = new ShowDatabasesStmtContext(_ctx, getState());
        enterRule(_localctx, 76, RULE_showDatabasesStmt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(798);
                match(T_SHOW);
                setState(799);
                match(T_DATABASES);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DropDatabaseStmtContext extends ParserRuleContext {
        public Token dbName;

        public TerminalNode T_DROP() {
            return getToken(CupidDBSqlParser.T_DROP, 0);
        }

        public TerminalNode T_DATABASE() {
            return getToken(CupidDBSqlParser.T_DATABASE, 0);
        }

        public TerminalNode T_SCHEMA() {
            return getToken(CupidDBSqlParser.T_SCHEMA, 0);
        }

        public TerminalNode L_ID() {
            return getToken(CupidDBSqlParser.L_ID, 0);
        }

        public TerminalNode T_IF() {
            return getToken(CupidDBSqlParser.T_IF, 0);
        }

        public TerminalNode T_EXISTS() {
            return getToken(CupidDBSqlParser.T_EXISTS, 0);
        }

        public DropDatabaseStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_dropDatabaseStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDropDatabaseStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DropDatabaseStmtContext dropDatabaseStmt() throws RecognitionException {
        DropDatabaseStmtContext _localctx = new DropDatabaseStmtContext(_ctx, getState());
        enterRule(_localctx, 78, RULE_dropDatabaseStmt);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(801);
                match(T_DROP);
                setState(802);
                _la = _input.LA(1);
                if (!(_la == T_DATABASE || _la == T_SCHEMA)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(805);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_IF) {
                    {
                        setState(803);
                        match(T_IF);
                        setState(804);
                        match(T_EXISTS);
                    }
                }

                setState(807);
                ((DropDatabaseStmtContext) _localctx).dbName = match(L_ID);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CreateDatabaseOptionContext extends ParserRuleContext {
        public TerminalNode T_COMMENT() {
            return getToken(CupidDBSqlParser.T_COMMENT, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode T_LOCATION() {
            return getToken(CupidDBSqlParser.T_LOCATION, 0);
        }

        public CreateDatabaseOptionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_createDatabaseOption;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCreateDatabaseOption(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CreateDatabaseOptionContext createDatabaseOption() throws RecognitionException {
        CreateDatabaseOptionContext _localctx = new CreateDatabaseOptionContext(_ctx, getState());
        enterRule(_localctx, 80, RULE_createDatabaseOption);
        try {
            setState(813);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_COMMENT:
                    enterOuterAlt(_localctx, 1); {
                    setState(809);
                    match(T_COMMENT);
                    setState(810);
                    expr(0);
                }
                    break;
                case T_LOCATION:
                    enterOuterAlt(_localctx, 2); {
                    setState(811);
                    match(T_LOCATION);
                    setState(812);
                    expr(0);
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DropTableStmtContext extends ParserRuleContext {
        public TerminalNode T_DROP() {
            return getToken(CupidDBSqlParser.T_DROP, 0);
        }

        public TerminalNode T_TABLE() {
            return getToken(CupidDBSqlParser.T_TABLE, 0);
        }

        public TableNameContext tableName() {
            return getRuleContext(TableNameContext.class, 0);
        }

        public TerminalNode T_IF() {
            return getToken(CupidDBSqlParser.T_IF, 0);
        }

        public TerminalNode T_EXISTS() {
            return getToken(CupidDBSqlParser.T_EXISTS, 0);
        }

        public DropTableStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_dropTableStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDropTableStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DropTableStmtContext dropTableStmt() throws RecognitionException {
        DropTableStmtContext _localctx = new DropTableStmtContext(_ctx, getState());
        enterRule(_localctx, 82, RULE_dropTableStmt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(815);
                match(T_DROP);
                setState(816);
                match(T_TABLE);
                setState(819);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 78, _ctx)) {
                    case 1: {
                        setState(817);
                        match(T_IF);
                        setState(818);
                        match(T_EXISTS);
                    }
                        break;
                }
                setState(821);
                tableName();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class InsertStmtContext extends ParserRuleContext {
        public TerminalNode T_INSERT() {
            return getToken(CupidDBSqlParser.T_INSERT, 0);
        }

        public TableNameContext tableName() {
            return getRuleContext(TableNameContext.class, 0);
        }

        public TerminalNode T_OVERWRITE() {
            return getToken(CupidDBSqlParser.T_OVERWRITE, 0);
        }

        public TerminalNode T_TABLE() {
            return getToken(CupidDBSqlParser.T_TABLE, 0);
        }

        public TerminalNode T_INTO() {
            return getToken(CupidDBSqlParser.T_INTO, 0);
        }

        public SelectStmtContext selectStmt() {
            return getRuleContext(SelectStmtContext.class, 0);
        }

        public InsertStmtRowsContext insertStmtRows() {
            return getRuleContext(InsertStmtRowsContext.class, 0);
        }

        public InsertStmtColsContext insertStmtCols() {
            return getRuleContext(InsertStmtColsContext.class, 0);
        }

        public InsertStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_insertStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitInsertStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final InsertStmtContext insertStmt() throws RecognitionException {
        InsertStmtContext _localctx = new InsertStmtContext(_ctx, getState());
        enterRule(_localctx, 84, RULE_insertStmt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(823);
                match(T_INSERT);
                setState(830);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case T_OVERWRITE: {
                        setState(824);
                        match(T_OVERWRITE);
                        setState(825);
                        match(T_TABLE);
                    }
                        break;
                    case T_INTO: {
                        setState(826);
                        match(T_INTO);
                        setState(828);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 79, _ctx)) {
                            case 1: {
                                setState(827);
                                match(T_TABLE);
                            }
                                break;
                        }
                    }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
                setState(832);
                tableName();
                setState(834);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 81, _ctx)) {
                    case 1: {
                        setState(833);
                        insertStmtCols();
                    }
                        break;
                }
                setState(838);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case T_SEL:
                    case T_SELECT:
                    case T_WITH:
                    case T_OPEN_P: {
                        setState(836);
                        selectStmt();
                    }
                        break;
                    case T_VALUES: {
                        setState(837);
                        insertStmtRows();
                    }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class InsertStmtColsContext extends ParserRuleContext {
        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public List<IdentContext> ident() {
            return getRuleContexts(IdentContext.class);
        }

        public IdentContext ident(int i) {
            return getRuleContext(IdentContext.class, i);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public InsertStmtColsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_insertStmtCols;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitInsertStmtCols(this);
            else return visitor.visitChildren(this);
        }
    }

    public final InsertStmtColsContext insertStmtCols() throws RecognitionException {
        InsertStmtColsContext _localctx = new InsertStmtColsContext(_ctx, getState());
        enterRule(_localctx, 86, RULE_insertStmtCols);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(840);
                match(T_OPEN_P);
                setState(841);
                ident();
                setState(846);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(842);
                            match(T_COMMA);
                            setState(843);
                            ident();
                        }
                    }
                    setState(848);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(849);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class InsertStmtRowsContext extends ParserRuleContext {
        public TerminalNode T_VALUES() {
            return getToken(CupidDBSqlParser.T_VALUES, 0);
        }

        public List<InsertStmtRowContext> insertStmtRow() {
            return getRuleContexts(InsertStmtRowContext.class);
        }

        public InsertStmtRowContext insertStmtRow(int i) {
            return getRuleContext(InsertStmtRowContext.class, i);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public InsertStmtRowsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_insertStmtRows;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitInsertStmtRows(this);
            else return visitor.visitChildren(this);
        }
    }

    public final InsertStmtRowsContext insertStmtRows() throws RecognitionException {
        InsertStmtRowsContext _localctx = new InsertStmtRowsContext(_ctx, getState());
        enterRule(_localctx, 88, RULE_insertStmtRows);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(851);
                match(T_VALUES);
                setState(852);
                insertStmtRow();
                setState(857);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(853);
                            match(T_COMMA);
                            setState(854);
                            insertStmtRow();
                        }
                    }
                    setState(859);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class InsertStmtRowContext extends ParserRuleContext {
        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public InsertStmtRowContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_insertStmtRow;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitInsertStmtRow(this);
            else return visitor.visitChildren(this);
        }
    }

    public final InsertStmtRowContext insertStmtRow() throws RecognitionException {
        InsertStmtRowContext _localctx = new InsertStmtRowContext(_ctx, getState());
        enterRule(_localctx, 90, RULE_insertStmtRow);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(860);
                match(T_OPEN_P);
                setState(861);
                expr(0);
                setState(866);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(862);
                            match(T_COMMA);
                            setState(863);
                            expr(0);
                        }
                    }
                    setState(868);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(869);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class TruncateStmtContext extends ParserRuleContext {
        public TerminalNode T_TRUNCATE() {
            return getToken(CupidDBSqlParser.T_TRUNCATE, 0);
        }

        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_TABLE() {
            return getToken(CupidDBSqlParser.T_TABLE, 0);
        }

        public TruncateStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_truncateStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitTruncateStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TruncateStmtContext truncateStmt() throws RecognitionException {
        TruncateStmtContext _localctx = new TruncateStmtContext(_ctx, getState());
        enterRule(_localctx, 92, RULE_truncateStmt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(871);
                match(T_TRUNCATE);
                setState(873);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 86, _ctx)) {
                    case 1: {
                        setState(872);
                        match(T_TABLE);
                    }
                        break;
                }
                setState(875);
                ident();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class UseStmtContext extends ParserRuleContext {
        public Token dbName;

        public TerminalNode T_USE() {
            return getToken(CupidDBSqlParser.T_USE, 0);
        }

        public TerminalNode L_ID() {
            return getToken(CupidDBSqlParser.L_ID, 0);
        }

        public TerminalNode T_DEFAULT() {
            return getToken(CupidDBSqlParser.T_DEFAULT, 0);
        }

        public UseStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_useStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitUseStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final UseStmtContext useStmt() throws RecognitionException {
        UseStmtContext _localctx = new UseStmtContext(_ctx, getState());
        enterRule(_localctx, 94, RULE_useStmt);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(877);
                match(T_USE);
                setState(878);
                ((UseStmtContext) _localctx).dbName = _input.LT(1);
                _la = _input.LA(1);
                if (!(_la == T_DEFAULT || _la == L_ID)) {
                    ((UseStmtContext) _localctx).dbName = (Token) _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SelectStmtContext extends ParserRuleContext {
        public FullselectStmtContext fullselectStmt() {
            return getRuleContext(FullselectStmtContext.class, 0);
        }

        public CteSelectStmtContext cteSelectStmt() {
            return getRuleContext(CteSelectStmtContext.class, 0);
        }

        public SelectStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_selectStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitSelectStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SelectStmtContext selectStmt() throws RecognitionException {
        SelectStmtContext _localctx = new SelectStmtContext(_ctx, getState());
        enterRule(_localctx, 96, RULE_selectStmt);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(881);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_WITH) {
                    {
                        setState(880);
                        cteSelectStmt();
                    }
                }

                setState(883);
                fullselectStmt();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CteSelectStmtContext extends ParserRuleContext {
        public TerminalNode T_WITH() {
            return getToken(CupidDBSqlParser.T_WITH, 0);
        }

        public List<CteSelectStmtItemContext> cteSelectStmtItem() {
            return getRuleContexts(CteSelectStmtItemContext.class);
        }

        public CteSelectStmtItemContext cteSelectStmtItem(int i) {
            return getRuleContext(CteSelectStmtItemContext.class, i);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public CteSelectStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_cteSelectStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCteSelectStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CteSelectStmtContext cteSelectStmt() throws RecognitionException {
        CteSelectStmtContext _localctx = new CteSelectStmtContext(_ctx, getState());
        enterRule(_localctx, 98, RULE_cteSelectStmt);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(885);
                match(T_WITH);
                setState(886);
                cteSelectStmtItem();
                setState(891);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(887);
                            match(T_COMMA);
                            setState(888);
                            cteSelectStmtItem();
                        }
                    }
                    setState(893);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CteSelectStmtItemContext extends ParserRuleContext {
        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_AS() {
            return getToken(CupidDBSqlParser.T_AS, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public FullselectStmtContext fullselectStmt() {
            return getRuleContext(FullselectStmtContext.class, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public CteSelectColsContext cteSelectCols() {
            return getRuleContext(CteSelectColsContext.class, 0);
        }

        public CteSelectStmtItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_cteSelectStmtItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCteSelectStmtItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CteSelectStmtItemContext cteSelectStmtItem() throws RecognitionException {
        CteSelectStmtItemContext _localctx = new CteSelectStmtItemContext(_ctx, getState());
        enterRule(_localctx, 100, RULE_cteSelectStmtItem);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(894);
                ident();
                setState(896);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_OPEN_P) {
                    {
                        setState(895);
                        cteSelectCols();
                    }
                }

                setState(898);
                match(T_AS);
                setState(899);
                match(T_OPEN_P);
                setState(900);
                fullselectStmt();
                setState(901);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class CteSelectColsContext extends ParserRuleContext {
        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public List<IdentContext> ident() {
            return getRuleContexts(IdentContext.class);
        }

        public IdentContext ident(int i) {
            return getRuleContext(IdentContext.class, i);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public CteSelectColsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_cteSelectCols;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCteSelectCols(this);
            else return visitor.visitChildren(this);
        }
    }

    public final CteSelectColsContext cteSelectCols() throws RecognitionException {
        CteSelectColsContext _localctx = new CteSelectColsContext(_ctx, getState());
        enterRule(_localctx, 102, RULE_cteSelectCols);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(903);
                match(T_OPEN_P);
                setState(904);
                ident();
                setState(909);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(905);
                            match(T_COMMA);
                            setState(906);
                            ident();
                        }
                    }
                    setState(911);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(912);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FullselectStmtContext extends ParserRuleContext {
        public List<FullselectStmtItemContext> fullselectStmtItem() {
            return getRuleContexts(FullselectStmtItemContext.class);
        }

        public FullselectStmtItemContext fullselectStmtItem(int i) {
            return getRuleContext(FullselectStmtItemContext.class, i);
        }

        public List<FullselectSetClauseContext> fullselectSetClause() {
            return getRuleContexts(FullselectSetClauseContext.class);
        }

        public FullselectSetClauseContext fullselectSetClause(int i) {
            return getRuleContext(FullselectSetClauseContext.class, i);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public FullselectStmtContext fullselectStmt() {
            return getRuleContext(FullselectStmtContext.class, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public FullselectStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fullselectStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFullselectStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FullselectStmtContext fullselectStmt() throws RecognitionException {
        FullselectStmtContext _localctx = new FullselectStmtContext(_ctx, getState());
        enterRule(_localctx, 104, RULE_fullselectStmt);
        int _la;
        try {
            setState(927);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 92, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(914);
                    fullselectStmtItem();
                    setState(920);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == T_EXCEPT || _la == T_INTERSECT || _la == T_UNION) {
                        {
                            {
                                setState(915);
                                fullselectSetClause();
                                setState(916);
                                fullselectStmtItem();
                            }
                        }
                        setState(922);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(923);
                    match(T_OPEN_P);
                    setState(924);
                    fullselectStmt();
                    setState(925);
                    match(T_CLOSE_P);
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FullselectStmtItemContext extends ParserRuleContext {
        public SubselectStmtContext subselectStmt() {
            return getRuleContext(SubselectStmtContext.class, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public FullselectStmtContext fullselectStmt() {
            return getRuleContext(FullselectStmtContext.class, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public FullselectStmtItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fullselectStmtItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFullselectStmtItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FullselectStmtItemContext fullselectStmtItem() throws RecognitionException {
        FullselectStmtItemContext _localctx = new FullselectStmtItemContext(_ctx, getState());
        enterRule(_localctx, 106, RULE_fullselectStmtItem);
        try {
            setState(934);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_SEL:
                case T_SELECT:
                    enterOuterAlt(_localctx, 1); {
                    setState(929);
                    subselectStmt();
                }
                    break;
                case T_OPEN_P:
                    enterOuterAlt(_localctx, 2); {
                    setState(930);
                    match(T_OPEN_P);
                    setState(931);
                    fullselectStmt();
                    setState(932);
                    match(T_CLOSE_P);
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FullselectSetClauseContext extends ParserRuleContext {
        public TerminalNode T_UNION() {
            return getToken(CupidDBSqlParser.T_UNION, 0);
        }

        public TerminalNode T_ALL() {
            return getToken(CupidDBSqlParser.T_ALL, 0);
        }

        public TerminalNode T_EXCEPT() {
            return getToken(CupidDBSqlParser.T_EXCEPT, 0);
        }

        public TerminalNode T_INTERSECT() {
            return getToken(CupidDBSqlParser.T_INTERSECT, 0);
        }

        public FullselectSetClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fullselectSetClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFullselectSetClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FullselectSetClauseContext fullselectSetClause() throws RecognitionException {
        FullselectSetClauseContext _localctx = new FullselectSetClauseContext(_ctx, getState());
        enterRule(_localctx, 108, RULE_fullselectSetClause);
        int _la;
        try {
            setState(948);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_UNION:
                    enterOuterAlt(_localctx, 1); {
                    setState(936);
                    match(T_UNION);
                    setState(938);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_ALL) {
                        {
                            setState(937);
                            match(T_ALL);
                        }
                    }

                }
                    break;
                case T_EXCEPT:
                    enterOuterAlt(_localctx, 2); {
                    setState(940);
                    match(T_EXCEPT);
                    setState(942);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_ALL) {
                        {
                            setState(941);
                            match(T_ALL);
                        }
                    }

                }
                    break;
                case T_INTERSECT:
                    enterOuterAlt(_localctx, 3); {
                    setState(944);
                    match(T_INTERSECT);
                    setState(946);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_ALL) {
                        {
                            setState(945);
                            match(T_ALL);
                        }
                    }

                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SubselectStmtContext extends ParserRuleContext {
        public SelectListContext selectList() {
            return getRuleContext(SelectListContext.class, 0);
        }

        public TerminalNode T_SELECT() {
            return getToken(CupidDBSqlParser.T_SELECT, 0);
        }

        public TerminalNode T_SEL() {
            return getToken(CupidDBSqlParser.T_SEL, 0);
        }

        public IntoClauseContext intoClause() {
            return getRuleContext(IntoClauseContext.class, 0);
        }

        public FromClauseContext fromClause() {
            return getRuleContext(FromClauseContext.class, 0);
        }

        public WhereClauseContext whereClause() {
            return getRuleContext(WhereClauseContext.class, 0);
        }

        public GroupByClauseContext groupByClause() {
            return getRuleContext(GroupByClauseContext.class, 0);
        }

        public HavingClauseContext havingClause() {
            return getRuleContext(HavingClauseContext.class, 0);
        }

        public QualifyClauseContext qualifyClause() {
            return getRuleContext(QualifyClauseContext.class, 0);
        }

        public OrderByClauseContext orderByClause() {
            return getRuleContext(OrderByClauseContext.class, 0);
        }

        public SelectOptionsContext selectOptions() {
            return getRuleContext(SelectOptionsContext.class, 0);
        }

        public SubselectStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_subselectStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitSubselectStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SubselectStmtContext subselectStmt() throws RecognitionException {
        SubselectStmtContext _localctx = new SubselectStmtContext(_ctx, getState());
        enterRule(_localctx, 110, RULE_subselectStmt);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(950);
                _la = _input.LA(1);
                if (!(_la == T_SEL || _la == T_SELECT)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(951);
                selectList();
                setState(953);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_INTO) {
                    {
                        setState(952);
                        intoClause();
                    }
                }

                setState(956);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_FROM) {
                    {
                        setState(955);
                        fromClause();
                    }
                }

                setState(959);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_WHERE) {
                    {
                        setState(958);
                        whereClause();
                    }
                }

                setState(962);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_GROUP) {
                    {
                        setState(961);
                        groupByClause();
                    }
                }

                setState(966);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case T_HAVING: {
                        setState(964);
                        havingClause();
                    }
                        break;
                    case T_QUALIFY: {
                        setState(965);
                        qualifyClause();
                    }
                        break;
                    case EOF:
                    case T_AUTO_INCREMENT:
                    case T_CHARACTER:
                    case T_CHARSET:
                    case T_COMMENT:
                    case T_DEFAULT:
                    case T_ENGINE:
                    case T_EXCEPT:
                    case T_INTERSECT:
                    case T_LIMIT:
                    case T_ON:
                    case T_ORDER:
                    case T_UNION:
                    case T_WITH:
                    case T_CLOSE_P:
                    case T_SEMICOLON:
                        break;
                    default:
                        break;
                }
                setState(969);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_ORDER) {
                    {
                        setState(968);
                        orderByClause();
                    }
                }

                setState(972);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_LIMIT || _la == T_WITH) {
                    {
                        setState(971);
                        selectOptions();
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SelectListContext extends ParserRuleContext {
        public List<SelectListItemContext> selectListItem() {
            return getRuleContexts(SelectListItemContext.class);
        }

        public SelectListItemContext selectListItem(int i) {
            return getRuleContext(SelectListItemContext.class, i);
        }

        public SelectListSetContext selectListSet() {
            return getRuleContext(SelectListSetContext.class, 0);
        }

        public SelectListLimitContext selectListLimit() {
            return getRuleContext(SelectListLimitContext.class, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public SelectListContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_selectList;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitSelectList(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SelectListContext selectList() throws RecognitionException {
        SelectListContext _localctx = new SelectListContext(_ctx, getState());
        enterRule(_localctx, 112, RULE_selectList);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(975);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 105, _ctx)) {
                    case 1: {
                        setState(974);
                        selectListSet();
                    }
                        break;
                }
                setState(978);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 106, _ctx)) {
                    case 1: {
                        setState(977);
                        selectListLimit();
                    }
                        break;
                }
                setState(980);
                selectListItem();
                setState(985);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(981);
                            match(T_COMMA);
                            setState(982);
                            selectListItem();
                        }
                    }
                    setState(987);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SelectListSetContext extends ParserRuleContext {
        public TerminalNode T_ALL() {
            return getToken(CupidDBSqlParser.T_ALL, 0);
        }

        public TerminalNode T_DISTINCT() {
            return getToken(CupidDBSqlParser.T_DISTINCT, 0);
        }

        public SelectListSetContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_selectListSet;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitSelectListSet(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SelectListSetContext selectListSet() throws RecognitionException {
        SelectListSetContext _localctx = new SelectListSetContext(_ctx, getState());
        enterRule(_localctx, 114, RULE_selectListSet);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(988);
                _la = _input.LA(1);
                if (!(_la == T_ALL || _la == T_DISTINCT)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SelectListLimitContext extends ParserRuleContext {
        public TerminalNode T_TOP() {
            return getToken(CupidDBSqlParser.T_TOP, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public SelectListLimitContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_selectListLimit;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitSelectListLimit(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SelectListLimitContext selectListLimit() throws RecognitionException {
        SelectListLimitContext _localctx = new SelectListLimitContext(_ctx, getState());
        enterRule(_localctx, 116, RULE_selectListLimit);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(990);
                match(T_TOP);
                setState(991);
                expr(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SelectListItemContext extends ParserRuleContext {
        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public SelectListAsteriskContext selectListAsterisk() {
            return getRuleContext(SelectListAsteriskContext.class, 0);
        }

        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_EQUAL() {
            return getToken(CupidDBSqlParser.T_EQUAL, 0);
        }

        public SelectListAliasContext selectListAlias() {
            return getRuleContext(SelectListAliasContext.class, 0);
        }

        public SelectListItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_selectListItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitSelectListItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SelectListItemContext selectListItem() throws RecognitionException {
        SelectListItemContext _localctx = new SelectListItemContext(_ctx, getState());
        enterRule(_localctx, 118, RULE_selectListItem);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1003);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 110, _ctx)) {
                    case 1: {
                        setState(996);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 108, _ctx)) {
                            case 1: {
                                setState(993);
                                ident();
                                setState(994);
                                match(T_EQUAL);
                            }
                                break;
                        }
                        setState(998);
                        expr(0);
                        setState(1000);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 109, _ctx)) {
                            case 1: {
                                setState(999);
                                selectListAlias();
                            }
                                break;
                        }
                    }
                        break;
                    case 2: {
                        setState(1002);
                        selectListAsterisk();
                    }
                        break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SelectListAliasContext extends ParserRuleContext {
        public List<IdentContext> ident() {
            return getRuleContexts(IdentContext.class);
        }

        public IdentContext ident(int i) {
            return getRuleContext(IdentContext.class, i);
        }

        public TerminalNode T_AS() {
            return getToken(CupidDBSqlParser.T_AS, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public TerminalNode T_TITLE() {
            return getToken(CupidDBSqlParser.T_TITLE, 0);
        }

        public TerminalNode L_S_STRING() {
            return getToken(CupidDBSqlParser.L_S_STRING, 0);
        }

        public SelectListAliasContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_selectListAlias;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitSelectListAlias(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SelectListAliasContext selectListAlias() throws RecognitionException {
        SelectListAliasContext _localctx = new SelectListAliasContext(_ctx, getState());
        enterRule(_localctx, 120, RULE_selectListAlias);
        int _la;
        try {
            setState(1027);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 114, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(1006);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 111, _ctx)) {
                        case 1: {
                            setState(1005);
                            match(T_AS);
                        }
                            break;
                    }
                    setState(1008);
                    ident();
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(1010);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_AS) {
                        {
                            setState(1009);
                            match(T_AS);
                        }
                    }

                    setState(1012);
                    match(T_OPEN_P);
                    setState(1013);
                    ident();
                    setState(1018);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == T_COMMA) {
                        {
                            {
                                setState(1014);
                                match(T_COMMA);
                                setState(1015);
                                ident();
                            }
                        }
                        setState(1020);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                    setState(1021);
                    match(T_CLOSE_P);
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(1023);
                    match(T_OPEN_P);
                    setState(1024);
                    match(T_TITLE);
                    setState(1025);
                    match(L_S_STRING);
                    setState(1026);
                    match(T_CLOSE_P);
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SelectListAsteriskContext extends ParserRuleContext {
        public TerminalNode T_MUL() {
            return getToken(CupidDBSqlParser.T_MUL, 0);
        }

        public TerminalNode L_ID() {
            return getToken(CupidDBSqlParser.L_ID, 0);
        }

        public TerminalNode T_DOT() {
            return getToken(CupidDBSqlParser.T_DOT, 0);
        }

        public SelectListAsteriskContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_selectListAsterisk;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitSelectListAsterisk(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SelectListAsteriskContext selectListAsterisk() throws RecognitionException {
        SelectListAsteriskContext _localctx = new SelectListAsteriskContext(_ctx, getState());
        enterRule(_localctx, 122, RULE_selectListAsterisk);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1031);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == L_ID) {
                    {
                        setState(1029);
                        match(L_ID);
                        setState(1030);
                        match(T_DOT);
                    }
                }

                setState(1033);
                match(T_MUL);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class IntoClauseContext extends ParserRuleContext {
        public TerminalNode T_INTO() {
            return getToken(CupidDBSqlParser.T_INTO, 0);
        }

        public List<IdentContext> ident() {
            return getRuleContexts(IdentContext.class);
        }

        public IdentContext ident(int i) {
            return getRuleContext(IdentContext.class, i);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public IntoClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_intoClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitIntoClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IntoClauseContext intoClause() throws RecognitionException {
        IntoClauseContext _localctx = new IntoClauseContext(_ctx, getState());
        enterRule(_localctx, 124, RULE_intoClause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1035);
                match(T_INTO);
                setState(1036);
                ident();
                setState(1041);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(1037);
                            match(T_COMMA);
                            setState(1038);
                            ident();
                        }
                    }
                    setState(1043);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FromClauseContext extends ParserRuleContext {
        public TerminalNode T_FROM() {
            return getToken(CupidDBSqlParser.T_FROM, 0);
        }

        public FromTableClauseContext fromTableClause() {
            return getRuleContext(FromTableClauseContext.class, 0);
        }

        public List<FromJoinClauseContext> fromJoinClause() {
            return getRuleContexts(FromJoinClauseContext.class);
        }

        public FromJoinClauseContext fromJoinClause(int i) {
            return getRuleContext(FromJoinClauseContext.class, i);
        }

        public FromClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fromClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFromClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FromClauseContext fromClause() throws RecognitionException {
        FromClauseContext _localctx = new FromClauseContext(_ctx, getState());
        enterRule(_localctx, 126, RULE_fromClause);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(1044);
                match(T_FROM);
                setState(1045);
                fromTableClause();
                setState(1049);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 117, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(1046);
                                fromJoinClause();
                            }
                        }
                    }
                    setState(1051);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 117, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FromTableClauseContext extends ParserRuleContext {
        public FromTableNameClauseContext fromTableNameClause() {
            return getRuleContext(FromTableNameClauseContext.class, 0);
        }

        public FromSubselectClauseContext fromSubselectClause() {
            return getRuleContext(FromSubselectClauseContext.class, 0);
        }

        public FromTableValuesClauseContext fromTableValuesClause() {
            return getRuleContext(FromTableValuesClauseContext.class, 0);
        }

        public FromTableClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fromTableClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFromTableClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FromTableClauseContext fromTableClause() throws RecognitionException {
        FromTableClauseContext _localctx = new FromTableClauseContext(_ctx, getState());
        enterRule(_localctx, 128, RULE_fromTableClause);
        try {
            setState(1055);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 118, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(1052);
                    fromTableNameClause();
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(1053);
                    fromSubselectClause();
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(1054);
                    fromTableValuesClause();
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FromTableNameClauseContext extends ParserRuleContext {
        public TableNameContext tableName() {
            return getRuleContext(TableNameContext.class, 0);
        }

        public FromAliasClauseContext fromAliasClause() {
            return getRuleContext(FromAliasClauseContext.class, 0);
        }

        public SampleClauseContext sampleClause() {
            return getRuleContext(SampleClauseContext.class, 0);
        }

        public FromTableNameClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fromTableNameClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFromTableNameClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FromTableNameClauseContext fromTableNameClause() throws RecognitionException {
        FromTableNameClauseContext _localctx = new FromTableNameClauseContext(_ctx, getState());
        enterRule(_localctx, 130, RULE_fromTableNameClause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1057);
                tableName();
                setState(1059);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 119, _ctx)) {
                    case 1: {
                        setState(1058);
                        fromAliasClause();
                    }
                        break;
                }
                setState(1062);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_TABLESAMPLE) {
                    {
                        setState(1061);
                        sampleClause();
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FromSubselectClauseContext extends ParserRuleContext {
        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public SelectStmtContext selectStmt() {
            return getRuleContext(SelectStmtContext.class, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public FromAliasClauseContext fromAliasClause() {
            return getRuleContext(FromAliasClauseContext.class, 0);
        }

        public FromSubselectClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fromSubselectClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFromSubselectClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FromSubselectClauseContext fromSubselectClause() throws RecognitionException {
        FromSubselectClauseContext _localctx = new FromSubselectClauseContext(_ctx, getState());
        enterRule(_localctx, 132, RULE_fromSubselectClause);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1064);
                match(T_OPEN_P);
                setState(1065);
                selectStmt();
                setState(1066);
                match(T_CLOSE_P);
                setState(1068);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 121, _ctx)) {
                    case 1: {
                        setState(1067);
                        fromAliasClause();
                    }
                        break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FromJoinClauseContext extends ParserRuleContext {
        public TerminalNode T_COMMA() {
            return getToken(CupidDBSqlParser.T_COMMA, 0);
        }

        public FromTableClauseContext fromTableClause() {
            return getRuleContext(FromTableClauseContext.class, 0);
        }

        public FromJoinTypeClauseContext fromJoinTypeClause() {
            return getRuleContext(FromJoinTypeClauseContext.class, 0);
        }

        public TerminalNode T_ON() {
            return getToken(CupidDBSqlParser.T_ON, 0);
        }

        public BoolExprContext boolExpr() {
            return getRuleContext(BoolExprContext.class, 0);
        }

        public FromJoinClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fromJoinClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFromJoinClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FromJoinClauseContext fromJoinClause() throws RecognitionException {
        FromJoinClauseContext _localctx = new FromJoinClauseContext(_ctx, getState());
        enterRule(_localctx, 134, RULE_fromJoinClause);
        try {
            setState(1077);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_COMMA:
                    enterOuterAlt(_localctx, 1); {
                    setState(1070);
                    match(T_COMMA);
                    setState(1071);
                    fromTableClause();
                }
                    break;
                case T_FULL:
                case T_INNER:
                case T_JOIN:
                case T_LEFT:
                case T_RIGHT:
                    enterOuterAlt(_localctx, 2); {
                    setState(1072);
                    fromJoinTypeClause();
                    setState(1073);
                    fromTableClause();
                    setState(1074);
                    match(T_ON);
                    setState(1075);
                    boolExpr(0);
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FromJoinTypeClauseContext extends ParserRuleContext {
        public TerminalNode T_JOIN() {
            return getToken(CupidDBSqlParser.T_JOIN, 0);
        }

        public TerminalNode T_INNER() {
            return getToken(CupidDBSqlParser.T_INNER, 0);
        }

        public TerminalNode T_LEFT() {
            return getToken(CupidDBSqlParser.T_LEFT, 0);
        }

        public TerminalNode T_RIGHT() {
            return getToken(CupidDBSqlParser.T_RIGHT, 0);
        }

        public TerminalNode T_FULL() {
            return getToken(CupidDBSqlParser.T_FULL, 0);
        }

        public TerminalNode T_OUTER() {
            return getToken(CupidDBSqlParser.T_OUTER, 0);
        }

        public FromJoinTypeClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fromJoinTypeClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFromJoinTypeClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FromJoinTypeClauseContext fromJoinTypeClause() throws RecognitionException {
        FromJoinTypeClauseContext _localctx = new FromJoinTypeClauseContext(_ctx, getState());
        enterRule(_localctx, 136, RULE_fromJoinTypeClause);
        int _la;
        try {
            setState(1088);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_INNER:
                case T_JOIN:
                    enterOuterAlt(_localctx, 1); {
                    setState(1080);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_INNER) {
                        {
                            setState(1079);
                            match(T_INNER);
                        }
                    }

                    setState(1082);
                    match(T_JOIN);
                }
                    break;
                case T_FULL:
                case T_LEFT:
                case T_RIGHT:
                    enterOuterAlt(_localctx, 2); {
                    setState(1083);
                    _la = _input.LA(1);
                    if (!(_la == T_FULL || _la == T_LEFT || _la == T_RIGHT)) {
                        _errHandler.recoverInline(this);
                    } else {
                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                        _errHandler.reportMatch(this);
                        consume();
                    }
                    setState(1085);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_OUTER) {
                        {
                            setState(1084);
                            match(T_OUTER);
                        }
                    }

                    setState(1087);
                    match(T_JOIN);
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FromTableValuesClauseContext extends ParserRuleContext {
        public TerminalNode T_TABLE() {
            return getToken(CupidDBSqlParser.T_TABLE, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public TerminalNode T_VALUES() {
            return getToken(CupidDBSqlParser.T_VALUES, 0);
        }

        public List<FromTableValuesRowContext> fromTableValuesRow() {
            return getRuleContexts(FromTableValuesRowContext.class);
        }

        public FromTableValuesRowContext fromTableValuesRow(int i) {
            return getRuleContext(FromTableValuesRowContext.class, i);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public FromAliasClauseContext fromAliasClause() {
            return getRuleContext(FromAliasClauseContext.class, 0);
        }

        public FromTableValuesClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fromTableValuesClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFromTableValuesClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FromTableValuesClauseContext fromTableValuesClause() throws RecognitionException {
        FromTableValuesClauseContext _localctx = new FromTableValuesClauseContext(_ctx, getState());
        enterRule(_localctx, 138, RULE_fromTableValuesClause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1090);
                match(T_TABLE);
                setState(1091);
                match(T_OPEN_P);
                setState(1092);
                match(T_VALUES);
                setState(1093);
                fromTableValuesRow();
                setState(1098);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(1094);
                            match(T_COMMA);
                            setState(1095);
                            fromTableValuesRow();
                        }
                    }
                    setState(1100);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(1101);
                match(T_CLOSE_P);
                setState(1103);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 127, _ctx)) {
                    case 1: {
                        setState(1102);
                        fromAliasClause();
                    }
                        break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FromTableValuesRowContext extends ParserRuleContext {
        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public FromTableValuesRowContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fromTableValuesRow;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFromTableValuesRow(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FromTableValuesRowContext fromTableValuesRow() throws RecognitionException {
        FromTableValuesRowContext _localctx = new FromTableValuesRowContext(_ctx, getState());
        enterRule(_localctx, 140, RULE_fromTableValuesRow);
        int _la;
        try {
            setState(1117);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 129, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(1105);
                    expr(0);
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(1106);
                    match(T_OPEN_P);
                    setState(1107);
                    expr(0);
                    setState(1112);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == T_COMMA) {
                        {
                            {
                                setState(1108);
                                match(T_COMMA);
                                setState(1109);
                                expr(0);
                            }
                        }
                        setState(1114);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                    setState(1115);
                    match(T_CLOSE_P);
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FromAliasClauseContext extends ParserRuleContext {
        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_AS() {
            return getToken(CupidDBSqlParser.T_AS, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public List<TerminalNode> L_ID() {
            return getTokens(CupidDBSqlParser.L_ID);
        }

        public TerminalNode L_ID(int i) {
            return getToken(CupidDBSqlParser.L_ID, i);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public FromAliasClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_fromAliasClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFromAliasClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FromAliasClauseContext fromAliasClause() throws RecognitionException {
        FromAliasClauseContext _localctx = new FromAliasClauseContext(_ctx, getState());
        enterRule(_localctx, 142, RULE_fromAliasClause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1119);
                if (!(!_input.LT(1).getText().equalsIgnoreCase("EXEC")
                    && !_input.LT(1).getText().equalsIgnoreCase("EXECUTE")
                    && !_input.LT(1).getText().equalsIgnoreCase("INNER")
                    && !_input.LT(1).getText().equalsIgnoreCase("LEFT")
                    && !_input.LT(1).getText().equalsIgnoreCase("GROUP")
                    && !_input.LT(1).getText().equalsIgnoreCase("ORDER")
                    && !_input.LT(1).getText().equalsIgnoreCase("LIMIT")
                    && !_input.LT(1).getText().equalsIgnoreCase("WITH")))
                    throw new FailedPredicateException(
                            this,
                            "!_input.LT(1).getText().equalsIgnoreCase(\"EXEC\") &&\n        !_input.LT(1).getText().equalsIgnoreCase(\"EXECUTE\") &&\n        !_input.LT(1).getText().equalsIgnoreCase(\"INNER\") &&\n        !_input.LT(1).getText().equalsIgnoreCase(\"LEFT\") &&\n        !_input.LT(1).getText().equalsIgnoreCase(\"GROUP\") &&\n        !_input.LT(1).getText().equalsIgnoreCase(\"ORDER\") &&\n        !_input.LT(1).getText().equalsIgnoreCase(\"LIMIT\") &&\n        !_input.LT(1).getText().equalsIgnoreCase(\"WITH\")"
                    );
                setState(1121);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 130, _ctx)) {
                    case 1: {
                        setState(1120);
                        match(T_AS);
                    }
                        break;
                }
                setState(1123);
                ident();
                setState(1134);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_OPEN_P) {
                    {
                        setState(1124);
                        match(T_OPEN_P);
                        setState(1125);
                        match(L_ID);
                        setState(1130);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        while (_la == T_COMMA) {
                            {
                                {
                                    setState(1126);
                                    match(T_COMMA);
                                    setState(1127);
                                    match(L_ID);
                                }
                            }
                            setState(1132);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                        }
                        setState(1133);
                        match(T_CLOSE_P);
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class TableNameContext extends ParserRuleContext {
        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TableNameContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_tableName;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitTableName(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TableNameContext tableName() throws RecognitionException {
        TableNameContext _localctx = new TableNameContext(_ctx, getState());
        enterRule(_localctx, 144, RULE_tableName);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1136);
                ident();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class WhereClauseContext extends ParserRuleContext {
        public TerminalNode T_WHERE() {
            return getToken(CupidDBSqlParser.T_WHERE, 0);
        }

        public BoolExprContext boolExpr() {
            return getRuleContext(BoolExprContext.class, 0);
        }

        public WhereClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_whereClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitWhereClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final WhereClauseContext whereClause() throws RecognitionException {
        WhereClauseContext _localctx = new WhereClauseContext(_ctx, getState());
        enterRule(_localctx, 146, RULE_whereClause);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1138);
                match(T_WHERE);
                setState(1139);
                boolExpr(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class GroupByClauseContext extends ParserRuleContext {
        public TerminalNode T_GROUP() {
            return getToken(CupidDBSqlParser.T_GROUP, 0);
        }

        public TerminalNode T_BY() {
            return getToken(CupidDBSqlParser.T_BY, 0);
        }

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public GroupByClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_groupByClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitGroupByClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final GroupByClauseContext groupByClause() throws RecognitionException {
        GroupByClauseContext _localctx = new GroupByClauseContext(_ctx, getState());
        enterRule(_localctx, 148, RULE_groupByClause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1141);
                match(T_GROUP);
                setState(1142);
                match(T_BY);
                setState(1143);
                expr(0);
                setState(1148);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(1144);
                            match(T_COMMA);
                            setState(1145);
                            expr(0);
                        }
                    }
                    setState(1150);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class HavingClauseContext extends ParserRuleContext {
        public TerminalNode T_HAVING() {
            return getToken(CupidDBSqlParser.T_HAVING, 0);
        }

        public BoolExprContext boolExpr() {
            return getRuleContext(BoolExprContext.class, 0);
        }

        public HavingClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_havingClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitHavingClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final HavingClauseContext havingClause() throws RecognitionException {
        HavingClauseContext _localctx = new HavingClauseContext(_ctx, getState());
        enterRule(_localctx, 150, RULE_havingClause);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1151);
                match(T_HAVING);
                setState(1152);
                boolExpr(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class QualifyClauseContext extends ParserRuleContext {
        public TerminalNode T_QUALIFY() {
            return getToken(CupidDBSqlParser.T_QUALIFY, 0);
        }

        public BoolExprContext boolExpr() {
            return getRuleContext(BoolExprContext.class, 0);
        }

        public QualifyClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_qualifyClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitQualifyClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final QualifyClauseContext qualifyClause() throws RecognitionException {
        QualifyClauseContext _localctx = new QualifyClauseContext(_ctx, getState());
        enterRule(_localctx, 152, RULE_qualifyClause);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1154);
                match(T_QUALIFY);
                setState(1155);
                boolExpr(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class OrderByClauseContext extends ParserRuleContext {
        public TerminalNode T_ORDER() {
            return getToken(CupidDBSqlParser.T_ORDER, 0);
        }

        public TerminalNode T_BY() {
            return getToken(CupidDBSqlParser.T_BY, 0);
        }

        public List<OrderByClauseItemContext> orderByClauseItem() {
            return getRuleContexts(OrderByClauseItemContext.class);
        }

        public OrderByClauseItemContext orderByClauseItem(int i) {
            return getRuleContext(OrderByClauseItemContext.class, i);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public OrderByClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_orderByClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitOrderByClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final OrderByClauseContext orderByClause() throws RecognitionException {
        OrderByClauseContext _localctx = new OrderByClauseContext(_ctx, getState());
        enterRule(_localctx, 154, RULE_orderByClause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1157);
                match(T_ORDER);
                setState(1158);
                match(T_BY);
                setState(1159);
                orderByClauseItem();
                setState(1164);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(1160);
                            match(T_COMMA);
                            setState(1161);
                            orderByClauseItem();
                        }
                    }
                    setState(1166);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class OrderByClauseItemContext extends ParserRuleContext {
        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode T_ASC() {
            return getToken(CupidDBSqlParser.T_ASC, 0);
        }

        public TerminalNode T_DESC() {
            return getToken(CupidDBSqlParser.T_DESC, 0);
        }

        public OrderByClauseItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_orderByClauseItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitOrderByClauseItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final OrderByClauseItemContext orderByClauseItem() throws RecognitionException {
        OrderByClauseItemContext _localctx = new OrderByClauseItemContext(_ctx, getState());
        enterRule(_localctx, 156, RULE_orderByClauseItem);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1167);
                expr(0);
                setState(1169);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_ASC || _la == T_DESC) {
                    {
                        setState(1168);
                        _la = _input.LA(1);
                        if (!(_la == T_ASC || _la == T_DESC)) {
                            _errHandler.recoverInline(this);
                        } else {
                            if (_input.LA(1) == Token.EOF) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SampleClauseContext extends ParserRuleContext {
        public TerminalNode T_TABLESAMPLE() {
            return getToken(CupidDBSqlParser.T_TABLESAMPLE, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public TerminalNode L_INT() {
            return getToken(CupidDBSqlParser.L_INT, 0);
        }

        public TerminalNode T_PERCENT() {
            return getToken(CupidDBSqlParser.T_PERCENT, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public SampleClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_sampleClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitSampleClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SampleClauseContext sampleClause() throws RecognitionException {
        SampleClauseContext _localctx = new SampleClauseContext(_ctx, getState());
        enterRule(_localctx, 158, RULE_sampleClause);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1171);
                match(T_TABLESAMPLE);
                setState(1172);
                match(T_OPEN_P);
                setState(1173);
                match(L_INT);
                setState(1174);
                match(T_PERCENT);
                setState(1175);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SelectOptionsContext extends ParserRuleContext {
        public List<SelectOptionsItemContext> selectOptionsItem() {
            return getRuleContexts(SelectOptionsItemContext.class);
        }

        public SelectOptionsItemContext selectOptionsItem(int i) {
            return getRuleContext(SelectOptionsItemContext.class, i);
        }

        public SelectOptionsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_selectOptions;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitSelectOptions(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SelectOptionsContext selectOptions() throws RecognitionException {
        SelectOptionsContext _localctx = new SelectOptionsContext(_ctx, getState());
        enterRule(_localctx, 160, RULE_selectOptions);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1178);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(1177);
                            selectOptionsItem();
                        }
                    }
                    setState(1180);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while (_la == T_LIMIT || _la == T_WITH);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SelectOptionsItemContext extends ParserRuleContext {
        public TerminalNode T_LIMIT() {
            return getToken(CupidDBSqlParser.T_LIMIT, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode T_WITH() {
            return getToken(CupidDBSqlParser.T_WITH, 0);
        }

        public TerminalNode T_RR() {
            return getToken(CupidDBSqlParser.T_RR, 0);
        }

        public TerminalNode T_RS() {
            return getToken(CupidDBSqlParser.T_RS, 0);
        }

        public TerminalNode T_CS() {
            return getToken(CupidDBSqlParser.T_CS, 0);
        }

        public TerminalNode T_UR() {
            return getToken(CupidDBSqlParser.T_UR, 0);
        }

        public TerminalNode T_USE() {
            return getToken(CupidDBSqlParser.T_USE, 0);
        }

        public TerminalNode T_AND() {
            return getToken(CupidDBSqlParser.T_AND, 0);
        }

        public TerminalNode T_KEEP() {
            return getToken(CupidDBSqlParser.T_KEEP, 0);
        }

        public TerminalNode T_LOCKS() {
            return getToken(CupidDBSqlParser.T_LOCKS, 0);
        }

        public TerminalNode T_EXCLUSIVE() {
            return getToken(CupidDBSqlParser.T_EXCLUSIVE, 0);
        }

        public TerminalNode T_UPDATE() {
            return getToken(CupidDBSqlParser.T_UPDATE, 0);
        }

        public SelectOptionsItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_selectOptionsItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitSelectOptionsItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final SelectOptionsItemContext selectOptionsItem() throws RecognitionException {
        SelectOptionsItemContext _localctx = new SelectOptionsItemContext(_ctx, getState());
        enterRule(_localctx, 162, RULE_selectOptionsItem);
        int _la;
        try {
            setState(1193);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_LIMIT:
                    enterOuterAlt(_localctx, 1); {
                    setState(1182);
                    match(T_LIMIT);
                    setState(1183);
                    expr(0);
                }
                    break;
                case T_WITH:
                    enterOuterAlt(_localctx, 2); {
                    setState(1184);
                    match(T_WITH);
                    setState(1185);
                    _la = _input.LA(1);
                    if (!(_la == T_CS || _la == T_RR || _la == T_RS || _la == T_UR)) {
                        _errHandler.recoverInline(this);
                    } else {
                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                        _errHandler.reportMatch(this);
                        consume();
                    }
                    setState(1191);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_USE) {
                        {
                            setState(1186);
                            match(T_USE);
                            setState(1187);
                            match(T_AND);
                            setState(1188);
                            match(T_KEEP);
                            setState(1189);
                            _la = _input.LA(1);
                            if (!(_la == T_EXCLUSIVE || _la == T_UPDATE)) {
                                _errHandler.recoverInline(this);
                            } else {
                                if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                _errHandler.reportMatch(this);
                                consume();
                            }
                            setState(1190);
                            match(T_LOCKS);
                        }
                    }

                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class UpdateStmtContext extends ParserRuleContext {
        public TerminalNode T_UPDATE() {
            return getToken(CupidDBSqlParser.T_UPDATE, 0);
        }

        public UpdateTableContext updateTable() {
            return getRuleContext(UpdateTableContext.class, 0);
        }

        public TerminalNode T_SET() {
            return getToken(CupidDBSqlParser.T_SET, 0);
        }

        public UpdateAssignmentContext updateAssignment() {
            return getRuleContext(UpdateAssignmentContext.class, 0);
        }

        public WhereClauseContext whereClause() {
            return getRuleContext(WhereClauseContext.class, 0);
        }

        public UpdateUpsertContext updateUpsert() {
            return getRuleContext(UpdateUpsertContext.class, 0);
        }

        public UpdateStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_updateStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitUpdateStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final UpdateStmtContext updateStmt() throws RecognitionException {
        UpdateStmtContext _localctx = new UpdateStmtContext(_ctx, getState());
        enterRule(_localctx, 164, RULE_updateStmt);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1195);
                match(T_UPDATE);
                setState(1196);
                updateTable();
                setState(1197);
                match(T_SET);
                setState(1198);
                updateAssignment();
                setState(1200);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_WHERE) {
                    {
                        setState(1199);
                        whereClause();
                    }
                }

                setState(1203);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_ELSE) {
                    {
                        setState(1202);
                        updateUpsert();
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class UpdateAssignmentContext extends ParserRuleContext {
        public List<AssignmentStmtItemContext> assignmentStmtItem() {
            return getRuleContexts(AssignmentStmtItemContext.class);
        }

        public AssignmentStmtItemContext assignmentStmtItem(int i) {
            return getRuleContext(AssignmentStmtItemContext.class, i);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public UpdateAssignmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_updateAssignment;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitUpdateAssignment(this);
            else return visitor.visitChildren(this);
        }
    }

    public final UpdateAssignmentContext updateAssignment() throws RecognitionException {
        UpdateAssignmentContext _localctx = new UpdateAssignmentContext(_ctx, getState());
        enterRule(_localctx, 166, RULE_updateAssignment);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1205);
                assignmentStmtItem();
                setState(1210);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(1206);
                            match(T_COMMA);
                            setState(1207);
                            assignmentStmtItem();
                        }
                    }
                    setState(1212);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class UpdateTableContext extends ParserRuleContext {
        public TableNameContext tableName() {
            return getRuleContext(TableNameContext.class, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public SelectStmtContext selectStmt() {
            return getRuleContext(SelectStmtContext.class, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public FromClauseContext fromClause() {
            return getRuleContext(FromClauseContext.class, 0);
        }

        public TerminalNode T_AS() {
            return getToken(CupidDBSqlParser.T_AS, 0);
        }

        public UpdateTableContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_updateTable;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitUpdateTable(this);
            else return visitor.visitChildren(this);
        }
    }

    public final UpdateTableContext updateTable() throws RecognitionException {
        UpdateTableContext _localctx = new UpdateTableContext(_ctx, getState());
        enterRule(_localctx, 168, RULE_updateTable);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1221);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case T_ACTION:
                    case T_ADD2:
                    case T_ALL:
                    case T_ALLOCATE:
                    case T_ALTER:
                    case T_AND:
                    case T_ANSI_NULLS:
                    case T_ANSI_PADDING:
                    case T_AS:
                    case T_ASC:
                    case T_ASSOCIATE:
                    case T_AT:
                    case T_AVG:
                    case T_BATCHSIZE:
                    case T_BEGIN:
                    case T_BETWEEN:
                    case T_BIGINT:
                    case T_BINARY_DOUBLE:
                    case T_BINARY_FLOAT:
                    case T_BIT:
                    case T_BODY:
                    case T_BREAK:
                    case T_BY:
                    case T_BYTE:
                    case T_CALL:
                    case T_CALLER:
                    case T_CASCADE:
                    case T_CASE:
                    case T_CASESPECIFIC:
                    case T_CAST:
                    case T_CHAR:
                    case T_CHARACTER:
                    case T_CHARSET:
                    case T_CLIENT:
                    case T_CLOSE:
                    case T_CLUSTERED:
                    case T_CMP:
                    case T_COLLECT:
                    case T_COLLECTION:
                    case T_COLUMN:
                    case T_COMMENT:
                    case T_CONSTANT:
                    case T_COMMIT:
                    case T_COMPRESS:
                    case T_CONCAT:
                    case T_CONDITION:
                    case T_CONSTRAINT:
                    case T_CONTINUE:
                    case T_COPY:
                    case T_COUNT:
                    case T_COUNT_BIG:
                    case T_CREATION:
                    case T_CREATOR:
                    case T_CS:
                    case T_CURRENT:
                    case T_CURRENT_SCHEMA:
                    case T_CURSOR:
                    case T_DATABASE:
                    case T_DATA:
                    case T_DATE:
                    case T_DATETIME:
                    case T_DAY:
                    case T_DAYS:
                    case T_DEC:
                    case T_DECIMAL:
                    case T_DECLARE:
                    case T_DEFAULT:
                    case T_DEFERRED:
                    case T_DEFINED:
                    case T_DEFINER:
                    case T_DEFINITION:
                    case T_DELETE:
                    case T_DELIMITED:
                    case T_DELIMITER:
                    case T_DESC:
                    case T_DESCRIBE:
                    case T_DIAGNOSTICS:
                    case T_DIR:
                    case T_DIRECTORY:
                    case T_DISTINCT:
                    case T_DISTRIBUTE:
                    case T_DO:
                    case T_DOUBLE:
                    case T_DOWNLOAD:
                    case T_DROP:
                    case T_DYNAMIC:
                    case T_ENABLE:
                    case T_ENGINE:
                    case T_ESCAPED:
                    case T_EXCEPT:
                    case T_EXEC:
                    case T_EXECUTE:
                    case T_EXCEPTION:
                    case T_EXCLUSIVE:
                    case T_EXISTS:
                    case T_EXIT:
                    case T_FALLBACK:
                    case T_FALSE:
                    case T_FETCH:
                    case T_FIELDS:
                    case T_FILE:
                    case T_FILES:
                    case T_FLOAT:
                    case T_FOR:
                    case T_FOREIGN:
                    case T_FORMAT:
                    case T_FOUND:
                    case T_FROM:
                    case T_FULL:
                    case T_FUNCTION:
                    case T_GET:
                    case T_GLOBAL:
                    case T_GO:
                    case T_GRANT:
                    case T_GROUP:
                    case T_HANDLER:
                    case T_HASH:
                    case T_HAVING:
                    case T_HOST:
                    case T_IDENTITY:
                    case T_IF:
                    case T_IGNORE:
                    case T_IMMEDIATE:
                    case T_IN:
                    case T_INCLUDE:
                    case T_INDEX:
                    case T_INITRANS:
                    case T_INNER:
                    case T_INOUT:
                    case T_INSERT:
                    case T_INT:
                    case T_INT2:
                    case T_INT4:
                    case T_INT8:
                    case T_INTEGER:
                    case T_INTERSECT:
                    case T_INTERVAL:
                    case T_INTO:
                    case T_INVOKER:
                    case T_IS:
                    case T_ISOPEN:
                    case T_ITEMS:
                    case T_JOIN:
                    case T_KEEP:
                    case T_KEY:
                    case T_KEYS:
                    case T_LANGUAGE:
                    case T_LEAVE:
                    case T_LEFT:
                    case T_LIKE:
                    case T_LIMIT:
                    case T_LINES:
                    case T_LOCAL:
                    case T_LOCATION:
                    case T_LOCATOR:
                    case T_LOCATORS:
                    case T_LOCKS:
                    case T_LOG:
                    case T_LOGGED:
                    case T_LOGGING:
                    case T_LOOP:
                    case T_MAP:
                    case T_MATCHED:
                    case T_MAX:
                    case T_MAXTRANS:
                    case T_MERGE:
                    case T_MESSAGE_TEXT:
                    case T_MICROSECOND:
                    case T_MICROSECONDS:
                    case T_MIN:
                    case T_MULTISET:
                    case T_NCHAR:
                    case T_NEW:
                    case T_NVARCHAR:
                    case T_NO:
                    case T_NOCOUNT:
                    case T_NOCOMPRESS:
                    case T_NOLOGGING:
                    case T_NONE:
                    case T_NOT:
                    case T_NOTFOUND:
                    case T_NUMERIC:
                    case T_NUMBER:
                    case T_OBJECT:
                    case T_OFF:
                    case T_ON:
                    case T_ONLY:
                    case T_OPEN:
                    case T_OR:
                    case T_ORDER:
                    case T_OUT:
                    case T_OUTER:
                    case T_OVER:
                    case T_OVERWRITE:
                    case T_OWNER:
                    case T_PACKAGE:
                    case T_PARTITION:
                    case T_PCTFREE:
                    case T_PCTUSED:
                    case T_PRECISION:
                    case T_PRESERVE:
                    case T_PRIMARY:
                    case T_PRINT:
                    case T_PROC:
                    case T_PROCEDURE:
                    case T_QUALIFY:
                    case T_QUERY_BAND:
                    case T_QUIT:
                    case T_QUOTED_IDENTIFIER:
                    case T_QUOTES:
                    case T_RAISE:
                    case T_REAL:
                    case T_REFERENCES:
                    case T_REGEXP:
                    case T_REPLACE:
                    case T_RESIGNAL:
                    case T_RESTRICT:
                    case T_RESULT:
                    case T_RESULT_SET_LOCATOR:
                    case T_RETURN:
                    case T_RETURNS:
                    case T_REVERSE:
                    case T_RIGHT:
                    case T_RLIKE:
                    case T_ROLE:
                    case T_ROLLBACK:
                    case T_ROW:
                    case T_ROWS:
                    case T_ROW_COUNT:
                    case T_RR:
                    case T_RS:
                    case T_PWD:
                    case T_TRIM:
                    case T_SCHEMA:
                    case T_SECOND:
                    case T_SECONDS:
                    case T_SECURITY:
                    case T_SEGMENT:
                    case T_SEL:
                    case T_SELECT:
                    case T_SET:
                    case T_SESSION:
                    case T_SESSIONS:
                    case T_SETS:
                    case T_SIGNAL:
                    case T_SIMPLE_DOUBLE:
                    case T_SIMPLE_FLOAT:
                    case T_SMALLDATETIME:
                    case T_SMALLINT:
                    case T_SQL:
                    case T_SQLEXCEPTION:
                    case T_SQLINSERT:
                    case T_SQLSTATE:
                    case T_SQLWARNING:
                    case T_STATS:
                    case T_STATISTICS:
                    case T_STEP:
                    case T_STORAGE:
                    case T_STORE:
                    case T_STORED:
                    case T_STREAM:
                    case T_STRING:
                    case T_SUBDIR:
                    case T_SUBSTRING:
                    case T_SUM:
                    case T_SUMMARY:
                    case T_SYS_REFCURSOR:
                    case T_TABLE:
                    case T_TABLESPACE:
                    case T_TEMPORARY:
                    case T_TERMINATED:
                    case T_TEXTIMAGE_ON:
                    case T_THEN:
                    case T_TIMESTAMP:
                    case T_TITLE:
                    case T_TO:
                    case T_TOP:
                    case T_TRANSACTION:
                    case T_TRUE:
                    case T_TRUNCATE:
                    case T_UNIQUE:
                    case T_UPDATE:
                    case T_UR:
                    case T_USE:
                    case T_USING:
                    case T_VALUE:
                    case T_VALUES:
                    case T_VAR:
                    case T_VARCHAR:
                    case T_VARCHAR2:
                    case T_VARYING:
                    case T_VOLATILE:
                    case T_WHILE:
                    case T_WITH:
                    case T_WITHOUT:
                    case T_WORK:
                    case T_XACT_ABORT:
                    case T_XML:
                    case T_YES:
                    case T_ACTIVITY_COUNT:
                    case T_CUME_DIST:
                    case T_CURRENT_DATE:
                    case T_CURRENT_TIME:
                    case T_PI:
                    case T_CURRENT_TIMESTAMP:
                    case T_CURRENT_USER:
                    case T_DENSE_RANK:
                    case T_FIRST_VALUE:
                    case T_LAG:
                    case T_LAST_VALUE:
                    case T_LEAD:
                    case T_PART_COUNT:
                    case T_PART_LOC:
                    case T_RANK:
                    case T_ROW_NUMBER:
                    case T_STDEV:
                    case T_SYSDATE:
                    case T_VARIANCE:
                    case T_USER:
                    case T_SUB:
                    case L_ID: {
                        setState(1213);
                        tableName();
                        setState(1215);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 142, _ctx)) {
                            case 1: {
                                setState(1214);
                                fromClause();
                            }
                                break;
                        }
                    }
                        break;
                    case T_OPEN_P: {
                        setState(1217);
                        match(T_OPEN_P);
                        setState(1218);
                        selectStmt();
                        setState(1219);
                        match(T_CLOSE_P);
                    }
                        break;
                    default:
                        throw new NoViableAltException(this);
                }
                setState(1227);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 145, _ctx)) {
                    case 1: {
                        setState(1224);
                        _errHandler.sync(this);
                        switch (getInterpreter().adaptivePredict(_input, 144, _ctx)) {
                            case 1: {
                                setState(1223);
                                match(T_AS);
                            }
                                break;
                        }
                        setState(1226);
                        ident();
                    }
                        break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class UpdateUpsertContext extends ParserRuleContext {
        public TerminalNode T_ELSE() {
            return getToken(CupidDBSqlParser.T_ELSE, 0);
        }

        public InsertStmtContext insertStmt() {
            return getRuleContext(InsertStmtContext.class, 0);
        }

        public UpdateUpsertContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_updateUpsert;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitUpdateUpsert(this);
            else return visitor.visitChildren(this);
        }
    }

    public final UpdateUpsertContext updateUpsert() throws RecognitionException {
        UpdateUpsertContext _localctx = new UpdateUpsertContext(_ctx, getState());
        enterRule(_localctx, 170, RULE_updateUpsert);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1229);
                match(T_ELSE);
                setState(1230);
                insertStmt();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DeleteStmtContext extends ParserRuleContext {
        public TerminalNode T_DELETE() {
            return getToken(CupidDBSqlParser.T_DELETE, 0);
        }

        public TableNameContext tableName() {
            return getRuleContext(TableNameContext.class, 0);
        }

        public TerminalNode T_FROM() {
            return getToken(CupidDBSqlParser.T_FROM, 0);
        }

        public DeleteAliasContext deleteAlias() {
            return getRuleContext(DeleteAliasContext.class, 0);
        }

        public WhereClauseContext whereClause() {
            return getRuleContext(WhereClauseContext.class, 0);
        }

        public TerminalNode T_ALL() {
            return getToken(CupidDBSqlParser.T_ALL, 0);
        }

        public DeleteStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_deleteStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDeleteStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DeleteStmtContext deleteStmt() throws RecognitionException {
        DeleteStmtContext _localctx = new DeleteStmtContext(_ctx, getState());
        enterRule(_localctx, 172, RULE_deleteStmt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1232);
                match(T_DELETE);
                setState(1234);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 146, _ctx)) {
                    case 1: {
                        setState(1233);
                        match(T_FROM);
                    }
                        break;
                }
                setState(1236);
                tableName();
                setState(1238);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 147, _ctx)) {
                    case 1: {
                        setState(1237);
                        deleteAlias();
                    }
                        break;
                }
                setState(1242);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case T_WHERE: {
                        setState(1240);
                        whereClause();
                    }
                        break;
                    case T_ALL: {
                        setState(1241);
                        match(T_ALL);
                    }
                        break;
                    case EOF:
                    case T_SEMICOLON:
                        break;
                    default:
                        break;
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DeleteAliasContext extends ParserRuleContext {
        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_AS() {
            return getToken(CupidDBSqlParser.T_AS, 0);
        }

        public DeleteAliasContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_deleteAlias;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDeleteAlias(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DeleteAliasContext deleteAlias() throws RecognitionException {
        DeleteAliasContext _localctx = new DeleteAliasContext(_ctx, getState());
        enterRule(_localctx, 174, RULE_deleteAlias);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1244);
                if (!(!_input.LT(1).getText().equalsIgnoreCase("ALL")))
                    throw new FailedPredicateException(
                        this,
                        "!_input.LT(1).getText().equalsIgnoreCase(\"ALL\")"
                    );
                setState(1246);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 149, _ctx)) {
                    case 1: {
                        setState(1245);
                        match(T_AS);
                    }
                        break;
                }
                setState(1248);
                ident();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DescribeStmtContext extends ParserRuleContext {
        public UserDotDbDotTableContext userDotDbDotTable() {
            return getRuleContext(UserDotDbDotTableContext.class, 0);
        }

        public TerminalNode T_DESCRIBE() {
            return getToken(CupidDBSqlParser.T_DESCRIBE, 0);
        }

        public TerminalNode T_DESC() {
            return getToken(CupidDBSqlParser.T_DESC, 0);
        }

        public TerminalNode T_TABLE() {
            return getToken(CupidDBSqlParser.T_TABLE, 0);
        }

        public TerminalNode T_VIEW() {
            return getToken(CupidDBSqlParser.T_VIEW, 0);
        }

        public DescribeStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_describeStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDescribeStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DescribeStmtContext describeStmt() throws RecognitionException {
        DescribeStmtContext _localctx = new DescribeStmtContext(_ctx, getState());
        enterRule(_localctx, 176, RULE_describeStmt);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1250);
                _la = _input.LA(1);
                if (!(_la == T_DESC || _la == T_DESCRIBE)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(1252);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_TABLE || _la == T_VIEW) {
                    {
                        setState(1251);
                        _la = _input.LA(1);
                        if (!(_la == T_TABLE || _la == T_VIEW)) {
                            _errHandler.recoverInline(this);
                        } else {
                            if (_input.LA(1) == Token.EOF) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                    }
                }

                setState(1254);
                userDotDbDotTable();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class LoadStmtContext extends ParserRuleContext {
        public TerminalNode T_LOAD() {
            return getToken(CupidDBSqlParser.T_LOAD, 0);
        }

        public TerminalNode T_INPATH() {
            return getToken(CupidDBSqlParser.T_INPATH, 0);
        }

        public StringContext string() {
            return getRuleContext(StringContext.class, 0);
        }

        public TerminalNode T_TO() {
            return getToken(CupidDBSqlParser.T_TO, 0);
        }

        public Table_nameContext table_name() {
            return getRuleContext(Table_nameContext.class, 0);
        }

        public TerminalNode T_CSV() {
            return getToken(CupidDBSqlParser.T_CSV, 0);
        }

        public TerminalNode T_TABLE() {
            return getToken(CupidDBSqlParser.T_TABLE, 0);
        }

        public Load_mapping_columnsContext load_mapping_columns() {
            return getRuleContext(Load_mapping_columnsContext.class, 0);
        }

        public Csv_file_optionsContext csv_file_options() {
            return getRuleContext(Csv_file_optionsContext.class, 0);
        }

        public Csv_file_formatContext csv_file_format() {
            return getRuleContext(Csv_file_formatContext.class, 0);
        }

        public LoadStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_loadStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitLoadStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final LoadStmtContext loadStmt() throws RecognitionException {
        LoadStmtContext _localctx = new LoadStmtContext(_ctx, getState());
        enterRule(_localctx, 178, RULE_loadStmt);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1256);
                match(T_LOAD);
                setState(1258);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_CSV) {
                    {
                        setState(1257);
                        match(T_CSV);
                    }
                }

                setState(1260);
                match(T_INPATH);
                setState(1261);
                string();
                setState(1262);
                match(T_TO);
                setState(1264);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 152, _ctx)) {
                    case 1: {
                        setState(1263);
                        match(T_TABLE);
                    }
                        break;
                }
                setState(1266);
                table_name();
                setState(1268);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_OPEN_P) {
                    {
                        setState(1267);
                        load_mapping_columns();
                    }
                }

                setState(1271);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_FIELDS) {
                    {
                        setState(1270);
                        csv_file_options();
                    }
                }

                setState(1274);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_HEADER || _la == T_WITH || _la == T_WITHOUT) {
                    {
                        setState(1273);
                        csv_file_format();
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Load_mapping_columnsContext extends ParserRuleContext {
        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public Load_mapping_itemsContext load_mapping_items() {
            return getRuleContext(Load_mapping_itemsContext.class, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public Load_mapping_columnsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_load_mapping_columns;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitLoad_mapping_columns(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Load_mapping_columnsContext load_mapping_columns() throws RecognitionException {
        Load_mapping_columnsContext _localctx = new Load_mapping_columnsContext(_ctx, getState());
        enterRule(_localctx, 180, RULE_load_mapping_columns);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1276);
                match(T_OPEN_P);
                setState(1277);
                load_mapping_items();
                setState(1278);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Load_mapping_itemsContext extends ParserRuleContext {
        public List<Load_mapping_itemContext> load_mapping_item() {
            return getRuleContexts(Load_mapping_itemContext.class);
        }

        public Load_mapping_itemContext load_mapping_item(int i) {
            return getRuleContext(Load_mapping_itemContext.class, i);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public Load_mapping_itemsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_load_mapping_items;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitLoad_mapping_items(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Load_mapping_itemsContext load_mapping_items() throws RecognitionException {
        Load_mapping_itemsContext _localctx = new Load_mapping_itemsContext(_ctx, getState());
        enterRule(_localctx, 182, RULE_load_mapping_items);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1280);
                load_mapping_item();
                setState(1285);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(1281);
                            match(T_COMMA);
                            setState(1282);
                            load_mapping_item();
                        }
                    }
                    setState(1287);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Load_mapping_itemContext extends ParserRuleContext {
        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public Load_mapping_itemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_load_mapping_item;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitLoad_mapping_item(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Load_mapping_itemContext load_mapping_item() throws RecognitionException {
        Load_mapping_itemContext _localctx = new Load_mapping_itemContext(_ctx, getState());
        enterRule(_localctx, 184, RULE_load_mapping_item);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1288);
                ident();
                setState(1289);
                expr(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Csv_file_optionsContext extends ParserRuleContext {
        public TerminalNode T_FIELDS() {
            return getToken(CupidDBSqlParser.T_FIELDS, 0);
        }

        public TerminalNode T_DELIMITER() {
            return getToken(CupidDBSqlParser.T_DELIMITER, 0);
        }

        public List<StringContext> string() {
            return getRuleContexts(StringContext.class);
        }

        public StringContext string(int i) {
            return getRuleContext(StringContext.class, i);
        }

        public TerminalNode T_QUOTES() {
            return getToken(CupidDBSqlParser.T_QUOTES, 0);
        }

        public Csv_file_optionsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_csv_file_options;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCsv_file_options(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Csv_file_optionsContext csv_file_options() throws RecognitionException {
        Csv_file_optionsContext _localctx = new Csv_file_optionsContext(_ctx, getState());
        enterRule(_localctx, 186, RULE_csv_file_options);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1291);
                match(T_FIELDS);
                setState(1294);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_DELIMITER) {
                    {
                        setState(1292);
                        match(T_DELIMITER);
                        setState(1293);
                        string();
                    }
                }

                setState(1298);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_QUOTES) {
                    {
                        setState(1296);
                        match(T_QUOTES);
                        setState(1297);
                        string();
                    }
                }

            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Csv_file_formatContext extends ParserRuleContext {
        public TerminalNode T_HEADER() {
            return getToken(CupidDBSqlParser.T_HEADER, 0);
        }

        public TerminalNode T_WITH() {
            return getToken(CupidDBSqlParser.T_WITH, 0);
        }

        public TerminalNode T_WITHOUT() {
            return getToken(CupidDBSqlParser.T_WITHOUT, 0);
        }

        public Csv_file_formatContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_csv_file_format;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitCsv_file_format(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Csv_file_formatContext csv_file_format() throws RecognitionException {
        Csv_file_formatContext _localctx = new Csv_file_formatContext(_ctx, getState());
        enterRule(_localctx, 188, RULE_csv_file_format);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1301);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_WITH || _la == T_WITHOUT) {
                    {
                        setState(1300);
                        _la = _input.LA(1);
                        if (!(_la == T_WITH || _la == T_WITHOUT)) {
                            _errHandler.recoverInline(this);
                        } else {
                            if (_input.LA(1) == Token.EOF) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                    }
                }

                setState(1303);
                match(T_HEADER);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class RenameTableStmtContext extends ParserRuleContext {
        public TerminalNode T_RENAME() {
            return getToken(CupidDBSqlParser.T_RENAME, 0);
        }

        public TerminalNode T_TABLE() {
            return getToken(CupidDBSqlParser.T_TABLE, 0);
        }

        public Old_nameContext old_name() {
            return getRuleContext(Old_nameContext.class, 0);
        }

        public TerminalNode T_TO() {
            return getToken(CupidDBSqlParser.T_TO, 0);
        }

        public New_nameContext new_name() {
            return getRuleContext(New_nameContext.class, 0);
        }

        public RenameTableStmtContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_renameTableStmt;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitRenameTableStmt(this);
            else return visitor.visitChildren(this);
        }
    }

    public final RenameTableStmtContext renameTableStmt() throws RecognitionException {
        RenameTableStmtContext _localctx = new RenameTableStmtContext(_ctx, getState());
        enterRule(_localctx, 190, RULE_renameTableStmt);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1305);
                match(T_RENAME);
                setState(1306);
                match(T_TABLE);
                setState(1307);
                old_name();
                setState(1308);
                match(T_TO);
                setState(1309);
                new_name();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Old_nameContext extends ParserRuleContext {
        public QidentContext qident() {
            return getRuleContext(QidentContext.class, 0);
        }

        public Old_nameContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_old_name;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitOld_name(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Old_nameContext old_name() throws RecognitionException {
        Old_nameContext _localctx = new Old_nameContext(_ctx, getState());
        enterRule(_localctx, 192, RULE_old_name);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1311);
                qident();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class New_nameContext extends ParserRuleContext {
        public QidentContext qident() {
            return getRuleContext(QidentContext.class, 0);
        }

        public New_nameContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_new_name;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitNew_name(this);
            else return visitor.visitChildren(this);
        }
    }

    public final New_nameContext new_name() throws RecognitionException {
        New_nameContext _localctx = new New_nameContext(_ctx, getState());
        enterRule(_localctx, 194, RULE_new_name);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1313);
                qident();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class BoolExprContext extends ParserRuleContext {
        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public List<BoolExprContext> boolExpr() {
            return getRuleContexts(BoolExprContext.class);
        }

        public BoolExprContext boolExpr(int i) {
            return getRuleContext(BoolExprContext.class, i);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public TerminalNode T_NOT() {
            return getToken(CupidDBSqlParser.T_NOT, 0);
        }

        public BoolExprAtomContext boolExprAtom() {
            return getRuleContext(BoolExprAtomContext.class, 0);
        }

        public BoolExprLogicalOperatorContext boolExprLogicalOperator() {
            return getRuleContext(BoolExprLogicalOperatorContext.class, 0);
        }

        public BoolExprContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_boolExpr;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitBoolExpr(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BoolExprContext boolExpr() throws RecognitionException {
        return boolExpr(0);
    }

    private BoolExprContext boolExpr(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        BoolExprContext _localctx = new BoolExprContext(_ctx, _parentState);
        BoolExprContext _prevctx = _localctx;
        int _startState = 196;
        enterRecursionRule(_localctx, 196, RULE_boolExpr, _p);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(1324);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 161, _ctx)) {
                    case 1: {
                        setState(1317);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                        if (_la == T_NOT) {
                            {
                                setState(1316);
                                match(T_NOT);
                            }
                        }

                        setState(1319);
                        match(T_OPEN_P);
                        setState(1320);
                        boolExpr(0);
                        setState(1321);
                        match(T_CLOSE_P);
                    }
                        break;
                    case 2: {
                        setState(1323);
                        boolExprAtom();
                    }
                        break;
                }
                _ctx.stop = _input.LT(-1);
                setState(1332);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 162, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            {
                                _localctx = new BoolExprContext(_parentctx, _parentState);
                                pushNewRecursionContext(_localctx, _startState, RULE_boolExpr);
                                setState(1326);
                                if (!(precpred(_ctx, 2))) throw new FailedPredicateException(
                                    this,
                                    "precpred(_ctx, 2)"
                                );
                                setState(1327);
                                boolExprLogicalOperator();
                                setState(1328);
                                boolExpr(3);
                            }
                        }
                    }
                    setState(1334);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 162, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public static class BoolExprAtomContext extends ParserRuleContext {
        public BoolExprUnaryContext boolExprUnary() {
            return getRuleContext(BoolExprUnaryContext.class, 0);
        }

        public BoolExprBinaryContext boolExprBinary() {
            return getRuleContext(BoolExprBinaryContext.class, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public BoolExprAtomContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_boolExprAtom;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitBoolExprAtom(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BoolExprAtomContext boolExprAtom() throws RecognitionException {
        BoolExprAtomContext _localctx = new BoolExprAtomContext(_ctx, getState());
        enterRule(_localctx, 198, RULE_boolExprAtom);
        try {
            setState(1338);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 163, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(1335);
                    boolExprUnary();
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(1336);
                    boolExprBinary();
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(1337);
                    expr(0);
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class BoolExprUnaryContext extends ParserRuleContext {
        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public TerminalNode T_IS() {
            return getToken(CupidDBSqlParser.T_IS, 0);
        }

        public TerminalNode T_NULL() {
            return getToken(CupidDBSqlParser.T_NULL, 0);
        }

        public TerminalNode T_NOT() {
            return getToken(CupidDBSqlParser.T_NOT, 0);
        }

        public TerminalNode T_BETWEEN() {
            return getToken(CupidDBSqlParser.T_BETWEEN, 0);
        }

        public TerminalNode T_AND() {
            return getToken(CupidDBSqlParser.T_AND, 0);
        }

        public TerminalNode T_EXISTS() {
            return getToken(CupidDBSqlParser.T_EXISTS, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public SelectStmtContext selectStmt() {
            return getRuleContext(SelectStmtContext.class, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public BoolExprSingleInContext boolExprSingleIn() {
            return getRuleContext(BoolExprSingleInContext.class, 0);
        }

        public BoolExprMultiInContext boolExprMultiIn() {
            return getRuleContext(BoolExprMultiInContext.class, 0);
        }

        public BoolExprUnaryContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_boolExprUnary;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitBoolExprUnary(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BoolExprUnaryContext boolExprUnary() throws RecognitionException {
        BoolExprUnaryContext _localctx = new BoolExprUnaryContext(_ctx, getState());
        enterRule(_localctx, 200, RULE_boolExprUnary);
        int _la;
        try {
            setState(1363);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 166, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(1340);
                    expr(0);
                    setState(1341);
                    match(T_IS);
                    setState(1343);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_NOT) {
                        {
                            setState(1342);
                            match(T_NOT);
                        }
                    }

                    setState(1345);
                    match(T_NULL);
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(1347);
                    expr(0);
                    setState(1348);
                    match(T_BETWEEN);
                    setState(1349);
                    expr(0);
                    setState(1350);
                    match(T_AND);
                    setState(1351);
                    expr(0);
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(1354);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_NOT) {
                        {
                            setState(1353);
                            match(T_NOT);
                        }
                    }

                    setState(1356);
                    match(T_EXISTS);
                    setState(1357);
                    match(T_OPEN_P);
                    setState(1358);
                    selectStmt();
                    setState(1359);
                    match(T_CLOSE_P);
                }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4); {
                    setState(1361);
                    boolExprSingleIn();
                }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5); {
                    setState(1362);
                    boolExprMultiIn();
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class BoolExprSingleInContext extends ParserRuleContext {
        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public TerminalNode T_IN() {
            return getToken(CupidDBSqlParser.T_IN, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public SelectStmtContext selectStmt() {
            return getRuleContext(SelectStmtContext.class, 0);
        }

        public TerminalNode T_NOT() {
            return getToken(CupidDBSqlParser.T_NOT, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public BoolExprSingleInContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_boolExprSingleIn;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitBoolExprSingleIn(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BoolExprSingleInContext boolExprSingleIn() throws RecognitionException {
        BoolExprSingleInContext _localctx = new BoolExprSingleInContext(_ctx, getState());
        enterRule(_localctx, 202, RULE_boolExprSingleIn);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1365);
                expr(0);
                setState(1367);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_NOT) {
                    {
                        setState(1366);
                        match(T_NOT);
                    }
                }

                setState(1369);
                match(T_IN);
                setState(1370);
                match(T_OPEN_P);
                setState(1380);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 169, _ctx)) {
                    case 1: {
                        {
                            setState(1371);
                            expr(0);
                            setState(1376);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            while (_la == T_COMMA) {
                                {
                                    {
                                        setState(1372);
                                        match(T_COMMA);
                                        setState(1373);
                                        expr(0);
                                    }
                                }
                                setState(1378);
                                _errHandler.sync(this);
                                _la = _input.LA(1);
                            }
                        }
                    }
                        break;
                    case 2: {
                        setState(1379);
                        selectStmt();
                    }
                        break;
                }
                setState(1382);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class BoolExprMultiInContext extends ParserRuleContext {
        public List<TerminalNode> T_OPEN_P() {
            return getTokens(CupidDBSqlParser.T_OPEN_P);
        }

        public TerminalNode T_OPEN_P(int i) {
            return getToken(CupidDBSqlParser.T_OPEN_P, i);
        }

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public List<TerminalNode> T_CLOSE_P() {
            return getTokens(CupidDBSqlParser.T_CLOSE_P);
        }

        public TerminalNode T_CLOSE_P(int i) {
            return getToken(CupidDBSqlParser.T_CLOSE_P, i);
        }

        public TerminalNode T_IN() {
            return getToken(CupidDBSqlParser.T_IN, 0);
        }

        public SelectStmtContext selectStmt() {
            return getRuleContext(SelectStmtContext.class, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public TerminalNode T_NOT() {
            return getToken(CupidDBSqlParser.T_NOT, 0);
        }

        public BoolExprMultiInContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_boolExprMultiIn;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitBoolExprMultiIn(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BoolExprMultiInContext boolExprMultiIn() throws RecognitionException {
        BoolExprMultiInContext _localctx = new BoolExprMultiInContext(_ctx, getState());
        enterRule(_localctx, 204, RULE_boolExprMultiIn);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1384);
                match(T_OPEN_P);
                setState(1385);
                expr(0);
                setState(1390);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(1386);
                            match(T_COMMA);
                            setState(1387);
                            expr(0);
                        }
                    }
                    setState(1392);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(1393);
                match(T_CLOSE_P);
                setState(1395);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_NOT) {
                    {
                        setState(1394);
                        match(T_NOT);
                    }
                }

                setState(1397);
                match(T_IN);
                setState(1398);
                match(T_OPEN_P);
                setState(1399);
                selectStmt();
                setState(1400);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class BoolExprBinaryContext extends ParserRuleContext {
        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public BoolExprBinaryOperatorContext boolExprBinaryOperator() {
            return getRuleContext(BoolExprBinaryOperatorContext.class, 0);
        }

        public BoolExprBinaryContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_boolExprBinary;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitBoolExprBinary(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BoolExprBinaryContext boolExprBinary() throws RecognitionException {
        BoolExprBinaryContext _localctx = new BoolExprBinaryContext(_ctx, getState());
        enterRule(_localctx, 206, RULE_boolExprBinary);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1402);
                expr(0);
                setState(1403);
                boolExprBinaryOperator();
                setState(1404);
                expr(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class BoolExprLogicalOperatorContext extends ParserRuleContext {
        public TerminalNode T_AND() {
            return getToken(CupidDBSqlParser.T_AND, 0);
        }

        public TerminalNode T_OR() {
            return getToken(CupidDBSqlParser.T_OR, 0);
        }

        public BoolExprLogicalOperatorContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_boolExprLogicalOperator;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitBoolExprLogicalOperator(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BoolExprLogicalOperatorContext boolExprLogicalOperator()
        throws RecognitionException {
        BoolExprLogicalOperatorContext _localctx = new BoolExprLogicalOperatorContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 208, RULE_boolExprLogicalOperator);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1406);
                _la = _input.LA(1);
                if (!(_la == T_AND || _la == T_OR)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class BoolExprBinaryOperatorContext extends ParserRuleContext {
        public TerminalNode T_EQUAL() {
            return getToken(CupidDBSqlParser.T_EQUAL, 0);
        }

        public TerminalNode T_EQUAL2() {
            return getToken(CupidDBSqlParser.T_EQUAL2, 0);
        }

        public TerminalNode T_NOTEQUAL() {
            return getToken(CupidDBSqlParser.T_NOTEQUAL, 0);
        }

        public TerminalNode T_NOTEQUAL2() {
            return getToken(CupidDBSqlParser.T_NOTEQUAL2, 0);
        }

        public TerminalNode T_LESS() {
            return getToken(CupidDBSqlParser.T_LESS, 0);
        }

        public TerminalNode T_LESSEQUAL() {
            return getToken(CupidDBSqlParser.T_LESSEQUAL, 0);
        }

        public TerminalNode T_GREATER() {
            return getToken(CupidDBSqlParser.T_GREATER, 0);
        }

        public TerminalNode T_GREATEREQUAL() {
            return getToken(CupidDBSqlParser.T_GREATEREQUAL, 0);
        }

        public TerminalNode T_LIKE() {
            return getToken(CupidDBSqlParser.T_LIKE, 0);
        }

        public TerminalNode T_RLIKE() {
            return getToken(CupidDBSqlParser.T_RLIKE, 0);
        }

        public TerminalNode T_REGEXP() {
            return getToken(CupidDBSqlParser.T_REGEXP, 0);
        }

        public TerminalNode T_NOT() {
            return getToken(CupidDBSqlParser.T_NOT, 0);
        }

        public BoolExprBinaryOperatorContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_boolExprBinaryOperator;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitBoolExprBinaryOperator(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BoolExprBinaryOperatorContext boolExprBinaryOperator()
        throws RecognitionException {
        BoolExprBinaryOperatorContext _localctx = new BoolExprBinaryOperatorContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 210, RULE_boolExprBinaryOperator);
        int _la;
        try {
            setState(1420);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_EQUAL:
                    enterOuterAlt(_localctx, 1); {
                    setState(1408);
                    match(T_EQUAL);
                }
                    break;
                case T_EQUAL2:
                    enterOuterAlt(_localctx, 2); {
                    setState(1409);
                    match(T_EQUAL2);
                }
                    break;
                case T_NOTEQUAL:
                    enterOuterAlt(_localctx, 3); {
                    setState(1410);
                    match(T_NOTEQUAL);
                }
                    break;
                case T_NOTEQUAL2:
                    enterOuterAlt(_localctx, 4); {
                    setState(1411);
                    match(T_NOTEQUAL2);
                }
                    break;
                case T_LESS:
                    enterOuterAlt(_localctx, 5); {
                    setState(1412);
                    match(T_LESS);
                }
                    break;
                case T_LESSEQUAL:
                    enterOuterAlt(_localctx, 6); {
                    setState(1413);
                    match(T_LESSEQUAL);
                }
                    break;
                case T_GREATER:
                    enterOuterAlt(_localctx, 7); {
                    setState(1414);
                    match(T_GREATER);
                }
                    break;
                case T_GREATEREQUAL:
                    enterOuterAlt(_localctx, 8); {
                    setState(1415);
                    match(T_GREATEREQUAL);
                }
                    break;
                case T_LIKE:
                case T_NOT:
                case T_REGEXP:
                case T_RLIKE:
                    enterOuterAlt(_localctx, 9); {
                    setState(1417);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_NOT) {
                        {
                            setState(1416);
                            match(T_NOT);
                        }
                    }

                    setState(1419);
                    _la = _input.LA(1);
                    if (!(_la == T_LIKE || _la == T_REGEXP || _la == T_RLIKE)) {
                        _errHandler.recoverInline(this);
                    } else {
                        if (_input.LA(1) == Token.EOF) matchedEOF = true;
                        _errHandler.reportMatch(this);
                        consume();
                    }
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprContext extends ParserRuleContext {
        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public SelectStmtContext selectStmt() {
            return getRuleContext(SelectStmtContext.class, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public ExprIntervalContext exprInterval() {
            return getRuleContext(ExprIntervalContext.class, 0);
        }

        public ExprConcatContext exprConcat() {
            return getRuleContext(ExprConcatContext.class, 0);
        }

        public ExprCaseContext exprCase() {
            return getRuleContext(ExprCaseContext.class, 0);
        }

        public ExprCursorAttributeContext exprCursorAttribute() {
            return getRuleContext(ExprCursorAttributeContext.class, 0);
        }

        public ExprAggWindowFuncContext exprAggWindowFunc() {
            return getRuleContext(ExprAggWindowFuncContext.class, 0);
        }

        public ExprSpecFuncContext exprSpecFunc() {
            return getRuleContext(ExprSpecFuncContext.class, 0);
        }

        public ExprFuncContext exprFunc() {
            return getRuleContext(ExprFuncContext.class, 0);
        }

        public ExprAtomContext exprAtom() {
            return getRuleContext(ExprAtomContext.class, 0);
        }

        public TerminalNode T_MUL() {
            return getToken(CupidDBSqlParser.T_MUL, 0);
        }

        public TerminalNode T_DIV() {
            return getToken(CupidDBSqlParser.T_DIV, 0);
        }

        public TerminalNode T_ADD() {
            return getToken(CupidDBSqlParser.T_ADD, 0);
        }

        public TerminalNode T_SUB() {
            return getToken(CupidDBSqlParser.T_SUB, 0);
        }

        public IntervalItemContext intervalItem() {
            return getRuleContext(IntervalItemContext.class, 0);
        }

        public ExprContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expr;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExpr(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprContext expr() throws RecognitionException {
        return expr(0);
    }

    private ExprContext expr(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ExprContext _localctx = new ExprContext(_ctx, _parentState);
        ExprContext _prevctx = _localctx;
        int _startState = 212;
        enterRecursionRule(_localctx, 212, RULE_expr, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(1439);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 174, _ctx)) {
                    case 1: {
                        setState(1423);
                        match(T_OPEN_P);
                        setState(1424);
                        selectStmt();
                        setState(1425);
                        match(T_CLOSE_P);
                    }
                        break;
                    case 2: {
                        setState(1427);
                        match(T_OPEN_P);
                        setState(1428);
                        expr(0);
                        setState(1429);
                        match(T_CLOSE_P);
                    }
                        break;
                    case 3: {
                        setState(1431);
                        exprInterval();
                    }
                        break;
                    case 4: {
                        setState(1432);
                        exprConcat();
                    }
                        break;
                    case 5: {
                        setState(1433);
                        exprCase();
                    }
                        break;
                    case 6: {
                        setState(1434);
                        exprCursorAttribute();
                    }
                        break;
                    case 7: {
                        setState(1435);
                        exprAggWindowFunc();
                    }
                        break;
                    case 8: {
                        setState(1436);
                        exprSpecFunc();
                    }
                        break;
                    case 9: {
                        setState(1437);
                        exprFunc();
                    }
                        break;
                    case 10: {
                        setState(1438);
                        exprAtom();
                    }
                        break;
                }
                _ctx.stop = _input.LT(-1);
                setState(1457);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 176, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            setState(1455);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 175, _ctx)) {
                                case 1: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(1441);
                                    if (!(precpred(_ctx, 14))) throw new FailedPredicateException(
                                        this,
                                        "precpred(_ctx, 14)"
                                    );
                                    setState(1442);
                                    match(T_MUL);
                                    setState(1443);
                                    expr(15);
                                }
                                    break;
                                case 2: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(1444);
                                    if (!(precpred(_ctx, 13))) throw new FailedPredicateException(
                                        this,
                                        "precpred(_ctx, 13)"
                                    );
                                    setState(1445);
                                    match(T_DIV);
                                    setState(1446);
                                    expr(14);
                                }
                                    break;
                                case 3: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(1447);
                                    if (!(precpred(_ctx, 12))) throw new FailedPredicateException(
                                        this,
                                        "precpred(_ctx, 12)"
                                    );
                                    setState(1448);
                                    match(T_ADD);
                                    setState(1449);
                                    expr(13);
                                }
                                    break;
                                case 4: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(1450);
                                    if (!(precpred(_ctx, 11))) throw new FailedPredicateException(
                                        this,
                                        "precpred(_ctx, 11)"
                                    );
                                    setState(1451);
                                    match(T_SUB);
                                    setState(1452);
                                    expr(12);
                                }
                                    break;
                                case 5: {
                                    _localctx = new ExprContext(_parentctx, _parentState);
                                    pushNewRecursionContext(_localctx, _startState, RULE_expr);
                                    setState(1453);
                                    if (!(precpred(_ctx, 15))) throw new FailedPredicateException(
                                        this,
                                        "precpred(_ctx, 15)"
                                    );
                                    setState(1454);
                                    intervalItem();
                                }
                                    break;
                            }
                        }
                    }
                    setState(1459);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 176, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public static class ExprAtomContext extends ParserRuleContext {
        public DateLiteralContext dateLiteral() {
            return getRuleContext(DateLiteralContext.class, 0);
        }

        public TimestampLiteralContext timestampLiteral() {
            return getRuleContext(TimestampLiteralContext.class, 0);
        }

        public BoolLiteralContext boolLiteral() {
            return getRuleContext(BoolLiteralContext.class, 0);
        }

        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public StringContext string() {
            return getRuleContext(StringContext.class, 0);
        }

        public DecNumberContext decNumber() {
            return getRuleContext(DecNumberContext.class, 0);
        }

        public IntNumberContext intNumber() {
            return getRuleContext(IntNumberContext.class, 0);
        }

        public NullConstContext nullConst() {
            return getRuleContext(NullConstContext.class, 0);
        }

        public ExprAtomContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprAtom;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprAtom(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprAtomContext exprAtom() throws RecognitionException {
        ExprAtomContext _localctx = new ExprAtomContext(_ctx, getState());
        enterRule(_localctx, 214, RULE_exprAtom);
        try {
            setState(1468);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 177, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(1460);
                    dateLiteral();
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(1461);
                    timestampLiteral();
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(1462);
                    boolLiteral();
                }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4); {
                    setState(1463);
                    ident();
                }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5); {
                    setState(1464);
                    string();
                }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6); {
                    setState(1465);
                    decNumber();
                }
                    break;
                case 7:
                    enterOuterAlt(_localctx, 7); {
                    setState(1466);
                    intNumber();
                }
                    break;
                case 8:
                    enterOuterAlt(_localctx, 8); {
                    setState(1467);
                    nullConst();
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprIntervalContext extends ParserRuleContext {
        public TerminalNode T_INTERVAL() {
            return getToken(CupidDBSqlParser.T_INTERVAL, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public IntervalItemContext intervalItem() {
            return getRuleContext(IntervalItemContext.class, 0);
        }

        public ExprIntervalContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprInterval;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprInterval(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprIntervalContext exprInterval() throws RecognitionException {
        ExprIntervalContext _localctx = new ExprIntervalContext(_ctx, getState());
        enterRule(_localctx, 216, RULE_exprInterval);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1470);
                match(T_INTERVAL);
                setState(1471);
                expr(0);
                setState(1472);
                intervalItem();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class IntervalItemContext extends ParserRuleContext {
        public TerminalNode T_DAY() {
            return getToken(CupidDBSqlParser.T_DAY, 0);
        }

        public TerminalNode T_DAYS() {
            return getToken(CupidDBSqlParser.T_DAYS, 0);
        }

        public TerminalNode T_MICROSECOND() {
            return getToken(CupidDBSqlParser.T_MICROSECOND, 0);
        }

        public TerminalNode T_MICROSECONDS() {
            return getToken(CupidDBSqlParser.T_MICROSECONDS, 0);
        }

        public TerminalNode T_SECOND() {
            return getToken(CupidDBSqlParser.T_SECOND, 0);
        }

        public TerminalNode T_SECONDS() {
            return getToken(CupidDBSqlParser.T_SECONDS, 0);
        }

        public IntervalItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_intervalItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitIntervalItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IntervalItemContext intervalItem() throws RecognitionException {
        IntervalItemContext _localctx = new IntervalItemContext(_ctx, getState());
        enterRule(_localctx, 218, RULE_intervalItem);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1474);
                _la = _input.LA(1);
                if (!(_la == T_DAY
                    || _la == T_DAYS
                    || _la == T_MICROSECOND
                    || _la == T_MICROSECONDS
                    || _la == T_SECOND
                    || _la == T_SECONDS)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprConcatContext extends ParserRuleContext {
        public List<ExprConcatItemContext> exprConcatItem() {
            return getRuleContexts(ExprConcatItemContext.class);
        }

        public ExprConcatItemContext exprConcatItem(int i) {
            return getRuleContext(ExprConcatItemContext.class, i);
        }

        public List<TerminalNode> T_PIPE() {
            return getTokens(CupidDBSqlParser.T_PIPE);
        }

        public TerminalNode T_PIPE(int i) {
            return getToken(CupidDBSqlParser.T_PIPE, i);
        }

        public List<TerminalNode> T_CONCAT() {
            return getTokens(CupidDBSqlParser.T_CONCAT);
        }

        public TerminalNode T_CONCAT(int i) {
            return getToken(CupidDBSqlParser.T_CONCAT, i);
        }

        public ExprConcatContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprConcat;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprConcat(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprConcatContext exprConcat() throws RecognitionException {
        ExprConcatContext _localctx = new ExprConcatContext(_ctx, getState());
        enterRule(_localctx, 220, RULE_exprConcat);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(1476);
                exprConcatItem();
                setState(1477);
                _la = _input.LA(1);
                if (!(_la == T_CONCAT || _la == T_PIPE)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(1478);
                exprConcatItem();
                setState(1483);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 178, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(1479);
                                _la = _input.LA(1);
                                if (!(_la == T_CONCAT || _la == T_PIPE)) {
                                    _errHandler.recoverInline(this);
                                } else {
                                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                                    _errHandler.reportMatch(this);
                                    consume();
                                }
                                setState(1480);
                                exprConcatItem();
                            }
                        }
                    }
                    setState(1485);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 178, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprConcatItemContext extends ParserRuleContext {
        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public ExprCaseContext exprCase() {
            return getRuleContext(ExprCaseContext.class, 0);
        }

        public ExprAggWindowFuncContext exprAggWindowFunc() {
            return getRuleContext(ExprAggWindowFuncContext.class, 0);
        }

        public ExprSpecFuncContext exprSpecFunc() {
            return getRuleContext(ExprSpecFuncContext.class, 0);
        }

        public ExprFuncContext exprFunc() {
            return getRuleContext(ExprFuncContext.class, 0);
        }

        public ExprAtomContext exprAtom() {
            return getRuleContext(ExprAtomContext.class, 0);
        }

        public ExprConcatItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprConcatItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprConcatItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprConcatItemContext exprConcatItem() throws RecognitionException {
        ExprConcatItemContext _localctx = new ExprConcatItemContext(_ctx, getState());
        enterRule(_localctx, 222, RULE_exprConcatItem);
        try {
            setState(1495);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 179, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(1486);
                    match(T_OPEN_P);
                    setState(1487);
                    expr(0);
                    setState(1488);
                    match(T_CLOSE_P);
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(1490);
                    exprCase();
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(1491);
                    exprAggWindowFunc();
                }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4); {
                    setState(1492);
                    exprSpecFunc();
                }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5); {
                    setState(1493);
                    exprFunc();
                }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6); {
                    setState(1494);
                    exprAtom();
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprCaseContext extends ParserRuleContext {
        public ExprCaseSimpleContext exprCaseSimple() {
            return getRuleContext(ExprCaseSimpleContext.class, 0);
        }

        public ExprCaseSearchedContext exprCaseSearched() {
            return getRuleContext(ExprCaseSearchedContext.class, 0);
        }

        public ExprCaseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprCase;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprCase(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprCaseContext exprCase() throws RecognitionException {
        ExprCaseContext _localctx = new ExprCaseContext(_ctx, getState());
        enterRule(_localctx, 224, RULE_exprCase);
        try {
            setState(1499);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 180, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(1497);
                    exprCaseSimple();
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(1498);
                    exprCaseSearched();
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprCaseSimpleContext extends ParserRuleContext {
        public TerminalNode T_CASE() {
            return getToken(CupidDBSqlParser.T_CASE, 0);
        }

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public TerminalNode T_END() {
            return getToken(CupidDBSqlParser.T_END, 0);
        }

        public List<TerminalNode> T_WHEN() {
            return getTokens(CupidDBSqlParser.T_WHEN);
        }

        public TerminalNode T_WHEN(int i) {
            return getToken(CupidDBSqlParser.T_WHEN, i);
        }

        public List<TerminalNode> T_THEN() {
            return getTokens(CupidDBSqlParser.T_THEN);
        }

        public TerminalNode T_THEN(int i) {
            return getToken(CupidDBSqlParser.T_THEN, i);
        }

        public TerminalNode T_ELSE() {
            return getToken(CupidDBSqlParser.T_ELSE, 0);
        }

        public ExprCaseSimpleContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprCaseSimple;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprCaseSimple(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprCaseSimpleContext exprCaseSimple() throws RecognitionException {
        ExprCaseSimpleContext _localctx = new ExprCaseSimpleContext(_ctx, getState());
        enterRule(_localctx, 226, RULE_exprCaseSimple);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1501);
                match(T_CASE);
                setState(1502);
                expr(0);
                setState(1508);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(1503);
                            match(T_WHEN);
                            setState(1504);
                            expr(0);
                            setState(1505);
                            match(T_THEN);
                            setState(1506);
                            expr(0);
                        }
                    }
                    setState(1510);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while (_la == T_WHEN);
                setState(1514);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_ELSE) {
                    {
                        setState(1512);
                        match(T_ELSE);
                        setState(1513);
                        expr(0);
                    }
                }

                setState(1516);
                match(T_END);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprCaseSearchedContext extends ParserRuleContext {
        public TerminalNode T_CASE() {
            return getToken(CupidDBSqlParser.T_CASE, 0);
        }

        public TerminalNode T_END() {
            return getToken(CupidDBSqlParser.T_END, 0);
        }

        public List<ExprCaseItemContext> exprCaseItem() {
            return getRuleContexts(ExprCaseItemContext.class);
        }

        public ExprCaseItemContext exprCaseItem(int i) {
            return getRuleContext(ExprCaseItemContext.class, i);
        }

        public TerminalNode T_ELSE() {
            return getToken(CupidDBSqlParser.T_ELSE, 0);
        }

        public BoolExprContext boolExpr() {
            return getRuleContext(BoolExprContext.class, 0);
        }

        public ExprCaseSearchedContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprCaseSearched;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprCaseSearched(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprCaseSearchedContext exprCaseSearched() throws RecognitionException {
        ExprCaseSearchedContext _localctx = new ExprCaseSearchedContext(_ctx, getState());
        enterRule(_localctx, 228, RULE_exprCaseSearched);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1518);
                match(T_CASE);
                setState(1520);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(1519);
                            exprCaseItem();
                        }
                    }
                    setState(1522);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                } while (_la == T_WHEN);
                setState(1526);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_ELSE) {
                    {
                        setState(1524);
                        match(T_ELSE);
                        setState(1525);
                        boolExpr(0);
                    }
                }

                setState(1528);
                match(T_END);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprCaseItemContext extends ParserRuleContext {
        public BoolExprContext whenExpr;
        public BoolExprContext thenExpr;

        public TerminalNode T_WHEN() {
            return getToken(CupidDBSqlParser.T_WHEN, 0);
        }

        public TerminalNode T_THEN() {
            return getToken(CupidDBSqlParser.T_THEN, 0);
        }

        public List<BoolExprContext> boolExpr() {
            return getRuleContexts(BoolExprContext.class);
        }

        public BoolExprContext boolExpr(int i) {
            return getRuleContext(BoolExprContext.class, i);
        }

        public ExprCaseItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprCaseItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprCaseItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprCaseItemContext exprCaseItem() throws RecognitionException {
        ExprCaseItemContext _localctx = new ExprCaseItemContext(_ctx, getState());
        enterRule(_localctx, 230, RULE_exprCaseItem);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1530);
                match(T_WHEN);
                setState(1531);
                ((ExprCaseItemContext) _localctx).whenExpr = boolExpr(0);
                setState(1532);
                match(T_THEN);
                setState(1533);
                ((ExprCaseItemContext) _localctx).thenExpr = boolExpr(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprCursorAttributeContext extends ParserRuleContext {
        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_ISOPEN() {
            return getToken(CupidDBSqlParser.T_ISOPEN, 0);
        }

        public TerminalNode T_FOUND() {
            return getToken(CupidDBSqlParser.T_FOUND, 0);
        }

        public TerminalNode T_NOTFOUND() {
            return getToken(CupidDBSqlParser.T_NOTFOUND, 0);
        }

        public ExprCursorAttributeContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprCursorAttribute;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprCursorAttribute(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprCursorAttributeContext exprCursorAttribute() throws RecognitionException {
        ExprCursorAttributeContext _localctx = new ExprCursorAttributeContext(_ctx, getState());
        enterRule(_localctx, 232, RULE_exprCursorAttribute);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1535);
                ident();
                setState(1536);
                match(T__0);
                setState(1537);
                _la = _input.LA(1);
                if (!(_la == T_FOUND || _la == T_ISOPEN || _la == T_NOTFOUND)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprAggWindowFuncContext extends ParserRuleContext {
        public TerminalNode T_AVG() {
            return getToken(CupidDBSqlParser.T_AVG, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public ExprFuncAllDistinctContext exprFuncAllDistinct() {
            return getRuleContext(ExprFuncAllDistinctContext.class, 0);
        }

        public ExprFuncOverClauseContext exprFuncOverClause() {
            return getRuleContext(ExprFuncOverClauseContext.class, 0);
        }

        public TerminalNode T_COUNT() {
            return getToken(CupidDBSqlParser.T_COUNT, 0);
        }

        public TerminalNode T_MUL() {
            return getToken(CupidDBSqlParser.T_MUL, 0);
        }

        public TerminalNode T_COUNT_BIG() {
            return getToken(CupidDBSqlParser.T_COUNT_BIG, 0);
        }

        public TerminalNode T_CUME_DIST() {
            return getToken(CupidDBSqlParser.T_CUME_DIST, 0);
        }

        public TerminalNode T_DENSE_RANK() {
            return getToken(CupidDBSqlParser.T_DENSE_RANK, 0);
        }

        public TerminalNode T_FIRST_VALUE() {
            return getToken(CupidDBSqlParser.T_FIRST_VALUE, 0);
        }

        public TerminalNode T_LAG() {
            return getToken(CupidDBSqlParser.T_LAG, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public TerminalNode T_LAST_VALUE() {
            return getToken(CupidDBSqlParser.T_LAST_VALUE, 0);
        }

        public TerminalNode T_LEAD() {
            return getToken(CupidDBSqlParser.T_LEAD, 0);
        }

        public TerminalNode T_MAX() {
            return getToken(CupidDBSqlParser.T_MAX, 0);
        }

        public TerminalNode T_MIN() {
            return getToken(CupidDBSqlParser.T_MIN, 0);
        }

        public TerminalNode T_RANK() {
            return getToken(CupidDBSqlParser.T_RANK, 0);
        }

        public TerminalNode T_ROW_NUMBER() {
            return getToken(CupidDBSqlParser.T_ROW_NUMBER, 0);
        }

        public TerminalNode T_STDEV() {
            return getToken(CupidDBSqlParser.T_STDEV, 0);
        }

        public TerminalNode T_SUM() {
            return getToken(CupidDBSqlParser.T_SUM, 0);
        }

        public TerminalNode T_VAR() {
            return getToken(CupidDBSqlParser.T_VAR, 0);
        }

        public TerminalNode T_VARIANCE() {
            return getToken(CupidDBSqlParser.T_VARIANCE, 0);
        }

        public ExprAggWindowFuncContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprAggWindowFunc;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprAggWindowFunc(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprAggWindowFuncContext exprAggWindowFunc() throws RecognitionException {
        ExprAggWindowFuncContext _localctx = new ExprAggWindowFuncContext(_ctx, getState());
        enterRule(_localctx, 234, RULE_exprAggWindowFunc);
        int _la;
        try {
            setState(1691);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case T_AVG:
                    enterOuterAlt(_localctx, 1); {
                    setState(1539);
                    match(T_AVG);
                    setState(1540);
                    match(T_OPEN_P);
                    setState(1542);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 185, _ctx)) {
                        case 1: {
                            setState(1541);
                            exprFuncAllDistinct();
                        }
                            break;
                    }
                    setState(1544);
                    expr(0);
                    setState(1545);
                    match(T_CLOSE_P);
                    setState(1547);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 186, _ctx)) {
                        case 1: {
                            setState(1546);
                            exprFuncOverClause();
                        }
                            break;
                    }
                }
                    break;
                case T_COUNT:
                    enterOuterAlt(_localctx, 2); {
                    setState(1549);
                    match(T_COUNT);
                    setState(1550);
                    match(T_OPEN_P);
                    setState(1556);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case T_ACTION:
                        case T_ADD2:
                        case T_ALL:
                        case T_ALLOCATE:
                        case T_ALTER:
                        case T_AND:
                        case T_ANSI_NULLS:
                        case T_ANSI_PADDING:
                        case T_AS:
                        case T_ASC:
                        case T_ASSOCIATE:
                        case T_AT:
                        case T_AVG:
                        case T_BATCHSIZE:
                        case T_BEGIN:
                        case T_BETWEEN:
                        case T_BIGINT:
                        case T_BINARY_DOUBLE:
                        case T_BINARY_FLOAT:
                        case T_BIT:
                        case T_BODY:
                        case T_BREAK:
                        case T_BY:
                        case T_BYTE:
                        case T_CALL:
                        case T_CALLER:
                        case T_CASCADE:
                        case T_CASE:
                        case T_CASESPECIFIC:
                        case T_CAST:
                        case T_CHAR:
                        case T_CHARACTER:
                        case T_CHARSET:
                        case T_CLIENT:
                        case T_CLOSE:
                        case T_CLUSTERED:
                        case T_CMP:
                        case T_COLLECT:
                        case T_COLLECTION:
                        case T_COLUMN:
                        case T_COMMENT:
                        case T_CONSTANT:
                        case T_COMMIT:
                        case T_COMPRESS:
                        case T_CONCAT:
                        case T_CONDITION:
                        case T_CONSTRAINT:
                        case T_CONTINUE:
                        case T_COPY:
                        case T_COUNT:
                        case T_COUNT_BIG:
                        case T_CREATION:
                        case T_CREATOR:
                        case T_CS:
                        case T_CURRENT:
                        case T_CURRENT_SCHEMA:
                        case T_CURSOR:
                        case T_DATABASE:
                        case T_DATA:
                        case T_DATE:
                        case T_DATETIME:
                        case T_DAY:
                        case T_DAYS:
                        case T_DEC:
                        case T_DECIMAL:
                        case T_DECLARE:
                        case T_DEFAULT:
                        case T_DEFERRED:
                        case T_DEFINED:
                        case T_DEFINER:
                        case T_DEFINITION:
                        case T_DELETE:
                        case T_DELIMITED:
                        case T_DELIMITER:
                        case T_DESC:
                        case T_DESCRIBE:
                        case T_DIAGNOSTICS:
                        case T_DIR:
                        case T_DIRECTORY:
                        case T_DISTINCT:
                        case T_DISTRIBUTE:
                        case T_DO:
                        case T_DOUBLE:
                        case T_DOWNLOAD:
                        case T_DROP:
                        case T_DYNAMIC:
                        case T_ENABLE:
                        case T_ENGINE:
                        case T_ESCAPED:
                        case T_EXCEPT:
                        case T_EXEC:
                        case T_EXECUTE:
                        case T_EXCEPTION:
                        case T_EXCLUSIVE:
                        case T_EXISTS:
                        case T_EXIT:
                        case T_FALLBACK:
                        case T_FALSE:
                        case T_FETCH:
                        case T_FIELDS:
                        case T_FILE:
                        case T_FILES:
                        case T_FLOAT:
                        case T_FOR:
                        case T_FOREIGN:
                        case T_FORMAT:
                        case T_FOUND:
                        case T_FROM:
                        case T_FULL:
                        case T_FUNCTION:
                        case T_GET:
                        case T_GLOBAL:
                        case T_GO:
                        case T_GRANT:
                        case T_GROUP:
                        case T_HANDLER:
                        case T_HASH:
                        case T_HAVING:
                        case T_HOST:
                        case T_IDENTITY:
                        case T_IF:
                        case T_IGNORE:
                        case T_IMMEDIATE:
                        case T_IN:
                        case T_INCLUDE:
                        case T_INDEX:
                        case T_INITRANS:
                        case T_INNER:
                        case T_INOUT:
                        case T_INSERT:
                        case T_INT:
                        case T_INT2:
                        case T_INT4:
                        case T_INT8:
                        case T_INTEGER:
                        case T_INTERSECT:
                        case T_INTERVAL:
                        case T_INTO:
                        case T_INVOKER:
                        case T_IS:
                        case T_ISOPEN:
                        case T_ITEMS:
                        case T_JOIN:
                        case T_KEEP:
                        case T_KEY:
                        case T_KEYS:
                        case T_LANGUAGE:
                        case T_LEAVE:
                        case T_LEFT:
                        case T_LIKE:
                        case T_LIMIT:
                        case T_LINES:
                        case T_LOCAL:
                        case T_LOCATION:
                        case T_LOCATOR:
                        case T_LOCATORS:
                        case T_LOCKS:
                        case T_LOG:
                        case T_LOGGED:
                        case T_LOGGING:
                        case T_LOOP:
                        case T_MAP:
                        case T_MATCHED:
                        case T_MAX:
                        case T_MAXTRANS:
                        case T_MERGE:
                        case T_MESSAGE_TEXT:
                        case T_MICROSECOND:
                        case T_MICROSECONDS:
                        case T_MIN:
                        case T_MULTISET:
                        case T_NCHAR:
                        case T_NEW:
                        case T_NVARCHAR:
                        case T_NO:
                        case T_NOCOUNT:
                        case T_NOCOMPRESS:
                        case T_NOLOGGING:
                        case T_NONE:
                        case T_NOT:
                        case T_NOTFOUND:
                        case T_NULL:
                        case T_NUMERIC:
                        case T_NUMBER:
                        case T_OBJECT:
                        case T_OFF:
                        case T_ON:
                        case T_ONLY:
                        case T_OPEN:
                        case T_OR:
                        case T_ORDER:
                        case T_OUT:
                        case T_OUTER:
                        case T_OVER:
                        case T_OVERWRITE:
                        case T_OWNER:
                        case T_PACKAGE:
                        case T_PARTITION:
                        case T_PCTFREE:
                        case T_PCTUSED:
                        case T_PRECISION:
                        case T_PRESERVE:
                        case T_PRIMARY:
                        case T_PRINT:
                        case T_PROC:
                        case T_PROCEDURE:
                        case T_QUALIFY:
                        case T_QUERY_BAND:
                        case T_QUIT:
                        case T_QUOTED_IDENTIFIER:
                        case T_QUOTES:
                        case T_RAISE:
                        case T_REAL:
                        case T_REFERENCES:
                        case T_REGEXP:
                        case T_REPLACE:
                        case T_RESIGNAL:
                        case T_RESTRICT:
                        case T_RESULT:
                        case T_RESULT_SET_LOCATOR:
                        case T_RETURN:
                        case T_RETURNS:
                        case T_REVERSE:
                        case T_RIGHT:
                        case T_RLIKE:
                        case T_ROLE:
                        case T_ROLLBACK:
                        case T_ROW:
                        case T_ROWS:
                        case T_ROW_COUNT:
                        case T_RR:
                        case T_RS:
                        case T_PWD:
                        case T_TRIM:
                        case T_SCHEMA:
                        case T_SECOND:
                        case T_SECONDS:
                        case T_SECURITY:
                        case T_SEGMENT:
                        case T_SEL:
                        case T_SELECT:
                        case T_SET:
                        case T_SESSION:
                        case T_SESSIONS:
                        case T_SETS:
                        case T_SIGNAL:
                        case T_SIMPLE_DOUBLE:
                        case T_SIMPLE_FLOAT:
                        case T_SMALLDATETIME:
                        case T_SMALLINT:
                        case T_SQL:
                        case T_SQLEXCEPTION:
                        case T_SQLINSERT:
                        case T_SQLSTATE:
                        case T_SQLWARNING:
                        case T_STATS:
                        case T_STATISTICS:
                        case T_STEP:
                        case T_STORAGE:
                        case T_STORE:
                        case T_STORED:
                        case T_STREAM:
                        case T_STRING:
                        case T_SUBDIR:
                        case T_SUBSTRING:
                        case T_SUM:
                        case T_SUMMARY:
                        case T_SYS_REFCURSOR:
                        case T_TABLE:
                        case T_TABLESPACE:
                        case T_TEMPORARY:
                        case T_TERMINATED:
                        case T_TEXTIMAGE_ON:
                        case T_THEN:
                        case T_TIMESTAMP:
                        case T_TITLE:
                        case T_TO:
                        case T_TOP:
                        case T_TRANSACTION:
                        case T_TRUE:
                        case T_TRUNCATE:
                        case T_UNIQUE:
                        case T_UPDATE:
                        case T_UR:
                        case T_USE:
                        case T_USING:
                        case T_VALUE:
                        case T_VALUES:
                        case T_VAR:
                        case T_VARCHAR:
                        case T_VARCHAR2:
                        case T_VARYING:
                        case T_VOLATILE:
                        case T_WHILE:
                        case T_WITH:
                        case T_WITHOUT:
                        case T_WORK:
                        case T_XACT_ABORT:
                        case T_XML:
                        case T_YES:
                        case T_ACTIVITY_COUNT:
                        case T_CUME_DIST:
                        case T_CURRENT_DATE:
                        case T_CURRENT_TIME:
                        case T_PI:
                        case T_CURRENT_TIMESTAMP:
                        case T_CURRENT_USER:
                        case T_DENSE_RANK:
                        case T_FIRST_VALUE:
                        case T_LAG:
                        case T_LAST_VALUE:
                        case T_LEAD:
                        case T_MAX_PART_STRING:
                        case T_MIN_PART_STRING:
                        case T_MAX_PART_INT:
                        case T_MIN_PART_INT:
                        case T_MAX_PART_DATE:
                        case T_MIN_PART_DATE:
                        case T_PART_COUNT:
                        case T_PART_LOC:
                        case T_RANK:
                        case T_ROW_NUMBER:
                        case T_STDEV:
                        case T_SYSDATE:
                        case T_VARIANCE:
                        case T_USER:
                        case T_ADD:
                        case T_OPEN_P:
                        case T_SUB:
                        case L_ID:
                        case L_S_STRING:
                        case L_D_STRING:
                        case L_INT:
                        case L_DEC: {
                            {
                                setState(1552);
                                _errHandler.sync(this);
                                switch (getInterpreter().adaptivePredict(_input, 187, _ctx)) {
                                    case 1: {
                                        setState(1551);
                                        exprFuncAllDistinct();
                                    }
                                        break;
                                }
                                setState(1554);
                                expr(0);
                            }
                        }
                            break;
                        case T_MUL: {
                            setState(1555);
                            match(T_MUL);
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    setState(1558);
                    match(T_CLOSE_P);
                    setState(1560);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 189, _ctx)) {
                        case 1: {
                            setState(1559);
                            exprFuncOverClause();
                        }
                            break;
                    }
                }
                    break;
                case T_COUNT_BIG:
                    enterOuterAlt(_localctx, 3); {
                    setState(1562);
                    match(T_COUNT_BIG);
                    setState(1563);
                    match(T_OPEN_P);
                    setState(1569);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case T_ACTION:
                        case T_ADD2:
                        case T_ALL:
                        case T_ALLOCATE:
                        case T_ALTER:
                        case T_AND:
                        case T_ANSI_NULLS:
                        case T_ANSI_PADDING:
                        case T_AS:
                        case T_ASC:
                        case T_ASSOCIATE:
                        case T_AT:
                        case T_AVG:
                        case T_BATCHSIZE:
                        case T_BEGIN:
                        case T_BETWEEN:
                        case T_BIGINT:
                        case T_BINARY_DOUBLE:
                        case T_BINARY_FLOAT:
                        case T_BIT:
                        case T_BODY:
                        case T_BREAK:
                        case T_BY:
                        case T_BYTE:
                        case T_CALL:
                        case T_CALLER:
                        case T_CASCADE:
                        case T_CASE:
                        case T_CASESPECIFIC:
                        case T_CAST:
                        case T_CHAR:
                        case T_CHARACTER:
                        case T_CHARSET:
                        case T_CLIENT:
                        case T_CLOSE:
                        case T_CLUSTERED:
                        case T_CMP:
                        case T_COLLECT:
                        case T_COLLECTION:
                        case T_COLUMN:
                        case T_COMMENT:
                        case T_CONSTANT:
                        case T_COMMIT:
                        case T_COMPRESS:
                        case T_CONCAT:
                        case T_CONDITION:
                        case T_CONSTRAINT:
                        case T_CONTINUE:
                        case T_COPY:
                        case T_COUNT:
                        case T_COUNT_BIG:
                        case T_CREATION:
                        case T_CREATOR:
                        case T_CS:
                        case T_CURRENT:
                        case T_CURRENT_SCHEMA:
                        case T_CURSOR:
                        case T_DATABASE:
                        case T_DATA:
                        case T_DATE:
                        case T_DATETIME:
                        case T_DAY:
                        case T_DAYS:
                        case T_DEC:
                        case T_DECIMAL:
                        case T_DECLARE:
                        case T_DEFAULT:
                        case T_DEFERRED:
                        case T_DEFINED:
                        case T_DEFINER:
                        case T_DEFINITION:
                        case T_DELETE:
                        case T_DELIMITED:
                        case T_DELIMITER:
                        case T_DESC:
                        case T_DESCRIBE:
                        case T_DIAGNOSTICS:
                        case T_DIR:
                        case T_DIRECTORY:
                        case T_DISTINCT:
                        case T_DISTRIBUTE:
                        case T_DO:
                        case T_DOUBLE:
                        case T_DOWNLOAD:
                        case T_DROP:
                        case T_DYNAMIC:
                        case T_ENABLE:
                        case T_ENGINE:
                        case T_ESCAPED:
                        case T_EXCEPT:
                        case T_EXEC:
                        case T_EXECUTE:
                        case T_EXCEPTION:
                        case T_EXCLUSIVE:
                        case T_EXISTS:
                        case T_EXIT:
                        case T_FALLBACK:
                        case T_FALSE:
                        case T_FETCH:
                        case T_FIELDS:
                        case T_FILE:
                        case T_FILES:
                        case T_FLOAT:
                        case T_FOR:
                        case T_FOREIGN:
                        case T_FORMAT:
                        case T_FOUND:
                        case T_FROM:
                        case T_FULL:
                        case T_FUNCTION:
                        case T_GET:
                        case T_GLOBAL:
                        case T_GO:
                        case T_GRANT:
                        case T_GROUP:
                        case T_HANDLER:
                        case T_HASH:
                        case T_HAVING:
                        case T_HOST:
                        case T_IDENTITY:
                        case T_IF:
                        case T_IGNORE:
                        case T_IMMEDIATE:
                        case T_IN:
                        case T_INCLUDE:
                        case T_INDEX:
                        case T_INITRANS:
                        case T_INNER:
                        case T_INOUT:
                        case T_INSERT:
                        case T_INT:
                        case T_INT2:
                        case T_INT4:
                        case T_INT8:
                        case T_INTEGER:
                        case T_INTERSECT:
                        case T_INTERVAL:
                        case T_INTO:
                        case T_INVOKER:
                        case T_IS:
                        case T_ISOPEN:
                        case T_ITEMS:
                        case T_JOIN:
                        case T_KEEP:
                        case T_KEY:
                        case T_KEYS:
                        case T_LANGUAGE:
                        case T_LEAVE:
                        case T_LEFT:
                        case T_LIKE:
                        case T_LIMIT:
                        case T_LINES:
                        case T_LOCAL:
                        case T_LOCATION:
                        case T_LOCATOR:
                        case T_LOCATORS:
                        case T_LOCKS:
                        case T_LOG:
                        case T_LOGGED:
                        case T_LOGGING:
                        case T_LOOP:
                        case T_MAP:
                        case T_MATCHED:
                        case T_MAX:
                        case T_MAXTRANS:
                        case T_MERGE:
                        case T_MESSAGE_TEXT:
                        case T_MICROSECOND:
                        case T_MICROSECONDS:
                        case T_MIN:
                        case T_MULTISET:
                        case T_NCHAR:
                        case T_NEW:
                        case T_NVARCHAR:
                        case T_NO:
                        case T_NOCOUNT:
                        case T_NOCOMPRESS:
                        case T_NOLOGGING:
                        case T_NONE:
                        case T_NOT:
                        case T_NOTFOUND:
                        case T_NULL:
                        case T_NUMERIC:
                        case T_NUMBER:
                        case T_OBJECT:
                        case T_OFF:
                        case T_ON:
                        case T_ONLY:
                        case T_OPEN:
                        case T_OR:
                        case T_ORDER:
                        case T_OUT:
                        case T_OUTER:
                        case T_OVER:
                        case T_OVERWRITE:
                        case T_OWNER:
                        case T_PACKAGE:
                        case T_PARTITION:
                        case T_PCTFREE:
                        case T_PCTUSED:
                        case T_PRECISION:
                        case T_PRESERVE:
                        case T_PRIMARY:
                        case T_PRINT:
                        case T_PROC:
                        case T_PROCEDURE:
                        case T_QUALIFY:
                        case T_QUERY_BAND:
                        case T_QUIT:
                        case T_QUOTED_IDENTIFIER:
                        case T_QUOTES:
                        case T_RAISE:
                        case T_REAL:
                        case T_REFERENCES:
                        case T_REGEXP:
                        case T_REPLACE:
                        case T_RESIGNAL:
                        case T_RESTRICT:
                        case T_RESULT:
                        case T_RESULT_SET_LOCATOR:
                        case T_RETURN:
                        case T_RETURNS:
                        case T_REVERSE:
                        case T_RIGHT:
                        case T_RLIKE:
                        case T_ROLE:
                        case T_ROLLBACK:
                        case T_ROW:
                        case T_ROWS:
                        case T_ROW_COUNT:
                        case T_RR:
                        case T_RS:
                        case T_PWD:
                        case T_TRIM:
                        case T_SCHEMA:
                        case T_SECOND:
                        case T_SECONDS:
                        case T_SECURITY:
                        case T_SEGMENT:
                        case T_SEL:
                        case T_SELECT:
                        case T_SET:
                        case T_SESSION:
                        case T_SESSIONS:
                        case T_SETS:
                        case T_SIGNAL:
                        case T_SIMPLE_DOUBLE:
                        case T_SIMPLE_FLOAT:
                        case T_SMALLDATETIME:
                        case T_SMALLINT:
                        case T_SQL:
                        case T_SQLEXCEPTION:
                        case T_SQLINSERT:
                        case T_SQLSTATE:
                        case T_SQLWARNING:
                        case T_STATS:
                        case T_STATISTICS:
                        case T_STEP:
                        case T_STORAGE:
                        case T_STORE:
                        case T_STORED:
                        case T_STREAM:
                        case T_STRING:
                        case T_SUBDIR:
                        case T_SUBSTRING:
                        case T_SUM:
                        case T_SUMMARY:
                        case T_SYS_REFCURSOR:
                        case T_TABLE:
                        case T_TABLESPACE:
                        case T_TEMPORARY:
                        case T_TERMINATED:
                        case T_TEXTIMAGE_ON:
                        case T_THEN:
                        case T_TIMESTAMP:
                        case T_TITLE:
                        case T_TO:
                        case T_TOP:
                        case T_TRANSACTION:
                        case T_TRUE:
                        case T_TRUNCATE:
                        case T_UNIQUE:
                        case T_UPDATE:
                        case T_UR:
                        case T_USE:
                        case T_USING:
                        case T_VALUE:
                        case T_VALUES:
                        case T_VAR:
                        case T_VARCHAR:
                        case T_VARCHAR2:
                        case T_VARYING:
                        case T_VOLATILE:
                        case T_WHILE:
                        case T_WITH:
                        case T_WITHOUT:
                        case T_WORK:
                        case T_XACT_ABORT:
                        case T_XML:
                        case T_YES:
                        case T_ACTIVITY_COUNT:
                        case T_CUME_DIST:
                        case T_CURRENT_DATE:
                        case T_CURRENT_TIME:
                        case T_PI:
                        case T_CURRENT_TIMESTAMP:
                        case T_CURRENT_USER:
                        case T_DENSE_RANK:
                        case T_FIRST_VALUE:
                        case T_LAG:
                        case T_LAST_VALUE:
                        case T_LEAD:
                        case T_MAX_PART_STRING:
                        case T_MIN_PART_STRING:
                        case T_MAX_PART_INT:
                        case T_MIN_PART_INT:
                        case T_MAX_PART_DATE:
                        case T_MIN_PART_DATE:
                        case T_PART_COUNT:
                        case T_PART_LOC:
                        case T_RANK:
                        case T_ROW_NUMBER:
                        case T_STDEV:
                        case T_SYSDATE:
                        case T_VARIANCE:
                        case T_USER:
                        case T_ADD:
                        case T_OPEN_P:
                        case T_SUB:
                        case L_ID:
                        case L_S_STRING:
                        case L_D_STRING:
                        case L_INT:
                        case L_DEC: {
                            {
                                setState(1565);
                                _errHandler.sync(this);
                                switch (getInterpreter().adaptivePredict(_input, 190, _ctx)) {
                                    case 1: {
                                        setState(1564);
                                        exprFuncAllDistinct();
                                    }
                                        break;
                                }
                                setState(1567);
                                expr(0);
                            }
                        }
                            break;
                        case T_MUL: {
                            setState(1568);
                            match(T_MUL);
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    setState(1571);
                    match(T_CLOSE_P);
                    setState(1573);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 192, _ctx)) {
                        case 1: {
                            setState(1572);
                            exprFuncOverClause();
                        }
                            break;
                    }
                }
                    break;
                case T_CUME_DIST:
                    enterOuterAlt(_localctx, 4); {
                    setState(1575);
                    match(T_CUME_DIST);
                    setState(1576);
                    match(T_OPEN_P);
                    setState(1577);
                    match(T_CLOSE_P);
                    setState(1578);
                    exprFuncOverClause();
                }
                    break;
                case T_DENSE_RANK:
                    enterOuterAlt(_localctx, 5); {
                    setState(1579);
                    match(T_DENSE_RANK);
                    setState(1580);
                    match(T_OPEN_P);
                    setState(1581);
                    match(T_CLOSE_P);
                    setState(1582);
                    exprFuncOverClause();
                }
                    break;
                case T_FIRST_VALUE:
                    enterOuterAlt(_localctx, 6); {
                    setState(1583);
                    match(T_FIRST_VALUE);
                    setState(1584);
                    match(T_OPEN_P);
                    setState(1585);
                    expr(0);
                    setState(1586);
                    match(T_CLOSE_P);
                    setState(1587);
                    exprFuncOverClause();
                }
                    break;
                case T_LAG:
                    enterOuterAlt(_localctx, 7); {
                    setState(1589);
                    match(T_LAG);
                    setState(1590);
                    match(T_OPEN_P);
                    setState(1591);
                    expr(0);
                    setState(1598);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COMMA) {
                        {
                            setState(1592);
                            match(T_COMMA);
                            setState(1593);
                            expr(0);
                            setState(1596);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            if (_la == T_COMMA) {
                                {
                                    setState(1594);
                                    match(T_COMMA);
                                    setState(1595);
                                    expr(0);
                                }
                            }

                        }
                    }

                    setState(1600);
                    match(T_CLOSE_P);
                    setState(1601);
                    exprFuncOverClause();
                }
                    break;
                case T_LAST_VALUE:
                    enterOuterAlt(_localctx, 8); {
                    setState(1603);
                    match(T_LAST_VALUE);
                    setState(1604);
                    match(T_OPEN_P);
                    setState(1605);
                    expr(0);
                    setState(1606);
                    match(T_CLOSE_P);
                    setState(1607);
                    exprFuncOverClause();
                }
                    break;
                case T_LEAD:
                    enterOuterAlt(_localctx, 9); {
                    setState(1609);
                    match(T_LEAD);
                    setState(1610);
                    match(T_OPEN_P);
                    setState(1611);
                    expr(0);
                    setState(1618);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COMMA) {
                        {
                            setState(1612);
                            match(T_COMMA);
                            setState(1613);
                            expr(0);
                            setState(1616);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            if (_la == T_COMMA) {
                                {
                                    setState(1614);
                                    match(T_COMMA);
                                    setState(1615);
                                    expr(0);
                                }
                            }

                        }
                    }

                    setState(1620);
                    match(T_CLOSE_P);
                    setState(1621);
                    exprFuncOverClause();
                }
                    break;
                case T_MAX:
                    enterOuterAlt(_localctx, 10); {
                    setState(1623);
                    match(T_MAX);
                    setState(1624);
                    match(T_OPEN_P);
                    setState(1626);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 197, _ctx)) {
                        case 1: {
                            setState(1625);
                            exprFuncAllDistinct();
                        }
                            break;
                    }
                    setState(1628);
                    expr(0);
                    setState(1629);
                    match(T_CLOSE_P);
                    setState(1631);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 198, _ctx)) {
                        case 1: {
                            setState(1630);
                            exprFuncOverClause();
                        }
                            break;
                    }
                }
                    break;
                case T_MIN:
                    enterOuterAlt(_localctx, 11); {
                    setState(1633);
                    match(T_MIN);
                    setState(1634);
                    match(T_OPEN_P);
                    setState(1636);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 199, _ctx)) {
                        case 1: {
                            setState(1635);
                            exprFuncAllDistinct();
                        }
                            break;
                    }
                    setState(1638);
                    expr(0);
                    setState(1639);
                    match(T_CLOSE_P);
                    setState(1641);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 200, _ctx)) {
                        case 1: {
                            setState(1640);
                            exprFuncOverClause();
                        }
                            break;
                    }
                }
                    break;
                case T_RANK:
                    enterOuterAlt(_localctx, 12); {
                    setState(1643);
                    match(T_RANK);
                    setState(1644);
                    match(T_OPEN_P);
                    setState(1645);
                    match(T_CLOSE_P);
                    setState(1646);
                    exprFuncOverClause();
                }
                    break;
                case T_ROW_NUMBER:
                    enterOuterAlt(_localctx, 13); {
                    setState(1647);
                    match(T_ROW_NUMBER);
                    setState(1648);
                    match(T_OPEN_P);
                    setState(1649);
                    match(T_CLOSE_P);
                    setState(1650);
                    exprFuncOverClause();
                }
                    break;
                case T_STDEV:
                    enterOuterAlt(_localctx, 14); {
                    setState(1651);
                    match(T_STDEV);
                    setState(1652);
                    match(T_OPEN_P);
                    setState(1654);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 201, _ctx)) {
                        case 1: {
                            setState(1653);
                            exprFuncAllDistinct();
                        }
                            break;
                    }
                    setState(1656);
                    expr(0);
                    setState(1657);
                    match(T_CLOSE_P);
                    setState(1659);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 202, _ctx)) {
                        case 1: {
                            setState(1658);
                            exprFuncOverClause();
                        }
                            break;
                    }
                }
                    break;
                case T_SUM:
                    enterOuterAlt(_localctx, 15); {
                    setState(1661);
                    match(T_SUM);
                    setState(1662);
                    match(T_OPEN_P);
                    setState(1664);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 203, _ctx)) {
                        case 1: {
                            setState(1663);
                            exprFuncAllDistinct();
                        }
                            break;
                    }
                    setState(1666);
                    expr(0);
                    setState(1667);
                    match(T_CLOSE_P);
                    setState(1669);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 204, _ctx)) {
                        case 1: {
                            setState(1668);
                            exprFuncOverClause();
                        }
                            break;
                    }
                }
                    break;
                case T_VAR:
                    enterOuterAlt(_localctx, 16); {
                    setState(1671);
                    match(T_VAR);
                    setState(1672);
                    match(T_OPEN_P);
                    setState(1674);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 205, _ctx)) {
                        case 1: {
                            setState(1673);
                            exprFuncAllDistinct();
                        }
                            break;
                    }
                    setState(1676);
                    expr(0);
                    setState(1677);
                    match(T_CLOSE_P);
                    setState(1679);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 206, _ctx)) {
                        case 1: {
                            setState(1678);
                            exprFuncOverClause();
                        }
                            break;
                    }
                }
                    break;
                case T_VARIANCE:
                    enterOuterAlt(_localctx, 17); {
                    setState(1681);
                    match(T_VARIANCE);
                    setState(1682);
                    match(T_OPEN_P);
                    setState(1684);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 207, _ctx)) {
                        case 1: {
                            setState(1683);
                            exprFuncAllDistinct();
                        }
                            break;
                    }
                    setState(1686);
                    expr(0);
                    setState(1687);
                    match(T_CLOSE_P);
                    setState(1689);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 208, _ctx)) {
                        case 1: {
                            setState(1688);
                            exprFuncOverClause();
                        }
                            break;
                    }
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprFuncAllDistinctContext extends ParserRuleContext {
        public TerminalNode T_ALL() {
            return getToken(CupidDBSqlParser.T_ALL, 0);
        }

        public TerminalNode T_DISTINCT() {
            return getToken(CupidDBSqlParser.T_DISTINCT, 0);
        }

        public ExprFuncAllDistinctContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprFuncAllDistinct;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprFuncAllDistinct(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprFuncAllDistinctContext exprFuncAllDistinct() throws RecognitionException {
        ExprFuncAllDistinctContext _localctx = new ExprFuncAllDistinctContext(_ctx, getState());
        enterRule(_localctx, 236, RULE_exprFuncAllDistinct);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1693);
                _la = _input.LA(1);
                if (!(_la == T_ALL || _la == T_DISTINCT)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprFuncOverClauseContext extends ParserRuleContext {
        public TerminalNode T_OVER() {
            return getToken(CupidDBSqlParser.T_OVER, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public ExprFuncPartitionByClauseContext exprFuncPartitionByClause() {
            return getRuleContext(ExprFuncPartitionByClauseContext.class, 0);
        }

        public OrderByClauseContext orderByClause() {
            return getRuleContext(OrderByClauseContext.class, 0);
        }

        public ExprFuncOverClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprFuncOverClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprFuncOverClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprFuncOverClauseContext exprFuncOverClause() throws RecognitionException {
        ExprFuncOverClauseContext _localctx = new ExprFuncOverClauseContext(_ctx, getState());
        enterRule(_localctx, 238, RULE_exprFuncOverClause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1695);
                match(T_OVER);
                setState(1696);
                match(T_OPEN_P);
                setState(1698);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_PARTITION) {
                    {
                        setState(1697);
                        exprFuncPartitionByClause();
                    }
                }

                setState(1701);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_ORDER) {
                    {
                        setState(1700);
                        orderByClause();
                    }
                }

                setState(1703);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprFuncPartitionByClauseContext extends ParserRuleContext {
        public TerminalNode T_PARTITION() {
            return getToken(CupidDBSqlParser.T_PARTITION, 0);
        }

        public TerminalNode T_BY() {
            return getToken(CupidDBSqlParser.T_BY, 0);
        }

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public ExprFuncPartitionByClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprFuncPartitionByClause;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprFuncPartitionByClause(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprFuncPartitionByClauseContext exprFuncPartitionByClause()
        throws RecognitionException {
        ExprFuncPartitionByClauseContext _localctx = new ExprFuncPartitionByClauseContext(
            _ctx,
            getState()
        );
        enterRule(_localctx, 240, RULE_exprFuncPartitionByClause);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1705);
                match(T_PARTITION);
                setState(1706);
                match(T_BY);
                setState(1707);
                expr(0);
                setState(1712);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(1708);
                            match(T_COMMA);
                            setState(1709);
                            expr(0);
                        }
                    }
                    setState(1714);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprSpecFuncContext extends ParserRuleContext {
        public TerminalNode T_ACTIVITY_COUNT() {
            return getToken(CupidDBSqlParser.T_ACTIVITY_COUNT, 0);
        }

        public TerminalNode T_CAST() {
            return getToken(CupidDBSqlParser.T_CAST, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public List<ExprContext> expr() {
            return getRuleContexts(ExprContext.class);
        }

        public ExprContext expr(int i) {
            return getRuleContext(ExprContext.class, i);
        }

        public TerminalNode T_AS() {
            return getToken(CupidDBSqlParser.T_AS, 0);
        }

        public DtypeContext dtype() {
            return getRuleContext(DtypeContext.class, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public DtypeLenContext dtypeLen() {
            return getRuleContext(DtypeLenContext.class, 0);
        }

        public TerminalNode T_COUNT() {
            return getToken(CupidDBSqlParser.T_COUNT, 0);
        }

        public TerminalNode T_MUL() {
            return getToken(CupidDBSqlParser.T_MUL, 0);
        }

        public TerminalNode T_CURRENT_DATE() {
            return getToken(CupidDBSqlParser.T_CURRENT_DATE, 0);
        }

        public TerminalNode T_CURRENT() {
            return getToken(CupidDBSqlParser.T_CURRENT, 0);
        }

        public TerminalNode T_DATE() {
            return getToken(CupidDBSqlParser.T_DATE, 0);
        }

        public TerminalNode T_CURRENT_TIME() {
            return getToken(CupidDBSqlParser.T_CURRENT_TIME, 0);
        }

        public TerminalNode T_PI() {
            return getToken(CupidDBSqlParser.T_PI, 0);
        }

        public TerminalNode T_CURRENT_TIMESTAMP() {
            return getToken(CupidDBSqlParser.T_CURRENT_TIMESTAMP, 0);
        }

        public TerminalNode T_TIMESTAMP() {
            return getToken(CupidDBSqlParser.T_TIMESTAMP, 0);
        }

        public TerminalNode T_CURRENT_USER() {
            return getToken(CupidDBSqlParser.T_CURRENT_USER, 0);
        }

        public TerminalNode T_USER() {
            return getToken(CupidDBSqlParser.T_USER, 0);
        }

        public TerminalNode T_MAX_PART_STRING() {
            return getToken(CupidDBSqlParser.T_MAX_PART_STRING, 0);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public List<TerminalNode> T_EQUAL() {
            return getTokens(CupidDBSqlParser.T_EQUAL);
        }

        public TerminalNode T_EQUAL(int i) {
            return getToken(CupidDBSqlParser.T_EQUAL, i);
        }

        public TerminalNode T_MIN_PART_STRING() {
            return getToken(CupidDBSqlParser.T_MIN_PART_STRING, 0);
        }

        public TerminalNode T_MAX_PART_INT() {
            return getToken(CupidDBSqlParser.T_MAX_PART_INT, 0);
        }

        public TerminalNode T_MIN_PART_INT() {
            return getToken(CupidDBSqlParser.T_MIN_PART_INT, 0);
        }

        public TerminalNode T_MAX_PART_DATE() {
            return getToken(CupidDBSqlParser.T_MAX_PART_DATE, 0);
        }

        public TerminalNode T_MIN_PART_DATE() {
            return getToken(CupidDBSqlParser.T_MIN_PART_DATE, 0);
        }

        public TerminalNode T_PART_COUNT() {
            return getToken(CupidDBSqlParser.T_PART_COUNT, 0);
        }

        public TerminalNode T_PART_LOC() {
            return getToken(CupidDBSqlParser.T_PART_LOC, 0);
        }

        public TerminalNode T_SUBSTRING() {
            return getToken(CupidDBSqlParser.T_SUBSTRING, 0);
        }

        public TerminalNode T_FROM() {
            return getToken(CupidDBSqlParser.T_FROM, 0);
        }

        public TerminalNode T_FOR() {
            return getToken(CupidDBSqlParser.T_FOR, 0);
        }

        public TerminalNode T_SYSDATE() {
            return getToken(CupidDBSqlParser.T_SYSDATE, 0);
        }

        public ExprSpecFuncContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprSpecFunc;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprSpecFunc(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprSpecFuncContext exprSpecFunc() throws RecognitionException {
        ExprSpecFuncContext _localctx = new ExprSpecFuncContext(_ctx, getState());
        enterRule(_localctx, 242, RULE_exprSpecFunc);
        int _la;
        try {
            int _alt;
            setState(1912);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 233, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {
                    setState(1715);
                    match(T_ACTIVITY_COUNT);
                }
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(1716);
                    match(T_CAST);
                    setState(1717);
                    match(T_OPEN_P);
                    setState(1718);
                    expr(0);
                    setState(1719);
                    match(T_AS);
                    setState(1720);
                    dtype();
                    setState(1722);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_OPEN_P) {
                        {
                            setState(1721);
                            dtypeLen();
                        }
                    }

                    setState(1724);
                    match(T_CLOSE_P);
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(1726);
                    match(T_COUNT);
                    setState(1727);
                    match(T_OPEN_P);
                    setState(1730);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case T_ACTION:
                        case T_ADD2:
                        case T_ALL:
                        case T_ALLOCATE:
                        case T_ALTER:
                        case T_AND:
                        case T_ANSI_NULLS:
                        case T_ANSI_PADDING:
                        case T_AS:
                        case T_ASC:
                        case T_ASSOCIATE:
                        case T_AT:
                        case T_AVG:
                        case T_BATCHSIZE:
                        case T_BEGIN:
                        case T_BETWEEN:
                        case T_BIGINT:
                        case T_BINARY_DOUBLE:
                        case T_BINARY_FLOAT:
                        case T_BIT:
                        case T_BODY:
                        case T_BREAK:
                        case T_BY:
                        case T_BYTE:
                        case T_CALL:
                        case T_CALLER:
                        case T_CASCADE:
                        case T_CASE:
                        case T_CASESPECIFIC:
                        case T_CAST:
                        case T_CHAR:
                        case T_CHARACTER:
                        case T_CHARSET:
                        case T_CLIENT:
                        case T_CLOSE:
                        case T_CLUSTERED:
                        case T_CMP:
                        case T_COLLECT:
                        case T_COLLECTION:
                        case T_COLUMN:
                        case T_COMMENT:
                        case T_CONSTANT:
                        case T_COMMIT:
                        case T_COMPRESS:
                        case T_CONCAT:
                        case T_CONDITION:
                        case T_CONSTRAINT:
                        case T_CONTINUE:
                        case T_COPY:
                        case T_COUNT:
                        case T_COUNT_BIG:
                        case T_CREATION:
                        case T_CREATOR:
                        case T_CS:
                        case T_CURRENT:
                        case T_CURRENT_SCHEMA:
                        case T_CURSOR:
                        case T_DATABASE:
                        case T_DATA:
                        case T_DATE:
                        case T_DATETIME:
                        case T_DAY:
                        case T_DAYS:
                        case T_DEC:
                        case T_DECIMAL:
                        case T_DECLARE:
                        case T_DEFAULT:
                        case T_DEFERRED:
                        case T_DEFINED:
                        case T_DEFINER:
                        case T_DEFINITION:
                        case T_DELETE:
                        case T_DELIMITED:
                        case T_DELIMITER:
                        case T_DESC:
                        case T_DESCRIBE:
                        case T_DIAGNOSTICS:
                        case T_DIR:
                        case T_DIRECTORY:
                        case T_DISTINCT:
                        case T_DISTRIBUTE:
                        case T_DO:
                        case T_DOUBLE:
                        case T_DOWNLOAD:
                        case T_DROP:
                        case T_DYNAMIC:
                        case T_ENABLE:
                        case T_ENGINE:
                        case T_ESCAPED:
                        case T_EXCEPT:
                        case T_EXEC:
                        case T_EXECUTE:
                        case T_EXCEPTION:
                        case T_EXCLUSIVE:
                        case T_EXISTS:
                        case T_EXIT:
                        case T_FALLBACK:
                        case T_FALSE:
                        case T_FETCH:
                        case T_FIELDS:
                        case T_FILE:
                        case T_FILES:
                        case T_FLOAT:
                        case T_FOR:
                        case T_FOREIGN:
                        case T_FORMAT:
                        case T_FOUND:
                        case T_FROM:
                        case T_FULL:
                        case T_FUNCTION:
                        case T_GET:
                        case T_GLOBAL:
                        case T_GO:
                        case T_GRANT:
                        case T_GROUP:
                        case T_HANDLER:
                        case T_HASH:
                        case T_HAVING:
                        case T_HOST:
                        case T_IDENTITY:
                        case T_IF:
                        case T_IGNORE:
                        case T_IMMEDIATE:
                        case T_IN:
                        case T_INCLUDE:
                        case T_INDEX:
                        case T_INITRANS:
                        case T_INNER:
                        case T_INOUT:
                        case T_INSERT:
                        case T_INT:
                        case T_INT2:
                        case T_INT4:
                        case T_INT8:
                        case T_INTEGER:
                        case T_INTERSECT:
                        case T_INTERVAL:
                        case T_INTO:
                        case T_INVOKER:
                        case T_IS:
                        case T_ISOPEN:
                        case T_ITEMS:
                        case T_JOIN:
                        case T_KEEP:
                        case T_KEY:
                        case T_KEYS:
                        case T_LANGUAGE:
                        case T_LEAVE:
                        case T_LEFT:
                        case T_LIKE:
                        case T_LIMIT:
                        case T_LINES:
                        case T_LOCAL:
                        case T_LOCATION:
                        case T_LOCATOR:
                        case T_LOCATORS:
                        case T_LOCKS:
                        case T_LOG:
                        case T_LOGGED:
                        case T_LOGGING:
                        case T_LOOP:
                        case T_MAP:
                        case T_MATCHED:
                        case T_MAX:
                        case T_MAXTRANS:
                        case T_MERGE:
                        case T_MESSAGE_TEXT:
                        case T_MICROSECOND:
                        case T_MICROSECONDS:
                        case T_MIN:
                        case T_MULTISET:
                        case T_NCHAR:
                        case T_NEW:
                        case T_NVARCHAR:
                        case T_NO:
                        case T_NOCOUNT:
                        case T_NOCOMPRESS:
                        case T_NOLOGGING:
                        case T_NONE:
                        case T_NOT:
                        case T_NOTFOUND:
                        case T_NULL:
                        case T_NUMERIC:
                        case T_NUMBER:
                        case T_OBJECT:
                        case T_OFF:
                        case T_ON:
                        case T_ONLY:
                        case T_OPEN:
                        case T_OR:
                        case T_ORDER:
                        case T_OUT:
                        case T_OUTER:
                        case T_OVER:
                        case T_OVERWRITE:
                        case T_OWNER:
                        case T_PACKAGE:
                        case T_PARTITION:
                        case T_PCTFREE:
                        case T_PCTUSED:
                        case T_PRECISION:
                        case T_PRESERVE:
                        case T_PRIMARY:
                        case T_PRINT:
                        case T_PROC:
                        case T_PROCEDURE:
                        case T_QUALIFY:
                        case T_QUERY_BAND:
                        case T_QUIT:
                        case T_QUOTED_IDENTIFIER:
                        case T_QUOTES:
                        case T_RAISE:
                        case T_REAL:
                        case T_REFERENCES:
                        case T_REGEXP:
                        case T_REPLACE:
                        case T_RESIGNAL:
                        case T_RESTRICT:
                        case T_RESULT:
                        case T_RESULT_SET_LOCATOR:
                        case T_RETURN:
                        case T_RETURNS:
                        case T_REVERSE:
                        case T_RIGHT:
                        case T_RLIKE:
                        case T_ROLE:
                        case T_ROLLBACK:
                        case T_ROW:
                        case T_ROWS:
                        case T_ROW_COUNT:
                        case T_RR:
                        case T_RS:
                        case T_PWD:
                        case T_TRIM:
                        case T_SCHEMA:
                        case T_SECOND:
                        case T_SECONDS:
                        case T_SECURITY:
                        case T_SEGMENT:
                        case T_SEL:
                        case T_SELECT:
                        case T_SET:
                        case T_SESSION:
                        case T_SESSIONS:
                        case T_SETS:
                        case T_SIGNAL:
                        case T_SIMPLE_DOUBLE:
                        case T_SIMPLE_FLOAT:
                        case T_SMALLDATETIME:
                        case T_SMALLINT:
                        case T_SQL:
                        case T_SQLEXCEPTION:
                        case T_SQLINSERT:
                        case T_SQLSTATE:
                        case T_SQLWARNING:
                        case T_STATS:
                        case T_STATISTICS:
                        case T_STEP:
                        case T_STORAGE:
                        case T_STORE:
                        case T_STORED:
                        case T_STREAM:
                        case T_STRING:
                        case T_SUBDIR:
                        case T_SUBSTRING:
                        case T_SUM:
                        case T_SUMMARY:
                        case T_SYS_REFCURSOR:
                        case T_TABLE:
                        case T_TABLESPACE:
                        case T_TEMPORARY:
                        case T_TERMINATED:
                        case T_TEXTIMAGE_ON:
                        case T_THEN:
                        case T_TIMESTAMP:
                        case T_TITLE:
                        case T_TO:
                        case T_TOP:
                        case T_TRANSACTION:
                        case T_TRUE:
                        case T_TRUNCATE:
                        case T_UNIQUE:
                        case T_UPDATE:
                        case T_UR:
                        case T_USE:
                        case T_USING:
                        case T_VALUE:
                        case T_VALUES:
                        case T_VAR:
                        case T_VARCHAR:
                        case T_VARCHAR2:
                        case T_VARYING:
                        case T_VOLATILE:
                        case T_WHILE:
                        case T_WITH:
                        case T_WITHOUT:
                        case T_WORK:
                        case T_XACT_ABORT:
                        case T_XML:
                        case T_YES:
                        case T_ACTIVITY_COUNT:
                        case T_CUME_DIST:
                        case T_CURRENT_DATE:
                        case T_CURRENT_TIME:
                        case T_PI:
                        case T_CURRENT_TIMESTAMP:
                        case T_CURRENT_USER:
                        case T_DENSE_RANK:
                        case T_FIRST_VALUE:
                        case T_LAG:
                        case T_LAST_VALUE:
                        case T_LEAD:
                        case T_MAX_PART_STRING:
                        case T_MIN_PART_STRING:
                        case T_MAX_PART_INT:
                        case T_MIN_PART_INT:
                        case T_MAX_PART_DATE:
                        case T_MIN_PART_DATE:
                        case T_PART_COUNT:
                        case T_PART_LOC:
                        case T_RANK:
                        case T_ROW_NUMBER:
                        case T_STDEV:
                        case T_SYSDATE:
                        case T_VARIANCE:
                        case T_USER:
                        case T_ADD:
                        case T_OPEN_P:
                        case T_SUB:
                        case L_ID:
                        case L_S_STRING:
                        case L_D_STRING:
                        case L_INT:
                        case L_DEC: {
                            setState(1728);
                            expr(0);
                        }
                            break;
                        case T_MUL: {
                            setState(1729);
                            match(T_MUL);
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    setState(1732);
                    match(T_CLOSE_P);
                }
                    break;
                case 4:
                    enterOuterAlt(_localctx, 4); {
                    setState(1733);
                    match(T_CURRENT_DATE);
                }
                    break;
                case 5:
                    enterOuterAlt(_localctx, 5); {
                    setState(1734);
                    match(T_CURRENT);
                    setState(1735);
                    match(T_DATE);
                }
                    break;
                case 6:
                    enterOuterAlt(_localctx, 6); {
                    setState(1736);
                    match(T_CURRENT_TIME);
                }
                    break;
                case 7:
                    enterOuterAlt(_localctx, 7); {
                    setState(1737);
                    match(T_PI);
                }
                    break;
                case 8:
                    enterOuterAlt(_localctx, 8); {
                    setState(1741);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case T_CURRENT_TIMESTAMP: {
                            setState(1738);
                            match(T_CURRENT_TIMESTAMP);
                        }
                            break;
                        case T_CURRENT: {
                            setState(1739);
                            match(T_CURRENT);
                            setState(1740);
                            match(T_TIMESTAMP);
                        }
                            break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    setState(1747);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 216, _ctx)) {
                        case 1: {
                            setState(1743);
                            match(T_OPEN_P);
                            setState(1744);
                            expr(0);
                            setState(1745);
                            match(T_CLOSE_P);
                        }
                            break;
                    }
                }
                    break;
                case 9:
                    enterOuterAlt(_localctx, 9); {
                    setState(1749);
                    match(T_CURRENT_USER);
                }
                    break;
                case 10:
                    enterOuterAlt(_localctx, 10); {
                    setState(1750);
                    match(T_CURRENT);
                    setState(1751);
                    match(T_USER);
                }
                    break;
                case 11:
                    enterOuterAlt(_localctx, 11); {
                    setState(1752);
                    match(T_MAX_PART_STRING);
                    setState(1753);
                    match(T_OPEN_P);
                    setState(1754);
                    expr(0);
                    setState(1767);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COMMA) {
                        {
                            setState(1755);
                            match(T_COMMA);
                            setState(1756);
                            expr(0);
                            setState(1764);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            while (_la == T_COMMA) {
                                {
                                    {
                                        setState(1757);
                                        match(T_COMMA);
                                        setState(1758);
                                        expr(0);
                                        setState(1759);
                                        match(T_EQUAL);
                                        setState(1760);
                                        expr(0);
                                    }
                                }
                                setState(1766);
                                _errHandler.sync(this);
                                _la = _input.LA(1);
                            }
                        }
                    }

                    setState(1769);
                    match(T_CLOSE_P);
                }
                    break;
                case 12:
                    enterOuterAlt(_localctx, 12); {
                    setState(1771);
                    match(T_MIN_PART_STRING);
                    setState(1772);
                    match(T_OPEN_P);
                    setState(1773);
                    expr(0);
                    setState(1786);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COMMA) {
                        {
                            setState(1774);
                            match(T_COMMA);
                            setState(1775);
                            expr(0);
                            setState(1783);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            while (_la == T_COMMA) {
                                {
                                    {
                                        setState(1776);
                                        match(T_COMMA);
                                        setState(1777);
                                        expr(0);
                                        setState(1778);
                                        match(T_EQUAL);
                                        setState(1779);
                                        expr(0);
                                    }
                                }
                                setState(1785);
                                _errHandler.sync(this);
                                _la = _input.LA(1);
                            }
                        }
                    }

                    setState(1788);
                    match(T_CLOSE_P);
                }
                    break;
                case 13:
                    enterOuterAlt(_localctx, 13); {
                    setState(1790);
                    match(T_MAX_PART_INT);
                    setState(1791);
                    match(T_OPEN_P);
                    setState(1792);
                    expr(0);
                    setState(1805);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COMMA) {
                        {
                            setState(1793);
                            match(T_COMMA);
                            setState(1794);
                            expr(0);
                            setState(1802);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            while (_la == T_COMMA) {
                                {
                                    {
                                        setState(1795);
                                        match(T_COMMA);
                                        setState(1796);
                                        expr(0);
                                        setState(1797);
                                        match(T_EQUAL);
                                        setState(1798);
                                        expr(0);
                                    }
                                }
                                setState(1804);
                                _errHandler.sync(this);
                                _la = _input.LA(1);
                            }
                        }
                    }

                    setState(1807);
                    match(T_CLOSE_P);
                }
                    break;
                case 14:
                    enterOuterAlt(_localctx, 14); {
                    setState(1809);
                    match(T_MIN_PART_INT);
                    setState(1810);
                    match(T_OPEN_P);
                    setState(1811);
                    expr(0);
                    setState(1824);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COMMA) {
                        {
                            setState(1812);
                            match(T_COMMA);
                            setState(1813);
                            expr(0);
                            setState(1821);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            while (_la == T_COMMA) {
                                {
                                    {
                                        setState(1814);
                                        match(T_COMMA);
                                        setState(1815);
                                        expr(0);
                                        setState(1816);
                                        match(T_EQUAL);
                                        setState(1817);
                                        expr(0);
                                    }
                                }
                                setState(1823);
                                _errHandler.sync(this);
                                _la = _input.LA(1);
                            }
                        }
                    }

                    setState(1826);
                    match(T_CLOSE_P);
                }
                    break;
                case 15:
                    enterOuterAlt(_localctx, 15); {
                    setState(1828);
                    match(T_MAX_PART_DATE);
                    setState(1829);
                    match(T_OPEN_P);
                    setState(1830);
                    expr(0);
                    setState(1843);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COMMA) {
                        {
                            setState(1831);
                            match(T_COMMA);
                            setState(1832);
                            expr(0);
                            setState(1840);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            while (_la == T_COMMA) {
                                {
                                    {
                                        setState(1833);
                                        match(T_COMMA);
                                        setState(1834);
                                        expr(0);
                                        setState(1835);
                                        match(T_EQUAL);
                                        setState(1836);
                                        expr(0);
                                    }
                                }
                                setState(1842);
                                _errHandler.sync(this);
                                _la = _input.LA(1);
                            }
                        }
                    }

                    setState(1845);
                    match(T_CLOSE_P);
                }
                    break;
                case 16:
                    enterOuterAlt(_localctx, 16); {
                    setState(1847);
                    match(T_MIN_PART_DATE);
                    setState(1848);
                    match(T_OPEN_P);
                    setState(1849);
                    expr(0);
                    setState(1862);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COMMA) {
                        {
                            setState(1850);
                            match(T_COMMA);
                            setState(1851);
                            expr(0);
                            setState(1859);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            while (_la == T_COMMA) {
                                {
                                    {
                                        setState(1852);
                                        match(T_COMMA);
                                        setState(1853);
                                        expr(0);
                                        setState(1854);
                                        match(T_EQUAL);
                                        setState(1855);
                                        expr(0);
                                    }
                                }
                                setState(1861);
                                _errHandler.sync(this);
                                _la = _input.LA(1);
                            }
                        }
                    }

                    setState(1864);
                    match(T_CLOSE_P);
                }
                    break;
                case 17:
                    enterOuterAlt(_localctx, 17); {
                    setState(1866);
                    match(T_PART_COUNT);
                    setState(1867);
                    match(T_OPEN_P);
                    setState(1868);
                    expr(0);
                    setState(1876);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    while (_la == T_COMMA) {
                        {
                            {
                                setState(1869);
                                match(T_COMMA);
                                setState(1870);
                                expr(0);
                                setState(1871);
                                match(T_EQUAL);
                                setState(1872);
                                expr(0);
                            }
                        }
                        setState(1878);
                        _errHandler.sync(this);
                        _la = _input.LA(1);
                    }
                    setState(1879);
                    match(T_CLOSE_P);
                }
                    break;
                case 18:
                    enterOuterAlt(_localctx, 18); {
                    setState(1881);
                    match(T_PART_LOC);
                    setState(1882);
                    match(T_OPEN_P);
                    setState(1883);
                    expr(0);
                    setState(1889);
                    _errHandler.sync(this);
                    _alt = 1;
                    do {
                        switch (_alt) {
                            case 1: {
                                {
                                    setState(1884);
                                    match(T_COMMA);
                                    setState(1885);
                                    expr(0);
                                    setState(1886);
                                    match(T_EQUAL);
                                    setState(1887);
                                    expr(0);
                                }
                            }
                                break;
                            default:
                                throw new NoViableAltException(this);
                        }
                        setState(1891);
                        _errHandler.sync(this);
                        _alt = getInterpreter().adaptivePredict(_input, 230, _ctx);
                    } while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER);
                    setState(1895);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_COMMA) {
                        {
                            setState(1893);
                            match(T_COMMA);
                            setState(1894);
                            expr(0);
                        }
                    }

                    setState(1897);
                    match(T_CLOSE_P);
                }
                    break;
                case 19:
                    enterOuterAlt(_localctx, 19); {
                    setState(1899);
                    match(T_SUBSTRING);
                    setState(1900);
                    match(T_OPEN_P);
                    setState(1901);
                    expr(0);
                    setState(1902);
                    match(T_FROM);
                    setState(1903);
                    expr(0);
                    setState(1906);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                    if (_la == T_FOR) {
                        {
                            setState(1904);
                            match(T_FOR);
                            setState(1905);
                            expr(0);
                        }
                    }

                    setState(1908);
                    match(T_CLOSE_P);
                }
                    break;
                case 20:
                    enterOuterAlt(_localctx, 20); {
                    setState(1910);
                    match(T_SYSDATE);
                }
                    break;
                case 21:
                    enterOuterAlt(_localctx, 21); {
                    setState(1911);
                    match(T_USER);
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprFuncContext extends ParserRuleContext {
        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_OPEN_P() {
            return getToken(CupidDBSqlParser.T_OPEN_P, 0);
        }

        public TerminalNode T_CLOSE_P() {
            return getToken(CupidDBSqlParser.T_CLOSE_P, 0);
        }

        public ExprFuncParamsContext exprFuncParams() {
            return getRuleContext(ExprFuncParamsContext.class, 0);
        }

        public ExprFuncContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprFunc;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprFunc(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprFuncContext exprFunc() throws RecognitionException {
        ExprFuncContext _localctx = new ExprFuncContext(_ctx, getState());
        enterRule(_localctx, 244, RULE_exprFunc);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1914);
                ident();
                setState(1915);
                match(T_OPEN_P);
                setState(1917);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 234, _ctx)) {
                    case 1: {
                        setState(1916);
                        exprFuncParams();
                    }
                        break;
                }
                setState(1919);
                match(T_CLOSE_P);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExprFuncParamsContext extends ParserRuleContext {
        public List<FuncParamContext> funcParam() {
            return getRuleContexts(FuncParamContext.class);
        }

        public FuncParamContext funcParam(int i) {
            return getRuleContext(FuncParamContext.class, i);
        }

        public List<TerminalNode> T_COMMA() {
            return getTokens(CupidDBSqlParser.T_COMMA);
        }

        public TerminalNode T_COMMA(int i) {
            return getToken(CupidDBSqlParser.T_COMMA, i);
        }

        public ExprFuncParamsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exprFuncParams;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitExprFuncParams(this);
            else return visitor.visitChildren(this);
        }
    }

    public final ExprFuncParamsContext exprFuncParams() throws RecognitionException {
        ExprFuncParamsContext _localctx = new ExprFuncParamsContext(_ctx, getState());
        enterRule(_localctx, 246, RULE_exprFuncParams);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1921);
                funcParam();
                setState(1926);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_COMMA) {
                    {
                        {
                            setState(1922);
                            match(T_COMMA);
                            setState(1923);
                            funcParam();
                        }
                    }
                    setState(1928);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FuncParamContext extends ParserRuleContext {
        public ExprContext expr() {
            return getRuleContext(ExprContext.class, 0);
        }

        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public TerminalNode T_EQUAL() {
            return getToken(CupidDBSqlParser.T_EQUAL, 0);
        }

        public TerminalNode T_GREATER() {
            return getToken(CupidDBSqlParser.T_GREATER, 0);
        }

        public TerminalNode T_MUL() {
            return getToken(CupidDBSqlParser.T_MUL, 0);
        }

        public FuncParamContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_funcParam;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitFuncParam(this);
            else return visitor.visitChildren(this);
        }
    }

    public final FuncParamContext funcParam() throws RecognitionException {
        FuncParamContext _localctx = new FuncParamContext(_ctx, getState());
        enterRule(_localctx, 248, RULE_funcParam);
        int _la;
        try {
            setState(1940);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 238, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1); {}
                    break;
                case 2:
                    enterOuterAlt(_localctx, 2); {
                    setState(1930);
                    if (!(!_input.LT(1).getText().equalsIgnoreCase("INTO")))
                        throw new FailedPredicateException(
                            this,
                            "!_input.LT(1).getText().equalsIgnoreCase(\"INTO\")"
                        );
                    setState(1936);
                    _errHandler.sync(this);
                    switch (getInterpreter().adaptivePredict(_input, 237, _ctx)) {
                        case 1: {
                            setState(1931);
                            ident();
                            setState(1932);
                            match(T_EQUAL);
                            setState(1934);
                            _errHandler.sync(this);
                            _la = _input.LA(1);
                            if (_la == T_GREATER) {
                                {
                                    setState(1933);
                                    match(T_GREATER);
                                }
                            }

                        }
                            break;
                    }
                    setState(1938);
                    expr(0);
                }
                    break;
                case 3:
                    enterOuterAlt(_localctx, 3); {
                    setState(1939);
                    match(T_MUL);
                }
                    break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class Table_nameContext extends ParserRuleContext {
        public QidentContext qident() {
            return getRuleContext(QidentContext.class, 0);
        }

        public Table_nameContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_table_name;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitTable_name(this);
            else return visitor.visitChildren(this);
        }
    }

    public final Table_nameContext table_name() throws RecognitionException {
        Table_nameContext _localctx = new Table_nameContext(_ctx, getState());
        enterRule(_localctx, 250, RULE_table_name);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1942);
                qident();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class User_nameContext extends ParserRuleContext {
        public IdentContext ident() {
            return getRuleContext(IdentContext.class, 0);
        }

        public User_nameContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_user_name;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitUser_name(this);
            else return visitor.visitChildren(this);
        }
    }

    public final User_nameContext user_name() throws RecognitionException {
        User_nameContext _localctx = new User_nameContext(_ctx, getState());
        enterRule(_localctx, 252, RULE_user_name);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1944);
                ident();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class QidentContext extends ParserRuleContext {
        public List<IdentContext> ident() {
            return getRuleContexts(IdentContext.class);
        }

        public IdentContext ident(int i) {
            return getRuleContext(IdentContext.class, i);
        }

        public List<TerminalNode> T_DOT() {
            return getTokens(CupidDBSqlParser.T_DOT);
        }

        public TerminalNode T_DOT(int i) {
            return getToken(CupidDBSqlParser.T_DOT, i);
        }

        public QidentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_qident;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitQident(this);
            else return visitor.visitChildren(this);
        }
    }

    public final QidentContext qident() throws RecognitionException {
        QidentContext _localctx = new QidentContext(_ctx, getState());
        enterRule(_localctx, 254, RULE_qident);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1946);
                ident();
                setState(1951);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T_DOT) {
                    {
                        {
                            setState(1947);
                            match(T_DOT);
                            setState(1948);
                            ident();
                        }
                    }
                    setState(1953);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DateLiteralContext extends ParserRuleContext {
        public TerminalNode T_DATE() {
            return getToken(CupidDBSqlParser.T_DATE, 0);
        }

        public StringContext string() {
            return getRuleContext(StringContext.class, 0);
        }

        public DateLiteralContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_dateLiteral;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDateLiteral(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DateLiteralContext dateLiteral() throws RecognitionException {
        DateLiteralContext _localctx = new DateLiteralContext(_ctx, getState());
        enterRule(_localctx, 256, RULE_dateLiteral);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1954);
                match(T_DATE);
                setState(1955);
                string();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class TimestampLiteralContext extends ParserRuleContext {
        public TerminalNode T_TIMESTAMP() {
            return getToken(CupidDBSqlParser.T_TIMESTAMP, 0);
        }

        public StringContext string() {
            return getRuleContext(StringContext.class, 0);
        }

        public TimestampLiteralContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_timestampLiteral;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitTimestampLiteral(this);
            else return visitor.visitChildren(this);
        }
    }

    public final TimestampLiteralContext timestampLiteral() throws RecognitionException {
        TimestampLiteralContext _localctx = new TimestampLiteralContext(_ctx, getState());
        enterRule(_localctx, 258, RULE_timestampLiteral);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1957);
                match(T_TIMESTAMP);
                setState(1958);
                string();
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class IdentContext extends ParserRuleContext {
        public List<IdentItemContext> identItem() {
            return getRuleContexts(IdentItemContext.class);
        }

        public IdentItemContext identItem(int i) {
            return getRuleContext(IdentItemContext.class, i);
        }

        public TerminalNode T_SUB() {
            return getToken(CupidDBSqlParser.T_SUB, 0);
        }

        public List<TerminalNode> T_DOT() {
            return getTokens(CupidDBSqlParser.T_DOT);
        }

        public TerminalNode T_DOT(int i) {
            return getToken(CupidDBSqlParser.T_DOT, i);
        }

        public IdentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ident;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitIdent(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IdentContext ident() throws RecognitionException {
        IdentContext _localctx = new IdentContext(_ctx, getState());
        enterRule(_localctx, 260, RULE_ident);
        int _la;
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                setState(1961);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_SUB) {
                    {
                        setState(1960);
                        match(T_SUB);
                    }
                }

                setState(1963);
                identItem();
                setState(1968);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 241, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        {
                            {
                                setState(1964);
                                match(T_DOT);
                                setState(1965);
                                identItem();
                            }
                        }
                    }
                    setState(1970);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 241, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class IdentItemContext extends ParserRuleContext {
        public TerminalNode L_ID() {
            return getToken(CupidDBSqlParser.L_ID, 0);
        }

        public NonReservedWordsContext nonReservedWords() {
            return getRuleContext(NonReservedWordsContext.class, 0);
        }

        public IdentItemContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_identItem;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitIdentItem(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IdentItemContext identItem() throws RecognitionException {
        IdentItemContext _localctx = new IdentItemContext(_ctx, getState());
        enterRule(_localctx, 262, RULE_identItem);
        try {
            setState(1973);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case L_ID:
                    enterOuterAlt(_localctx, 1); {
                    setState(1971);
                    match(L_ID);
                }
                    break;
                case T_ACTION:
                case T_ADD2:
                case T_ALL:
                case T_ALLOCATE:
                case T_ALTER:
                case T_AND:
                case T_ANSI_NULLS:
                case T_ANSI_PADDING:
                case T_AS:
                case T_ASC:
                case T_ASSOCIATE:
                case T_AT:
                case T_AVG:
                case T_BATCHSIZE:
                case T_BEGIN:
                case T_BETWEEN:
                case T_BIGINT:
                case T_BINARY_DOUBLE:
                case T_BINARY_FLOAT:
                case T_BIT:
                case T_BODY:
                case T_BREAK:
                case T_BY:
                case T_BYTE:
                case T_CALL:
                case T_CALLER:
                case T_CASCADE:
                case T_CASE:
                case T_CASESPECIFIC:
                case T_CAST:
                case T_CHAR:
                case T_CHARACTER:
                case T_CHARSET:
                case T_CLIENT:
                case T_CLOSE:
                case T_CLUSTERED:
                case T_CMP:
                case T_COLLECT:
                case T_COLLECTION:
                case T_COLUMN:
                case T_COMMENT:
                case T_CONSTANT:
                case T_COMMIT:
                case T_COMPRESS:
                case T_CONCAT:
                case T_CONDITION:
                case T_CONSTRAINT:
                case T_CONTINUE:
                case T_COPY:
                case T_COUNT:
                case T_COUNT_BIG:
                case T_CREATION:
                case T_CREATOR:
                case T_CS:
                case T_CURRENT:
                case T_CURRENT_SCHEMA:
                case T_CURSOR:
                case T_DATABASE:
                case T_DATA:
                case T_DATE:
                case T_DATETIME:
                case T_DAY:
                case T_DAYS:
                case T_DEC:
                case T_DECIMAL:
                case T_DECLARE:
                case T_DEFAULT:
                case T_DEFERRED:
                case T_DEFINED:
                case T_DEFINER:
                case T_DEFINITION:
                case T_DELETE:
                case T_DELIMITED:
                case T_DELIMITER:
                case T_DESC:
                case T_DESCRIBE:
                case T_DIAGNOSTICS:
                case T_DIR:
                case T_DIRECTORY:
                case T_DISTINCT:
                case T_DISTRIBUTE:
                case T_DO:
                case T_DOUBLE:
                case T_DOWNLOAD:
                case T_DROP:
                case T_DYNAMIC:
                case T_ENABLE:
                case T_ENGINE:
                case T_ESCAPED:
                case T_EXCEPT:
                case T_EXEC:
                case T_EXECUTE:
                case T_EXCEPTION:
                case T_EXCLUSIVE:
                case T_EXISTS:
                case T_EXIT:
                case T_FALLBACK:
                case T_FALSE:
                case T_FETCH:
                case T_FIELDS:
                case T_FILE:
                case T_FILES:
                case T_FLOAT:
                case T_FOR:
                case T_FOREIGN:
                case T_FORMAT:
                case T_FOUND:
                case T_FROM:
                case T_FULL:
                case T_FUNCTION:
                case T_GET:
                case T_GLOBAL:
                case T_GO:
                case T_GRANT:
                case T_GROUP:
                case T_HANDLER:
                case T_HASH:
                case T_HAVING:
                case T_HOST:
                case T_IDENTITY:
                case T_IF:
                case T_IGNORE:
                case T_IMMEDIATE:
                case T_IN:
                case T_INCLUDE:
                case T_INDEX:
                case T_INITRANS:
                case T_INNER:
                case T_INOUT:
                case T_INSERT:
                case T_INT:
                case T_INT2:
                case T_INT4:
                case T_INT8:
                case T_INTEGER:
                case T_INTERSECT:
                case T_INTERVAL:
                case T_INTO:
                case T_INVOKER:
                case T_IS:
                case T_ISOPEN:
                case T_ITEMS:
                case T_JOIN:
                case T_KEEP:
                case T_KEY:
                case T_KEYS:
                case T_LANGUAGE:
                case T_LEAVE:
                case T_LEFT:
                case T_LIKE:
                case T_LIMIT:
                case T_LINES:
                case T_LOCAL:
                case T_LOCATION:
                case T_LOCATOR:
                case T_LOCATORS:
                case T_LOCKS:
                case T_LOG:
                case T_LOGGED:
                case T_LOGGING:
                case T_LOOP:
                case T_MAP:
                case T_MATCHED:
                case T_MAX:
                case T_MAXTRANS:
                case T_MERGE:
                case T_MESSAGE_TEXT:
                case T_MICROSECOND:
                case T_MICROSECONDS:
                case T_MIN:
                case T_MULTISET:
                case T_NCHAR:
                case T_NEW:
                case T_NVARCHAR:
                case T_NO:
                case T_NOCOUNT:
                case T_NOCOMPRESS:
                case T_NOLOGGING:
                case T_NONE:
                case T_NOT:
                case T_NOTFOUND:
                case T_NUMERIC:
                case T_NUMBER:
                case T_OBJECT:
                case T_OFF:
                case T_ON:
                case T_ONLY:
                case T_OPEN:
                case T_OR:
                case T_ORDER:
                case T_OUT:
                case T_OUTER:
                case T_OVER:
                case T_OVERWRITE:
                case T_OWNER:
                case T_PACKAGE:
                case T_PARTITION:
                case T_PCTFREE:
                case T_PCTUSED:
                case T_PRECISION:
                case T_PRESERVE:
                case T_PRIMARY:
                case T_PRINT:
                case T_PROC:
                case T_PROCEDURE:
                case T_QUALIFY:
                case T_QUERY_BAND:
                case T_QUIT:
                case T_QUOTED_IDENTIFIER:
                case T_QUOTES:
                case T_RAISE:
                case T_REAL:
                case T_REFERENCES:
                case T_REGEXP:
                case T_REPLACE:
                case T_RESIGNAL:
                case T_RESTRICT:
                case T_RESULT:
                case T_RESULT_SET_LOCATOR:
                case T_RETURN:
                case T_RETURNS:
                case T_REVERSE:
                case T_RIGHT:
                case T_RLIKE:
                case T_ROLE:
                case T_ROLLBACK:
                case T_ROW:
                case T_ROWS:
                case T_ROW_COUNT:
                case T_RR:
                case T_RS:
                case T_PWD:
                case T_TRIM:
                case T_SCHEMA:
                case T_SECOND:
                case T_SECONDS:
                case T_SECURITY:
                case T_SEGMENT:
                case T_SEL:
                case T_SELECT:
                case T_SET:
                case T_SESSION:
                case T_SESSIONS:
                case T_SETS:
                case T_SIGNAL:
                case T_SIMPLE_DOUBLE:
                case T_SIMPLE_FLOAT:
                case T_SMALLDATETIME:
                case T_SMALLINT:
                case T_SQL:
                case T_SQLEXCEPTION:
                case T_SQLINSERT:
                case T_SQLSTATE:
                case T_SQLWARNING:
                case T_STATS:
                case T_STATISTICS:
                case T_STEP:
                case T_STORAGE:
                case T_STORE:
                case T_STORED:
                case T_STREAM:
                case T_STRING:
                case T_SUBDIR:
                case T_SUBSTRING:
                case T_SUM:
                case T_SUMMARY:
                case T_SYS_REFCURSOR:
                case T_TABLE:
                case T_TABLESPACE:
                case T_TEMPORARY:
                case T_TERMINATED:
                case T_TEXTIMAGE_ON:
                case T_THEN:
                case T_TIMESTAMP:
                case T_TITLE:
                case T_TO:
                case T_TOP:
                case T_TRANSACTION:
                case T_TRUE:
                case T_TRUNCATE:
                case T_UNIQUE:
                case T_UPDATE:
                case T_UR:
                case T_USE:
                case T_USING:
                case T_VALUE:
                case T_VALUES:
                case T_VAR:
                case T_VARCHAR:
                case T_VARCHAR2:
                case T_VARYING:
                case T_VOLATILE:
                case T_WHILE:
                case T_WITH:
                case T_WITHOUT:
                case T_WORK:
                case T_XACT_ABORT:
                case T_XML:
                case T_YES:
                case T_ACTIVITY_COUNT:
                case T_CUME_DIST:
                case T_CURRENT_DATE:
                case T_CURRENT_TIME:
                case T_PI:
                case T_CURRENT_TIMESTAMP:
                case T_CURRENT_USER:
                case T_DENSE_RANK:
                case T_FIRST_VALUE:
                case T_LAG:
                case T_LAST_VALUE:
                case T_LEAD:
                case T_PART_COUNT:
                case T_PART_LOC:
                case T_RANK:
                case T_ROW_NUMBER:
                case T_STDEV:
                case T_SYSDATE:
                case T_VARIANCE:
                case T_USER:
                    enterOuterAlt(_localctx, 2); {
                    setState(1972);
                    nonReservedWords();
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class StringContext extends ParserRuleContext {
        public StringContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_string;
        }

        public StringContext() {}

        public void copyFrom(StringContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class SingleQuotedStringContext extends StringContext {
        public TerminalNode L_S_STRING() {
            return getToken(CupidDBSqlParser.L_S_STRING, 0);
        }

        public SingleQuotedStringContext(StringContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitSingleQuotedString(this);
            else return visitor.visitChildren(this);
        }
    }

    public static class DoubleQuotedStringContext extends StringContext {
        public TerminalNode L_D_STRING() {
            return getToken(CupidDBSqlParser.L_D_STRING, 0);
        }

        public DoubleQuotedStringContext(StringContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDoubleQuotedString(this);
            else return visitor.visitChildren(this);
        }
    }

    public final StringContext string() throws RecognitionException {
        StringContext _localctx = new StringContext(_ctx, getState());
        enterRule(_localctx, 264, RULE_string);
        try {
            setState(1977);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case L_S_STRING:
                    _localctx = new SingleQuotedStringContext(_localctx);
                    enterOuterAlt(_localctx, 1); {
                    setState(1975);
                    match(L_S_STRING);
                }
                    break;
                case L_D_STRING:
                    _localctx = new DoubleQuotedStringContext(_localctx);
                    enterOuterAlt(_localctx, 2); {
                    setState(1976);
                    match(L_D_STRING);
                }
                    break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class IntNumberContext extends ParserRuleContext {
        public TerminalNode L_INT() {
            return getToken(CupidDBSqlParser.L_INT, 0);
        }

        public TerminalNode T_SUB() {
            return getToken(CupidDBSqlParser.T_SUB, 0);
        }

        public TerminalNode T_ADD() {
            return getToken(CupidDBSqlParser.T_ADD, 0);
        }

        public IntNumberContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_intNumber;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitIntNumber(this);
            else return visitor.visitChildren(this);
        }
    }

    public final IntNumberContext intNumber() throws RecognitionException {
        IntNumberContext _localctx = new IntNumberContext(_ctx, getState());
        enterRule(_localctx, 266, RULE_intNumber);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1980);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_ADD || _la == T_SUB) {
                    {
                        setState(1979);
                        _la = _input.LA(1);
                        if (!(_la == T_ADD || _la == T_SUB)) {
                            _errHandler.recoverInline(this);
                        } else {
                            if (_input.LA(1) == Token.EOF) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                    }
                }

                setState(1982);
                match(L_INT);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DecNumberContext extends ParserRuleContext {
        public TerminalNode L_DEC() {
            return getToken(CupidDBSqlParser.L_DEC, 0);
        }

        public TerminalNode T_SUB() {
            return getToken(CupidDBSqlParser.T_SUB, 0);
        }

        public TerminalNode T_ADD() {
            return getToken(CupidDBSqlParser.T_ADD, 0);
        }

        public DecNumberContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_decNumber;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitDecNumber(this);
            else return visitor.visitChildren(this);
        }
    }

    public final DecNumberContext decNumber() throws RecognitionException {
        DecNumberContext _localctx = new DecNumberContext(_ctx, getState());
        enterRule(_localctx, 268, RULE_decNumber);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1985);
                _errHandler.sync(this);
                _la = _input.LA(1);
                if (_la == T_ADD || _la == T_SUB) {
                    {
                        setState(1984);
                        _la = _input.LA(1);
                        if (!(_la == T_ADD || _la == T_SUB)) {
                            _errHandler.recoverInline(this);
                        } else {
                            if (_input.LA(1) == Token.EOF) matchedEOF = true;
                            _errHandler.reportMatch(this);
                            consume();
                        }
                    }
                }

                setState(1987);
                match(L_DEC);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class BoolLiteralContext extends ParserRuleContext {
        public TerminalNode T_TRUE() {
            return getToken(CupidDBSqlParser.T_TRUE, 0);
        }

        public TerminalNode T_FALSE() {
            return getToken(CupidDBSqlParser.T_FALSE, 0);
        }

        public BoolLiteralContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_boolLiteral;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitBoolLiteral(this);
            else return visitor.visitChildren(this);
        }
    }

    public final BoolLiteralContext boolLiteral() throws RecognitionException {
        BoolLiteralContext _localctx = new BoolLiteralContext(_ctx, getState());
        enterRule(_localctx, 270, RULE_boolLiteral);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1989);
                _la = _input.LA(1);
                if (!(_la == T_FALSE || _la == T_TRUE)) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class NullConstContext extends ParserRuleContext {
        public TerminalNode T_NULL() {
            return getToken(CupidDBSqlParser.T_NULL, 0);
        }

        public NullConstContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_nullConst;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitNullConst(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NullConstContext nullConst() throws RecognitionException {
        NullConstContext _localctx = new NullConstContext(_ctx, getState());
        enterRule(_localctx, 272, RULE_nullConst);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1991);
                match(T_NULL);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class NonReservedWordsContext extends ParserRuleContext {
        public TerminalNode T_ACTION() {
            return getToken(CupidDBSqlParser.T_ACTION, 0);
        }

        public TerminalNode T_ACTIVITY_COUNT() {
            return getToken(CupidDBSqlParser.T_ACTIVITY_COUNT, 0);
        }

        public TerminalNode T_ADD2() {
            return getToken(CupidDBSqlParser.T_ADD2, 0);
        }

        public TerminalNode T_ALL() {
            return getToken(CupidDBSqlParser.T_ALL, 0);
        }

        public TerminalNode T_ALLOCATE() {
            return getToken(CupidDBSqlParser.T_ALLOCATE, 0);
        }

        public TerminalNode T_ALTER() {
            return getToken(CupidDBSqlParser.T_ALTER, 0);
        }

        public TerminalNode T_AND() {
            return getToken(CupidDBSqlParser.T_AND, 0);
        }

        public TerminalNode T_ANSI_NULLS() {
            return getToken(CupidDBSqlParser.T_ANSI_NULLS, 0);
        }

        public TerminalNode T_ANSI_PADDING() {
            return getToken(CupidDBSqlParser.T_ANSI_PADDING, 0);
        }

        public TerminalNode T_AS() {
            return getToken(CupidDBSqlParser.T_AS, 0);
        }

        public TerminalNode T_ASC() {
            return getToken(CupidDBSqlParser.T_ASC, 0);
        }

        public TerminalNode T_ASSOCIATE() {
            return getToken(CupidDBSqlParser.T_ASSOCIATE, 0);
        }

        public TerminalNode T_AT() {
            return getToken(CupidDBSqlParser.T_AT, 0);
        }

        public TerminalNode T_AVG() {
            return getToken(CupidDBSqlParser.T_AVG, 0);
        }

        public TerminalNode T_BATCHSIZE() {
            return getToken(CupidDBSqlParser.T_BATCHSIZE, 0);
        }

        public TerminalNode T_BEGIN() {
            return getToken(CupidDBSqlParser.T_BEGIN, 0);
        }

        public TerminalNode T_BETWEEN() {
            return getToken(CupidDBSqlParser.T_BETWEEN, 0);
        }

        public TerminalNode T_BIGINT() {
            return getToken(CupidDBSqlParser.T_BIGINT, 0);
        }

        public TerminalNode T_BINARY_DOUBLE() {
            return getToken(CupidDBSqlParser.T_BINARY_DOUBLE, 0);
        }

        public TerminalNode T_BINARY_FLOAT() {
            return getToken(CupidDBSqlParser.T_BINARY_FLOAT, 0);
        }

        public TerminalNode T_BIT() {
            return getToken(CupidDBSqlParser.T_BIT, 0);
        }

        public TerminalNode T_BODY() {
            return getToken(CupidDBSqlParser.T_BODY, 0);
        }

        public TerminalNode T_BREAK() {
            return getToken(CupidDBSqlParser.T_BREAK, 0);
        }

        public TerminalNode T_BY() {
            return getToken(CupidDBSqlParser.T_BY, 0);
        }

        public TerminalNode T_BYTE() {
            return getToken(CupidDBSqlParser.T_BYTE, 0);
        }

        public TerminalNode T_CALL() {
            return getToken(CupidDBSqlParser.T_CALL, 0);
        }

        public TerminalNode T_CALLER() {
            return getToken(CupidDBSqlParser.T_CALLER, 0);
        }

        public TerminalNode T_CASCADE() {
            return getToken(CupidDBSqlParser.T_CASCADE, 0);
        }

        public TerminalNode T_CASE() {
            return getToken(CupidDBSqlParser.T_CASE, 0);
        }

        public TerminalNode T_CASESPECIFIC() {
            return getToken(CupidDBSqlParser.T_CASESPECIFIC, 0);
        }

        public TerminalNode T_CAST() {
            return getToken(CupidDBSqlParser.T_CAST, 0);
        }

        public TerminalNode T_CHAR() {
            return getToken(CupidDBSqlParser.T_CHAR, 0);
        }

        public TerminalNode T_CHARACTER() {
            return getToken(CupidDBSqlParser.T_CHARACTER, 0);
        }

        public TerminalNode T_CHARSET() {
            return getToken(CupidDBSqlParser.T_CHARSET, 0);
        }

        public TerminalNode T_CLIENT() {
            return getToken(CupidDBSqlParser.T_CLIENT, 0);
        }

        public TerminalNode T_CLOSE() {
            return getToken(CupidDBSqlParser.T_CLOSE, 0);
        }

        public TerminalNode T_CLUSTERED() {
            return getToken(CupidDBSqlParser.T_CLUSTERED, 0);
        }

        public TerminalNode T_CMP() {
            return getToken(CupidDBSqlParser.T_CMP, 0);
        }

        public TerminalNode T_COLLECT() {
            return getToken(CupidDBSqlParser.T_COLLECT, 0);
        }

        public TerminalNode T_COLLECTION() {
            return getToken(CupidDBSqlParser.T_COLLECTION, 0);
        }

        public TerminalNode T_COLUMN() {
            return getToken(CupidDBSqlParser.T_COLUMN, 0);
        }

        public TerminalNode T_COMMENT() {
            return getToken(CupidDBSqlParser.T_COMMENT, 0);
        }

        public TerminalNode T_COMPRESS() {
            return getToken(CupidDBSqlParser.T_COMPRESS, 0);
        }

        public TerminalNode T_CONSTANT() {
            return getToken(CupidDBSqlParser.T_CONSTANT, 0);
        }

        public TerminalNode T_COPY() {
            return getToken(CupidDBSqlParser.T_COPY, 0);
        }

        public TerminalNode T_COMMIT() {
            return getToken(CupidDBSqlParser.T_COMMIT, 0);
        }

        public TerminalNode T_CONCAT() {
            return getToken(CupidDBSqlParser.T_CONCAT, 0);
        }

        public TerminalNode T_CONDITION() {
            return getToken(CupidDBSqlParser.T_CONDITION, 0);
        }

        public TerminalNode T_CONSTRAINT() {
            return getToken(CupidDBSqlParser.T_CONSTRAINT, 0);
        }

        public TerminalNode T_CONTINUE() {
            return getToken(CupidDBSqlParser.T_CONTINUE, 0);
        }

        public TerminalNode T_COUNT() {
            return getToken(CupidDBSqlParser.T_COUNT, 0);
        }

        public TerminalNode T_COUNT_BIG() {
            return getToken(CupidDBSqlParser.T_COUNT_BIG, 0);
        }

        public TerminalNode T_CREATION() {
            return getToken(CupidDBSqlParser.T_CREATION, 0);
        }

        public TerminalNode T_CREATOR() {
            return getToken(CupidDBSqlParser.T_CREATOR, 0);
        }

        public TerminalNode T_CS() {
            return getToken(CupidDBSqlParser.T_CS, 0);
        }

        public TerminalNode T_CUME_DIST() {
            return getToken(CupidDBSqlParser.T_CUME_DIST, 0);
        }

        public TerminalNode T_CURRENT() {
            return getToken(CupidDBSqlParser.T_CURRENT, 0);
        }

        public TerminalNode T_CURRENT_DATE() {
            return getToken(CupidDBSqlParser.T_CURRENT_DATE, 0);
        }

        public TerminalNode T_CURRENT_TIME() {
            return getToken(CupidDBSqlParser.T_CURRENT_TIME, 0);
        }

        public TerminalNode T_PI() {
            return getToken(CupidDBSqlParser.T_PI, 0);
        }

        public TerminalNode T_CURRENT_SCHEMA() {
            return getToken(CupidDBSqlParser.T_CURRENT_SCHEMA, 0);
        }

        public TerminalNode T_CURRENT_TIMESTAMP() {
            return getToken(CupidDBSqlParser.T_CURRENT_TIMESTAMP, 0);
        }

        public TerminalNode T_CURRENT_USER() {
            return getToken(CupidDBSqlParser.T_CURRENT_USER, 0);
        }

        public TerminalNode T_CURSOR() {
            return getToken(CupidDBSqlParser.T_CURSOR, 0);
        }

        public TerminalNode T_DATA() {
            return getToken(CupidDBSqlParser.T_DATA, 0);
        }

        public TerminalNode T_DATABASE() {
            return getToken(CupidDBSqlParser.T_DATABASE, 0);
        }

        public TerminalNode T_DATE() {
            return getToken(CupidDBSqlParser.T_DATE, 0);
        }

        public TerminalNode T_DATETIME() {
            return getToken(CupidDBSqlParser.T_DATETIME, 0);
        }

        public TerminalNode T_DAY() {
            return getToken(CupidDBSqlParser.T_DAY, 0);
        }

        public TerminalNode T_DAYS() {
            return getToken(CupidDBSqlParser.T_DAYS, 0);
        }

        public TerminalNode T_DEC() {
            return getToken(CupidDBSqlParser.T_DEC, 0);
        }

        public TerminalNode T_DECIMAL() {
            return getToken(CupidDBSqlParser.T_DECIMAL, 0);
        }

        public TerminalNode T_DECLARE() {
            return getToken(CupidDBSqlParser.T_DECLARE, 0);
        }

        public TerminalNode T_DEFAULT() {
            return getToken(CupidDBSqlParser.T_DEFAULT, 0);
        }

        public TerminalNode T_DEFERRED() {
            return getToken(CupidDBSqlParser.T_DEFERRED, 0);
        }

        public TerminalNode T_DEFINED() {
            return getToken(CupidDBSqlParser.T_DEFINED, 0);
        }

        public TerminalNode T_DEFINER() {
            return getToken(CupidDBSqlParser.T_DEFINER, 0);
        }

        public TerminalNode T_DEFINITION() {
            return getToken(CupidDBSqlParser.T_DEFINITION, 0);
        }

        public TerminalNode T_DELETE() {
            return getToken(CupidDBSqlParser.T_DELETE, 0);
        }

        public TerminalNode T_DELIMITED() {
            return getToken(CupidDBSqlParser.T_DELIMITED, 0);
        }

        public TerminalNode T_DELIMITER() {
            return getToken(CupidDBSqlParser.T_DELIMITER, 0);
        }

        public TerminalNode T_DENSE_RANK() {
            return getToken(CupidDBSqlParser.T_DENSE_RANK, 0);
        }

        public TerminalNode T_DESC() {
            return getToken(CupidDBSqlParser.T_DESC, 0);
        }

        public TerminalNode T_DESCRIBE() {
            return getToken(CupidDBSqlParser.T_DESCRIBE, 0);
        }

        public TerminalNode T_DIAGNOSTICS() {
            return getToken(CupidDBSqlParser.T_DIAGNOSTICS, 0);
        }

        public TerminalNode T_DIR() {
            return getToken(CupidDBSqlParser.T_DIR, 0);
        }

        public TerminalNode T_DIRECTORY() {
            return getToken(CupidDBSqlParser.T_DIRECTORY, 0);
        }

        public TerminalNode T_DISTINCT() {
            return getToken(CupidDBSqlParser.T_DISTINCT, 0);
        }

        public TerminalNode T_DISTRIBUTE() {
            return getToken(CupidDBSqlParser.T_DISTRIBUTE, 0);
        }

        public TerminalNode T_DO() {
            return getToken(CupidDBSqlParser.T_DO, 0);
        }

        public TerminalNode T_DOUBLE() {
            return getToken(CupidDBSqlParser.T_DOUBLE, 0);
        }

        public TerminalNode T_DOWNLOAD() {
            return getToken(CupidDBSqlParser.T_DOWNLOAD, 0);
        }

        public TerminalNode T_DROP() {
            return getToken(CupidDBSqlParser.T_DROP, 0);
        }

        public TerminalNode T_DYNAMIC() {
            return getToken(CupidDBSqlParser.T_DYNAMIC, 0);
        }

        public TerminalNode T_ENABLE() {
            return getToken(CupidDBSqlParser.T_ENABLE, 0);
        }

        public TerminalNode T_ENGINE() {
            return getToken(CupidDBSqlParser.T_ENGINE, 0);
        }

        public TerminalNode T_ESCAPED() {
            return getToken(CupidDBSqlParser.T_ESCAPED, 0);
        }

        public TerminalNode T_EXCEPT() {
            return getToken(CupidDBSqlParser.T_EXCEPT, 0);
        }

        public TerminalNode T_EXEC() {
            return getToken(CupidDBSqlParser.T_EXEC, 0);
        }

        public TerminalNode T_EXECUTE() {
            return getToken(CupidDBSqlParser.T_EXECUTE, 0);
        }

        public TerminalNode T_EXCEPTION() {
            return getToken(CupidDBSqlParser.T_EXCEPTION, 0);
        }

        public TerminalNode T_EXCLUSIVE() {
            return getToken(CupidDBSqlParser.T_EXCLUSIVE, 0);
        }

        public TerminalNode T_EXISTS() {
            return getToken(CupidDBSqlParser.T_EXISTS, 0);
        }

        public TerminalNode T_EXIT() {
            return getToken(CupidDBSqlParser.T_EXIT, 0);
        }

        public TerminalNode T_FALLBACK() {
            return getToken(CupidDBSqlParser.T_FALLBACK, 0);
        }

        public TerminalNode T_FALSE() {
            return getToken(CupidDBSqlParser.T_FALSE, 0);
        }

        public TerminalNode T_FETCH() {
            return getToken(CupidDBSqlParser.T_FETCH, 0);
        }

        public TerminalNode T_FIELDS() {
            return getToken(CupidDBSqlParser.T_FIELDS, 0);
        }

        public TerminalNode T_FILE() {
            return getToken(CupidDBSqlParser.T_FILE, 0);
        }

        public TerminalNode T_FILES() {
            return getToken(CupidDBSqlParser.T_FILES, 0);
        }

        public TerminalNode T_FIRST_VALUE() {
            return getToken(CupidDBSqlParser.T_FIRST_VALUE, 0);
        }

        public TerminalNode T_FLOAT() {
            return getToken(CupidDBSqlParser.T_FLOAT, 0);
        }

        public TerminalNode T_FOR() {
            return getToken(CupidDBSqlParser.T_FOR, 0);
        }

        public TerminalNode T_FOREIGN() {
            return getToken(CupidDBSqlParser.T_FOREIGN, 0);
        }

        public TerminalNode T_FORMAT() {
            return getToken(CupidDBSqlParser.T_FORMAT, 0);
        }

        public TerminalNode T_FOUND() {
            return getToken(CupidDBSqlParser.T_FOUND, 0);
        }

        public TerminalNode T_FROM() {
            return getToken(CupidDBSqlParser.T_FROM, 0);
        }

        public TerminalNode T_FULL() {
            return getToken(CupidDBSqlParser.T_FULL, 0);
        }

        public TerminalNode T_FUNCTION() {
            return getToken(CupidDBSqlParser.T_FUNCTION, 0);
        }

        public TerminalNode T_GET() {
            return getToken(CupidDBSqlParser.T_GET, 0);
        }

        public TerminalNode T_GLOBAL() {
            return getToken(CupidDBSqlParser.T_GLOBAL, 0);
        }

        public TerminalNode T_GO() {
            return getToken(CupidDBSqlParser.T_GO, 0);
        }

        public TerminalNode T_GRANT() {
            return getToken(CupidDBSqlParser.T_GRANT, 0);
        }

        public TerminalNode T_GROUP() {
            return getToken(CupidDBSqlParser.T_GROUP, 0);
        }

        public TerminalNode T_HANDLER() {
            return getToken(CupidDBSqlParser.T_HANDLER, 0);
        }

        public TerminalNode T_HASH() {
            return getToken(CupidDBSqlParser.T_HASH, 0);
        }

        public TerminalNode T_HAVING() {
            return getToken(CupidDBSqlParser.T_HAVING, 0);
        }

        public TerminalNode T_HOST() {
            return getToken(CupidDBSqlParser.T_HOST, 0);
        }

        public TerminalNode T_IDENTITY() {
            return getToken(CupidDBSqlParser.T_IDENTITY, 0);
        }

        public TerminalNode T_IF() {
            return getToken(CupidDBSqlParser.T_IF, 0);
        }

        public TerminalNode T_IGNORE() {
            return getToken(CupidDBSqlParser.T_IGNORE, 0);
        }

        public TerminalNode T_IMMEDIATE() {
            return getToken(CupidDBSqlParser.T_IMMEDIATE, 0);
        }

        public TerminalNode T_IN() {
            return getToken(CupidDBSqlParser.T_IN, 0);
        }

        public TerminalNode T_INCLUDE() {
            return getToken(CupidDBSqlParser.T_INCLUDE, 0);
        }

        public TerminalNode T_INDEX() {
            return getToken(CupidDBSqlParser.T_INDEX, 0);
        }

        public TerminalNode T_INITRANS() {
            return getToken(CupidDBSqlParser.T_INITRANS, 0);
        }

        public TerminalNode T_INNER() {
            return getToken(CupidDBSqlParser.T_INNER, 0);
        }

        public TerminalNode T_INOUT() {
            return getToken(CupidDBSqlParser.T_INOUT, 0);
        }

        public TerminalNode T_INSERT() {
            return getToken(CupidDBSqlParser.T_INSERT, 0);
        }

        public TerminalNode T_INT() {
            return getToken(CupidDBSqlParser.T_INT, 0);
        }

        public TerminalNode T_INT2() {
            return getToken(CupidDBSqlParser.T_INT2, 0);
        }

        public TerminalNode T_INT4() {
            return getToken(CupidDBSqlParser.T_INT4, 0);
        }

        public TerminalNode T_INT8() {
            return getToken(CupidDBSqlParser.T_INT8, 0);
        }

        public TerminalNode T_INTEGER() {
            return getToken(CupidDBSqlParser.T_INTEGER, 0);
        }

        public TerminalNode T_INTERSECT() {
            return getToken(CupidDBSqlParser.T_INTERSECT, 0);
        }

        public TerminalNode T_INTERVAL() {
            return getToken(CupidDBSqlParser.T_INTERVAL, 0);
        }

        public TerminalNode T_INTO() {
            return getToken(CupidDBSqlParser.T_INTO, 0);
        }

        public TerminalNode T_INVOKER() {
            return getToken(CupidDBSqlParser.T_INVOKER, 0);
        }

        public TerminalNode T_ITEMS() {
            return getToken(CupidDBSqlParser.T_ITEMS, 0);
        }

        public TerminalNode T_IS() {
            return getToken(CupidDBSqlParser.T_IS, 0);
        }

        public TerminalNode T_ISOPEN() {
            return getToken(CupidDBSqlParser.T_ISOPEN, 0);
        }

        public TerminalNode T_JOIN() {
            return getToken(CupidDBSqlParser.T_JOIN, 0);
        }

        public TerminalNode T_KEEP() {
            return getToken(CupidDBSqlParser.T_KEEP, 0);
        }

        public TerminalNode T_KEY() {
            return getToken(CupidDBSqlParser.T_KEY, 0);
        }

        public TerminalNode T_KEYS() {
            return getToken(CupidDBSqlParser.T_KEYS, 0);
        }

        public TerminalNode T_LAG() {
            return getToken(CupidDBSqlParser.T_LAG, 0);
        }

        public TerminalNode T_LANGUAGE() {
            return getToken(CupidDBSqlParser.T_LANGUAGE, 0);
        }

        public TerminalNode T_LAST_VALUE() {
            return getToken(CupidDBSqlParser.T_LAST_VALUE, 0);
        }

        public TerminalNode T_LEAD() {
            return getToken(CupidDBSqlParser.T_LEAD, 0);
        }

        public TerminalNode T_LEAVE() {
            return getToken(CupidDBSqlParser.T_LEAVE, 0);
        }

        public TerminalNode T_LEFT() {
            return getToken(CupidDBSqlParser.T_LEFT, 0);
        }

        public TerminalNode T_LIKE() {
            return getToken(CupidDBSqlParser.T_LIKE, 0);
        }

        public TerminalNode T_LIMIT() {
            return getToken(CupidDBSqlParser.T_LIMIT, 0);
        }

        public TerminalNode T_LINES() {
            return getToken(CupidDBSqlParser.T_LINES, 0);
        }

        public TerminalNode T_LOCAL() {
            return getToken(CupidDBSqlParser.T_LOCAL, 0);
        }

        public TerminalNode T_LOCATION() {
            return getToken(CupidDBSqlParser.T_LOCATION, 0);
        }

        public TerminalNode T_LOCATOR() {
            return getToken(CupidDBSqlParser.T_LOCATOR, 0);
        }

        public TerminalNode T_LOCATORS() {
            return getToken(CupidDBSqlParser.T_LOCATORS, 0);
        }

        public TerminalNode T_LOCKS() {
            return getToken(CupidDBSqlParser.T_LOCKS, 0);
        }

        public TerminalNode T_LOG() {
            return getToken(CupidDBSqlParser.T_LOG, 0);
        }

        public TerminalNode T_LOGGED() {
            return getToken(CupidDBSqlParser.T_LOGGED, 0);
        }

        public TerminalNode T_LOGGING() {
            return getToken(CupidDBSqlParser.T_LOGGING, 0);
        }

        public TerminalNode T_LOOP() {
            return getToken(CupidDBSqlParser.T_LOOP, 0);
        }

        public TerminalNode T_MAP() {
            return getToken(CupidDBSqlParser.T_MAP, 0);
        }

        public TerminalNode T_MATCHED() {
            return getToken(CupidDBSqlParser.T_MATCHED, 0);
        }

        public TerminalNode T_MAX() {
            return getToken(CupidDBSqlParser.T_MAX, 0);
        }

        public TerminalNode T_MAXTRANS() {
            return getToken(CupidDBSqlParser.T_MAXTRANS, 0);
        }

        public TerminalNode T_MERGE() {
            return getToken(CupidDBSqlParser.T_MERGE, 0);
        }

        public TerminalNode T_MESSAGE_TEXT() {
            return getToken(CupidDBSqlParser.T_MESSAGE_TEXT, 0);
        }

        public TerminalNode T_MICROSECOND() {
            return getToken(CupidDBSqlParser.T_MICROSECOND, 0);
        }

        public TerminalNode T_MICROSECONDS() {
            return getToken(CupidDBSqlParser.T_MICROSECONDS, 0);
        }

        public TerminalNode T_MIN() {
            return getToken(CupidDBSqlParser.T_MIN, 0);
        }

        public TerminalNode T_MULTISET() {
            return getToken(CupidDBSqlParser.T_MULTISET, 0);
        }

        public TerminalNode T_NCHAR() {
            return getToken(CupidDBSqlParser.T_NCHAR, 0);
        }

        public TerminalNode T_NEW() {
            return getToken(CupidDBSqlParser.T_NEW, 0);
        }

        public TerminalNode T_NVARCHAR() {
            return getToken(CupidDBSqlParser.T_NVARCHAR, 0);
        }

        public TerminalNode T_NO() {
            return getToken(CupidDBSqlParser.T_NO, 0);
        }

        public TerminalNode T_NOCOMPRESS() {
            return getToken(CupidDBSqlParser.T_NOCOMPRESS, 0);
        }

        public TerminalNode T_NOCOUNT() {
            return getToken(CupidDBSqlParser.T_NOCOUNT, 0);
        }

        public TerminalNode T_NOLOGGING() {
            return getToken(CupidDBSqlParser.T_NOLOGGING, 0);
        }

        public TerminalNode T_NONE() {
            return getToken(CupidDBSqlParser.T_NONE, 0);
        }

        public TerminalNode T_NOT() {
            return getToken(CupidDBSqlParser.T_NOT, 0);
        }

        public TerminalNode T_NOTFOUND() {
            return getToken(CupidDBSqlParser.T_NOTFOUND, 0);
        }

        public TerminalNode T_NUMERIC() {
            return getToken(CupidDBSqlParser.T_NUMERIC, 0);
        }

        public TerminalNode T_NUMBER() {
            return getToken(CupidDBSqlParser.T_NUMBER, 0);
        }

        public TerminalNode T_OBJECT() {
            return getToken(CupidDBSqlParser.T_OBJECT, 0);
        }

        public TerminalNode T_OFF() {
            return getToken(CupidDBSqlParser.T_OFF, 0);
        }

        public TerminalNode T_ON() {
            return getToken(CupidDBSqlParser.T_ON, 0);
        }

        public TerminalNode T_ONLY() {
            return getToken(CupidDBSqlParser.T_ONLY, 0);
        }

        public TerminalNode T_OPEN() {
            return getToken(CupidDBSqlParser.T_OPEN, 0);
        }

        public TerminalNode T_OR() {
            return getToken(CupidDBSqlParser.T_OR, 0);
        }

        public TerminalNode T_ORDER() {
            return getToken(CupidDBSqlParser.T_ORDER, 0);
        }

        public TerminalNode T_OUT() {
            return getToken(CupidDBSqlParser.T_OUT, 0);
        }

        public TerminalNode T_OUTER() {
            return getToken(CupidDBSqlParser.T_OUTER, 0);
        }

        public TerminalNode T_OVER() {
            return getToken(CupidDBSqlParser.T_OVER, 0);
        }

        public TerminalNode T_OVERWRITE() {
            return getToken(CupidDBSqlParser.T_OVERWRITE, 0);
        }

        public TerminalNode T_OWNER() {
            return getToken(CupidDBSqlParser.T_OWNER, 0);
        }

        public TerminalNode T_PACKAGE() {
            return getToken(CupidDBSqlParser.T_PACKAGE, 0);
        }

        public TerminalNode T_PART_COUNT() {
            return getToken(CupidDBSqlParser.T_PART_COUNT, 0);
        }

        public TerminalNode T_PART_LOC() {
            return getToken(CupidDBSqlParser.T_PART_LOC, 0);
        }

        public TerminalNode T_PARTITION() {
            return getToken(CupidDBSqlParser.T_PARTITION, 0);
        }

        public TerminalNode T_PCTFREE() {
            return getToken(CupidDBSqlParser.T_PCTFREE, 0);
        }

        public TerminalNode T_PCTUSED() {
            return getToken(CupidDBSqlParser.T_PCTUSED, 0);
        }

        public TerminalNode T_PRECISION() {
            return getToken(CupidDBSqlParser.T_PRECISION, 0);
        }

        public TerminalNode T_PRESERVE() {
            return getToken(CupidDBSqlParser.T_PRESERVE, 0);
        }

        public TerminalNode T_PRIMARY() {
            return getToken(CupidDBSqlParser.T_PRIMARY, 0);
        }

        public TerminalNode T_PRINT() {
            return getToken(CupidDBSqlParser.T_PRINT, 0);
        }

        public TerminalNode T_PROC() {
            return getToken(CupidDBSqlParser.T_PROC, 0);
        }

        public TerminalNode T_PROCEDURE() {
            return getToken(CupidDBSqlParser.T_PROCEDURE, 0);
        }

        public TerminalNode T_PWD() {
            return getToken(CupidDBSqlParser.T_PWD, 0);
        }

        public TerminalNode T_QUALIFY() {
            return getToken(CupidDBSqlParser.T_QUALIFY, 0);
        }

        public TerminalNode T_QUERY_BAND() {
            return getToken(CupidDBSqlParser.T_QUERY_BAND, 0);
        }

        public TerminalNode T_QUIT() {
            return getToken(CupidDBSqlParser.T_QUIT, 0);
        }

        public TerminalNode T_QUOTED_IDENTIFIER() {
            return getToken(CupidDBSqlParser.T_QUOTED_IDENTIFIER, 0);
        }

        public TerminalNode T_QUOTES() {
            return getToken(CupidDBSqlParser.T_QUOTES, 0);
        }

        public TerminalNode T_RAISE() {
            return getToken(CupidDBSqlParser.T_RAISE, 0);
        }

        public TerminalNode T_RANK() {
            return getToken(CupidDBSqlParser.T_RANK, 0);
        }

        public TerminalNode T_REAL() {
            return getToken(CupidDBSqlParser.T_REAL, 0);
        }

        public TerminalNode T_REFERENCES() {
            return getToken(CupidDBSqlParser.T_REFERENCES, 0);
        }

        public TerminalNode T_REGEXP() {
            return getToken(CupidDBSqlParser.T_REGEXP, 0);
        }

        public TerminalNode T_RR() {
            return getToken(CupidDBSqlParser.T_RR, 0);
        }

        public TerminalNode T_REPLACE() {
            return getToken(CupidDBSqlParser.T_REPLACE, 0);
        }

        public TerminalNode T_RESIGNAL() {
            return getToken(CupidDBSqlParser.T_RESIGNAL, 0);
        }

        public TerminalNode T_RESTRICT() {
            return getToken(CupidDBSqlParser.T_RESTRICT, 0);
        }

        public TerminalNode T_RESULT() {
            return getToken(CupidDBSqlParser.T_RESULT, 0);
        }

        public TerminalNode T_RESULT_SET_LOCATOR() {
            return getToken(CupidDBSqlParser.T_RESULT_SET_LOCATOR, 0);
        }

        public TerminalNode T_RETURN() {
            return getToken(CupidDBSqlParser.T_RETURN, 0);
        }

        public TerminalNode T_RETURNS() {
            return getToken(CupidDBSqlParser.T_RETURNS, 0);
        }

        public TerminalNode T_REVERSE() {
            return getToken(CupidDBSqlParser.T_REVERSE, 0);
        }

        public TerminalNode T_RIGHT() {
            return getToken(CupidDBSqlParser.T_RIGHT, 0);
        }

        public TerminalNode T_RLIKE() {
            return getToken(CupidDBSqlParser.T_RLIKE, 0);
        }

        public TerminalNode T_RS() {
            return getToken(CupidDBSqlParser.T_RS, 0);
        }

        public TerminalNode T_ROLE() {
            return getToken(CupidDBSqlParser.T_ROLE, 0);
        }

        public TerminalNode T_ROLLBACK() {
            return getToken(CupidDBSqlParser.T_ROLLBACK, 0);
        }

        public TerminalNode T_ROW() {
            return getToken(CupidDBSqlParser.T_ROW, 0);
        }

        public TerminalNode T_ROWS() {
            return getToken(CupidDBSqlParser.T_ROWS, 0);
        }

        public TerminalNode T_ROW_COUNT() {
            return getToken(CupidDBSqlParser.T_ROW_COUNT, 0);
        }

        public TerminalNode T_ROW_NUMBER() {
            return getToken(CupidDBSqlParser.T_ROW_NUMBER, 0);
        }

        public TerminalNode T_SCHEMA() {
            return getToken(CupidDBSqlParser.T_SCHEMA, 0);
        }

        public TerminalNode T_SECOND() {
            return getToken(CupidDBSqlParser.T_SECOND, 0);
        }

        public TerminalNode T_SECONDS() {
            return getToken(CupidDBSqlParser.T_SECONDS, 0);
        }

        public TerminalNode T_SECURITY() {
            return getToken(CupidDBSqlParser.T_SECURITY, 0);
        }

        public TerminalNode T_SEGMENT() {
            return getToken(CupidDBSqlParser.T_SEGMENT, 0);
        }

        public TerminalNode T_SEL() {
            return getToken(CupidDBSqlParser.T_SEL, 0);
        }

        public TerminalNode T_SELECT() {
            return getToken(CupidDBSqlParser.T_SELECT, 0);
        }

        public TerminalNode T_SESSION() {
            return getToken(CupidDBSqlParser.T_SESSION, 0);
        }

        public TerminalNode T_SESSIONS() {
            return getToken(CupidDBSqlParser.T_SESSIONS, 0);
        }

        public TerminalNode T_SET() {
            return getToken(CupidDBSqlParser.T_SET, 0);
        }

        public TerminalNode T_SETS() {
            return getToken(CupidDBSqlParser.T_SETS, 0);
        }

        public TerminalNode T_SIGNAL() {
            return getToken(CupidDBSqlParser.T_SIGNAL, 0);
        }

        public TerminalNode T_SIMPLE_DOUBLE() {
            return getToken(CupidDBSqlParser.T_SIMPLE_DOUBLE, 0);
        }

        public TerminalNode T_SIMPLE_FLOAT() {
            return getToken(CupidDBSqlParser.T_SIMPLE_FLOAT, 0);
        }

        public TerminalNode T_SMALLDATETIME() {
            return getToken(CupidDBSqlParser.T_SMALLDATETIME, 0);
        }

        public TerminalNode T_SMALLINT() {
            return getToken(CupidDBSqlParser.T_SMALLINT, 0);
        }

        public TerminalNode T_SQL() {
            return getToken(CupidDBSqlParser.T_SQL, 0);
        }

        public TerminalNode T_SQLEXCEPTION() {
            return getToken(CupidDBSqlParser.T_SQLEXCEPTION, 0);
        }

        public TerminalNode T_SQLINSERT() {
            return getToken(CupidDBSqlParser.T_SQLINSERT, 0);
        }

        public TerminalNode T_SQLSTATE() {
            return getToken(CupidDBSqlParser.T_SQLSTATE, 0);
        }

        public TerminalNode T_SQLWARNING() {
            return getToken(CupidDBSqlParser.T_SQLWARNING, 0);
        }

        public TerminalNode T_STATS() {
            return getToken(CupidDBSqlParser.T_STATS, 0);
        }

        public TerminalNode T_STATISTICS() {
            return getToken(CupidDBSqlParser.T_STATISTICS, 0);
        }

        public TerminalNode T_STEP() {
            return getToken(CupidDBSqlParser.T_STEP, 0);
        }

        public TerminalNode T_STDEV() {
            return getToken(CupidDBSqlParser.T_STDEV, 0);
        }

        public TerminalNode T_STORAGE() {
            return getToken(CupidDBSqlParser.T_STORAGE, 0);
        }

        public TerminalNode T_STORE() {
            return getToken(CupidDBSqlParser.T_STORE, 0);
        }

        public TerminalNode T_STORED() {
            return getToken(CupidDBSqlParser.T_STORED, 0);
        }

        public TerminalNode T_STREAM() {
            return getToken(CupidDBSqlParser.T_STREAM, 0);
        }

        public TerminalNode T_STRING() {
            return getToken(CupidDBSqlParser.T_STRING, 0);
        }

        public TerminalNode T_SUBDIR() {
            return getToken(CupidDBSqlParser.T_SUBDIR, 0);
        }

        public TerminalNode T_SUBSTRING() {
            return getToken(CupidDBSqlParser.T_SUBSTRING, 0);
        }

        public TerminalNode T_SUM() {
            return getToken(CupidDBSqlParser.T_SUM, 0);
        }

        public TerminalNode T_SUMMARY() {
            return getToken(CupidDBSqlParser.T_SUMMARY, 0);
        }

        public TerminalNode T_SYSDATE() {
            return getToken(CupidDBSqlParser.T_SYSDATE, 0);
        }

        public TerminalNode T_SYS_REFCURSOR() {
            return getToken(CupidDBSqlParser.T_SYS_REFCURSOR, 0);
        }

        public TerminalNode T_TABLE() {
            return getToken(CupidDBSqlParser.T_TABLE, 0);
        }

        public TerminalNode T_TABLESPACE() {
            return getToken(CupidDBSqlParser.T_TABLESPACE, 0);
        }

        public TerminalNode T_TEMPORARY() {
            return getToken(CupidDBSqlParser.T_TEMPORARY, 0);
        }

        public TerminalNode T_TERMINATED() {
            return getToken(CupidDBSqlParser.T_TERMINATED, 0);
        }

        public TerminalNode T_TEXTIMAGE_ON() {
            return getToken(CupidDBSqlParser.T_TEXTIMAGE_ON, 0);
        }

        public TerminalNode T_THEN() {
            return getToken(CupidDBSqlParser.T_THEN, 0);
        }

        public TerminalNode T_TIMESTAMP() {
            return getToken(CupidDBSqlParser.T_TIMESTAMP, 0);
        }

        public TerminalNode T_TITLE() {
            return getToken(CupidDBSqlParser.T_TITLE, 0);
        }

        public TerminalNode T_TO() {
            return getToken(CupidDBSqlParser.T_TO, 0);
        }

        public TerminalNode T_TOP() {
            return getToken(CupidDBSqlParser.T_TOP, 0);
        }

        public TerminalNode T_TRANSACTION() {
            return getToken(CupidDBSqlParser.T_TRANSACTION, 0);
        }

        public TerminalNode T_TRIM() {
            return getToken(CupidDBSqlParser.T_TRIM, 0);
        }

        public TerminalNode T_TRUE() {
            return getToken(CupidDBSqlParser.T_TRUE, 0);
        }

        public TerminalNode T_TRUNCATE() {
            return getToken(CupidDBSqlParser.T_TRUNCATE, 0);
        }

        public TerminalNode T_UNIQUE() {
            return getToken(CupidDBSqlParser.T_UNIQUE, 0);
        }

        public TerminalNode T_UPDATE() {
            return getToken(CupidDBSqlParser.T_UPDATE, 0);
        }

        public TerminalNode T_UR() {
            return getToken(CupidDBSqlParser.T_UR, 0);
        }

        public TerminalNode T_USE() {
            return getToken(CupidDBSqlParser.T_USE, 0);
        }

        public TerminalNode T_USER() {
            return getToken(CupidDBSqlParser.T_USER, 0);
        }

        public TerminalNode T_USING() {
            return getToken(CupidDBSqlParser.T_USING, 0);
        }

        public TerminalNode T_VALUE() {
            return getToken(CupidDBSqlParser.T_VALUE, 0);
        }

        public TerminalNode T_VALUES() {
            return getToken(CupidDBSqlParser.T_VALUES, 0);
        }

        public TerminalNode T_VAR() {
            return getToken(CupidDBSqlParser.T_VAR, 0);
        }

        public TerminalNode T_VARCHAR() {
            return getToken(CupidDBSqlParser.T_VARCHAR, 0);
        }

        public TerminalNode T_VARCHAR2() {
            return getToken(CupidDBSqlParser.T_VARCHAR2, 0);
        }

        public TerminalNode T_VARYING() {
            return getToken(CupidDBSqlParser.T_VARYING, 0);
        }

        public TerminalNode T_VARIANCE() {
            return getToken(CupidDBSqlParser.T_VARIANCE, 0);
        }

        public TerminalNode T_VOLATILE() {
            return getToken(CupidDBSqlParser.T_VOLATILE, 0);
        }

        public TerminalNode T_WHILE() {
            return getToken(CupidDBSqlParser.T_WHILE, 0);
        }

        public TerminalNode T_WITH() {
            return getToken(CupidDBSqlParser.T_WITH, 0);
        }

        public TerminalNode T_WITHOUT() {
            return getToken(CupidDBSqlParser.T_WITHOUT, 0);
        }

        public TerminalNode T_WORK() {
            return getToken(CupidDBSqlParser.T_WORK, 0);
        }

        public TerminalNode T_XACT_ABORT() {
            return getToken(CupidDBSqlParser.T_XACT_ABORT, 0);
        }

        public TerminalNode T_XML() {
            return getToken(CupidDBSqlParser.T_XML, 0);
        }

        public TerminalNode T_YES() {
            return getToken(CupidDBSqlParser.T_YES, 0);
        }

        public NonReservedWordsContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_nonReservedWords;
        }

        @Override
        public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
            if (visitor instanceof CupidDBSqlVisitor) return ((CupidDBSqlVisitor<
                ? extends T>) visitor).visitNonReservedWords(this);
            else return visitor.visitChildren(this);
        }
    }

    public final NonReservedWordsContext nonReservedWords() throws RecognitionException {
        NonReservedWordsContext _localctx = new NonReservedWordsContext(_ctx, getState());
        enterRule(_localctx, 274, RULE_nonReservedWords);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(1993);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0
                    && ((1L << _la) & ((1L << T_ACTION) | (1L << T_ADD2) | (1L << T_ALL) | (1L
                        << T_ALLOCATE) | (1L << T_ALTER) | (1L << T_AND) | (1L << T_ANSI_NULLS)
                        | (1L << T_ANSI_PADDING) | (1L << T_AS) | (1L << T_ASC) | (1L
                            << T_ASSOCIATE) | (1L << T_AT) | (1L << T_AVG) | (1L << T_BATCHSIZE)
                        | (1L << T_BEGIN) | (1L << T_BETWEEN) | (1L << T_BIGINT) | (1L
                            << T_BINARY_DOUBLE) | (1L << T_BINARY_FLOAT) | (1L << T_BIT) | (1L
                                << T_BODY) | (1L << T_BREAK) | (1L << T_BY) | (1L << T_BYTE) | (1L
                                    << T_CALL) | (1L << T_CALLER) | (1L << T_CASCADE) | (1L
                                        << T_CASE) | (1L << T_CASESPECIFIC) | (1L << T_CAST) | (1L
                                            << T_CHAR) | (1L << T_CHARACTER) | (1L << T_CHARSET)
                        | (1L << T_CLIENT) | (1L << T_CLOSE) | (1L << T_CLUSTERED) | (1L << T_CMP)
                        | (1L << T_COLLECT) | (1L << T_COLLECTION) | (1L << T_COLUMN) | (1L
                            << T_COMMENT) | (1L << T_CONSTANT) | (1L << T_COMMIT) | (1L
                                << T_COMPRESS) | (1L << T_CONCAT) | (1L << T_CONDITION) | (1L
                                    << T_CONSTRAINT) | (1L << T_CONTINUE) | (1L << T_COPY) | (1L
                                        << T_COUNT) | (1L << T_COUNT_BIG) | (1L << T_CREATION) | (1L
                                            << T_CREATOR) | (1L << T_CS))) != 0)
                    || ((((_la - 64)) & ~0x3f) == 0
                        && ((1L << (_la - 64)) & ((1L << (T_CURRENT - 64)) | (1L
                            << (T_CURRENT_SCHEMA - 64)) | (1L << (T_CURSOR - 64)) | (1L
                                << (T_DATABASE - 64)) | (1L << (T_DATA - 64)) | (1L << (T_DATE
                                    - 64)) | (1L << (T_DATETIME - 64)) | (1L << (T_DAY - 64)) | (1L
                                        << (T_DAYS - 64)) | (1L << (T_DEC - 64)) | (1L << (T_DECIMAL
                                            - 64)) | (1L << (T_DECLARE - 64)) | (1L << (T_DEFAULT
                                                - 64)) | (1L << (T_DEFERRED - 64)) | (1L
                                                    << (T_DEFINED - 64)) | (1L << (T_DEFINER - 64))
                            | (1L << (T_DEFINITION - 64)) | (1L << (T_DELETE - 64)) | (1L
                                << (T_DELIMITED - 64)) | (1L << (T_DELIMITER - 64)) | (1L << (T_DESC
                                    - 64)) | (1L << (T_DESCRIBE - 64)) | (1L << (T_DIAGNOSTICS
                                        - 64)) | (1L << (T_DIR - 64)) | (1L << (T_DIRECTORY - 64))
                            | (1L << (T_DISTINCT - 64)) | (1L << (T_DISTRIBUTE - 64)) | (1L << (T_DO
                                - 64)) | (1L << (T_DOUBLE - 64)) | (1L << (T_DOWNLOAD - 64)) | (1L
                                    << (T_DROP - 64)) | (1L << (T_DYNAMIC - 64)) | (1L << (T_ENABLE
                                        - 64)) | (1L << (T_ENGINE - 64)) | (1L << (T_ESCAPED - 64))
                            | (1L << (T_EXCEPT - 64)) | (1L << (T_EXEC - 64)) | (1L << (T_EXECUTE
                                - 64)) | (1L << (T_EXCEPTION - 64)) | (1L << (T_EXCLUSIVE - 64))
                            | (1L << (T_EXISTS - 64)) | (1L << (T_EXIT - 64)) | (1L << (T_FALLBACK
                                - 64)) | (1L << (T_FALSE - 64)) | (1L << (T_FETCH - 64)) | (1L
                                    << (T_FIELDS - 64)) | (1L << (T_FILE - 64)) | (1L << (T_FILES
                                        - 64)) | (1L << (T_FLOAT - 64)) | (1L << (T_FOR - 64)) | (1L
                                            << (T_FOREIGN - 64)) | (1L << (T_FORMAT - 64)) | (1L
                                                << (T_FOUND - 64)) | (1L << (T_FROM - 64)) | (1L
                                                    << (T_FULL - 64)) | (1L << (T_FUNCTION - 64))
                            | (1L << (T_GET - 64)) | (1L << (T_GLOBAL - 64)) | (1L << (T_GO
                                - 64)))) != 0)
                    || ((((_la - 128)) & ~0x3f) == 0
                        && ((1L << (_la - 128)) & ((1L << (T_GRANT - 128)) | (1L << (T_GROUP - 128))
                            | (1L << (T_HANDLER - 128)) | (1L << (T_HASH - 128)) | (1L << (T_HAVING
                                - 128)) | (1L << (T_HOST - 128)) | (1L << (T_IDENTITY - 128)) | (1L
                                    << (T_IF - 128)) | (1L << (T_IGNORE - 128)) | (1L
                                        << (T_IMMEDIATE - 128)) | (1L << (T_IN - 128)) | (1L
                                            << (T_INCLUDE - 128)) | (1L << (T_INDEX - 128)) | (1L
                                                << (T_INITRANS - 128)) | (1L << (T_INNER - 128))
                            | (1L << (T_INOUT - 128)) | (1L << (T_INSERT - 128)) | (1L << (T_INT
                                - 128)) | (1L << (T_INT2 - 128)) | (1L << (T_INT4 - 128)) | (1L
                                    << (T_INT8 - 128)) | (1L << (T_INTEGER - 128)) | (1L
                                        << (T_INTERSECT - 128)) | (1L << (T_INTERVAL - 128)) | (1L
                                            << (T_INTO - 128)) | (1L << (T_INVOKER - 128)) | (1L
                                                << (T_IS - 128)) | (1L << (T_ISOPEN - 128)) | (1L
                                                    << (T_ITEMS - 128)) | (1L << (T_JOIN - 128))
                            | (1L << (T_KEEP - 128)) | (1L << (T_KEY - 128)) | (1L << (T_KEYS
                                - 128)) | (1L << (T_LANGUAGE - 128)) | (1L << (T_LEAVE - 128)) | (1L
                                    << (T_LEFT - 128)) | (1L << (T_LIKE - 128)) | (1L << (T_LIMIT
                                        - 128)) | (1L << (T_LINES - 128)) | (1L << (T_LOCAL - 128))
                            | (1L << (T_LOCATION - 128)) | (1L << (T_LOCATOR - 128)) | (1L
                                << (T_LOCATORS - 128)) | (1L << (T_LOCKS - 128)) | (1L << (T_LOG
                                    - 128)) | (1L << (T_LOGGED - 128)) | (1L << (T_LOGGING - 128))
                            | (1L << (T_LOOP - 128)) | (1L << (T_MAP - 128)) | (1L << (T_MATCHED
                                - 128)) | (1L << (T_MAX - 128)) | (1L << (T_MAXTRANS - 128)) | (1L
                                    << (T_MERGE - 128)) | (1L << (T_MESSAGE_TEXT - 128)) | (1L
                                        << (T_MICROSECOND - 128)) | (1L << (T_MICROSECONDS
                                            - 128)))) != 0)
                    || ((((_la - 192)) & ~0x3f) == 0
                        && ((1L << (_la - 192)) & ((1L << (T_MIN - 192)) | (1L << (T_MULTISET
                            - 192)) | (1L << (T_NCHAR - 192)) | (1L << (T_NEW - 192)) | (1L
                                << (T_NVARCHAR - 192)) | (1L << (T_NO - 192)) | (1L << (T_NOCOUNT
                                    - 192)) | (1L << (T_NOCOMPRESS - 192)) | (1L << (T_NOLOGGING
                                        - 192)) | (1L << (T_NONE - 192)) | (1L << (T_NOT - 192))
                            | (1L << (T_NOTFOUND - 192)) | (1L << (T_NUMERIC - 192)) | (1L
                                << (T_NUMBER - 192)) | (1L << (T_OBJECT - 192)) | (1L << (T_OFF
                                    - 192)) | (1L << (T_ON - 192)) | (1L << (T_ONLY - 192)) | (1L
                                        << (T_OPEN - 192)) | (1L << (T_OR - 192)) | (1L << (T_ORDER
                                            - 192)) | (1L << (T_OUT - 192)) | (1L << (T_OUTER
                                                - 192)) | (1L << (T_OVER - 192)) | (1L
                                                    << (T_OVERWRITE - 192)) | (1L << (T_OWNER
                                                        - 192)) | (1L << (T_PACKAGE - 192)) | (1L
                                                            << (T_PARTITION - 192)) | (1L
                                                                << (T_PCTFREE - 192)) | (1L
                                                                    << (T_PCTUSED - 192)) | (1L
                                                                        << (T_PRECISION - 192))
                            | (1L << (T_PRESERVE - 192)) | (1L << (T_PRIMARY - 192)) | (1L
                                << (T_PRINT - 192)) | (1L << (T_PROC - 192)) | (1L << (T_PROCEDURE
                                    - 192)) | (1L << (T_QUALIFY - 192)) | (1L << (T_QUERY_BAND
                                        - 192)) | (1L << (T_QUIT - 192)) | (1L
                                            << (T_QUOTED_IDENTIFIER - 192)) | (1L << (T_QUOTES
                                                - 192)) | (1L << (T_RAISE - 192)) | (1L << (T_REAL
                                                    - 192)) | (1L << (T_REFERENCES - 192)) | (1L
                                                        << (T_REGEXP - 192)) | (1L << (T_REPLACE
                                                            - 192)) | (1L << (T_RESIGNAL - 192))
                            | (1L << (T_RESTRICT - 192)) | (1L << (T_RESULT - 192)) | (1L
                                << (T_RESULT_SET_LOCATOR - 192)) | (1L << (T_RETURN - 192)) | (1L
                                    << (T_RETURNS - 192)) | (1L << (T_REVERSE - 192)))) != 0)
                    || ((((_la - 256)) & ~0x3f) == 0
                        && ((1L << (_la - 256)) & ((1L << (T_RIGHT - 256)) | (1L << (T_RLIKE - 256))
                            | (1L << (T_ROLE - 256)) | (1L << (T_ROLLBACK - 256)) | (1L << (T_ROW
                                - 256)) | (1L << (T_ROWS - 256)) | (1L << (T_ROW_COUNT - 256)) | (1L
                                    << (T_RR - 256)) | (1L << (T_RS - 256)) | (1L << (T_PWD - 256))
                            | (1L << (T_TRIM - 256)) | (1L << (T_SCHEMA - 256)) | (1L << (T_SECOND
                                - 256)) | (1L << (T_SECONDS - 256)) | (1L << (T_SECURITY - 256))
                            | (1L << (T_SEGMENT - 256)) | (1L << (T_SEL - 256)) | (1L << (T_SELECT
                                - 256)) | (1L << (T_SET - 256)) | (1L << (T_SESSION - 256)) | (1L
                                    << (T_SESSIONS - 256)) | (1L << (T_SETS - 256)) | (1L
                                        << (T_SIGNAL - 256)) | (1L << (T_SIMPLE_DOUBLE - 256)) | (1L
                                            << (T_SIMPLE_FLOAT - 256)) | (1L << (T_SMALLDATETIME
                                                - 256)) | (1L << (T_SMALLINT - 256)) | (1L << (T_SQL
                                                    - 256)) | (1L << (T_SQLEXCEPTION - 256)) | (1L
                                                        << (T_SQLINSERT - 256)) | (1L << (T_SQLSTATE
                                                            - 256)) | (1L << (T_SQLWARNING - 256))
                            | (1L << (T_STATS - 256)) | (1L << (T_STATISTICS - 256)) | (1L
                                << (T_STEP - 256)) | (1L << (T_STORAGE - 256)) | (1L << (T_STORE
                                    - 256)) | (1L << (T_STORED - 256)) | (1L << (T_STREAM - 256))
                            | (1L << (T_STRING - 256)) | (1L << (T_SUBDIR - 256)) | (1L
                                << (T_SUBSTRING - 256)) | (1L << (T_SUM - 256)) | (1L << (T_SUMMARY
                                    - 256)) | (1L << (T_SYS_REFCURSOR - 256)) | (1L << (T_TABLE
                                        - 256)) | (1L << (T_TABLESPACE - 256)) | (1L << (T_TEMPORARY
                                            - 256)) | (1L << (T_TERMINATED - 256)) | (1L
                                                << (T_TEXTIMAGE_ON - 256)) | (1L << (T_THEN - 256))
                            | (1L << (T_TIMESTAMP - 256)))) != 0)
                    || ((((_la - 320)) & ~0x3f) == 0
                        && ((1L << (_la - 320)) & ((1L << (T_TITLE - 320)) | (1L << (T_TO - 320))
                            | (1L << (T_TOP - 320)) | (1L << (T_TRANSACTION - 320)) | (1L << (T_TRUE
                                - 320)) | (1L << (T_TRUNCATE - 320)) | (1L << (T_UNIQUE - 320))
                            | (1L << (T_UPDATE - 320)) | (1L << (T_UR - 320)) | (1L << (T_USE
                                - 320)) | (1L << (T_USING - 320)) | (1L << (T_VALUE - 320)) | (1L
                                    << (T_VALUES - 320)) | (1L << (T_VAR - 320)) | (1L << (T_VARCHAR
                                        - 320)) | (1L << (T_VARCHAR2 - 320)) | (1L << (T_VARYING
                                            - 320)) | (1L << (T_VOLATILE - 320)) | (1L << (T_WHILE
                                                - 320)) | (1L << (T_WITH - 320)) | (1L << (T_WITHOUT
                                                    - 320)) | (1L << (T_WORK - 320)) | (1L
                                                        << (T_XACT_ABORT - 320)) | (1L << (T_XML
                                                            - 320)) | (1L << (T_YES - 320)) | (1L
                                                                << (T_ACTIVITY_COUNT - 320)) | (1L
                                                                    << (T_CUME_DIST - 320)) | (1L
                                                                        << (T_CURRENT_DATE - 320))
                            | (1L << (T_CURRENT_TIME - 320)) | (1L << (T_PI - 320)) | (1L
                                << (T_CURRENT_TIMESTAMP - 320)) | (1L << (T_CURRENT_USER - 320))
                            | (1L << (T_DENSE_RANK - 320)) | (1L << (T_FIRST_VALUE - 320)) | (1L
                                << (T_LAG - 320)) | (1L << (T_LAST_VALUE - 320)) | (1L << (T_LEAD
                                    - 320)) | (1L << (T_PART_COUNT - 320)) | (1L << (T_PART_LOC
                                        - 320)) | (1L << (T_RANK - 320)) | (1L << (T_ROW_NUMBER
                                            - 320)) | (1L << (T_STDEV - 320)) | (1L << (T_SYSDATE
                                                - 320)) | (1L << (T_VARIANCE - 320)) | (1L
                                                    << (T_USER - 320)))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 71:
                return fromAliasClause_sempred((FromAliasClauseContext) _localctx, predIndex);
            case 87:
                return deleteAlias_sempred((DeleteAliasContext) _localctx, predIndex);
            case 98:
                return boolExpr_sempred((BoolExprContext) _localctx, predIndex);
            case 106:
                return expr_sempred((ExprContext) _localctx, predIndex);
            case 124:
                return funcParam_sempred((FuncParamContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean fromAliasClause_sempred(FromAliasClauseContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return !_input.LT(1).getText().equalsIgnoreCase("EXEC")
                    && !_input.LT(1).getText().equalsIgnoreCase("EXECUTE")
                    && !_input.LT(1).getText().equalsIgnoreCase("INNER")
                    && !_input.LT(1).getText().equalsIgnoreCase("LEFT")
                    && !_input.LT(1).getText().equalsIgnoreCase("GROUP")
                    && !_input.LT(1).getText().equalsIgnoreCase("ORDER")
                    && !_input.LT(1).getText().equalsIgnoreCase("LIMIT")
                    && !_input.LT(1).getText().equalsIgnoreCase("WITH");
        }
        return true;
    }

    private boolean deleteAlias_sempred(DeleteAliasContext _localctx, int predIndex) {
        switch (predIndex) {
            case 1:
                return !_input.LT(1).getText().equalsIgnoreCase("ALL");
        }
        return true;
    }

    private boolean boolExpr_sempred(BoolExprContext _localctx, int predIndex) {
        switch (predIndex) {
            case 2:
                return precpred(_ctx, 2);
        }
        return true;
    }

    private boolean expr_sempred(ExprContext _localctx, int predIndex) {
        switch (predIndex) {
            case 3:
                return precpred(_ctx, 14);
            case 4:
                return precpred(_ctx, 13);
            case 5:
                return precpred(_ctx, 12);
            case 6:
                return precpred(_ctx, 11);
            case 7:
                return precpred(_ctx, 15);
        }
        return true;
    }

    private boolean funcParam_sempred(FuncParamContext _localctx, int predIndex) {
        switch (predIndex) {
            case 8:
                return !_input.LT(1).getText().equalsIgnoreCase("INTO");
        }
        return true;
    }

    public static final String _serializedATN =
        "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\u01a9\u07ce\4\2\t"
            + "\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"
            + "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"
            + "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"
            + "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"
            + "\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"
            + ",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"
            + "\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="
            + "\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"
            + "\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"
            + "\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"
            + "`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\4f\tf\4g\tg\4h\th\4i\ti\4j\tj\4k\t"
            + "k\4l\tl\4m\tm\4n\tn\4o\to\4p\tp\4q\tq\4r\tr\4s\ts\4t\tt\4u\tu\4v\tv\4"
            + "w\tw\4x\tx\4y\ty\4z\tz\4{\t{\4|\t|\4}\t}\4~\t~\4\177\t\177\4\u0080\t\u0080"
            + "\4\u0081\t\u0081\4\u0082\t\u0082\4\u0083\t\u0083\4\u0084\t\u0084\4\u0085"
            + "\t\u0085\4\u0086\t\u0086\4\u0087\t\u0087\4\u0088\t\u0088\4\u0089\t\u0089"
            + "\4\u008a\t\u008a\4\u008b\t\u008b\3\2\3\2\5\2\u0119\n\2\3\2\3\2\3\3\3\3"
            + "\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"
            + "\3\3\3\3\3\5\3\u0133\n\3\3\4\3\4\3\4\3\4\3\4\5\4\u013a\n\4\3\4\3\4\3\4"
            + "\3\4\3\5\3\5\5\5\u0142\n\5\3\5\3\5\3\6\3\6\3\6\5\6\u0149\n\6\3\6\5\6\u014c"
            + "\n\6\3\7\3\7\3\7\5\7\u0151\n\7\3\b\3\b\5\b\u0155\n\b\3\b\3\b\3\b\3\b\3"
            + "\b\3\b\3\b\5\b\u015e\n\b\3\b\3\b\3\b\5\b\u0163\n\b\3\t\3\t\3\t\3\t\7\t"
            + "\u0169\n\t\f\t\16\t\u016c\13\t\3\t\3\t\5\t\u0170\n\t\3\t\3\t\3\t\3\t\3"
            + "\t\7\t\u0177\n\t\f\t\16\t\u017a\13\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\7\n\u0183"
            + "\n\n\f\n\16\n\u0186\13\n\3\n\3\n\5\n\u018a\n\n\3\n\5\n\u018d\n\n\3\n\3"
            + "\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3"
            + "\r\3\r\5\r\u01a2\n\r\3\16\3\16\5\16\u01a6\n\16\3\16\3\16\3\16\3\16\5\16"
            + "\u01ac\n\16\3\16\3\16\5\16\u01b0\n\16\3\16\3\16\3\16\3\16\3\16\3\16\3"
            + "\16\3\16\5\16\u01ba\n\16\3\16\3\16\3\16\5\16\u01bf\n\16\5\16\u01c1\n\16"
            + "\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\5\20\u01cb\n\20\3\20\3\20\3\20"
            + "\3\20\3\20\5\20\u01d2\n\20\3\20\3\20\3\20\3\20\3\20\5\20\u01d9\n\20\3"
            + "\20\5\20\u01dc\n\20\3\21\3\21\3\21\7\21\u01e1\n\21\f\21\16\21\u01e4\13"
            + "\21\3\22\3\22\3\22\7\22\u01e9\n\22\f\22\16\22\u01ec\13\22\3\22\7\22\u01ef"
            + "\n\22\f\22\16\22\u01f2\13\22\3\22\3\22\5\22\u01f6\n\22\3\22\3\22\3\22"
            + "\3\22\5\22\u01fc\n\22\3\22\3\22\5\22\u0200\n\22\5\22\u0202\n\22\3\23\3"
            + "\23\3\23\3\23\7\23\u0208\n\23\f\23\16\23\u020b\13\23\3\23\3\23\3\24\3"
            + "\24\3\24\3\25\3\25\3\26\3\26\5\26\u0216\n\26\3\26\3\26\3\26\3\26\3\26"
            + "\3\26\3\26\3\26\3\26\3\26\7\26\u0222\n\26\f\26\16\26\u0225\13\26\3\26"
            + "\3\26\3\26\3\26\3\26\7\26\u022c\n\26\f\26\16\26\u022f\13\26\3\26\3\26"
            + "\3\26\5\26\u0234\n\26\3\27\3\27\3\27\5\27\u0239\n\27\3\27\3\27\3\27\5"
            + "\27\u023e\n\27\3\27\3\27\3\27\5\27\u0243\n\27\7\27\u0245\n\27\f\27\16"
            + "\27\u0248\13\27\3\27\3\27\5\27\u024c\n\27\3\27\3\27\3\27\3\27\3\27\3\27"
            + "\7\27\u0254\n\27\f\27\16\27\u0257\13\27\3\27\3\27\3\27\3\27\3\27\3\27"
            + "\3\27\7\27\u0260\n\27\f\27\16\27\u0263\13\27\3\27\3\27\7\27\u0267\n\27"
            + "\f\27\16\27\u026a\13\27\5\27\u026c\n\27\3\30\3\30\3\30\3\30\3\30\3\30"
            + "\3\30\3\30\3\30\3\30\5\30\u0278\n\30\3\31\6\31\u027b\n\31\r\31\16\31\u027c"
            + "\3\32\3\32\3\32\3\33\5\33\u0283\n\33\3\33\3\33\3\34\6\34\u0288\n\34\r"
            + "\34\16\34\u0289\3\35\3\35\3\35\3\35\3\35\5\35\u0291\n\35\3\36\3\36\5\36"
            + "\u0295\n\36\3\36\3\36\3\36\5\36\u029a\n\36\3\36\3\36\5\36\u029e\n\36\3"
            + "\36\3\36\3\36\5\36\u02a3\n\36\3\36\5\36\u02a6\n\36\3\36\3\36\3\36\5\36"
            + "\u02ab\n\36\3\36\5\36\u02ae\n\36\3\37\3\37\5\37\u02b2\n\37\3\37\3\37\3"
            + "\37\3\37\3\37\3\37\3\37\3\37\7\37\u02bc\n\37\f\37\16\37\u02bf\13\37\3"
            + "\37\3\37\3 \3 \5 \u02c5\n \3!\3!\3!\3!\3!\3!\3\"\5\"\u02ce\n\"\3\"\3\""
            + "\3\"\5\"\u02d3\n\"\3\"\3\"\5\"\u02d7\n\"\5\"\u02d9\n\"\3#\3#\3#\3$\5$"
            + "\u02df\n$\3$\3$\3$\3$\3$\5$\u02e6\n$\3$\5$\u02e9\n$\3%\3%\3%\3%\3%\3%"
            + "\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\3%\5%\u0304\n%"
            + "\5%\u0306\n%\3&\3&\3&\5&\u030b\n&\3&\3&\5&\u030f\n&\3&\3&\3\'\3\'\3\'"
            + "\3\'\3\'\5\'\u0318\n\'\3\'\3\'\7\'\u031c\n\'\f\'\16\'\u031f\13\'\3(\3"
            + "(\3(\3)\3)\3)\3)\5)\u0328\n)\3)\3)\3*\3*\3*\3*\5*\u0330\n*\3+\3+\3+\3"
            + "+\5+\u0336\n+\3+\3+\3,\3,\3,\3,\3,\5,\u033f\n,\5,\u0341\n,\3,\3,\5,\u0345"
            + "\n,\3,\3,\5,\u0349\n,\3-\3-\3-\3-\7-\u034f\n-\f-\16-\u0352\13-\3-\3-\3"
            + ".\3.\3.\3.\7.\u035a\n.\f.\16.\u035d\13.\3/\3/\3/\3/\7/\u0363\n/\f/\16"
            + "/\u0366\13/\3/\3/\3\60\3\60\5\60\u036c\n\60\3\60\3\60\3\61\3\61\3\61\3"
            + "\62\5\62\u0374\n\62\3\62\3\62\3\63\3\63\3\63\3\63\7\63\u037c\n\63\f\63"
            + "\16\63\u037f\13\63\3\64\3\64\5\64\u0383\n\64\3\64\3\64\3\64\3\64\3\64"
            + "\3\65\3\65\3\65\3\65\7\65\u038e\n\65\f\65\16\65\u0391\13\65\3\65\3\65"
            + "\3\66\3\66\3\66\3\66\7\66\u0399\n\66\f\66\16\66\u039c\13\66\3\66\3\66"
            + "\3\66\3\66\5\66\u03a2\n\66\3\67\3\67\3\67\3\67\3\67\5\67\u03a9\n\67\3"
            + "8\38\58\u03ad\n8\38\38\58\u03b1\n8\38\38\58\u03b5\n8\58\u03b7\n8\39\3"
            + "9\39\59\u03bc\n9\39\59\u03bf\n9\39\59\u03c2\n9\39\59\u03c5\n9\39\39\5"
            + "9\u03c9\n9\39\59\u03cc\n9\39\59\u03cf\n9\3:\5:\u03d2\n:\3:\5:\u03d5\n"
            + ":\3:\3:\3:\7:\u03da\n:\f:\16:\u03dd\13:\3;\3;\3<\3<\3<\3=\3=\3=\5=\u03e7"
            + "\n=\3=\3=\5=\u03eb\n=\3=\5=\u03ee\n=\3>\5>\u03f1\n>\3>\3>\5>\u03f5\n>"
            + "\3>\3>\3>\3>\7>\u03fb\n>\f>\16>\u03fe\13>\3>\3>\3>\3>\3>\3>\5>\u0406\n"
            + ">\3?\3?\5?\u040a\n?\3?\3?\3@\3@\3@\3@\7@\u0412\n@\f@\16@\u0415\13@\3A"
            + "\3A\3A\7A\u041a\nA\fA\16A\u041d\13A\3B\3B\3B\5B\u0422\nB\3C\3C\5C\u0426"
            + "\nC\3C\5C\u0429\nC\3D\3D\3D\3D\5D\u042f\nD\3E\3E\3E\3E\3E\3E\3E\5E\u0438"
            + "\nE\3F\5F\u043b\nF\3F\3F\3F\5F\u0440\nF\3F\5F\u0443\nF\3G\3G\3G\3G\3G"
            + "\3G\7G\u044b\nG\fG\16G\u044e\13G\3G\3G\5G\u0452\nG\3H\3H\3H\3H\3H\7H\u0459"
            + "\nH\fH\16H\u045c\13H\3H\3H\5H\u0460\nH\3I\3I\5I\u0464\nI\3I\3I\3I\3I\3"
            + "I\7I\u046b\nI\fI\16I\u046e\13I\3I\5I\u0471\nI\3J\3J\3K\3K\3K\3L\3L\3L"
            + "\3L\3L\7L\u047d\nL\fL\16L\u0480\13L\3M\3M\3M\3N\3N\3N\3O\3O\3O\3O\3O\7"
            + "O\u048d\nO\fO\16O\u0490\13O\3P\3P\5P\u0494\nP\3Q\3Q\3Q\3Q\3Q\3Q\3R\6R"
            + "\u049d\nR\rR\16R\u049e\3S\3S\3S\3S\3S\3S\3S\3S\3S\5S\u04aa\nS\5S\u04ac"
            + "\nS\3T\3T\3T\3T\3T\5T\u04b3\nT\3T\5T\u04b6\nT\3U\3U\3U\7U\u04bb\nU\fU"
            + "\16U\u04be\13U\3V\3V\5V\u04c2\nV\3V\3V\3V\3V\5V\u04c8\nV\3V\5V\u04cb\n"
            + "V\3V\5V\u04ce\nV\3W\3W\3W\3X\3X\5X\u04d5\nX\3X\3X\5X\u04d9\nX\3X\3X\5"
            + "X\u04dd\nX\3Y\3Y\5Y\u04e1\nY\3Y\3Y\3Z\3Z\5Z\u04e7\nZ\3Z\3Z\3[\3[\5[\u04ed"
            + "\n[\3[\3[\3[\3[\5[\u04f3\n[\3[\3[\5[\u04f7\n[\3[\5[\u04fa\n[\3[\5[\u04fd"
            + "\n[\3\\\3\\\3\\\3\\\3]\3]\3]\7]\u0506\n]\f]\16]\u0509\13]\3^\3^\3^\3_"
            + "\3_\3_\5_\u0511\n_\3_\3_\5_\u0515\n_\3`\5`\u0518\n`\3`\3`\3a\3a\3a\3a"
            + "\3a\3a\3b\3b\3c\3c\3d\3d\5d\u0528\nd\3d\3d\3d\3d\3d\5d\u052f\nd\3d\3d"
            + "\3d\3d\7d\u0535\nd\fd\16d\u0538\13d\3e\3e\3e\5e\u053d\ne\3f\3f\3f\5f\u0542"
            + "\nf\3f\3f\3f\3f\3f\3f\3f\3f\3f\5f\u054d\nf\3f\3f\3f\3f\3f\3f\3f\5f\u0556"
            + "\nf\3g\3g\5g\u055a\ng\3g\3g\3g\3g\3g\7g\u0561\ng\fg\16g\u0564\13g\3g\5"
            + "g\u0567\ng\3g\3g\3h\3h\3h\3h\7h\u056f\nh\fh\16h\u0572\13h\3h\3h\5h\u0576"
            + "\nh\3h\3h\3h\3h\3h\3i\3i\3i\3i\3j\3j\3k\3k\3k\3k\3k\3k\3k\3k\3k\5k\u058c"
            + "\nk\3k\5k\u058f\nk\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l"
            + "\5l\u05a2\nl\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\3l\7l\u05b2\nl\fl"
            + "\16l\u05b5\13l\3m\3m\3m\3m\3m\3m\3m\3m\5m\u05bf\nm\3n\3n\3n\3n\3o\3o\3"
            + "p\3p\3p\3p\3p\7p\u05cc\np\fp\16p\u05cf\13p\3q\3q\3q\3q\3q\3q\3q\3q\3q"
            + "\5q\u05da\nq\3r\3r\5r\u05de\nr\3s\3s\3s\3s\3s\3s\3s\6s\u05e7\ns\rs\16"
            + "s\u05e8\3s\3s\5s\u05ed\ns\3s\3s\3t\3t\6t\u05f3\nt\rt\16t\u05f4\3t\3t\5"
            + "t\u05f9\nt\3t\3t\3u\3u\3u\3u\3u\3v\3v\3v\3v\3w\3w\3w\5w\u0609\nw\3w\3"
            + "w\3w\5w\u060e\nw\3w\3w\3w\5w\u0613\nw\3w\3w\5w\u0617\nw\3w\3w\5w\u061b"
            + "\nw\3w\3w\3w\5w\u0620\nw\3w\3w\5w\u0624\nw\3w\3w\5w\u0628\nw\3w\3w\3w"
            + "\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\5w\u063f\nw\5w"
            + "\u0641\nw\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\5w\u0653\nw"
            + "\5w\u0655\nw\3w\3w\3w\3w\3w\3w\5w\u065d\nw\3w\3w\3w\5w\u0662\nw\3w\3w"
            + "\3w\5w\u0667\nw\3w\3w\3w\5w\u066c\nw\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w\3w"
            + "\5w\u0679\nw\3w\3w\3w\5w\u067e\nw\3w\3w\3w\5w\u0683\nw\3w\3w\3w\5w\u0688"
            + "\nw\3w\3w\3w\5w\u068d\nw\3w\3w\3w\5w\u0692\nw\3w\3w\3w\5w\u0697\nw\3w"
            + "\3w\3w\5w\u069c\nw\5w\u069e\nw\3x\3x\3y\3y\3y\5y\u06a5\ny\3y\5y\u06a8"
            + "\ny\3y\3y\3z\3z\3z\3z\3z\7z\u06b1\nz\fz\16z\u06b4\13z\3{\3{\3{\3{\3{\3"
            + "{\3{\5{\u06bd\n{\3{\3{\3{\3{\3{\3{\5{\u06c5\n{\3{\3{\3{\3{\3{\3{\3{\3"
            + "{\3{\5{\u06d0\n{\3{\3{\3{\3{\5{\u06d6\n{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3"
            + "{\3{\3{\3{\7{\u06e5\n{\f{\16{\u06e8\13{\5{\u06ea\n{\3{\3{\3{\3{\3{\3{"
            + "\3{\3{\3{\3{\3{\3{\7{\u06f8\n{\f{\16{\u06fb\13{\5{\u06fd\n{\3{\3{\3{\3"
            + "{\3{\3{\3{\3{\3{\3{\3{\3{\7{\u070b\n{\f{\16{\u070e\13{\5{\u0710\n{\3{"
            + "\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\7{\u071e\n{\f{\16{\u0721\13{\5{\u0723"
            + "\n{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\7{\u0731\n{\f{\16{\u0734\13{\5"
            + "{\u0736\n{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\7{\u0744\n{\f{\16{\u0747"
            + "\13{\5{\u0749\n{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\7{\u0755\n{\f{\16{\u0758"
            + "\13{\3{\3{\3{\3{\3{\3{\3{\3{\3{\3{\6{\u0764\n{\r{\16{\u0765\3{\3{\5{\u076a"
            + "\n{\3{\3{\3{\3{\3{\3{\3{\3{\3{\5{\u0775\n{\3{\3{\3{\3{\5{\u077b\n{\3|"
            + "\3|\3|\5|\u0780\n|\3|\3|\3}\3}\3}\7}\u0787\n}\f}\16}\u078a\13}\3~\3~\3"
            + "~\3~\3~\5~\u0791\n~\5~\u0793\n~\3~\3~\5~\u0797\n~\3\177\3\177\3\u0080"
            + "\3\u0080\3\u0081\3\u0081\3\u0081\7\u0081\u07a0\n\u0081\f\u0081\16\u0081"
            + "\u07a3\13\u0081\3\u0082\3\u0082\3\u0082\3\u0083\3\u0083\3\u0083\3\u0084"
            + "\5\u0084\u07ac\n\u0084\3\u0084\3\u0084\3\u0084\7\u0084\u07b1\n\u0084\f"
            + "\u0084\16\u0084\u07b4\13\u0084\3\u0085\3\u0085\5\u0085\u07b8\n\u0085\3"
            + "\u0086\3\u0086\5\u0086\u07bc\n\u0086\3\u0087\5\u0087\u07bf\n\u0087\3\u0087"
            + "\3\u0087\3\u0088\5\u0088\u07c4\n\u0088\3\u0088\3\u0088\3\u0089\3\u0089"
            + "\3\u008a\3\u008a\3\u008b\3\u008b\3\u008b\2\4\u00c6\u00d6\u008c\2\4\6\b"
            + "\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVX"
            + "Z\\^`bdfhjlnprtvxz|~\u0080\u0082\u0084\u0086\u0088\u008a\u008c\u008e\u0090"
            + "\u0092\u0094\u0096\u0098\u009a\u009c\u009e\u00a0\u00a2\u00a4\u00a6\u00a8"
            + "\u00aa\u00ac\u00ae\u00b0\u00b2\u00b4\u00b6\u00b8\u00ba\u00bc\u00be\u00c0"
            + "\u00c2\u00c4\u00c6\u00c8\u00ca\u00cc\u00ce\u00d0\u00d2\u00d4\u00d6\u00d8"
            + "\u00da\u00dc\u00de\u00e0\u00e2\u00e4\u00e6\u00e8\u00ea\u00ec\u00ee\u00f0"
            + "\u00f2\u00f4\u00f6\u00f8\u00fa\u00fc\u00fe\u0100\u0102\u0104\u0106\u0108"
            + "\u010a\u010c\u010e\u0110\u0112\u0114\2\36\5\2OO\u0198\u0198\u01a1\u01a1"
            + "\4\2\20\20\u0135\u0135\4\2\r\rWW\4\2TT\u014b\u014b\4\2qq\u00b6\u00b6\4"
            + "\2TT\u00e5\u00e5\4\2%%@@\4\2\u0108\u0108\u0148\u0148\4\2\u00bc\u00bc\u01a4"
            + "\u01a4\4\2  \'\'\4\2EE\u010f\u010f\4\2OO\u01a1\u01a1\3\2\u0114\u0115\4"
            + "\2\6\6\\\\\5\2}}\u00ac\u00ac\u0102\u0102\5\2@@\u010a\u010b\u014c\u014c"
            + "\4\2nn\u014b\u014b\3\2WX\4\2\u0136\u0136\u0138\u0138\3\2\u0159\u015a\4"
            + "\2\t\t\u00d6\u00d6\5\2\u00ad\u00ad\u00f7\u00f7\u0103\u0103\5\2JK\u00c0"
            + "\u00c1\u0110\u0111\4\2\66\66\u018c\u018c\5\2{{\u00a2\u00a2\u00cd\u00cd"
            + "\4\2\u0189\u0189\u01a0\u01a0\4\2rr\u0146\u0146\'\2\4\17\23\31\33\35\37"
            + "\61\63<>@BEGbffh\u0082\u0084\u0087\u0089\u008a\u008c\u0090\u0092\u0095"
            + "\u0097\u00a5\u00a7\u00a8\u00aa\u00af\u00b1\u00cd\u00cf\u00de\u00e0\u00e1"
            + "\u00e4\u00e5\u00e7\u00ea\u00ef\u00f7\u00f9\u0100\u0102\u0107\u0109\u010d"
            + "\u010f\u0119\u011b\u011d\u011f\u0125\u0127\u0128\u012a\u0134\u0136\u0136"
            + "\u013b\u0140\u0142\u0147\u014a\u0155\u0158\u016a\u0171\u0178\2\u08b9\2"
            + "\u0116\3\2\2\2\4\u0132\3\2\2\2\6\u0134\3\2\2\2\b\u0141\3\2\2\2\n\u014b"
            + "\3\2\2\2\f\u0150\3\2\2\2\16\u0162\3\2\2\2\20\u0164\3\2\2\2\22\u0189\3"
            + "\2\2\2\24\u0193\3\2\2\2\26\u0198\3\2\2\2\30\u019b\3\2\2\2\32\u01c0\3\2"
            + "\2\2\34\u01c2\3\2\2\2\36\u01d8\3\2\2\2 \u01dd\3\2\2\2\"\u0201\3\2\2\2"
            + "$\u0203\3\2\2\2&\u020e\3\2\2\2(\u0211\3\2\2\2*\u0233\3\2\2\2,\u026b\3"
            + "\2\2\2.\u026d\3\2\2\2\60\u027a\3\2\2\2\62\u027e\3\2\2\2\64\u0282\3\2\2"
            + "\2\66\u0287\3\2\2\28\u0290\3\2\2\2:\u02ad\3\2\2\2<\u02af\3\2\2\2>\u02c2"
            + "\3\2\2\2@\u02c6\3\2\2\2B\u02d8\3\2\2\2D\u02da\3\2\2\2F\u02e8\3\2\2\2H"
            + "\u0305\3\2\2\2J\u0307\3\2\2\2L\u0312\3\2\2\2N\u0320\3\2\2\2P\u0323\3\2"
            + "\2\2R\u032f\3\2\2\2T\u0331\3\2\2\2V\u0339\3\2\2\2X\u034a\3\2\2\2Z\u0355"
            + "\3\2\2\2\\\u035e\3\2\2\2^\u0369\3\2\2\2`\u036f\3\2\2\2b\u0373\3\2\2\2"
            + "d\u0377\3\2\2\2f\u0380\3\2\2\2h\u0389\3\2\2\2j\u03a1\3\2\2\2l\u03a8\3"
            + "\2\2\2n\u03b6\3\2\2\2p\u03b8\3\2\2\2r\u03d1\3\2\2\2t\u03de\3\2\2\2v\u03e0"
            + "\3\2\2\2x\u03ed\3\2\2\2z\u0405\3\2\2\2|\u0409\3\2\2\2~\u040d\3\2\2\2\u0080"
            + "\u0416\3\2\2\2\u0082\u0421\3\2\2\2\u0084\u0423\3\2\2\2\u0086\u042a\3\2"
            + "\2\2\u0088\u0437\3\2\2\2\u008a\u0442\3\2\2\2\u008c\u0444\3\2\2\2\u008e"
            + "\u045f\3\2\2\2\u0090\u0461\3\2\2\2\u0092\u0472\3\2\2\2\u0094\u0474\3\2"
            + "\2\2\u0096\u0477\3\2\2\2\u0098\u0481\3\2\2\2\u009a\u0484\3\2\2\2\u009c"
            + "\u0487\3\2\2\2\u009e\u0491\3\2\2\2\u00a0\u0495\3\2\2\2\u00a2\u049c\3\2"
            + "\2\2\u00a4\u04ab\3\2\2\2\u00a6\u04ad\3\2\2\2\u00a8\u04b7\3\2\2\2\u00aa"
            + "\u04c7\3\2\2\2\u00ac\u04cf\3\2\2\2\u00ae\u04d2\3\2\2\2\u00b0\u04de\3\2"
            + "\2\2\u00b2\u04e4\3\2\2\2\u00b4\u04ea\3\2\2\2\u00b6\u04fe\3\2\2\2\u00b8"
            + "\u0502\3\2\2\2\u00ba\u050a\3\2\2\2\u00bc\u050d\3\2\2\2\u00be\u0517\3\2"
            + "\2\2\u00c0\u051b\3\2\2\2\u00c2\u0521\3\2\2\2\u00c4\u0523\3\2\2\2\u00c6"
            + "\u052e\3\2\2\2\u00c8\u053c\3\2\2\2\u00ca\u0555\3\2\2\2\u00cc\u0557\3\2"
            + "\2\2\u00ce\u056a\3\2\2\2\u00d0\u057c\3\2\2\2\u00d2\u0580\3\2\2\2\u00d4"
            + "\u058e\3\2\2\2\u00d6\u05a1\3\2\2\2\u00d8\u05be\3\2\2\2\u00da\u05c0\3\2"
            + "\2\2\u00dc\u05c4\3\2\2\2\u00de\u05c6\3\2\2\2\u00e0\u05d9\3\2\2\2\u00e2"
            + "\u05dd\3\2\2\2\u00e4\u05df\3\2\2\2\u00e6\u05f0\3\2\2\2\u00e8\u05fc\3\2"
            + "\2\2\u00ea\u0601\3\2\2\2\u00ec\u069d\3\2\2\2\u00ee\u069f\3\2\2\2\u00f0"
            + "\u06a1\3\2\2\2\u00f2\u06ab\3\2\2\2\u00f4\u077a\3\2\2\2\u00f6\u077c\3\2"
            + "\2\2\u00f8\u0783\3\2\2\2\u00fa\u0796\3\2\2\2\u00fc\u0798\3\2\2\2\u00fe"
            + "\u079a\3\2\2\2\u0100\u079c\3\2\2\2\u0102\u07a4\3\2\2\2\u0104\u07a7\3\2"
            + "\2\2\u0106\u07ab\3\2\2\2\u0108\u07b7\3\2\2\2\u010a\u07bb\3\2\2\2\u010c"
            + "\u07be\3\2\2\2\u010e\u07c3\3\2\2\2\u0110\u07c7\3\2\2\2\u0112\u07c9\3\2"
            + "\2\2\u0114\u07cb\3\2\2\2\u0116\u0118\5\4\3\2\u0117\u0119\7\u019f\2\2\u0118"
            + "\u0117\3\2\2\2\u0118\u0119\3\2\2\2\u0119\u011a\3\2\2\2\u011a\u011b\7\2"
            + "\2\3\u011b\3\3\2\2\2\u011c\u0133\5L\'\2\u011d\u0133\5\32\16\2\u011e\u0133"
            + "\5\6\4\2\u011f\u0133\5<\37\2\u0120\u0133\5\u00b2Z\2\u0121\u0133\5P)\2"
            + "\u0122\u0133\5T+\2\u0123\u0133\5@!\2\u0124\u0133\5^\60\2\u0125\u0133\5"
            + "`\61\2\u0126\u0133\5N(\2\u0127\u0133\5D#\2\u0128\u0133\5\24\13\2\u0129"
            + "\u0133\5\26\f\2\u012a\u0133\5\30\r\2\u012b\u0133\5V,\2\u012c\u0133\5\u00a6"
            + "T\2\u012d\u0133\5\u00aeX\2\u012e\u0133\5b\62\2\u012f\u0133\5\34\17\2\u0130"
            + "\u0133\5\u00b4[\2\u0131\u0133\5\u00c0a\2\u0132\u011c\3\2\2\2\u0132\u011d"
            + "\3\2\2\2\u0132\u011e\3\2\2\2\u0132\u011f\3\2\2\2\u0132\u0120\3\2\2\2\u0132"
            + "\u0121\3\2\2\2\u0132\u0122\3\2\2\2\u0132\u0123\3\2\2\2\u0132\u0124\3\2"
            + "\2\2\u0132\u0125\3\2\2\2\u0132\u0126\3\2\2\2\u0132\u0127\3\2\2\2\u0132"
            + "\u0128\3\2\2\2\u0132\u0129\3\2\2\2\u0132\u012a\3\2\2\2\u0132\u012b\3\2"
            + "\2\2\u0132\u012c\3\2\2\2\u0132\u012d\3\2\2\2\u0132\u012e\3\2\2\2\u0132"
            + "\u012f\3\2\2\2\u0132\u0130\3\2\2\2\u0132\u0131\3\2\2\2\u0133\5\3\2\2\2"
            + "\u0134\u0135\7=\2\2\u0135\u0139\7\u0136\2\2\u0136\u0137\7\u008c\2\2\u0137"
            + "\u0138\7\u00cc\2\2\u0138\u013a\7o\2\2\u0139\u0136\3\2\2\2\u0139\u013a"
            + "\3\2\2\2\u013a\u013b\3\2\2\2\u013b\u013c\5\u00fc\177\2\u013c\u013d\7\u00ad"
            + "\2\2\u013d\u013e\5\u00fc\177\2\u013e\7\3\2\2\2\u013f\u0140\t\2\2\2\u0140"
            + "\u0142\7\u018e\2\2\u0141\u013f\3\2\2\2\u0141\u0142\3\2\2\2\u0142\u0143"
            + "\3\2\2\2\u0143\u0144\t\2\2\2\u0144\t\3\2\2\2\u0145\u014c\5\b\5\2\u0146"
            + "\u0147\7\u01a1\2\2\u0147\u0149\7\u018e\2\2\u0148\u0146\3\2\2\2\u0148\u0149"
            + "\3\2\2\2\u0149\u014a\3\2\2\2\u014a\u014c\5\b\5\2\u014b\u0145\3\2\2\2\u014b"
            + "\u0148\3\2\2\2\u014c\13\3\2\2\2\u014d\u0151\5\16\b\2\u014e\u0151\5\20"
            + "\t\2\u014f\u0151\5\22\n\2\u0150\u014d\3\2\2\2\u0150\u014e\3\2\2\2\u0150"
            + "\u014f\3\2\2\2\u0151\r\3\2\2\2\u0152\u0154\5\u0106\u0084\2\u0153\u0155"
            + "\7\u018a\2\2\u0154\u0153\3\2\2\2\u0154\u0155\3\2\2\2\u0155\u0156\3\2\2"
            + "\2\u0156\u0157\7\u0190\2\2\u0157\u0158\5\u00d6l\2\u0158\u0163\3\2\2\2"
            + "\u0159\u015a\7\u019a\2\2\u015a\u015b\5\u0106\u0084\2\u015b\u015d\7\u019d"
            + "\2\2\u015c\u015e\7\u018a\2\2\u015d\u015c\3\2\2\2\u015d\u015e\3\2\2\2\u015e"
            + "\u015f\3\2\2\2\u015f\u0160\7\u0190\2\2\u0160\u0161\5\u00d6l\2\u0161\u0163"
            + "\3\2\2\2\u0162\u0152\3\2\2\2\u0162\u0159\3\2\2\2\u0163\17\3\2\2\2\u0164"
            + "\u0165\7\u019a\2\2\u0165\u016a\5\u0106\u0084\2\u0166\u0167\7\u018b\2\2"
            + "\u0167\u0169\5\u0106\u0084\2\u0168\u0166\3\2\2\2\u0169\u016c\3\2\2\2\u016a"
            + "\u0168\3\2\2\2\u016a\u016b\3\2\2\2\u016b\u016d\3\2\2\2\u016c\u016a\3\2"
            + "\2\2\u016d\u016f\7\u019d\2\2\u016e\u0170\7\u018a\2\2\u016f\u016e\3\2\2"
            + "\2\u016f\u0170\3\2\2\2\u0170\u0171\3\2\2\2\u0171\u0172\7\u0190\2\2\u0172"
            + "\u0173\7\u019a\2\2\u0173\u0178\5\u00d6l\2\u0174\u0175\7\u018b\2\2\u0175"
            + "\u0177\5\u00d6l\2\u0176\u0174\3\2\2\2\u0177\u017a\3\2\2\2\u0178\u0176"
            + "\3\2\2\2\u0178\u0179\3\2\2\2\u0179\u017b\3\2\2\2\u017a\u0178\3\2\2\2\u017b"
            + "\u017c\7\u019d\2\2\u017c\21\3\2\2\2\u017d\u018a\5\u0106\u0084\2\u017e"
            + "\u017f\7\u019a\2\2\u017f\u0184\5\u0106\u0084\2\u0180\u0181\7\u018b\2\2"
            + "\u0181\u0183\5\u0106\u0084\2\u0182\u0180\3\2\2\2\u0183\u0186\3\2\2\2\u0184"
            + "\u0182\3\2\2\2\u0184\u0185\3\2\2\2\u0185\u0187\3\2\2\2\u0186\u0184\3\2"
            + "\2\2\u0187\u0188\7\u019d\2\2\u0188\u018a\3\2\2\2\u0189\u017d\3\2\2\2\u0189"
            + "\u017e\3\2\2\2\u018a\u018c\3\2\2\2\u018b\u018d\7\u018a\2\2\u018c\u018b"
            + "\3\2\2\2\u018c\u018d\3\2\2\2\u018d\u018e\3\2\2\2\u018e\u018f\7\u0190\2"
            + "\2\u018f\u0190\7\u019a\2\2\u0190\u0191\5b\62\2\u0191\u0192\7\u019d\2\2"
            + "\u0192\23\3\2\2\2\u0193\u0194\7\u011a\2\2\u0194\u0195\7=\2\2\u0195\u0196"
            + "\7\u0136\2\2\u0196\u0197\5\u0106\u0084\2\u0197\25\3\2\2\2\u0198\u0199"
            + "\7\u011a\2\2\u0199\u019a\7\u0129\2\2\u019a\27\3\2\2\2\u019b\u019c\7\u011a"
            + "\2\2\u019c\u019d\7\u0092\2\2\u019d\u019e\7|\2\2\u019e\u01a1\5\u0092J\2"
            + "\u019f\u01a0\7|\2\2\u01a0\u01a2\7\u01a1\2\2\u01a1\u019f\3\2\2\2\u01a1"
            + "\u01a2\3\2\2\2\u01a2\31\3\2\2\2\u01a3\u01a5\7=\2\2\u01a4\u01a6\7\u012e"
            + "\2\2\u01a5\u01a4\3\2\2\2\u01a5\u01a6\3\2\2\2\u01a6\u01a7\3\2\2\2\u01a7"
            + "\u01ab\7\u0136\2\2\u01a8\u01a9\7\u008c\2\2\u01a9\u01aa\7\u00cc\2\2\u01aa"
            + "\u01ac\7o\2\2\u01ab\u01a8\3\2\2\2\u01ab\u01ac\3\2\2\2\u01ac\u01ad\3\2"
            + "\2\2\u01ad\u01af\5\u00fc\177\2\u01ae\u01b0\5\60\31\2\u01af\u01ae\3\2\2"
            + "\2\u01af\u01b0\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01b2\5\36\20\2\u01b2"
            + "\u01c1\3\2\2\2\u01b3\u01b4\7=\2\2\u01b4\u01b5\7\u0149\2\2\u01b5\u01b9"
            + "\7\u0136\2\2\u01b6\u01b7\7\u008c\2\2\u01b7\u01b8\7\u00cc\2\2\u01b8\u01ba"
            + "\7o\2\2\u01b9\u01b6\3\2\2\2\u01b9\u01ba\3\2\2\2\u01ba\u01bb\3\2\2\2\u01bb"
            + "\u01be\5\u00fc\177\2\u01bc\u01bf\5\36\20\2\u01bd\u01bf\5\u0080A\2\u01be"
            + "\u01bc\3\2\2\2\u01be\u01bd\3\2\2\2\u01bf\u01c1\3\2\2\2\u01c0\u01a3\3\2"
            + "\2\2\u01c0\u01b3\3\2\2\2\u01c1\33\3\2\2\2\u01c2\u01c3\7=\2\2\u01c3\u01c4"
            + "\7\u0178\2\2\u01c4\u01c5\5\u00fe\u0080\2\u01c5\u01c6\7\u008b\2\2\u01c6"
            + "\u01c7\7\37\2\2\u01c7\u01c8\5\u010a\u0086\2\u01c8\35\3\2\2\2\u01c9\u01cb"
            + "\7\f\2\2\u01ca\u01c9\3\2\2\2\u01ca\u01cb\3\2\2\2\u01cb\u01cc\3\2\2\2\u01cc"
            + "\u01cd\7\u019a\2\2\u01cd\u01ce\5b\62\2\u01ce\u01cf\7\u019d\2\2\u01cf\u01d9"
            + "\3\2\2\2\u01d0\u01d2\7\f\2\2\u01d1\u01d0\3\2\2\2\u01d1\u01d2\3\2\2\2\u01d2"
            + "\u01d3\3\2\2\2\u01d3\u01d9\5b\62\2\u01d4\u01d5\7\u019a\2\2\u01d5\u01d6"
            + "\5 \21\2\u01d6\u01d7\7\u019d\2\2\u01d7\u01d9\3\2\2\2\u01d8\u01ca\3\2\2"
            + "\2\u01d8\u01d1\3\2\2\2\u01d8\u01d4\3\2\2\2\u01d9\u01db\3\2\2\2\u01da\u01dc"
            + "\5\66\34\2\u01db\u01da\3\2\2\2\u01db\u01dc\3\2\2\2\u01dc\37\3\2\2\2\u01dd"
            + "\u01e2\5\"\22\2\u01de\u01df\7\u018b\2\2\u01df\u01e1\5\"\22\2\u01e0\u01de"
            + "\3\2\2\2\u01e1\u01e4\3\2\2\2\u01e2\u01e0\3\2\2\2\u01e2\u01e3\3\2\2\2\u01e3"
            + "!\3\2\2\2\u01e4\u01e2\3\2\2\2\u01e5\u01e6\5(\25\2\u01e6\u01ea\5H%\2\u01e7"
            + "\u01e9\5F$\2\u01e8\u01e7\3\2\2\2\u01e9\u01ec\3\2\2\2\u01ea\u01e8\3\2\2"
            + "\2\u01ea\u01eb\3\2\2\2\u01eb\u01f0\3\2\2\2\u01ec\u01ea\3\2\2\2\u01ed\u01ef"
            + "\5*\26\2\u01ee\u01ed\3\2\2\2\u01ef\u01f2\3\2\2\2\u01f0\u01ee\3\2\2\2\u01f0"
            + "\u01f1\3\2\2\2\u01f1\u0202\3\2\2\2\u01f2\u01f0\3\2\2\2\u01f3\u01f4\78"
            + "\2\2\u01f4\u01f6\5\u0100\u0081\2\u01f5\u01f3\3\2\2\2\u01f5\u01f6\3\2\2"
            + "\2\u01f6\u01f7\3\2\2\2\u01f7\u0202\5,\27\2\u01f8\u01f9\t\3\2\2\u01f9\u01fb"
            + "\7\u0092\2\2\u01fa\u01fc\5\u0106\u0084\2\u01fb\u01fa\3\2\2\2\u01fb\u01fc"
            + "\3\2\2\2\u01fc\u01fd\3\2\2\2\u01fd\u01ff\5$\23\2\u01fe\u0200\5&\24\2\u01ff"
            + "\u01fe\3\2\2\2\u01ff\u0200\3\2\2\2\u0200\u0202\3\2\2\2\u0201\u01e5\3\2"
            + "\2\2\u0201\u01f5\3\2\2\2\u0201\u01f8\3\2\2\2\u0202#\3\2\2\2\u0203\u0204"
            + "\7\u019a\2\2\u0204\u0209\5\u0100\u0081\2\u0205\u0206\7\u018b\2\2\u0206"
            + "\u0208\5\u0100\u0081\2\u0207\u0205\3\2\2\2\u0208\u020b\3\2\2\2\u0209\u0207"
            + "\3\2\2\2\u0209\u020a\3\2\2\2\u020a\u020c\3\2\2\2\u020b\u0209\3\2\2\2\u020c"
            + "\u020d\7\u019d\2\2\u020d%\3\2\2\2\u020e\u020f\7\u0148\2\2\u020f\u0210"
            + "\5\u0106\u0084\2\u0210\'\3\2\2\2\u0211\u0212\5\u0100\u0081\2\u0212)\3"
            + "\2\2\2\u0213\u0234\5B\"\2\u0214\u0216\7\u00cc\2\2\u0215\u0214\3\2\2\2"
            + "\u0215\u0216\3\2\2\2\u0216\u0217\3\2\2\2\u0217\u0234\7\u00ce\2\2\u0218"
            + "\u0219\7\u00e7\2\2\u0219\u0234\7\u00a7\2\2\u021a\u0234\7\u014a\2\2\u021b"
            + "\u021c\7\u00f6\2\2\u021c\u021d\5\u00fc\177\2\u021d\u021e\7\u019a\2\2\u021e"
            + "\u021f\5\u0100\u0081\2\u021f\u0223\7\u019d\2\2\u0220\u0222\5.\30\2\u0221"
            + "\u0220\3\2\2\2\u0222\u0225\3\2\2\2\u0223\u0221\3\2\2\2\u0223\u0224\3\2"
            + "\2\2\u0224\u0234\3\2\2\2\u0225\u0223\3\2\2\2\u0226\u0227\7\u008a\2\2\u0227"
            + "\u0228\7\u019a\2\2\u0228\u022d\7\u01a4\2\2\u0229\u022a\7\u018b\2\2\u022a"
            + "\u022c\7\u01a4\2\2\u022b\u0229\3\2\2\2\u022c\u022f\3\2\2\2\u022d\u022b"
            + "\3\2\2\2\u022d\u022e\3\2\2\2\u022e\u0230\3\2\2\2\u022f\u022d\3\2\2\2\u0230"
            + "\u0234\7\u019d\2\2\u0231\u0234\7\22\2\2\u0232\u0234\7f\2\2\u0233\u0213"
            + "\3\2\2\2\u0233\u0215\3\2\2\2\u0233\u0218\3\2\2\2\u0233\u021a\3\2\2\2\u0233"
            + "\u021b\3\2\2\2\u0233\u0226\3\2\2\2\u0233\u0231\3\2\2\2\u0233\u0232\3\2"
            + "\2\2\u0234+\3\2\2\2\u0235\u0236\7\u00e7\2\2\u0236\u0238\7\u00a7\2\2\u0237"
            + "\u0239\7,\2\2\u0238\u0237\3\2\2\2\u0238\u0239\3\2\2\2\u0239\u023a\3\2"
            + "\2\2\u023a\u023b\7\u019a\2\2\u023b\u023d\5\u0100\u0081\2\u023c\u023e\t"
            + "\4\2\2\u023d\u023c\3\2\2\2\u023d\u023e\3\2\2\2\u023e\u0246\3\2\2\2\u023f"
            + "\u0240\7\u018b\2\2\u0240\u0242\5\u0100\u0081\2\u0241\u0243\t\4\2\2\u0242"
            + "\u0241\3\2\2\2\u0242\u0243\3\2\2\2\u0243\u0245\3\2\2\2\u0244\u023f\3\2"
            + "\2\2\u0245\u0248\3\2\2\2\u0246\u0244\3\2\2\2\u0246\u0247\3\2\2\2\u0247"
            + "\u0249\3\2\2\2\u0248\u0246\3\2\2\2\u0249\u024b\7\u019d\2\2\u024a\u024c"
            + "\7f\2\2\u024b\u024a\3\2\2\2\u024b\u024c\3\2\2\2\u024c\u026c\3\2\2\2\u024d"
            + "\u024e\7y\2\2\u024e\u024f\7\u00a7\2\2\u024f\u0250\7\u019a\2\2\u0250\u0255"
            + "\5\u0100\u0081\2\u0251\u0252\7\u018b\2\2\u0252\u0254\5\u0100\u0081\2\u0253"
            + "\u0251\3\2\2\2\u0254\u0257\3\2\2\2\u0255\u0253\3\2\2\2\u0255\u0256\3\2"
            + "\2\2\u0256\u0258\3\2\2\2\u0257\u0255\3\2\2\2\u0258\u0259\7\u019d\2\2\u0259"
            + "\u025a\7\u00f6\2\2\u025a\u025b\5\u00fc\177\2\u025b\u025c\7\u019a\2\2\u025c"
            + "\u0261\5\u0100\u0081\2\u025d\u025e\7\u018b\2\2\u025e\u0260\5\u0100\u0081"
            + "\2\u025f\u025d\3\2\2\2\u0260\u0263\3\2\2\2\u0261\u025f\3\2\2\2\u0261\u0262"
            + "\3\2\2\2\u0262\u0264\3\2\2\2\u0263\u0261\3\2\2\2\u0264\u0268\7\u019d\2"
            + "\2\u0265\u0267\5.\30\2\u0266\u0265\3\2\2\2\u0267\u026a\3\2\2\2\u0268\u0266"
            + "\3\2\2\2\u0268\u0269\3\2\2\2\u0269\u026c\3\2\2\2\u026a\u0268\3\2\2\2\u026b"
            + "\u0235\3\2\2\2\u026b\u024d\3\2\2\2\u026c-\3\2\2\2\u026d\u026e\7\u00d3"
            + "\2\2\u026e\u0277\t\5\2\2\u026f\u0270\7\u00c7\2\2\u0270\u0278\7\4\2\2\u0271"
            + "\u0278\7\u00fb\2\2\u0272\u0273\7\u0116\2\2\u0273\u0278\7\u00ce\2\2\u0274"
            + "\u0275\7\u0116\2\2\u0275\u0278\7O\2\2\u0276\u0278\7#\2\2\u0277\u026f\3"
            + "\2\2\2\u0277\u0271\3\2\2\2\u0277\u0272\3\2\2\2\u0277\u0274\3\2\2\2\u0277"
            + "\u0276\3\2\2\2\u0278/\3\2\2\2\u0279\u027b\5\62\32\2\u027a\u0279\3\2\2"
            + "\2\u027b\u027c\3\2\2\2\u027c\u027a\3\2\2\2\u027c\u027d\3\2\2\2\u027d\61"
            + "\3\2\2\2\u027e\u027f\7\u018b\2\2\u027f\u0280\5\64\33\2\u0280\63\3\2\2"
            + "\2\u0281\u0283\7\u00c7\2\2\u0282\u0281\3\2\2\2\u0282\u0283\3\2\2\2\u0283"
            + "\u0284\3\2\2\2\u0284\u0285\t\6\2\2\u0285\65\3\2\2\2\u0286\u0288\58\35"
            + "\2\u0287\u0286\3\2\2\2\u0288\u0289\3\2\2\2\u0289\u0287\3\2\2\2\u0289\u028a"
            + "\3\2\2\2\u028a\67\3\2\2\2\u028b\u028c\7\u00d3\2\2\u028c\u028d\7\64\2\2"
            + "\u028d\u028e\t\7\2\2\u028e\u0291\7\u0107\2\2\u028f\u0291\5:\36\2\u0290"
            + "\u028b\3\2\2\2\u0290\u028f\3\2\2\2\u02919\3\2\2\2\u0292\u0294\7\22\2\2"
            + "\u0293\u0295\7\u0190\2\2\u0294\u0293\3\2\2\2\u0294\u0295\3\2\2\2\u0295"
            + "\u0296\3\2\2\2\u0296\u02ae\5\u00d6l\2\u0297\u0299\7\61\2\2\u0298\u029a"
            + "\7\u0190\2\2\u0299\u0298\3\2\2\2\u0299\u029a\3\2\2\2\u029a\u029b\3\2\2"
            + "\2\u029b\u02ae\5\u00d6l\2\u029c\u029e\7O\2\2\u029d\u029c\3\2\2\2\u029d"
            + "\u029e\3\2\2\2\u029e\u02a2\3\2\2\2\u029f\u02a0\7(\2\2\u02a0\u02a3\7\u0116"
            + "\2\2\u02a1\u02a3\7)\2\2\u02a2\u029f\3\2\2\2\u02a2\u02a1\3\2\2\2\u02a3"
            + "\u02a5\3\2\2\2\u02a4\u02a6\7\u0190\2\2\u02a5\u02a4\3\2\2\2\u02a5\u02a6"
            + "\3\2\2\2\u02a6\u02a7\3\2\2\2\u02a7\u02ae\5\u00d6l\2\u02a8\u02aa\7h\2\2"
            + "\u02a9\u02ab\7\u0190\2\2\u02aa\u02a9\3\2\2\2\u02aa\u02ab\3\2\2\2\u02ab"
            + "\u02ac\3\2\2\2\u02ac\u02ae\5\u00d6l\2\u02ad\u0292\3\2\2\2\u02ad\u0297"
            + "\3\2\2\2\u02ad\u029d\3\2\2\2\u02ad\u02a8\3\2\2\2\u02ae;\3\2\2\2\u02af"
            + "\u02b1\7=\2\2\u02b0\u02b2\7\u014a\2\2\u02b1\u02b0\3\2\2\2\u02b1\u02b2"
            + "\3\2\2\2\u02b2\u02b3\3\2\2\2\u02b3\u02b4\7\u0092\2\2\u02b4\u02b5\5\u0106"
            + "\u0084\2\u02b5\u02b6\7\u00d3\2\2\u02b6\u02b7\5\u0092J\2\u02b7\u02b8\7"
            + "\u019a\2\2\u02b8\u02bd\5> \2\u02b9\u02ba\7\u018b\2\2\u02ba\u02bc\5> \2"
            + "\u02bb\u02b9\3\2\2\2\u02bc\u02bf\3\2\2\2\u02bd\u02bb\3\2\2\2\u02bd\u02be"
            + "\3\2\2\2\u02be\u02c0\3\2\2\2\u02bf\u02bd\3\2\2\2\u02c0\u02c1\7\u019d\2"
            + "\2\u02c1=\3\2\2\2\u02c2\u02c4\5\u0106\u0084\2\u02c3\u02c5\t\4\2\2\u02c4"
            + "\u02c3\3\2\2\2\u02c4\u02c5\3\2\2\2\u02c5?\3\2\2\2\u02c6\u02c7\7a\2\2\u02c7"
            + "\u02c8\7\u0092\2\2\u02c8\u02c9\7\u01a1\2\2\u02c9\u02ca\7\u00d3\2\2\u02ca"
            + "\u02cb\5\u0092J\2\u02cbA\3\2\2\2\u02cc\u02ce\7\u018a\2\2\u02cd\u02cc\3"
            + "\2\2\2\u02cd\u02ce\3\2\2\2\u02ce\u02cf\3\2\2\2\u02cf\u02d0\7\u0190\2\2"
            + "\u02d0\u02d9\5\u00d6l\2\u02d1\u02d3\7\u0159\2\2\u02d2\u02d1\3\2\2\2\u02d2"
            + "\u02d3\3\2\2\2\u02d3\u02d4\3\2\2\2\u02d4\u02d6\7O\2\2\u02d5\u02d7\5\u00d6"
            + "l\2\u02d6\u02d5\3\2\2\2\u02d6\u02d7\3\2\2\2\u02d7\u02d9\3\2\2\2\u02d8"
            + "\u02cd\3\2\2\2\u02d8\u02d2\3\2\2\2\u02d9C\3\2\2\2\u02da\u02db\7\u011a"
            + "\2\2\u02db\u02dc\7\u0139\2\2\u02dcE\3\2\2\2\u02dd\u02df\7\u00cc\2\2\u02de"
            + "\u02dd\3\2\2\2\u02de\u02df\3\2\2\2\u02df\u02e0\3\2\2\2\u02e0\u02e9\7\u00ce"
            + "\2\2\u02e1\u02e2\7(\2\2\u02e2\u02e3\7\u0116\2\2\u02e3\u02e9\5\u0106\u0084"
            + "\2\u02e4\u02e6\7\u00cc\2\2\u02e5\u02e4\3\2\2\2\u02e5\u02e6\3\2\2\2\u02e6"
            + "\u02e7\3\2\2\2\u02e7\u02e9\t\b\2\2\u02e8\u02de\3\2\2\2\u02e8\u02e1\3\2"
            + "\2\2\u02e8\u02e5\3\2\2\2\u02e9G\3\2\2\2\u02ea\u0306\7I\2\2\u02eb\u0306"
            + "\7_\2\2\u02ec\u0306\7w\2\2\u02ed\u0306\7\u0098\2\2\u02ee\u0306\7\u009c"
            + "\2\2\u02ef\u0306\7\u012f\2\2\u02f0\u0306\7\u0140\2\2\u02f1\u0306\7\u017a"
            + "\2\2\u02f2\u0306\7\u017b\2\2\u02f3\u0306\7\u017c\2\2\u02f4\u0306\7\u017d"
            + "\2\2\u02f5\u0306\7\u017e\2\2\u02f6\u0306\7\u017f\2\2\u02f7\u0306\7\u0180"
            + "\2\2\u02f8\u0306\7\u0181\2\2\u02f9\u0306\7\u0182\2\2\u02fa\u0306\7\u0183"
            + "\2\2\u02fb\u0306\7\u0184\2\2\u02fc\u0306\7\u0185\2\2\u02fd\u0306\7\u0186"
            + "\2\2\u02fe\u0306\7\u0187\2\2\u02ff\u0306\7\u0188\2\2\u0300\u0303\5\u0106"
            + "\u0084\2\u0301\u0302\7\3\2\2\u0302\u0304\t\t\2\2\u0303\u0301\3\2\2\2\u0303"
            + "\u0304\3\2\2\2\u0304\u0306\3\2\2\2\u0305\u02ea\3\2\2\2\u0305\u02eb\3\2"
            + "\2\2\u0305\u02ec\3\2\2\2\u0305\u02ed\3\2\2\2\u0305\u02ee\3\2\2\2\u0305"
            + "\u02ef\3\2\2\2\u0305\u02f0\3\2\2\2\u0305\u02f1\3\2\2\2\u0305\u02f2\3\2"
            + "\2\2\u0305\u02f3\3\2\2\2\u0305\u02f4\3\2\2\2\u0305\u02f5\3\2\2\2\u0305"
            + "\u02f6\3\2\2\2\u0305\u02f7\3\2\2\2\u0305\u02f8\3\2\2\2\u0305\u02f9\3\2"
            + "\2\2\u0305\u02fa\3\2\2\2\u0305\u02fb\3\2\2\2\u0305\u02fc\3\2\2\2\u0305"
            + "\u02fd\3\2\2\2\u0305\u02fe\3\2\2\2\u0305\u02ff\3\2\2\2\u0305\u0300\3\2"
            + "\2\2\u0306I\3\2\2\2\u0307\u0308\7\u019a\2\2\u0308\u030a\t\n\2\2\u0309"
            + "\u030b\t\13\2\2\u030a\u0309\3\2\2\2\u030a\u030b\3\2\2\2\u030b\u030e\3"
            + "\2\2\2\u030c\u030d\7\u018b\2\2\u030d\u030f\7\u01a4\2\2\u030e\u030c\3\2"
            + "\2\2\u030e\u030f\3\2\2\2\u030f\u0310\3\2\2\2\u0310\u0311\7\u019d\2\2\u0311"
            + "K\3\2\2\2\u0312\u0313\7=\2\2\u0313\u0317\t\f\2\2\u0314\u0315\7\u008c\2"
            + "\2\u0315\u0316\7\u00cc\2\2\u0316\u0318\7o\2\2\u0317\u0314\3\2\2\2\u0317"
            + "\u0318\3\2\2\2\u0318\u0319\3\2\2\2\u0319\u031d\7\u01a1\2\2\u031a\u031c"
            + "\5R*\2\u031b\u031a\3\2\2\2\u031c\u031f\3\2\2\2\u031d\u031b\3\2\2\2\u031d"
            + "\u031e\3\2\2\2\u031eM\3\2\2\2\u031f\u031d\3\2\2\2\u0320\u0321\7\u011a"
            + "\2\2\u0321\u0322\7F\2\2\u0322O\3\2\2\2\u0323\u0324\7a\2\2\u0324\u0327"
            + "\t\f\2\2\u0325\u0326\7\u008c\2\2\u0326\u0328\7o\2\2\u0327\u0325\3\2\2"
            + "\2\u0327\u0328\3\2\2\2\u0328\u0329\3\2\2\2\u0329\u032a\7\u01a1\2\2\u032a"
            + "Q\3\2\2\2\u032b\u032c\7\61\2\2\u032c\u0330\5\u00d6l\2\u032d\u032e\7\u00b2"
            + "\2\2\u032e\u0330\5\u00d6l\2\u032f\u032b\3\2\2\2\u032f\u032d\3\2\2\2\u0330"
            + "S\3\2\2\2\u0331\u0332\7a\2\2\u0332\u0335\7\u0136\2\2\u0333\u0334\7\u008c"
            + "\2\2\u0334\u0336\7o\2\2\u0335\u0333\3\2\2\2\u0335\u0336\3\2\2\2\u0336"
            + "\u0337\3\2\2\2\u0337\u0338\5\u0092J\2\u0338U\3\2\2\2\u0339\u0340\7\u0097"
            + "\2\2\u033a\u033b\7\u00db\2\2\u033b\u0341\7\u0136\2\2\u033c\u033e\7\u009f"
            + "\2\2\u033d\u033f\7\u0136\2\2\u033e\u033d\3\2\2\2\u033e\u033f\3\2\2\2\u033f"
            + "\u0341\3\2\2\2\u0340\u033a\3\2\2\2\u0340\u033c\3\2\2\2\u0341\u0342\3\2"
            + "\2\2\u0342\u0344\5\u0092J\2\u0343\u0345\5X-\2\u0344\u0343\3\2\2\2\u0344"
            + "\u0345\3\2\2\2\u0345\u0348\3\2\2\2\u0346\u0349\5b\62\2\u0347\u0349\5Z"
            + ".\2\u0348\u0346\3\2\2\2\u0348\u0347\3\2\2\2\u0349W\3\2\2\2\u034a\u034b"
            + "\7\u019a\2\2\u034b\u0350\5\u0106\u0084\2\u034c\u034d\7\u018b\2\2\u034d"
            + "\u034f\5\u0106\u0084\2\u034e\u034c\3\2\2\2\u034f\u0352\3\2\2\2\u0350\u034e"
            + "\3\2\2\2\u0350\u0351\3\2\2\2\u0351\u0353\3\2\2\2\u0352\u0350\3\2\2\2\u0353"
            + "\u0354\7\u019d\2\2\u0354Y\3\2\2\2\u0355\u0356\7\u0150\2\2\u0356\u035b"
            + "\5\\/\2\u0357\u0358\7\u018b\2\2\u0358\u035a\5\\/\2\u0359\u0357\3\2\2\2"
            + "\u035a\u035d\3\2\2\2\u035b\u0359\3\2\2\2\u035b\u035c\3\2\2\2\u035c[\3"
            + "\2\2\2\u035d\u035b\3\2\2\2\u035e\u035f\7\u019a\2\2\u035f\u0364\5\u00d6"
            + "l\2\u0360\u0361\7\u018b\2\2\u0361\u0363\5\u00d6l\2\u0362\u0360\3\2\2\2"
            + "\u0363\u0366\3\2\2\2\u0364\u0362\3\2\2\2\u0364\u0365\3\2\2\2\u0365\u0367"
            + "\3\2\2\2\u0366\u0364\3\2\2\2\u0367\u0368\7\u019d\2\2\u0368]\3\2\2\2\u0369"
            + "\u036b\7\u0147\2\2\u036a\u036c\7\u0136\2\2\u036b\u036a\3\2\2\2\u036b\u036c"
            + "\3\2\2\2\u036c\u036d\3\2\2\2\u036d\u036e\5\u0106\u0084\2\u036e_\3\2\2"
            + "\2\u036f\u0370\7\u014d\2\2\u0370\u0371\t\r\2\2\u0371a\3\2\2\2\u0372\u0374"
            + "\5d\63\2\u0373\u0372\3\2\2\2\u0373\u0374\3\2\2\2\u0374\u0375\3\2\2\2\u0375"
            + "\u0376\5j\66\2\u0376c\3\2\2\2\u0377\u0378\7\u0159\2\2\u0378\u037d\5f\64"
            + "\2\u0379\u037a\7\u018b\2\2\u037a\u037c\5f\64\2\u037b\u0379\3\2\2\2\u037c"
            + "\u037f\3\2\2\2\u037d\u037b\3\2\2\2\u037d\u037e\3\2\2\2\u037ee\3\2\2\2"
            + "\u037f\u037d\3\2\2\2\u0380\u0382\5\u0106\u0084\2\u0381\u0383\5h\65\2\u0382"
            + "\u0381\3\2\2\2\u0382\u0383\3\2\2\2\u0383\u0384\3\2\2\2\u0384\u0385\7\f"
            + "\2\2\u0385\u0386\7\u019a\2\2\u0386\u0387\5j\66\2\u0387\u0388\7\u019d\2"
            + "\2\u0388g\3\2\2\2\u0389\u038a\7\u019a\2\2\u038a\u038f\5\u0106\u0084\2"
            + "\u038b\u038c\7\u018b\2\2\u038c\u038e\5\u0106\u0084\2\u038d\u038b\3\2\2"
            + "\2\u038e\u0391\3\2\2\2\u038f\u038d\3\2\2\2\u038f\u0390\3\2\2\2\u0390\u0392"
            + "\3\2\2\2\u0391\u038f\3\2\2\2\u0392\u0393\7\u019d\2\2\u0393i\3\2\2\2\u0394"
            + "\u039a\5l\67\2\u0395\u0396\5n8\2\u0396\u0397\5l\67\2\u0397\u0399\3\2\2"
            + "\2\u0398\u0395\3\2\2\2\u0399\u039c\3\2\2\2\u039a\u0398\3\2\2\2\u039a\u039b"
            + "\3\2\2\2\u039b\u03a2\3\2\2\2\u039c\u039a\3\2\2\2\u039d\u039e\7\u019a\2"
            + "\2\u039e\u039f\5j\66\2\u039f\u03a0\7\u019d\2\2\u03a0\u03a2\3\2\2\2\u03a1"
            + "\u0394\3\2\2\2\u03a1\u039d\3\2\2\2\u03a2k\3\2\2\2\u03a3\u03a9\5p9\2\u03a4"
            + "\u03a5\7\u019a\2\2\u03a5\u03a6\5j\66\2\u03a6\u03a7\7\u019d\2\2\u03a7\u03a9"
            + "\3\2\2\2\u03a8\u03a3\3\2\2\2\u03a8\u03a4\3\2\2\2\u03a9m\3\2\2\2\u03aa"
            + "\u03ac\7\u0149\2\2\u03ab\u03ad\7\6\2\2\u03ac\u03ab\3\2\2\2\u03ac\u03ad"
            + "\3\2\2\2\u03ad\u03b7\3\2\2\2\u03ae\u03b0\7j\2\2\u03af\u03b1\7\6\2\2\u03b0"
            + "\u03af\3\2\2\2\u03b0\u03b1\3\2\2\2\u03b1\u03b7\3\2\2\2\u03b2\u03b4\7\u009d"
            + "\2\2\u03b3\u03b5\7\6\2\2\u03b4\u03b3\3\2\2\2\u03b4\u03b5\3\2\2\2\u03b5"
            + "\u03b7\3\2\2\2\u03b6\u03aa\3\2\2\2\u03b6\u03ae\3\2\2\2\u03b6\u03b2\3\2"
            + "\2\2\u03b7o\3\2\2\2\u03b8\u03b9\t\16\2\2\u03b9\u03bb\5r:\2\u03ba\u03bc"
            + "\5~@\2\u03bb\u03ba\3\2\2\2\u03bb\u03bc\3\2\2\2\u03bc\u03be\3\2\2\2\u03bd"
            + "\u03bf\5\u0080A\2\u03be\u03bd\3\2\2\2\u03be\u03bf\3\2\2\2\u03bf\u03c1"
            + "\3\2\2\2\u03c0\u03c2\5\u0094K\2\u03c1\u03c0\3\2\2\2\u03c1\u03c2\3\2\2"
            + "\2\u03c2\u03c4\3\2\2\2\u03c3\u03c5\5\u0096L\2\u03c4\u03c3\3\2\2\2\u03c4"
            + "\u03c5\3\2\2\2\u03c5\u03c8\3\2\2\2\u03c6\u03c9\5\u0098M\2\u03c7\u03c9"
            + "\5\u009aN\2\u03c8\u03c6\3\2\2\2\u03c8\u03c7\3\2\2\2\u03c8\u03c9\3\2\2"
            + "\2\u03c9\u03cb\3\2\2\2\u03ca\u03cc\5\u009cO\2\u03cb\u03ca\3\2\2\2\u03cb"
            + "\u03cc\3\2\2\2\u03cc\u03ce\3\2\2\2\u03cd\u03cf\5\u00a2R\2\u03ce\u03cd"
            + "\3\2\2\2\u03ce\u03cf\3\2\2\2\u03cfq\3\2\2\2\u03d0\u03d2\5t;\2\u03d1\u03d0"
            + "\3\2\2\2\u03d1\u03d2\3\2\2\2\u03d2\u03d4\3\2\2\2\u03d3\u03d5\5v<\2\u03d4"
            + "\u03d3\3\2\2\2\u03d4\u03d5\3\2\2\2\u03d5\u03d6\3\2\2\2\u03d6\u03db\5x"
            + "=\2\u03d7\u03d8\7\u018b\2\2\u03d8\u03da\5x=\2\u03d9\u03d7\3\2\2\2\u03da"
            + "\u03dd\3\2\2\2\u03db\u03d9\3\2\2\2\u03db\u03dc\3\2\2\2\u03dcs\3\2\2\2"
            + "\u03dd\u03db\3\2\2\2\u03de\u03df\t\17\2\2\u03dfu\3\2\2\2\u03e0\u03e1\7"
            + "\u0144\2\2\u03e1\u03e2\5\u00d6l\2\u03e2w\3\2\2\2\u03e3\u03e4\5\u0106\u0084"
            + "\2\u03e4\u03e5\7\u0190\2\2\u03e5\u03e7\3\2\2\2\u03e6\u03e3\3\2\2\2\u03e6"
            + "\u03e7\3\2\2\2\u03e7\u03e8\3\2\2\2\u03e8\u03ea\5\u00d6l\2\u03e9\u03eb"
            + "\5z>\2\u03ea\u03e9\3\2\2\2\u03ea\u03eb\3\2\2\2\u03eb\u03ee\3\2\2\2\u03ec"
            + "\u03ee\5|?\2\u03ed\u03e6\3\2\2\2\u03ed\u03ec\3\2\2\2\u03eey\3\2\2\2\u03ef"
            + "\u03f1\7\f\2\2\u03f0\u03ef\3\2\2\2\u03f0\u03f1\3\2\2\2\u03f1\u03f2\3\2"
            + "\2\2\u03f2\u0406\5\u0106\u0084\2\u03f3\u03f5\7\f\2\2\u03f4\u03f3\3\2\2"
            + "\2\u03f4\u03f5\3\2\2\2\u03f5\u03f6\3\2\2\2\u03f6\u03f7\7\u019a\2\2\u03f7"
            + "\u03fc\5\u0106\u0084\2\u03f8\u03f9\7\u018b\2\2\u03f9\u03fb\5\u0106\u0084"
            + "\2\u03fa\u03f8\3\2\2\2\u03fb\u03fe\3\2\2\2\u03fc\u03fa\3\2\2\2\u03fc\u03fd"
            + "\3\2\2\2\u03fd\u03ff\3\2\2\2\u03fe\u03fc\3\2\2\2\u03ff\u0400\7\u019d\2"
            + "\2\u0400\u0406\3\2\2\2\u0401\u0402\7\u019a\2\2\u0402\u0403\7\u0142\2\2"
            + "\u0403\u0404\7\u01a2\2\2\u0404\u0406\7\u019d\2\2\u0405\u03f0\3\2\2\2\u0405"
            + "\u03f4\3\2\2\2\u0405\u0401\3\2\2\2\u0406{\3\2\2\2\u0407\u0408\7\u01a1"
            + "\2\2\u0408\u040a\7\u018e\2\2\u0409\u0407\3\2\2\2\u0409\u040a\3\2\2\2\u040a"
            + "\u040b\3\2\2\2\u040b\u040c\7\u0198\2\2\u040c}\3\2\2\2\u040d\u040e\7\u009f"
            + "\2\2\u040e\u0413\5\u0106\u0084\2\u040f\u0410\7\u018b\2\2\u0410\u0412\5"
            + "\u0106\u0084\2\u0411\u040f\3\2\2\2\u0412\u0415\3\2\2\2\u0413\u0411\3\2"
            + "\2\2\u0413\u0414\3\2\2\2\u0414\177\3\2\2\2\u0415\u0413\3\2\2\2\u0416\u0417"
            + "\7|\2\2\u0417\u041b\5\u0082B\2\u0418\u041a\5\u0088E\2\u0419\u0418\3\2"
            + "\2\2\u041a\u041d\3\2\2\2\u041b\u0419\3\2\2\2\u041b\u041c\3\2\2\2\u041c"
            + "\u0081\3\2\2\2\u041d\u041b\3\2\2\2\u041e\u0422\5\u0084C\2\u041f\u0422"
            + "\5\u0086D\2\u0420\u0422\5\u008cG\2\u0421\u041e\3\2\2\2\u0421\u041f\3\2"
            + "\2\2\u0421\u0420\3\2\2\2\u0422\u0083\3\2\2\2\u0423\u0425\5\u0092J\2\u0424"
            + "\u0426\5\u0090I\2\u0425\u0424\3\2\2\2\u0425\u0426\3\2\2\2\u0426\u0428"
            + "\3\2\2\2\u0427\u0429\5\u00a0Q\2\u0428\u0427\3\2\2\2\u0428\u0429\3\2\2"
            + "\2\u0429\u0085\3\2\2\2\u042a\u042b\7\u019a\2\2\u042b\u042c\5b\62\2\u042c"
            + "\u042e\7\u019d\2\2\u042d\u042f\5\u0090I\2\u042e\u042d\3\2\2\2\u042e\u042f"
            + "\3\2\2\2\u042f\u0087\3\2\2\2\u0430\u0431\7\u018b\2\2\u0431\u0438\5\u0082"
            + "B\2\u0432\u0433\5\u008aF\2\u0433\u0434\5\u0082B\2\u0434\u0435\7\u00d3"
            + "\2\2\u0435\u0436\5\u00c6d\2\u0436\u0438\3\2\2\2\u0437\u0430\3\2\2\2\u0437"
            + "\u0432\3\2\2\2\u0438\u0089\3\2\2\2\u0439\u043b\7\u0094\2\2\u043a\u0439"
            + "\3\2\2\2\u043a\u043b\3\2\2\2\u043b\u043c\3\2\2\2\u043c\u0443\7\u00a4\2"
            + "\2\u043d\u043f\t\20\2\2\u043e\u0440\7\u00d9\2\2\u043f\u043e\3\2\2\2\u043f"
            + "\u0440\3\2\2\2\u0440\u0441\3\2\2\2\u0441\u0443\7\u00a4\2\2\u0442\u043a"
            + "\3\2\2\2\u0442\u043d\3\2\2\2\u0443\u008b\3\2\2\2\u0444\u0445\7\u0136\2"
            + "\2\u0445\u0446\7\u019a\2\2\u0446\u0447\7\u0150\2\2\u0447\u044c\5\u008e"
            + "H\2\u0448\u0449\7\u018b\2\2\u0449\u044b\5\u008eH\2\u044a\u0448\3\2\2\2"
            + "\u044b\u044e\3\2\2\2\u044c\u044a\3\2\2\2\u044c\u044d\3\2\2\2\u044d\u044f"
            + "\3\2\2\2\u044e\u044c\3\2\2\2\u044f\u0451\7\u019d\2\2\u0450\u0452\5\u0090"
            + "I\2\u0451\u0450\3\2\2\2\u0451\u0452\3\2\2\2\u0452\u008d\3\2\2\2\u0453"
            + "\u0460\5\u00d6l\2\u0454\u0455\7\u019a\2\2\u0455\u045a\5\u00d6l\2\u0456"
            + "\u0457\7\u018b\2\2\u0457\u0459\5\u00d6l\2\u0458\u0456\3\2\2\2\u0459\u045c"
            + "\3\2\2\2\u045a\u0458\3\2\2\2\u045a\u045b\3\2\2\2\u045b\u045d\3\2\2\2\u045c"
            + "\u045a\3\2\2\2\u045d\u045e\7\u019d\2\2\u045e\u0460\3\2\2\2\u045f\u0453"
            + "\3\2\2\2\u045f\u0454\3\2\2\2\u0460\u008f\3\2\2\2\u0461\u0463\6I\2\2\u0462"
            + "\u0464\7\f\2\2\u0463\u0462\3\2\2\2\u0463\u0464\3\2\2\2\u0464\u0465\3\2"
            + "\2\2\u0465\u0470\5\u0106\u0084\2\u0466\u0467\7\u019a\2\2\u0467\u046c\7"
            + "\u01a1\2\2\u0468\u0469\7\u018b\2\2\u0469\u046b\7\u01a1\2\2\u046a\u0468"
            + "\3\2\2\2\u046b\u046e\3\2\2\2\u046c\u046a\3\2\2\2\u046c\u046d\3\2\2\2\u046d"
            + "\u046f\3\2\2\2\u046e\u046c\3\2\2\2\u046f\u0471\7\u019d\2\2\u0470\u0466"
            + "\3\2\2\2\u0470\u0471\3\2\2\2\u0471\u0091\3\2\2\2\u0472\u0473\5\u0106\u0084"
            + "\2\u0473\u0093\3\2\2\2\u0474\u0475\7\u0157\2\2\u0475\u0476\5\u00c6d\2"
            + "\u0476\u0095\3\2\2\2\u0477\u0478\7\u0084\2\2\u0478\u0479\7\37\2\2\u0479"
            + "\u047e\5\u00d6l\2\u047a\u047b\7\u018b\2\2\u047b\u047d\5\u00d6l\2\u047c"
            + "\u047a\3\2\2\2\u047d\u0480\3\2\2\2\u047e\u047c\3\2\2\2\u047e\u047f\3\2"
            + "\2\2\u047f\u0097\3\2\2\2\u0480\u047e\3\2\2\2\u0481\u0482\7\u0087\2\2\u0482"
            + "\u0483\5\u00c6d\2\u0483\u0099\3\2\2\2\u0484\u0485\7\u00ef\2\2\u0485\u0486"
            + "\5\u00c6d\2\u0486\u009b\3\2\2\2\u0487\u0488\7\u00d7\2\2\u0488\u0489\7"
            + "\37\2\2\u0489\u048e\5\u009eP\2\u048a\u048b\7\u018b\2\2\u048b\u048d\5\u009e"
            + "P\2\u048c\u048a\3\2\2\2\u048d\u0490\3\2\2\2\u048e\u048c\3\2\2\2\u048e"
            + "\u048f\3\2\2\2\u048f\u009d\3\2\2\2\u0490\u048e\3\2\2\2\u0491\u0493\5\u00d6"
            + "l\2\u0492\u0494\t\4\2\2\u0493\u0492\3\2\2\2\u0493\u0494\3\2\2\2\u0494"
            + "\u009f\3\2\2\2\u0495\u0496\7\u010e\2\2\u0496\u0497\7\u019a\2\2\u0497\u0498"
            + "\7\u01a4\2\2\u0498\u0499\7\u00e2\2\2\u0499\u049a\7\u019d\2\2\u049a\u00a1"
            + "\3\2\2\2\u049b\u049d\5\u00a4S\2\u049c\u049b\3\2\2\2\u049d\u049e\3\2\2"
            + "\2\u049e\u049c\3\2\2\2\u049e\u049f\3\2\2\2\u049f\u00a3\3\2\2\2\u04a0\u04a1"
            + "\7\u00ae\2\2\u04a1\u04ac\5\u00d6l\2\u04a2\u04a3\7\u0159\2\2\u04a3\u04a9"
            + "\t\21\2\2\u04a4\u04a5\7\u014d\2\2\u04a5\u04a6\7\t\2\2\u04a6\u04a7\7\u00a5"
            + "\2\2\u04a7\u04a8\t\22\2\2\u04a8\u04aa\7\u00b5\2\2\u04a9\u04a4\3\2\2\2"
            + "\u04a9\u04aa\3\2\2\2\u04aa\u04ac\3\2\2\2\u04ab\u04a0\3\2\2\2\u04ab\u04a2"
            + "\3\2\2\2\u04ac\u00a5\3\2\2\2\u04ad\u04ae\7\u014b\2\2\u04ae\u04af\5\u00aa"
            + "V\2\u04af\u04b0\7\u0116\2\2\u04b0\u04b2\5\u00a8U\2\u04b1\u04b3\5\u0094"
            + "K\2\u04b2\u04b1\3\2\2\2\u04b2\u04b3\3\2\2\2\u04b3\u04b5\3\2\2\2\u04b4"
            + "\u04b6\5\u00acW\2\u04b5\u04b4\3\2\2\2\u04b5\u04b6\3\2\2\2\u04b6\u00a7"
            + "\3\2\2\2\u04b7\u04bc\5\f\7\2\u04b8\u04b9\7\u018b\2\2\u04b9\u04bb\5\f\7"
            + "\2\u04ba\u04b8\3\2\2\2\u04bb\u04be\3\2\2\2\u04bc\u04ba\3\2\2\2\u04bc\u04bd"
            + "\3\2\2\2\u04bd\u00a9\3\2\2\2\u04be\u04bc\3\2\2\2\u04bf\u04c1\5\u0092J"
            + "\2\u04c0\u04c2\5\u0080A\2\u04c1\u04c0\3\2\2\2\u04c1\u04c2\3\2\2\2\u04c2"
            + "\u04c8\3\2\2\2\u04c3\u04c4\7\u019a\2\2\u04c4\u04c5\5b\62\2\u04c5\u04c6"
            + "\7\u019d\2\2\u04c6\u04c8\3\2\2\2\u04c7\u04bf\3\2\2\2\u04c7\u04c3\3\2\2"
            + "\2\u04c8\u04cd\3\2\2\2\u04c9\u04cb\7\f\2\2\u04ca\u04c9\3\2\2\2\u04ca\u04cb"
            + "\3\2\2\2\u04cb\u04cc\3\2\2\2\u04cc\u04ce\5\u0106\u0084\2\u04cd\u04ca\3"
            + "\2\2\2\u04cd\u04ce\3\2\2\2\u04ce\u00ab\3\2\2\2\u04cf\u04d0\7c\2\2\u04d0"
            + "\u04d1\5V,\2\u04d1\u00ad\3\2\2\2\u04d2\u04d4\7T\2\2\u04d3\u04d5\7|\2\2"
            + "\u04d4\u04d3\3\2\2\2\u04d4\u04d5\3\2\2\2\u04d5\u04d6\3\2\2\2\u04d6\u04d8"
            + "\5\u0092J\2\u04d7\u04d9\5\u00b0Y\2\u04d8\u04d7\3\2\2\2\u04d8\u04d9\3\2"
            + "\2\2\u04d9\u04dc\3\2\2\2\u04da\u04dd\5\u0094K\2\u04db\u04dd\7\6\2\2\u04dc"
            + "\u04da\3\2\2\2\u04dc\u04db\3\2\2\2\u04dc\u04dd\3\2\2\2\u04dd\u00af\3\2"
            + "\2\2\u04de\u04e0\6Y\3\2\u04df\u04e1\7\f\2\2\u04e0\u04df\3\2\2\2\u04e0"
            + "\u04e1\3\2\2\2\u04e1\u04e2\3\2\2\2\u04e2\u04e3\5\u0106\u0084\2\u04e3\u00b1"
            + "\3\2\2\2\u04e4\u04e6\t\23\2\2\u04e5\u04e7\t\24\2\2\u04e6\u04e5\3\2\2\2"
            + "\u04e6\u04e7\3\2\2\2\u04e7\u04e8\3\2\2\2\u04e8\u04e9\5\n\6\2\u04e9\u00b3"
            + "\3\2\2\2\u04ea\u04ec\7\u00b0\2\2\u04eb\u04ed\7A\2\2\u04ec\u04eb\3\2\2"
            + "\2\u04ec\u04ed\3\2\2\2\u04ed\u04ee\3\2\2\2\u04ee\u04ef\7\u0096\2\2\u04ef"
            + "\u04f0\5\u010a\u0086\2\u04f0\u04f2\7\u0143\2\2\u04f1\u04f3\7\u0136\2\2"
            + "\u04f2\u04f1\3\2\2\2\u04f2\u04f3\3\2\2\2\u04f3\u04f4\3\2\2\2\u04f4\u04f6"
            + "\5\u00fc\177\2\u04f5\u04f7\5\u00b6\\\2\u04f6\u04f5\3\2\2\2\u04f6\u04f7"
            + "\3\2\2\2\u04f7\u04f9\3\2\2\2\u04f8\u04fa\5\u00bc_\2\u04f9\u04f8\3\2\2"
            + "\2\u04f9\u04fa\3\2\2\2\u04fa\u04fc\3\2\2\2\u04fb\u04fd\5\u00be`\2\u04fc"
            + "\u04fb\3\2\2\2\u04fc\u04fd\3\2\2\2\u04fd\u00b5\3\2\2\2\u04fe\u04ff\7\u019a"
            + "\2\2\u04ff\u0500\5\u00b8]\2\u0500\u0501\7\u019d\2\2\u0501\u00b7\3\2\2"
            + "\2\u0502\u0507\5\u00ba^\2\u0503\u0504\7\u018b\2\2\u0504\u0506\5\u00ba"
            + "^\2\u0505\u0503\3\2\2\2\u0506\u0509\3\2\2\2\u0507\u0505\3\2\2\2\u0507"
            + "\u0508\3\2\2\2\u0508\u00b9\3\2\2\2\u0509\u0507\3\2\2\2\u050a\u050b\5\u0106"
            + "\u0084\2\u050b\u050c\5\u00d6l\2\u050c\u00bb\3\2\2\2\u050d\u0510\7t\2\2"
            + "\u050e\u050f\7V\2\2\u050f\u0511\5\u010a\u0086\2\u0510\u050e\3\2\2\2\u0510"
            + "\u0511\3\2\2\2\u0511\u0514\3\2\2\2\u0512\u0513\7\u00f3\2\2\u0513\u0515"
            + "\5\u010a\u0086\2\u0514\u0512\3\2\2\2\u0514\u0515\3\2\2\2\u0515\u00bd\3"
            + "\2\2\2\u0516\u0518\t\25\2\2\u0517\u0516\3\2\2\2\u0517\u0518\3\2\2\2\u0518"
            + "\u0519\3\2\2\2\u0519\u051a\7\u0088\2\2\u051a\u00bf\3\2\2\2\u051b\u051c"
            + "\7\u00f8\2\2\u051c\u051d\7\u0136\2\2\u051d\u051e\5\u00c2b\2\u051e\u051f"
            + "\7\u0143\2\2\u051f\u0520\5\u00c4c\2\u0520\u00c1\3\2\2\2\u0521\u0522\5"
            + "\u0100\u0081\2\u0522\u00c3\3\2\2\2\u0523\u0524\5\u0100\u0081\2\u0524\u00c5"
            + "\3\2\2\2\u0525\u0527\bd\1\2\u0526\u0528\7\u00cc\2\2\u0527\u0526\3\2\2"
            + "\2\u0527\u0528\3\2\2\2\u0528\u0529\3\2\2\2\u0529\u052a\7\u019a\2\2\u052a"
            + "\u052b\5\u00c6d\2\u052b\u052c\7\u019d\2\2\u052c\u052f\3\2\2\2\u052d\u052f"
            + "\5\u00c8e\2\u052e\u0525\3\2\2\2\u052e\u052d\3\2\2\2\u052f\u0536\3\2\2"
            + "\2\u0530\u0531\f\4\2\2\u0531\u0532\5\u00d2j\2\u0532\u0533\5\u00c6d\5\u0533"
            + "\u0535\3\2\2\2\u0534\u0530\3\2\2\2\u0535\u0538\3\2\2\2\u0536\u0534\3\2"
            + "\2\2\u0536\u0537\3\2\2\2\u0537\u00c7\3\2\2\2\u0538\u0536\3\2\2\2\u0539"
            + "\u053d\5\u00caf\2\u053a\u053d\5\u00d0i\2\u053b\u053d\5\u00d6l\2\u053c"
            + "\u0539\3\2\2\2\u053c\u053a\3\2\2\2\u053c\u053b\3\2\2\2\u053d\u00c9\3\2"
            + "\2\2\u053e\u053f\5\u00d6l\2\u053f\u0541\7\u00a1\2\2\u0540\u0542\7\u00cc"
            + "\2\2\u0541\u0540\3\2\2\2\u0541\u0542\3\2\2\2\u0542\u0543\3\2\2\2\u0543"
            + "\u0544\7\u00ce\2\2\u0544\u0556\3\2\2\2\u0545\u0546\5\u00d6l\2\u0546\u0547"
            + "\7\26\2\2\u0547\u0548\5\u00d6l\2\u0548\u0549\7\t\2\2\u0549\u054a\5\u00d6"
            + "l\2\u054a\u0556\3\2\2\2\u054b\u054d\7\u00cc\2\2\u054c\u054b\3\2\2\2\u054c"
            + "\u054d\3\2\2\2\u054d\u054e\3\2\2\2\u054e\u054f\7o\2\2\u054f\u0550\7\u019a"
            + "\2\2\u0550\u0551\5b\62\2\u0551\u0552\7\u019d\2\2\u0552\u0556\3\2\2\2\u0553"
            + "\u0556\5\u00ccg\2\u0554\u0556\5\u00ceh\2\u0555\u053e\3\2\2\2\u0555\u0545"
            + "\3\2\2\2\u0555\u054c\3\2\2\2\u0555\u0553\3\2\2\2\u0555\u0554\3\2\2\2\u0556"
            + "\u00cb\3\2\2\2\u0557\u0559\5\u00d6l\2\u0558\u055a\7\u00cc\2\2\u0559\u0558"
            + "\3\2\2\2\u0559\u055a\3\2\2\2\u055a\u055b\3\2\2\2\u055b\u055c\7\u008f\2"
            + "\2\u055c\u0566\7\u019a\2\2\u055d\u0562\5\u00d6l\2\u055e\u055f\7\u018b"
            + "\2\2\u055f\u0561\5\u00d6l\2\u0560\u055e\3\2\2\2\u0561\u0564\3\2\2\2\u0562"
            + "\u0560\3\2\2\2\u0562\u0563\3\2\2\2\u0563\u0567\3\2\2\2\u0564\u0562\3\2"
            + "\2\2\u0565\u0567\5b\62\2\u0566\u055d\3\2\2\2\u0566\u0565\3\2\2\2\u0567"
            + "\u0568\3\2\2\2\u0568\u0569\7\u019d\2\2\u0569\u00cd\3\2\2\2\u056a\u056b"
            + "\7\u019a\2\2\u056b\u0570\5\u00d6l\2\u056c\u056d\7\u018b\2\2\u056d\u056f"
            + "\5\u00d6l\2\u056e\u056c\3\2\2\2\u056f\u0572\3\2\2\2\u0570\u056e\3\2\2"
            + "\2\u0570\u0571\3\2\2\2\u0571\u0573\3\2\2\2\u0572\u0570\3\2\2\2\u0573\u0575"
            + "\7\u019d\2\2\u0574\u0576\7\u00cc\2\2\u0575\u0574\3\2\2\2\u0575\u0576\3"
            + "\2\2\2\u0576\u0577\3\2\2\2\u0577\u0578\7\u008f\2\2\u0578\u0579\7\u019a"
            + "\2\2\u0579\u057a\5b\62\2\u057a\u057b\7\u019d\2\2\u057b\u00cf\3\2\2\2\u057c"
            + "\u057d\5\u00d6l\2\u057d\u057e\5\u00d4k\2\u057e\u057f\5\u00d6l\2\u057f"
            + "\u00d1\3\2\2\2\u0580\u0581\t\26\2\2\u0581\u00d3\3\2\2\2\u0582\u058f\7"
            + "\u0190\2\2\u0583\u058f\7\u0191\2\2\u0584\u058f\7\u0192\2\2\u0585\u058f"
            + "\7\u0193\2\2\u0586\u058f\7\u0196\2\2\u0587\u058f\7\u0197\2\2\u0588\u058f"
            + "\7\u0194\2\2\u0589\u058f\7\u0195\2\2\u058a\u058c\7\u00cc\2\2\u058b\u058a"
            + "\3\2\2\2\u058b\u058c\3\2\2\2\u058c\u058d\3\2\2\2\u058d\u058f\t\27\2\2"
            + "\u058e\u0582\3\2\2\2\u058e\u0583\3\2\2\2\u058e\u0584\3\2\2\2\u058e\u0585"
            + "\3\2\2\2\u058e\u0586\3\2\2\2\u058e\u0587\3\2\2\2\u058e\u0588\3\2\2\2\u058e"
            + "\u0589\3\2\2\2\u058e\u058b\3\2\2\2\u058f\u00d5\3\2\2\2\u0590\u0591\bl"
            + "\1\2\u0591\u0592\7\u019a\2\2\u0592\u0593\5b\62\2\u0593\u0594\7\u019d\2"
            + "\2\u0594\u05a2\3\2\2\2\u0595\u0596\7\u019a\2\2\u0596\u0597\5\u00d6l\2"
            + "\u0597\u0598\7\u019d\2\2\u0598\u05a2\3\2\2\2\u0599\u05a2\5\u00dan\2\u059a"
            + "\u05a2\5\u00dep\2\u059b\u05a2\5\u00e2r\2\u059c\u05a2\5\u00eav\2\u059d"
            + "\u05a2\5\u00ecw\2\u059e\u05a2\5\u00f4{\2\u059f\u05a2\5\u00f6|\2\u05a0"
            + "\u05a2\5\u00d8m\2\u05a1\u0590\3\2\2\2\u05a1\u0595\3\2\2\2\u05a1\u0599"
            + "\3\2\2\2\u05a1\u059a\3\2\2\2\u05a1\u059b\3\2\2\2\u05a1\u059c\3\2\2\2\u05a1"
            + "\u059d\3\2\2\2\u05a1\u059e\3\2\2\2\u05a1\u059f\3\2\2\2\u05a1\u05a0\3\2"
            + "\2\2\u05a2\u05b3\3\2\2\2\u05a3\u05a4\f\20\2\2\u05a4\u05a5\7\u0198\2\2"
            + "\u05a5\u05b2\5\u00d6l\21\u05a6\u05a7\f\17\2\2\u05a7\u05a8\7\u018d\2\2"
            + "\u05a8\u05b2\5\u00d6l\20\u05a9\u05aa\f\16\2\2\u05aa\u05ab\7\u0189\2\2"
            + "\u05ab\u05b2\5\u00d6l\17\u05ac\u05ad\f\r\2\2\u05ad\u05ae\7\u01a0\2\2\u05ae"
            + "\u05b2\5\u00d6l\16\u05af\u05b0\f\21\2\2\u05b0\u05b2\5\u00dco\2\u05b1\u05a3"
            + "\3\2\2\2\u05b1\u05a6\3\2\2\2\u05b1\u05a9\3\2\2\2\u05b1\u05ac\3\2\2\2\u05b1"
            + "\u05af\3\2\2\2\u05b2\u05b5\3\2\2\2\u05b3\u05b1\3\2\2\2\u05b3\u05b4\3\2"
            + "\2\2\u05b4\u00d7\3\2\2\2\u05b5\u05b3\3\2\2\2\u05b6\u05bf\5\u0102\u0082"
            + "\2\u05b7\u05bf\5\u0104\u0083\2\u05b8\u05bf\5\u0110\u0089\2\u05b9\u05bf"
            + "\5\u0106\u0084\2\u05ba\u05bf\5\u010a\u0086\2\u05bb\u05bf\5\u010e\u0088"
            + "\2\u05bc\u05bf\5\u010c\u0087\2\u05bd\u05bf\5\u0112\u008a\2\u05be\u05b6"
            + "\3\2\2\2\u05be\u05b7\3\2\2\2\u05be\u05b8\3\2\2\2\u05be\u05b9\3\2\2\2\u05be"
            + "\u05ba\3\2\2\2\u05be\u05bb\3\2\2\2\u05be\u05bc\3\2\2\2\u05be\u05bd\3\2"
            + "\2\2\u05bf\u00d9\3\2\2\2\u05c0\u05c1\7\u009e\2\2\u05c1\u05c2\5\u00d6l"
            + "\2\u05c2\u05c3\5\u00dco\2\u05c3\u00db\3\2\2\2\u05c4\u05c5\t\30\2\2\u05c5"
            + "\u00dd\3\2\2\2\u05c6\u05c7\5\u00e0q\2\u05c7\u05c8\t\31\2\2\u05c8\u05cd"
            + "\5\u00e0q\2\u05c9\u05ca\t\31\2\2\u05ca\u05cc\5\u00e0q\2\u05cb\u05c9\3"
            + "\2\2\2\u05cc\u05cf\3\2\2\2\u05cd\u05cb\3\2\2\2\u05cd\u05ce\3\2\2\2\u05ce"
            + "\u00df\3\2\2\2\u05cf\u05cd\3\2\2\2\u05d0\u05d1\7\u019a\2\2\u05d1\u05d2"
            + "\5\u00d6l\2\u05d2\u05d3\7\u019d\2\2\u05d3\u05da\3\2\2\2\u05d4\u05da\5"
            + "\u00e2r\2\u05d5\u05da\5\u00ecw\2\u05d6\u05da\5\u00f4{\2\u05d7\u05da\5"
            + "\u00f6|\2\u05d8\u05da\5\u00d8m\2\u05d9\u05d0\3\2\2\2\u05d9\u05d4\3\2\2"
            + "\2\u05d9\u05d5\3\2\2\2\u05d9\u05d6\3\2\2\2\u05d9\u05d7\3\2\2\2\u05d9\u05d8"
            + "\3\2\2\2\u05da\u00e1\3\2\2\2\u05db\u05de\5\u00e4s\2\u05dc\u05de\5\u00e6"
            + "t\2\u05dd\u05db\3\2\2\2\u05dd\u05dc\3\2\2\2\u05de\u00e3\3\2\2\2\u05df"
            + "\u05e0\7$\2\2\u05e0\u05e6\5\u00d6l\2\u05e1\u05e2\7\u0156\2\2\u05e2\u05e3"
            + "\5\u00d6l\2\u05e3\u05e4\7\u013f\2\2\u05e4\u05e5\5\u00d6l\2\u05e5\u05e7"
            + "\3\2\2\2\u05e6\u05e1\3\2\2\2\u05e7\u05e8\3\2\2\2\u05e8\u05e6\3\2\2\2\u05e8"
            + "\u05e9\3\2\2\2\u05e9\u05ec\3\2\2\2\u05ea\u05eb\7c\2\2\u05eb\u05ed\5\u00d6"
            + "l\2\u05ec\u05ea\3\2\2\2\u05ec\u05ed\3\2\2\2\u05ed\u05ee\3\2\2\2\u05ee"
            + "\u05ef\7g\2\2\u05ef\u00e5\3\2\2\2\u05f0\u05f2\7$\2\2\u05f1\u05f3\5\u00e8"
            + "u\2\u05f2\u05f1\3\2\2\2\u05f3\u05f4\3\2\2\2\u05f4\u05f2\3\2\2\2\u05f4"
            + "\u05f5\3\2\2\2\u05f5\u05f8\3\2\2\2\u05f6\u05f7\7c\2\2\u05f7\u05f9\5\u00c6"
            + "d\2\u05f8\u05f6\3\2\2\2\u05f8\u05f9\3\2\2\2\u05f9\u05fa\3\2\2\2\u05fa"
            + "\u05fb\7g\2\2\u05fb\u00e7\3\2\2\2\u05fc\u05fd\7\u0156\2\2\u05fd\u05fe"
            + "\5\u00c6d\2\u05fe\u05ff\7\u013f\2\2\u05ff\u0600\5\u00c6d\2\u0600\u00e9"
            + "\3\2\2\2\u0601\u0602\5\u0106\u0084\2\u0602\u0603\7\3\2\2\u0603\u0604\t"
            + "\32\2\2\u0604\u00eb\3\2\2\2\u0605\u0606\7\23\2\2\u0606\u0608\7\u019a\2"
            + "\2\u0607\u0609\5\u00eex\2\u0608\u0607\3\2\2\2\u0608\u0609\3\2\2\2\u0609"
            + "\u060a\3\2\2\2\u060a\u060b\5\u00d6l\2\u060b\u060d\7\u019d\2\2\u060c\u060e"
            + "\5\u00f0y\2\u060d\u060c\3\2\2\2\u060d\u060e\3\2\2\2\u060e\u069e\3\2\2"
            + "\2\u060f\u0610\7;\2\2\u0610\u0616\7\u019a\2\2\u0611\u0613\5\u00eex\2\u0612"
            + "\u0611\3\2\2\2\u0612\u0613\3\2\2\2\u0613\u0614\3\2\2\2\u0614\u0617\5\u00d6"
            + "l\2\u0615\u0617\7\u0198\2\2\u0616\u0612\3\2\2\2\u0616\u0615\3\2\2\2\u0617"
            + "\u0618\3\2\2\2\u0618\u061a\7\u019d\2\2\u0619\u061b\5\u00f0y\2\u061a\u0619"
            + "\3\2\2\2\u061a\u061b\3\2\2\2\u061b\u069e\3\2\2\2\u061c\u061d\7<\2\2\u061d"
            + "\u0623\7\u019a\2\2\u061e\u0620\5\u00eex\2\u061f\u061e\3\2\2\2\u061f\u0620"
            + "\3\2\2\2\u0620\u0621\3\2\2\2\u0621\u0624\5\u00d6l\2\u0622\u0624\7\u0198"
            + "\2\2\u0623\u061f\3\2\2\2\u0623\u0622\3\2\2\2\u0624\u0625\3\2\2\2\u0625"
            + "\u0627\7\u019d\2\2\u0626\u0628\5\u00f0y\2\u0627\u0626\3\2\2\2\u0627\u0628"
            + "\3\2\2\2\u0628\u069e\3\2\2\2\u0629\u062a\7\u0160\2\2\u062a\u062b\7\u019a"
            + "\2\2\u062b\u062c\7\u019d\2\2\u062c\u069e\5\u00f0y\2\u062d\u062e\7\u0166"
            + "\2\2\u062e\u062f\7\u019a\2\2\u062f\u0630\7\u019d\2\2\u0630\u069e\5\u00f0"
            + "y\2\u0631\u0632\7\u0167\2\2\u0632\u0633\7\u019a\2\2\u0633\u0634\5\u00d6"
            + "l\2\u0634\u0635\7\u019d\2\2\u0635\u0636\5\u00f0y\2\u0636\u069e\3\2\2\2"
            + "\u0637\u0638\7\u0168\2\2\u0638\u0639\7\u019a\2\2\u0639\u0640\5\u00d6l"
            + "\2\u063a\u063b\7\u018b\2\2\u063b\u063e\5\u00d6l\2\u063c\u063d\7\u018b"
            + "\2\2\u063d\u063f\5\u00d6l\2\u063e\u063c\3\2\2\2\u063e\u063f\3\2\2\2\u063f"
            + "\u0641\3\2\2\2\u0640\u063a\3\2\2\2\u0640\u0641\3\2\2\2\u0641\u0642\3\2"
            + "\2\2\u0642\u0643\7\u019d\2\2\u0643\u0644\5\u00f0y\2\u0644\u069e\3\2\2"
            + "\2\u0645\u0646\7\u0169\2\2\u0646\u0647\7\u019a\2\2\u0647\u0648\5\u00d6"
            + "l\2\u0648\u0649\7\u019d\2\2\u0649\u064a\5\u00f0y\2\u064a\u069e\3\2\2\2"
            + "\u064b\u064c\7\u016a\2\2\u064c\u064d\7\u019a\2\2\u064d\u0654\5\u00d6l"
            + "\2\u064e\u064f\7\u018b\2\2\u064f\u0652\5\u00d6l\2\u0650\u0651\7\u018b"
            + "\2\2\u0651\u0653\5\u00d6l\2\u0652\u0650\3\2\2\2\u0652\u0653\3\2\2\2\u0653"
            + "\u0655\3\2\2\2\u0654\u064e\3\2\2\2\u0654\u0655\3\2\2\2\u0655\u0656\3\2"
            + "\2\2\u0656\u0657\7\u019d\2\2\u0657\u0658\5\u00f0y\2\u0658\u069e\3\2\2"
            + "\2\u0659\u065a\7\u00bc\2\2\u065a\u065c\7\u019a\2\2\u065b\u065d\5\u00ee"
            + "x\2\u065c\u065b\3\2\2\2\u065c\u065d\3\2\2\2\u065d\u065e\3\2\2\2\u065e"
            + "\u065f\5\u00d6l\2\u065f\u0661\7\u019d\2\2\u0660\u0662\5\u00f0y\2\u0661"
            + "\u0660\3\2\2\2\u0661\u0662\3\2\2\2\u0662\u069e\3\2\2\2\u0663\u0664\7\u00c2"
            + "\2\2\u0664\u0666\7\u019a\2\2\u0665\u0667\5\u00eex\2\u0666\u0665\3\2\2"
            + "\2\u0666\u0667\3\2\2\2\u0667\u0668\3\2\2\2\u0668\u0669\5\u00d6l\2\u0669"
            + "\u066b\7\u019d\2\2\u066a\u066c\5\u00f0y\2\u066b\u066a\3\2\2\2\u066b\u066c"
            + "\3\2\2\2\u066c\u069e\3\2\2\2\u066d\u066e\7\u0173\2\2\u066e\u066f\7\u019a"
            + "\2\2\u066f\u0670\7\u019d\2\2\u0670\u069e\5\u00f0y\2\u0671\u0672\7\u0174"
            + "\2\2\u0672\u0673\7\u019a\2\2\u0673\u0674\7\u019d\2\2\u0674\u069e\5\u00f0"
            + "y\2\u0675\u0676\7\u0175\2\2\u0676\u0678\7\u019a\2\2\u0677\u0679\5\u00ee"
            + "x\2\u0678\u0677\3\2\2\2\u0678\u0679\3\2\2\2\u0679\u067a\3\2\2\2\u067a"
            + "\u067b\5\u00d6l\2\u067b\u067d\7\u019d\2\2\u067c\u067e\5\u00f0y\2\u067d"
            + "\u067c\3\2\2\2\u067d\u067e\3\2\2\2\u067e\u069e\3\2\2\2\u067f\u0680\7\u0132"
            + "\2\2\u0680\u0682\7\u019a\2\2\u0681\u0683\5\u00eex\2\u0682\u0681\3\2\2"
            + "\2\u0682\u0683\3\2\2\2\u0683\u0684\3\2\2\2\u0684\u0685\5\u00d6l\2\u0685"
            + "\u0687\7\u019d\2\2\u0686\u0688\5\u00f0y\2\u0687\u0686\3\2\2\2\u0687\u0688"
            + "\3\2\2\2\u0688\u069e\3\2\2\2\u0689\u068a\7\u0151\2\2\u068a\u068c\7\u019a"
            + "\2\2\u068b\u068d\5\u00eex\2\u068c\u068b\3\2\2\2\u068c\u068d\3\2\2\2\u068d"
            + "\u068e\3\2\2\2\u068e\u068f\5\u00d6l\2\u068f\u0691\7\u019d\2\2\u0690\u0692"
            + "\5\u00f0y\2\u0691\u0690\3\2\2\2\u0691\u0692\3\2\2\2\u0692\u069e\3\2\2"
            + "\2\u0693\u0694\7\u0177\2\2\u0694\u0696\7\u019a\2\2\u0695\u0697\5\u00ee"
            + "x\2\u0696\u0695\3\2\2\2\u0696\u0697\3\2\2\2\u0697\u0698\3\2\2\2\u0698"
            + "\u0699\5\u00d6l\2\u0699\u069b\7\u019d\2\2\u069a\u069c\5\u00f0y\2\u069b"
            + "\u069a\3\2\2\2\u069b\u069c\3\2\2\2\u069c\u069e\3\2\2\2\u069d\u0605\3\2"
            + "\2\2\u069d\u060f\3\2\2\2\u069d\u061c\3\2\2\2\u069d\u0629\3\2\2\2\u069d"
            + "\u062d\3\2\2\2\u069d\u0631\3\2\2\2\u069d\u0637\3\2\2\2\u069d\u0645\3\2"
            + "\2\2\u069d\u064b\3\2\2\2\u069d\u0659\3\2\2\2\u069d\u0663\3\2\2\2\u069d"
            + "\u066d\3\2\2\2\u069d\u0671\3\2\2\2\u069d\u0675\3\2\2\2\u069d\u067f\3\2"
            + "\2\2\u069d\u0689\3\2\2\2\u069d\u0693\3\2\2\2\u069e\u00ed\3\2\2\2\u069f"
            + "\u06a0\t\17\2\2\u06a0\u00ef\3\2\2\2\u06a1\u06a2\7\u00da\2\2\u06a2\u06a4"
            + "\7\u019a\2\2\u06a3\u06a5\5\u00f2z\2\u06a4\u06a3\3\2\2\2\u06a4\u06a5\3"
            + "\2\2\2\u06a5\u06a7\3\2\2\2\u06a6\u06a8\5\u009cO\2\u06a7\u06a6\3\2\2\2"
            + "\u06a7\u06a8\3\2\2\2\u06a8\u06a9\3\2\2\2\u06a9\u06aa\7\u019d\2\2\u06aa"
            + "\u00f1\3\2\2\2\u06ab\u06ac\7\u00de\2\2\u06ac\u06ad\7\37\2\2\u06ad\u06b2"
            + "\5\u00d6l\2\u06ae\u06af\7\u018b\2\2\u06af\u06b1\5\u00d6l\2\u06b0\u06ae"
            + "\3\2\2\2\u06b1\u06b4\3\2\2\2\u06b2\u06b0\3\2\2\2\u06b2\u06b3\3\2\2\2\u06b3"
            + "\u00f3\3\2\2\2\u06b4\u06b2\3\2\2\2\u06b5\u077b\7\u015f\2\2\u06b6\u06b7"
            + "\7&\2\2\u06b7\u06b8\7\u019a\2\2\u06b8\u06b9\5\u00d6l\2\u06b9\u06ba\7\f"
            + "\2\2\u06ba\u06bc\5H%\2\u06bb\u06bd\5J&\2\u06bc\u06bb\3\2\2\2\u06bc\u06bd"
            + "\3\2\2\2\u06bd\u06be\3\2\2\2\u06be\u06bf\7\u019d\2\2\u06bf\u077b\3\2\2"
            + "\2\u06c0\u06c1\7;\2\2\u06c1\u06c4\7\u019a\2\2\u06c2\u06c5\5\u00d6l\2\u06c3"
            + "\u06c5\7\u0198\2\2\u06c4\u06c2\3\2\2\2\u06c4\u06c3\3\2\2\2\u06c5\u06c6"
            + "\3\2\2\2\u06c6\u077b\7\u019d\2\2\u06c7\u077b\7\u0161\2\2\u06c8\u06c9\7"
            + "B\2\2\u06c9\u077b\7H\2\2\u06ca\u077b\7\u0162\2\2\u06cb\u077b\7\u0163\2"
            + "\2\u06cc\u06d0\7\u0164\2\2\u06cd\u06ce\7B\2\2\u06ce\u06d0\7\u0140\2\2"
            + "\u06cf\u06cc\3\2\2\2\u06cf\u06cd\3\2\2\2\u06d0\u06d5\3\2\2\2\u06d1\u06d2"
            + "\7\u019a\2\2\u06d2\u06d3\5\u00d6l\2\u06d3\u06d4\7\u019d\2\2\u06d4\u06d6"
            + "\3\2\2\2\u06d5\u06d1\3\2\2\2\u06d5\u06d6\3\2\2\2\u06d6\u077b\3\2\2\2\u06d7"
            + "\u077b\7\u0165\2\2\u06d8\u06d9\7B\2\2\u06d9\u077b\7\u0178\2\2\u06da\u06db"
            + "\7\u016b\2\2\u06db\u06dc\7\u019a\2\2\u06dc\u06e9\5\u00d6l\2\u06dd\u06de"
            + "\7\u018b\2\2\u06de\u06e6\5\u00d6l\2\u06df\u06e0\7\u018b\2\2\u06e0\u06e1"
            + "\5\u00d6l\2\u06e1\u06e2\7\u0190\2\2\u06e2\u06e3\5\u00d6l\2\u06e3\u06e5"
            + "\3\2\2\2\u06e4\u06df\3\2\2\2\u06e5\u06e8\3\2\2\2\u06e6\u06e4\3\2\2\2\u06e6"
            + "\u06e7\3\2\2\2\u06e7\u06ea\3\2\2\2\u06e8\u06e6\3\2\2\2\u06e9\u06dd\3\2"
            + "\2\2\u06e9\u06ea\3\2\2\2\u06ea\u06eb\3\2\2\2\u06eb\u06ec\7\u019d\2\2\u06ec"
            + "\u077b\3\2\2\2\u06ed\u06ee\7\u016c\2\2\u06ee\u06ef\7\u019a\2\2\u06ef\u06fc"
            + "\5\u00d6l\2\u06f0\u06f1\7\u018b\2\2\u06f1\u06f9\5\u00d6l\2\u06f2\u06f3"
            + "\7\u018b\2\2\u06f3\u06f4\5\u00d6l\2\u06f4\u06f5\7\u0190\2\2\u06f5\u06f6"
            + "\5\u00d6l\2\u06f6\u06f8\3\2\2\2\u06f7\u06f2\3\2\2\2\u06f8\u06fb\3\2\2"
            + "\2\u06f9\u06f7\3\2\2\2\u06f9\u06fa\3\2\2\2\u06fa\u06fd\3\2\2\2\u06fb\u06f9"
            + "\3\2\2\2\u06fc\u06f0\3\2\2\2\u06fc\u06fd\3\2\2\2\u06fd\u06fe\3\2\2\2\u06fe"
            + "\u06ff\7\u019d\2\2\u06ff\u077b\3\2\2\2\u0700\u0701\7\u016d\2\2\u0701\u0702"
            + "\7\u019a\2\2\u0702\u070f\5\u00d6l\2\u0703\u0704\7\u018b\2\2\u0704\u070c"
            + "\5\u00d6l\2\u0705\u0706\7\u018b\2\2\u0706\u0707\5\u00d6l\2\u0707\u0708"
            + "\7\u0190\2\2\u0708\u0709\5\u00d6l\2\u0709\u070b\3\2\2\2\u070a\u0705\3"
            + "\2\2\2\u070b\u070e\3\2\2\2\u070c\u070a\3\2\2\2\u070c\u070d\3\2\2\2\u070d"
            + "\u0710\3\2\2\2\u070e\u070c\3\2\2\2\u070f\u0703\3\2\2\2\u070f\u0710\3\2"
            + "\2\2\u0710\u0711\3\2\2\2\u0711\u0712\7\u019d\2\2\u0712\u077b\3\2\2\2\u0713"
            + "\u0714\7\u016e\2\2\u0714\u0715\7\u019a\2\2\u0715\u0722\5\u00d6l\2\u0716"
            + "\u0717\7\u018b\2\2\u0717\u071f\5\u00d6l\2\u0718\u0719\7\u018b\2\2\u0719"
            + "\u071a\5\u00d6l\2\u071a\u071b\7\u0190\2\2\u071b\u071c\5\u00d6l\2\u071c"
            + "\u071e\3\2\2\2\u071d\u0718\3\2\2\2\u071e\u0721\3\2\2\2\u071f\u071d\3\2"
            + "\2\2\u071f\u0720\3\2\2\2\u0720\u0723\3\2\2\2\u0721\u071f\3\2\2\2\u0722"
            + "\u0716\3\2\2\2\u0722\u0723\3\2\2\2\u0723\u0724\3\2\2\2\u0724\u0725\7\u019d"
            + "\2\2\u0725\u077b\3\2\2\2\u0726\u0727\7\u016f\2\2\u0727\u0728\7\u019a\2"
            + "\2\u0728\u0735\5\u00d6l\2\u0729\u072a\7\u018b\2\2\u072a\u0732\5\u00d6"
            + "l\2\u072b\u072c\7\u018b\2\2\u072c\u072d\5\u00d6l\2\u072d\u072e\7\u0190"
            + "\2\2\u072e\u072f\5\u00d6l\2\u072f\u0731\3\2\2\2\u0730\u072b\3\2\2\2\u0731"
            + "\u0734\3\2\2\2\u0732\u0730\3\2\2\2\u0732\u0733\3\2\2\2\u0733\u0736\3\2"
            + "\2\2\u0734\u0732\3\2\2\2\u0735\u0729\3\2\2\2\u0735\u0736\3\2\2\2\u0736"
            + "\u0737\3\2\2\2\u0737\u0738\7\u019d\2\2\u0738\u077b\3\2\2\2\u0739\u073a"
            + "\7\u0170\2\2\u073a\u073b\7\u019a\2\2\u073b\u0748\5\u00d6l\2\u073c\u073d"
            + "\7\u018b\2\2\u073d\u0745\5\u00d6l\2\u073e\u073f\7\u018b\2\2\u073f\u0740"
            + "\5\u00d6l\2\u0740\u0741\7\u0190\2\2\u0741\u0742\5\u00d6l\2\u0742\u0744"
            + "\3\2\2\2\u0743\u073e\3\2\2\2\u0744\u0747\3\2\2\2\u0745\u0743\3\2\2\2\u0745"
            + "\u0746\3\2\2\2\u0746\u0749\3\2\2\2\u0747\u0745\3\2\2\2\u0748\u073c\3\2"
            + "\2\2\u0748\u0749\3\2\2\2\u0749\u074a\3\2\2\2\u074a\u074b\7\u019d\2\2\u074b"
            + "\u077b\3\2\2\2\u074c\u074d\7\u0171\2\2\u074d\u074e\7\u019a\2\2\u074e\u0756"
            + "\5\u00d6l\2\u074f\u0750\7\u018b\2\2\u0750\u0751\5\u00d6l\2\u0751\u0752"
            + "\7\u0190\2\2\u0752\u0753\5\u00d6l\2\u0753\u0755\3\2\2\2\u0754\u074f\3"
            + "\2\2\2\u0755\u0758\3\2\2\2\u0756\u0754\3\2\2\2\u0756\u0757\3\2\2\2\u0757"
            + "\u0759\3\2\2\2\u0758\u0756\3\2\2\2\u0759\u075a\7\u019d\2\2\u075a\u077b"
            + "\3\2\2\2\u075b\u075c\7\u0172\2\2\u075c\u075d\7\u019a\2\2\u075d\u0763\5"
            + "\u00d6l\2\u075e\u075f\7\u018b\2\2\u075f\u0760\5\u00d6l\2\u0760\u0761\7"
            + "\u0190\2\2\u0761\u0762\5\u00d6l\2\u0762\u0764\3\2\2\2\u0763\u075e\3\2"
            + "\2\2\u0764\u0765\3\2\2\2\u0765\u0763\3\2\2\2\u0765\u0766\3\2\2\2\u0766"
            + "\u0769\3\2\2\2\u0767\u0768\7\u018b\2\2\u0768\u076a\5\u00d6l\2\u0769\u0767"
            + "\3\2\2\2\u0769\u076a\3\2\2\2\u076a\u076b\3\2\2\2\u076b\u076c\7\u019d\2"
            + "\2\u076c\u077b\3\2\2\2\u076d\u076e\7\u0131\2\2\u076e\u076f\7\u019a\2\2"
            + "\u076f\u0770\5\u00d6l\2\u0770\u0771\7|\2\2\u0771\u0774\5\u00d6l\2\u0772"
            + "\u0773\7x\2\2\u0773\u0775\5\u00d6l\2\u0774\u0772\3\2\2\2\u0774\u0775\3"
            + "\2\2\2\u0775\u0776\3\2\2\2\u0776\u0777\7\u019d\2\2\u0777\u077b\3\2\2\2"
            + "\u0778\u077b\7\u0176\2\2\u0779\u077b\7\u0178\2\2\u077a\u06b5\3\2\2\2\u077a"
            + "\u06b6\3\2\2\2\u077a\u06c0\3\2\2\2\u077a\u06c7\3\2\2\2\u077a\u06c8\3\2"
            + "\2\2\u077a\u06ca\3\2\2\2\u077a\u06cb\3\2\2\2\u077a\u06cf\3\2\2\2\u077a"
            + "\u06d7\3\2\2\2\u077a\u06d8\3\2\2\2\u077a\u06da\3\2\2\2\u077a\u06ed\3\2"
            + "\2\2\u077a\u0700\3\2\2\2\u077a\u0713\3\2\2\2\u077a\u0726\3\2\2\2\u077a"
            + "\u0739\3\2\2\2\u077a\u074c\3\2\2\2\u077a\u075b\3\2\2\2\u077a\u076d\3\2"
            + "\2\2\u077a\u0778\3\2\2\2\u077a\u0779\3\2\2\2\u077b\u00f5\3\2\2\2\u077c"
            + "\u077d\5\u0106\u0084\2\u077d\u077f\7\u019a\2\2\u077e\u0780\5\u00f8}\2"
            + "\u077f\u077e\3\2\2\2\u077f\u0780\3\2\2\2\u0780\u0781\3\2\2\2\u0781\u0782"
            + "\7\u019d\2\2\u0782\u00f7\3\2\2\2\u0783\u0788\5\u00fa~\2\u0784\u0785\7"
            + "\u018b\2\2\u0785\u0787\5\u00fa~\2\u0786\u0784\3\2\2\2\u0787\u078a\3\2"
            + "\2\2\u0788\u0786\3\2\2\2\u0788\u0789\3\2\2\2\u0789\u00f9\3\2\2\2\u078a"
            + "\u0788\3\2\2\2\u078b\u0797\3\2\2\2\u078c\u0792\6~\n\2\u078d\u078e\5\u0106"
            + "\u0084\2\u078e\u0790\7\u0190\2\2\u078f\u0791\7\u0194\2\2\u0790\u078f\3"
            + "\2\2\2\u0790\u0791\3\2\2\2\u0791\u0793\3\2\2\2\u0792\u078d\3\2\2\2\u0792"
            + "\u0793\3\2\2\2\u0793\u0794\3\2\2\2\u0794\u0797\5\u00d6l\2\u0795\u0797"
            + "\7\u0198\2\2\u0796\u078b\3\2\2\2\u0796\u078c\3\2\2\2\u0796\u0795\3\2\2"
            + "\2\u0797\u00fb\3\2\2\2\u0798\u0799\5\u0100\u0081\2\u0799\u00fd\3\2\2\2"
            + "\u079a\u079b\5\u0106\u0084\2\u079b\u00ff\3\2\2\2\u079c\u07a1\5\u0106\u0084"
            + "\2\u079d\u079e\7\u018e\2\2\u079e\u07a0\5\u0106\u0084\2\u079f\u079d\3\2"
            + "\2\2\u07a0\u07a3\3\2\2\2\u07a1\u079f\3\2\2\2\u07a1\u07a2\3\2\2\2\u07a2"
            + "\u0101\3\2\2\2\u07a3\u07a1\3\2\2\2\u07a4\u07a5\7H\2\2\u07a5\u07a6\5\u010a"
            + "\u0086\2\u07a6\u0103\3\2\2\2\u07a7\u07a8\7\u0140\2\2\u07a8\u07a9\5\u010a"
            + "\u0086\2\u07a9\u0105\3\2\2\2\u07aa\u07ac\7\u01a0\2\2\u07ab\u07aa\3\2\2"
            + "\2\u07ab\u07ac\3\2\2\2\u07ac\u07ad\3\2\2\2\u07ad\u07b2\5\u0108\u0085\2"
            + "\u07ae\u07af\7\u018e\2\2\u07af\u07b1\5\u0108\u0085\2\u07b0\u07ae\3\2\2"
            + "\2\u07b1\u07b4\3\2\2\2\u07b2\u07b0\3\2\2\2\u07b2\u07b3\3\2\2\2\u07b3\u0107"
            + "\3\2\2\2\u07b4\u07b2\3\2\2\2\u07b5\u07b8\7\u01a1\2\2\u07b6\u07b8\5\u0114"
            + "\u008b\2\u07b7\u07b5\3\2\2\2\u07b7\u07b6\3\2\2\2\u07b8\u0109\3\2\2\2\u07b9"
            + "\u07bc\7\u01a2\2\2\u07ba\u07bc\7\u01a3\2\2\u07bb\u07b9\3\2\2\2\u07bb\u07ba"
            + "\3\2\2\2\u07bc\u010b\3\2\2\2\u07bd\u07bf\t\33\2\2\u07be\u07bd\3\2\2\2"
            + "\u07be\u07bf\3\2\2\2\u07bf\u07c0\3\2\2\2\u07c0\u07c1\7\u01a4\2\2\u07c1"
            + "\u010d\3\2\2\2\u07c2\u07c4\t\33\2\2\u07c3\u07c2\3\2\2\2\u07c3\u07c4\3"
            + "\2\2\2\u07c4\u07c5\3\2\2\2\u07c5\u07c6\7\u01a5\2\2\u07c6\u010f\3\2\2\2"
            + "\u07c7\u07c8\t\34\2\2\u07c8\u0111\3\2\2\2\u07c9\u07ca\7\u00ce\2\2\u07ca"
            + "\u0113\3\2\2\2\u07cb\u07cc\t\35\2\2\u07cc\u0115\3\2\2\2\u00f8\u0118\u0132"
            + "\u0139\u0141\u0148\u014b\u0150\u0154\u015d\u0162\u016a\u016f\u0178\u0184"
            + "\u0189\u018c\u01a1\u01a5\u01ab\u01af\u01b9\u01be\u01c0\u01ca\u01d1\u01d8"
            + "\u01db\u01e2\u01ea\u01f0\u01f5\u01fb\u01ff\u0201\u0209\u0215\u0223\u022d"
            + "\u0233\u0238\u023d\u0242\u0246\u024b\u0255\u0261\u0268\u026b\u0277\u027c"
            + "\u0282\u0289\u0290\u0294\u0299\u029d\u02a2\u02a5\u02aa\u02ad\u02b1\u02bd"
            + "\u02c4\u02cd\u02d2\u02d6\u02d8\u02de\u02e5\u02e8\u0303\u0305\u030a\u030e"
            + "\u0317\u031d\u0327\u032f\u0335\u033e\u0340\u0344\u0348\u0350\u035b\u0364"
            + "\u036b\u0373\u037d\u0382\u038f\u039a\u03a1\u03a8\u03ac\u03b0\u03b4\u03b6"
            + "\u03bb\u03be\u03c1\u03c4\u03c8\u03cb\u03ce\u03d1\u03d4\u03db\u03e6\u03ea"
            + "\u03ed\u03f0\u03f4\u03fc\u0405\u0409\u0413\u041b\u0421\u0425\u0428\u042e"
            + "\u0437\u043a\u043f\u0442\u044c\u0451\u045a\u045f\u0463\u046c\u0470\u047e"
            + "\u048e\u0493\u049e\u04a9\u04ab\u04b2\u04b5\u04bc\u04c1\u04c7\u04ca\u04cd"
            + "\u04d4\u04d8\u04dc\u04e0\u04e6\u04ec\u04f2\u04f6\u04f9\u04fc\u0507\u0510"
            + "\u0514\u0517\u0527\u052e\u0536\u053c\u0541\u054c\u0555\u0559\u0562\u0566"
            + "\u0570\u0575\u058b\u058e\u05a1\u05b1\u05b3\u05be\u05cd\u05d9\u05dd\u05e8"
            + "\u05ec\u05f4\u05f8\u0608\u060d\u0612\u0616\u061a\u061f\u0623\u0627\u063e"
            + "\u0640\u0652\u0654\u065c\u0661\u0666\u066b\u0678\u067d\u0682\u0687\u068c"
            + "\u0691\u0696\u069b\u069d\u06a4\u06a7\u06b2\u06bc\u06c4\u06cf\u06d5\u06e6"
            + "\u06e9\u06f9\u06fc\u070c\u070f\u071f\u0722\u0732\u0735\u0745\u0748\u0756"
            + "\u0765\u0769\u0774\u077a\u077f\u0788\u0790\u0792\u0796\u07a1\u07ab\u07b2"
            + "\u07b7\u07bb\u07be\u07c3";
    public static final ATN _ATN = new ATNDeserializer().deserialize(_serializedATN.toCharArray());
    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
