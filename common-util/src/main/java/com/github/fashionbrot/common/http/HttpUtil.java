package com.github.fashionbrot.common.http;

import com.github.fashionbrot.common.consts.CharsetConst;
import com.github.fashionbrot.common.util.IoUtil;
import com.github.fashionbrot.common.util.ObjectUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;


public class HttpUtil {

    public static final Integer DEFAULT_CONNECT_TIMEOUT = 5000;
    public static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);


    /**
     * 发送 HTTP 请求并获取响应。
     *
     * @param request HTTP 请求对象。
     * @return HttpResponse 响应对象。
     * @throws Exception 如果出现异常。
     */
    public static HttpResponse send(HttpRequest request) throws Exception {
        HttpResponse response = new HttpResponse();
        HttpURLConnection httpURLConnection = null;

        try {
            if (!request.verifySsl()){
                trustAllCertificates();
            }

            URL url = new URL(request.url());
            httpURLConnection = (HttpURLConnection) url.openConnection();

            // 设置连接属性
            setConnectionProperties(httpURLConnection, request);

            // 连接并写入请求体
            connectAndWriteRequestBody(httpURLConnection, request);

            // 填充响应信息
            populateResponseInfo(httpURLConnection, response);

            // 读取响应体
            response.responseBody(readResponseBody(httpURLConnection));

        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return response;
    }

    private static void trustAllCertificates() throws NoSuchAlgorithmException, KeyManagementException {
        // 创建信任所有证书的 TrustManager
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };
        // 获取 SSL 上下文
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // 设置默认的 SSLSocketFactory 和 HostnameVerifier
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }


    /**
     * 设置 HttpURLConnection 连接属性，包括请求方法、超时时间、请求头等。
     *
     * @param connection HttpURLConnection 连接对象。
     * @param request    HttpRequest 请求对象。
     * @throws ProtocolException 如果请求方法不支持，将抛出此异常。
     */
    public static void setConnectionProperties(HttpURLConnection connection, HttpRequest request) throws ProtocolException {
        connection.setRequestMethod(request.httpMethod().name());
        connection.setDoInput(true);

        HttpMethod httpMethod = request.httpMethod();
        if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.DELETE ||
                httpMethod == HttpMethod.PATCH || httpMethod == HttpMethod.PUT) {
            connection.setDoOutput(true);
        }

        // 设置连接超时时间
        connection.setConnectTimeout(request.connectTimeout() != null ? request.connectTimeout() : DEFAULT_CONNECT_TIMEOUT);
        connection.setUseCaches(request.useCaches() != null ? request.useCaches() : false);
        connection.setInstanceFollowRedirects(request.instanceFollowRedirects() != null ? request.instanceFollowRedirects() : false);

        // 设置请求头、全局请求头、Cookie 等
        setRequestHeaders(connection, request);
        setGlobalHeader(connection);
        setCookie(connection, request);
    }

    /**
     * 连接并写入请求体，用于 POST、DELETE、PATCH 和 PUT 请求。
     *
     * @param connection HTTP连接。
     * @param request    HttpRequest 请求。
     * @throws IOException 如果发生I/O错误。
     */
    public static void connectAndWriteRequestBody(HttpURLConnection connection, HttpRequest request) throws IOException {
        connection.connect();

        HttpMethod httpMethod = request.httpMethod();
        if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.DELETE ||
                httpMethod == HttpMethod.PATCH || httpMethod == HttpMethod.PUT) {
            try (OutputStream outputStream = connection.getOutputStream()) {
                IoUtil.write(outputStream, request.requestBody());
            }
        }
    }

    /**
     * 从HTTP连接中提取响应信息并填充到 HttpResponse 对象中。
     *
     * @param connection HTTP连接。
     * @param response   HttpResponse 对象。
     * @throws IOException 如果发生I/O错误。
     */
    public static void populateResponseInfo(HttpURLConnection connection, HttpResponse response) throws IOException {
        response.responseCode(connection.getResponseCode());
        response.responseMessage(connection.getResponseMessage());
        response.requestMethod(connection.getRequestMethod());
        response.headerFields(connection.getHeaderFields());
        response.charset(getCharset(connection));
        response.contentLength(getContentLengthLong(connection));
    }

    /**
     * 读取 HttpURLConnection 的响应体并返回字节数组。
     *
     * @param connection HttpURLConnection 连接对象。
     * @return 字节数组表示的响应体内容。
     * @throws IOException 如果读取过程中出现 IO 错误，将抛出此异常。
     */
    public static byte[] readResponseBody(HttpURLConnection connection) throws IOException {
        String encoding = connection.getContentEncoding();
        try (InputStream inputStream = getContentStream(connection, encoding)) {
            return IoUtil.toByte(inputStream);
        }
    }


    /**
     * 获取响应体的输入流，考虑 gzip 压缩等情况。
     *
     * @param connection HTTP连接。
     * @param encoding   内容的编码。
     * @return 包含内容的输入流。
     * @throws IOException 如果发生I/O错误。
     */
    public static InputStream getContentStream(HttpURLConnection connection, String encoding) throws IOException {
        int responseCode = connection.getResponseCode();

        if (responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
            if (ObjectUtil.isNotEmpty(encoding) && encoding.contains("gzip")) {
                return new GZIPInputStream(connection.getInputStream());
            } else {
                return connection.getInputStream();
            }
        } else {
            return connection.getErrorStream();
        }
    }

    /**
     * 设置请求头信息到 HttpURLConnection 请求中。
     *
     * @param httpURLConnection HttpURLConnection 对象
     * @param request           包含请求头信息的请求对象
     */
    public static void setRequestHeaders(HttpURLConnection httpURLConnection, HttpRequest request) {
        HttpHeader header = request.header();
        if (header != null) {
            Boolean override = header.getOverride();
            Map<String, String> headerMap = header.getHeader();
            if (ObjectUtil.isNotEmpty(headerMap)) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (ObjectUtil.isTrue(override)) {
                        httpURLConnection.setRequestProperty(key, value);
                    } else {
                        httpURLConnection.addRequestProperty(key, value);
                    }
                }
            }
        }
        if (header==null && request.contentType()!=null){
            httpURLConnection.setRequestProperty("Content-Type",request.contentType().getValue());;
        }
    }


    /**
     * 设置 Cookie 到 HttpURLConnection 请求头中。
     *
     * @param httpURLConnection HttpURLConnection 对象
     * @param request           包含 Cookie 信息的请求对象
     */
    public static void setCookie(HttpURLConnection httpURLConnection, HttpRequest request) {
        HttpCookie cookie = request.cookie();
        if (cookie != null) {
            List<String> cookieList = cookie.getCookieList();
            for (String cookieString : cookieList) {
                httpURLConnection.addRequestProperty("Cookie", cookieString);
            }
        }
    }


    /**
     * 设置全局请求头信息到 HttpURLConnection 请求中。
     *
     * @param httpURLConnection HttpURLConnection 对象
     */
    public static void setGlobalHeader(HttpURLConnection httpURLConnection) {
        httpURLConnection.setRequestProperty(Header.ACCEPT.name(), "text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
        httpURLConnection.setRequestProperty(Header.ACCEPT_LANGUAGE.name(), "zh-CN,zh;q=0.9,en;q=0.8");
        httpURLConnection.setRequestProperty(Header.ACCEPT_ENCODING.name(), "gzip, deflate");
    }

    /**
     * 从 HTTP 响应头的 Content-Type 中获取字符集信息。
     *
     * @param httpURLConnection HttpURLConnection 对象。
     * @return 解析到的字符集，如果没有找到则返回默认字符集。
     */
    public static Charset getCharset(HttpURLConnection httpURLConnection) {
        if (httpURLConnection == null) {
            return CharsetConst.DEFAULT_CHARSET;
        }

        String contentType = httpURLConnection.getContentType();
        if (ObjectUtil.isEmpty(contentType)){
            return CharsetConst.DEFAULT_CHARSET;
        }
        String charsetName = extractCharsetFromContentType(contentType);
        return (ObjectUtil.isNotEmpty(charsetName)) ? Charset.forName(charsetName) : CharsetConst.DEFAULT_CHARSET;
    }

    /**
     * 从 Content-Type 字符串中提取字符集信息。
     *
     * @param contentType Content-Type 字符串。
     * @return 提取到的字符集，如果没有找到则返回 null。
     */
    public static String extractCharsetFromContentType(String contentType) {
        if (ObjectUtil.isEmpty(contentType)){
            return CharsetConst.UTF_8;
        }
        Matcher matcher = CHARSET_PATTERN.matcher(contentType);
        return matcher.find() ? matcher.group(1) : "";
    }


    private static final long DEFAULT_CONTENT_LENGTH = -1;

    /**
     * 获取 HTTP 响应中的 Content-Length 头字段值。
     *
     * @param httpURLConnection HttpURLConnection 对象。
     * @return Content-Length 头字段值，如果没有找到则返回默认值。
     */
    public static long getContentLengthLong(HttpURLConnection httpURLConnection) {
        if (httpURLConnection == null) {
            throw new IllegalArgumentException("HttpURLConnection cannot be null");
        }

        return httpURLConnection.getHeaderFieldLong("Content-Length", DEFAULT_CONTENT_LENGTH);
    }

}
