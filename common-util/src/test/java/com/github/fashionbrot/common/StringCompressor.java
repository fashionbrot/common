package com.github.fashionbrot.common;

import com.github.fashionbrot.common.compress.GzipUtil;
import com.github.fashionbrot.common.util.LvBufferTypeUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.*;

public class StringCompressor {

    public static byte[] compressString(String input) throws IOException {
        // 创建一个字节数组输出流，用于存储压缩后的数据
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // 创建一个Deflater对象，用于压缩数据
        Deflater deflater = new Deflater();

        // 创建一个DeflaterOutputStream对象，将其与字节数组输出流关联
        DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater);

        try {
            // 将输入的字符串转换为字节数组，并将其写入到DeflaterOutputStream中
            byte[] inputBytes = input.getBytes("UTF-8"); // 使用指定的字符集将字符串转换为字节数组
            deflaterOutputStream.write(inputBytes);
        } finally {
            // 关闭DeflaterOutputStream
            deflaterOutputStream.close();
        }

        // 返回压缩后的字节数组
        return byteArrayOutputStream.toByteArray();
    }

    public static String decompressByteArray(byte[] compressedData) throws IOException {
        // 创建一个字节数组输入流，用于读取压缩后的数据
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);

        // 创建一个Inflater对象，用于解压缩数据
        Inflater inflater = new Inflater();

        // 创建一个InflaterInputStream对象，将其与字节数组输入流关联
        InflaterInputStream inflaterInputStream = new InflaterInputStream(byteArrayInputStream, inflater);

        // 创建一个字节数组输出流，用于存储解压缩后的数据
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // 读取解压缩后的数据，并写入到字节数组输出流中
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inflaterInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } finally {
            // 关闭InflaterInputStream
            inflaterInputStream.close();
        }

        // 使用指定的字符集将字节数组转换为字符串
        return byteArrayOutputStream.toString("UTF-8");
    }

    public static void main(String[] args) throws IOException, DataFormatException {
        String input ="这两个方法都实现了字符串的压缩，但是采用了不同的压缩算法和实现方式。\n";
        System.out.println(input.getBytes().length);
        byte[] compressedData = compressString(input);
        System.out.println("Compressed data length: " + compressedData.length);

        String s = decompressByteArray(compressedData);
        System.out.println(s);


        byte[] compress = GzipUtil.compress(input);
        System.out.println(new String(compress, StandardCharsets.UTF_8));
        System.out.println(compress.length);

        System.out.println(GzipUtil.decompress(compress));

    }
}
