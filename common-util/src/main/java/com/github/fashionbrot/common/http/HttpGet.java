package com.github.fashionbrot.common.http;

import com.github.fashionbrot.common.consts.CharsetConst;
import com.github.fashionbrot.common.util.CharsetUtil;
import com.github.fashionbrot.common.util.IoUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Map;

@Slf4j
public class HttpGet {

    /**
     * 发送 HTTP GET 请求并获取响应结果，使用默认的字符集。
     *
     * @param url 请求的 URL
     * @return 响应结果
     */
    public static String send(final String url){
        return send(url,CharsetConst.UTF8_CHARSET);
    }

    /**
     * 发送 HTTP GET 请求并获取响应结果，使用指定的字符集。
     *
     * @param url          请求的 URL
     * @param responseChatSet 响应结果的字符集
     * @return 响应结果
     */
    public static String send(final String url,final String responseChatSet ){
        return send(url,false,responseChatSet);
    }

    /**
     * 发送 HTTP GET 请求并获取响应结果，使用指定的字符集和是否验证 SSL。
     *
     * @param url          请求的 URL
     * @param verifySsl    是否验证 SSL
     * @param responseChatSet 响应结果的字符集
     * @return 响应结果
     */
    public static String send(final String url,final Boolean verifySsl,final String responseChatSet){
        String responseText = "";
        HttpResponse httpResponse = getHttpResponse(url,verifySsl);
        if (httpResponse!=null){
            Charset charset = CharsetUtil.getCharset(responseChatSet);
            if (charset==null){
                charset = CharsetConst.DEFAULT_CHARSET;
            }
            responseText = IoUtil.toString(httpResponse.responseBody(),charset);
        }
        return responseText;
    }


    /**
     * 发送 HTTP GET 请求并获取响应结果，使用指定的字符集。
     *
     * @param url          请求的 URL
     * @param responseChatSet 响应结果的字符集
     * @return 响应结果
     */
    public static String send(final String url,final Charset responseChatSet){
        String responseText = "";
        HttpResponse httpResponse = getHttpResponse(url,false);
        if (httpResponse!=null){
            responseText = IoUtil.toString(httpResponse.responseBody(),responseChatSet);
        }
        return responseText;
    }


    public static String send(final String url,final Map<String,Object> params,final String responseChatSet){
        return send(url,params,false,false,responseChatSet);
    }

    public static String send(final String url,final Map<String,Object> params,final Boolean verifySsl,final Charset responseChatSet){
        return send(url,params,verifySsl,false,responseChatSet);
    }

    public static String send(final String url,final Map<String,Object> params,final Charset responseChatSet){
        return send(url,params,false,false,responseChatSet);
    }


    public static String send(final String url,final Map<String,Object> params,final Boolean verifySsl,boolean paramsIgnoreNull,final String responseChatSet){
        String responseText = "";
        HttpResponse httpResponse = getHttpResponse(url,params,verifySsl, paramsIgnoreNull);
        if (httpResponse!=null){
            Charset charset = CharsetUtil.getCharset(responseChatSet);
            if (charset==null){
                charset = CharsetConst.DEFAULT_CHARSET;
            }
            responseText = IoUtil.toString(httpResponse.responseBody(),charset);
        }
        return responseText;
    }


    public static String send(final String url,final Map<String,Object> params,final Boolean verifySsl,final boolean paramsIgnoreNull,final Charset responseChatSet){
        String responseText = "";
        HttpResponse httpResponse = getHttpResponse(url,params,verifySsl, paramsIgnoreNull);
        if (httpResponse!=null){
            responseText = IoUtil.toString(httpResponse.responseBody(),responseChatSet);
        }
        return responseText;
    }



    public static HttpResponse getHttpResponse(final String url,final Boolean verifySsl){
        return getHttpResponse(new HttpRequest().url(url).verifySsl(verifySsl).httpMethod(HttpMethod.GET));
    }


    public static HttpResponse getHttpResponse(final String url,final Map<String,Object> params,final Boolean verifySsl,final boolean paramsIgnoreNull){
        String paramsString = HttpParamUtil.mapToUrlParam(params, paramsIgnoreNull);
        return getHttpResponse(HttpParamUtil.formatGetUrl(url,paramsString),verifySsl);
    }


    public static HttpResponse getHttpResponse(HttpRequest request){
        HttpResponse httpResponse = null;
        try {
            httpResponse =  HttpUtil.send(request);
        } catch (Exception e) {
            log.error("getHttpResponse error",e);
        }
        return httpResponse;
    }

}
