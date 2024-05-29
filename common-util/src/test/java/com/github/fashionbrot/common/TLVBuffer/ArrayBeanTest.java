package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.util.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayBeanTest {

    @Data
    public static class ListChildEntity{
        private String abc;
    }

    @Data
    public static class ListBeanEntity{
        private ListChildEntity[] a1;
        private ListChildEntity[] b1;
        private ListChildEntity[] c1;
    }

    @Test
    public void test1() throws IOException {

        ListChildEntity childEntity=new ListChildEntity();
        childEntity.setAbc("123123dsafds");
        ListBeanEntity beanEntity=new ListBeanEntity();
        beanEntity.setA1(new ListChildEntity[]{childEntity});
        beanEntity.setB1(new ListChildEntity[]{});
        beanEntity.setC1(null);


        byte[] bytes = TLVBufferUtil.serializeNew(beanEntity);

        ListBeanEntity deserialized = TLVBufferUtil.deserializeNew(ListBeanEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(beanEntity.getA1()[0].getAbc(),childEntity.getAbc());
    }



    @Data
    public static class ListBeanEntity2{
        private Object[] a1;
        private Object[] b1;
        private Object[] c1;
    }


    @Test
    public void test2() throws IOException {

        ListBeanEntity2 entity2=new ListBeanEntity2();
        entity2.setA1(new Object[]{1,2,"你"});
        entity2.setB1(new Object[]{});
        entity2.setC1(null);

        byte[] bytes = TLVBufferUtil.serializeNew(entity2);

        ListBeanEntity2 deserialized = TLVBufferUtil.deserializeNew(ListBeanEntity2.class, bytes);
        System.out.println(deserialized);
        Assert.assertArrayEquals(entity2.getA1(),deserialized.getA1());
        Assert.assertArrayEquals(entity2.getB1(),deserialized.getB1());
        Assert.assertArrayEquals(entity2.getC1(),deserialized.getC1());
    }


    @Data
    public static class ListBeanEntity3{
        private ListBeanEntity4[] a1;

    }

    @Data
    public static class ListBeanEntity4{
        private Object[] a1;
    }

    @Test
    public void test3() throws IOException {
        ListBeanEntity4 listBeanEntity4=new ListBeanEntity4();
        listBeanEntity4.setA1(new Object[]{1,2,'A'});

        ListBeanEntity3 listBeanEntity3= new ListBeanEntity3();
        listBeanEntity3.setA1(new ListBeanEntity4[]{listBeanEntity4});

        byte[] bytes = TLVBufferUtil.serializeNew(listBeanEntity3);
        System.out.println(Arrays.toString(bytes));

        ListBeanEntity3 deserialized = TLVBufferUtil.deserializeNew(ListBeanEntity3.class, bytes);
        System.out.println(deserialized);
        Assert.assertArrayEquals(listBeanEntity3.getA1(),deserialized.getA1());
        Assert.assertArrayEquals(listBeanEntity3.getA1()[0].getA1(),deserialized.getA1()[0].getA1());
    }


}
