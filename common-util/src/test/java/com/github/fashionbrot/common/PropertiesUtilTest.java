package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.PropertiesUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class PropertiesUtilTest {


    @Test
    public void test1(){
        String content = "abc=abc";
        Properties properties = PropertiesUtil.toProperties(content);
        Assert.assertEquals("abc",properties.getProperty("abc"));
    }

    @Test
    public void test2(){
        String content = "${abc }";
        String value = PropertiesUtil.resolvePlaceholder(content);
        System.out.println(value);
        Assert.assertEquals("abc",value);
    }

}