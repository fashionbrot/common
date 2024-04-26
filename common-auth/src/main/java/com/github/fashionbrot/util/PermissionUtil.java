package com.github.fashionbrot.util;

import com.github.fashionbrot.annotation.Permission;
import com.github.fashionbrot.common.util.ObjectUtil;
import com.github.fashionbrot.function.GetPermissionFunction;
import com.github.fashionbrot.function.GetSuperAdminFunction;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import java.lang.reflect.Method;
import java.util.Set;

public class PermissionUtil {


    public static boolean checkPermission(Object handler, GetSuperAdminFunction superAdminFunction, GetPermissionFunction getPermissionFunction) {
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // Check if the user is a super admin
        if (superAdminFunction != null && superAdminFunction.isSuperAdmin()) {
            return true;
        }

        // Check if method is accessible to all
        if (method.getAnnotation(Permission.class) == null) {
            return true;
        }

        // Check method-level permissions
        String[] requiredPermissions = method.getAnnotation(Permission.class).value();
        if (ObjectUtil.isNotEmpty(requiredPermissions)) {
            Set<String> userPermissions = getPermissionFunction.getPermission();
            for (String permission : requiredPermissions) {
                if (permission!=null && userPermissions.contains(permission)) {
                    return true;
                }
            }
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
