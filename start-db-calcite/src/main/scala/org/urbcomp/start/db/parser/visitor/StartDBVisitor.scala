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
package org.urbcomp.start.db.parser.visitor

import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.misc.Interval
import org.apache.calcite.sql._
import org.apache.calcite.sql.`type`.SqlTypeName
import org.apache.calcite.sql.ddl.{SqlDdlNodes, SqlDropSchema, SqlDropTable}
import org.apache.calcite.sql.fun.{SqlCase, SqlStdOperatorTable}
import org.apache.calcite.sql.parser.SqlParserPos
import org.apache.calcite.util.{DateString, TimestampString}
import org.urbcomp.start.db.parser.dcl.{SqlColumnMappingDeclaration, SqlCreateUser, SqlLoadData}
import org.urbcomp.start.db.parser.ddl.{
  SqlCreateDatabase,
  SqlStartCreateTable,
  SqlStartCreateTableLike,
  SqlDropIndex,
  SqlIndexDeclaration,
  SqlRenameTable,
  SqlTruncateTable,
  SqlUseDatabase
}
import org.urbcomp.start.db.parser.dql.{
  SqlShowCreateTable,
  SqlShowDatabases,
  SqlShowIndex,
  SqlShowStatus,
  SqlShowTables
}
import org.urbcomp.start.db.parser.parser.StartDBSqlBaseVisitor
import org.urbcomp.start.db.parser.parser.StartDBSqlParser._
import org.urbcomp.start.db.parser.visitor.StartDBVisitor._
import org.urbcomp.start.db.schema.IndexType
import org.urbcomp.start.db.udf.DataEngine
import org.urbcomp.start.db.udf.DataEngine.Calcite
import org.urbcomp.start.db.util.{MetadataUtil, StringUtil}
import org.reflections.Reflections
import org.urbcomp.start.db.udtf.AbstractUdtf

import scala.collection.convert.ImplicitConversions._
import java.util
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.language.postfixOps

/**
  * Start DB grammar visitor
  *
  * @author : zaiyuan
  */
class StartDBVisitor(user: String, db: String) extends StartDBSqlBaseVisitor[AnyRef] {

  private val pos: SqlParserPos = SqlParserPos.ZERO

  override def visitProgram(ctx: ProgramContext): SqlNode = visitStmt(ctx.stmt())

  override def visitStmt(ctx: StmtContext): SqlNode = ctx.getChild(0) match {
    case c: SelectStmtContext          => visitSelectStmt(c)
    case c: CreateTableStmtContext     => visitCreateTableStmt(c)
    case c: CreateTableLikeStmtContext => visitCreateTableLikeStmt(c)
    case c: ShowTablesStmtContext      => visitShowTablesStmt(c)
    case c: CreateDatabaseStmtContext  => visitCreateDatabaseStmt(c)
    case c: DropDatabaseStmtContext    => visitDropDatabaseStmt(c)
    case c: ShowDatabasesStmtContext   => visitShowDatabasesStmt(c)
    case c: ShowCreateTableStmtContext => visitShowCreateTableStmt(c)
    case c: ShowIndexStmtContext       => visitShowIndexStmt(c)
    case c: ShowStatusStmtContext      => visitShowStatusStmt(c)
    case c: DropTableStmtContext       => visitDropTableStmt(c)
    case c: DropIndexStmtContext       => visitDropIndexStmt(c)
    case c: UseStmtContext             => visitUseStmt(c)
    case c: DescribeStmtContext        => visitDescribeStmt(c)
    case c: InsertStmtContext          => visitInsertStmt(c)
    case c: DeleteStmtContext          => visitDeleteStmt(c)
    case c: TruncateStmtContext        => visitTruncateStmt(c)
    case c: UpdateStmtContext          => visitUpdateStmt(c)
    case c: CreateUserStmtContext      => visitCreateUserStmt(c)
    case c: LoadStmtContext            => visitLoadStmt(c)
    case c: RenameTableStmtContext     => visitRenameTableStmt(c)
    case _                             => throw new IllegalArgumentException("unexpected sql")
  }

  //////////////////////////////////////////////////////
  //                     Load Clause                  //
  //////////////////////////////////////////////////////

  override def visitLoadStmt(ctx: LoadStmtContext): SqlNode = {
    val tableName = visitTable_name(ctx.table_name()).asInstanceOf[SqlIdentifier]
    val path = StringUtil.dropQuota(ctx.string().getText)
    var mappings = new SqlNodeList(pos);
    if (ctx.load_mapping_columns() != null) {
      val mappingItems = ctx
        .load_mapping_columns()
        .load_mapping_items()
        .load_mapping_item()
        .asScala
        .map(i => {
          val field = visitIdent(i.ident())
          val expr = visitExpr(i.expr())
          new SqlColumnMappingDeclaration(pos, field, expr)
        })
        .toList
        .asJava
      mappings = new SqlNodeList(mappingItems, pos)
    }
    var delimiter = ","
    var hasDelimiter = false
    if (ctx.csv_file_options() != null) {
      if (ctx.csv_file_options().T_DELIMITER() != null) {
        delimiter = StringUtil.dropQuota(ctx.csv_file_options().string(0).getText)
        hasDelimiter = true
      }
    }
    var quotes = "\""
    var hasQuotes = false
    if (ctx.csv_file_options() != null) {
      if (ctx.csv_file_options().T_QUOTES() != null) {
        quotes = StringUtil.dropQuota(ctx.csv_file_options().string(1).getText)
        hasQuotes = true
      }
    }
    var hasHeader = true
    if (ctx.csv_file_format().T_WITHOUT() != null) {
      hasHeader = false
    }
    new SqlLoadData(
      pos,
      path,
      tableName,
      mappings,
      delimiter,
      quotes,
      hasDelimiter,
      hasQuotes,
      hasHeader
    )
  }

