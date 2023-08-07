package com.github.fashionbrot.common.http;

import com.github.fashionbrot.common.util.IoUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author fashionbrot
 */
@Data
@Accessors(fluent=true)
public class HttpResponse {

    // 获取服务器的响应代码。
    private int responseCode;
    // 获取服务器的响应消息。
    private String responseMessage;

    private String requestMethod;

    private Map<String, List<String>> headerFields;

    private Charset charset;

    private byte[] responseBody;

    private long contentLength;

    public String getResponseBody(){
        return IoUtil.toString(responseBody,charset);
    }
}
