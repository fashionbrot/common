package com.github.fashionbrot.common;

import com.github.fashionbrot.common.entity.TestEntity;
import com.github.fashionbrot.common.util.LvBufferTypeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author fashionbrot
 */
public class Test {

    public static void main(String[] args) throws IOException {

        TestEntity build = TestEntity.builder()
                .id(Long.MAX_VALUE)
                .name("张三")
                .build();
        System.out.println( ByteBuffer.allocate(Long.BYTES).putLong(build.getId()).array().length+(build.getName().getBytes().length));
        byte[] serialize = ProtobufSerializer.serialize(build);
        System.out.println(serialize.length);
        System.out.println(Arrays.toString(serialize));

        TestEntity deserialize = ProtobufDeserializer.deserialize(serialize);
        System.out.println(deserialize);

        System.out.println(Integer.MAX_VALUE);
        System.out.println(LvBufferTypeUtil.encodeVarLong(Integer.MAX_VALUE).length);

    }

}