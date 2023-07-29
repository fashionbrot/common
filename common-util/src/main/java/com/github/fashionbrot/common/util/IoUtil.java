package com.github.fashionbrot.common.util;


import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.charset.Charset;

/**
 * @author fashionbrot
 */
public class IoUtil {

    public static void close(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
}
