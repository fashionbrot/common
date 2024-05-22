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

}
