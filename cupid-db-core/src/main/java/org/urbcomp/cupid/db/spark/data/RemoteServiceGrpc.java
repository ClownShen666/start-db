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

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.19.0)",
    comments = "Source: src/main/resources/proto/grpc_remote.proto"
)
public final class RemoteServiceGrpc {

    private RemoteServiceGrpc() {}

    public static final String SERVICE_NAME = "org.urbcomp.cupid.db.spark.data.RemoteService";

    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<
        org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaRequest,
        org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaResponse> getSendSchemaMethod;

    @io.grpc.stub.annotations.RpcMethod(
        fullMethodName = SERVICE_NAME + '/' + "sendSchema",
        requestType = org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaRequest.class,
        responseType = org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaResponse.class,
        methodType = io.grpc.MethodDescriptor.MethodType.UNARY
    )
    public static
        io.grpc.MethodDescriptor<
            org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaRequest,
            org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaResponse>
        getSendSchemaMethod() {
        io.grpc.MethodDescriptor<
            org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaRequest,
            org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaResponse> getSendSchemaMethod;
        if ((getSendSchemaMethod = RemoteServiceGrpc.getSendSchemaMethod) == null) {
            synchronized (RemoteServiceGrpc.class) {
                if ((getSendSchemaMethod = RemoteServiceGrpc.getSendSchemaMethod) == null) {
                    RemoteServiceGrpc.getSendSchemaMethod = getSendSchemaMethod =
                        io.grpc.MethodDescriptor.<
                            org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaRequest,
                            org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaResponse>newBuilder()
                            .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                            .setFullMethodName(
                                generateFullMethodName(
                                    "org.urbcomp.cupid.db.spark.data.RemoteService",
                                    "sendSchema"
                                )
                            )
                            .setSampledToLocalTracing(true)
                            .setRequestMarshaller(
                                io.grpc.protobuf.ProtoUtils.marshaller(
                                    org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaRequest
                                        .getDefaultInstance()
                                )
                            )
                            .setResponseMarshaller(
                                io.grpc.protobuf.ProtoUtils.marshaller(
                                    org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaResponse
                                        .getDefaultInstance()
                                )
                            )
                            .setSchemaDescriptor(
                                new RemoteServiceMethodDescriptorSupplier("sendSchema")
                            )
                            .build();
                }
            }
        }
        return getSendSchemaMethod;
    }

    private static volatile io.grpc.MethodDescriptor<
        org.urbcomp.cupid.db.spark.data.GrpcRemote.RowRequest,
        org.urbcomp.cupid.db.spark.data.GrpcRemote.RowResponse> getSendRowMethod;

    @io.grpc.stub.annotations.RpcMethod(
        fullMethodName = SERVICE_NAME + '/' + "sendRow",
        requestType = org.urbcomp.cupid.db.spark.data.GrpcRemote.RowRequest.class,
        responseType = org.urbcomp.cupid.db.spark.data.GrpcRemote.RowResponse.class,
        methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING
    )
    public static
        io.grpc.MethodDescriptor<
            org.urbcomp.cupid.db.spark.data.GrpcRemote.RowRequest,
            org.urbcomp.cupid.db.spark.data.GrpcRemote.RowResponse>
        getSendRowMethod() {
        io.grpc.MethodDescriptor<
            org.urbcomp.cupid.db.spark.data.GrpcRemote.RowRequest,
            org.urbcomp.cupid.db.spark.data.GrpcRemote.RowResponse> getSendRowMethod;
        if ((getSendRowMethod = RemoteServiceGrpc.getSendRowMethod) == null) {
            synchronized (RemoteServiceGrpc.class) {
                if ((getSendRowMethod = RemoteServiceGrpc.getSendRowMethod) == null) {
                    RemoteServiceGrpc.getSendRowMethod = getSendRowMethod =
                        io.grpc.MethodDescriptor.<
                            org.urbcomp.cupid.db.spark.data.GrpcRemote.RowRequest,
                            org.urbcomp.cupid.db.spark.data.GrpcRemote.RowResponse>newBuilder()
                            .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
                            .setFullMethodName(
                                generateFullMethodName(
                                    "org.urbcomp.cupid.db.spark.data.RemoteService",
                                    "sendRow"
                                )
                            )
                            .setSampledToLocalTracing(true)
                            .setRequestMarshaller(
                                io.grpc.protobuf.ProtoUtils.marshaller(
                                    org.urbcomp.cupid.db.spark.data.GrpcRemote.RowRequest
                                        .getDefaultInstance()
                                )
                            )
                            .setResponseMarshaller(
                                io.grpc.protobuf.ProtoUtils.marshaller(
                                    org.urbcomp.cupid.db.spark.data.GrpcRemote.RowResponse
                                        .getDefaultInstance()
                                )
                            )
                            .setSchemaDescriptor(
                                new RemoteServiceMethodDescriptorSupplier("sendRow")
                            )
                            .build();
                }
            }
        }
        return getSendRowMethod;
    }

