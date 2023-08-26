package com.github.fashionbrot.common;

import com.github.fashionbrot.common.util.TypeUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * @author fashionbrot
 */
public class TypeUtilTest {

    class Example<T, U> {
    }

    @Test
    public void testGetTypeVariableIndex() {
        TypeVariable<?>[] typeVariables = TypeUtilTest.Example.class.getTypeParameters();
        String fieldTypeName = "U";

        Integer index = TypeUtil.getTypeVariableIndex(typeVariables, fieldTypeName);
        Assert.assertNotNull(index);
        Assert.assertEquals(1, index.intValue());
    }



    @Test
    public void testGetActualTypeArguments() {
        // 创建一个示例类
        class Example<T, U> {
            public void method(T t, U u) {
            }
        }

        try {
            // 获取示例方法
            Method method = Example.class.getDeclaredMethod("method", Object.class, Object.class);
            // 获取方法参数
            Parameter[] parameters = method.getParameters();
            // 获取参数的泛型类型参数
            Type[] actualTypeArguments = TypeUtil.getActualTypeArguments(parameters[0]);

            // 验证预期结果
            Assert.assertNotNull(actualTypeArguments);
            Assert.assertEquals(1, actualTypeArguments.length);
            Assert.assertTrue(actualTypeArguments[0] instanceof Class);
            Assert.assertEquals(Object.class, actualTypeArguments[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
