package org.urbcomp.cupid.db.spark.res

import org.apache.spark.sql.{DataFrame, SaveMode}
import org.urbcomp.cupid.db.spark.ds.remote.RemoteWriteSource

/**
 * @author jimo
 * */
class SparkResult2RemoteExporter extends ISparkResultExporter {
  override def exportData(sqlId: String, data: DataFrame): Unit = {
    data.coalesce(2)
      .write
      .format(RemoteWriteSource.getClass.getCanonicalName)
      .mode(SaveMode.Overwrite)
      .option(RemoteWriteSource.SCHEMA_KEY, data.schema.json)
      .option("sqlId", sqlId)
      .save()
  }
}
