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
package org.urbcomp.start.db.algorithm.clustering;

import org.locationtech.jts.geom.MultiPoint;
import org.urbcomp.start.db.model.point.SpatialPoint;
import org.urbcomp.start.db.util.GeoFunctions;
import org.urbcomp.start.db.util.GeometryFactoryUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class KMeansClustering extends AbstractClustering {

    private final int k;

    public KMeansClustering(List<SpatialPoint> pointList, int k) {
        super(pointList);
        this.k = k;
    }

    private List<List<SpatialPoint>> doKMeans(List<SpatialPoint> points) {
        List<List<SpatialPoint>> clusters = new ArrayList<>(Math.min(k, points.size()));

        if (this.k > points.size()) {
            for (SpatialPoint point : points) {
                clusters.add(Collections.singletonList(point));
            }
            return clusters;
        }

        List<SpatialPoint> centerPoints = initCenterPoints(points);
        List<SpatialPoint> newCenterPoints;

        double err = Integer.MAX_VALUE;
        while (err > 0.00001 * k) {
            clusters.clear();
            for (int i = 0; i < centerPoints.size(); i++) {
                clusters.add(new ArrayList<>());
            }
            for (SpatialPoint point : points) {
                int index = findClusterIndex(point, centerPoints);
                clusters.get(index).add(point);
            }
            newCenterPoints = getNewCenterPoints(clusters);
            err = getError(centerPoints, newCenterPoints);
            centerPoints = newCenterPoints;
        }

        return clusters;
    }

    private int findClusterIndex(SpatialPoint point, List<SpatialPoint> centerPoints) {
        int index = 0;
        double tmpMinDistance = Double.MAX_VALUE;
        for (int i = 0; i < centerPoints.size(); i++) {
            double distance = GeoFunctions.getDistanceInM(point, centerPoints.get(i));
            if (distance < tmpMinDistance) {
                tmpMinDistance = distance;
                index = i;
            }
        }
        return index;
    }

    private List<SpatialPoint> initCenterPoints(List<SpatialPoint> points) {
        List<SpatialPoint> res = new ArrayList<>();
        int interval = points.size() / k;
        for (int i = 0; i < points.size() && res.size() < k; i += interval) {
            res.add(points.get(i));
        }
        return res;
    }

    private List<SpatialPoint> getNewCenterPoints(List<List<SpatialPoint>> clusters) {
        List<SpatialPoint> centerPoints = new ArrayList<>(clusters.size());
        for (List<SpatialPoint> cluster : clusters) {
            OptionalDouble cLat = cluster.stream().mapToDouble(SpatialPoint::getLat).average();
            OptionalDouble cLng = cluster.stream().mapToDouble(SpatialPoint::getLng).average();
            centerPoints.add(new SpatialPoint(cLng.orElse(0), cLat.orElse(0)));
        }
        return centerPoints;
    }

    private double getError(List<SpatialPoint> centerPoints, List<SpatialPoint> newCenterPoints) {
        double error = 0;
        for (int i = 0; i < centerPoints.size(); i++) {
            error += GeoFunctions.getDistanceInM(centerPoints.get(i), newCenterPoints.get(i));
        }
        return error;
    }

    private MultiPoint getMultiPoint(List<SpatialPoint> points) {
        SpatialPoint[] arr = new SpatialPoint[points.size()];
        return new MultiPoint(points.toArray(arr), GeometryFactoryUtils.defaultGeometryFactory());
    }

    @Override
    public List<MultiPoint> cluster() {
        List<List<SpatialPoint>> clusters = doKMeans(pointList);
        return clusters.stream().map(this::getMultiPoint).collect(Collectors.toList());
    }
}
