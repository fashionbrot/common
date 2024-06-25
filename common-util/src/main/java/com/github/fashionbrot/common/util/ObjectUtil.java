package com.github.fashionbrot.common.util;


import com.github.fashionbrot.common.consts.CharsetConst;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;

/**
 * @author fashi
 */
@Slf4j
public class ObjectUtil {

    /**
     * 比较两个对象是否相等。
     *
     * @param a 第一个对象
     * @param b 第二个对象
     * @return 如果两个对象相等，则返回true；否则返回false。
     */
    public static boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    /**
     * 比较两个对象是否不相等。
     *
     * @param a 第一个对象
     * @param b 第二个对象
     * @return 如果两个对象不相等，则返回true；否则返回false。
     */
    public static boolean notEquals(Object a, Object b) {
        return !equals(a, b);
    }

    /**
     * 去除字符串两端的空白字符。
     *
     * @param str 待处理的字符串
     * @return 去除空白字符后的字符串，如果输入为null，则返回空字符串。
     */
    public static String trim(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * 判断字符串是否为空或仅包含空白字符。
     *
     * @param str 待判断的字符串
     * @return 如果字符串为空或仅包含空白字符，则返回true；否则返回false。
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为空且不仅包含空白字符。
     *
     * @param str 待判断的字符串
     * @return 如果字符串不为空且不仅包含空白字符，则返回true；否则返回false。
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }


    /**
     * 判断字符序列是否为空。
     *
     * @param cs 待判断的字符序列
     * @return 如果字符序列为空，则返回true；否则返回false。
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 判断字符序列是否不为空。
     *
     * @param cs 待判断的字符序列
     * @return 如果字符序列不为空，则返回true；否则返回false。
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }



    /**
     * 判断集合是否为空。
     *
     * @param collection 待判断的集合
     * @return 如果集合为空，则返回true；否则返回false。
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断集合是否不为空。
     *
     * @param collection 待判断的集合
     * @return 如果集合不为空，则返回true；否则返回false。
     */
    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断Map是否为空。
     *
     * @param map 待判断的Map
     * @return 如果Map为空，则返回true；否则返回false。
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断Map是否不为空。
     *
     * @param map 待判断的Map
     * @return 如果Map不为空，则返回true；否则返回false。
     */
    public static boolean isNotEmpty(final Map<?, ?> map) {
        return !isEmpty(map);
    }


    /**
     * 判断数组是否为空。
     *
     * @param objects 待判断的数组
     * @return 如果数组为空，则返回true；否则返回false。
     */
    public static boolean isEmpty(final Object[] objects) {
        return objects == null || objects.length == 0;
    }

    /**
     * 判断数组是否不为空。
     *
     * @param objects 待判断的数组
     * @return 如果数组不为空，则返回true；否则返回false。
     */
    public static boolean isNotEmpty(final Object[] objects) {
        return !isEmpty(objects);
    }


    /**
     * 判断字节数组是否为空。
     *
     * @param bytes 待判断的字节数组
     * @return 如果字节数组为空，则返回true；否则返回false。
     */
    public static boolean isEmpty(final byte[] bytes) {
        return bytes == null || bytes.length == 0;
    }

    /**
     * 判断给定的字符串是否代表真值。
     *
     * @param str 要检查的字符串。
     * @return 如果字符串不为空且代表真值，则返回 {@code true}，否则返回 {@code false}。
     */
    public static boolean isTrue(final String str){
        if (ObjectUtil.isEmpty(str)){
            return false;
        }
        return parseBoolean(str);
    }

    /**
     * 判断给定的字符串是否代表假值。
     *
     * @param str 要检查的字符串。
     * @return 如果字符串为空或代表假值，则返回 {@code true}，否则返回 {@code false}。
     */
    public static boolean isFalse(final String str){
        return !isTrue(str);
    }

    /**
     * 验证 Boolean 对象的值。
     *
     * @param b 要验证的 Boolean 对象，可以为null
     * @return 如果输入的 Boolean 对象不为null且为true，则返回true；否则返回false
     */
    public static boolean isBoolean(Boolean b) {
        return b != null && b;
    }

    /**
     * 验证 Boolean 是否为 false。
     *
     * @param bool 要验证的 Boolean 对象，可以为 null
     * @return 如果输入的 Boolean 对象为 null 或者为 false，则返回 true；否则返回 false
     */
    public static boolean isFalse(Boolean bool) {
        return bool == null || !bool;
    }


    /**
     * 验证 Boolean 是否为 true。
     *
     * @param bool 要验证的 Boolean 对象，可以为 null
     * @return 如果输入的 Boolean 对象不为 null 且为 true，则返回 true；否则返回 false
     */
    public static boolean isTrue(Boolean bool) {
        return isBoolean(bool);
    }


