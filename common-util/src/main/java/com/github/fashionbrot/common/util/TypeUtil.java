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

    public static TypeVariable[] getTypeVariable(Type type){
        Class typeClass = null;
        if(type instanceof  Class){
            typeClass = (Class) type;
        }else if (type instanceof ParameterizedType){
            typeClass = (Class) ((ParameterizedType) type).getRawType();
        }
        if (typeClass!=null){
            return typeClass.getTypeParameters();
        }
        return null;
    }

    public static Class typeConvertClass(Type type) {
        Class typeClass = null;
        if(type instanceof  Class){
            typeClass = (Class) type;
        }else if (type instanceof ParameterizedType){
            typeClass = (Class) ((ParameterizedType) type).getRawType();
        }
        return typeClass;
    }

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
            return TypeUtil.typeConvertClass(fieldType);
        }
        return null;
    }
}
