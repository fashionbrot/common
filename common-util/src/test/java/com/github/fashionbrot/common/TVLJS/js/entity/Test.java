package com.github.fashionbrot.common.TVLJS.js.entity;

import com.alibaba.fastjson2.JSON;
import com.github.fashionbrot.common.TLVBuffer.entity.JsEntity1;
import com.github.fashionbrot.common.TLVBuffer.entity.Test1;
import com.github.fashionbrot.common.TLVBuffer.entity.Test1Child;
import com.github.fashionbrot.common.date.LocalDateTimeUtil;
import com.github.fashionbrot.common.date.LocalDateUtil;
import com.github.fashionbrot.common.date.LocalTimeUtil;
import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import com.github.fashionbrot.common.util.BigDecimalUtil;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

public class Test {

    public static void main(String[] args) {
        test0();
    }

    private static void test0(){
        JsEntity1 entity=new JsEntity1();
        entity.setShortMax(Short.MAX_VALUE);
        entity.setShortMin(Short.MIN_VALUE);
        entity.setShortNull(null);

        entity.setIntMax(Integer.MAX_VALUE);
        entity.setIntMin(Integer.MIN_VALUE);
        entity.setIntNull(null);

        entity.setLongMax(Long.MAX_VALUE);
        entity.setLongMin(Long.MIN_VALUE);
        entity.setLongNull(null);

        entity.setFloatMax(12345.1234f);
        entity.setFloatMin(-54321.6543f);
        entity.setFloatNull(null);

        entity.setDoubleMax(Double.MAX_VALUE);
        entity.setDoubleMin(Double.MIN_VALUE);
        entity.setDoubleNull(null);

        entity.setBooleanTrue(true);
        entity.setBooleanFalse(false);
        entity.setBooleanNull(null);

        entity.setCharMax(Character.MAX_VALUE);
        entity.setCharMin(Character.MIN_VALUE);
        entity.setCharNull(null);

        entity.setByteMax(Byte.MAX_VALUE);
        entity.setByteMin(Byte.MIN_VALUE);
        entity.setByteNull(null);

        entity.setBigDecimalMax(BigDecimalUtil.format("999999.1234567"));
        entity.setBigDecimalMin(BigDecimalUtil.format("-999999.1234567"));
        entity.setBigDecimalNull(null);

        entity.setString1("你好啊TLVBuffer");
        entity.setString2("0");
        entity.setStringNull(null);

        entity.setDate1(new Date());
        entity.setDate2(null);

        entity.setLocalTime1(LocalTimeUtil.toLocalTime("12:12:12"));
        entity.setLocalTime2(null);

        entity.setLocalDate1(LocalDateUtil.toLocalDate("2024-06-02"));
        entity.setLocalTime2(null);

        entity.setLocalDateTime1(LocalDateTimeUtil.toLocalDateTime("2024-06-02 15:41:30"));
        entity.setLocalDateTime2(null);

        System.out.println(JSON.toJSONString(entity));
        System.out.println(JSON.toJSONString(entity).getBytes(StandardCharsets.UTF_8).length);

        byte[] serialize = TLVBufferUtil.serialize(entity);
        System.out.println(Arrays.toString(serialize));
        System.out.println(serialize.length);

        JsEntity1 deserialize = TLVBufferUtil.deserialize(JsEntity1.class, serialize);
        System.out.println(deserialize);
    }



    public static void test1(){

        Test1 t=new Test1();
        t.setA1("1");

        Test1Child child=new Test1Child();
        child.setB1("2");

        t.setC1(child);
//        t.setList1(Arrays.asList(child));

        byte[] serialize = TLVBufferUtil.serialize(t);
        System.out.println(Arrays.toString(serialize));
        System.out.println(serialize.length);

        Test1 deserialize = TLVBufferUtil.deserialize(Test1.class, serialize);
        System.out.println(deserialize);
    }
}
