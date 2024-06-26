package com.github.fashionbrot.common;

import com.alibaba.fastjson2.JSON;
import com.github.fashionbrot.common.date.LocalDateTimeUtil;
import com.github.fashionbrot.common.date.LocalDateUtil;
import com.github.fashionbrot.common.date.LocalTimeUtil;
import com.github.fashionbrot.common.entity.LLVEntity;
import com.github.fashionbrot.common.entity.LVVListEntity;
import com.github.fashionbrot.common.util.BigDecimalUtil;
import com.github.fashionbrot.common.tlv.TLVUtil;
import com.github.fashionbrot.common.util.ObjectUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.DataFormatException;

public class LLvBufferUtilTest {

    public static void main(String[] args) throws IOException, DataFormatException {
        long l = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            test();
        }

//        test();
//        test();

        System.out.println(System.currentTimeMillis()-l);
    }

    public static void test() throws IOException, DataFormatException {

        int maxLength = 11; // 近似最大长度，减去一些以避免OutOfMemoryError
        StringBuilder sb = new StringBuilder(maxLength);
        for (int i = 0; i < maxLength; i++) {
            sb.append('a');
        }
        LVVListEntity lvvListEntity=new LVVListEntity();
        lvvListEntity.setId(3333);
        lvvListEntity.setName("李四");
        lvvListEntity.setParentName("李老板");

        LLVEntity build = LLVEntity.builder()
                .b(sb.toString())
                .b1((byte) 0x01)
                .b2(ObjectUtil.formatShort("12346"))
                .b3(333333333)
                .b4(4444444444L)
                .b5(5555.f)
                .b6(6666666d)
                .b7(BigDecimalUtil.format("1234.11"))
                .b8(new Date())
                .b9(LocalTimeUtil.toLocalTime(new Date()))
                .b10(LocalDateUtil.toLocalDate(new Date()))
                .b11(LocalDateTimeUtil.toLocalDateTime(new Date()))
                .b12(null)
                .list1(Arrays.asList(lvvListEntity,lvvListEntity))
                .array1(new LVVListEntity[]{lvvListEntity})
                .list2(Arrays.asList("张三","李四"))
                .build();
        build.setC1("张三");
        build.setD1(2222L);

        System.out.println("原始数据json序列化长度："+ JSON.toJSONString(build).getBytes().length+" byte");
        byte[] serialize = TLVUtil.serialize( build);
        System.out.println("自己实现序列化长度："+serialize.length+" byte");
        System.out.println(Arrays.toString(serialize));

//        LLvBufferUtil.ByteArrayReader inputStream=new LLvBufferUtil.ByteArrayReader(serialize);
//        byte[] bytes = inputStream.readFromTo(0, 123);
//
//        System.out.println(Arrays.toString(bytes));
//        System.out.println("111:"+inputStream.getLastReadIndex());
//        System.out.println(inputStream.isReadComplete());


        LLVEntity deserialize = TLVUtil.deserialize(LLVEntity.class, serialize);
        System.out.println(JSON.toJSONString(deserialize));
//
//        byte[] compress = GzipUtil.compress(JSON.toJSONString(build));
//        System.out.println("gizp压缩后长度："+compress.length);
//        System.out.println("gizp:"+JSONObject.parseObject(GzipUtil.decompress(compress)).toString());
    }

}
