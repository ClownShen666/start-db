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
package org.urbcomp.cupid.db.flink.algorithm.step.object;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author syy
 **/

public class GpsPoint {

    // Longitude coordinate (x)
    private double lng;
    // Latitude coordinate (y)
    private double lat;
    private String tid;
    // Timestamp
    private long ingestionTime;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public long getIngestionTime() {
        return ingestionTime;
    }

    public void setIngestionTime(long ingestionTime) {
        this.ingestionTime = ingestionTime;
    }

    public int getLonCol() {
        return lonCol;
    }

    public void setLonCol(int lonCol) {
        this.lonCol = lonCol;
    }

    public int getLatCol() {
        return latCol;
    }

    public void setLatCol(int latCol) {
        this.latCol = latCol;
    }

    public boolean isGridFlag() {
        return gridFlag;
    }

    public void setGridFlag(boolean gridFlag) {
        this.gridFlag = gridFlag;
    }

    private int lonCol;
    private int latCol;
    private boolean gridFlag = false;

    // Default constructor
    public GpsPoint() {}

    /**
     * Constructs a GpsPoint object with specified parameters.
     *
     * @param lng            Longitude coordinate
     * @param lat            Latitude coordinate
     * @param tid            Point identifier
     * @param ingestionTime  Ingestion time as a string
     * @param a              Additional time offset
     * @throws ParseException If parsing the timestamp fails
     */
    public GpsPoint(double lng, double lat, String tid, String ingestionTime, long a)
        throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.ingestionTime = formatter.parse(ingestionTime).getTime() + a;
        this.tid = tid;
        this.lng = lng;
        this.lat = lat;
    }

    /**
     * Constructs a GpsPoint object with specified parameters.
     *
     * @param lng            Longitude coordinate
     * @param lat            Latitude coordinate
     * @param tid            Point identifier
     * @param ingestionTime  Ingestion time as a timestamp
     * @param a              Additional time offset
     * @throws ParseException If parsing the timestamp fails
     */
    public GpsPoint(double lng, double lat, String tid, long ingestionTime, long a)
        throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.ingestionTime = ingestionTime + a;
        this.tid = tid;
        this.lng = lng;
        this.lat = lat;
    }

    /**
     * Returns a string representation of the GpsPoint object.
     *
     * @return String representation
     */
    @Override
    public String toString() {
        return "Point ID: "
            + tid
            + ", Timestamp: "
            + new Timestamp(ingestionTime)
            + ", Longitude: "
            + lng
            + ", Latitude: "
            + lat;
    }
}
