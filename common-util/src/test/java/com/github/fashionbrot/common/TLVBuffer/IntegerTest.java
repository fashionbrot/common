package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.util.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class IntegerTest {

    @Data
    public static class IntegerEntity{
        private int a1;
        private Integer b1;
    }

    @Test
    public void test1() throws IOException {
        IntegerEntity entity=new IntegerEntity();
        entity.setA1(Integer.MAX_VALUE);
        entity.setB1(Integer.MIN_VALUE);
        byte[] bytes = TLVBufferUtil.serializeNew( entity);

        IntegerEntity deserialized = TLVBufferUtil.deserializeNew(IntegerEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        IntegerEntity entity=new IntegerEntity();
        entity.setA1(0);
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serializeNew(entity);

        IntegerEntity deserialized = TLVBufferUtil.deserializeNew(IntegerEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
