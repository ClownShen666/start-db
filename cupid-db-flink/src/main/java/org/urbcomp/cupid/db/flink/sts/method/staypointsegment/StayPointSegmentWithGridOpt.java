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
package org.urbcomp.cupid.db.flink.sts.method.staypointsegment;

import org.urbcomp.cupid.db.flink.algorithm.step.index.AreaEnum;
import org.urbcomp.cupid.db.flink.algorithm.step.index.Grid;
import org.urbcomp.cupid.db.flink.algorithm.step.object.GpsPoint;
import org.urbcomp.cupid.db.flink.algorithm.step.object.PointList;
import org.urbcomp.cupid.db.flink.algorithm.step.util.CalculateDistance;

import java.util.ArrayList;
import java.util.List;

public class StayPointSegmentWithGridOpt {
    public Grid grid;

    public PointList pointList;
    public double maxD;
    public long minT;

    public List<PointList> result;

    public StayPointSegmentWithGridOpt(
        PointList pointList,
        double maxD,
        long minT,
        Grid grid,
        List<PointList> result
    ) {
        this.pointList = pointList;
        this.maxD = maxD;
        this.minT = minT;
        this.grid = grid;
        this.result = result;
    }

    public void stayPointDetection() {
        GpsPoint latestGPSPoint = pointList.getPointList().get(pointList.pointList.size() - 1);
        for (int i = pointList.pointList.size() - 2; i >= 0; i--) {
            double distance;
            GpsPoint nowPoint = pointList.getPointList().get(i);
            AreaEnum area = grid.getArea(nowPoint, latestGPSPoint);
            if (area == AreaEnum.CHECK_AREA) {
                distance = CalculateDistance.calDistance(latestGPSPoint, nowPoint);
                if (distance > maxD) {
                    long timeInterval = latestGPSPoint.ingestionTime - pointList.getPointList()
                        .get(i + 1).ingestionTime;
                    if (timeInterval > minT) {
                        // 找到了驻留点
                        if (pointList.hasStayPoint) {
                            processWithStayPoints(true, i, result);
                        } else {
                            processWithoutStayPoints(true, i, result);
                        }
                    } else {
                        // 没找到驻留点
                        if (pointList.hasStayPoint) {
                            processWithStayPoints(false, i, result);
                        } else {
                            processWithoutStayPoints(false, i, result);
                        }
                    }
                    return;
                }
            } else if (area == AreaEnum.PRUNED_AREA) {
                long timeInterval = latestGPSPoint.ingestionTime - pointList.getPointList()
                    .get(i + 1).ingestionTime;
                if (timeInterval > minT) {
                    // 找到了驻留点
                    if (pointList.hasStayPoint) {
                        processWithStayPoints(true, i, result);
                    } else {
                        processWithoutStayPoints(true, i, result);
                    }
                    return;
                } else {
                    // 没找到驻留点
                    if (pointList.hasStayPoint) {
                        processWithStayPoints(false, i, result);
                    } else {
                        processWithoutStayPoints(false, i, result);
                    }
                }
                return;
            }
        }

    }

    public void processWithoutStayPoints(boolean findOrNot, int index, List<PointList> result) {
        if (findOrNot) {
            // Case 1.3 Only One Stay Point
            exactStayPoint(pointList, index, result);
        }
        // Case 2.3 No Stay Point(do nothing)
    }

    public void processWithStayPoints(boolean findOrNot, int index, List<PointList> result) {
        if (findOrNot) {
            if (index <= pointList.stayPointEndLocalIndex) {
                // Case 1.2 Two Stay Points Intersected
                mergeStayPoint(pointList);
            } else {
                // Case 1.1 Two Stay Points Separated
                int sizeOfFirstStayPoint = pointList.stayPointEndLocalIndex;
                breakStayPoint(pointList, result);
                int startIndexOfSecondStayPoint = index - sizeOfFirstStayPoint;
                if (startIndexOfSecondStayPoint >= 0) {
                    exactStayPoint(pointList, startIndexOfSecondStayPoint, result);
                }

            }
        } else {
            // Case 2.1 Far Away from the Stay Point
            GpsPoint latestGPSPoint = pointList.getPointList().get(pointList.pointList.size() - 1);
            GpsPoint stayPointEnd = pointList.getPointList()
                .get(pointList.stayPointEndLocalIndex - 1);
            AreaEnum area = grid.getArea(stayPointEnd, latestGPSPoint);
            if (area == AreaEnum.CHECK_AREA) {
                double distance = CalculateDistance.calDistance(latestGPSPoint, stayPointEnd);
                if (distance > maxD) {
                    breakStayPoint(pointList, result);
                }
            } else if (area == AreaEnum.PRUNED_AREA) {
                breakStayPoint(pointList, result);
            }
            // case 2.2(do nothing)
        }
    }

    protected void breakStayPoint(PointList pointList, List<PointList> result) {
        PointList list1 = new PointList();
        list1.pointList = new ArrayList<>(
            pointList.getPointList().subList(0, pointList.stayPointEndLocalIndex)
        );
        result.add(list1);
        // System.out.println(list);
        pointList.pointList = new ArrayList<>(
            pointList.getPointList()
                .subList(pointList.stayPointEndLocalIndex, pointList.pointList.size())
        );
        pointList.hasStayPoint = false;
        pointList.stayPointEndLocalIndex = -1;

    }

    protected void mergeStayPoint(PointList pointList) {
        int mergeListSize = pointList.pointList.size() - pointList.stayPointEndLocalIndex;
        pointList.stayPointEndGlobalIndex = pointList.stayPointEndGlobalIndex + mergeListSize;
        pointList.stayPointEndLocalIndex = pointList.pointList.size();
    }

    protected void exactStayPoint(PointList pointList, int currentIndex, List<PointList> result) {
        pointList.stayPointStartGlobalIndex += currentIndex + 2;
        pointList.hasStayPoint = true;
        PointList list1 = new PointList();
        list1.pointList = new ArrayList<>(pointList.getPointList().subList(0, currentIndex + 1));
        result.add(list1);
        pointList.pointList = new ArrayList<>(
            pointList.getPointList().subList(currentIndex + 1, pointList.pointList.size())
        );
        pointList.stayPointEndGlobalIndex = pointList.stayPointStartGlobalIndex
            + pointList.pointList.size() - 1;
        pointList.stayPointEndLocalIndex = pointList.pointList.size();
    }

}
