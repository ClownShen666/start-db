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
package org.urbcomp.cupid.db.spark;

import org.urbcomp.cupid.db.util.SparkSqlParam;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author jimo
 **/
public class LocalSparkSubmitter implements ISparkSubmitter {

    @Override
    public SubmitResult submit(SparkSqlParam param) {
        final String sqlId = UUID.randomUUID().toString();
        param.setSqlId(sqlId);
        SparkQueryExecutor.execute(param, null);
        return SubmitResult.builder().sqlId(sqlId).build();
    }

    @Override
    public void waitToFinish(SubmitResult res) throws TimeoutException {}
}
