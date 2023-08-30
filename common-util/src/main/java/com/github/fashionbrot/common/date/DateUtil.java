package com.github.fashionbrot.common.date;

import com.github.fashionbrot.common.consts.DateConst;
import com.github.fashionbrot.common.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
@Slf4j
public class DateUtil {




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
            log.error("Parsing ZoneId failed. Using default ZoneId: {}", DateConst.DEFAULT_ZONE_ID);
            return DateConst.DEFAULT_ZONE_ID;
        }
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
     * 将日期字符串转换为日期对象，使用系统默认时区。
     *
     * @param dateString 要转换的日期字符串
     * @return 转换后的日期对象
     */
    public static Date parseDate(String dateString){
        return parseDate(dateString,DateConst.DEFAULT_ZONE_ID);
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
            LocalDate localDate = LocalDate.parse(dateString,DateConst.DATE_FORMAT);
            Instant instant = localDate.atStartOfDay(zoneId).toInstant();
            return Date.from(instant);
        } catch (Exception e) {
            log.error("Parsing or conversion failed due to an exception: {}", e.getMessage());
            return null;
        }
    }






}
