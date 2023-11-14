package org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.io.ParseException;

public class st_multiPointFromWKT extends ScalarFunction {
    @DataTypeHint(value = "RAW", bridgedTo = MultiPoint.class)
    public MultiPoint eval(@DataTypeHint("String") String wkt) throws ParseException {
        return (MultiPoint) util.fromWKT(wkt);
    }
}
