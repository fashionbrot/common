package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.ObjectUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ObjectUtilTest {


    @Test
    public void test1(){
        Boolean bool = true;
        boolean aTrue = ObjectUtil.isTrue(bool);
        Assert.assertEquals(true,aTrue);
    }

    @Test
    public void test2(){
        Boolean bool = null;
        boolean aTrue = ObjectUtil.isFalse(bool);
        Assert.assertEquals(true,aTrue);
    }

    @Test
    public void test3(){
        Boolean bool = false;
        boolean aTrue = ObjectUtil.isFalse(bool);
        Assert.assertEquals(true,aTrue);
    }

    @Test
    public void test4(){
        String path = "a.b.c";
        String s = ObjectUtil.replacePath(path);
        System.out.println(s);
        Assert.assertEquals("a\\b\\c",s);
    }

    @Test
    public void test5(){
        String path = "a/b/c";
        String s = ObjectUtil.replacePath(path);
        System.out.println(s);
        Assert.assertEquals("a\\b\\c",s);
    }

    @Test
    public void test6(){
        List<String> strings = ObjectUtil.splitContent("", "");
        strings.add("abc");
        System.out.println(strings);
    }





    @Test
    public void testIsTrueWithTrueString() {
        assertTrue(ObjectUtil.isTrue("true"));
        assertTrue(ObjectUtil.isTrue("TRUE"));
        assertTrue(ObjectUtil.isTrue("TrUe"));
    }

    @Test
    public void testIsTrueWithFalseString() {
        assertFalse(ObjectUtil.isTrue("false"));
        assertFalse(ObjectUtil.isTrue("FALSE"));
        assertFalse(ObjectUtil.isTrue("random"));
        assertFalse(ObjectUtil.isTrue(""));
    }

    @Test
    public void testIsFalseWithTrueString() {
        assertFalse(ObjectUtil.isFalse("true"));
        assertFalse(ObjectUtil.isFalse("TRUE"));
        assertFalse(ObjectUtil.isFalse("TrUe"));
    }

    @Test
    public void testIsFalseWithFalseString() {
        assertTrue(ObjectUtil.isFalse("false"));
        assertTrue(ObjectUtil.isFalse("FALSE"));
        assertTrue(ObjectUtil.isFalse("random"));
        assertTrue(ObjectUtil.isFalse(""));
    }



    @Test
    public void testGt_ColumnGreaterThanValue() {
        assertTrue(ObjectUtil.gt(5L, 3));
    }

    @Test
    public void testGt_ColumnLessThanValue() {
        assertFalse(ObjectUtil.gt(3L, 5));
    }

    @Test
    public void testGt_ColumnEqualToValue() {
        assertFalse(ObjectUtil.gt(5L, 5));
    }

    @Test
    public void testGt_ColumnIsNull() {
        assertFalse(ObjectUtil.gt((Long) null, 5));
    }

    // Test cases for gt method with boolean condition

    @Test
    public void testGt_ConditionTrueColumnGreaterThanValue() {
        assertTrue(ObjectUtil.gt(true, 5L, 3));
    }

    @Test
    public void testGt_ConditionFalseColumnGreaterThanValue() {
        assertFalse(ObjectUtil.gt(false, 5L, 3));
    }

    @Test
    public void testGt_ConditionTrueColumnLessThanValue() {
        assertFalse(ObjectUtil.gt(true, 3L, 5));
    }

    @Test
    public void testGt_ConditionFalseColumnLessThanValue() {
        assertFalse(ObjectUtil.gt(false, 3L, 5));
    }

    @Test
    public void testGt_ConditionTrueColumnEqualToValue() {
        assertFalse(ObjectUtil.gt(true, 5L, 5));
    }

    @Test
    public void testGt_ConditionFalseColumnEqualToValue() {
        assertFalse(ObjectUtil.gt(false, 5L, 5));
    }

    @Test
    public void testGt_ConditionTrueColumnIsNull() {
        assertFalse(ObjectUtil.gt(true, (Long) null, 5L));
    }

    @Test
    public void testGt_ConditionFalseColumnIsNull() {
        assertFalse(ObjectUtil.gt(false, (Long) null, 5));
    }


    @Test
    public void testLeTrueCondition() {
        // Arrange
        Double column = 5.0;
        double val = 10.0;

        // Act
        boolean result = ObjectUtil.le(true, column, val);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testLeFalseCondition() {
        // Arrange
        Double column = 15.0;
        double val = 10.0;

        // Act
        boolean result = ObjectUtil.le(false, column, val);

        // Assert
        assertFalse(result);
    }



}
