package com.github.fashionbrot.common.http;

/**
 * @author fashionbrot
 */
public interface HttpCallback {



    /**
     * 请求成功 responseCode==200
     * @param request HttpRequest
     * @param response HttpResponse
     */
    void success(HttpRequest request,HttpResponse response);

    /**
     * 请求失败 responseCode!=200
     * @param request HttpRequest
     * @param response HttpResponse
     */
    default void failed(HttpRequest request,HttpResponse response){

    }

    /**
     * 请求异常
     * @param request HttpRequest
     * @param exception Exception
     */
    default void exception(HttpRequest request,Exception exception){

    }

}
