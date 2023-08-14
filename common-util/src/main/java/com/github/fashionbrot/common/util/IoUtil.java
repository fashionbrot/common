package com.github.fashionbrot.common.util;


import com.github.fashionbrot.common.consts.CharsetConst;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * @author fashionbrot
 */
public class IoUtil {

    /** 默认缓存大小 */
    public static final int DEFAULT_BUFFER_SIZE = 2048;
    /** 默认中等缓存大小 */
    public static final int DEFAULT_MIDDLE_BUFFER_SIZE = 4096;
    /** 默认大缓存大小 */
    public static final int DEFAULT_LARGE_BUFFER_SIZE = 8192;

    /** 数据流末尾 */
    public static final int EOF = -1;

    public static void close(Reader input) {
        close((Closeable)input);
    }

    public static void close(Writer output) {
        close((Closeable)output);
    }

    public static void close(InputStream input) {
        close((Closeable)input);
    }
    public static void close(OutputStream output) {
        close((Closeable)output);
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    public static byte[] toByteAndClose(InputStream input){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            copy(input, output);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            close(input);
        }
        return output.toByteArray();
    }

    public static byte[] toByteArray(InputStream input){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            copy(input, output);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return output.toByteArray();
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }
    public static long copyLarge(InputStream input, OutputStream output) throws IOException {
        return copyLarge(input, output, new byte[DEFAULT_MIDDLE_BUFFER_SIZE]);
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
            throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }



    public static void close(final URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }

    /**
     * close Closeable
     * @param closeables the closeables
     */
    public static void close(AutoCloseable... closeables) {
        if (ObjectUtil.isNotEmpty(closeables)) {
            for (AutoCloseable closeable : closeables) {
                close(closeable);
            }
        }
    }

    /**
     * close Closeable
     * @param closeable the closeable
     */
    public static void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignore) {
            }
        }
    }


    public static void write(OutputStream outputStream,byte[] data){
        try {
            if (ObjectUtil.isNotEmpty(data)){
                outputStream.write(data);
                outputStream.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (outputStream!=null){
                close(outputStream);
            }
        }
    }

    public static  String toString(InputStream inputStream, Charset charset) {
        byte[] bytes = toByteArray(inputStream);
        return toString(bytes,charset);
    }

    public static String toString(byte[] bytes,Charset charset){
        if (ObjectUtil.isNotEmpty(bytes)){
            return new String(bytes,charset!=null?charset: CharsetConst.DEFAULT_CHARSET);
        }
        return ObjectUtil.EMPTY;
    }
}
