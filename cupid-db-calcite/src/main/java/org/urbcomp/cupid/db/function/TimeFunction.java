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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Time UDF functions
 *
 * @author Wang Bohong
 * @Date 2022-05-29
 */
public class TimeFunction {

    /**
     * default time format, the order is important
     */
    private final String[] DEFAULT_FORMATS = new String[] {
        "yyyy-MM-dd HH:mm:ss.SSS",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd" };

    /**
     * Converts a String to Datetime as the given format
     * @param dateString date(time) String
     * @param format date format
     * @return datetime datetime instance
     * @throws DateTimeException parse exception
     */
    @CupidDBFunction("toDatetime")
    public LocalDateTime toDatetime(String dateString, String format) throws DateTimeException {
        if (dateString.length() == 10) {
            dateString += " 00:00:00";
            format += " HH:mm:ss";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format.trim());
        return LocalDateTime.parse(dateString, formatter);
    }

    /**
     * Converts a String to Datetime
     * @param dateString date(time) String
     * @return Datetime instance
     * @throws DateTimeParseException parse exception
     */
    @CupidDBFunction("toDatetime")
    public LocalDateTime toDateTime(String dateString) throws DateTimeParseException {
        if (dateString == null) return null;
        LocalDateTime localDateTime = LocalDateTime.MIN;
        boolean isCorrect = false;
        DateTimeParseException pe = null;
        if (dateString.length() == 10) {
            dateString += " 00:00:00";
        }
        for (String format : DEFAULT_FORMATS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                localDateTime = LocalDateTime.parse(dateString, formatter);
                isCorrect = true;
                break;
            } catch (DateTimeParseException exception) {
                pe = exception;
            }
        }
        if (!isCorrect && pe != null) {
            throw new DateTimeParseException(
                "Date format is error. Only receive " + String.join(",", DEFAULT_FORMATS),
                dateString,
                pe.getErrorIndex()
            );
        }
        return localDateTime;
    }

    /**
     * Convert datetime to timestamp
     * @param timestamp Timestamp instance
     * @return datetime instance
     */
    @CupidDBFunction("timestampToDatetime")
    public LocalDateTime timestampToDatetime(Timestamp timestamp) {
        String tsString = timestamp.toString();
        if (tsString.length() != 23) {
            tsString = tsString.substring(0, tsString.length() - 2);
        }
        return toDateTime(tsString);
    }

    /**
     * Convert timestamp to datetime
     * @param tsString Timestamp String
     * @return datetime datetime instance
     * @throws ParseException parse exception
     */
    @CupidDBFunction("timestampToDatetime")
    public LocalDateTime timestampToDatetime(String tsString) throws ParseException {
        Timestamp timestamp = toTimestamp(tsString);
        return timestampToDatetime(timestamp);
    }

    /**
     * Converts a date string to a timestamp
     *
     * @param dateString date(time) String
     * @return timestamp
     * @throws ParseException parse exception
     */
    @CupidDBFunction("toTimestamp")
    public Timestamp toTimestamp(String dateString) throws ParseException {
        long time = 0;
        boolean isCorrect = false;
        ParseException pe = null;
        for (String format : DEFAULT_FORMATS) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                Date date = simpleDateFormat.parse(dateString);
                time = date.getTime();
                isCorrect = true;
                break;
            } catch (ParseException ex) {
                pe = ex;
            }
        }
        if (!isCorrect && pe != null) {
            throw new ParseException(
                "Date format is error. Only receive " + String.join(",", DEFAULT_FORMATS),
                pe.getErrorOffset()
            );
        }
        return new Timestamp(time);
    }

    /**
     * get current datetime
     * @return datetime instance
     */
    @CupidDBFunction("currentDatetime")
    public LocalDateTime currentDatetime() {
        return LocalDateTime.now();
    }

    /**
     * Formats one datetime instance into the specified format
     * @param dt datetime instance
     * @param format format string
     * @return datetime string
     */
    @CupidDBFunction("datetimeFormat")
    public String datetimeFormat(LocalDateTime dt, String format) throws DateTimeException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format.trim());
        return dateTimeFormatter.format(dt);
    }

    /**
     * get hour value of datetime
     * @param localDateTime datetime
     * @return hour value
     */
    @CupidDBFunction("hour")
    public int hour(LocalDateTime localDateTime) {
        return localDateTime.getHour();
    }

    /**
     * get minute value of datetime
     * @param dtString datetime string
     * @return minute value
     * @throws DateTimeException parse exception
     */
    @CupidDBFunction("minute")
    public int minute(String dtString) throws DateTimeException {
        return toDateTime(dtString).getMinute();
    }

    /**
     * get second value of datetime
     * @param dtString datetime string
     * @return second value
     * @throws DateTimeException parse exception
     */
    @CupidDBFunction("second")
    public int second(String dtString) throws DateTimeException {
        return toDateTime(dtString).getSecond();
    }

    /**
     * get month of the year
     * @param dtString datetime instance
     * @return month of the year
     * @throws DateTimeException parse exception
     */
    @CupidDBFunction("month")
    public int month(String dtString) throws DateTimeException {
        return toDateTime(dtString).getMonth().getValue();
    }

    /**
     * get day of month
     * @param dtString datetime string
     * @return day of month
     * @throws DateTimeException parse exception
     */
    @CupidDBFunction("dayOfMonth")
    public int dayOfMonth(String dtString) throws DateTimeException {
        return toDateTime(dtString).getDayOfMonth();
    }

    /**
     * get day of week
     * @param dtString datetime string
     * @return day of week
     * @throws DateTimeException parse exception
     */
    @CupidDBFunction("dayOfWeek")
    public int dayOfWeek(String dtString) throws DateTimeException {
        return toDateTime(dtString).getDayOfWeek().getValue();
    }

    /**
     * get day of year
     * @param dtString datetime string
     * @return day of year
     * @throws DateTimeException parse Exception
     */
    @CupidDBFunction("dayOfYear")
    public int dayOfYear(String dtString) throws DateTimeException {
        return toDateTime(dtString).getDayOfYear();
    }

    @CupidDBFunction("now")
    public String now() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMATS[1]);
        return LocalDateTime.now().format(formatter);
    }
}
