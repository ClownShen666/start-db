package org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.io.ParseException;

public class st_multiPolygonFromWKT extends ScalarFunction {
    @DataTypeHint(value = "RAW", bridgedTo = MultiPolygon.class)
    public MultiPolygon eval(@DataTypeHint("String") String wkt) throws ParseException {
        return (MultiPolygon) util.fromWKT(wkt);
    }
}
