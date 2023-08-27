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
     * 构建树状结构数据。
     *
     * @param <T>             节点的类型
     * @param <R>             节点 ID 的类型
     * @param <U>             节点是否有子节点的标识类型
     * @param source          源列表
     * @param idFunc          从节点中提取 ID 的函数
     * @param pIdFunc         从节点中提取父节点 ID 的函数 pId不能为Null
     * @param setChildListFunc 设置子节点列表的函数
     * @param setHasChildFunc 设置是否有子节点的函数
     * @param rootPIdValue    判断根节点的条件，为 null 则不进行过滤
     * @return 构建好的树状结构数据列表
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
        // 使用父节点 ID 进行分组
        Map<R, List<T>> map = source.stream().collect(Collectors.groupingBy(pIdFunc));
        //// 用于判断是否有子节点的计数函数
        Function<Boolean, Boolean> countFunc = f -> f;
        // 遍历每个节点，设置子节点列表和是否有子节点的标识
        source.forEach(model -> {
            setChildListFunc.accept(model, map.get(idFunc.apply(model)));
            setHasChildFunc.accept(model, (U) (ObjectUtil.isEmpty(map.get(idFunc.apply(model))) ? countFunc.apply(false) : countFunc.apply(true)));
        });
        // 根据根节点条件进行过滤
        if (rootPIdValue != null) {
            return source.stream().filter(rootPIdValue).collect(Collectors.toList());
        }
        return source;
    }


    /**
     * 构建有序的树状结构数据。
     *
     * @param <T>             节点的类型
     * @param <R>             节点 ID 的类型
     * @param <U>             节点是否有子节点的标识类型
     * @param source          源列表
     * @param idFunc          从节点中提取 ID 的函数
     * @param pIdFunc         从节点中提取父节点 ID 的函数
     * @param setChildListFunc 设置子节点列表的函数
     * @param setHasChildFunc 设置是否有子节点的函数
     * @param rootPIdValue    判断根节点的条件，为 null 则不进行过滤
     * @param sortValue       对每一级的节点进行排序的比较器，为 null 则不排序
     * @return 构建好的有序树状结构数据列表
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
     * 构建降序排列的树状结构数据。
     *
     * @param <T>             节点的类型
     * @param <R>             节点 ID 的类型
     * @param <U>             节点是否有子节点的标识类型
     * @param source          源列表
     * @param idFunc          从节点中提取 ID 的函数
     * @param pIdFunc         从节点中提取父节点 ID 的函数
     * @param setChildListFunc 设置子节点列表的函数
     * @param setHasChildFunc 设置是否有子节点的函数
     * @param rootPIdValue    判断根节点的条件，为 null 则不进行过滤
     * @param sortValue       对每一级的节点进行降序排序的比较器，为 null 则不排序
     * @return 构建好的降序排列的树状结构数据列表
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


    /**
     * 将树状结构数据转换为列表。
     *
     * @param <T>             节点的类型
     * @param source          树状结构的源列表
     * @param getChildListFunc 获取节点的子节点列表的函数
     * @param setChildListFunc 设置节点的子节点列表的函数
     * @return 转换后的线性列表
     */
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


    /**
     * 遍历树状结构数据并为每个节点设置属性。
     *
     * @param <T>             节点的类型
     * @param <R>             属性值的类型
     * @param source          树状结构的源列表
     * @param getChildListFunc 获取节点的子节点列表的函数
     * @param getPropertyFunc  获取要设置的属性值的函数
     * @param setPropertyFunc 设置属性值的函数
     */
    public static <T, R> void traverseTree(List<T> source, Function<T, List<T>> getChildListFunc, Function<T, R> getPropertyFunc, BiConsumer<T, R> setPropertyFunc) {
        source.forEach(model -> {
            setPropertyFunc.accept(model,  getPropertyFunc.apply(model));
            List<T> childList = getChildListFunc.apply(model);
            if (ObjectUtil.isNotEmpty(childList)) {
                traverseTree(childList, getChildListFunc, getPropertyFunc, setPropertyFunc);
            }
        });
    }


    /**
     * 在树状结构数据中搜索包含特定关键字的节点。
     *
     * @param <T>             节点的类型
     * @param <R>             属性值的类型
     * @param source          树状结构的源列表
     * @param getChildListFunc 获取节点的子节点列表的函数
     * @param setChildListFunc 设置节点的子节点列表的函数
     * @param getPropertyFunc  获取要搜索的属性值的函数
     * @param keyWord         要搜索的关键字
     * @return 包含特定关键字的节点列表及其子节点
     */
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



    /**
     * 在树状结构数据中查找特定父节点的子节点列表。
     *
     * @param <T>             节点的类型
     * @param <R>             节点 ID 的类型
     * @param source          树状结构的源列表
     * @param idFunc          从节点中提取 ID 的函数
     * @param pIdFunc         从节点中提取父节点 ID 的函数
     * @param rootPIdValue    判断特定父节点的条件
     * @return 特定父节点的子节点列表
     */
    public static <T, R> List<T> findChildList(List<T> source, Function<T, R> idFunc, Function<T, R> pIdFunc, Predicate<R> rootPIdValue) {
        if (ObjectUtil.isEmpty(source)) {
            return source;
        }
        // 使用父节点 ID 进行分组
        Map<R, List<T>> map = source.stream().collect(Collectors.groupingBy(pIdFunc));
        // 使用节点 ID 进行分组，以备后续使用
        Map<R, List<T>> map2 = new HashMap<>();

        source.forEach(model -> {
            R r = idFunc.apply(model);
            List<T> childList = map.get(r);
            if (ObjectUtil.isNotEmpty(childList)) {
                List<T> tList = map2.computeIfAbsent(r, k -> new ArrayList<>());
                tList.addAll(childList);
            }
        });
        // 根据特定父节点条件，过滤并获取子节点列表
        return map2.entrySet().stream()
                .filter(t -> rootPIdValue.test(t.getKey()))
                .map(Map.Entry::getValue)
                .findAny()
                .orElse(new ArrayList<>());
    }


}
