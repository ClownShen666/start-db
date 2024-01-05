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
package org.urbcomp.cupid.db.flink.sts.object;

import java.util.ArrayList;
import java.util.List;

/**
 * @author syy
 **/
public class PointList {

    // temporary gps point list
    public List<GpsPoint> pointList = new ArrayList<>();
    // indicates the presence or absence of stay point
    public boolean hasStayPoint = false;
    // the location of stay point(total), inclusive
    public int stayPointStartGlobalIndex = -1;
    public int stayPointEndGlobalIndex = -1;
    public int stayPointEndLocalIndex = -1;

    public PointList() {}

    public void add(GpsPoint p) {
        pointList.add(p);
    }

    public List<GpsPoint> getPointList() {
        return pointList;
    }

    @Override
    public String toString() {
        return pointList.toString();
    }
}
