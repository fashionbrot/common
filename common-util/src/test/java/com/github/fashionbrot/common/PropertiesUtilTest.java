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

    @Test
    public void test3(){
        String content = "abc=你\n" +
                "t1=好\n"+
                "test=${abc}${t1}";
        Properties properties = PropertiesUtil.toProperties(content);
        Properties resolve = PropertiesUtil.resolve(properties);
        String test = resolve.getProperty("test");
        System.out.println(resolve.toString());
        System.out.println(test);
        Assert.assertEquals("你好",test);
    }


}
