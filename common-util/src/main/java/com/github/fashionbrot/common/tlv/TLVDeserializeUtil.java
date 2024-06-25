package com.github.fashionbrot.common.tlv;

import com.github.fashionbrot.common.tlv.annotation.TLVField;
import com.github.fashionbrot.common.util.ByteUtil;
import com.github.fashionbrot.common.util.MethodUtil;
import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.common.util.TypeUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fashionbrot
 */
public class TLVDeserializeUtil {

    public static <T> T deserialize(Class<T> deserializeClass,byte[] data)  {
        return deserialize(deserializeClass,deserializeClass,new ByteArrayReader(data));
    }

    public static <T> T deserialize(Class type , Class<T> deserializeClass,ByteArrayReader reader)  {
        if (reader.isReadComplete()){
            return null;
        }
        if (TypeHandleFactory.isPrimitive(type)){
            byte[] nextBytes = getNextBytes(reader);
            return (T) TypeHandleFactory.toJava(deserializeClass, nextBytes);
        }else if (List.class.isAssignableFrom(type)){
            return (T) deserializeList(deserializeClass,reader);
        }else if (type.isArray()){
            return (T) deserializeArray(deserializeClass,reader);
        }else{
            return (T) deserializeEntity(deserializeClass, reader);
        }
    }

    public static <T> List<T> deserializeList(Class<T> clazz,byte[] bytes)  {
        return deserializeList(clazz,new ByteArrayReader(bytes));
    }

    public static <T> List<T> deserializeList(Class<T> clazz,ByteArrayReader reader)  {
        List<T> list= new ArrayList<>();
        if (reader.isCollectionEmpty()){
            return list;
        }
        while (!reader.isReadComplete()){
            list.add(deserializeEntity(clazz, reader));
        }
        return list;
    }

    public static <T> T[] deserializeArray(Class<T> clazz,byte[] bytes) {
        return deserializeArray(clazz,new ByteArrayReader(bytes));
    }

    public static <T> T[] deserializeArray(Class<T> clazz,ByteArrayReader reader) {
        if (reader.isCollectionEmpty()){
            return MethodUtil.newArrayInstance(clazz,0);
        }
        List<Object> list=new ArrayList<>();
        while (!reader.isReadComplete()){
            list.add(deserialize(clazz,clazz, reader));
        }
        return (T[]) list.toArray((Object[]) Array.newInstance(clazz, list.size()));
    }


    public static <T> T deserializeEntity(Class<T> clazz,ByteArrayReader reader){
        T instance = MethodUtil.newInstance(clazz);
        List<Field> fieldList = TLVSerializeUtil.getSortedClassField(clazz);
        if (ObjectUtil.isEmpty(fieldList)){
            return instance;
        }
        for (Field field : fieldList) {
            if (reader.isReadComplete()){
                break;
            }

            TLVField annotation = field.getAnnotation(TLVField.class);
            if (annotation!=null && !annotation.serialize()){
                continue;
            }

            Class type = field.getType();
            Class deserializeClass =  field.getType();
            if (List.class.isAssignableFrom(type)){
                deserializeClass = getListGenericClass(field);
                byte[] nextBytes = getNextBytes(reader);
                Object fieldValue = deserialize(type,deserializeClass, new ByteArrayReader(nextBytes));
                MethodUtil.setFieldValue(field,instance,fieldValue);
            }else if (type.isArray()){
                deserializeClass = type.getComponentType();
                byte[] nextBytes = getNextBytes(reader);
                Object fieldValue = deserialize(type,deserializeClass, new ByteArrayReader(nextBytes));
                MethodUtil.setFieldValue(field,instance,fieldValue);
            }else{
                Object fieldValue = deserialize(type,deserializeClass, reader);
                MethodUtil.setFieldValue(field,instance,fieldValue);
            }

        }
        return instance;
    }

    public static byte[] getNextBytes(ByteArrayReader reader){
        int readIndex = reader.getLastReadIndex();
        byte firstByte = reader.readFrom(readIndex);
        //第一位byte(前5个bit 是value数据类型 后3个bit 是valueByte.length 经过 varInt 压缩后的长度)
        String binaryString = ByteUtil.byteToBinaryString(firstByte);
        BinaryType valueType = BinaryType.fromBinaryCode(binaryString.substring(0, 5));
        int valueByteLengthLength = BinaryCodeLength.getLength(binaryString.substring(5, 8));

        reader.setLastBinaryType(valueType);

        int valueByteLength = TLVTypeUtil.decodeVarInteger(reader.readFromTo(readIndex + 1, readIndex + 1 + valueByteLengthLength));
        if (valueByteLength==0){
            return ByteUtil.BYTE_ARRAY_EMPTY;
        }
        return reader.readFromTo(readIndex +1+ valueByteLengthLength, readIndex +1 + valueByteLengthLength+ valueByteLength);
    }


    public static Class getListGenericClass(Field field){
        Type[] actualTypeArguments = TypeUtil.getActualTypeArguments(field);
        if (ObjectUtil.isNotEmpty(actualTypeArguments)) {
            return TypeUtil.convertTypeToClass(actualTypeArguments[0]);
        }
        return Object.class;
    }



}
