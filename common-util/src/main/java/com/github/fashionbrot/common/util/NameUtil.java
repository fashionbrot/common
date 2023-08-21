package com.github.fashionbrot.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameUtil {

    /**
     * 将 首字母转大写
     * @param str 字符串
     * @return String
     */
    public static String captureName(String str) {
        if (ObjectUtil.isNotEmpty(str)) {
            // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
            char[] cs = str.toCharArray();
            cs[0] -= 32;
            return String.valueOf(cs);
        }
        return "";
    }


    private static Pattern linePattern = Pattern.compile("_(\\w)");

    /**
     * 下划线转驼峰
     * @param str String
     * @return String
     */
    public static String columnToJava(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
