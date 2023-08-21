package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.ObjectUtil;
import org.junit.Assert;
import org.junit.Test;

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

}
