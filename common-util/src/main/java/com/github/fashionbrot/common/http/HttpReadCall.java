package com.github.fashionbrot.common.http;

import com.github.fashionbrot.common.util.NanoIdUtil;
import com.github.fashionbrot.common.util.Sequence;
import com.github.fashionbrot.common.util.SequenceUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fashionbrot
 */
@Slf4j
public class HttpReadCall implements HttpCall{

    private HttpRequest request;

    public HttpReadCall(HttpRequest request) {
        this.request = request;
    }

    @Override
    public void execute(HttpCallback callback) {
        try {
            HttpResponse send = HttpUtil.send(request);
            callback.success(request,send);
        }catch (Exception e){
            callback.exception(request,e);
        }
    }

    @Override
    public void executeAsync(HttpCallback callback) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                execute(callback);
            }
        });
        thread.start();
    }


}
