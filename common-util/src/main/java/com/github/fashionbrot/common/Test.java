package com.github.fashionbrot.common;

import jdk.nashorn.internal.ir.debug.JSONWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fashionbrot
 */
public class Test
{

    public static void main(String[] args) {
            List<String> fruits = new ArrayList<>();
            fruits.add("Banana");
            List<String> copyFruits = fruits.stream().collect(Collectors.toList());
            copyFruits.add("dfad");

        System.out.println(1);
    }
}
