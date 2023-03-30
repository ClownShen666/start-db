/*
 * This file is inherited from Apache Calcite and modifed by ST-Lab under apache license.
 * You can find the original code from
 *
 * https://github.com/apache/calcite
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.urbcomp.cupid.db.function;

/**
 * @author zaiyuan, XiangHe
 * @date 2022-05-26 23:12:07
 */
public class StringFunction {

    @CupidDBFunction("lpad")
    public String lpad(String str, int len, String pad) {
        String res = pad(str, len, pad);
        if (res == null) return null;
        if (res.length() == len) return res;
        return res + str;
    }

    @CupidDBFunction("rpad")
    public String rpad(String str, int len, String pad) {
        String res = pad(str, len, pad);
        if (res == null) return null;
        if (res.length() == len) return res;
        return str + pad(str, len, pad);
    }

    @CupidDBFunction("locate")
    public Object locate(String substr, String str, int pos) {
        if (substr == null || str == null) return null;
        return str.indexOf(substr, pos) + 1;
    }

    private String pad(String str, int len, String pad) {
        if (str == null || pad == null) return null;
        if (len <= str.length()) return str.substring(0, len);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len - str.length(); ++i) {
            sb.append(pad);
        }
        return sb.toString();
    }
}
