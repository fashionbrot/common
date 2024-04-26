package com.github.fashionbrot;

import com.github.fashionbrot.common.util.SetUtil;
import com.github.fashionbrot.exception.CommonException;
import com.github.fashionbrot.util.PermissionUtil;
import org.junit.Test;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

public class CheckPermissionTest {


    @Test
    public void test1() throws NoSuchMethodException {
        TestClass test=new TestClass();
        Method declaredMethod = test.getClass().getDeclaredMethod("test", null);
        HandlerMethod handlerMethod=new HandlerMethod(test,declaredMethod);

        boolean b = PermissionUtil.checkPermission(handlerMethod, () -> {
            return false;
        }, () -> {
            return SetUtil.newSet("test111");
        });

        CommonException.isTrue(!b).throwMsg("没有权限");

        System.out.println(b);
    }

}
