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

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoteServiceImpl extends RemoteServiceGrpc.RemoteServiceImplBase {

    @Override
    public void sendSchema(
        GrpcRemote.SchemaRequest request,
        StreamObserver<GrpcRemote.SchemaResponse> responseObserver
    ) {
        final String sqlId = request.getSqlId();
        final String schemaJson = request.getSchemaJson();
        System.out.println("收到schema：" + schemaJson);

        final GrpcRemote.SchemaResponse response = GrpcRemote.SchemaResponse.newBuilder()
            .setRes("ok")
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GrpcRemote.RowRequest> sendRow(
        StreamObserver<GrpcRemote.RowResponse> responseObserver
    ) {
        return new StreamObserver<GrpcRemote.RowRequest>() {
            @Override
            public void onNext(GrpcRemote.RowRequest rowRequest) {
                System.out.println(
                    "收到一条数据：" + rowRequest.getSqlId() + ", bytes=" + rowRequest.getData()
                );
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("accept row error", throwable);
            }

            @Override
            public void onCompleted() {
                System.out.println("结束了");
                responseObserver.onNext(GrpcRemote.RowResponse.newBuilder().build());
                responseObserver.onCompleted();
            }
        };
    }
}
