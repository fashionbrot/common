package com.github.fashionbrot.common.util;

import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fashionbrot
 */
@Slf4j
public class ListUtil {

    /**
     * 深度copy
     * @param src src
     * @param <T> T
     * @return List
     */
    public static <T> List<T> deepCopy(List<T> src)  {
        if (ObjectUtil.isNotEmpty(src)){
            return src.stream().collect(Collectors.toList());
        }
        return  null;
    }

}
