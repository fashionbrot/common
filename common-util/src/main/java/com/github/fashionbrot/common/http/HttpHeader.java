package com.github.fashionbrot.common.http;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fashionbrot
 */
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP 请求头信息封装类。
 */
public class HttpHeader {

    // 是否允许覆盖已存在的请求头
    private Boolean override;

    // 存储请求头的键值对
    private Map<String, String> header = new HashMap<>();

    /**
     * 设置是否允许覆盖已存在的请求头。
     *
     * @param override 是否允许覆盖。
     * @return 当前 HttpHeader 实例，用于链式调用。
     */
    public HttpHeader override(Boolean override) {
        this.override = override;
        return this;
    }

    /**
     * 添加单个请求头。
     *
     * @param key   请求头的键。
     * @param value 请求头的值。
     * @return 当前 HttpHeader 实例，用于链式调用。
     */
    public HttpHeader add(String key, String value) {
        header.put(key, value);
        return this;
    }

    /**
     * 添加多个请求头。
     *
     * @param headers 要添加的多个请求头键值对。
     * @return 当前 HttpHeader 实例，用于链式调用。
     */
    public HttpHeader add(Map<String, String> headers) {
        header.putAll(headers);
        return this;
    }

    /**
     * 直接设置请求头。
     *
     * @param headers 要设置的请求头键值对。
     * @return 当前 HttpHeader 实例，用于链式调用。
     */
    public HttpHeader set(Map<String, String> headers) {
        this.header = headers;
        return this;
    }

    /**
     * 获取所有的请求头键值对。
     *
     * @return 请求头键值对。
     */
    public Map<String, String> getHeader() {
        return header;
    }

    /**
     * 获取是否允许覆盖已存在的请求头。
     *
     * @return 是否允许覆盖。
     */
    public Boolean getOverride() {
        return override;
    }
}
