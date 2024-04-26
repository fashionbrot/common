package com.github.fashionbrot.common.compress;

import com.github.fashionbrot.common.util.ObjectUtil;
import org.junit.Test;

import java.util.zip.DataFormatException;

public class GzipTest {

    @Test
    public void test() throws DataFormatException {
        String str = "[{\"user\":\"张三\"},{\"user\":\"张三\"},{\"user\":\"张三\"}]";
        System.out.println(str.getBytes().length);
        byte[] compress = GzipUtil.compress(str);
        System.out.println(compress.length);
        System.out.println("compress:"+ ObjectUtil.byteToString(compress));

        String decompress = GzipUtil.decompress(compress);
        System.out.println(decompress);
    }

}
