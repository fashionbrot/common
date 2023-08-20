package com.github.fashionbrot.common;

import com.github.fashionbrot.common.system.OsUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author fashionbrot
 */
public class OsUtilTest {


    @Test
    public void test1(){
        boolean windows = OsUtil.isWindows();
        System.out.println(windows);
        Assert.assertEquals(windows,true);
    }
}
