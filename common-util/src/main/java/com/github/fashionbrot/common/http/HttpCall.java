package com.github.fashionbrot.common.http;



/**
 * @author fashionbrot
 */
public interface HttpCall {

    void execute(HttpCallback callback);

    void executeAsync(HttpCallback callback);

    HttpResponse execute() throws Exception;

}
