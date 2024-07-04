package com.github.fashionbrot.common.util;

import com.github.fashionbrot.common.enums.ClassTypeEnum;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;

/**
 * java 对应工具类
 */
public class JavaUtil {



    /**
     * 检查一个类是否为基本数据类型。 {@link ClassTypeEnum}
     *
     * @param clazz 要检查的类
     * @return 如果类是基本数据类型，返回 true，否则返回 false
     */
    public static boolean isPrimitive(Class clazz) {
        return ClassTypeEnum.checkClass(clazz.getTypeName());
    }

    /**
     * 判断 Class  是基本类型 {@link ClassTypeEnum}
     * @param clazz Class
     * @return boolean
     */
    public static boolean isNotPrimitive(Class clazz) {
        return !isPrimitive(clazz);
    }

    /**
     * 判断 Class typeName 是基本类型  {@link ClassTypeEnum}
     * @param typeName Class typeName
     * @return boolean
     */
    public static boolean isPrimitive(String typeName) {
        return ClassTypeEnum.checkClass(typeName);
    }

    /**
     * 判断 Class typeName 不是基本类型 {@link ClassTypeEnum}
     * @param typeName Class typeName
     * @return boolean
     */
    public static boolean isNotPrimitive(String typeName) {
        return !isPrimitive(typeName);
    }

    /**
     * 判断Class 是集合
     * @param  clazz  clazz
     * @return boolean
     */
    public static boolean isCollection(Class clazz) {
        return clazz!=null && Collection.class.isAssignableFrom(clazz);
    }


    /**
     * 判断Class 是Map
     * @param clazz Class
     * @return boolean
     */
    public static boolean isMap(Class clazz) {
        return clazz!=null && Map.class.isAssignableFrom(clazz);
    }


    /**
     * 判断对象是 Object
     * @param clazz Class
     * @return boolean
     */
    public static boolean isObject(Class clazz){
        return clazz!=null && clazz == Object.class;
    }

    /**
     * 判断对象不是 Object
     * @param clazz Class
     * @return boolean
     */
    public static boolean isNotObject(Class clazz){
        if (clazz==null){
            return false;
        }
        return !isObject(clazz);
    }


    /**
     * 判断class TypeName 是Array
     * @param typeName Class TypeName
     * @return boolean
     */
    public static boolean isArray(String typeName) {
        return ObjectUtil.isNotEmpty(typeName) && typeName.endsWith("[]");
    }

    /**
     * 判断class TypeName 不是Array
     * @param typeName Class TypeName
     * @return boolean
     */
    public static boolean isNotArray(String typeName){
        return !isArray(typeName);
    }

    /**
     * 判断Class 是Array
     * @param clazz type name
     * @return boolean
     */
    public static boolean isArray(Class clazz) {
        return clazz!=null && clazz.isArray();
    }


    public static boolean isBooleanClass(Class type){
        return type!=null && (type == boolean.class ||type == Boolean.class);
    }

    public static boolean isByteClass(Class type){
        return type!=null && (type == byte.class ||type == Byte.class);
    }

    public static boolean isCharClass(Class type){
        return type!=null && (type == char.class ||type == Character.class);
    }

    public static boolean isStringClass(Class type){
        return type!=null && (type == String.class ||type == CharSequence.class);
    }

    public static boolean isFloatClass(Class type) {
        return type != null && (type == float.class || type == Float.class);
    }

    public static boolean isIntClass(Class type) {
        return type != null && (type == int.class || type == Integer.class);
    }

    public static boolean isLongClass(Class type) {
        return type != null && (type == long.class || type == Long.class);
    }

    public static boolean isShortClass(Class type) {
        return type != null && (type == short.class || type == Short.class);
    }

    public static boolean isDoubleClass(Class type) {
        return type != null && (type == double.class || type == Double.class);
    }

    public static boolean isBigDecimalClass(Class type) {
        return type != null && type == BigDecimal.class;
    }

    public static boolean isBigIntegerClass(Class type) {
        return type != null && type == BigInteger.class;
    }

}
