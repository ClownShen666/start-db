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

import org.urbcomp.start.db.model.roadnetwork.RoadNode;

public class ReachableNode {

    private final RoadNode node;
    private final double timeCost;

    public ReachableNode(RoadNode node, double timeCost) {
        this.node = node;
        this.timeCost = timeCost;
    }

    public RoadNode getNode() {
        return this.node;
    }

    public double getCost() {
        return this.timeCost;
    }

}
