package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.util.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CharTest {

    @Data
    public static class CharEntity{
        private char a1;
        private Character b1;
    }

    @Test
    public void test1() throws IOException {
        CharEntity entity=new CharEntity();
        entity.setA1(Character.MAX_VALUE);
        entity.setB1(Character.MIN_VALUE);
        byte[] bytes = TLVBufferUtil.serializeNew( entity);

        CharEntity deserialized = TLVBufferUtil.deserializeNew(CharEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        CharEntity entity=new CharEntity();
        entity.setA1('A');
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serializeNew( entity);

        CharEntity deserialized = TLVBufferUtil.deserializeNew(CharEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