    /**
     * 将字符串转换为大写形式，防止空指针。
     *
     * @param str 要转换的字符串，可以为null
     * @return 转换为大写的字符串，如果输入为null，则返回空字符串
     */
    public static String toUpperCase(String str) {
        if (ObjectUtil.isEmpty(str)) {
            return "";
        }
        return str.toUpperCase();
    }

    /**
     * 将字符串转换为小写形式，防止空指针。
     *
     * @param str 要转换的字符串，可以为null
     * @return 转换为小写的字符串，如果输入为null，则返回空字符串
     */
    public static String toLowerCase(String str) {
        if (ObjectUtil.isEmpty(str)) {
            return "";
        }
        return str.toLowerCase();
    }

    /**
     * 判断字节数组是否不为空。
     *
     * @param bytes 待判断的字节数组
     * @return 如果字节数组不为空，则返回true；否则返回false。
     */
    public static boolean isNotEmpty(final byte[] bytes) {
        return !isEmpty(bytes);
    }



    /**
     * 判断一个字符串是否仅包含数字字符（'0' 到 '9'）。
     *
     * @param str 待检查的字符串
     * @return 如果字符串仅包含数字字符，则返回 true；否则返回 false。
     * @see ObjectUtil#isNumeric(CharSequence)
     */
    public static boolean isDigits(String str) {
        return isNumeric(str);
    }


