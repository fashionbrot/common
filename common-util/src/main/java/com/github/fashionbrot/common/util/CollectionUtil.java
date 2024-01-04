package com.github.fashionbrot.common.util;

import lombok.extern.slf4j.Slf4j;


import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author fashionbrot
 */
@Slf4j
public class CollectionUtil {


    /**
     * 合并嵌套的集合，将所有元素合并到一个新集合中。
     *
     * @param nestedCollections 包含多个集合的嵌套集合
     * @param <T>              集合中元素的类型
     * @return 合并后的集合，如果输入为空，则返回 {@code null}
     */
    public static <T> Collection<T> marge(Collection<? extends Collection<T>> nestedCollections) {
        if (ObjectUtil.isEmpty(nestedCollections)){
            return null;
        }
        return nestedCollections.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }


    /**
     * 从给定的列表中筛选出第一个满足指定条件的元素，并返回该元素。
     *
     * @param <T>        列表元素的类型
     * @param list       要筛选的列表
     * @param predicate  用于筛选元素的谓词条件
     * @return           第一个满足条件的元素，如果没有找到则返回null
     */
    public static  <T> T filter(List<T> list, Predicate<? super T> predicate) {
        if (ObjectUtil.isEmpty(list)) {
            return null;
        }

        return list.stream()
                .filter(predicate)
                .findFirst()
                .orElse(null);
    }

    /**
     * 将传入的可变参数值转换为 List。
     *
     * @param values 传入的可变参数值
     * @param <T>    List 中元素的类型
     * @return 包含传入值的 List
     */
    public static <T> List<T> convertToList(T... values) {
        return new ArrayList<>(Arrays.asList(values));
    }


    /**
     * 检查传入的可变参数值是否与指定值相等。
     *
     * @param val    要比较的值
     * @param values 要检查相等性的可变参数值
     * @param <T>    值的类型
     * @return 如果任一可变参数值与指定值相等，则返回 true；否则返回 false
     */
    public static <T> boolean isEqualToAny(T val, T... values) {
        return values != null && Arrays.stream(values).anyMatch(value -> Objects.equals(val, value));
    }

    /**
     * 检查指定值是否存在于列表中。
     *
     * @param val  要检查的值
     * @param list 列表
     * @param <T>  值的类型
     * @return 如果值存在于列表中则返回 true，否则返回 false
     */
    public static <T> boolean containsValue(T val, List<T> list) {
        return list != null && list.contains(val);
    }

}
