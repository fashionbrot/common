package com.github.fashionbrot.common.util;

import java.util.HashSet;
import java.util.Set;

public class SetUtil {


    /**
     * 初始化一个 Set，并添加传入的值。
     *
     * @param values 传入的可变参数值
     * @param <T>    Set 中元素的类型
     * @return 初始化并包含传入值的 Set
     */
    public static <T> Set<T> newSet(T... values) {
        Set<T> set = new HashSet<>();
        if (values != null) {
            for (T value : values) {
                if (value!=null) {
                    set.add(value);
                }
            }
        }
        return set;
    }

}
