package com.github.fashionbrot.common.util;

import lombok.extern.slf4j.Slf4j;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author fashionbrot
 */
@Slf4j
public class ListUtil {


    public static <T> Collection<T> marge(Collection<? extends Collection<T>> nestedCollections) {
        if (ObjectUtil.isEmpty(nestedCollections)){
            return null;
        }
        return nestedCollections.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }



}
