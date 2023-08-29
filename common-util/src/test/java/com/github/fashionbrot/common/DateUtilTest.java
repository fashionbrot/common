package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.DateUtil;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

public class DateUtilTest {


    @Test
    public void test1(){
        Date nowDate = DateUtil.nowDate();
        String s = DateUtil.dateToString(nowDate);
        System.out.println(s);


        LocalDateTime localDateTime = DateUtil.toLocalDateTime(nowDate);
        String s1 = DateUtil.toString(localDateTime);
        System.out.println(s1);
    }

}