  override def visitTable_name(ctx: Table_nameContext): SqlNode = {
    val names = ctx
      .qident()
      .ident()
      .asScala
      .map(ident => {
        ident.getText
      })
      .toList
      .asJava
    new SqlIdentifier(names, pos)
  }

  //////////////////////////////////////////////////////
  //                    Select Clause                 //
  //////////////////////////////////////////////////////

  override def visitSelectStmt(ctx: SelectStmtContext): SqlNode = {
    // TODO 暂时不考虑cte情况
    visitFullselectStmt(ctx.fullselectStmt())
  }

  def transfer(item: FullselectSetClauseContext): SqlOperator = {
    item.getText.toLowerCase match {
      case UNION     => SqlStdOperatorTable.UNION
      case UNION_ALL => SqlStdOperatorTable.UNION_ALL
      case EXCEPT    => SqlStdOperatorTable.EXCEPT
    }
  }

  override def visitFullselectStmt(ctx: FullselectStmtContext): SqlNode = {
    var stmItem = ctx.fullselectStmtItem().asScala
    if (ctx.fullselectSetClause().size() > 1) {
      val left = visitFullselectStmtItem(stmItem.head)
      val right = visitFullselectStmtItem(stmItem(1))
      val startCall =
        new SqlBasicCall(transfer(ctx.fullselectSetClause().get(0)), Array(left, right), pos)
      stmItem = stmItem.drop(2)
      var clause = ctx.fullselectSetClause().asScala
      val res = stmItem.foldLeft(startCall)((b, a) => {
        clause = clause.drop(1)
        new SqlBasicCall(transfer(clause.head), Array(b, visitFullselectStmtItem(a)), pos)
      })
      res
    } else if (ctx.fullselectSetClause().size() == 1) {
      new SqlBasicCall(
        transfer(ctx.fullselectSetClause().get(0)),
        Array(visitFullselectStmtItem(stmItem.head), visitFullselectStmtItem(stmItem(1))),
        pos
      )
    } else visitFullselectStmtItem(stmItem.head)
  }

  override def visitFullselectStmtItem(ctx: FullselectStmtItemContext): SqlNode = {
    if (ctx.fullselectStmt() != null) {
      return visitFullselectStmt(ctx.fullselectStmt())
    }
    ctx.subselectStmt() match {
      case null => null // TODO 可能需要处理嵌套的事情
      case _    => visitSubselectStmt(ctx.subselectStmt())
    }
  }

  override def visitSubselectStmt(ctx: SubselectStmtContext): SqlNode = {
    val selectList = visitSelectList(ctx.selectList()).asInstanceOf[SqlNodeList]
    val from = if (null != ctx.fromClause()) visitFromClause(ctx.fromClause()) else null
    val where = if (null != ctx.whereClause()) visitBoolExpr(ctx.whereClause().boolExpr) else null
    val groupBy =
      if (null != ctx.groupByClause())
        new SqlNodeList(ctx.groupByClause().expr().asScala.map(visitExpr).asJava, pos)
      else null
    val having =
      if (null != ctx.havingClause()) visitBoolExpr(ctx.havingClause().boolExpr()) else null
    val windowDecls = null // TODO windows function
    val query = new SqlSelect(
      pos,
      getKeyWord(ctx.selectList()),
      selectList,
      from,
      where,
      groupBy,
      having,
      windowDecls,
      null,
      null,
      null,
      null
    )
    val orderBy =
      if (null != ctx.orderByClause())
        new SqlNodeList(
          ctx.orderByClause().orderByClauseItem().asScala.map(visitOrderByClauseItem).asJava,
          pos
        )
      else null
    val offset = null // TODO offset statment
    val fetch =
      if (null != ctx.selectOptions() && null != ctx.selectOptions().selectOptionsItem())
        visitSelectOptionsItem(ctx.selectOptions().selectOptionsItem().get(0))
      else null
    if (null != orderBy || null != fetch) {
      new SqlOrderBy(pos, query, orderBy, offset, fetch)
    } else {
      query
    }
  }

  def visitOverFunction(ctx: ExprFuncOverClauseContext, aggBasicCall: SqlBasicCall): SqlNode = {
    val partitionList = if (ctx.exprFuncPartitionByClause() != null) {
      val tempList = new SqlNodeList(pos)
      ctx.exprFuncPartitionByClause().expr().asScala.foreach(item => tempList.add(visitExpr(item)))
      tempList
    } else SqlNodeList.EMPTY

    val orderList = if (ctx.orderByClause() != null) {
      val tempList = new SqlNodeList(pos)
      ctx
        .orderByClause()
        .orderByClauseItem()
        .asScala
        .foreach(item => tempList.add(visitExpr(item.expr())))
      tempList
    } else SqlNodeList.EMPTY

    val sqlWindow = new SqlWindow(
      pos,
      null,
      null,
      partitionList,
      orderList,
      SqlLiteral.createBoolean(false, pos),
      null,
      null,
      null
    )
    new SqlBasicCall(SqlStdOperatorTable.OVER, Array(aggBasicCall, sqlWindow), pos)
  }

  def getKeyWord(list: SelectListContext): SqlNodeList = {
    if (list.selectListSet() == null) null
    else {
      val context = list.selectListSet()
      val keywords = new util.ArrayList[SqlLiteral]
      context.start.getText.toLowerCase match {
        case DISTINCT => keywords.add(SqlSelectKeyword.DISTINCT.symbol(pos))
        case ALL      => keywords.add(SqlSelectKeyword.ALL.symbol(pos))
        case _        => throw new RuntimeException("not supported operator")
      }
      new SqlNodeList(keywords, pos)
    }
  }

