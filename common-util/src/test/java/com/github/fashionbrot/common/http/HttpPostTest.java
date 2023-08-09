package com.github.fashionbrot.common.http;

import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class HttpPostTest {

    @Data
    @Accessors(fluent = true)
    public class Request3 {
        private String method;
        private String v;
    }

    @Test
    public void test1() {

        Request3 request3 = new Request3().method("taobao.opensecurity.uid.get").v("2.0");
        HttpClient.create(
                        new HttpRequest()
                                .contentType(ContentType.FORM_URLENCODED)
                                .httpMethod(HttpMethod.POST)
                                .url("https://eco.taobao.com/router/rest")
                                .header(new HttpHeader()
                                        .add("Content-Type","application/json;charset=UTF-8"))
                )
                .execute(new HttpCallback() {
                    @Override
                    public void success(HttpRequest request, HttpResponse response) {
                        System.out.println(request.url());
                        System.out.println(response.getResponseBody());
                    }
                });
    }


    @Test
    public void test2(){
        String requestBody="id=1689145255816355842&title=HttpClient-test2";
        HttpClient.create(new HttpRequest()
                        .url("http://localhost:8082/banner/updateByIdPost")
                        .httpMethod(HttpMethod.POST)
                        .contentType(ContentType.FORM_URLENCODED)
                        .requestBody(requestBody.getBytes(StandardCharsets.UTF_8))
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
