//package com.github.fashionbrot.common.tlv;
//
//import com.github.fashionbrot.common.tlv.annotation.TLVField;
//import com.github.fashionbrot.common.util.*;
//import java.lang.reflect.Array;
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.lang.reflect.Type;
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class TLVUtil {
//
//
//
//    public static byte[] serialize(Object input){
//        if (input==null){
//            return null;
//        }
//        List<byte[]> byteList=new ArrayList<>();
//        Class<?> clazz = input.getClass();
//        if (JavaUtil.isPrimitive(clazz) || JavaUtil.isObject(clazz)){
//            byteList.add(addPrimitiveOrObject(clazz,input));
//        }else if (List.class.isAssignableFrom(clazz)){
//            byteList.add(encodeListValue(clazz, input));
//        }else if (clazz.isArray()){
//            byteList.add(encodeArrayValue(clazz,input));
//        }else{
//            addFields(byteList,clazz,input);
//        }
//        return mergeByteArrayList(byteList);
//    }
//
//    public static <T> T deserialize(Class<T> clazz,byte[] data)  {
//        return deserialize(clazz,new ByteArrayReader(data));
//    }
//
//    public static <T> T deserialize(Class<T> clazz,ByteArrayReader reader){
//        if (JavaUtil.isObject(clazz) || JavaUtil.isPrimitive(clazz)) {
//            if (reader.isReadComplete()){
//                return null;
//            }
//            byte[] valueBytes = getNextBytes(reader);
//            BinaryType lastBinaryType = reader.getLastBinaryType();
//
//            if (lastBinaryType == BinaryType.LIST) {
//                return (T) decodeListValue(clazz, valueBytes);
//            } else if (lastBinaryType == BinaryType.ARRAY) {
//                return (T) decodeArrayValue(clazz, valueBytes);
//            } else {
//                return (T) TypeHandleFactory.toJava(reader.getLastBinaryType().getType()[0], valueBytes);
//            }
//        }else if (List.class.isAssignableFrom(clazz)) {
//            return (T) deserializeList(clazz, reader);
//        }else if (clazz.isArray()){
//            return (T) deserializeArray(clazz,reader);
//        }else {
//            return deserializeEntity(clazz,reader);
//        }
//    }
//
//    public static <T> T[] deserializeArray(Class<T> clazz,byte[] bytes) {
//        return deserializeArray(clazz,new ByteArrayReader(bytes));
//    }
//
//    public static <T> T[] deserializeArray(Class<T> clazz,ByteArrayReader reader) {
//        List<Object> list=new ArrayList<>();
//        while (!reader.isReadComplete()){
//            list.add(deserialize(clazz, reader));
//        }
//        return (T[]) list.toArray((Object[]) Array.newInstance(clazz, list.size()));
//    }
//
//    public static <T> List<T> deserializeList(Class<T> clazz,byte[] bytes)  {
//        return deserializeList(clazz,new ByteArrayReader(bytes));
//    }
//
//    public static <T> List<T> deserializeList(Class<T> clazz,ByteArrayReader reader)  {
//        List<T> list= new ArrayList<>();
//        while (!reader.isReadComplete()){
//            list.add(deserializeEntity(clazz, reader));
//        }
//        return list;
//    }
//
//    public static <T> T deserializeEntity(Class<T> clazz,byte[] bytes){
//        return deserializeEntity(clazz,new ByteArrayReader(bytes));
//    }
//
//    public static <T> T deserializeEntity(Class<T> clazz,ByteArrayReader reader){
//        T instance = MethodUtil.newInstance(clazz);
//        List<Field> fieldList = getSortedClassField(clazz);
//        if (ObjectUtil.isEmpty(fieldList)){
//            return instance;
//        }
//        for (Field field : fieldList) {
//            if (reader.isReadComplete()){
//                break;
//            }
//            TLVField annotation = field.getAnnotation(TLVField.class);
//            if (annotation!=null && !annotation.serialize()){
//                continue;
//            }
//
//            Object objectValue;
////            if (ObjectUtil.isNotEmpty(valueBytes)){
//                Class<?> type = field.getType();
//                if (List.class.isAssignableFrom(type)){
//                    byte[] valueBytes = getNextBytes(reader);
//                    Class listGenericClass = getListGenericClass(field);
//                    objectValue = decodeListValue(listGenericClass,valueBytes);
//                }else if (field.getType().isArray()){
//                    byte[] valueBytes = getNextBytes(reader);
//                    Class<?> componentType = type.getComponentType();
//                    objectValue = decodeArrayValue(componentType,valueBytes);
//                }else  if (JavaUtil.isPrimitive(type) || JavaUtil.isObject(type)) {
//                    byte[] valueBytes = getNextBytes(reader);
//                    objectValue = TypeHandleFactory.toJava(field.getType(), valueBytes);
//                }else{
//                    objectValue = deserializeEntity(type,reader);
//                }
////            }else{
////                objectValue = null;
////            }
//            MethodUtil.setFieldValue(field,instance,objectValue);
//        }
//        return instance;
//    }
//
//
//    public static byte[] getNextBytes(ByteArrayReader reader){
//        int readIndex = reader.getLastReadIndex();
//        byte firstByte = reader.readFrom(readIndex);
//        //第一位byte(前5个bit 是value数据类型 后3个bit 是valueByte.length 经过 varInt 压缩后的长度)
//        String binaryString = ByteUtil.byteToBinaryString(firstByte);
//        BinaryType valueType = BinaryType.fromBinaryCode(binaryString.substring(0, 5));
//        int valueByteLengthLength = BinaryCodeLength.getLength(binaryString.substring(5, 8));
//
//        reader.setLastBinaryType(valueType);
//
//        int valueByteLength = TLVTypeUtil.decodeVarInteger(reader.readFromTo(readIndex + 1, readIndex + 1 + valueByteLengthLength));
//        if (valueByteLength==0){
//            return new byte[]{};
//        }
//        return reader.readFromTo(readIndex +1+ valueByteLengthLength, readIndex +1 + valueByteLengthLength+ valueByteLength);
//    }
//
//
//
//
//
//    private static byte[] addPrimitiveOrObject(Class<?> clazz, Object input) {
//        List<byte[]> byteList =new ArrayList<>();
//        byte[] valueBytes = TypeHandleFactory.toByte(clazz, input);
//        byte tag = generateTag(clazz, valueBytes);
//        byteList.add(new byte[]{tag});
//        byteList.add(TLVTypeUtil.encodeVarInteger(valueBytes.length));
//        byteList.add(valueBytes);
//        return mergeByteArrayList(byteList);
//    }
//
////    private static void addFields(List<byte[]> byteList, Class<?> clazz, Object input) {
////        List<Field> fieldList = getSortedClassField(clazz);
////        for (Field field : fieldList) {
////            TLVField annotation = field.getAnnotation(TLVField.class);
////            if (annotation!=null && !annotation.serialize()){
////                continue;
////            }
////            Class<?> fieldType = field.getType();
////            Object fieldValue = MethodUtil.getFieldValue(field, input);
////
////            byte[] valueBytes = (fieldValue != null) ? encodeFieldValue(field, fieldValue) : new byte[]{};
////
////            byte tag = generateTag(fieldType, valueBytes);
////            byteList.add(new byte[]{tag});
////            byteList.add(TLVTypeUtil.encodeVarInteger(valueBytes.length));
////            byteList.add(valueBytes);
////        }
////    }
//
//    private static void addFields(List<byte[]> byteList, Class<?> clazz, Object input) {
//        List<Field> fieldList = getSortedClassField(clazz);
//        for (Field field : fieldList) {
//            TLVField annotation = field.getAnnotation(TLVField.class);
//            if (annotation!=null && !annotation.serialize()){
//                continue;
//            }
//            Class<?> fieldType = field.getType();
//            Object fieldValue = MethodUtil.getFieldValue(field, input);
//            if (fieldValue==null){
//
//                byte tag = generateTag(fieldType, null);
//                byteList.add(new byte[]{tag});
//                byteList.add(TLVTypeUtil.encodeVarInteger(0));
//                byteList.add(new byte[]{});
//
//            }else{
//
//                byte[] valueBytes;
//                if (List.class.isAssignableFrom(fieldType)) {
//                    Class listGenericClass = getListGenericClass(field);
//                    valueBytes = encodeListValue(listGenericClass,fieldValue);
//                }else if (fieldType.isArray()){
//                    Class<?> componentType = field.getType().getComponentType();
//                    valueBytes = encodeArrayValue(componentType,fieldValue);
//                }else if (JavaUtil.isPrimitive(fieldType) || JavaUtil.isObject(fieldType)) {
//                    valueBytes = TypeHandleFactory.toByte(fieldType,fieldValue);
//                }else{
//                    valueBytes = serialize(fieldValue);
//                    byteList.add(valueBytes);
//                    continue;
//                }
//
//                byte tag = generateTag(fieldType, valueBytes);
//                byteList.add(new byte[]{tag});
//                byteList.add(TLVTypeUtil.encodeVarInteger(valueBytes.length));
//                byteList.add(valueBytes);
//            }
//        }
//    }
//
//
//    private static byte[] encodeFieldValue(Field field,Object value) {
//        Class<?> fieldType = field.getType();
//        byte[] valueBytes ;
//        if (List.class.isAssignableFrom(fieldType)) {
//            Class listGenericClass = getListGenericClass(field);
//            valueBytes = encodeListValue(listGenericClass,value);
//        }else if (fieldType.isArray()){
//            Class<?> componentType = field.getType().getComponentType();
//            valueBytes = encodeArrayValue(componentType,value);
//        }else if (JavaUtil.isPrimitive(fieldType) || JavaUtil.isObject(fieldType)) {
//
//            List<byte[]> byteList = new ArrayList<>();
//            valueBytes = TypeHandleFactory.toByte(fieldType,value);
//            byte tag = generateTag(fieldType, valueBytes);
//            byteList.add(new byte[]{tag});
//            byteList.add(TLVTypeUtil.encodeVarInteger(valueBytes.length));
//            byteList.add(valueBytes);
//            return mergeByteArrayList(byteList);
//
//        }else{
//            valueBytes = serialize(value);
//        }
//
//        return valueBytes;
//    }
//
//    public static Class getListGenericClass(Field field){
//        Type[] actualTypeArguments = TypeUtil.getActualTypeArguments(field);
//        if (ObjectUtil.isNotEmpty(actualTypeArguments)) {
//            return TypeUtil.convertTypeToClass(actualTypeArguments[0]);
//        }
//        return Object.class;
//    }
//
//
//
//    public static byte generateTag(Class classType,byte[] valueBytes){
//        BinaryType binaryType = BinaryType.getBinaryType(classType);
//        String valueByteLengthBinary = BinaryCodeLength.getBinaryCode(TLVTypeUtil.encodeVarInteger(ObjectUtil.isNotEmpty(valueBytes)?valueBytes.length:0).length);
//        return ByteUtil.binaryStringToByte(binaryType.getBinaryCode() + valueByteLengthBinary);
//    }
//
//
//
//    // 将 List 中的 byte[] 数组合并为一个单独的字节数组
//    public static byte[] mergeByteArrayList(List<byte[]> list) {
//        // 计算总长度
//        int totalLength = 0;
//        for (byte[] array : list) {
//            if (array != null && array.length > 0) {
//                totalLength += array.length;
//            }
//        }
//        // 创建结果数组
//        byte[] result = new byte[totalLength];
//        // 合并字节数组
//        int currentIndex = 0;
//        for (byte[] array : list) {
//            if (array != null && array.length > 0) {
//                System.arraycopy(array, 0, result, currentIndex, array.length);
//                currentIndex += array.length;
//            }
//        }
//        return result;
//    }
//
//    public static List<Field> getSortedClassField(Class clazz){
//        List<FieldModel> classFieldList = getNonStaticNonFinalFieldModels(clazz);
//        List<FieldModel> superClassField = getSuperClassField(clazz);
//        if (ObjectUtil.isNotEmpty(superClassField)){
//            classFieldList.addAll(superClassField);
//        }
//        Collections.sort(classFieldList,Comparator.comparing(FieldModel::getIndex).thenComparing(f -> f.getField().getName()));
//        List<Field> fieldList = new ArrayList<>();
//        if (ObjectUtil.isNotEmpty(classFieldList)){
//            for (FieldModel fieldModel : classFieldList) {
//                fieldList.add(fieldModel.getField());
//            }
//        }
//        return fieldList;
//    }
//
//
//    public static List<FieldModel> getNonStaticNonFinalFieldModels(Class<?> clazz) {
//        List<FieldModel> fieldList = new ArrayList<>();
//        Field[] declaredFields = clazz.getDeclaredFields();
//        for (Field declaredField : declaredFields) {
//            if (!isStaticOrFinal(declaredField)){
//                TLVField annotation = declaredField.getAnnotation(TLVField.class);
//                if (annotation!=null && !annotation.serialize()){
//                    continue;
//                }
//                int index = annotation != null ? annotation.index() : Integer.MAX_VALUE;
//                fieldList.add(FieldModel.builder().index(index).field(declaredField).build());
//            }
//        }
//        return fieldList;
//    }
//
//    public static List<FieldModel> getSuperClassField(Class clazz){
//        Class superclass = clazz.getSuperclass();
//        if (superclass != null && JavaUtil.isNotObject(superclass)) {
//            List<FieldModel> classFieldList = getNonStaticNonFinalFieldModels(superclass);
//
//            List<FieldModel> superClassFieldList = getSuperClassField(superclass);
//            if (ObjectUtil.isNotEmpty(superClassFieldList)){
//                classFieldList.addAll(superClassFieldList);
//            }
//            return classFieldList;
//        }
//        return null;
//    }
//
//
//    private static boolean isStaticOrFinal(Field field) {
//        int modifiers = field.getModifiers();
//        return Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers);
//    }
//
//    public static byte[] encodeListValue(Class genericClass, Object value) {
//        if (value == null) {
//            return null;
//        }
//        List<Object> objectList = (List<Object>) value;
//        if (objectList != null && objectList.size() == 0) {
//            return new byte[1];
//        }
//        if (ObjectUtil.isNotEmpty(objectList)) {
//            List<byte[]> listByteList = new ArrayList<>();
//            for (Object obj : objectList) {
//                listByteList.add(serialize(obj));
//            }
//            return mergeByteArrayList(listByteList);
//        } else {
//            return new byte[1];
//        }
//    }
//
//
//    public static byte[] encodeArrayValue(Class type ,Object fieldValue){
//        Object[] arrayValue = (Object[])fieldValue;
//        if (arrayValue!=null && arrayValue.length==0){
//            return new byte[1];
//        }
//        if (ObjectUtil.isNotEmpty(arrayValue)){
//            List<byte[]> arrayList=new ArrayList<>();
//            for (Object o : arrayValue) {
//                arrayList.add(serialize(o));
//            }
//            return mergeByteArrayList(arrayList);
//        }
//        return null;
//    }
//
//
//
//
//    public static Object decodeListValue(Class<?> genericClass ,byte[] bytes) {
//        if (ObjectUtil.isEmpty(bytes)){
//            return null;
//        }
//        List list = new ArrayList();
//        if (bytes!=null && bytes.length==1 && bytes[0]==0x00){
//            return list;
//        }
//        if (ObjectUtil.isNotEmpty(bytes)) {
//            ByteArrayReader reader = new ByteArrayReader(bytes);
//            while (!reader.isReadComplete()){
//                list.add(deserialize(genericClass, reader));
//            }
//        }
//        return list;
//    }
//
//    public static Object decodeArrayValue(Class genericClass,byte[] bytes)  {
//        if (bytes!=null && bytes.length==1 && bytes[0]==0x00){
//            return Array.newInstance(genericClass, 0);
//        }
//        if (ObjectUtil.isNotEmpty(bytes)){
//            List<Object> list=new ArrayList<>();
//            ByteArrayReader reader = new ByteArrayReader(bytes);
//            while (!reader.isReadComplete()){
//                list.add(deserialize(genericClass, reader));
//            }
//            return list.toArray((Object[]) Array.newInstance(genericClass, list.size()));
//        }
//        return  null;
//    }
//
//
//}
