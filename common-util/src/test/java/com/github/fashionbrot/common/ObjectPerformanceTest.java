package com.github.fashionbrot.common;

public class ObjectPerformanceTest {


    public static void main(String[] args) {
        Class<?> clazz = Object.class;
        int iterations = 1000000;  // 1亿次迭代

        // 测试 Object.class == clazz
        long startTime1 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            boolean result = (Object.class == clazz);
        }
        long endTime1 = System.nanoTime();
        long duration1 = endTime1 - startTime1;
        System.out.println("Object.class == clazz: " + duration1 + " ns");

        // 测试 Object.class.isAssignableFrom(clazz)
        long startTime2 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            boolean result = Object.class.isAssignableFrom(clazz);
        }
        long endTime2 = System.nanoTime();
        long duration2 = endTime2 - startTime2;
        System.out.println("Object.class.isAssignableFrom(clazz): " + duration2 + " ns");
    }
}
