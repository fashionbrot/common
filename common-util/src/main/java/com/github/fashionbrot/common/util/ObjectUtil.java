package com.github.fashionbrot.common.util;


import lombok.extern.slf4j.Slf4j;

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

    public static final String EMPTY="";

    /**
     * trim
     * @param str str
     * @return String
     */
    public static String trim( String str) {
        return str == null ? EMPTY : str.trim();
    }

    public static boolean isBlank(final String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((!Character.isWhitespace(str.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(final String str) {
        return !isBlank(str);
    }


    /**
     * 判断字符是否为空
     * @param cs cs
     * @return boolean
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 判断字符是否不为空
     * @param cs cs
     * @return boolean
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }


    /**
     * 判断集合是否为空
     * @param collection collection
     * @return boolean
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断集合是否不为空
     * @param collection collection
     * @return boolean
     */
    public static boolean isNotEmpty(final Collection<?> collection) {
        return !isEmpty(collection);
    }


    /**
     * 判断Map是否为空
     * @param map map
     * @return boolean
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断Map是否不为空
     * @param map map
     * @return boolean
     */
    public static boolean isNotEmpty(final Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断objects是否为空
     * @param objects objects
     * @return boolean
     */
    public static boolean isEmpty(final Object[] objects) {
        return objects == null || objects.length==0;
    }

    /**
     * 判断objects是否不为空
     * @param objects objects
     * @return boolean
     */
    public static boolean isNotEmpty(final Object[] objects) {
        return !isEmpty(objects);
    }


    /**
     * 判断 bytes 是否不为空
     * @param bytes bytes
     * @return boolean
     */
    public static boolean isNotEmpty(final byte[] bytes) {
        return !isEmpty(bytes);
    }

    /**
     * 判断 bytes 是否为空
     * @param bytes bytes
     * @return boolean
     */
    public static boolean isEmpty(final byte[] bytes) {
        return bytes == null || bytes.length==0;
    }




    public static boolean isDigits(String str) {
        return isNumeric(str);
    }


    /**
     * 判断 字符串是否是数字
     * @param cs cs
     * @return boolean
     */
    public static boolean isNumeric(final CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for(int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * 格式化Object 为 String
     * @param object object
     * @return String
     */
    public static String formatString(final Object object){
        if (object==null){
            return EMPTY;
        }else{
            return object.toString();
        }
    }


    public static Short parseShort(final String str){
        return parseShort(str,(short)0);
    }

    public static Short parseShort(final String str,final Short defaultValue){
        if (ObjectUtil.isEmpty(str)){
            return defaultValue;
        }
        try {
            return Short.valueOf(str);
        }catch (Exception e){
            return defaultValue;
        }
    }

    public static Float parseFloat(final String str) {
        return parseFloat(str,0.0F);
    }

    public static Float parseFloat(final String str,final Float defaultValue) {
        if (ObjectUtil.isEmpty(str)){
            return defaultValue;
        }
        try {
            return Float.valueOf(str);
        }catch (Exception e){
            return defaultValue;
        }
    }

    public static Double parseDouble(final String str) {
        return parseDouble(str,0.0D);
    }

    public static Double parseDouble(final String str,final Double defaultValue) {
        if (ObjectUtil.isEmpty(str)){
            return defaultValue;
        }
        try {
            return Double.valueOf(str);
        }catch (Exception e){
            return defaultValue;
        }
    }

    /**
     * 格式化String 为 Integer
     * @param str str
     * @return Integer
     */
    public static Integer parseInteger(final String str){
        return parseInteger(str,0);
    }

    /**
     * 格式化String 为 Integer
     * @param str str
     * @param defaultValue defaultValue
     * @return Integer
     */
    public static Integer parseInteger(final String str ,final Integer defaultValue){
        if (ObjectUtil.isEmpty(str)){
            return defaultValue;
        }
        try {
            return Integer.valueOf(str);
        }catch (Exception e){
            return defaultValue;
        }
    }

    /**
     * 格式String 为 Long
     * @param str str
     * @return Long
     */
    public static Long parseLong(final String str){
        return parseLong(str,0L);
    }

    /**
     * 格式String 为 Long
     * @param str str
     * @param defaultValue defaultValue
     * @return Long
     */
    public static Long parseLong(final String str ,final Long defaultValue){
        if (ObjectUtil.isEmpty(str)){
            return defaultValue;
        }
        try {
            return Long.parseLong(str);
        }catch (Exception e){
            return defaultValue;
        }
    }

    /**
     * 格式化 String 为 boolean
     * @param str str
     * @return boolean
     */
    public static boolean parseBoolean(final String str){
        return parseBoolean(str,false);
    }

    /**
     * 格式化 String 为 boolean
     * @param str str
     * @param defaultValue defaultValue
     * @return boolean
     */
    public static boolean parseBoolean(final String str,boolean defaultValue){
        if (ObjectUtil.isEmpty(str)){
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(str);
        }catch (Exception e){
            return defaultValue;
        }
    }

    /**
     * 格式化 Object 为 Integer
     * @param object object
     * @return Integer
     */
    public static Integer formatInteger(final Object object){
        if (object==null){
            return 0;
        }else{
            String str=object.toString();
            if (!isNumeric(str)){
                return 0;
            }else{
                try {
                    return Integer.valueOf(str);
                }catch (Exception e){
                    return 0;
                }
            }
        }
    }

    /**
     * 格式化 Object 为 Long
     * @param object object
     * @return Long
     */
    public static Long formatLong(final Object object){
        if (object==null){
            return 0L;
        }else{
            String str=object.toString();
            if (!isNumeric(str)){
                return 0L;
            }else{
                try {
                    return Long.valueOf(str);
                }catch (Exception e){
                    return 0L;
                }
            }
        }
    }

    /**
     * 格式化 Object 为 Double
     * @param object object
     * @return Double
     */
    public static Double formatDouble(final Object object){
        if (object==null){
            return 0.00;
        }else{
            try {
                return Double.valueOf(object.toString());
            }catch (Exception e){
                return 0.00;
            }
        }
    }

    /**
     * 格式化 Object 为 Float
     * @param object object
     * @return Float
     */
    public static Float formatFloat(final Object object){
        if (object==null){
            return 0F;
        }else{
            try {
                return Float.valueOf(object.toString());
            }catch (Exception e){
                return 0F;
            }
        }
    }

    /**
     * 格式化 Object 为 Short
     * @param object object
     * @return Short
     */
    public static Short formatShort(final Object object){
        if (object==null){
            return 0;
        }else{
            try {
                return Short.valueOf(object.toString());
            }catch (Exception e){
                return 0;
            }
        }
    }

    /**
     * 格式化Object 为 boolean
     * @param object object
     * @return boolean
     */
    public static boolean formatBoolean(final Object object){
        if (object==null){
            return false;
        }else{
            String str=object.toString();
            try {
                return Boolean.valueOf(str);
            }catch (Exception e){
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
     * @since 3.6
     */
    public static String getDigits(final String str) {
        if (isEmpty(str)) {
            return str;
        }
        final int sz = str.length();
        final StringBuilder strDigits = new StringBuilder(sz);
        for (int i = 0; i < sz; i++) {
            final char tempChar = str.charAt(i);
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
     * @since 3.5
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
     * @since 3.5
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
            return EMPTY;
        }
        if (str.length() > maxWidth) {
            final int ix = offset + maxWidth > str.length() ? str.length() : offset + maxWidth;
            return str.substring(offset, ix);
        }
        return str.substring(offset);
    }


    public static byte[] getBytesUtf8(final String string) {
        return getBytes(string, Charset.forName("UTF-8"));
    }

    /**
     * Calls {@link String#getBytes(Charset)}
     *
     * @param string
     *            The string to encode (if null, return null).
     * @param charset
     *            The {@link Charset} to encode the <code>String</code>
     * @return the encoded bytes
     */
    private static byte[] getBytes(final String string, final Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }

    public static String newStringUsAscii(final byte[] bytes) {
        return newString(bytes, Charset.forName("UTF-8"));
    }


    private static String newString(final byte[] bytes, final Charset charset) {
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



    public static int getIntValue(Object object){
        if (object==null){
            return 0;
        }else{
            String str=object.toString();
            if (!isNumeric(str)){
                return 0;
            }else{
                try {
                    return Integer.valueOf(str);
                }catch (Exception e){
                    return 0;
                }
            }
        }
    }

    public static long getLongValue(Object object){
        if (object==null){
            return 0;
        }else{
            String str=object.toString();
            if (!isNumeric(str)){
                return 0;
            }else{
                try {
                    return Long.valueOf(str);
                }catch (Exception e){
                    return 0;
                }
            }
        }
    }

    public static double getDoubleValue(Object object){
        if (object==null){
            return 0;
        }else{
            try {
                return Double.valueOf(object.toString());
            }catch (Exception e){
                return 0;
            }
        }
    }

    public static float getFloatValue(Object object){
        if (object==null){
            return 0;
        }else{
            try {
                return Float.valueOf(object.toString());
            }catch (Exception e){
                return 0;
            }
        }
    }

    public static short getShortValue(Object object){
        if (object==null){
            return 0;
        }else{
            try {
                return Short.valueOf(object.toString());
            }catch (Exception e){
                return 0;
            }
        }
    }

    public static BigDecimal getBigDecimalValue(Object object){
        if (object==null){
            return BigDecimal.ZERO;
        }else{
            try {
                return new BigDecimal(object.toString());
            }catch (Exception e){
                return BigDecimal.ZERO;
            }
        }
    }

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
     * 验证 Boolean
     * @param b Boolean
     * @return boolean
     */
    public static boolean isBoolean(Boolean b){
        if (b==null){
            return false;
        }
        return b;
    }

    /**
     * 验证 Boolean 是否是false
     * @param bool Boolean
     * @return boolean
     */
    public static boolean isFalse(Boolean bool){
        return bool==null || !isBoolean(bool);
    }

    /**
     * 验证 Boolean 是否是true
     * @param bool Boolean
     * @return boolean
     */
    public static boolean isTrue(Boolean bool){
        return isBoolean(bool);
    }


    /**
     * 判断字符串是否是以指定内容结束。忽略大小写
     * @param str the {@code String} to check
     * @param prefix the prefix to look for
     * @see java.lang.String#startsWith
     * @return boolean
     */
    public static boolean startsWithIgnoreCase(final String str,final String prefix) {
        return (str != null && prefix != null && str.length() >= prefix.length()
                && str.regionMatches(true, 0, prefix, 0, prefix.length()));
    }

    /**
     * 判断字符串是否已指定内容开头。忽略大小写
     * @param str the {@code String} to check
     * @param suffix the suffix to look for
     * @see java.lang.String#endsWith
     * @return boolean
     */
    public static boolean endsWithIgnoreCase(final String str, final String suffix) {
        return (str != null && suffix != null && str.length() >= suffix.length() &&
                str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length()));
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
     * @param charset 字符集，如果此字段为空，则解码的结果取决于平台
     * @return byte[]
     */
    public static byte[] toByte(CharSequence str, Charset charset) {
        if (str == null) {
            return null;
        }
        if (null == charset) {
            return str.toString().getBytes();
        }
        return str.toString().getBytes(charset);
    }


}
