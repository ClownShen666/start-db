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
package org.urbcomp.start.db.flink.algorithm.step.index;

import org.urbcomp.start.db.flink.algorithm.step.object.SegGpsPoint;

import java.io.Serializable;

public class Grid implements Serializable {
    private static final double LNG1 = 103.987863;
    private static final double LAT1 = 10.901805;
    private static final double LAT2 = 31.367676;
    private final double condition;
    private final int[] squareArray;
    private final double deltaLon;
    private final double deltaLat;

    // 0~100
    private int[] initializeSquareArray() {
        int[] squares = new int[102];
        for (int i = -1; i < 101; i++) {
            squares[i + 1] = i * i;
        }
        return squares;
    }

    public Grid(double maxD, int n) {
        this.condition = 8 * n * n;
        double d = maxD * Math.sqrt(2) / (4 * n);
        deltaLat = d * 360 / (2 * Math.PI * 6371004);
        deltaLon = d * 360 / (2 * Math.PI * 6371004 * Math.cos((LAT1 + LAT2) * Math.PI / 360));
        this.squareArray = initializeSquareArray();
    }

    public void calGirdId(SegGpsPoint point) {
        point.setLatCol((int) ((point.getLat() - LAT1) / deltaLat));
        point.setLonCol((int) ((point.getLng() - LNG1) / deltaLon));
    }

    public AreaEnum getArea(SegGpsPoint p0, SegGpsPoint p1) {
        int diffLon = Math.abs(p1.getLonCol() - p0.getLonCol());
        int diffLat = Math.abs(p1.getLatCol() - p0.getLatCol());
        int diffLonMinusOne = diffLon - 1;
        int diffLatMinusOne = diffLat - 1;
        int diffLonPlusOne = diffLon + 1;
        int diffLatPlusOne = diffLat + 1;

        /*if (diffLonPlusOne <= 100 && diffLatPlusOne <= 100) {
            if (squareArray[diffLonMinusOne + 1] + squareArray[diffLatMinusOne + 1] >= condition) {
                return AreaEnum.PRUNED_AREA;
            } else if (squareArray[diffLonPlusOne + 1] + squareArray[diffLatPlusOne + 1] <= condition) {
                return AreaEnum.CONFIRMED_AREA;
            } else {
                return AreaEnum.CHECK_AREA;
            }
        } else {*/
        if (diffLonMinusOne * diffLonMinusOne + diffLatMinusOne * diffLatMinusOne >= condition) {
            return AreaEnum.PRUNED_AREA;
        }
        if (diffLonPlusOne * diffLonPlusOne + diffLatPlusOne * diffLatPlusOne <= condition) {
            return AreaEnum.CONFIRMED_AREA;
        }
        return AreaEnum.CHECK_AREA;
        // }
    }
}
