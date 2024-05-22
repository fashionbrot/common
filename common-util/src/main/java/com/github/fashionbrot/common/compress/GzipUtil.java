package com.github.fashionbrot.common.compress;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class GzipUtil {

    public static byte[] compress(String data) {
        Deflater deflater = new Deflater();
        byte[] input = data.getBytes();
        deflater.setInput(input);
        deflater.finish();

        byte[] output = new byte[1024];
        int compressedDataLength = deflater.deflate(output);
        deflater.end();

        byte[] compressedData = new byte[compressedDataLength];
        System.arraycopy(output, 0, compressedData, 0, compressedDataLength);
        return compressedData;
    }

    public static String decompress(byte[] compressedData) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);

        byte[] output = new byte[1024];
        int decompressedDataLength = inflater.inflate(output);
        inflater.end();

        return new String(output, 0, decompressedDataLength);
    }

}
