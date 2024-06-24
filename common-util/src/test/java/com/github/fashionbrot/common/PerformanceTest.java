package com.github.fashionbrot.common;

import com.github.fashionbrot.common.TLV.entity.Test1Entity;

public class PerformanceTest {
    public static void main(String[] args) {
        long startTime, endTime;
        int iterations = 1000000; // 要执行的迭代次数

        Test1Entity entity=new Test1Entity();
        entity.setA1(12);

        Test1Entity entity1= new Test1Entity();
        entity1.setA1(13);

        Test1Entity[] test1Entities = {entity, entity1};

        // 使用 clazz.isArray() 进行测试
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            boolean isArray = test1Entities.getClass().isArray();
        }
        endTime = System.nanoTime();
        System.out.println("clazz.isArray() took: " + (endTime - startTime) + " ns");

        // 使用 endsWith("[]") 进行测试
        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            boolean isArray = test1Entities.getClass().getSimpleName().endsWith("[]");
        }
        endTime = System.nanoTime();
        System.out.println("endsWith(\"[]\") took: " + (endTime - startTime) + " ns");
    }
}
