package com.github.fashionbrot.util;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.github.fashionbrot.annotation.Permission;
import com.github.fashionbrot.common.util.JavaUtil;
import com.github.fashionbrot.common.util.MethodUtil;
import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.common.util.TypeUtil;
import com.github.fashionbrot.function.*;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class PermissionUtil {


    public static Map<String, Claim>  checkToken(Algorithm algorithm,
                                                 GetTokenFunction tokenFunction,
                                                 TokenExpiredFunction tokenExpiredFunction,
                                                 SignatureVerificationFunction signatureVerificationFunction){
        if (tokenFunction==null){
            return null;
        }

        String token = tokenFunction.getToken();
        if (ObjectUtil.isEmpty(token)){
            return null;
        }
        Map<String, Claim> decode = null;
        try {
            decode = JwtUtil.decode(algorithm, token);
        }catch (TokenExpiredException tokenExpiredException){
            if (tokenExpiredFunction!=null){
                tokenExpiredFunction.throwException(tokenExpiredException);
            }
        }catch (SignatureVerificationException signatureVerificationException){
            if (signatureVerificationFunction!=null){
                signatureVerificationFunction.throwException(signatureVerificationException);
            }
        }
        return decode;
    }

    public static <T> T getToken(Algorithm algorithm,
                                 GetTokenFunction tokenFunction,
                                 TokenExpiredFunction tokenExpiredFunction,
                                 SignatureVerificationFunction signatureVerificationFunction,
                                 String key,
                                 Class<T> resultClass){
        Map<String, Claim> stringClaimMap = checkToken(algorithm, tokenFunction, tokenExpiredFunction, signatureVerificationFunction);
        if (ObjectUtil.isEmpty(stringClaimMap)){
            return null;
        }
        return JwtUtil.get(stringClaimMap, key, resultClass,null);
    }

    public static <T> T getToken(Algorithm algorithm,
                                 GetTokenFunction tokenFunction,
                                 TokenExpiredFunction tokenExpiredFunction,
                                 SignatureVerificationFunction signatureVerificationFunction,
                                 Class<T> resultClass){

        Map<String, Claim> stringClaimMap = checkToken(algorithm, tokenFunction, tokenExpiredFunction, signatureVerificationFunction);

        T t = newInstance(resultClass);
        if (ObjectUtil.isEmpty(stringClaimMap)){
            return t;
        }

        if (t!=null) {
            Field[] declaredFields = t.getClass().getDeclaredFields();
            if (ObjectUtil.isNotEmpty(declaredFields)){
                for (Field declaredField : declaredFields) {
                    if (MethodUtil.isStaticOrFinal(declaredField)){
                        continue;
                    }
                    declaredField.setAccessible(true);
                    String name = declaredField.getName();
                    if (!stringClaimMap.containsKey(name)){
                        continue;
                    }
                    Class<?> actualType = null;
                    if (JavaUtil.isCollection(declaredField.getType())){
                        Type[] actualTypeArguments = TypeUtil.getActualTypeArguments(declaredField);
                        if (ObjectUtil.isNotEmpty(actualTypeArguments)){
                            actualType = TypeUtil.convertTypeToClass(actualTypeArguments[0]);
                        }
                    }

                    Object value = JwtUtil.get(stringClaimMap, name, declaredField.getType(), actualType);
                    if (value==null){
                        continue;
                    }

                    try {
                        declaredField.set(t,value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return t;
    }


    public static  <T> T newInstance(Class<T> resultClass){
        try {
            return resultClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }






    public static boolean checkPermission(Object handler,
                                          GetSuperAdminFunction superAdminFunction,
                                          GetPermissionFunction permissionFunction,
                                          GetAnnotationFunction annotationFunction){
        if (!instanceofHandlerMethod(handler)) {
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // Check if the user is a super admin
        if (superAdminFunction != null && superAdminFunction.isSuperAdmin()) {
            return true;
        }
        if (annotationFunction==null){
            return false;
        }
        String[] requiredPermissions = annotationFunction.value(method);
        // Check method-level permissions
        if (ObjectUtil.isNotEmpty(requiredPermissions)) {
            Set<String> userPermissions = permissionFunction.getPermission();
            for (String permission : requiredPermissions) {
                if (permission!=null && userPermissions.contains(permission)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean checkPermission(Object handler, GetSuperAdminFunction superAdminFunction, GetPermissionFunction permissionFunction) {
        if (!instanceofHandlerMethod(handler)){
            return false;
        }

        GetAnnotationFunction annotationFunction = method -> {
            if (method.getAnnotation(Permission.class) == null) {
                return null;
            }
            return method.getAnnotation(Permission.class).value();
        };
        return checkPermission(handler,superAdminFunction,permissionFunction,annotationFunction);
    }


    public static boolean checkPermission(Object handler, GetPermissionFunction permissionFunction) {
        return checkPermission(handler,null,permissionFunction);
    }


    public static boolean checkPermission(Object handler, GetPermissionFunction permissionFunction,GetAnnotationFunction annotationFunction) {
        return checkPermission(handler,null,permissionFunction,annotationFunction);
    }


    public static boolean instanceofHandlerMethod(Object handler) {
        if (handler!=null && handler instanceof HandlerMethod) {
            return true;
        }
        return false;
    }

    private static boolean isRequestBody(Object handler){
        if (handler!=null && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.isAnnotationPresent(ResponseBody.class)) {
                return true;
            }
            Class<?> beanType = handlerMethod.getBeanType();
            if (beanType.isAnnotationPresent(RestController.class)) {
                return true;
            }
        }
        return false;
    }

}