  override def visitSelectList(ctx: SelectListContext): SqlNode = {
    new SqlNodeList(ctx.selectListItem().asScala.map(visitSelectListItem).toList.asJava, pos)
  }

  //////////////////////////////////////////////////////
  //                    From Clause                   //
  //////////////////////////////////////////////////////
  def visitJoinTypeClause(ctx: FromJoinTypeClauseContext): SqlLiteral =
    if (null == ctx) JoinType.COMMA.symbol(pos)
    else
      ctx.getChild(0).getText match {
        case JoinType.LEFT.lowerName  => JoinType.LEFT.symbol(pos)
        case JoinType.RIGHT.lowerName => JoinType.RIGHT.symbol(pos)
        case JoinType.FULL.lowerName  => JoinType.FULL.symbol(pos)
        case _                        => JoinType.INNER.symbol(pos)
      }

  def visitJoinConditionType(context: BoolExprContext): SqlLiteral = context match {
    case null => JoinConditionType.NONE.symbol(pos)
    case _    => JoinConditionType.ON.symbol(pos)
  }

  private def getSqlJoin(
      contexts: mutable.Buffer[FromJoinClauseContext],
      ctx: FromClauseContext,
      flag: Boolean
  ) = {
    val left =
      if (flag) visitFromTableClause(ctx.fromTableClause())
      else
        visitFromTableJoinClause(contexts.drop(1), ctx)
    new SqlJoin(
      pos,
      left,
      SqlLiteral.createBoolean(false, pos),
      visitJoinTypeClause(contexts.head.fromJoinTypeClause()),
      visitFromTableClause(contexts.head.fromTableClause()),
      visitJoinConditionType(contexts.head.boolExpr()),
      if (null != contexts.head.boolExpr()) visitBoolExpr(contexts.head.boolExpr()) else null
    )
  }

  def visitFromTableJoinClause(
      contexts: mutable.Buffer[FromJoinClauseContext],
      ctx: FromClauseContext
  ): SqlNode = {
    if (contexts.size == 1) getSqlJoin(contexts, ctx, flag = true)
    else getSqlJoin(contexts, ctx, flag = false)
  }

  override def visitFromClause(ctx: FromClauseContext): SqlNode = {
    if (ctx.fromJoinClause().isEmpty) visitFromTableClause(ctx.fromTableClause())
    else visitFromTableJoinClause(ctx.fromJoinClause().asScala.reverse, ctx)
  }

  override def visitFromTableNameClause(ctx: FromTableNameClauseContext): SqlNode =
    ctx.fromAliasClause() match {
      case null =>
        if (null != ctx.sampleClause())
          new SqlBasicCall(
            SqlStdOperatorTable.TABLESAMPLE,
            Array(visitTableName(ctx.tableName()), visitSampleClause(ctx.sampleClause())),
            pos
          )
        else visitTableName(ctx.tableName())
      case _ =>
        new SqlBasicCall(
          new SqlAsOperator(),
          Array(visitTableName(ctx.tableName()), visitIdent(ctx.fromAliasClause().ident())),
          pos
        )
    }

  override def visitFromTableClause(ctx: FromTableClauseContext): SqlNode = ctx.getChild(0) match {
    case c: FromTableNameClauseContext => visitFromTableNameClause(c)
    case c: FromSubselectClauseContext => visitFromSubselectClause(c)
    case _                             => null // TODO 其他情况需要实现
  }

  override def visitFromSubselectClause(ctx: FromSubselectClauseContext): SqlNode =
    ctx.fromAliasClause() match {
      case null => visitSelectStmt(ctx.selectStmt())
      case _ =>
        new SqlBasicCall(
          new SqlAsOperator(),
          Array(visitSelectStmt(ctx.selectStmt()), visitIdent(ctx.fromAliasClause().ident())),
          pos
        )
    }

  override def visitTableName(ctx: TableNameContext): SqlIdentifier = {
    var names: mutable.Buffer[String] = ctx.ident().identItem().asScala.map(v => v.getText)
    // TODO 这里应该在antlr里解决反引号的解析问题：select * from `a.b.c` 要解析成 3个字符串，现在是一个
    if (names.length == 1 && names.head.contains("`")) {
      names = StringUtil.dropQuota(names.head).split('.').toBuffer
    }
    // keep username case sensitive
    if (names.length >= 3)
      names = names.takeRight(2).map(v => v.toLowerCase).+:(names(names.length - 3))
    // consider user.database.table
    names.length match {
      case 1 => new SqlIdentifier(MetadataUtil.combineUserDbTableKey(user, db, names.head), pos)
      case 2 =>
        new SqlIdentifier(MetadataUtil.combineUserDbTableKey(user, names.head, names.last), pos)
      case _ =>
        new SqlIdentifier(
          MetadataUtil
            .combineUserDbTableKey(names(names.length - 3), names(names.length - 2), names.last),
          pos
        )
    }
  }

  //////////////////////////////////////////////////////
  //                    Boolean expression            //
  //////////////////////////////////////////////////////

  override def visitBoolExpr(ctx: BoolExprContext): SqlNode = {
    if (null != ctx.boolExprAtom())
      visitBoolExprAtom(ctx.boolExprAtom())
    else if (null != ctx.boolExprLogicalOperator()) {
      new SqlBasicCall(
        visitBoolExprLogicalOperator(ctx.boolExprLogicalOperator()),
        ctx.boolExpr().asScala.map(visitBoolExpr).toArray,
        pos
      )
    } else visitBoolExpr(ctx.boolExpr().asScala.head)
  }

