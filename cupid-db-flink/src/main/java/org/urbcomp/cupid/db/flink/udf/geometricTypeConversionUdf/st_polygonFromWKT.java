package org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

public class st_polygonFromWKT extends ScalarFunction {
    @DataTypeHint(value = "RAW", bridgedTo = Polygon.class)
    public Polygon eval(@DataTypeHint("String") String wkt) throws ParseException {
        if (wkt == null) {
            return null;
        }
        else {
            WKTReader wktReader = new WKTReader();
            return (Polygon) wktReader.read(wkt);
        }
    }
}
