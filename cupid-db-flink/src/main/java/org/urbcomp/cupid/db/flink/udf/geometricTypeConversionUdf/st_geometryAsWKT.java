package org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;

import java.io.IOException;

public class st_geometryAsWKT extends ScalarFunction {
    @DataTypeHint("String")
    public String eval(@DataTypeHint(value = "RAW", bridgedTo = Geometry.class) Geometry geometry) throws IOException {
        return util.asWKT(geometry);
    }
}
