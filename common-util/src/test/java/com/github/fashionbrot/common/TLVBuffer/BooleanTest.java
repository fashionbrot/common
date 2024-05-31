package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class BooleanTest {

    @Data
    public static class BooleanEntity{
        private boolean a1;
        private Boolean b1;
    }

    @Test
    public void test1() throws IOException {
        BooleanEntity entity=new BooleanEntity();
        entity.setA1(Boolean.TRUE);
        entity.setB1(Boolean.FALSE);
        byte[] bytes = TLVBufferUtil.serialize( entity);

        BooleanEntity deserialized = TLVBufferUtil.deserialize(BooleanEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.isA1(),deserialized.isA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        BooleanEntity entity=new BooleanEntity();
        entity.setA1(true);
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serialize( entity);

        BooleanEntity deserialized = TLVBufferUtil.deserialize(BooleanEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.isA1(),deserialized.isA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