    /**
     * Creates a new async stub that supports all call types for the service
     */
    public static RemoteServiceStub newStub(io.grpc.Channel channel) {
        return new RemoteServiceStub(channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static RemoteServiceBlockingStub newBlockingStub(io.grpc.Channel channel) {
        return new RemoteServiceBlockingStub(channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static RemoteServiceFutureStub newFutureStub(io.grpc.Channel channel) {
        return new RemoteServiceFutureStub(channel);
    }

    /**
     */
    public static abstract class RemoteServiceImplBase implements io.grpc.BindableService {

        /**
         */
        public void sendSchema(
            org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaRequest request,
            io.grpc.stub.StreamObserver<
                org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaResponse> responseObserver
        ) {
            asyncUnimplementedUnaryCall(getSendSchemaMethod(), responseObserver);
        }

        /**
         */
        public
            io.grpc.stub.StreamObserver<org.urbcomp.cupid.db.spark.data.GrpcRemote.RowRequest>
            sendRow(
                io.grpc.stub.StreamObserver<
                    org.urbcomp.cupid.db.spark.data.GrpcRemote.RowResponse> responseObserver
            ) {
            return asyncUnimplementedStreamingCall(getSendRowMethod(), responseObserver);
        }

        @java.lang.Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                    getSendSchemaMethod(),
                    asyncUnaryCall(
                        new MethodHandlers<
                            org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaRequest,
                            org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaResponse>(
                                this,
                                METHODID_SEND_SCHEMA
                            )
                    )
                )
                .addMethod(
                    getSendRowMethod(),
                    asyncClientStreamingCall(
                        new MethodHandlers<
                            org.urbcomp.cupid.db.spark.data.GrpcRemote.RowRequest,
                            org.urbcomp.cupid.db.spark.data.GrpcRemote.RowResponse>(
                                this,
                                METHODID_SEND_ROW
                            )
                    )
                )
                .build();
        }
    }

    /**
     */
    public static final class RemoteServiceStub extends io.grpc.stub.AbstractStub<
        RemoteServiceStub> {
        private RemoteServiceStub(io.grpc.Channel channel) {
            super(channel);
        }

        private RemoteServiceStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected RemoteServiceStub build(
            io.grpc.Channel channel,
            io.grpc.CallOptions callOptions
        ) {
            return new RemoteServiceStub(channel, callOptions);
        }

        /**
         */
        public void sendSchema(
            org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaRequest request,
            io.grpc.stub.StreamObserver<
                org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaResponse> responseObserver
        ) {
            asyncUnaryCall(
                getChannel().newCall(getSendSchemaMethod(), getCallOptions()),
                request,
                responseObserver
            );
        }

        /**
         */
        public
            io.grpc.stub.StreamObserver<org.urbcomp.cupid.db.spark.data.GrpcRemote.RowRequest>
            sendRow(
                io.grpc.stub.StreamObserver<
                    org.urbcomp.cupid.db.spark.data.GrpcRemote.RowResponse> responseObserver
            ) {
            return asyncClientStreamingCall(
                getChannel().newCall(getSendRowMethod(), getCallOptions()),
                responseObserver
            );
        }
    }

    /**
     */
    public static final class RemoteServiceBlockingStub extends io.grpc.stub.AbstractStub<
        RemoteServiceBlockingStub> {
        private RemoteServiceBlockingStub(io.grpc.Channel channel) {
            super(channel);
        }

        private RemoteServiceBlockingStub(
            io.grpc.Channel channel,
            io.grpc.CallOptions callOptions
        ) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected RemoteServiceBlockingStub build(
            io.grpc.Channel channel,
            io.grpc.CallOptions callOptions
        ) {
            return new RemoteServiceBlockingStub(channel, callOptions);
        }

        /**
         */
        public org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaResponse sendSchema(
            org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaRequest request
        ) {
            return blockingUnaryCall(
                getChannel(),
                getSendSchemaMethod(),
                getCallOptions(),
                request
            );
        }
    }

    /**
     */
    public static final class RemoteServiceFutureStub extends io.grpc.stub.AbstractStub<
        RemoteServiceFutureStub> {
        private RemoteServiceFutureStub(io.grpc.Channel channel) {
            super(channel);
        }

        private RemoteServiceFutureStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            super(channel, callOptions);
        }

        @java.lang.Override
        protected RemoteServiceFutureStub build(
            io.grpc.Channel channel,
            io.grpc.CallOptions callOptions
        ) {
            return new RemoteServiceFutureStub(channel, callOptions);
        }

        /**
         */
        public
            com.google.common.util.concurrent.ListenableFuture<
                org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaResponse>
            sendSchema(org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaRequest request) {
            return futureUnaryCall(
                getChannel().newCall(getSendSchemaMethod(), getCallOptions()),
                request
            );
        }
    }

    private static final int METHODID_SEND_SCHEMA = 0;
    private static final int METHODID_SEND_ROW = 1;

    private static final class MethodHandlers<Req, Resp>
        implements
            io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
            io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final RemoteServiceImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(RemoteServiceImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_SEND_SCHEMA:
                    serviceImpl.sendSchema(
                        (org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaRequest) request,
                        (io.grpc.stub.StreamObserver<
                            org.urbcomp.cupid.db.spark.data.GrpcRemote.SchemaResponse>) responseObserver
                    );
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(
            io.grpc.stub.StreamObserver<Resp> responseObserver
        ) {
            switch (methodId) {
                case METHODID_SEND_ROW:
                    return (io.grpc.stub.StreamObserver<Req>) serviceImpl.sendRow(
                        (io.grpc.stub.StreamObserver<
                            org.urbcomp.cupid.db.spark.data.GrpcRemote.RowResponse>) responseObserver
                    );
                default:
                    throw new AssertionError();
            }
        }
    }

    private static abstract class RemoteServiceBaseDescriptorSupplier
        implements
            io.grpc.protobuf.ProtoFileDescriptorSupplier,
            io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        RemoteServiceBaseDescriptorSupplier() {}

        @java.lang.Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return org.urbcomp.cupid.db.spark.data.GrpcRemote.getDescriptor();
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
            return getFileDescriptor().findServiceByName("RemoteService");
        }
    }

    private static final class RemoteServiceFileDescriptorSupplier extends
        RemoteServiceBaseDescriptorSupplier {
        RemoteServiceFileDescriptorSupplier() {}
    }

    private static final class RemoteServiceMethodDescriptorSupplier extends
        RemoteServiceBaseDescriptorSupplier
        implements
            io.grpc.protobuf.ProtoMethodDescriptorSupplier {
        private final String methodName;

        RemoteServiceMethodDescriptorSupplier(String methodName) {
            this.methodName = methodName;
        }

        @java.lang.Override
        public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
            return getServiceDescriptor().findMethodByName(methodName);
        }
    }

    private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

    public static io.grpc.ServiceDescriptor getServiceDescriptor() {
        io.grpc.ServiceDescriptor result = serviceDescriptor;
        if (result == null) {
            synchronized (RemoteServiceGrpc.class) {
                result = serviceDescriptor;
                if (result == null) {
                    serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
                        .setSchemaDescriptor(new RemoteServiceFileDescriptorSupplier())
                        .addMethod(getSendSchemaMethod())
                        .addMethod(getSendRowMethod())
                        .build();
                }
            }
        }
        return result;
    }
}
