package org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;

import java.io.IOException;

public class st_multiPointAsWKT extends ScalarFunction {
    @DataTypeHint("String")
    public String eval(@DataTypeHint(value = "RAW", bridgedTo = MultiPoint.class) Geometry geometry) throws IOException {
        return util.asWKT(geometry);
    }
}
