package com.github.fashionbrot.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PropertiesUtil {

    public static final String PLACEHOLDER_PREFIX = "${";

    public static final String PLACEHOLDER_SUFFIX = "}";

    public static final String VALUE_SEPARATOR = ":";

    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

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
     * 解析占位符，从占位符字符串中提取内容。
     *
     * @param placeholder 要解析的占位符字符串。
     * @return 提取的内容，如果占位符格式不正确则返回 null。
     */
    public static String resolvePlaceholder(String placeholder) {
        // 检查占位符是否以预定的前缀开始，如果不是则返回 null
        if (!placeholder.startsWith(PLACEHOLDER_PREFIX)) {
            return "";
        }

        // 检查占位符是否以预定的后缀结尾，如果不是则返回 null
        if (!placeholder.endsWith(PLACEHOLDER_SUFFIX)) {
            return "";
        }

        // 检查占位符长度是否足够容纳实际内容，如果不是则返回 null
        if (placeholder.length() <= PLACEHOLDER_PREFIX.length() + PLACEHOLDER_SUFFIX.length()) {
            return "";
        }
        // 计算提取内容的起始和结束索引
        int beginIndex = PLACEHOLDER_PREFIX.length();
        int endIndex = placeholder.length() - PLACEHOLDER_SUFFIX.length();
        // 提取实际内容部分
        placeholder = placeholder.substring(beginIndex, endIndex);
        // 查找值分隔符的索引
        int separatorIndex = placeholder.indexOf(VALUE_SEPARATOR);

        // 如果存在值分隔符，则返回分隔符之前的部分作为提取的内容
        if (separatorIndex != -1) {
            return placeholder.substring(0, separatorIndex);
        }
        // 否则，返回经过去除首尾空白后的内容
        return ObjectUtil.trim(placeholder);
    }



    /**
     * 解析属性值中的占位符。
     *
     * @param properties 带有占位符的输入属性。
     * @return 带有已解析占位符的 Properties 对象。
     */
    public static Properties resolve(Properties properties) {
        Properties resolvedProperties = new Properties();

        for (Map.Entry<?, ?> entry : properties.entrySet()) {
            Object valueObj = entry.getValue();
            if (valueObj instanceof CharSequence) {
                String value = valueObj.toString();
                String key = entry.getKey().toString();

                Matcher matcher = PLACEHOLDER_PATTERN.matcher(value);
                StringBuffer resolvedValue = new StringBuffer();

                while (matcher.find()) {
                    String placeholder = matcher.group(1);
                    String replacement = properties.getProperty(placeholder, ""); // 如有需要，可以提供默认值
                    matcher.appendReplacement(resolvedValue, Matcher.quoteReplacement(replacement));
                }
                matcher.appendTail(resolvedValue);
                resolvedProperties.setProperty(key, resolvedValue.toString());
            } else {
                // 若值不是 CharSequence，保持其原样放入已解析属性中
                resolvedProperties.put(entry.getKey(), valueObj);
            }
        }

        return resolvedProperties;
    }




}
