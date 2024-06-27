# common  工具类项目
## common-util   基于jdk 封装的工具
## common-spring 注入IOC 工具类


### 工具包
```gradle
implementation "com.github.fashionbrot:common-util:0.1.4"
```

### 增加 tlv 序列化
```java
public class TLVSerializeTest {

    @Test
    public void test9(){

        Test2ChildEntity childEntity=new Test2ChildEntity();
        childEntity.setL1(1L);

        Test2ChildEntity childEntity2=new Test2ChildEntity();
        childEntity2.setL1(2L);

        Test2Entity entity=new Test2Entity();
        entity.setArray(new Test2ChildEntity[]{childEntity2,childEntity});
        entity.setList(Arrays.asList(childEntity,childEntity2));


        PageResponse pageResponse=new PageResponse();
        pageResponse.setRows(Arrays.asList(entity));
        pageResponse.setTotal(1);


        Response response=new Response();
        response.setData(pageResponse);
        response.setCode(0);
        response.setMsg("成功");


        byte[] bytes = TLVUtil.serialize(response);
        System.out.println(Arrays.toString(bytes));
        System.out.println(bytes.length);

        Response deserialize = TLVUtil.deserialize(Response.class, bytes);
        String jsonString = JSON.toJSONString(deserialize);
        System.out.println(jsonString);
        System.out.println(jsonString.getBytes().length);
        Assert.assertTrue(Objects.equals(response,deserialize));
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

