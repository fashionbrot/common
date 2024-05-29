package com.github.fashionbrot.common.tlv.parser;


import com.github.fashionbrot.common.tlv.TLVTypeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author fashionbrot
 */
public class LocalDateTimeTypeHandle implements TypeHandle {


    @Override
    public byte[] toByte(Object value) {
        return TLVTypeUtil.encodeVarLocalDateTime((LocalDateTime) value);
    }

    @Override
    public Object toJava(byte[] bytes) {
        return TLVTypeUtil.decodeVarLocalDateTime(bytes);
    }

}
