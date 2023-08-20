package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.FileUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author fashionbrot
 */
public class FileUtilTest {

    @Test
    public void test1(){
        String fileName="111.jpg";
        String filePrefix = FileUtil.getFilePrefix(fileName);
        System.out.println(filePrefix);
        Assert.assertEquals("111",filePrefix);
    }

    @Test
    public void test2(){
        String fileName="222.333.111.txt";
        String filePrefix = FileUtil.getFilePrefix(fileName);
        System.out.println(filePrefix);
        Assert.assertEquals("222.333.111",filePrefix);
    }


    @Test
    public void test3(){
        String fileName="222.333.111.txt";
        String fileSuffix = FileUtil.getFileSuffix(fileName);
        System.out.println(fileSuffix);
        Assert.assertEquals("txt",fileSuffix);
    }

    @Test
    public void test4(){
        String fileName="111.jpg";
        String fileSuffix = FileUtil.getFileSuffix(fileName);
        System.out.println(fileSuffix);
        Assert.assertEquals("jpg",fileSuffix);
    }

    @Test
    public void test5(){
        String fileName="111";
        String fileSuffix = FileUtil.getFileSuffix(fileName);
        System.out.println(fileSuffix);
        Assert.assertEquals("",fileSuffix);
    }

}
