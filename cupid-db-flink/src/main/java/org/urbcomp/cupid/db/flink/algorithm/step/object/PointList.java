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

import java.util.ArrayList;
import java.util.List;

/**
 * @author syy
 **/
public class PointList {

    // temporary gps point list
    private List<GpsPoint> pointList = new ArrayList<>();
    // indicates the presence or absence of stay point
    private boolean hasStayPoint = false;

    public void setPointList(List<GpsPoint> pointList) {
        this.pointList = pointList;
    }

    // the location of stay point(total), inclusive
    private int stayPointStartGlobalIndex = -1;

    public boolean isHasStayPoint() {
        return hasStayPoint;
    }

    public void setHasStayPoint(boolean hasStayPoint) {
        this.hasStayPoint = hasStayPoint;
    }

    public int getStayPointStartGlobalIndex() {
        return stayPointStartGlobalIndex;
    }

    public void setStayPointStartGlobalIndex(int stayPointStartGlobalIndex) {
        this.stayPointStartGlobalIndex = stayPointStartGlobalIndex;
    }

    public int getStayPointEndGlobalIndex() {
        return stayPointEndGlobalIndex;
    }

    public void setStayPointEndGlobalIndex(int stayPointEndGlobalIndex) {
        this.stayPointEndGlobalIndex = stayPointEndGlobalIndex;
    }

    public int getStayPointEndLocalIndex() {
        return stayPointEndLocalIndex;
    }

    public void setStayPointEndLocalIndex(int stayPointEndLocalIndex) {
        this.stayPointEndLocalIndex = stayPointEndLocalIndex;
    }

    private int stayPointEndGlobalIndex = -1;
    private int stayPointEndLocalIndex = -1;

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
