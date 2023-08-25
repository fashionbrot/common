package com.github.fashionbrot.common.http;

import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.util.concurrent.CompletableFuture;

/**
 * @author fashionbrot
 */
/**
 * 读取型 HTTP 请求调用实现。
 */
public class HttpReadCall implements HttpCall {

    private HttpRequest request;

    public HttpReadCall(final HttpRequest request) {
        this.request = request;
    }

    @Override
    public void execute(HttpCallback callback) {
        try {
            HttpResponse response = execute();
            if (response.responseCode() == HttpURLConnection.HTTP_OK) {
                callback.success(request, response);
            } else {
                callback.failed(request, response);
            }
        } catch (Exception e) {
            callback.exception(request, e);
        }
    }

    @Override
    public void executeAsync(HttpCallback callback) {
        Thread thread = new Thread(() -> {
            synchronized (this) {
                execute(callback);
            }
        });
        thread.start();
    }

    @Override
    public HttpResponse execute() throws Exception {
        return HttpUtil.send(request);
    }
}