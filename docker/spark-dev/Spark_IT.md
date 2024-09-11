# Spark集成测试步骤

1. 打包db-spark包

```shell
mvn clean package -U -pl start-db-spark -am "-Dmaven.test.skip=true" -DskipTests
```

2. 将包复制到 docker/spark-dev/apps, 这目录会被容器挂载

3. 启动容器()：

```shell
cd docker/spark-dev
docker-compose up -d 
```

启动后可访问 Livy界面：http://localhost:8998/ui

4. 启动DB Server

5. 运行测试：

org.urbcomp.start.db.jdbc.DriverTest.testUseSparkRemote

通过调节不同参数，测试spark不同功能。

