/* 
 * Copyright (C) 2022  ST-Lab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.urbcomp.start.db.flink.processfunction;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.types.Row;
import org.apache.flink.util.Collector;
import org.urbcomp.start.db.config.ExecuteEngine;
import org.urbcomp.start.db.flink.visitor.JoinVisitor;
import org.urbcomp.start.db.metadata.CalciteHelper;
import org.urbcomp.start.db.metadata.MetadataAccessorFromDb;
import org.urbcomp.start.db.util.FlinkSqlParam;
import org.urbcomp.start.db.util.SqlParam;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class JoinProcess extends ProcessFunction<Row, Row> {
    // todo:设置具体大小
    private final Cache<String, List<Object[]>> cache = Caffeine.newBuilder()
        .maximumSize(10000000)
        .build();
    private Map<String, Integer> streamFiledToIdx;

    private Map<String, Integer> firstStreamFiledToIdx;
    private final Map<String, Integer> selectStreamFiledToIdx = new HashMap<>();

    private final Map<String, Integer> dimensionFiledToIdx = new HashMap<>();
    private JoinVisitor joinVisitor;

    private List<JoinVisitor.On> onList;
    private final String sql;
    private List<String> selectFiledList;
    private final static String STREAM_TABLE = "streamResult";

    @Override
    public void open(Configuration parameters) throws Exception {
        loadData();
    }

    @Override
    public void close() throws Exception {
        cache.cleanUp();
    }

    public JoinProcess(String sql) {
        this.sql = sql;
    }

    @Override
    public void processElement(
        Row row,
        ProcessFunction<Row, Row>.Context context,
        Collector<Row> collector
    ) {
        List<Row> streamRows = new ArrayList<>();

        streamRows.add(row);

        boolean isFirstLoop = true;
        for (JoinVisitor.On on1 : onList) {
            if (on1.left.split("\\.")[0].equals(STREAM_TABLE)
                && on1.right.split("\\.")[0].equals(STREAM_TABLE)) continue;
            List<Row> temp = new ArrayList<>();
            for (Row streamRow : streamRows) {
                temp.addAll(compareOn(on1, streamRow, isFirstLoop));
                isFirstLoop = false;
            }
            streamRows = temp;
        }
        for (Row streamRow : streamRows) {
            collector.collect(streamRow);
        }

    }

    public String dimensionTableAndFiled(JoinVisitor.On on) {

        return STREAM_TABLE.equals(leftTableFromOn(on)) ? on.right : on.left;

    }

    public String streamTableAndFiled(JoinVisitor.On on) {
        return STREAM_TABLE.equals(leftTableFromOn(on)) ? on.left : on.right;

    }

    public List<Row> compareOn(JoinVisitor.On on, Row streamRow, boolean isFirstInvoke) {

        String dimensionTable = dimensionTableFromOn(on);

        String streamTableAndFiled = streamTableAndFiled(on);
        String dimensionTableAndFiled = dimensionTableAndFiled(on);

        boolean isMatch = false;

        // todo: parse where
        List<Row> rows = new ArrayList<>();
        for (Object[] staticRow : Objects.requireNonNull(cache.getIfPresent(dimensionTable))) {
            List<String> res = new ArrayList<>();
            Row row = new Row(selectFiledList.size());

            if (Objects.equals(
                streamRow.getField(streamFiledToIdx.get(streamTableAndFiled)),
                staticRow[dimensionFiledToIdx.get(dimensionTableAndFiled)]
            )) {
                int i = 0;
                for (String filed : selectFiledList) {
                    if (dimensionFiledToIdx.containsKey(filed)
                        && filed.split("\\.")[0].equals(dimensionTable)) {
                        // res.add(staticRow[dimensionFiledToIdx.get(filed)]);
                        row.setField(i, staticRow[dimensionFiledToIdx.get(filed)]);
                    } else if (streamFiledToIdx.containsKey(filed)
                        && filed.split("\\.")[0].equals(STREAM_TABLE)) {
                            // res.add(streamRow.getField(streamFiledToIdx.get(filed)));
                            row.setField(i, streamRow.getField(streamFiledToIdx.get(filed)));
                        } else row.setField(i, null);
                    i++;
                }
                rows.add(row);
                isMatch = true;
            }
        }

        if (!isFirstInvoke) {
            streamFiledToIdx = selectStreamFiledToIdx;
        } else {
            streamFiledToIdx = firstStreamFiledToIdx;

        }

        if (isMatch) return rows;

        Row row = new Row(selectFiledList.size());
        int i = 0;
        for (String filed : selectFiledList) {
            if (streamFiledToIdx.containsKey(filed)) {
                // res.add(streamRow.get(streamFiledToIdx.get(filed)));
                row.setField(i, streamRow.getField(streamFiledToIdx.get(filed)));
            } else {
                row.setField(i, null);
            }
            i++;
        }
        rows.add(row);
        return rows;

    }

    public String leftTableFromOn(JoinVisitor.On on) {
        return on.left.split("\\.")[0];
    }

    public String dimensionTableFromOn(JoinVisitor.On on) {
        String leftTable = leftTableFromOn(on);
        return STREAM_TABLE.equals(leftTable) ? on.right.split("\\.")[0] : leftTable;
    }

    private void initSelectStreamFiledToIdx() {
        for (int i = 0; i < selectFiledList.size(); i++) {
            selectStreamFiledToIdx.put(selectFiledList.get(i), i);
        }
    }

    public void loadData() throws SQLException {
        // todo: 设置预查询数据量，传递sqlParam
        SqlParam sqlParam = new SqlParam("root", "default", ExecuteEngine.FLINK, true);
        FlinkSqlParam flinkSqlParam = new FlinkSqlParam(sqlParam);
        SqlParam.CACHE.set(sqlParam);
        flinkSqlParam.setTestNum(1);
        FlinkSqlParam.CACHE.set(flinkSqlParam);
        Connection connection = CalciteHelper.createConnection();
        Statement statement = connection.createStatement();
        statement.executeQuery(sql);
        // todo: use setJoinVisitor before loading rather than new during loading
        this.joinVisitor = new JoinVisitor(sql, flinkSqlParam);
        this.selectFiledList = joinVisitor.getSelectFiledList();
        this.onList = joinVisitor.getMixOnList();
        this.streamFiledToIdx = getStreamFiledToIdx();
        this.firstStreamFiledToIdx = streamFiledToIdx;
        // initNameTypeList(sqlParam);
        initSelectStreamFiledToIdx();
        // stringToRow = new StringToRow(nameList,typeList);
        for (Map.Entry<String, String> entry : getDimensionTableToSql().entrySet()) {
            String table = entry.getKey();
            String sql = entry.getValue();
            List<Object[]> rows = new ArrayList<>();
            ResultSet rs = statement.executeQuery(sql);
            int columnCount = rs.getMetaData().getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                dimensionFiledToIdx.put(table + "." + rs.getMetaData().getColumnName(i + 1), i);
            }
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                rows.add(row);
            }
            cache.put(table, rows);
            rs.close();
        }
        statement.close();
        connection.close();
    }

    public Map<String, Integer> getStreamFiledToIdx() {
        Map<String, Integer> filedIdx = new HashMap<>();

        int i = 0;
        for (JoinVisitor.Field field : joinVisitor.getStreamFieldList()) {
            filedIdx.put("streamResult" + "." + field.field, i++);
        }

        return filedIdx;
    }

    public Map<String, String> getFiledMapType(SqlParam sqlParam) {
        Map<String, String> filedMapType = new HashMap<>();
        MetadataAccessorFromDb accessor = new MetadataAccessorFromDb();
        joinVisitor.getStreamTableList().forEach(s -> {
            accessor.getFields(sqlParam.getUserName(), sqlParam.getDbName(), s)
                .forEach(
                    field -> filedMapType.put(STREAM_TABLE + "." + field.getName(), field.getType())
                );
        });
        joinVisitor.getDimensionTableList().forEach(s -> {
            accessor.getFields(sqlParam.getUserName(), sqlParam.getDbName(), s)
                .forEach(field -> filedMapType.put(s + "." + field.getName(), field.getType()));
        });
        return filedMapType;
    }

    public List<String> recordToArr(String record) {

        return Arrays.stream(record.substring(3, record.length() - 1).split(",,"))
            .collect(Collectors.toList());
    }

    public String arrToRecord(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s).append(",,");
        }
        sb.delete(sb.length() - 2, sb.length());

        return sb.toString();
    }

    public Map<String, String> getDimensionTableToSql() {
        HashMap<String, String> tableSql = new HashMap<>();
        joinVisitor.getDimensionFieldList()
            .stream()
            .filter(field -> !Objects.equals(field.table, "streamResult"))
            .collect(
                Collectors.groupingBy(
                    filed -> filed.table,
                    Collectors.mapping(filed -> filed.field, Collectors.toList())
                )
            )
            .forEach((k, v) -> {
                StringBuilder sql = new StringBuilder("select ");
                v.forEach(filed -> sql.append(filed).append(","));
                sql.deleteCharAt(sql.length() - 1);
                sql.append(" from ").append(k);
                tableSql.put(k, sql.toString());
            });
        return tableSql;
    }

}
