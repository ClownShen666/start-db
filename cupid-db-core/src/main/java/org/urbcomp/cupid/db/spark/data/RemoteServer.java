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

import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author jimo
 **/
@Slf4j
public class RemoteServer {

    private int port;
    private final Server server;

    public RemoteServer(int port) {
        // 高版本用法
        // this.server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
        // .addService(new RemoteServiceImpl())
        // .build();
        this(ServerBuilder.forPort(port).addService(new RemoteServiceImpl()).build());
    }

    public RemoteServer(Server server) {
        this.server = server;
        try {
            this.port = server.getPort();
        } catch (IllegalStateException e) {
            log.info("not a HTTP server", e);
            this.port = -1;
        }
    }

    public void start() throws IOException {
        server.start();
        log.info("Grpc Remote Server started, listening on {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("**** shutting down gRPC server since JVM is shutting down **");
                try {
                    RemoteServer.this.stop();
                } catch (InterruptedException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** Server shut down ***");
            }
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }
}
