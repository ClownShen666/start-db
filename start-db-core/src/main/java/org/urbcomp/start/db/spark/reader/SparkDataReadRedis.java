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
package org.urbcomp.start.db.spark.reader;

import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.parser.LegacyTypeStringParser;
import org.apache.spark.sql.types.AbstractDataType;
import org.apache.spark.sql.types.StructField;
import org.urbcomp.start.db.config.DynamicConfig;
import org.urbcomp.start.db.infra.MetadataResult;
import org.urbcomp.start.db.spark.ISparkDataRead;
import org.urbcomp.start.db.spark.SparkExecutor;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hang Wu
 **/
@Slf4j
public class SparkDataReadRedis implements ISparkDataRead {

    @Override
    public <T> MetadataResult<T> read(String sqlId) {
        try {
            log.info("Redis table: " + sqlId);
            SparkSession spark = SparkExecutor.getSparkSession();
            StructType schema = readSchema(spark, DynamicConfig.getResultSchemaName(sqlId));
            List<Object[]> data = readDataframe(
                spark,
                DynamicConfig.getResultDataName(sqlId),
                schema
            );
            return (MetadataResult<T>) MetadataResult.buildResult(
                Arrays.stream(schema.fields()).map(StructField::name).toArray(String[]::new),
                data
            );
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }

    public StructType readSchema(SparkSession spark, String schemaTable) throws Exception {
        Dataset<Row> schemaDf = spark.read()
            .format("org.apache.spark.sql.redis")
            .option("table", schemaTable)
            .load();
        List<Row> schema = schemaDf.collectAsList();
        if (schema.size() != 1) {
            throw new Exception("Schema table length is " + schema.size() + "!");
        }
        final String schemaJson = schema.get(0).<String>getAs(0);
        AbstractDataType val = null;
        try {
            val = DataType.fromJson(schemaJson);
        } catch (Exception e) {
            val = LegacyTypeStringParser.parseString(schemaJson);
        }
        if (val instanceof StructType) {
            return (StructType) val;
        } else {
            throw new Exception("Schema cannot be deserialized: " + schemaJson);
        }
    }

    private List<Object[]> readDataframe(SparkSession spark, String dataTable, StructType schema)
        throws Exception {
        Dataset<Row> df = spark.read()
            .schema(schema)
            .format("org.apache.spark.sql.redis")
            .option("table", dataTable)
            .option("model", "binary")
            .load();
        log.info("Fetch data from redis: ");
        df.printSchema();
        df.show();
        StructField[] fields = schema.fields();
        return Arrays.stream((Row[]) df.collect()).map(row -> {
            Object[] temp = new Object[fields.length];
            for (int i = 0; i < fields.length; ++i) {
                temp[i] = row.getAs(fields[i].name());
            }
            return temp;
        }).collect(Collectors.toList());
    }
}
