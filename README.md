# common  工具类项目
## common-util   基于jdk 封装的工具
## common-spring 注入IOC 工具类


### 工具包
```gradle
implementation "com.github.fashionbrot:common-util:0.1.3"
```

### 增加 tlv 序列化
```java
package com.github.fashionbrot.common.TLVBuffer;

import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class IntegerTest {

    @Data
    public static class IntegerEntity{
        private int a1;
        private Integer b1;
    }

    @Test
    public void test1() throws IOException {
        IntegerEntity entity=new IntegerEntity();
        entity.setA1(Integer.MAX_VALUE);
        entity.setB1(Integer.MIN_VALUE);
        byte[] bytes = TLVBufferUtil.serialize( entity);

        IntegerEntity deserialized = TLVBufferUtil.deserialize(IntegerEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test2() throws IOException {
        IntegerEntity entity=new IntegerEntity();
        entity.setA1(0);
        entity.setB1(null);
        byte[] bytes = TLVBufferUtil.serialize(entity);

        IntegerEntity deserialized = TLVBufferUtil.deserialize(IntegerEntity.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(entity.getA1(),deserialized.getA1());
        Assert.assertEquals(entity.getB1(),deserialized.getB1());
    }

    @Test
    public void test3(){
        Integer abc = 12;
        byte[] bytes = TLVBufferUtil.serialize(abc);
        System.out.println(Arrays.toString(bytes));

        Integer deserialized = TLVBufferUtil.deserialize(Integer.class, bytes);
        System.out.println(deserialized);
        Assert.assertEquals(abc,deserialized);
    }

}

```
### 下面是测试速度比较
```shell
        TestEntity build2 = TestEntity.builder()
                .id(22L)
                .name(TLVTypeUtil.maxString())
                .parentId(33L)
                .parentName(TLVTypeUtil.maxString())
                .test5(55L)
                .test6(66L)
                .test7(TLVTypeUtil.maxString())
                .test8(88L)
                .test9(99L)
                .test10("aa")
                .build();
转成json：196723 字节
----------------------自己实现的压缩---------------------start
tlv ：196639 byte
23毫秒
----------------------自己实现的压缩---------------------end
----------------------protobuf---------------------start
protobuf：196633 byte
52毫秒
----------------------protobuf---------------------end
```


### spring ioc注入
```gradle
implementation "com.github.fashionbrot:common-spring:0.1.3"
```

