package com.github.fashionbrot.common.http;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fashionbrot
 */
import java.util.ArrayList;
import java.util.List;

/**
 * HTTP 请求中的 Cookie 封装类。
 */
public class HttpCookie {

    // 存储多个 Cookie 的列表
    private List<String> cookieList = new ArrayList<>();

    /**
     * 添加一个 Cookie 到列表中。
     *
     * @param cookie 要添加的 Cookie 字符串。
     * @return 当前 HttpCookie 实例，用于链式调用。
     */
    public HttpCookie add(String cookie) {
        cookieList.add(cookie);
        return this;
    }

    /**
     * 获取存储的多个 Cookie。
     *
     * @return 存储的多个 Cookie 列表。
     */
    public List<String> getCookieList() {
        return cookieList;
    }
}
