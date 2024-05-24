package com.github.fashionbrot.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class LvBufferTypeUtil {

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

    public static long decodeVarLong(byte[] data) throws IOException {
        long result = 0L;
        int shift = 0;
        int index = 0;
        byte b;
        do {
            if (index >= data.length) {
                throw new IOException("Varint decoding error: Insufficient data");
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

    public static double decodeVarDouble(byte[] data) throws IOException {
        long longValue = decodeVarLong(data);
        return Double.longBitsToDouble(longValue);
    }


    public static byte[] encodeVarDate(Date date) {
        long longValue = date.getTime(); // 获取 Date 对象的时间戳
        return encodeVarLong(longValue); // 调用通用的长整型编码方法
    }

    public static Date decodeVarDate(byte[] data) throws IOException {
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

    public static LocalDate decodeVarLocalDate(byte[] data) throws IOException {
        long daysSinceEpoch = decodeVarLong(data); // 调用通用的长整型解码方法
        return LocalDate.ofEpochDay(daysSinceEpoch); // 使用天数创建 LocalDate 对象
    }

    public static byte[] encodeVarLocalDateTime(LocalDateTime datetime) {
        byte[] dateBytes = encodeVarLocalDate(datetime.toLocalDate()); // 编码 LocalDate 部分
        byte[] timeBytes = encodeVarLocalTime(datetime.toLocalTime()); // 编码 LocalTime 部分
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(dateBytes, 0, dateBytes.length); // 将 LocalDate 字节表示写入输出流
        output.write(timeBytes, 0, timeBytes.length); // 将 LocalTime 字节表示写入输出流
        return output.toByteArray(); // 返回整个 LocalDateTime 的字节表示
    }

    public static LocalDateTime decodeVarLocalDateTime(byte[] data) throws IOException {
        byte[] dateBytes = new byte[4]; // LocalDate 部分的字节长度为 4
        byte[] timeBytes = new byte[data.length - 4]; // 剩余部分为 LocalTime 字节表示
        System.arraycopy(data, 0, dateBytes, 0, dateBytes.length); // 复制 LocalDate 字节表示
        System.arraycopy(data, dateBytes.length, timeBytes, 0, timeBytes.length); // 复制 LocalTime 字节表示
        LocalDate date = decodeVarLocalDate(dateBytes); // 解码 LocalDate 部分
        LocalTime time = decodeVarLocalTime(timeBytes); // 解码 LocalTime 部分
        return LocalDateTime.of(date, time); // 使用解码后的 LocalDate 和 LocalTime 创建 LocalDateTime 对象
    }

    public static LocalTime decodeVarLocalTime(byte[] data) throws IOException {
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


//    public static void main(String[] args) throws IOException {
//        byte[] bytes = encodeVarInteger(11);
//        int i = decodeVarInteger(bytes);
//        System.out.println(i);
//    }
}
