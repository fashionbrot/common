package com.github.fashionbrot.common.http;

import com.github.fashionbrot.common.util.JavaUtil;
import com.github.fashionbrot.common.util.MethodUtil;
import com.github.fashionbrot.common.util.ObjectUtil;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;

public class HttpParamUtil {


    /**
     * 格式化 GET 请求的 URL，将参数拼接到 URL 上。
     *
     * @param url    原始 URL。
     * @param params 参数字符串。
     * @return 格式化后的 URL。
     */
    public static String formatGetUrl(String url, String params) {
        if (ObjectUtil.isEmpty(params)) {
            return url; // 如果参数为空，直接返回原始 URL
        }

        // 使用 Java 的 StringJoiner 来拼接参数
        StringJoiner joiner = new StringJoiner("&");
        joiner.add(params);

        // 判断原始 URL 是否已经包含参数
        if (url.indexOf("?") > 0) {
            return url + "&" + joiner.toString();
        } else {
            return url + "?" + joiner.toString();
        }
    }



    /**
     * 将实体类对象转换为 URL 参数字符串。
     *
     * @param entity     实体类对象
     * @param ignoreNull 是否忽略空值字段
     * @param <T>        实体类的泛型
     * @return URL 参数字符串
     */
    public static <T> String entityToUrlParam(T entity, boolean ignoreNull) {
        StringBuilder urlParam = new StringBuilder();
        entityToUrlParamRecursive(entity, entity.getClass(), urlParam, ignoreNull);
        if (urlParam.length() > 0) {
            urlParam.deleteCharAt(urlParam.length() - 1);
        }
        return urlParam.toString();
    }

    private static <T> void entityToUrlParamRecursive(T entity, Class<?> clazz, StringBuilder urlParam, boolean ignoreNull) {
        if (clazz.equals(Object.class) || JavaUtil.isPrimitive(clazz)) {
            return;
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (MethodUtil.isStaticOrFinal(field) || !JavaUtil.isPrimitive(field.getType())) {
                continue;
            }

            field.setAccessible(true);
            try {
                Object value = field.get(entity);

                if (value != null || !ignoreNull) {
                    urlParam.append(field.getName()).append("=").append(value).append("&");
                }
            } catch (IllegalAccessException e) {
                // 忽略异常，继续处理下一个字段
            }
        }

        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null && JavaUtil.isNotObject(superclass)) {
            entityToUrlParamRecursive(entity, superclass, urlParam, ignoreNull);
        }
    }


    public static String mapToUrlParam2(Map<String,Object> paramMap,boolean ignoreNull){
        if (ObjectUtil.isEmpty(paramMap)){
            return "";
        }
        StringBuffer params=new StringBuffer();
        Iterator<Map.Entry<String, Object>> iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            if (JavaUtil.isPrimitive(value.getClass())){
                if (value==null && ignoreNull) {
                    params.append(key).append("=").append(value).append("&");
                }else if (value!=null){
                    params.append(key).append("=").append(value).append("&");
                }
            }
        }
        if(params.length()>0){
            //去除最后一个&字符
            params.deleteCharAt(params.length() - 1);
        }
        return params.toString();
    }


    /**
     * 将 Map 转换为 URL 参数字符串。
     *
     * @param paramMap   包含参数的 Map
     * @param ignoreNull 是否忽略空值字段
     * @return URL 参数字符串
     */
    public static String mapToUrlParam(Map<String, Object> paramMap, boolean ignoreNull) {
        if (ObjectUtil.isEmpty(paramMap)) {
            return "";
        }

        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (JavaUtil.isPrimitive(value.getClass())) {
                if (!ignoreNull || value != null) {
                    appendParam(params, key, value);
                }
            }
        }
        if (params.length() > 0) {
            params.deleteCharAt(params.length() - 1);
        }
        return params.toString();
    }

    private static void appendParam(StringBuilder params, String key, Object value) {
        params.append(key).append("=").append(value).append("&");
    }


}
