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

    @Data
    public static class ListObjectEntity2{
        private List<Object> aa;
    }

    @Data
    public static class ListObjectEntity3{
        private List<Object> bb;
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


        byte[] bytes = TLVBufferUtil.serializeNew( beanEntity);
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


//    @Test TODO
    public void test2() throws IOException {

        ListObjectEntity3 entity3=new ListObjectEntity3();
        entity3.setBb(Arrays.asList("entity3"));

        ListObjectEntity2 entity2=new ListObjectEntity2();
        entity2.setAa(Arrays.asList(entity3));

        byte[] bytes = TLVBufferUtil.serializeNew(entity2);
        System.out.println(bytes.length);
        System.out.println(Arrays.toString(bytes));

        ListObjectEntity2 deserialized = TLVBufferUtil.deserializeNew(ListObjectEntity2.class, bytes);
        System.out.println(deserialized);

    }


}
