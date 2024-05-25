package com.github.fashionbrot.common;

/**
 * @author fashionbrot
 */
import com.github.fashionbrot.common.entity.TestEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProtobufSerializer {

    public static byte[] serialize(TestEntity testEntity) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 序列化 id 字段
        writeVarintTag(outputStream, 1);
        writeVarint(outputStream, testEntity.getId());

        // 序列化 name 字段
        writeLengthDelimitedTag(outputStream, 2);
        writeString(outputStream, testEntity.getName());

        return outputStream.toByteArray();
    }

    private static void writeVarintTag(ByteArrayOutputStream outputStream, int fieldNumber) throws IOException {
        int tag = (fieldNumber << 3) | 0;
        writeVarint(outputStream, tag);
    }

    private static void writeVarint(ByteArrayOutputStream outputStream, long value) throws IOException {
        while (true) {
            if ((value & ~0x7FL) == 0) {
                outputStream.write((int) value);
                return;
            } else {
                outputStream.write(((int) value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }

    private static void writeLengthDelimitedTag(ByteArrayOutputStream outputStream, int fieldNumber) throws IOException {
        int tag = (fieldNumber << 3) | 2;
        writeVarint(outputStream, tag);
    }

    private static void writeString(ByteArrayOutputStream outputStream, String value) throws IOException {
        byte[] bytes = value.getBytes("UTF-8");
        writeVarint(outputStream, bytes.length);
        outputStream.write(bytes);
    }
}
