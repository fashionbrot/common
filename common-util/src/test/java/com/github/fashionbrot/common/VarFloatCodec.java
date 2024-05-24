package com.github.fashionbrot.common;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class VarFloatCodec {

    public static byte[] encodeVarFloat(float value) {
        int intValue = Float.floatToIntBits(value);
        return encodeVarInt(intValue);
    }

    public static float decodeVarFloat(byte[] data) {
        int intValue = decodeVarInt(data);
        return Float.intBitsToFloat(intValue);
    }

    private static byte[] encodeVarInt(int value) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((value & 0xFFFFFF80) != 0) {
            output.write((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        output.write(value & 0x7F);
        return output.toByteArray();
    }

    private static int decodeVarInt(byte[] data) {
        int result = 0;
        int shift = 0;
        int index = 0;
        byte b;
        do {
            if (index >= data.length) {
                throw new RuntimeException("Varint decoding error: Insufficient data");
            }
            b = data[index++];
            result |= (b & 0x7F) << shift;
            shift += 7;
        } while ((b & 0x80) != 0);
        return result;
    }

    public static void main(String[] args) {
        float floatValue = 3.14f;
        byte[] encoded = encodeVarFloat(floatValue);
        System.out.println(encoded.length);
        System.out.println("Encoded: " + ByteBuffer.wrap(encoded).getInt());

        float decoded = decodeVarFloat(encoded);
        System.out.println("Decoded: " + decoded);
    }
}
