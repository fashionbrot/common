package com.github.fashionbrot.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * @author fashionbrot
 */
@Slf4j
public class FileUtil {

    private static final String USER_HOME;

    static {
        USER_HOME = System.getProperty("user.home");
    }

    public static String getUserHome(){
        return USER_HOME;
    }


    private static final int LOCK_COUNT = 10;

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
     * get file content
     *
     * @param file file
     * @return String
     */
    public static String getFileContent(File file) {
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


            byte[] buf = new byte[1024];
            StringBuffer sb = new StringBuffer();
            while ((randomAccessFile.read(buf)) != -1) {
                sb.append(new String(buf, "UTF-8"));
                buf = new byte[1024];
            }

            return sb.toString();
        } catch (Exception e) {
            log.error("getFileContent error", e);
        } finally {
            close(fileLock, randomAccessFile, null);
        }
        return "";
    }

    public static void deleteFile(File file){
        try {
            file.delete();
        }catch (Exception e){
            log.error("deleteFile error ",e);
        }
    }

    public static void writeFile(File file, String content) {
        try {
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            log.error("writeFile error", e);
            return;
        }


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

            randomAccessFile.write(content.getBytes("UTF-8"));
        } catch (Exception e) {
            log.error("writeFile error", e);
        } finally {
            close(fileLock, randomAccessFile, fileChannel);
        }
    }


    private static void close(FileLock fileLock, RandomAccessFile randomAccessFile, FileChannel fileChannel) {
        if (fileLock != null) {
            try {
                fileLock.release();
                fileLock = null;
            } catch (IOException e) {
                log.error("fileLock release error");
            }
        }
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
                randomAccessFile = null;
            } catch (IOException e) {
                log.error("randomAccessFile close error");
            }
        }
        if (fileChannel != null) {
            try {
                fileChannel.close();
                fileChannel = null;
            } catch (IOException e) {
                log.error("fileChannel close error");
            }
        }
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
     * 将文件名中的类型部分去掉。
     *
     * @param fileName 文件名
     * @return 去掉类型部分的结果
     */
    public static String getFilePrefix(String fileName) {
        if (ObjectUtil.isEmpty(fileName)){
            return ObjectUtil.EMPTY;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(0, index);
        } else {
            return fileName;
        }
    }

    /**
     * 获取文件后缀
     * @param fileName 文件名
     * @return 文件后缀
     */
    public static String getFileSuffix(String fileName){
        if (ObjectUtil.isEmpty(fileName)){
            return ObjectUtil.EMPTY;
        }
        int index = fileName.lastIndexOf(".");
        if (index!=-1){
            return fileName.substring(index+1);
        }else{
            return ObjectUtil.EMPTY;
        }
    }


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


    public static Properties fileToProperties(File file) {
        if (file==null){
            return null;
        }
        InputStream in = null;
        Properties properties = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            properties.load(in);
        } catch (Exception e) {
            log.error("putProperties error ", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
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
