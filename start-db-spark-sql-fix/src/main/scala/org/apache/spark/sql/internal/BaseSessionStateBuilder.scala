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
package org.apache.spark.sql.internal

import org.apache.spark.SparkConf
import org.apache.spark.annotation.Unstable
import org.apache.spark.sql.{ExperimentalMethods, SparkSession, UDFRegistration, _}
import org.apache.spark.sql.catalyst.analysis.{Analyzer, FunctionRegistry, ResolveSessionCatalog}
import org.apache.spark.sql.catalyst.catalog.SessionCatalog
import org.apache.spark.sql.catalyst.optimizer.Optimizer
import org.apache.spark.sql.catalyst.parser.ParserInterface
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.catalyst.rules.Rule
import org.apache.spark.sql.connector.catalog.CatalogManager
import org.apache.spark.sql.execution.{
  ColumnarRule,
  QueryExecution,
  SparkOptimizer,
  SparkPlan,
  SparkPlanner,
  SparkSqlParser
}
import org.apache.spark.sql.execution.aggregate.ResolveEncodersInScalaAgg
import org.apache.spark.sql.execution.analysis.DetectAmbiguousSelfJoin
import org.apache.spark.sql.execution.command.CommandCheck
import org.apache.spark.sql.execution.datasources._
import org.apache.spark.sql.execution.datasources.v2.{TableCapabilityCheck, V2SessionCatalog}
import org.apache.spark.sql.streaming.StreamingQueryManager
import org.apache.spark.sql.util.ExecutionListenerManager

/**
  * Builder class that coordinates construction of a new [[SessionState]].
  *
  * The builder explicitly defines all components needed by the session state, and creates a session
  * state when `build` is called. Components should only be initialized once. This is not a problem
  * for most components as they are only used in the `build` function. However some components
  * (`conf`, `catalog`, `functionRegistry`, `experimentalMethods` & `sqlParser`) are as dependencies
  * for other components and are shared as a result. These components are defined as lazy vals to
  * make sure the component is created only once.
  *
  * A developer can modify the builder by providing custom versions of components, or by using the
  * hooks provided for the analyzer, optimizer & planner. There are some dependencies between the
  * components (they are documented per dependency), a developer should respect these when making
  * modifications in order to prevent initialization problems.
  *
  * A parent [[SessionState]] can be used to initialize the new [[SessionState]]. The new session
  * state will clone the parent sessions state's `conf`, `functionRegistry`, `experimentalMethods`
  * and `catalog` fields. Note that the state is cloned when `build` is called, and not before.
  */
