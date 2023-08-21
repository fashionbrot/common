package com.github.fashionbrot.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MethodUtil {

    /**
     * 判断 Field 是否是 static 或者 final
     * @param field field
     * @return boolean
     */
    public static boolean isStaticOrFinal(Field field){
        return isStatic(field) || isFinal(field);
    }

    /**
     * 判断 Field 是否是 static
     * @param field field
     * @return boolean
     */
    public static boolean isStatic(Field field){
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * 判断 Field 是否是 final
     * @param field field
     * @return boolean
     */
    public static boolean isFinal(Field field){
        return Modifier.isFinal(field.getModifiers());
    }

    public static Object getFieldValue(Field field,Object obj){
        if (field!=null){
            try {
                //设置可以操作私有成员
                field.setAccessible(true);
                return field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}
