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

}
