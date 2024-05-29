package com.github.fashionbrot.common.tlv.parser;


import com.github.fashionbrot.common.util.ObjectUtil;

/**
 * @author fashionbrot
 */
public class BooleanTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return new byte[]{(byte) (ObjectUtil.isBoolean((Boolean) value)?1:0)};
    }

    @Override
    public Object toJava(byte[] bytes) {
        if (ObjectUtil.isEmpty(bytes)){
            return null;
        }
        return bytes[0]==1?true:false;
    }

}
