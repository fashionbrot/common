package com.github.fashionbrot.common.http;


import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


/**
 * HTTP 客户端类，用于创建和执行 HTTP 请求。
 * @author fashionbrot
 */
@Slf4j
public class HttpClient {

    // HTTP 请求对象
    private final HttpRequest request;

    /**
     * 构造方法，初始化 HTTP 请求对象。
     *
     * @param request 要执行的 HTTP 请求对象。
     */
    public HttpClient(HttpRequest request) {
        this.request = request;
    }

    /**
     * 执行同步 HTTP 请求并回调处理结果。
     *
     * @param callback HTTP 回调对象，用于处理请求结果。
     */
    public void execute(HttpCallback callback) {
        // 创建 HTTP 调用实例
        HttpCall httpCall = new HttpReadCall(request);
        // 执行 HTTP 请求并回调处理结果
        httpCall.execute(callback);
    }

    /**
     * 异步执行 HTTP 请求并回调处理结果。
     *
     * @param callback HTTP 回调对象，用于处理请求结果。
     */
    public void executeAsync(HttpCallback callback) {
        // 创建 HTTP 调用实例
        HttpCall httpCall = new HttpReadCall(request);
        // 在新线程中执行 HTTP 请求并回调处理结果
        httpCall.executeAsync(callback);
    }

    /**
     * 创建 HTTP 客户端实例。
     *
     * @param request 要执行的 HTTP 请求对象。
     * @return 新的 HttpClient 实例。
     */
    public static HttpClient create(HttpRequest request) {
        return new HttpClient(request);
    }

    /**
     * 创建 GET 请求的 HTTP 客户端实例。
     *
     * @param url 请求的 URL 地址。
     * @return 新的 HttpClient 实例。
     */
    public static HttpClient get(final String url) {
        // 创建 HTTP 请求对象并设置 GET 请求方法
        return new HttpClient(new HttpRequest().url(url).httpMethod(HttpMethod.GET));
    }

    /**
     * 创建 HTTP 调用实例。
     *
     * @param request 要执行的 HTTP 请求对象。
     * @return 新的 HttpCall 实例。
     */
    public static HttpCall createCall(HttpRequest request) {
        return new HttpReadCall(request);
    }

    /**
     * 执行 HTTP 请求并返回响应对象。
     *
     * @param request 要执行的 HTTP 请求对象。
     * @return HttpResponse 响应对象。
     * @throws Exception 如果执行过程中出现异常。
     */
    public static HttpResponse execute(HttpRequest request) throws Exception {
        // 创建 HTTP 调用实例并执行请求
        return new HttpReadCall(request).execute();
    }
}

