
# 构建geomesa-hbase

构建的原始方法见: https://hub.docker.com/layers/spatiotemporallab/geomesa-hbase/base/images/sha256-1988186b5d7ba078462531397b69884e13bd6d12c8b368ea5526f2defa578da0?context=explore

```shell
docker tag spatiotemporallab/geomesa-hbase:latest spatiotemporallab/geomesa-hbase:base
 
docker push spatiotemporallab/geomesa-hbase:base
```

# 更新了索引如何重新构建

1.打包索引模块

```shell
mvn package -pl start-db-store -am -Dmaven.test.skip=true
```

2.将打好的包start-db-store-1.0.0-SNAPSHOT.jar复制到当前目录

3.构建镜像

```shell
docker build -t spatiotemporallab/geomesa-hbase-start:1.0.0 .
```

4.push镜像

```shell
docker push spatiotemporallab/geomesa-hbase-start:1.0.0
```
