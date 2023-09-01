package com.github.fashionbrot.common.date;

import com.github.fashionbrot.common.consts.DateConst;
import com.github.fashionbrot.common.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 */
@Slf4j
public class DateUtil {



    /**
     * 解析时间字符串为Date对象。
     *
     * @param time 要解析的时间字符串，格式为 "HH:mm:ss"
     * @return 解析后的Date对象，解析失败或输入为空时返回null
     */
    public static Date parseTime(String time){
        if (ObjectUtil.isEmpty(time)){
            return null;
        }
        return LocalTimeUtil.toDate(time,DateConst.TIME_FORMAT);
    }

    /**
     * 解析时间字符串为Date对象。
     *
     * @param time 要解析的时间字符串，格式为 "HH:mm:ss"
     * @return 解析后的Date对象，解析失败或输入为空时返回null
     */
    public static String formatTime(Date time) {
        LocalTime localTime = LocalTimeUtil.toLocalTime(time);
        if (localTime!=null){
            return LocalTimeUtil.toString(localTime);
        }
        return "";
    }


    /**
     * 将Date对象格式化为日期时间字符串。
     *
     * @param datetime 要格式化的Date对象
     * @return 格式化后的日期时间字符串，如果输入为null则返回空字符串
     */
    public static String formatDateTime(Date datetime){
        LocalDateTime localDateTime = LocalDateTimeUtil.toLocalDateTime(datetime);
        if (localDateTime!=null){
            return LocalDateTimeUtil.toString(localDateTime);
        }
        return "";
    }

    /**
     * 解析日期时间字符串为Date对象。
     *
     * @param dateTimeStr 要解析的日期时间字符串，格式为 "yyyy-MM-dd HH:mm:ss"
     * @return 解析后的Date对象，解析失败或输入为空时返回null
     */
    public static Date parseDateTime(String dateTimeStr){
        if (ObjectUtil.isEmpty(dateTimeStr)){
            return null;
        }
        return LocalDateTimeUtil.parseDateTime(dateTimeStr,DateConst.DEFAULT_ZONE_ID);
    }


    /**
     * 使用给定的 SimpleDateFormat 格式化器解析日期字符串为日期对象。
     *
     * @param format     SimpleDateFormat 格式化器
     * @param dateString 日期字符串
     * @return 解析后的日期对象，如果解析失败则返回 null
     */
    public static Date parse(SimpleDateFormat format,String dateString){
        if (format==null || ObjectUtil.isEmpty(dateString)){
            return null;
        }
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }


    /**
     * 将Date对象格式化为日期字符串。
     *
     * @param date 要格式化的Date对象
     * @return 格式化后的日期字符串，如果输入为null则返回空字符串
     */
    public static String formatDate(Date date) {
        LocalDate localDate = LocalDateUtil.toLocalDate(date);
        if (localDate!=null){
            return LocalDateUtil.toString(localDate);
        }
        return "";
    }


