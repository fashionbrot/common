package com.github.fashionbrot.common;

import com.github.fashionbrot.common.entity.LvEntity;
import com.github.fashionbrot.common.entity.TestEntity;
import com.github.fashionbrot.common.util.LLvBufferUtil;
import com.github.fashionbrot.common.util.LvBufferTypeUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * @author fashionbrot
 */
public class Test {

    public static void main(String[] args) throws IOException {

        test2();


    }



    public static void test2(){
        long l = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            List<Field> sortedClassField = LLvBufferUtil.getSortedClassField(LvEntity.class);
            System.out.print(sortedClassField.size());
        }
        System.out.println();
        System.out.println(System.currentTimeMillis()-l);
    }

}