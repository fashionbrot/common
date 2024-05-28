package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.util.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ShortTest {

    @Data
    public static class ShortEntity{
        private short a1;
        private Short b1;
    }

    @Test
    public void test1() throws IOException {
        ShortEntity entity=new ShortEntity();
        entity.setA1(Short.MAX_VALUE);
        entity.setB1(Short.MIN_VALUE);
        byte[] bytes = TLVBufferUtil.serializeNew(entity);

        ShortEntity deserialized = TLVBufferUtil.deserializeNew(ShortEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        ShortEntity entity=new ShortEntity();
        entity.setA1((short) 0);
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serializeNew( entity);

        ShortEntity deserialized = TLVBufferUtil.deserializeNew(ShortEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
