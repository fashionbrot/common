package com.github.fashionbrot.common.util;

import com.github.fashionbrot.common.consts.CharsetConst;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author fashionbrot
 */
public class Base64Util {


    /**
     * 使用 Base64 编码对二进制数据进行编码。
     *
     * @param binaryData 要编码的二进制数据。
     * @return 编码后的二进制数据，以字节数组形式返回。
     */
    protected static byte[] encode(final byte[] binaryData) {
        if (ObjectUtil.isNotEmpty(binaryData)) {
            return Base64.getEncoder().encode(binaryData);
        }
        return new byte[0];
    }




    /**
     * 将二进制数据进行MIME编码。
     *
     * @param binaryData 要编码的二进制数据
     * @return MIME编码后的二进制数据，如果输入数据为空，则返回空数组
     */
    public static byte[] mimeEncode(final byte[] binaryData) {
        if (ObjectUtil.isNotEmpty(binaryData)) {
            return Base64.getMimeEncoder().encode(binaryData);
        }
        return new byte[0];
    }

    /**
     * 将字符串编码为字节数组。
     *
     * @param str 要编码的字符串
     * @return 编码后的字节数组，如果输入字符串为空，则返回空数组
     */
    public static String encodeString(final String str) {
        return IoUtil.toString(encode(str, CharsetConst.UTF8_CHARSET));
    }

    /**
     * 将字符串编码为字节数组。
     *
     * @param str 要编码的字符串
     * @return 编码后的字节数组，如果输入字符串为空，则返回空数组
     */
    public static byte[] encode(final String str) {
        return encode(str, CharsetConst.UTF8_CHARSET);
    }

    /**
     * 将字符串编码为字节数组。
     *
     * @param str 要编码的字符串
     * @return 编码后的字节数组，如果输入字符串为空，则返回空数组
     */
    public static byte[] encode(final String str, Charset charset) {
        if (ObjectUtil.isNotEmpty(str)) {
            return Base64.getDecoder().decode(str.getBytes(charset));
        }
        return new byte[0];
    }



    /**
     * 对 MIME 编码的字节数组进行解码。
     *
     * @param bytes 要解码的 MIME 编码字节数组
     * @return 解码后的字节数组，如果输入字节数组为空，则返回空数组
     */
    public static byte[] mimeDecode(final byte[] bytes) {
        if (ObjectUtil.isNotEmpty(bytes)) {
            return Base64.getMimeDecoder().decode(bytes);
        }
        return new byte[0];
    }


    /**
     * 对经过 Base64 编码的字符串进行解码。
     *
     * @param str 要解码的经过 Base64 编码的字符串
     * @return 解码后的字节数组，如果输入字符串为空，则返回空数组
     */
    public static byte[] decode(final String str) {
        if (ObjectUtil.isNotEmpty(str)) {
            return Base64.getDecoder().decode(str);
        }
        return new byte[0];
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
