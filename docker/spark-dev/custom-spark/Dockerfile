FROM monokultur/cluster-apache-spark:3.0.2

COPY ./guava-24.1-jre.jar /opt/spark/jars/guava-24.1-jre.jar

RUN rm -f /opt/spark/jars/guava-14.0.1.jar

CMD ["/bin/bash", "/start-spark.sh"]