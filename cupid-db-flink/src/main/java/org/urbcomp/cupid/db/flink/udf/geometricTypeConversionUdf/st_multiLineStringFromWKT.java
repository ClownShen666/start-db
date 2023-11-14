package org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.io.ParseException;

public class st_multiLineStringFromWKT extends ScalarFunction {
    @DataTypeHint(value = "RAW", bridgedTo = MultiLineString.class)
    public MultiLineString eval(@DataTypeHint("String") String wkt) throws ParseException {
        return (MultiLineString) util.fromWKT(wkt);
    }
}
