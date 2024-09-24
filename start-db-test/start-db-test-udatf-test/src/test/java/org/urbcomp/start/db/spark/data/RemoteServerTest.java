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
package org.urbcomp.start.db.spark.data;

import com.google.protobuf.ByteString;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.apache.spark.sql.catalyst.expressions.GenericInternalRow;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.Test;
import org.urbcomp.start.db.infra.MetadataResult;
import org.urbcomp.start.db.spark.cache.SparkDataSerializer;
import org.urbcomp.start.db.spark.reader.SparkDataReadCache;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * ref https://github.com/grpc/grpc-java/blob/v1.19.x/examples/src/test/java/io/grpc/examples/routeguide/RouteGuideServerTest.java
 */
public class RemoteServerTest {

    @Test
    public void testClientServer() throws InterruptedException, IOException {
        final String name = InProcessServerBuilder.generateName();
        final RemoteServer server = new RemoteServer(
            InProcessServerBuilder.forName(name).addService(new RemoteServiceImpl()).build()
        );
        server.start();

        final RemoteClient remoteClient = new RemoteClient(
            InProcessChannelBuilder.forName(name).directExecutor().build()
        );

        final StructType structType = new StructType(
            new StructField[] {
                new StructField("age", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("name", DataTypes.StringType, true, Metadata.empty()) }
        );

        final String sqlId = "1";
        remoteClient.sendSchema(
            GrpcRemote.SchemaRequest.newBuilder()
                .setSqlId(sqlId)
                .setSchemaJson(structType.json())
                .build()
        );

        final GenericInternalRow row = new GenericInternalRow(new Object[] { 18, "jimo" });
        final byte[] bytes = SparkDataSerializer.serialize(row, structType);
        final ByteString byteStringRow = ByteString.copyFrom(bytes);
        remoteClient.sendRow(
            GrpcRemote.RowRequest.newBuilder().setSqlId(sqlId).setData(byteStringRow).build()
        );
        remoteClient.sendRow(
            GrpcRemote.RowRequest.newBuilder().setSqlId(sqlId).setData(byteStringRow).build()
        );
        remoteClient.sendRow(
            GrpcRemote.RowRequest.newBuilder().setSqlId(sqlId).setData(byteStringRow).build()
        );

        remoteClient.commit();

        TimeUnit.SECONDS.sleep(1);

        // check value
        final SparkDataReadCache sparkDataReadCache = new SparkDataReadCache();
        final MetadataResult<Object> res = sparkDataReadCache.read(sqlId);
        assertEquals(2, res.columns.size());
    }
}
