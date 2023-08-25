package com.github.fashionbrot.common.http;

import com.github.fashionbrot.common.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author fashionbrot
 */
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * HTTP 请求和响应的内容类型枚举，定义了常见的内容类型。
 */
@AllArgsConstructor
@Getter
public enum ContentType {

    // 标准表单编码，浏览器会使用 x-www-form-urlencoded 编码方式将 form 数据转换成字串
    FORM_URLENCODED("application/x-www-form-urlencoded"),
    // 文件上传编码，浏览器会将整个表单分割为多个部分并添加控件，每部分都带有 Content-Disposition 和分割符(boundary)
    MULTIPART("multipart/form-data"),
    // REST 请求 JSON 编码
    JSON("application/json"),
    // REST 请求 XML 编码
    XML("application/xml"),
    // REST 请求 text/xml 编码
    TEXT_XML("text/xml"),
    // text/html 编码
    TEXT_HTML("text/html");

    private String value;

    // 映射内容类型和 ContentType 枚举的映射表
    private static Map<String, ContentType> map = new HashMap<>();

    static {
        // 初始化映射表
        for (ContentType contentType : ContentType.values()) {
            map.put(contentType.value, contentType);
        }
    }

    /**
     * 根据内容类型字符串判断是否包含在枚举中，如果不包含则默认为 FORM_URLENCODED 类型。
     *
     * @param contentType 内容类型字符串
     * @return 对应的 ContentType 枚举值
     */
    public static ContentType contains(String contentType) {
        if (contentType == null || contentType.isEmpty()) {
            return ContentType.FORM_URLENCODED;
        }

        // 遍历映射表，判断内容类型字符串是否包含在映射表中
        for (Map.Entry<String, ContentType> entry : map.entrySet()) {
            if (contentType.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // 默认为 FORM_URLENCODED 类型
        return ContentType.FORM_URLENCODED;
    }
}

