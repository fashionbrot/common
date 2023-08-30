package com.github.fashionbrot.common.date;

import com.github.fashionbrot.common.consts.DateConst;
import com.github.fashionbrot.common.util.ObjectUtil;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;


public class LocalDateUtil {

    /**
     * 将日期字符串转换为 Date 对象
     *
     * @param dateString 要转换的日期字符串
     * @param formatter  日期格式化对象，用于指定日期字符串的格式
     * @return 转换后的 Date 对象，如果转换失败则返回 null
     */
    public static Date toDate(String dateString,DateTimeFormatter formatter){
        LocalDate localDate = toLocalDate(dateString, formatter);
        if (localDate!=null){
            return toDate(localDate);
        }
        return null;
    }

    /**
     * 将 LocalDate 对象转换为 Date 对象，使用默认时区。
     *
     * @param localDate 要转换的 LocalDate 对象
     * @return 转换后的 Date 对象，转换失败或输入为空时返回 null
     */
    public static Date toDate(LocalDate localDate) {
        return toDate(localDate, DateConst.DEFAULT_ZONE_ID);
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
        return toLocalDate(dateString,DateConst.DATE_FORMAT);
    }


    /**
     * 将日期字符串转换为 LocalDate 对象
     *
     * @param dateString 要转换的日期字符串
     * @param formatter  日期格式化对象，用于指定日期字符串的格式
     * @return 转换后的 LocalDate 对象，如果转换失败则返回 null
     */
    public static LocalDate toLocalDate(String dateString,DateTimeFormatter formatter) {
        if (ObjectUtil.isNotEmpty(dateString)) {
            try {
                return LocalDate.parse(dateString, formatter);
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
        return toLocalDate(date,DateConst.DEFAULT_ZONE_ID);
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
     * 将 LocalDate 对象转换为日期字符串。 yyyy-MM-dd
     *
     * @param localDate 要转换的 LocalDate 对象
     * @return 转换后的日期字符串，转换失败或输入为空时返回 null
     */
    public static String toString(LocalDate localDate) {
        return toString(localDate,DateConst.DATE_FORMAT);
    }


    /**
     * 将 LocalDate 对象转换为日期字符串
     *
     * @param localDate 要转换的 LocalDate 对象
     * @param formatter 日期格式化对象，用于指定输出日期字符串的格式
     * @return 转换后的日期字符串，如果 localDate 为 null 或转换失败则返回 null
     */
    public static String toString(LocalDate localDate,DateTimeFormatter formatter) {
        if (localDate != null) {
            try {
                return localDate.format(formatter);
            } catch (DateTimeException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 将 Date 转换为 LocalDate 对象，然后将其转换为 "yyyy-MM-dd" 格式的字符串。
     *
     * @param date 要转换的日期
     * @return 格式化后的日期字符串
     */
    public static String dateToString(Date date){
        return toString(toLocalDate(date),DateConst.DATE_FORMAT);
    }

}