  override def visitBoolExprAtom(ctx: BoolExprAtomContext): SqlNode = {
    ctx.getChild(0) match {
      case c: BoolExprBinaryContext => visitBoolExprBinary(c)
      case c: BoolExprUnaryContext  => visitBoolExprUnary(c)
      case c: ExprContext           => visitExpr(c)
      case _                        => null
    }
  }

  override def visitBoolExprBinary(ctx: BoolExprBinaryContext): SqlNode = {
    val exprs = ctx.expr().asScala.map(visitExpr).toArray
    new SqlBasicCall(visitBoolExprBinaryOperator(ctx.boolExprBinaryOperator()), exprs, pos)
  }

  override def visitBoolExprSingleIn(ctx: BoolExprSingleInContext): SqlNode = {
    val exprs = ctx.expr().asScala.map(visitExpr)
    new SqlBasicCall(
      SqlStdOperatorTable.IN,
      Array(exprs.head, new SqlNodeList(exprs.drop(1).asJava, pos)),
      pos
    )
  }

  override def visitBoolExprLogicalOperator(
      ctx: BoolExprLogicalOperatorContext
  ): SqlBinaryOperator =
    ctx.T_AND() match {
      case null => SqlStdOperatorTable.OR
      case _    => SqlStdOperatorTable.AND
    }

  override def visitBoolExprBinaryOperator(ctx: BoolExprBinaryOperatorContext): SqlOperator = {
    if (null != ctx.T_EQUAL) SqlStdOperatorTable.EQUALS
    else if (null != ctx.T_EQUAL2) SqlStdOperatorTable.EQUALS
    else if (null != ctx.T_GREATER) SqlStdOperatorTable.GREATER_THAN
    else if (null != ctx.T_GREATEREQUAL) SqlStdOperatorTable.GREATER_THAN_OR_EQUAL
    else if (null != ctx.T_LESS) SqlStdOperatorTable.LESS_THAN
    else if (null != ctx.T_LESSEQUAL) SqlStdOperatorTable.LESS_THAN_OR_EQUAL
    else if (null != ctx.T_NOTEQUAL) SqlStdOperatorTable.NOT_EQUALS
    else if (null != ctx.T_NOTEQUAL2) SqlStdOperatorTable.NOT_EQUALS
    else if (null != ctx.T_NOT() && null != ctx.T_LIKE) SqlStdOperatorTable.NOT_LIKE
    else if (null != ctx.T_NOT() && null != ctx.T_REGEXP) null // TODO
    else if (null != ctx.T_NOT() && null != ctx.T_RLIKE) null // TODO
    else if (null == ctx.T_NOT() && null != ctx.T_LIKE) SqlStdOperatorTable.LIKE
    else if (null == ctx.T_NOT() && null != ctx.T_REGEXP) null // TODO
    else if (null == ctx.T_NOT() && null != ctx.T_RLIKE) null // TODO
    else null
  }

  def visitBoolExprContext(c: ExprContext): SqlNode = null // TODO

  override def visitBoolExprUnary(ctx: BoolExprUnaryContext): SqlNode = {
    if (ctx.getText.toLowerCase.contains("between"))
      return new SqlBasicCall(
        SqlStdOperatorTable.BETWEEN,
        ctx.expr().asScala.map(visitExpr).toArray,
        pos
      )
    if (ctx.T_IS() != null && ctx.T_NULL() != null && ctx.T_NOT() != null)
      return new SqlBasicCall(
        SqlStdOperatorTable.IS_NOT_NULL,
        ctx.expr().asScala.map(visitExpr).toArray,
        pos
      )
    if (ctx.T_IS() != null && ctx.T_NULL() != null && ctx.T_NOT() == null)
      return new SqlBasicCall(
        SqlStdOperatorTable.IS_NULL,
        ctx.expr().asScala.map(visitExpr).toArray,
        pos
      )
    ctx.getChild(0) match {
      case c: BoolExprSingleInContext => visitBoolExprSingleIn(c)
      case c: ExprContext             => visitBoolExprContext(c)
      case c                          => null // TODO
    }
  }

  //////////////////////////////////////////////////////
  //                    orderBy Clause                //
  //////////////////////////////////////////////////////
  override def visitOrderByClauseItem(ctx: OrderByClauseItemContext): SqlNode = ctx.T_DESC() match {
    case null => visitExpr(ctx.expr())
    case _    => new SqlBasicCall(SqlStdOperatorTable.DESC, Array(visitExpr(ctx.expr())), pos)
  }

  override def visitSelectOptionsItem(ctx: SelectOptionsItemContext): SqlNode = ctx.expr() match {
    case null => null
    case _    => visitExpr(ctx.expr())
  }

  /////////////////////////////////////////////////////////////////////////
  //                    DQL(Data Query Language)                         //
  /////////////////////////////////////////////////////////////////////////

  override def visitSelectListItem(ctx: SelectListItemContext): SqlNode = ctx.expr() match {
    case null => visitSelectListAsterisk(ctx.selectListAsterisk())
    case _ =>
      if (null != ctx.selectListAlias()) {
        if (null == ctx.selectListAlias().T_CLOSE_P()) {
          new SqlBasicCall(
            SqlStdOperatorTable.AS,
            Array(visitExpr(ctx.expr()), visitIdentOrigin(ctx.selectListAlias().ident(0))),
            pos
          )
        } else {
          new SqlBasicCall(
            SqlStdOperatorTable.AS,
            Array(
              visitExpr(ctx.expr()),
              new SqlNodeList(
                ctx.selectListAlias().ident().asScala.map(visitIdentOrigin).asJava,
                pos
              )
            ),
            pos
          )
        }
      } else visitExpr(ctx.expr())
  }

