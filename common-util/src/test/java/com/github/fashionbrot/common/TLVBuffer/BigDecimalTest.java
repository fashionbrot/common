package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.util.BigDecimalUtil;
import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

public class BigDecimalTest {

    @Data
    public static class BigDecimalEntity{
        private BigDecimal a1;
        private BigDecimal b1;
    }

    @Test
    public void test1() throws IOException {
        BigDecimalEntity entity=new BigDecimalEntity();
        entity.setA1(BigDecimal.ZERO);
        entity.setB1(BigDecimalUtil.format("0.00000000001"));
        byte[] bytes = TLVBufferUtil.serialize( entity);

        BigDecimalEntity deserialized = TLVBufferUtil.deserialize(BigDecimalEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
        System.out.println(BigDecimalUtil.toString(deserialized.getB1()));
    }

    @Test
    public void test2() throws IOException {
        BigDecimalEntity entity=new BigDecimalEntity();
        entity.setA1(BigDecimalUtil.format("0"));
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serialize( entity);
        System.out.println(Arrays.toString(bytes));
        BigDecimalEntity deserialized = TLVBufferUtil.deserialize(BigDecimalEntity.class, bytes);
        System.out.println(deserialized.toString());
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
