package org.urbcomp.cupid.db.flink.udf.geometricTypeConversionUdf;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;

import java.io.IOException;

public class st_lineStringAsWKT extends ScalarFunction {
    @DataTypeHint("String")
    public String eval(@DataTypeHint(value = "RAW", bridgedTo = LineString.class) Geometry geometry) throws IOException {
        return util.asWKT(geometry);
    }
}
