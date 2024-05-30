package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class DoubleTest {

    @Data
    public static class DoubleEntity{
        private double a1;
        private Double b1;
    }

    @Test
    public void test1() throws IOException {
        DoubleEntity entity=new DoubleEntity();
        entity.setA1(Double.MAX_VALUE);
        entity.setB1(Double.MIN_VALUE);
        byte[] bytes = TLVBufferUtil.serialize( entity);

        DoubleEntity deserialized = TLVBufferUtil.deserialize(DoubleEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertTrue(ObjectUtil.equals(entity.getA1(),deserialized.getA1()));
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        DoubleEntity entity=new DoubleEntity();
        entity.setA1(0.10D);
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serialize(entity);

        DoubleEntity deserialized = TLVBufferUtil.deserialize(DoubleEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertTrue(ObjectUtil.equals(entity.getA1(),deserialized.getA1()));
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
