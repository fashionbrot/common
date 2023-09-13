package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.JavaUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Hashtable;
import java.util.Properties;
import java.util.SortedMap;

public class JavaUtilTest {


    @Test
    public void test1(){
        Object[] obj=new Object[]{};
        boolean array = JavaUtil.isArray(obj.getClass());
        System.out.println(array);
        Assert.assertEquals(true,array);
        Assert.assertEquals(true,JavaUtil.isArray(obj.getClass().getTypeName()));
    }

    @Test
    public void test2(){
        Properties properties =new Properties();

        boolean map = JavaUtil.isMap(properties.getClass());
        System.out.println(map);
        Assert.assertEquals(true,map);

        Assert.assertEquals(true,JavaUtil.isMap(SortedMap.class));
        Assert.assertEquals(true,JavaUtil.isMap(Hashtable.class));
    }

}
