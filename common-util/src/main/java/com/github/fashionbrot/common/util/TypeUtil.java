package com.github.fashionbrot.common.util;

import java.lang.reflect.*;

/**
 * @author fashionbrot
 */
public class TypeUtil {




    /**
     * 获取参数的实际类型参数。
     *
     * @param parameter 要获取实际类型参数的参数
     * @return 参数的实际类型参数数组，如果参数没有实际类型参数，则返回null
     */
    public static Type[] getActualTypeArguments(Parameter parameter){
        Type parameterizedType = parameter.getParameterizedType();
        if (parameterizedType!=null){
            return convertActualTypeArguments(parameterizedType);
        }
        return null;
    }



    /**
     * 获取字段的实际类型参数。
     *
     * @param field 要获取实际类型参数的字段，可以为null
     * @return 字段的实际类型参数数组，如果字段为null或没有实际类型参数，则返回null
     */
    public static Type[] getActualTypeArguments(Field field) {
        if (field == null) {
            return null;
        }

        Type parameterizedType = field.getGenericType();
        if (parameterizedType != null) {
            return convertActualTypeArguments(parameterizedType);
        }
        return null;
    }

    /**
     * 从给定的 Type 中提取实际的类型参数数组。
     *
     * @param type 要提取类型参数的 Type
     * @return 实际类型参数数组，如果类型不是 ParameterizedType，则返回 null
     */
    public static Type[] convertActualTypeArguments(Type type){
        if (type!=null){
            if ( type instanceof ParameterizedType) {
                return ((ParameterizedType) type).getActualTypeArguments();
            }else if (type instanceof Class){

            }
        }
        return null;
    }



    public static TypeVariable[] getTypeVariable(Parameter parameter){
        Type parameterizedType = parameter.getParameterizedType();
        if (parameterizedType!=null){
            return getTypeVariable(parameterizedType);
        }
        return null;
    }

    public static TypeVariable[] getTypeVariable(Field field){
        Type parameterizedType = field.getGenericType();
        if (parameterizedType!=null){
            return getTypeVariable(parameterizedType);
        }
        return null;
    }

    /**
     * 从给定的 Type 中提取泛型类型变量数组。
     *
     * @param type 要提取泛型类型变量的 Type
     * @return 泛型类型变量数组，如果无法提取则返回 null
     */
    public static TypeVariable[] getTypeVariable(Type type){
        if(type instanceof  Class){
            return ((Class<?>) type).getTypeParameters();
        }else if (type instanceof ParameterizedType){
            // 如果 Type 是 ParameterizedType，则获取原始类型，并返回其类型参数
            Class<?> rawType = (Class<?>) ((ParameterizedType) type).getRawType();
            return rawType.getTypeParameters();
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
        }
        return null;
    }


    /**
     * 在给定的泛型类型变量数组中查找指定类型变量名称，并返回其索引。
     *
     * @param typeVariables   泛型类型变量数组
     * @param fieldTypeName   要查找的类型变量名称
     * @return 类型变量名称的索引，如果未找到则返回 null
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

    public static Type getTypeByTypeName(Type[] types, TypeVariable<?>[] typeVariables, String fieldTypeName) {
        if (ObjectUtil.isNotEmpty(types) && ObjectUtil.isNotEmpty(typeVariables)) {
            Integer typeVariableIndex = getTypeVariableIndex(typeVariables, fieldTypeName);
            if (typeVariableIndex != null) {
                Type type = types[typeVariableIndex];
                return type;
            }
        }
        return null;
    }


    public static Type getFieldType(Field field,Type classType){
        Type fieldGenericType = field.getGenericType();
        if (fieldGenericType instanceof Class){
            return field.getType().getComponentType();
        }else if (fieldGenericType instanceof GenericArrayType){

            Type genericComponentType = ((GenericArrayType) fieldGenericType).getGenericComponentType();
            Type[] convertActualTypeArguments = TypeUtil.convertActualTypeArguments(classType);
            TypeVariable[] typeVariables = TypeUtil.getTypeVariable(classType);
            Type typeByTypeName = getTypeByTypeName(convertActualTypeArguments, typeVariables, genericComponentType.getTypeName());
            return typeByTypeName;

        }else if (fieldGenericType instanceof TypeVariable){

            Type[] convertActualTypeArguments = TypeUtil.convertActualTypeArguments(classType);
            TypeVariable[] typeVariables = TypeUtil.getTypeVariable(classType);
            Type typeByTypeName = getTypeByTypeName(convertActualTypeArguments, typeVariables, fieldGenericType.getTypeName());
            return typeByTypeName;

        }else if (fieldGenericType instanceof ParameterizedType){

            if (JavaUtil.isCollection(field.getType())){
                Type[] fieldActualTypeArguments = TypeUtil.convertActualTypeArguments(fieldGenericType);
                if (ObjectUtil.isNotEmpty(fieldActualTypeArguments)){
                    if (fieldActualTypeArguments[0] instanceof Class){
                        return fieldActualTypeArguments[0];
                    }else if (fieldActualTypeArguments[0] instanceof ParameterizedType) {
                        TypeVariable[] typeVariables = TypeUtil.getTypeVariable(classType);
                        Type[] convertActualTypeArguments = TypeUtil.convertActualTypeArguments(classType);
                        Type typeByTypeName = getTypeByTypeName(convertActualTypeArguments, typeVariables, fieldActualTypeArguments[0].getTypeName());
                        return typeByTypeName;
                    }else if (fieldActualTypeArguments[0] instanceof TypeVariable){
                        TypeVariable[] typeVariables = TypeUtil.getTypeVariable(classType);
                        Type[] convertActualTypeArguments = TypeUtil.convertActualTypeArguments(classType);
                        Type typeByTypeName = getTypeByTypeName(convertActualTypeArguments, typeVariables, fieldActualTypeArguments[0].getTypeName());
                        return typeByTypeName;
                    }
                }
            }

        }
        return null;

    }

    public static Class getFieldTypeClass(Field field,Type classType){
        Type fieldType = getFieldType(field, classType);
        if (fieldType!=null){
            return TypeUtil.convertTypeToClass(fieldType);
        }
        return null;
    }
}
