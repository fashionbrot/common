package com.github.fashionbrot.common;

import com.github.fashionbrot.common.date.LocalDateTimeUtil;
import com.github.fashionbrot.common.util.GenericTokenUtil;
import com.github.fashionbrot.common.util.MatcherUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MatcherUtilTest {

    @Test
    public void test(){
        Map<String,Object> map=new HashMap<>();
        map.put("abc","123123");
        String matchString= "${ abc} 你在干吗?";
        String match = MatcherUtil.replacePlaceholders(map, matchString);
        System.out.println(match);

        Assert.assertEquals("123123 你在干吗?",match);
    }

    @Test
    public void testReplacePlaceholders_SinglePlaceholder() {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("name", "Alice");

        String inputString = "Hello, my name is ${name }.";
        String expected = "Hello, my name is Alice.";

        String result = MatcherUtil.replacePlaceholders(valueMap, inputString);
        System.out.println(result);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReplacePlaceholders_MultiplePlaceholders() {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("name", "Bob");
        valueMap.put("age", "25");

        String inputString = "My name is ${ name} and I am ${ age } years old.";
        String expected = "My name is Bob and I am 25 years old.";

        String result = MatcherUtil.replacePlaceholders(valueMap, inputString);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReplacePlaceholders_UnmatchedPlaceholder() {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("name", "Charlie");

        String inputString = "Hello, my name is ${name} and I am from ${city}.";
        String expected = "Hello, my name is Charlie and I am from ${city}.";

        String result = MatcherUtil.replacePlaceholders(valueMap, inputString);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReplacePlaceholders_NullInputString() {
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("name", "David");

        String inputString = null;
        String expected = "";

        String result = MatcherUtil.replacePlaceholders(valueMap, inputString);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReplacePlaceholders_NullValueMap() {
        String inputString = "Hello, my name is ${name}.";
        String expected = "Hello, my name is ${name}.";

        String result = MatcherUtil.replacePlaceholders(null, inputString);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void sleepTest(){
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("name", "Charlie");

        String inputString = "Hello, my name is ${name} and I am from ${city}.";

        long startTime, endTime;
        int iterations = 1000000; // 要执行的迭代次数


        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            String parse = GenericTokenUtil.parse(inputString, valueMap);
        }
        endTime = System.nanoTime();
        System.out.println("GenericTokenUtil took: " + (endTime - startTime) + " ns");


        startTime = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            String parse = MatcherUtil.replacePlaceholders(valueMap,inputString);
        }
        endTime = System.nanoTime();
        System.out.println("MatcherUtil      took: " + (endTime - startTime) + " ns");

    }

}
