package com.github.fashionbrot.common.tlv.parser;


/**
 * @author fashionbrot
 */
public interface TypeHandle {

    byte[] toByte(Object value);

    Object toJava(byte[] bytes);

}