  override def visitSelectListAsterisk(ctx: SelectListAsteriskContext): SqlNode = ctx.L_ID() match {
    case null => new SqlIdentifier(List("").asJava, pos)
    case _    => new SqlIdentifier(List(ctx.L_ID().getText.toLowerCase, "").asJava, pos)
  }

  override def visitExprFunc(ctx: ExprFuncContext): SqlNode = {
    val funcName =
      new SqlIdentifier(ctx.ident().identItem().asScala.map(_.getText.toLowerCase()).asJava, pos)
    val operator = funcName.names.get(0) match {
      case STRUCT => SqlStdOperatorTable.ROW
      case ARRAY  => SqlStdOperatorTable.ARRAY_VALUE_CONSTRUCTOR
      case MAP    => SqlStdOperatorTable.MAP_VALUE_CONSTRUCTOR
      case _ =>
        new SqlUnresolvedFunction(
          funcName,
          null,
          null,
          null,
          null,
          SqlFunctionCategory.USER_DEFINED_FUNCTION
        )
    }
    val operands: Array[SqlNode] =
      if (null != ctx.exprFuncParams().funcParam().asScala.head.children)
        ctx.exprFuncParams().funcParam().asScala.map(visitFuncParam).toArray
      else SqlNode.EMPTY_ARRAY
    val res = new SqlBasicCall(operator, operands, pos)
    handleFunction(ctx, res)
  }

  override def visitFuncParam(ctx: FuncParamContext): SqlNode = {
    if (null != ctx.expr()) visitExpr(ctx.expr())
    else
      new SqlIdentifier(ctx.T_MUL().getText.toLowerCase, pos)
  }

  override def visitSampleClause(ctx: SampleClauseContext): SqlNode = ctx match {
    case c if null != c.L_INT() =>
      SqlLiteral.createSample(
        SqlSampleSpec.createTableSample(false, (c.L_INT().getText.toDouble / 100).toFloat),
        pos
      )
    case _ => throw new RuntimeException("this sample type has not been supported")
  }

  /////////////////////////////////////////////////////////////////////////
  //                    DDL(Data Definite Language)                      //
  /////////////////////////////////////////////////////////////////////////

  override def visitShowDatabasesStmt(ctx: ShowDatabasesStmtContext): SqlNode = {
    new SqlShowDatabases(pos);
  }

  override def visitCreateDatabaseStmt(ctx: CreateDatabaseStmtContext): SqlNode = {
    new SqlCreateDatabase(pos, new SqlIdentifier(ctx.dbName.getText, pos), ctx.T_EXISTS() != null);
  }

  override def visitDropDatabaseStmt(ctx: DropDatabaseStmtContext): SqlNode = {
    new SqlDropSchema(
      pos,
      false,
      ctx.T_EXISTS() != null,
      new SqlIdentifier(ctx.dbName.getText, pos)
    )
  }

  override def visitDropIndexStmt(ctx: DropIndexStmtContext): SqlNode = {
    new SqlDropIndex(pos, ctx.L_ID().getText, visitIdentOrigin(ctx.tableName().ident()))
  }

  /**
    * handle alias name
    *
    * @param ctx ExprFuncContext
    * @param res SqlBasicCall
    * @return add alias sqlnode
    */
  def handleFunction(ctx: ExprFuncContext, res: SqlBasicCall): SqlNode = {
    val udtfOutputColumns = mutable.HashMap.empty[String, List[String]]
    val reflections = new Reflections("org.urbcomp.start.db.udtf")
    val udtfClasses =
      reflections.getSubTypesOf(classOf[AbstractUdtf]).toSet[Class[_ <: AbstractUdtf]].toList
    udtfClasses.forEach(clazz => {
      val instance = clazz.newInstance()
      val name: String = clazz.getDeclaredMethod("name").invoke(instance).asInstanceOf[String]
      val registerEngines: List[DataEngine.Value] =
        clazz
          .getDeclaredMethod("registerEngines")
          .invoke(instance)
          .asInstanceOf[List[DataEngine.Value]]
      if (registerEngines.contains(Calcite)) {
        val outputColumns: List[(String, SqlTypeName)] =
          clazz
            .getDeclaredMethod("outputColumns")
            .invoke(instance)
            .asInstanceOf[List[(String, SqlTypeName)]]
        udtfOutputColumns += (name.toLowerCase -> outputColumns.map(_._1))
      }
    })

    val text = ctx.ident().getText.toLowerCase
    if (udtfOutputColumns.contains(text)) {
      val nodeList = udtfOutputColumns(text).map(new SqlIdentifier(_, pos)).asJava
      new SqlBasicCall(SqlStdOperatorTable.AS, Array(res, new SqlNodeList(nodeList, pos)), pos)
    } else res

  }

  override def visitUseStmt(ctx: UseStmtContext): SqlUseDatabase = {
    new SqlUseDatabase(pos, new SqlIdentifier(ctx.dbName.getText, pos))
  }

