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
package org.urbcomp.cupid.db.flink.sts.index;

import org.urbcomp.cupid.db.flink.algorithm.step.object.GpsPoint;

import java.io.Serializable;

public class Grid implements Serializable {

    public double lng1 = 103.987863;
    public double lat1 = 10.901805;
    public double lat2 = 31.367676;
    public double condition;
    // 0~100
    public int[] squareArray = {
        1,
        0,
        1,
        4,
        9,
        16,
        25,
        36,
        49,
        64,
        81,
        100,
        121,
        144,
        169,
        196,
        225,
        256,
        289,
        324,
        361,
        400,
        441,
        484,
        529,
        576,
        625,
        676,
        729,
        784,
        841,
        900,
        961,
        1024,
        1089,
        1156,
        1225,
        1296,
        1369,
        1444,
        1521,
        1600,
        1681,
        1764,
        1849,
        1936,
        2025,
        2116,
        2209,
        2304,
        2401,
        2500,
        2601,
        2704,
        2809,
        2916,
        3025,
        3136,
        3249,
        3364,
        3481,
        3600,
        3721,
        3844,
        3969,
        4096,
        4225,
        4356,
        4489,
        4624,
        4761,
        4900,
        5041,
        5184,
        5329,
        5476,
        5625,
        5776,
        5929,
        6084,
        6241,
        6400,
        6561,
        6724,
        6889,
        7056,
        7225,
        7396,
        7569,
        7744,
        7921,
        8100,
        8281,
        8464,
        8649,
        8836,
        9025,
        9216,
        9409,
        9604,
        9801,
        10000 };
    public double deltaLon;

    public double deltaLat;

    public Grid(double maxD, int n) {
        this.condition = 8 * n * n;
        double d = maxD * Math.sqrt(2) / (4 * n);
        deltaLat = d * 360 / (2 * Math.PI * 6371004);
        deltaLon = d * 360 / (2 * Math.PI * 6371004 * Math.cos((lat1 + lat2) * Math.PI / 360));
    }

    public void calGirdId(GpsPoint point) {
        point.setLatCol((int) ((point.getLat() - lat1) / deltaLat));
        point.setLonCol((int) ((point.getLng() - lng1) / deltaLon));
    }

    public AreaEnum getArea(GpsPoint p0, GpsPoint p1) {
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
