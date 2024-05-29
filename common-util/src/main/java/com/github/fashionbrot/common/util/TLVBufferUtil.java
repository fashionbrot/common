package com.github.fashionbrot.common.util;



import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public static <T> T deserializeNew(Class<T> clazz,byte[] data) throws IOException {
        return deserializeNew(clazz,new ByteArrayReader(data));
    }

    public static <T> T deserializeNew(Class<T> clazz,ByteArrayReader reader)throws IOException{
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
                return (T) decodePrimitiveValue(reader.getLastBinaryType().getType(), valueBytes);
            }
        }else if (List.class.isAssignableFrom(clazz)) {
            return (T) deserializeList(clazz, reader);
        }else if (clazz.isArray()){
            return (T) deserializeArray(clazz,reader);
        }else {
            return deserializeEntity(clazz,reader);
        }
    }

    public static <T> T[] deserializeArray(Class<T> clazz,ByteArrayReader reader) throws IOException {
        List<Object> list=new ArrayList<>();
        while (!reader.isReadComplete()){
            list.add(deserializeNew(clazz, reader));
        }
        return (T[]) list.toArray((Object[]) Array.newInstance(clazz, list.size()));
    }

    public static <T> List<T> deserializeList(Class<T> clazz,ByteArrayReader reader) throws IOException {
        List<T> list= new ArrayList<>();
        while (!reader.isReadComplete()){
            list.add(deserializeEntity(clazz, reader));
        }
        return list;
    }

    public static <T> T deserializeEntity(Class<T> clazz,ByteArrayReader reader)throws IOException{
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
                    objectValue = decodePrimitiveValue(field.getType(),valueBytes);
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

        int valueByteLength = TLVBufferTypeUtil.decodeVarInteger(reader.readFromTo(readIndex + 1, readIndex + 1 + valueByteLengthLength));
        if (valueByteLength==0){
            return new byte[]{};
        }
        return reader.readFromTo(readIndex +1+ valueByteLengthLength, readIndex +1 + valueByteLengthLength+ valueByteLength);
    }


    public static <T> T deserializeNew1(Class<T> clazz,byte[] data)throws IOException{
        T t = MethodUtil.newInstance(clazz);
        List<Field> fieldList = getSortedClassField(clazz);
        if (ObjectUtil.isEmpty(fieldList)){
            return t;
        }
        if (ObjectUtil.isEmpty(data)){
            return t;
        }

        int readIndex=0;
        for (Field field : fieldList) {

            byte firstByte = data[readIndex];
            //第一位byte(前5个bit 是value数量类型 后3个bit 是valueByte.length 经过 varInt 压缩后的长度)
            String binaryString = ByteUtil.byteToBinaryString(firstByte);
            BinaryType valueType = BinaryType.fromBinaryCode(binaryString.substring(0, 5));
            int valueByteLengthLength = BinaryCodeLength.getLength(binaryString.substring(5, 8));//LvBufferTypeUtil.encodeVarInteger().length;

            int valueByteLength = TLVBufferTypeUtil.decodeVarInteger(Arrays.copyOfRange(data, readIndex + 1, readIndex + 1 + valueByteLengthLength));
            if (valueByteLength==0){
                readIndex+=2;
                continue;
            }

            byte[] bytes = Arrays.copyOfRange(data, readIndex +1+ valueByteLengthLength, readIndex +1 + valueByteLengthLength+ valueByteLength);

            readIndex = readIndex +1 + valueByteLengthLength+ valueByteLength;

            Class<?> type = field.getType();

            Object value = decodePrimitiveValue(field.getType(),bytes);

            MethodUtil.setFieldValue(field,t,value);
        }
        return t;
    }

    public static <T> T deserialize(Class<T> clazz,byte[] data) throws IOException {
        List<Field> fieldList = getSortedClassField(clazz);
        if (ObjectUtil.isNotEmpty(fieldList)) {
            T t = MethodUtil.newInstance(clazz);

            int index=0;
            int readIndex=0;
            for (;;){
                if (index>=fieldList.size()){
                    break;
                }
                byte firstByte = data[readIndex];
                //第一位byte(前5个bit 是value数量类型 后3个bit 是valueByte.length 经过 varInt 压缩后的长度)
                String binaryString = ByteUtil.byteToBinaryString(firstByte);
                BinaryType valueType = BinaryType.fromBinaryCode(binaryString.substring(0, 5));
                int valueByteLengthLength = BinaryCodeLength.getLength(binaryString.substring(5, 8));//LvBufferTypeUtil.encodeVarInteger().length;

                short valueByteLength = TLVBufferTypeUtil.decodeVarShort(Arrays.copyOfRange(data, readIndex + 1, readIndex + 1 + valueByteLengthLength));

                if (valueByteLength==0){
                    readIndex+=1;
                    index++;
                    continue;
                }

                byte[] bytes = Arrays.copyOfRange(data, readIndex +1+ valueByteLengthLength, readIndex +1 + valueByteLengthLength+ valueByteLength);

                readIndex = readIndex +1 + valueByteLengthLength+ valueByteLength;

                Field field = fieldList.get(index);
                Class<?> type = field.getType();

                Object value = decodePrimitiveValue(field.getType(),bytes);

                MethodUtil.setFieldValue(field,t,value);

                index++;
            }
            return t;
        }
        return null;
    }


    public static byte[] serializeNew(Object input){

        Class<?> clazz = input.getClass();
        List<byte[]> byteList=new ArrayList<>();
        if (JavaUtil.isPrimitive(clazz) || JavaUtil.isObject(clazz)){

            byte[] valueBytes = encodePrimitiveValue(clazz, input);
            byte tag = generateTag(clazz, valueBytes);
            byteList.add(new byte[]{tag});
            byteList.add(TLVBufferTypeUtil.encodeVarInteger(valueBytes.length));
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
                    byteList.add(TLVBufferTypeUtil.encodeVarInteger(0));
                    byteList.add(new byte[]{0x00});
                }else{
                    byte[] valueBytes = encodeFieldValue(field,fieldValue);

                    byte tag = generateTag(fieldType, valueBytes);
                    //第一个是 valueType + valueByteLengthLength
                    byteList.add(new byte[]{tag});

                    byte[] valueByteLength = TLVBufferTypeUtil.encodeVarInteger(valueBytes.length);
                    //第二个是 valueByteLength
                    byteList.add(valueByteLength);
                    //第三个是value byte
                    byteList.add(valueBytes);
                }

            }
        }

