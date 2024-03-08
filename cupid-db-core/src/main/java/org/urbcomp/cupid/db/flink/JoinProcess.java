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
package org.urbcomp.cupid.db.flink;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.urbcomp.cupid.db.config.ExecuteEngine;
import org.urbcomp.cupid.db.metadata.CalciteHelper;
import org.urbcomp.cupid.db.metadata.MetadataAccessorFromDb;
import org.urbcomp.cupid.db.metadata.entity.Field;
import org.urbcomp.cupid.db.util.SqlParam;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

public class JoinProcess extends ProcessFunction<String, String> {
    // todo:设置具体大小
    private final Cache<String, List<String[]>> cache = Caffeine.newBuilder()
        .maximumSize(10000000)
        .build();
    private Map<String, Integer> streamFiledToIdx;

    private final Map<String, Integer> dimensionFiledToIdx = new HashMap<>();
    private JoinVisitor joinVisitor;

    private JoinVisitor.On on;
    private final String sql;
    private List<String> selectFiledList;

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
        String s,
        ProcessFunction<String, String>.Context context,
        Collector<String> collector
    ) {
        String[] streamRow = recordToArr(s);
        String leftTable = on.left.split("\\.")[0];

        String dimensionTable = "streamResult".equals(leftTable)
            ? on.right.split("\\.")[0]
            : leftTable;

        String streamTableAndFiled = "streamResult".equals(leftTable) ? on.left : on.right;
        String dimensionTableAndFiled = "streamResult".equals(leftTable) ? on.right : on.left;

        boolean isMatch = false;
        // todo: parse where

        for (String[] staticRow : Objects.requireNonNull(cache.getIfPresent(dimensionTable))) {
            List<String> res = new ArrayList<>();
            for (String filed : selectFiledList) {
                if (streamFiledToIdx.containsKey(filed)) {
                    res.add(streamRow[streamFiledToIdx.get(filed)]);
                }
            }

            if (streamRow[streamFiledToIdx.get(streamTableAndFiled)].equals(
                staticRow[dimensionFiledToIdx.get(dimensionTableAndFiled)]
            )) {
                for (String filed : selectFiledList) {
                    if (dimensionFiledToIdx.containsKey(filed)) {
                        res.add(staticRow[dimensionFiledToIdx.get(filed)]);
                    }
                }
                isMatch = true;
                collector.collect(arrToRecord(res));
            }
        }

        if (!isMatch) {
            List<String> res = new ArrayList<>();

            for (String filed : selectFiledList) {
                if (streamFiledToIdx.containsKey(filed)) {
                    res.add(streamRow[streamFiledToIdx.get(filed)]);
                }
            }
            int count = selectFiledList.size() - res.size();
            for (int i = 0; i < count; i++) {
                res.add(null);
            }
            collector.collect(arrToRecord(res));
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
        this.joinVisitor = new JoinVisitor(sql);
        String streamTable = streamTable();
        this.selectFiledList = getSelectFiledList();
        this.on = getOn();
        this.streamFiledToIdx = getStreamFiledToIdx(sqlParam, streamTable);

        for (Map.Entry<String, String> entry : getDimensionTableToSql().entrySet()) {
            String table = entry.getKey();
            String sql = entry.getValue();
            List<String[]> rows = new ArrayList<>();
            ResultSet rs = statement.executeQuery(sql);
            int columnCount = rs.getMetaData().getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                dimensionFiledToIdx.put(table + "." + rs.getMetaData().getColumnName(i + 1), i);
            }
            while (rs.next()) {
                String[] row = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getObject(i + 1).toString();
                }
                rows.add(row);
            }
            cache.put(table, rows);
            rs.close();
        }
        statement.close();
        connection.close();
    }

    public JoinVisitor.On getOn() {
        return joinVisitor.getDimensionOnList().get(0);

    }

    public List<String> getSelectFiledList() {
        Set<String> set = joinVisitor.getDimensionFieldList()
            .stream()
            .map(field -> field.table)
            .collect(Collectors.toSet());
        List<String> list = joinVisitor.getStreamFieldList()
            .stream()
            .filter(field -> field.isSelect && !set.contains(field.table))
            .map(field -> "streamResult." + field.field)
            .collect(Collectors.toList());
        List<String> res = joinVisitor.getDimensionFieldList()
            .stream()
            .filter(field -> field.isSelect)
            .map(field -> field.table + "." + field.field)
            .collect(Collectors.toList());
        res.addAll(list);
        return res;

    }

    public Map<String, Integer> getStreamFiledToIdx(SqlParam sqlParam, String tableName) {

        Map<String, Integer> filedIdx = new HashMap<>();

        MetadataAccessorFromDb accessor = new MetadataAccessorFromDb();

        List<Field> fields = accessor.getFields(
            sqlParam.getUserName(),
            sqlParam.getDbName(),
            tableName
        );

        for (int i = 0; i < fields.size(); i++) {
            filedIdx.put("streamResult" + "." + fields.get(i).getName(), i);
        }

        return filedIdx;
    }

    public String[] recordToArr(String record) {
        return record.substring(3, record.length() - 1).split(",,");
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

    public String streamTable() {
        return joinVisitor.getStreamTableList().get(0);
    }

}
