package com.github.fashionbrot.common.http;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author fashionbrot
 */
@Data
@Accessors(fluent=true)
public class HttpRequest {

    /**
     * 请求 url
     */
    private String url;

    /**
     * 请求方法
     */
    private HttpMethod httpMethod;
    /**
     * 超时时间
     */
    private Integer connectTimeout;
    /**
     * 建立连接后从服务器读取到可用资源所用的时间
     */
    private Integer readTimeout;

    private Boolean useCaches;

    /**
     * 设置请求类型
     */
    private ContentType contentType;

    private Boolean instanceFollowRedirects;

    private Integer chunkedStreamingMode;

    private Long fixedLengthStreamingMode;

    /**
     * 入参 header
     */
    private HttpHeader header;

    /**
     * 入参 cookie
     */
    private HttpCookie cookie;

    /**
     * 入参 不包含GET 请求
     */
    private byte[] requestBody;


}
