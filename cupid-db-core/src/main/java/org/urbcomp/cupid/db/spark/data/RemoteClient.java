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

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class RemoteClient implements Serializable {

    private final RemoteServiceGrpc.RemoteServiceBlockingStub blockingStub;
    private final StreamObserver<GrpcRemote.RowRequest> requestStreamObserver;

    public RemoteClient() {
        this(ManagedChannelBuilder.forAddress("localhost", 8848).usePlaintext().build());
    }

    public RemoteClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    public RemoteClient(Channel channel) {
        this.blockingStub = RemoteServiceGrpc.newBlockingStub(channel);
        RemoteServiceGrpc.RemoteServiceStub asyncStub = RemoteServiceGrpc.newStub(channel);

        final StreamObserver<GrpcRemote.RowResponse> streamObserver = new StreamObserver<
            GrpcRemote.RowResponse>() {

            @Override
            public void onNext(GrpcRemote.RowResponse rowResponse) {
                log.info("SendRow onNext");
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("SendRow Error", throwable);
            }

            @Override
            public void onCompleted() {
                log.info("SendRow Finished");
            }
        };
        requestStreamObserver = asyncStub.sendRow(streamObserver);
    }

    public void sendSchema(GrpcRemote.SchemaRequest request) {
        final GrpcRemote.SchemaResponse resp = blockingStub.sendSchema(request);
    }

    public void sendRow(GrpcRemote.RowRequest request) {
        requestStreamObserver.onNext(request);
    }

    public void commit() {
        requestStreamObserver.onCompleted();
    }

    public void error() {
        requestStreamObserver.onError(new RuntimeException("Abort"));
    }
}
