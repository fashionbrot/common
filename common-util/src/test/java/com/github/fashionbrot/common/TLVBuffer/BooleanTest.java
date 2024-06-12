package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class BooleanTest {

    @Data
    public static class BooleanEntity{
        private Boolean a1;
        private boolean b1;
    }

    @Test
    public void test1() throws IOException {
        BooleanEntity entity=new BooleanEntity();
        entity.setA1(Boolean.TRUE);
        entity.setB1(Boolean.FALSE);
        byte[] bytes = TLVBufferUtil.serialize( entity);

        BooleanEntity deserialized = TLVBufferUtil.deserialize(BooleanEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.isB1(),deserialized.isB1());
    }

    @Test
    public void test2() throws IOException {
        BooleanEntity entity=new BooleanEntity();
        entity.setA1(null);
        entity.setB1(false);
        byte[] bytes = TLVBufferUtil.serialize( entity);

        BooleanEntity deserialized = TLVBufferUtil.deserialize(BooleanEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.isB1(),deserialized.isB1());
    }

}
