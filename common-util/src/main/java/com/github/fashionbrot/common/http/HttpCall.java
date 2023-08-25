package com.github.fashionbrot.common.http;



/**
 * @author fashionbrot
 */
/**
 * HTTP 请求调用接口，定义了 HTTP 请求的执行方法。
 */
public interface HttpCall {

    /**
     * 执行 HTTP 请求并回调处理结果。
     *
     * @param callback HTTP 回调对象，用于处理请求结果。
     */
    void execute(HttpCallback callback);

    /**
     * 异步执行 HTTP 请求并回调处理结果。
     *
     * @param callback HTTP 回调对象，用于处理请求结果。
     */
    void executeAsync(HttpCallback callback);

    /**
     * 执行 HTTP 请求并返回响应对象。
     *
     * @return HttpResponse 响应对象。
     * @throws Exception 如果执行过程中出现异常。
     */
    HttpResponse execute() throws Exception;
}