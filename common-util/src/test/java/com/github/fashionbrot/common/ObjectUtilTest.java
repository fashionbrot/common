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

}
