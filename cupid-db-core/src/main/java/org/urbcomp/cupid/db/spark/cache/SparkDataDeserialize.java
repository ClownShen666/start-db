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
package org.urbcomp.cupid.db.spark.cache;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import org.urbcomp.cupid.db.datatype.KryoHelper;

/**
 * @author jimo
 **/
public class SparkDataDeserialize {

    private final static Kryo KRYO = KryoHelper.getKryo();

    public static Object[] deserialize(byte[] bytes) {
        final Input input = new Input(bytes);
        final int numFields = input.readInt();
        Object[] row = new Object[numFields];
        for (int i = 0; i < numFields; i++) {
            final Object o = KRYO.readObject(input, Object.class);
            row[i] = o;
        }
        return row;
    }
}
