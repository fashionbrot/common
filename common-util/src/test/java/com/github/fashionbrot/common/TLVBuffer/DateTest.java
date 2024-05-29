package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.date.DateUtil;
import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class DateTest {

    @Data
    public static class DateEntity{
        private Date a1;
        private Date b1;
    }

    @Test
    public void test1() throws IOException {
        DateEntity entity=new DateEntity();
        entity.setA1(new Date());
        entity.setB1(DateUtil.parseDateTime("2024-05-28 12:00:00"));
        byte[] bytes = TLVBufferUtil.serializeNew( entity);

        DateEntity deserialized = TLVBufferUtil.deserializeNew(DateEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        DateEntity entity=new DateEntity();
        entity.setA1(DateUtil.parseDate("2024-05-28"));
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serializeNew(entity);
        System.out.println(Arrays.toString(bytes));
        DateEntity deserialized = TLVBufferUtil.deserializeNew(DateEntity.class, bytes);
        System.out.println(deserialized.toString());
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
