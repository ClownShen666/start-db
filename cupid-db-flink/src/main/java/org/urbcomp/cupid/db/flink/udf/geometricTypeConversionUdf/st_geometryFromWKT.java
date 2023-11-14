package org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;

public class st_geometryFromWKT extends ScalarFunction {
    @DataTypeHint(value = "RAW", bridgedTo = Geometry.class)
    public Geometry eval(@DataTypeHint("String") String wkt) throws ParseException {
        return util.fromWKT(wkt);
    }
}
