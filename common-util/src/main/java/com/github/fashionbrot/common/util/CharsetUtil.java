package com.github.fashionbrot.common.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author fashionbrot
 */
@Slf4j
public class CharsetUtil {


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