@Unstable
abstract class BaseSessionStateBuilder(
    val session: SparkSession,
    val parentState: Option[SessionState] = None
) {
  type NewBuilder = (SparkSession, Option[SessionState]) => BaseSessionStateBuilder

  /**
    * Function that produces a new instance of the `BaseSessionStateBuilder`. This is used by the
    * [[SessionState]]'s clone functionality. Make sure to override this when implementing your own
    * [[SessionStateBuilder]].
    */
  protected def newBuilder: NewBuilder

  /**
    * Session extensions defined in the [[SparkSession]].
    */
  protected def extensions: SparkSessionExtensions = session.extensions

  /**
    * Extract entries from `SparkConf` and put them in the `SQLConf`
    */
  protected def mergeSparkConf(sqlConf: SQLConf, sparkConf: SparkConf): Unit = {
    sparkConf.getAll.foreach {
      case (k, v) =>
        sqlConf.setConfString(k, v)
    }
  }

  /**
    * SQL-specific key-value configurations.
    *
    * These either get cloned from a pre-existing instance or newly created. The conf is merged
    * with its [[SparkConf]] only when there is no parent session.
    */
  protected lazy val conf: SQLConf = {
    parentState
      .map { s =>
        val cloned = s.conf.clone()
        if (session.sparkContext.conf.get(StaticSQLConf.SQL_LEGACY_SESSION_INIT_WITH_DEFAULTS)) {
          mergeSparkConf(cloned, session.sparkContext.conf)
        }
        cloned
      }
      .getOrElse {
        val conf = new SQLConf
        mergeSparkConf(conf, session.sparkContext.conf)
        conf
      }
  }

  /**
    * Internal catalog managing functions registered by the user.
    *
    * This either gets cloned from a pre-existing version or cloned from the built-in registry.
    */
  protected lazy val functionRegistry: FunctionRegistry = {
    parentState
      .map(_.functionRegistry.clone())
      .getOrElse(extensions.registerFunctions(FunctionRegistry.builtin.clone()))
  }

  /**
    * Experimental methods that can be used to define custom optimization rules and custom planning
    * strategies.
    *
    * This either gets cloned from a pre-existing version or newly created.
    */
  protected lazy val experimentalMethods: ExperimentalMethods = {
    parentState.map(_.experimentalMethods.clone()).getOrElse(new ExperimentalMethods)
  }

  /**
    * Parser that extracts expressions, plans, table identifiers etc. from SQL texts.
    *
    * Note: this depends on the `conf` field.
    */
  protected lazy val sqlParser: ParserInterface = {
    extensions.buildParser(session, new SparkSqlParser(conf))
  }

  /**
    * ResourceLoader that is used to load function resources and jars.
    */
  protected lazy val resourceLoader: SessionResourceLoader = new SessionResourceLoader(session)

  /**
    * Catalog for managing table and database states. If there is a pre-existing catalog, the state
    * of that catalog (temp tables & current database) will be copied into the new catalog.
    *
    * Note: this depends on the `conf`, `functionRegistry` and `sqlParser` fields.
    */
  protected lazy val catalog: SessionCatalog = {
    val catalog = new SessionCatalog(
      () => session.sharedState.externalCatalog,
      () => session.sharedState.globalTempViewManager,
      functionRegistry,
      conf,
      SessionState.newHadoopConf(session.sparkContext.hadoopConfiguration, conf),
      sqlParser,
      resourceLoader
    )
    parentState.foreach(_.catalog.copyStateTo(catalog))
    catalog
  }

  protected lazy val v2SessionCatalog = new V2SessionCatalog(catalog, conf)

  protected lazy val catalogManager = new CatalogManager(conf, v2SessionCatalog, catalog)

  /**
    * Interface exposed to the user for registering user-defined functions.
    *
    * Note 1: The user-defined functions must be deterministic.
    * Note 2: This depends on the `functionRegistry` field.
    */
  protected def udfRegistration: UDFRegistration = new UDFRegistration(functionRegistry)

  /**
    * Logical query plan analyzer for resolving unresolved attributes and relations.
    *
    * Note: this depends on the `conf` and `catalog` fields.
    */
  protected def analyzer: Analyzer = new Analyzer(catalogManager, conf) {
    override val extendedResolutionRules: Seq[Rule[LogicalPlan]] =
      new FindDataSourceTable(session) +:
        new ResolveSQLOnFile(session) +:
        new FallBackFileSourceV2(session) +:
        ResolveEncodersInScalaAgg +:
        new ResolveSessionCatalog(catalogManager, conf, catalog.isTempView, catalog.isTempFunction) +:
        customResolutionRules

    override val postHocResolutionRules: Seq[Rule[LogicalPlan]] =
      new DetectAmbiguousSelfJoin(conf) +:
        PreprocessTableCreation(session) +:
        PreprocessTableInsertion(conf) +:
        DataSourceAnalysis(conf) +:
        customPostHocResolutionRules

    override val extendedCheckRules: Seq[LogicalPlan => Unit] =
      PreWriteCheck +:
        PreReadCheck +:
        HiveOnlyCheck +:
        TableCapabilityCheck +:
        CommandCheck(conf) +:
        customCheckRules
  }

  /**
    * Custom resolution rules to add to the Analyzer. Prefer overriding this instead of creating
    * your own Analyzer.
    *
    * Note that this may NOT depend on the `analyzer` function.
    */
  protected def customResolutionRules: Seq[Rule[LogicalPlan]] = {
    extensions.buildResolutionRules(session)
  }

  /**
    * Custom post resolution rules to add to the Analyzer. Prefer overriding this instead of
    * creating your own Analyzer.
    *
    * Note that this may NOT depend on the `analyzer` function.
    */
  protected def customPostHocResolutionRules: Seq[Rule[LogicalPlan]] = {
    extensions.buildPostHocResolutionRules(session)
  }

  /**
    * Custom check rules to add to the Analyzer. Prefer overriding this instead of creating
    * your own Analyzer.
    *
    * Note that this may NOT depend on the `analyzer` function.
    */
  protected def customCheckRules: Seq[LogicalPlan => Unit] = {
    extensions.buildCheckRules(session)
  }

  /**
    * Logical query plan optimizer.
    *
    * Note: this depends on `catalog` and `experimentalMethods` fields.
    */
  protected def optimizer: Optimizer = {
    new SparkOptimizer(catalogManager, catalog, experimentalMethods) {
      override def earlyScanPushDownRules: Seq[Rule[LogicalPlan]] =
        super.earlyScanPushDownRules ++ customEarlyScanPushDownRules

      override def extendedOperatorOptimizationRules: Seq[Rule[LogicalPlan]] =
        super.extendedOperatorOptimizationRules ++ customOperatorOptimizationRules
    }
  }

  /**
    * Custom operator optimization rules to add to the Optimizer. Prefer overriding this instead
    * of creating your own Optimizer.
    *
    * Note that this may NOT depend on the `optimizer` function.
    */
  protected def customOperatorOptimizationRules: Seq[Rule[LogicalPlan]] = {
    extensions.buildOptimizerRules(session)
  }

  /**
    * Custom early scan push down rules to add to the Optimizer. Prefer overriding this instead
    * of creating your own Optimizer.
    *
    * Note that this may NOT depend on the `optimizer` function.
    */
  protected def customEarlyScanPushDownRules: Seq[Rule[LogicalPlan]] = Nil

  /**
    * Planner that converts optimized logical plans to physical plans.
    *
    * Note: this depends on the `conf` and `experimentalMethods` fields.
    */
  protected def planner: SparkPlanner = {
    new SparkPlanner(session, conf, experimentalMethods) {
      override def extraPlanningStrategies: Seq[Strategy] =
        super.extraPlanningStrategies ++ customPlanningStrategies
    }
  }

  /**
    * Custom strategies to add to the planner. Prefer overriding this instead of creating
    * your own Planner.
    *
    * Note that this may NOT depend on the `planner` function.
    */
  protected def customPlanningStrategies: Seq[Strategy] = {
    extensions.buildPlannerStrategies(session)
  }

  protected def columnarRules: Seq[ColumnarRule] = {
    extensions.buildColumnarRules(session)
  }

  protected def queryStagePrepRules: Seq[Rule[SparkPlan]] = {
    extensions.buildQueryStagePrepRules(session)
  }

  /**
    * Create a query execution object.
    */
  protected def createQueryExecution: LogicalPlan => QueryExecution = { plan =>
    new QueryExecution(session, plan)
  }

  /**
    * Interface to start and stop streaming queries.
    */
  protected def streamingQueryManager: StreamingQueryManager = new StreamingQueryManager(session)

  /**
    * An interface to register custom [[org.apache.spark.sql.util.QueryExecutionListener]]s
    * that listen for execution metrics.
    *
    * This gets cloned from parent if available, otherwise a new instance is created.
    */
  protected def listenerManager: ExecutionListenerManager = {
    parentState
      .map(_.listenerManager.clone(session))
      .getOrElse(new ExecutionListenerManager(session, loadExtensions = true))
  }

  /**
    * Function used to make clones of the session state.
    */
  protected def createClone: (SparkSession, SessionState) => SessionState = {
    val createBuilder = newBuilder
    (session, state) => createBuilder(session, Option(state)).build()
  }

  /**
    * Build the [[SessionState]].
    */
  def build(): SessionState = {
    new SessionState(
      session.sharedState,
      conf,
      experimentalMethods,
      functionRegistry,
      udfRegistration,
      () => catalog,
      sqlParser,
      () => analyzer,
      () => optimizer,
      planner,
      () => streamingQueryManager,
      listenerManager,
      () => resourceLoader,
      createQueryExecution,
      createClone,
      columnarRules,
      queryStagePrepRules
    )
  }
}

/**
  * Helper class for using SessionStateBuilders during tests.
  */
private[sql] trait WithTestConf { self: BaseSessionStateBuilder =>
  def overrideConfs: Map[String, String]

  override protected lazy val conf: SQLConf = {
    val overrideConfigurations = overrideConfs
    parentState
      .map { s =>
        val cloned = s.conf.clone()
        if (session.sparkContext.conf.get(StaticSQLConf.SQL_LEGACY_SESSION_INIT_WITH_DEFAULTS)) {
          mergeSparkConf(conf, session.sparkContext.conf)
        }
        cloned
      }
      .getOrElse {
        val conf = new SQLConf {
          clear()
          override def clear(): Unit = {
            super.clear()
            // Make sure we start with the default test configs even after clear
            overrideConfigurations.foreach { case (key, value) => setConfString(key, value) }
          }
        }
        mergeSparkConf(conf, session.sparkContext.conf)
        conf
      }
  }
}
