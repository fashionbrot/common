package com.github.fashionbrot.common.tlv;

import com.github.fashionbrot.common.date.LocalDateTimeUtil;
import com.github.fashionbrot.common.date.LocalDateUtil;
import com.github.fashionbrot.common.date.LocalTimeUtil;
import com.github.fashionbrot.common.util.BigDecimalUtil;
import com.github.fashionbrot.common.util.ObjectUtil;

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



    public static byte[] encodeVarLocalDate(LocalDate localDate) {
        Date date = LocalDateUtil.toDate(localDate);
        return encodeVarLong(date.getTime());
    }

    public static LocalDate decodeVarLocalDate(byte[] buffer)  {
        long varLong = decodeVarLong(buffer);
        return LocalDateUtil.toLocalDate(new Date(varLong));
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

    public static byte[] encodeVarLocalTime(LocalTime localTime) {
        if (localTime==null){
            return null;
        }
        Date date = LocalTimeUtil.toDate(localTime);
        return encodeVarLong(date.getTime());
    }

    public static LocalTime decodeVarLocalTime(byte[] data) {
        long varLong = decodeVarLong(data);
        return LocalTimeUtil.toLocalTime(new Date(varLong));
    }

    public static byte[] encodeVarBigDecimal(BigDecimal bd) {
        if (bd==null){
            return new byte[]{};
        }
        String value = BigDecimalUtil.toString(bd);
        return value.getBytes(StandardCharsets.UTF_8);
    }

    public static BigDecimal decodeVarBigDecimal(byte[] data) {
        if (ObjectUtil.isEmpty(data)){
            return null;
        }
        String strValue = new String(data,StandardCharsets.UTF_8);
        return BigDecimalUtil.format(strValue);
    }


    public static String maxString(){
        int maxLength = 65535; // 近似最大长度，减去一些以避免OutOfMemoryError
        StringBuilder sb = new StringBuilder(maxLength);
        for (int i = 0; i < maxLength; i++) {
            sb.append('a');
        }
        return sb.toString();
    }


}
