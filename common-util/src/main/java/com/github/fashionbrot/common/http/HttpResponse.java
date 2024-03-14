package com.github.fashionbrot.common.http;

import com.github.fashionbrot.common.util.IoUtil;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import com.github.fashionbrot.common.util.IoUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * HTTP 响应对象，用于封装服务器响应信息。
 */
@Data
@Accessors(fluent = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HttpResponse {

    // 服务器响应的状态代码
    private int responseCode;

    // 服务器响应的状态消息
    private String responseMessage;

    // 请求的方法（HTTP 方法）
    private String requestMethod;

    // 服务器响应的头字段信息
    private Map<String, List<String>> headerFields;

    // 响应的字符编码
    private Charset charset;

    // 响应体的字节数组
    private byte[] responseBody;

    // 响应体的长度
    private long contentLength;

}

