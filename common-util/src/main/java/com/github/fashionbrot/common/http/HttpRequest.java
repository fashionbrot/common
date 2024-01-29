package com.github.fashionbrot.common.http;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * HTTP 请求对象，用于封装请求信息。
 */
@Data
@Accessors(fluent = true)
public class HttpRequest {

    /**
     * 请求的 URL 地址
     */
    private String url;

    /**
     * 请求方法（GET、POST 等）
     */
    private HttpMethod httpMethod;

    /**
     * 连接超时时间（单位：毫秒）
     */
    private Integer connectTimeout;

    /**
     * 从服务器读取数据的超时时间（单位：毫秒）
     */
    private Integer readTimeout;

    /**
     * 是否使用缓存
     */
    private Boolean useCaches;

    /**
     * 请求的内容类型
     */
    private ContentType contentType;

    /**
     * 是否自动处理重定向
     */
    private Boolean instanceFollowRedirects;

    /**
     * 用于 chunked 流传输的流的大小（字节数）
     */
    private Integer chunkedStreamingMode;

    /**
     * 用于固定长度流传输的流的大小（字节数）
     */
    private Long fixedLengthStreamingMode;

    /**
     * 请求头信息
     */
    private HttpHeader header;

    /**
     * 请求时携带的 Cookie
     */
    private HttpCookie cookie;

    /**
     * 请求体的字节数组（不包含 GET 请求）
     */
    private byte[] requestBody;

    /**
     * 将 SSL/TLS 证书验证切换为打开或关闭。默认为 TRUE（开启）
     */
    private boolean verifySsl = true;
}
