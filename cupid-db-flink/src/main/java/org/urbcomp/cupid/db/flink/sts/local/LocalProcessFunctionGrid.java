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
package org.urbcomp.cupid.db.flink.sts.local;

import org.urbcomp.cupid.db.flink.algorithm.step.index.Grid;
import org.urbcomp.cupid.db.flink.algorithm.step.method.staypointsegment.StayPointSegmentWithGridOpt;
import org.urbcomp.cupid.db.flink.algorithm.step.object.GpsPoint;
import org.urbcomp.cupid.db.flink.algorithm.step.object.PointList;

import java.io.Serializable;
import java.util.List;

public class LocalProcessFunctionGrid implements Serializable {

    public double maxD;
    public long minT;
    public Grid grid;

    public LocalProcessFunctionGrid(double maxD, long minT, int gridSize) {
        this.maxD = maxD;
        this.minT = minT;
        grid = new Grid(maxD, gridSize);
    }

    public void process(PointList pointList, GpsPoint point, List<PointList> result) {
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
