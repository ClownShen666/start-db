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
import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.sql.types.DataType;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.unsafe.types.UTF8String;
import org.urbcomp.cupid.db.datatype.DataConvertFactory;
import org.urbcomp.cupid.db.datatype.KryoHelper;
import java.io.ByteArrayOutputStream;
import java.util.Locale;

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
            System.out.println("deserializing ...");
            System.out.println(typeName.toLowerCase(Locale.ROOT));
            final Object o = KRYO.readObjectOrNull(
                input,
                DataConvertFactory.strTypeToClass(typeName.toLowerCase(Locale.ROOT))
            );
            row[i] = o;
        }
        return row;
    }

    public static byte[] serialize(InternalRow row, StructType schema) {
        System.out.println("serializing " + row);
        final Output output = new Output(new ByteArrayOutputStream());
        final int numFields = row.numFields();
        output.writeInt(numFields);
        final StructField[] fieldType = schema.fields();
        for (int i = 0; i < numFields; i++) {
            final DataType dataType = fieldType[i].dataType();
            output.writeString(dataType.typeName());
            Object data = row.get(i, dataType);
            if (data instanceof UTF8String) {
                data = data.toString();
            }
            System.out.println("serializing ...");
            System.out.println(dataType.typeName().toLowerCase(Locale.ROOT) + ": " + data);
            if (dataType.typeName().toLowerCase(Locale.ROOT).equals("timestamp")) {
                System.out.println("hit!");
                System.out.println(
                    ((java.sql.Timestamp) data).getTime()
                        + " "
                        + ((java.sql.Timestamp) data).toString()
                );
                System.out.println(
                    ((java.util.Date) data).getTime() + " " + ((java.util.Date) data).toString()
                );
                DefaultSerializers.DateSerializer ss = new DefaultSerializers.DateSerializer();
                Output temp = new Output(new ByteArrayOutputStream());
                ss.write(KRYO, temp, (java.util.Date) data);
                System.out.println(temp.toBytes());
                System.out.println("hit finished");
            }
            KRYO.writeObjectOrNull(
                output,
                data,
                DataConvertFactory.strTypeToClass(dataType.typeName().toLowerCase(Locale.ROOT))
            );
        }
        return output.toBytes();
    }
}
