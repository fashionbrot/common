package com.github.fashionbrot.common.TLVBuffer;

import com.alibaba.fastjson2.JSON;
import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class FloatTest {

    @Data
    public static class FloatEntity{
        private float a1;
        private Float b1;
    }

    @Test
    public void test1() throws IOException {
        FloatEntity entity=new FloatEntity();
        entity.setA1(Float.MAX_VALUE);
        entity.setB1(Float.MIN_VALUE);
        byte[] bytes = TLVBufferUtil.serialize( entity);
        System.out.println(Arrays.toString(bytes));
        FloatEntity deserialized = TLVBufferUtil.deserialize(FloatEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertTrue(ObjectUtil.equals(entity.getA1(),deserialized.getA1()));
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        FloatEntity entity=new FloatEntity();
        entity.setA1(123456.12f);
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serialize( entity);
        System.out.println(Arrays.toString(bytes));
        FloatEntity deserialized = TLVBufferUtil.deserialize(FloatEntity.class, bytes);
        System.out.println(deserialized);
        System.out.println(JSON.toJSONString(entity));
        Assert.assertTrue(ObjectUtil.equals(entity.getA1(),deserialized.getA1()));
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}