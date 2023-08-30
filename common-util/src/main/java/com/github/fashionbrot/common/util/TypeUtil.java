package com.github.fashionbrot.common.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.*;

/**
 * @author fashionbrot
 */
@Slf4j
public class TypeUtil {



    /**
     * 从方法参数中获取实际类型参数。
     *
     * @param parameter 方法参数对象。
     * @return 实际类型参数数组，如果未找到则返回 null。
     */
    public static Type[] getActualTypeArguments(Parameter parameter){
        if (parameter==null){
            return null;
        }
        Type parameterizedType = parameter.getParameterizedType();
        if (parameterizedType!=null){
            return getActualTypeArguments(parameterizedType);
        }
        return null;
    }

    /**
     * 从字段中获取实际类型参数。
     *
     * @param field 字段对象。
     * @return 实际类型参数数组，如果未找到则返回 null。
     */
    public static Type[] getActualTypeArguments(Field field){
        if (field==null){
            return null;
        }
        Type parameterizedType = field.getGenericType();
        if (parameterizedType!=null){
            return getActualTypeArguments(field.getGenericType());
        }
        return null;
    }

    /**
     * 从 ParameterizedType 中获取实际类型参数。
     *
     * @param type ParameterizedType 对象。
     * @return 实际类型参数数组，如果未找到则返回 null。
     */
    public static Type[] getActualTypeArguments(Type type){
        if (type==null){
            return null;
        }
        if (type instanceof ParameterizedType){
            return ((ParameterizedType) type).getActualTypeArguments();
        }else if (type instanceof TypeVariable){
            return  ((TypeVariable) type).getBounds();
        }
        return null;
    }




    /**
     * 从给定的方法参数中获取泛型类型的类型参数。
     *
     * @param parameter 要提取类型参数的方法参数。
     * @return 泛型类型的类型参数数组，如果未找到则返回 null。
     */
    public static TypeVariable[] getTypeVariables(Parameter parameter){
        if (parameter==null){
            return null;
        }
        Type parameterizedType = parameter.getParameterizedType();
        if (parameterizedType!=null){
            return getTypeVariables(parameterizedType);
        }
        return null;
    }

    /**
     * 从给定的字段中获取泛型类型的类型参数。
     *
     * @param field 要提取类型参数的字段。
     * @return 泛型类型的类型参数数组，如果未找到则返回 null。
     */
    public static TypeVariable[] getTypeVariables(Field field){
        if (field==null) {
            return null;
        }
        Type parameterizedType = field.getGenericType();
        if (parameterizedType!=null){
            return getTypeVariables(parameterizedType);
        }
        return null;
    }


    /**
     * 从给定的 Type 对象中获取类型参数。
     *
     * @param type 要提取类型参数的 Type 对象。
     * @return 类型参数的 TypeVariable 数组，如果未找到则返回 null。
     */
    public static TypeVariable[] getTypeVariables(Type type) {
        if (type==null){
            return null;
        }
        if (type instanceof Class) {
            // 对于普通 Class 类型，直接获取类型参数
            return ((Class<?>) type).getTypeParameters();
        } else if (type instanceof ParameterizedType) {
            // 对于 ParameterizedType，获取原始类型的类型参数
            Type rawType = ((ParameterizedType) type).getRawType();
            if (rawType!=null && rawType instanceof Class) {
                return ((Class<?>) rawType).getTypeParameters();
            }
        } else if (type instanceof GenericArrayType) {
            // 对于 GenericArrayType，递归获取数组元素的类型参数
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return getTypeVariables(componentType);
        }
        return null;
    }

