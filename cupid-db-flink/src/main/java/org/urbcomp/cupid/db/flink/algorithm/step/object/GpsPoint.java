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
package org.urbcomp.cupid.db.flink.algorithm.step.object;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author syy
 **/

public class GpsPoint {

    // 经度坐标(x)
    public double lng;
    // 纬度坐标(y)
    public double lat;
    public String tid;
    // 时间戳
    public long ingestionTime;
    public int lonCol;
    public int latCol;
    public boolean gridFlag = false;
    public long processTime;

    // 构造方法
    public GpsPoint() {}

    public GpsPoint(double lng, double lat, String tid, String ingestionTime, long a)
        throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.ingestionTime = formatter.parse(ingestionTime).getTime() + a;
        this.tid = tid;
        this.lng = lng;
        this.lat = lat;
    }

    public GpsPoint(double lng, double lat, String tid, long ingestionTime, long a)
        throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.ingestionTime = ingestionTime + a;
        this.tid = tid;
        this.lng = lng;
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "编号:" + tid + ",时间戳:" + new Timestamp(ingestionTime) + ",经度:" + lng + ",纬度:" + lat;
    }

}
