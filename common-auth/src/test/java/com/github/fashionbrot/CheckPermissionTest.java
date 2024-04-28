package com.github.fashionbrot;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.github.fashionbrot.annotation.Permission;
import com.github.fashionbrot.common.util.MapUtil;
import com.github.fashionbrot.common.util.SetUtil;
import com.github.fashionbrot.exception.CommonException;
import com.github.fashionbrot.function.GetTokenFunction;
import com.github.fashionbrot.function.SignatureVerificationFunction;
import com.github.fashionbrot.function.TokenExpiredFunction;
import com.github.fashionbrot.util.JwtUtil;
import com.github.fashionbrot.util.PermissionUtil;
import org.junit.Test;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

public class CheckPermissionTest {


    @Test
    public void test1() throws NoSuchMethodException {
        TestClass test=new TestClass();
        Method declaredMethod = test.getClass().getDeclaredMethod("test", null);
        HandlerMethod handlerMethod=new HandlerMethod(test,declaredMethod);

        boolean b = PermissionUtil.checkPermission(handlerMethod, () -> {
            return false;
        }, () -> {
            return SetUtil.newSet("test1");
        });

        CommonException.isTrue(!b).throwMsg("没有权限");

        System.out.println(b);
    }

    @Test
    public void test2() throws NoSuchMethodException {
        TestClass test = new TestClass();
        Method declaredMethod = test.getClass().getDeclaredMethod("test", null);
        HandlerMethod handlerMethod = new HandlerMethod(test, declaredMethod);

        boolean b = PermissionUtil.checkPermission(handlerMethod, () -> {
            return false;
        }, () -> {
            return SetUtil.newSet("test1");
        }, method -> {
            if (method.getAnnotation(TestPermission.class) == null) {
                return null;
            }
            return method.getAnnotation(TestPermission.class).value();
        });

        CommonException.isTrue(!b).throwMsg("没有权限");

        System.out.println(b);
    }


    @Test
    public void test3() throws Exception {

        String secret = "12345678";
        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JwtUtil.encrypt(algorithm, 10, MapUtil.createMap("userId", 1000));
        System.out.println("token:"+token);

        GetTokenFunction tokenFunction = ()-> {
            return token;
        };

        Map<String, Claim> stringClaimMap = PermissionUtil.checkToken(algorithm, tokenFunction,tokenExpiredFunction,signatureVerificationFunction);
        System.out.println(stringClaimMap);
    }


    @Test
    public void test4() throws Exception {

        String secret = "12345678";
        Algorithm algorithm = Algorithm.HMAC256(secret);

        String[] array = new String[]{"1"};
        List<Integer> list=new ArrayList<>();
        list.add(1);

        Map<String, Object> mm=new HashMap<>();
        mm.put("abc",22);


        Map<String, Object> map = MapUtil.createMap("userId", 1000, "array", array, "list", list,"str","张三","date",new Date(),"map",mm);

        String token = JwtUtil.encrypt(algorithm, 10,map);
        System.out.println("token:"+token);

        GetTokenFunction tokenFunction = ()-> {
            return token;
        };

        TokenModel tokenModel = PermissionUtil.getToken(algorithm, tokenFunction,tokenExpiredFunction,signatureVerificationFunction,TokenModel.class);
        System.out.println(tokenModel);
    }

    TokenExpiredFunction tokenExpiredFunction = (exception)->{
        CommonException.throwMsg("token 已过期");
    };

    SignatureVerificationFunction signatureVerificationFunction = (exception)->{
        CommonException.throwMsg("token 验证签名失败");
    };

}
