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
package org.urbcomp.start.db.spark;

import org.apache.spark.sql.SparkSession;
import org.urbcomp.start.db.config.DynamicConfig;
import org.urbcomp.start.db.infra.MetadataResult;
import org.urbcomp.start.db.util.SparkSqlParam;

import java.util.concurrent.TimeoutException;

/**
 * @author jimo
 **/
public class SparkExecutor {
    private static SparkSession spark = null;
    static {
        if (DynamicConfig.getSparkRedisHost().isEmpty()) {
            spark = SparkQueryExecutor.getSparkSession(true, scala.Option.apply(null));
        } else {
            spark = SparkQueryExecutor.getSparkSession(
                true,
                scala.Option.apply(
                    new SparkQueryExecutor.RedisConf(
                        DynamicConfig.getSparkRedisHost(),
                        DynamicConfig.getSparkRedisPort(),
                        DynamicConfig.getSparkRedisAuth()
                    )
                )
            );
        }
    }

    public static SparkSession getSparkSession() {
        return spark;
    }

    /**
     * 提交到spark并返回结果
     * 如果是异步，那么只返回一个提交成功
     */
    public <T> MetadataResult<T> execute(SparkSqlParam param) {
        final ISparkSubmitter submitter = ISparkSubmitter.getInstance(param.getExecuteEngine());
        final SubmitResult res = submitter.submit(param);
        if (param.isAsync()) {
            // TODO 记录执行任务到缓存队列
            return MetadataResult.buildDDLResult(0);
        }
        // wait result
        try {
            submitter.waitToFinish(res);
        } catch (TimeoutException e) {
            throw new RuntimeException("sync wait timeout", e);
        }
        return ISparkDataRead.getReader(param.getExportType()).read(res.getSqlId());
    }
}
