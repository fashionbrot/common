package com.github.fashionbrot.common.encrypt;


import org.junit.Assert;
import org.junit.Test;


/**
 * @author fashionbrot
 */
public class AesTest {



    @Test
    public void test1(){

        String secret ="1234567890abcdef";
        String result = AesUtil.encryptString("1111", secret);
        System.out.println(result);
        Assert.assertEquals("A53B6895A2D6ECBDA944FEC5A0F8D364",result);

        String decrypt = AesUtil.decryptString(result, secret);
        System.out.println(decrypt);
        Assert.assertEquals("1111",decrypt);
    }
}
