package com.github.fashionbrot.common.TLVBuffer;

import com.alibaba.fastjson2.JSON;
import com.github.fashionbrot.common.util.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

public class StringTest {

    @Data
    public static class StringEntity{
        private String a1;
        private CharSequence b1;
    }

    @Test
    public void test1() throws IOException {
        StringEntity entity=new StringEntity();
        entity.setA1("张三");
        entity.setB1("李四");
        byte[] bytes = TLVBufferUtil.serializeNew( entity);

        StringEntity deserialized = TLVBufferUtil.deserializeNew(StringEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        StringEntity entity=new StringEntity();
        entity.setA1("");
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serializeNew( entity);
        System.out.println(Arrays.toString(bytes));
        StringEntity deserialized = TLVBufferUtil.deserializeNew(StringEntity.class, bytes);
        System.out.println(deserialized.toString());
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

}
