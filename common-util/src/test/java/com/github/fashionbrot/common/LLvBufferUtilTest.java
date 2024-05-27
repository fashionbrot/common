package com.github.fashionbrot.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.fashionbrot.common.compress.GzipUtil;
import com.github.fashionbrot.common.date.LocalDateTimeUtil;
import com.github.fashionbrot.common.date.LocalDateUtil;
import com.github.fashionbrot.common.date.LocalTimeUtil;
import com.github.fashionbrot.common.entity.LVVListEntity;
import com.github.fashionbrot.common.entity.LvEntity;
import com.github.fashionbrot.common.util.BigDecimalUtil;
import com.github.fashionbrot.common.util.LLvBufferUtil;
import com.github.fashionbrot.common.util.LvBufferUtil;
import com.github.fashionbrot.common.util.ObjectUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.DataFormatException;

public class LLvBufferUtilTest {

    public static void main(String[] args) throws IOException, DataFormatException {
        long l = System.currentTimeMillis();

//        for (int i = 0; i < 1000; i++) {
//            test();
//        }

//        test();
        test();

        System.out.println(System.currentTimeMillis()-l);
    }

    public static void test() throws IOException, DataFormatException {

        int maxLength = 11; // 近似最大长度，减去一些以避免OutOfMemoryError
        StringBuilder sb = new StringBuilder(maxLength);
        for (int i = 0; i < maxLength; i++) {
            sb.append('a');
        }
        LVVListEntity lvvListEntity=LVVListEntity.builder()
                .id(2222)
                .name("李四")
                .build();

        LvEntity build = LvEntity.builder()
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
                .list1(Arrays.asList(lvvListEntity))
                .build();
        build.setC1("张三");
        build.setD1(2222L);

        System.out.println("原始数据json序列化长度："+ JSON.toJSONString(build).getBytes().length+" byte");
        byte[] serialize = LLvBufferUtil.serializeNew(LvEntity.class, build);
        System.out.println("自己实现序列化长度："+serialize.length+" byte");
        System.out.println(Arrays.toString(serialize));


        LvEntity deserialize = LLvBufferUtil.deserializeNew(LvEntity.class, serialize);
        System.out.println(JSON.toJSONString(deserialize));

        byte[] compress = GzipUtil.compress(JSON.toJSONString(build));
        System.out.println("gizp压缩后长度："+compress.length);
        System.out.println("gizp:"+JSONObject.parseObject(GzipUtil.decompress(compress)).toString());
    }

}
