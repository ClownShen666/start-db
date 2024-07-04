FROM amazoncorretto:8-al2-generic-jdk

WORKDIR /cupid-server

COPY cupid-db.jar /cupid-server/cupid-db.jar
COPY cupid-db-flink-1.0.0-SNAPSHOT-jar-with-dependencies.jar /cupid-server/cupid-db-flink-1.0.0-SNAPSHOT-jar-with-dependencies.jar

CMD ["java", "-jar", "/cupid-server/cupid-db.jar"]
