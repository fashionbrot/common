package com.github.fashionbrot.common.util;

/**
 * @author fashionbrot
 */
public class SequenceUtil {

    private static Sequence sequence=new Sequence();


    public static Long nextId(){
        return sequence.nextId();
    }

}
