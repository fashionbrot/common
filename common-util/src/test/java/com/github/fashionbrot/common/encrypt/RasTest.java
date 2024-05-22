package com.github.fashionbrot.common.encrypt;

import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

public class RasTest {


    @Test
    public void test(){
        KeyPair keyPair = RsaUtil.genKeyPair(512);
        String publicKey = RsaUtil.publicKeyToString((RSAPublicKey) keyPair.getPublic());
        String privateKey = RsaUtil.privateKeyToString((RSAPrivateKey) keyPair.getPrivate());

        long l = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            String str = "{\"id\":1111111,\"name\":\"我是你大爷\"}";
            System.out.println("str:"+str);

            String encrypt = RsaUtil.encrypt(str, publicKey);
            System.out.println("encrypt:"+encrypt);

            String decrypt = RsaUtil.decrypt(encrypt, privateKey);
            System.out.println("decrypt:"+decrypt);
        }
        System.out.println((System.currentTimeMillis()-l));
    }


    @Test
    public void test2() {
        Map<String, String> stringStringMap = RsaUtil.genKeyPairMap(512, "12345678".getBytes());

        System.out.println(stringStringMap);
        String publicKey = stringStringMap.get("publicKey");
        String privateKey = stringStringMap.get("privateKey");

        String content = "{张三啊a}11%%%8）";

        String encrypt = RsaUtil.encrypt(content, publicKey);
        System.out.println("contentEncrypt:" + encrypt);

        String decrypt = RsaUtil.decrypt(encrypt, privateKey);
        System.out.println("contentDecrypt:" + decrypt);

        Assert.assertEquals(content,decrypt);

    }

}
