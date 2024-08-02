package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.ZipUtil;
import com.github.fashionbrot.common.util.ZipWriterImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

public class ZipUtilTest {

    private Set<String> suffixSet;

    @Before
    public void setUp() {
        // 初始化需要过滤的文件后缀集合
        suffixSet = new HashSet<>(Arrays.asList(".txt", ".md"));
    }

    @Test
    public void testGetFileList() throws IOException {
        ZipWriterImpl zipWriter=new ZipWriterImpl();

        zipWriter.addEntry( "test1.txt", "This is a test file 1.".getBytes());
        zipWriter.addEntry(  "test2.md", "This is a test file 2.".getBytes());
        zipWriter.addEntry( "test3.jpg", "This is a test file 3.".getBytes());

        byte[] zipBytes = zipWriter.getZipBytes();

        InputStream zipInputStream = new ByteArrayInputStream(zipBytes);
        List<ZipUtil.ZipEntity> fileList = ZipUtil.getFileList(zipInputStream, suffixSet);

        assertEquals("The number of files should be 2.", 2, fileList.size());
        assertTrue("test1.txt should be in the list.", fileList.stream().anyMatch(e -> e.getFileName().equals("test1.txt")));
        assertTrue("test2.md should be in the list.", fileList.stream().anyMatch(e -> e.getFileName().equals("test2.md")));

        zipWriter.close();
    }


}
