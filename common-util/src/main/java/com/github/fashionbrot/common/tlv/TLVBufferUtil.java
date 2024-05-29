package com.github.fashionbrot.common.tlv;

import com.github.fashionbrot.common.tlv.parser.TypeHandle;
import com.github.fashionbrot.common.util.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class TLVBufferUtil {


    // Utility method to get generic type of a field (for List<>)
    private static Class<?> getGenericType(Field field) {
        java.lang.reflect.Type genericType = field.getGenericType();
        if (genericType instanceof java.lang.reflect.ParameterizedType) {
            java.lang.reflect.ParameterizedType pt = (java.lang.reflect.ParameterizedType) genericType;
            java.lang.reflect.Type[] fieldArgTypes = pt.getActualTypeArguments();
            if (fieldArgTypes.length > 0 && fieldArgTypes[0] instanceof Class) {
                return (Class<?>) fieldArgTypes[0];
            }
        }
        return Object.class;
    }

    public static <T> T deserializeNew(Class<T> clazz,byte[] data)  {
        return deserializeNew(clazz,new ByteArrayReader(data));
    }

    public static <T> T deserializeNew(Class<T> clazz,ByteArrayReader reader){
        if (clazz== Void.class){
            return null;
        }else if (JavaUtil.isObject(clazz) || JavaUtil.isPrimitive(clazz)) {
            byte[] valueBytes = getNextBytes(reader);
            BinaryType lastBinaryType = reader.getLastBinaryType();

            if (lastBinaryType == BinaryType.LIST) {
                return (T) decodeListValue(clazz, valueBytes);
            } else if (lastBinaryType == BinaryType.ARRAY) {
                return (T) decodeArrayValue(clazz, valueBytes);
            } else {
                return (T) TypeHandleFactory.toJava(reader.getLastBinaryType().getType(), valueBytes);
            }
        }else if (List.class.isAssignableFrom(clazz)) {
            return (T) deserializeList(clazz, reader);
        }else if (clazz.isArray()){
            return (T) deserializeArray(clazz,reader);
        }else {
            return deserializeEntity(clazz,reader);
        }
    }

    public static <T> T[] deserializeArray(Class<T> clazz,ByteArrayReader reader) {
        List<Object> list=new ArrayList<>();
        while (!reader.isReadComplete()){
            list.add(deserializeNew(clazz, reader));
        }
        return (T[]) list.toArray((Object[]) Array.newInstance(clazz, list.size()));
    }

    public static <T> List<T> deserializeList(Class<T> clazz,ByteArrayReader reader)  {
        List<T> list= new ArrayList<>();
        while (!reader.isReadComplete()){
            list.add(deserializeEntity(clazz, reader));
        }
        return list;
    }

    public static <T> T deserializeEntity(Class<T> clazz,ByteArrayReader reader){
        T instance = MethodUtil.newInstance(clazz);
        List<Field> fieldList = getSortedClassField(clazz);
        if (ObjectUtil.isEmpty(fieldList)){
            return instance;
        }
        for (Field field : fieldList) {
            byte[] valueBytes = getNextBytes(reader);
            Object objectValue;
            if (ObjectUtil.isNotEmpty(valueBytes)){
                if (List.class.isAssignableFrom(field.getType())){
                    Class listGenericClass = getListGenericClass(field);
                    objectValue = decodeListValue(listGenericClass,valueBytes);
                }else if (field.getType().isArray()){
                    Class<?> componentType = field.getType().getComponentType();
                    objectValue = decodeArrayValue(componentType,valueBytes);
                }else{
                    objectValue = TypeHandleFactory.toJava(field.getType(),valueBytes);
                }
            }else{
                objectValue = null;
            }
            MethodUtil.setFieldValue(field,instance,objectValue);
        }
        return instance;
    }


    public static byte[] getNextBytes(ByteArrayReader reader){
        int readIndex = reader.getLastReadIndex();
        byte firstByte = reader.readFrom(readIndex);
        //第一位byte(前5个bit 是value数量类型 后3个bit 是valueByte.length 经过 varInt 压缩后的长度)
        String binaryString = ByteUtil.byteToBinaryString(firstByte);
        BinaryType valueType = BinaryType.fromBinaryCode(binaryString.substring(0, 5));
        int valueByteLengthLength = BinaryCodeLength.getLength(binaryString.substring(5, 8));//LvBufferTypeUtil.encodeVarInteger().length;

        reader.setLastBinaryType(valueType);

        int valueByteLength = TLVTypeUtil.decodeVarInteger(reader.readFromTo(readIndex + 1, readIndex + 1 + valueByteLengthLength));
        if (valueByteLength==0){
            return new byte[]{};
        }
        return reader.readFromTo(readIndex +1+ valueByteLengthLength, readIndex +1 + valueByteLengthLength+ valueByteLength);
    }



    public static byte[] serializeNew(Object input){

        Class<?> clazz = input.getClass();
        List<byte[]> byteList=new ArrayList<>();
        if (JavaUtil.isPrimitive(clazz) || JavaUtil.isObject(clazz)){

            byte[] valueBytes = encodePrimitiveValue(clazz, input);

            byte tag = generateTag(clazz, valueBytes);
            byteList.add(new byte[]{tag});
            byteList.add(TLVTypeUtil.encodeVarInteger(valueBytes.length));
            byteList.add(valueBytes);

        }else if (List.class.isAssignableFrom(clazz)){
            byteList.add(encodeListValue(clazz, input));
        }else if (clazz.isArray()){
            byteList.add(encodeArrayValue(clazz,input));
        }else{
            List<Field> fieldList = getSortedClassField(clazz);
            for (Field field : fieldList) {

                Class<?> fieldType = field.getType();

                Object fieldValue = MethodUtil.getFieldValue(field, input);
                if (fieldValue==null){
                    byteList.add(TLVTypeUtil.encodeVarInteger(0));
                    byteList.add(new byte[]{0x00});
                }else{
                    byte[] valueBytes = encodeFieldValue(field,fieldValue);

                    byte tag = generateTag(fieldType, valueBytes);
                    //第一个是 valueType + valueByteLengthLength
                    byteList.add(new byte[]{tag});

                    byte[] valueByteLength = TLVTypeUtil.encodeVarInteger(valueBytes.length);
                    //第二个是 valueByteLength
                    byteList.add(valueByteLength);
                    //第三个是value byte
                    byteList.add(valueBytes);
                }

            }
        }

        return mergeByteArrayList(byteList);
    }



    private static byte[] encodeFieldValue(Field field,Object value) {
        Class<?> fieldType = field.getType();
        byte[] valueBytes ;
        if (List.class.isAssignableFrom(fieldType)) {
            Class listGenericClass = getListGenericClass(field);
            valueBytes = encodeListValue(listGenericClass,value);
        }else if (fieldType.isArray()){
            Class<?> componentType = field.getType().getComponentType();
            valueBytes = encodeArrayValue(componentType,value);
        }else{
            valueBytes = encodePrimitiveValue(fieldType,value);

        }
        return valueBytes;
    }

    public static Class getListGenericClass(Field field){
        Type[] actualTypeArguments = TypeUtil.getActualTypeArguments(field);
        return TypeUtil.convertTypeToClass(actualTypeArguments[0]);
    }



    public static byte generateTag(Class classType,byte[] valueBytes){
        BinaryType binaryType = BinaryType.getBinaryType(classType);
        String valueByteLengthBinary = BinaryCodeLength.getBinaryCode(TLVTypeUtil.encodeVarInteger(valueBytes.length).length);
        return ByteUtil.binaryStringToByte(binaryType.getBinaryCode() + valueByteLengthBinary);
    }



    public static byte[] mergeByteArrayList(List<byte[]> list) {
        // 将 List 中的 byte[] 数组转换为数组
        byte[][] arrays = new byte[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            arrays[i] = list.get(i);
        }

        // 调用 mergeByteArrays 方法进行合并
        return mergeByteArrays(arrays);
    }


    public static byte[] mergeByteArrays(byte[]... arrays) {
        int totalLength = 0;
        for (byte[] array : arrays) {
            totalLength += array.length;
        }
        byte[] result = new byte[totalLength];
        int currentIndex = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, currentIndex, array.length);
            currentIndex += array.length;
        }

        return result;
    }

    public static List<Field> getSortedClassField(Class clazz){
        List<Field> classFieldList = getNonStaticNonFinalFields(clazz);
        List<Field> superClassField = getSuperClassField(clazz);
        if (ObjectUtil.isNotEmpty(superClassField)){
            classFieldList.addAll(superClassField);
        }
        classFieldList.sort(Comparator.comparing(Field::getName));
        return classFieldList;
    }

    public static List<Field> getNonStaticNonFinalFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).filter(m-> !isStaticOrFinal(m)).collect(Collectors.toList());
    }

    public static List<Field> getSuperClassField(Class clazz){
        Class superclass = clazz.getSuperclass();
        if (superclass != null && JavaUtil.isNotObject(superclass)) {
            List<Field> classFieldList = getNonStaticNonFinalFields(superclass);

            List<Field> superClassFieldList = getSuperClassField(superclass);
            if (ObjectUtil.isNotEmpty(superClassFieldList)){
                classFieldList.addAll(superClassFieldList);
            }
            return classFieldList;
        }
        return null;
    }


    private static boolean isStaticOrFinal(Field field) {
        int modifiers = field.getModifiers();
        return Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers);
    }

    public static byte[] encodeListValue(Class genericClass, Object value) {
        //List [00000=type 000=[valueByteLength]Length] [valueByteLength] [value]
        List<Object> objectList = (List<Object>) value;
        if (objectList != null && objectList.size() == 0) {
            return new byte[1];
        }

        if (objectList == null) {
            return null;
        }
        if (ObjectUtil.isNotEmpty(objectList)) {
            List<byte[]> listByteList = new ArrayList<>();
            for (Object obj : objectList) {
                listByteList.add(serializeNew(obj));
            }
            return mergeByteArrayList(listByteList);
        } else {
            return new byte[1];
        }
    }


    public static byte[] encodeArrayValue(Class type ,Object fieldValue){
        Object[] arrayValue = (Object[])fieldValue;
        if (arrayValue!=null && arrayValue.length==0){
            return new byte[1];
        }
        if (ObjectUtil.isNotEmpty(arrayValue)){
            List<byte[]> arrayList=new ArrayList<>();
            for (Object o : arrayValue) {
                arrayList.add(serializeNew(o));
            }
            return mergeByteArrayList(arrayList);
        }
        return null;
    }


    public static byte[] encodePrimitiveValue(Class<?> type,Object value){
        TypeHandle typeHandle = TypeHandleFactory.getTypeHandle(type);
        return typeHandle.toByte(value);
    }

    public static Object decodeListValue(Class<?> genericClass ,byte[] bytes) {
        if (ObjectUtil.isEmpty(bytes)){
            return null;
        }
        List list = new ArrayList();
        if (bytes!=null && bytes.length==1 && bytes[0]==0x00){
            return list;
        }
        if (ObjectUtil.isNotEmpty(bytes)) {
            ByteArrayReader reader = new ByteArrayReader(bytes);
            while (!reader.isReadComplete()){
                list.add(deserializeNew(genericClass, reader));
            }
        }
        return list;
    }

    public static Object decodeArrayValue(Class genericClass,byte[] bytes)  {
        if (bytes!=null && bytes.length==1 && bytes[0]==0x00){
            return Array.newInstance(genericClass, 0);
        }
        List<Object> list=new ArrayList<>();
        if (ObjectUtil.isNotEmpty(bytes)){
            ByteArrayReader reader = new ByteArrayReader(bytes);
            while (!reader.isReadComplete()){
                list.add(deserializeNew(genericClass, reader));
            }
        }
        return list.toArray((Object[]) Array.newInstance(genericClass, list.size()));
    }




}
