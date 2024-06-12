package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class DoubleTest {

    @Data
    public static class DoubleEntity{
        private Double a1;
        private double b1;
    }

    @Test
    public void test1() throws IOException {
        DoubleEntity entity=new DoubleEntity();
        entity.setA1(Double.MAX_VALUE);
        entity.setB1(Double.MIN_VALUE);
        byte[] bytes = TLVBufferUtil.serialize( entity);
        System.out.println(Arrays.toString(bytes));
        DoubleEntity deserialized = TLVBufferUtil.deserialize(DoubleEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertTrue(ObjectUtil.equals(entity.getA1(),deserialized.getA1()));
        Assert.assertTrue(ObjectUtil.equals(entity.getB1(),deserialized.getB1()));
    }

    @Test
    public void test2() throws IOException {
        DoubleEntity entity=new DoubleEntity();
        entity.setA1(null);
        entity.setB1(0.10D);
        byte[] bytes = TLVBufferUtil.serialize(entity);
        System.out.println(Arrays.toString(bytes));
        DoubleEntity deserialized = TLVBufferUtil.deserialize(DoubleEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertTrue(ObjectUtil.equals(entity.getA1(),deserialized.getA1()));
        Assert.assertTrue(ObjectUtil.equals(entity.getB1(),deserialized.getB1()));
    }

}
