package com.github.fashionbrot.common.util;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.File;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LLvBufferUtil {


    public static <T> T deserialize2(Class<T> clazz, byte[] data) throws IOException {
        List<Field> fieldList = getSortedClassField(clazz);
        if (ObjectUtil.isNotEmpty(fieldList)) {
            T instance = MethodUtil.newInstance(clazz);

            int dataIndex = 0;
            for (Field field : fieldList) {
                int fieldLength = data[dataIndex];
                dataIndex++;
                if (fieldLength == 0) {
                    continue; // Field has zero length, skip it
                }
                byte[] fieldData = Arrays.copyOfRange(data, dataIndex, dataIndex + fieldLength);
                dataIndex += fieldLength;
                Object decodedValue = decodeValue(field,fieldData);
                MethodUtil.setFieldValue(field, instance, decodedValue);
            }
            return instance;

        }
        return null;
    }

    public static <T> T deserialize3(Class<T> clazz, byte[] data) throws IOException {
        List<Field> fieldList = getSortedClassField(clazz);
        if (fieldList != null && !fieldList.isEmpty()) {
            T instance = MethodUtil.newInstance(clazz);
            int dataIndex = 0;
            for (Field field : fieldList) {
                int fieldLength = data[dataIndex];
                dataIndex++;
                if (fieldLength == 0) {
                    continue; // Field has zero length, skip it
                }
                byte[] fieldData = Arrays.copyOfRange(data, dataIndex, dataIndex + fieldLength);
                dataIndex += fieldLength;
                Object decodedValue;
                if (List.class.isAssignableFrom(field.getType())) {
                    decodedValue = decodeList(fieldData, field);
                } else if (field.getType().isArray()) {
                    decodedValue = decodeArray(fieldData, field);
                } else {
                    decodedValue = decodeValue(field,fieldData);
                }
                MethodUtil.setFieldValue(field, instance, decodedValue);
            }
            return instance;
        }
        return null;
    }


    private static Object decodeList(byte[] data, Field field) throws IOException {
        Class<?> elementType = getGenericType(field);
        List<Object> list = new ArrayList<>();
        int index = 0;
        while (index < data.length) {
            int elementLength = data[index];
            index++;
            if (elementLength == 0) {
                list.add(null); // Add null element to the list
            } else {
                byte[] elementData = Arrays.copyOfRange(data, index, index + elementLength);
                index += elementLength;
                list.add(decodeValue(field, elementData));
            }
        }
        return list;
    }

    private static Object decodeArray(byte[] data, Field field) throws IOException {
        Class<?> elementType = field.getType().getComponentType();
        int arrayLength = data.length;
        Object array = java.lang.reflect.Array.newInstance(elementType, arrayLength);
        int index = 0;
        for (int i = 0; i < arrayLength; i++) {
            int elementLength = data[index];
            index++;
            if (elementLength == 0) {
                java.lang.reflect.Array.set(array, i, null); // Set null element in the array
            } else {
                byte[] elementData = Arrays.copyOfRange(data, index, index + elementLength);
                index += elementLength;
                java.lang.reflect.Array.set(array, i, decodeValue(field, elementData));
            }
        }
        return array;
    }

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
        return Object.class; // Default to Object type if unable to determine generic type
    }

    public static <T> T deserializeNew(Class<T> clazz,byte[] data) throws IOException {
        return deserializeNew(clazz,new ByteArrayReader(data));
    }

    public static <T> T deserializeNew(Class<T> clazz,ByteArrayReader reader)throws IOException{
        T t = MethodUtil.newInstance(clazz);
        List<Field> fieldList = getSortedClassField(clazz);
        if (ObjectUtil.isEmpty(fieldList)){
            return t;
        }

        int readIndex=reader.getLastReadIndex();
        for (Field field : fieldList) {

            byte firstByte = reader.readFrom(readIndex);
            //第一位byte(前5个bit 是value数量类型 后3个bit 是valueByte.length 经过 varInt 压缩后的长度)
            String binaryString = ByteUtil.byteToBinaryString(firstByte);
            BinaryType valueType = BinaryType.fromBinaryCode(binaryString.substring(0, 5));
            int valueByteLengthLength = BinaryCodeLength.getLength(binaryString.substring(5, 8));//LvBufferTypeUtil.encodeVarInteger().length;

            int valueByteLength = LvBufferTypeUtil.decodeVarInteger(reader.readFromTo(readIndex + 1, readIndex + 1 + valueByteLengthLength));
            if (valueByteLength==0){
                readIndex+=2;
                continue;
            }

            byte[] bytes = reader.readFromTo(readIndex +1+ valueByteLengthLength, readIndex +1 + valueByteLengthLength+ valueByteLength);

            readIndex = readIndex +1 + valueByteLengthLength+ valueByteLength;

            Object value = decodeValue(field,bytes);

            MethodUtil.setFieldValue(field,t,value);
        }
        return t;
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

            int valueByteLength = LvBufferTypeUtil.decodeVarInteger(Arrays.copyOfRange(data, readIndex + 1, readIndex + 1 + valueByteLengthLength));
            if (valueByteLength==0){
                readIndex+=2;
                continue;
            }

            byte[] bytes = Arrays.copyOfRange(data, readIndex +1+ valueByteLengthLength, readIndex +1 + valueByteLengthLength+ valueByteLength);

            readIndex = readIndex +1 + valueByteLengthLength+ valueByteLength;

            Class<?> type = field.getType();

            Object value = decodeValue(field,bytes);

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

                short valueByteLength = LvBufferTypeUtil.decodeVarShort(Arrays.copyOfRange(data, readIndex + 1, readIndex + 1 + valueByteLengthLength));

                if (valueByteLength==0){
                    readIndex+=1;
                    index++;
                    continue;
                }

                byte[] bytes = Arrays.copyOfRange(data, readIndex +1+ valueByteLengthLength, readIndex +1 + valueByteLengthLength+ valueByteLength);

                readIndex = readIndex +1 + valueByteLengthLength+ valueByteLength;

                Field field = fieldList.get(index);
                Class<?> type = field.getType();

                Object value = decodeValue(field,bytes);

                MethodUtil.setFieldValue(field,t,value);

                index++;
            }
            return t;
        }
        return null;
    }

    public static byte[] serializeNew(Class clazz,Object input){
        List<Field> fieldList = getSortedClassField(clazz);
        if (ObjectUtil.isEmpty(fieldList)){
            return new byte[]{};
        }
        List<byte[]> list=new ArrayList<>();
        for (Field field : fieldList) {
            Object fieldValue = MethodUtil.getFieldValue(field, input);
            encodeField(input,field,fieldValue,list);
        }
        if (ObjectUtil.isEmpty(list)){
            return new byte[]{};
        }
        return mergeByteArrayList(list);
    }


    private static void encodeField(Object input,Field field, Object fieldValue,List<byte[]> list) {

        if (fieldValue==null){
            list.add(LvBufferTypeUtil.encodeVarInteger(0));
            list.add(new byte[]{0x00});
        }else{
            byte[] valueBytes = encodeValue(input,field,fieldValue);

            BinaryType binaryType = getFieldBinaryType(field);

            String valueByteLengthBinary = BinaryCodeLength.getBinaryCode(LvBufferTypeUtil.encodeVarInteger(valueBytes.length).length);

            byte b = ByteUtil.binaryStringToByte(binaryType.getBinaryCode() + valueByteLengthBinary);
            //第一个是 valueType + valueByteLengthLength
            list.add(new byte[]{b});

            byte[] valueByteLength = LvBufferTypeUtil.encodeVarInteger(valueBytes.length);
            //第二个是 valueByteLength
            list.add(valueByteLength);
            //第三个是value byte
            list.add(valueBytes);
        }
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

                    BinaryType binaryType = getFieldBinaryType(field);

                    String valueByteLengthBinary = BinaryCodeLength.getBinaryCode(LvBufferTypeUtil.encodeVarInteger(valueBytes.length).length);

                    byte b = ByteUtil.binaryStringToByte(binaryType.getBinaryCode() + valueByteLengthBinary);
                    //第一个是 valueType + valueByteLengthLength
                    list.add(new byte[]{b});

                    byte[] valueByteLength = LvBufferTypeUtil.encodeVarInteger(valueBytes.length);
                    //第二个是 valueByteLength
                    list.add(valueByteLength);
                    //第三个是value byte
                    list.add(valueBytes);
                }else{
                    list.add(LvBufferTypeUtil.encodeVarInteger(0));
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
        return LvBufferTypeUtil.encodeVarInteger(length);
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



    public static byte[] encodeValue(Object input,Field field,Object value){
        Class<?> type = field.getType();
        if (type== byte.class || type == Byte.class){
            return new byte[]{(byte) value};
        }else if (type == boolean.class || type==Boolean.class){
            return new byte[]{(byte) (ObjectUtil.isBoolean((Boolean) value)?1:0)};
        }else if (type == char.class || type== Character.class){
            return new byte[]{(byte) ((char) value)};
        }else if (type== short.class || type == Short.class){
            return LvBufferTypeUtil.encodeVarShort((Short)value);
        }else if (type == int.class || type == Integer.class) {
            return LvBufferTypeUtil.encodeVarInteger((Integer) value);
        }else if (type == float.class || type == Float.class){
            return LvBufferTypeUtil.encodeVarFloat((Float)value);
        }else if (type == long.class || type == Long.class){
            return LvBufferTypeUtil.encodeVarLong((Long)value);
        }else if (type == double.class || type == Double.class){
            return LvBufferTypeUtil.encodeVarDouble((Double)value);
        }else if (type == String.class) {
            return ((String) value).getBytes(StandardCharsets.UTF_8);
        }else if ( type == BigDecimal.class){
            return LvBufferTypeUtil.encodeVarBigDecimal((BigDecimal) value);
        }else if (type == Date.class){
            return LvBufferTypeUtil.encodeVarDate((Date) value);
        }else if (type == LocalTime.class){
            return LvBufferTypeUtil.encodeVarLocalTime((LocalTime) value);
        }else if (type == LocalDate.class){
            return LvBufferTypeUtil.encodeVarLocalDate((LocalDate) value);
        }else if (type == LocalDateTime.class) {
            return LvBufferTypeUtil.encodeVarLocalDateTime((LocalDateTime) value);
        }else if (List.class.isAssignableFrom(field.getType())) {
            //List [00000=type 000=[valueByteLength]Length] [valueByteLength] [value]
            Type[] actualTypeArguments = TypeUtil.getActualTypeArguments(field);
            if (ObjectUtil.isNotEmpty(actualTypeArguments)){
                Class<?> listGenericClass = TypeUtil.convertTypeToClass(actualTypeArguments[0]);
                List<Object> listValue = (List<Object>) MethodUtil.getFieldValue(field, input);
                if (ObjectUtil.isNotEmpty(listValue)){
                    List<byte[]> listByteList=new ArrayList<>();
                    for (Object list : listValue) {
                        byte[] bytes = serializeNew(listGenericClass, list);
                        listByteList.add(bytes);
                    }
                    return mergeByteArrayList(listByteList);
                }else{
                    return new byte[0x00];
                }
            }
            return new byte[0x00];
        }else if (field.getType().isArray()) {
            Class<?> componentType = field.getType().getComponentType();
            if (JavaUtil.isPrimitive(componentType)){

            }else{
                Object[] fieldValue = (Object[]) MethodUtil.getFieldValue(field, input);
                if (ObjectUtil.isNotEmpty(fieldValue)){
                    List<byte[]> arrayList=new ArrayList<>();
                    for (Object o : fieldValue) {
                        arrayList.add(serializeNew(componentType,o));
                    }
                    return mergeByteArrayList(arrayList);
                }
            }
            return null;
        }else {
            throw new IllegalArgumentException("Unsupported type: " + field.getType());
        }
    }

    public static byte[] encodePrimitiveToByteArray(Object obj) {
        if (obj instanceof Byte) {
            return new byte[]{(byte) obj};
        } else if (obj instanceof Short) {
            return LvBufferTypeUtil.encodeVarShort((Short)obj);
        } else if (obj instanceof Integer) {
            return LvBufferTypeUtil.encodeVarInteger((Integer) obj);
        } else if (obj instanceof Long) {
            return LvBufferTypeUtil.encodeVarLong((Long)obj);
        } else if (obj instanceof Float) {
            return LvBufferTypeUtil.encodeVarFloat((Float)obj);
        } else if (obj instanceof Double) {
            return LvBufferTypeUtil.encodeVarDouble((Double)obj);
        } else if (obj instanceof Character) {
            return new byte[]{(byte) ((char) obj)};
        } else if (obj instanceof String ) {
            return ((String) obj).getBytes(StandardCharsets.UTF_8);
        } else if (obj instanceof Date) {
            return LvBufferTypeUtil.encodeVarDate((Date) obj);
        } else if (obj instanceof LocalTime) {
            return LvBufferTypeUtil.encodeVarLocalTime((LocalTime) obj);
        }else if (obj instanceof LocalDate) {
            return LvBufferTypeUtil.encodeVarLocalDate((LocalDate) obj);
        }else if (obj instanceof LocalDateTime) {
            return LvBufferTypeUtil.encodeVarLocalDateTime((LocalDateTime) obj);
        }else if (obj instanceof BigDecimal){
            return LvBufferTypeUtil.encodeVarBigDecimal((BigDecimal) obj);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getName());
        }
    }


    public static BinaryType getFieldBinaryType(Field field) {
        Class type = field.getType();
        if (type == boolean.class || type==Boolean.class) {
            return BinaryType.BOOLEAN;
        }else if (type==Long.class || type==long.class){
            return BinaryType.LONG;
        }else if (String.class == type){
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
        }else  if (List.class.isAssignableFrom(field.getType())) {
            return BinaryType.LIST;
        }else if (field.getType().isArray()){
            return BinaryType.ARRAY;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    public static Object decodeValue(Field field,byte[] bytes) throws IOException {
        Class<?> type = field.getType();
        if (type == boolean.class || type == Boolean.class){
            return bytes[0]==1?true:false;
        }else if (type==Long.class || type==long.class){
            return LvBufferTypeUtil.decodeVarLong(bytes);
        }else if (String.class == type){
            return new String(bytes);
        }else if (short.class == type || Short.class==type){
            return LvBufferTypeUtil.decodeVarShort(bytes);
        }else if (int.class == type || Integer.class==type){
            return LvBufferTypeUtil.decodeVarInteger(bytes);
        }else if (byte.class ==type || Byte.class==type){
            if (bytes.length>0) {
                return bytes[0];
            }
            return null;
        }else if (Float.class== type || float.class== type){
            return LvBufferTypeUtil.decodeVarFloat(bytes);
        }else if (Double.class== type || double.class==type){
            return LvBufferTypeUtil.decodeVarDouble(bytes);
        }else if (BigDecimal.class ==type){
            return LvBufferTypeUtil.decodeVarBigDecimal(bytes);
        }else if (Date.class == type){
            return LvBufferTypeUtil.decodeVarDate(bytes);
        }else if (LocalTime.class == type){
            return LvBufferTypeUtil.decodeVarLocalTime(bytes);
        }else if (LocalDate.class == type){
            return LvBufferTypeUtil.decodeVarLocalDate(bytes);
        }else if (LocalDateTime.class == type) {
            return LvBufferTypeUtil.decodeVarLocalDateTime(bytes);
        }else if (List.class.isAssignableFrom(field.getType())) {
            Type[] actualTypeArguments = TypeUtil.getActualTypeArguments(field);
            if (ObjectUtil.isNotEmpty(actualTypeArguments)) {
                Class<?> genericClass = TypeUtil.convertTypeToClass(actualTypeArguments[0]);
                List list = new ArrayList();
                if (ObjectUtil.isNotEmpty(bytes)) {
                    ByteArrayReader reader = new ByteArrayReader(bytes);
                    while (!reader.isReadComplete()){
                        list.add(deserializeNew(genericClass, reader));
                    }
                }
                return list;
            }
            return null;
        }else if (field.getType().isArray()){
            Class genericClass = field.getType().getComponentType();
            List<Object> list=new ArrayList<>();
            if (ObjectUtil.isNotEmpty(bytes)){
                ByteArrayReader reader = new ByteArrayReader(bytes);
                while (!reader.isReadComplete()){
                    list.add(deserializeNew(genericClass, reader));
                }
            }
            return list.toArray((Object[]) Array.newInstance(genericClass, list.size()));
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
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
        BOOLEAN("00000"),
        BYTE("00001"),
        CHAR("00010"),
        SHORT("00011"),
        INTEGER("00100"),
        FLOAT("00101"),
        LONG("00110"),
        DOUBLE("00111"),
        STRING("01000"),
        DATE("01001"),
        LOCAL_TIME("01010"),
        LOCAL_DATE("01011"),
        LOCAL_DATE_TIME("01100"),
        BIG_DECIMAL("01101"),
        ARRAY("01110"),
        LIST("01111"),
            ;

        private String binaryCode;

        public static BinaryType fromBinaryCode(String binaryCode) {
            for (BinaryType type : values()) {
                if (type.getBinaryCode().equals(binaryCode)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("No enum constant found for binary code: " + binaryCode);
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
