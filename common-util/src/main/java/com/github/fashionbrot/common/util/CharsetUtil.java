package com.github.fashionbrot.common.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author fashionbrot
 */
@Slf4j
public class CharsetUtil {


    /**
     * 获取指定字符集名称对应的 Charset 对象。
     *
     * @param charsetName 字符集名称
     * @return 对应的 Charset 对象，如果字符集名称为空则返回 null；如果获取失败也返回 null
     */
    public static Charset getCharset(String charsetName){
        if (ObjectUtil.isNotEmpty(charsetName)){
            return null;
        }
        try {
            return Charset.forName(charsetName);
        }catch (Exception e){
            log.error("getCharset error",e);
        }
        return null;
    }


}
