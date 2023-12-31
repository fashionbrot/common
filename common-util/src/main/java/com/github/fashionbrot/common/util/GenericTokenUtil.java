package com.github.fashionbrot.common.util;

import java.util.Map;

/**
 * @author fashionbrot
 */
public class GenericTokenUtil {

    public static final String OPEN_TOKEN="${";
    public static final String CLOSE_TOKEN="}";

    public static String parse(String text,Map<String,Object> map){
        return parse(text,OPEN_TOKEN,CLOSE_TOKEN,map);
    }

    public static boolean isOpenToken(String text,String openToken){
        if (ObjectUtil.isEmpty(text)) {
            return false;
        }
        // search open token
        int start = text.indexOf(openToken);
        if (start == -1) {
            return false;
        }
        return true;
    }

    /**
     * 解析文本中的模板占位符，将占位符替换为映射中对应的值。
     *
     * @param text 待解析的文本
     * @param openToken 开始占位符标记
     * @param closeToken 结束占位符标记
     * @param map 用于替换的映射
     * @return 解析后的文本
     */
    public static String parse(String text, String openToken, String closeToken, Map<String,Object> map) {
        if (ObjectUtil.isEmpty(text)) {
            return "";
        }
        // search open token
        int start = text.indexOf(openToken);
        if (start == -1) {
            return text;
        }
        char[] src = text.toCharArray();
        int offset = 0;
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        do {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        break;
                    }
                }
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    Object o = map.get(trim(expression));

                    builder.append(o);
                    offset = end + closeToken.length();
                }
            }
            start = text.indexOf(openToken, offset);
        } while (start > -1);
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }

    private static String trim(StringBuilder sb){
        if (sb==null){
            return "";
        }
        String s = sb.toString();
        if (ObjectUtil.isNotEmpty(s)){
            return s.trim();
        }
        return "";
    }

}
