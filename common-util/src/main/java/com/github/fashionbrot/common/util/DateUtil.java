package com.github.fashionbrot.common.util;

import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
@Slf4j
public class DateUtil {

    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * 默认时区
     */
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();


    /**
     * 将时区字符串转换为 ZoneId 对象。
     *
     * @param zoneIdString 要转换的时区字符串
     * @return 转换后的 ZoneId 对象，转换失败时返回默认时区
     */
    public static ZoneId parseZoneId(String zoneIdString) {
        try {
            return ZoneId.of(zoneIdString);
        } catch (DateTimeException e) {
            log.error("Parsing ZoneId failed. Using default ZoneId: {}", DEFAULT_ZONE_ID);
            return DEFAULT_ZONE_ID;
        }
    }

    /**
     * 将 LocalDate 对象转换为 Date 对象，使用默认时区。
     *
     * @param localDate 要转换的 LocalDate 对象
     * @return 转换后的 Date 对象，转换失败或输入为空时返回 null
     */
    public static Date toDate(LocalDate localDate) {
        return toDate(localDate,DEFAULT_ZONE_ID);
    }

    /**
     * 将 LocalDate 对象转换为 Date 对象，使用指定时区。
     *
     * @param localDate 要转换的 LocalDate 对象
     * @param zoneId    指定的时区
     * @return 转换后的 Date 对象，转换失败或输入为空时返回 null
     */
    public static Date toDate(LocalDate localDate, ZoneId zoneId) {
        if (localDate == null) {
            return null;
        }
        try {
            Instant instant = localDate.atStartOfDay(zoneId).toInstant();
            return Date.from(instant);
        } catch (Exception e) {
            log.error("Converting LocalDate to Date failed: {}", e.getMessage());
            return null;
        }
    }


