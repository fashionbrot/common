package com.github.fashionbrot.common;

/**
 * @author fashionbrot
 */
import com.github.fashionbrot.common.entity.TestEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ProtobufDeserializer {

    public static TestEntity deserialize(byte[] data) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

        long id = 0;
        String name = "";

        while (inputStream.available() > 0) {
            int tag = (int) readVarint(inputStream);
            int fieldNumber = tag >> 3;
            int wireType = tag & 0x7;

            switch (fieldNumber) {
                case 1:
                    if (wireType == 0) {
                        id = readVarint(inputStream);
                    } else {
                        throw new IOException("Invalid wire type for field 1");
                    }
                    break;
                case 2:
                    if (wireType == 2) {
                        name = readString(inputStream);
                    } else {
                        throw new IOException("Invalid wire type for field 2");
                    }
                    break;
                default:
                    throw new IOException("Unknown field number: " + fieldNumber);
            }
        }

        return TestEntity.builder()
                .id(id)
                .name(name)
                .build();
    }

//    private static int readVarint(ByteArrayInputStream inputStream) throws IOException {
//        int result = 0;
//        int shift = 0;
//        while (true) {
//            int b = inputStream.read();
//            if (b == -1) {
//                throw new IOException("Unexpected end of stream");
//            }
//            result |= (b & 0x7F) << shift;
//            if ((b & 0x80) == 0) {
//                return result;
//            }
//            shift += 7;
//        }
//    }

    private static long readVarint(ByteArrayInputStream inputStream) throws IOException {
        long result = 0;
        int shift = 0;
        int b;
        do {
            b = inputStream.read();
            if (b == -1) {
                throw new IOException("Unexpected end of stream");
            }
            result |= (long)(b & 0x7F) << shift;
            shift += 7;
        } while ((b & 0x80) != 0);
        return result;
    }


    private static String readString(ByteArrayInputStream inputStream) throws IOException {
        int length = (int) readVarint(inputStream);
        byte[] bytes = new byte[length];
        if (inputStream.read(bytes) != length) {
            throw new IOException("Unexpected end of stream");
        }
        return new String(bytes, "UTF-8");
    }
}
