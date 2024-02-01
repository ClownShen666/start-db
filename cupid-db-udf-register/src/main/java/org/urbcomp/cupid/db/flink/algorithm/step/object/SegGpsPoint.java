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

import org.urbcomp.cupid.db.model.Attribute;
import org.urbcomp.cupid.db.model.point.GPSPoint;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

/**
 * @author syy
 **/

public class SegGpsPoint extends GPSPoint implements Serializable {

    private String tid;

    public SegGpsPoint(Timestamp time, double lng, double lat, String tid) {
        super(time, lng, lat);
        this.tid = tid;
    }

    public SegGpsPoint(Timestamp time, double lng, double lat, Map<String, Attribute> attributes) {
        super(time, lng, lat, attributes);

    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
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

}
