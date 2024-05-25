package com.github.fashionbrot.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fashionbrot
 */
public class ByteUtil {


    public static void main(String[] args) {
        byte[] newArray=new byte[10];
        newArray[0]=0;
        newArray[1]=1;
        // 打印新的数组
        for (byte b : newArray) {
            System.out.print(b + " ");
        }
    }

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


}
