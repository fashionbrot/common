package com.github.fashionbrot.common.util;

import java.util.Arrays;

public class ArraysUtil {

    /**
     * 合并两个类型为 T 的数组为一个数组。
     *
     * @param field1 第一个要合并的数组
     * @param field2 第二个要合并的数组
     * @param <T> 数组中元素的类型
     * @return 包含两个输入数组中所有元素的新数组
     */
    public static <T> T[] mergeFieldArrays(T[] field1, T[] field2) {
        // 创建一个新的数组，长度为 field1 和 field2 的总和
        T[] mergedArray = Arrays.copyOf(field1, field1.length + field2.length);

        // 复制 field2 到 mergedArray
        System.arraycopy(field2, 0, mergedArray, field1.length, field2.length);
        return mergedArray;
    }


}
