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
package org.urbcomp.start.db.flink.source;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.urbcomp.start.db.flink.algorithm.step.object.SegGpsPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Timestamp;
import java.text.ParseException;

public class GPSSource implements SourceFunction<Tuple2<String, SegGpsPoint>> {

    @Override
    public void run(SourceContext<Tuple2<String, SegGpsPoint>> sourceContext) throws Exception {
        for (int i = 1; i <= 500; i++) {
            BufferedReader reader = new BufferedReader(
                new FileReader("src/main/resources/batch-taxi-1000/taxi-1000-" + i + ".txt")
            );
            String line;
            while ((line = reader.readLine()) != null) {
                SegGpsPoint gpsPoint = mapFunction(line);
                sourceContext.collect(Tuple2.of(gpsPoint.getTid(), gpsPoint));
            }
        }
    }

    @Override
    public void cancel() {}

    private SegGpsPoint mapFunction(String line) throws ParseException {
        String[] result = line.replace("'", "")
            .replace("[", "")
            .replace("]", "")
            .replace(" ", "")
            .split(",");
        String t1 = result[0];
        String t2 = result[1];
        String lng = result[4];
        String lat = result[5];
        String tid = result[3];

        String time = t1.substring(0, 4)
            + "-"
            + t1.substring(4, 6)
            + "-"
            + t1.substring(6, 8)
            + " "
            + t2.substring(0, 2)
            + ":"
            + t2.substring(2, 4)
            + ":"
            + t2.substring(4, 6);
        return new SegGpsPoint(
            Timestamp.valueOf(time),
            Double.parseDouble(lng),
            Double.parseDouble(lat),
            tid
        );
    }

}
