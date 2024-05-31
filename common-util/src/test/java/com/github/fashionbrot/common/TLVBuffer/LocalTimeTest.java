package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.date.LocalTimeUtil;
import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Arrays;

public class LocalTimeTest {

    @Data
    public static class LocalTimeEntity{
        private LocalTime a1;
        private LocalTime b1;
    }

    @Test
    public void test1() throws IOException {
        LocalTimeEntity entity=new LocalTimeEntity();
        entity.setA1(LocalTime.MAX);
        entity.setB1(LocalTime.MIN);
        byte[] bytes = TLVBufferUtil.serialize( entity);

        LocalTimeEntity deserialized = TLVBufferUtil.deserialize(LocalTimeEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        LocalTimeEntity entity=new LocalTimeEntity();
        entity.setA1(LocalTimeUtil.toLocalTime("01:00:59"));
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serialize( entity);
        System.out.println(Arrays.toString(bytes));
        LocalTimeEntity deserialized = TLVBufferUtil.deserialize(LocalTimeEntity.class, bytes);
        System.out.println(deserialized.toString());
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
