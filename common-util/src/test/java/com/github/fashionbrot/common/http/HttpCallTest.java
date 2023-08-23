package com.github.fashionbrot.common.http;

import org.junit.Test;

/**
 * @author fashionbrot
 */
public class HttpCallTest {


    @Test
    public void test1() {

        HttpClient.get("https://blog.csdn.net/rfgreeee/article/details/82864240111").execute(new HttpCallback() {
            @Override
            public void success(HttpRequest request, HttpResponse response) {
                System.out.println(response.getResponseBody());
            }

            @Override
            public void failed(HttpRequest request, HttpResponse response) {
                System.out.println(response.getResponseBody());
            }

            @Override
            public void exception(HttpRequest request, Exception exception) {
                System.out.println(exception);
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
                        .contentType(ContentType.JSON)
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
    }


}
