package com.github.fashionbrot.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {

    /**
     * file 转  Properties
     * @param file File
     * @return Properties
     */
    public static Properties toProperties(File file) {
        if (file==null){
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            return toProperties(inputStream);
        } catch (Exception e) {
            log.error("toProperties error ", e);
        }
        return null;
    }

    /**
     * InputStream 转 properties
     * @param inputStream InputStream
     * @return Properties
     */
    public static Properties toProperties(InputStream inputStream) {
        Properties properties = new Properties();
        if (inputStream==null){
            return null;
        }
        try {
            properties.load(inputStream);
        } catch (Exception e) {
            log.error("putProperties error ", e);
        } finally {
            IoUtil.close(inputStream);
        }
        return properties;
    }

    /**
     * Reader 转 Properties
     * @param reader Reader
     * @return Properties
     */
    public static Properties toProperties(Reader reader) {
        Properties properties = new Properties();
        if (reader==null){
            return null;
        }
        try {
            properties.load(reader);
        } catch (Exception e) {
            log.error("toProperties error ", e);
        } finally {
            IoUtil.close(reader);
        }
        return properties;
    }


    /**
     * 字符串 转 Properties
     * @param content 文本内容
     * @return Properties
     */
    public static Properties toProperties(String content){
        return toProperties(new StringReader(content));
    }




}
