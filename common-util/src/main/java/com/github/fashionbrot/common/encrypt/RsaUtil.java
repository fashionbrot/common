package com.github.fashionbrot.common.encrypt;


import com.github.fashionbrot.common.util.Base64Util;
import com.github.fashionbrot.common.util.ObjectUtil;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA 加密解密
 * @author fashi
 */
public class RsaUtil {


    /**
     * 随机生成密钥对
     * @param keySize  keySize 512 1024
     * @return  KeyPair
     */
    public static KeyPair genKeyPair(Integer keySize) {
        return genKeyPair(keySize,null);
    }

    /**
     * 随机生成密钥对
     * @param keySize  keySize 512 1024
     * @param seed seed
     * @return  KeyPair
     */
    public static KeyPair genKeyPair(Integer keySize,byte[] seed) {
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(keySize, ObjectUtil.isNotEmpty(seed)? new SecureRandom(seed): new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }


    /**
     * 随机生成密钥对
     * @param keySize   keySize 512 1024
     * @param seed      seed
     * @return map
     */
    public static Map<String, String> genKeyPairMap(Integer keySize,byte[] seed) {

        KeyPair keyPair =genKeyPair(keySize,seed);
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        String publicKeyStr = publicKeyToString(publicKey);
        // 得到私钥字符串
        String privateKeyStr = privateKeyToString(privateKey);

        Map<String, String> keyMap = new HashMap<>(2);
        // 将公钥和私钥保存到Map
        keyMap.put("publicKey", publicKeyStr);
        keyMap.put("privateKey", privateKeyStr);
        return keyMap;
    }

    /**
     * 公钥 RSAPublicKey 转 String
     * @param publicKey publicKey
     * @return String
     */
    public static String publicKeyToString(final RSAPublicKey publicKey) {
        if (publicKey == null) {
            return "";
        }
        return Base64Util.encodeBase64String(publicKey.getEncoded());
    }

    /**
     * 私钥 RSAPrivateKey 转 字符串
     * @param privateKey privateKey
     * @return  String
     */
    public static String privateKeyToString(final RSAPrivateKey privateKey) {
        if (privateKey == null) {
            return "";
        }
        return Base64Util.encodeBase64String(privateKey.getEncoded());
    }

    /**
     * RSA公钥加密
     *
     * @param keyPair   keyPair
     * @param str       str
     * @return  String
     */
    public static String encrypt(KeyPair keyPair, String str) {
        if (ObjectUtil.isEmpty(str)) {
            return "";
        }
        return encrypt(str, (RSAPublicKey) keyPair.getPublic());
    }

    /**
     * 秘钥转换
     * @param publicKey publicKey
     * @return  RSAPublicKey
     */
    public static RSAPublicKey convertPublicKey(String publicKey){
        RSAPublicKey pubKey = null;
        if (ObjectUtil.isNotEmpty(publicKey)){
            byte[] decoded = Base64Util.decode(publicKey);
            try {
                pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return pubKey;
    }

    /**
     * 秘钥转换
     * @param privateKey    privateKey
     * @return  RSAPrivateKey
     */
    public static RSAPrivateKey convertPrivateKey(String privateKey){
        RSAPrivateKey priKey = null;
        if (ObjectUtil.isNotEmpty(privateKey)){
            byte[] decoded = Base64Util.decode(privateKey);
            try {
                priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return priKey;
    }

    /**
     * RSA公钥加密
     *
     * @param str       需要加密的字符串
     * @param publicKey 公钥
     * @return String密文
     */
    public static String encrypt(String str, String publicKey){
        if (ObjectUtil.isEmpty(str)) {
            return "";
        }
        RSAPublicKey rsaPublicKey = convertPublicKey(publicKey);
        return encrypt(str,rsaPublicKey);
    }

    /**
     * RSA公钥加密
     *
     * @param str           需要加密的字符串
     * @param publicKey    公钥
     * @return String
     */
    public static String encrypt(String str,RSAPublicKey publicKey){
        //RSA加密
        if (ObjectUtil.isNotEmpty(str)){
            byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
            byte[] encrypt = encrypt(bytes, publicKey);
            if (ObjectUtil.isNotEmpty(encrypt)){
                return Base64Util.encodeBase64String(encrypt);
            }
        }
        return "";
    }

    /**
     * RSA公钥加密
     *
     * @param bytes         bytes
     * @param publicKey    公钥
     * @return byte[]
     */
    public static byte[] encrypt(byte[] bytes,RSAPublicKey publicKey){
        if (ObjectUtil.isNotEmpty(bytes)){
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(bytes);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }




    /**
     * RSA私钥解密
     *
     * @param str        需要解密的字符串
     * @param privateKey 私钥字符串
     * @return String名文
     */
    public static String decrypt(String str, String privateKey)  {
        if (ObjectUtil.isEmpty(str)) {
            return "";
        }
        RSAPrivateKey rsaPrivateKey = convertPrivateKey(privateKey);
        return decrypt(str,rsaPrivateKey);
    }


    /**
     * RSA私钥解密
     *
     * @param str        需要解密的字符串
     * @param privateKey 私钥
     * @return String名文
     */
    public static String decrypt(String str, RSAPrivateKey privateKey) {
        if (ObjectUtil.isNotEmpty(str)){
            byte[] inputByte = Base64Util.decode(str);
            byte[] decrypt = decrypt(inputByte, privateKey);
            if (ObjectUtil.isNotEmpty(decrypt)){
                return new String(decrypt);
            }
        }
        return "";
    }

    /**
     * RSA私钥解密
     *
     * @param bytes         bytes
     * @param privateKey    私钥
     * @return byte[]
     */
    public static byte[] decrypt(byte[] bytes, RSAPrivateKey privateKey) {
        if (ObjectUtil.isNotEmpty(bytes)){
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(bytes);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }


    public static String decrypt(KeyPair keyPair, String str)  {
        if (ObjectUtil.isEmpty(str)) {
            return "";
        }
        return decrypt(str, (RSAPrivateKey) keyPair.getPrivate());
    }




}
