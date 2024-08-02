package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.ZipUtil;
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
    public void testGetFileList() {
        // 创建一个内存中的 ZIP 文件
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            addFileToZip(zos, "test1.txt", "This is a test file 1.");
            addFileToZip(zos, "test2.md", "This is a test file 2.");
            addFileToZip(zos, "test3.jpg", "This is a test file 3.");
        } catch (IOException e) {
            fail("Failed to create ZIP file in memory.");
        }

        InputStream zipInputStream = new ByteArrayInputStream(baos.toByteArray());
        List<ZipUtil.ZipEntity> fileList = ZipUtil.getFileList(zipInputStream, suffixSet);

        assertEquals("The number of files should be 2.", 2, fileList.size());
        assertTrue("test1.txt should be in the list.", fileList.stream().anyMatch(e -> e.getFileName().equals("test1.txt")));
        assertTrue("test2.md should be in the list.", fileList.stream().anyMatch(e -> e.getFileName().equals("test2.md")));
    }

    private void addFileToZip(ZipOutputStream zos, String fileName, String content) throws IOException {
        ZipEntry entry = new ZipEntry(fileName);
        zos.putNextEntry(entry);
        zos.write(content.getBytes());
        zos.closeEntry();
    }
}
