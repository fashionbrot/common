package com.github.fashionbrot.common.http;


import lombok.extern.slf4j.Slf4j;

/**
 * @author fashionbrot
 */
@Slf4j
public class HttpClient {

    private  HttpRequest request;


    public HttpClient(HttpRequest request) {
        this.request = request;
    }

    public  void execute(HttpCallback callback){
        HttpCall httpCall = new HttpReadCall(request);
        httpCall.execute(callback);
    }

    public  void executeAsync(HttpCallback callback){
        HttpCall httpCall = new HttpReadCall(request);
        httpCall.executeAsync(callback);
    }


    public static HttpClient create(HttpRequest request) {
        return new HttpClient(request);
    }


    public static HttpClient get(String url) {
        return new HttpClient(new HttpRequest().url(url).httpMethod(HttpMethod.GET));
    }




}