    /**
     * 判断给定的字符序列是否表示一个数字。一个字符串被认为是数字，当且仅当其每个字符都是数字字符。
     *
     * @param cs 要检查的字符序列
     * @return 如果字符序列是数字，则返回 true；否则返回 false。
     */
    public static boolean isNumeric(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        }

        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }

        return true;
    }



    /**
     * 格式化Object 为 String
     * @param object object
     * @return String
     */
    public static String formatString(final Object object) {
        if (object instanceof CharSequence){
            return (String) object;
        }
        return (object == null) ? "" : object.toString();
    }

    /**
     * 将对象转换为字符串，如果对象为null，则返回空字符串。
     *
     * @param var 要转换的对象
     * @return 对象的字符串表示，如果对象为null，则返回空字符串
     */
    public static String toString(final Object var) {
        if (var instanceof CharSequence){
            return (String) var;
        }
        return (var == null) ? "" : var.toString();
    }

    /**
     * 将Long类型变量转换为字符串，如果为null，则返回空字符串。
     *
     * @param var 要转换的Long变量
     * @return Long变量的字符串表示，如果为null，则返回空字符串
     */
    public static String toString(final Long var) {
        return (var == null) ? "" : var.toString();
    }

    /**
     * 将Integer类型变量转换为字符串，如果为null，则返回空字符串。
     *
     * @param var 要转换的Integer变量
     * @return Integer变量的字符串表示，如果为null，则返回空字符串
     */
    public static String toString(final Integer var) {
        return (var == null) ? "" : var.toString();
    }

    /**
     * 将Short类型变量转换为字符串，如果为null，则返回空字符串。
     *
     * @param var 要转换的Short变量
     * @return Short变量的字符串表示，如果为null，则返回空字符串
     */
    public static String toString(final Short var) {
        return (var == null) ? "" : var.toString();
    }

    /**
     * 将Float类型变量转换为字符串，如果为null，则返回空字符串。
     *
     * @param var 要转换的Float变量
     * @return Float变量的字符串表示，如果为null，则返回空字符串
     */
    public static String toString(final Float var) {
        return (var == null) ? "" : var.toString();
    }

    /**
     * 将Double类型变量转换为字符串，如果为null，则返回空字符串。
     *
     * @param var 要转换的Double变量
     * @return Double变量的字符串表示，如果为null，则返回空字符串
     */
    public static String toString(final Double var) {
        return (var == null) ? "" : var.toString();
    }

    /**
     * 将Byte类型变量转换为字符串，如果为null，则返回空字符串。
     *
     * @param var 要转换的Byte变量
     * @return Byte变量的字符串表示，如果为null，则返回空字符串
     */
    public static String toString(final Byte var) {
        return (var == null) ? "" : var.toString();
    }

    /**
     * 将Boolean类型变量转换为字符串，如果为null，则返回空字符串。
     *
     * @param var 要转换的Boolean变量
     * @return Boolean变量的字符串表示，如果为null，则返回空字符串
     */
    public static String toString(final Boolean var) {
        return (var == null) ? "" : var.toString();
    }


    /**
     * 将字符串解析为 Short 类型的值。如果字符串为空，则返回默认值 0。
     *
     * @param str 要解析的字符串
     * @return 解析后的 Short 值，如果字符串为空则返回 0。
     */
    public static Short parseShort(final String str) {
        return parseShort(str, (short) 0);
    }

    /**
     * 将字符串解析为 Short 类型的值。如果字符串为空或解析失败，则返回指定的默认值。
     *
     * @param str 要解析的字符串
     * @param defaultValue 解析失败时返回的默认值
     * @return 解析后的 Short 值，如果字符串为空或解析失败则返回默认值。
     */
    public static Short parseShort(final String str, final Short defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Short.valueOf(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }


    /**
     * 将字符串解析为 Float 类型的值。如果字符串为空，则返回默认值 0.0。
     *
     * @param str 要解析的字符串
     * @return 解析后的 Float 值，如果字符串为空则返回 0.0。
     */
    public static Float parseFloat(final String str) {
        return parseFloat(str, 0.0F);
    }

    /**
     * 将字符串解析为 Float 类型的值。如果字符串为空或解析失败，则返回指定的默认值。
     *
     * @param str 要解析的字符串
     * @param defaultValue 解析失败时返回的默认值
     * @return 解析后的 Float 值，如果字符串为空或解析失败则返回默认值。
     */
    public static Float parseFloat(final String str, final Float defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Float.valueOf(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }



    /**
     * 将字符串解析为 Double 类型的值。如果字符串为空，则返回默认值 0.0。
     *
     * @param str 要解析的字符串
     * @return 解析后的 Double 值，如果字符串为空则返回 0.0。
     */
    public static Double parseDouble(final String str) {
        return parseDouble(str, 0.0D);
    }

    /**
     * 将字符串解析为 Double 类型的值。如果字符串为空或解析失败，则返回指定的默认值。
     *
     * @param str 要解析的字符串
     * @param defaultValue 解析失败时返回的默认值
     * @return 解析后的 Double 值，如果字符串为空或解析失败则返回默认值。
     */
    public static Double parseDouble(final String str, final Double defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        }

        try {
            return Double.valueOf(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }



    /**
     * 将字符串解析为 Integer 类型的值。如果字符串为空，则返回默认值 0。
     *
     * @param str 要解析的字符串
     * @return 解析后的 Integer 值，如果字符串为空则返回 0。
     */
    public static Integer parseInteger(final String str){
        return parseInteger(str, 0);
    }

    /**
     * 将字符串解析为 Integer 类型的值。如果字符串为空或解析失败，则返回指定的默认值。
     *
     * @param str 要解析的字符串
     * @param defaultValue 解析失败时返回的默认值
     * @return 解析后的 Integer 值，如果字符串为空或解析失败则返回默认值。
     */
    public static Integer parseInteger(final String str, final Integer defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }



    /**
     * 将字符串解析为 Long 类型的值。如果字符串为空，则返回默认值 0L。
     *
     * @param str 要解析的字符串
     * @return 解析后的 Long 值，如果字符串为空则返回 0L。
     */
    public static Long parseLong(final String str){
        return parseLong(str, 0L);
    }

    /**
     * 将字符串解析为 Long 类型的值。如果字符串为空或解析失败，则返回指定的默认值。
     *
     * @param str 要解析的字符串
     * @param defaultValue 解析失败时返回的默认值
     * @return 解析后的 Long 值，如果字符串为空或解析失败则返回默认值。
     */
    public static Long parseLong(final String str, final Long defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 将字符串解析为 boolean 类型的值。如果字符串为空，则返回默认值 false。
     *
     * @param str 要解析的字符串
     * @return 解析后的 boolean 值，如果字符串为空则返回 false。
     */
    public static boolean parseBoolean(final String str){
        return parseBoolean(str, false);
    }

    /**
     * 将字符串解析为 boolean 类型的值。如果字符串为空或解析失败，则返回指定的默认值。
     *
     * @param str 要解析的字符串
     * @param defaultValue 解析失败时返回的默认值
     * @return 解析后的 boolean 值，如果字符串为空或解析失败则返回默认值。
     */
    public static boolean parseBoolean(final String str, boolean defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception e) {
            return defaultValue;
        }
    }


    /**
     * 将 Object 格式化为 Integer 类型的值。如果 Object 为 null 或无法解析为数字，则返回默认值 0。
     *
     * @param object 要格式化的 Object
     * @return 格式化后的 Integer 值，如果 Object 为 null 或无法解析为数字则返回默认值 0。
     */
    public static Integer formatInteger(final Object object) {
        if (object == null) {
            return 0;
        }
        try {
            return Integer.valueOf(toString(object));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 将 Object 格式化为 Long 类型的值。如果 Object 为 null 或无法解析为数字，则返回默认值 0L。
     *
     * @param object 要格式化的 Object
     * @return 格式化后的 Long 值，如果 Object 为 null 或无法解析为数字则返回默认值 0L。
     */
    public static Long formatLong(final Object object) {
        if (object == null) {
            return 0L;
        }
        String str = toString(object);
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }


    /**
     * 将 Object 格式化为 Double 类型的值。如果 Object 为 null 或无法解析为 Double，则返回默认值 0.00。
     *
     * @param object 要格式化的 Object
     * @return 格式化后的 Double 值，如果 Object 为 null 或无法解析为 Double 则返回默认值 0.00。
     */
    public static Double formatDouble(final Object object) {
        if (object == null) {
            return 0.00;
        }
        try {
            return Double.valueOf(toString(object));
        } catch (Exception e) {
            return 0.00;
        }
    }



    /**
     * 将 Object 格式化为 Float 类型的值。如果 Object 为 null 或无法解析为 Float，则返回默认值 0.0F。
     *
     * @param object 要格式化的 Object
     * @return 格式化后的 Float 值，如果 Object 为 null 或无法解析为 Float 则返回默认值 0.0F。
     */
    public static Float formatFloat(final Object object){
        if (object == null){
            return 0.0F;
        } else {
            try {
                return Float.valueOf(toString(object));
            } catch (Exception e) {
                return 0.0F;
            }
        }
    }


    /**
     * 将 Object 格式化为 Short 类型的值。如果 Object 为 null 或无法解析为 Short，则返回默认值 0。
     *
     * @param object 要格式化的 Object
     * @return 格式化后的 Short 值，如果 Object 为 null 或无法解析为 Short 则返回默认值 0。
     */
    public static Short formatShort(final Object object){
        if (object == null){
            return 0;
        } else {
            try {
                return Short.valueOf(toString(object));
            } catch (Exception e) {
                return 0;
            }
        }
    }


    /**
     * 将 Object 对象格式化为 boolean 类型。
     * <p>
     * 这个方法尝试将给定的 Object 对象转换为 boolean 值。如果转换成功，
     * 则返回转换后的 boolean 值。如果无法进行转换或发生异常，将返回 false。
     *
     * @param object 要进行格式化的 Object 对象，可以为 null
     * @return 如果成功将对象格式化为 boolean，则返回格式化后的 boolean 值，否则返回 false
     *
     * <pre>
     * {@code
     * Object trueObject = "true";
     * Object falseObject = "false";
     * Object otherObject = "foo";
     *
     * boolean trueResult = formatBoolean(trueObject);  // 返回 true
     * boolean falseResult = formatBoolean(falseObject); // 返回 false
     * boolean otherResult = formatBoolean(otherObject); // 返回 false
     * }
     * </pre>
     */
    public static boolean formatBoolean(final Object object) {
        if (object == null) {
            return false;
        } else {
            try {
                // 尝试将字符串解析为 boolean 类型
                return Boolean.parseBoolean(toString(object));
            } catch (Exception e) {
                return false;
            }
        }
    }



    /**
     * <p>Checks if a String {@code str} contains Unicode digits,
     * if yes then concatenate all the digits in {@code str} and return it as a String.</p>
     *
     * <p>An empty ("") String will be returned if no digits found in {@code str}.</p>
     *
     * <pre>
     * ObjectUtil.getDigits(null)  = null
     * ObjectUtil.getDigits("")    = ""
     * ObjectUtil.getDigits("abc") = ""
     * ObjectUtil.getDigits("1000$") = "1000"
     * ObjectUtil.getDigits("1123~45") = "112345"
     * ObjectUtil.getDigits("(541) 754-3010") = "5417543010"
     * ObjectUtil.getDigits("\u0967\u0968\u0969") = "\u0967\u0968\u0969"
     * </pre>
     *
     * @param str the String to extract digits from, may be null
     * @return String with only digits,
     *           or an empty ("") String if no digits found,
     *           or {@code null} String if {@code str} is null
     */
    public static String getDigits(final String str) {
        if (str == null) {
            return "";
        }

        final int sz = str.length();
        final StringBuilder strDigits = new StringBuilder(sz);

        for (char tempChar : str.toCharArray()) {
            if (Character.isDigit(tempChar)) {
                strDigits.append(tempChar);
            }
        }

        return strDigits.toString();
    }


    /**
     * <p>Truncates a String. This will turn
     * "Now is the time for all good men" into "Now is the time for".</p>
     *
     * <p>Specifically:</p>
     * <ul>
     *   <li>If {@code str} is less than {@code maxWidth} characters
     *       long, return it.</li>
     *   <li>Else truncate it to {@code substring(str, 0, maxWidth)}.</li>
     *   <li>If {@code maxWidth} is less than {@code 0}, throw an
     *       {@code IllegalArgumentException}.</li>
     *   <li>In no case will it return a String of length greater than
     *       {@code maxWidth}.</li>
     * </ul>
     *
     * <pre>
     * ObjectUtil.truncate(null, 0)       = null
     * ObjectUtil.truncate(null, 2)       = null
     * ObjectUtil.truncate("", 4)         = ""
     * ObjectUtil.truncate("abcdefg", 4)  = "abcd"
     * ObjectUtil.truncate("abcdefg", 6)  = "abcdef"
     * ObjectUtil.truncate("abcdefg", 7)  = "abcdefg"
     * ObjectUtil.truncate("abcdefg", 8)  = "abcdefg"
     * ObjectUtil.truncate("abcdefg", -1) = throws an IllegalArgumentException
     * </pre>
     *
     * @param str  the String to truncate, may be null
     * @param maxWidth  maximum length of result String, must be positive
     * @return truncated String, {@code null} if null String input
     */
    public static String truncate(final String str, final int maxWidth) {
        return truncate(str, 0, maxWidth);
    }

    /**
     * <p>Truncates a String. This will turn
     * "Now is the time for all good men" into "is the time for all".</p>
     *
     * <p>Works like {@code truncate(String, int)}, but allows you to specify
     * a "left edge" offset.
     *
     * <p>Specifically:</p>
     * <ul>
     *   <li>If {@code str} is less than {@code maxWidth} characters
     *       long, return it.</li>
     *   <li>Else truncate it to {@code substring(str, offset, maxWidth)}.</li>
     *   <li>If {@code maxWidth} is less than {@code 0}, throw an
     *       {@code IllegalArgumentException}.</li>
     *   <li>If {@code offset} is less than {@code 0}, throw an
     *       {@code IllegalArgumentException}.</li>
     *   <li>In no case will it return a String of length greater than
     *       {@code maxWidth}.</li>
     * </ul>
     *
     * <pre>
     * ObjectUtil.truncate(null, 0, 0) = null
     * ObjectUtil.truncate(null, 2, 4) = null
     * ObjectUtil.truncate("", 0, 10) = ""
     * ObjectUtil.truncate("", 2, 10) = ""
     * ObjectUtil.truncate("abcdefghij", 0, 3) = "abc"
     * ObjectUtil.truncate("abcdefghij", 5, 6) = "fghij"
     * ObjectUtil.truncate("raspberry peach", 10, 15) = "peach"
     * ObjectUtil.truncate("abcdefghijklmno", 0, 10) = "abcdefghij"
     * ObjectUtil.truncate("abcdefghijklmno", -1, 10) = throws an IllegalArgumentException
     * ObjectUtil.truncate("abcdefghijklmno", Integer.MIN_VALUE, 10) = "abcdefghij"
     * ObjectUtil.truncate("abcdefghijklmno", Integer.MIN_VALUE, Integer.MAX_VALUE) = "abcdefghijklmno"
     * ObjectUtil.truncate("abcdefghijklmno", 0, Integer.MAX_VALUE) = "abcdefghijklmno"
     * ObjectUtil.truncate("abcdefghijklmno", 1, 10) = "bcdefghijk"
     * ObjectUtil.truncate("abcdefghijklmno", 2, 10) = "cdefghijkl"
     * ObjectUtil.truncate("abcdefghijklmno", 3, 10) = "defghijklm"
     * ObjectUtil.truncate("abcdefghijklmno", 4, 10) = "efghijklmn"
     * ObjectUtil.truncate("abcdefghijklmno", 5, 10) = "fghijklmno"
     * ObjectUtil.truncate("abcdefghijklmno", 5, 5) = "fghij"
     * ObjectUtil.truncate("abcdefghijklmno", 5, 3) = "fgh"
     * ObjectUtil.truncate("abcdefghijklmno", 10, 3) = "klm"
     * ObjectUtil.truncate("abcdefghijklmno", 10, Integer.MAX_VALUE) = "klmno"
     * ObjectUtil.truncate("abcdefghijklmno", 13, 1) = "n"
     * ObjectUtil.truncate("abcdefghijklmno", 13, Integer.MAX_VALUE) = "no"
     * ObjectUtil.truncate("abcdefghijklmno", 14, 1) = "o"
     * ObjectUtil.truncate("abcdefghijklmno", 14, Integer.MAX_VALUE) = "o"
     * ObjectUtil.truncate("abcdefghijklmno", 15, 1) = ""
     * ObjectUtil.truncate("abcdefghijklmno", 15, Integer.MAX_VALUE) = ""
     * ObjectUtil.truncate("abcdefghijklmno", Integer.MAX_VALUE, Integer.MAX_VALUE) = ""
     * ObjectUtil.truncate("abcdefghij", 3, -1) = throws an IllegalArgumentException
     * ObjectUtil.truncate("abcdefghij", -2, 4) = throws an IllegalArgumentException
     * </pre>
     *
     * @param str  the String to check, may be null
     * @param offset  left edge of source String
     * @param maxWidth  maximum length of result String, must be positive
     * @return truncated String, {@code null} if null String input
     */
    public static String truncate(final String str, final int offset, final int maxWidth) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        if (maxWidth < 0) {
            throw new IllegalArgumentException("maxWith cannot be negative");
        }
        if (str == null) {
            return null;
        }
        if (offset > str.length()) {
            return "";
        }
        if (str.length() > maxWidth) {
            final int ix = offset + maxWidth > str.length() ? str.length() : offset + maxWidth;
            return str.substring(offset, ix);
        }
        return str.substring(offset);
    }


    /**
     * 提供一种将字符串转换为UTF-8编码字节数组的便捷方法。
     *
     * @param string 要转换的字符串，可以为null
     * @return UTF-8编码的字节数组，如果输入为null则返回空字节数组
     */
    public static byte[] getBytesUtf8(final String string) {
        if (string == null) {
            return new byte[0];  // 返回空字节数组
        }
        return string.getBytes(CharsetConst.UTF8_CHARSET);
    }



    /**
     * 将字符串转换为指定字符集编码的字节数组。
     *
     * @param string  要转换的字符串，可以为null
     * @param charset 要使用的字符集，如果为null则默认为UTF-8
     * @return 字节数组，如果输入为null则返回null
     */
    private static byte[] getBytes(final String string, final Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }


    public static String byteToString(final byte[] bytes) {
        return byteToString(bytes,CharsetConst.UTF8_CHARSET);
    }


    private static String byteToString(final byte[] bytes, final Charset charset) {
        return bytes == null ? null : new String(bytes, charset);
    }


    public static boolean nullSafeEquals( Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        if (o1.equals(o2)) {
            return true;
        }
        if (o1.getClass().isArray() && o2.getClass().isArray()) {
            return arrayEquals(o1, o2);
        }
        return false;
    }

    /**
     * Compare the given arrays with {@code Arrays.equals}, performing an equality
     * check based on the array elements rather than the array reference.
     * @param o1 first array to compare
     * @param o2 second array to compare
     * @return whether the given objects are equal
     * @see #nullSafeEquals(Object, Object)
     * @see Arrays#equals
     */
    private static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[]) o1, (Object[]) o2);
        }
        if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[]) o1, (boolean[]) o2);
        }
        if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        }
        if (o1 instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        }
        if (o1 instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[]) o1, (double[]) o2);
        }
        if (o1 instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[]) o1, (float[]) o2);
        }
        if (o1 instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[]) o1, (int[]) o2);
        }
        if (o1 instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[]) o1, (long[]) o2);
        }
        if (o1 instanceof short[] && o2 instanceof short[]) {
            return Arrays.equals((short[]) o1, (short[]) o2);
        }
        return false;
    }



    /**
     * 判断给定的对象数组中是否包含指定的对象。
     *
     * @param array 要搜索的对象数组
     * @param objectToFind 要在数组中查找的对象
     * @return 如果数组包含指定的对象，则返回 true；否则返回 false
     */
    public static boolean contains(Object[] array, Object objectToFind){
        return indexOf(array, objectToFind) != -1;
    }

    public static int indexOf(Object[] array, Object objectToFind) {
        return indexOf(array, objectToFind, 0);
    }

    public static int indexOf(Object[] array, Object objectToFind, int startIndex) {
        if (array == null) {
            return -1;
        } else {
            if (startIndex < 0) {
                startIndex = 0;
            }

            int i;
            if (objectToFind == null) {
                for(i = startIndex; i < array.length; ++i) {
                    if (array[i] == null) {
                        return i;
                    }
                }
            } else {
                for(i = startIndex; i < array.length; ++i) {
                    if (objectToFind.equals(array[i])) {
                        return i;
                    }
                }
            }

            return -1;
        }
    }


    /**
     * 判断字符串是否以指定内容开始，忽略大小写。
     *
     * @param str    要检查的字符串
     * @param prefix 要查找的前缀
     * @return 如果输入字符串不为null，且以指定前缀开始（忽略大小写），则返回true；否则返回false
     * @see java.lang.String#startsWith(String)
     */
    public static boolean startsWithIgnoreCase(final String str, final String prefix) {
        return str != null && prefix != null && str.length() >= prefix.length()
                && str.regionMatches(true, 0, prefix, 0, prefix.length());
    }


    /**
     * 判断字符串是否以指定内容结尾，忽略大小写。
     *
     * @param str    要检查的字符串
     * @param suffix 要查找的后缀
     * @return 如果输入字符串不为null，且以指定后缀结尾（忽略大小写），则返回true；否则返回false
     * @see java.lang.String#endsWith(String)
     */
    public static boolean endsWithIgnoreCase(final String str, final String suffix) {
        return str != null && suffix != null && str.length() >= suffix.length() &&
                str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length());
    }



    /**
     * 编码字符串
     *
     * @param str 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return byte[]
     */
    public static byte[] toByte(CharSequence str, String charset) {
        return toByte(str, isEmpty(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }


    /**
     * 将  com.github.fashionbrot 替换成  window 或 linux 对应的  / 或 \
     * @param str 字符串
     * @return String
     */
    public static String replacePath(String str){
        return str.replaceAll("\\.", Matcher.quoteReplacement(File.separator))
                .replaceAll("/",Matcher.quoteReplacement(File.separator))
                .replaceAll("\\\\",Matcher.quoteReplacement(File.separator));
    }


    /**
     * 分割字符串 返回List
     * @param content   文本值
     * @param delimiter 分割符
     * @return List
     */
    public static  List<String> splitContent(String content, String delimiter){
        if (ObjectUtil.isEmpty(content)){
            return new ArrayList<>();
        }
        return Arrays.asList(content.split(delimiter)) ;
    }





    /**
     * 编码字符串
     *
     * @param str 字符串
     * @return byte[]
     */
    public static byte[] toByte(CharSequence str) {
        return toByte(str,Charset.defaultCharset());
    }

    /**
     * 编码字符串
     *
     * @param str 字符串
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return byte[]
     */
    public static byte[] toByte(CharSequence str, Charset charset) {
        if (ObjectUtil.isEmpty(str)) {
            return new byte[]{};
        }
        if (null == charset) {
            return str.toString().getBytes();
        }
        return str.toString().getBytes(charset);
    }


    /**
     * 检查Long列是否大于指定的长整型值。
     *
     * @param column 要比较的Long列
     * @param val 指定的长整型值
     * @return 如果条件为真且列值大于指定值，则返回true；否则返回false
     */
    public static boolean gt(Long column, long val) {
        return gt(true, column, val);
    }

    /**
     * 检查Long列是否大于指定的长整型值，并考虑附加的条件。
     *
     * @param condition 附加的条件
     * @param column 要比较的Long列
     * @param val 指定的长整型值
     * @return 如果条件为真且列值大于指定值，则返回true；否则返回false
     */
    public static boolean gt(boolean condition, Long column, long val) {
        return condition && column != null && column > val;
    }

    /**
     * 检查Long列是否大于或等于指定的长整型值。
     *
     * @param column 要比较的Long列
     * @param val 指定的长整型值
     * @return 如果条件为真且列值大于或等于指定值，则返回true；否则返回false
     */
    public static boolean ge(Long column, long val) {
        return ge(true, column, val);
    }

    /**
     * 检查Long列是否大于或等于指定的长整型值，并考虑附加的条件。
     *
     * @param condition 附加的条件
     * @param column 要比较的Long列
     * @param val 指定的长整型值
     * @return 如果条件为真且列值大于或等于指定值，则返回true；否则返回false
     */
    public static boolean ge(boolean condition, Long column, long val) {
        return condition && column != null && column >= val;
    }

    /**
     * 检查Long列是否小于指定的长整型值。
     *
     * @param column 要比较的Long列
     * @param val 指定的长整型值
     * @return 如果条件为真且列值小于指定值，则返回true；否则返回false
     */
    public static boolean lt(Long column, long val) {
        return lt(true, column, val);
    }

    /**
     * 检查Long列是否小于指定的长整型值，并考虑附加的条件。
     *
     * @param condition 附加的条件
     * @param column 要比较的Long列
     * @param val 指定的长整型值
     * @return 如果条件为真且列值小于指定值，则返回true；否则返回false
     */
    public static boolean lt(boolean condition, Long column, long val) {
        return condition && column != null && column < val;
    }

    /**
     * 检查Long列是否小于或等于指定的长整型值。
     *
     * @param column 要比较的Long列
     * @param val 指定的长整型值
     * @return 如果条件为真且列值小于或等于指定值，则返回true；否则返回false
     */
    public static boolean le(Long column, long val) {
        return le(true, column, val);
    }

    /**
     * 检查Long列是否小于或等于指定的长整型值，并考虑附加的条件。
     *
     * @param condition 附加的条件
     * @param column 要比较的Long列
     * @param val 指定的长整型值
     * @return 如果条件为真且列值小于或等于指定值，则返回true；否则返回false
     */
    public static boolean le(boolean condition, Long column, long val) {
        return condition && column != null && column <= val;
    }



    /**
     * 检查给定的整数列是否大于指定的值。
     *
     * @param column 要比较的整数列
     * @param val 要比较的值
     * @return 如果整数列大于指定的值，则为 true；否则为 false
     */
    public static boolean gt(Integer column, int val) {
        return gt(true, column, val);
    }

    /**
     * 根据给定的条件，检查给定的整数列是否大于指定的值。
     *
     * @param condition 条件，如果为 true，则进行比较；否则直接返回 false
     * @param column 要比较的整数列
     * @param val 要比较的值
     * @return 如果满足条件且整数列大于指定的值，则为 true；否则为 false
     */
    public static boolean gt(boolean condition, Integer column, int val) {
        return condition && column != null && column > val;
    }

    /**
     * 检查给定的整数列是否大于或等于指定的值。
     *
     * @param column 要比较的整数列
     * @param val 要比较的值
     * @return 如果整数列大于或等于指定的值，则为 true；否则为 false
     */
    public static boolean ge(Integer column, int val) {
        return ge(true, column, val);
    }

    /**
     * 根据给定的条件，检查给定的整数列是否大于或等于指定的值。
     *
     * @param condition 条件，如果为 true，则进行比较；否则直接返回 false
     * @param column 要比较的整数列
     * @param val 要比较的值
     * @return 如果满足条件且整数列大于或等于指定的值，则为 true；否则为 false
     */
    public static boolean ge(boolean condition, Integer column, int val) {
        return condition && column != null && column >= val;
    }

    /**
     * 检查给定的整数列是否小于指定的值。
     *
     * @param column 要比较的整数列
     * @param val 要比较的值
     * @return 如果整数列小于指定的值，则为 true；否则为 false
     */
    public static boolean lt(Integer column, int val) {
        return lt(true, column, val);
    }

    /**
     * 根据给定的条件，检查给定的整数列是否小于指定的值。
     *
     * @param condition 条件，如果为 true，则进行比较；否则直接返回 false
     * @param column 要比较的整数列
     * @param val 要比较的值
     * @return 如果满足条件且整数列小于指定的值，则为 true；否则为 false
     */
    public static boolean lt(boolean condition, Integer column, int val) {
        return condition && column != null && column < val;
    }

    /**
     * 检查给定的整数列是否小于或等于指定的值。
     *
     * @param column 要比较的整数列
     * @param val 要比较的值
     * @return 如果整数列小于或等于指定的值，则为 true；否则为 false
     */
    public static boolean le(Integer column, int val) {
        return le(true, column, val);
    }

    /**
     * 根据给定的条件，检查给定的整数列是否小于或等于指定的值。
     *
     * @param condition 条件，如果为 true，则进行比较；否则直接返回 false
     * @param column 要比较的整数列
     * @param val 要比较的值
     * @return 如果满足条件且整数列小于或等于指定的值，则为 true；否则为 false
     */
    public static boolean le(boolean condition, Integer column, int val) {
        return condition && column != null && column <= val;
    }



    /**
     * 判断给定的列值是否大于指定值。
     *
     * @param column 列值
     * @param val 指定值
     * @return 如果列值大于指定值，则返回 true，否则返回 false
     */
    public static boolean gt(Double column, double val) {
        return gt(true, column, val);
    }

    /**
     * 根据条件判断给定的列值是否大于指定值。
     *
     * @param condition 判断条件
     * @param column 列值
     * @param val 指定值
     * @return 如果条件成立且列值大于指定值，则返回 true，否则返回 false
     */
    public static boolean gt(boolean condition, Double column, double val) {
        return condition && column != null && column > val;
    }

    /**
     * 判断给定的列值是否大于等于指定值。
     *
     * @param column 列值
     * @param val 指定值
     * @return 如果列值大于等于指定值，则返回 true，否则返回 false
     */
    public static boolean ge(Double column, double val) {
        return ge(true, column, val);
    }

    /**
     * 根据条件判断给定的列值是否大于等于指定值。
     *
     * @param condition 判断条件
     * @param column 列值
     * @param val 指定值
     * @return 如果条件成立且列值大于等于指定值，则返回 true，否则返回 false
     */
    public static boolean ge(boolean condition, Double column, double val) {
        return condition && column != null && column >= val;
    }

    /**
     * 判断给定的列值是否小于指定值。
     *
     * @param column 列值
     * @param val 指定值
     * @return 如果列值小于指定值，则返回 true，否则返回 false
     */
    public static boolean lt(Double column, double val) {
        return lt(true, column, val);
    }

    /**
     * 根据条件判断给定的列值是否小于指定值。
     *
     * @param condition 判断条件
     * @param column 列值
     * @param val 指定值
     * @return 如果条件成立且列值小于指定值，则返回 true，否则返回 false
     */
    public static boolean lt(boolean condition, Double column, double val) {
        return condition && column != null && column < val;
    }

    /**
     * 判断给定的列值是否小于等于指定值。
     *
     * @param column 列值
     * @param val 指定值
     * @return 如果列值小于等于指定值，则返回 true，否则返回 false
     */
    public static boolean le(Double column, double val) {
        return le(true, column, val);
    }

    /**
     * 根据条件判断给定的列值是否小于等于指定值。
     *
     * @param condition 判断条件
     * @param column 列值
     * @param val 指定值
     * @return 如果条件成立且列值小于等于指定值，则返回 true，否则返回 false
     */
    public static boolean le(boolean condition, Double column, double val) {
        return condition && column != null && column <= val;
    }




}
