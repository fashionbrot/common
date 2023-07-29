package com.github.fashionbrot.common.encrypt;

import com.github.fashionbrot.common.util.ObjectUtil;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Aes 加密解密
 * @author fashi
 */
public class AesUtil {


    /**
     * aes 加密
     * @param str str
     * @param key key
     * @return byte[]
     */
    public static byte[]  encrypt(String str ,String key){
        try {
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),"AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,spec);
            byte[] encryptByte = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
            return encryptByte;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * aes 加密
     * @param str   str
     * @param key   key
     * @return String
     */
    public static String encryptString(String str,String key){
        byte[] encrypt = encrypt(str, key);
        return parseByte2HexStr(encrypt);
    }


    /**
     * aes 解密
     * @param str str
     * @param key key
     * @return byte[]
     */
    public static byte[] decrypt(String str ,String key){
        try {

            byte[] bytes = parseHexStr2Byte(str);

            SecretKeySpec spec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),"AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,spec);
            byte[] encryptByte = cipher.doFinal(bytes);
            return encryptByte;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ase 解密
     * @param str str
     * @param key key
     * @return String
     */
    public static String decryptString(String str,String key){
        byte[] decrypt = decrypt(str, key);
        return byteToString(decrypt);
    }

    /**
     * byte[]  转  String
     * @param bytes bytes
     * @return  String
     */
    public static String byteToString(byte[] bytes){
        if (ObjectUtil.isNotEmpty(bytes)){
            return new String(bytes,StandardCharsets.UTF_8);
        }
        return "";
    }

    /**
     * 二进制转换 16进制
     * @param bytes bytes
     * @return String
     */
    public static String parseByte2HexStr(byte[] bytes) {
        if (ObjectUtil.isNotEmpty(bytes)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(bytes[i] & 0xFF);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                sb.append(hex.toUpperCase());
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 16进制转换二进制
     * @param hexStr    hexStr
     * @return byte[]
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (ObjectUtil.isEmpty(hexStr)) {
            return null;
        }
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

//    public static void main(String[] args) {
//        String key = "q!w@e#r$t%Y^UIOP";
//        String str = "张三";
//        System.out.println(str);
//        String encrypt = encryptString(str, key);
//        System.out.println(encrypt);
//        String decrypt = decryptString(encrypt, key);
//        System.out.println(decrypt);
//
//        String salt = NanoIdUtil.randomNanoId(32);
//        System.out.println("salt:"+salt);
//        String password = "12345";
//        String encryptPassword = AesUtil.encryptString(password, salt);
//        System.out.println("encryptPassword:"+encryptPassword);
//
//    }
}
