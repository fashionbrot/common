package com.github.fashionbrot.common;

import com.github.fashionbrot.common.encrypt.RsaUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Map;

/**
 * @author fashionbrot
 */
public class RsaUtilTest {


    @Test
    public void test1() {
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
