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
package org.urbcomp.cupid.db.flink.index;

import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.urbcomp.cupid.db.flink.cache.GlobalCache;
import org.urbcomp.cupid.db.util.GeoFunctions;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class GridIndex {
    public GridIndex(
        double minLongitude,
        double maxLongitude,
        double minLatitude,
        double maxLatitude,
        int gridSize
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
    }

    public static final ConcurrentMap<String, Long> tableQueryTimeOffset =
        new ConcurrentHashMap<>();

    private double minLongitude;
    private double minLatitude;
    private int gridSize;
    private int midLatitude;
    private int midLongitude;

    public List<String> getFieldTypeList() {
        return fieldTypeList;
    }

    public void setFieldTypeList(List<String> fieldTypeList) {
        this.fieldTypeList = fieldTypeList;
    }

    private int rows;
    private List<String> fieldTypeList;

    private void updateQueryTimeOffset(String tableName, long offset) {
        tableQueryTimeOffset.put(tableName.toLowerCase(), offset);
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
        return null;

    }

    // 计算空间对象所在网格的编号
    private String calculateGridId(Point point, String tableName) {

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
        return tableName + (row * rows + col);
    }

    public void addSpatialObject(String rowStr, String tableName, long offset)
        throws ParseException {
        updateQueryTimeOffset(tableName, offset);

        Point obj = getPointFromRowStr(rowStr, fieldTypeList);
        if (obj == null) return;
        // 计算对象所在网格的编号
        String gridId = calculateGridId(obj, tableName);

        // 将对象加入对应网格
        if (GlobalCache.grid.getIfPresent(gridId) == null) {
            GlobalCache.grid.put(gridId, new ArrayList<>());
        }
        Objects.requireNonNull(GlobalCache.grid.getIfPresent(gridId)).add(rowStr);
    }

    // 根据空间范围查询网格内的空间对象
    public List<String> querySpatialObjects(
        double x1,
        double y1,
        double x2,
        double y2,
        String tableName
    ) {
        Set<String> gridIds = getIntersectedGridIds(x1, y1, x2, y2, tableName);
        List<String> result = new ArrayList<>();
        // 遍历与查询范围相交的网格，将其中的空间对象加入结果列表
        for (String gridId : gridIds) {
            if (GlobalCache.grid.getIfPresent(gridId) != null) {
                result.addAll(Objects.requireNonNull(GlobalCache.grid.getIfPresent(gridId)));
            }
        }
        return result;
    }

    // 计算与查询范围相交的网格编号
    private Set<String> getIntersectedGridIds(
        double x1,
        double y1,
        double x2,
        double y2,
        String tableName
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
                intersectedGridIds.add(tableName + (i * rows + j));
            }
        }
        return intersectedGridIds;
    }

}
