package com.github.fashionbrot.common;

import com.alibaba.fastjson2.JSON;
import com.github.fashionbrot.common.date.LocalDateTimeUtil;
import com.github.fashionbrot.common.date.LocalDateUtil;
import com.github.fashionbrot.common.date.LocalTimeUtil;
import com.github.fashionbrot.common.entity.LvEntity;
import com.github.fashionbrot.common.util.BigDecimalUtil;
import com.github.fashionbrot.common.util.LvBufferUtil;
import com.github.fashionbrot.common.util.ObjectUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class LvBufferUtilTest {

    public static void main(String[] args) throws IOException {
        long l = System.currentTimeMillis();

//        for (int i = 0; i < 1000; i++) {
//            test();
//        }

        test();

        System.out.println(System.currentTimeMillis()-l);
    }

    public static void test() throws IOException {
        int index=0;
        for (int i = 0x00; i <= 0xFF; i++) {
            // 将整数转换为十六进制字符串，并打印
            System.out.println(index+" 0x" + Integer.toHexString(i).toUpperCase());
            index++;
        }
        System.out.println(Integer.MAX_VALUE);
        if (true){
            return;
        }

        int maxLength = 65565; // 近似最大长度，减去一些以避免OutOfMemoryError
        StringBuilder sb = new StringBuilder(maxLength);
        for (int i = 0; i < maxLength; i++) {
            sb.append('a');
        }

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
                .build();
        System.out.println("原始数据json序列化长度："+ JSON.toJSONString(build).getBytes().length+" byte");
        byte[] serialize = LvBufferUtil.serialize2(LvEntity.class, build);
        System.out.println("自己实现序列化长度："+serialize.length+" byte");
        System.out.println(Arrays.toString(serialize));


        LvEntity deserialize = LvBufferUtil.deserialize2(LvEntity.class, serialize);
        System.out.println(deserialize);
    }

}
