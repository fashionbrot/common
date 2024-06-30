package com.github.fashionbrot.common.util;

import com.github.fashionbrot.common.enums.ClassTypeEnum;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
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



    public static boolean isCollection(Class type) {
        return type!=null && Collection.class.isAssignableFrom(type);
    }

    public static boolean isNotCollection(Class type){
        return !isCollection(type);
    }




    public static boolean isMap(Class type) {
        return type!=null && Map.class.isAssignableFrom(type);
    }

    public static boolean isNotMap(Class type){
        return !isMap(type);
    }



    public static boolean isObject(Class type){
        return type!=null && type.isAssignableFrom(Object.class);
    }


    public static boolean isNotObject(Class type){
        if (type==null){
            return false;
        }
        return !isObject(type);
    }


    public static boolean isArray(Class type) {
        return type!=null && type.isArray();
    }

    public static boolean isNotArray(Class type){
        return !isArray(type);
    }



    public static boolean isBoolean(Class type){
        return type!=null && (type == boolean.class ||type == Boolean.class);
    }

    public static boolean isNotBoolean(Class type){
        return !isBoolean(type);
    }

    public static boolean isByte(Class type){
        return type!=null && (type == byte.class ||type == Byte.class);
    }

    public static boolean isNotByte(Class type){
        return !isByte(type);
    }

    public static boolean isChar(Class type){
        return type!=null && (type == char.class ||type == Character.class);
    }

    public static boolean isNotChar(Class type){
        return !isChar(type);
    }

    public static boolean isString(Class type){
        return type!=null && (type == String.class ||type == CharSequence.class);
    }

    public static boolean isNotString(Class type){
        return !isString(type);
    }

    public static boolean isFloat(Class type) {
        return type != null && (type == float.class || type == Float.class);
    }

    public static boolean isNotFloat(Class type){
        return !isFloat(type);
    }

    public static boolean isInt(Class type) {
        return type != null && (type == int.class || type == Integer.class);
    }

    public static boolean isNotInt(Class type){
        return !isInt(type);
    }

    public static boolean isLong(Class type) {
        return type != null && (type == long.class || type == Long.class);
    }

    public static boolean isNotLong(Class type){
        return !isLong(type);
    }

    public static boolean isShort(Class type) {
        return type != null && (type == short.class || type == Short.class);
    }

    public static boolean isNotShort(Class type){
        return !isShort(type);
    }

    public static boolean isDouble(Class type) {
        return type != null && (type == double.class || type == Double.class);
    }

    public static boolean isNotDouble(Class type){
        return !isDouble(type);
    }

    public static boolean isBigDecimal(Class type) {
        return type != null && type == BigDecimal.class;
    }

    public static boolean isNotBigDecimal(Class type){
        return !isBigDecimal(type);
    }

    public static boolean isBigInteger(Class type) {
        return type != null && type == BigInteger.class;
    }

    public static boolean isNotBigInteger(Class type){
        return !isBigInteger(type);
    }

    public static boolean isDate(Class type){
        return type!=null && type == Date.class;
    }

    public static boolean isNotDate(Class type){
        return !isDate(type);
    }


}
