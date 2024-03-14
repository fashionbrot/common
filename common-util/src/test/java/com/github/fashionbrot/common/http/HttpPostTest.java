package com.github.fashionbrot.common.http;

import com.github.fashionbrot.common.consts.CharsetConst;
import com.github.fashionbrot.common.util.IoUtil;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class HttpPostTest {


    @Test
    public void test1() {

        String entityToUrlParam = "{\"cityCode\":\"110100\"}";//HttpParamUtil.entityToUrlParam(request3, false);

        System.out.println(entityToUrlParam);
        String url ="http://localhost:8083//api/areaList";

        HttpRequest request = HttpRequest.builder()
                .httpMethod(HttpMethod.POST)
                .url(url)
                .contentType(ContentType.JSON)
                .header(new HttpHeader()
                        .add("Content-Type","application/json;charset=UTF-8"))
                .requestBody(entityToUrlParam.getBytes(StandardCharsets.UTF_8))
                .build();
        String responseText = "";
        try {
            HttpResponse response = HttpUtil.send(request);
            if (response!=null){
                byte[] bytes = response.responseBody();
                responseText = IoUtil.toString(bytes, CharsetConst.UTF8_CHARSET);
            }
        } catch (Exception e) {
//            throw new RuntimeException(e);
        }

        System.out.println(responseText);
    }



}
