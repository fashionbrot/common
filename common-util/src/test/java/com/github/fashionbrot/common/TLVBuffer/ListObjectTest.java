package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.util.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListObjectTest {



    @Data
    public static class ListObjectEntity{
        private List<Object> a1;
        private List<Object> b1;
        private List c1;
    }

    @Test
    public void test1() throws IOException {

        List<Object> a1 = Arrays.asList(1, 2, "你好");
        ArrayList b1 = new ArrayList<>();
        b1.add("b1");

        ListObjectEntity beanEntity=new ListObjectEntity();
        beanEntity.setA1(a1);
        beanEntity.setB1(b1);
        beanEntity.setC1(null);


        byte[] bytes = TLVBufferUtil.serializeNew(ListObjectEntity.class, beanEntity);
        System.out.println(bytes.length);
        System.out.println(Arrays.toString(bytes));

        ListObjectEntity deserialized = TLVBufferUtil.deserializeNew(ListObjectEntity.class, bytes);
        System.out.println(deserialized);
//        System.out.println(a1.get(0));
        Assert.assertEquals(a1.get(0),deserialized.getA1().get(0));
        Assert.assertEquals(a1.get(1),deserialized.getA1().get(1));
        Assert.assertEquals(a1.get(2),deserialized.getA1().get(2));
        Assert.assertEquals(b1.get(0),deserialized.getB1().get(0));
    }


    @Test
    public void test2() throws IOException {


        List<Object> a1 = Arrays.asList(1, 2, "你好");
        ArrayList b1 = new ArrayList<>();
        b1.add(a1);

        ListObjectEntity beanEntity=new ListObjectEntity();
//        beanEntity.setA1(a1);
        beanEntity.setB1(b1);
//        beanEntity.setC1(null);


        byte[] bytes = TLVBufferUtil.serializeNew(ListObjectEntity.class, beanEntity);
        System.out.println(bytes.length);
        System.out.println(Arrays.toString(bytes));

        ListObjectEntity deserialized = TLVBufferUtil.deserializeNew(ListObjectEntity.class, bytes);
        System.out.println(deserialized);
//        System.out.println(a1.get(0));
        Assert.assertEquals(a1.get(0),deserialized.getA1().get(0));
        Assert.assertEquals(a1.get(1),deserialized.getA1().get(1));
        Assert.assertEquals(a1.get(2),deserialized.getA1().get(2));
        Assert.assertEquals(b1.get(0),deserialized.getB1().get(0));
    }


}
