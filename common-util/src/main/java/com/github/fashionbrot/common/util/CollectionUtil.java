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
     * 在列表中根据给定的条件过滤元素，并返回第一个符合条件的元素。
     *
     * @param list      要过滤的列表
     * @param predicate 过滤条件
     * @return 第一个符合条件的元素，如果没有符合条件的元素则返回 {@code null}
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

}
