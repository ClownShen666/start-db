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
package org.urbcomp.start.db.flink.algorithm.step.util;

import org.urbcomp.start.db.flink.algorithm.step.object.SegGpsPoint;

import java.util.List;

/**
 * @author syy
 */
public class FindGPSPointsWithInT {
    public static int findIndex(List<SegGpsPoint> pointList, long t) {
        int size = pointList.size();
        long maxT = pointList.get(size - 1).getTime().getTime();
        int i = 0;
        while (i < size && maxT - pointList.get(i).getTime().getTime() >= t) {
            i++;
        }
        return i + 1;
    }
}
