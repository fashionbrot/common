package com.github.fashionbrot.common.util;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class LLvBufferUtil {


    public static <T> T deserialize2(Class<T> clazz, byte[] data) throws IOException {
        List<Field> fieldList = getSortedNonStaticNonFinalFields(clazz);
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
                Object decodedValue = decodeByteToPrimitive(fieldData, field.getType());
                MethodUtil.setFieldValue(field, instance, decodedValue);
            }
            return instance;

        }
        return null;
    }

    public static <T> T deserialize3(Class<T> clazz, byte[] data) throws IOException {
        List<Field> fieldList = getSortedNonStaticNonFinalFields(clazz);
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
                    decodedValue = decodeByteToPrimitive(fieldData, field.getType());
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
                list.add(decodeByteToPrimitive(elementData, elementType));
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
                java.lang.reflect.Array.set(array, i, decodeByteToPrimitive(elementData, elementType));
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

    public static <T> T deserialize(Class<T> clazz,byte[] data) throws IOException {
        List<Field> fieldList = getSortedNonStaticNonFinalFields(clazz);
        if (ObjectUtil.isNotEmpty(fieldList)) {
            T t = MethodUtil.newInstance(clazz);

            int index=0;
            int readIndex=0;
            for (;;){
                if (index>=fieldList.size()){
                    break;
                }
                byte firstByte = data[readIndex];

                String binaryString = ByteUtil.byteToBinaryString(firstByte);
                BinaryType valueType = BinaryType.fromBinaryCode(binaryString.substring(0, 4));
                int valueByteLengthLength = ByteUtil.binaryStringToDecimal(binaryString.substring(4, 8));

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

                Object value = decodeByteToPrimitive(bytes, type);

                MethodUtil.setFieldValue(field,t,value);

                index++;
            }
            return t;
        }
        return null;
    }


    public static byte[] serialize(Class clazz,Object inputValue){
        List<byte[]> list=new ArrayList<>();
        List<Field> fieldList = getSortedNonStaticNonFinalFields(clazz);
        if (ObjectUtil.isNotEmpty(fieldList)){
            for (int i = 0; i < fieldList.size(); i++) {
                Field field = fieldList.get(i);
                Object fieldValue = MethodUtil.getFieldValue(field, inputValue);
                if (fieldValue!=null){
                    byte[] valueBytes = encodePrimitiveToByteArray(fieldValue);

                    BinaryType binaryType = fieldTypeToBinaryType(field.getType());
                    String valueByteLengthBinary = ByteUtil.toBinaryString(LvBufferTypeUtil.encodeVarInteger(valueBytes.length).length, 4);

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
        List<Field> fieldList = getSortedNonStaticNonFinalFields(clazz);
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


    public static <T> List<Field> getSortedNonStaticNonFinalFields(Class<T> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        return Arrays.stream(declaredFields)
                .filter(m -> !isStaticOrFinal(m))
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }

    private static boolean isStaticOrFinal(Field field) {
        int modifiers = field.getModifiers();
        return java.lang.reflect.Modifier.isStatic(modifiers) || java.lang.reflect.Modifier.isFinal(modifiers);
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


    public static BinaryType fieldTypeToBinaryType(Class type) {
        if (type==Long.class || type==long.class){
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
        }else if (LocalDateTime.class == type){
            return BinaryType.LOCAL_DATE_TIME;
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    public static Object decodeByteToPrimitive(byte[] bytes,Class<?> type) throws IOException {
        if (type==Long.class || type==long.class){
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
        }else if (LocalDateTime.class == type){
            return LvBufferTypeUtil.decodeVarLocalDateTime(bytes);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }


    @AllArgsConstructor
    @Getter
    public  enum BinaryType{

        /**
         * 0000 = 0
         * 0001 = 1
         * 0010 = 2
         * 0011 = 3
         * 0100 = 4
         * 0101 = 5
         * 0110 = 6
         * 0111 = 7
         * 1000 = 8
         * 1001 = 9
         * 1010 = 10
         * 1011 = 11
         * 1100 = 12
         * 1101 = 13
         * 1110 = 14
         * 1111 = 15
         */
        BOOLEAN("0000"),
        BYTE("0001"),
        CHAR("0010"),
        SHORT("0011"),
        INTEGER("0100"),
        FLOAT("0101"),
        LONG("0110"),
        DOUBLE("0111"),
        STRING("1000"),
        DATE("1001"),
        LOCAL_TIME("1010"),
        LOCAL_DATE("1011"),
        LOCAL_DATE_TIME("1100"),
        BIG_DECIMAL("1101"),
        ARRAY("1110"),
        LIST("1111"),
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

}
