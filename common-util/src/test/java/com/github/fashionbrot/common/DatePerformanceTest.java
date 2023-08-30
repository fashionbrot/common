package com.github.fashionbrot.common;

import com.github.fashionbrot.common.date.LocalDateTimeUtil;
import com.github.fashionbrot.common.util.SimpleDateFormatUtil;
import org.junit.Test;

import java.text.SimpleDateFormat;
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
            Date date = SimpleDateFormatUtil.parseDateTime("2023-08-29 01:01:01");
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

}
