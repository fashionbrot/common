package com.github.fashionbrot.common;

public class PerformanceTest {
    public static void main(String[] args) {
        long startTime, endTime;
        int iterations = 1000000; // 要执行的迭代次数

        // 使用 clazz.isArray() 进行测试
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            boolean isArray = String[].class.isArray();
        }
        endTime = System.nanoTime();
        System.out.println("clazz.isArray() took: " + (endTime - startTime) + " ns");

        // 使用 endsWith("[]") 进行测试
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            boolean isArray = "java.lang.String[]".endsWith("[]");
        }
        endTime = System.nanoTime();
        System.out.println("endsWith(\"[]\") took: " + (endTime - startTime) + " ns");
    }
}
