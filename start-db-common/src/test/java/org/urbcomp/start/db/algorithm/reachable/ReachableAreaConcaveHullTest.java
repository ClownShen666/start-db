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
package org.urbcomp.start.db.algorithm.reachable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.urbcomp.start.db.model.point.CandidatePoint;
import org.urbcomp.start.db.model.point.SpatialPoint;
import org.urbcomp.start.db.model.roadnetwork.RoadNetwork;
import org.urbcomp.start.db.model.roadnetwork.RoadNode;
import org.urbcomp.start.db.model.sample.ModelGenerator;
import org.urbcomp.start.db.util.GeoFunctions;

public class ReachableAreaConcaveHullTest {
    private static ReachableAreaConcaveHull concaveHull;
    private RoadNetwork roadNetwork;
    private SpatialPoint startPt;
    private double timeBudgetInS;

    @Before
    public void setup() {
        timeBudgetInS = 3600.0;
        startPt = new SpatialPoint(108.98897, 34.25815);
        this.roadNetwork = ModelGenerator.generateRoadNetwork();
        concaveHull = new ReachableAreaConcaveHull(roadNetwork, startPt, timeBudgetInS, "Walk");
    }

    @Test
    public void reachableAreaTest() {

        Polygon hull = concaveHull.getHull();
        System.out.println(hull);
        CandidatePoint startCandidatePoint = CandidatePoint.getNearestCandidatePoint(
            startPt,
            roadNetwork,
            100
        );

        assert startCandidatePoint != null;
        RoadNode startNode = roadNetwork.getRoadSegmentById(startCandidatePoint.getRoadSegmentId())
            .getStartNode();
        SpatialPoint startNodePoint = new SpatialPoint(startNode.getLng(), startNode.getLat());

        for (Coordinate coordinate : hull.getCoordinates()) {
            SpatialPoint endPt = new SpatialPoint(coordinate.y, coordinate.x);
            double dis = GeoFunctions.getDistanceInM(startNodePoint, endPt);
            Assert.assertTrue(dis < AbstractReachableArea.walkSpeedInMeterPerSec * timeBudgetInS);
        }
    }
}
