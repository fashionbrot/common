package com.github.fashionbrot.common.date;

import com.github.fashionbrot.common.consts.DateConst;
import com.github.fashionbrot.common.util.ObjectUtil;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class LocalDateTimeUtil {


    /**
     * 将日期时间字符串转换为 LocalDateTime 对象。
     *
     * @param dateTimeString 要转换的日期时间字符串，支持格式 "yyyy-MM-dd HH:mm:ss"
     * @return 转换后的 LocalDateTime 对象，转换失败或输入为空时返回 null
     */
    public static LocalDateTime toLocalDateTime(String dateTimeString) {
        if (ObjectUtil.isNotEmpty(dateTimeString)) {
            try {
                return LocalDateTime.parse(dateTimeString, DateConst.DATE_TIME_FORMAT);
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 将 LocalDateTime 对象转换为日期时间字符串。
     *
     * @param localDateTime 要转换的 LocalDateTime 对象
     * @return 转换后的日期时间字符串，转换失败或输入为空时返回 null
     */
    public static String toString(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            try {
                return localDateTime.format(DateConst.DATE_TIME_FORMAT);
            } catch (DateTimeException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 将日期字符串转换为日期对象。
     *
     * @param dateString 要转换的日期字符串，格式为 "yyyy-MM-dd HH:mm:ss"
     * @return 转换后的日期对象
     */
    public static Date parseDateTime(String dateString)  {
        return parseDateTime(dateString,DateConst.DEFAULT_ZONE_ID);
    }




    /**
     * 将日期字符串转换为日期对象。
     *
     * @param dateString 要转换的日期字符串   "yyyy-MM-dd HH:mm:ss"
     * @param zoneId     转换时使用的时区
     * @return 转换后的日期对象，转换失败时返回 null
     */
    public static Date parseDateTime(String dateString , ZoneId zoneId) {
        if (dateString == null  || zoneId == null) {
            return null;
        }
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateString, DateConst.DATE_TIME_FORMAT);
            Instant instant = localDateTime.atZone(zoneId).toInstant();
            return Date.from(instant);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将日期对象转换为本地日期时间对象，使用默认时区（系统默认时区）。
     *
     * @param date 要转换的日期对象
     * @return 转换后的本地日期时间对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return toLocalDateTime(date, DateConst.DEFAULT_ZONE_ID);
    }

    /**
     * 将 java.util.Date 转换为 java.time.LocalDateTime，使用指定时区。
     *
     * @param date    要转换的 java.util.Date 对象
     * @param zoneId  指定的时区
     * @return 转换后的 java.time.LocalDateTime 对象，转换失败时返回 null
     */
    public static LocalDateTime toLocalDateTime(Date date, ZoneId zoneId) {
        if (date == null) {
            return null;
        }
        if (zoneId == null) {
            return null;
        }
        Instant instant = date.toInstant();
        try {
            return instant.atZone(zoneId).toLocalDateTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将 java.time.LocalDateTime 转换为 java.util.Date，使用默认时区。
     *
     * @param localDateTime 要转换的 java.time.LocalDateTime 对象
     * @return 转换后的 java.util.Date 对象，转换失败时返回 null
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return toDate(localDateTime,DateConst.DEFAULT_ZONE_ID);
    }


    /**
     * 将 java.time.LocalDateTime 转换为 java.util.Date，使用指定时区。
     *
     * @param localDateTime 要转换的 java.time.LocalDateTime 对象
     * @param zoneId        指定的时区
     * @return 转换后的 java.util.Date 对象，转换失败时返回 null
     */
    public static Date toDate(LocalDateTime localDateTime, ZoneId zoneId) {
        if (localDateTime == null || zoneId == null) {
            return null;
        }
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        try {
            return Date.from(instant);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 将 java.util.Date 转换为 "yyyy-MM-dd HH:mm:ss" 格式的字符串。
     *
     * @param dateTime 要转换的日期时间
     * @return 格式化后的日期时间字符串
     */
    public static String dateTimeToString(Date dateTime){
        return toString(toLocalDateTime(dateTime));
    }

}
