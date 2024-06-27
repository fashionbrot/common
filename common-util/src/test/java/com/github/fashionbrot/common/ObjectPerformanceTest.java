package com.github.fashionbrot.common;

public class ObjectPerformanceTest {


    public static void main(String[] args) throws InterruptedException {
        Class<?> clazz = int.class;
        int iterations = 100000000;  // 1亿次迭代


        Thread.sleep(11000);

        test1(clazz, iterations);

        // 测试 Object.class.isAssignableFrom(clazz)
        test2(clazz, iterations);

        test1(clazz, iterations);

        test2(clazz, iterations);

    }

    private static void test2(Class<?> clazz, int iterations) {
        long startTime2 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            boolean result = clazz.isAssignableFrom(Object.class);
            if (result){

            }
        }
        long endTime2 = System.nanoTime();
        long duration2 = endTime2 - startTime2;
        System.out.println("Object.class.isAssignableFrom(clazz): " + duration2 + " ns");
    }

    private static void test1(Class<?> clazz, int iterations) {
        // 测试 Object.class == clazz
        long startTime1 = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            boolean result = (Object.class == clazz);
            if (result){

            }
        }
        long endTime1 = System.nanoTime();
        long duration1 = endTime1 - startTime1;
        System.out.println("Object.class == clazz: " + duration1 + " ns");
    }
}
