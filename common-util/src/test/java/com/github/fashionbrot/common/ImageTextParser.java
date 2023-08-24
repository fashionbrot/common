package com.github.fashionbrot.common;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * @author fashionbrot
 */
public class ImageTextParser {

    /**
     * 解析图片上的文字。
     *
     * @param imagePath 图片文件的路径
     * @return 图片中的文字内容，或解析失败的错误信息
     */
    public static String parseImageText(String imagePath) {
        File imageFile = new File(imagePath);
        ITesseract tesseract = new Tesseract();

        // 设置Tesseract库的数据路径，需要下载对应的语言数据文件
        // 比如：tesseract.setDatapath("/path/to/tessdata");
        tesseract.setDatapath("E:\\tessdata-main");


        // 设置输出编码为UTF-8，以正确处理中文字符
        tesseract.setTessVariable("user_defined_output_char_whitelist", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");

        tesseract.setLanguage("chi_sim");//chi_tra  chi_sim

        try {
            String result = tesseract.doOCR(imageFile);
            return result;
        } catch (TesseractException e) {
            e.printStackTrace();
            return "解析失败：" + e.getMessage();
        }
    }

    public static void main(String[] args) {
        String imagePath = "C:\\Users\\fashionbrot\\Desktop\\222.jpg";
        String parsedText = parseImageText(imagePath);
        System.out.println("解析结果：" + parsedText);
    }
}
