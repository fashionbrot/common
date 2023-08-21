package com.github.fashionbrot.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {

    private static final String PLACEHOLDER_PREFIX = "${";

    private static final String PLACEHOLDER_SUFFIX = "}";

    private static final String VALUE_SEPARATOR = ":";


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




    /**
     * ${abc} 返回 abc
     * @param placeholder 要解析的内容
     * @return String
     */
    public static String resolvePlaceholder(String placeholder) {
        if (!placeholder.startsWith(PLACEHOLDER_PREFIX)) {
            return null;
        }

        if (!placeholder.endsWith(PLACEHOLDER_SUFFIX)) {
            return null;
        }

        if (placeholder.length() <= PLACEHOLDER_PREFIX.length() + PLACEHOLDER_SUFFIX.length()) {
            return null;
        }

        int beginIndex = PLACEHOLDER_PREFIX.length();
        int endIndex = placeholder.length() - PLACEHOLDER_PREFIX.length() + 1;
        placeholder = placeholder.substring(beginIndex, endIndex);
        int separatorIndex = placeholder.indexOf(VALUE_SEPARATOR);
        if (separatorIndex != -1) {
            return placeholder.substring(0, separatorIndex);
        }
        return ObjectUtil.trim(placeholder);
    }



    public static Properties resolve(Map<?, ?> properties, Properties source) {
        Properties resolvedProperties = new Properties();
        for (Map.Entry<?, ?> entry : properties.entrySet()) {
            if (entry.getValue() instanceof CharSequence) {
                String key = String.valueOf(entry.getKey());
                String value = String.valueOf(entry.getValue());

                String resolvedKey = resolvePlaceholder(value);
                if (ObjectUtil.isEmpty(resolvedKey)){
                    resolvedProperties.setProperty(key, value);
                }else{
                    if (source!=null && source.containsKey(resolvedKey)){
                        String resolvedValue = source.getProperty( resolvedKey) ;
                        resolvedProperties.setProperty(key, resolvedValue);
                    }else{
                        resolvedProperties.setProperty(key, "");
                    }
                }
            }
        }
        return resolvedProperties;
    }



}
