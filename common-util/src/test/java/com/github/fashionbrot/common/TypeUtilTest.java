package com.github.fashionbrot.common;

import com.github.fashionbrot.common.entity.RequestTest;
import com.github.fashionbrot.common.util.TypeUtil;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @author fashionbrot
 */
public class TypeUtilTest {

    // 将示例类 Example 移到测试类的外部
    class Example<T, U> {
        public void method(T t, U u) {
        }
    }

    @Test
    public void testGetTypeVariableIndex() {
        TypeVariable<?>[] typeVariables = Example.class.getTypeParameters();
        String fieldTypeName = "U";

        Integer index = TypeUtil.getTypeVariableIndex(typeVariables, fieldTypeName);
        Assert.assertNotNull(index);
        Assert.assertEquals(1, index.intValue());
    }

    public class TestController{

        private void test1(RequestTest<Integer> test){

        }
    }


    @Test
    public void testGetActualTypeArguments() {

        Class<TestController> clzzz = TestController.class;
        Method method = Arrays.stream(clzzz.getDeclaredMethods()).filter(m -> m.getName().equals("test1")).collect(Collectors.toList()).get(0);

        // 获取方法参数
        Parameter[] parameters = method.getParameters();
        // 获取参数的泛型类型参数
        Type[] actualTypeArguments = TypeUtil.getActualTypeArguments(parameters[0]);
        TypeVariable[] typeVariables = TypeUtil.getTypeVariables(parameters[0]);

        // 验证预期结果
        Assert.assertNotNull(actualTypeArguments);
        Assert.assertNotNull(typeVariables);
        Assert.assertEquals(1, actualTypeArguments.length);
        Assert.assertTrue(actualTypeArguments[0] instanceof Class);
        Assert.assertEquals(Integer.class, actualTypeArguments[0]);

    }


    public static void main(String[] args) {
        Integer status=1;
        Integer status2=1;
        System.out.println(status.equals(status2));
    }
}
