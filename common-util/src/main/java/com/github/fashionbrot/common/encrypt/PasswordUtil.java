package com.github.fashionbrot.common.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


public class PasswordUtil {

//    public static void main(String[] args){
//        //be3ce882f406df824ac7e2bced77fcf2
//        String uuid = "3d76917c8c6e48259dbb48deba993a59";//getSalt();
//        String pwd="test";
//        System.out.println(encryptPassword(pwd,uuid));
//    }


    public static String getSalt(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 加密
     * @param password  密码
     * @param salt      盐
     * @return  String
     */
    public static String encryptPassword(String password, String salt) {
        MessageDigest instance = null;
        try {
            instance = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        instance.reset();
        instance.update(salt.getBytes());
        byte[] digest = instance.digest(password.getBytes());
        return md5DigestAsHex(digest);
    }



    private static final String MD5_ALGORITHM_NAME = "MD5";
    private static final char[] HEX_CHARS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    public static byte[] md5Digest(byte[] bytes) {
        return digest(MD5_ALGORITHM_NAME, bytes);
    }

    public static String md5DigestAsHex(byte[] bytes) {
        return digestAsHexString(MD5_ALGORITHM_NAME, bytes);
    }

    public static StringBuilder appendMd5DigestAsHex(byte[] bytes, StringBuilder builder) {
        return appendDigestAsHex(MD5_ALGORITHM_NAME, bytes, builder);
    }

    private static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException var2) {
            throw new IllegalStateException("Could not find MessageDigest with algorithm \"" + algorithm + "\"", var2);
        }
    }

    private static byte[] digest(String algorithm, byte[] bytes) {
        return getDigest(algorithm).digest(bytes);
    }

    private static String digestAsHexString(String algorithm, byte[] bytes) {
        char[] hexDigest = digestAsHexChars(algorithm, bytes);
        return new String(hexDigest);
    }

    private static StringBuilder appendDigestAsHex(String algorithm, byte[] bytes, StringBuilder builder) {
        char[] hexDigest = digestAsHexChars(algorithm, bytes);
        return builder.append(hexDigest);
    }

    private static char[] digestAsHexChars(String algorithm, byte[] bytes) {
        byte[] digest = digest(algorithm, bytes);
        return encodeHex(digest);
    }

    private static char[] encodeHex(byte[] bytes) {
        char[] chars = new char[32];

        for(int i = 0; i < chars.length; i += 2) {
            byte b = bytes[i / 2];
            chars[i] = HEX_CHARS[b >>> 4 & 15];
            chars[i + 1] = HEX_CHARS[b & 15];
        }

        return chars;
    }


}
