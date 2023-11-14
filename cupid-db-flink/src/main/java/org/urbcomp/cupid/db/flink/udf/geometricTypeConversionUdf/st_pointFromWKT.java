package org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;

public class st_pointFromWKT extends ScalarFunction {
        @DataTypeHint(value = "RAW", bridgedTo = Point.class)
        public Point eval(@DataTypeHint("String") String wkt) throws ParseException {
                return (Point) util.fromWKT(wkt);
        }
}
