package com.github.fashionbrot.common.util;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

/**
 * @author fashionbrot
 */
public class PathUtil {

    public static final String REQUEST_SEPARATOR="/";

    /**
     * 格式化路径的工具方法
     *
     * @param paths 要连接的路径部分
     * @return 格式化后的路径
     */
    public static String formatPath(String... paths) {
        if (ObjectUtil.isEmpty(paths)){
            return "";
        }
        StringBuilder formattedPath = new StringBuilder();
        for (String path : paths) {
            if (ObjectUtil.isNotEmpty(path)) {
                if (!path.startsWith(REQUEST_SEPARATOR)) {
                    formattedPath.append(REQUEST_SEPARATOR);
                }
                formattedPath.append(path);
            }
        }
        return formattedPath.toString();
    }


    /**
     * 格式化URL，去除末尾的分隔符
     *
     * @param url 原始URL
     * @return 格式化后的URL
     */
    public static String formatUrl(String url) {
        // 检查URL是否不为空
        if (ObjectUtil.isNotEmpty(url)) {
            // 去除末尾的分隔符（如果有）
            return url.replaceAll(REQUEST_SEPARATOR + "+$", "");
        }
        return ""; // 如果URL为空，返回空字符串
    }

    public static boolean isPatternPrefixValid(String pattern) {
        if (ObjectUtil.isEmpty(pattern)){
            return false;
        }
        return pattern.startsWith("glob:") || pattern.startsWith("regex:");
    }

    public static String constructPatternFromPackages(String[] packages) {
        StringBuilder sb = new StringBuilder();
        for (String aPackage : packages) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(aPackage.endsWith("*") ? aPackage : aPackage + ".*");
        }
        return sb.toString();
    }

    /**
     * 根据语法和模式创建并返回PathMatcher对象。
     * 默认情况下，使用glob模式匹配任意路径。
     *
     * @param syntaxAndPattern 语法和模式字符串
     * @return PathMatcher对象
     */
    public static PathMatcher getPathMatcher(String syntaxAndPattern) {
        // 默认情况下，使用glob模式匹配任意路径
        if (ObjectUtil.isEmpty(syntaxAndPattern)) {
            syntaxAndPattern = "glob:{**}";
        } else {
            String[] packages = syntaxAndPattern.split(",");
            if (ObjectUtil.isNotEmpty(packages)) {
                syntaxAndPattern = constructPatternFromPackages(packages);
            }
            if (!isPatternPrefixValid(syntaxAndPattern)) {
                syntaxAndPattern = "glob:{" + syntaxAndPattern + "}";
            }
        }
        // 创建并返回PathMatcher对象
        return FileSystems.getDefault().getPathMatcher(syntaxAndPattern);
    }



    /**
     * 检查给定路径是否与 PathMatcher 匹配
     *
     * @param matcher PathMatcher 对象，用于定义匹配规则
     * @param path    要检查的路径字符串
     * @return 如果路径匹配则返回 true，否则返回 false
     */
    public static boolean matches(PathMatcher matcher, String path) {
        // 将路径字符串转换为 Path 对象
        Path filePath = Paths.get(path);

        // 使用 PathMatcher 对路径进行匹配
        return matcher.matches(filePath);
    }

    /**
     * 检查给定路径是否不与 PathMatcher 匹配
     *
     * @param matcher PathMatcher 对象，用于定义匹配规则
     * @param path    要检查的路径字符串
     * @return 如果路径不匹配则返回 true，否则返回 false
     */
    public static boolean notMatches(PathMatcher matcher, String path) {
        return !matches(matcher,path);
    }



}
