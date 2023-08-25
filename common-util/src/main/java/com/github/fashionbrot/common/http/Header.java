package com.github.fashionbrot.common.http;

/**
 * HTTP 头部字段枚举，定义了常见的 HTTP 头部字段。
 */
public enum Header {

    //------------------------------------------------------------- 通用头域
    /** 提供日期和时间标志，指示报文创建时间 */
    DATE("Date"),
    /** 允许客户端和服务器指定连接选项 */
    CONNECTION("Connection"),
    /** 提供发送端使用的 MIME 版本 */
    MIME_VERSION("MIME-Version"),
    /** 列出采用分块传输编码的报文拖挂字段 */
    TRAILER("Trailer"),
    /** 指示报文使用的传输编码方式 */
    TRANSFER_ENCODING("Transfer-Encoding"),
    /** 指定新版本和协议以进行升级 */
    UPGRADE("Upgrade"),
    /** 表示报文经过的中间节点 */
    VIA("Via"),
    /** 指定缓存机制 */
    CACHE_CONTROL("Cache-Control"),
    /** 控制缓存的实现指令，如 no-cache、no-store */
    PRAGMA("Pragma"),
    /** 指示提交或返回的内容类型 */
    CONTENT_TYPE("Content-Type"),

    //------------------------------------------------------------- 请求头域
    /** 指定请求资源的主机和端口号 */
    HOST("Host"),
    /** 指定请求的源资源地址，用于生成回退链表等 */
    REFERER("Referer"),
    /** 指定请求的源域 */
    ORIGIN("Origin"),
    /** 指定客户端的浏览器信息 */
    USER_AGENT("User-Agent"),
    /** 指定客户端接受的内容类型 */
    ACCEPT("Accept"),
    /** 指定客户端优先接受的语言 */
    ACCEPT_LANGUAGE("Accept-Language"),
    /** 指定客户端支持的内容压缩编码 */
    ACCEPT_ENCODING("Accept-Encoding"),
    /** 指定客户端接受的字符编码集 */
    ACCEPT_CHARSET("Accept-Charset"),
    /** 发送请求域下的所有 Cookie */
    COOKIE("Cookie"),
    /** 请求的内容长度 */
    CONTENT_LENGTH("Content-Length"),

    //------------------------------------------------------------- 响应头域
    /** 响应中的 Cookie 字段 */
    SET_COOKIE("Set-Cookie"),
    /** 响应内容的压缩编码方式 */
    CONTENT_ENCODING("Content-Encoding"),
    /** 响应内容的展示方式 */
    CONTENT_DISPOSITION("Content-Disposition"),
    /** 资源标识符的版本号 */
    ETAG("ETag"),
    /** 重定向的目标 URL */
    LOCATION("Location");

    private String value;

    Header(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

