package com.github.fashionbrot.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fashionbrot
 */
public class MapUtil {

    /**
     * 创建map
     * @param kv kv...
     * @return Map
     */
    public static Map<String,Object> put(Object... kv){
        int initialCapacity = 16;
        if (ObjectUtil.isNotEmpty(kv)){
            if(kv.length%2!=0) {
                throw new RuntimeException("kv length%2!=0");
            }
            initialCapacity =  kv.length/2;
        }
        Map<String,Object> map=new HashMap<>(initialCapacity);
        if (ObjectUtil.isNotEmpty(kv)) {
            for (int i = 0; i < kv.length; i += 2) {
                map.put(ObjectUtil.formatString(kv[i]), kv[i + 1]);
            }
        }
        return map;
    }

//    public static void main(String[] args) {
//        System.out.println(4%2);
//        System.out.println(put("1",1,"2",2L));
//    }
}