    /**
     * 将给定的 Type 转换为对应的 Class。
     *
     * @param type 要转换的 Type
     * @return 转换后的 Class，如果无法转换则返回 null
     */
    public static Class<?> convertTypeToClass(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }else if (type instanceof GenericArrayType){
            // 对于 GenericArrayType，递归获取数组元素的类型参数
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return convertTypeToClass(componentType);
        }
        return null;
    }


    /**
     * 在给定的 TypeVariable 数组中查找特定类型变量的索引。
     *
     * @param typeVariables TypeVariable 数组，可能包含类型变量。
     * @param fieldTypeName 要查找的类型变量的类型名称。
     * @return 类型变量的索引，如果未找到则返回 null。
     */
    public static Integer getTypeVariableIndex(TypeVariable<?>[] typeVariables, String fieldTypeName) {
        if (ObjectUtil.isNotEmpty(typeVariables) && ObjectUtil.isNotEmpty(fieldTypeName)) {
            for (int i = 0; i < typeVariables.length; i++) {
                TypeVariable<?> typeVariable = typeVariables[i];
                if (typeVariable.getTypeName().equals(fieldTypeName)) {
                    return i;
                }
            }
        }
        return null;
    }


    /**
     * 获取给定类型变量对应的实际类型。
     *
     * @param types         Type 数组，包含类型信息。
     * @param typeVariables TypeVariable 数组，可能包含类型变量。
     * @param typeVariableName 要获取对应实际类型的类型变量的名称。
     * @return 给定类型变量的实际类型，如果未找到则返回 null。
     */
    public static Type getActualTypeForVariable(Type[] types, TypeVariable<?>[] typeVariables, String typeVariableName) {
        if (ObjectUtil.isNotEmpty(types) && ObjectUtil.isNotEmpty(typeVariables)) {
            Integer typeVariableIndex = getTypeVariableIndex(typeVariables, typeVariableName);
            if (typeVariableIndex != null) {
                return types[typeVariableIndex];
            }
        }
        return null;
    }


    /**
     * 根据给定的类类型和泛型类型，获取泛型类型的实际类型。
     *
     * @param classType  给定的类类型。
     * @param genericType 泛型类型。
     * @return 泛型类型的实际类型，如果未找到则返回 null。
     */
    public static Type getActualTypeArgument(Type classType,Type genericType){
        if (genericType==null){
            return null;
        }
        return getActualTypeArgument(classType,genericType.getTypeName());
    }

    /**
     * 根据给定的类类型和泛型类型名称，获取泛型类型的实际类型。
     *
     * @param classType        给定的类类型。
     * @param genericTypeName  泛型类型的名称。
     * @return 泛型类型的实际类型，如果未找到则返回 null。
     */
    public static Type getActualTypeArgument(Type classType, String genericTypeName) {
        if (ObjectUtil.isNotEmpty(genericTypeName)) {
            Type[] actualTypeArguments = TypeUtil.getActualTypeArguments(classType);
            TypeVariable[] typeVariables = TypeUtil.getTypeVariables(classType);
            return getActualTypeForVariable(actualTypeArguments, typeVariables, genericTypeName);
        }
        return null;
    }


    /**
     * 获取泛型实际类型的 Class Type 对象。
     *
     * @param classType    使用泛型的类或接口的 Type 对象
     * @param genericType  泛型类型的 Type 对象
     * @return 泛型实际类型的 Class 对象，如果无法解析则返回 null
     */
    public static Type getActualType(Type classType,Type genericType){
        if (classType==null || genericType==null){
            return null;
        }
        if (genericType instanceof Class){
            return ((Class<?>) genericType).getComponentType();
        }else if ( genericType instanceof GenericArrayType){
            Type genericComponentType = ((GenericArrayType) genericType).getGenericComponentType();
            return getActualType(classType,genericComponentType);
        }else if (genericType instanceof TypeVariable){
            return getActualType(classType,genericType);
        }else if (genericType instanceof ParameterizedType){
            if (JavaUtil.isCollection(genericType.getClass())){
                Type[] actualTypeArguments = getActualTypeArguments(genericType);
                if (ObjectUtil.isNotEmpty(actualTypeArguments)){
                    Type actualTypeArgument = actualTypeArguments[0];
                    return getActualType(classType,actualTypeArgument);
                }
            }
        }else{
            log.error("getActualTypeClass error genericType:{}",genericType.toString());
        }
        return null;
    }

    /**
     * 获取泛型实际类型的 Class 对象。
     *
     * @param classType    使用泛型的类或接口的 Type 对象
     * @param genericType  泛型类型的 Type 对象
     * @return 泛型实际类型的 Class 对象，如果无法解析则返回 null
     */
    public static Class getActualTypeClass(Type classType,Type genericType){
        Type actualType = getActualType(classType, genericType);
        if (actualType!=null){
            return convertTypeToClass(actualType);
        }
        return null;
    }

}
