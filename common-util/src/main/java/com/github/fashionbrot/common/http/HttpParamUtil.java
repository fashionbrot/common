package com.github.fashionbrot.common.http;

import com.github.fashionbrot.common.util.JavaUtil;
import com.github.fashionbrot.common.util.MethodUtil;
import com.github.fashionbrot.common.util.ObjectUtil;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

public class HttpParamUtil {


    public static String formatGetUrl(String url,String params){
        if (ObjectUtil.isEmpty(params)){
            return url;
        }
        if (url.indexOf("?")>0){
            return url+"&"+params;
        }
        return url+"?"+params;
    }


    /**
     * 实体类对象转URL参
     * @param t 实体类对象
     * @param <T> 实体类泛型
     * @param ignoreNull 是否忽略空值
     * @return String
     */
    public static <T> String entityToUrlParam(T t,boolean ignoreNull){
        // URL 参数存储器
        StringBuffer urlParam = new StringBuffer();
        //扩展转换父类成员功能
        entitySuperclassToUrlParam(t, t.getClass(),urlParam,ignoreNull);
        if(urlParam.length()>0){
            //去除最后一个&字符
            urlParam.deleteCharAt(urlParam.length() - 1);
        }
        return urlParam.toString();
    }

    /**
     * 实体类对象转URL参
     * @param t 实体类对象
     * @param clazz 实体类类型
     * @param urlParam URL 参数存储器
     * @param ignoreNull 是否忽略空值
     * @param <T> 实体类泛型
     */
    public static <T> void entitySuperclassToUrlParam(T t,Class clazz,StringBuffer urlParam,boolean ignoreNull){
        if (clazz.equals(Object.class)){
            return;
        }
        if (JavaUtil.isPrimitive(clazz)){
            return;
        }

        Field[] declaredFields = clazz.getDeclaredFields();
        if (ObjectUtil.isEmpty(declaredFields)){
            return;
        }

        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            if (MethodUtil.isStaticOrFinal(field)) {
                continue;
            }
            if (!JavaUtil.isPrimitive(field.getType())){
                continue;
            }
            //获取成员值
            Object value = MethodUtil.getFieldValue(field,t);
            //成员值为 Null 时，则不处理
            if (value==null && ignoreNull) {
                urlParam.append(field.getName()).append("=").append(value).append("&");
            }else if (value!=null){
                urlParam.append(field.getName()).append("=").append(value).append("&");
            }
        }

        Class superclass = clazz.getSuperclass();
        if (superclass != null && JavaUtil.isNotObject(superclass)) {
            //获取父类类型
            clazz = clazz.getSuperclass();
            //递归调用，获取父类的处理结果
            entitySuperclassToUrlParam(t,clazz,urlParam,ignoreNull);
        }
    }


    public static String mapToUrlParam(Map<String,Object> paramMap,boolean ignoreNull){
        if (ObjectUtil.isEmpty(paramMap)){
            return ObjectUtil.EMPTY;
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


}
