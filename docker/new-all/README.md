# 流批开发环境
1. 进入`new-all`目录,启动 mysql-local 和 geomesa-hbase-local 容器
`
docker-compose up -d mysql-local geomesa-hbase-local
`
2. 打包cupid-db(跳过测试)，cupid-db.jar和cupid-db-flink-1.0.0-SNAPSHOT-jar-with-dependencies.jar拷贝到docker/new-all/cupid-db目录下
3. 启动所有容器
`docker-compose up -d`
