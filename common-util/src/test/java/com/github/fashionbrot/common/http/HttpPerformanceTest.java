package com.github.fashionbrot.common.http;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author fashionbrot
 */
public class HttpPerformanceTest {


    public static void main(String[] args) {

    }

    @Test
    public void test1() {
        for (int i = 0; i < 200; i++) {
            HttpClient.get("https://eco.taobao.com/router/rest?method=alibaba.icbu.category.postcat.get")
                    .execute(new HttpCallback() {
                        @Override
                        public void success(HttpRequest request, HttpResponse response) {
                            System.out.println(response.getResponseBody());
                        }

                        @Override
                        public void failed(HttpRequest request, HttpResponse response) {
                            System.out.println(response.getResponseBody());
                        }
                    });
        }

    }

    @Test
    public void test2() {
        byte[] params = "method=alibaba.icbu.category.postcat.get".getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < 200; i++) {
            HttpClient.create(new HttpRequest()
                            .url("https://eco.taobao.com/router/rest")
                            .httpMethod(HttpMethod.POST)
                            .requestBody(params)
                    )
                    .execute(new HttpCallback() {
                        @Override
                        public void success(HttpRequest request, HttpResponse response) {
                            System.out.println(response.getResponseBody());
                        }
                    });
        }

    }

}
