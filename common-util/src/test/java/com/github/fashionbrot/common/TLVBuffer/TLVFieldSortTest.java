package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import com.github.fashionbrot.common.tlv.annotation.TLVField;
import com.github.fashionbrot.common.util.ObjectUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TLVFieldSortTest {

    @Data
    public static class TLVEntity{
        @TLVField(index = 1,serialize = false)
        private int c1;

        private String b1;

        @TLVField(index = 2,serialize = false)
        private String a1;

    }

    @Test
    public void test(){

        TLVEntity tlvEntity=new TLVEntity();
        tlvEntity.setA1("1");
        tlvEntity.setB1("2");
        tlvEntity.setC1(3);

        byte[] serialize = TLVBufferUtil.serialize(tlvEntity);
        System.out.println(Arrays.toString(serialize));

        TLVEntity deserialize = TLVBufferUtil.deserialize(TLVEntity.class, serialize);
        System.out.println(deserialize);

        Assert.assertTrue(deserialize.getA1()==null);
        Assert.assertTrue(deserialize.getB1().equals("2"));
        Assert.assertTrue(deserialize.getC1()==0);

    }

    @Data
    public static class TLVEntity2{
        @TLVField(index = 1,serialize = false)
        private int c1;
        private String b1;
        private String a1;
        private int d1;
    }

}
