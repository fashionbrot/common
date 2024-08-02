package com.github.fashionbrot.common.util;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipWriterImpl extends AbstractZipWriter {

    public ZipWriterImpl() {
        super();
    }

    public ZipWriterImpl(ZipOutputStream zos) {
        super(zos);
    }

    @Override
    public void addEntry(String fileName, byte[] fileData) throws IOException {
        ZipEntry entry = new ZipEntry(fileName);
        zos.putNextEntry(entry);
        zos.write(fileData);
        zos.closeEntry();
    }
}
