package org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.io.ParseException;

public class st_lineStringFromWKT extends ScalarFunction {
    @DataTypeHint(value = "RAW", bridgedTo = LineString.class)
    public LineString eval(@DataTypeHint("String") String wkt) throws ParseException {
        return (LineString) util.fromWKT(wkt);
    }
}
