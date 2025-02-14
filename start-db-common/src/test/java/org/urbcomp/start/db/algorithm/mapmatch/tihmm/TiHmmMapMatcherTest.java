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
package org.urbcomp.start.db.algorithm.mapmatch.tihmm;

import org.junit.Before;
import org.junit.Test;
import org.urbcomp.start.db.algorithm.mapmatch.routerecover.ShortestPathPathRecover;
import org.urbcomp.start.db.algorithm.shortestpath.BiDijkstraShortestPath;
import org.urbcomp.start.db.algorithm.shortestpath.ManyToManyShortestPath;
import org.urbcomp.start.db.exception.AlgorithmExecuteException;
import org.urbcomp.start.db.model.roadnetwork.RoadNetwork;
import org.urbcomp.start.db.model.sample.ModelGenerator;
import org.urbcomp.start.db.model.trajectory.MapMatchedTrajectory;
import org.urbcomp.start.db.model.trajectory.PathOfTrajectory;
import org.urbcomp.start.db.model.trajectory.Trajectory;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TiHmmMapMatcherTest {

    private Trajectory trajectory;
    private TiHmmMapMatcher mapMatcher;
    private ShortestPathPathRecover recover;

    @Before
    public void setUp() {
        trajectory = ModelGenerator.generateTrajectory();
        RoadNetwork roadNetwork = ModelGenerator.generateRoadNetwork();
        mapMatcher = new TiHmmMapMatcher(roadNetwork, new ManyToManyShortestPath(roadNetwork));
        recover = new ShortestPathPathRecover(roadNetwork, new BiDijkstraShortestPath(roadNetwork));
    }

    @Test
    public void matchTrajToMapMatchedTraj() throws AlgorithmExecuteException {
        MapMatchedTrajectory mmTrajectory = mapMatcher.mapMatch(trajectory);
        assertEquals(trajectory.getGPSPointList().size(), mmTrajectory.getMmPtList().size());
        List<PathOfTrajectory> pTrajectories = recover.recover(mmTrajectory);
        assertEquals(1, pTrajectories.size());
    }
}
