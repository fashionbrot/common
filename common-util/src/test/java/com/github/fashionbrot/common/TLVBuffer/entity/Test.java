package com.github.fashionbrot.common.TLVBuffer.entity;

import com.github.fashionbrot.common.tlv.ByteArrayReader;
import com.github.fashionbrot.common.tlv.TLVUtil;
import com.github.fashionbrot.common.tlv.TypeToken;
import com.github.fashionbrot.common.util.TypeUtil;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @author fashionbrot
 */
public class Test {

    public static void main(String[] args) {

        Test1Child t=new Test1Child();
        t.setB1("a");

        PageResponse<Test1Child> pageResponse=new PageResponse<>();
        pageResponse.setRows(Arrays.asList(t));
        pageResponse.setTotal(1);

        Response<PageResponse<Test1Child>> response=new Response<>();
        response.setData(pageResponse);
        response.setCode(0);
        response.setMsg("成功");

        byte[] serialize = TLVUtil.serialize(response);
        System.out.println(Arrays.toString(serialize));

//        Type type = TLVUtil.<Response<PageResponse<Test1Child>>>getType();

        TypeToken<Response<PageResponse<Test1Child>>> typeToken = new TypeToken<Response<PageResponse<Test1Child>>>(){};
        Type type = typeToken.getType();

//        TypeToken<Response<PageResponse<Test1Child>>> typeToken=new TypeToken<Response<PageResponse<Test1Child>>>(){};

        Response deserialize = (Response) TLVUtil.deserialize(TypeUtil.convertTypeToClass(type),serialize);
        System.out.println(deserialize);

    }


}
