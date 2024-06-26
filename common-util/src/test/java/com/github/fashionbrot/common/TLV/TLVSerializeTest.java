package com.github.fashionbrot.common.TLV;

import com.alibaba.fastjson2.JSON;
import com.github.fashionbrot.common.TLV.entity.Test1Entity;
import com.github.fashionbrot.common.TLV.entity.Test2ChildEntity;
import com.github.fashionbrot.common.TLV.entity.Test2Entity;
import com.github.fashionbrot.common.TLVBuffer.ArrayBeanTest;
import com.github.fashionbrot.common.TLVBuffer.ListObjectTest;
import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import com.github.fashionbrot.common.tlv.TLVDeserializeUtil;
import com.github.fashionbrot.common.tlv.TLVSerializeUtil;
import com.github.fashionbrot.common.util.BigDecimalUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author fashionbrot
 */
public class TLVSerializeTest {


    @Test
    public void test1(){

        Integer abc= 11;

        byte[] serialize = TLVSerializeUtil.serialize(abc);
        System.out.println(serialize.length);
        System.out.println(Arrays.toString(serialize));
        byte[] expected = new byte[]{32, 1, 11};
        Assert.assertTrue("基本类型序列化失败",Arrays.equals(expected,serialize));

        Integer deserialize = TLVDeserializeUtil.deserialize(abc.getClass(), serialize);
        System.out.println(deserialize);
        Assert.assertEquals("基本类型反序列化失败",abc,deserialize);
    }

    @Test
    public void entityTest(){

        Test1Entity entity=new Test1Entity();
        entity.setA1(12);
        entity.setS1("你好");

        byte[] serialize = TLVSerializeUtil.serialize(entity);
        System.out.println(serialize.length);
        System.out.println(Arrays.toString(serialize));

        byte[] expected = new byte[]{32, 1, 12, 64, 6, -28, -67, -96, -27, -91, -67};

        Assert.assertTrue("Entity序列化失败",Arrays.equals(expected,serialize));

        Test1Entity deserialize = TLVDeserializeUtil.deserialize(Test1Entity.class, serialize);
        System.out.println(deserialize.toString());
        Assert.assertEquals("基本类型反序列化失败",entity.toString(),deserialize.toString());
    }


    @Test
    public void test3(){

        Test1Entity entity=new Test1Entity();
        entity.setA1(12);

        Test1Entity entity1= new Test1Entity();
        entity1.setA1(13);

        List<Test1Entity> entityList = new ArrayList<>();
        entityList.add(entity);
        entityList.add(entity1);

        byte[] serialize = TLVSerializeUtil.serialize(entityList);
        System.out.println(serialize.length);
        System.out.println(Arrays.toString(serialize));

        byte[] expected = new byte[]{32, 1, 12, 64, 0, 32, 1, 13, 64, 0};
        Assert.assertTrue("ListEntity序列化失败",Arrays.equals(expected,serialize));

        List<Test1Entity> deserialize = TLVDeserializeUtil.deserializeList(Test1Entity.class, serialize);
        System.out.println(JSON.toJSONString(deserialize));
        System.out.println(JSON.toJSONString(deserialize).toString().getBytes().length);

        Assert.assertTrue("ListEntity反序列化失败", Objects.equals(entityList,deserialize));
    }

    @Test
    public void arrayTest(){
        Test1Entity entity=new Test1Entity();
        entity.setA1(12);

        Test1Entity entity1= new Test1Entity();
        entity1.setA1(13);

        Test1Entity[] entityList ={entity,entity1};

        byte[] serialize = TLVSerializeUtil.serialize(entityList);
        System.out.println(serialize.length);
        System.out.println(Arrays.toString(serialize));

        byte[] expected = new byte[]{32, 1, 12, 64, 0, 32, 1, 13, 64, 0};
        Assert.assertTrue("ArrayEntity序列化失败",Arrays.equals(expected,serialize));


        Test1Entity[] deserialize = TLVDeserializeUtil.deserializeArray(Test1Entity.class, serialize);
        System.out.println(JSON.toJSONString(deserialize));
        System.out.println(JSON.toJSONString(deserialize).toString().getBytes().length);

        Assert.assertTrue("ArrayEntity反序列化失败", Arrays.equals(entityList,deserialize));
    }


    @Test
    public void test5(){

        Test2Entity entity=new Test2Entity();
//        entity.setB1(BigDecimalUtil.format("1.111"));
        Test2ChildEntity child=new Test2ChildEntity();

        entity.setArray(new Test2ChildEntity[]{child});
        entity.setList(Arrays.asList(child));

        byte[] serialize = TLVSerializeUtil.serialize(entity);
        System.out.println(serialize.length);
        System.out.println(Arrays.toString(serialize));
        byte[] expected = new byte[]{48, 0};


        Test2Entity deserialize = TLVDeserializeUtil.deserialize(Test2Entity.class, serialize);
        System.out.println(JSON.toJSONString(deserialize));
        System.out.println(JSON.toJSONString(deserialize).toString().getBytes().length);

        Assert.assertTrue("ArrayEntity反序列化失败", Objects.equals(entity,deserialize));

    }


    @Data
    public static class ListChildEntity{
        private String abc;
    }

    @Data
    public static class ListBeanEntity{
        private ListChildEntity[] a1;
        private ListChildEntity[] b1;
        private ListChildEntity[] c1;
    }

    @Test
    public void test6()  {

        ListChildEntity childEntity=new ListChildEntity();
        childEntity.setAbc("1");

        ListBeanEntity beanEntity=new ListBeanEntity();
        beanEntity.setA1(new ListChildEntity[]{childEntity});
        beanEntity.setB1(new ListChildEntity[]{});
        beanEntity.setC1(null);


        byte[] bytes = TLVSerializeUtil.serialize(beanEntity);
        System.out.println(Arrays.toString(bytes));
        System.out.println(bytes.length);

        ListBeanEntity deserialized = TLVDeserializeUtil.deserialize(ListBeanEntity.class, bytes);
        System.out.println(deserialized);

        Assert.assertEquals(beanEntity.getA1()[0].getAbc(),childEntity.getAbc());
    }




    @Data
    public static class ListObjectEntity{
        private List<Object> a1;
        private Object[] b1;
        private List c1;
    }
    @Test
    public void test7()  {

        List<Object> a1 = Arrays.asList(1, "你好");

        Object[] b1 = {1,"2"};

        ListObjectEntity beanEntity=new ListObjectEntity();
        beanEntity.setA1(a1);
        beanEntity.setB1(b1);
        beanEntity.setC1(null);


        byte[] bytes = TLVSerializeUtil.serialize( beanEntity);
        System.out.println(bytes.length);
        System.out.println(Arrays.toString(bytes));

        ListObjectEntity deserialized = TLVDeserializeUtil.deserialize(ListObjectEntity.class, bytes);
        System.out.println(deserialized);

        Assert.assertTrue("test7失败",Objects.equals(beanEntity,deserialized));
    }

}
