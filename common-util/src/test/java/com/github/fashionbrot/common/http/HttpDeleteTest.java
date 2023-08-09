package com.github.fashionbrot.common.http;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class HttpDeleteTest {

    @Test
    public void test1(){
        String requestBody="{\"id\":\"1689144167306387457\"}";
        HttpClient.create(new HttpRequest()
                        .url("http://localhost:8082/banner/deleteById2")
                        .httpMethod(HttpMethod.DELETE)
                        .contentType(ContentType.JSON)
                        .requestBody(requestBody.getBytes(StandardCharsets.UTF_8))
                        .header(new HttpHeader().add("Content-Type","application/json;charset=UTF-8"))
                )
                .execute(new HttpCallback() {
                    @Override
                    public void success(HttpRequest request, HttpResponse response) {
                        System.out.println(response.getResponseBody());
                    }

                    @Override
                    public void failed(HttpRequest request, HttpResponse response) {
                        System.out.println(response.responseCode());
                        System.out.println(response.getResponseBody());
                    }
                });
    }


}
