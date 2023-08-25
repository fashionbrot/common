package com.github.fashionbrot.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author fashi
 */
public class Base64Util {

    public final static byte[] byte_empty = new byte[]{};


    /**
     * 加密
     * @param binaryData binaryData
     * @return byte[]
     */
    protected static byte[] encode(byte[] binaryData) {
        byte[] result = byte_empty;
        if (ObjectUtil.isNotEmpty(binaryData)) {
            result = Base64.getEncoder().encode(binaryData);
        }
        return result;
    }

    /**
     * 加密
     * @param binaryData binaryData
     * @return byte[]
     */
    public static byte[] mimeEncode(final byte[] binaryData) {
        if (ObjectUtil.isNotEmpty(binaryData)) {
            return Base64.getMimeEncoder().encode(binaryData);
        }
        return byte_empty;
    }

    /**
     * 加密
     * @param str str
     * @return byte[]
     */
    public static byte[] encode(final String str) {
        if (ObjectUtil.isNotEmpty(str)) {
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            return encode(bytes);
        }
        return byte_empty;
    }


    /**
     * 解密
     * @param bytes bytes
     * @return byte[]
     */
    public static byte[] decode(final byte[] bytes) {
        if (ObjectUtil.isNotEmpty(bytes)) {
            return Base64.getDecoder().decode(bytes);
        }
        return byte_empty;
    }

    /**
     * 解密
     * @param bytes bytes
     * @return byte[]
     */
    public static byte[] mimeDecode(byte[] bytes) {
        if (ObjectUtil.isNotEmpty(bytes)) {
            return Base64.getMimeDecoder().decode(bytes);
        }
        return byte_empty;
    }


    /**
     * 解密
     * @param str str
     * @return byte[]
     */
    public static byte[] decode(final String str) {
        if (ObjectUtil.isNotEmpty(str)) {
            return Base64.getDecoder().decode(str);
        }
        return byte_empty;
    }


    /**
     * byte[]  转 String
     * @param binaryData binaryData
     * @return String
     */
    public static String encodeBase64String(final byte[] binaryData) {
        return ObjectUtil.byteToString(encode(binaryData));
    }



}
