package com.github.fashionbrot.common.util;

import com.github.fashionbrot.common.enums.ClassTypeEnum;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;


public class JavaUtil {


    /**
     * Check if it is the basic data type of json data
     *
     * @param clazz clazz
     * @return boolean
     */
    public static boolean isPrimitive(Class clazz) {
        return ClassTypeEnum.checkClass(clazz.getTypeName());
    }

    public static boolean isNotPrimitive(Class clazz) {
        return !isPrimitive(clazz);
    }

    /**
     * Check if it is the basic data type of json data
     *
     * @param typeName clazz typeName
     * @return boolean
     */
    public static boolean isPrimitive(String typeName) {
        return ClassTypeEnum.checkClass(typeName);
    }

    public static boolean isNotPrimitive(String typeName) {
        return !isPrimitive(typeName);
    }

    /**
     * validate java collection
     *
     * @param  clazz  clazz
     * @return boolean
     */
    public static boolean isCollection(Class clazz) {

        return clazz!=null && Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isMap(Class clazz) {
        return clazz!=null && Map.class.isAssignableFrom(clazz);
    }

    /**
     * validate java collection
     *
     * @param type java typeName
     * @return boolean
     */
    public static boolean isCollection(String type) {
        switch (type) {
            case "java.util.List":
            case "java.util.LinkedList":
            case "java.util.ArrayList":
            case "java.util.Set":
            case "java.util.TreeSet":
            case "java.util.HashSet":
            case "java.util.SortedSet":
            case "java.util.Collection":
            case "java.util.ArrayDeque":
            case "java.util.PriorityQueue":
                return true;
            default:
                return false;
        }
    }

    public static boolean isObject(Class clazz){
        return "java.lang.Object".equals(clazz.getTypeName());
    }

    public static boolean isNotObject(Class clazz){
        if (clazz==null){
            return false;
        }
        return !isObject(clazz);
    }

    public static boolean isBaseType(Class clazz){
        if (clazz==null){
            return false;
        }
        return JavaUtil.isNotPrimitive(clazz.getTypeName());
    }

    /**
     * check array
     *
     * @param type type name
     * @return boolean
     */
    public static boolean isArray(String type) {
        return type.endsWith("[]");
    }

    /**
     * check array
     *
     * @param clazz type name
     * @return boolean
     */
    public static boolean isArray(Class clazz) {
        return clazz!=null && clazz.isArray();
    }


}