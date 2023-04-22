LOAD CSV INPATH 'hdfs://user/data.csv' TO gemo_table(
    road.oid oid,
    name 0,
    startP 1,
    endP 2,
    dtg to_timestamp(3)
) WITH HEADER