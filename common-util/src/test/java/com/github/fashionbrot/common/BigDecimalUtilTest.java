package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.BigDecimalUtil;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author fashionbrot
 */
public class BigDecimalUtilTest {

    @Test
    public void test1(){
        double v = BigDecimalUtil.addDouble(null, 0.1, null, 0.2);
        String s = Double.toString(v);
        System.out.println(s);
        Assert.assertEquals(s,"0.3");
    }

    @Test
    public void test2(){
        double v = BigDecimalUtil.addDouble(null);
        String s = Double.toString(v);
        System.out.println(s);
        Assert.assertEquals(s,"0.0");
    }

    @Test
    public void test3(){
        BigDecimal v = BigDecimalUtil.add(null,new BigDecimal("1"));
        String s = v.toPlainString();
        System.out.println(s);
        Assert.assertEquals(s,"1");
    }



    @Test
    public void test4(){
        System.out.println(BigDecimalUtil.formatBigDecimal("0").divide(BigDecimalUtil.formatBigDecimal("2")));
    }


}
