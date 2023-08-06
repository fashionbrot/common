package com.github.fashionbrot.common.util;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author fashionbrot
 */
@Slf4j
public class GzipUtil {

    private static final int DEFAULT_BYTE_ARRAY_LENGTH = 32;

    /** 默认编码，使用平台相关编码 */
    private static final Charset DEFAULT_CHARSET = CharsetUtil.defaultCharset();


    /**
     * Gzip压缩处理
     *
     * @param bytes 被压缩的字节流
     * @return 压缩后的字节流
     */
    public static byte[] gzip(byte[] bytes) {
        return gzip(new ByteArrayInputStream(bytes), bytes.length);
    }

    /**
     * Gzip压缩文件
     *
     * @param in 被压缩的流
     * @param length 预估长度
     * @return 压缩后的字节流
     */
    public static byte[] gzip(InputStream in, int length) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
        GZIPOutputStream outputStream = null;
        try {
            outputStream = new GZIPOutputStream(bos);
            IoUtil.copy(in, outputStream);
        } catch (IOException e) {
            log.error("gzip error",e);
        } finally {
            IoUtil.close(outputStream);
        }
        // 返回必须在关闭gos后进行，因为关闭时会自动执行finish()方法，保证数据全部写出
        return bos.toByteArray();
    }


    /**
     * Gzip解压缩处理
     *
     * @param buf 压缩过的字节流
     * @param charset 编码
     * @return 解压后的字符串
     * @throws UtilException IO异常
     */
    public static String unGzip(byte[] buf, String charset) {
        return StrUtil.str(unGzip(buf), charset);
    }

    /**
     * Gzip解压处理
     *
     * @param buf buf
     * @return bytes
     * @throws UtilException IO异常
     */
    public static byte[] unGzip(byte[] buf) {
        return unGzip(new ByteArrayInputStream(buf), buf.length);
    }

    /**
     * Gzip解压处理
     *
     * @param in Gzip数据
     * @return 解压后的数据
     * @throws UtilException IO异常
     */
    public static byte[] unGzip(InputStream in) throws UtilException {
        return unGzip(in, DEFAULT_BYTE_ARRAY_LENGTH);
    }

    /**
     * Gzip解压处理
     *
     * @param in Gzip数据
     * @param length 估算长度，如果无法确定请传入{@link #DEFAULT_BYTE_ARRAY_LENGTH}
     * @return 解压后的数据
     */
    public static byte[] unGzip(InputStream in, int length) {
        GZIPInputStream gzi = null;
        FastByteArrayOutputStream bos = null;
        try {
            gzi = (in instanceof GZIPInputStream) ? (GZIPInputStream) in : new GZIPInputStream(in);
            bos = new FastByteArrayOutputStream(length);
            IoUtil.copy(gzi, bos);
        } catch (IOException e) {
            log.error("unGzip error",e);
        } finally {
            IoUtil.close(gzi);
        }
        // 返回必须在关闭gos后进行，因为关闭时会自动执行finish()方法，保证数据全部写出
        return bos.toByteArray();
    }


    private static final int BUFFER_SIZE = 8192;

    public static byte[] compress(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes is null");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(bytes);
            gzip.flush();
            gzip.finish();
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip compress error", e);
        }
    }

    public static byte[] decompress(InputStream inputStream) {
        if (inputStream == null) {
            throw new NullPointerException("inputStream is null");
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GZIPInputStream gunzip = new GZIPInputStream(inputStream)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = gunzip.read(buffer)) > -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip decompress error", e);
        }
    }




}
