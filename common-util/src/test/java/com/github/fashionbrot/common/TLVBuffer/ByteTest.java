package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ByteTest {

    @Data
    public static class ByteEntity{
        private byte a1;
        private Byte b1;
    }

    @Test
    public void test1() throws IOException {
        ByteEntity entity=new ByteEntity();
        entity.setA1(Byte.MIN_VALUE);
        entity.setB1(Byte.MAX_VALUE);
        byte[] bytes = TLVBufferUtil.serializeNew(entity);

        ByteEntity deserialized = TLVBufferUtil.deserializeNew(ByteEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        ByteEntity entity=new ByteEntity();
        entity.setA1((byte) 0x00);
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serializeNew(entity);

        ByteEntity deserialized = TLVBufferUtil.deserializeNew(ByteEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
