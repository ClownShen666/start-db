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
package org.urbcomp.start.db.algorithm.trajectorysegment;

import org.urbcomp.start.db.algorithm.staypointdetect.StayPointDetect;
import org.urbcomp.start.db.algorithm.staypointdetect.StayPointDetectResult;
import org.urbcomp.start.db.model.point.GPSPoint;
import org.urbcomp.start.db.model.trajectory.Trajectory;

import java.util.ArrayList;
import java.util.List;

public class StayPointSegment implements AbstractTrajectorySegment {
    private final double maxStayTimeInSecond;
    private final double maxDistInMeter;
    private final StayPointDetect stayPointDetect = new StayPointDetect();

    public StayPointSegment(double maxStayTimeInSecond, double maxDistInMeter) {
        this.maxStayTimeInSecond = maxStayTimeInSecond;
        this.maxDistInMeter = maxDistInMeter;
    }

    @Override
    public List<Trajectory> segment(Trajectory trajectory) {
        List<Trajectory> result = new ArrayList<>();
        List<GPSPoint> pts = trajectory.getGPSPointList();
        List<StayPointDetectResult> stayPoints = stayPointDetect.detect(
            trajectory,
            maxDistInMeter,
            maxStayTimeInSecond
        );
        if (stayPoints.size() == 0) {
            result.add(trajectory);
        } else {
            int stayPointIndex = 0;
            int startIndex = 0;
            int curIndex = 0;
            while (curIndex < trajectory.getGPSPointList().size()) {
                GPSPoint point = pts.get(curIndex);
                if (point.getTime() == stayPoints.get(stayPointIndex).getStarTime()) {
                    Trajectory subTrajectory = new Trajectory(
                        trajectory.getTid() + "_stayPoint_" + result.size(),
                        trajectory.getOid(),
                        new ArrayList<>(pts.subList(startIndex, curIndex + 1))
                    );
                    if (subTrajectory.getGPSPointList().size() > 1) {
                        result.add(subTrajectory);
                    }
                } else if (point.getTime() == stayPoints.get(stayPointIndex).getEndTime()) {
                    startIndex = curIndex;
                    stayPointIndex++;
                    if (stayPointIndex == stayPoints.size() && stayPointIndex < pts.size() - 1) {
                        Trajectory subTrajectory = new Trajectory(
                            trajectory.getTid() + "_stayPoint_" + result.size(),
                            trajectory.getOid(),
                            new ArrayList<>(pts.subList(startIndex, pts.size()))
                        );
                        result.add(subTrajectory);
                        break;
                    }
                }
                curIndex++;
            }
        }
        return result;
    }
}
