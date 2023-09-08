package com.github.fashionbrot.common.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 推荐使用
 * @see GenericTokenUtil
 */
@Deprecated
public class MatcherUtil {

    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");
    public static final String PLACEHOLDER_PATTERN_PREFIX = "${";

    /**
     * 根据提供的映射替换字符串中的占位符。
     *
     * @param valueMap       包含占位符及其替代值的映射。
     * @param inputString    要进行占位符替换的输入字符串。
     * @return 替换占位符后的结果字符串。
     */
    public static String replacePlaceholders(Map<String,Object> valueMap, String inputString){
        if (ObjectUtil.isEmpty(inputString)){
            return "";
        }
        if (inputString.indexOf(PLACEHOLDER_PATTERN_PREFIX)==-1){
            return inputString;
        }
        if (ObjectUtil.isEmpty(valueMap)){
            return inputString;
        }
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(inputString);
        StringBuffer resolvedValue = new StringBuffer();

        while (matcher.find()) {
            String placeholder = ObjectUtil.trim(matcher.group(1));
            if (ObjectUtil.isEmpty(placeholder)){
                continue;
            }
            if (valueMap.containsKey(placeholder)){
                Object objectValue = valueMap.get(placeholder);
                if (objectValue==null){
                    continue;
                }
                String replacement = ObjectUtil.formatString(objectValue);
                matcher.appendReplacement(resolvedValue, Matcher.quoteReplacement(replacement));
            }
        }
        matcher.appendTail(resolvedValue);
        return resolvedValue.toString();
    }




}
