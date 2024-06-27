package com.github.fashionbrot.common.tlv;

import com.github.fashionbrot.common.tlv.annotation.TLVField;
import com.github.fashionbrot.common.util.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TLVUtil {

    private static final Map<Class, List<Field>> CLASS_CACHE = new ConcurrentHashMap<>();


    /**
     * 序列化
     * @param input  input 输入对象
     * @return       byte数组
     * @param <T>    泛型
     */
    public static <T> byte[] serialize(T input) {
        if (input == null) {
            return null;
        }
        Class<?> inputClass = input.getClass();
        return serialize(input, inputClass);
    }

    public static byte[] serialize(Object input, Class inputClass) {
        List<byte[]> byteList = new ArrayList<>();
        if (TypeHandleFactory.isPrimitive(inputClass)) {
            byteList.add(primitiveToBytes(input, inputClass));
        } else if (List.class.isAssignableFrom(inputClass)) {
            byteList.add(listToBytes(input));
        } else if (inputClass.isArray()) {
            byteList.add(arrayToBytes(input));
        } else {
            byteList.add(entityToBytes(input));
        }
        return ByteUtil.mergeByteArrayList(byteList);
    }


    private static byte[] primitiveToBytes(Object input, Class inputClass) {
        byte[] valueBytes = TypeHandleFactory.toByte(inputClass, input);
        return createTLV(valueBytes, inputClass);
    }

    private static byte[] listOrArrayToByte(byte[] listValue, Class type) {
        return createTLV(listValue, type);
    }

    private static byte[] createTLV(byte[] valueBytes, Class type) {
        byte tag = generateTag(type, valueBytes);
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

    private static byte[] entityToBytes(Object input) {
        Class<?> inputClass = input.getClass();
        List<Field> fieldList = getSortedClassField(inputClass);
        if (fieldList == null || fieldList.isEmpty()) {
            return ByteUtil.BYTE_ARRAY_EMPTY;
        }

        List<byte[]> byteList = new ArrayList<>();
        for (Field field : fieldList) {
            TLVField annotation = field.getAnnotation(TLVField.class);
            if (annotation != null && !annotation.serialize()) {
                continue;
            }
            Class<?> fieldType = field.getType();
            if (List.class.isAssignableFrom(fieldType)) {
                Object fieldValue = MethodUtil.getFieldValue(field, input);
                byte[] serialize = serialize(fieldValue, fieldType);
                byteList.add(listOrArrayToByte(serialize, fieldType));
            } else if (fieldType.isArray()) {
                Object fieldValue = MethodUtil.getFieldValue(field, input);
                byte[] serialize = serialize(fieldValue, fieldType);
                byteList.add(listOrArrayToByte(serialize, fieldType));
            } else {
                Object fieldValue = MethodUtil.getFieldValue(field, input);
                byte[] serialize = serialize(fieldValue, fieldType);
                byteList.add(serialize);
            }


        }
        return ByteUtil.mergeByteArrayList(byteList);
    }

    private static byte[] listToBytes(Object input) {
        if (input == null) {
            return null;
        }

        List<Object> objectList = (List<Object>) input;
        if (objectList != null && objectList.size() == 0) {
            return ByteUtil.BYTE_ARRAY_ONE;
        }
        List<byte[]> byteList = new ArrayList<>();
        for (Object obj : objectList) {
            byteList.add(serialize(obj, obj.getClass()));
        }
        return ByteUtil.mergeByteArrayList(byteList);
    }

    private static byte[] arrayToBytes(Object input) {
        if (input == null) {
            return null;
        }
        Object[] arrayValue = (Object[]) input;
        if (arrayValue != null && arrayValue.length == 0) {
            return ByteUtil.BYTE_ARRAY_ONE;
        }
        List<byte[]> byteList = new ArrayList<>();
        for (Object array : arrayValue) {
            byteList.add(serialize(array, array.getClass()));
        }
        return ByteUtil.mergeByteArrayList(byteList);
    }


    public static byte generateTag(Class classType, byte[] valueBytes) {
        BinaryType binaryType = BinaryType.getBinaryType(classType);
        String valueByteLengthBinary = BinaryCodeLength.getBinaryCode(TLVTypeUtil.encodeVarInteger(ObjectUtil.isNotEmpty(valueBytes) ? valueBytes.length : 0).length);
        return ByteUtil.binaryStringToByte(binaryType.getBinaryCode() + valueByteLengthBinary);
    }


    public static List<Field> getSortedClassField(Class clazz) {
        List<Field> cacheListField = CLASS_CACHE.get(clazz);
        if (cacheListField != null) {
            return cacheListField;
        }
        List<FieldModel> classFieldList = getNonStaticNonFinalFieldModels(clazz);
        List<FieldModel> superClassField = getSuperClassField(clazz);
        if (ObjectUtil.isNotEmpty(superClassField)) {
            classFieldList.addAll(superClassField);
        }
        Collections.sort(classFieldList, Comparator.comparing(FieldModel::getIndex).thenComparing(f -> f.getField().getName()));
        List<Field> fieldList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(classFieldList)) {
            for (FieldModel fieldModel : classFieldList) {
                fieldList.add(fieldModel.getField());
            }
        }
        CLASS_CACHE.putIfAbsent(clazz, fieldList);
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
            if (annotation != null && !annotation.serialize()) {
                continue;
            }
            int index = annotation != null ? annotation.index() : Integer.MAX_VALUE;
            fieldList.add(FieldModel.builder().index(index).field(declaredField).build());
        }
        return fieldList;
    }

    public static List<FieldModel> getSuperClassField(Class clazz) {
        Class superclass = clazz.getSuperclass();
        if (superclass != null && JavaUtil.isNotObject(superclass)) {
            List<FieldModel> classFieldList = getNonStaticNonFinalFieldModels(superclass);

            List<FieldModel> superClassFieldList = getSuperClassField(superclass);
            if (ObjectUtil.isNotEmpty(superClassFieldList)) {
                classFieldList.addAll(superClassFieldList);
            }
            return classFieldList;
        }
        return null;
    }


    /** ------------------------------------------反序列化-----------------------------------------------**/


    /**
     * 反序列化
     * @param deserializeClass 反序列化Class
     * @param data             byte数组
     * @return                 Class对应的对象
     * @param <T>              泛型
     */
    public static <T> T deserialize(Class<T> deserializeClass, byte[] data) {
        return deserialize(deserializeClass, deserializeClass, new ByteArrayReader(data));
    }

    public static <T> T deserialize(Class type, Class<T> deserializeClass, ByteArrayReader reader) {
        if (reader.isReadComplete()) {
            return null;
        }

        if (TypeHandleFactory.isPrimitive(type)) {
            byte[] nextBytes = getNextBytes(reader);
            return (T) TypeHandleFactory.toJava(deserializeClass, nextBytes);
        } else if (type.isAssignableFrom(Object.class)) {
            byte[] nextBytes = getNextBytes(reader);
            return (T) TypeHandleFactory.toJava(reader.getLastBinaryType().getType()[0], nextBytes);
        } else if (List.class.isAssignableFrom(type)) {
            return (T) deserializeList(deserializeClass, reader);
        } else if (type.isArray()) {
            return (T) deserializeArray(deserializeClass, reader);
        } else {
            return deserializeEntity(deserializeClass, reader);
        }
    }

    public static <T> List<T> deserializeList(Class<T> clazz, byte[] bytes) {
        return deserializeList(clazz, new ByteArrayReader(bytes));
    }

    public static <T> List<T> deserializeList(Class<T> clazz, ByteArrayReader reader) {
        List<T> list = new ArrayList<>();
        if (reader.isCollectionEmpty()) {
            return list;
        }
        while (!reader.isReadComplete()) {
            list.add(deserialize(clazz, clazz, reader));
        }
        return list;
    }

    public static <T> T[] deserializeArray(Class<T> clazz, byte[] bytes) {
        return deserializeArray(clazz, new ByteArrayReader(bytes));
    }

    public static <T> T[] deserializeArray(Class<T> clazz, ByteArrayReader reader) {
        if (reader.isCollectionEmpty()) {
            return MethodUtil.newArrayInstance(clazz, 0);
        }
        List<Object> list = new ArrayList<>();
        while (!reader.isReadComplete()) {
            list.add(deserialize(clazz, clazz, reader));
        }
        return list.toArray(MethodUtil.newArrayInstance(clazz, list.size()));
    }


    public static <T> T deserializeEntity(Class<T> clazz, ByteArrayReader reader) {
        T instance = MethodUtil.newInstance(clazz);
        List<Field> fieldList = getSortedClassField(clazz);
        if (ObjectUtil.isEmpty(fieldList)) {
            return instance;
        }
        for (Field field : fieldList) {
            if (reader.isReadComplete()) {
                break;
            }

            TLVField annotation = field.getAnnotation(TLVField.class);
            if (annotation != null && !annotation.serialize()) {
                continue;
            }

            Class type = field.getType();
            Class deserializeClass = field.getType();
            if (List.class.isAssignableFrom(type)) {
                deserializeClass = getListGenericClass(field);
                byte[] nextBytes = getNextBytes(reader);
                Object fieldValue = deserialize(type, deserializeClass, new ByteArrayReader(nextBytes));
                MethodUtil.setFieldValue(field, instance, fieldValue);
            } else if (type.isArray()) {
                deserializeClass = type.getComponentType();
                byte[] nextBytes = getNextBytes(reader);
                Object fieldValue = deserialize(type, deserializeClass, new ByteArrayReader(nextBytes));
                MethodUtil.setFieldValue(field, instance, fieldValue);
            } else {
                Object fieldValue = deserialize(type, deserializeClass, reader);
                MethodUtil.setFieldValue(field, instance, fieldValue);
            }

        }
        return instance;
    }

    public static byte[] getNextBytes(ByteArrayReader reader) {
        int readIndex = reader.getLastReadIndex();
        byte firstByte = reader.readFrom(readIndex);
        //第一位byte(前5个bit 是value数据类型 后3个bit 是valueByte.length 经过 varInt 压缩后的长度)
        String binaryString = ByteUtil.byteToBinaryString(firstByte);
        BinaryType valueType = BinaryType.fromBinaryCode(binaryString.substring(0, 5));
        int valueByteLengthLength = BinaryCodeLength.getLength(binaryString.substring(5, 8));

        reader.setLastBinaryType(valueType);

        int valueByteLength = TLVTypeUtil.decodeVarInteger(reader.readFromTo(readIndex + 1, readIndex + 1 + valueByteLengthLength));
        if (valueByteLength == 0) {
            return ByteUtil.BYTE_ARRAY_EMPTY;
        }
        return reader.readFromTo(readIndex + 1 + valueByteLengthLength, readIndex + 1 + valueByteLengthLength + valueByteLength);
    }


    public static Class getListGenericClass(Field field) {
        Type[] actualTypeArguments = TypeUtil.getActualTypeArguments(field);
        if (ObjectUtil.isNotEmpty(actualTypeArguments)) {
            return TypeUtil.convertTypeToClass(actualTypeArguments[0]);
        }
        return Object.class;
    }



}
