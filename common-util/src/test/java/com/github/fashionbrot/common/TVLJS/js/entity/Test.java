package com.github.fashionbrot.common.TVLJS.js.entity;

import com.github.fashionbrot.common.tlv.TLVBufferUtil;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        JsEntity1 entity=new JsEntity1();
        entity.setAbc(123);
        byte[] serialize = TLVBufferUtil.serialize(entity);
        System.out.println(Arrays.toString(serialize));
    }

}