    /**
     * 解析日期字符串为Date对象。
     *
     * @param dateStr 要解析的日期字符串，格式为 "yyyy-MM-dd"
     * @return 解析后的Date对象，解析失败或输入为空时返回null
     */
    public static Date parseDate(String dateStr){
        if (ObjectUtil.isEmpty(dateStr)){
            return null;
        }
        return LocalDateUtil.toDate(dateStr,DateConst.DATE_FORMAT);
    }

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
     * 截断日期的时间部分，仅保留日期。
     *
     * @param date 要截断的日期
     * @return 截断后的日期
     */
    public static Date truncateTime(Date date) {
        if (date==null){
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    /**
     * 获取两个日期之间的日期列表。
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 包含所有日期的列表，日期格式为 "yyyy-MM-dd"
     */
    public static List<Date> getDatesBetween(Date startDate, Date endDate) {
        List<Date> dateList = new ArrayList<>();
        if (startDate==null || endDate==null || startDate.after(endDate)){
            return dateList;
        }
        Date start = truncateTime(startDate);
        Date end = truncateTime(endDate);
        if (start==null || end==null){
            return dateList;
        }
        if (start.equals(end)){
            dateList.add(start);
            return dateList;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        while (!calendar.getTime().after(end)) {
            dateList.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1); // 增加一天
        }
        return dateList;
    }


    /**
     * 获取给定日期所在月份的第一天，时间部分被设置为零时。
     *
     * @param date 要获取第一天的日期
     * @return 该月的第一天日期，时间部分为零时
     */
    public static Date getFirstDayOfMonthTruncateTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 设置日期为月份的第一天，并将时间部分设为零时
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        // 将时间部分设为零时
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    /**
     * 获取给定日期所在月份的最后一天，时间部分被设置为零时。
     *
     * @param date 要获取最后一天的日期
     * @return 该月的最后一天日期，时间部分为零时
     */
    public static Date getLastDayOfMonthTruncateTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 设置日期为该月的最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }



    /**
     * 检查给定日期是否在指定时间范围内（包括开始日期和结束日期）。
     *
     * @param targetDate 要检查的日期
     * @param startDate  时间范围的开始日期
     * @param endDate    时间范围的结束日期
     * @return 如果目标日期在时间范围内，返回 true；否则返回 false
     */
    public static boolean isDateBetweenInclusive(Date targetDate, Date startDate, Date endDate) {
        if (targetDate == null || startDate == null || endDate == null) {
            return false;
        }

        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(targetDate);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        // 使用 compareTo 方法比较日期
        return targetCalendar.compareTo(startCalendar) >= 0 && targetCalendar.compareTo(endCalendar) <= 0;
    }



    /**
     * 获取指定日期的起始时间（00:00:00）。
     *
     * @param date 要获取起始时间的日期
     * @return 指定日期的起始时间
     */
    public static Date getStartOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的结束时间（23:59:59）。
     *
     * @param date 要获取结束时间的日期
     * @return 指定日期的结束时间
     */
    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 计算两个日期之间的天数差。
     *
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 两个日期之间的天数差
     */
    public static int getDaysBetweenDates(Date startDate, Date endDate) {
        if (startDate==null || endDate==null || startDate.after(endDate)){
            return 0;
        }
        long startTime = startDate.getTime();
        long endTime = endDate.getTime();

        // 清除时分秒毫秒部分，以免影响计算
        startTime = startTime - (startTime % (24 * 60 * 60 * 1000));
        endTime = endTime - (endTime % (24 * 60 * 60 * 1000));

        long diffMilliseconds = endTime - startTime;
        return (int) (diffMilliseconds / (24 * 60 * 60 * 1000));
    }

    /**
     * 获取指定日期的年份。
     *
     * @param date 要获取年份的日期
     * @return 指定日期的年份
     */
    public static int getYearFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取指定日期的月份。
     *
     * @param date 要获取月份的日期
     * @return 指定日期的月份，从 1 开始计数
     */
    public static int getMonthFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }


    /**
     * 判断指定日期是否为闰年。
     *
     * @param date 要判断的日期
     * @return 如果指定日期所在年份为闰年，返回 true；否则返回 false
     */
    public static boolean isLeapYear(Date date) {
        if (date == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }


    /**
     * 计算两个日期之间的分钟数差。
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 两个日期之间的分钟数差
     */
    public static int getMinutesBetweenDates(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return 0;
        }

        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diffMilliseconds = endTime - startTime;
        return (int) (diffMilliseconds / (60 * 1000));
    }

    /**
     * 计算两个日期之间的秒数差。
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 两个日期之间的秒数差
     */
    public static int getSecondsBetweenDates(Date startDate, Date endDate) {
        if (startDate == null || endDate == null || startDate.after(endDate)) {
            return 0;
        }

        long startTime = startDate.getTime();
        long endTime = endDate.getTime();
        long diffMilliseconds = endTime - startTime;
        return (int) (diffMilliseconds / 1000);
    }

}
