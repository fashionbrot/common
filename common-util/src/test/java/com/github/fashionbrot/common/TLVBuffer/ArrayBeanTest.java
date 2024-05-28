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


        byte[] bytes = TLVBufferUtil.serializeNew(ListBeanEntity.class, beanEntity);

        ListBeanEntity deserialized = TLVBufferUtil.deserializeNew(ListBeanEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(beanEntity.getA1()[0].getAbc(),childEntity.getAbc());
    }



}
