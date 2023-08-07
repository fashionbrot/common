package com.github.fashionbrot.common.http;

import org.junit.Test;

/**
 * @author fashionbrot
 */
public class HttpCallTest {



    @Test
    public void test1() {
        String url = "https://api.apiopen.top/api/getHaoKanVideo?page=0&size=2";
        HttpClient.create(
                new HttpRequest()
                        .url(url)
                        .httpMethod(HttpMethod.GET)
                        .connectTimeout(2000)
                        .readTimeout(2000)
                        .contentType(HttpContentType.JSON)
                        .header(new HttpHeader()
                                .override(true)
                                .add("Accept-Encoding", "gzip,compress")
                                .add("Charset", "UTF-8")
                                .add("accept", "application/json")
                        )
                        .cookie(new HttpCookie().add("zhangsan=zhangsan"))
        ).execute(new HttpCallback() {
            @Override
            public void success(HttpRequest request, HttpResponse response) {
                System.out.println(response.getResponseBody());
            }
        });
    }

    @Test
    public void test2() {
        String url = "https://api.apiopen.top/api/getHaoKanVideo?page=0&size=2";
        HttpClient.create(
                new HttpRequest()
                        .url(url)
                        .httpMethod(HttpMethod.GET)
                        .contentType(HttpContentType.JSON)
                        .header(new HttpHeader()
                                .add("Accept-Encoding", "gzip,compress")
                                .add("Charset", "UTF-8")
                                .add("accept", "application/json")
                        )
                        .cookie(new HttpCookie().add("zhangsan=zhangsan"))
        ).executeAsync (new HttpCallback() {
            @Override
            public void success(HttpRequest request, HttpResponse response) {
                System.out.println(response.getResponseBody());
            }
        });
        System.out.println("test2");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("结束");
    }
}
