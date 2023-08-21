package com.github.fashionbrot.common.util;


import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 树工具类
 * @author panzh
 */
public class TreeUtil {

    /**
     * list转tree
     *
     * @param <T> T
     * @param <R> R
     * @param <U> U
     * @param source           源list
     * @param idFunc           id
     * @param pIdFunc          父id
     * @param setChildListFunc childList 子节点list
     * @param setHasChildFunc  hasChildren 是否有子节点
     * @param rootPIdValue     设置根节点条件 rootPIdValue
     * @return <code>List</code>
     */
    public static <T, R, U> List<T> buildTree(List<T> source,
                                              Function<T, R> idFunc,
                                              Function<T, R> pIdFunc,
                                              BiConsumer<T, List<T>> setChildListFunc,
                                              BiConsumer<T, U> setHasChildFunc,
                                              Predicate<T> rootPIdValue) {
        if (ObjectUtil.isEmpty(source)) {
            return source;
        }
        Map<R, List<T>> map = source.stream().collect(Collectors.groupingBy(pIdFunc));
        Function<Boolean, Boolean> countFunc = f -> f;
        source.forEach(model -> {
            setChildListFunc.accept(model, map.get(idFunc.apply(model)));
            setHasChildFunc.accept(model, (U) (ObjectUtil.isEmpty(map.get(idFunc.apply(model))) ? countFunc.apply(false) : countFunc.apply(true)));
        });
        if (rootPIdValue != null) {
            return source.stream().filter(rootPIdValue).collect(Collectors.toList());
        }
        return source;
    }


    /**
     * list转tree 根据某个参数，升序排序
     * @param <T> T
     * @param <R> R
     * @param <U> U
     * @param source           源list
     * @param idFunc           id
     * @param pIdFunc          父id
     * @param setChildListFunc childList 子节点list
     * @param setHasChildFunc  hasChildren 是否有子节点
     * @param rootPIdValue     设置根节点条件 rootPIdValue
     * @param sortValue        设置排序条件
     * @return List
     */
    public static <T, R, U> List<T> buildAscTree(List<T> source,
                                                 Function<T, R> idFunc,
                                                 Function<T, R> pIdFunc,
                                                 BiConsumer<T, List<T>> setChildListFunc,
                                                 BiConsumer<T, U> setHasChildFunc,
                                                 Predicate<T> rootPIdValue,
                                                 Comparator<T> sortValue) {
        if (ObjectUtil.isEmpty(source)) {
            return source;
        }
        if (sortValue != null) {
            source = source.stream().sorted(sortValue).collect(Collectors.toList());
        }
        return buildTree(source, idFunc, pIdFunc, setChildListFunc, setHasChildFunc, rootPIdValue);
    }


    /**
     * list转tree 根据某个参数，降序排序
     * @param <T> T
     * @param <R> R
     * @param <U> U
     * @param source           源list
     * @param idFunc           id
     * @param pIdFunc          父id
     * @param setChildListFunc childList 子节点list
     * @param setHasChildFunc  hasChildren 是否有子节点
     * @param rootPIdValue     设置根节点条件 rootPIdValue
     * @param sortValue        设置排序条件
     * @return List
     */
    public static <T, R, U> List<T> buildDescTree(List<T> source, Function<T, R> idFunc, Function<T, R> pIdFunc, BiConsumer<T, List<T>> setChildListFunc, BiConsumer<T, U> setHasChildFunc, Predicate<T> rootPIdValue, Comparator<T> sortValue) {
        if (ObjectUtil.isEmpty(source)) {
            return source;
        }
        if (sortValue != null) {
            source = source.stream().sorted(sortValue.reversed()).collect(Collectors.toList());
        }
        return buildTree(source, idFunc, pIdFunc, setChildListFunc, setHasChildFunc, rootPIdValue);
    }


    public static <T> List<T> treeToList(List<T> source, Function<T, List<T>> getChildListFunc, BiConsumer<T, List<T>> setChildListFunc) {
        if (ObjectUtil.isEmpty(source)) {
            return source;
        }
        List<T> target = new ArrayList<>();
        innerTraverseTreeToList(source, target, getChildListFunc);
        target.forEach(model -> setChildListFunc.accept(model, null));
        return target;
    }


    private static <T> void innerTraverseTreeToList(List<T> source, List<T> target, Function<T, List<T>> getChildListFunc) {
        target.addAll(source);
        source.forEach(model -> {
            List<T> childList = getChildListFunc.apply(model);
            if (!ObjectUtil.isEmpty(childList)) {
                innerTraverseTreeToList(childList, target, getChildListFunc);
            }
        });
    }


    public static <T, R> void traverseTree(List<T> source, Function<T, List<T>> getChildListFunc, Function<T, R> getPropertyFunc, BiConsumer<T, R> setPropertyFunc) {
        source.forEach(model -> {
            setPropertyFunc.accept(model,  getPropertyFunc.apply(model));
            List<T> childList = getChildListFunc.apply(model);
            if (ObjectUtil.isNotEmpty(childList)) {
                traverseTree(childList, getChildListFunc, getPropertyFunc, setPropertyFunc);
            }
        });
    }


    public static <T, R> List<T> searchTree(List<T> source, Function<T, List<T>> getChildListFunc, BiConsumer<T, List<T>> setChildListFunc, Function<T, R> getPropertyFunc, String keyWord) {
        List<T> resultList = new ArrayList<T>();
        source.forEach(model -> {
            R r = getPropertyFunc.apply(model);
            if (r != null && r.toString().contains(keyWord)) {
                resultList.add(model);
            } else {
                List<T> childList = getChildListFunc.apply(model);
                if (ObjectUtil.isNotEmpty(childList)) {
                    List<T> subList = searchTree(childList, getChildListFunc, setChildListFunc, getPropertyFunc, keyWord);
                    if (ObjectUtil.isNotEmpty(subList)) {
                        setChildListFunc.accept(model, subList);
                        resultList.add(model);
                    }
                }
            }
        });
        return resultList;
    }


    public static <T, R> List<T> findChildList(List<T> source, Function<T, R> idFunc, Function<T, R> pIdFunc, Predicate<R> rootPIdValue) {
        if (ObjectUtil.isEmpty(source)) {
            return source;
        }
        Map<R, List<T>> map = source.stream().collect(Collectors.groupingBy(pIdFunc));
        Map<R, List<T>> map2 = new HashMap<>();
        source.forEach(model -> {
            R r = idFunc.apply(model);
            List<T> childList = map.get(r);
            if (ObjectUtil.isNotEmpty(childList)) {
                List<T> tList = map2.computeIfAbsent(r, k -> new ArrayList<>());
                tList.addAll(childList);
            }
        });
        return map2.entrySet().stream().filter(t -> rootPIdValue.test(t.getKey())).map(Map.Entry::getValue).findAny().orElse(new ArrayList<>());
    }


}
