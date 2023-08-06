package com.github.fashionbrot.common.http;

import com.github.fashionbrot.common.util.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author fashionbrot
 */
@Slf4j
public class HttpTest {


    @Test
    public void test1(){

        HttpRequest request =new HttpRequest()
                .url("https://baidu.com")
                .httpMethod(HttpMethod.GET)
                .connectTimeout(500)
                .readTimeout(500)
                .useCaches(false)
                .contentType(HttpContentType.TEXT_HTML)
                ;
        HttpClient.send(request, new HttpCall() {
            @Override
            public void success(HttpRequest request, HttpResponse response) {
                String s = IoUtil.toString(response.inputStream(), response.charset());
                System.out.println(s);
                System.out.println("success");
            }

            @Override
            public void failed(HttpRequest request, HttpResponse response) {
                String s = IoUtil.toString(response.inputStream(), response.charset());
                System.out.println(s);
                System.out.println("failed");
            }

            @Override
            public void exception(HttpRequest request, Exception exception) {
                System.out.println("exception");
            }
        });


    }


    @Test
    public void test2(){
        String url = "https://api.apiopen.top/api/getHaoKanVideo?page=0&size=2";
        HttpClient.send(new HttpRequest()
                .url(url)
                .httpMethod(HttpMethod.GET)
                .connectTimeout(1000)
                .readTimeout(1000)
                .useCaches(false)
                .contentType(HttpContentType.JSON)
                .header(new HttpHeader()
                        .add("Accept-Encoding","gzip,compress")
                        .add("Charset","UTF-8")
                        .add("accept","application/json")
                ),
                new HttpCall() {
            @Override
            public void success(HttpRequest request, HttpResponse response) {
                String content = IoUtil.toString(response.inputStream(),response.charset());

                System.out.println("success:"+content);
            }

            @Override
            public void failed(HttpRequest request, HttpResponse response) {
                System.out.println("failed");
            }

            @Override
            public void exception(HttpRequest request, Exception exception) {
                System.out.println("exception"+exception.getMessage());
                log.error("error",exception);
            }
        });
    }


    @Test
    public void test3(){
        String url="";
        String body = "";
        HttpClient.send(new HttpRequest()
                        .url(url)
                        .httpMethod(HttpMethod.POST)
                        .connectTimeout(2000)
                        .readTimeout(2000)
                        .contentType(HttpContentType.JSON)
                        .header(new HttpHeader()
                                .add("Accept-Encoding","gzip,compress")
                                .add("Charset","UTF-8")
                                .add("accept","application/json")
                        )
                        .cookie(new HttpCookie().add("zhangsan=zhangsan"))
                        .body(body.getBytes(StandardCharsets.UTF_8))
                ,
                new HttpCall() {
                    @Override
                    public void success(HttpRequest request, HttpResponse response) {
                        byte[] bytes = IoUtil.toByteArray(response.inputStream());
                        String content = IoUtil.toString(bytes,response.charset());

                        System.out.println("success:"+content);
                        System.out.println("length:"+bytes.length);
                    }
                });
    }
}
