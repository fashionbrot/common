package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class LongTest {

    @Data
    public static class IntegerEntity{
        private long a1;
        private Long b1;
    }

    @Test
    public void test1() throws IOException {
        IntegerEntity entity=new IntegerEntity();
        entity.setA1(Long.MAX_VALUE);
        entity.setB1(Long.MIN_VALUE);
        byte[] bytes = TLVBufferUtil.serialize(entity);
        System.out.println(Arrays.toString(bytes));
        IntegerEntity deserialized = TLVBufferUtil.deserialize(IntegerEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        IntegerEntity entity=new IntegerEntity();
        entity.setA1(0);
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serialize( entity);

        IntegerEntity deserialized = TLVBufferUtil.deserialize(IntegerEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
