package com.github.fashionbrot.common.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


@Slf4j
public class ZipUtil {

    @Data
    public static class ZipEntity{
        private String fileName;
        private InputStream inputStream;
    }

    /**
     * 从输入流中提取符合指定后缀的文件
     * @param inputStream ZIP 文件输入流
     * @param suffixSet 需要过滤的文件后缀集合
     * @return 符合条件的 ZipEntity 列表
     */
    public static List<ZipEntity> getFileList(InputStream inputStream, Set<String> suffixSet) {
        ZipInputStream zipFile = null;
        List<ZipEntity> entityList = new ArrayList<>();
        try {
            zipFile = new ZipInputStream(inputStream, Charset.forName("GBK"));
            ZipEntry entry;
            while ((entry = zipFile.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    String fileName = getFileNameWithoutDirectory(entry.getName());
                    if (suffixSet.isEmpty() || suffixSet.stream().anyMatch(fileName::endsWith)) {
                        ZipEntity zipEntity = new ZipEntity();
                        zipEntity.setFileName(fileName);
                        zipEntity.setInputStream(zipFile);
                        entityList.add(zipEntity);
                    }
                }
            }
        } catch (Exception e) {
            log.error("getFile error", e);
        } finally {
            IoUtil.close(zipFile);
            IoUtil.close(inputStream);
        }
        return entityList;
    }

    /**
     * 从完整路径中提取文件名
     * @param fullPath 文件的完整路径
     * @return 不包含目录路径的文件名
     */
    private static String getFileNameWithoutDirectory(String fullPath) {
        int lastSeparatorIndex = fullPath.lastIndexOf(File.separator);
        int lastSlashIndex = fullPath.lastIndexOf("/");
        int lastIndex = Math.max(lastSeparatorIndex, lastSlashIndex);
        return (lastIndex > -1) ? fullPath.substring(lastIndex + 1) : fullPath;
    }


    /**
     * 压缩文件或目录到一个 ZIP 文件。
     *
     * @param sourcePath 源文件或目录路径
     * @param zipPath    输出 ZIP 文件路径
     * @throws IOException 如果发生 I/O 错误
     */
    public static void zip(String sourcePath, String zipPath) throws IOException {
        Path source = Paths.get(sourcePath);
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {
            if (Files.isDirectory(source)) {
                // 如果是目录，遍历所有文件和子目录
                Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        zos.putNextEntry(new ZipEntry(source.relativize(file).toString()));
                        Files.copy(file, zos);
                        zos.closeEntry();
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        zos.putNextEntry(new ZipEntry(source.relativize(dir).toString() + "/"));
                        zos.closeEntry();
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else {
                // 如果是文件，直接压缩
                zos.putNextEntry(new ZipEntry(source.getFileName().toString()));
                Files.copy(source, zos);
                zos.closeEntry();
            }
        }
    }

    /**
     * 解压 ZIP 文件到指定目录。
     *
     * @param zipPath  ZIP 文件路径
     * @param destPath 输出目录路径
     * @throws IOException 如果发生 I/O 错误
     */
    public static void unzip(String zipPath, String destPath) throws IOException {
        Path destDir = Paths.get(destPath);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                Path filePath = destDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectories(filePath);
                } else {
                    Files.createDirectories(filePath.getParent());
                    try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(filePath))) {
                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = zis.read(buffer)) != -1) {
                            bos.write(buffer, 0, read);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }



}
