package com.github.fashionbrot.common.http;


import lombok.extern.slf4j.Slf4j;

/**
 * @author fashionbrot
 */
@Slf4j
public class HttpClient {

    private static HttpRequest request;

    public HttpClient(HttpRequest request) {
        this.request = request;
    }

    public static HttpClient create(HttpRequest request){
        return new HttpClient(request);
    }

    public static void execute(HttpCallback callback){
        HttpCall httpCall = new HttpReadCall(request);
        httpCall.execute(callback);
    }

    public static void executeAsync(HttpCallback callback){
        HttpCall httpCall = new HttpReadCall(request);
        httpCall.executeAsync(callback);
    }

}
