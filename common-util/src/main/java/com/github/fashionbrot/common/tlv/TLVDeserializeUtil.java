package com.github.fashionbrot.common.tlv;

import com.github.fashionbrot.common.tlv.annotation.TLVField;
import com.github.fashionbrot.common.util.ByteUtil;
import com.github.fashionbrot.common.util.JavaUtil;
import com.github.fashionbrot.common.util.MethodUtil;
import com.github.fashionbrot.common.util.ObjectUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fashionbrot
 */
public class TLVDeserializeUtil {

    public static <T> T deserialize(Class<T> deserializeClass,byte[] data)  {
        return deserialize(deserializeClass,new ByteArrayReader(data));
    }

    public static <T> T deserialize(Class<T> deserializeClass,ByteArrayReader reader)  {
        if (TypeHandleFactory.isPrimitive(deserializeClass)){
            byte[] nextBytes = getNextBytes(reader);
            return (T) TypeHandleFactory.toJava(deserializeClass, nextBytes);
        }
        if (reader.isReadComplete()){
            return null;
        }
        T instance = (T) deserializeEntity(deserializeClass, reader);
        return instance;
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
            Class<?> fieldType = field.getType();
            Object fieldValue = deserialize(fieldType, reader);
            MethodUtil.setFieldValue(field,instance,fieldValue);
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
            return new byte[]{};
        }
        return reader.readFromTo(readIndex +1+ valueByteLengthLength, readIndex +1 + valueByteLengthLength+ valueByteLength);
    }




}
