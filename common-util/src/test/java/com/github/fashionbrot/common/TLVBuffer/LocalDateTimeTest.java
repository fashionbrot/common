package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.date.LocalDateTimeUtil;
import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

public class LocalDateTimeTest {

    @Data
    public static class LocalDateTimeEntity{
        private LocalDateTime a1;
        private LocalDateTime b1;
    }

    @Test
    public void test1() throws IOException {
        LocalDateTimeEntity entity=new LocalDateTimeEntity();
        entity.setA1(LocalDateTime.MIN);
        entity.setB1(LocalDateTime.MAX);
        byte[] bytes = TLVBufferUtil.serialize(entity);

        LocalDateTime max = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
        LocalDateTime min = LocalDateTime.of(0, 1, 1, 0, 0);

        LocalDateTimeEntity deserialized = TLVBufferUtil.deserialize(LocalDateTimeEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(min,deserialized.getA1());
        Assert.assertEquals(max,deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        LocalDateTimeEntity entity=new LocalDateTimeEntity();
        entity.setA1(LocalDateTimeUtil.toLocalDateTime("2024-05-05 23:59:59"));
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serialize( entity);
        System.out.println(Arrays.toString(bytes));
        LocalDateTimeEntity deserialized = TLVBufferUtil.deserialize(LocalDateTimeEntity.class, bytes);
        System.out.println(deserialized.toString());
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
