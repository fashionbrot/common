package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.BigDecimalUtil;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author fashionbrot
 */
public class BigDecimalUtilTest {

    @Test
    public void test1(){
        double v = BigDecimalUtil.add(null, 0.1, null, 0.2);
        String s = Double.toString(v);
        System.out.println(s);
        assertEquals(s,"0.3");
    }

    @Test
    public void test2(){
        Double d=null;
        double v = BigDecimalUtil.add(d);
        String s = Double.toString(v);
        System.out.println(s);
        assertEquals(s,"0.0");
    }

    @Test
    public void test3(){
        BigDecimal v = BigDecimalUtil.add(null,new BigDecimal("1"));
        String s = v.toPlainString();
        System.out.println(s);
        assertEquals(s,"1");
    }


    @Test
    public void test4(){
        BigDecimal v = BigDecimalUtil.avg(null,new BigDecimal("1"));
        System.out.println(v);
        assertTrue(BigDecimalUtil.equals(v,BigDecimalUtil.format("0.5")));
    }

    @Test
    public void test5(){
        BigDecimal avg = BigDecimalUtil.avg(BigDecimalUtil.format("4"), BigDecimalUtil.format("2"));
        System.out.println(avg);
        System.out.println(avg.compareTo(new BigDecimal(3)));
        Assert.assertTrue(avg.compareTo(new BigDecimal(3))==0);
    }

    @Test
    public void test6(){
        Assert.assertTrue(BigDecimalUtil.equals(BigDecimalUtil.format("100.00"),BigDecimalUtil.format("100")));
    }


    @Test
    public void testFormatWithValidInput() {
        // 测试使用有效的输入转换为 BigDecimal 对象
        BigDecimal result = BigDecimalUtil.format(new Float("123.45"));
        assertTrue(BigDecimalUtil.equals(new BigDecimal("123.45"), result));
    }

    @Test
    public void testFormatWithInvalidInput() {
        // 测试使用无效的输入转换为 BigDecimal 对象，期望返回 BigDecimal.ZERO
        BigDecimal result = BigDecimalUtil.format("abc");
        assertTrue(BigDecimalUtil.equals(BigDecimal.ZERO, result));
    }

    @Test
    public void testFormatWithNullInput() {
        // 测试使用空输入转换为 BigDecimal 对象，期望返回 BigDecimal.ZERO
        Object obj=null;
        BigDecimal result = BigDecimalUtil.format(obj);
        assertTrue(BigDecimalUtil.equals(BigDecimal.ZERO, result));
    }

    @Test
    public void testAddWithValidValues() {
        // 测试使用有效的值进行累加
        double result = BigDecimalUtil.add(10.5, 20.3, 5.2);
        assertTrue(BigDecimalUtil.equals(36D,result)); // 期望结果等于 36.0，允许误差在 0.0001 内
    }


    @Test
    public void testAddWithNullValues() {
        // 测试使用包含null值的数组进行累加，期望返回 0.0
        double result = BigDecimalUtil.add(10.5, null, 5.2);
        assertTrue(BigDecimalUtil.equals(15.7D,result)); // 忽略null值，只计算有效值的累加
    }


    @Test
    public void testEqualsWithEqualValues() {
        // 测试相等的 BigDecimal 值和 Double 值，期望返回 true
        boolean result = BigDecimalUtil.equals(new BigDecimal("10.5"), 10.5);
        assertTrue(result);
    }

    @Test
    public void testEqualsWithUnequalValues() {
        // 测试不相等的 BigDecimal 值和 Double 值，期望返回 false
        boolean result = BigDecimalUtil.equals(new BigDecimal("10.5"), 20.3);
        assertFalse(result);
    }

    @Test
    public void testEqualsWithNullValues() {
        // 测试一个 BigDecimal 值为null，期望返回 false
        BigDecimal a = null;
        boolean result = BigDecimalUtil.equals(a, 10.5);
        assertFalse(result);
    }



    @Test
    public void testNegateWithPositiveValue() {
        // 测试将正数取反，期望返回其相反数
        BigDecimal result = BigDecimalUtil.negate(new BigDecimal("10.5"));
        assertTrue(BigDecimalUtil.equals(new BigDecimal("-10.5"), result));
    }

    @Test
    public void testNegateWithNegativeValue() {
        // 测试将负数取反，期望返回其相反数
        BigDecimal result = BigDecimalUtil.negate(new BigDecimal("-7.80"));
        assertTrue(BigDecimalUtil.equals(new BigDecimal("7.800"), result));
    }

    @Test
    public void testNegateWithZeroValue() {
        // 测试将零取反，期望返回零
        BigDecimal result = BigDecimalUtil.negate(BigDecimal.ZERO);
        assertTrue(BigDecimalUtil.equals(BigDecimal.ZERO, result));
    }

    @Test
    public void testCompareTo() {
        BigDecimal b1 = new BigDecimal("123.45");
        BigDecimal b2 = new BigDecimal("678.90");
        BigDecimal b3 = new BigDecimal("123.45");

        assertEquals(-1, BigDecimalUtil.compareTo(b1, b2));
        assertEquals(0, BigDecimalUtil.compareTo(b1, b3));
        assertEquals(1, BigDecimalUtil.compareTo(b2, b3));
    }

}
