package com.github.fashionbrot.common.http;

/**
 * @author fashionbrot
 */
public interface HttpCall {


    void success(HttpRequest request,HttpResponse response);

    default void failed(HttpRequest request,HttpResponse response){

    }
    default void exception(HttpRequest request,Exception exception){

    }
}
