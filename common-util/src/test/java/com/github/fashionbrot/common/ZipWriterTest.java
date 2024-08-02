package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.IoUtil;
import com.github.fashionbrot.common.util.ZipWriterImpl;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.*;

public class ZipWriterTest {

    @Test
    public void testZipWriter() {
        ZipWriterImpl zipWriter = new ZipWriterImpl();

        try {
            zipWriter.addEntry("test1.txt", "This is a test file 1.".getBytes());
            zipWriter.addEntry("test2.txt", "This is a test file 2.".getBytes());

            byte[] zipData = zipWriter.getZipBytes();
            assertNotNull(zipData);
            assertTrue(zipData.length > 0);

            OutputStream outputStream = zipWriter.getOutputStream();
            assertNotNull(outputStream);
            assertTrue(((ByteArrayOutputStream) outputStream).toByteArray().length > 0);

            IoUtil.close(zipWriter);
        } catch (IOException e) {
            fail("IOException should not occur.");
        }
    }

}
