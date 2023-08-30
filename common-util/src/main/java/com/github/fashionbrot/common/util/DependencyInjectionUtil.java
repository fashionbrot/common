package com.github.fashionbrot.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class DependencyInjectionUtil {

    private static Map<Class<?>, Supplier<?>> dependencies = new ConcurrentHashMap<>();

    /**
     * 注册依赖
     * @param type 依赖类型
     * @param supplier 依赖提供者
     * @param <T> 依赖类型
     */
    public static <T> void registerDependency(Class<T> type, Supplier<T> supplier) {
        dependencies.put(type, supplier);
    }

    /**
     * 获取依赖实例
     * @param type 依赖类型
     * @param <T> 依赖类型
     * @return 依赖实例
     */
    public static <T> T getDependency(Class<T> type) {
        Supplier<?> supplier = dependencies.get(type);
        if (supplier != null) {
            return type.cast(supplier.get());
        }
        return null;
    }

}
