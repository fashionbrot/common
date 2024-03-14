package com.github.fashionbrot.common.http;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpGetTest {


    @Test
    public void test0() throws Exception {
        String responseBody = "{\"code\":200,\"message\":\"成功!\",\"result\":{\"total\":14782,\"list\":[{\"id\":1,\"title\":\"复盘局（最后的对决）：天才神医PK老玩家，上演谍中谍中谍？\",\"userName\":\"清禾电影\",\"userPic\":\"https://pic.rmb.bdstatic.com/bjh/user/a28440c6e36702bec3b8c8ed91e8ebbe.jpeg?x-bce-process=image/resize,m_lfit,w_200,h_200\\u0026autime=65208\",\"coverUrl\":\"https://f7.baidu.com/it/u=3414722929,2491184617\\u0026fm=222\\u0026app=108\\u0026f=JPEG@s_2,w_681,h_381,q_100\",\"playUrl\":\"http://vd2.bdstatic.com/mda-mjtjk7ck9dmcx0kt/cae_h264/1635505796467794816/mda-mjtjk7ck9dmcx0kt.mp4\",\"duration\":\"06:58\"}]}}";
        String result = HttpGet.send("https://api.apiopen.top/api/getHaoKanVideo?page=1&size=1");
        System.out.println(result);
        Assert.assertEquals(responseBody,result);
    }



}
