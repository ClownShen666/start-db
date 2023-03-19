
# 编译 protobuf & grpc

[下载 protobuf](https://github.com/protocolbuffers/protobuf/releases/tag/v2.5.0) 

为什么用protobuf2.5？ 因为spark现在还是用的2.5.0，其他版本会有依赖冲突问题。

grpc用1.19.0，和protobuf 3.6.1对应。

[下载protoc-gen-grpc-java](https://repo1.maven.org/maven2/io/grpc/protoc-gen-grpc-java/1.36.3/protoc-gen-grpc-java-1.19.0-windows-x86_64.exe)

编译
```shell
cd cupid-db-core

# 编译protobuf时低版本2.5.0不支持 service写法，可以先删掉service定义
D:\software\protoc-2.5.0-win32\protoc.exe --java_out=src/main/java src/main/resources/proto/grpc_remote.proto

# D:\software\protoc-3.6.1-win32\bin\protoc.exe --java_out=src/main/java src/main/resources/proto/grpc_remote.proto

D:\software\protoc-3.6.1-win32\bin\protoc.exe --plugin=protoc-gen-grpc-java="D:\software\protoc-gen-grpc-java-1.19.0-windows-x86_64.exe" --grpc-java_out=src/main/java src/main/resources/proto/grpc_remote.proto
```

