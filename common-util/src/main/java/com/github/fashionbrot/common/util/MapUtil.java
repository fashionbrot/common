package com.github.fashionbrot.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fashionbrot
 */
public class MapUtil {

    /**
     * 创建一个包含键值对的 Map。
     *
     * @param kv 键值对（key-value）参数，依次为 key1, value1, key2, value2...
     * @return 创建的 Map 对象。
     * @throws RuntimeException 如果传入的键值对数量不是偶数，将抛出异常。
     */
    public static Map<String, Object> createMap(Object... kv) {
        int initialCapacity = 16;
        // 如果参数不为空
        if (ObjectUtil.isNotEmpty(kv)) {
            // 检查参数数量是否为偶数，如果不是则抛出异常
            if (kv.length % 2 != 0) {
                throw new RuntimeException("kv length%2!=0");
            }
            // 根据参数数量的一半来设置初始容量
            initialCapacity = kv.length / 2;
        }

        // 创建一个 HashMap 并设置初始容量
        Map<String, Object> map = new HashMap<>(initialCapacity);
        if (ObjectUtil.isNotEmpty(kv)) {
            for (int i = 0; i < kv.length; i += 2) {
                // 将键值对添加到 Map 中，键使用 ObjectUtil.formatString 进行格式化
                map.put(ObjectUtil.formatString(kv[i]), kv[i + 1]);
            }
        }
        return map;
    }

}
