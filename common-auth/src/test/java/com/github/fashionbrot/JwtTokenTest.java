package com.github.fashionbrot;

import com.github.fashionbrot.common.compress.GzipUtil;
import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenTest {


    private static final String SECRET_KEY = "your_secret_key";

    public static void main(String[] args) {


        rsaTest();
    }

    private static void HS256(){
        Map<String,Object> map=new HashMap<>();
        map.put("u",1);
        map.put("n","张三");

        // 1. 生成JWT Token
        String token = JwtUtil.generateToken(map,2000, SignatureAlgorithm.HS256,SECRET_KEY);
        System.out.println("Generated JWT Token: " + token);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // 2. 解析JWT Token（不验证）
        Claims decodedToken = JwtUtil.decodeToken(SECRET_KEY,token);
        if (decodedToken != null) {
            Integer u = decodedToken.get("u", Integer.class);
            System.out.println(u);
            System.out.println(decodedToken.get("n", String.class));
            System.out.println("Decoded JWT Token without verification: " + decodedToken);
        } else {
            System.out.println("Failed to decode JWT Token.");
        }

    }


    public static KeyPair generateRSAKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512); // 设置密钥长度
        return keyPairGenerator.generateKeyPair();
    }

    private static void rsaTest(){
//        KeyPair keyPair =RsaUtil.genKeyPair(512,"12345678".getBytes());
//        // 得到私钥
//        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//        // 得到公钥
//        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();


        Map<String,Object> map=new HashMap<>();
        map.put("u",1);
        map.put("n","张三");

        // 生成RSA密钥对
        KeyPair keyPair = null;
        try {
            keyPair = generateRSAKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 获取私钥和公钥
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        // 生成JWT
        String token = JwtUtil.generateToken(map,3600,SignatureAlgorithm.RS256,privateKey);
        System.out.println(token);
        byte[] compress = GzipUtil.compress(token);
        System.out.println("token length:"+token.getBytes().length);
        System.out.println("token 压缩后 length:"+compress.length);
        System.out.println("token 压缩后："+ ObjectUtil.byteToString(compress));
        // 验证JWT
        Claims claims = JwtUtil.decodeToken(token,publicKey);
        System.out.println("JWT验证成功，Payload: " + claims);

    }

}
