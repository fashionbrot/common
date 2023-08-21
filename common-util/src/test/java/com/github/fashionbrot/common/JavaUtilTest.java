package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.JavaUtil;
import org.junit.Assert;
import org.junit.Test;

public class JavaUtilTest {


    @Test
    public void test1(){
        Object[] obj=new Object[]{};
        boolean array = JavaUtil.isArray(obj.getClass());
        System.out.println(array);
        Assert.assertEquals(true,array);
        Assert.assertEquals(true,JavaUtil.isArray(obj.getClass().getTypeName()));
    }

}
