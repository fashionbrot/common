package com.github.fashionbrot.common;

import com.alibaba.fastjson2.JSON;
import com.github.fashionbrot.common.entity.TestEntity;
import com.github.fashionbrot.common.entity.TestEntityProto;
import com.github.fashionbrot.common.util.LLvBufferUtil;
import com.github.fashionbrot.common.util.MethodUtil;
import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.common.util.LvBufferTypeUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ByteUtilTest {

    /**
     * const byteArray = new Uint8Array([8, 123, 18, 5, 65, 108, 105, 99, 101]);
     *
     * // 解析 Tag
     * function parseTag(byteArray, offset) {
     *   const tag = byteArray[offset];
     *   const fieldNumber = tag >> 3; // 右移 3 位得到字段编号
     *   const wireType = tag & 0x07; // 与运算获取低 3 位得到 Wire Type
     *   return { fieldNumber, wireType };
     * }
     *
     * // 解析 Varint 编码的整数
     * function parseVarint(byteArray, offset) {
     *   let result = 0;
     *   let shift = 0;
     *   let byte;
     *   do {
     *     byte = byteArray[offset++];
     *     result |= (byte & 0x7F) << shift;
     *     shift += 7;
     *   } while (byte & 0x80);
     *   return { value: result, offset };
     * }
     *
     * // 解析 Length-delimited 编码的字符串
     * function parseString(byteArray, offset, length) {
     *   const strBytes = byteArray.subarray(offset, offset + length);
     *   const decoder = new TextDecoder("utf-8");
     *   const value = decoder.decode(strBytes);
     *   return { value, offset: offset + length };
     * }
     *
     * // 解析 byte 数组
     * function parseByteArray(byteArray) {
     *   let offset = 0;
     *   let id, name;
     *
     *   while (offset < byteArray.length) {
     *     const { fieldNumber, wireType } = parseTag(byteArray, offset);
     *     if (fieldNumber === 1 && wireType === 0) {
     *       // id 字段
     *       const { value, offset: newOffset } = parseVarint(byteArray, offset + 1);
     *       id = value;
     *       offset = newOffset;
     *     } else if (fieldNumber === 2 && wireType === 2) {
     *       // name 字段
     *       const { value, offset: newOffset } = parseVarint(byteArray, offset + 1);
     *       const { value: strValue, offset: stringOffset } = parseString(byteArray, newOffset, value);
     *       name = strValue;
     *       offset = stringOffset;
     *     } else {
     *       // 未知字段类型
     *       console.error("Unknown field type or number");
     *       break;
     *     }
     *   }
     *
     *   return { id, name };
     * }
     *
     * // 解析 byte 数组
     * const parsedData = parseByteArray(byteArray);
     * console.log(parsedData);
     * @param value
     * @return
     */

    public static byte[] intToBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value >> 24);
        bytes[1] = (byte) (value >> 16);
        bytes[2] = (byte) (value >> 8);
        bytes[3] = (byte) value;
        return bytes;
    }

    public static int bytesToInt(byte[] bytes) {
        if (bytes == null || bytes.length != 4) {
            throw new IllegalArgumentException("The byte array must be non-null and have exactly 4 elements.");
        }

        return ((bytes[0] & 0xFF) << 24) |
                ((bytes[1] & 0xFF) << 16) |
                ((bytes[2] & 0xFF) << 8) |
                (bytes[3] & 0xFF);
    }


    public static void main(String[] args) throws IOException {
        TestEntity build = TestEntity.builder()
                .id(22L)
                .name(LvBufferTypeUtil.maxString())
                .parentId(33L)
                .parentName(LvBufferTypeUtil.maxString())
                .test5(55L)
                .test6(66L)
                .test7(LvBufferTypeUtil.maxString())
                .test8(88L)
                .test9(99L)
                .test10("aa")
                .build();
        byte[] jsonBytes = JSON.toJSONBytes(build);
        System.out.println("转成json："+jsonBytes.length);


        long l2 = System.currentTimeMillis();
        System.out.println("----------------------自己实现的压缩---------------------start");
        TestEntity build2 = TestEntity.builder()
                .id(22L)
                .name(LvBufferTypeUtil.maxString())
                .parentId(33L)
                .parentName(LvBufferTypeUtil.maxString())
                .test5(55L)
                .test6(66L)
                .test7(LvBufferTypeUtil.maxString())
                .test8(88L)
                .test9(99L)
                .test10("aa")
                .build();
//        byte[] bytes2 = compressObjectToByte(build2,TestEntity.class);
        byte[] bytes2 = LLvBufferUtil.serialize(TestEntity.class, build2);
        System.out.println("Varint压缩后 value本身占用："+bytes2.length+"byte");
        System.out.println(Arrays.toString(bytes2));


//        TestEntity testEntity = deCompressObject(bytes2, TestEntity.class);
        TestEntity testEntity = LLvBufferUtil.deserialize(TestEntity.class, bytes2);
        System.out.println(testEntity.toString());

        System.out.println(System.currentTimeMillis()-l2+"毫秒");
        System.out.println("----------------------自己实现的压缩---------------------end");



        long l1 = System.currentTimeMillis();
        System.out.println("----------------------protobuf---------------------start");
        TestEntityProto.TestEntity test= TestEntityProto.TestEntity.newBuilder()
                .setId(22L)
                .setParentId(33L)
                .setName(LvBufferTypeUtil.maxString())
                .setParentName(LvBufferTypeUtil.maxString())
                .setTest5(55L)
                .setTest6(66L)
                .setTest7(LvBufferTypeUtil.maxString())
                .setTest8(88L)
                .setTest9(99L)
                .setTest10("aa")
                .build();

        byte[] byteArray = test.toByteArray();
        System.out.println("protobuf："+byteArray.length+"byte");
        System.out.println(Arrays.toString(byteArray));

        TestEntityProto.TestEntity test1 = TestEntityProto.TestEntity.parseFrom(byteArray);
        TestEntity build1 = TestEntity.builder()
                .id(test1.getId())
                .name(test1.getName())
                .parentId(test1.getParentId())
                .parentName(test1.getParentName())
                .test5(test1.getTest5())
                .test6(test1.getTest6())
                .test7(test1.getTest7())
                .test8(test1.getTest8())
                .test9(test1.getTest9())
                .test10(test1.getTest10())
                .build();
        System.out.println(build1.toString());
        System.out.println(System.currentTimeMillis()-l1+"毫秒");
        System.out.println("----------------------protobuf---------------------end");
        System.out.println();


        byte[] bytes = objectToByte(build);
        System.out.println("value本身占用："+bytes.length);



    }

    public static  <T> T newInstance(Class<T> resultClass){
        try {
            return resultClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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


    public static <T> T deCompressObject(byte[] data,Class<T> clazz) throws IOException {
        Field[] declaredFields = clazz.getDeclaredFields();
        T t = newInstance(clazz);
        if (ObjectUtil.isNotEmpty(declaredFields)) {



            List<Field> fieldList = getSortedNonStaticNonFinalFields(clazz);

            int index=0;
            int readIndex=0;
            for (;;){
                if (index>=fieldList.size()){
                    break;
                }
                byte byteLength = data[readIndex];
                int length = LvBufferTypeUtil.decodeVarInteger(new byte[]{byteLength});
                if (length==0){
                    readIndex+=1;
                    index++;
                    continue;
                }

                byte[] bytes = Arrays.copyOfRange(data, readIndex + 1, readIndex + 1+ length);

                readIndex = readIndex + 1+ length;

                Field field = fieldList.get(index);
                Class<?> type = field.getType();

                Object value = decode(bytes, type);

                field.setAccessible(true);
                try {
                    field.set(t,value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                index++;
            }

        }
        return t;
    }



    public static byte[] compressObjectToByte(Object object,Class clazz){
        List<byte[]> list=new ArrayList<>();
        Field[] declaredFields = object.getClass().getDeclaredFields();
        if (ObjectUtil.isNotEmpty(declaredFields)){

            List<Field> fieldList = getSortedNonStaticNonFinalFields(clazz);

            for (int i = 0; i < fieldList.size(); i++) {
                Field declaredField = fieldList.get(i);

                String fieldName = declaredField.getName();
                byte[] fieldNameByte = stringToByte(fieldName);
//                list.add(new byte[]{(byte) fieldNameByte.length});
//                list.add(fieldNameByte);
                declaredField.setAccessible(true);
                try {
                    Object value = declaredField.get(object);
                    if (value!=null){
                        byte[] valueBytes = compressPrimitiveToByteArray(value);
                        list.add(LvBufferTypeUtil.encodeVarInteger(valueBytes.length));
                        list.add(valueBytes);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return mergeByteArrayList(list);
    }

    public static byte[] objectToByte(Object object){
        List<byte[]> list=new ArrayList<>();
        Field[] declaredFields = object.getClass().getDeclaredFields();
        if (ObjectUtil.isNotEmpty(declaredFields)){
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                if (MethodUtil.isStaticOrFinal(declaredField)){
                    continue;
                }
                String fieldName = declaredField.getName();
                byte[] fieldNameByte = stringToByte(fieldName);
//                list.add(new byte[]{(byte) fieldNameByte.length});
//                list.add(fieldNameByte);
                declaredField.setAccessible(true);
                try {
                    Object value = declaredField.get(object);
                    if (value!=null){
                        byte[] valueBytes = primitiveToByteArray(value);
//                        try {
//                            list.add(VarintUtil.encodeVarLong(valueBytes.length));
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
                        list.add(valueBytes);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return mergeByteArrayList(list);
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

    public static Object decode(byte[] bytes,Class<?> type){
        if (type==Long.class || type==long.class){
            try {
                return LvBufferTypeUtil.decodeVarLong(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else if (String.class == type){
            return new String(bytes);
        }
        return null;
    }

    public static byte[] primitiveToByteArray(Object obj) {
        if (obj instanceof Byte) {
            return new byte[]{(byte) obj};
        } else if (obj instanceof Short) {
            return ByteBuffer.allocate(2).putShort((short) obj).array();
        } else if (obj instanceof Integer) {
            return ByteBuffer.allocate(4).putInt((int) obj).array();
        } else if (obj instanceof Long) {
            return ByteBuffer.allocate(Long.BYTES).putLong((Long)obj).array();
        } else if (obj instanceof Float) {
            return ByteBuffer.allocate(4).putFloat((float) obj).array();
        } else if (obj instanceof Double) {
            return ByteBuffer.allocate(8).putDouble((double) obj).array();
        } else if (obj instanceof Character) {
            return new byte[]{(byte) ((char) obj)};
        } else if (obj instanceof String ){
            return ((String)obj).getBytes(StandardCharsets.UTF_8);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getName());
        }
    }


    public static byte[] compressPrimitiveToByteArray(Object obj) {
        if (obj instanceof Byte) {
            return new byte[]{(byte) obj};
        } else if (obj instanceof Short) {
            return ByteBuffer.allocate(2).putShort((short) obj).array();
        } else if (obj instanceof Integer) {
            return LvBufferTypeUtil.encodeVarInteger((Integer) obj);
        } else if (obj instanceof Long) {
            return LvBufferTypeUtil.encodeVarLong((Long)obj);
        } else if (obj instanceof Float) {
            return ByteBuffer.allocate(4).putFloat((float) obj).array();
        } else if (obj instanceof Double) {
            return ByteBuffer.allocate(8).putDouble((double) obj).array();
        } else if (obj instanceof Character) {
            return new byte[]{(byte) ((char) obj)};
        } else if (obj instanceof String ){
            return ((String)obj).getBytes(StandardCharsets.UTF_8);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getName());
        }
    }

    // 压缩字符串
    public static byte[] compressString(String str) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
            gzipOutputStream.write(str.getBytes());
        }
        return outputStream.toByteArray();
    }

    // 解压缩字符串
    public static String decompressString(byte[] data) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = gzipInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        }
        return outputStream.toString();
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

    public static byte[] stringToByte(String str){
        if (ObjectUtil.isEmpty(str)){
            return null;
        }
        return str.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] stringToCharToByte(String str){
        if (ObjectUtil.isEmpty(str)){
            return null;
        }
        char[] charArray = str.toCharArray();
        byte[] bytes=new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            bytes[i]= (byte) charArray[i];
        }
        return bytes;
    }

}
