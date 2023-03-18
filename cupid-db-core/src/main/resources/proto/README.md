
# 编译 protobuf & grpc

[下载 protobuf](https://github.com/protocolbuffers/protobuf/releases/tag/v2.5.0) 

为什么用protobuf2.5？ 因为spark现在还是用的2.5.0，其他版本会有依赖冲突问题。

[下载protoc-gen-grpc-java](https://repo1.maven.org/maven2/io/grpc/protoc-gen-grpc-java/1.36.3/protoc-gen-grpc-java-1.36.3-windows-x86_64.exe)

编译
```shell
cd cupid-db-core

D:\software\protoc-2.5.0-win32\protoc.exe --java_out=src/main/java src/main/resources/proto/grpc_remote.proto
D:\software\protoc-2.5.0-win32\bin\protoc.exe --plugin=protoc-gen-grpc-java="D:\software\protoc-gen-grpc-java-1.36.3-windows-x86_64.exe" --grpc-java_out=src/main/java src/main/resources/proto/grpc_remote.proto
```

