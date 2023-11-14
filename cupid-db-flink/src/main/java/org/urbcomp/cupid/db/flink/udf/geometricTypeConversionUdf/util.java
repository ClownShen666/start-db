package org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

import java.io.IOException;
import java.io.StringWriter;

public class util {
    public static Geometry fromWKT(String wkt) throws ParseException {
        if (wkt == null) {
            return null;
        }
        else {
            WKTReader wktReader = new WKTReader();
            return wktReader.read(wkt);
        }
    }

    public static String asWKT(Geometry geometry) throws IOException {
        if (geometry == null) {
            return null;
        }
        else {
            WKTWriter wktWriter = new WKTWriter();
            StringWriter writer = new StringWriter();
            wktWriter.write(geometry, writer);
            return writer.toString();
        }
    }
}
