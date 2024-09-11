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

public final class GrpcRemote {
    private GrpcRemote() {}

    public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {}

    public interface SchemaRequestOrBuilder extends com.google.protobuf.MessageOrBuilder {

        // required string sqlId = 1;
        /**
         * <code>required string sqlId = 1;</code>
         */
        boolean hasSqlId();

        /**
         * <code>required string sqlId = 1;</code>
         */
        java.lang.String getSqlId();

        /**
         * <code>required string sqlId = 1;</code>
         */
        com.google.protobuf.ByteString getSqlIdBytes();

        // required string schemaJson = 2;
        /**
         * <code>required string schemaJson = 2;</code>
         */
        boolean hasSchemaJson();

        /**
         * <code>required string schemaJson = 2;</code>
         */
        java.lang.String getSchemaJson();

        /**
         * <code>required string schemaJson = 2;</code>
         */
        com.google.protobuf.ByteString getSchemaJsonBytes();
    }

    /**
     * Protobuf type {@code org.urbcomp.start.db.spark.data.SchemaRequest}
     */
    public static final class SchemaRequest extends com.google.protobuf.GeneratedMessage
        implements
            SchemaRequestOrBuilder {
        // Use SchemaRequest.newBuilder() to construct.
        private SchemaRequest(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private SchemaRequest(boolean noInit) {
            this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
        }

        private static final SchemaRequest defaultInstance;

        public static SchemaRequest getDefaultInstance() {
            return defaultInstance;
        }

        public SchemaRequest getDefaultInstanceForType() {
            return defaultInstance;
        }

        private final com.google.protobuf.UnknownFieldSet unknownFields;

        @java.lang.Override
        public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private SchemaRequest(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            initFields();
            int mutable_bitField0_ = 0;
            com.google.protobuf.UnknownFieldSet.Builder unknownFields =
                com.google.protobuf.UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            done = true;
                            break;
                        default: {
                            if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                done = true;
                            }
                            break;
                        }
                        case 10: {
                            bitField0_ |= 0x00000001;
                            sqlId_ = input.readBytes();
                            break;
                        }
                        case 18: {
                            bitField0_ |= 0x00000002;
                            schemaJson_ = input.readBytes();
                            break;
                        }
                    }
                }
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(this);
            } catch (java.io.IOException e) {
                throw new com.google.protobuf.InvalidProtocolBufferException(e.getMessage())
                    .setUnfinishedMessage(this);
            } finally {
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_SchemaRequest_descriptor;
        }

        protected
            com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
            return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_SchemaRequest_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                    org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest.class,
                    org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest.Builder.class
                );
        }

        public static com.google.protobuf.Parser<SchemaRequest> PARSER =
            new com.google.protobuf.AbstractParser<SchemaRequest>() {
                public SchemaRequest parsePartialFrom(
                    com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry
                ) throws com.google.protobuf.InvalidProtocolBufferException {
                    return new SchemaRequest(input, extensionRegistry);
                }
            };

        @java.lang.Override
        public com.google.protobuf.Parser<SchemaRequest> getParserForType() {
            return PARSER;
        }

        private int bitField0_;
        // required string sqlId = 1;
        public static final int SQLID_FIELD_NUMBER = 1;
        private java.lang.Object sqlId_;

        /**
         * <code>required string sqlId = 1;</code>
         */
        public boolean hasSqlId() {
            return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        /**
         * <code>required string sqlId = 1;</code>
         */
        public java.lang.String getSqlId() {
            java.lang.Object ref = sqlId_;
            if (ref instanceof java.lang.String) {
                return (java.lang.String) ref;
            } else {
                com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    sqlId_ = s;
                }
                return s;
            }
        }

        /**
         * <code>required string sqlId = 1;</code>
         */
        public com.google.protobuf.ByteString getSqlIdBytes() {
            java.lang.Object ref = sqlId_;
            if (ref instanceof java.lang.String) {
                com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref
                );
                sqlId_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        // required string schemaJson = 2;
        public static final int SCHEMAJSON_FIELD_NUMBER = 2;
        private java.lang.Object schemaJson_;

        /**
         * <code>required string schemaJson = 2;</code>
         */
        public boolean hasSchemaJson() {
            return ((bitField0_ & 0x00000002) == 0x00000002);
        }

        /**
         * <code>required string schemaJson = 2;</code>
         */
        public java.lang.String getSchemaJson() {
            java.lang.Object ref = schemaJson_;
            if (ref instanceof java.lang.String) {
                return (java.lang.String) ref;
            } else {
                com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    schemaJson_ = s;
                }
                return s;
            }
        }

        /**
         * <code>required string schemaJson = 2;</code>
         */
        public com.google.protobuf.ByteString getSchemaJsonBytes() {
            java.lang.Object ref = schemaJson_;
            if (ref instanceof java.lang.String) {
                com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref
                );
                schemaJson_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        private void initFields() {
            sqlId_ = "";
            schemaJson_ = "";
        }

        private byte memoizedIsInitialized = -1;

        public final boolean isInitialized() {
            byte isInitialized = memoizedIsInitialized;
            if (isInitialized != -1) return isInitialized == 1;

            if (!hasSqlId()) {
                memoizedIsInitialized = 0;
                return false;
            }
            if (!hasSchemaJson()) {
                memoizedIsInitialized = 0;
                return false;
            }
            memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
            getSerializedSize();
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                output.writeBytes(1, getSqlIdBytes());
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                output.writeBytes(2, getSchemaJsonBytes());
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;

            size = 0;
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                size += com.google.protobuf.CodedOutputStream.computeBytesSize(1, getSqlIdBytes());
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                size += com.google.protobuf.CodedOutputStream.computeBytesSize(
                    2,
                    getSchemaJsonBytes()
                );
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        private static final long serialVersionUID = 0L;

        @java.lang.Override
        protected java.lang.Object writeReplace() throws java.io.ObjectStreamException {
            return super.writeReplace();
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest parseFrom(
            com.google.protobuf.ByteString data
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest parseFrom(
            byte[] data
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest parseFrom(
            java.io.InputStream input
        ) throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest parseDelimitedFrom(
            java.io.InputStream input
        ) throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest parseFrom(
            com.google.protobuf.CodedInputStream input
        ) throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(
            org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest prototype
        ) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessage.BuilderParent parent
        ) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /**
         * Protobuf type {@code org.urbcomp.start.db.spark.data.SchemaRequest}
         */
        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<
            Builder> implements org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequestOrBuilder {
            public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_SchemaRequest_descriptor;
            }

            protected
                com.google.protobuf.GeneratedMessage.FieldAccessorTable
                internalGetFieldAccessorTable() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_SchemaRequest_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                        org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest.class,
                        org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest.Builder.class
                    );
            }

            // Construct using org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest.newBuilder()
            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                }
            }

            private static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                sqlId_ = "";
                bitField0_ = (bitField0_ & ~0x00000001);
                schemaJson_ = "";
                bitField0_ = (bitField0_ & ~0x00000002);
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_SchemaRequest_descriptor;
            }

            public
                org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest
                getDefaultInstanceForType() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest
                    .getDefaultInstance();
            }

            public org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest build() {
                org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            public org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest buildPartial() {
                org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest result =
                    new org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest(this);
                int from_bitField0_ = bitField0_;
                int to_bitField0_ = 0;
                if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
                    to_bitField0_ |= 0x00000001;
                }
                result.sqlId_ = sqlId_;
                if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
                    to_bitField0_ |= 0x00000002;
                }
                result.schemaJson_ = schemaJson_;
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest) {
                    return mergeFrom(
                        (org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest) other
                    );
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(
                org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest other
            ) {
                if (other == org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest
                    .getDefaultInstance()) return this;
                if (other.hasSqlId()) {
                    bitField0_ |= 0x00000001;
                    sqlId_ = other.sqlId_;
                    onChanged();
                }
                if (other.hasSchemaJson()) {
                    bitField0_ |= 0x00000002;
                    schemaJson_ = other.schemaJson_;
                    onChanged();
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public final boolean isInitialized() {
                if (!hasSqlId()) {

                    return false;
                }
                if (!hasSchemaJson()) {

                    return false;
                }
                return true;
            }

            public Builder mergeFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry
            ) throws java.io.IOException {
                org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest parsedMessage = null;
                try {
                    parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
                } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                    parsedMessage = (org.urbcomp.start.db.spark.data.GrpcRemote.SchemaRequest) e
                        .getUnfinishedMessage();
                    throw e;
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            private int bitField0_;

            // required string sqlId = 1;
            private java.lang.Object sqlId_ = "";

            /**
             * <code>required string sqlId = 1;</code>
             */
            public boolean hasSqlId() {
                return ((bitField0_ & 0x00000001) == 0x00000001);
            }

            /**
             * <code>required string sqlId = 1;</code>
             */
            public java.lang.String getSqlId() {
                java.lang.Object ref = sqlId_;
                if (!(ref instanceof java.lang.String)) {
                    java.lang.String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
                    sqlId_ = s;
                    return s;
                } else {
                    return (java.lang.String) ref;
                }
            }

            /**
             * <code>required string sqlId = 1;</code>
             */
            public com.google.protobuf.ByteString getSqlIdBytes() {
                java.lang.Object ref = sqlId_;
                if (ref instanceof String) {
                    com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8(
                        (java.lang.String) ref
                    );
                    sqlId_ = b;
                    return b;
                } else {
                    return (com.google.protobuf.ByteString) ref;
                }
            }

            /**
             * <code>required string sqlId = 1;</code>
             */
            public Builder setSqlId(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                sqlId_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>required string sqlId = 1;</code>
             */
            public Builder clearSqlId() {
                bitField0_ = (bitField0_ & ~0x00000001);
                sqlId_ = getDefaultInstance().getSqlId();
                onChanged();
                return this;
            }

            /**
             * <code>required string sqlId = 1;</code>
             */
            public Builder setSqlIdBytes(com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                sqlId_ = value;
                onChanged();
                return this;
            }

            // required string schemaJson = 2;
            private java.lang.Object schemaJson_ = "";

            /**
             * <code>required string schemaJson = 2;</code>
             */
            public boolean hasSchemaJson() {
                return ((bitField0_ & 0x00000002) == 0x00000002);
            }

            /**
             * <code>required string schemaJson = 2;</code>
             */
            public java.lang.String getSchemaJson() {
                java.lang.Object ref = schemaJson_;
                if (!(ref instanceof java.lang.String)) {
                    java.lang.String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
                    schemaJson_ = s;
                    return s;
                } else {
                    return (java.lang.String) ref;
                }
            }

            /**
             * <code>required string schemaJson = 2;</code>
             */
            public com.google.protobuf.ByteString getSchemaJsonBytes() {
                java.lang.Object ref = schemaJson_;
                if (ref instanceof String) {
                    com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8(
                        (java.lang.String) ref
                    );
                    schemaJson_ = b;
                    return b;
                } else {
                    return (com.google.protobuf.ByteString) ref;
                }
            }

            /**
             * <code>required string schemaJson = 2;</code>
             */
            public Builder setSchemaJson(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000002;
                schemaJson_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>required string schemaJson = 2;</code>
             */
            public Builder clearSchemaJson() {
                bitField0_ = (bitField0_ & ~0x00000002);
                schemaJson_ = getDefaultInstance().getSchemaJson();
                onChanged();
                return this;
            }

            /**
             * <code>required string schemaJson = 2;</code>
             */
            public Builder setSchemaJsonBytes(com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000002;
                schemaJson_ = value;
                onChanged();
                return this;
            }

            // @@protoc_insertion_point(builder_scope:org.urbcomp.start.db.spark.data.SchemaRequest)
        }

        static {
            defaultInstance = new SchemaRequest(true);
            defaultInstance.initFields();
        }

        // @@protoc_insertion_point(class_scope:org.urbcomp.start.db.spark.data.SchemaRequest)
    }

    public interface SchemaResponseOrBuilder extends com.google.protobuf.MessageOrBuilder {

        // optional string res = 1;
        /**
         * <code>optional string res = 1;</code>
         */
        boolean hasRes();

        /**
         * <code>optional string res = 1;</code>
         */
        java.lang.String getRes();

        /**
         * <code>optional string res = 1;</code>
         */
        com.google.protobuf.ByteString getResBytes();
    }

    /**
     * Protobuf type {@code org.urbcomp.start.db.spark.data.SchemaResponse}
     */
    public static final class SchemaResponse extends com.google.protobuf.GeneratedMessage
        implements
            SchemaResponseOrBuilder {
        // Use SchemaResponse.newBuilder() to construct.
        private SchemaResponse(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private SchemaResponse(boolean noInit) {
            this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
        }

        private static final SchemaResponse defaultInstance;

        public static SchemaResponse getDefaultInstance() {
            return defaultInstance;
        }

        public SchemaResponse getDefaultInstanceForType() {
            return defaultInstance;
        }

        private final com.google.protobuf.UnknownFieldSet unknownFields;

        @java.lang.Override
        public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private SchemaResponse(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            initFields();
            int mutable_bitField0_ = 0;
            com.google.protobuf.UnknownFieldSet.Builder unknownFields =
                com.google.protobuf.UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            done = true;
                            break;
                        default: {
                            if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                done = true;
                            }
                            break;
                        }
                        case 10: {
                            bitField0_ |= 0x00000001;
                            res_ = input.readBytes();
                            break;
                        }
                    }
                }
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(this);
            } catch (java.io.IOException e) {
                throw new com.google.protobuf.InvalidProtocolBufferException(e.getMessage())
                    .setUnfinishedMessage(this);
            } finally {
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_SchemaResponse_descriptor;
        }

        protected
            com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
            return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_SchemaResponse_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                    org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse.class,
                    org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse.Builder.class
                );
        }

        public static com.google.protobuf.Parser<SchemaResponse> PARSER =
            new com.google.protobuf.AbstractParser<SchemaResponse>() {
                public SchemaResponse parsePartialFrom(
                    com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry
                ) throws com.google.protobuf.InvalidProtocolBufferException {
                    return new SchemaResponse(input, extensionRegistry);
                }
            };

        @java.lang.Override
        public com.google.protobuf.Parser<SchemaResponse> getParserForType() {
            return PARSER;
        }

        private int bitField0_;
        // optional string res = 1;
        public static final int RES_FIELD_NUMBER = 1;
        private java.lang.Object res_;

        /**
         * <code>optional string res = 1;</code>
         */
        public boolean hasRes() {
            return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        /**
         * <code>optional string res = 1;</code>
         */
        public java.lang.String getRes() {
            java.lang.Object ref = res_;
            if (ref instanceof java.lang.String) {
                return (java.lang.String) ref;
            } else {
                com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    res_ = s;
                }
                return s;
            }
        }

        /**
         * <code>optional string res = 1;</code>
         */
        public com.google.protobuf.ByteString getResBytes() {
            java.lang.Object ref = res_;
            if (ref instanceof java.lang.String) {
                com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref
                );
                res_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        private void initFields() {
            res_ = "";
        }

        private byte memoizedIsInitialized = -1;

        public final boolean isInitialized() {
            byte isInitialized = memoizedIsInitialized;
            if (isInitialized != -1) return isInitialized == 1;

            memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
            getSerializedSize();
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                output.writeBytes(1, getResBytes());
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;

            size = 0;
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                size += com.google.protobuf.CodedOutputStream.computeBytesSize(1, getResBytes());
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        private static final long serialVersionUID = 0L;

        @java.lang.Override
        protected java.lang.Object writeReplace() throws java.io.ObjectStreamException {
            return super.writeReplace();
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse parseFrom(
            com.google.protobuf.ByteString data
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse parseFrom(
            byte[] data
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse parseFrom(
            java.io.InputStream input
        ) throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse parseDelimitedFrom(
            java.io.InputStream input
        ) throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse parseFrom(
            com.google.protobuf.CodedInputStream input
        ) throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(
            org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse prototype
        ) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessage.BuilderParent parent
        ) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /**
         * Protobuf type {@code org.urbcomp.start.db.spark.data.SchemaResponse}
         */
        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<
            Builder> implements org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponseOrBuilder {
            public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_SchemaResponse_descriptor;
            }

            protected
                com.google.protobuf.GeneratedMessage.FieldAccessorTable
                internalGetFieldAccessorTable() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_SchemaResponse_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                        org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse.class,
                        org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse.Builder.class
                    );
            }

            // Construct using
            // org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse.newBuilder()
            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                }
            }

            private static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                res_ = "";
                bitField0_ = (bitField0_ & ~0x00000001);
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_SchemaResponse_descriptor;
            }

            public
                org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse
                getDefaultInstanceForType() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse
                    .getDefaultInstance();
            }

            public org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse build() {
                org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            public org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse buildPartial() {
                org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse result =
                    new org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse(this);
                int from_bitField0_ = bitField0_;
                int to_bitField0_ = 0;
                if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
                    to_bitField0_ |= 0x00000001;
                }
                result.res_ = res_;
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse) {
                    return mergeFrom(
                        (org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse) other
                    );
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(
                org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse other
            ) {
                if (other == org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse
                    .getDefaultInstance()) return this;
                if (other.hasRes()) {
                    bitField0_ |= 0x00000001;
                    res_ = other.res_;
                    onChanged();
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public final boolean isInitialized() {
                return true;
            }

            public Builder mergeFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry
            ) throws java.io.IOException {
                org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse parsedMessage = null;
                try {
                    parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
                } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                    parsedMessage = (org.urbcomp.start.db.spark.data.GrpcRemote.SchemaResponse) e
                        .getUnfinishedMessage();
                    throw e;
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            private int bitField0_;

            // optional string res = 1;
            private java.lang.Object res_ = "";

            /**
             * <code>optional string res = 1;</code>
             */
            public boolean hasRes() {
                return ((bitField0_ & 0x00000001) == 0x00000001);
            }

            /**
             * <code>optional string res = 1;</code>
             */
            public java.lang.String getRes() {
                java.lang.Object ref = res_;
                if (!(ref instanceof java.lang.String)) {
                    java.lang.String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
                    res_ = s;
                    return s;
                } else {
                    return (java.lang.String) ref;
                }
            }

            /**
             * <code>optional string res = 1;</code>
             */
            public com.google.protobuf.ByteString getResBytes() {
                java.lang.Object ref = res_;
                if (ref instanceof String) {
                    com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8(
                        (java.lang.String) ref
                    );
                    res_ = b;
                    return b;
                } else {
                    return (com.google.protobuf.ByteString) ref;
                }
            }

            /**
             * <code>optional string res = 1;</code>
             */
            public Builder setRes(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                res_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>optional string res = 1;</code>
             */
            public Builder clearRes() {
                bitField0_ = (bitField0_ & ~0x00000001);
                res_ = getDefaultInstance().getRes();
                onChanged();
                return this;
            }

            /**
             * <code>optional string res = 1;</code>
             */
            public Builder setResBytes(com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                res_ = value;
                onChanged();
                return this;
            }

            // @@protoc_insertion_point(builder_scope:org.urbcomp.start.db.spark.data.SchemaResponse)
        }

        static {
            defaultInstance = new SchemaResponse(true);
            defaultInstance.initFields();
        }

        // @@protoc_insertion_point(class_scope:org.urbcomp.start.db.spark.data.SchemaResponse)
    }

    public interface RowRequestOrBuilder extends com.google.protobuf.MessageOrBuilder {

        // required string sqlId = 1;
        /**
         * <code>required string sqlId = 1;</code>
         */
        boolean hasSqlId();

        /**
         * <code>required string sqlId = 1;</code>
         */
        java.lang.String getSqlId();

        /**
         * <code>required string sqlId = 1;</code>
         */
        com.google.protobuf.ByteString getSqlIdBytes();

        // required bytes data = 2;
        /**
         * <code>required bytes data = 2;</code>
         */
        boolean hasData();

        /**
         * <code>required bytes data = 2;</code>
         */
        com.google.protobuf.ByteString getData();
    }

    /**
     * Protobuf type {@code org.urbcomp.start.db.spark.data.RowRequest}
     */
    public static final class RowRequest extends com.google.protobuf.GeneratedMessage
        implements
            RowRequestOrBuilder {
        // Use RowRequest.newBuilder() to construct.
        private RowRequest(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private RowRequest(boolean noInit) {
            this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
        }

        private static final RowRequest defaultInstance;

        public static RowRequest getDefaultInstance() {
            return defaultInstance;
        }

        public RowRequest getDefaultInstanceForType() {
            return defaultInstance;
        }

        private final com.google.protobuf.UnknownFieldSet unknownFields;

        @java.lang.Override
        public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private RowRequest(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            initFields();
            int mutable_bitField0_ = 0;
            com.google.protobuf.UnknownFieldSet.Builder unknownFields =
                com.google.protobuf.UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            done = true;
                            break;
                        default: {
                            if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                done = true;
                            }
                            break;
                        }
                        case 10: {
                            bitField0_ |= 0x00000001;
                            sqlId_ = input.readBytes();
                            break;
                        }
                        case 18: {
                            bitField0_ |= 0x00000002;
                            data_ = input.readBytes();
                            break;
                        }
                    }
                }
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(this);
            } catch (java.io.IOException e) {
                throw new com.google.protobuf.InvalidProtocolBufferException(e.getMessage())
                    .setUnfinishedMessage(this);
            } finally {
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_RowRequest_descriptor;
        }

        protected
            com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
            return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_RowRequest_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                    org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest.class,
                    org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest.Builder.class
                );
        }

        public static com.google.protobuf.Parser<RowRequest> PARSER =
            new com.google.protobuf.AbstractParser<RowRequest>() {
                public RowRequest parsePartialFrom(
                    com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry
                ) throws com.google.protobuf.InvalidProtocolBufferException {
                    return new RowRequest(input, extensionRegistry);
                }
            };

        @java.lang.Override
        public com.google.protobuf.Parser<RowRequest> getParserForType() {
            return PARSER;
        }

        private int bitField0_;
        // required string sqlId = 1;
        public static final int SQLID_FIELD_NUMBER = 1;
        private java.lang.Object sqlId_;

        /**
         * <code>required string sqlId = 1;</code>
         */
        public boolean hasSqlId() {
            return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        /**
         * <code>required string sqlId = 1;</code>
         */
        public java.lang.String getSqlId() {
            java.lang.Object ref = sqlId_;
            if (ref instanceof java.lang.String) {
                return (java.lang.String) ref;
            } else {
                com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    sqlId_ = s;
                }
                return s;
            }
        }

        /**
         * <code>required string sqlId = 1;</code>
         */
        public com.google.protobuf.ByteString getSqlIdBytes() {
            java.lang.Object ref = sqlId_;
            if (ref instanceof java.lang.String) {
                com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref
                );
                sqlId_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        // required bytes data = 2;
        public static final int DATA_FIELD_NUMBER = 2;
        private com.google.protobuf.ByteString data_;

        /**
         * <code>required bytes data = 2;</code>
         */
        public boolean hasData() {
            return ((bitField0_ & 0x00000002) == 0x00000002);
        }

        /**
         * <code>required bytes data = 2;</code>
         */
        public com.google.protobuf.ByteString getData() {
            return data_;
        }

        private void initFields() {
            sqlId_ = "";
            data_ = com.google.protobuf.ByteString.EMPTY;
        }

        private byte memoizedIsInitialized = -1;

        public final boolean isInitialized() {
            byte isInitialized = memoizedIsInitialized;
            if (isInitialized != -1) return isInitialized == 1;

            if (!hasSqlId()) {
                memoizedIsInitialized = 0;
                return false;
            }
            if (!hasData()) {
                memoizedIsInitialized = 0;
                return false;
            }
            memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
            getSerializedSize();
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                output.writeBytes(1, getSqlIdBytes());
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                output.writeBytes(2, data_);
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;

            size = 0;
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                size += com.google.protobuf.CodedOutputStream.computeBytesSize(1, getSqlIdBytes());
            }
            if (((bitField0_ & 0x00000002) == 0x00000002)) {
                size += com.google.protobuf.CodedOutputStream.computeBytesSize(2, data_);
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        private static final long serialVersionUID = 0L;

        @java.lang.Override
        protected java.lang.Object writeReplace() throws java.io.ObjectStreamException {
            return super.writeReplace();
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest parseFrom(
            com.google.protobuf.ByteString data
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest parseFrom(
            java.io.InputStream input
        ) throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest parseDelimitedFrom(
            java.io.InputStream input
        ) throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest parseFrom(
            com.google.protobuf.CodedInputStream input
        ) throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(
            org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest prototype
        ) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessage.BuilderParent parent
        ) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /**
         * Protobuf type {@code org.urbcomp.start.db.spark.data.RowRequest}
         */
        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<
            Builder> implements org.urbcomp.start.db.spark.data.GrpcRemote.RowRequestOrBuilder {
            public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_RowRequest_descriptor;
            }

            protected
                com.google.protobuf.GeneratedMessage.FieldAccessorTable
                internalGetFieldAccessorTable() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_RowRequest_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                        org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest.class,
                        org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest.Builder.class
                    );
            }

            // Construct using org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest.newBuilder()
            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                }
            }

            private static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                sqlId_ = "";
                bitField0_ = (bitField0_ & ~0x00000001);
                data_ = com.google.protobuf.ByteString.EMPTY;
                bitField0_ = (bitField0_ & ~0x00000002);
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_RowRequest_descriptor;
            }

            public
                org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest
                getDefaultInstanceForType() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest.getDefaultInstance();
            }

            public org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest build() {
                org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            public org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest buildPartial() {
                org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest result =
                    new org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest(this);
                int from_bitField0_ = bitField0_;
                int to_bitField0_ = 0;
                if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
                    to_bitField0_ |= 0x00000001;
                }
                result.sqlId_ = sqlId_;
                if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
                    to_bitField0_ |= 0x00000002;
                }
                result.data_ = data_;
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest) {
                    return mergeFrom((org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest other) {
                if (other == org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest
                    .getDefaultInstance()) return this;
                if (other.hasSqlId()) {
                    bitField0_ |= 0x00000001;
                    sqlId_ = other.sqlId_;
                    onChanged();
                }
                if (other.hasData()) {
                    setData(other.getData());
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public final boolean isInitialized() {
                if (!hasSqlId()) {

                    return false;
                }
                if (!hasData()) {

                    return false;
                }
                return true;
            }

            public Builder mergeFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry
            ) throws java.io.IOException {
                org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest parsedMessage = null;
                try {
                    parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
                } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                    parsedMessage = (org.urbcomp.start.db.spark.data.GrpcRemote.RowRequest) e
                        .getUnfinishedMessage();
                    throw e;
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            private int bitField0_;

            // required string sqlId = 1;
            private java.lang.Object sqlId_ = "";

            /**
             * <code>required string sqlId = 1;</code>
             */
            public boolean hasSqlId() {
                return ((bitField0_ & 0x00000001) == 0x00000001);
            }

            /**
             * <code>required string sqlId = 1;</code>
             */
            public java.lang.String getSqlId() {
                java.lang.Object ref = sqlId_;
                if (!(ref instanceof java.lang.String)) {
                    java.lang.String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
                    sqlId_ = s;
                    return s;
                } else {
                    return (java.lang.String) ref;
                }
            }

            /**
             * <code>required string sqlId = 1;</code>
             */
            public com.google.protobuf.ByteString getSqlIdBytes() {
                java.lang.Object ref = sqlId_;
                if (ref instanceof String) {
                    com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8(
                        (java.lang.String) ref
                    );
                    sqlId_ = b;
                    return b;
                } else {
                    return (com.google.protobuf.ByteString) ref;
                }
            }

            /**
             * <code>required string sqlId = 1;</code>
             */
            public Builder setSqlId(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                sqlId_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>required string sqlId = 1;</code>
             */
            public Builder clearSqlId() {
                bitField0_ = (bitField0_ & ~0x00000001);
                sqlId_ = getDefaultInstance().getSqlId();
                onChanged();
                return this;
            }

            /**
             * <code>required string sqlId = 1;</code>
             */
            public Builder setSqlIdBytes(com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                sqlId_ = value;
                onChanged();
                return this;
            }

            // required bytes data = 2;
            private com.google.protobuf.ByteString data_ = com.google.protobuf.ByteString.EMPTY;

            /**
             * <code>required bytes data = 2;</code>
             */
            public boolean hasData() {
                return ((bitField0_ & 0x00000002) == 0x00000002);
            }

            /**
             * <code>required bytes data = 2;</code>
             */
            public com.google.protobuf.ByteString getData() {
                return data_;
            }

            /**
             * <code>required bytes data = 2;</code>
             */
            public Builder setData(com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000002;
                data_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>required bytes data = 2;</code>
             */
            public Builder clearData() {
                bitField0_ = (bitField0_ & ~0x00000002);
                data_ = getDefaultInstance().getData();
                onChanged();
                return this;
            }

            // @@protoc_insertion_point(builder_scope:org.urbcomp.start.db.spark.data.RowRequest)
        }

        static {
            defaultInstance = new RowRequest(true);
            defaultInstance.initFields();
        }

        // @@protoc_insertion_point(class_scope:org.urbcomp.start.db.spark.data.RowRequest)
    }

    public interface RowResponseOrBuilder extends com.google.protobuf.MessageOrBuilder {

        // optional string res = 1;
        /**
         * <code>optional string res = 1;</code>
         */
        boolean hasRes();

        /**
         * <code>optional string res = 1;</code>
         */
        java.lang.String getRes();

        /**
         * <code>optional string res = 1;</code>
         */
        com.google.protobuf.ByteString getResBytes();
    }

    /**
     * Protobuf type {@code org.urbcomp.start.db.spark.data.RowResponse}
     */
    public static final class RowResponse extends com.google.protobuf.GeneratedMessage
        implements
            RowResponseOrBuilder {
        // Use RowResponse.newBuilder() to construct.
        private RowResponse(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.unknownFields = builder.getUnknownFields();
        }

        private RowResponse(boolean noInit) {
            this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance();
        }

        private static final RowResponse defaultInstance;

        public static RowResponse getDefaultInstance() {
            return defaultInstance;
        }

        public RowResponse getDefaultInstanceForType() {
            return defaultInstance;
        }

        private final com.google.protobuf.UnknownFieldSet unknownFields;

        @java.lang.Override
        public final com.google.protobuf.UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private RowResponse(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            initFields();
            int mutable_bitField0_ = 0;
            com.google.protobuf.UnknownFieldSet.Builder unknownFields =
                com.google.protobuf.UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    int tag = input.readTag();
                    switch (tag) {
                        case 0:
                            done = true;
                            break;
                        default: {
                            if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                done = true;
                            }
                            break;
                        }
                        case 10: {
                            bitField0_ |= 0x00000001;
                            res_ = input.readBytes();
                            break;
                        }
                    }
                }
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(this);
            } catch (java.io.IOException e) {
                throw new com.google.protobuf.InvalidProtocolBufferException(e.getMessage())
                    .setUnfinishedMessage(this);
            } finally {
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_RowResponse_descriptor;
        }

        protected
            com.google.protobuf.GeneratedMessage.FieldAccessorTable
            internalGetFieldAccessorTable() {
            return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_RowResponse_fieldAccessorTable
                .ensureFieldAccessorsInitialized(
                    org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse.class,
                    org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse.Builder.class
                );
        }

        public static com.google.protobuf.Parser<RowResponse> PARSER =
            new com.google.protobuf.AbstractParser<RowResponse>() {
                public RowResponse parsePartialFrom(
                    com.google.protobuf.CodedInputStream input,
                    com.google.protobuf.ExtensionRegistryLite extensionRegistry
                ) throws com.google.protobuf.InvalidProtocolBufferException {
                    return new RowResponse(input, extensionRegistry);
                }
            };

        @java.lang.Override
        public com.google.protobuf.Parser<RowResponse> getParserForType() {
            return PARSER;
        }

        private int bitField0_;
        // optional string res = 1;
        public static final int RES_FIELD_NUMBER = 1;
        private java.lang.Object res_;

        /**
         * <code>optional string res = 1;</code>
         */
        public boolean hasRes() {
            return ((bitField0_ & 0x00000001) == 0x00000001);
        }

        /**
         * <code>optional string res = 1;</code>
         */
        public java.lang.String getRes() {
            java.lang.Object ref = res_;
            if (ref instanceof java.lang.String) {
                return (java.lang.String) ref;
            } else {
                com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
                java.lang.String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    res_ = s;
                }
                return s;
            }
        }

        /**
         * <code>optional string res = 1;</code>
         */
        public com.google.protobuf.ByteString getResBytes() {
            java.lang.Object ref = res_;
            if (ref instanceof java.lang.String) {
                com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8(
                    (java.lang.String) ref
                );
                res_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }

        private void initFields() {
            res_ = "";
        }

        private byte memoizedIsInitialized = -1;

        public final boolean isInitialized() {
            byte isInitialized = memoizedIsInitialized;
            if (isInitialized != -1) return isInitialized == 1;

            memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output)
            throws java.io.IOException {
            getSerializedSize();
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                output.writeBytes(1, getResBytes());
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;

            size = 0;
            if (((bitField0_ & 0x00000001) == 0x00000001)) {
                size += com.google.protobuf.CodedOutputStream.computeBytesSize(1, getResBytes());
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        private static final long serialVersionUID = 0L;

        @java.lang.Override
        protected java.lang.Object writeReplace() throws java.io.ObjectStreamException {
            return super.writeReplace();
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse parseFrom(
            com.google.protobuf.ByteString data
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse parseFrom(
            com.google.protobuf.ByteString data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse parseFrom(byte[] data)
            throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse parseFrom(
            byte[] data,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws com.google.protobuf.InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse parseFrom(
            java.io.InputStream input
        ) throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse parseFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse parseDelimitedFrom(
            java.io.InputStream input
        ) throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse parseDelimitedFrom(
            java.io.InputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws java.io.IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse parseFrom(
            com.google.protobuf.CodedInputStream input
        ) throws java.io.IOException {
            return PARSER.parseFrom(input);
        }

        public static org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse parseFrom(
            com.google.protobuf.CodedInputStream input,
            com.google.protobuf.ExtensionRegistryLite extensionRegistry
        ) throws java.io.IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(
            org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse prototype
        ) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        @java.lang.Override
        protected Builder newBuilderForType(
            com.google.protobuf.GeneratedMessage.BuilderParent parent
        ) {
            Builder builder = new Builder(parent);
            return builder;
        }

        /**
         * Protobuf type {@code org.urbcomp.start.db.spark.data.RowResponse}
         */
        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<
            Builder> implements org.urbcomp.start.db.spark.data.GrpcRemote.RowResponseOrBuilder {
            public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_RowResponse_descriptor;
            }

            protected
                com.google.protobuf.GeneratedMessage.FieldAccessorTable
                internalGetFieldAccessorTable() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_RowResponse_fieldAccessorTable
                    .ensureFieldAccessorsInitialized(
                        org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse.class,
                        org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse.Builder.class
                    );
            }

            // Construct using org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse.newBuilder()
            private Builder() {
                maybeForceBuilderInitialization();
            }

            private Builder(com.google.protobuf.GeneratedMessage.BuilderParent parent) {
                super(parent);
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
                }
            }

            private static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                res_ = "";
                bitField0_ = (bitField0_ & ~0x00000001);
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.internal_static_org_urbcomp_start_db_spark_data_RowResponse_descriptor;
            }

            public
                org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse
                getDefaultInstanceForType() {
                return org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse.getDefaultInstance();
            }

            public org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse build() {
                org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return result;
            }

            public org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse buildPartial() {
                org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse result =
                    new org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse(this);
                int from_bitField0_ = bitField0_;
                int to_bitField0_ = 0;
                if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
                    to_bitField0_ |= 0x00000001;
                }
                result.res_ = res_;
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse) {
                    return mergeFrom(
                        (org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse) other
                    );
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse other) {
                if (other == org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse
                    .getDefaultInstance()) return this;
                if (other.hasRes()) {
                    bitField0_ |= 0x00000001;
                    res_ = other.res_;
                    onChanged();
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public final boolean isInitialized() {
                return true;
            }

            public Builder mergeFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry
            ) throws java.io.IOException {
                org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse parsedMessage = null;
                try {
                    parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
                } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                    parsedMessage = (org.urbcomp.start.db.spark.data.GrpcRemote.RowResponse) e
                        .getUnfinishedMessage();
                    throw e;
                } finally {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                }
                return this;
            }

            private int bitField0_;

            // optional string res = 1;
            private java.lang.Object res_ = "";

            /**
             * <code>optional string res = 1;</code>
             */
            public boolean hasRes() {
                return ((bitField0_ & 0x00000001) == 0x00000001);
            }

            /**
             * <code>optional string res = 1;</code>
             */
            public java.lang.String getRes() {
                java.lang.Object ref = res_;
                if (!(ref instanceof java.lang.String)) {
                    java.lang.String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
                    res_ = s;
                    return s;
                } else {
                    return (java.lang.String) ref;
                }
            }

            /**
             * <code>optional string res = 1;</code>
             */
            public com.google.protobuf.ByteString getResBytes() {
                java.lang.Object ref = res_;
                if (ref instanceof String) {
                    com.google.protobuf.ByteString b = com.google.protobuf.ByteString.copyFromUtf8(
                        (java.lang.String) ref
                    );
                    res_ = b;
                    return b;
                } else {
                    return (com.google.protobuf.ByteString) ref;
                }
            }

            /**
             * <code>optional string res = 1;</code>
             */
            public Builder setRes(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                res_ = value;
                onChanged();
                return this;
            }

            /**
             * <code>optional string res = 1;</code>
             */
            public Builder clearRes() {
                bitField0_ = (bitField0_ & ~0x00000001);
                res_ = getDefaultInstance().getRes();
                onChanged();
                return this;
            }

            /**
             * <code>optional string res = 1;</code>
             */
            public Builder setResBytes(com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                bitField0_ |= 0x00000001;
                res_ = value;
                onChanged();
                return this;
            }

            // @@protoc_insertion_point(builder_scope:org.urbcomp.start.db.spark.data.RowResponse)
        }

        static {
            defaultInstance = new RowResponse(true);
            defaultInstance.initFields();
        }

        // @@protoc_insertion_point(class_scope:org.urbcomp.start.db.spark.data.RowResponse)
    }

    private static com.google.protobuf.Descriptors.Descriptor internal_static_org_urbcomp_start_db_spark_data_SchemaRequest_descriptor;
    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_org_urbcomp_start_db_spark_data_SchemaRequest_fieldAccessorTable;
    private static com.google.protobuf.Descriptors.Descriptor internal_static_org_urbcomp_start_db_spark_data_SchemaResponse_descriptor;
    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_org_urbcomp_start_db_spark_data_SchemaResponse_fieldAccessorTable;
    private static com.google.protobuf.Descriptors.Descriptor internal_static_org_urbcomp_start_db_spark_data_RowRequest_descriptor;
    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_org_urbcomp_start_db_spark_data_RowRequest_fieldAccessorTable;
    private static com.google.protobuf.Descriptors.Descriptor internal_static_org_urbcomp_start_db_spark_data_RowResponse_descriptor;
    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_org_urbcomp_start_db_spark_data_RowResponse_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor descriptor;
    static {
        java.lang.String[] descriptorData = {
            "\n*src/main/resources/proto/grpc_remote.p"
                + "roto\022\037org.urbcomp.start.db.spark.data\"2\n"
                + "\rSchemaRequest\022\r\n\005sqlId\030\001 \002(\t\022\022\n\nschemaJ"
                + "son\030\002 \002(\t\"\035\n\016SchemaResponse\022\013\n\003res\030\001 \001(\t"
                + "\")\n\nRowRequest\022\r\n\005sqlId\030\001 \002(\t\022\014\n\004data\030\002 "
                + "\002(\014\"\032\n\013RowResponse\022\013\n\003res\030\001 \001(\t" };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
            new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
                public com.google.protobuf.ExtensionRegistry assignDescriptors(
                    com.google.protobuf.Descriptors.FileDescriptor root
                ) {
                    descriptor = root;
                    internal_static_org_urbcomp_start_db_spark_data_SchemaRequest_descriptor =
                        getDescriptor().getMessageTypes().get(0);
                    internal_static_org_urbcomp_start_db_spark_data_SchemaRequest_fieldAccessorTable =
                        new com.google.protobuf.GeneratedMessage.FieldAccessorTable(
                            internal_static_org_urbcomp_start_db_spark_data_SchemaRequest_descriptor,
                            new java.lang.String[] { "SqlId", "SchemaJson", }
                        );
                    internal_static_org_urbcomp_start_db_spark_data_SchemaResponse_descriptor =
                        getDescriptor().getMessageTypes().get(1);
                    internal_static_org_urbcomp_start_db_spark_data_SchemaResponse_fieldAccessorTable =
                        new com.google.protobuf.GeneratedMessage.FieldAccessorTable(
                            internal_static_org_urbcomp_start_db_spark_data_SchemaResponse_descriptor,
                            new java.lang.String[] { "Res", }
                        );
                    internal_static_org_urbcomp_start_db_spark_data_RowRequest_descriptor =
                        getDescriptor().getMessageTypes().get(2);
                    internal_static_org_urbcomp_start_db_spark_data_RowRequest_fieldAccessorTable =
                        new com.google.protobuf.GeneratedMessage.FieldAccessorTable(
                            internal_static_org_urbcomp_start_db_spark_data_RowRequest_descriptor,
                            new java.lang.String[] { "SqlId", "Data", }
                        );
                    internal_static_org_urbcomp_start_db_spark_data_RowResponse_descriptor =
                        getDescriptor().getMessageTypes().get(3);
                    internal_static_org_urbcomp_start_db_spark_data_RowResponse_fieldAccessorTable =
                        new com.google.protobuf.GeneratedMessage.FieldAccessorTable(
                            internal_static_org_urbcomp_start_db_spark_data_RowResponse_descriptor,
                            new java.lang.String[] { "Res", }
                        );
                    return null;
                }
            };
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
            descriptorData,
            new com.google.protobuf.Descriptors.FileDescriptor[] {},
            assigner
        );
    }

    // @@protoc_insertion_point(outer_class_scope)
}
