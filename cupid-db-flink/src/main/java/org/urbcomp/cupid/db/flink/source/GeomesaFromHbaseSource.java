/* 
 * Copyright (C) 2022  ST-Lab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.urbcomp.cupid.db.flink.source;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.geotools.data.*;
import org.geotools.data.Transaction;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.urbcomp.cupid.db.common.ConfigProvider;

import java.io.IOException;
import java.util.Map;

public class GeomesaFromHbaseSource implements SourceFunction<SimpleFeature> {

    FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;

    public GeomesaFromHbaseSource(String catalog, String sftName, String ecql) throws IOException,
        CQLException {
        Map<String, String> param = ConfigProvider.getGeomesaHbaseJavaParam(catalog);
        DataStore dataStore = DataStoreFinder.getDataStore(param);
        Query query = new Query(sftName, ECQL.toFilter(ecql));
        featureReader = dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT);
    }

    @Override
    public void run(SourceContext<SimpleFeature> sourceContext) throws Exception {
        while (featureReader.hasNext()) {
            sourceContext.collect(featureReader.next());
        }
    }

    @Override
    public void cancel() {
        try {
            featureReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