  override def visitCreateTableStmt(ctx: CreateTableStmtContext): SqlNode = {
    val tableName = visitIdent(ctx.table_name().qident().ident().get(0))
    val ifNotExists = null != ctx.T_EXISTS()
    val union = null != ctx.T_UNION()
    val stream = null != ctx.T_STREAM()
    val from = if (null != ctx.fromClause()) visitFromClause(ctx.fromClause()) else null
    val query = if (null != ctx.create_table_definition().selectStmt()) {
      visitSelectStmt(ctx.create_table_definition().selectStmt())
    } else null
    val columnList = if (null != ctx.create_table_definition().create_table_columns()) {
      ctx
        .create_table_definition()
        .create_table_columns()
        .create_table_columns_item()
        .asScala
        .filter(i => i.T_INDEX() == null)
        .map { i =>
          val fieldName = visitIdent(i.column_name().qident().ident().get(0))
          var dataType: SqlUserDefinedTypeNameSpec = null
          if (i.dtype().ident() != null) {
            dataType = new SqlUserDefinedTypeNameSpec(visitIdent(i.dtype().ident()), pos)
          } else {
            dataType =
              new SqlUserDefinedTypeNameSpec(new SqlIdentifier(i.dtype().getText, pos), pos)
          }
          val dataTypeSpec = new SqlDataTypeSpec(dataType, pos)
          SqlDdlNodes.column(pos, fieldName, dataTypeSpec, null, null)
        }
        .toList
        .asJava
    } else null
    val indexList = if (null != ctx.create_table_definition().create_table_columns()) {
      ctx
        .create_table_definition()
        .create_table_columns()
        .create_table_columns_item()
        .asScala
        .filter(i => i.T_INDEX() != null)
        .map { i =>
          var indexType: IndexType = null
          if (i.T_SPATIAL() != null) {
            indexType = IndexType.SPATIAL
          } else if (i.T_ATTRIBUTE() != null) {
            indexType = IndexType.ATTRIBUTE
          } else indexType = IndexType.GRID

          var indexName: SqlIdentifier = null
          if (i.ident() != null) {
            indexName = new SqlIdentifier(i.ident().getText, pos)
          }

          var indexImplType: SqlIdentifier = null
          if (i.index_type_decl() != null) {
            indexImplType = new SqlIdentifier(i.index_type_decl().ident().getText.toLowerCase, pos)
          }

          val columns = i
            .key_list()
            .qident()
            .asScala
            .map(i => {
              new SqlIdentifier(i.getText, pos)
            })
            .toList
            .asJava
          new SqlIndexDeclaration(pos, indexType, indexName, columns, indexImplType)
        }
        .toList
        .asJava
    } else null

    new SqlStartCreateTable(
      pos,
      false,
      ifNotExists,
      stream,
      union,
      from,
      tableName,
      new SqlNodeList(columnList, pos),
      new SqlNodeList(indexList, pos),
      query
    )
  }

  override def visitCreateTableLikeStmt(ctx: CreateTableLikeStmtContext): SqlNode = {
    val targetTableName = visitIdent(ctx.table_name(0).qident().ident().get(0))
    val sourceTableName = visitIdent(ctx.table_name(1).qident().ident().get(0))

    val ifNotExists = null != ctx.T_EXISTS()

    new SqlStartCreateTableLike(pos, false, ifNotExists, targetTableName, sourceTableName)
  }

  override def visitDescribeStmt(ctx: DescribeStmtContext): SqlNode = {
    val targetTable = visitUserDotDbDotTable(ctx.userDotDbDotTable())
    new SqlDescribeTable(pos, targetTable, null)
  }

  override def visitUserDotDbDotTable(ctx: UserDotDbDotTableContext): SqlIdentifier = {
    val targetUser = if (ctx.user != null) ctx.user.getText else user;
    val iden = visitDbDotTable(ctx.dbDotTable())
    val names = List.concat[String](List(targetUser), iden.names.asScala)
    new SqlIdentifier(names.asJava, pos);
  }

  override def visitDbDotTable(ctx: DbDotTableContext): SqlIdentifier = {
    val targetDb = if (ctx.db != null) ctx.db.getText else db;
    val targetTable = ctx.table.getText;
    new SqlIdentifier(List(targetDb, targetTable).asJava, pos);
  }

  override def visitShowCreateTableStmt(ctx: ShowCreateTableStmtContext): SqlNode = {
    val targetTale = visitIdent(ctx.ident());
    new SqlShowCreateTable(pos, targetTale);
  }

  override def visitShowStatusStmt(ctx: ShowStatusStmtContext): SqlNode = {
    new SqlShowStatus(pos)
  }

  override def visitDropTableStmt(ctx: DropTableStmtContext): SqlNode = {
    new SqlDropTable(pos, ctx.T_EXISTS() != null, visitIdent(ctx.tableName().ident()))
  }

  override def visitShowTablesStmt(ctx: ShowTablesStmtContext): SqlNode = {
    new SqlShowTables(pos, new SqlIdentifier(MetadataUtil.combineUserDbKey(user, db), pos))
  }

  override def visitShowIndexStmt(ctx: ShowIndexStmtContext): SqlNode = {
    new SqlShowIndex(pos, visitIdent(ctx.tableName().ident()))
  }

  override def visitRenameTableStmt(ctx: RenameTableStmtContext): SqlNode = {
    val oldTableName = visitOld_name(ctx.old_name()).asInstanceOf[SqlIdentifier]
    val newTableName = visitNew_name(ctx.new_name()).asInstanceOf[SqlIdentifier]
    new SqlRenameTable(pos, oldTableName, newTableName)
  }

  override def visitOld_name(ctx: Old_nameContext): SqlNode = {
    val names = ctx
      .qident()
      .ident()
      .asScala
      .map(ident => {
        ident.getText
      })
      .toList
      .asJava
    new SqlIdentifier(names, pos)
  }

  override def visitNew_name(ctx: New_nameContext): SqlNode = {
    val names = ctx
      .qident()
      .ident()
      .asScala
      .map(ident => {
        ident.getText
      })
      .toList
      .asJava
    new SqlIdentifier(names, pos)
  }

  /////////////////////////////////////////////////////////////////////////
  //                    DCL(Data Control Language)                       //
  /////////////////////////////////////////////////////////////////////////

