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

    public static byte[] toByte(File file){
        return toByteAndClose(toInputStream(file));
    }

    public static byte[] toByte(InputStream input){
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


    public static int copy(InputStream in, OutputStream out) throws IOException {
        try {
            return copyBuffer(in, out);
        }
        finally {
            close(in);
            close(out);
        }
    }



    /**
     * Copy the contents of the given InputStream to the given OutputStream.
     * <p>Leaves both streams open when done.
     * @param in the InputStream to copy from
     * @param out the OutputStream to copy to
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copyBuffer(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[DEFAULT_MIDDLE_BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }

    /**
     * InputStream 转 String
     * @param inputStream InputStream
     * @param charset     charset
     * @return String
     */
    public static  String toString(InputStream inputStream, Charset charset) {
        byte[] bytes = toByte(inputStream);
        return toString(bytes,charset);
    }

    /**
     * InputStream 转 String
     * @param inputStream InputStream
     * @return String
     */
    public static String toString(InputStream inputStream){
        return toString(inputStream,CharsetConst.DEFAULT_CHARSET);
    }

    /**
     * File 转 String
     * @param file      File
     * @param charset   charset
     * @return String
     */
    public static String toString(File file,Charset charset){
        return toString(toInputStream(file),charset);
    }

    /**
     * File 转 String
     * @param file      File
     * @return String
     */
    public static String toString(File file){
        return toString(toInputStream(file));
    }


    /**
     * File 转  FileInputStream
     * @param file File
     * @return FileInputStream
     */
    public static FileInputStream toInputStream(File file){
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * byte[] 转 String
     * @param bytes    byte[]
     * @param charset  charset
     * @return String
     */
    public static String toString(byte[] bytes,Charset charset){
        if (ObjectUtil.isEmpty(bytes)){
            return ObjectUtil.EMPTY;
        }
        if (charset==null){
            return new String(bytes);
        }
        return new String(bytes,charset);
    }

    /**
     * byte[]  转 String
     * @param bytes byte[]
     * @return String
     */
    public static String toString(byte[] bytes){
        if (ObjectUtil.isEmpty(bytes)){
            return ObjectUtil.EMPTY;
        }
        return toString(bytes,CharsetConst.DEFAULT_CHARSET);
    }


}
