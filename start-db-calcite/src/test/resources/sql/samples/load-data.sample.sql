LOAD CSV INPATH 'hdfs://user/data.csv' TO gemo_table(
    road.oid _c0,
    name _c1,
    startP _c2,
    endP _c3,
    dtg to_timestamp(_c4)
) WITH HEADER