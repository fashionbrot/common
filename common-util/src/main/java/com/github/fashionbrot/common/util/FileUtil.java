package com.github.fashionbrot.common.util;

import com.github.fashionbrot.common.consts.CharsetConst;
import com.github.fashionbrot.common.system.OsUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * @author fashionbrot
 */
@Slf4j
public class FileUtil {


    /**
     * 获取 user.home 路径
     * @return String
     */
    public static String getUserHome(){
        return OsUtil.getUserHome();
    }

    /** 最大数组 */
    public static final int MAX_BUFFER_SIZE = Integer.MAX_VALUE - 8;


    private static final int LOCK_COUNT = 10;

    /**
     * 按照关键字搜索 文件夹
     * @param folder   file文件夹
     * @param keyword  fileName 关键字
     * @return List
     */
    public static List<File> searchFiles(File folder, final String keyword) {
        List<File> result = new ArrayList<File>();
        if (folder.isFile()) {
            result.add(folder);
        }
        File[] subFolders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getName().contains(keyword)) {
                    return true;
                }
                return false;
            }
        });
        if (subFolders != null) {
            for (File file : subFolders) {
                if (file.isFile()) {
                    // 如果是文件则将文件添加到结果列表中
                    result.add(file);
                }
            }
        }
        return result;
    }

    /**
     * 获取 file 文件内容
     * @param file  file
     * @return String
     */
    public static String getFileContent(File file) {
        return getFileContent(file, CharsetConst.DEFAULT_CHARSET);
    }

    /**
     * 读取文件的内容并返回字符串。
     *
     * @param file    要读取的文件
     * @param charset 使用的字符集
     * @return 文件内容的字符串表示，如果文件为空或读取失败，则返回空字符串
     */
    public static String getFileContent(File file, Charset charset) {
        byte[] bytes = fileToByte(file);
        if (ObjectUtil.isNotEmpty(bytes)) {
            return IoUtil.toString(bytes, charset);
        }
        return "";
    }


    /**
     * File 转  byte[]
     * @param file File
     * @return byte[]
     */
    public static byte[] fileToByte(File file) {
        RandomAccessFile randomAccessFile = null;
        FileLock fileLock = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            FileChannel fileChannel = randomAccessFile.getChannel();
            do {
                try {
                    fileLock = fileChannel.tryLock(0L, Long.MAX_VALUE, true);
                } catch (Exception e) {
                    log.error("getFileContent error", e);
                }
            } while (null == fileLock);
            long length = file.length();
            if (length>MAX_BUFFER_SIZE){
                throw new OutOfMemoryError("Required array size too large");
            }
            byte[] buf = new byte[(int)file.length()];
            randomAccessFile.read(buf);
            return buf;
        } catch (Exception e) {
            log.error("fileToByte error", e);
        } finally {
            IoUtil.close(fileLock);
            IoUtil.close(randomAccessFile);
        }
        return null;
    }

    /**
     * 删除文件
     * @param file File
     */
    public static void deleteFile(File file){
        try {
            file.delete();
        }catch (Exception e){
            log.error("deleteFile error ",e);
        }
    }


    /**
     * 将内容写入 File
     * @param file      file
     * @param content   文本内容
     */
    public static void writeFile(File file, String content) {
        writeFile(file,content,Charset.defaultCharset());
    }

    /**
     * 将内容写入文件。
     *
     * @param file     要写入的文件
     * @param content  要写入的文本内容，可以为null
     * @param charset  使用的字符集
     */
    public static void writeFile(File file, String content,Charset charset) {

        createFile(file);

        FileChannel fileChannel = null;
        FileLock fileLock = null;
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            fileChannel = randomAccessFile.getChannel();
            int i = 0;
            do {
                try {
                    fileLock = fileChannel.tryLock();
                } catch (Exception e) {
                    ++i;
                    if (i > LOCK_COUNT) {
                        log.error("writeFile  get lock count error filePath:{}", file.getAbsolutePath(), e);
                    }
                }
            } while (null == fileLock);
            if (ObjectUtil.isEmpty(content)){
                content = "";
            }
            randomAccessFile.write(content.getBytes(charset));
        } catch (Exception e) {
            log.error("writeFile error", e);
        } finally {
            IoUtil.close(fileLock);
            IoUtil.close(randomAccessFile);
            IoUtil.close(fileChannel);
        }
    }


    public static boolean createFile(File file){
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 创建多级目录
     * @param path path
     * @return boolean
     */
    public static boolean mkdirs(String path){
        return mkdirs(new File(path));
    }

    /**
     * 创建多级目录
     * @param file File
     * @return boolean
     */
    public static boolean mkdirs(File file){
        if (file==null ){
            return false;
        }
        if(!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }


    /**
     * 创建指定的目录。
     * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
     * <b>注意：可能会在返回false的时候创建部分父目录。</b>
     *
     * @param file 要创建的目录
     * @return 完全创建成功时返回true，否则返回false。
     */
    public static boolean makeDirectory(File file) {
        File parent = file.getParentFile();
        if (parent != null) {
            return parent.mkdirs();
        }
        return false;
    }

    /**
     * 创建指定的目录。
     * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
     * <b>注意：可能会在返回false的时候创建部分父目录。</b>
     *
     * @param fileName 要创建的目录的目录名
     * @return 完全创建成功时返回true，否则返回false。
     */
    public static boolean makeDirectory(String fileName) {
        File file = new File(fileName);
        return makeDirectory(file);
    }


    /**
     * 删除指定目录及其中的所有内容。
     *
     * @param dirName 要删除的目录的目录名
     * @return 删除成功时返回true，否则返回false。
     */
    public static boolean deleteDirectory(String dirName) {
        return deleteDirectory(new File(dirName));
    }

    /**
     * 删除指定目录及其中的所有内容。
     *
     * @param dir 要删除的目录
     * @return 删除成功时返回true，否则返回false。
     */
    public static boolean deleteDirectory(File dir) {
        if ((dir == null) || !dir.isDirectory()) {
            throw new IllegalArgumentException("Argument " + dir +
                    " is not a directory. ");
        }

        File[] entries = dir.listFiles();
        int sz = entries.length;

        for (int i = 0; i < sz; i++) {
            if (entries[i].isDirectory()) {
                if (!deleteDirectory(entries[i])) {
                    return false;
                }
            } else {
                if (!entries[i].delete()) {
                    return false;
                }
            }
        }

        if (!dir.delete()) {
            return false;
        }
        return true;
    }


    /**
     * 从文件名中去掉文件类型部分。
     *
     * @param fileName 文件名，可以为null
     * @return 去掉类型部分的文件名，如果文件名为null，则返回空字符串
     */
    public static String getFilePrefix(String fileName) {
        if (ObjectUtil.isEmpty(fileName)) {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(0, index);
        } else {
            return fileName;
        }
    }

    /**
     * 获取文件名的后缀部分。
     *
     * @param fileName 文件名，可以为null
     * @return 文件后缀，如果文件名为null或者没有后缀，则返回空字符串
     */
    public static String getFileSuffix(String fileName) {
        if (ObjectUtil.isEmpty(fileName)) {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1 && index < fileName.length() - 1) {
            return fileName.substring(index + 1);
        } else {
            return "";
        }
    }



    /**
     * 根据 path 获取文件list
     * @param path      path
     * @param fileList  fileList
     * @param suffix    关键字或后缀
     */
    public static void findPath(String path, List<File> fileList, String suffix) {
        File f = new File(path);
        if (f != null) {
            File[] files = f.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    findPath(file.getPath(), fileList, suffix);
                } else {
                    if (file.getName().endsWith(suffix)) {
                        fileList.add(file);
                    }
                }
            }
        }
    }





    /**
     * Returns the path to the system temporary directory.
     *
     * @return the path to the system temporary directory.
     *
     * @since 2.0
     */
    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * Returns a {@link File} representing the system temporary directory.
     *
     * @return the system temporary directory.
     *
     * @since 2.0
     */
    public static File getTempDirectory() {
        return new File(getTempDirectoryPath());
    }

    /**
     * Returns the path to the user's home directory.
     *
     * @return the path to the user's home directory.
     *
     * @since 2.0
     */
    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    /**
     * Returns a {@link File} representing the user's home directory.
     *
     * @return the user's home directory.
     *
     * @since 2.0
     */
    public static File getUserDirectory() {
        return new File(getUserDirectoryPath());
    }

    /**
     * 可读的文件大小<br>
     * 参考 http://stackoverflow.com/questions/3263892/format-file-size-as-mb-gb-etc
     *
     * @param size Long类型大小
     * @return 大小
     */
    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB", "EB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


}
