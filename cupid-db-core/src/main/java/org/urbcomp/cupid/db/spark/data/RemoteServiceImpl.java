package org.urbcomp.cupid.db.spark.data;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoteServiceImpl extends RemoteServiceGrpc.RemoteServiceImplBase {

    @Override
    public void sendSchema(GrpcRemote.SchemaRequest request, StreamObserver<GrpcRemote.SchemaResponse> responseObserver) {
        final String sqlId = request.getSqlId();
        final String schemaJson = request.getSchemaJson();
        System.out.println("收到schema：" + schemaJson);

        final GrpcRemote.SchemaResponse response = GrpcRemote.SchemaResponse.newBuilder().setRes("ok").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GrpcRemote.RowRequest> sendRow(StreamObserver<GrpcRemote.RowResponse> responseObserver) {
        return new StreamObserver<GrpcRemote.RowRequest>() {
            @Override
            public void onNext(GrpcRemote.RowRequest rowRequest) {
                System.out.println("收到一条数据：" + rowRequest.getSqlId());
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
