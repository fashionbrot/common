package com.github.fashionbrot.common.util;


import java.lang.reflect.*;

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

    /**
     * 根据 Field 获取 属性值
     * @param field Field
     * @param object Object
     * @return Object
     */
    public static Object getFieldValue(Field field,Object object){
        if (field!=null){
            try {
                //设置可以操作私有成员
                field.setAccessible(true);
                return field.get(object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void setFieldValue(Field field,Object object,Object value){
        if (field!=null){
            try {
                //设置可以操作私有成员
                field.setAccessible(true);
                field.set(object,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Determine whether the given method is a "hashCode" method.
     * @see java.lang.Object#hashCode()
     * @param method Method
     * @return boolean
     */
    public static boolean isHashCodeMethod( Method method) {
        return method != null && method.getParameterCount() == 0 && method.getName().equals("hashCode");
    }

    /**
     * Determine whether the given method is a "toString" method.
     * @see java.lang.Object#toString()
     * @param method Method
     * @return boolean
     */
    public static boolean isToStringMethod( Method method) {
        return (method != null && method.getParameterCount() == 0 && method.getName().equals("toString"));
    }

    /**
     * Determine whether the given method is originally declared by {@link java.lang.Object}.
     * @param method Method
     * @return boolean
     */
    public static boolean isObjectMethod( Method method) {
        return (method != null && (method.getDeclaringClass() == Object.class ||
                isEqualsMethod(method) || isHashCodeMethod(method) || isToStringMethod(method)));
    }

    /**
     * Determine whether the given method is an "equals" method.
     * @see java.lang.Object#equals(Object)
     * @param method Method
     * @return boolean
     */
    public static boolean isEqualsMethod( Method method) {
        if (method == null) {
            return false;
        }
        if (method.getParameterCount() != 1) {
            return false;
        }
        if (!method.getName().equals("equals")) {
            return false;
        }
        return method.getParameterTypes()[0] == Object.class;
    }


    public static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("No default constructor found for " + clazz.getName(), e);
        }
    }

    public static <T> T[] newArrayInstance(Class<T> clazz, int length) {
        return (T[]) java.lang.reflect.Array.newInstance(clazz, length);
    }

}
