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
package org.urbcomp.cupid.db.flink.udf.statefuludf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.urbcomp.cupid.db.flink.algorithm.step.local.LocalProcessFunctionGrid;
import org.urbcomp.cupid.db.flink.algorithm.step.object.PointList;
import org.urbcomp.cupid.db.flink.algorithm.step.object.Result;
import org.urbcomp.cupid.db.flink.algorithm.step.object.SegGpsPoint;
import org.urbcomp.cupid.db.flink.udf.StatefulUdf;
import org.urbcomp.cupid.db.model.trajectory.Trajectory;

import java.util.ArrayList;
import java.util.List;

@FunctionHint(output = @DataTypeHint(value = "RAW", bridgedTo = ArrayList.class))
public class TrajectorySegment extends StatefulUdf<List<Trajectory>, Result> {

    private static final long serialVersionUID = 1L;

    LocalProcessFunctionGrid STEP_GRID = new LocalProcessFunctionGrid(50, 300000, 1);

    @Override
    public Result createAccumulator() {
        return new Result();
    }

    @Override
    public List<Trajectory> getValue(
        @DataTypeHint(value = "RAW", bridgedTo = Result.class) Result pointLists
    ) {
        if (pointLists.pointLists != null) {
            List<PointList> result = new ArrayList<>(pointLists.pointLists);
            List<Trajectory> res = new ArrayList<>();
            result.forEach(pointList -> {
                // * @param tid the id of Trajectory, should be unique in a trajectory database
                // * @param oid the object id of a trajectory, such as plate number
                Trajectory tmp = new Trajectory(pointList.getPointList().get(0).getTid(), "");
                pointList.getPointList().forEach(tmp::addGPSPoint);
                res.add(tmp);
            });
            pointLists.pointLists.clear();
            return res;
        }
        return null;
    }

    public void accumulate(
        @DataTypeHint(value = "RAW", bridgedTo = Result.class) Result acc,
        @DataTypeHint(value = "RAW", bridgedTo = SegGpsPoint.class) SegGpsPoint point
    ) {
        STEP_GRID.process(point, acc.pointLists);
    }

    @Override
    public String name() {
        return "stream_st_traj_stayPointSegment";
    }
}
