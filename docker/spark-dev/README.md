# 搭建 spark cluster 开发环境

需准备以下docker镜像

- spark-cluster (spark-3.2.2-hadoop3.2)
- livy (livy 0.8.0-spark3-scala2.12)
- hadoop 
- hbase
- mysql

其中 spark-cluster和livy有定制改动，需要本地准备


## 准备资源
进入`spark-dev`目录,提前准备好spark和livy的压缩包

**spark**

```
export SPARK_VERSION=3.0.2
export HADOOP_VERSION=3.2
wget --no-verbose -O apache-spark.tgz "https://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz"
cp spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz spark/
```

**livy**

```
git clone https://github.com/apache/incubator-livy.git
cd incubator-livy
mvn clean package -DskipTests -Pscala-2.12 -Pspark3
cp ./incubator-livy/assembly/target/apache-livy-0.8.0-incubating-SNAPSHOT_2.12-bin.zip ./livy/
cd livy
unzip apache-livy-0.8.0-incubating-SNAPSHOT_2.12-bin.zip
tar -czvf apache-livy-0.8.0-incubating-SNAPSHOT_2.12-bin.tgz ./apache-livy-0.8.0-incubating-SNAPSHOT_2.12-bin
rum -rf apache-livy-0.8.0-incubating-SNAPSHOT_2.12-bin.zip ./apache-livy-0.8.0-incubating-SNAPSHOT_2.12-bin
cd -
cp spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop${HADOOP_VERSION}.tgz ./livy/
```

## 准备镜像

spark镜像和livy镜像需要自己打包，其他镜像可以直接从docker center拉下来。

进入`spark-dev`目录。


**spark**

参考：https://dev.to/mvillarrealb/creating-a-spark-standalone-cluster-with-docker-and-docker-compose-2021-update-6l4

```
cd spark
docker build -t cluster-apache-spark:3.0.2 .
cd -
```


**livy**

```
cd livy
docker build -t livy:0.8.0 .
cd -
```


## 启动服务 

```
docker compose up --force-recreate
```

对应的服务地址

spark master: http://localhost:8080/ 
spark worker: http://localhost:8081/，http://localhost:8082/
livy: http://localhost:8998/
hadoop: http://localhost:9870/

## 准备cupid-spark jar

进入cupid-db目录
更新cupid-spark jar包
```
mvn package -pl cupid-db-spark -am -Dmaven.test.skip=true
cp cupid-db-spark/target/cupid-db-spark-1.0.0-SNAPSHOT.jar docker/spark-dev/apps/cupid-db-spark-shaded.jar
```

至此即可以在IDE中启动spark server后测试`spark-cluster`模式


## 测试

### 进入容器测试

```shell
docker exec -it spark-dev-spark-master-1 bash

./bin/spark-submit \
  --class org.apache.spark.examples.SparkPi \
  --master spark://spark-master:7077 \
  --deploy-mode client \
  --executor-memory 1G \
  --total-executor-cores 1 \
  /opt/spark/examples/jars/spark-examples_2.12-3.0.2.jar
```

### 本机测试

本机和docker网络不通，无法从本地submit提交。

bin/spark-submit --class org.apache.spark.examples.SparkPi --master spark://localhost:7077 --deploy-mode client --executor-memory 1G --total-executor-cores 1 D:\software\spark-3.0.2-bin-hadoop3.2\examples\jars\spark-examples_2.12-3.0.2.jar

### livy测试

访问 http://localhost:8998/进入livy界面

```shell
# CREATING A LIVY SESSION
curl -X POST -d '{"kind": "spark","driverMemory":"512M","executorMemory":"512M", "jars":["/opt/spark-apps/cupid-db-spark-1.0.0-SNAPSHOT.jar"]}' -H "Content-Type: application/json" http://localhost:8998/sessions/

# SUBMITTING A SIMPLE LOGIC TO TEST SPARK SHELL
curl -X POST -d '{"code": "1 + 1"}' -H "Content-Type: application/json" http://localhost:8998/sessions/0/statements

# SUBMITTING A SPARK CODE
curl -X POST -d '{"code": "val data = Array(1,2,3); sc.parallelize(data).count"}' -H "Content-Type: application/json" http://localhost:8998/sessions/0/statements

# Delete a session
curl -X DELETE http://localhost:8998/sessions/0
```

