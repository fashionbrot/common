package com.github.fashionbrot.common;

import com.github.fashionbrot.common.date.DateUtil;
import com.github.fashionbrot.common.date.LocalDateUtil;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class LocalDateUtilTest {

    @Test
    public void testToLocalDateValidInput() {
        // 测试正常情况下的字符串转换为 LocalDate
        String validDateString = "2023-09-01";
        LocalDate resultLocalDate = LocalDateUtil.toLocalDate(validDateString);
        assertNotNull(resultLocalDate);

        // 检查结果是否正确
        LocalDate expectedLocalDate = LocalDate.of(2023, 9, 1);
        assertEquals(expectedLocalDate, resultLocalDate);
    }

    @Test
    public void testToLocalDateNullInput() {
        // 测试空输入
        LocalDate resultLocalDate = LocalDateUtil.toLocalDate("");
        assertNull(resultLocalDate);
    }

    @Test
    public void testToLocalDateInvalidInput() {
        // 测试无效的日期字符串
        String invalidDateString = "2023-09-01 15:30:45"; // 不符合默认格式
        LocalDate resultLocalDate = LocalDateUtil.toLocalDate(invalidDateString);
        assertNull(resultLocalDate);
    }

}
