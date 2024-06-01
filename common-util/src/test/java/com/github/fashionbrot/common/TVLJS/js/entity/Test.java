package com.github.fashionbrot.common.TVLJS.js.entity;

import com.alibaba.fastjson2.JSON;
import com.github.fashionbrot.common.tlv.TLVBufferUtil;
import com.github.fashionbrot.common.tlv.TLVTypeUtil;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        JsEntity1 entity=new JsEntity1();
        entity.setShortMax(Short.MAX_VALUE);
        entity.setShortMin(Short.MIN_VALUE);
        entity.setShortNull(null);

        entity.setIntMax(Integer.MAX_VALUE);
        entity.setIntMin(Integer.MIN_VALUE);
        entity.setIntNull(null);

        entity.setLongMax(Long.MAX_VALUE);
        entity.setLongMin(Long.MIN_VALUE);
        entity.setLongNull(null);

        entity.setFloatMax(12345.1234f);
        entity.setFloatMin(-54321.6543f);
        entity.setFloatNull(null);

        entity.setDoubleMax(Double.MAX_VALUE);
        entity.setDoubleMin(Double.MIN_VALUE);
        entity.setDoubleNull(null);

        entity.setBooleanTrue(true);
        entity.setBooleanFalse(false);
        entity.setBooleanNull(null);

        System.out.println(JSON.toJSONString(entity));

        byte[] serialize = TLVBufferUtil.serialize(entity);
        System.out.println(Arrays.toString(serialize));

        JsEntity1 deserialize = TLVBufferUtil.deserialize(JsEntity1.class, serialize);
        System.out.println(deserialize);
    }

}
