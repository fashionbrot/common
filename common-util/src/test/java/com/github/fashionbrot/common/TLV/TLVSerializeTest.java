package com.github.fashionbrot.common.TLV;

import com.github.fashionbrot.common.TLV.entity.Test1Entity;
import com.github.fashionbrot.common.tlv.TLVDeserializeUtil;
import com.github.fashionbrot.common.tlv.TLVSerializeUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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

        byte[] serialize = TLVSerializeUtil.serialize(entity);
        System.out.println(serialize.length);
        System.out.println(Arrays.toString(serialize));

        byte[] expected = new byte[]{32, 1, 12};

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

        List<Test1Entity> entityList = Arrays.asList(entity, entity1);

        byte[] serialize = TLVSerializeUtil.serialize(entityList);
        System.out.println(serialize.length);
        System.out.println(Arrays.toString(serialize));
//
//        byte[] expected = new byte[]{32, 1, 12};
//
//        Assert.assertTrue("Entity 解析失败",Arrays.equals(expected,serialize));
    }

    @Test
    public void arrayTest(){
        Test1Entity entity=new Test1Entity();
        entity.setA1(12);

        Test1Entity entity1= new Test1Entity();
        entity1.setA1(13);

        Test1Entity[] test1Entities = {entity, entity1};

        byte[] serialize = TLVSerializeUtil.serialize(test1Entities);
        System.out.println(serialize.length);
        System.out.println(Arrays.toString(serialize));
    }



}