  override def visitTruncateStmt(ctx: TruncateStmtContext): SqlNode = {
    new SqlTruncateTable(pos, visitIdent(ctx.ident()));
  }

  override def visitCreateUserStmt(ctx: CreateUserStmtContext): SqlNode = {
    var password = ctx.password.getText
    if (password.startsWith("'") && password.endsWith("'") ||
        password.startsWith("\"") && password.endsWith("\"")) {
      password = password.substring(1, password.length - 1)
    }
    new SqlCreateUser(pos, new SqlIdentifier(ctx.user_name.getText, pos), password)
  }

  /////////////////////////////////////////////////////////////////////////
  //                    DML(Data Manage Language)                       //
  /////////////////////////////////////////////////////////////////////////

  override def visitInsertStmt(ctx: InsertStmtContext): SqlInsert = {
    val keyWords = SqlNodeList.EMPTY
    val targetTable = visitIdent(ctx.tableName().ident())
    val columnList =
      if (ctx.insertStmtCols() != null)
        new SqlNodeList(ctx.insertStmtCols().ident().asScala.map(visitIdent).asJava, pos)
      else null
    // insert into table values ...
    if (ctx.insertStmtRows() != null) {
      val rows: Array[SqlNode] = ctx
        .insertStmtRows()
        .insertStmtRow()
        .asScala
        .map { i =>
          val objs = i.expr().asScala.map(visitExpr).toArray
          new SqlBasicCall(SqlStdOperatorTable.ROW, objs, pos)
        }
        .toArray
      val source = new SqlBasicCall(SqlStdOperatorTable.VALUES, rows, pos)
      new SqlInsert(pos, keyWords, targetTable, source, columnList)
    }
    // insert into table select ...
    else {
      val sqlInsert = new SqlInsert(pos, keyWords, targetTable, null, columnList)
      sqlInsert.setSource(visitSelectStmt(ctx.selectStmt()).asInstanceOf[SqlSelect])
      sqlInsert
    }
  }

  private def mkOriginSql(ctx: ParserRuleContext): String = {
    val start = ctx.start.getStartIndex
    val stop = ctx.stop.getStopIndex
    ctx.start.getInputStream.getText(new Interval(start, stop)).replaceAll("\\s+", " ")
  }

  override def visitDeleteStmt(ctx: DeleteStmtContext): SqlNode = {
    val tableIdentifier = visitIdent(ctx.tableName().ident())
    val condition = if (ctx.whereClause() != null) visitWhereClause(ctx.whereClause()) else null
    // TODO  complete sourceSelect and alias
    new SqlDelete(pos, tableIdentifier, condition, null, null)
  }

  override def visitUpdateStmt(ctx: UpdateStmtContext): SqlNode = {
    val targetTable = visitIdent(ctx.updateTable().tableName().ident());
    val condition = visitWhereClause(ctx.whereClause());
    val assignments = ctx.updateAssignment().assignmentStmtItem();
    val columns = assignments.asScala
      .map(assign => {
        // TODO multiple item and select item
        visitIdent(assign.assignmentStmtSingleItem().ident());
      })
      .asJava
    val targetColumnList = new SqlNodeList(columns, pos);
    val expressions = assignments.asScala
      .map(assign => {
        visitExpr(assign.assignmentStmtSingleItem().expr());
      })
      .asJava
    val sourceExpressionList = new SqlNodeList(expressions, pos);
    new SqlUpdate(pos, targetTable, targetColumnList, sourceExpressionList, condition, null, null);
  }

  override def visitUpdateAssignment(ctx: UpdateAssignmentContext): SqlNode = {
    null
  }

  override def visitUpdateTable(ctx: UpdateTableContext): SqlNode = {
    visitTableName(ctx.tableName());
    // TODO: update from select
  }

  override def visitAssignmentStmtItem(ctx: AssignmentStmtItemContext): SqlNode =
    ctx.getChild(0) match {
      case c: AssignmentStmtSingleItemContext => visitAssignmentStmtSingleItem(c)
      case _                                  => null //TODO 暂不考虑其他情况
    }

  override def visitAssignmentStmtSingleItem(ctx: AssignmentStmtSingleItemContext): SqlNode =
    null // TODO

  //////////////////////////////////////////////////////
  //                    叶子节点语法                   //
  //////////////////////////////////////////////////////

  override def visitExpr(ctx: ExprContext): SqlNode = {
    val children = ctx.children.asScala
    children.length match {
      case 1 =>
        children.head match {
          case c: ExprFuncContext          => visitExprFunc(c)
          case c: ExprAtomContext          => visitExprAtom(c)
          case c: ExprAggWindowFuncContext => visitExprAggWindowFunc(c)
          case c: ExprSpecFuncContext      => visitExprSpecFunc(c) // TODO
          case c: ExprCaseContext          => visitExprCase(c)
          case _                           => null // TODO 其他逻辑有待实现
        }
      case 2 => null
      case 3 =>
        children(1) match {
          case c: SelectStmtContext => visitSelectStmt(c)
          case c: ExprContext       => visitExpr(c)
          case _ =>
            children(1).getText match {
              case "+" =>
                new SqlBasicCall(
                  SqlStdOperatorTable.PLUS,
                  Array(
                    visitExpr(children.head.asInstanceOf[ExprContext]),
                    visitExpr(children.last.asInstanceOf[ExprContext])
                  ),
                  pos
                )
              case "-" =>
                new SqlBasicCall(
                  SqlStdOperatorTable.MINUS,
                  Array(
                    visitExpr(children.head.asInstanceOf[ExprContext]),
                    visitExpr(children.last.asInstanceOf[ExprContext])
                  ),
                  pos
                )
              case "*" =>
                new SqlBasicCall(
                  SqlStdOperatorTable.MULTIPLY,
                  Array(
                    visitExpr(children.head.asInstanceOf[ExprContext]),
                    visitExpr(children.last.asInstanceOf[ExprContext])
                  ),
                  pos
                )
              case "/" =>
                new SqlBasicCall(
                  SqlStdOperatorTable.DIVIDE,
                  Array(
                    visitExpr(children.head.asInstanceOf[ExprContext]),
                    visitExpr(children.last.asInstanceOf[ExprContext])
                  ),
                  pos
                )
            }
        }
      case _ => null
    }
  }

