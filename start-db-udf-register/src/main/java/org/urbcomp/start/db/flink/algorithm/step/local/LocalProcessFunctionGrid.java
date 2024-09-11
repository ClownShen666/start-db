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
package org.urbcomp.start.db.flink.algorithm.step.local;

import org.urbcomp.start.db.flink.algorithm.step.index.Grid;
import org.urbcomp.start.db.flink.algorithm.step.method.staypointsegment.StayPointSegmentWithGridOpt;
import org.urbcomp.start.db.flink.algorithm.step.object.PointList;
import org.urbcomp.start.db.flink.algorithm.step.object.SegGpsPoint;

import java.io.Serializable;
import java.util.List;

public class LocalProcessFunctionGrid implements Serializable {

    private double maxD;
    private long minT;
    private Grid grid;

    private PointList pointList;

    public double getMaxD() {
        return maxD;
    }

    public void setMaxD(double maxD) {
        this.maxD = maxD;
    }

    public long getMinT() {
        return minT;
    }

    public void setMinT(long minT) {
        this.minT = minT;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public LocalProcessFunctionGrid(double maxD, long minT, int gridSize) {
        this.maxD = maxD;
        this.minT = minT;
        grid = new Grid(maxD, gridSize);
        pointList = new PointList();
    }

    public void process(SegGpsPoint point, List<PointList> result) {
        if (!result.isEmpty()) {
            result.clear();
        }
        grid.calGirdId(point);
        pointList.add(point);
        StayPointSegmentWithGridOpt stayPointSegment = new StayPointSegmentWithGridOpt(
            pointList,
            maxD,
            minT,
            grid,
            result
        );
        stayPointSegment.stayPointDetection();
    }
}
