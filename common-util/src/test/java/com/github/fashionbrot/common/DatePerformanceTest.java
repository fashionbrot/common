package com.github.fashionbrot.common;

import com.github.fashionbrot.common.date.DateUtil;
import com.github.fashionbrot.common.date.LocalDateTimeUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePerformanceTest {



    @Test
    public void parseDate(){
        long startTime, endTime;
        int iterations = 1000000; // 要执行的迭代次数

        // 使用 DateTimeFormatter 进行测试
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Date date = LocalDateTimeUtil.parseDateTime("2023-08-29 01:01:01");
            if (date==null){
                System.out.println("DateTimeFormatter 异常");
            }
        }
        endTime = System.nanoTime();
        System.out.println("DateTimeFormatter took: " + (endTime - startTime) + " ns");

        // 使用 ThreadLocal<SimpleDateFormat> 进行测试
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Date date = DateUtil.parseDateTime("2023-08-29 01:01:01");
            if (date==null){
                System.out.println("ThreadLocal<SimpleDateFormat> 异常");
            }
        }
        endTime = System.nanoTime();
        System.out.println("ThreadLocal<SimpleDateFormat> took: " + (endTime - startTime) + " ns");

        startTime = System.nanoTime();
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < iterations; i++) {
            try {
                Date date = sf.parse("2023-08-29 01:01:01");
                if (date==null){
                    System.out.println("ThreadLocal<SimpleDateFormat> 异常");
                }
            }catch (Exception e){

            }
        }
        endTime = System.nanoTime();
        System.out.println("SimpleDateFormat took: " + (endTime - startTime) + " ns");
    }



    @Test
    public void formatDate(){
        long startTime, endTime;
        int iterations = 1000000; // 要执行的迭代次数

        // 使用 DateTimeFormatter 进行测试
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Date now = new Date();
            DateUtil.truncateTime(now);
        }
        endTime = System.nanoTime();
        System.out.println("LocalDate took: " + (endTime - startTime) + " ns");

        // 使用 ThreadLocal<SimpleDateFormat> 进行测试
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Date now = new Date();
            formatDate(now);
        }
        endTime = System.nanoTime();
        System.out.println("formatDat took: " + (endTime - startTime) + " ns");

    }


    public static Date formatDate(Date date){
        Calendar c= Calendar.getInstance();
        c.setTime(date);
        // 时
        c.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        c.set(Calendar.MINUTE, 0);
        // 秒
        c.set(Calendar.SECOND, 0);
        // 毫秒
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

}