  override def visitExprAggWindowFunc(ctx: ExprAggWindowFuncContext): SqlNode = {
    val aggBasicCall = ctx.getStart.getText.toLowerCase match {
      case COUNT =>
        if (ctx.expr().size() == 0)
          new SqlBasicCall(SqlStdOperatorTable.COUNT, Array(SqlIdentifier.STAR), pos)
        else new SqlBasicCall(SqlStdOperatorTable.COUNT, Array(visitExpr(ctx.expr(0))), pos)
      case MAX        => new SqlBasicCall(SqlStdOperatorTable.MAX, Array(visitExpr(ctx.expr(0))), pos)
      case SUM        => new SqlBasicCall(SqlStdOperatorTable.SUM, Array(visitExpr(ctx.expr(0))), pos)
      case AVG        => new SqlBasicCall(SqlStdOperatorTable.AVG, Array(visitExpr(ctx.expr(0))), pos)
      case MIN        => new SqlBasicCall(SqlStdOperatorTable.MIN, Array(visitExpr(ctx.expr(0))), pos)
      case ROW_NUMBER => new SqlBasicCall(SqlStdOperatorTable.ROW_NUMBER, SqlNode.EMPTY_ARRAY, pos)
      case RANK       => new SqlBasicCall(SqlStdOperatorTable.RANK, SqlNode.EMPTY_ARRAY, pos)
      case DENSE_RANK => new SqlBasicCall(SqlStdOperatorTable.DENSE_RANK, SqlNode.EMPTY_ARRAY, pos)
      case _          => null
    }
    if (ctx.exprFuncOverClause() != null) visitOverFunction(ctx.exprFuncOverClause(), aggBasicCall)
    else aggBasicCall
  }

  def castFunction(context: DtypeContext): SqlDataTypeSpec = null // TODO

  override def visitExprSpecFunc(ctx: ExprSpecFuncContext): SqlNode = {
    ctx.start.getText.toLowerCase match {
      case CAST => null // TODO
      case _    => throw new RuntimeException("not supported operator")
    }
  }

  override def visitExprAtom(ctx: ExprAtomContext): SqlNode = ctx.getChild(0) match {
    case c: DateLiteralContext => SqlLiteral.createDate(new DateString(c.getText), pos)
    case c: TimestampLiteralContext => // TODO 此处的precision仍需调研
      SqlLiteral.createTimestamp(new TimestampString(c.getText), 10, pos)
    case c: BoolLiteralContext => SqlLiteral.createBoolean(c.getText.toBoolean, pos)
    case c: StringContext =>
      SqlLiteral.createCharString(StringUtil.dropQuota(c.getText), "UTF-8", pos)
    case c: IdentContext     => visitIdent(c) // 封装标识符
    case c: DecNumberContext => SqlLiteral.createExactNumeric(c.getText, pos)
    case c: IntNumberContext => SqlLiteral.createExactNumeric(c.getText, pos) // 封装整型数字
    case _: NullConstContext => SqlLiteral.createNull(pos)
    case _                   => null // TODO 其他逻辑有待实现
  }

  override def visitWhereClause(ctx: WhereClauseContext): SqlNode = {
    visitBoolExpr(ctx.boolExpr());

  }

  override def visitExprCase(ctx: ExprCaseContext): SqlNode = ctx.getChild(0) match {
    case c: ExprCaseSearchedContext => visitExprCaseSearched(c)
    case c: ExprCaseSimpleContext   => null
  }

  override def visitExprCaseSearched(ctx: ExprCaseSearchedContext): SqlNode = {
    val (whenList, thenList) = ctx
      .exprCaseItem()
      .asScala
      .map { i =>
        (visitBoolExpr(i.whenExpr), visitBoolExpr(i.thenExpr))
      }
      .unzip
    val when = new SqlNodeList(whenList.toList.asJava, pos)
    val then = new SqlNodeList(thenList.toList.asJava, pos)
    val elseNode = visitBoolExpr(ctx.boolExpr())
    new SqlCase(pos, null, when, then, elseNode)
  }

  override def visitIdent(ctx: IdentContext): SqlIdentifier = {
    new SqlIdentifier(ctx.identItem().asScala.map(_.getText.toLowerCase).asJava, pos)
  }

  def visitIdentOrigin(ctx: IdentContext): SqlIdentifier = {
    new SqlIdentifier(ctx.identItem().asScala.map(_.getText).asJava, pos)
  }

}

object StartDBVisitor {
  val COUNT = "count"
  val MAX = "max"
  val MIN = "min"
  val AVG = "avg"
  val SUM = "sum"
  val UNION = "union"
  val UNION_ALL = "unionall"
  val EXCEPT = "except"
  val CAST = "cast"
  val ROW_NUMBER = "row_number"
  val RANK = "rank"
  val DENSE_RANK = "dense_rank"
  val ARRAY = "array"
  val STRUCT = "struct"
  val MAP = "map"
  val STAR = "*"
  val DISTINCT = "distinct"
  val ALL = "all"
}
