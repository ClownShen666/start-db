# 流批环境
1. 进入`new-all`目录,启动 mysql-local 和 geomesa-hbase-local 容器
`
docker-compose up -d mysql-local geomesa-hbase-local
`
2. 打包cupid-db(跳过测试)，cupid-db.jar和cupid-db-flink-1.0.0-SNAPSHOT-jar-with-dependencies.jar拷贝到`new-all/cupid-db`目录下
3. cluster-apache-spark和livy.img镜像文件拷贝到`new-all`目录下
`docker load -i cluster-apache-spark
 docker load -i livy.img
 docker tag <cluster-apache-spark_imgID> cluster-apache-spark:latest
 docker tag <livy.img_imgID> livy:latest
   `
4. `docker-compose --profile cluster up -d`


