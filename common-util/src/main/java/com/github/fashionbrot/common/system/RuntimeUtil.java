package com.github.fashionbrot.common.system;


import com.github.fashionbrot.common.util.FileUtil;

/**
 * @author fashionbrot
 */
public class RuntimeUtil {


    private Runtime currentRuntime = Runtime.getRuntime();

    /**
     * 获得运行时对象
     * @return {@link Runtime}
     */
    public final Runtime getRuntime(){
        return currentRuntime;
    }

    /**
     * 获得JVM最大可用内存
     * @return 最大可用内存
     */
    public final long getMaxMemory(){
        return currentRuntime.maxMemory();
    }

    /**
     * 获得JVM已分配内存
     * @return 已分配内存
     */
    public final long getTotalMemory(){
        return currentRuntime.totalMemory();
    }

    /**
     * 获得JVM已分配内存中的剩余空间
     * @return 已分配内存中的剩余空间
     */
    public final long getFreeMemory(){
        return currentRuntime.freeMemory();
    }

    /**
     * 获得JVM最大可用内存
     * @return 最大可用内存
     */
    public final long getUsableMemory(){
        return currentRuntime.maxMemory() - currentRuntime.totalMemory() + currentRuntime.freeMemory();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Max Memory:    ").append(FileUtil.readableFileSize(getMaxMemory())).append("\n");
        builder.append( "Max Memory:    ").append( FileUtil.readableFileSize(getMaxMemory())).append("\n");
        builder.append( "Total Memory:     ").append( FileUtil.readableFileSize(getTotalMemory())).append("\n");
        builder.append( "Free Memory:     ").append( FileUtil.readableFileSize(getFreeMemory())).append("\n");
        builder.append( "Usable Memory:     ").append( FileUtil.readableFileSize(getUsableMemory())).append("\n");
        return builder.toString();
    }

}
