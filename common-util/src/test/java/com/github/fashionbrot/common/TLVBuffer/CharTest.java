package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CharTest {

    @Data
    public static class CharEntity{
        private Character a1;
        private char b1;
    }

    @Test
    public void test1() throws IOException {
        CharEntity entity=new CharEntity();
        entity.setA1(Character.MAX_VALUE);
        entity.setB1(Character.MIN_VALUE);
        byte[] bytes = TLVBufferUtil.serialize( entity);

        CharEntity deserialized = TLVBufferUtil.deserialize(CharEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        CharEntity entity=new CharEntity();
        entity.setA1(null);
        entity.setB1('A');
        byte[] bytes = TLVBufferUtil.serialize( entity);

        CharEntity deserialized = TLVBufferUtil.deserialize(CharEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