//        byte tag = generateTag(fieldType, valueBytes);
//        //第一个是 valueType + valueByteLengthLength
//        list.add(new byte[]{tag});
//
//        byte[] valueByteLength = TLVBufferTypeUtil.encodeVarInteger(valueBytes.length);
//        //第二个是 valueByteLength
//        list.add(valueByteLength);
//        //第三个是value byte
//        list.add(valueBytes);

        return mergeByteArrayList(byteList);
    }

    public static byte[] serializeNew2(Class clazz,Object input){
        List<Field> fieldList = getSortedClassField(clazz);
        if (ObjectUtil.isEmpty(fieldList)){
            return new byte[]{};
        }
        List<byte[]> list=new ArrayList<>();
        for (Field field : fieldList) {

//            encodeField(field,input,list);
        }
        if (ObjectUtil.isEmpty(list)){
            return new byte[]{};
        }
        return mergeByteArrayList(list);
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


    private static void encodeField2(Object input,Field field,List<byte[]> list) {
        Object fieldValue = MethodUtil.getFieldValue(field, input);
        Class<?> fieldType = field.getType();
        if (fieldValue==null){
            list.add(TLVBufferTypeUtil.encodeVarInteger(0));
            list.add(new byte[]{0x00});
        }else{

            byte[] valueBytes ;
            if (List.class.isAssignableFrom(field.getType())) {
                Class listGenericClass = getListGenericClass(field);
                valueBytes = encodeListValue(listGenericClass,fieldValue);
            }else if (field.getType().isArray()){
                Class<?> componentType = fieldType.getComponentType();
                valueBytes = encodeArrayValue(componentType,fieldValue);
            }else{
                valueBytes = encodePrimitiveValue(fieldType,fieldValue);
            }

            byte tag = generateTag(fieldType, valueBytes);
            //第一个是 valueType + valueByteLengthLength
            list.add(new byte[]{tag});

            byte[] valueByteLength = TLVBufferTypeUtil.encodeVarInteger(valueBytes.length);
            //第二个是 valueByteLength
            list.add(valueByteLength);
            //第三个是value byte
            list.add(valueBytes);
        }
    }

    public static byte generateTag(Class classType,byte[] valueBytes){
        BinaryType binaryType = getBinaryType(classType);
        String valueByteLengthBinary = BinaryCodeLength.getBinaryCode(TLVBufferTypeUtil.encodeVarInteger(valueBytes.length).length);
        return ByteUtil.binaryStringToByte(binaryType.getBinaryCode() + valueByteLengthBinary);
    }

    public static byte[] serialize(Class clazz,Object inputValue){
        List<byte[]> list=new ArrayList<>();
        List<Field> fieldList = getSortedClassField(clazz);
        if (ObjectUtil.isNotEmpty(fieldList)){
            for (int i = 0; i < fieldList.size(); i++) {
                Field field = fieldList.get(i);
                Object fieldValue = MethodUtil.getFieldValue(field, inputValue);
                if (fieldValue!=null){
                    byte[] valueBytes = encodePrimitiveToByteArray(fieldValue);

                    BinaryType binaryType = getBinaryType(field.getType());

                    String valueByteLengthBinary = BinaryCodeLength.getBinaryCode(TLVBufferTypeUtil.encodeVarInteger(valueBytes.length).length);

                    byte b = ByteUtil.binaryStringToByte(binaryType.getBinaryCode() + valueByteLengthBinary);
                    //第一个是 valueType + valueByteLengthLength
                    list.add(new byte[]{b});

                    byte[] valueByteLength = TLVBufferTypeUtil.encodeVarInteger(valueBytes.length);
                    //第二个是 valueByteLength
                    list.add(valueByteLength);
                    //第三个是value byte
                    list.add(valueBytes);
                }else{
                    list.add(TLVBufferTypeUtil.encodeVarInteger(0));
                    list.add(new byte[]{0x00});
                }
            }
        }
        if (ObjectUtil.isEmpty(list)){
            return null;
        }
        return mergeByteArrayList(list);
    }

    public static byte[] serialize2(Class clazz, Object inputValue) {
        List<byte[]> byteArrayList = new ArrayList<>();
        List<Field> fieldList = getSortedClassField(clazz);
        if (ObjectUtil.isNotEmpty(fieldList)) {
            for (Field field : fieldList) {
                Object fieldValue = MethodUtil.getFieldValue(field, inputValue);
                byte[] valueBytes = encodeFieldValue(fieldValue);
                byteArrayList.add(encodeFieldLength(valueBytes.length));
                byteArrayList.add(valueBytes);
            }
        }
        return mergeByteArrayList(byteArrayList);
    }

    private static byte[] encodeFieldValue(Object value) {
        if (value != null) {
            return encodePrimitiveToByteArray(value);
        } else {
            return new byte[]{0x00}; // Representing null value with zero length
        }
    }

    private static byte[] encodeFieldLength(int length) {
        return TLVBufferTypeUtil.encodeVarInteger(length);
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

    public static byte[] encodeListValue(Class genericClass,Object value){
        //List [00000=type 000=[valueByteLength]Length] [valueByteLength] [value]
        List<Object> objectList = (List<Object>)value ;
        if (objectList!=null && objectList.size()==0){
            return new byte[1];
        }

//        if(JavaUtil.isPrimitive(genericClass) || JavaUtil.isObject(genericClass)){
//
//        }else{
//            if (objectList==null){
//                return null;
//            }
//            if (ObjectUtil.isNotEmpty(objectList)){
//                List<byte[]> listByteList=new ArrayList<>();
//                for (Object obj : objectList) {
//                    listByteList.add(serializeNew(genericClass, obj));
//                }
//                return mergeByteArrayList(listByteList);
//            }else{
//                return new byte[1];
//            }
//        }
            if (objectList==null){
                return null;
            }
            if (ObjectUtil.isNotEmpty(objectList)){
                List<byte[]> listByteList=new ArrayList<>();
                for (Object obj : objectList) {
                    listByteList.add(serializeNew( obj));
                }
                return mergeByteArrayList(listByteList);
            }else{
                return new byte[1];
            }
    }

//    public static void test(){
//        Class<?> inputClass = input.getClass();
//        byte[] valueBytes = encodePrimitiveValue(inputClass, input);
//        byte tag = generateTag(inputClass, valueBytes);
//        byteList.add(new byte[]{tag});
//        byteList.add(TLVBufferTypeUtil.encodeVarInteger(valueBytes.length));
//        byteList.add(valueBytes);
//    }


    public static byte[] encodeListValue2(Object input,Field field,Object fieldValue){
        //List [00000=type 000=[valueByteLength]Length] [valueByteLength] [value]
        Type[] actualTypeArguments = TypeUtil.getActualTypeArguments(field);
        if (ObjectUtil.isNotEmpty(actualTypeArguments)){
            Class<?> genericClass = TypeUtil.convertTypeToClass(actualTypeArguments[0]);
            List<Object> objectList = (List<Object>) MethodUtil.getFieldValue(field, input);
            if (objectList!=null && objectList.size()==0){
                return new byte[1];
            }
            if(JavaUtil.isPrimitive(genericClass) || JavaUtil.isObject(genericClass)){
                List<byte[]> byteList=new ArrayList<>();
                for (Object obj : objectList) {
                    Class<?> objClass = obj.getClass();
                    byte[] valueBytes = encodePrimitiveValue(objClass, obj);
                    byte tag = generateTag(objClass, valueBytes);
                    byteList.add(new byte[]{tag});
                    byteList.add(TLVBufferTypeUtil.encodeVarInteger(valueBytes.length));
                    byteList.add(valueBytes);
                }
                return mergeByteArrayList(byteList);
            }else{
                if (objectList==null){
                    return null;
                }
                if (ObjectUtil.isNotEmpty(objectList)){
                    List<byte[]> listByteList=new ArrayList<>();
                    for (Object obj : objectList) {
                        listByteList.add(serializeNew( obj));
                    }
                    return mergeByteArrayList(listByteList);
                }else{
                    return new byte[1];
                }
            }
        }
        return new byte[1];
    }

    public static byte[] encodeArrayValue(Class type ,Object fieldValue){
        if (JavaUtil.isPrimitive(type)){

        }else{
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
        }
        return null;
    }

    public static byte[] encodeArrayValue2(Object input,Field field,Object fieldValue){
        Class<?> componentType = field.getType().getComponentType();
        if (JavaUtil.isPrimitive(componentType)){

        }else{
            Object[] arrayValue = (Object[]) MethodUtil.getFieldValue(field, input);
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
        }
        return null;
    }

    public static byte[] encodePrimitiveValue(Class<?> type,Object value){
        if (type== byte.class || type == Byte.class){
            return new byte[]{(byte) value};
        }else if (type == boolean.class || type==Boolean.class){
            return new byte[]{(byte) (ObjectUtil.isBoolean((Boolean) value)?1:0)};
        }else if (type == char.class || type== Character.class){
            return TLVBufferTypeUtil.encodeVarChar((Character) value);
        }else if (type== short.class || type == Short.class){
            return TLVBufferTypeUtil.encodeVarShort((Short)value);
        }else if (type == int.class || type == Integer.class) {
            return TLVBufferTypeUtil.encodeVarInteger((Integer) value);
        }else if (type == float.class || type == Float.class){
            return TLVBufferTypeUtil.encodeVarFloat((Float)value);
        }else if (type == long.class || type == Long.class){
            return TLVBufferTypeUtil.encodeVarLong((Long)value);
        }else if (type == double.class || type == Double.class){
            return TLVBufferTypeUtil.encodeVarDouble((Double)value);
        }else if (type == String.class || type == CharSequence.class) {
            if (((String)value).length()==0){
                return new byte[1];
            }
            return ((String) value).getBytes(StandardCharsets.UTF_8);
        }else if ( type == BigDecimal.class){
            return TLVBufferTypeUtil.encodeVarBigDecimal((BigDecimal) value);
        }else if (type == Date.class){
            return TLVBufferTypeUtil.encodeVarDate((Date) value);
        }else if (type == LocalTime.class){
            return TLVBufferTypeUtil.encodeVarLocalTime((LocalTime) value);
        }else if (type == LocalDate.class){
            return TLVBufferTypeUtil.encodeVarLocalDate((LocalDate) value);
        }else if (type == LocalDateTime.class) {
            return TLVBufferTypeUtil.encodeVarLocalDateTime((LocalDateTime) value);
        }else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    public static byte[] encodePrimitiveToByteArray(Object obj) {
        if (obj instanceof Byte) {
            return new byte[]{(byte) obj};
        } else if (obj instanceof Short) {
            return TLVBufferTypeUtil.encodeVarShort((Short)obj);
        } else if (obj instanceof Integer) {
            return TLVBufferTypeUtil.encodeVarInteger((Integer) obj);
        } else if (obj instanceof Long) {
            return TLVBufferTypeUtil.encodeVarLong((Long)obj);
        } else if (obj instanceof Float) {
            return TLVBufferTypeUtil.encodeVarFloat((Float)obj);
        } else if (obj instanceof Double) {
            return TLVBufferTypeUtil.encodeVarDouble((Double)obj);
        } else if (obj instanceof Character) {
            return new byte[]{(byte) ((char) obj)};
        } else if (obj instanceof String ) {
            return ((String) obj).getBytes(StandardCharsets.UTF_8);
        } else if (obj instanceof Date) {
            return TLVBufferTypeUtil.encodeVarDate((Date) obj);
        } else if (obj instanceof LocalTime) {
            return TLVBufferTypeUtil.encodeVarLocalTime((LocalTime) obj);
        }else if (obj instanceof LocalDate) {
            return TLVBufferTypeUtil.encodeVarLocalDate((LocalDate) obj);
        }else if (obj instanceof LocalDateTime) {
            return TLVBufferTypeUtil.encodeVarLocalDateTime((LocalDateTime) obj);
        }else if (obj instanceof BigDecimal){
            return TLVBufferTypeUtil.encodeVarBigDecimal((BigDecimal) obj);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getName());
        }
    }


    public static BinaryType getBinaryType(Class type) {
        if (type == char.class || type == Character.class){
            return BinaryType.CHAR;
        }else if (type == boolean.class || type==Boolean.class) {
            return BinaryType.BOOLEAN;
        }else if (type==Long.class || type==long.class){
            return BinaryType.LONG;
        }else if (String.class == type || type == CharSequence.class){
            return BinaryType.STRING;
        }else if (short.class == type || Short.class==type){
            return BinaryType.SHORT;
        }else if (int.class == type || Integer.class==type){
            return BinaryType.INTEGER;
        }else if (byte.class ==type || Byte.class==type){
            return BinaryType.BYTE;
        }else if (Float.class== type || float.class== type){
            return BinaryType.FLOAT;
        }else if (Double.class== type || double.class==type){
            return BinaryType.DOUBLE;
        }else if (BigDecimal.class ==type){
            return BinaryType.BIG_DECIMAL;
        }else if (Date.class == type){
            return BinaryType.DATE;
        }else if (LocalTime.class == type){
            return BinaryType.LOCAL_TIME;
        }else if (LocalDate.class == type){
            return BinaryType.LOCAL_DATE;
        }else if (LocalDateTime.class == type) {
            return BinaryType.LOCAL_DATE_TIME;
        }else  if (List.class.isAssignableFrom(type)) {
            return BinaryType.LIST;
        }else if (type.isArray()){
            return BinaryType.ARRAY;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    public static Object decodeListValue(Class<?> genericClass ,byte[] bytes) throws IOException {
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

    public static Object decodeArrayValue(Class genericClass,byte[] bytes) throws IOException {
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

    public static Object decodePrimitiveValue(Class<?> type,byte[] bytes) throws IOException {

        if (ObjectUtil.isEmpty(bytes)) {
            return getDefaultForType(type);
        }

        if (type == char.class || type == Character.class){
            return TLVBufferTypeUtil.decodeVarChar(bytes);
        }else if (type == boolean.class || type == Boolean.class){
            if (ObjectUtil.isEmpty(bytes)){
                return null;
            }
            return bytes[0]==1?true:false;
        }else if (type==Long.class || type==long.class){
            return TLVBufferTypeUtil.decodeVarLong(bytes);
        }else if (String.class == type || type ==CharSequence.class){
            if (bytes.length==1 && bytes[0] == 0x00){
                return "";
            }
            return new String(bytes,StandardCharsets.UTF_8);
        }else if (short.class == type || Short.class==type){
            return TLVBufferTypeUtil.decodeVarShort(bytes);
        }else if (int.class == type || Integer.class==type){
            return TLVBufferTypeUtil.decodeVarInteger(bytes);
        }else if (byte.class ==type || Byte.class==type){
            if (bytes.length>0) {
                return bytes[0];
            }
            return null;
        }else if (Float.class== type || float.class== type){
            return TLVBufferTypeUtil.decodeVarFloat(bytes);
        }else if (Double.class== type || double.class==type){
            return TLVBufferTypeUtil.decodeVarDouble(bytes);
        }else if (BigDecimal.class ==type){
            return TLVBufferTypeUtil.decodeVarBigDecimal(bytes);
        }else if (Date.class == type){
            return TLVBufferTypeUtil.decodeVarDate(bytes);
        }else if (LocalTime.class == type){
            return TLVBufferTypeUtil.decodeVarLocalTime(bytes);
        }else if (LocalDate.class == type){
            return TLVBufferTypeUtil.decodeVarLocalDate(bytes);
        }else if (LocalDateTime.class == type) {
            return TLVBufferTypeUtil.decodeVarLocalDateTime(bytes);
        }else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    private static Object getDefaultForType(Class<?> type) {
        if (type.isPrimitive()) {
            if (type == boolean.class) return false;
            if (type == char.class) return '\0';
            if (type == byte.class) return (byte) 0;
            if (type == short.class) return (short) 0;
            if (type == int.class) return 0;
            if (type == long.class) return 0L;
            if (type == float.class) return 0f;
            if (type == double.class) return 0d;
        }
        return null;
    }

    @AllArgsConstructor
    @Getter
    public  enum BinaryType{

        /**
         * 00000 (0)
         * 00001 (1)
         * 00010 (2)
         * 00011 (3)
         * 00100 (4)
         * 00101 (5)
         * 00110 (6)
         * 00111 (7)
         * 01000 (8)
         * 01001 (9)
         * 01010 (10)
         * 01011 (11)
         * 01100 (12)
         * 01101 (13)
         * 01110 (14)
         * 01111 (15)
         *
         * 10000 (16)
         * 10001 (17)
         * 10010 (18)
         * 10011 (19)
         * 10100 (20)
         * 10101 (21)
         * 10110 (22)
         * 10111 (23)
         * 11000 (24)
         * 11001 (25)
         * 11010 (26)
         * 11011 (27)
         * 11100 (28)
         * 11101 (29)
         * 11110 (30)
         * 11111 (31)
         */
        BOOLEAN("00000",boolean.class),
        BYTE("00001",byte.class),
        CHAR("00010",char.class),
        SHORT("00011",short.class),
        INTEGER("00100",int.class),
        FLOAT("00101",float.class),
        LONG("00110",long.class),
        DOUBLE("00111",double.class),
        STRING("01000",String.class),
        DATE("01001",Date.class),
        LOCAL_TIME("01010",LocalTime.class),
        LOCAL_DATE("01011",LocalDate.class),
        LOCAL_DATE_TIME("01100",LocalDateTime.class),
        BIG_DECIMAL("01101",BigDecimal.class),
        ARRAY("01110",Array.class),
        LIST("01111",List.class),
            ;

        private final String binaryCode;
        private final Class<?> type;

        private static final Map<String, BinaryType> BINARY_CODE_MAP = new HashMap<>();

        static {
            for (BinaryType type : values()) {
                BINARY_CODE_MAP.put(type.binaryCode, type);
            }
        }

        public static BinaryType fromBinaryCode(String binaryCode) {
            BinaryType result = BINARY_CODE_MAP.get(binaryCode);
            if (result == null) {
                throw new IllegalArgumentException("No enum constant found for binary code: " + binaryCode);
            }
            return result;
        }

    }


    @Getter
    @AllArgsConstructor
    public enum BinaryCodeLength {
        B1("000", 1),
        B2("001", 2),
        B3("010", 3),
        B4("011", 4),
        B5("100", 5),
        B6("101", 6),
        B7("110", 7),
        B8("111", 8);

        private final String binaryCode;
        private final int length;

        public static int getLength(String binaryCode) {
            for (BinaryCodeLength code : BinaryCodeLength.values()) {
                if (code.getBinaryCode().equals(binaryCode)) {
                    return code.getLength();
                }
            }
            throw new IllegalArgumentException("Invalid binary code: " + binaryCode);
        }

        public static String getBinaryCode(int length){
            for (BinaryCodeLength code : BinaryCodeLength.values()) {
                if (code.getLength()==length) {
                    return code.getBinaryCode();
                }
            }
            throw new IllegalArgumentException("Invalid length: " + length);
        }
    }


    public static class ByteArrayReader {
        private final byte[] data;
        private int lastReadIndex; // 最后读取的下标
        private BinaryType lastBinaryType;

        public ByteArrayReader(byte[] data) {
            this.data = data;
            this.lastReadIndex = 0; // 初始化为0
        }

        public byte readFrom(int end){
            if ( end > data.length) {
                throw new IndexOutOfBoundsException("Start or end index out of bounds");
            }
            lastReadIndex = end;
            return data[end];
        }

        /**
         * 从给定的起始下标开始读取到结束下标（不包含结束下标位置的元素），并返回子数组。
         *
         * @param start 开始下标（包含）
         * @param end 结束下标（不包含）
         * @return 子数组
         * @throws IndexOutOfBoundsException 如果起始下标或结束下标超出数组范围
         */
        public byte[] readFromTo(int start, int end) {
            if (start < 0 || start > end || end > data.length) {
                throw new IndexOutOfBoundsException("Start or end index out of bounds");
            }
            lastReadIndex =  end;
            return Arrays.copyOfRange(data, start, end);
        }

        public int getLastReadIndex() {
            return lastReadIndex;
        }

        public BinaryType getLastBinaryType() {
            return lastBinaryType;
        }

        public void setLastBinaryType(BinaryType lastBinaryType) {
            this.lastBinaryType = lastBinaryType;
        }

        /**
         * 检查是否已读取完所有数据
         *
         * @return 如果已读取完所有数据则返回true，否则返回false
         */
        public boolean isReadComplete() {
            return lastReadIndex == data.length;
        }
    }


}
