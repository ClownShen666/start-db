package org.urbcomp.cupid.db.server;

import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import lombok.extern.slf4j.Slf4j;
import org.urbcomp.cupid.db.spark.data.RemoteServiceImpl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author jimo
 **/
@Slf4j
public class RemoteServer {

    private final int port;
    private final Server server;

    public RemoteServer(int port) {
        this.port = port;
        this.server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(new RemoteServiceImpl())
                .build();
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
