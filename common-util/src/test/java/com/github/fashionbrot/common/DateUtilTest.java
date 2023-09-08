package com.github.fashionbrot.common;

import com.github.fashionbrot.common.consts.DateConst;
import com.github.fashionbrot.common.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class DateUtilTest {


    @Test
    public void test1(){
        SimpleDateFormat sf=new SimpleDateFormat(DateConst.DATE_TIME_PATTERN);
        String dateString = "2023-08-31 01:01:01";
        Date now= DateUtil.parseDateTime(dateString);

        Date date = DateUtil.truncateTime(now);

        String format = sf.format(date);
        System.out.println(format);
        assertEquals(format,"2023-08-31 00:00:00");
    }


    @Test
    public void testGetDatesBetween() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateConst.DATE_TIME_PATTERN);

        // 创建起始日期和结束日期
        Date startDate = DateUtil.parseDateTime("2023-09-01 01:01:01");
        Date endDate = DateUtil.parseDateTime("2023-09-04 23:59:59");

        // 调用方法获取日期列表
        List<Date> dateList = DateUtil.getDatesBetween(startDate, endDate);

        // 预期结果
        String[] expectedDates = {"2023-09-01", "2023-09-02", "2023-09-03", "2023-09-04"};
        String[] expectedDates2 = {"2023-09-01 00:00:00", "2023-09-02 00:00:00", "2023-09-03 00:00:00", "2023-09-04 00:00:00"};


        // 断言列表长度
        assertEquals(expectedDates2.length, dateList.size());

        // 断言日期是否与预期一致
        for (int i = 0; i < expectedDates2.length; i++) {
            Date expectedDate = dateFormat.parse(expectedDates2[i]);
            assertEquals(expectedDate, dateList.get(i));
        }
    }


    @Test
    public void testGetDatesBetween2() {

        // 创建起始日期和结束日期（startDate 晚于 endDate）
        Date startDate = DateUtil.parseDateTime("2023-09-01 01:01:01");
        Date endDate = DateUtil.parseDateTime("2023-08-01 01:01:01");

        // 调用方法获取日期列表
        List<Date> dateList = DateUtil.getDatesBetween(startDate, endDate);

        // 预期结果：应该返回一个空的日期列表
        assertEquals(0, dateList.size());
    }

    @Test
    public void testGetDatesBetween3() {

        // 创建起始日期和结束日期（startDate 晚于 endDate）
        Date startDate = DateUtil.parseDateTime("2023-08-01 01:01:01");
        Date endDate = DateUtil.parseDateTime("2023-08-31 01:01:01");

        // 调用方法获取日期列表
        List<Date> dateList = DateUtil.getDatesBetween(startDate, endDate);
        for(Date date:dateList){
            System.out.println(DateUtil.formatDateTime(date));
        }
        // 预期结果：应该返回一个空的日期列表
        assertEquals(31, dateList.size());
    }

    @Test
    public void testGetDatesBetween4() {

        // 创建起始日期和结束日期（startDate 晚于 endDate）
        Date startDate = DateUtil.parseDateTime("2023-08-01 01:01:01");
        Date endDate = DateUtil.parseDateTime("2023-08-01 11:01:01");

        // 调用方法获取日期列表
        List<Date> dateList = DateUtil.getDatesBetween(startDate, endDate);
        for(Date date:dateList){
            System.out.println(DateUtil.formatDateTime(date));
        }
        // 预期结果：应该返回一个空的日期列表
        assertEquals(1, dateList.size());
    }



    @Test
    public void testDateAfterRange() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = dateFormat.parse("2023-01-01");
        Date endDate = dateFormat.parse("2023-12-31");
        Date targetDate = dateFormat.parse("2024-06-15");

        assertFalse(DateUtil.isDateBetweenInclusive(targetDate, startDate, endDate));
    }

    @Test
    public void testStartDateEqualsTargetDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = dateFormat.parse("2023-01-01");
        Date endDate = dateFormat.parse("2023-12-31");
        Date targetDate = dateFormat.parse("2023-01-01");

        assertTrue(DateUtil.isDateBetweenInclusive(targetDate, startDate, endDate));
    }

    @Test
    public void testEndDateEqualsTargetDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = dateFormat.parse("2023-01-01");
        Date endDate = dateFormat.parse("2023-12-31");
        Date targetDate = dateFormat.parse("2023-12-31");

        assertTrue(DateUtil.isDateBetweenInclusive(targetDate, startDate, endDate));
    }

    @Test
    public void testNullInputs() {
        assertFalse(DateUtil.isDateBetweenInclusive(null, null, null));
    }

    @Test
    public void testDateInRange() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startDate = dateFormat.parse("2023-01-01 00:00:00");
        Date endDate = dateFormat.parse("2023-12-31 23:59:59");
        Date targetDate = dateFormat.parse("2023-06-15 12:30:00");

        assertTrue(DateUtil.isDateBetweenInclusive(targetDate, startDate, endDate));
    }

    @Test
    public void testDateBeforeRange() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startDate = dateFormat.parse("2023-01-01 00:00:00");
        Date endDate = dateFormat.parse("2023-12-31 23:59:59");
        Date targetDate = dateFormat.parse("2022-06-15 15:45:30");

        assertFalse(DateUtil.isDateBetweenInclusive(targetDate, startDate, endDate));
    }


    @Test
    public void testParseTime() {
        // 测试正常情况下的解析
        String validTime = "15:30:45";
        Date parsedDate = DateUtil.parseTime(validTime);
        assertNotNull(parsedDate);

        // 测试空输入
        String emptyTime = "";
        Date emptyParsedDate = DateUtil.parseTime(emptyTime);
        assertNull(emptyParsedDate);
    }

    @Test
    public void testFormatTime() throws ParseException {
        // 创建一个测试日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse("2023-09-01 15:30:45");

        // 测试正常情况下的格式化
        String formattedTime = DateUtil.formatTime(date);
        assertEquals("15:30:45", formattedTime);

        // 测试空输入
        String emptyFormattedTime = DateUtil.formatTime(null);
        assertEquals("", emptyFormattedTime);
    }

    @Test
    public void testFormatDateTimeValidDate() throws ParseException {
        // 测试正常情况下的格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetime = sdf.parse("2023-09-01 15:30:45");
        String formattedDateTime = DateUtil.formatDateTime(datetime);
        assertEquals("2023-09-01 15:30:45", formattedDateTime);
    }

    @Test
    public void testFormatDateTimeNullDate() {
        // 测试空输入
        String emptyFormattedDateTime = DateUtil.formatDateTime(null);
        assertEquals("", emptyFormattedDateTime);
    }

    @Test
    public void testFormatDateTimeInvalidDate() throws ParseException {
        // 测试无效的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date datetime = sdf.parse("2023-09-01");
        String formattedDateTime = DateUtil.formatDateTime(datetime);
        assertEquals("2023-09-01 00:00:00", formattedDateTime);
    }



    @Test
    public void testParseDateTimeValidInput() throws ParseException {
        // 测试正常情况下的解析
        String validDateTimeStr = "2023-09-01 15:30:45";
        Date parsedDateTime = DateUtil.parseDateTime(validDateTimeStr);
        assertNotNull(parsedDateTime);
    }

    @Test
    public void testParseDateTimeNullInput() {
        // 测试空输入
        Date parsedDateTime = DateUtil.parseDateTime(null);
        assertNull(parsedDateTime);
    }

    @Test
    public void testParseDateTimeInvalidInput() {
        // 测试无效的日期时间字符串
        String invalidDateTimeStr = "2023-09-01T15:30:45"; // 不符合默认格式
        Date parsedDateTime = DateUtil.parseDateTime(invalidDateTimeStr);
        assertNull(parsedDateTime);
    }


    @Test
    public void testFormatDateValidDate() throws ParseException {
        // 测试正常情况下的格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2023-09-01");
        String formattedDate = DateUtil.formatDate(date);
        assertEquals("2023-09-01", formattedDate);
    }

    @Test
    public void testFormatDateNullDate() {
        // 测试空输入
        String emptyFormattedDate = DateUtil.formatDate(null);
        assertEquals("", emptyFormattedDate);
    }

    @Test
    public void testFormatDateInvalidDate() throws ParseException {
        // 测试无效的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse("2023-09-01 15:30:45");
        String formattedDate = DateUtil.formatDate(date);
        assertEquals("2023-09-01", formattedDate);
    }


    @Test
    public void testParseDateValidInput() throws ParseException {
        // 测试正常情况下的解析
        String validDateStr = "2023-09-01";
        Date parsedDate = DateUtil.parseDate(validDateStr);
        assertNotNull(parsedDate);
    }

    @Test
    public void testParseDateNullInput() {
        // 测试空输入
        Date parsedDate = DateUtil.parseDate(null);
        assertNull(parsedDate);
    }

    @Test
    public void testParseDateInvalidInput() {
        // 测试无效的日期字符串格式
        String invalidDateStr = "2023-09-01 15:30:45"; // 不符合默认格式
        Date parsedDate = DateUtil.parseDate(invalidDateStr);
        assertNull(parsedDate);
    }


    @Test
    public void testAddDaysPositive() throws ParseException {
        // 测试添加正整数天数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate = sdf.parse("2023-09-01");
        Date resultDate = DateUtil.addDays(inputDate, 7);
        assertNotNull(resultDate);

        // 检查结果日期是否正确
        Date expectedDate = sdf.parse("2023-09-08");
        assertEquals(expectedDate, resultDate);
    }

    @Test
    public void testAddDaysNegative() throws ParseException {
        // 测试添加负整数天数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate = sdf.parse("2023-09-15");
        Date resultDate = DateUtil.addDays(inputDate, -7);
        assertNotNull(resultDate);

        // 检查结果日期是否正确
        Date expectedDate = sdf.parse("2023-09-08");
        assertEquals(expectedDate, resultDate);
    }

    @Test
    public void testAddDaysZero() throws ParseException {
        // 测试添加零天
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate = sdf.parse("2023-09-01");
        Date resultDate = DateUtil.addDays(inputDate, 0);
        assertNotNull(resultDate);

        // 检查结果日期是否与输入日期相同
        assertEquals(inputDate, resultDate);
    }



    @Test
    public void testGetFirstDayOfMonthTruncateTimeValidInput() throws ParseException {
        // 测试正常情况下的获取月份第一天并清除时间部分
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date inputDate = sdf.parse("2023-09-15 15:30:45");
        Date resultDate = DateUtil.getFirstDayOfMonthTruncateTime(inputDate);
        assertNotNull(resultDate);

        // 检查结果日期是否正确
        Date expectedDate = sdf.parse("2023-09-01 00:00:00");
        assertEquals(expectedDate, resultDate);
    }

    @Test
    public void testGetFirstDayOfMonthTruncateTimeNullInput() {
        // 测试空输入
        Date resultDate = DateUtil.getFirstDayOfMonthTruncateTime(null);
        assertNull(resultDate);
    }


    @Test
    public void testGetLastDayOfMonthTruncateTimeValidInput() throws ParseException {
        // 测试正常情况下的获取月份最后一天并清除时间部分
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date inputDate = sdf.parse("2023-09-15 15:30:45");
        Date resultDate = DateUtil.getLastDayOfMonthTruncateTime(inputDate);
        assertNotNull(resultDate);

        // 检查结果日期是否正确
        Date expectedDate = sdf.parse("2023-09-30 00:00:00");
        assertEquals(expectedDate, resultDate);
    }

    @Test
    public void testGetLastDayOfMonthTruncateTimeNullInput() {
        // 测试空输入
        Date resultDate = DateUtil.getLastDayOfMonthTruncateTime(null);
        assertNull(resultDate);
    }





    @Test
    public void testGetDaysBetweenDatesValidInput() throws ParseException {
        // 测试正常情况下的计算日期间隔
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = sdf.parse("2023-09-01 15:30:45");
        Date endDate = sdf.parse("2023-09-10 10:45:30");
        int daysBetween = DateUtil.getDaysBetweenDates(startDate, endDate);
        assertEquals(9, daysBetween);
    }

    @Test
    public void testGetDaysBetweenDatesNullInput() {
        // 测试空输入
        int daysBetween = DateUtil.getDaysBetweenDates(null, null);
        assertEquals(0, daysBetween);
    }

    @Test
    public void testGetDaysBetweenDatesInvalidInput() throws ParseException {
        // 测试无效的日期输入
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2023-09-01");
        Date endDate = sdf.parse("2023-08-01"); // 结束日期在开始日期之前
        int daysBetween = DateUtil.getDaysBetweenDates(startDate, endDate);
        assertEquals(0, daysBetween);
    }



    @Test
    public void testGetMinutesBetweenDatesValidInput() throws ParseException {
        // 测试正常情况下的计算分钟间隔
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = sdf.parse("2023-09-01 15:30:45");
        Date endDate = sdf.parse("2023-09-01 16:45:30");
        int minutesBetween = DateUtil.getMinutesBetweenDates(startDate, endDate);
        assertEquals(74, minutesBetween);
    }

    @Test
    public void testGetMinutesBetweenDatesNullInput() {
        // 测试空输入
        int minutesBetween = DateUtil.getMinutesBetweenDates(null, null);
        assertEquals(0, minutesBetween);
    }

    @Test
    public void testGetMinutesBetweenDatesInvalidInput() throws ParseException {
        // 测试无效的日期输入
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("2023-09-01");
        Date endDate = sdf.parse("2023-08-01"); // 结束日期在开始日期之前
        int minutesBetween = DateUtil.getMinutesBetweenDates(startDate, endDate);
        assertEquals(0, minutesBetween);
    }

}
