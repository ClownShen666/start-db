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
import com.esotericsoftware.kryo.io.Output;
import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.urbcomp.cupid.db.datatype.DataConvertFactory;
import org.urbcomp.cupid.db.datatype.KryoHelper;

import java.io.ByteArrayOutputStream;

/**
 * @author jimo
 **/
public class SparkDataSerializer {

    private final static Kryo KRYO = KryoHelper.getKryo();

    public static Object[] deserialize(byte[] bytes) {
        final Input input = new Input(bytes);
        final int numFields = input.readInt();
        Object[] row = new Object[numFields];
        for (int i = 0; i < numFields; i++) {
            final String typeName = input.readString();
            final Object o = KRYO.readObjectOrNull(
                input,
                DataConvertFactory.strTypeToClass(typeName)
            );
            row[i] = o;
        }
        return row;
    }

    public static byte[] serialize(InternalRow row, StructType schema) {
        final Output output = new Output(new ByteArrayOutputStream());
        final int numFields = row.numFields();
        output.writeInt(numFields);
        final StructField[] fieldType = schema.fields();
        for (int i = 0; i < numFields; i++) {
            final DataType dataType = fieldType[i].dataType();
            output.writeString(dataType.typeName());
            KRYO.writeObjectOrNull(
                output,
                row.get(i, dataType),
                DataConvertFactory.strTypeToClass(dataType.typeName())
            );
        }
        return output.toBytes();
    }
}
