package com.github.fashionbrot.common.tlv;

import com.github.fashionbrot.common.date.LocalDateTimeUtil;
import com.github.fashionbrot.common.util.BigDecimalUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;

public class TLVTypeUtil {

    public static byte[] encodeVarChar(char value) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int intValue = value; // 将char转换为int以便处理
        while ((intValue & 0xFFFFFF80) != 0) {
            output.write((intValue & 0x7F) | 0x80);
            intValue >>>= 7;
        }
        output.write(intValue & 0x7F);
        return output.toByteArray();
    }


    public static char decodeVarChar(byte[] data) {
        int result = 0;
        int shift = 0;
        int index = 0;
        byte b;
        do {
            if (index >= data.length) {
                throw new RuntimeException("decodeVarChar decoding error: Insufficient data");
            }
            b = data[index++];
            result |= (b & 0x7F) << shift;
            shift += 7;
        } while ((b & 0x80) != 0);
        return (char) result;
    }





    public static byte[] encodeVarInteger(int value)  {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((value & 0xFFFFFF80) != 0) {
            output.write((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        output.write(value & 0x7F);
        return output.toByteArray();
    }

    public static int decodeVarInteger(byte[] data)  {

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


    public static byte[] encodeVarShort(short value) {
        return encodeVarInteger(value);
    }

    public static short decodeVarShort(byte[] data) {
        return (short) decodeVarInteger(data);
    }



    public static byte[] encodeVarLong(long value) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((value & 0xFFFFFFFFFFFFFF80L) != 0L) {
            output.write((byte) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        output.write((byte) (value & 0x7F));
        return output.toByteArray();
    }

    public static long decodeVarLong(byte[] data)  {
        long result = 0L;
        int shift = 0;
        int index = 0;
        byte b;
        do {
            if (index >= data.length) {
                throw new RuntimeException("Varint decoding error: Insufficient data");
            }
            b = data[index++];
            result |= (b & 0x7FL) << shift;
            shift += 7;
        } while ((b & 0x80) != 0);
        return result;
    }


    public static byte[] encodeVarFloat(float value) {
        int intValue = Float.floatToIntBits(value);
        return encodeVarInteger(intValue);
    }

    public static float decodeVarFloat(byte[] data) {
        int intValue = decodeVarInteger(data);
        return Float.intBitsToFloat(intValue);
    }

    public static byte[] encodeVarDouble(double value) {
        long longValue = Double.doubleToLongBits(value);
        return encodeVarLong(longValue);
    }

    public static double decodeVarDouble(byte[] data)  {
        long longValue = decodeVarLong(data);
        return Double.longBitsToDouble(longValue);
    }


    public static byte[] encodeVarDate(Date date) {
        long longValue = date.getTime(); // 获取 Date 对象的时间戳
        return encodeVarLong(longValue); // 调用通用的长整型编码方法
    }

    public static Date decodeVarDate(byte[] data) {
        long longValue = decodeVarLong(data); // 调用通用的长整型解码方法
        return new Date(longValue); // 使用时间戳创建 Date 对象
    }

    public static byte[] encodeVarLocalTime(LocalTime time) {
        long nanoOfDay = time.toNanoOfDay(); // 将 LocalTime 转换为纳秒数
        return encodeVarLong(nanoOfDay); // 调用通用的长整型编码方法
    }

    public static byte[] encodeVarLocalDate(LocalDate date) {
        long daysSinceEpoch = date.toEpochDay(); // 将 LocalDate 转换为自公元前1年1月1日以来的天数
        return encodeVarLong(daysSinceEpoch); // 调用通用的长整型编码方法
    }

    public static LocalDate decodeVarLocalDate(byte[] data)  {
        long daysSinceEpoch = decodeVarLong(data); // 调用通用的长整型解码方法
        return LocalDate.ofEpochDay(daysSinceEpoch); // 使用天数创建 LocalDate 对象
    }

    public static byte[] encodeVarLocalDateTime(LocalDateTime datetime) {
        Date date = LocalDateTimeUtil.toDate(datetime);
        if (date==null){
            return null;
        }
        return encodeVarDate(date);
    }

    public static LocalDateTime decodeVarLocalDateTime(byte[] data)  {
        Date date = decodeVarDate(data);
        if (date==null){
            return null;
        }
        return LocalDateTimeUtil.toLocalDateTime(date);
    }

    public static LocalTime decodeVarLocalTime(byte[] data) {
        long nanoOfDay = decodeVarLong(data); // 调用通用的长整型解码方法
        return LocalTime.ofNanoOfDay(nanoOfDay); // 使用纳秒数创建 LocalTime 对象
    }

    public static byte[] encodeVarBigDecimal(BigDecimal bd) {
        String strValue = bd.toString(); // 将 BigDecimal 转换为字符串
        return strValue.getBytes(); // 将字符串转换为字节数组
    }

    public static BigDecimal decodeVarBigDecimal(byte[] data) {
        String strValue = new String(data); // 将字节数组转换为字符串
        return BigDecimalUtil.format(strValue); // 使用字符串创建 BigDecimal 对象
    }

    public static byte[] compressString(String str) throws IOException {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] lengthBytes = encodeVarInteger(bytes.length);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(lengthBytes);
        outputStream.write(bytes);

        return outputStream.toByteArray();
    }

    public static String decompressString(byte[] data) throws IOException {
        int length = decodeVarInteger(data);
        byte[] stringBytes = new byte[length];
        System.arraycopy(data, VarintLength(length), stringBytes, 0, length);
        return new String(stringBytes, StandardCharsets.UTF_8);
    }

    // 计算 Varint 编码长度所需的字节数
    public static int VarintLength(int value) {
        int length = 0;
        do {
            value >>>= 7;
            length++;
        } while (value != 0);
        return length;
    }


    // 将字节数组转换为十六进制字符串（辅助方法）
    public static String byteArrayToHexString(byte[] array) {
        StringBuilder builder = new StringBuilder();
        for (byte b : array) {
            builder.append(String.format("%02X ", b));
        }
        return builder.toString();
    }

    public static String maxString(){
        int maxLength = 65535; // 近似最大长度，减去一些以避免OutOfMemoryError
        StringBuilder sb = new StringBuilder(maxLength);
        for (int i = 0; i < maxLength; i++) {
            sb.append('a');
        }
        return sb.toString();
    }


    public static void main(String[] args) throws IOException {
        char originalChar = Character.MIN_VALUE; // 测试 Character.MAX_VALUE
        byte[] encodedChar = encodeVarChar(originalChar);
        char decodedChar = decodeVarChar(encodedChar);

        System.out.println("Original Char: " + (int)originalChar);
        System.out.println("Encoded Bytes: " + Arrays.toString(encodedChar));
        System.out.println("Decoded Char: " + (int)decodedChar);
    }
}
