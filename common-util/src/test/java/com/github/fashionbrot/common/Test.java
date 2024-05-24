package com.github.fashionbrot.common;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * @author fashionbrot
 */
public class Test {

    public static void main(String[] args) {
        String binaryNumber = "11010";

        // 移位后的二进制数
        String shiftedBinaryNumber = shift(binaryNumber);

        // 输出结果
        System.out.println("原始二进制数：" + binaryNumber +" "+Integer.parseInt(binaryNumber,2));
        System.out.println("移位后的二进制数：" + shiftedBinaryNumber+" "+Integer.parseInt(shiftedBinaryNumber,2));
    }

    // 移位操作
    public static String shift(String binaryNumber) {
        // 在二进制数末尾添加一个零
        String shiftedBinaryNumber = binaryNumber + "0";
        // 将第4位移到第5位
        shiftedBinaryNumber = shiftedBinaryNumber.substring(0, 4) + shiftedBinaryNumber.charAt(2) + shiftedBinaryNumber.substring(4);
        // 将第2位移到第3位
        shiftedBinaryNumber = shiftedBinaryNumber.substring(0, 2) + shiftedBinaryNumber.charAt(0) + shiftedBinaryNumber.substring(2);

        return shiftedBinaryNumber;
    }
}