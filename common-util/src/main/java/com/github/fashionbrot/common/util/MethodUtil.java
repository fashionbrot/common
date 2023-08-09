package com.github.fashionbrot.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class MethodUtil {

    public static boolean filterField(Field field){
        if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())){
            return true;
        }
        return false;
    }


    public static Object getFieldValue(Field field,Object obj){
        if (field!=null){
            try {
                return field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
