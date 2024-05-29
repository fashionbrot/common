package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.tlv.ByteArrayReader;
import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListBeanTest {

    @Data
    public static class ListChildEntity{
        private String abc;
    }

    @Data
    public static class ListBeanEntity{
        private List<ListChildEntity> a1;
        private List<ListChildEntity> b1;
        private List<ListChildEntity> c1;
    }

    @Test
    public void test1() throws IOException {

        ListChildEntity childEntity=new ListChildEntity();
        childEntity.setAbc("123123dsafds");
        ListBeanEntity beanEntity=new ListBeanEntity();
        beanEntity.setA1(Arrays.asList(childEntity));
        beanEntity.setB1(new ArrayList<>());
        beanEntity.setC1(null);


        byte[] bytes = TLVBufferUtil.serializeNew( beanEntity);
        System.out.println(Arrays.toString(bytes));

        ListBeanEntity deserialized = TLVBufferUtil.deserializeNew(ListBeanEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(beanEntity.getA1().get(0).getAbc(),childEntity.getAbc());
    }


    @Data
    public static class ListBeanEntity2{
        private List<ListChildEntity> a1;
    }


    @Test
    public void test2()  {

        ListChildEntity childEntity=new ListChildEntity();
        childEntity.setAbc("111111");
        ListChildEntity childEntity2=new ListChildEntity();
        childEntity2.setAbc("222222");


        byte[] bytes = TLVBufferUtil.serializeNew(Arrays.asList(childEntity,childEntity2));
        System.out.println(Arrays.toString(bytes));

        List<ListChildEntity> deserialized = TLVBufferUtil.deserializeList(ListChildEntity.class, new ByteArrayReader(bytes));
        System.out.println(deserialized);
        Assert.assertEquals(childEntity.getAbc(),deserialized.get(0).getAbc());
        Assert.assertEquals(childEntity2.getAbc(),deserialized.get(1).getAbc());
    }




}
