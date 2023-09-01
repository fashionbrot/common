package com.github.fashionbrot.common;

import com.github.fashionbrot.common.date.LocalDateTimeUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LocalDateTimeUtilTest {


    @Test
    public void testValidDateTimeString() {
        String validDateTimeString = "2023-08-31 15:30:00";
        LocalDateTime expectedDateTime = LocalDateTime.of(2023, 8, 31, 15, 30, 0);

        LocalDateTime result = LocalDateTimeUtil.toLocalDateTime(validDateTimeString);
        assertEquals(expectedDateTime, result);
    }

    @Test
    public void testInvalidDateTimeString() {
        String invalidDateTimeString = "2023-08-31T15:30:00"; // Invalid format

        LocalDateTime result = LocalDateTimeUtil.toLocalDateTime(invalidDateTimeString);

        assertNull(result);
    }

    @Test
    public void testEmptyDateTimeString() {
        String emptyDateTimeString = "";

        LocalDateTime result = LocalDateTimeUtil.toLocalDateTime(emptyDateTimeString);

        assertNull(result);
    }

    @Test
    public void testNullDateTimeString() {
        String validDateTimeString = "2023-08-31";
        LocalDateTime result = LocalDateTimeUtil.toLocalDateTime(validDateTimeString);
        assertNull(result);
    }


    @Test
    public void testValidLocalDateTimeToString() {
        LocalDateTime validDateTime = LocalDateTime.of(2023, 8, 31, 15, 30, 0);
        String expectedDateTimeString = "2023-08-31 15:30:00";

        String result = LocalDateTimeUtil.toString(validDateTime);
        System.out.println(result);
        assertEquals(expectedDateTimeString, result);
    }

    @Test
    public void testNullLocalDateTimeToString() {
        LocalDateTime nullDateTime = null;
        String result = LocalDateTimeUtil.toString(nullDateTime);
        System.out.println(result);
        assertNull(result);
    }


    @Test
    public void testValidDateTimeParsing() throws Exception {
        String validDateTimeString = "2023-08-31 15:30:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expectedDate = sdf.parse(validDateTimeString);

        Date result = LocalDateTimeUtil.parseDateTime(validDateTimeString);

        assertEquals(expectedDate, result);
    }

    @Test
    public void testNullDateTimeParsing() {
        String nullDateTimeString = null;

        Date result = LocalDateTimeUtil.parseDateTime(nullDateTimeString);

        assertNull(result);
    }

    @Test
    public void testInvalidDateTimeParsing() {
        String invalidDateTimeString = "2023-08-31T15:30:00"; // Invalid format

        Date result = LocalDateTimeUtil.parseDateTime(invalidDateTimeString);

        assertNull(result);
    }
}
