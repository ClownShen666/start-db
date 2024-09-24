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
package org.urbcomp.start.db.flink.index;

import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.urbcomp.start.db.flink.cache.GlobalCache;
import org.urbcomp.start.db.flink.storage.KafkaToHBaseWriter;
import org.urbcomp.start.db.metadata.MetadataAccessorFromDb;
import org.urbcomp.start.db.metadata.entity.Field;
import org.urbcomp.start.db.util.GeoFunctions;
import org.urbcomp.start.db.util.MetadataUtil;
import org.urbcomp.start.db.util.UserDbTable;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Data
public class GridIndex {
    public GridIndex(
        double minLongitude,
        double maxLongitude,
        double minLatitude,
        double maxLatitude,
        int gridSize,
        Jedis jedis
    ) {
        this.minLongitude = minLongitude;
        // 经度范围
        this.minLatitude = minLatitude;
        // 纬度范围
        this.gridSize = gridSize;
        this.rows = (int) ((GeoFunctions.getDistanceInM(
            maxLongitude,
            midLatitude,
            minLongitude,
            midLatitude
        )) / gridSize);
        this.midLatitude = (int) ((minLatitude + maxLatitude) / 2);
        this.midLongitude = (int) ((minLongitude + maxLongitude) / 2);
        this.jedis = jedis;
    }

    public static final ConcurrentMap<String, Long> tableQueryTimeOffset =
        new ConcurrentHashMap<>();

    private double minLongitude;
    private double minLatitude;
    private int gridSize;
    private int midLatitude;
    private int midLongitude;

    private Jedis jedis;

    public List<String> getFieldTypeList() {
        return fieldTypeList;
    }

    public void setFieldTypeList(List<String> fieldTypeList) {
        this.fieldTypeList = fieldTypeList;
    }

    private int rows;
    private List<String> fieldTypeList;

    private void updateQueryTimeOffset(String userDbTableKey, long offset) {
        tableQueryTimeOffset.put(userDbTableKey, offset);
    }

    public Point getPointFromRowStr(String rowStr, List<String> fieldTypeList)
        throws ParseException {
        int len = fieldTypeList.size();
        String[] columnList = rowStr.split(",,");
        WKTReader reader = new WKTReader();
        for (int i = 0; i < len; i++) {
            String type = fieldTypeList.get(i);
            if ("Point".equals(type) || "point".equals(type)) {
                return (Point) reader.read(columnList[i]);
            }
        }
        throw new IllegalArgumentException("Invalid index no fields");

    }

    // 计算空间对象所在网格的编号
    private String calculateGridId(Point point, String userDbTableKey) {

        // 将经纬度映射到网格中，然后根据网格大小计算网格编号
        int row = (int) GeoFunctions.getDistanceInM(
            point.getX(),
            midLatitude,
            minLongitude,
            midLatitude
        ) / gridSize;
        int col = (int) GeoFunctions.getDistanceInM(
            point.getY(),
            midLongitude,
            minLatitude,
            midLongitude
        ) / gridSize;
        // 二维坐标转换为唯一编号
        return userDbTableKey + (row * rows + col);
    }

    public void addSpatialObject(String rowStr, String userDbTableKey, long offset, long timestamp)
        throws ParseException {

        updateQueryTimeOffset(userDbTableKey, offset);

        Point obj = getPointFromRowStr(rowStr, fieldTypeList);
        if (obj == null) return;
        // 计算对象所在网格的编号
        String gridId = calculateGridId(obj, userDbTableKey);
        // 将对象加入对应网格
        if (!jedis.exists(gridId)) {
            jedis.rpush(gridId, rowStr);
            jedis.expire(gridId, GlobalCache.KAFKA_DATA_TTL);
        } else jedis.rpush(gridId, rowStr);
        // 保存第一条数据的时间
        if (!GlobalCache.girdIdLatestTimestamp.asMap().containsKey(gridId))
            GlobalCache.girdIdLatestTimestamp.put(gridId, timestamp);

        Optional<Long> optionalTimestamp = Optional.ofNullable(
            GlobalCache.girdIdLatestTimestamp.getIfPresent(gridId)
        );
        optionalTimestamp.ifPresent(val -> {
            if ((timestamp - val) > GlobalCache.KAFKA_DATA_TTL
                || jedis.objectIdletime(gridId) >= GlobalCache.KAFKA_DATA_TTL) {
                List<String> strRowList = jedis.lrange(userDbTableKey, 0, -1);

                UserDbTable userDbTable = MetadataUtil.splitUserDbTable(userDbTableKey);
                MetadataAccessorFromDb accessor = new MetadataAccessorFromDb();
                List<Field> fieldList = accessor.getFields(
                    userDbTable.getUsername(),
                    userDbTable.getDbName(),
                    userDbTable.getTableName()
                );

                List<String> fieldTypeList = new ArrayList<>();
                List<String> fieldNameList = new ArrayList<>();
                for (Field field : fieldList) {
                    fieldTypeList.add(field.getType());
                    fieldNameList.add(field.getName());
                }
                // Write into geomesa-hbase
                KafkaToHBaseWriter.write(
                    userDbTable.getUsername(),
                    userDbTable.getDbName(),
                    userDbTable.getTableName(),
                    strRowList,
                    fieldNameList,
                    fieldTypeList
                );
                // delete
                jedis.del(gridId);
            }
        });

    }

    // 根据空间范围查询网格内的空间对象
    public List<String> querySpatialObjects(
        double x1,
        double y1,
        double x2,
        double y2,
        String userDbTableKey
    ) {
        Set<String> gridIds = getIntersectedGridIds(x1, y1, x2, y2, userDbTableKey);
        List<String> result = new ArrayList<>();
        // 遍历与查询范围相交的网格，将其中的空间对象加入结果列表
        for (String gridId : gridIds) {
            if (jedis.exists(gridId)) {
                result.addAll(jedis.lrange(gridId, 0, -1));
            }
            // if (GlobalCache.grid.getIfPresent(gridId) != null) {
            // result.addAll(Objects.requireNonNull(GlobalCache.grid.getIfPresent(gridId)));
            // }
        }
        return result;
    }

    // 计算与查询范围相交的网格编号
    private Set<String> getIntersectedGridIds(
        double x1,
        double y1,
        double x2,
        double y2,
        String userDbTableKey
    ) {
        Set<String> intersectedGridIds = new HashSet<>();
        // 根据查询范围的实际边界计算相交的网格编号
        int startRow = (int) (GeoFunctions.getDistanceInM(
            x1,
            midLatitude,
            minLongitude,
            midLatitude
        ) / gridSize);
        int endRow = (int) (GeoFunctions.getDistanceInM(x2, midLatitude, minLongitude, midLatitude)
            / gridSize);
        int startCol = (int) GeoFunctions.getDistanceInM(
            y1,
            midLongitude,
            minLatitude,
            midLongitude
        ) / gridSize;
        int endCol = (int) GeoFunctions.getDistanceInM(y2, midLongitude, minLatitude, midLongitude)
            / gridSize;
        // 遍历相交的网格范围，计算网格编号
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startCol; j <= endCol; j++) {
                intersectedGridIds.add(userDbTableKey + (i * rows + j));
            }
        }
        return intersectedGridIds;
    }

}
