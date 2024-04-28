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
     * @param kv 键值对参数，以交替顺序出现，例如 key1, value1, key2, value2, ...
     * @return 包含键值对的 Map
     * @throws IllegalArgumentException 如果参数数量不为偶数
     */
    public static Map<String, Object> createMap(Object... kv) {
        if (kv.length % 2 != 0) {
            throw new IllegalArgumentException("Key-value pairs must be provided in even numbers.");
        }
        // 利用 LinkedHashMap 以保持插入顺序
        Map<String, Object> map = new HashMap<>((int) (kv.length / 1.5)); // 默认负载因子为0.75，适当调整初始容量

        for (int i = 0; i < kv.length; i += 2) {
            String key = ObjectUtil.toString(kv[i]);  // 使用 String.valueOf 防止空指针异常
            Object value = kv[i + 1];
            map.put(key, value);
        }

        return map;
    }

}
