package com.github.fashionbrot.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fashionbrot
 */
public class ByteUtil {

    public static final byte[] BYTE_ARRAY_EMPTY = new byte[]{};
    public static final byte[] BYTE_ARRAY_ONE = new byte[1];
    public static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();



    public static byte[][] splitByDoubleZeroBytes(byte[] originalArray) {
        List<byte[]> parts = new ArrayList<>();
        int start = 0;
        int length = originalArray.length;

        for (int i = 0; i < length - 1; i++) {
            if (originalArray[i] == 0 && originalArray[i + 1] == 0) {
                if (i - start > 0) { // 避免空数组的复制
                    parts.add(subarray(originalArray, start, i));
                }
                start = i + 2;
            }
        }

        // 添加最后一部分
        if (length - start > 0) { // 避免空数组的复制
            parts.add(subarray(originalArray, start, length));
        }

        // 将List转换为数组
        return parts.toArray(new byte[0][]);
    }

    // 从原数组中截取子数组
    private static byte[] subarray(byte[] array, int start, int end) {
        byte[] result = new byte[end - start];
        System.arraycopy(array, start, result, 0, end - start);
        return result;
    }

    public static byte binaryStringToByte(String binaryString) {
        if (binaryString.length() != 8) {
            throw new IllegalArgumentException("Binary string must be 8 bits long");
        }
        return (byte) Integer.parseInt(binaryString, 2);
    }


    public static String byteToBinaryString(byte value) {
        StringBuilder binaryString = new StringBuilder(8);
        for (int i = 7; i >= 0; i--) {
            binaryString.append((value >> i) & 1);
        }
        return binaryString.toString();
    }


    public static int binaryStringToDecimal(String binary) {
        if (binary.length() != 4) {
            throw new IllegalArgumentException("Binary string must be 4 bits long");
        }

        int result = 0;
        for (int i = 0; i < binary.length(); i++) {
            char bit = binary.charAt(i);
            if (bit == '1') {
                result |= (1 << (binary.length() - 1 - i));
            } else if (bit != '0') {
                throw new IllegalArgumentException("Invalid binary digit: " + bit);
            }
        }
        return result;
    }

    public static String toBinaryString(int decimal, int bitCount) {
        if (bitCount < 1) {
            throw new IllegalArgumentException("Bit count must be positive");
        }

        StringBuilder binaryString = new StringBuilder(bitCount);
        for (int i = bitCount - 1; i >= 0; i--) {
            int bit = (decimal >> i) & 1;
            binaryString.append(bit);
        }
        return binaryString.toString();
    }


    /**
     * 将字符转换为字节数组
     * @param c 要转换的字符
     * @return 转换后的字节数组
     */
    public static byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }

    /**
     * 将字节数组转换为字符
     * @param b 要转换的字节数组
     * @return 转换后的字符
     */
    public static char byteToChar(byte[] b) {
        int hi = (b[0] & 0xFF) << 8;
        int lo = b[1] & 0xFF;
        return (char) (hi | lo);
    }

    // 将 List 中的 byte[] 数组合并为一个单独的字节数组
    public static byte[] mergeByteArrayList(List<byte[]> list) {
        // 计算总长度
        int totalLength = 0;
        for (byte[] array : list) {
            if (array != null && array.length > 0) {
                totalLength += array.length;
            }
        }
        // 创建结果数组
        byte[] result = new byte[totalLength];
        // 合并字节数组
        int currentIndex = 0;
        for (byte[] array : list) {
            if (array != null && array.length > 0) {
                for (byte b : array) {
                    result[currentIndex++] = b;
                }
            }
        }
        return result;
    }


    public static final String bytesToHexStringWithSpaces(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length * 3); // 设置合理的初始容量，避免频繁扩容
        for (int i = 0; i < bArray.length; i++) {
            if (i > 0) {
                sb.append(' '); // 添加空格分隔符
            }
            int v = bArray[i] & 0xFF;
            sb.append(HEX_ARRAY[v >>> 4]); // 右移4位获取高位
            sb.append(HEX_ARRAY[v & 0x0F]); // 与运算获取低位
        }
        return sb.toString();
    }

}
