package com.github.fashionbrot.common.tlv.parser;

import com.github.fashionbrot.common.tlv.TLVTypeUtil;

/**
 * @author fashionbrot
 */
public class IntegerTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return TLVTypeUtil.encodeVarInteger((Integer) value);
    }

    @Override
    public Object toJava(byte[] bytes) {
        return TLVTypeUtil.decodeVarInteger(bytes);
    }

}