    /**
     * 将日期字符串转换为 LocalDate 对象。
     *
     * @param dateString 要转换的日期字符串，格式为 "yyyy-MM-dd"
     * @return 转换后的 LocalDate 对象，转换失败或输入为空时返回 null
     */
    public static LocalDate toLocalDate(String dateString) {
        if (ObjectUtil.isNotEmpty(dateString)) {
            try {
                return LocalDate.parse(dateString, DATE_FORMAT);
            } catch ( DateTimeParseException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 将 java.util.Date 转换为 java.time.LocalDate 对象，使用默认时区。
     *
     * @param date 要转换的日期
     * @return 转换后的 LocalDate 对象
     */
    public static LocalDate toLocalDate(Date date) {
        return toLocalDate(date,DEFAULT_ZONE_ID);
    }

    /**
     * 将 java.util.Date 转换为 java.time.LocalDate 对象，使用指定的时区。
     *
     * @param date   要转换的日期
     * @param zoneId 日期的时区
     * @return 转换后的 LocalDate 对象
     */
    public static LocalDate toLocalDate(Date date,ZoneId zoneId) {
        if (date!=null) {
            try {
                Instant instant = date.toInstant();
                return instant.atZone(zoneId).toLocalDate();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 将 LocalDate 对象转换为日期字符串。
     *
     * @param localDate 要转换的 LocalDate 对象
     * @return 转换后的日期字符串，转换失败或输入为空时返回 null
     */
    public static String toString(LocalDate localDate) {
        if (localDate != null) {
            try {
                return localDate.format(DATE_FORMAT);
            } catch (DateTimeException  e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 将日期时间字符串转换为 LocalDateTime 对象。
     *
     * @param dateTimeString 要转换的日期时间字符串，支持格式 "yyyy-MM-dd HH:mm:ss"
     * @return 转换后的 LocalDateTime 对象，转换失败或输入为空时返回 null
     */
    public static LocalDateTime toLocalDateTime(String dateTimeString) {
        if (ObjectUtil.isNotEmpty(dateTimeString)) {
            try {
                return LocalDateTime.parse(dateTimeString, DATE_TIME_FORMAT);
            } catch (DateTimeParseException  e) {
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
                return localDateTime.format(DATE_TIME_FORMAT);
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
        return parseDateTime(dateString,DEFAULT_ZONE_ID);
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
            LocalDateTime localDateTime = LocalDateTime.parse(dateString, DATE_TIME_FORMAT);
            Instant instant = localDateTime.atZone(zoneId).toInstant();
            return Date.from(instant);
        } catch (Exception e) {
            log.error("Parsing or conversion failed due to an exception: {}", e.getMessage());
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
        return toLocalDateTime(date,DEFAULT_ZONE_ID);
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
            log.error("Conversion failed due to an exception: {}", e.getMessage());
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
        return toDate(localDateTime,DEFAULT_ZONE_ID);
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
            log.error("Conversion failed due to an exception: {}", e.getMessage());
            return null;
        }
    }


    /**
     * 将时间字符串转换为 LocalTime 对象。
     *
     * @param timeString 要转换的时间字符串，支持格式 "HH:mm:ss"
     * @return 转换后的 LocalTime 对象，转换失败或输入为空时返回 null
     */
    public static LocalTime toLocalTime(String timeString) {
        if (ObjectUtil.isNotEmpty(timeString)) {
            try {
                return LocalTime.parse(timeString, TIME_FORMAT);
            } catch (DateTimeParseException  e) {
                return null;
            }
        }
        return null;
    }


    /**
     * 将 LocalTime 对象转换为时间字符串。
     *
     * @param localTime 要转换的 LocalTime 对象
     * @return 转换后的时间字符串，转换失败或输入为空时返回 null
     */
    public static String toString(LocalTime localTime) {
        if (localTime != null) {
            try {
                return localTime.format(TIME_FORMAT);
            } catch (DateTimeException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 将 Date 对象转换为 LocalTime 对象。
     *
     * @param date 要转换的 Date 对象
     * @return 转换后的 LocalTime 对象，转换失败或输入为空时返回 null
     */
    public static LocalTime toLocalTime(Date date) {
        return toLocalTime(date,DEFAULT_ZONE_ID);
    }

    /**
     * 将 Date 对象转换为 LocalTime 对象。
     *
     * @param date 要转换的 Date 对象
     * @param zoneId 用于确定日期的时区
     * @return 转换后的 LocalTime 对象，转换失败或输入为空时返回 null
     */
    public static LocalTime toLocalTime(Date date, ZoneId zoneId) {
        if (date != null && zoneId!=null) {
            try {
                return date.toInstant().atZone(zoneId).toLocalTime();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }


    /**
     * 将 LocalTime 对象转换为 Date 对象。
     *
     * @param localTime 要转换的 LocalTime 对象
     * @return 转换后的 Date 对象，转换失败或输入为空时返回 null
     */
    public static Date toDate(LocalTime localTime) {
        return toDate(localTime,DEFAULT_ZONE_ID);
    }
    /**
     * 将 LocalTime 对象转换为 Date 对象。
     *
     * @param localTime 要转换的 LocalTime 对象
     * @param zoneId 用于确定日期的时区
     * @return 转换后的 Date 对象，转换失败或输入为空时返回 null
     */
    public static Date toDate(LocalTime localTime, ZoneId zoneId) {
        if (localTime != null && zoneId!=null) {
            try {
                return Date.from(localTime.atDate(LocalDate.now()).atZone(zoneId).toInstant());
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 在当前日期上增加指定的天数。
     *
     * @param days 要增加的天数（可以为负数表示减少天数）
     * @return 增加指定天数后的日期
     */
    public static Date addDays(int days) {
        return addDays(new Date(),days);
    }

    /**
     * 在给定的日期上增加指定的天数。
     *
     * @param date 要增加天数的日期
     * @param days 要增加的天数（可以为负数表示减少天数）
     * @return 增加指定天数后的日期
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }


    /**
     * 在给定的日期上增加指定的月数。
     *
     * @param date  要增加月数的日期
     * @param months 要增加的月数（可以为负数表示减少月数）
     * @return 增加指定月数后的日期
     */
    public static Date addMonths(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }

    /**
     * 在当前日期上增加指定的月数。
     *
     * @param months 要增加的月数（可以为负数表示减少月数）
     * @return 增加指定月数后的日期
     */
    public static Date addMonths(int months) {
        return addMonths(new Date(), months);
    }

    /**
     * 在给定的日期上增加指定的年数。
     *
     * @param date  要增加年数的日期
     * @param years 要增加的年数（可以为负数表示减少年数）
     * @return 增加指定年数后的日期
     */
    public static Date addYears(Date date, int years) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, years);
        return calendar.getTime();
    }

    /**
     * 在当前日期上增加指定的年数。
     *
     * @param years 要增加的年数（可以为负数表示减少年数）
     * @return 增加指定年数后的日期
     */
    public static Date addYears(int years) {
        return addYears(new Date(), years);
    }


    /**
     * 获取当前日期，并将其转换为 Date 对象。
     *
     * @return 当前日期的 Date 对象表示
     */
    public static Date nowDate() {
        return toDate(LocalDate.now());
    }

    /**
     * 将日期字符串转换为日期对象，使用系统默认时区。
     *
     * @param dateString 要转换的日期字符串
     * @return 转换后的日期对象
     */
    public static Date parseDate(String dateString){
        return parseDate(dateString,DEFAULT_ZONE_ID);
    }

    /**
     * 将日期字符串转换为日期对象。
     *
     * @param dateString 要转换的日期字符串
     * @param zoneId     日期的时区
     * @return 转换后的日期对象
     */
    public static Date parseDate(String dateString,ZoneId zoneId){
        if (ObjectUtil.isEmpty(dateString)  || zoneId == null) {
            return null;
        }
        try {
            LocalDate localDate = LocalDate.parse(dateString, DATE_FORMAT);
            Instant instant = localDate.atStartOfDay(zoneId).toInstant();
            return Date.from(instant);
        } catch (Exception e) {
            log.error("Parsing or conversion failed due to an exception: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 将 Date 转换为 LocalDate 对象，然后将其转换为 "yyyy-MM-dd" 格式的字符串。
     *
     * @param date 要转换的日期
     * @return 格式化后的日期字符串
     */
    public static String dateToString(Date date){
        return toString(toLocalDate(date));
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

    /**
     * 将 java.util.Date 转换为 "HH:mm:ss" 格式的字符串。
     *
     * @param time 要转换的时间
     * @return 格式化后的时间字符串
     */
    public static String timeToString(Date time){
        return toString(toLocalTime(time));
    }

}
