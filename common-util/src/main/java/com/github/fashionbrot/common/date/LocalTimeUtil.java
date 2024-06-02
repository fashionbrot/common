package com.github.fashionbrot.common.date;

import com.github.fashionbrot.common.consts.DateConst;
import com.github.fashionbrot.common.util.ObjectUtil;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class LocalTimeUtil {

    /**
     * 将时间字符串转换为 Date 对象
     *
     * @param timeString 要转换的时间字符串
     * @param formatter  时间格式化对象，用于指定时间字符串的格式
     * @return 转换后的 Date 对象，如果转换失败则返回 null
     */
    public static Date toDate(String timeString, DateTimeFormatter formatter){
        LocalTime localTime = toLocalTime(timeString, formatter);
        if (localTime!=null){
            return toDate(localTime);
        }
        return null;
    }


    /**
     * 将时间字符串转换为 LocalTime 对象。
     *
     * @param timeString 要转换的时间字符串，支持格式 "HH:mm:ss"
     * @return 转换后的 LocalTime 对象，转换失败或输入为空时返回 null
     */
    public static LocalTime toLocalTime(String timeString) {
        return toLocalTime(timeString, DateConst.TIME_FORMAT);
    }


    /**
     * 将时间字符串转换为 LocalTime 对象
     *
     * @param timeString 要转换的时间字符串
     * @param formatter  时间格式化对象，用于指定时间字符串的格式
     * @return 转换后的 LocalTime 对象，如果转换失败则返回 null
     */
    public static LocalTime toLocalTime(String timeString, DateTimeFormatter formatter) {
        if (ObjectUtil.isNotEmpty(timeString)) {
            try {
                return LocalTime.parse(timeString, formatter);
            } catch (DateTimeParseException e) {
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
                return localTime.format(DateConst.TIME_FORMAT);
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
        return toLocalTime(date,DateConst.DEFAULT_ZONE_ID);
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
        if (localTime!=null && localTime.getNano()>999){
            LocalTime newLocalTime = LocalTime.of(23,59,59,0);
            return toDate(newLocalTime,DateConst.DEFAULT_ZONE_ID);
        }else if (localTime!=null && localTime.getNano()<0){
            LocalTime newLocalTime = LocalTime.of(0,0,0,0);
            return toDate(newLocalTime,DateConst.DEFAULT_ZONE_ID);
        }
        return toDate(localTime,DateConst.DEFAULT_ZONE_ID);
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

}
