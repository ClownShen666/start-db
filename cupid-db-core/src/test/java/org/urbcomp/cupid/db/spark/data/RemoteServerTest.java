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
package org.urbcomp.cupid.db.spark.data;

import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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

        remoteClient.sendSchema(
            GrpcRemote.SchemaRequest.newBuilder().setSqlId("").setSchemaJson("").build()
        );

        remoteClient.sendRow(GrpcRemote.RowRequest.newBuilder().setSqlId("1").build());
        remoteClient.sendRow(GrpcRemote.RowRequest.newBuilder().setSqlId("2").build());
        remoteClient.sendRow(GrpcRemote.RowRequest.newBuilder().setSqlId("3").build());

        remoteClient.commit();

        TimeUnit.SECONDS.sleep(1);
    }
}
