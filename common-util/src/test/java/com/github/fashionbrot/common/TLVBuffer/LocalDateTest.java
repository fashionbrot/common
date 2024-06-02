package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.consts.DateConst;
import com.github.fashionbrot.common.date.LocalDateUtil;
import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

public class LocalDateTest {

    @Data
    public static class LocalDateEntity{
        private LocalDate a1;
        private LocalDate b1;
    }

    @Test
    public void test1() throws IOException {
        LocalDateEntity entity=new LocalDateEntity();
        entity.setA1(LocalDate.MIN);
        entity.setB1(LocalDate.MAX);
        byte[] bytes = TLVBufferUtil.serialize(entity);

        LocalDate max = LocalDate.of(9999, 12, 31);
        LocalDate min = LocalDate.of(0, 1, 1);

        LocalDateEntity deserialized = TLVBufferUtil.deserialize(LocalDateEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(min,deserialized.getA1());
        Assert.assertEquals(max,deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        LocalDateEntity entity=new LocalDateEntity();
        entity.setA1(LocalDateUtil.toLocalDate("2024-05-05"));
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serialize(entity);
        System.out.println(Arrays.toString(bytes));
        LocalDateEntity deserialized = TLVBufferUtil.deserialize(LocalDateEntity.class, bytes);
        System.out.println(deserialized.toString());
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
