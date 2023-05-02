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
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.WeekFields;

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
     * get week value of the year
     * @param dtString datetime string
     * @return week of the year
     * @throws DateTimeException parse exception
     */
    @CupidDBFunction("week")
    public int week(String dtString) throws DateTimeException {
        WeekFields weekFields = WeekFields.ISO;
        return toDateTime(dtString).get(weekFields.weekOfYear());
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
     * get year value
     * @param dtString datetime string
     * @return year value
     * @throws DateTimeException parse exception
     */
    @CupidDBFunction("year")
    public int year(String dtString) throws DateTimeException {
        return toDateTime(dtString).getYear();
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
