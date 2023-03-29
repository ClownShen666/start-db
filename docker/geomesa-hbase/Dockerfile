FROM spatiotemporallab/geomesa-hbase:base

COPY cupid-db-store-1.0.0-SNAPSHOT.jar $HBASE_HOME/lib/

ENTRYPOINT ["/bin/sh", "-c", "${HBASE_HOME}/bin/start-hbase.sh && tail -f /dev/null"]