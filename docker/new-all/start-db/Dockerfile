FROM amazoncorretto:8-al2-generic-jdk

WORKDIR /start-server

COPY start-db.jar /start-server/start-db.jar
COPY start-db-flink-1.0.0-SNAPSHOT-jar-with-dependencies.jar /start-server/start-db-flink-1.0.0-SNAPSHOT-jar-with-dependencies.jar

CMD ["java", "-jar", "/start-server/start-db.jar"]
