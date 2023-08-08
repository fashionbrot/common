package com.github.fashionbrot.common.http;

import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.charset.Charset;

/**
 * @author fashionbrot
 */
@Data
@Accessors(fluent=true)
public class HttpRequest {

    private String url;

    private HttpMethod httpMethod;
    //超时时间
    private Integer connectTimeout;

    private Integer readTimeout;

    private Boolean useCaches;

    private ContentType contentType;

    private Boolean instanceFollowRedirects;

    private Integer chunkedStreamingMode;

    private Long fixedLengthStreamingMode;

    private HttpHeader header;

    private HttpCookie cookie;

    private byte[] requestBody;


}
