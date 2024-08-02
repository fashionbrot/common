package com.github.fashionbrot.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

public abstract class AbstractZipWriter implements AutoCloseable{

    protected ZipOutputStream zos;
    protected ByteArrayOutputStream baos;

    public AbstractZipWriter() {
        this.baos = new ByteArrayOutputStream();
        this.zos = new ZipOutputStream(baos);
    }

    public AbstractZipWriter(ZipOutputStream zos) {
        this.zos = zos;
    }

    /**
     * 添加文件条目到 ZIP 输出流。
     *
     * @param fileName 文件名
     * @param fileData 文件数据
     * @throws IOException 如果发生 I/O 错误
     */
    public abstract void addEntry(String fileName, byte[] fileData) throws IOException;

    /**
     * 返回 ZIP 输出流。
     *
     * @return ZIP 输出流
     * @throws IOException 如果发生 I/O 错误
     */
    public ZipOutputStream getZipOutputStream() throws IOException {
        if (zos != null) {
            zos.finish();
        }
        return zos;
    }

    /**
     * 返回 ByteArrayOutputStream 中的字节数组。
     *
     * @return ZIP 文件的字节数组
     * @throws IOException 如果发生 I/O 错误
     */
    public byte[] getZipBytes() throws IOException {
        if (baos != null) {
            zos.finish();
            return baos.toByteArray();
        }
        return new byte[0];
    }

    /**
     * 返回 OutputStream
     *
     * @return OutputStream
     * @throws IOException 如果发生 I/O 错误
     */
    public OutputStream getOutputStream() throws IOException {
        if (baos != null) {
            zos.finish();
            return baos;
        }
        throw new IOException("No ByteArrayOutputStream available.");
    }


    /**
     * 关闭 ZIP 输出流及其内部的 ByteArrayOutputStream（如果存在）。
     *
     * @throws IOException 如果发生 I/O 错误
     */
    @Override
    public void close() throws IOException {
        if (zos != null) {
            zos.close();
        }
        if (baos != null) {
            baos.close();
        }
    }
}

