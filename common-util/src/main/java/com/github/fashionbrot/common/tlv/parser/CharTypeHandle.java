package com.github.fashionbrot.common.tlv.parser;


import com.github.fashionbrot.common.tlv.TLVTypeUtil;

/**
 * @author fashionbrot
 */
public class CharTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return TLVTypeUtil.encodeVarChar((Character) value);
    }

    @Override
    public Object toJava(byte[] bytes) {
        return TLVTypeUtil.decodeVarChar(bytes);
    }

}
