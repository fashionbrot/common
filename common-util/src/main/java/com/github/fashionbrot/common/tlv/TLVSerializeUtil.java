package com.github.fashionbrot.common.tlv;

import com.github.fashionbrot.common.tlv.annotation.TLVField;
import com.github.fashionbrot.common.util.ByteUtil;
import com.github.fashionbrot.common.util.JavaUtil;
import com.github.fashionbrot.common.util.MethodUtil;
import com.github.fashionbrot.common.util.ObjectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author fashionbrot
 */
public class TLVSerializeUtil {
//
    public static byte[] serialize(Object input){
        if (input==null){
            return null;
        }
        Class<?> inputClass = input.getClass();
        return serialize(input,inputClass);
    }

    public static byte[] serialize(Object input,Class inputClass){

        List<byte[]> byteList=new ArrayList<>();
        if (TypeHandleFactory.isPrimitive(inputClass)){
            byteList.add(primitiveToBytes(input,inputClass));
        }else if (List.class.isAssignableFrom(inputClass)){
            byteList.add(listToBytes(input));
        }else if (inputClass.isArray()){
            byteList.add(arrayToBytes(input));
        }else{
            byteList.add(entityToBytes(input));;
        }
        return ByteUtil.mergeByteArrayList(byteList);
    }


    private static byte[] primitiveToBytes(Object input,Class inputClass){
        byte[] valueBytes = TypeHandleFactory.toByte(inputClass, input);
        byte tag = generateTag(inputClass, valueBytes);
        // 计算总长度
        byte[] lengthBytes = TLVTypeUtil.encodeVarInteger(valueBytes.length);
        int totalLength = 1 + lengthBytes.length + valueBytes.length;
        // 创建结果数组
        byte[] result = new byte[totalLength];
        // 填充结果数组
        int currentIndex = 0;
        // 添加 tag
        result[currentIndex++] = tag;
        // 添加 length
        for (byte b : lengthBytes) {
            result[currentIndex++] = b;
        }
        // 添加 valueBytes
        for (byte b : valueBytes) {
            result[currentIndex++] = b;
        }
        return result;
    }

    private static byte[] listOrArrayToByte(byte[] listValue,Class type){
        byte tag = generateTag(type, listValue);
        // 计算总长度
        byte[] lengthBytes = TLVTypeUtil.encodeVarInteger(listValue.length);
        int totalLength = 1 + lengthBytes.length + listValue.length;
        // 创建结果数组
        byte[] result = new byte[totalLength];
        // 填充结果数组
        int currentIndex = 0;
        // 添加 tag
        result[currentIndex++] = tag;
        // 添加 length
        for (byte b : lengthBytes) {
            result[currentIndex++] = b;
        }
        // 添加 valueBytes
        for (byte b : listValue) {
            result[currentIndex++] = b;
        }
        return result;
    }

    private static byte[] entityToBytes(Object input){
        Class<?> inputClass = input.getClass();
        List<Field> fieldList = getSortedClassField(inputClass);
        if (fieldList == null || fieldList.isEmpty()) {
            return ByteUtil.BYTE_ARRAY_EMPTY;
        }

        List<byte[]> byteList=new ArrayList<>();
        for (Field field : fieldList) {
            TLVField annotation = field.getAnnotation(TLVField.class);
            if (annotation!=null && !annotation.serialize()){
                continue;
            }
            Class<?> fieldType = field.getType();
            if (List.class.isAssignableFrom(fieldType)){
                Object fieldValue = MethodUtil.getFieldValue(field, input);
                byte[] serialize = serialize(fieldValue,fieldType);
                byteList.add(listOrArrayToByte(serialize, fieldType));
            }else if (fieldType.isArray()){
                Object fieldValue = MethodUtil.getFieldValue(field, input);
                byte[] serialize = serialize(fieldValue,fieldType);
                byteList.add(listOrArrayToByte(serialize, fieldType));
            }else{
                Object fieldValue = MethodUtil.getFieldValue(field, input);
                byte[] serialize = serialize(fieldValue,fieldType);
                byteList.add(serialize);
            }


        }
        return ByteUtil.mergeByteArrayList(byteList);
    }

    private static byte[] listToBytes(Object input){
        if (input==null){
            return null;
        }

        List<Object> objectList = (List<Object>) input;
        if (objectList != null && objectList.size() == 0) {
            return ByteUtil.BYTE_ARRAY_ONE;
        }
        List<byte[]> byteList = new ArrayList<>();
        for (Object obj : objectList) {
            byteList.add(serialize(obj,obj.getClass()));
        }
        return ByteUtil.mergeByteArrayList(byteList);
    }

    private static byte[] arrayToBytes(Object input){
        if (input==null){
            return null;
        }
        Object[] arrayValue = (Object[])input;
        if (arrayValue!=null && arrayValue.length==0){
            return ByteUtil.BYTE_ARRAY_ONE;
        }
        List<byte[]> byteList=new ArrayList<>();
        for (Object array : arrayValue) {
            byteList.add(serialize(array,array.getClass()));
        }
        return ByteUtil.mergeByteArrayList(byteList);
    }


    public static byte generateTag(Class classType,byte[] valueBytes){
        BinaryType binaryType = BinaryType.getBinaryType(classType);
        String valueByteLengthBinary = BinaryCodeLength.getBinaryCode(TLVTypeUtil.encodeVarInteger(ObjectUtil.isNotEmpty(valueBytes)?valueBytes.length:0).length);
        return ByteUtil.binaryStringToByte(binaryType.getBinaryCode() + valueByteLengthBinary);
    }



    public static List<Field> getSortedClassField(Class clazz){
        //TODO 需要缓存
        List<FieldModel> classFieldList = getNonStaticNonFinalFieldModels(clazz);
        List<FieldModel> superClassField = getSuperClassField(clazz);
        if (ObjectUtil.isNotEmpty(superClassField)){
            classFieldList.addAll(superClassField);
        }
        Collections.sort(classFieldList, Comparator.comparing(FieldModel::getIndex).thenComparing(f -> f.getField().getName()));
        List<Field> fieldList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(classFieldList)){
            for (FieldModel fieldModel : classFieldList) {
                fieldList.add(fieldModel.getField());
            }
        }
        return fieldList;
    }


    public static List<FieldModel> getNonStaticNonFinalFieldModels(Class<?> clazz) {
        List<FieldModel> fieldList = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (MethodUtil.isStaticOrFinal(declaredField)) {
                continue;
            }
            TLVField annotation = declaredField.getAnnotation(TLVField.class);
            if (annotation!=null && !annotation.serialize()){
                continue;
            }
            int index = annotation != null ? annotation.index() : Integer.MAX_VALUE;
            fieldList.add(FieldModel.builder().index(index).field(declaredField).build());
        }
        return fieldList;
    }

    public static List<FieldModel> getSuperClassField(Class clazz){
        Class superclass = clazz.getSuperclass();
        if (superclass != null && JavaUtil.isNotObject(superclass)) {
            List<FieldModel> classFieldList = getNonStaticNonFinalFieldModels(superclass);

            List<FieldModel> superClassFieldList = getSuperClassField(superclass);
            if (ObjectUtil.isNotEmpty(superClassFieldList)){
                classFieldList.addAll(superClassFieldList);
            }
            return classFieldList;
        }
        return null;
    }



}
