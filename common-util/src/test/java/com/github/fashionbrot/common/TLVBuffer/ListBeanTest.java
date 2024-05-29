package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.util.TLVBufferUtil;
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



}
