package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class ByteTest {

    @Data
    public static class ByteEntity{
        private Byte a1;
        private byte b1;
    }

    @Test
    public void test1() throws IOException {
        ByteEntity entity=new ByteEntity();
        entity.setA1(Byte.MIN_VALUE);
        entity.setB1(Byte.MAX_VALUE);
        byte[] bytes = TLVBufferUtil.serialize(entity);

        ByteEntity deserialized = TLVBufferUtil.deserialize(ByteEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        ByteEntity entity=new ByteEntity();
        entity.setA1(null);
        entity.setB1((byte) 0xFF);
        byte[] bytes = TLVBufferUtil.serialize(entity);
        System.out.println(Arrays.toString(bytes));
        ByteEntity deserialized = TLVBufferUtil.deserialize(ByteEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
