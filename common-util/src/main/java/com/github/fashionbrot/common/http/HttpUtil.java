package com.github.fashionbrot.common.http;

import com.github.fashionbrot.common.consts.CharsetConst;
import com.github.fashionbrot.common.util.CharsetUtil;
import com.github.fashionbrot.common.util.IoUtil;
import com.github.fashionbrot.common.util.ObjectUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * @author fashionbrot
 */
public class HttpUtil {


    public static HttpResponse send(HttpRequest request)throws Exception {

        HttpResponse response = new HttpResponse();
        HttpURLConnection httpURLConnection = null;
        try {
            //得到访问地址的URL
            URL url = new URL(request.url());
            //得到网络访问对象
            httpURLConnection = (HttpURLConnection) url.openConnection();

            if (request.httpMethod()==null){
                // 请求方式
                httpURLConnection.setRequestMethod(request.httpMethod().name());
            }
            httpURLConnection.setDoInput(true);
            if (request.httpMethod() == HttpMethod.POST ||
                    request.httpMethod() == HttpMethod.DELETE ||
                    request.httpMethod() == HttpMethod.PATCH ||
                    request.httpMethod() == HttpMethod.PUT ) {

                // 设置是否输出
                httpURLConnection.setDoOutput(true);
            }
            if (request.connectTimeout() != null) {
                // 超时时间
                httpURLConnection.setConnectTimeout(request.connectTimeout());
            }
            // 设置是否使用缓存
            httpURLConnection.setUseCaches(request.useCaches()!=null?request.useCaches():false);
            // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
            httpURLConnection.setInstanceFollowRedirects(request.instanceFollowRedirects()!=null?request.instanceFollowRedirects():false);

            if (request.chunkedStreamingMode()!=null) {
                httpURLConnection.setChunkedStreamingMode(request.chunkedStreamingMode());
            }
            if (request.fixedLengthStreamingMode()!=null){
                httpURLConnection.setFixedLengthStreamingMode(request.fixedLengthStreamingMode());
            }

            if (request.readTimeout() != null) {
                httpURLConnection.setReadTimeout(request.readTimeout());
            }
            if (request.contentType()!=null) {
                httpURLConnection.addRequestProperty(Header.CONTENT_TYPE.name(), request.contentType().getValue());
            }

            setGlobalHeader(httpURLConnection);
            setHeader(httpURLConnection,request);
            setCookie(httpURLConnection,request);

            // 连接
            httpURLConnection.connect();

            if (request.httpMethod() == HttpMethod.POST ||
                    request.httpMethod() == HttpMethod.DELETE ||
                    request.httpMethod() == HttpMethod.PATCH ||
                    request.httpMethod() == HttpMethod.PUT ) {
                IoUtil.write(httpURLConnection.getOutputStream(),request.requestBody());
            }

            InputStream inputStream = null;
            String encoding = httpURLConnection.getContentEncoding();
            if(ObjectUtil.isNotEmpty(encoding) && encoding.contains("gzip")){
                inputStream = new GZIPInputStream(httpURLConnection.getInputStream());
            }else{
                inputStream = httpURLConnection.getInputStream();
            }
            response.charset(getCharset(httpURLConnection));
            response.responseCode(httpURLConnection.getResponseCode());
            response.responseMessage(httpURLConnection.getResponseMessage());
            response.requestMethod(httpURLConnection.getRequestMethod());
            response.headerFields(httpURLConnection.getHeaderFields());
            response.responseBody(IoUtil.toByteAndClose(inputStream));
            response.contentLength(getContentLengthLong(httpURLConnection));

        }finally {
            if (httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
        }
        return response;
    }

    /** 正则：Content-Type中的编码信息 */
    public static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);

    public static Charset getCharset(HttpURLConnection httpURLConnection){
        if (httpURLConnection!=null){
            String contentType = httpURLConnection.getContentType();
            String charsetName =get(CHARSET_PATTERN, contentType, 1);
            if (ObjectUtil.isNotEmpty(charsetName)){
                return Charset.forName(charsetName);
            }
        }
        return CharsetConst.DEFAULT_CHARSET;
    }

    /**
     * 获得匹配的字符串，对应分组0表示整个匹配内容，1表示第一个括号分组内容，依次类推
     *
     * @param pattern 编译后的正则模式
     * @param content 被匹配的内容
     * @param groupIndex 匹配正则的分组序号，0表示整个匹配内容，1表示第一个括号分组内容，依次类推
     * @return 匹配后得到的字符串，未匹配返回null
     */
    public static String get(Pattern pattern, CharSequence content, int groupIndex) {
        if (null == content || null == pattern) {
            return null;
        }

        final Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return null;
    }


    public static void setHeader(HttpURLConnection httpURLConnection,HttpRequest request){
        HttpHeader header = request.header();
        if (header!=null){
            Boolean override = header.getOverride();
            Map<String, String> headerMap = header.getHeader();
            if (ObjectUtil.isNotEmpty(headerMap)){
                Iterator<Map.Entry<String, String>> iterator = headerMap.entrySet().iterator();
                if (iterator.hasNext()){
                    Map.Entry<String, String> next = iterator.next();
                    if (override!=null && override){
                        httpURLConnection.setRequestProperty(next.getKey(),next.getValue());
                    }else{
                        httpURLConnection.addRequestProperty(next.getKey(),next.getValue());
                    }
                }
            }
        }
    }

    public static void setCookie(HttpURLConnection httpURLConnection,HttpRequest request){
        HttpCookie cookie = request.cookie();
        if (cookie!=null){
            List<String> cookieList = cookie.getCookieList();
            if (ObjectUtil.isNotEmpty(cookieList)){
                for (int i = 0; i < cookieList.size(); i++) {
                    String cookieString = cookieList.get(i);
                    httpURLConnection.addRequestProperty("Cookie",cookieString);
                }
            }
        }
    }

    public static long getContentLengthLong(HttpURLConnection httpURLConnection) {
        return httpURLConnection.getHeaderFieldLong("Content-Length", -1);
    }

    /**
     * 字符串转 byte[]
     * @param str 字符串
     * @param charsetName charsetName如utf-8、gbk...
     * @return byte[]
     */
    public static byte[] toByte(String str,String charsetName){
        if (ObjectUtil.isEmpty(str)){
            return null;
        }
        Charset charset = CharsetUtil.getCharset(charsetName);
        if (charset!=null){
            return str.getBytes(charset);
        }
        return str.getBytes();
    }

    /**
     * 字符串转 byte[]
     * @param str 字符串
     * @param charset Charset
     * @return byte[]
     */
    public static byte[] toByte(String str,Charset charset){
        if (ObjectUtil.isEmpty(str)){
            return null;
        }
        if (charset!=null){
            return str.getBytes(charset);
        }
        return str.getBytes();
    }

    /**
     * 字符串转 byte[]
     * @param str 字符串
     * @return byte[]
     */
    public static byte[] toByte(String str){
        return toByte(str,(String) null);
    }


    public static void setGlobalHeader(HttpURLConnection httpURLConnection){
        httpURLConnection.addRequestProperty(Header.ACCEPT.name(),"text/html,application/xhtml+xml,application/xml,application/json;q=0.9,*/*;q=0.8");
        httpURLConnection.addRequestProperty(Header.ACCEPT_LANGUAGE.name(),"zh-CN,zh;q=0.9,en;q=0.8");
        httpURLConnection.addRequestProperty(Header.ACCEPT_ENCODING.name(),"gzip, deflate");
    }

}
